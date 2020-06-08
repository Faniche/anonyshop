package indi.faniche.anonyshop.payment.service;

/* File:   PaypalService.java
 * -------------------------
 * Author: faniche
 * Date:   5/5/20
 */

import com.paypal.api.payments.Payment;
import com.paypal.base.rest.PayPalRESTException;

import java.math.BigDecimal;

public interface PaypalService {
    Payment createPayment(
            BigDecimal total,
            String currency,
            String method,
            String intent,
            String description,
            String cancelUrl,
            String successUrl) throws PayPalRESTException;

    Payment executePayment(String paymentId, String payerId) throws PayPalRESTException;
}
