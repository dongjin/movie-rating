package com.dongjin.son.movierating.amqp;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
//@EnableConfigurationProperties(AmqpProps.class)
public class AmqpTest {
    
    @Autowired
    AmqpProps amqpProp;
    
    @Value("${amqp.dongjin.queue}")
    String queue;
    
    
    public void test() {
        System.out.println("properties:" + amqpProp.getQueue());        
        System.out.println("queue:" + queue);
    }
    
}
