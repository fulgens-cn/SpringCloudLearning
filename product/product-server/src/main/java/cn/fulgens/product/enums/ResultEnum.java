package cn.fulgens.product.enums;

public enum  ResultEnum {

    PRODUCT_NOT_EXIST(0, "商品不存在"),
    PRODUCT_UNDER_STOCK(1, "库存不足");

    private Integer code;

    private String msg;

    ResultEnum(Integer code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }
}
