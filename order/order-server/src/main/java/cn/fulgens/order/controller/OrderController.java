package cn.fulgens.order.controller;

import cn.fulgens.order.common.JsonResult;
import cn.fulgens.order.dto.OrderDto;
import cn.fulgens.order.service.OrderService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@Slf4j
@RestController
@RequestMapping("/order")
public class OrderController {

    @Autowired
    private OrderService orderService;

    /**
     * 创建订单
     * @param orderDto
     * @return
     */
    @PostMapping("/create")
    public JsonResult<OrderDto> createOrder(@RequestBody OrderDto orderDto) {
        OrderDto result = orderService.createOrder(orderDto);
        return JsonResult.ok("创建订单成功", result);
    }

    /**
     * 完结订单（卖家访问）
     * @param orderId
     * @return
     */
    @PostMapping("/finish")
    public JsonResult<OrderDto> finishOrder(@RequestParam String orderId) {
        OrderDto result = orderService.finishOrder(orderId);
        return JsonResult.ok("完结订单成功", result);
    }

}
