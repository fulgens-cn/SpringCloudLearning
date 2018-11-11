package cn.fulgens.product.mapper;

import cn.fulgens.product.common.MyBaseMapper;
import cn.fulgens.product.entity.ProductInfo;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProductInfoMapper extends MyBaseMapper<ProductInfo> {

    List<ProductInfo> selectByProductStatus(Integer productStatus);

}