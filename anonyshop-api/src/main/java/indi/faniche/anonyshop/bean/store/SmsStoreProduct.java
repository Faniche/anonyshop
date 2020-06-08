package indi.faniche.anonyshop.bean.store;

import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.io.Serializable;
import java.math.BigDecimal;

/**
 * @file: SmsStoreProduct
 * @author Faniche
 * @date: 2020-04-09 09:28:51
 */

public class SmsStoreProduct implements Serializable {
    private static final long serialVersionUID = 539281936991687672L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private String id;

    private String porductId;

    private String storeId;

    private String skuId;

    private BigDecimal score;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getPorductId() {
        return porductId;
    }

    public void setPorductId(String porductId) {
        this.porductId = porductId;
    }

    public String getStoreId() {
        return storeId;
    }

    public void setStoreId(String storeId) {
        this.storeId = storeId;
    }

    public String getSkuId() {
        return skuId;
    }

    public void setSkuId(String skuId) {
        this.skuId = skuId;
    }

    public BigDecimal getScore() {
        return score;
    }

    public void setScore(BigDecimal score) {
        this.score = score;
    }
}