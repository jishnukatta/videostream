package com.videostreaming.video_streaming_service.dto;

import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Video {

    private Long id;

    private String title;

    private String description;

    private String filePath;

    private String category;

    private SubscriptionLevel subscriptionLevel;


    private VideoStatus status;

    private LocalDateTime uploadedAt;
}

