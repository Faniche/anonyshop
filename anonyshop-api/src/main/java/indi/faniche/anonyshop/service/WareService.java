package indi.faniche.anonyshop.service;

/* File:   WareService.java
 * -------------------------
 * Author: faniche
 * Date:   5/14/20
 */

import indi.faniche.anonyshop.bean.ware.WmsWareInfo;

import java.util.List;

public interface WareService {
    List<WmsWareInfo> getAllWares(String storeId);
}
