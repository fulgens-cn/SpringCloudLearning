package cn.fulgens.user.service.impl;

import cn.fulgens.user.entity.UserInfo;
import cn.fulgens.user.mapper.UserInfoMapper;
import cn.fulgens.user.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tk.mybatis.mapper.entity.Example;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserInfoMapper userInfoMapper;


    @Override
    public UserInfo findByOpenid(String openid) {
        Example example = new Example(UserInfo.class);
        example.createCriteria().andEqualTo("openid", openid);

        return userInfoMapper.selectOneByExample(example);
    }
}
