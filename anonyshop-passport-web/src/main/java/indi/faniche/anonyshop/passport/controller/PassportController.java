package indi.faniche.anonyshop.passport.controller;

/* File:   PassportController.java
 * -------------------------
 * Author: faniche
 * Date:   4/30/20
 */

import com.alibaba.dubbo.config.annotation.Reference;
import com.alibaba.fastjson.JSON;
import indi.faniche.anonyshop.annotations.LoginRequired;
import indi.faniche.anonyshop.bean.user.UmsLogin;
import indi.faniche.anonyshop.service.UserService;
import indi.faniche.anonyshop.util.CookieUtil;
import indi.faniche.anonyshop.util.JwtUtil;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.Map;

@Controller
@CrossOrigin
public class PassportController {

    @Reference
    UserService userService;

    // 点击登录链接返回登录页面
    @RequestMapping(value = {"", "/"})
    public String toLogin(HttpServletRequest request, Model model) {
        String username = (String) request.getAttribute("username");
        String userId = (String) request.getAttribute("userId");
        model.addAttribute("username", username);
        model.addAttribute("userId", userId);
        model.addAttribute("returnUrl", request.getHeader("Referer"));
        return "passport/login";
    }

    /* 登录验证 */
    @RequestMapping("login")
    @ResponseBody
    public String authenticate(UmsLogin umsLogin, HttpServletRequest request) {
        String token = "";
        String username = umsLogin.getUsername();
        if (username.contains("@")) {
            umsLogin.setEmail(username);
            umsLogin.setUsername("");
        }
//        Map<String, String> ret = new HashMap<>();
        UmsLogin login = userService.login(umsLogin);
        if (login != null) {
            // 登录成功
            Map<String, Object> userMap = new HashMap<>();
            String userId = login.getId();
            userMap.put("userId", login.getId());
            userMap.put("username", login.getUsername());
            userMap.put("roleId", login.getRoleId());
            String remoteAddr = request.getHeader("s-forward-for");
            if (StringUtils.isBlank(remoteAddr)) {
                remoteAddr = request.getRemoteAddr();
                if (StringUtils.isBlank(remoteAddr)) {
                    remoteAddr = "127.0.0.1";
                }
            }
            // 按照设计的算法对参数加密后得到token
            token = JwtUtil.encode("2020anonyshop", userMap, remoteAddr);
            userService.addToken(token, userId);
            return JSON.toJSONString(token);
        } else {
            return JSON.toJSONString("fail");
        }
    }

    @RequestMapping("verify")
    @ResponseBody
    public String verify(String token, String currentIp) {
        // 通过jwt校验token真假
        Map<String, String> map = new HashMap<>();
        Map<String, Object> decode = JwtUtil.decode(token, "2020anonyshop", currentIp);
        if (decode != null) {
            map.put("status", "success");
            map.put("userId", (String) decode.get("userId"));
            map.put("username", (String) decode.get("username"));
            map.put("roleId", (String) decode.get("roleId"));
        } else {
            map.put("status", "fail");
        }
        return JSON.toJSONString(map);
    }

    @RequestMapping("logout")
    @LoginRequired(loginSuccess = false)
    public String logout(HttpServletRequest request, HttpServletResponse response) {
        String userId = (String) request.getAttribute("userId");
        CookieUtil.deleteCookie(request, response, "oldToken");
        userService.deleteToken(userId);
        return "redirect:http://anonyshop.tech";
    }


}
