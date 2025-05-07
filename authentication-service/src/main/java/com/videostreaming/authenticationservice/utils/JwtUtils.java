package com.videostreaming.authenticationservice.utils;

import java.util.Date;

import org.springframework.stereotype.Component;

import com.videostreaming.authenticationservice.entity.User;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

@Component
public class JwtUtils {

	private final String SECRET_KEY = "jishnu";

	public String generateToken(User user) {

		return Jwts.builder().setSubject(user.getUsername()).claim("role", user.getRole()).setIssuedAt(new Date())
				.setExpiration(new Date(System.currentTimeMillis() + 1000 * 60 * 60 * 10))
				.signWith(SignatureAlgorithm.HS256, SECRET_KEY).compact();
	}

}
