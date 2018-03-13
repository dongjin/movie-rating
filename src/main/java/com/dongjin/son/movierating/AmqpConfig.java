package com.dongjin.son.movierating;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;


/**
 * Configure Queue, TopicExchange, Binding
 *   
 */


@Configuration
public class AmqpConfig {

    // Rabbit MQ
    @Value("${amqp.dongjin.exchange.topic}") 
    String exchangeTopic;
    
    @Value("${amqp.dongjin.queue.topic}") 
    String queueTopic;
    
    
    @Bean
    Queue queue() {
        return  new Queue(queueTopic, false);
    }

    @Bean
    TopicExchange exchange() {
        return new TopicExchange(exchangeTopic);
    }

    @Bean
    Binding binding(Queue queue, TopicExchange exchange) {

        // with(--> routing key)
        return BindingBuilder.bind(queue).to(exchange).with(queueTopic);
    }

    /*
    @Bean
    SimpleMessageListenerContainer container(ConnectionFactory connectionFactory,
            MessageListenerAdapter listenerAdapter) {
        SimpleMessageListenerContainer container = new SimpleMessageListenerContainer();
        container.setConnectionFactory(connectionFactory);
        container.setQueueNames(queueName);
        container.setMessageListener(listenerAdapter);
        return container;
    }

    @Bean
    MessageListenerAdapter listenerAdapter(Receiver receiver) {
        return new MessageListenerAdapter(receiver, "receiveMessage");
    }
    */
}
