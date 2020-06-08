package indi.faniche.anonyshop.payment.controller;

/* File:   PaypalController.java
 * -------------------------
 * Author: faniche
 * Date:   5/6/20
 */

import com.paypal.api.payments.Links;
import com.paypal.api.payments.Payment;
import com.paypal.base.rest.PayPalRESTException;
import indi.faniche.anonyshop.annotations.LoginRequired;
import indi.faniche.anonyshop.modol.PaypalOrder;
import indi.faniche.anonyshop.payment.service.PaypalService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServletRequest;
import java.math.BigDecimal;

@Controller
@CrossOrigin
public class PaypalController {


    @Autowired
    PaypalService paypalService;

    @RequestMapping("paypal")
    @LoginRequired(loginSuccess = true)
    public String paypal(PaypalOrder order, String outTradeNo, String payMethod, BigDecimal totalAmount, Model model, HttpServletRequest request) {
        String userId = (String) request.getAttribute("userId");
        String username = (String) request.getAttribute("username");
        // 	CNY
        try {
            Payment payment = paypalService.createPayment(totalAmount, "USD", payMethod,
                    "ORDER", "Pixel 4a", "http://payment.anonyshop.tech/paypal/cancel",
                    "http://payment.anonyshop.tech/paypal/success");
            for(Links link:payment.getLinks()) {
                if(link.getRel().equals("approval_url")) {
                    return "redirect:"+link.getHref();
                }
            }
        } catch (PayPalRESTException e) {

            e.printStackTrace();
        }
        return "payment/payment";
    }

    @RequestMapping("paypal/success")
    @LoginRequired(loginSuccess = true)
    public String paypalSuccess(@RequestParam("paymentId") String paymentId, @RequestParam("PayerID") String payerId, Model model, HttpServletRequest request) {
        String userId = (String) request.getAttribute("userId");
        String username = (String) request.getAttribute("username");
        try {
            Payment payment = paypalService.executePayment(paymentId, payerId);
            System.out.println(payment.toJSON());
            if (payment.getState().equals("approved")) {
                return "paypal/success";
            }
        } catch (PayPalRESTException e) {
            System.out.println(e.getMessage());
        }
        return "paypal/success";
    }

    @RequestMapping("paypal/cancel")
    @LoginRequired(loginSuccess = true)
    public String paypal(HttpServletRequest request) {
        return "payment/cancel";
    }
}
