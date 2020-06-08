package indi.faniche.anonyshop.manage.controller;

/* File:   UserController.java
 * -------------------------
 * Author: faniche
 * Date:   5/9/20
 */

import com.alibaba.dubbo.config.annotation.Reference;
import com.alibaba.fastjson.JSON;
import indi.faniche.anonyshop.annotations.LoginRequired;
import indi.faniche.anonyshop.bean.address.AddrCity;
import indi.faniche.anonyshop.bean.address.AddrCounty;
import indi.faniche.anonyshop.bean.address.AddrProvince;
import indi.faniche.anonyshop.bean.store.SmsStore;
import indi.faniche.anonyshop.bean.user.UmsDefaultAddress;
import indi.faniche.anonyshop.bean.user.UmsInfo;
import indi.faniche.anonyshop.bean.user.UmsLogin;
import indi.faniche.anonyshop.modol.Msg;
import indi.faniche.anonyshop.service.*;
import indi.faniche.anonyshop.util.CookieUtil;
import indi.faniche.anonyshop.util.JwtUtil;
import indi.faniche.anonyshop.util.MailUtil;
import org.apache.commons.lang3.StringUtils;
import org.springframework.http.HttpRequest;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import javax.mail.MessagingException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@CrossOrigin
public class UserController {

    @Reference
    UserService userService;

    @Reference
    UmsInfoService umsInfoService;

    @Reference
    CascadeAddressService cascadeAddressService;

    @Reference
    UmsDefaultAddressService umsDefaultAddressService;

    @Reference
    StoreService storeService;
    
    //========================================================manage====================================================
    @RequestMapping("user/login")
    public String login(HttpServletRequest request, HttpServletResponse response, HttpSession session, UmsLogin umsLogin, Model model) {
        String username = umsLogin.getUsername();
        if (username.contains("@")) {
            umsLogin.setEmail(username);
            umsLogin.setUsername("");
        }
        UmsLogin login = userService.login(umsLogin);
        if (login != null) {
            updateToken(login, request, response);
            model.addAttribute("userId", login.getId());
            model.addAttribute("roleId", login.getRoleId());
            return "redirect:http://manage.anonyshop.tech/profile";
        }
        model.addAttribute("msg", "用户名或密码错误");
        return "admin_seller/login";
    }

    @RequestMapping("seller/registry")
    public String toRegistry() {
        return "seller/register";
    }

    @RequestMapping("seller/registrySubmit")
    @ResponseBody
    public String registrySubmit(UmsLogin login, Model model) {
        login.setRoleId("2");
        login = userService.addUser(login);
        if (login.getId() != null) {
            model.addAttribute("userId", login.getId());
            model.addAttribute("username", login.getUsername());
            MailUtil.setReceiver(login.getEmail());
            MailUtil.setSubject("Anonyshop 账户激活");
            String content = "<a href='http://manage.anonyshop.tech/user/confirm?userId=" + login.getId() + "'>点击这里的网址激活账户</a>";
            MailUtil.setContent(content);
            try {
                MailUtil.sendMail();
            } catch (MessagingException e) {
                e.printStackTrace();
            }
            return JSON.toJSONString("注册成功，确认邮件以发送至邮箱，请尽快确认以激活账户");
        } else {
            return JSON.toJSONString("用户名已注册过");
        }
    }

    @RequestMapping("seller/confirm")
    public String toConfirm(String userId, Model model, HttpServletRequest request) {
        userService.activateAccount(userId);
        String username = (String) request.getAttribute("username");

        model.addAttribute("username", username);
        model.addAttribute("userId", userId);

        Msg msg = new Msg();
        msg.setTitle("激活账户");
        msg.setContent("账户已成功激活");
        model.addAttribute("msg", msg);
        return "admin_seller/login";
    }

    @RequestMapping(value = {"", "/", "profile"})
    @LoginRequired(loginSuccess = false)
    public String toLogin(HttpServletRequest request, HttpServletResponse response, Model model) {
        String username = (String) request.getAttribute("username");
        if (username != null) {
            String userId = (String) request.getAttribute("userId");
            String roleId = (String) request.getAttribute("roleId");
            UmsLogin umsLogin = userService.getUserByUsername(username);
            UmsDefaultAddress defaultAddress = getUmsDefaultAddressFromCookie(request, response, userId);
            List<AddrProvince> provinceList = getProvinceList(request, response);
            UmsInfo umsInfo = umsInfoService.getUserInfo(userId);
            model.addAttribute("username", username);
            model.addAttribute("userId", userId);
            model.addAttribute("roleId", roleId);
            model.addAttribute("defaultAddress", defaultAddress);
            model.addAttribute("provinceList", provinceList);
            model.addAttribute("umsLogin", umsLogin);
            model.addAttribute("umsInfo", umsInfo);
            // session.setAttribute("umsInfo", umsInfo);
            // session.setAttribute("manager", umsLogin);
            if (roleId.equals("2")) {
                SmsStore store = null;
                        String storeStr = CookieUtil.getCookieValue(request, "store", true);
                if (StringUtils.isNotBlank(storeStr)) {
                    store = JSON.parseObject(storeStr, SmsStore.class);
                } else {
                    store = storeService.getStore(userId);
                    CookieUtil.setCookie(request, response, "store", JSON.toJSONString(store), 60 * 60 * 72, true);
                }
                model.addAttribute("store", store);
            }
            return "common/profile";
        }
        return "admin_seller/login";
    }

//===================================================common=============================================================
//    @RequestMapping("user/profile")
//    @LoginRequired
//    public String toProfile(HttpServletRequest request, HttpServletResponse response, Model model) {
//        String username = (String) request.getAttribute("username");
//        String userId = (String) request.getAttribute("userId");
//        String roleId = (String) request.getAttribute("roleId");
//        UmsLogin umsLogin = userService.getUserByUsername(username);
//
//        UmsDefaultAddress defaultAddress = getUmsDefaultAddressFromCookie(request, response, userId);
//        List<AddrProvince> provinceList = getProvinceList(request, response);
//
//        UmsInfo umsInfo = umsInfoService.getUserInfo(userId);
//        SmsStore store = storeService.getStore(userId);
//        model.addAttribute("defaultAddress", defaultAddress);
//        model.addAttribute("provinceList", provinceList);
//        model.addAttribute("username", username);
//        model.addAttribute("userId", userId);
//        model.addAttribute("roleId", roleId);
//        model.addAttribute("umsLogin", umsLogin);
//        model.addAttribute("umsInfo", umsInfo);
//        model.addAttribute("store", store);
//        return "common/profile";
//    }

