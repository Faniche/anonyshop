package indi.faniche.anonyshop.service;

/* File:   SmsStoreProductService.java
 * -------------------------
 * Author: faniche
 * Date:   5/17/20
 */

import indi.faniche.anonyshop.bean.comment.PmsComment;

import java.math.BigDecimal;
import java.util.List;

public interface SmsStoreProductService {
    void updateProductScore(List<PmsComment> pmsComments);

    BigDecimal getStoreProcuctScore(String productId, String storeId);
}
