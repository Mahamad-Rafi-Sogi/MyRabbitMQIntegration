package com.bosch.coding.consumer;

import static org.mockito.Mockito.mock;

import org.junit.jupiter.api.Test;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@SpringBootTest
@MockBean(RabbitTemplate.class)
class EventConsumerApplicationTests {

	@Test
	void contextLoads() {
	}
	

@Configuration
static class TestConfig {
    @Bean
    public RabbitTemplate rabbitTemplate() {
        return mock(RabbitTemplate.class);
    }
}

}
