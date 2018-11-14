package cn.fulgens.order.controller;

import cn.fulgens.order.dto.OrderDto;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class MsgController {

    @Autowired
    private AmqpTemplate amqpTemplate;

    @GetMapping("/sendMsg")
    public void sendMsg() {
        OrderDto orderDto = new OrderDto();
        orderDto.setOrderId("aaaa");
        amqpTemplate.convertAndSend("myExchange", "key2", orderDto);
    }

}
