package indi.faniche.anonyshop.manage.controller;

/* File:   StoreController.java
 * -------------------------
 * Author: faniche
 * Date:   5/9/20
 */

import com.alibaba.dubbo.config.annotation.Reference;
import com.alibaba.fastjson.JSON;
import indi.faniche.anonyshop.bean.store.SmsStore;
import indi.faniche.anonyshop.modol.Msg;
import indi.faniche.anonyshop.service.OrderService;
import indi.faniche.anonyshop.service.StoreService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import sun.security.pkcs11.Secmod;

import java.util.*;

@Controller
@CrossOrigin
public class StoreController {
    @Reference
    StoreService storeService;

    @Reference
    OrderService orderService;

    private static final long TWO_MONTH = 1000 * 60 * 60 * 24 * 60;

    @RequestMapping("store/close")
    @ResponseBody
    public String closeStore(String storeId){
        Date latestOrderDate = orderService.getStoreLatestOrderDate(storeId);
        Date now = new Date();
        Map<String, Object> msg = new HashMap<>();
        if (now.compareTo(latestOrderDate) >= TWO_MONTH) {
            storeService.colse(storeId, "3");
            msg.put("result", "success");
        } else {
            storeService.colse(storeId, "2");
            Date closeDate = new Date(latestOrderDate.getTime() + TWO_MONTH);
            msg.put("result", "fail");
            msg.put("closeDate", closeDate);
        }
        return JSON.toJSONString(msg);
    }

    @RequestMapping("store/stop")
    @ResponseBody
    public String stopStore(String storeId){
        Date latestOrderDate = orderService.getStoreLatestOrderDate(storeId);
        Date now = new Date();
        Map<String, Object> msg = new HashMap<>();
        if (now.compareTo(latestOrderDate) >= TWO_MONTH) {
            storeService.colse(storeId, "3");
            msg.put("result", "success");
        } else {
            storeService.colse(storeId, "2");
            Date closeDate = new Date(latestOrderDate.getTime() + TWO_MONTH);
            msg.put("result", "fail");
            msg.put("closeDate", closeDate);
        }
        return JSON.toJSONString(msg);
    }

    @RequestMapping("store/modify")
    public String modifyStore(SmsStore store, Model model){
        if (store.getId().equals("")) store.setId(null);
        store = storeService.addStore(store);
        model.addAttribute("store", store);
        Msg storeMsg = new Msg();
        storeMsg.setContent("修改成功");
        model.addAttribute("storeMsg", "修改成功");
        model.addAttribute("store", store);
        return "common/profile::div-store";
    }
}
