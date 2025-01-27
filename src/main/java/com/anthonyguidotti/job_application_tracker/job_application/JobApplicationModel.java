package com.anthonyguidotti.job_application_tracker.job_application;

import com.anthonyguidotti.job_application_tracker.company.CompanyModel;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.NamedAttributeNode;
import jakarta.persistence.NamedEntityGraph;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import org.hibernate.Length;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import org.hibernate.annotations.UuidGenerator;

import java.time.ZonedDateTime;
import java.util.UUID;

import static org.hibernate.annotations.UuidGenerator.Style.TIME;

@Entity(name = "JobApplication")
@Table(name = "job_application")
@NamedEntityGraph(
        name = "JobApplication.partial",
        includeAllAttributes = true,
        attributeNodes = {
                @NamedAttributeNode("company")
        }
)
@NamedEntityGraph(
        name = "JobApplication.full",
        includeAllAttributes = true,
        attributeNodes = {
                @NamedAttributeNode("company"),
                @NamedAttributeNode("resume")
        }
)
public class JobApplicationModel {

    @Id
    @GeneratedValue
    @UuidGenerator(style = TIME)
    @Column(name = "job_application_id")
    private UUID id;

    @Column(
            name = "job_application_job_title",
            nullable = false
    )
    private String jobTitle;

    @Column(
            name = "job_application_job_description",
            nullable = false,
            length = Length.LOB_DEFAULT
    )
    private String jobDescription;

    @OneToOne(
            fetch = FetchType.LAZY,
            mappedBy = "jobApplication",
            cascade = CascadeType.ALL
    )
    private ResumeModel resume;

    @Enumerated(EnumType.ORDINAL)
    @Column(
            name = "job_application_status",
            nullable = false
    )
    private JobApplicationStatus status;

    @Column(
            name = "job_application_job_posting_url",
            nullable = false
    )
    private String jobPostingUrl;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(
            name = "job_application_company_id",
            updatable = false
    )
    private CompanyModel company;

    @Column(
            name = "job_application_sent_date",
            nullable = false
    )
    private ZonedDateTime sentDate;

    @Column(
            name = "job_application_notes",
            length = Length.LONG
    )
    private String notes;

    @CreationTimestamp
    @Column(
            name = "job_application_created_timestamp",
            nullable = false,
            updatable = false
    )
    private ZonedDateTime createdTimestamp;

    @UpdateTimestamp
    @Column(
            name = "job_application_updated_timestamp",
            nullable = false
    )
    private ZonedDateTime updatedTimestamp;

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
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

    public ResumeModel getResume() {
        return resume;
    }

    public void setResume(ResumeModel resume) {
        resume.setId(this.id);
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

    public CompanyModel getCompany() {
        return company;
    }

    public void setCompany(CompanyModel company) {
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
