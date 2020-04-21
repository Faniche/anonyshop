package indi.faniche.anonyshop.manage.controller;

/* File:   IndexController.java
 * -------------------------
 * Author: faniche
 * Date:   4/13/20
 */

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class IndexController {
    @RequestMapping("index")
    public ModelAndView index(){
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("index");
        return modelAndView;
    }

    /*查询当日成交金额*/
    private double getTrunover(){
        double result = 0;
        return result;
    }

    /*查询总的成交金额*/
    private double getHistoryTrunover(){
        double result = 0;
        return result;
    }

    /*查询当日未发货的订单占当日总订单的百分比*/
    private double getOrderFinPercent(){
        double result = 0;
        return result;
    }

    /*查询为处理的售后请求数量*/
    private int getSaleSupport(){
        return 1;
    }
}
