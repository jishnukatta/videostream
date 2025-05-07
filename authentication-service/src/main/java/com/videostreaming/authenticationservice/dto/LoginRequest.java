package com.videostreaming.authenticationservice.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class LoginRequest {

	@Email(message = "email should be valid")	
	private String email;

	@NotBlank(message = "password is mandatory")
	@Size(min = 5, max = 20, message = "password should be in between 8 to 20 characters")
	private String password;
}
