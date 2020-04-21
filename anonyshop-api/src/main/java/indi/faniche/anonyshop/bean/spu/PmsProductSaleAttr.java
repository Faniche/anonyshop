package indi.faniche.anonyshop.bean.spu;

import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Transient;
import java.io.Serializable;
import java.util.List;

/**
 * @File: PmsProductSaleAttr
 * @author: Faniche
 * @since: 2020-04-03 18:44:03
 */

public class PmsProductSaleAttr implements Serializable {
    private static final long serialVersionUID = 291756275479116246L;
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private String id;
    
    private String productId;
    
    private String saleAttrId;
    
    private String saleAttrName;

    @Transient
    private List<PmsProductSaleAttrValue> spuSaleAttrValueList;

    @Transient
    private String spuSaleAttrValueListStr = "";

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getProductId() {
        return productId;
    }

    public void setProductId(String productId) {
        this.productId = productId;
    }

    public String getSaleAttrId() {
        return saleAttrId;
    }

    public void setSaleAttrId(String saleAttrId) {
        this.saleAttrId = saleAttrId;
    }

    public String getSaleAttrName() {
        return saleAttrName;
    }

    public void setSaleAttrName(String saleAttrName) {
        this.saleAttrName = saleAttrName;
    }

    public List<PmsProductSaleAttrValue> getSpuSaleAttrValueList() {
        return spuSaleAttrValueList;
    }

    public void setSpuSaleAttrValueList(List<PmsProductSaleAttrValue> spuSaleAttrValueList) {
        this.spuSaleAttrValueList = spuSaleAttrValueList;
    }

    public String getSpuSaleAttrValueListStr() {
        return spuSaleAttrValueListStr;
    }

    public void setSpuSaleAttrValueListStr(String spuSaleAttrValueListStr) {
        this.spuSaleAttrValueListStr = spuSaleAttrValueListStr;
    }
}