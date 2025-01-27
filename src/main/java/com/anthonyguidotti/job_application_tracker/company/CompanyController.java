package com.anthonyguidotti.job_application_tracker.company;

import com.anthonyguidotti.job_application_tracker.validation.JsonSchemaValidator;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class CompanyController {
    private final CompanyService companyService;
    private final JsonSchemaValidator jsonSchemaValidator;

    public CompanyController(
            CompanyService companyService,
            JsonSchemaValidator jsonSchemaValidator
    ) {
        this.companyService = companyService;
        this.jsonSchemaValidator = jsonSchemaValidator;
    }

    @InitBinder
    protected void initBinder(WebDataBinder binder) {
        binder.setValidator(jsonSchemaValidator);
    }

    @GetMapping("/companies")
    public List<Company> getCompanies(HttpServletRequest request) {
        return companyService.getAll();
    }

    @PostMapping("/companies")
    public Company createCompany(
            @RequestBody @Valid CompanyCreatePayload companyCreatePayload,
            HttpServletResponse response
    ) {
        Company companyModel = companyService.save(companyCreatePayload);
        response.setStatus(HttpServletResponse.SC_CREATED);
        return companyModel;
    }
}
