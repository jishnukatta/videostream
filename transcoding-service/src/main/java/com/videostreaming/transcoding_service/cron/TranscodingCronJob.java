package com.videostreaming.transcoding_service.cron;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.videostreaming.transcoding_service.dao.VideoRepository;
import com.videostreaming.transcoding_service.entity.Video;
import com.videostreaming.transcoding_service.entity.VideoStatus;
import com.videostreaming.transcoding_service.service.TranscodingService;

@Component
public class TranscodingCronJob {
	
	@Autowired
	private VideoRepository videoRepository;
	@Autowired
	private TranscodingService service;

    // Runs every 60 seconds
    @Scheduled(cron = "0 * * * * *")
    public void checkPendingTranscodes() {
        System.out.println("Checking for pending transcoding tasks...");
        List<Video>failedVideos = videoRepository.findByStatus(VideoStatus.FAILED);
        for (Video video : failedVideos) {
        	service.startTranscoding(video.getId()); // reuse your existing method
        }
    }
}