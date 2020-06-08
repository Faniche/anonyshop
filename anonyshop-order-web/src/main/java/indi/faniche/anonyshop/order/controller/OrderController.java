package indi.faniche.anonyshop.order.controller;

/* File:   OrderController.java
 * -------------------------
 * Author: faniche
 * Date:   5/1/20
 */

import com.alibaba.dubbo.config.annotation.Reference;
import indi.faniche.anonyshop.annotations.LoginRequired;
import indi.faniche.anonyshop.bean.address.AddrCity;
import indi.faniche.anonyshop.bean.address.AddrCounty;
import indi.faniche.anonyshop.bean.address.AddrProvince;
import indi.faniche.anonyshop.bean.checkout.OmsCartItem;
import indi.faniche.anonyshop.bean.checkout.OmsOrder;
import indi.faniche.anonyshop.bean.checkout.OmsOrderItem;
import indi.faniche.anonyshop.bean.user.UmsDefaultAddress;
import indi.faniche.anonyshop.service.*;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

@Controller
@CrossOrigin
public class OrderController {

    @Reference
    UmsDefaultAddressService umsDefaultAddressService;

    @Reference
    CascadeAddressService cascadeAddressService;

    @Reference
    CartService cartService;

    @Reference
    OrderService orderService;

    @Reference
    SkuService skuService;

    @RequestMapping(value = {"toTrade", "", "/"})
    @LoginRequired(loginSuccess = true)
    public String toTrade(HttpServletRequest request, HttpServletResponse response, HttpSession session, Model model) {
        String userId = (String) request.getAttribute("userId");
        String username = (String) request.getAttribute("username");
        UmsDefaultAddress defaultAddress = umsDefaultAddressService.getDefaultAddressByLoginId(userId);
        // 将购物车集合转换为订单集合
        List<OmsCartItem> omsCartItems = cartService.cartList(userId);
        List<OmsOrderItem> omsOrderItems = new ArrayList<>();
        // 每循环一个购物车对象，就封装一个商品的详情到OmsOrderItem
        for (OmsCartItem omsCartItem : omsCartItems) {
            // 每循环一个购物车对象，就封装一个商品的详情到OmsOrderItem
            if (omsCartItem.getIsChecked().equals("1")) {
                omsOrderItems.add(getOrderItemFromCartItem(omsCartItem, true));
            }
        }
        List<AddrProvince> provinceList = cascadeAddressService.getAllProvince();
        model.addAttribute("provinceList", provinceList);
        model.addAttribute("defaultAddress", defaultAddress);
        model.addAttribute("orderList", omsOrderItems);
        model.addAttribute("username", username);
        model.addAttribute("userId", userId);
        model.addAttribute("totalPrice", getTotalAmount(omsCartItems));
        // 生成交易码，为了在提交订单时做交易码的校验
        String tradeCode = orderService.genTradeCode(userId);
        model.addAttribute("tradeCode", tradeCode);
        return "order/order";
    }

    @RequestMapping("getCityList")
    public String getCityList(String privinceId, Model model) {
        List<AddrCity> cityList = cascadeAddressService.getAllCityByProvinceId(privinceId);
        model.addAttribute("cityList", cityList);
        return "order/order::select-city";
    }

    @RequestMapping("getCountyList")
    public String getCountyList(String cityId, Model model) {
        List<AddrCounty> countyList = cascadeAddressService.getAllCountyByCityId(cityId);
        model.addAttribute("countyList", countyList);
        return "order/order::select-region";
    }

    @RequestMapping("submit")
    @LoginRequired(loginSuccess = true)
    public ModelAndView submitOrder(OmsOrder subOmsOrder, HttpServletRequest request) {
        String userId = (String) request.getAttribute("userId");
        String username = (String) request.getAttribute("username");
        // 校验 检查交易码
        String success = orderService.checkTradeCode(userId, subOmsOrder.getTradeCode());
        ModelAndView modelAndView = new ModelAndView();
        if (success.equals("success")) {
            // 生成订单基本信息
            subOmsOrder = genOmsOrder(subOmsOrder, userId, username);
            List<OmsOrderItem> omsOrderItems = new ArrayList<>();
            List<OmsCartItem> omsCartItems = cartService.cartList(userId);
            for (OmsCartItem omsCartItem : omsCartItems) {
                if (omsCartItem.getIsChecked().equals("1")) {
                    // 检价
                    boolean b = skuService.checkPrice(omsCartItem.getProductSkuId(), omsCartItem.getPrice());
                    if (b == false) {
                        modelAndView.setViewName("order/fail");
                        return modelAndView;
                    }
                    OmsOrderItem omsOrderItem = getOrderItemFromCartItem(omsCartItem, false);
                    // 验库存,远程调用库存系统
                    omsOrderItem.setOrderSn(subOmsOrder.getOrderSn());// 外部订单号，用来和其他系统进行交互，防止重复
                    omsOrderItem.setProductId(omsCartItem.getProductId());
                    omsOrderItem.setIntegrationAmount(new BigDecimal(0));
                    omsOrderItems.add(omsOrderItem);
                    subOmsOrder.setBillContent(subOmsOrder.getBillContent() != null? subOmsOrder.getBillContent() + omsCartItem.getProductName() + "\n" : omsCartItem.getProductName());
                }
            }
            subOmsOrder.setStoreId(omsCartItems.get(0).getStoreId());
            subOmsOrder.setOmsOrderItemList(omsOrderItems);
            // 将订单和订单详情写入数据库
            orderService.saveOrder(subOmsOrder);
            // 删除购物车的对应商品
            for (OmsCartItem omsCartItem : omsCartItems) {
                if (omsCartItem.getIsChecked().equals("1"))
                    cartService.deleteCartItem(userId, omsCartItem.getProductSkuId());
            }
            cartService.syncCartCache(userId);
            // 重定向到支付系统
            modelAndView.setViewName("redirect:http://payment.anonyshop.tech");
            modelAndView.addObject("outTradeNo", subOmsOrder.getOrderSn());
            modelAndView.addObject("totalAmount", subOmsOrder.getTotalAmount());
            return modelAndView;
        } else {
            modelAndView.setViewName("order/fail");
            return modelAndView;
        }
    }

