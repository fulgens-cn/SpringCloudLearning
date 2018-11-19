package cn.fulgens.user.enums;

public enum RoleEnum {
    ROLE_BUYER(1, "买家"),
    ROLE_SELLER(2, "卖家");

    private Integer code;

    private String roleName;

    RoleEnum(Integer code, String roleName) {
        this.code = code;
        this.roleName = roleName;
    }

    public Integer getCode() {
        return code;
    }

    public String getRoleName() {
        return roleName;
    }
}
