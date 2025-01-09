package com.bosch.coding.producer;

import org.junit.jupiter.api.Test;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessageListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.amqp.rabbit.listener.MessageListenerContainer;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.rabbit.core.RabbitAdmin;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
@ActiveProfiles("test")  // Use a specific profile for tests if required
public class EventProducerApplicationTests {

    @Autowired
    private RabbitTemplate rabbitTemplate; // RabbitTemplate to send messages

    @Autowired
    private RabbitAdmin rabbitAdmin; // Used to declare queues in the test

    @Autowired
    private MessageListenerContainer messageListenerContainer; // Listener to capture received messages

    @Test
    public void testRabbitMQProducerConsumer() throws InterruptedException {
        String messageToSend = "Hello RabbitMQ!";
        
        // Declare the queue for the test (ensure it exists)
        rabbitAdmin.declareQueue(new Queue("testQueue"));

        // Create a listener to capture received messages
        messageListenerContainer.setupMessageListener(new MessageListener() {
            @Override
            public void onMessage(Message message) {
                String receivedMessage = new String(message.getBody());
                // Assert that the received message matches the one sent
                assertEquals(messageToSend, receivedMessage);
            }
        });

        // Send message to RabbitMQ
        rabbitTemplate.convertAndSend("testQueue", messageToSend);

        // Sleep to give time for the listener to process the message
        Thread.sleep(1000); // Adjust time as needed for RabbitMQ processing
    }
}