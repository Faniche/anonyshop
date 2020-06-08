package indi.faniche.anonyshop.cart.controller;

/* File:   CartController.java
 * -------------------------
 * Author: faniche
 * Date:   4/28/20
 */

import com.alibaba.dubbo.config.annotation.Reference;
import com.alibaba.fastjson.JSON;
import indi.faniche.anonyshop.annotations.LoginRequired;
import indi.faniche.anonyshop.bean.checkout.OmsCartItem;
import indi.faniche.anonyshop.bean.sku.PmsSkuInfo;
import indi.faniche.anonyshop.bean.sku.PmsSkuSaleAttrValue;
import indi.faniche.anonyshop.service.CartService;
import indi.faniche.anonyshop.service.SkuService;
import indi.faniche.anonyshop.util.CookieUtil;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.math.BigDecimal;
import java.util.*;

@Controller
@CrossOrigin
public class CartController {
    @Reference
    SkuService skuService;

    @Reference
    CartService cartService;

    @RequestMapping(value = {"", "/"})
    @LoginRequired(loginSuccess = false)
    public String toCart(HttpServletRequest request, Model model) {
        return cartList(request, model);
    }

    @RequestMapping("checkCart")
    @LoginRequired(loginSuccess = false)
    public String checkCart(HttpServletRequest request, HttpServletResponse response, String isChecked, String skuId, Model model) {
        List<OmsCartItem> omsCartItems = new ArrayList<>();
        String userId = (String) request.getAttribute("userId");
        if (StringUtils.isBlank(userId)) {
            String cartListCookie = CookieUtil.getCookieValue(request, "cartListCookie", true);
            omsCartItems = JSON.parseArray(cartListCookie, OmsCartItem.class);
            for (OmsCartItem cartItem : omsCartItems) {
                if (cartItem.getProductSkuId().equals(skuId)) {
                    cartItem.setIsChecked(isChecked);
                    break;
                }
            }
            CookieUtil.setCookie(request, response, "cartListCookie", JSON.toJSONString(omsCartItems), 60 * 60 * 72, true);
        } else {
            // 用户已经登录
            OmsCartItem omsCartItemFromDb = cartService.ifCartExistByUser(userId, skuId);
            omsCartItemFromDb.setIsChecked(isChecked);
            cartService.updateCart(omsCartItemFromDb);
            // 同步缓存
            cartService.syncCartCache(userId);
            omsCartItems = cartService.cartList(userId);
        }
        model = sortAndAddToModel(model, omsCartItems);
        return "cart/cart-list-inner";
    }

    @RequestMapping("checkAll")
    @LoginRequired(loginSuccess = false)
    public String checkAllCartItem(HttpServletRequest request, HttpServletResponse response, String isChecked, Model model) {
        List<OmsCartItem> omsCartItems = new ArrayList<>();
        String userId = (String) request.getAttribute("userId");
        if (StringUtils.isBlank(userId)) {
            String cartListCookie = CookieUtil.getCookieValue(request, "cartListCookie", true);
            omsCartItems = JSON.parseArray(cartListCookie, OmsCartItem.class);
            for (OmsCartItem cartItem : omsCartItems) {
                cartItem.setIsChecked(isChecked);
            }
            CookieUtil.setCookie(request, response, "cartListCookie", JSON.toJSONString(omsCartItems), 60 * 60 * 72, true);
        } else {
            // 用户已经登录
            cartService.checkAll(userId, isChecked);
            // 同步缓存
            cartService.syncCartCache(userId);
            omsCartItems = cartService.cartList(userId);
        }
        model = sortAndAddToModel(model, omsCartItems);
        model.addAttribute("checkAll", isChecked);
        return "cart/cart-list-inner";
    }

    @RequestMapping("delete")
    @LoginRequired(loginSuccess = false)
    public String deleteCartItem(HttpServletRequest request, HttpServletResponse response, String skuId, Model model) {
        List<OmsCartItem> omsCartItems = new ArrayList<>();
        String userId = (String) request.getAttribute("userId");
        if (StringUtils.isBlank(userId)) {
            String cartListCookie = CookieUtil.getCookieValue(request, "cartListCookie", true);
            omsCartItems = JSON.parseArray(cartListCookie, OmsCartItem.class);
            for (int i = 0; i < omsCartItems.size(); i++){
                if (omsCartItems.get(i).getProductSkuId().equals(skuId)) {
                    omsCartItems.remove(i);
                    break;
                }
            }
            CookieUtil.setCookie(request, response, "cartListCookie", JSON.toJSONString(omsCartItems), 60 * 60 * 72, true);
        } else {
            // 用户已经登录
            cartService.deleteCartItem(userId, skuId);
            // 同步缓存
            cartService.syncCartCache(userId);
            omsCartItems = cartService.cartList(userId);
        }
        // 排序
        model = sortAndAddToModel(model, omsCartItems);
        return "cart/cart-list-inner";
    }

