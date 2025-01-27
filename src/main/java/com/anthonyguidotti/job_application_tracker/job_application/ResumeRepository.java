package com.anthonyguidotti.job_application_tracker.job_application;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface ResumeRepository extends JpaRepository<ResumeModel, UUID> {

}
