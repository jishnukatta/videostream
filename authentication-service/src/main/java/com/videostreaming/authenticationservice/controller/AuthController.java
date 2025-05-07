package com.videostreaming.authenticationservice.controller;

import java.io.IOException;
import java.util.Base64;
import java.util.Map;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.videostreaming.authenticationservice.dto.LoginRequest;
import com.videostreaming.authenticationservice.dto.LoginResponse;
import com.videostreaming.authenticationservice.dto.SignupRequest;
import com.videostreaming.authenticationservice.dto.SignupResponse;
import com.videostreaming.authenticationservice.exception.PasswordMismatchException;
import com.videostreaming.authenticationservice.service.AuthService;

import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
public class AuthController {

	private final AuthService authservice;
	private final RestTemplate restTemplate;

	@PostMapping("/auth/user-signup")
	public ResponseEntity<?> userSignup(@Valid @RequestBody SignupRequest signupRequest)
	{
		if (!signupRequest.getPassword().equals(signupRequest.getConfirmPassword())) {
			throw new PasswordMismatchException("Password and Confirm Password do not match");
		}
		
		SignupResponse response = authservice.userSignup(signupRequest);
		return ResponseEntity.status(HttpStatus.CREATED).body(response);
	}

	@PostMapping("/auth/user-login")
	public ResponseEntity<?> userLogin(@Valid @RequestBody LoginRequest request) {

	
			LoginResponse loginResponse = authservice.userLogin(request);
			return ResponseEntity.ok(loginResponse);
		
	}
	
	@GetMapping("/auth/admin-login")
	public void redirectToGoogle(HttpServletResponse response) throws IOException {
	    String redirectUri = "http://localhost:8081/auth/oauth2/callback";
	    String clientId = "738526775144-ul8vutiah6urjf0a8drsodcarih7clfj.apps.googleusercontent.com";
	    String googleOAuthUrl = "https://accounts.google.com/o/oauth2/v2/auth" +
	            "?client_id=" + clientId +
	            "&redirect_uri=" + redirectUri +
	            "&response_type=code" +
	            "&scope=openid%20email%20profile";
	    response.sendRedirect(googleOAuthUrl);
	}


	@GetMapping("/auth/oauth2/callback")
	public void handleCallback(@RequestParam("code") String code, HttpServletResponse response) throws IOException {
	    // Exchange code for access token + ID token
	    String tokenEndpoint = "https://oauth2.googleapis.com/token";

	    HttpHeaders headers = new HttpHeaders();
	    headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
 
	    MultiValueMap<String, String> body = new LinkedMultiValueMap<>();
	    body.add("code", code);
	    body.add("client_id", System.getenv("GOOGLE_OAUTH_CLIENT_ID"));
	    body.add("client_secret", System.getenv("GOOGLE_OAUTH_CLIENT_SECRET"));
	    body.add("redirect_uri", "http://localhost:8081/auth/oauth2/callback");
	    body.add("grant_type", "authorization_code");

	    HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<>(body, headers);

	    ResponseEntity<Map> tokenResponse = restTemplate.postForEntity(tokenEndpoint, request, Map.class);
	    String idToken = (String) tokenResponse.getBody().get("id_token");

	    // Decode ID token (Google JWT) to get user info
	    Map<String, Object> userInfo = parseGoogleJwt(idToken);
	    String email = (String) userInfo.get("email");
	    String name = (String) userInfo.get("name");

	    // Save if new
	    LoginResponse login_response = authservice.processGoogleLogin(email,name); // your method, inserts if not exists


	    response.sendRedirect("http://localhost:8080/finalize?token=" + login_response.getAccessToken());	
	    }
	
	
	private Map<String, Object> parseGoogleJwt(String jwtToken) throws JsonMappingException, JsonProcessingException {
	    String[] parts = jwtToken.split("\\.");
	    Base64.Decoder decoder = Base64.getUrlDecoder();

	    String payload = new String(decoder.decode(parts[1]));
	    ObjectMapper mapper = new ObjectMapper();
	    return mapper.readValue(payload, new TypeReference<Map<String, Object>>() {});
	}


}
