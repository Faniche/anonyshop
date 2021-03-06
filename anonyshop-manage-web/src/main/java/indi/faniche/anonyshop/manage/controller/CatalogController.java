package indi.faniche.anonyshop.manage.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import indi.faniche.anonyshop.bean.catalog.PmsBaseCatalog1;
import indi.faniche.anonyshop.bean.catalog.PmsBaseCatalog2;
import indi.faniche.anonyshop.bean.catalog.PmsBaseCatalog3;
import indi.faniche.anonyshop.bean.catalog.PmsBrand;
import indi.faniche.anonyshop.service.CatalogService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@Controller
@CrossOrigin
@RequestMapping("catalog")
public class CatalogController {

    @Reference
    CatalogService catalogService;

    @RequestMapping("getCatalog1")
    @ResponseBody
    public List<PmsBaseCatalog1> getCatalog1(){
        return catalogService.getCatalog1();
    }

    @RequestMapping("getCatalog2")
    @ResponseBody
    public List<PmsBaseCatalog2> getCatalog2(String catalog1Id){
        return catalogService.getCatalog2(catalog1Id);
    }

    @RequestMapping("getCatalog3")
    @ResponseBody
    public List<PmsBaseCatalog3> getCatalog3(String catalog2Id){
        return catalogService.getCatalog3(catalog2Id);
    }

    @RequestMapping("getBrandList")
    @ResponseBody
    public List<PmsBrand> getBrandList(String catalog3Id){
        return catalogService.getBrandList(catalog3Id);
    }


}
