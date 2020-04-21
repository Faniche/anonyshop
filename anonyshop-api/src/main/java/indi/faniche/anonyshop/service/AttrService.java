package indi.faniche.anonyshop.service;

/* File:   AttrService.java
 * -------------------------
 * Author: faniche
 * Date:   4/14/20
 */

import indi.faniche.anonyshop.bean.baseattr.PmsBaseAttrInfo;
import indi.faniche.anonyshop.bean.baseattr.PmsBaseSaleAttr;
import indi.faniche.anonyshop.bean.spu.PmsProductSaleAttr;

import java.util.List;

public interface AttrService {
    List<PmsBaseAttrInfo> getAttrInfoList(String catalog3Id);

    void deleteAttrInfo(PmsBaseAttrInfo pmsBaseAttrInfo);

    void editAttrInfo(PmsBaseAttrInfo pmsBaseAttrInfo);

    void addAttrInfo(PmsBaseAttrInfo pmsBaseAttrInfo);

    List<PmsBaseSaleAttr> baseSaleAttrList();

    PmsProductSaleAttr addProdSaleAttr(PmsProductSaleAttr pmsProductSaleAttr);

    PmsProductSaleAttr deleteSaleAttr(PmsProductSaleAttr pmsProductSaleAttr);
}
