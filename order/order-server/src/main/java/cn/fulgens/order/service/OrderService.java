package cn.fulgens.order.service;

import cn.fulgens.order.dto.OrderDto;

public interface OrderService {

    /**
     * 创建订单
     * @param orderDto
     * @return
     */
    OrderDto createOrder(OrderDto orderDto) throws RuntimeException;

    /**
     * 完结订单
     * @param orderId
     * @return
     * @throws RuntimeException
     */
    OrderDto finishOrder(String orderId) throws RuntimeException;
}
