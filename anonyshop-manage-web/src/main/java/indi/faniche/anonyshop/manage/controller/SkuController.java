package indi.faniche.anonyshop.manage.controller;

/* File:   SkuController.java
 * -------------------------
 * Author: faniche
 * Date:   4/21/20
 */

import com.alibaba.dubbo.config.annotation.Reference;
import indi.faniche.anonyshop.bean.sku.PmsSkuInfo;
import indi.faniche.anonyshop.service.SkuService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@CrossOrigin
@RequestMapping("sku")
public class SkuController {

    @Reference
    SkuService skuService;

    @RequestMapping
    public String toSkuManage(){
        return "skumanage";
    }

    @RequestMapping("saveSkuInfo")
    @ResponseBody
    public String saveSkuInfo(@RequestBody PmsSkuInfo pmsSkuInfo){
        // 使用隐藏域传递productid
//        pmsSkuInfo.setProductId(pmsSkuInfo.getSpuId());

        // 处理默认图片
        String skuDefaultImg = pmsSkuInfo.getSkuDefaultImg();
        if(StringUtils.isBlank(skuDefaultImg)){
            pmsSkuInfo.setSkuDefaultImg(pmsSkuInfo.getSkuImageList().get(0).getImgUrl());
        }
        // skuService.saveSkuInfo(pmsSkuInfo);
        return "success";
    }
}
