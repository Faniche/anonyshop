package indi.faniche.anonyshop.delivery.mq;

/* File:   DeliveryServiceMqListener.java
 * -------------------------
 * Author: faniche
 * Date:   5/11/20
 */

import com.alibaba.fastjson.JSON;
import indi.faniche.anonyshop.bean.checkout.OmsOrder;
import indi.faniche.anonyshop.service.DeliveryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.stereotype.Component;

import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.TextMessage;

@Component
public class DeliveryServiceMqListener {

    @Autowired
    DeliveryService deliveryService;

    @JmsListener(destination = "ORDER_PAY_QUEUE", containerFactory = "jmsQueueListener")
    public void newDelivery(TextMessage textMessage) throws JMSException {
        String orderStr = textMessage.getText();
        OmsOrder omsOrder = JSON.parseObject(orderStr, OmsOrder.class);
        deliveryService.addNewDelivery(omsOrder);
        System.out.println(omsOrder.getStatus());
    }
}
