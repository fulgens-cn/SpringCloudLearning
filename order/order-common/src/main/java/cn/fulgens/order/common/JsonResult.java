package cn.fulgens.order.common;

import java.io.Serializable;

public class JsonResult<T> implements Serializable {

    private static final long serialVersionUID = -6754444739914048523L;

    private static final Integer SUCCESS = 0;

    private static final Integer ERROR = 1;

    private Integer code;

    private String msg;

    private T data;

    private JsonResult() {}

    private JsonResult(Integer code, String msg, T data) {
        this.code = code;
        this.msg = msg;
        this.data = data;
    }

    public static <T> JsonResult<T> ok(String msg, T data) {
        return new JsonResult<>(SUCCESS, msg, data);
    }

    public static <T> JsonResult<T> error(String msg, T data) {
        return new JsonResult<>(ERROR, msg, data);
    }

    public static <T> JsonResult<T> build(Integer code, String msg, T data) {
        return new JsonResult<>(code, msg, data);
    }

    public boolean isOk() {
        return code == SUCCESS;
    }

    public Integer getCode() {
        return code;
    }

    public String getMsg() {
        return msg;
    }

    public T getData() {
        return data;
    }
}
