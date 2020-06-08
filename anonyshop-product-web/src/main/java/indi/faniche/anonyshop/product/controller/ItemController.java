package indi.faniche.anonyshop.product.controller;

/* File:   ItemController.java
 * -------------------------
 * Author: faniche
 * Date:   4/24/20
 */

import com.alibaba.dubbo.config.annotation.Reference;
import com.alibaba.fastjson.JSON;
import indi.faniche.anonyshop.annotations.LoginRequired;
import indi.faniche.anonyshop.bean.comment.PmsComment;
import indi.faniche.anonyshop.bean.sku.PmsSkuInfo;
import indi.faniche.anonyshop.bean.sku.PmsSkuSaleAttrValue;
import indi.faniche.anonyshop.bean.spu.PmsProductSaleAttr;
import indi.faniche.anonyshop.bean.store.SmsStore;
import indi.faniche.anonyshop.bean.store.SmsStoreProduct;
import indi.faniche.anonyshop.service.*;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;
import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@CrossOrigin
public class ItemController {

    @Reference
    SkuService skuService;

    @Reference
    SpuService spuService;

    @Reference
    StoreService storeService;

    @Reference
    SmsStoreProductService smsStoreProductService;

    @Reference
    CommentService commentService;

    @RequestMapping("{skuId}.html")
    @LoginRequired(loginSuccess = false)
    public String item(@PathVariable String skuId, Model model, HttpServletRequest request){
        String username = (String) request.getAttribute("username");
        String userId = (String) request.getAttribute("userId");
        PmsSkuInfo pmsSkuInfo = skuService.getSkuById(skuId, request.getRemoteAddr());
        SmsStore smsStore = storeService.getStoreByStoreId(pmsSkuInfo.getStoreId());
        BigDecimal productScore = smsStoreProductService.getStoreProcuctScore(pmsSkuInfo.getProductId(), pmsSkuInfo.getStoreId());
        model.addAttribute("productScore", productScore);
        List<PmsComment> comments = commentService.getComments(pmsSkuInfo.getProductId(), pmsSkuInfo.getStoreId());
        model.addAttribute("comments", comments);
        pmsSkuInfo.setSmsStore(smsStore);
        model.addAttribute("skuInfo", pmsSkuInfo);
        List<PmsProductSaleAttr> pmsProductSaleAttrs = spuService.spuSaleAttrListCheckBySku(pmsSkuInfo.getProductId(),pmsSkuInfo.getId());
        model.addAttribute("spuSaleAttrListCheckBySku",pmsProductSaleAttrs);
        Map<String, String> skuSaleAttrHash = new HashMap<>();
        List<PmsSkuInfo> pmsSkuInfos = skuService.getSkuSaleAttrValueListBySpu(pmsSkuInfo.getProductId());
        for (PmsSkuInfo skuInfo : pmsSkuInfos) {
            String k = "";
            String v = skuInfo.getId();
            List<PmsSkuSaleAttrValue> skuSaleAttrValueList = skuInfo.getSkuSaleAttrValueList();
            for (PmsSkuSaleAttrValue pmsSkuSaleAttrValue : skuSaleAttrValueList) {
                k += pmsSkuSaleAttrValue.getSaleAttrValueId() + "|";
            }
            skuSaleAttrHash.put(k,v);
        }
        String skuSaleAttrHashJsonStr = JSON.toJSONString(skuSaleAttrHash);
        model.addAttribute("skuSaleAttrHashJsonStr",skuSaleAttrHashJsonStr);
        model.addAttribute("username", username);
        model.addAttribute("userId", userId);
        return "product/product-page";
    }
}
