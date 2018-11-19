package cn.fulgens.user.entity;

import lombok.Getter;
import lombok.Setter;

import java.util.Date;
import javax.persistence.*;

@Setter
@Getter
@Table(name = "user_info")
public class UserInfo {
    @Id
    private String id;

    private String username;

    private String password;

    /**
     * 微信openid
     */
    private String openid;

    /**
     * 1买家2卖家
     */
    private Integer role;

    /**
     * 创建时间
     */
    @Column(name = "create_time")
    private Date createTime;

    /**
     * 修改时间
     */
    @Column(name = "update_time")
    private Date updateTime;
}