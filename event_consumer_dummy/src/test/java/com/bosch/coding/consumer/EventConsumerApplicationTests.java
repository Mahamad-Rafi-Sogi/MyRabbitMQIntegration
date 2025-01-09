package com.bosch.coding.consumer;

import org.junit.jupiter.api.Test;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.rabbit.listener.MessageListener;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.amqp.rabbit.listener.MessageListenerContainer;

import static org.mockito.Mockito.*;

@SpringBootTest
public class EventConsumerApplicationTests {

    @MockBean
    private RabbitTemplate rabbitTemplate; // Mock RabbitTemplate
    
    @MockBean
    private MessageListenerContainer messageListenerContainer; // Mock MessageListenerContainer

    @Test
    void testMessageConsumer() throws InterruptedException {
        String messageToReceive = "Hello from RabbitMQ!";
        
        // Create a mock message with content
        Message message = mock(Message.class);
        when(message.getBody()).thenReturn(messageToReceive.getBytes());

        // Create a listener to simulate message consumption
        MessageListener listener = new MessageListener() {
            @Override
            public void onMessage(Message receivedMessage) {
                String received = new String(receivedMessage.getBody());
                // Assert that the message received is as expected
                assert received.equals(messageToReceive);
            }
        };

        // Simulate setting up the listener in the container
        when(messageListenerContainer.isRunning()).thenReturn(true);
        messageListenerContainer.setupMessageListener(listener);

        // Simulate receiving the message
        listener.onMessage(message);

        // Verify that the message was processed (consumed)
        verify(rabbitTemplate, times(1)).convertAndSend(anyString(), eq(messageToReceive));
    }

    @Configuration
    static class TestConfig {
        @Bean
        public RabbitTemplate rabbitTemplate() {
            return mock(RabbitTemplate.class);
        }
    }
}