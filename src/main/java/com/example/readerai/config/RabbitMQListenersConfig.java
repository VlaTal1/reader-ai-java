package com.example.readerai.config;

import com.example.readerai.service.TestResponseHandler;
import lombok.RequiredArgsConstructor;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.listener.SimpleMessageListenerContainer;
import org.springframework.amqp.rabbit.listener.adapter.MessageListenerAdapter;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@RequiredArgsConstructor
public class RabbitMQListenersConfig {

    private final TestResponseHandler testResponseHandler;

    private final Jackson2JsonMessageConverter jsonMessageConverter;

    @Bean
    public MessageListenerAdapter responseListenerAdapter() {
        MessageListenerAdapter adapter = new MessageListenerAdapter(testResponseHandler, "handleTestGenerationResponse");
        adapter.setMessageConverter(jsonMessageConverter);
        return adapter;
    }

    @Bean
    public SimpleMessageListenerContainer responseListenerContainer(ConnectionFactory connectionFactory) {
        SimpleMessageListenerContainer container = new SimpleMessageListenerContainer();
        container.setConnectionFactory(connectionFactory);
        container.setQueueNames(RabbitMQConfig.RESPONSE_QUEUE);
        container.setMessageListener(responseListenerAdapter());
        return container;
    }
}