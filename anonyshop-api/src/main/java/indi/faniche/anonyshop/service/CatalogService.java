package indi.faniche.anonyshop.service;

import indi.faniche.anonyshop.bean.catalog.PmsBaseCatalog1;
import indi.faniche.anonyshop.bean.catalog.PmsBaseCatalog2;
import indi.faniche.anonyshop.bean.catalog.PmsBaseCatalog3;
import indi.faniche.anonyshop.bean.catalog.PmsBrand;

import java.util.List;

public interface CatalogService {
    List<PmsBaseCatalog1> getCatalog1();

    List<PmsBaseCatalog2> getCatalog2(String catalog1Id);

    List<PmsBaseCatalog3> getCatalog3(String catalog2Id);

    List<PmsBrand> getBrandList(String catalog3Id);
}
