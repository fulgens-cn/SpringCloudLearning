package cn.fulgens.product.service;

import cn.fulgens.product.common.JsonResult;
import cn.fulgens.product.dto.CartDto;
import cn.fulgens.product.dto.ProductInfoDto;
import cn.fulgens.product.exception.ProductException;
import cn.fulgens.product.vo.ProductVo;

import java.util.List;

public interface ProductService {

    /**
     * 获取在架商品列表
     * @return
     */
    JsonResult<List<ProductVo>> getAllProductOnShelve();

    /**
     * 根据商品id集合获取商品信息
     * @param productIdList
     * @return
     */
    List<ProductInfoDto> getListByIdIn(List<String> productIdList);

    /**
     * 扣库存
     * @param cartDto
     */
    void decreaseStock(CartDto cartDto) throws ProductException;
}
