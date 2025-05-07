package com.videostreaming.authenticationservice.dao;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.videostreaming.authenticationservice.entity.User;

public interface AuthUserRepository extends JpaRepository<User, Long>{

	Optional<User> findByEmail(String email);

}
