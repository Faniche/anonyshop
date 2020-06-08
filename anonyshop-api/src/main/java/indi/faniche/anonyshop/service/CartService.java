package indi.faniche.anonyshop.service;

/* File:   CartService.java
 * -------------------------
 * Author: faniche
 * Date:   4/28/20
 */

import indi.faniche.anonyshop.bean.checkout.OmsCartItem;

import java.util.List;

public interface CartService {
    OmsCartItem ifCartExistByUser(String memberId, String skuId);

    void addCart(OmsCartItem omsCartItem);

    void updateCart(OmsCartItem omsCartItemFromDb);

    void syncCartCache(String memberId);

    List<OmsCartItem> cartList(String memberId);

    void checkCart(OmsCartItem omsCartItem);

    void deleteCartItem(String userId, String skuId);

    void checkAll(String userId, String isChecked);
}
