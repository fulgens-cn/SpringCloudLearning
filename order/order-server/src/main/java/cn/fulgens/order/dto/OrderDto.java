package cn.fulgens.order.dto;

import cn.fulgens.order.entity.OrderDetail;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.List;

@Setter
@Getter
public class OrderDto {

    private String orderId;

    /**
     * 买家名字
     */
    private String buyerName;

    /**
     * 买家电话
     */
    private String buyerPhone;

    /**
     * 买家地址
     */
    private String buyerAddress;

    /**
     * 买家微信openid
     */
    private String buyerOpenid;

    /**
     * 订单总金额
     */
    private BigDecimal orderAmount;

    /**
     * 订单状态, 默认为新下单
     */
    private Byte orderStatus;

    /**
     * 支付状态, 默认未支付
     */
    private Byte payStatus;

    /**
     * 订单项集合
     */
    private List<OrderDetail> orderDetailList;
}
