package com.dongjin.son.movierating.rabbitmq.sender;

import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
public class AmqpProducer {

	private RabbitTemplate template;
			
	@Autowired
	public AmqpProducer(RabbitTemplate template) {
		this.template = template;
	}
	
	public void sendMessage(String exchange, String routingKey, String message) {
		
		log.error(">>>>>>>>>>>> Sending message: exchange:" + exchange + ", RoutingKey:" + routingKey + ", message:" + message);		
		template.convertAndSend(exchange, routingKey, message);
	}	
	
}
