package com.videostreaming.video_upload_service.dto;

import com.videostreaming.video_upload_service.entity.SubscriptionLevel;

import lombok.Data;

@Data
public class VideoRequest {
    private String title;
    private String description;
    private String category;
    private SubscriptionLevel subscriptionLevel;
}
