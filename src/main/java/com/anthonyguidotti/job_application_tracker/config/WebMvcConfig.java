package com.anthonyguidotti.job_application_tracker.config;

import com.fasterxml.jackson.databind.SerializationFeature;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.List;

@EnableWebMvc
@Configuration
public class WebMvcConfig implements WebMvcConfigurer {
    private final String applicationDomainName;

    public WebMvcConfig(
            @Value("${jobApplicationTracker.client.url}") String applicationDomainName
    ) {
        this.applicationDomainName = applicationDomainName;
    }

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/job-applications/**")
                .allowedOrigins(applicationDomainName)
                .allowedMethods(
                        RequestMethod.HEAD.name(),
                        RequestMethod.GET.name(),
                        RequestMethod.POST.name(),
                        RequestMethod.PUT.name()
                );
        registry.addMapping("/companies/**")
                .allowedOrigins(applicationDomainName);
        registry.addMapping("/statuses")
                .allowedOrigins(applicationDomainName);
    }

    @Override
    public void extendMessageConverters(List<HttpMessageConverter<?>> messageConverters) {
        // It's not known why Spring has decided to write ZonedDateTime as a timestamp with an incorrect date
        // This will forcibly override that behavior
        for (HttpMessageConverter<?> messageConverter : messageConverters) {
            if (messageConverter instanceof MappingJackson2HttpMessageConverter) {
                ((MappingJackson2HttpMessageConverter) messageConverter)
                        .getObjectMapper()
                        .disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
            }
        }
    }
}
