package indi.faniche.anonyshop.bean.sku;

import indi.faniche.anonyshop.bean.store.SmsStore;

import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Transient;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;

/**
 * @File: PmsSkuInfo
 * @author: Faniche
 * @since: 2020-04-03 18:44:03
 */

public class PmsSkuInfo implements Serializable {
    private static final long serialVersionUID = -46005726763736244L;
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private String id;
    
    private String productId;
    
    private BigDecimal price;
    
    private String skuName;
    
    private String skuDesc;
    
    private String weight;

    private String brandId;
    
    private String catalog3Id;
    
    private String skuDefaultImg;

    private String storeId;

    private String quantity;

    @Transient
    private SmsStore smsStore;

    @Transient
    List<PmsSkuImage> skuImageList;
    @Transient
    List<PmsSkuAttrValue> skuAttrValueList;
    @Transient
    List<PmsSkuSaleAttrValue> skuSaleAttrValueList;
    

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

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public String getSkuName() {
        return skuName;
    }

    public void setSkuName(String skuName) {
        this.skuName = skuName;
    }

    public String getSkuDesc() {
        return skuDesc;
    }

    public void setSkuDesc(String skuDesc) {
        this.skuDesc = skuDesc;
    }

    public String getWeight() {
        return weight;
    }

    public void setWeight(String weight) {
        this.weight = weight;
    }

    public String getBrandId() {
        return brandId;
    }

    public void setBrandId(String brandId) {
        this.brandId = brandId;
    }

    public String getCatalog3Id() {
        return catalog3Id;
    }

    public void setCatalog3Id(String catalog3Id) {
        this.catalog3Id = catalog3Id;
    }

    public String getSkuDefaultImg() {
        return skuDefaultImg;
    }

    public void setSkuDefaultImg(String skuDefaultImg) {
        this.skuDefaultImg = skuDefaultImg;
    }

    public String getStoreId() {
        return storeId;
    }

    public SmsStore getSmsStore() {
        return smsStore;
    }

    public void setSmsStore(SmsStore smsStore) {
        this.smsStore = smsStore;
    }

    public void setStoreId(String storeId) {
        this.storeId = storeId;
    }

    public List<PmsSkuImage> getSkuImageList() {
        return skuImageList;
    }

    public void setSkuImageList(List<PmsSkuImage> skuImageList) {
        this.skuImageList = skuImageList;
    }

    public List<PmsSkuAttrValue> getSkuAttrValueList() {
        return skuAttrValueList;
    }

    public void setSkuAttrValueList(List<PmsSkuAttrValue> skuAttrValueList) {
        this.skuAttrValueList = skuAttrValueList;
    }

    public List<PmsSkuSaleAttrValue> getSkuSaleAttrValueList() {
        return skuSaleAttrValueList;
    }

    public void setSkuSaleAttrValueList(List<PmsSkuSaleAttrValue> skuSaleAttrValueList) {
        this.skuSaleAttrValueList = skuSaleAttrValueList;
    }

    public String getQuantity() {
        return quantity;
    }

    public void setQuantity(String quantity) {
        this.quantity = quantity;
    }
}