package indi.faniche.anonyshop.user.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.alibaba.fastjson.JSON;
import indi.faniche.anonyshop.annotations.LoginRequired;
import indi.faniche.anonyshop.bean.user.UmsLogin;
import indi.faniche.anonyshop.modol.Msg;
import indi.faniche.anonyshop.service.UserService;
import indi.faniche.anonyshop.util.MailUtil;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import javax.mail.MessagingException;
import javax.servlet.http.HttpServletRequest;

@Controller
public class UserLoginInfoController {
    @Reference
    UserService userService;

//    @Resource
//    MailUtil MailUtil;

    @RequestMapping("registry")
    public String toRegistry(){
        return "user/registration";
    }

    @RequestMapping("registrySubmit")
    @ResponseBody
    public String registrySubmit(UmsLogin login, Model model){
        login.setRoleId("1");
        login = userService.addUser(login);
        if (login.getId() != null) {
            model.addAttribute("userId", login.getId());
            model.addAttribute("username", login.getUsername());
            MailUtil.setReceiver(login.getEmail());
            MailUtil.setSubject("Anonyshop 账户激活");
            String content = "<a href='http://user.anonyshop.tech/confirm?userId=" + login.getId() + "'>点击这里的网址激活账户</a>";
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

    @RequestMapping("confirm")
    @LoginRequired(loginSuccess = false)
    public String toConfirm(String userId, Model model, HttpServletRequest request) {
        userService.activateAccount(userId);
        String username = (String) request.getAttribute("username");
        model.addAttribute("username", username);
        model.addAttribute("userId", userId);
        Msg msg = new Msg();
        msg.setTitle("激活账户");
        msg.setContent("账户已成功激活");
        model.addAttribute("msg", msg);
        return "msg";
    }

    @RequestMapping("forgetpass")
    public String toForget() {
        return "user/forgetpass";
    }

    @RequestMapping("sndresetlink")
    public String sendLink(String email, Model model, HttpServletRequest request) {
        MailUtil.setReceiver(email);
        MailUtil.setSubject("Anonyshop 重置密码");
        String content = "<a href='http://user.anonyshop.tech/toreset?email=" + email + "'>点击这里重置密码</a>";
        MailUtil.setContent(content);
        try {
            MailUtil.sendMail();
        } catch (MessagingException e) {
            e.printStackTrace();
        }
        Msg msg = new Msg();
        msg.setTitle("重置密码");
        msg.setContent("重置密码的邮件已发送，请尽快操作");
        model.addAttribute("msg", msg);
        return "msg";
    }

    @RequestMapping("toreset")
    public String toResetPass(String email, Model model, HttpServletRequest request) {
        model.addAttribute("email", email);
        Msg msg = new Msg();
        msg.setTitle("重置密码");
        msg.setContent("重置密码的邮件已发送，请尽快操作");
        model.addAttribute("msg", msg);
        return "user/resetpass";
    }

    @RequestMapping("resetpass")
    public String resetPass(UmsLogin login, Model model, HttpServletRequest request) {
        userService.resetPass(login);
        Msg msg = new Msg();
        msg.setTitle("重置密码");
        msg.setContent("重置密码成功，请重新登录");
        model.addAttribute("msg", msg);
        return "msg";
    }
}
