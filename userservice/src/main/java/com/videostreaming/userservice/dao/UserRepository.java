package com.videostreaming.userservice.dao;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.videostreaming.userservice.entity.User;

public interface UserRepository extends JpaRepository<User, Long> {

	Optional<User> findByEmail(String email);

	Optional<User> findById(int id);


}
