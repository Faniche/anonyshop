package indi.faniche.anonyshop.service;

/* File:   ProductImageService.java
 * -------------------------
 * Author: faniche
 * Date:   4/20/20
 */

import indi.faniche.anonyshop.bean.spu.PmsProductImage;

import java.util.List;

public interface ProductImageService {
    void saveImage(PmsProductImage productImage);

    List<PmsProductImage> getImgListBySpuId(String productId);
}
