package com.videostreaming.video_streaming_service.dto;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class User {

    private Long id;
	private String username;
	private String password;
	private String email;
	private String role;
	private boolean isVerified;
	private String category;
}
