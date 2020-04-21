package indi.faniche.anonyshop.service;

/* File:   SpuService.java
 * -------------------------
 * Author: faniche
 * Date:   4/17/20
 */

import indi.faniche.anonyshop.bean.spu.PmsProductInfo;

import java.util.List;

public interface SpuService {
    List<PmsProductInfo> getSpuList(String catalog3Id);

    PmsProductInfo addPsuInfo(PmsProductInfo pmsProductInfo);

    PmsProductInfo editSpuInfo(PmsProductInfo pmsProductInfo);

    void deleteSpuInfo(PmsProductInfo pmsProductInfo);
}
