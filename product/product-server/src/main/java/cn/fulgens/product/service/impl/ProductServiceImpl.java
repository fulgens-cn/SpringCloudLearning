package cn.fulgens.product.service.impl;

import cn.fulgens.product.common.JsonResult;
import cn.fulgens.product.dto.CartDto;
import cn.fulgens.product.dto.CartItemDto;
import cn.fulgens.product.dto.ProductInfoDto;
import cn.fulgens.product.entity.ProductCategory;
import cn.fulgens.product.entity.ProductInfo;
import cn.fulgens.product.enums.ProductStatusEnum;
import cn.fulgens.product.enums.ResultEnum;
import cn.fulgens.product.exception.ProductException;
import cn.fulgens.product.mapper.ProductCategoryMapper;
import cn.fulgens.product.mapper.ProductInfoMapper;
import cn.fulgens.product.service.ProductService;
import cn.fulgens.product.vo.ProductInfoVo;
import cn.fulgens.product.vo.ProductVo;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import tk.mybatis.mapper.entity.Example;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ProductServiceImpl implements ProductService {

    @Autowired
    private ProductInfoMapper productInfoMapper;

    @Autowired
    private ProductCategoryMapper productCategoryMapper;

    @Override
    public JsonResult<List<ProductVo>> getAllProductOnShelve() {
        // 查询所有在架商品
        List<ProductInfo> productInfoList = productInfoMapper.selectByProductStatus(ProductStatusEnum.ON_SHELVE.getCode());
        // 获取在架商品分类
        List<Integer> categoryTypeList = productInfoList.stream().map(ProductInfo::getCategoryType)
                .collect(Collectors.toList());
        List<ProductCategory> productCategoryList = productCategoryMapper.selectByCategoryTypeIn(categoryTypeList);

        // 封装数据
        List<ProductVo> productVoList = new ArrayList<>();
        for (ProductCategory productCategory : productCategoryList) {
            ProductVo productVo = new ProductVo();
            productVo.setCategoryName(productCategory.getCategoryName());
            productVo.setCategoryType(productCategory.getCategoryType());

            List<ProductInfoVo> productInfoVoList = new ArrayList<>();
            for (ProductInfo productInfo : productInfoList) {
                if (productInfo.getCategoryType().equals(productCategory.getCategoryType())) {
                    ProductInfoVo productInfoVo = new ProductInfoVo();
                    BeanUtils.copyProperties(productInfo, productInfoVo);
                    productInfoVoList.add(productInfoVo);
                }
            }
            productVo.setProductInfoVoList(productInfoVoList);
            productVoList.add(productVo);
        }
        return JsonResult.ok("success", productVoList);
    }

    @Override
    public List<ProductInfoDto> getListByIdIn(List<String> productIdList) {
        Example example = new Example(ProductInfo.class);
        example.createCriteria().andIn("productId", productIdList);
        List<ProductInfo> productInfoList = productInfoMapper.selectByExample(example);
        List<ProductInfoDto> productInfoDtoList = new ArrayList<>();
        productInfoList.stream().forEach(e -> {
            ProductInfoDto productInfoDto = new ProductInfoDto();
            BeanUtils.copyProperties(e, productInfoDto);
            productInfoDtoList.add(productInfoDto);
        });
        return productInfoDtoList;
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED)
    public void decreaseStock(CartDto cartDto) throws ProductException {
        if (cartDto.getItems().size() > 0) {
            for (CartItemDto cartItemDto : cartDto.getItems()) {
                String productId = cartItemDto.getProductId();
                Integer productQuantity = cartItemDto.getProductQuantity();
                ProductInfo productInfo = productInfoMapper.selectByPrimaryKey(productId);
                if (productInfo == null) {
                    throw new ProductException(ResultEnum.PRODUCT_NOT_EXIST);
                }
                // 扣库存
                Integer result = productInfo.getProductStock() - productQuantity;
                if (result < 0) {
                    throw new ProductException(ResultEnum.PRODUCT_UNDER_STOCK);
                }
                productInfo.setProductStock(result);
                productInfoMapper.updateByPrimaryKey(productInfo);
            }
        }
    }

}
