package com.anthonyguidotti.job_application_tracker.job_application;

import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.time.ZonedDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface JobApplicationRepository extends JpaRepository<JobApplicationModel, UUID> {

    @EntityGraph(value = "JobApplication.partial", type = EntityGraph.EntityGraphType.LOAD)
    List<JobApplicationModel> findAllByOrderByCreatedTimestampDesc();

    @EntityGraph(value = "JobApplication.full", type = EntityGraph.EntityGraphType.LOAD)
    Optional<JobApplicationModel> findWithReferencesById(UUID id);

    @EntityGraph(value = "JobApplication.partial", type = EntityGraph.EntityGraphType.LOAD)
    Optional<JobApplicationModel> findWithoutReferencesById(UUID id);

    @Modifying
    @Query(value = "UPDATE JobApplication SET status = :newStatus WHERE status = :oldStatus AND sentDate < :sentDate")
    int updateStatusWhereSentDateBefore(ZonedDateTime sentDate, JobApplicationStatus oldStatus, JobApplicationStatus newStatus);
}
