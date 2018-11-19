package cn.fulgens.user.service;

import cn.fulgens.user.entity.UserInfo;

public interface UserService {

    UserInfo findByOpenid(String openid);

}
