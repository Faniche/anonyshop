package indi.faniche.anonyshop.order.service.impl;

/* File:   OrderServiceImpl.java
 * -------------------------
 * Author: faniche
 * Date:   5/3/20
 */

import com.alibaba.dubbo.config.annotation.Service;
import com.alibaba.fastjson.JSON;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import indi.faniche.anonyshop.bean.checkout.OmsOrder;
import indi.faniche.anonyshop.bean.checkout.OmsOrderItem;
import indi.faniche.anonyshop.bean.sku.PmsSkuInfo;
import indi.faniche.anonyshop.mq.ActiveMQUtil;
import indi.faniche.anonyshop.order.mapper.OmsOrderItemMapper;
import indi.faniche.anonyshop.order.mapper.OmsOrderMapper;
import indi.faniche.anonyshop.service.OrderService;
import indi.faniche.anonyshop.util.RedisUtil;
import org.apache.activemq.command.ActiveMQTextMessage;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import redis.clients.jedis.Jedis;
import tk.mybatis.mapper.entity.Example;

import javax.jms.*;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.UUID;

@Service
public class OrderServiceImpl implements OrderService {

    @Autowired
    RedisUtil redisUtil;

    @Autowired
    OmsOrderMapper omsOrderMapper;

    @Autowired
    OmsOrderItemMapper omsOrderItemMapper;

    @Autowired
    ActiveMQUtil activeMQUtil;

    @Override
    public String genTradeCode(String userId) {
        Jedis jedis = redisUtil.getJedis();
        String key = "user:" + userId + ":tradeCode";
        String tradeCode = UUID.randomUUID().toString();
        jedis.setex(key, 60 * 15, tradeCode);
        jedis.close();
        return tradeCode;
    }

    @Override
    public String checkTradeCode(String userId, String tradeCode) {
        Jedis jedis = null;
        try {
            jedis = redisUtil.getJedis();
            String key = "user:" + userId + ":tradeCode";
//             String tradeCodeFromCache = jedis.get(key);
            String script = "if redis.call('get', KEYS[1]) == ARGV[1] then return redis.call('del', KEYS[1]) else return 0 end";
            Long eval = (Long) jedis.eval(script, Collections.singletonList(key), Collections.singletonList(tradeCode));

            if (eval != null && eval != 0) {
                jedis.del(key);
                return "success";
            } else {
                return "fail";
            }
        } finally {
            jedis.close();
        }
    }

    @Override
    public void saveOrder(OmsOrder subOmsOrder) {
        // 保存订单表
        omsOrderMapper.insertSelective(subOmsOrder);
        String orderId = subOmsOrder.getId();
        // 保存订单详情
        List<OmsOrderItem> omsOrderItems = subOmsOrder.getOmsOrderItemList();
        for (OmsOrderItem omsOrderItem : omsOrderItems) {
            omsOrderItem.setOrderId(orderId);
            omsOrderItemMapper.insertSelective(omsOrderItem);
        }
    }

    @Override
    public OmsOrder getOrderByOutTradeNo(String outTradeNo) {
        OmsOrder omsOrder = new OmsOrder();
        omsOrder.setOrderSn(outTradeNo);
        OmsOrder omsOrder1 = omsOrderMapper.selectOne(omsOrder);
        return omsOrder1;
    }

