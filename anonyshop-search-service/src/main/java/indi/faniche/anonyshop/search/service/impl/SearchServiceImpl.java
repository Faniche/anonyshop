package indi.faniche.anonyshop.search.service.impl;

/* File:   SearchServiceImpl.java
 * -------------------------
 * Author: faniche
 * Date:   4/27/20
 */


import com.alibaba.dubbo.config.annotation.Service;
import indi.faniche.anonyshop.bean.sku.PmsSkuAttrValue;
import indi.faniche.anonyshop.bean.sku.PmsSkuInfo;
import indi.faniche.anonyshop.modol.ClassSearch;
import indi.faniche.anonyshop.modol.FilterItem;
import indi.faniche.anonyshop.product.mapper.PmsSkuAttrValueMapper;
import indi.faniche.anonyshop.product.mapper.PmsSkuInfoMapper;
import indi.faniche.anonyshop.service.SearchService;
import org.springframework.beans.factory.annotation.Autowired;
import tk.mybatis.mapper.entity.Example;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
public class SearchServiceImpl implements SearchService {

    @Autowired
    PmsSkuInfoMapper pmsSkuInfoMapper;

    @Autowired
    PmsSkuAttrValueMapper pmsSkuAttrValueMapper;

    @Override
    public List<PmsSkuInfo> classSearch(ClassSearch classSearch) {
        List<String> brandIdList = classSearch.getBrandList();
        List<FilterItem> filterItemList = classSearch.getFilterList();
        List<PmsSkuInfo> skuInfoList = new ArrayList<>();
        Example example1 = new Example(PmsSkuInfo.class);
        Example example2 = new Example(PmsSkuAttrValue.class);
        Set<String> skuSet = new HashSet<>();
        if (filterItemList != null) {
            for (FilterItem filterItem : filterItemList) {
                if (filterItem.getValue() == null) continue;
                example2.createCriteria().andEqualTo("attrId", filterItem.getKey()).andIn("valueId", filterItem.getValue());
                List<PmsSkuAttrValue> skuAttrValues = pmsSkuAttrValueMapper.selectByExample(example2);
                if (skuAttrValues.isEmpty()) continue;
                for (PmsSkuAttrValue skuAttrValue : skuAttrValues) {
                    skuSet.add(skuAttrValue.getSkuId());
                }
            }
        }
        if (brandIdList == null && !skuSet.isEmpty()) {
            example1.createCriteria().andIn("id", skuSet);
            skuInfoList = pmsSkuInfoMapper.selectByExample(example1);
            return skuInfoList;
        } else if (brandIdList != null && !skuSet.isEmpty()) {
            example1.createCriteria().andIn("brandId", brandIdList).andIn("id", skuSet);
            skuInfoList = pmsSkuInfoMapper.selectByExample(example1);
            return skuInfoList;
        } else {
            example1.createCriteria().andIn("brandId", brandIdList);
            skuInfoList = pmsSkuInfoMapper.selectByExample(example1);
            return skuInfoList;
        }
    }

    @Override
    public List<PmsSkuInfo> ambSearch(String keyWord) {
        Example example = new Example(PmsSkuInfo.class);
        example.createCriteria().andLike("skuName", keyWord);
        return pmsSkuInfoMapper.selectByExample(example);
    }
}
