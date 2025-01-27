package com.anthonyguidotti.job_application_tracker;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

import java.util.Map;

@SpringBootApplication
@EnableJpaRepositories
public class Application {

	public static void main(String[] args) {
		// TODO: Look into merging the API and UI apps into a mono repo
		SpringApplication.run(Application.class, args);
	}
}
