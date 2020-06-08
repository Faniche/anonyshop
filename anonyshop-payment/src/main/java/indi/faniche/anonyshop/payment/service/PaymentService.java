package indi.faniche.anonyshop.payment.service;

/* File:   PaymentService.java
 * -------------------------
 * Author: faniche
 * Date:   5/5/20
 */

import indi.faniche.anonyshop.bean.payment.PaymentInfo;

import java.util.Date;
import java.util.Map;

public interface PaymentService {
    void updatePayment(PaymentInfo paymentInfo, String total_amount, Date paymentTime);

    void savePaymentInfo(PaymentInfo paymentInfo);

    void sendDelayPaymentResultCheckQueue(String outTradeNo, int count);

    Map<String, Object> checkAlipayPayment(String out_trade_no);
}
