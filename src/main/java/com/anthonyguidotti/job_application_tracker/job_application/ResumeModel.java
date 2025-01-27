package com.anthonyguidotti.job_application_tracker.job_application;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.Lob;
import jakarta.persistence.MapsId;
import jakarta.persistence.OneToOne;
import org.hibernate.Length;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.sql.Blob;
import java.time.ZonedDateTime;
import java.util.UUID;

@Entity(name = "resume")
public class ResumeModel {

    @Id
    private UUID id;

    @Column(
            name = "resume_text",
            length = Length.LONG,
            nullable = false
    )
    private String text;

    @Lob
    @Column(
            name = "resume_pdf_file"
    )
    private Blob pdfFile;

    @Column(
            name = "resume_pdf_hash",
            nullable = false
    )
    private String pdfHash;

    @OneToOne
    @JoinColumn(name = "job_application_id")
    @MapsId
    private JobApplicationModel jobApplication;

    @CreationTimestamp
    @Column(
            name = "resume_created_timestamp",
            nullable = false,
            updatable = false
    )
    private ZonedDateTime createdTimestamp;

    @UpdateTimestamp
    @Column(
            name = "resume_updated_timestamp",
            nullable = false
    )
    private ZonedDateTime updatedTimestamp;

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public Blob getPdfFile() {
        return pdfFile;
    }

    public void setPdfFile(Blob pdfFile) {
        this.pdfFile = pdfFile;
    }

    public String getPdfHash() {
        return pdfHash;
    }

    public void setPdfHash(String pdfHash) {
        this.pdfHash = pdfHash;
    }

    public JobApplicationModel getJobApplication() {
        return jobApplication;
    }

    public void setJobApplication(JobApplicationModel jobApplication) {
        this.jobApplication = jobApplication;
    }

    public ZonedDateTime getCreatedTimestamp() {
        return createdTimestamp;
    }

    public void setCreatedTimestamp(ZonedDateTime createdTimestamp) {
        this.createdTimestamp = createdTimestamp;
    }

    public ZonedDateTime getUpdatedTimestamp() {
        return updatedTimestamp;
    }

    public void setUpdatedTimestamp(ZonedDateTime updatedTimestamp) {
        this.updatedTimestamp = updatedTimestamp;
    }
}
