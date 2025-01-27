package com.anthonyguidotti.job_application_tracker.job_application;

import com.anthonyguidotti.job_application_tracker.validation.JsonSchemaValidator;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Arrays;
import java.util.List;

@RestController
public class JobApplicationController {
    private final JobApplicationService jobApplicationService;
    private final JsonSchemaValidator jsonSchemaValidator;
    private final String apiUrl;

    public JobApplicationController(
            JobApplicationService jobApplicationService,
            JsonSchemaValidator jsonSchemaValidator,
            @Value("${jobApplicationTracker.api.url}") String apiUrl
    ) {
        this.jobApplicationService = jobApplicationService;
        this.jsonSchemaValidator = jsonSchemaValidator;
        this.apiUrl = apiUrl;
    }

    @InitBinder
    protected void initBinder(WebDataBinder binder) {
        binder.setValidator(jsonSchemaValidator);
    }

    @GetMapping("/job-applications")
    public List<JobApplication> getJobApplications(
            @RequestParam(required = false, defaultValue = "false") Boolean partial
    ) {
        // TODO: pagination required for this endpoint
        if (partial) {
            return jobApplicationService.getAllAsPartial();
        }
        return jobApplicationService.getAll();
    }

    @PostMapping("/job-applications")
    public JobApplication createJobApplication(
            @RequestBody @Valid JobApplicationCreatePayload jobApplication,
            HttpServletResponse response
    ) {
        try {
            JobApplication createdJobApplication = jobApplicationService.create(jobApplication);
            response.setStatus(HttpServletResponse.SC_CREATED);
            response.setHeader(
                    HttpHeaders.LOCATION,
                    apiUrl + "/job-applications/" + createdJobApplication.getId()
            );

            return createdJobApplication;
        } catch (JobApplicationInvalidRepresentationException e) {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            return null;
        }
    }

    @GetMapping("/job-applications/{jobApplicationId}")
    public JobApplication getJobApplication(
            @PathVariable String jobApplicationId,
            HttpServletResponse response
    ) {
        JobApplication jobApplication = jobApplicationService.get(jobApplicationId);
        if (jobApplication == null) {
            response.setStatus(HttpServletResponse.SC_NOT_FOUND);
            return null;
        }
        return jobApplication;
    }

    @PutMapping("/job-applications/{jobApplicationId}")
    public void updateJobApplication(
            @PathVariable String jobApplicationId,
            @RequestBody @Valid JobApplicationUpdatePayload jobApplication,
            HttpServletResponse response
    ) {
        if (jobApplicationService.get(jobApplicationId) == null) {
            response.setStatus(HttpServletResponse.SC_NOT_FOUND);
            return;
        }
        if (!jobApplicationId.equals(jobApplication.getId())) {
            response.setStatus(HttpServletResponse.SC_CONFLICT);
            return;
        }
        try {
            jobApplicationService.replace(jobApplication);
            response.setStatus(HttpServletResponse.SC_NO_CONTENT);
        } catch (JobApplicationInvalidRepresentationException e) {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
        } catch (JobApplicationNotFoundException e) {
            response.setStatus(HttpServletResponse.SC_NOT_FOUND);
        }
    }

    @DeleteMapping("/job-applications/{jobApplicationId}")
    public void deleteJobApplication(
            @PathVariable String jobApplicationId,
            HttpServletResponse response
    ) {
        if (jobApplicationService.get(jobApplicationId) == null) {
            response.setStatus(HttpServletResponse.SC_NOT_FOUND);
        } else {
            jobApplicationService.delete(jobApplicationId);
        }
    }

    @GetMapping("/statuses")
    public List<JobApplicationStatus> getJobApplicationStatuses() {
        return Arrays.asList(JobApplicationStatus.values());
    }
}
