package com.dongjin.son.movierating.rabbitmq.receiver;

import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
public class AnnotatedConsumer {

	
	@RabbitListener(queues="${amqp.dongjin.queue.topic}")
	public void process(String message){
		
		log.error("<<<<<<<<<<<<  Received Message (Annotated Topic Consumer):" + message);
		
	}
}
