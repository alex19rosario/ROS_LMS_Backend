package com.ros.lms;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@ActiveProfiles("test")
class LmsApplicationTests {

	@Test
	void contextLoads() {
		// This test just verifies that the Spring context loads
		assertThat(true).isTrue();
	}

}
