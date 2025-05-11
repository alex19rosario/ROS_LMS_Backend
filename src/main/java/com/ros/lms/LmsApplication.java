package com.ros.lms;

import com.ros.lms.infraestructure.security.RsaKeyProperties;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

@EnableConfigurationProperties(RsaKeyProperties.class)
@SpringBootApplication
public class LmsApplication {

	public static void main(String[] args) {
		SpringApplication.run(LmsApplication.class, args);
	}

}
