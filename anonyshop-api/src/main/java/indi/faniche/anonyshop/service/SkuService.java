package indi.faniche.anonyshop.service;

/* File:   SkuService.java
 * -------------------------
 * Author: faniche
 * Date:   4/22/20
 */

import com.github.pagehelper.PageInfo;
import indi.faniche.anonyshop.bean.checkout.OmsOrderItem;
import indi.faniche.anonyshop.bean.sku.PmsSkuInfo;

import java.math.BigDecimal;
import java.util.List;

public interface SkuService {
    void saveSkuInfo(PmsSkuInfo pmsSkuInfo);

    PmsSkuInfo getSkuById(String skuId, String ip);
//    PmsSkuInfo getSkuById(String skuId);

    List<PmsSkuInfo> getSkuSaleAttrValueListBySpu(String productId);

    boolean checkPrice(String productSkuId, BigDecimal price);

    PageInfo<PmsSkuInfo> getStoreSkuList(int page, String id);

    void modifySkuInfo(PmsSkuInfo pmsSkuInfo);

    void delSkuInfo(String skuId);

    PageInfo<PmsSkuInfo> getClassSearchPageRandomList(int pageNum);

    List<PmsSkuInfo> getPopularProduct();

    void updateQuantity(List<OmsOrderItem> orderItemList);
}
