package indi.faniche.anonyshop.product.service.impl;

/* File:   SpuServiceImpl.java
 * -------------------------
 * Author: faniche
 * Date:   4/17/20
 */

import com.alibaba.dubbo.config.annotation.Service;
import indi.faniche.anonyshop.bean.spu.PmsProductImage;
import indi.faniche.anonyshop.bean.spu.PmsProductInfo;
import indi.faniche.anonyshop.bean.spu.PmsProductSaleAttr;
import indi.faniche.anonyshop.bean.spu.PmsProductSaleAttrValue;
import indi.faniche.anonyshop.product.mapper.PmsProductImageMapper;
import indi.faniche.anonyshop.product.mapper.PmsProductInfoMapper;
import indi.faniche.anonyshop.product.mapper.PmsProductSaleAttrMapper;
import indi.faniche.anonyshop.product.mapper.PmsProductSaleAttrValueMapper;
import indi.faniche.anonyshop.service.SpuService;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

@Service
public class SpuServiceImpl implements SpuService {

    @Autowired
    PmsProductInfoMapper pmsProductInfoMapper;

    @Autowired
    PmsProductSaleAttrMapper pmsProductSaleAttrMapper;

    @Autowired
    PmsProductSaleAttrValueMapper pmsProductSaleAttrValueMapper;

    @Autowired
    PmsProductImageMapper pmsProductImageMapper;

    @Override
    public List<PmsProductInfo> getSpuList(String catalog3Id) {
        // 查询 PmsProductInfo 信息
        PmsProductInfo productInfo = new PmsProductInfo();
        productInfo.setCatalog3Id(catalog3Id);
        List<PmsProductInfo> pmsProductInfoList = pmsProductInfoMapper.select(productInfo);

        // 查询销售属性集合
        for (PmsProductInfo pmsProductInfo : pmsProductInfoList) {
            PmsProductSaleAttr pmsProductSaleAttr = new PmsProductSaleAttr();
            pmsProductSaleAttr.setProductId(pmsProductInfo.getId());
            List<PmsProductSaleAttr> pmsProductSaleAttrList = pmsProductSaleAttrMapper.select(pmsProductSaleAttr);

            // 遍历销售属性集合，查询销售属性值的集合
            for (PmsProductSaleAttr productSaleAttr : pmsProductSaleAttrList) {
                PmsProductSaleAttrValue pmsProductSaleAttrValue = new PmsProductSaleAttrValue();
                pmsProductSaleAttrValue.setSaleAttrId(productSaleAttr.getSaleAttrId());
                pmsProductSaleAttrValue.setProductId(pmsProductInfo.getId());
                List<PmsProductSaleAttrValue> pmsProductSaleAttrValueList = pmsProductSaleAttrValueMapper.select(pmsProductSaleAttrValue);
                productSaleAttr.setSpuSaleAttrValueList(pmsProductSaleAttrValueList);
                String spuSaleAttrValueListStr = "";
                for (PmsProductSaleAttrValue tmp : pmsProductSaleAttrValueList) {
                    spuSaleAttrValueListStr += tmp.getSaleAttrValueName() + " ";
                }
                productSaleAttr.setSpuSaleAttrValueListStr(spuSaleAttrValueListStr);
            }
            pmsProductInfo.setSpuSaleAttrList(pmsProductSaleAttrList);
        }
        return pmsProductInfoList;
    }

    @Override
    public PmsProductInfo addPsuInfo(PmsProductInfo pmsProductInfo) {
        pmsProductInfoMapper.insertSelective(pmsProductInfo);
        return pmsProductInfo;
    }

    @Override
    public PmsProductInfo editSpuInfo(PmsProductInfo pmsProductInfo) {
        pmsProductInfoMapper.updateByPrimaryKeySelective(pmsProductInfo);
        return pmsProductInfo;
    }

    @Override
    public void deleteSpuInfo(PmsProductInfo pmsProductInfo) {
        // 删除spu
        pmsProductInfoMapper.deleteByPrimaryKey(pmsProductInfo);
        // 删除销售值
        PmsProductSaleAttrValue pmsProductSaleAttrValue = new PmsProductSaleAttrValue();
        pmsProductSaleAttrValue.setProductId(pmsProductInfo.getId());
        pmsProductSaleAttrValueMapper.delete(pmsProductSaleAttrValue);
        // 删除销售属性
        PmsProductSaleAttr productSaleAttr = new PmsProductSaleAttr();
        productSaleAttr.setProductId(pmsProductInfo.getId());
        pmsProductSaleAttrMapper.delete(productSaleAttr);
        // 删除图片
        PmsProductImage pmsProductImage = new PmsProductImage();
        pmsProductImage.setProductId(pmsProductInfo.getId());
        pmsProductImageMapper.delete(pmsProductImage);
    }

    @Override
    public List<PmsProductSaleAttr> spuSaleAttrList(String spuId) {
        PmsProductSaleAttr pmsProductSaleAttr = new PmsProductSaleAttr();
        pmsProductSaleAttr.setProductId(spuId);
        List<PmsProductSaleAttr> PmsProductSaleAttrs = pmsProductSaleAttrMapper.select(pmsProductSaleAttr);
        for (PmsProductSaleAttr productSaleAttr : PmsProductSaleAttrs) {
            PmsProductSaleAttrValue pmsProductSaleAttrValue = new PmsProductSaleAttrValue();
            pmsProductSaleAttrValue.setProductId(spuId);
            pmsProductSaleAttrValue.setSaleAttrId(productSaleAttr.getSaleAttrId());
            List<PmsProductSaleAttrValue> pmsProductSaleAttrValues = pmsProductSaleAttrValueMapper.select(pmsProductSaleAttrValue);
            productSaleAttr.setSpuSaleAttrValueList(pmsProductSaleAttrValues);
        }
        return PmsProductSaleAttrs;
    }

    @Override
    public List<PmsProductImage> spuImageList(String spuId) {
        PmsProductImage pmsProductImage = new PmsProductImage();
        pmsProductImage.setProductId(spuId);
        List<PmsProductImage> pmsProductImages = pmsProductImageMapper.select(pmsProductImage);
        return pmsProductImages;
    }

}
