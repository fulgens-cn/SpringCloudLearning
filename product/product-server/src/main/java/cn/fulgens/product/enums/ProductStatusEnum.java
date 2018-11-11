package cn.fulgens.product.enums;

public enum ProductStatusEnum {

    ON_SHELVE(0, "在架"),
    OFF_SHELVE(1, "下架");

    private Integer code;

    private String msg;

    ProductStatusEnum(Integer code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    public Integer getCode() {
        return code;
    }

    public String getMsg() {
        return msg;
    }
}
