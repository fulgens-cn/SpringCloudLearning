package cn.fulgens.product.client.fallback;

import cn.fulgens.product.client.ProductFeignClient;
import cn.fulgens.product.common.JsonResult;
import cn.fulgens.product.dto.CartDto;
import cn.fulgens.product.dto.ProductInfoDto;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * hystrix服务熔断{@link ProductFeignClient}fallback
 *
 * @author fulgens
 */
@Component
public class ProductFeignClientFallback implements ProductFeignClient {
    @Override
    public JsonResult<List<ProductInfoDto>> listForOrder(List<String> productIdList) {
        return JsonResult.error("根据商品id获取商品信息服务当前不可用，请稍后重试！", null);
    }

    @Override
    public JsonResult decreaseStock(CartDto cartDto) {
        return JsonResult.error("商品减库存服务当前不可用，请稍后重试！", null);
    }
}
