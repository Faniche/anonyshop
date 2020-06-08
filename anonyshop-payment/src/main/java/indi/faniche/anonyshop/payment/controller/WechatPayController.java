package indi.faniche.anonyshop.payment.controller;

/* File:   WechatPayController.java
 * -------------------------
 * Author: faniche
 * Date:   5/6/20
 */

import indi.faniche.anonyshop.annotations.LoginRequired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;
import java.math.BigDecimal;

@Controller
@CrossOrigin
public class WechatPayController {

    @RequestMapping("wechat")
    @LoginRequired(loginSuccess = true)
    public String wechat(String outTradeNo, BigDecimal totalAmount, Model model, HttpServletRequest request) {
        String userId = (String) request.getAttribute("userId");
        String username = (String) request.getAttribute("username");
        model.addAttribute("username", username);
        model.addAttribute("outTradeNo", outTradeNo);
        model.addAttribute("totalAmount", totalAmount);
        model.addAttribute("userId", userId);
        model.addAttribute("outTradeNo", outTradeNo);
        model.addAttribute("totalAmount", totalAmount);
        return "payment/payment";
    }
}
