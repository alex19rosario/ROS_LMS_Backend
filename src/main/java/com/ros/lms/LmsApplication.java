package com.ros.lms;

import com.ros.lms.infraestructure.config.StorageProperties;
import com.ros.lms.infraestructure.security.RsaKeyProperties;
import com.ros.lms.ports.inbound.service_contracts.StorageService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;

@EnableConfigurationProperties({RsaKeyProperties.class, StorageProperties.class})
@SpringBootApplication
public class LmsApplication {

	public static void main(String[] args) {
		SpringApplication.run(LmsApplication.class, args);
	}

	@Bean
	CommandLineRunner init(StorageService storageService) {
		return (args) -> {
			storageService.init();
		};
	}

}
