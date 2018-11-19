package cn.fulgens.order.service.impl;

import cn.fulgens.order.dto.OrderDto;
import cn.fulgens.order.entity.OrderDetail;
import cn.fulgens.order.entity.OrderMaster;
import cn.fulgens.order.enums.OrderStatusEnum;
import cn.fulgens.order.enums.PayStatusEnum;
import cn.fulgens.order.mapper.OrderDetailMapper;
import cn.fulgens.order.mapper.OrderMasterMapper;
import cn.fulgens.order.service.OrderService;
import cn.fulgens.product.client.ProductFeignClient;
import cn.fulgens.product.common.JsonResult;
import cn.fulgens.product.dto.CartDto;
import cn.fulgens.product.dto.CartItemDto;
import cn.fulgens.product.dto.ProductInfoDto;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import tk.mybatis.mapper.entity.Example;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Slf4j
@Service
public class OrderServiceImpl implements OrderService {

    @Autowired
    private OrderMasterMapper orderMasterMapper;

    @Autowired
    private OrderDetailMapper orderDetailMapper;

    @Autowired
    private ProductFeignClient productFeignClient;

    @Autowired
    private ObjectMapper objectMapper;

    @Override
    @Transactional(propagation = Propagation.REQUIRED)
    public OrderDto createOrder(OrderDto orderDto) throws RuntimeException {

        String orderId = getRandomUUID();

        // 调用商品服务获取商品信息
        List<String> productIdList = orderDto.getOrderDetailList().stream()
                .map(OrderDetail::getProductId)
                .collect(Collectors.toList());
        log.info("调用商品服务获取商品信息开始，入参：{}", productIdList);
        JsonResult<List<ProductInfoDto>> productInfoResult = productFeignClient.listForOrder(productIdList);
        log.info("调用商品服务获取商品信息结束，返回值：{}", writeJsonValue(objectMapper, productInfoResult));
        if (!productInfoResult.isOk()) {
            throw new RuntimeException("调用商品服务获取商品信息异常");
        }
        List<ProductInfoDto> productInfoDtoList = productInfoResult.getData();
        // 计算总价
        log.info("开始计算订单总价...");
        BigDecimal orderTotalPrice = BigDecimal.ZERO;
        for (OrderDetail orderDetail : orderDto.getOrderDetailList()) {
            for (ProductInfoDto productInfo : productInfoDtoList) {
                if (orderDetail.getProductId().equals(productInfo.getProductId())) {
                    orderTotalPrice = productInfo.getProductPrice()
                            .multiply(new BigDecimal(orderDetail.getProductQuantity()))
                            .add(orderTotalPrice);
                    // 订单项入库
                    BeanUtils.copyProperties(productInfo, orderDetail);
                    String detailId = getRandomUUID();
                    orderDetail.setDetailId(detailId);
                    orderDetail.setOrderId(orderId);
                    orderDetail.setCreateTime(new Date());
                    orderDetail.setUpdateTime(new Date());
                    log.info("id：{}的订单项入库开始，详细信息：{}", orderDetail.getProductId(), orderDetail);
                    orderDetailMapper.insert(orderDetail);
                    log.info("id：{}的订单项入库结束", orderDetail.getDetailId());
                }
            }
        }
        log.info("订单总价计算完成，总价: {}", orderTotalPrice);

        // 调用商品服务扣库存
        CartDto cartDto = new CartDto();
        List<CartItemDto> items = orderDto.getOrderDetailList().stream()
                .map(e -> new CartItemDto(e.getProductId(), e.getProductQuantity()))
                .collect(Collectors.toList());
        cartDto.setItems(items);
        log.info("调用商品服务扣库存开始，入参：{}", writeJsonValue(objectMapper, cartDto));
        JsonResult jsonResult = productFeignClient.decreaseStock(cartDto);
        log.info("调用商品服务扣库存结束，返回值：{}", writeJsonValue(objectMapper, jsonResult));
        if (!jsonResult.isOk()) {
            throw new RuntimeException("调用商品服务扣库存异常");
        }

        // 订单入库
        OrderMaster orderMaster = new OrderMaster();
        orderDto.setOrderId(orderId);
        orderDto.setOrderAmount(orderTotalPrice);
        orderDto.setOrderStatus(OrderStatusEnum.NEW.getCode());
        orderDto.setPayStatus(PayStatusEnum.WAIT.getCode());

        BeanUtils.copyProperties(orderDto, orderMaster);

        orderMaster.setCreateTime(new Date());
        orderMaster.setUpdateTime(new Date());
        log.info("订单入库开始，详细信息：{}", writeJsonValue(objectMapper, orderMaster));
        orderMasterMapper.insert(orderMaster);
        log.info("订单入库结束...");

        return orderDto;
    }

    @Override
    @Transactional
    public OrderDto finishOrder(String orderId) throws RuntimeException {
        if (StringUtils.isEmpty(orderId)) {
            throw new RuntimeException("订单号为空");
        }

        OrderMaster orderMaster = orderMasterMapper.selectByPrimaryKey(orderId);
        if (orderMaster == null) {
            throw new RuntimeException("订单不存在");
        }
        if (orderMaster.getOrderStatus() != OrderStatusEnum.NEW.getCode()) {
            throw new RuntimeException("订单状态不正确");
        }
        // 更新订单状态为完结
        orderMaster.setOrderStatus(OrderStatusEnum.FINISHED.getCode());
        orderMasterMapper.updateByPrimaryKeySelective(orderMaster);

        OrderDto orderDto = new OrderDto();
        BeanUtils.copyProperties(orderMaster, orderDto);
        Example example = new Example(OrderDetail.class);
        example.createCriteria().andEqualTo("orderId", orderMaster.getOrderId());
        List<OrderDetail> orderDetailList = orderDetailMapper.selectByExample(example);
        orderDto.setOrderDetailList(orderDetailList);

        return orderDto;
    }

    private synchronized String getRandomUUID() {
        return UUID.randomUUID().toString().replaceAll("-", "");
    }

    private String writeJsonValue(ObjectMapper objectMapper, Object object) {
        String jsonStr = null;
        try {
            jsonStr = objectMapper.writeValueAsString(object);
        } catch (JsonProcessingException e) {
            log.error("json序列化发生异常...");
        }
        return jsonStr;
    }
}
