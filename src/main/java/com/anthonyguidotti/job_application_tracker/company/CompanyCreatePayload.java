package com.anthonyguidotti.job_application_tracker.company;

import com.anthonyguidotti.job_application_tracker.validation.JsonSchemaValidate;

@JsonSchemaValidate("schema/company.json")
public class CompanyCreatePayload {
    private String name;
    private String webSiteUrl;

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
}