    private OmsOrder genOmsOrder(OmsOrder subOmsOrder, String userId, String username) {
        subOmsOrder.setMemberId(userId);

        String outTradeNo = "anonyshop";
        outTradeNo = outTradeNo + System.currentTimeMillis();// 将毫秒时间戳拼接到外部订单号
        SimpleDateFormat sdf = new SimpleDateFormat("YYYYMMDDHHmmss");
        outTradeNo = outTradeNo + sdf.format(new Date());// 将时间字符串拼接到外部订单号
        subOmsOrder.setOrderSn(outTradeNo);//外部订单号

        subOmsOrder.setCreateTime(new Date());
        subOmsOrder.setTotalAmount(getTotalAmount(cartService.cartList(userId)));
        subOmsOrder.setFreightAmount(new BigDecimal("10")); //运费，支付后，在生成物流信息时
        subOmsOrder.setIntegrationAmount(new BigDecimal(0));
        subOmsOrder.setStatus("0");
        if (subOmsOrder.getSetDefaultAddress().equals("1")) {
            UmsDefaultAddress defaultAddress = umsDefaultAddressService.getDefaultAddressByLoginId(userId);
            subOmsOrder.setReceiverCity(defaultAddress.getCity());
            subOmsOrder.setReceiverDetailAddress(defaultAddress.getDetailAddress());
            subOmsOrder.setReceiverName(defaultAddress.getReceiverName());
            subOmsOrder.setReceiverPhone(defaultAddress.getPhone());
            subOmsOrder.setReceiverEmail(defaultAddress.getEmail());
            subOmsOrder.setReceiverProvince(defaultAddress.getProvince());
            subOmsOrder.setReceiverRegion(defaultAddress.getRegion());
        }
        subOmsOrder.setUseIntegration("0");
        subOmsOrder.setConfirmStatus(0);
        subOmsOrder.setAutoConfirmDay("7");

//        // 当前日期加一天，一天后配送
//        Calendar c = Calendar.getInstance();
//        c.add(Calendar.DATE, 1);
//        Date time = c.getTime();
//        subOmsOrder.setReceiveTime(time);

        subOmsOrder.setBillHeader(username);
        subOmsOrder.setBillReceiverEmail(subOmsOrder.getReceiverEmail());
        subOmsOrder.setBillReceiverPhone(subOmsOrder.getReceiverPhone());
        subOmsOrder.setBillType(0);
        return subOmsOrder;
    }

    private OmsOrderItem getOrderItemFromCartItem(OmsCartItem omsCartItem, boolean isToUser) {
        OmsOrderItem omsOrderItem = new OmsOrderItem();
        omsOrderItem.setProductPic(omsCartItem.getProductPic());
        omsOrderItem.setProductName(omsCartItem.getProductName());
        omsOrderItem.setProductPrice(omsCartItem.getPrice().multiply(new BigDecimal(omsCartItem.getQuantity())));
        omsOrderItem.setProductAttr(omsCartItem.getProductSaleAttr());
        omsOrderItem.setProductQuantity(omsCartItem.getQuantity());
        if (isToUser) {
            return omsOrderItem;
        }
        omsOrderItem.setProductId(omsCartItem.getProductId());
        omsOrderItem.setProductBrand(omsCartItem.getProductBrand());
        omsOrderItem.setProductSkuId(omsCartItem.getProductSkuId());
        omsOrderItem.setSp1(omsCartItem.getSp1() != null ? omsCartItem.getSp1() : null);
        omsOrderItem.setSp2(omsCartItem.getSp2() != null ? omsCartItem.getSp2() : null);
        omsOrderItem.setSp3(omsCartItem.getSp3() != null ? omsCartItem.getSp3() : null);
        omsOrderItem.setProductName(omsCartItem.getProductName());
        omsOrderItem.setProductPic(omsCartItem.getProductPic());
        return omsOrderItem;
    }

    private BigDecimal getTotalAmount(List<OmsCartItem> omsCartItems) {
        BigDecimal totalAmount = new BigDecimal("0");
        for (OmsCartItem omsCartItem : omsCartItems) {
            if (omsCartItem.getIsChecked().equals("1")) {
                totalAmount = totalAmount.add(omsCartItem.getTotalPrice());
            }
        }
        return totalAmount;
    }
}
