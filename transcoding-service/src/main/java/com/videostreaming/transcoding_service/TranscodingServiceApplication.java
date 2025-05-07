package com.videostreaming.transcoding_service;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class TranscodingServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(TranscodingServiceApplication.class, args);
	}

}
