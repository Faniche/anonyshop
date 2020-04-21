package indi.faniche.anonyshop.product.service.impl;

/* File:   CatalogServiceImpl.java
 * -------------------------
 * Author: faniche
 * Date:   4/12/20
 */
import com.alibaba.dubbo.config.annotation.Service;
import indi.faniche.anonyshop.bean.catalog.PmsBaseCatalog1;
import indi.faniche.anonyshop.bean.catalog.PmsBaseCatalog2;
import indi.faniche.anonyshop.bean.catalog.PmsBaseCatalog3;
import indi.faniche.anonyshop.product.mapper.PmsBaseCatalog1Mapper;
import indi.faniche.anonyshop.product.mapper.PmsBaseCatalog2Mapper;
import indi.faniche.anonyshop.product.mapper.PmsBaseCatalog3Mapper;
import indi.faniche.anonyshop.service.CatalogService;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

@Service
public class CatalogServiceImpl implements CatalogService {

    @Autowired
    PmsBaseCatalog1Mapper pmsBaseCatalog1Mapper;

    @Autowired
    PmsBaseCatalog2Mapper pmsBaseCatalog2Mapper;

    @Autowired
    PmsBaseCatalog3Mapper pmsBaseCatalog3Mapper;

    @Override
    public List<PmsBaseCatalog1> getCatalog1() {
        return pmsBaseCatalog1Mapper.selectAll();
    }

    @Override
    public List<PmsBaseCatalog2> getCatalog2(String catalog1Id) {
        PmsBaseCatalog2 pmsBaseCatalog2 = new PmsBaseCatalog2();
        pmsBaseCatalog2.setCatalog1Id(catalog1Id);
        return pmsBaseCatalog2Mapper.select(pmsBaseCatalog2);
    }

    @Override
    public List<PmsBaseCatalog3> getCatalog3(String catalog2Id) {
        PmsBaseCatalog3 pmsBaseCatalog3 = new PmsBaseCatalog3();
        pmsBaseCatalog3.setCatalog2Id(catalog2Id);
        return pmsBaseCatalog3Mapper.select(pmsBaseCatalog3);
    }
}