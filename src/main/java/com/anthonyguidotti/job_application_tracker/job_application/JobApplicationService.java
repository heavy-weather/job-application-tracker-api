package com.anthonyguidotti.job_application_tracker.job_application;

import com.anthonyguidotti.job_application_tracker.company.Company;
import com.anthonyguidotti.job_application_tracker.company.CompanyModel;
import com.anthonyguidotti.job_application_tracker.company.CompanyRepository;
import com.anthonyguidotti.job_application_tracker.job_application.JobApplication.Resume;
import com.anthonyguidotti.job_application_tracker.util.HashUtil;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.PdfWriter;
import org.apache.pdfbox.Loader;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.text.PDFTextStripper;
import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.apache.poi.xwpf.usermodel.XWPFParagraph;
import org.hibernate.engine.jdbc.BlobProxy;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.sql.Blob;
import java.sql.SQLException;
import java.util.Base64;
import java.util.List;
import java.util.UUID;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;

@Service
public class JobApplicationService {
    private final JobApplicationRepository jobApplicationRepository;
    private final CompanyRepository companyRepository;

    public JobApplicationService(
            JobApplicationRepository jobApplicationRepository,
            CompanyRepository companyRepository
    ) {
        this.jobApplicationRepository = jobApplicationRepository;
        this.companyRepository = companyRepository;
    }

    public List<JobApplication> getAll() {
        return jobApplicationRepository.findAllByOrderByCreatedTimestampDesc()
                .stream()
                .map(this::mapToExternalRepresentation)
                .toList();
    }

    public List<JobApplication> getAllAsPartial() {
        return jobApplicationRepository.findAllByOrderByCreatedTimestampDesc()
                .stream()
                .map((jobApplicationModel) -> {
                    JobApplication jobApplication = new JobApplication();
                    jobApplication.setId(jobApplicationModel.getId().toString());
                    jobApplication.setJobTitle(jobApplicationModel.getJobTitle());
                    jobApplication.setStatus(jobApplicationModel.getStatus());
                    jobApplication.setSentDate(jobApplicationModel.getSentDate());

                    Company company = new Company();
                    company.setId(jobApplicationModel.getCompany().getId().toString());
                    company.setName(jobApplicationModel.getCompany().getName());
                    jobApplication.setCompany(company);

                    return jobApplication;
                }).toList();
    }

    public JobApplication create(
            JobApplicationCreatePayload jobApplication
    ) throws JobApplicationInvalidRepresentationException {
        CompanyModel companyModel = companyRepository.findById(UUID.fromString(jobApplication.getCompanyId()))
                .orElseThrow(() ->
                        new JobApplicationInvalidRepresentationException("Job application must refer to a valid company")
                );
        ResumeModel resumeModel = populateResumeDomainModel(
                jobApplication.getResume().getDocxFile(),
                new ResumeModel()
        );
        JobApplicationModel jobApplicationModel = populateNewJobApplicationDomainModel(
                jobApplication,
                new JobApplicationModel(),
                companyModel,
                resumeModel
        );

        return mapToExternalRepresentation(jobApplicationRepository.save(jobApplicationModel));
    }

    private ResumeModel populateResumeDomainModel(
            String b64EncodedGZippedDocxFile,
            ResumeModel resumeModel
    ) throws JobApplicationInvalidRepresentationException {
        try {
            // Use the decompressed pdf file to generate the hash and the plain text representations
            byte[] decompressedPdfBytes = convertDocxToPdf(
                     gunzipBytes(
                             Base64.getDecoder().decode(b64EncodedGZippedDocxFile)
                     )
            );
            resumeModel.setPdfHash(HashUtil.md5Hash(decompressedPdfBytes));
            resumeModel.setText(convertPdfToPlainText(decompressedPdfBytes));

            // Compress the pdf file before storing. The compressed pdf is base64 encoded and included in the
            // representations of job applications returned by the API
            resumeModel.setPdfFile(
                    BlobProxy.generateProxy(
                            gzipBytes(decompressedPdfBytes)
                    )
            );

        } catch (DocumentException | IOException e) {
            throw new JobApplicationInvalidRepresentationException("Malformed .docx representation");
        }

        return resumeModel;
    }

    private byte[] gunzipBytes(byte[] content) throws IOException {
        return (new GZIPInputStream(new ByteArrayInputStream(content))).readAllBytes();
    }

    private byte[] convertDocxToPdf(byte[] docxFile) throws IOException, DocumentException {
        try (XWPFDocument docxDocument = new XWPFDocument(new ByteArrayInputStream(docxFile))) {
            try (ByteArrayOutputStream pdfOutputStream = new ByteArrayOutputStream()) {
                Document pdfDocument = new Document();
                PdfWriter.getInstance(pdfDocument, pdfOutputStream);

                // Write all paragraphs from docx file to pdf file
                // TODO: Retain the docx formatting when converting to pdf
                pdfDocument.open();
                for (XWPFParagraph docxParagraph : docxDocument.getParagraphs()) {
                    pdfDocument.add(new Paragraph(docxParagraph.getText()));
                }
                pdfDocument.close();

                pdfOutputStream.close();
                return pdfOutputStream.toByteArray();
            }
        }
    }

    private String convertPdfToPlainText(byte[] pdfFileBytes) throws IOException {
        PDFTextStripper pdfTextStripper = new PDFTextStripper();
        PDDocument pdDocument = Loader.loadPDF(pdfFileBytes);
        return pdfTextStripper.getText(pdDocument);
    }

