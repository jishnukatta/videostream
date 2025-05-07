package com.videostreaming.userservice.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.videostreaming.userservice.entity.User;
import com.videostreaming.userservice.service.UserService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/user")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @GetMapping("/profile/{email}")
    public User getProfile(@PathVariable String email) {
        return userService.getUserProfile(email);
    }
    
    @GetMapping("/profile/id/{id}")
    public User getProfile(@PathVariable int id) {
        return userService.getUserProfile(id);
    }
    
    @PutMapping("/category/{id}")
    public ResponseEntity<String> updateUserCategory(@PathVariable Long id, @RequestParam String category) {
        return userService.changeCategory(id, category);
    }

}