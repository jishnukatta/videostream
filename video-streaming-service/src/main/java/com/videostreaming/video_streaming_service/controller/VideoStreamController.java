package com.videostreaming.video_streaming_service.controller;

import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.videostreaming.video_streaming_service.service.StreamService;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/stream")
public class VideoStreamController {

	private final StreamService streamService;

	@GetMapping("/video")
	public ResponseEntity<Resource> streamVideo(
	        @RequestParam Long userId,
	        @RequestParam Long videoId,
	        @RequestHeader HttpHeaders headers) {
	    return streamService.validateAndStream(userId, videoId, headers);
	}
}
