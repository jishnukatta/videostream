package com.videostreaming.authenticationservice.service;

import java.util.Optional;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

import com.videostreaming.authenticationservice.dao.AuthUserRepository;
import com.videostreaming.authenticationservice.dto.LoginRequest;
import com.videostreaming.authenticationservice.dto.LoginResponse;
import com.videostreaming.authenticationservice.dto.SignupRequest;
import com.videostreaming.authenticationservice.dto.SignupResponse;
import com.videostreaming.authenticationservice.entity.User;
import com.videostreaming.authenticationservice.exception.AdminLoginException;
import com.videostreaming.authenticationservice.kafka.KafkaProducer;
import com.videostreaming.authenticationservice.utils.JwtUtils;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

    private final AuthUserRepository authUserRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtils jwtUtils;
    private final KafkaProducer kafkaProducer;

	
	
	@Override
	public SignupResponse userSignup(SignupRequest signupRequest) {
		
        String encryptedPassword = passwordEncoder.encode(signupRequest.getPassword());
        User user = User.builder().email(signupRequest.getEmail())
        		    .password(encryptedPassword)
        		    .role(signupRequest.getRole())
        		    .username(signupRequest.getUsername())
        		    .category("NORMAL")
        		    .isVerified(false).build();
        
        try {
        	user = authUserRepository.save(user);
        }
        catch (Exception ex) {
            return new SignupResponse("failed to register, please try later ",false);
        }
        
        try {
            kafkaProducer.sendUserToUserService(user);
        }
        catch(Exception ex) {
        	System.out.println("failed to send the data to UserService "+ex);
        }

		return new SignupResponse("user successfully registered with id, please login"+user.getId(),true);
	}
 
	@Override
	public LoginResponse userLogin(LoginRequest loginRequest) {
		User user = authUserRepository.findByEmail(loginRequest.getEmail())
				.orElseThrow(() -> new RuntimeException("Invalid email or password"));

		if (!passwordEncoder.matches(loginRequest.getPassword(), user.getPassword())) {
			throw new RuntimeException("Invalid email or password");
		}
		
		String token = jwtUtils.generateToken(user);
		return LoginResponse.builder()
                .accessToken(token)
                .email(user.getEmail())
                .role(user.getRole())
                .build();
	}

	@Override
	public LoginResponse processGoogleLogin(String email, String name) {

		Optional<User> optionalUser = authUserRepository.findByEmail(email);
		System.out.println(email+" "+name);

		User user = null;

		if (optionalUser.isEmpty()) {
			user = User.builder().email(email).password("root").role("ADMIN").username(name).isVerified(true).build();

			try {
				user = authUserRepository.save(user);
			} catch (Exception ex) {
				throw new AdminLoginException("failed to login, please try later ");
			}

			kafkaProducer.sendUserToUserService(user);
		}
		else {
		    user = optionalUser.get();
		}

		String token = jwtUtils.generateToken(user);

		return LoginResponse.builder().accessToken(token).email(user.getEmail()).role(user.getRole()).build();
	}

}
