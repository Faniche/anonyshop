package indi.faniche.anonyshop.store.service.impl;

/* File:   SmsStoreProductServiceImpl.java
 * -------------------------
 * Author: faniche
 * Date:   5/17/20
 */

import com.alibaba.dubbo.config.annotation.Service;
import indi.faniche.anonyshop.bean.comment.PmsComment;
import indi.faniche.anonyshop.bean.store.SmsStoreProduct;
import indi.faniche.anonyshop.service.SmsStoreProductService;
import indi.faniche.anonyshop.store.mapper.SmsStoreProductMapper;
import org.springframework.beans.factory.annotation.Autowired;

import java.math.BigDecimal;
import java.util.List;

@Service
public class SmsStoreProductServiceImpl implements SmsStoreProductService {

    @Autowired
    SmsStoreProductMapper smsStoreProductMapper;

    @Override
    public void updateProductScore(List<PmsComment> pmsComments) {
        for (PmsComment pmsComment : pmsComments) {
            SmsStoreProduct smsStoreProduct = new SmsStoreProduct();
            smsStoreProduct.setPorductId(pmsComment.getProductId());
            smsStoreProduct.setStoreId(pmsComment.getStoreId());
            smsStoreProduct = smsStoreProductMapper.selectOne(smsStoreProduct);

            BigDecimal commentScore = getCommentScore(pmsComment);
            smsStoreProduct.setScore(smsStoreProduct.getScore().add(commentScore));
            smsStoreProductMapper.updateByPrimaryKeySelective(smsStoreProduct);
        }
    }

    @Override
    public BigDecimal getStoreProcuctScore(String productId, String storeId) {
        SmsStoreProduct smsStoreProduct = new SmsStoreProduct();
        smsStoreProduct.setPorductId(productId);
        smsStoreProduct.setStoreId(storeId);
        smsStoreProduct = smsStoreProductMapper.selectOne(smsStoreProduct);
        return smsStoreProduct.getScore();
    }

    private BigDecimal getCommentScore(PmsComment pmsComment) {
        double commentScore = pmsComment.getProductScore() * 0.5
                + pmsComment.getServiceScore() * 0.3
                + pmsComment.getDeliveryScore() * 0.2;
        if (commentScore >= 3) {
            commentScore = commentScore / 5 * 0.01;
            return new BigDecimal(commentScore);
        } else {
            commentScore = commentScore / 3 * 0.01;
            return new BigDecimal(commentScore).negate();
        }
    }
}