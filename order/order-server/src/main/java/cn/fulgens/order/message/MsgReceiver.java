package cn.fulgens.order.message;


import cn.fulgens.order.dto.OrderDto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.*;
import org.springframework.stereotype.Component;

@Slf4j
@Component
//@RabbitListener(queues = "myQueue")
//@RabbitListener(queuesToDeclare = @Queue("myQueue"))
public class MsgReceiver {

    @RabbitListener(bindings = @QueueBinding(
            value = @Queue("queue1"),
            exchange = @Exchange("myExchange"),
            key = "key1"
    ))
//    @RabbitHandler
    public void handleGetMsg(String msg) {
        log.info("MsgReceiver receive msg: {}", msg);
    }

    @RabbitListener(bindings = @QueueBinding(
            value = @Queue("queue2"),
            exchange = @Exchange("myExchange"),
            key = "key2"
    ))
    @RabbitHandler
    public void handle(OrderDto msg) {
        log.info("receive msg: {}", msg);
    }

}
