package indi.faniche.anonyshop.payment.controller;

/* File:   AlipayController.java
 * -------------------------
 * Author: faniche
 * Date:   5/6/20
 */

import com.alibaba.dubbo.config.annotation.Reference;
import com.alibaba.fastjson.JSON;
import com.alipay.api.AlipayApiException;
import com.alipay.api.AlipayClient;
import com.alipay.api.request.AlipayTradePagePayRequest;
import indi.faniche.anonyshop.annotations.LoginRequired;
import indi.faniche.anonyshop.bean.checkout.OmsOrder;
import indi.faniche.anonyshop.bean.payment.PaymentInfo;
import indi.faniche.anonyshop.payment.config.AlipayConfig;
import indi.faniche.anonyshop.payment.service.PaymentService;
import indi.faniche.anonyshop.service.OrderService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.math.BigDecimal;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Controller
@CrossOrigin
public class AlipayController {
    @Autowired
    AlipayClient alipayClient;

    @Reference
    OrderService orderService;

    @Autowired
    PaymentService paymentService;

    @RequestMapping("alipay")
    @LoginRequired(loginSuccess = true)
    @ResponseBody
    public String alipay(String outTradeNo, BigDecimal totalAmount, Model model, HttpServletRequest request) {
        String userId = (String) request.getAttribute("userId");
        String username = (String) request.getAttribute("username");
        // 获得一个支付宝请求的客户端(它并不是一个链接，而是一个封装好的http的表单请求)
        String form = null;
        //创建API对应的request
        AlipayTradePagePayRequest alipayRequest = new AlipayTradePagePayRequest();
        alipayRequest.setReturnUrl(AlipayConfig.return_payment_url);
        alipayRequest.setNotifyUrl(AlipayConfig.notify_payment_url);
        Map<String, Object> map = new HashMap<>();
        map.put("out_trade_no", outTradeNo);
        map.put("product_code", "FAST_INSTANT_TRADE_PAY");
        map.put("total_amount", totalAmount);
        OmsOrder omsOrder = orderService.getOrderByOutTradeNo(outTradeNo);
        map.put("subject", omsOrder.getBillContent());
        String param = JSON.toJSONString(map);
        alipayRequest.setBizContent(param);
        try {
            form = alipayClient.pageExecute(alipayRequest).getBody(); //调用SDK生成表单
        } catch (
                AlipayApiException e) {
            e.printStackTrace();
        }
        // 生成并且保存用户的支付信息
        PaymentInfo paymentInfo = new PaymentInfo();
        paymentInfo.setCreateTime(new Date());
        paymentInfo.setOrderId(omsOrder.getId());
        paymentInfo.setOrderSn(outTradeNo);
        paymentInfo.setPaymentStatus("未付款");
        paymentInfo.setSubject(omsOrder.getBillContent());
        paymentInfo.setTotalAmount(totalAmount);
        paymentService.savePaymentInfo(paymentInfo);
        // 向消息中间件发送一个检查支付状态(支付服务消费)的延迟消息队列
        paymentService.sendDelayPaymentResultCheckQueue(outTradeNo, 5);

        // 提交请求到支付宝
        return form;
    }

    @RequestMapping("alipay/success")
    @LoginRequired(loginSuccess = true)
    public String alipaySuccess(HttpServletRequest request, Model model) {
        String userId = (String) request.getAttribute("userId");
        String username = (String) request.getAttribute("username");
        String sign = request.getParameter("sign");
        String trade_no = request.getParameter("trade_no");
        String out_trade_no = request.getParameter("out_trade_no");
        String trade_status = request.getParameter("trade_status");
        String total_amount = request.getParameter("total_amount");
        String subject = request.getParameter("subject");
        String call_back_content = request.getQueryString();
        Date paymentTime = new Date();
        if (StringUtils.isNotBlank(sign)) {
            // 验签成功
            // 更新用户的支付状态
            PaymentInfo paymentInfo = new PaymentInfo();
            paymentInfo.setOrderSn(out_trade_no);
            paymentInfo.setPaymentStatus("已支付");
            paymentInfo.setAlipayTradeNo(trade_no);// 支付宝的交易凭证号
            paymentInfo.setCallbackContent(call_back_content);//回调请求字符串
            paymentInfo.setCallbackTime(new Date());
            paymentService.updatePayment(paymentInfo, total_amount, paymentTime);
        }
        // 订单服务更新 ----> 库存服务的更新 ----> 物流服务
        model.addAttribute("result", "付款成功");
        model.addAttribute("userId", userId);
        model.addAttribute("username", username);
        return "payment/success";
    }
}