    @RequestMapping("chgItemQuantity")
    @LoginRequired(loginSuccess = false)
    public String chgItemQuantity(HttpServletRequest request, HttpServletResponse response, Integer quantity, String skuId, Model model) {
        List<OmsCartItem> omsCartItems = new ArrayList<>();
        String userId = (String) request.getAttribute("userId");
        // 用户没有登录
        if (StringUtils.isBlank(userId)) {
            String cartListCookie = CookieUtil.getCookieValue(request, "cartListCookie", true);
            omsCartItems = JSON.parseArray(cartListCookie, OmsCartItem.class);
            for (OmsCartItem cartItem : omsCartItems) {
                if (cartItem.getProductSkuId().equals(skuId)) {
                    cartItem.setQuantity(quantity);
                    break;
                }
            }
            CookieUtil.setCookie(request, response, "cartListCookie", JSON.toJSONString(omsCartItems), 60 * 60 * 72, true);
        } else {
            // 用户已经登录
            OmsCartItem omsCartItemFromDb = cartService.ifCartExistByUser(userId, skuId);
            omsCartItemFromDb.setQuantity(quantity);
            cartService.updateCart(omsCartItemFromDb);
            // 同步缓存
            cartService.syncCartCache(userId);
            omsCartItems = cartService.cartList(userId);
        }
        // 排序
        model = sortAndAddToModel(model, omsCartItems);
        return "cart/cart-list-inner";
    }

    @RequestMapping("cartList")
    @LoginRequired(loginSuccess = false)
    public String cartList(HttpServletRequest request, Model model) {
        List<OmsCartItem> omsCartItems = new ArrayList<>();
        String userId = (String) request.getAttribute("userId");
        String username = (String) request.getAttribute("username");
        if (StringUtils.isNotBlank(userId)) {
            // 已经登录查询db
            omsCartItems = cartService.cartList(userId);
        } else {
            // 没有登录查询cookie
            String cartListCookie = CookieUtil.getCookieValue(request, "cartListCookie", true);
            if (StringUtils.isNotBlank(cartListCookie)) {
                omsCartItems = JSON.parseArray(cartListCookie, OmsCartItem.class);
            }
        }
        for (OmsCartItem omsCartItem : omsCartItems) {
            omsCartItem.setTotalPrice(omsCartItem.getPrice().multiply(new BigDecimal(omsCartItem.getQuantity())));
        }
        model = sortAndAddToModel(model, omsCartItems);
        model.addAttribute("username", username);
        model.addAttribute("userId", userId);
        return "cart/shopping-cart";
    }

    @RequestMapping("addToCart")
    @LoginRequired(loginSuccess = false)
    public String addToCart(String skuId, Integer quantity, HttpServletRequest request, HttpServletResponse response) {
        List<OmsCartItem> omsCartItems = new ArrayList<>();
        // 利用数量和skuId 得到封装好的 CartItem
        OmsCartItem omsCartItem = getOmsCartItemFromSkuId(skuId, quantity);
        // 判断用户是否登录，如果登录，拦截其的 request 中会携带用户名和用户id
        String userId = (String) request.getAttribute("userId");
        String username = (String) request.getAttribute("username");
        // 用户没有登录
        if (StringUtils.isBlank(userId)) {
            // cookie里原有的购物车数据
            String cartListCookie = CookieUtil.getCookieValue(request, "cartListCookie", true);
            if (StringUtils.isBlank(cartListCookie)) {
                omsCartItems.add(omsCartItem);
            } else {
                omsCartItems = JSON.parseArray(cartListCookie, OmsCartItem.class);
                // 判断添加的购物车数据在cookie中是否存在
                if (if_cart_exist(omsCartItems, omsCartItem)) {
                    for (OmsCartItem cartItem : omsCartItems) {
                        if (cartItem.getProductSkuId().equals(omsCartItem.getProductSkuId())) {
                            cartItem.setQuantity(cartItem.getQuantity() + omsCartItem.getQuantity());
                            break;
                        }
                    }
                } else {
                    // 之前没有添加，新增当前的购物车
                    omsCartItems.add(omsCartItem);
                }
            }
            // 更新cookie
            CookieUtil.setCookie(request, response, "cartListCookie", JSON.toJSONString(omsCartItems), 60 * 60 * 72, true);
        } else {
            // 用户已经登录
            // 从db中查出购物车数据
            OmsCartItem omsCartItemFromDb = cartService.ifCartExistByUser(userId, skuId);
            if (omsCartItemFromDb == null) {
                // 该用户没有添加过当前商品
                omsCartItem.setUserId(userId);
                omsCartItem.setUsername(username);
                omsCartItem.setQuantity(quantity);
                cartService.addCart(omsCartItem);
            } else {
                // 该用户添加过当前商品
                omsCartItemFromDb.setQuantity(omsCartItemFromDb.getQuantity() + (omsCartItem.getQuantity()));
                cartService.updateCart(omsCartItemFromDb);
            }
            // 同步缓存
            cartService.syncCartCache(userId);
        }
        return "redirect:/cartList";
    }

