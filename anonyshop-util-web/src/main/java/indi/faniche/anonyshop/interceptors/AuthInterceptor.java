package indi.faniche.anonyshop.interceptors;

/* File:   AuthInterceptor.java
 * -------------------------
 * Author: faniche
 * Date:   4/30/20
 */

import com.alibaba.fastjson.JSON;
import indi.faniche.anonyshop.annotations.LoginRequired;
import indi.faniche.anonyshop.tuil.HttpclientUtil;
import indi.faniche.anonyshop.util.CookieUtil;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.Map;

@Component
public class AuthInterceptor extends HandlerInterceptorAdapter {
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        if (!(handler instanceof HandlerMethod)) return true;
        // 拦截代码
        // 判断被拦截的请求的访问的方法的注解(是否时需要拦截的)
        HandlerMethod handlerMethod = (HandlerMethod) handler;
        LoginRequired methodAnnotation = handlerMethod.getMethodAnnotation(LoginRequired.class);
        StringBuffer url = request.getRequestURL();
        // 该方法是否需要被拦截, 如果注解的括号里没有内容，默认放行
        if (methodAnnotation == null) return true;

        // 否则，开始验证
        // 更新token
        String token = "";
        String oldToken = CookieUtil.getCookieValue(request, "oldToken", true);
        if (StringUtils.isNotBlank(oldToken))  token = oldToken;
        String newToken = request.getParameter("token");
        if (StringUtils.isNotBlank(newToken)) token = newToken;

        // 该方法是否必须登录才能使用
        boolean loginSuccess = methodAnnotation.loginSuccess();

        // 调用认证中心进行验证
        String success = "fail";
        Map<String, String> successMap = new HashMap<>();
        if (StringUtils.isNotBlank(token)) {
            String ip = request.getHeader("x-forwarded-for");// 通过nginx转发的客户端ip
            if (StringUtils.isBlank(ip)) {
                ip = request.getRemoteAddr();// 从request中获取ip
                if (StringUtils.isBlank(ip)) {
                    ip = "127.0.0.1";
                }
            }
            String successJson = HttpclientUtil.doGet("http://passport.anonyshop.tech/verify?token=" + token + "&currentIp=" + ip);
            successMap = JSON.parseObject(successJson, Map.class);
            success = successMap.get("status");
        }

        if (loginSuccess) {
            // 必须登录成功才能使用
            if (!success.equals("success")) {
                //重定向会passport登录
                StringBuffer requestURL = request.getRequestURL();
                response.sendRedirect("http://passport.anonyshop.tech/?ReturnUrl=" + requestURL);
                return false;
            }
            // 需要将token携带的用户信息写入
            request.setAttribute("userId", successMap.get("userId"));
            request.setAttribute("username", successMap.get("username"));
            request.setAttribute("roleId", successMap.get("roleId"));
            //验证通过，覆盖cookie中的token
            if (StringUtils.isNotBlank(token)) {
                CookieUtil.setCookie(request, response, "oldToken", token, 60 * 60 * 2, true);
            }
        } else {
            // 没有登录也能用，但是必须验证
            if (success.equals("success")) {
                // 需要将token携带的用户信息写入
                request.setAttribute("userId", successMap.get("userId"));
                request.setAttribute("username", successMap.get("username"));
                request.setAttribute("roleId", successMap.get("roleId"));
                //验证通过，覆盖cookie中的token
                if (StringUtils.isNotBlank(token)) {
                    CookieUtil.setCookie(request, response, "oldToken", token, 60 * 60 * 2, true);
                }
            }
        }
        return true;
    }
}
