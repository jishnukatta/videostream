package com.videostreaming.userservice.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
public class User {

	@Id
    private Long id;
	private String username;
	private String password;
	private String email;
	private String role;
	@Column(name = "is_verified")
	private boolean isVerified;
	@Column(name = "category")
	private String category;
}
