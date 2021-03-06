package indi.faniche.anonyshop.manage.controller;

/* File:   SpuController.java
 * -------------------------
 * Author: faniche
 * Date:   4/17/20
 */

import com.alibaba.dubbo.config.annotation.Reference;
//import com.github.pagehelper.PageHelper;
import indi.faniche.anonyshop.annotations.LoginRequired;
import indi.faniche.anonyshop.bean.baseattr.PmsBaseSaleAttr;
import indi.faniche.anonyshop.bean.spu.PmsProductImage;
import indi.faniche.anonyshop.bean.spu.PmsProductInfo;
import indi.faniche.anonyshop.bean.spu.PmsProductSaleAttr;
import indi.faniche.anonyshop.manage.util.PmsUploadUtil;
import indi.faniche.anonyshop.service.AttrService;
import indi.faniche.anonyshop.service.ProductImageService;
import indi.faniche.anonyshop.service.SpuService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.ArrayList;
import java.util.List;

@Controller
@CrossOrigin
@RequestMapping("spu")
public class SpuController {

    @Reference
    SpuService spuService;

    @Reference
    AttrService attrService;

    @Reference
    ProductImageService productImageService;

    @RequestMapping
    @LoginRequired
    public String toSpuManage(HttpServletRequest request, Model model){
        String username = (String) request.getAttribute("username");
        String userId = (String) request.getAttribute("userId");
        String roleId = (String) request.getAttribute("roleId");
        model.addAttribute("username", username);
        model.addAttribute("userId", userId);
        model.addAttribute("roleId", roleId);
        return "admin/spumanage";
    }

    @RequestMapping("getSpuList")
    @ResponseBody
    public List<PmsProductInfo> getSpuList(HttpSession session, String catalog3Id) {
//        PageHelper.startPage()
        List<PmsProductInfo> pmsProductInfoList = spuService.getSpuList(catalog3Id);
        session.setAttribute("pmsProductInfoList", pmsProductInfoList);
        return pmsProductInfoList;
    }

    /* 编辑spu销售属性，先获取spu销售属性值, 包括空的销售属性 */
    @RequestMapping("getSpuSaleAttr")
    @ResponseBody
    public List<PmsProductSaleAttr> getSpuSaleAttr(HttpSession session, String spuIndex) {
        Integer index = Integer.valueOf(spuIndex);
        List<PmsProductInfo> pmsProductInfoList = (List<PmsProductInfo>) session.getAttribute("pmsProductInfoList");
        PmsProductInfo pmsProductInfo = pmsProductInfoList.get(index);
        List<PmsProductSaleAttr> productSaleAttrList = pmsProductInfo.getSpuSaleAttrList();
        List<PmsBaseSaleAttr> baseSaleAttrList = attrService.baseSaleAttrList();
        for (PmsBaseSaleAttr baseSaleAttr : baseSaleAttrList) {
            boolean isContain = false;
            for (PmsProductSaleAttr productSaleAttr : productSaleAttrList) {
                if (baseSaleAttr.getId().equals(productSaleAttr.getSaleAttrId())) {
                    isContain = true;
                    break;
                }
            }
            if (!isContain) {
                PmsProductSaleAttr tmp = new PmsProductSaleAttr();
                tmp.setSaleAttrId(baseSaleAttr.getId());
                tmp.setSaleAttrName(baseSaleAttr.getName());
                tmp.setProductId(pmsProductInfo.getId());
                productSaleAttrList.add(tmp);
            }
        }
        return productSaleAttrList;
    }

    /* 编辑spu销售属性，先获取spu销售属性值, 不包括空的销售属性 */
    @RequestMapping("getSpuSaleAttrNoNull")
    @ResponseBody
    public List<PmsProductSaleAttr> getSpuSaleAttrNoNull(HttpSession session, String spuIndex) {
        Integer index = Integer.valueOf(spuIndex);
        List<PmsProductInfo> pmsProductInfoList = (List<PmsProductInfo>) session.getAttribute("pmsProductInfoList");
        PmsProductInfo pmsProductInfo = pmsProductInfoList.get(index);
        List<PmsProductSaleAttr> productSaleAttrList = pmsProductInfo.getSpuSaleAttrList();
        return productSaleAttrList;
    }

    // 返回主键
    @RequestMapping("addSpuInfo")
    @ResponseBody
    public PmsProductInfo addSpuInfo(HttpSession session, PmsProductInfo pmsProductInfo) {
        PmsProductInfo tmp = spuService.addPsuInfo(pmsProductInfo);
        List<PmsProductImage> pmsProductImageList = (List<PmsProductImage>) session.getAttribute("pmsProductImageList");
        if (pmsProductImageList != null) {
            for (PmsProductImage productImage : pmsProductImageList) {
                productImage.setProductId(tmp.getId());
                productImageService.saveImage(productImage);
            }
            session.removeAttribute("pmsProductImageList");
        }
        return tmp;
    }

    @RequestMapping("editSpuInfo")
    @ResponseBody
    public PmsProductInfo editSpuInfo(PmsProductInfo pmsProductInfo) {
        return spuService.editSpuInfo(pmsProductInfo);
    }

    @RequestMapping("deleteSpuInfo")
    @ResponseBody
    public List<PmsProductInfo> deleteSpuInfo(HttpSession session, PmsProductInfo pmsProductInfo) {
        spuService.deleteSpuInfo(pmsProductInfo);
        return getSpuList(session, pmsProductInfo.getCatalog3Id());
    }



    /*================================================================================================================*/
    /* SKU 页面 */
    @RequestMapping("imageList")
    @ResponseBody
    public List<PmsProductImage> imageList(String productId){

        List<PmsProductImage> pmsProductImages = spuService.spuImageList(productId);
        return pmsProductImages;
    }

    @RequestMapping("saleAttrList")
    @ResponseBody
    public List<PmsProductSaleAttr> saleAttrList(String spuId){
        List<PmsProductSaleAttr> pmsProductSaleAttrs = spuService.spuSaleAttrList(spuId);
        return pmsProductSaleAttrs;
    }
}
