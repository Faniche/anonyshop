package indi.faniche.anonyshop.payment.controller;

/* File:   PaymentController.java
 * -------------------------
 * Author: faniche
 * Date:   5/4/20
 */

import com.alibaba.dubbo.config.annotation.Reference;
import com.alibaba.fastjson.JSON;
import com.alipay.api.AlipayApiException;
import com.alipay.api.AlipayClient;
import com.alipay.api.request.AlipayTradePagePayRequest;
import com.paypal.api.payments.Links;
import com.paypal.api.payments.Payment;
import com.paypal.base.rest.PayPalRESTException;
import indi.faniche.anonyshop.annotations.LoginRequired;
import indi.faniche.anonyshop.bean.checkout.OmsOrder;
import indi.faniche.anonyshop.bean.payment.PaymentInfo;
import indi.faniche.anonyshop.modol.PaypalOrder;
import indi.faniche.anonyshop.payment.config.AlipayConfig;
import indi.faniche.anonyshop.payment.service.PaypalService;
import indi.faniche.anonyshop.service.OrderService;
import indi.faniche.anonyshop.payment.service.PaymentService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.math.BigDecimal;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Controller
@CrossOrigin
public class PaymentController {

    @Autowired
    PaymentService paymentService;

    @Reference
    OrderService orderService;

    @Autowired
    PaypalService paypalService;

    @RequestMapping(value = {"", "/", "index"})
    @LoginRequired(loginSuccess = true)
    public String paymentIndex(String outTradeNo, BigDecimal totalAmount, Model model, HttpServletRequest request) {
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
