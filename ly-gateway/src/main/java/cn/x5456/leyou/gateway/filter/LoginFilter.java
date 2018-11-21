package cn.x5456.leyou.gateway.filter;

import cn.x5456.leyou.auth.entity.UserInfo;
import cn.x5456.leyou.auth.utils.JwtUtils;
import cn.x5456.leyou.common.utils.CookieUtils;
import cn.x5456.leyou.gateway.properties.FilterProperties;
import cn.x5456.leyou.gateway.properties.JwtProperties;
import com.netflix.zuul.ZuulFilter;
import com.netflix.zuul.context.RequestContext;
import com.netflix.zuul.exception.ZuulException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;

/**
 * 目的是，将一些需要登录的请求过滤掉，架构图请看README.md
 */
@Component
@EnableConfigurationProperties({JwtProperties.class, FilterProperties.class})
public class LoginFilter extends ZuulFilter {

    @Autowired
    private JwtProperties jwtProp;

    @Autowired
    private FilterProperties filterProp;


    private static final Logger logger = LoggerFactory.getLogger(LoginFilter.class);

    /**
     * pre :可以在请求被路由之前调用
     * route :在路由请求时候被调用
     * post :在route和error过滤器之后被调用
     * error :处理请求时发生错误时被调用
     */
    @Override
    public String filterType() {
        return "pre";	// 表示是前置过滤器
    }

    @Override
    public int filterOrder() {
        return 5;	// 优先级为5，数字越大，优先级越低
    }

    @Override
    public boolean shouldFilter() {	
        // 获取上下文
        RequestContext ctx = RequestContext.getCurrentContext();
        // 获取request
        HttpServletRequest req = ctx.getRequest();
        // 获取路径
        String requestURI = req.getRequestURI();
        // 判断白名单
        return !isAllowPath(requestURI);	// 是否执行该过滤器，此处为true，说明需要过滤
    }

    /**
     * 判断请求URI是不是白名单中的URI
     *
     * @param requestURI
     * @return
     */
    private Boolean isAllowPath(String requestURI) {
        boolean flag = false;

        for (String allowPath : filterProp.getAllowPaths()) {
            if (requestURI.startsWith(allowPath)) {
                //允许
                flag = true;
                break;
            }
        }
        return flag;

    }

    /** 
     * 过滤器的具体逻辑
     */
    @Override
    public Object run() throws ZuulException {
        // 获取上下文
        RequestContext ctx = RequestContext.getCurrentContext();
        // 获取request
        HttpServletRequest request = ctx.getRequest();
        // 获取token
        String token = CookieUtils.getCookieValue(request, jwtProp.getCookieName());
        // 校验
        try {
            // 校验通过什么都不做，即放行
            UserInfo userInfo = JwtUtils.getUserInfo(jwtProp.getPublicKey(), token);
            logger.info("用户{}已登录",userInfo.getUsername());
        } catch (Exception e) {
            // 校验出现异常，返回403
            ctx.setSendZuulResponse(false);
            ctx.setResponseStatusCode(403);
            logger.error("非法访问，未登录，地址：{}", request.getRemoteHost(), e );
        }
        return null;
    }
}