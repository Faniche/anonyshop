package indi.faniche.anonyshop.manage.controller;

/* File:   SkuController.java
 * -------------------------
 * Author: faniche
 * Date:   4/21/20
 */

import com.alibaba.dubbo.config.annotation.Reference;
import com.alibaba.fastjson.JSON;
import com.github.pagehelper.PageInfo;
import indi.faniche.anonyshop.annotations.LoginRequired;
import indi.faniche.anonyshop.bean.sku.PmsSkuInfo;
import indi.faniche.anonyshop.bean.store.SmsStore;
import indi.faniche.anonyshop.service.SkuService;
import indi.faniche.anonyshop.service.StoreService;
import indi.faniche.anonyshop.util.CookieUtil;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;

@Controller
@CrossOrigin
@RequestMapping("sku")
public class SkuController {

    @Reference
    SkuService skuService;

    @Reference
    StoreService storeService;

    @RequestMapping
    @LoginRequired
    public String toSkuManage(HttpServletRequest request, Model model){
        String userId = (String) request.getAttribute("userId");
        String username = (String) request.getAttribute("username");
        String roleId = (String) request.getAttribute("roleId");
        String storeId = getStoreId(request);
        String storeStr = CookieUtil.getCookieValue(request, "store", true);
        SmsStore store = JSON.parseObject(storeStr, SmsStore.class);
        //List<PmsSkuInfo> skuInfoList = skuService.getStoreSkuList(1, store.getId());

        PageInfo<PmsSkuInfo> skuList = skuService.getStoreSkuList(1, storeId);
        model.addAttribute("skuList", skuList);
        model.addAttribute("userId", userId);
        model.addAttribute("username", username);
        model.addAttribute("roleId", roleId);
        model.addAttribute("storeId", storeId);
        model.addAttribute("skuList", skuList);
        return "seller/skumanage";
    }

    @RequestMapping("list")
    public String skuList(Integer pageNum, HttpServletRequest request, Model model){
        PageInfo<PmsSkuInfo> skuInfoList = skuService.getStoreSkuList(pageNum, getStoreId(request));
        //PageInfo<PmsSkuInfo> skuList = new PageInfo<>(skuInfoList);
        model.addAttribute("skuList", skuInfoList);
        return "seller/skumanage::tab-skuList";
    }

    @RequestMapping("saveSkuInfo")
    @ResponseBody
    public Msg saveSkuInfo(HttpServletRequest request, PmsSkuInfo pmsSkuInfo){
        // 处理默认图片
        String skuDefaultImg = pmsSkuInfo.getSkuDefaultImg();
        if(StringUtils.isBlank(skuDefaultImg)){
            pmsSkuInfo.setSkuDefaultImg(pmsSkuInfo.getSkuImageList().get(0).getImgUrl());
            pmsSkuInfo.getSkuImageList().get(0).setIsDefault("true");
        }
        pmsSkuInfo.setBrandId("1");
        skuService.saveSkuInfo(pmsSkuInfo);
        Msg msg = new Msg("seccess");
        return msg;
    }

    @RequestMapping("editSkuInfo")
    @ResponseBody
    public String editSkuInfo(PmsSkuInfo pmsSkuInfo){
        skuService.modifySkuInfo(pmsSkuInfo);
        return JSON.toJSONString("success");
    }

    @RequestMapping("delSkuInfo")
    public String delSkuInfo(HttpServletRequest request, String skuId, Integer pageNum, Model model){
        skuService.delSkuInfo(skuId);
        PageInfo<PmsSkuInfo> skuInfoList = skuService.getStoreSkuList(pageNum, getStoreId(request));
        model.addAttribute("skuList", skuInfoList);
        return "seller/skumanage::tab-skuList";
    }

    private static final class Msg{
        private String msg;

        public Msg(String msg) {
            this.msg = msg;
        }

        public String getMsg() {
            return msg;
        }

        public void setMsg(String msg) {
            this.msg = msg;
        }
    }

    private String getStoreId(HttpServletRequest request){
        String storeStr = CookieUtil.getCookieValue(request, "store", true);
        SmsStore store = JSON.parseObject(storeStr, SmsStore.class);
        return store.getId();
    }
}
