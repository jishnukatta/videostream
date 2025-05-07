package com.videostreaming.authenticationservice.service;


import com.videostreaming.authenticationservice.dto.LoginRequest;
import com.videostreaming.authenticationservice.dto.LoginResponse;
import com.videostreaming.authenticationservice.dto.SignupRequest;
import com.videostreaming.authenticationservice.dto.SignupResponse;

public interface AuthService {

	SignupResponse userSignup(SignupRequest signupRequest);
    LoginResponse userLogin(LoginRequest loginRequest);
    LoginResponse processGoogleLogin(String email, String name);

}
