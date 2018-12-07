package cn.fulgens.product.client;

import cn.fulgens.product.client.fallback.ProductFeignClientFallback;
import cn.fulgens.product.common.JsonResult;
import cn.fulgens.product.dto.CartDto;
import cn.fulgens.product.dto.ProductInfoDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;

/**
 * product服务feign client（结合使用了hystrix服务熔断）
 *
 * @author fulgens
 */
@FeignClient(name = "product", path = "/product", decode404 = true, fallback = ProductFeignClientFallback.class)
public interface ProductFeignClient {

    @PostMapping("/listForOrder")
    JsonResult<List<ProductInfoDto>> listForOrder(@RequestBody List<String> productIdList);

    @PostMapping("/decreaseStock")
    JsonResult decreaseStock(@RequestBody CartDto cartDto);

}
