package indi.faniche.anonyshop.service;

/* File:   OrderService.java
 * -------------------------
 * Author: faniche
 * Date:   5/3/20
 */

import com.github.pagehelper.PageInfo;
import indi.faniche.anonyshop.bean.checkout.OmsOrder;
import indi.faniche.anonyshop.bean.checkout.OmsOrderItem;

import java.util.Date;
import java.util.List;

public interface OrderService {
    String genTradeCode(String userId);

    String checkTradeCode(String userId, String tradeCode);

    void saveOrder(OmsOrder subOmsOrder);

    OmsOrder getOrderByOutTradeNo(String outTradeNo);

    void updateOrderPayStatus(OmsOrder omsOrder);

    Date getStoreLatestOrderDate(String storeId);

    PageInfo<OmsOrder> getStoreOrderList(int page, String storeId);

    PageInfo<OmsOrder> getUserOrderList(int page, String userId);

    OmsOrder getOrderById(String orderId);

    void updateOrderDeliveryStatus(OmsOrder omsOrder);

    void delOrderById(String orderId);

    List<OmsOrderItem> getOrderItemListByOrderId(String orderId);
}
