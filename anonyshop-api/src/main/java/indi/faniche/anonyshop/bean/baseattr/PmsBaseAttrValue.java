package indi.faniche.anonyshop.bean.baseattr;

import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.io.Serializable;

/**
 * @File: PmsBaseAttrValue
 * @author: Faniche
 * @since: 2020-04-03 18:44:03
 */

public class PmsBaseAttrValue implements Serializable {
    private static final long serialVersionUID = 479681170767911060L;
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private String id;
    
    private String valueName;
    
    private String attrId;
    

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getValueName() {
        return valueName;
    }

    public void setValueName(String valueName) {
        this.valueName = valueName;
    }

    public String getAttrId() {
        return attrId;
    }

    public void setAttrId(String attrId) {
        this.attrId = attrId;
    }

}