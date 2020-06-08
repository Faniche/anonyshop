package indi.faniche.anonyshop.product.service.impl;

/* File:   CommentServiceImpl.java
 * -------------------------
 * Author: faniche
 * Date:   5/17/20
 */

import com.alibaba.dubbo.config.annotation.Service;
import com.alibaba.fastjson.JSON;
import indi.faniche.anonyshop.bean.comment.PmsComment;
import indi.faniche.anonyshop.modol.Comments;
import indi.faniche.anonyshop.mq.ActiveMQUtil;
import indi.faniche.anonyshop.product.mapper.PmsCommentMapper;
import indi.faniche.anonyshop.service.CommentService;

import org.apache.activemq.command.ActiveMQMapMessage;
import org.apache.activemq.command.ActiveMQTextMessage;
import org.checkerframework.checker.units.qual.C;
import org.springframework.beans.factory.annotation.Autowired;

import javax.jms.*;
import java.util.List;

@Service
public class CommentServiceImpl implements CommentService {

    @Autowired
    PmsCommentMapper pmsCommentMapper;

    @Autowired
    ActiveMQUtil activeMQUtil;

    @Override
    public void addCommment(Comments comments) {
        List<PmsComment> pmsComments = comments.getComments();
        for (PmsComment comment : pmsComments) {
            pmsCommentMapper.insertSelective(comment);
        }
        Connection connection = null;
        Session session = null;
        try {
            connection = activeMQUtil.getConnectionFactory().createConnection();
            session = connection.createSession(true, Session.SESSION_TRANSACTED);
            Queue product_review_queue = session.createQueue("PRODUCT_REVIEW_QUEUE");
            MessageProducer producer = session.createProducer(product_review_queue);
            TextMessage textMessage = new ActiveMQTextMessage();
            textMessage.setText(JSON.toJSONString(pmsComments));
            producer.send(textMessage);
            session.commit();
        } catch (Exception ex) {
            try {
                session.rollback();
            } catch (JMSException e1) {
                e1.printStackTrace();
            }
        } finally {
            try {
                connection.close();
            } catch (JMSException e1) {
                e1.printStackTrace();
            }
        }
    }

    @Override
    public List<PmsComment> getComments(String productId, String storeId) {
        PmsComment pmsComment = new PmsComment();
        pmsComment.setProductId(productId);
        pmsComment.setStoreId(storeId);
        List<PmsComment> pmsComments = pmsCommentMapper.select(pmsComment);
        for (PmsComment tmp : pmsComments) {
            int starNum = Integer.valueOf(tmp.getStar());
            int[] starNums = new int[starNum];
            for (int i = 0; i < starNum; i++) {
                 starNums[i] = i;
            }
            tmp.setStarArray(starNums);
            int emptyStarNum = 5 - starNum;
            starNums = new int[emptyStarNum];
            for (int i = 0; i < emptyStarNum; i++) {
                starNums[i] = i;
            }
            tmp.setEmptyStarArray(starNums);
        }
        return pmsComments;
    }
}
