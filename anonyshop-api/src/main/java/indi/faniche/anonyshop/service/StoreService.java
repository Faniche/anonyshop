package indi.faniche.anonyshop.service;

/* File:   StoreService.java
 * -------------------------
 * Author: faniche
 * Date:   5/9/20
 */

import indi.faniche.anonyshop.bean.store.SmsStore;

public interface StoreService {
    SmsStore getStore(String userId);

    void colse(String storeId, String status);

    SmsStore addStore(SmsStore store);

    SmsStore getStoreByStoreId(String storeId);
}
