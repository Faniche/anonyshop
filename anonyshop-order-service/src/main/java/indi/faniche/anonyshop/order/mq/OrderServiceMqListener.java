package indi.faniche.anonyshop.order.mq;

/* File:   OrderServiceMqListener.java
 * -------------------------
 * Author: faniche
 * Date:   5/7/20
 */


import indi.faniche.anonyshop.bean.checkout.OmsOrder;
import indi.faniche.anonyshop.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.stereotype.Component;

import javax.jms.JMSException;
import javax.jms.MapMessage;
import java.math.BigDecimal;
import java.util.Date;

@Component
public class OrderServiceMqListener {

    @Autowired
    OrderService orderService;

    @JmsListener(destination = "PAYMENT_SUCCESS_QUEUE", containerFactory = "jmsQueueListener")
    public void updateOrderStatus(MapMessage mapMessage) throws JMSException {
        String out_trade_no = mapMessage.getString("out_trade_no");
        Integer count = 0;
        if (mapMessage.getString("count") != null) {
            count = Integer.parseInt("" + mapMessage.getString("count"));
        }
        String total_amount = mapMessage.getString("total_amount");
        //Date paymentTime = (Date) mapMessage.getObject("payment_time");
        // 更新订单状态业务
//        System.out.println("更新订单状态");
//        System.out.println(out_trade_no);
        OmsOrder omsOrder = new OmsOrder();
        omsOrder.setOrderSn(out_trade_no);
        omsOrder.setPaymentTime(new Date());
        omsOrder.setPayAmount(new BigDecimal(total_amount));
        orderService.updateOrderPayStatus(omsOrder);
    }
}