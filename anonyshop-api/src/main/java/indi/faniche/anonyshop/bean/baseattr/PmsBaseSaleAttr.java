package indi.faniche.anonyshop.bean.baseattr;

import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.io.Serializable;

/**
 * @File: PmsBaseSaleAttr
 * @author: Faniche
 * @since: 2020-04-03 18:44:03
 */

public class PmsBaseSaleAttr implements Serializable {
    private static final long serialVersionUID = -85181968998104243L;
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private String id;
    
    private String name;
    

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

}