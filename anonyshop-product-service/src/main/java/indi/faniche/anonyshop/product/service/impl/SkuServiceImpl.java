package indi.faniche.anonyshop.product.service.impl;

/* File:   SkuServiceImpl.java
 * -------------------------
 * Author: faniche
 * Date:   4/22/20
 */

import com.alibaba.dubbo.config.annotation.Service;
import indi.faniche.anonyshop.bean.sku.PmsSkuAttrValue;
import indi.faniche.anonyshop.bean.sku.PmsSkuImage;
import indi.faniche.anonyshop.bean.sku.PmsSkuInfo;
import indi.faniche.anonyshop.bean.sku.PmsSkuSaleAttrValue;
import indi.faniche.anonyshop.product.mapper.PmsSkuAttrValueMapper;
import indi.faniche.anonyshop.product.mapper.PmsSkuImageMapper;
import indi.faniche.anonyshop.product.mapper.PmsSkuInfoMapper;
import indi.faniche.anonyshop.product.mapper.PmsSkuSaleAttrValueMapper;
import indi.faniche.anonyshop.service.SkuService;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

@Service
public class SkuServiceImpl implements SkuService {

    @Autowired
    PmsSkuInfoMapper pmsSkuInfoMapper;

    @Autowired
    PmsSkuAttrValueMapper pmsSkuAttrValueMapper;

    @Autowired
    PmsSkuSaleAttrValueMapper pmsSkuSaleAttrValueMapper;

    @Autowired
    PmsSkuImageMapper pmsSkuImageMapper;

    @Override
    public void saveSkuInfo(PmsSkuInfo pmsSkuInfo) {
        // 插入skuInfo
        pmsSkuInfoMapper.insertSelective(pmsSkuInfo);
        String skuId = pmsSkuInfo.getId();
        // 插入平台属性关联
        List<PmsSkuAttrValue> skuAttrValueList = pmsSkuInfo.getSkuAttrValueList();
        for (PmsSkuAttrValue pmsSkuAttrValue : skuAttrValueList) {
            if (pmsSkuAttrValue.getAttrId() != null){
                pmsSkuAttrValue.setSkuId(skuId);
                pmsSkuAttrValueMapper.insertSelective(pmsSkuAttrValue);
            }
        }
        // 插入销售属性关联
        List<PmsSkuSaleAttrValue> skuSaleAttrValueList = pmsSkuInfo.getSkuSaleAttrValueList();
        for (PmsSkuSaleAttrValue pmsSkuSaleAttrValue : skuSaleAttrValueList) {
            if (pmsSkuSaleAttrValue.getSaleAttrId() != null){
                pmsSkuSaleAttrValue.setSkuId(skuId);
                pmsSkuSaleAttrValueMapper.insertSelective(pmsSkuSaleAttrValue);
            }
        }
        // 插入图片信息
        List<PmsSkuImage> skuImageList = pmsSkuInfo.getSkuImageList();
        for (PmsSkuImage pmsSkuImage : skuImageList) {
            pmsSkuImage.setSkuId(skuId);
            pmsSkuImageMapper.insertSelective(pmsSkuImage);
        }
    }
}