    @Override
    public void updateOrderPayStatus(OmsOrder omsOrder) {
        Example e = new Example(OmsOrder.class);
        e.createCriteria().andEqualTo("orderSn", omsOrder.getOrderSn());
        OmsOrder omsOrderUpdate = new OmsOrder();
        omsOrderUpdate.setStatus("1");
        omsOrderUpdate.setPayAmount(omsOrder.getPayAmount());
        omsOrderUpdate.setPaymentTime(omsOrder.getPaymentTime());
        // 发送一个订单已支付的队列，提供给库存消费
        Connection connection = null;
        Session session = null;
        try {
            connection = activeMQUtil.getConnectionFactory().createConnection();
            session = connection.createSession(true, Session.SESSION_TRANSACTED);
            Queue payhment_success_queue = session.createQueue("ORDER_PAY_QUEUE");
            MessageProducer producer = session.createProducer(payhment_success_queue);
            TextMessage textMessage = new ActiveMQTextMessage();//字符串文本
            //MapMessage mapMessage = new ActiveMQMapMessage();// hash结构

            // 查询订单的对象，转化成json字符串，存入ORDER_PAY_QUEUE的消息队列
            OmsOrder omsOrderParam = new OmsOrder();
            omsOrderParam.setOrderSn(omsOrder.getOrderSn());
            OmsOrder omsOrderResponse = omsOrderMapper.selectOne(omsOrderParam);

            OmsOrderItem omsOrderItemParam = new OmsOrderItem();
            omsOrderItemParam.setOrderSn(omsOrderParam.getOrderSn());
            List<OmsOrderItem> select = omsOrderItemMapper.select(omsOrderItemParam);
            omsOrderResponse.setOmsOrderItemList(select);
            textMessage.setText(JSON.toJSONString(omsOrderResponse));

            omsOrderMapper.updateByExampleSelective(omsOrderUpdate, e);
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
    public Date getStoreLatestOrderDate(String storeId) {
        Example example = new Example(OmsOrder.class);
        example.createCriteria().andEqualTo("storeId", storeId);
        example.orderBy("receiveTime").desc();
        OmsOrder omsOrder = new OmsOrder();
        omsOrder.setStoreId(storeId);
        omsOrderMapper.select(omsOrder);
        List<OmsOrder> orderList = omsOrderMapper.selectByExample(example);
        if (!orderList.isEmpty()) {
            return orderList.get(0).getReceiveTime();
        }
        return null;
    }

    @Override
    public PageInfo<OmsOrder> getStoreOrderList(int page, String storeId) {
        OmsOrder order = new OmsOrder();
        order.setStoreId(storeId);
        try {
            PageHelper.startPage(page, 10); //每页的大小为pageSize，查询第page页的结果
            PageHelper.orderBy("id ASC "); //进行分页结果的排序
            List<OmsOrder> omsOrders = omsOrderMapper.select(order);
            for (OmsOrder tmp : omsOrders) {
                switch (tmp.getStatus()){
                    case "0":
                        tmp.setOrderStatusStr("待付款");
                        break;
                    case "1":
                        tmp.setOrderStatusStr("待发货");
                        break;
                    case "2":
                        tmp.setOrderStatusStr("已发货");
                        break;
                    case "3":
                        tmp.setOrderStatusStr("待签收");
                        break;
                    case "4":
                        tmp.setOrderStatusStr("已完成");
                        break;
                    case "5":
                        tmp.setOrderStatusStr("已关闭");
                        break;
                    case "6":
                        tmp.setOrderStatusStr("无效订单");
                        break;
                }

                OmsOrderItem orderItem = new OmsOrderItem();
                orderItem.setOrderId(tmp.getId());
                List<OmsOrderItem> omsOrderItems = omsOrderItemMapper.select(orderItem);
                tmp.setOmsOrderItemList(omsOrderItems);
            }
            PageInfo<OmsOrder> orderList = new PageInfo<>(omsOrders);
            return orderList;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public PageInfo<OmsOrder> getUserOrderList(int page, String userId) {
        OmsOrder order = new OmsOrder();
        order.setMemberId(userId);
        try {
            PageHelper.startPage(page, 10); //每页的大小为pageSize，查询第page页的结果
            PageHelper.orderBy("id ASC "); //进行分页结果的排序
            List<OmsOrder> omsOrders = omsOrderMapper.select(order);
            for (OmsOrder tmp : omsOrders) {
                switch (tmp.getStatus()){
                    case "0":
                        tmp.setOrderStatusStr("待付款");
                        break;
                    case "1":
                        tmp.setOrderStatusStr("待发货");
                        break;
                    case "2":
                        tmp.setOrderStatusStr("已发货");
                        break;
                    case "3":
                        tmp.setOrderStatusStr("待签收");
                        break;
                    case "4":
                        tmp.setOrderStatusStr("已完成");
                        break;
                    case "5":
                        tmp.setOrderStatusStr("已关闭");
                        break;
                    case "6":
                        tmp.setOrderStatusStr("无效订单");
                        break;
                }

                OmsOrderItem orderItem = new OmsOrderItem();
                orderItem.setOrderId(tmp.getId());
                List<OmsOrderItem> omsOrderItems = omsOrderItemMapper.select(orderItem);
                tmp.setOmsOrderItemList(omsOrderItems);
            }
            PageInfo<OmsOrder> orderList = new PageInfo<>(omsOrders);
            return orderList;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public OmsOrder getOrderById(String orderId) {
        OmsOrder omsOrder = new OmsOrder();
        omsOrder.setId(orderId);
        OmsOrder order = omsOrderMapper.selectOne(omsOrder);
        OmsOrderItem orderItem = new OmsOrderItem();
        orderItem.setOrderId(order.getId());
        List<OmsOrderItem> omsOrderItems = omsOrderItemMapper.select(orderItem);
        order.setOmsOrderItemList(omsOrderItems);
        return order;
    }

    @Override
    public void updateOrderDeliveryStatus(OmsOrder omsOrder) {
        omsOrderMapper.updateByPrimaryKeySelective(omsOrder);
    }

    @Override
    public void delOrderById(String orderId) {
        OmsOrderItem orderItem = new OmsOrderItem();
        orderItem.setOrderId(orderId);
        omsOrderItemMapper.delete(orderItem);
        omsOrderMapper.deleteByPrimaryKey(orderId);

    }

    @Override
    public List<OmsOrderItem> getOrderItemListByOrderId(String orderId) {
        OmsOrderItem orderItem = new OmsOrderItem();
        orderItem.setOrderId(orderId);
        List<OmsOrderItem> orderItemList = omsOrderItemMapper.select(orderItem);
        return orderItemList;
    }
}