    /* 判断 Cookie 中的购物车列表中是否包含添加的商品 */
    private boolean if_cart_exist(List<OmsCartItem> omsCartItems, OmsCartItem omsCartItem) {
        boolean ret = false;
        for (OmsCartItem cartItem : omsCartItems) {
            String productSkuId = cartItem.getProductSkuId();
            if (productSkuId.equals(omsCartItem.getProductSkuId())) {
                ret = true;
            }
        }
        return ret;
    }

    /* 获取购物车中所有选中的商品的价格 */
    private BigDecimal getTotalAmount(List<OmsCartItem> omsCartItems) {
        BigDecimal totalAmount = new BigDecimal("0");
        for (OmsCartItem omsCartItem : omsCartItems) {
            BigDecimal totalPrice = omsCartItem.getTotalPrice();
            if (omsCartItem.getIsChecked().equals("1")) {
                totalAmount = totalAmount.add(totalPrice);
            }
        }
        return totalAmount;
    }

    /* 对要返回的购物车列表进行排序，因为购物车中的商品状态改变会改变列表顺序，统一按照添加的时间排序 */
    private void sortCartItemByCreateDate(List<OmsCartItem> omsCartItems) {
        Collections.sort(omsCartItems, new Comparator<OmsCartItem>() {
            public int compare(OmsCartItem o1, OmsCartItem o2) {
                return o1.getCreateDate().compareTo(o2.getCreateDate());
            }
        });
    }

    /* 将商品的销售属性转化为字符串 */
    private String getSaleAttrStr(List<PmsSkuSaleAttrValue> list) {
        String saleAttrStr = "";
        for (PmsSkuSaleAttrValue tmp : list) {
            saleAttrStr += (tmp.getSaleAttrValueName() + "/");
        }
        return saleAttrStr.substring(0, saleAttrStr.length() - 2);
    }

    private OmsCartItem getOmsCartItemFromSkuId(String skuId, Integer quantity) {
        // 使用skuservice查询商品信息
        PmsSkuInfo skuInfo = skuService.getSkuById(skuId, "");
        // 封装购物车信息
        OmsCartItem omsCartItem = new OmsCartItem();
        omsCartItem.setCreateDate(new Date());
        omsCartItem.setPrice(skuInfo.getPrice());
        omsCartItem.setProductBrand(skuInfo.getBrandId());
        omsCartItem.setProductId(skuInfo.getProductId());
        omsCartItem.setProductName(skuInfo.getSkuName());
        omsCartItem.setProductPic(skuInfo.getSkuDefaultImg());
        omsCartItem.setProductSkuId(skuId);
        omsCartItem.setQuantity(quantity);
        // 添加购物车的时候默认选中状态
        omsCartItem.setIsChecked("1");
        omsCartItem.setProductSaleAttr(getSaleAttrStr(skuInfo.getSkuSaleAttrValueList()));
        return omsCartItem;
    }

    private Model sortAndAddToModel(Model model, List<OmsCartItem> omsCartItems){
        sortCartItemByCreateDate(omsCartItems);
        model.addAttribute("cartList", omsCartItems);
        // 被勾选商品的总额
        BigDecimal totalAmount = getTotalAmount(omsCartItems);
        model.addAttribute("totalAmount", totalAmount);
        return model;
    }
}
