package com.videostreaming.authenticationservice.config;

import java.util.HashMap;
import java.util.Map;

import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.serialization.StringSerializer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.core.DefaultKafkaProducerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.core.ProducerFactory;
import org.springframework.kafka.support.serializer.JsonSerializer;

import com.videostreaming.authenticationservice.entity.User;

@Configuration
public class KafkaConfig {

	@Bean
	public KafkaTemplate<String,User> getKafkaTemplate()
	{
		return new KafkaTemplate<>(getProducerFactory());
	}
	
	@Bean
	public ProducerFactory<String,User> getProducerFactory()
	{
		Map<String, Object> configProps = new HashMap<>();
		configProps.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, "localhost:9092");
		configProps.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
		configProps.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, JsonSerializer.class); // Serialize the AuthUser
																								// object
		return new DefaultKafkaProducerFactory<>(configProps);

	}
}
