package com.videostreaming.video_upload_service.controller;

import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.videostreaming.video_upload_service.dto.VideoRequest;
import com.videostreaming.video_upload_service.entity.Video;
import com.videostreaming.video_upload_service.service.VideoService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/videos")
@RequiredArgsConstructor
public class VideoController {

    private final VideoService videoService;

    @PostMapping(value = "/upload", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<?> uploadVideo(@RequestPart("metadata") VideoRequest request,
                                         @RequestPart("file") MultipartFile file) {
        return ResponseEntity.ok(videoService.uploadVideo(request, file));
    }
    
    @GetMapping("/id/{id}")
    public Video getProfile(@PathVariable int id) {
        return videoService.getVideoById(id);
    }
}
