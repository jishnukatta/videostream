package com.videostreaming.userservice.service;

import org.springframework.http.ResponseEntity;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

import com.videostreaming.userservice.dao.UserRepository;
import com.videostreaming.userservice.entity.User;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class UserService {
	
    private final UserRepository userRepository;
    

	
    @KafkaListener(topics = "usertopic", groupId = "user-group", containerFactory = "kafkaListenerContainerFactory")
    public void consumeUser(User user) {
        System.out.println("Received User: " + user);
        userRepository.save(user);
    }

	public User getUserProfile(String email) {
		return userRepository.findByEmail(email)
	            .orElseThrow(() -> new RuntimeException("User not found"));
    }
	
	public User getUserProfile(int id) {
		return userRepository.findById(id)
	            .orElseThrow(() -> new RuntimeException("User not found"));
	}

	public ResponseEntity<String> changeCategory(Long id,String category) {
		return userRepository.findById(id).map(user -> {
            user.setCategory(category.toUpperCase());
            userRepository.save(user);
            return ResponseEntity.ok("Category updated successfully");
        }).orElse(ResponseEntity.notFound().build());
	}


}
