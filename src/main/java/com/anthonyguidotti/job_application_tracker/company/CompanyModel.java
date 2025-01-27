package com.anthonyguidotti.job_application_tracker.company;

import com.anthonyguidotti.job_application_tracker.job_application.JobApplicationModel;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.NaturalId;
import org.hibernate.annotations.UpdateTimestamp;
import org.hibernate.annotations.UuidGenerator;

import java.time.ZonedDateTime;
import java.util.Set;
import java.util.UUID;

import static jakarta.persistence.CascadeType.DETACH;
import static jakarta.persistence.FetchType.LAZY;
import static org.hibernate.annotations.UuidGenerator.Style.TIME;

@Entity(name = "Company")
@Table(name = "company")
public class CompanyModel {

    @Id
    @GeneratedValue
    @UuidGenerator(style = TIME)
    @Column(name = "company_id")
    private UUID id;

    @NaturalId
    @Column(
            name = "company_name",
            nullable = false,
            unique = true
    )
    private String name;

    @Column(
            name = "company_web_site_url",
            nullable = false
    )
    private String webSiteUrl;

    @OneToMany(
            fetch = LAZY,
            mappedBy = "company"
    )
    private Set<JobApplicationModel> jobApplications;

    @CreationTimestamp
    @Column(
            name = "company_created_timestamp",
            nullable = false,
            updatable = false
    )
    private ZonedDateTime createdTimestamp;

    @UpdateTimestamp
    @Column(
            name = "company_updated_timestamp",
            nullable = false
    )
    private ZonedDateTime updatedTimestamp;

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getWebSiteUrl() {
        return webSiteUrl;
    }

    public void setWebSiteUrl(String webSiteUrl) {
        this.webSiteUrl = webSiteUrl;
    }

    public Set<JobApplicationModel> getJobApplications() {
        return jobApplications;
    }

    public void setJobApplications(Set<JobApplicationModel> jobApplications) {
        this.jobApplications = jobApplications;
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
