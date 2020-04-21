package indi.faniche.anonyshop.manage.controller;

/* File:   ProductController.java
 * -------------------------
 * Author: faniche
 * Date:   4/13/20
 */

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

@Controller
@CrossOrigin
public class ProductController {
    @RequestMapping("prodmanage")
    public ModelAndView toProductManage(){
        ModelAndView modelAndView = new ModelAndView("prodmanage");
        return modelAndView;
    }
}
