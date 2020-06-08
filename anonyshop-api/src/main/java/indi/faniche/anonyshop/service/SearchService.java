package indi.faniche.anonyshop.service;

/* File:   SearchService.java
 * -------------------------
 * Author: faniche
 * Date:   4/27/20
 */

import indi.faniche.anonyshop.bean.sku.PmsSkuAttrValue;
import indi.faniche.anonyshop.bean.sku.PmsSkuInfo;
import indi.faniche.anonyshop.modol.ClassSearch;

import java.util.List;

public interface SearchService {
    List<PmsSkuInfo> classSearch(ClassSearch classSearch);

    List<PmsSkuInfo> ambSearch(String keyWord);
}
