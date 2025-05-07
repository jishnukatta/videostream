package com.videostreaming.authenticationservice.kafka;

import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

import com.videostreaming.authenticationservice.entity.User;

import lombok.AllArgsConstructor;


@Component
@AllArgsConstructor
public class KafkaProducer {

    private final KafkaTemplate<String,User> userTopicTemplate;
    private final KafkaTopics kafkaTopics;
    
    public void sendUserToUserService(User user)
    { try {
        userTopicTemplate.send(kafkaTopics.getUserTopic(), user).get(); // This blocks
        System.out.println("@producer sent the user " + user);
    } catch (Exception e) {
        System.out.println("failed to send the data to UserService: " + e.getMessage());
    }}

}
