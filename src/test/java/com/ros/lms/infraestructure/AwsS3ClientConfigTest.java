package com.ros.lms.infraestructure;

import com.ros.lms.infraestructure.config.AwsS3ClientConfig;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.runner.ApplicationContextRunner;
import software.amazon.awssdk.services.s3.S3Client;

import static org.assertj.core.api.Assertions.assertThat;

class AwsS3ClientConfigTest {

    private final ApplicationContextRunner contextRunner = new ApplicationContextRunner()
            .withUserConfiguration(AwsS3ClientConfig.class)
            .withPropertyValues(
                    "cloud.aws.credentials.access-key=test-access-key",
                    "cloud.aws.credentials.secret-key=test-secret-key",
                    "cloud.aws.region.static=us-east-1"
            )
            .withSystemProperties("spring.profiles.active=prod");

    @Test
    void s3ClientBeanIsCreated() {
        contextRunner.run(context -> {
            assertThat(context).hasSingleBean(S3Client.class);
            S3Client s3Client = context.getBean(S3Client.class);
            assertThat(s3Client).isNotNull();
        });
    }
}
