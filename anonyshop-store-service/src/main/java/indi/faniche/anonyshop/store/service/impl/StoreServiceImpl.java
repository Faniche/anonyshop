package indi.faniche.anonyshop.store.service.impl;

/* File:   StoreServiceImpl.java
 * -------------------------
 * Author: faniche
 * Date:   5/9/20
 */

import com.alibaba.dubbo.config.annotation.Service;
import com.alibaba.fastjson.JSON;
import indi.faniche.anonyshop.bean.checkout.OmsCartItem;
import indi.faniche.anonyshop.bean.store.SmsStore;
import indi.faniche.anonyshop.service.StoreService;
import indi.faniche.anonyshop.store.mapper.SmsStoreMapper;
import indi.faniche.anonyshop.util.RedisUtil;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import redis.clients.jedis.Jedis;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
public class StoreServiceImpl implements StoreService {
    @Autowired
    SmsStoreMapper smsStoreMapper;

    @Autowired
    RedisUtil redisUtil;

    @Override
    public SmsStore getStore(String userId) {
        SmsStore store = new SmsStore();
        store.setOwnerId(userId);
        store = smsStoreMapper.selectOne(store);
        return store;
    }

    @Override
    public void colse(String storeId, String status) {
        SmsStore store = new SmsStore();
        store.setStatus(status);
        store.setId(storeId);
        smsStoreMapper.updateByPrimaryKeySelective(store);
        syncCache(store);
    }

    @Override
    public SmsStore addStore(SmsStore store) {
        if (store.getId().equals("")) {
//            store.setStatus("0");
            store.setStatus("1");
            store.setPoint("100");
            store.setCreateDate(new Date());
            smsStoreMapper.insertSelective(store);
        } else {
            smsStoreMapper.updateByPrimaryKeySelective(store);
        }
        syncCache(store);
        return store;
    }

    @Override
    public SmsStore getStoreByStoreId(String storeId) {
        SmsStore store = null;
        Jedis jedis = null;
        try {
            jedis = redisUtil.getJedis();
            String key = "store:" + storeId + ":info";
            String storeStr = jedis.get(key);
            if (StringUtils.isNotBlank(storeStr)) {
                store = JSON.parseObject(storeStr, SmsStore.class);
            } else {
                store = smsStoreMapper.selectByPrimaryKey(storeId);
                jedis.setex(key, 60 * 60 * 2, JSON.toJSONString(store));
            }
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        } finally {
            jedis.close();
        }
        return store;
    }

    private void syncCache(SmsStore store){
        Jedis jedis = null;
        try {
            jedis = redisUtil.getJedis();
            String key = "store:" + store.getId() + ":info";
            String storeStr = jedis.get(key);
            if (StringUtils.isNotBlank(storeStr)) {
                jedis.del(key);
            } else {
                jedis.setex(key, 60 * 60 * 2, JSON.toJSONString(store));
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            jedis.close();
        }
    }
}
