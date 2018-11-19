package cn.fulgens.apigateway.filter;

import cn.fulgens.apigateway.common.utils.CookieUtil;
import cn.fulgens.apigateway.properties.AuthRequestUrlsProperties;
import com.netflix.zuul.ZuulFilter;
import com.netflix.zuul.context.RequestContext;
import com.netflix.zuul.exception.ZuulException;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import java.util.Iterator;
import java.util.Set;

import static org.springframework.cloud.netflix.zuul.filters.support.FilterConstants.PRE_DECORATION_FILTER_ORDER;
import static org.springframework.cloud.netflix.zuul.filters.support.FilterConstants.PRE_TYPE;

/**
 * 卖家角色访问请求Filter
 *
 * @author fulgens
 */
@Slf4j
@Component
public class AuthSellerFilter extends ZuulFilter {

    private static final String COOKIE_KEY_TOKEN= "token";

    private static final String REDIS_KEY_TOKEN_PREFIX = "token_";

    @Autowired
    private AuthRequestUrlsProperties authRequestUrlsProperties;

    @Autowired
    private StringRedisTemplate stringRedisTemplate;

    @Override
    public String filterType() {
        return PRE_TYPE;
    }

    @Override
    public int filterOrder() {
        return PRE_DECORATION_FILTER_ORDER - 2;
    }

    @Override
    public boolean shouldFilter() {
        RequestContext requestContext = RequestContext.getCurrentContext();
        HttpServletRequest request = requestContext.getRequest();

        // 获取当前请求的url
        String requestURI = request.getRequestURI();
        log.info("用户当前请求url: {}", requestURI);
        // 获取需要卖家角色认证才能访问的url集合
        Set<String> authRequestUrlsOfSeller = authRequestUrlsProperties.getSeller();
        // 遍历比对
        Iterator<String> iterator = authRequestUrlsOfSeller.iterator();
        while(iterator.hasNext()) {
            String authRequestUrl = iterator.next();
            if (StringUtils.equals(requestURI, authRequestUrl)) {
                log.info("用户当前请求url需要买家角色认证");
                return true;
            }
        }

        return false;
    }

    @Override
    public Object run() throws ZuulException {
        RequestContext requestContext = RequestContext.getCurrentContext();
        HttpServletRequest request = requestContext.getRequest();

        String tokenInCookie = CookieUtil.getCookie(request, COOKIE_KEY_TOKEN);
        String openidInRedis = stringRedisTemplate.opsForValue().get(REDIS_KEY_TOKEN_PREFIX + tokenInCookie);
        if (StringUtils.isNotEmpty(tokenInCookie) || StringUtils.isNotEmpty(openidInRedis)) {
            requestContext.setResponseStatusCode(HttpStatus.UNAUTHORIZED.value());
            requestContext.setSendZuulResponse(false);
        }

        return null;
    }
}
