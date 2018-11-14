package cn.fulgens.product.controller;

import cn.fulgens.product.common.JsonResult;
import cn.fulgens.product.dto.CartDto;
import cn.fulgens.product.dto.ProductInfoDto;
import cn.fulgens.product.entity.ProductInfo;
import cn.fulgens.product.properties.GirlProperties;
import cn.fulgens.product.service.ProductService;
import cn.fulgens.product.vo.ProductVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/product")
@RefreshScope
public class ProductController {

    @Autowired
    private ProductService productService;

    @GetMapping("/list")
    public JsonResult<List<ProductVo>> getAllProductOnShelve() {
        return productService.getAllProductOnShelve();
    }

    @PostMapping("/listForOrder")
    public JsonResult<List<ProductInfoDto>> listForOrder(@RequestBody List<String> productIdList) {
        if (productIdList == null || productIdList.size() == 0) {
            return JsonResult.error("商品id集合不能为空！", null);
        }
        List<ProductInfoDto> productInfoList = productService.getListByIdIn(productIdList);
        return JsonResult.ok("success", productInfoList);
    }

    @PostMapping("/decreaseStock")
    public JsonResult decreaseStock(@RequestBody CartDto cartDto) {
        if (cartDto == null || cartDto.getItems() == null || cartDto.getItems().size() == 0) {
            return JsonResult.error("扣除商品库存入参不正确！", null);
        }
        productService.decreaseStock(cartDto);
        return JsonResult.ok("success", null);
    }

    @Value("${girl.name}")
    private String girlName;

    @Autowired
    private GirlProperties girlProperties;

    @GetMapping("/name")
    public String printName() {
        return girlProperties.getName() + ":" + girlProperties.getAge();
    }

}
