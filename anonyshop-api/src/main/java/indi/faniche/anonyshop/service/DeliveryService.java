package indi.faniche.anonyshop.service;

/* File:   DeliveryService.java
 * -------------------------
 * Author: faniche
 * Date:   5/11/20
 */

import com.github.pagehelper.PageInfo;
import indi.faniche.anonyshop.bean.checkout.OmsDelivery;
import indi.faniche.anonyshop.bean.checkout.OmsOrder;

public interface DeliveryService {
    void addNewDelivery(OmsOrder omsOrder);

    void sendDelivery(OmsDelivery delivery);

    OmsDelivery getDeliveryByOrderId(String orderId);

    PageInfo<OmsDelivery> getStoreProgressDelivery(Integer page, String storeId);

    PageInfo<OmsDelivery> getUserProgressDelivery(Integer page, String userId);

    OmsDelivery updateDeliveryByOrderId(OmsDelivery delivery);

    void delDeliveryByOrderId(OmsDelivery delivery);
}
