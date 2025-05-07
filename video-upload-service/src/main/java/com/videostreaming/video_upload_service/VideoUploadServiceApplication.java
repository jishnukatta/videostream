package com.videostreaming.video_upload_service;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;

@SpringBootApplication
public class VideoUploadServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(VideoUploadServiceApplication.class, args);
	}

}
