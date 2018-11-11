package cn.fulgens.order.service;

import cn.fulgens.order.dto.OrderDto;

public interface OrderService {

    /**
     * 创建订单
     * @param orderDto
     * @return
     */
    OrderDto createOrder(OrderDto orderDto) throws RuntimeException;
}
