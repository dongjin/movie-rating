package com.dongjin.son.movierating.amqp;


import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;
import org.springframework.validation.annotation.Validated;

import lombok.Data;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.Value;

@Data
@ConfigurationProperties(prefix="amqp.vote")
public class AmqpProps {

    private String queue;
    private String exchange;
    private String binding;

   /*
    public String getQueue() {
        return queue;
    }
    public void setQueue(String queue) {
        this.queue = queue;
    }
    public String getExchange() {
        return exchange;
    }
    public void setExchange(String exchange) {
        this.exchange = exchange;
    }
    public String getBinding() {
        return binding;
    }
    public void setBinding(String binding) {
        this.binding = binding;
    }
    */
    
}
