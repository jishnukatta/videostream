package com.videostreaming.transcoding_service.service;

import java.io.IOException;

import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.videostreaming.transcoding_service.dao.VideoRepository;
import com.videostreaming.transcoding_service.entity.Video;
import com.videostreaming.transcoding_service.entity.VideoStatus;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
@RequiredArgsConstructor
public class TranscodingService {

    private final VideoRepository videoRepository;

    @Transactional
    public void startTranscoding(Long videoId) {
          try {
            Video video = videoRepository.findById(videoId)
                    .orElseThrow(() -> new RuntimeException("Video not found"));

            String videoPath = video.getFilePath();
            log.info("Video path: {}", videoPath);

            transcodeVideo(videoPath);

            log.info("Transcoding complete. About to set status to READY for video ID: {}", videoId);

            video.setStatus(VideoStatus.READY);
            log.info("Status set to READY. About to save to DB for video ID: {}", videoId);

            videoRepository.save(video);
            log.info("Video saved successfully. Transcoding completed for video ID: {}", videoId);
            
        } catch (Exception e) {
            log.error("Error during transcoding for video ID: {}", videoId, e);
        }

    }

	private void transcodeVideo(String videoPath) throws IOException {
		String ffmpegPath = System.getenv("FFMPEG_PATH");

		ProcessBuilder builder = new ProcessBuilder(ffmpegPath, "-i", videoPath, "-c:v", "libx264", "-preset", "fast",
				"-c:a", "aac", "-strict", "-2", videoPath + "_transcoded.mp4");
		builder.redirectErrorStream(true); // merge stdout and stderr

		Process process = builder.start();

		// ✅ Read and log output while transcoding
		try (var reader = new java.io.BufferedReader(new java.io.InputStreamReader(process.getInputStream()))) {

			String line;
			while ((line = reader.readLine()) != null) {
				log.info("[FFmpeg] {}", line);
			}
		}

		try {
			int exitCode = process.waitFor();
			if (exitCode != 0) {
				throw new IOException("FFmpeg failed with exit code: " + exitCode);
			}
		} catch (InterruptedException e) {
			Thread.currentThread().interrupt();
			throw new IOException("Interrupted while waiting for FFmpeg", e);
		}
	}
}
