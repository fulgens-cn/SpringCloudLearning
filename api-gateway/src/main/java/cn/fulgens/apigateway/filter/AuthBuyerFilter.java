package cn.fulgens.apigateway.filter;

import cn.fulgens.apigateway.common.utils.CookieUtil;
import cn.fulgens.apigateway.properties.AuthRequestUrlsProperties;
import com.netflix.zuul.ZuulFilter;
import com.netflix.zuul.context.RequestContext;
import com.netflix.zuul.exception.ZuulException;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;

import java.util.Iterator;
import java.util.Set;

import static org.springframework.cloud.netflix.zuul.filters.support.FilterConstants.PRE_DECORATION_FILTER_ORDER;
import static org.springframework.cloud.netflix.zuul.filters.support.FilterConstants.PRE_TYPE;

/**
 * 买家角色访问请求Filter
 *
 * @author fulgens
 */
@Component
public class AuthBuyerFilter extends ZuulFilter {

    private static final String COOKIE_KEY_OPENID = "openid";

    @Autowired
    private AuthRequestUrlsProperties authRequestUrlsProperties;

    @Override
    public String filterType() {
        return PRE_TYPE;
    }

    @Override
    public int filterOrder() {
        return PRE_DECORATION_FILTER_ORDER - 1;
    }

    @Override
    public boolean shouldFilter() {
        RequestContext requestContext = RequestContext.getCurrentContext();
        HttpServletRequest request = requestContext.getRequest();

        // 获取当前请求的url
        String requestURI = request.getRequestURI();
        // 获取需要买家角色认证才能访问的url集合
        Set<String> authRequestUrlsOfBuyer = authRequestUrlsProperties.getBuyer();
        // 遍历比对
        Iterator<String> iterator = authRequestUrlsOfBuyer.iterator();
        while(iterator.hasNext()) {
            String authRequestUrl = iterator.next();
            if (StringUtils.equals(requestURI, authRequestUrl)) {
                return true;
            }
        }

        return false;
    }

    @Override
    public Object run() throws ZuulException {
        RequestContext requestContext = RequestContext.getCurrentContext();
        HttpServletRequest request = requestContext.getRequest();

        String cookie = CookieUtil.getCookie(request, COOKIE_KEY_OPENID);
        if (StringUtils.isEmpty(cookie)) {
            requestContext.setResponseStatusCode(HttpStatus.UNAUTHORIZED.value());
            requestContext.setSendZuulResponse(false);
        }

        return null;
    }
}
