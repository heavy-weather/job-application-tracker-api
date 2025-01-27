package com.anthonyguidotti.job_application_tracker.job_application;

import com.anthonyguidotti.job_application_tracker.company.Company;
import com.fasterxml.jackson.annotation.JsonInclude;

import java.time.ZonedDateTime;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class JobApplication {
    private String id;
    private String jobTitle;
    private String jobDescription;
    private Resume resume;
    private JobApplicationStatus status;
    private String jobPostingUrl;
    private Company company;
    private ZonedDateTime sentDate;
    private String notes;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getJobTitle() {
        return jobTitle;
    }

    public void setJobTitle(String jobTitle) {
        this.jobTitle = jobTitle;
    }

    public String getJobDescription() {
        return jobDescription;
    }

    public void setJobDescription(String jobDescription) {
        this.jobDescription = jobDescription;
    }

    public Resume getResume() {
        return resume;
    }

    public void setResume(Resume resume) {
        this.resume = resume;
    }

    public JobApplicationStatus getStatus() {
        return status;
    }

    public void setStatus(JobApplicationStatus status) {
        this.status = status;
    }

    public String getJobPostingUrl() {
        return jobPostingUrl;
    }

    public void setJobPostingUrl(String jobPostingUrl) {
        this.jobPostingUrl = jobPostingUrl;
    }

    public Company getCompany() {
        return company;
    }

    public void setCompany(Company company) {
        this.company = company;
    }

    public ZonedDateTime getSentDate() {
        return sentDate;
    }

    public void setSentDate(ZonedDateTime sentDate) {
        this.sentDate = sentDate;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    public static class Resume {
        private String pdfFile;
        private String pdfHash;

        public String getPdfFile() {
            return pdfFile;
        }

        public void setPdfFile(String pdfFile) {
            this.pdfFile = pdfFile;
        }

        public String getPdfHash() {
            return pdfHash;
        }

        public void setPdfHash(String pdfHash) {
            this.pdfHash = pdfHash;
        }
    }
}
