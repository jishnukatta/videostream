package com.videostreaming.apigateway.config;

import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;


@Configuration
public class GatewayConfig {

	@Bean
    public RouteLocator routes(RouteLocatorBuilder builder) {
        return builder.routes()
            .route("auth-service", r -> r.path("/auth/**")
                .uri("lb://AUTHENTICATION-SERVICE"))
            .route("user-service", r -> r.path("/user/**")
                .uri("lb://USER-SERVICE"))
            .route("video-upload-service", r -> r.path("/videos/**")
                    .uri("lb://VIDEO-UPLOAD-SERVICE"))
            .route("video-streaming-service", r -> r.path("/stream/**")
                    .uri("lb://VIDEO-STREAMING-SERVICE"))
            .build();
    }
	
}
