package indi.faniche.anonyshop.manage.controller;

/* File:   AttrController.java
 * -------------------------
 * Author: faniche
 * Date:   4/14/20
 */

import com.alibaba.dubbo.config.annotation.Reference;
import indi.faniche.anonyshop.bean.baseattr.PmsBaseAttrInfo;
import indi.faniche.anonyshop.bean.baseattr.PmsBaseSaleAttr;
import indi.faniche.anonyshop.bean.spu.PmsProductSaleAttr;
import indi.faniche.anonyshop.service.AttrService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

@Controller
@CrossOrigin
@RequestMapping("attr")
public class AttrController {

    @Reference
    AttrService attrService;

    /* 获取基本销售属性 */
    @RequestMapping("baseSaleAttrList")
    @ResponseBody
    public List<PmsBaseSaleAttr> baseSaleAttrList(){
        return attrService.baseSaleAttrList();
    }

    /* 获取三级分类下的平台属性 */
    @RequestMapping("getAttrInfoList")
    @ResponseBody
    public List<PmsBaseAttrInfo> getAttrInfoList(String catalog3Id){
        return attrService.getAttrInfoList(catalog3Id);
    }

    /* 添加平台属性 */
    @RequestMapping("addAttrInfo")
    @ResponseBody
    public List<PmsBaseAttrInfo> addAttrInfo(PmsBaseAttrInfo pmsBaseAttrInfo){
        attrService.addAttrInfo(pmsBaseAttrInfo);
        return attrService.getAttrInfoList(pmsBaseAttrInfo.getCatalog3Id());
    }

    /* 删除平台属性 */
    @RequestMapping("deleteAttrInfo")
    @ResponseBody
    public List<PmsBaseAttrInfo> deleteAttrInfo(PmsBaseAttrInfo pmsBaseAttrInfo){
        attrService.deleteAttrInfo(pmsBaseAttrInfo);
        return attrService.getAttrInfoList(pmsBaseAttrInfo.getCatalog3Id());
    }

    /* 编辑平台属性 */
    @RequestMapping("editAttrInfo")
    @ResponseBody
    public List<PmsBaseAttrInfo> editAttrInfo(PmsBaseAttrInfo pmsBaseAttrInfo){
        attrService.editAttrInfo(pmsBaseAttrInfo);
        return attrService.getAttrInfoList(pmsBaseAttrInfo.getCatalog3Id());
    }

    /*添加商品销售属性*/
    @RequestMapping("addProdSaleAttr")
    @ResponseBody
    public PmsProductSaleAttr addProdSaleAttr(PmsProductSaleAttr pmsProductSaleAttr){
        return attrService.addProdSaleAttr(pmsProductSaleAttr);
    }

    /*删除商品销售属性*/
    @RequestMapping("deleteSaleAttr")
    @ResponseBody
    public PmsProductSaleAttr deleteSaleAttr(PmsProductSaleAttr pmsProductSaleAttr){
        return attrService.deleteSaleAttr(pmsProductSaleAttr);
    }
}
