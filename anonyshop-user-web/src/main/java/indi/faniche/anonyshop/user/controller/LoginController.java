package indi.faniche.anonyshop.user.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import indi.faniche.anonyshop.bean.user.UmsLogin;
import indi.faniche.anonyshop.service.UserService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

@Controller
public class LoginController {
    @Reference
    UserService userService;

    @RequestMapping("index")
    @ResponseBody
    public String index(){
        return "hello";
    }

    @RequestMapping("getAllUser")
    @ResponseBody
    public List<UmsLogin> getAllUser(){
        List<UmsLogin> loginList = userService.getAllUmsLogin();
        return loginList;
    }


}
