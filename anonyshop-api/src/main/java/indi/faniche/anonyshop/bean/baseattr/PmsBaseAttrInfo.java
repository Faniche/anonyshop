package indi.faniche.anonyshop.bean.baseattr;

import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Transient;
import java.io.Serializable;
import java.util.List;

/**
 * @File: PmsBaseAttrInfo
 * @author: Faniche
 * @since: 2020-04-03 18:44:03
 */

public class PmsBaseAttrInfo implements Serializable {
    private static final long serialVersionUID = -75162432765598121L;
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private String id;
    
    private String attrName;
    
    private String catalog3Id;

    @Transient
    private List<PmsBaseAttrValue> attrValueList;

    @Transient
    private String attrValueListStr;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getAttrName() {
        return attrName;
    }

    public void setAttrName(String attrName) {
        this.attrName = attrName;
    }

    public String getCatalog3Id() {
        return catalog3Id;
    }

    public void setCatalog3Id(String catalog3Id) {
        this.catalog3Id = catalog3Id;
    }

    public List<PmsBaseAttrValue> getAttrValueList() {
        return attrValueList;
    }

    public void setAttrValueList(List<PmsBaseAttrValue> attrValueList) {
        this.attrValueList = attrValueList;
    }

    public String getAttrValueListStr() {
        return attrValueListStr;
    }

    public void setAttrValueListStr(String attrValueListStr) {
        this.attrValueListStr = attrValueListStr;
    }
}