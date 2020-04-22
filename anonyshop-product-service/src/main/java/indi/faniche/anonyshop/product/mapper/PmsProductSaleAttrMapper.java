package indi.faniche.anonyshop.product.mapper;

/* File:   PmsProductSaleAttrMapper.java
 * -------------------------
 * Author: faniche
 * Date:   4/17/20
 */

import indi.faniche.anonyshop.bean.spu.PmsProductSaleAttr;
import org.apache.ibatis.annotations.Param;
import tk.mybatis.mapper.common.Mapper;

import java.util.List;

public interface PmsProductSaleAttrMapper extends Mapper<PmsProductSaleAttr> {
    List<PmsProductSaleAttr> selectSpuSaleAttrListCheckBySku(@Param("productId") String productId, @Param("skuId") String skuId);
}
