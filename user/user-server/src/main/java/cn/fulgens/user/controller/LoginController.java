package cn.fulgens.user.controller;

import cn.fulgens.user.common.JsonResult;
import cn.fulgens.user.common.utils.CookieUtil;
import cn.fulgens.user.entity.UserInfo;
import cn.fulgens.user.enums.RoleEnum;
import cn.fulgens.user.service.UserService;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

@RestController
@RequestMapping("/login")
public class LoginController {

    private static final String COOKIE_KEY_OPENID = "openid";

    private static final Integer COOKIE_EXPIRE_TIME = 7200;

    private static final String COOKIE_KEY_TOKEN= "token";

    private static final String REDIS_KEY_TOKEN_PREFIX = "token_";

    @Autowired
    private UserService userService;

    @Autowired
    private StringRedisTemplate stringRedisTemplate;

    /**
     * 模拟买家登录
     * @param openid
     * @param response
     * @return
     */
    @GetMapping("/buyer")
    public JsonResult buyerLogin(@RequestParam String openid, HttpServletResponse response) {
        if (StringUtils.isEmpty(openid)) {
            return JsonResult.error("openid is empty", null);
        }
        UserInfo userInfo = userService.findByOpenid(openid);
        if (userInfo == null) {
            return JsonResult.error("user not exist", null);
        }
        if (userInfo.getRole() != RoleEnum.ROLE_BUYER.getCode()) {
            return JsonResult.error("role not matched", null);
        }
        CookieUtil.setCookie(response, COOKIE_KEY_OPENID, openid, COOKIE_EXPIRE_TIME);
        return JsonResult.ok("login success", null);
    }

    /**
     * 模拟卖家登录
     * @param openid
     * @param response
     * @return
     */
    @GetMapping("/seller")
    public JsonResult sellerLogin(@RequestParam String openid, HttpServletRequest request,
                                  HttpServletResponse response) {
        String tokenInCookie = CookieUtil.getCookie(request, COOKIE_KEY_TOKEN);
        String openidInRedis = stringRedisTemplate.opsForValue().get(REDIS_KEY_TOKEN_PREFIX + tokenInCookie);
        if (StringUtils.isNotEmpty(tokenInCookie) && StringUtils.isNotEmpty(openidInRedis)) {
            return JsonResult.ok("login success", null);
        }

        if (StringUtils.isEmpty(openid)) {
            return JsonResult.error("openid is empty", null);
        }
        UserInfo userInfo = userService.findByOpenid(openid);
        if (userInfo == null) {
            return JsonResult.error("user not exist", null);
        }
        if (userInfo.getRole() != RoleEnum.ROLE_SELLER.getCode()) {
            return JsonResult.error("role not matched", null);
        }

        // redis设置key="token_" + UUID, value=openid
        String token = UUID.randomUUID().toString().replaceAll("-", "");
        stringRedisTemplate.opsForValue().set(REDIS_KEY_TOKEN_PREFIX + token, openid, COOKIE_EXPIRE_TIME, TimeUnit.SECONDS);

        // cookie里设置token=UUID
        CookieUtil.setCookie(response, COOKIE_KEY_TOKEN, token, COOKIE_EXPIRE_TIME);
        return JsonResult.ok("login success", null);
    }

}
