package cn.fulgens.apigateway.filter;

import com.netflix.zuul.ZuulFilter;
import com.netflix.zuul.context.RequestContext;
import com.netflix.zuul.exception.ZuulException;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import java.io.IOException;

import static org.springframework.cloud.netflix.zuul.filters.support.FilterConstants.PRE_DECORATION_FILTER_ORDER;
import static org.springframework.cloud.netflix.zuul.filters.support.FilterConstants.PRE_TYPE;

/**
 * 自定义Token校验Filter
 *
 * @author fulgens
 */
@Slf4j
//@Component
public class TokenFilter extends ZuulFilter {

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
        return true;
    }

    @Override
    public Object run() throws ZuulException {
        RequestContext requestContext = RequestContext.getCurrentContext();
        HttpServletRequest request = requestContext.getRequest();
        HttpServletResponse response = requestContext.getResponse();
        String token = request.getParameter("token");
        if (StringUtils.isEmpty(token)) {
            log.error("token为空...");
            requestContext.setSendZuulResponse(false);
            requestContext.setResponseStatusCode(HttpStatus.UNAUTHORIZED.value());

            try {
                writeResponse(response, "unauthorized request!");
            } catch (IOException e) {
                log.error("回写响应信息发生异常，异常信息为：{}", e);
            }
        }
        return null;
    }

    private void writeResponse(HttpServletResponse response, String responseStr) throws IOException {
        response.setContentType(MediaType.APPLICATION_JSON_UTF8.toString());
        response.getWriter().write(responseStr);
        response.getWriter().close();
    }
}
