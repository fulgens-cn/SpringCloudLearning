package cn.fulgens.product.exception;

import cn.fulgens.product.enums.ResultEnum;

public class ProductException extends RuntimeException {

    private Integer code;

    public ProductException(Integer code, String message) {
        super(message);
        this.code = code;
    }

    public ProductException(ResultEnum resultEnum) {
        super(resultEnum.getMsg());
        this.code = code;
    }

    public Integer getCode() {
        return code;
    }
}
