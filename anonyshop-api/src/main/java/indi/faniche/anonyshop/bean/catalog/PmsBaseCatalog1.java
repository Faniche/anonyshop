package indi.faniche.anonyshop.bean.catalog;

import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Transient;
import java.io.Serializable;
import java.util.List;

/**
 * @File: PmsBaseCatalog1
 * @author: Faniche
 * @since: 2020-04-03 18:44:03
 */

public class PmsBaseCatalog1 implements Serializable {
    private static final long serialVersionUID = -28206981285282478L;
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private String id;
    
    private String name;
    
    @Transient
    private List<PmsBaseCatalog2> catalog2List;

    public List<PmsBaseCatalog2> getCatalog2List() {
        return catalog2List;
    }

    public void setCatalog2List(List<PmsBaseCatalog2> catalog2List) {
        this.catalog2List = catalog2List;
    }

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