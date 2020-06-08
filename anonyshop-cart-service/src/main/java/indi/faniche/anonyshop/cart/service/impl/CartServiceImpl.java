package indi.faniche.anonyshop.cart.service.impl;

/* File:   CartServiceImpl.java
 * -------------------------
 * Author: faniche
 * Date:   4/28/20
 */

import com.alibaba.dubbo.config.annotation.Service;
import com.alibaba.fastjson.JSON;
import indi.faniche.anonyshop.bean.checkout.OmsCartItem;
import indi.faniche.anonyshop.cart.mapper.OmsCartItemMapper;
import indi.faniche.anonyshop.service.CartService;
import indi.faniche.anonyshop.util.RedisUtil;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import redis.clients.jedis.Jedis;
import tk.mybatis.mapper.entity.Example;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class CartServiceImpl implements CartService {

    @Autowired
    RedisUtil redisUtil;

    @Autowired
    OmsCartItemMapper omsCartItemMapper;


    @Override
    public OmsCartItem ifCartExistByUser(String userId, String skuId) {
        OmsCartItem omsCartItem = new OmsCartItem();
        omsCartItem.setUserId(userId);
        omsCartItem.setProductSkuId(skuId);
        return omsCartItemMapper.selectOne(omsCartItem);
    }

    @Override
    public void addCart(OmsCartItem omsCartItem) {
        if (StringUtils.isNotBlank(omsCartItem.getUserId())) {
            omsCartItemMapper.insertSelective(omsCartItem);//避免添加空值
        }
    }

    @Override
    public void updateCart(OmsCartItem omsCartItemFromDb) {
        Example e = new Example(OmsCartItem.class);
        e.createCriteria().andEqualTo("id", omsCartItemFromDb.getId());
        omsCartItemMapper.updateByExampleSelective(omsCartItemFromDb, e);
    }

    @Override
    public void syncCartCache(String userId) {
        OmsCartItem omsCartItem = new OmsCartItem();
        omsCartItem.setUserId(userId);
        List<OmsCartItem> omsCartItems = omsCartItemMapper.select(omsCartItem);
        // 同步到redis缓存中
        if (!omsCartItems.isEmpty()) {
            Jedis jedis = redisUtil.getJedis();
            Map<String, String> map = new HashMap<>();
            for (OmsCartItem cartItem : omsCartItems) {
                cartItem.setTotalPrice(cartItem.getPrice().multiply(new BigDecimal(cartItem.getQuantity())));
                map.put(cartItem.getProductSkuId(), JSON.toJSONString(cartItem));
            }
            jedis.del("user:" + userId + ":cart");
            jedis.hmset("user:" + userId + ":cart", map);
            jedis.close();
        }
    }

    @Override
    public List<OmsCartItem> cartList(String userId) {
        Jedis jedis = null;
        List<OmsCartItem> omsCartItems = new ArrayList<>();
        try {
            jedis = redisUtil.getJedis();
            List<String> hvals = jedis.hvals("user:" + userId + ":cart");
            if (hvals.isEmpty()) syncCartCache(userId);
            hvals = jedis.hvals("user:" + userId + ":cart");
            for (String hval : hvals) {
                OmsCartItem omsCartItem = JSON.parseObject(hval, OmsCartItem.class);
                omsCartItems.add(omsCartItem);
            }
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        } finally {
            jedis.close();
        }
        return omsCartItems;
    }

    @Override
    public void checkCart(OmsCartItem omsCartItem) {
        Example example = new Example(OmsCartItem.class);
        example.createCriteria().andEqualTo("userId", omsCartItem.getUserId()).andEqualTo("productSkuId", omsCartItem.getProductSkuId());
        omsCartItemMapper.updateByExampleSelective(omsCartItem, example);
        // update cache
        syncCartCache(omsCartItem.getUserId());
    }

    @Override
    public void deleteCartItem(String userId, String skuId) {
        OmsCartItem omsCartItem = new OmsCartItem();
        omsCartItem.setUserId(userId);
        omsCartItem.setProductSkuId(skuId);
        omsCartItemMapper.delete(omsCartItem);
    }

    @Override
    public void checkAll(String userId, String isChecked) {
        OmsCartItem omsCartItem = new OmsCartItem();
        omsCartItem.setIsChecked(isChecked);
        Example example = new Example(OmsCartItem.class);
        example.createCriteria().andEqualTo("userId", userId);
        omsCartItemMapper.updateByExampleSelective(omsCartItem, example);
    }
}
