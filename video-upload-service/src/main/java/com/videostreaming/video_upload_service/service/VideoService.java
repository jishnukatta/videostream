package com.videostreaming.video_upload_service.service;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.reactive.function.client.WebClient;

import com.videostreaming.video_upload_service.dao.VideoRepository;
import com.videostreaming.video_upload_service.dto.VideoRequest;
import com.videostreaming.video_upload_service.entity.Video;
import com.videostreaming.video_upload_service.entity.VideoStatus;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class VideoService {

    private final VideoRepository videoRepository;
    private final WebClient webClient;

    @Autowired
    public VideoService(VideoRepository videoRepository, WebClient.Builder webClientBuilder) {
        this.videoRepository = videoRepository;
        this.webClient = webClientBuilder
                .baseUrl("http://transcoding-service") 	
                .build();
    }

    public String uploadVideo(VideoRequest request, MultipartFile file) {
        try {
            // Save file to disk
            String filename = System.currentTimeMillis() + "_" + file.getOriginalFilename();
            Path uploadPath = Paths.get("D:/uploads");

            if (!Files.exists(uploadPath)) {
                Files.createDirectories(uploadPath);
            }

            Path filePath = uploadPath.resolve(filename);
            file.transferTo(filePath);

            // Save metadata to DB
            Video video = Video.builder()
                    .title(request.getTitle())
                    .description(request.getDescription())
                    .category(request.getCategory())
                    .subscriptionLevel(request.getSubscriptionLevel())
                    .filePath(filePath.toString())
                    .status(VideoStatus.PROCESSING)
                    .build();

            Video savedVideo = videoRepository.save(video);
            Long videoId = savedVideo.getId();

            // Call transcoding service
            webClient.post()
                    .uri("/api/v1/transcoding/start/{videoId}", videoId)
                    .retrieve()
                    .bodyToMono(Void.class)
                    .doOnTerminate(() -> log.info("Transcoding triggered for video: {}", videoId))
                    .subscribe();

            return "Video uploaded and transcoding started (ID: " + videoId + ").";

        } catch (Exception e) {
            log.error("Error uploading video: ", e);
            throw new RuntimeException("Failed to upload video.");
        }
    }

	public Video getVideoById(int id) {
		return videoRepository.findById(id)
	            .orElseThrow(() -> new RuntimeException("User not found"));
	}
}
