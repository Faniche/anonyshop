package indi.faniche.anonyshop.bean.catalog;

import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Transient;
import java.io.Serializable;
import java.util.List;

/**
 * @File: PmsBaseCatalog3
 * @author: Faniche
 * @since: 2020-04-03 18:44:03
 */

public class PmsBaseCatalog3 implements Serializable {
    private static final long serialVersionUID = 928467037476682339L;
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private String id;
    
    private String name;
    
    private String catalog2Id;

    @Transient
    private List<PmsBrand> brandList;

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

    public String getCatalog2Id() {
        return catalog2Id;
    }

    public void setCatalog2Id(String catalog2Id) {
        this.catalog2Id = catalog2Id;
    }

    public List<PmsBrand> getBrandList() {
        return brandList;
    }

    public void setBrandList(List<PmsBrand> brandList) {
        this.brandList = brandList;
    }
}