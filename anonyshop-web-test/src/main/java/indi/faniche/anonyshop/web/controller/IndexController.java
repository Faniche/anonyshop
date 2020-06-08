package indi.faniche.anonyshop.web.controller;

/* File:   IndexController.java
 * -------------------------
 * Author: faniche
 * Date:   4/30/20
 */

import com.alibaba.dubbo.config.annotation.Reference;
import indi.faniche.anonyshop.annotations.LoginRequired;
import indi.faniche.anonyshop.bean.sku.PmsSkuInfo;
import indi.faniche.anonyshop.service.SkuService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@Controller
@CrossOrigin
public class IndexController {

    @Reference
    SkuService skuService;

    @RequestMapping(value = {"", "/", "index"})
    @LoginRequired(loginSuccess =false)
    public String toIndex(HttpServletRequest request, Model model){
        String username = (String) request.getAttribute("username");
        String userId = (String) request.getAttribute("userId");
         List<PmsSkuInfo> popularProduct = skuService.getPopularProduct();
//        List<PmsSkuInfo> popularProduct = skuService.getClassSearchPageRandomList(1).getList();
        model.addAttribute("username", username);
        model.addAttribute("userId", userId);
        model.addAttribute("popularProduct", popularProduct);
        return "common/index";
    }
}
