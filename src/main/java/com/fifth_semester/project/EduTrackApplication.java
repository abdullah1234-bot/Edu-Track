package com.fifth_semester.project;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
@EntityScan(basePackages = {"com.fifth_semester.project.entities"})
public class EduTrackApplication {

	public static void main(String[] args) {
		SpringApplication.run(EduTrackApplication.class, args);
	}

}
