package indi.faniche.anonyshop.bean.sku;

import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Transient;
import java.io.Serializable;
import java.util.List;

/**
 * @File: PmsSkuAttrValue
 * @author: Faniche
 * @since: 2020-04-03 18:44:03
 */

public class PmsSkuAttrValue implements Serializable {
    private static final long serialVersionUID = -98428005707265332L;
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private String id;
    
    private String attrId;
    
    private String valueId;
    
    private String skuId;

    @Transient
    private List<String> valueIdList;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getAttrId() {
        return attrId;
    }

    public void setAttrId(String attrId) {
        this.attrId = attrId;
    }

    public String getValueId() {
        return valueId;
    }

    public void setValueId(String valueId) {
        this.valueId = valueId;
    }

    public String getSkuId() {
        return skuId;
    }

    public void setSkuId(String skuId) {
        this.skuId = skuId;
    }

    public List<String> getValueIdList() {
        return valueIdList;
    }

    public void setValueIdList(List<String> valueIdList) {
        this.valueIdList = valueIdList;
    }

}