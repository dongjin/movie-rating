package com.dongjin.son.movierating.amqp;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableConfigurationProperties(AmqpProps.class)
public class AmqpConfigTest {

     @Autowired
     AmqpProps amqpProp;
     
     
     
     
    

    void test() {
        System.out.println(amqpProp.getBinding());
    }
    
}
