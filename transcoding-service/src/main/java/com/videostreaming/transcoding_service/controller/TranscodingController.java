package com.videostreaming.transcoding_service.controller;

import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.videostreaming.transcoding_service.service.TranscodingService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/v1/transcoding")
@RequiredArgsConstructor
public class TranscodingController {

    private final TranscodingService transcodingService;

    @PostMapping("/start/{videoId}")
    public String startTranscoding(@PathVariable Long videoId) {
        transcodingService.startTranscoding(videoId);
        return "Transcoding started for video ID: " + videoId;
    }
}