    @RequestMapping("user/chgUsernameOrPass")
    @ResponseBody
    public String changeUsernameOrPass(UmsLogin umsLogin, Model model) {
        userService.modifyUsernameOrEmail(umsLogin);
        return JSON.toJSONString("修改成功");
    }

    @RequestMapping("user/chgUsernameOrEmail")
    @ResponseBody
    public String chgUsernameOrEmail(HttpServletRequest request, HttpServletResponse response, UmsLogin umsLogin, Model model) {
        if (umsLogin.getUsername() == "") umsLogin.setUsername(null);
        if (umsLogin.getEmail() == "") umsLogin.setEmail(null);
        umsLogin = userService.modifyUsernameOrEmail(umsLogin);
        // 更新token
        updateToken(umsLogin, request, response);
        return "success";
    }

    @RequestMapping("user/getCityList")
    public String getCityList(String privinceId, Model model) {
        List<AddrCity> cityList = cascadeAddressService.getAllCityByProvinceId(privinceId);
        model.addAttribute("cityList", cityList);
        return "common/profile::select-city";
    }

    @RequestMapping("user/getCountyList")
    public String getCountyList(String cityId, Model model) {
        List<AddrCounty> countyList = cascadeAddressService.getAllCountyByCityId(cityId);
        model.addAttribute("countyList", countyList);
        return "common/profile::select-region";
    }

    @RequestMapping("user/saveDefaultAddr")
    @ResponseBody
    public String saveDefaultAddr(HttpServletRequest request, HttpServletResponse response, UmsDefaultAddress umsDefaultAddress, Model model) {
        umsDefaultAddress = umsDefaultAddressService.modifyDefaultAddress(umsDefaultAddress);
        CookieUtil.deleteCookie(request, response, "defaultAddress");
        CookieUtil.setCookie(request, response, "defaultAddress", JSON.toJSONString(umsDefaultAddress), 60 * 60 * 72, true);
        return "success";
    }

    @RequestMapping("user/delDefaultAddr")
    public String saveDefaultAddr(HttpServletRequest request, HttpServletResponse response, Model model) {
        String defaultAddrStr = CookieUtil.getCookieValue(request, "defaultAddress", true);
        UmsDefaultAddress defaultAddress = JSON.parseObject(defaultAddrStr, UmsDefaultAddress.class);
        umsDefaultAddressService.delDefaultAddressByLoginId(defaultAddress.getLoginId());
        CookieUtil.deleteCookie(request, response, "defaultAddress");
        return "common/profile::div-contact";
    }

    private UmsDefaultAddress getUmsDefaultAddressFromCookie(HttpServletRequest request, HttpServletResponse response, String userId) {
        UmsDefaultAddress defaultAddress = null;
        String defaultAddrStr = CookieUtil.getCookieValue(request, "defaultAddress", true);
        if (StringUtils.isNotBlank(defaultAddrStr)) {
            defaultAddress = JSON.parseObject(defaultAddrStr, UmsDefaultAddress.class);
        } else {
            defaultAddress = umsDefaultAddressService.getDefaultAddressByLoginId(userId);
        }
        CookieUtil.setCookie(request, response, "defaultAddress", JSON.toJSONString(defaultAddress), 60 * 60 * 72, true);
        return defaultAddress;
    }

    private List<AddrProvince> getProvinceList(HttpServletRequest request, HttpServletResponse response) {
        List<AddrProvince> provinceList = null;
        String provinceListStr = CookieUtil.getCookieValue(request, "provinceList", true);
        if (StringUtils.isNotBlank(provinceListStr)) {
            provinceList = JSON.parseArray(provinceListStr, AddrProvince.class);
        } else {
            provinceList = cascadeAddressService.getAllProvince();
        }
        CookieUtil.setCookie(request, response, "provinceList", JSON.toJSONString(provinceList), 60 * 60 * 72, true);
        return provinceList;
    }

    private void updateToken(UmsLogin umsLogin, HttpServletRequest request, HttpServletResponse response) {
        Map<String, Object> userMap = new HashMap<>();
        String userId = umsLogin.getId();
        userMap.put("userId", umsLogin.getId());
        userMap.put("username", umsLogin.getUsername());
        userMap.put("roleId", umsLogin.getRoleId());
        String remoteAddr = request.getHeader("s-forward-for");
        if (StringUtils.isBlank(remoteAddr)) {
            remoteAddr = request.getRemoteAddr();
            if (StringUtils.isBlank(remoteAddr)) {
                remoteAddr = "127.0.0.1";
            }
        }
        String token = JwtUtil.encode("2020anonyshop", userMap, remoteAddr);
        userService.addToken(token, userId);
        CookieUtil.setCookie(request, response, "oldToken", token, 60 * 60 * 72, false);
    }
}
