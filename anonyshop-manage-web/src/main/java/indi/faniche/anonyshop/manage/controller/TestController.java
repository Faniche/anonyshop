package indi.faniche.anonyshop.manage.controller;

/* File:   TestController.java
 * -------------------------
 * Author: faniche
 * Date:   4/23/20
 */

import com.alibaba.dubbo.config.annotation.Reference;
import indi.faniche.anonyshop.bean.spu.PmsProductImage;
import indi.faniche.anonyshop.service.ProductImageService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
public class TestController {

    @Reference
    ProductImageService productImageService;

    @RequestMapping("thymeleaf_test")
    public String thymeleafTest(Model model, String productId){
        List<PmsProductImage> pmsProductImages = productImageService.getImgListBySpuId(productId);
        String msg = "hello";
        model.addAttribute("msg", msg);
        return "atest";
    }
}
