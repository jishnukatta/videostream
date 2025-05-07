package com.videostreaming.authenticationservice.kafka;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import lombok.Getter;
import lombok.Setter;

@Component
@ConfigurationProperties(prefix = "kafkatopics")
@Setter
@Getter
public class KafkaTopics {

	@Value("${kafkatopics.usertopic}") //explicit binding...optional if namings are good(usertopic)
	public String userTopic;
}
