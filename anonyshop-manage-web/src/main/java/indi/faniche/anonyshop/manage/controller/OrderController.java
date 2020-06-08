package indi.faniche.anonyshop.manage.controller;

/* File:   OrderController.java
 * -------------------------
 * Author: faniche
 * Date:   5/13/20
 */

import com.alibaba.dubbo.config.annotation.Reference;
import com.alibaba.fastjson.JSON;
import com.github.pagehelper.PageInfo;
import indi.faniche.anonyshop.annotations.LoginRequired;
import indi.faniche.anonyshop.bean.checkout.OmsDelivery;
import indi.faniche.anonyshop.bean.checkout.OmsOrder;
import indi.faniche.anonyshop.bean.sku.PmsSkuInfo;
import indi.faniche.anonyshop.bean.store.SmsStore;
import indi.faniche.anonyshop.service.DeliveryService;
import indi.faniche.anonyshop.service.OrderService;
import indi.faniche.anonyshop.service.StoreService;
import indi.faniche.anonyshop.util.CookieUtil;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;

@Controller
@CrossOrigin
@RequestMapping("order")
public class OrderController {
    @Reference
    OrderService orderService;

    @Reference
    StoreService storeService;

    @Reference
    DeliveryService deliveryService;

    @RequestMapping
    @LoginRequired
    public String toOrder(HttpServletRequest request, Model model){
        String username = (String) request.getAttribute("username");
        String userId = (String) request.getAttribute("userId");
        String roleId = (String) request.getAttribute("roleId");
        PageInfo<OmsOrder> orderList = null;
        if (roleId.equals("1")) {
            orderList = orderService.getUserOrderList(1, userId);
            for (OmsOrder omsOrder : orderList.getList()){
                String storeName = storeService.getStoreByStoreId(omsOrder.getStoreId()).getName();
                omsOrder.setStoreName(storeName);
            }
        } else {
            orderList = orderService.getStoreOrderList(1, getStoreId(request));
            String storeStr = CookieUtil.getCookieValue(request, "store", true);
            SmsStore store = JSON.parseObject(storeStr, SmsStore.class);
            String storeName = store.getName();
            for (OmsOrder omsOrder : orderList.getList()){
                omsOrder.setStoreName(storeName);
            }
        }
        model.addAttribute("username", username);
        model.addAttribute("userId", userId);
        model.addAttribute("roleId", roleId);
        model.addAttribute("orderList", orderList);
        return "user_seller/order";
    }

    @RequestMapping("getOrder")
    public String toOrder(String orderId, Model model){
        OmsOrder omsOrder = orderService.getOrderById(orderId);
        model.addAttribute("commentOrderItems", omsOrder.getOmsOrderItemList());
        model.addAttribute("storeId", omsOrder.getStoreId());

        return "user_seller/delivery::order-comment";
    }

    @RequestMapping("list")
    @LoginRequired
    public String orderList(Integer pageNum, HttpServletRequest request, Model model){
        String username = (String) request.getAttribute("username");
        String userId = (String) request.getAttribute("userId");
        String roleId = (String) request.getAttribute("roleId");
        PageInfo<OmsOrder> orderList = null;
        if (roleId.equals("1")) {
            orderList = orderService.getUserOrderList(pageNum, userId);
            for (OmsOrder omsOrder : orderList.getList()){
                String storeName = storeService.getStoreByStoreId(omsOrder.getStoreId()).getName();
                omsOrder.setStoreName(storeName);
            }
        } else {
            orderList = orderService.getStoreOrderList(pageNum, getStoreId(request));
            String storeStr = CookieUtil.getCookieValue(request, "store", true);
            SmsStore store = JSON.parseObject(storeStr, SmsStore.class);
            String storeName = store.getName();
            for (OmsOrder omsOrder : orderList.getList()){
                omsOrder.setStoreName(storeName);
            }
        }
        model.addAttribute("username", username);
        model.addAttribute("userId", userId);
        model.addAttribute("roleId", roleId);
        model.addAttribute("orderList", orderList);
        return "user_seller/order";
    }

    @RequestMapping("delete")
    @LoginRequired
    public String delOrder(Integer pageNum, String orderId, HttpServletRequest request, Model model){
        String username = (String) request.getAttribute("username");
        String userId = (String) request.getAttribute("userId");
        String roleId = (String) request.getAttribute("roleId");
        orderService.delOrderById(orderId);

        OmsDelivery delivery = new OmsDelivery();
        delivery.setOrderId(orderId);
        deliveryService.delDeliveryByOrderId(delivery);

        PageInfo<OmsOrder> orderList = orderService.getUserOrderList(pageNum, getStoreId(request));
        for (OmsOrder omsOrder : orderList.getList()){
            String storeName = storeService.getStoreByStoreId(omsOrder.getStoreId()).getName();
            omsOrder.setStoreName(storeName);
        }
        model.addAttribute("username", username);
        model.addAttribute("userId", userId);
        model.addAttribute("roleId", roleId);
        model.addAttribute("orderList", orderList);
        return "user_seller/order";
    }

    private String getStoreId(HttpServletRequest request){
        String storeStr = CookieUtil.getCookieValue(request, "store", true);
        SmsStore store = JSON.parseObject(storeStr, SmsStore.class);
        return store.getId();
    }
}
