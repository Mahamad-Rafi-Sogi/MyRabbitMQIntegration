package com.bosch.coding.producer.service;

import com.bosch.coding.producer.event.WarehouseRequestEvent;
import com.bosch.coding.producer.event.WarehouseRequestEventFactory;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessageProperties;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import jakarta.annotation.PreDestroy;
import java.nio.charset.StandardCharsets;

@Service
public class MessageProducer {

    @Autowired
    private final RabbitTemplate rabbitTemplate;
    private final ObjectMapper objectMapper;
    private final WarehouseRequestEventFactory eventFactory;
    private volatile boolean isRunning = true;

    @Value("${message.producer.delay:3000}")
    private long messageDelay;

    @Value("${rabbitmq.exchange:warehouse.exchange}")
    private String exchange;

    @Value("${rabbitmq.routing.key:some.routing.key}")
    private String routingKey;

    public MessageProducer(RabbitTemplate rabbitTemplate, ObjectMapper objectMapper) {
        this.rabbitTemplate = rabbitTemplate;
        this.objectMapper = objectMapper;
        this.eventFactory = new WarehouseRequestEventFactory();
    }

    public void produceMessages() {
        int count = 0;
        try {
            while (isRunning) {
                try {
                    WarehouseRequestEvent event = eventFactory.createEvent();
                    String jsonEvent = objectMapper.writeValueAsString(event);
                    
                    MessageProperties properties = new MessageProperties();
                    properties.setContentType(MessageProperties.CONTENT_TYPE_JSON);
                    
                    Message message = new Message(
                        jsonEvent.getBytes(StandardCharsets.UTF_8),
                        properties
                    );
                    
                    System.out.println("Attempting to send message to exchange: " + exchange 
                        + " with routing key: " + routingKey);
                    rabbitTemplate.send(exchange, routingKey, message);
                    
                    System.out.println("Successfully sent message: " + jsonEvent);
                    
                    count++;
                    
                    Thread.sleep(messageDelay);
                    
                    if(count > 20) {
                    	System.out.println("tried with 20 random inputs, if you want to check more please update count from MessageProducer service ");
                        isRunning = false;
                    }
                } catch (Exception e) {
                    System.err.println("Failed to send message: " + e.getMessage());
                    e.printStackTrace();
                    // Optionally add a delay before retrying
                    Thread.sleep(1000);
                }
            }
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            System.out.println("Message production interrupted");
        }
    }

    @PreDestroy
    public void shutdown() {
        isRunning = false;
    }

    // Method to manually stop message production if needed
    public void stopProducing() {
        isRunning = false;
    }
}