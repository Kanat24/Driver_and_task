package com.example.drivesbillsmicroservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class DrivesBillsMicroserviceApplication {

	public static void main(String[] args) {
		SpringApplication.run(DrivesBillsMicroserviceApplication.class, args);
	}
}