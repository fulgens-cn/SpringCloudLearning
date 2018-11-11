package cn.fulgens.product.mapper;

import cn.fulgens.product.common.MyBaseMapper;
import cn.fulgens.product.entity.ProductCategory;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProductCategoryMapper extends MyBaseMapper<ProductCategory> {

    List<ProductCategory> selectByCategoryTypeIn(@Param("categoryTypeList") List<Integer> categoryTypeList);

}