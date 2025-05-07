package com.videostreaming.video_streaming_service.service;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpRange;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.MediaTypeFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.videostreaming.video_streaming_service.dto.SubscriptionLevel;
import com.videostreaming.video_streaming_service.dto.User;
import com.videostreaming.video_streaming_service.dto.Video;

@Service
public class StreamService {

    @Autowired
	private RestTemplate restTemplate;
	
    private final String userServiceUrl = "http://user-service/user/profile/id/";
    private final String videoServiceUrl = "http://video-upload-service/videos/id/";

    public ResponseEntity<Resource> validateAndStream(Long userId, Long videoId, HttpHeaders headers) {
      
    	ResponseEntity<User> userResponse = restTemplate.getForEntity(userServiceUrl + userId, User.class);
        User user = userResponse.getBody();

        ResponseEntity<Video> videoResponse = restTemplate.getForEntity(videoServiceUrl + videoId, Video.class);
        Video video = videoResponse.getBody();



        if (user == null || video == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }

        if (!canUserAccess(user.getCategory(), video.getSubscriptionLevel())) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }

        File videoFile = new File(video.getFilePath());
        if (!videoFile.exists()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }

        try {
            long fileLength = videoFile.length();
            List<HttpRange> ranges = headers.getRange();

            if (ranges.isEmpty()) {
                InputStreamResource resource = new InputStreamResource(new FileInputStream(videoFile));
                return ResponseEntity.ok()
                        .contentType(MediaTypeFactory.getMediaType(videoFile.getName()).orElse(MediaType.APPLICATION_OCTET_STREAM))
                        .contentLength(fileLength)
                        .body(resource);
            } else {
                HttpRange range = ranges.get(0);
                long start = range.getRangeStart(fileLength);
                long end = range.getRangeEnd(fileLength);

                long contentLength = end - start + 1;
                InputStream inputStream = new FileInputStream(videoFile);
                inputStream.skip(start);

                InputStreamResource resource = new InputStreamResource(new LimitedInputStream(inputStream, contentLength));

                return ResponseEntity.status(HttpStatus.PARTIAL_CONTENT)
                        .header(HttpHeaders.CONTENT_TYPE, Files.probeContentType(videoFile.toPath()))
                        .header(HttpHeaders.ACCEPT_RANGES, "bytes")
                        .header(HttpHeaders.CONTENT_LENGTH, String.valueOf(contentLength))
                        .header(HttpHeaders.CONTENT_RANGE, "bytes " + start + "-" + end + "/" + fileLength)
                        .body(resource);
            }

        } catch (IOException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    private boolean canUserAccess(String userCategory, SubscriptionLevel videoLevel) {
        SubscriptionLevel userLevel = SubscriptionLevel.fromString(userCategory);
        if (userLevel == null || videoLevel == null) return false;
        return userLevel.canAccess(videoLevel);
    }

    // Utility to limit input stream
    static class LimitedInputStream extends InputStream {
        private final InputStream delegate;
        private long remaining;

        public LimitedInputStream(InputStream delegate, long remaining) {
            this.delegate = delegate;
            this.remaining = remaining;
        }

        @Override
        public int read() throws IOException {
            if (remaining <= 0) return -1;
            int result = delegate.read();
            if (result != -1) remaining--;
            return result;
        }

        @Override
        public int read(byte[] b, int off, int len) throws IOException {
            if (remaining <= 0) return -1;
            len = (int) Math.min(len, remaining);
            int result = delegate.read(b, off, len);
            if (result != -1) remaining -= result;
            return result;
        }

        @Override
        public void close() throws IOException {
            delegate.close();
        }
    }
}