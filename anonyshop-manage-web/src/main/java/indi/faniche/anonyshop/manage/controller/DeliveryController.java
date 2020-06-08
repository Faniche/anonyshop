package indi.faniche.anonyshop.manage.controller;

/* File:   DeliveryController.java
 * -------------------------
 * Author: faniche
 * Date:   5/14/20
 */

import com.alibaba.dubbo.config.annotation.Reference;
import com.alibaba.fastjson.JSON;
import com.github.pagehelper.PageInfo;
import indi.faniche.anonyshop.annotations.LoginRequired;
import indi.faniche.anonyshop.bean.checkout.OmsDelivery;
import indi.faniche.anonyshop.bean.checkout.OmsOrder;
import indi.faniche.anonyshop.bean.checkout.OmsOrderItem;
import indi.faniche.anonyshop.bean.store.SmsStore;
import indi.faniche.anonyshop.bean.ware.WmsWareInfo;
import indi.faniche.anonyshop.service.*;
import indi.faniche.anonyshop.util.CookieUtil;
import indi.faniche.anonyshop.util.MailUtil;
import org.apache.commons.lang3.StringUtils;
import org.junit.jupiter.api.Order;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpServletRequest;
import java.util.Date;
import java.util.List;

@Controller
@CrossOrigin
@RequestMapping("delivery")
public class DeliveryController {
    @Reference
    DeliveryService deliveryService;

    @Reference
    OrderService orderService;

    @Reference
    StoreService storeService;

    @Reference
    WareService wareService;

    @Reference
    SkuService skuService;

    @RequestMapping(value = {"toExpress", ""})
    @LoginRequired
    public String toExpress(HttpServletRequest request, Integer pageNum, String orderId, Model model) {
        String username = (String) request.getAttribute("username");
        String userId = (String) request.getAttribute("userId");
        String roleId = (String) request.getAttribute("roleId");
        model.addAttribute("username", username);
        model.addAttribute("userId", userId);
        model.addAttribute("roleId", roleId);
        PageInfo<OmsDelivery> deliveryList = null;
        if (roleId.equals("1")) {
            deliveryList = deliveryService.getUserProgressDelivery(1, userId);
        } else {
            deliveryList = deliveryService.getStoreProgressDelivery(1, getStoreId(request));
        }
        model.addAttribute("deliveryList", deliveryList);
        if (StringUtils.isNotBlank(orderId)) {
            OmsDelivery delivery = deliveryService.getDeliveryByOrderId(orderId);
            model.addAttribute("delivery", delivery);
            String returnUrl = "http://manage.anonyshop.tech/order/list?pageNum=" + pageNum;
            model.addAttribute("returnUrl", returnUrl);
            OmsOrder order = orderService.getOrderById(orderId);
            model.addAttribute("order", order);
            List<WmsWareInfo> wareInfos = wareService.getAllWares(getStoreId(request));
            model.addAttribute("wareList", wareInfos);
            model.addAttribute("storeId", getStoreId(request));
        }
        return "user_seller/delivery";
    }

    @RequestMapping("list")
    public String deliveryList(HttpServletRequest request, Integer pageNum, String userId, Model model) {
        PageInfo<OmsDelivery> deliveryList = null;
        if (StringUtils.isNotBlank(userId)) {
            deliveryList = deliveryService.getUserProgressDelivery(pageNum, userId);
        } else {
            deliveryList = deliveryService.getStoreProgressDelivery(pageNum, getStoreId(request));
        }
        model.addAttribute("deliveryList", deliveryList);
        if (StringUtils.isNotBlank(userId)) {
            return "user_seller/delivery::tab-delivery-user";
        }
        return "user_seller/delivery::tab-delivery-seller";
    }

    @RequestMapping("arrive")
    public String deliveryArrive(HttpServletRequest request, String orderId, Integer pageNum, Model model) {
        OmsDelivery delivery = new OmsDelivery();
        delivery.setOrderId(orderId);
        delivery.setStatus("2");
        delivery = deliveryService.updateDeliveryByOrderId(delivery);

        OmsOrder order = new OmsOrder();
        order.setStatus("3");
        order.setId(orderId);
        orderService.updateOrderDeliveryStatus(order);

//        MailUtil.init();
//        MailUtil.setSubject("到货");
//        omsOrder = orderService.getOrderById(delivery.getOrderId());
//        String content = "您好，" + omsOrder.getReceiverName() + ":\n" +
//                "您的快递已送达，快递单号为：" + delivery.
//                MailUtil.setContent();

        PageInfo<OmsDelivery> deliveryList = deliveryService.getStoreProgressDelivery(pageNum, getStoreId(request));
        model.addAttribute("deliveryList", deliveryList);
        return "user_seller/delivery::tab-delivery-seller";
    }

    @RequestMapping("send")
    @ResponseBody
    public String sendExpress(OmsDelivery delivery, Model model) {
        deliveryService.sendDelivery(delivery);
        OmsOrder omsOrder = new OmsOrder();
        omsOrder.setId(delivery.getOrderId());
        omsOrder.setStatus("2");
        orderService.updateOrderDeliveryStatus(omsOrder);
        List<OmsOrderItem> orderItemList = orderService.getOrderItemListByOrderId(delivery.getOrderId());
        skuService.updateQuantity(orderItemList);

        return JSON.toJSONString("已发货");
    }

    @RequestMapping("confirmRcv")
    public String confirmRcv(String orderId, Integer pageNum, String userId, Model model) {
        OmsDelivery delivery = new OmsDelivery();
        delivery.setOrderId(orderId);
        delivery.setStatus("3");
        deliveryService.updateDeliveryByOrderId(delivery);

        OmsOrder omsOrder = new OmsOrder();
        omsOrder.setId(orderId);
        omsOrder.setStatus("4");
        omsOrder.setReceiveTime(new Date());
        orderService.updateOrderDeliveryStatus(omsOrder);

        PageInfo<OmsDelivery> deliveryList = deliveryService.getUserProgressDelivery(pageNum, userId);
        model.addAttribute("deliveryList", deliveryList);
        return "user_seller/delivery::tab-delivery-user";
    }

    private String getStoreId(HttpServletRequest request) {
        String storeStr = CookieUtil.getCookieValue(request, "store", true);
        SmsStore store = JSON.parseObject(storeStr, SmsStore.class);
        return store.getId();
    }
}
