package com.videostreaming.authenticationservice.config;

import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

@Configuration
public class TemplateConfig {
	
	@Bean
	public RestTemplate restTemplate() {
	    return new RestTemplate();
	}

}
