package indi.faniche.anonyshop.product.mapper;

/* File:   PmsSkuInfoMapper.java
 * -------------------------
 * Author: faniche
 * Date:   4/22/20
 */

import indi.faniche.anonyshop.bean.sku.PmsSkuInfo;
import indi.faniche.anonyshop.modol.FilterItem;
import org.apache.ibatis.annotations.Param;
import tk.mybatis.mapper.common.Mapper;

import java.util.List;

public interface PmsSkuInfoMapper extends Mapper<PmsSkuInfo> {
    List<PmsSkuInfo> selectSkuSaleAttrValueListBySpu(String productId);
    }
