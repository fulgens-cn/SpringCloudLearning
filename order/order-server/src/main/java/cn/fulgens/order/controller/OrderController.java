package cn.fulgens.order.controller;

import cn.fulgens.order.common.JsonResult;
import cn.fulgens.order.dto.OrderDto;
import cn.fulgens.order.service.OrderService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@Slf4j
@RestController
@RequestMapping("/order")
public class OrderController {

    @Autowired
    private OrderService orderService;

    @PostMapping("/create")
    public JsonResult<Map<String, Object>> createOrder(OrderDto orderDto) {
        OrderDto result = orderService.createOrder(orderDto);
        return null;
    }

}