    private byte[] gzipBytes(byte[] content) throws IOException {
        try (ByteArrayOutputStream bos = new ByteArrayOutputStream()) {
            try (GZIPOutputStream gos = new GZIPOutputStream(bos)) {
                gos.write(content);
            }
            bos.close();
            return bos.toByteArray();
        }
    }

    private JobApplicationModel populateNewJobApplicationDomainModel(
            JobApplicationCreatePayload jobApplicationPayload,
            JobApplicationModel jobApplicationModel,
            CompanyModel companyModel,
            ResumeModel resumeModel
    ) {
        jobApplicationModel.setJobTitle(jobApplicationPayload.getJobTitle());
        jobApplicationModel.setJobDescription(jobApplicationPayload.getJobDescription());
        jobApplicationModel.setStatus(jobApplicationPayload.getStatus());
        jobApplicationModel.setJobPostingUrl(jobApplicationPayload.getJobPostingUrl());
        jobApplicationModel.setSentDate(jobApplicationPayload.getSentDate());
        jobApplicationModel.setNotes(jobApplicationPayload.getNotes());
        jobApplicationModel.setCompany(companyModel);
        jobApplicationModel.setResume(resumeModel);

        resumeModel.setJobApplication(jobApplicationModel);

        return jobApplicationModel;
    }

    public JobApplication get(String id) {
        return jobApplicationRepository.findWithReferencesById(UUID.fromString(id))
                .map(this::mapToExternalRepresentation)
                .orElse(null);
    }

    public void replace(
            JobApplicationUpdatePayload jobApplicationReplacement
    ) throws JobApplicationInvalidRepresentationException, JobApplicationNotFoundException {
        JobApplicationModel jobApplicationModel = jobApplicationRepository.findWithReferencesById(
                UUID.fromString(jobApplicationReplacement.getId())
        ).orElseThrow(() -> new JobApplicationNotFoundException("Cannot replace job application which does not exist"));

        ResumeModel resumeModel = jobApplicationModel.getResume();
        if (
                !StringUtils.hasLength(jobApplicationReplacement.getResume().getPdfHash()) ||
                !jobApplicationReplacement.getResume().getPdfHash().equals(jobApplicationModel.getResume().getPdfHash())
        ) {
            if (!StringUtils.hasLength(jobApplicationReplacement.getResume().getDocxFile())) {
                throw new JobApplicationInvalidRepresentationException("Must include a .docx job application");
            }
            resumeModel = populateResumeDomainModel(
                    jobApplicationReplacement.getResume().getDocxFile(),
                    new ResumeModel()
            );
        }

        JobApplicationModel updatedJobApplicationModel = populateUpdatedJobApplicationDomainModel(
                jobApplicationReplacement,
                jobApplicationModel,
                resumeModel
        );
        jobApplicationRepository.save(updatedJobApplicationModel);
    }

    private JobApplicationModel populateUpdatedJobApplicationDomainModel(
            JobApplicationUpdatePayload jobApplicationPayload,
            JobApplicationModel jobApplicationModel,
            ResumeModel resumeModel
    ) {
        jobApplicationModel.setJobTitle(jobApplicationPayload.getJobTitle());
        jobApplicationModel.setJobDescription(jobApplicationPayload.getJobDescription());
        jobApplicationModel.setStatus(jobApplicationPayload.getStatus());
        jobApplicationModel.setJobPostingUrl(jobApplicationPayload.getJobPostingUrl());
        jobApplicationModel.setSentDate(jobApplicationPayload.getSentDate());
        jobApplicationModel.setNotes(jobApplicationPayload.getNotes());
        jobApplicationModel.setResume(resumeModel);

        resumeModel.setJobApplication(jobApplicationModel);

        return jobApplicationModel;
    }

    private JobApplication mapToExternalRepresentation(JobApplicationModel jobApplicationModel) {
        Resume resume = new Resume();
        resume.setPdfFile(convertBlobToBase64EncodedString(
                jobApplicationModel.getResume().getPdfFile()
        ));
        resume.setPdfHash(jobApplicationModel.getResume().getPdfHash());

        JobApplication jobApplication = new JobApplication();
        jobApplication.setId(jobApplicationModel.getId().toString());
        jobApplication.setJobTitle(jobApplicationModel.getJobTitle());
        jobApplication.setJobDescription(jobApplicationModel.getJobDescription());
        jobApplication.setResume(resume);
        jobApplication.setStatus(jobApplicationModel.getStatus());
        jobApplication.setJobPostingUrl(jobApplicationModel.getJobPostingUrl());
        if (jobApplicationModel.getCompany() != null) {
            Company company = new Company();
            company.setId(jobApplicationModel.getCompany().getId().toString());
            company.setName(jobApplicationModel.getCompany().getName());
            company.setWebSiteUrl(jobApplicationModel.getCompany().getWebSiteUrl());
            jobApplication.setCompany(company);
        }
        jobApplication.setSentDate(jobApplicationModel.getSentDate());
        jobApplication.setNotes(jobApplicationModel.getNotes());
        return jobApplication;
    }

    private String convertBlobToBase64EncodedString(Blob pdfFile) {
        try {
            return Base64.getEncoder().encodeToString(
                    pdfFile.getBinaryStream().readAllBytes()
            );
        } catch (SQLException | IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void delete(String id) {
        jobApplicationRepository.deleteById(UUID.fromString(id));
    }
}
