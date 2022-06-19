package com.springnetflix.products.api.message;

import com.springnetflix.products.api.data.vo.ProductVO;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class ProductSendMessage {

    @Value("${productsapi.rabbitmq.exchange}")
    String exchange;

    @Value("${productsapi.rabbitmq.routingkey}")
    String routingkey;

    public final RabbitTemplate rabbitTemplate;

    @Autowired
    public ProductSendMessage(RabbitTemplate rabbitTemplate) {
        this.rabbitTemplate = rabbitTemplate;
    }

    public void sendMessage(ProductVO productVO) {
        rabbitTemplate.convertAndSend(exchange, routingkey, productVO);
    }

}
