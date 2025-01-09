package com.bosch.coding.consumer.config;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessageListener;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.amqp.rabbit.annotation.EnableRabbit;
import org.springframework.amqp.rabbit.connection.CachingConnectionFactory;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.listener.MessageListenerContainer;
import org.springframework.amqp.rabbit.listener.SimpleMessageListenerContainer;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import com.bosch.coding.consumer.model.WarehouseRequestEvent;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.DeserializationFeature;

@Configuration
@EnableRabbit
public class RabbitMQConfig {

    @Value("${rabbitmq.host}")
    private String host;

    @Value("${rabbitmq.username}")
    private String username;

    @Value("${rabbitmq.password}")
    private String password;

    @Bean
    public ObjectMapper objectMapper() {
        ObjectMapper mapper = new ObjectMapper();
        mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        return mapper;
    }

    @Bean
    public ConnectionFactory connectionFactory() {
        CachingConnectionFactory connectionFactory = new CachingConnectionFactory(host);
        connectionFactory.setUsername(username);
        connectionFactory.setPassword(password);
        return connectionFactory;
    }

    @Bean
    public Queue queue() {
        return new Queue("warehouse.queue", true);
    }

    @Bean
    public MessageConverter jsonMessageConverter() {
        return new Jackson2JsonMessageConverter(objectMapper());
    }

    @Bean
    public TopicExchange exchange() {
        return new TopicExchange("warehouse.exchange", true, false);
    }

    @Bean
    public Binding binding() {
        return BindingBuilder.bind(queue())
                           .to(exchange())
                           .with("some.routing.key");
    }

    @Bean
    public MessageListenerContainer messageListenerContainer() {
        SimpleMessageListenerContainer container = new SimpleMessageListenerContainer();
        container.setConnectionFactory(connectionFactory());
        container.setQueueNames("warehouse.queue");
        
        MessageConverter converter = jsonMessageConverter();
        
        container.setMessageListener(new MessageListener() {
            @Override
            public void onMessage(Message message) {
                try {
                    // Convert the message using our JSON converter
                    Object converted = converter.fromMessage(message);
                    
                    if (converted instanceof WarehouseRequestEvent) {
                        WarehouseRequestEvent event = (WarehouseRequestEvent) converted;
                        System.out.println("Received event: " + event);
                        // Add your business logic here
                    } else {
                        System.out.println("Unexpected message type: " + converted.getClass());
                        System.out.println("Message content: " + new String(message.getBody()));
                    }
                } catch (Exception e) {
                    System.err.println("Error processing message: " + e.getMessage());
                    System.err.println("Message content: " + new String(message.getBody()));
                    e.printStackTrace();
                }
            }
        });
        
        return container;
    }
}