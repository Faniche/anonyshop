package indi.faniche.anonyshop.product.service.impl;

/* File:   ProductImageServiceImpl.java
 * -------------------------
 * Author: faniche
 * Date:   4/20/20
 */

import com.alibaba.dubbo.config.annotation.Service;
import indi.faniche.anonyshop.bean.spu.PmsProductImage;
import indi.faniche.anonyshop.product.mapper.PmsProductImageMapper;
import indi.faniche.anonyshop.service.ProductImageService;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

@Service
public class ProductImageServiceImpl implements ProductImageService {

    @Autowired
    PmsProductImageMapper pmsProductImageMapper;

    @Override
    public void saveImage(PmsProductImage productImage) {
        pmsProductImageMapper.insert(productImage);
    }

    @Override
    public List<PmsProductImage> getImgListBySpuId(String productId) {
        PmsProductImage pmsProductImage = new PmsProductImage();
        pmsProductImage.setProductId(productId);
        return pmsProductImageMapper.select(pmsProductImage);
    }
}
