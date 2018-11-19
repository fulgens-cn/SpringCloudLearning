package cn.fulgens.apigateway.common.utils;

import org.apache.commons.lang.StringUtils;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class CookieUtil {

    public static void setCookie(HttpServletResponse response, String name, String value, Integer expireTime) {
        Cookie cookie = new Cookie(name, value);
        cookie.setMaxAge(expireTime);
        response.addCookie(cookie);
    }

    public static String getCookie(HttpServletRequest request, String name) {
        if (StringUtils.isEmpty(name)) return null;

        Cookie[] cookies = request.getCookies();
        String value = null;
        if (cookies != null && cookies.length > 0) {
            for (Cookie cookie : cookies) {
                if (StringUtils.equals(name, cookie.getName())) {
                    value = cookie.getValue();
                }
            }
        }
        return value;
    }

}
