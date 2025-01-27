package com.anthonyguidotti.job_application_tracker.job_application;

import com.anthonyguidotti.job_application_tracker.validation.JsonSchemaValidate;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.time.ZonedDateTime;

@JsonSchemaValidate("schema/jobApplicationCreatePayload.json")
@JsonIgnoreProperties(ignoreUnknown = true)
public class JobApplicationCreatePayload {
    private String jobTitle;
    private String jobDescription;
    private ResumeCreatePayload resume;
    private JobApplicationStatus status;
    private String jobPostingUrl;
    private String companyId;
    private ZonedDateTime sentDate;
    private String notes;

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

    public ResumeCreatePayload getResume() {
        return resume;
    }

    public void setResume(ResumeCreatePayload resume) {
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

    public String getCompanyId() {
        return companyId;
    }

    public void setCompanyId(String companyId) {
        this.companyId = companyId;
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

    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class ResumeCreatePayload {
        private String docxFile;

        public String getDocxFile() {
            return docxFile;
        }

        public void setDocxFile(String docxFile) {
            this.docxFile = docxFile;
        }
    }
}
