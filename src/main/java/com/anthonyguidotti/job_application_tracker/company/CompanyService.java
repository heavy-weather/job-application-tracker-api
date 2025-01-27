package com.anthonyguidotti.job_application_tracker.company;

import org.springframework.boot.ssl.SslBundle;
import org.springframework.boot.ssl.SslBundleKey;
import org.springframework.boot.ssl.SslBundles;
import org.springframework.boot.ssl.SslStoreBundle;
import org.springframework.stereotype.Service;

import java.security.KeyStore;
import java.util.List;

@Service
public class CompanyService {
    private final CompanyRepository companyRepository;

    public CompanyService(
            CompanyRepository companyRepository,
            SslBundles sslBundles
    ) {
        this.companyRepository = companyRepository;
        SslBundle sslBundle = sslBundles.getBundle("server");
        SslBundleKey key = sslBundle.getKey();
        SslStoreBundle stores = sslBundle.getStores();
        KeyStore keyStore = stores.getKeyStore();
        String password = stores.getKeyStorePassword();

        System.out.println();
    }

    public List<Company> getAll() {
        return companyRepository.findAll()
                .stream()
                .map(this::mapExternalRepresentation)
                .toList();
    }

    public Company save(CompanyCreatePayload company) {
        CompanyModel companyModel = mapDomainModel(company);
        return mapExternalRepresentation(companyRepository.save(companyModel));
    }

    private Company mapExternalRepresentation(CompanyModel companyModel) {
        Company company = new Company();
        company.setId(companyModel.getId().toString());
        company.setName(companyModel.getName());
        company.setWebSiteUrl(companyModel.getWebSiteUrl());
        return company;
    }

    private CompanyModel mapDomainModel(CompanyCreatePayload companyCreatePayload) {
        CompanyModel companyModel = new CompanyModel();
        companyModel.setName(companyCreatePayload.getName());
        companyModel.setWebSiteUrl(companyCreatePayload.getWebSiteUrl());
        return companyModel;
    }
}
