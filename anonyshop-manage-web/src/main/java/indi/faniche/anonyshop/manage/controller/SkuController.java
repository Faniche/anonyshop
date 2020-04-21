package indi.faniche.anonyshop.manage.controller;

/* File:   SkuController.java
 * -------------------------
 * Author: faniche
 * Date:   4/21/20
 */

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@CrossOrigin
@RequestMapping("sku")
public class SkuController {

    @RequestMapping
    public String toSkuManage(){
        return "skumanage";
    }
}
