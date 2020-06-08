package indi.faniche.anonyshop.store.mq;

/* File:   CommentServiceMqListener.java
 * -------------------------
 * Author: faniche
 * Date:   5/17/20
 */

import com.alibaba.fastjson.JSON;
import indi.faniche.anonyshop.bean.comment.PmsComment;
import indi.faniche.anonyshop.bean.store.SmsStore;
import indi.faniche.anonyshop.bean.store.SmsStoreProduct;
import indi.faniche.anonyshop.service.SmsStoreProductService;
import indi.faniche.anonyshop.store.mapper.SmsStoreProductMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.stereotype.Component;

import javax.jms.JMSException;
import javax.jms.MapMessage;
import javax.jms.TextMessage;
import java.util.List;

@Component
public class CommentServiceMqListener {

    @Autowired
    SmsStoreProductService smsStoreProductService;

    @JmsListener(destination = "PRODUCT_REVIEW_QUEUE", containerFactory = "jmsQueueListener")
    public void newDelivery(TextMessage textMessage) throws JMSException {
        String pmsCommentsStr = textMessage.getText();
        List<PmsComment> pmsComments = JSON.parseArray(pmsCommentsStr, PmsComment.class);
        smsStoreProductService.updateProductScore(pmsComments);
    }
}
