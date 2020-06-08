package indi.faniche.anonyshop.product.service.impl;

/* File:   SkuServiceImpl.java
 * -------------------------
 * Author: faniche
 * Date:   4/22/20
 */

import com.alibaba.dubbo.config.annotation.Service;
import com.alibaba.fastjson.JSON;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import indi.faniche.anonyshop.bean.checkout.OmsOrderItem;
import indi.faniche.anonyshop.bean.sku.PmsSkuAttrValue;
import indi.faniche.anonyshop.bean.sku.PmsSkuImage;
import indi.faniche.anonyshop.bean.sku.PmsSkuInfo;
import indi.faniche.anonyshop.bean.sku.PmsSkuSaleAttrValue;
import indi.faniche.anonyshop.bean.store.SmsStore;
import indi.faniche.anonyshop.product.mapper.PmsSkuAttrValueMapper;
import indi.faniche.anonyshop.product.mapper.PmsSkuImageMapper;
import indi.faniche.anonyshop.product.mapper.PmsSkuInfoMapper;
import indi.faniche.anonyshop.product.mapper.PmsSkuSaleAttrValueMapper;
import indi.faniche.anonyshop.service.SkuService;
import indi.faniche.anonyshop.util.RedisUtil;
import org.apache.commons.lang3.StringUtils;
import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.annotation.Autowired;
import redis.clients.jedis.Jedis;
import tk.mybatis.mapper.entity.Example;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.UUID;

@Service
public class SkuServiceImpl implements SkuService {

    @Autowired
    PmsSkuInfoMapper pmsSkuInfoMapper;

    @Autowired
    PmsSkuAttrValueMapper pmsSkuAttrValueMapper;

    @Autowired
    PmsSkuSaleAttrValueMapper pmsSkuSaleAttrValueMapper;

    @Autowired
    PmsSkuImageMapper pmsSkuImageMapper;

    @Autowired
    RedisUtil redisUtil;

    @Autowired
    RedissonClient redissonClient;

    @Override
    public void saveSkuInfo(PmsSkuInfo pmsSkuInfo) {
        // 插入skuInfo
        pmsSkuInfoMapper.insertSelective(pmsSkuInfo);
        String skuId = pmsSkuInfo.getId();
        // 插入平台属性关联
        List<PmsSkuAttrValue> skuAttrValueList = pmsSkuInfo.getSkuAttrValueList();
        for (PmsSkuAttrValue pmsSkuAttrValue : skuAttrValueList) {
            if (pmsSkuAttrValue.getAttrId() != null) {
                pmsSkuAttrValue.setSkuId(skuId);
                pmsSkuAttrValueMapper.insertSelective(pmsSkuAttrValue);
            }
        }
        // 插入销售属性关联
        List<PmsSkuSaleAttrValue> skuSaleAttrValueList = pmsSkuInfo.getSkuSaleAttrValueList();
        for (PmsSkuSaleAttrValue pmsSkuSaleAttrValue : skuSaleAttrValueList) {
            if (pmsSkuSaleAttrValue.getSaleAttrId() != null) {
                pmsSkuSaleAttrValue.setSkuId(skuId);
                pmsSkuSaleAttrValueMapper.insertSelective(pmsSkuSaleAttrValue);
            }
        }
        // 插入图片信息
        List<PmsSkuImage> skuImageList = pmsSkuInfo.getSkuImageList();
        for (PmsSkuImage pmsSkuImage : skuImageList) {
            pmsSkuImage.setSkuId(skuId);
            pmsSkuImageMapper.insertSelective(pmsSkuImage);
        }
    }

    private PmsSkuInfo getSkuByIdFromDb(String skuId) {
        PmsSkuInfo pmsSkuInfo = new PmsSkuInfo();
        pmsSkuInfo.setId(skuId);
        pmsSkuInfo = pmsSkuInfoMapper.selectOne(pmsSkuInfo);
        PmsSkuImage pmsSkuImage = new PmsSkuImage();
        pmsSkuImage.setSkuId(skuId);
        List<PmsSkuImage> skuImageList = pmsSkuImageMapper.select(pmsSkuImage);
        pmsSkuInfo.setSkuImageList(skuImageList);
        PmsSkuSaleAttrValue pmsSkuSaleAttrValue = new PmsSkuSaleAttrValue();
        pmsSkuSaleAttrValue.setSkuId(skuId);
        List<PmsSkuSaleAttrValue> skuSaleAttrValues = pmsSkuSaleAttrValueMapper.select(pmsSkuSaleAttrValue);
        pmsSkuInfo.setSkuSaleAttrValueList(skuSaleAttrValues);
        return pmsSkuInfo;
    }

    @Override
    public PmsSkuInfo getSkuById(String skuId, String ip) {
        PmsSkuInfo pmsSkuInfo = new PmsSkuInfo();
        // 链接缓存
        Jedis jedis = redisUtil.getJedis();
        // 查询缓存
        String skuKey = "sku:" + skuId + ":info";
        String skuJson = jedis.get(skuKey);
        if (StringUtils.isNotBlank(skuJson)) {
            pmsSkuInfo = JSON.parseObject(skuJson, PmsSkuInfo.class);
        } else {
            // 如果缓存中没有，查询mysql
            // 设置分布式锁
            String token = UUID.randomUUID().toString();
            String OK = jedis.set("sku:" + skuId + ":lock", token, "nx", "px", 10 * 1000);// 拿到锁的线程有10秒的过期时间
            if (StringUtils.isNotBlank(OK) && OK.equals("OK")) {
                // 设置成功，有权在10秒的过期时间内访问数据库
                pmsSkuInfo = getSkuByIdFromDb(skuId);
                if (pmsSkuInfo != null) {
                    // mysql查询结果存入redis
                    jedis.set("sku:" + skuId + ":info", JSON.toJSONString(pmsSkuInfo));
                } else {
                    // 数据库中不存在该sku
                    // 为了防止缓存穿透将，null或者空字符串值设置给redis
                    jedis.setex("sku:" + skuId + ":info", 60 * 3, JSON.toJSONString(""));
                }

                // 在访问mysql后，将mysql的分布锁释放
                String lockToken = jedis.get("sku:" + skuId + ":lock");
                if (StringUtils.isNotBlank(lockToken) && lockToken.equals(token)) {
                    //jedis.eval("lua");可与用lua脚本，在查询到key的同时删除该key，防止高并发下的意外的发生
                    jedis.del("sku:" + skuId + ":lock");// 用token确认删除的是自己的sku的锁
                }
            } else {
                // 设置失败，自旋（该线程在睡眠几秒后，重新尝试访问本方法）
                return getSkuById(skuId, ip);
            }
        }
        jedis.close();
        return pmsSkuInfo;
//        return getSkuByIdFromDb(skuId);
    }

    @Override
    public List<PmsSkuInfo> getSkuSaleAttrValueListBySpu(String productId) {
        List<PmsSkuInfo> pmsSkuInfos = pmsSkuInfoMapper.selectSkuSaleAttrValueListBySpu(productId);
        return pmsSkuInfos;
    }

    @Override
    public boolean checkPrice(String productSkuId, BigDecimal price) {
        boolean pass = false;
        PmsSkuInfo pmsSkuInfo = new PmsSkuInfo();
        pmsSkuInfo.setId(productSkuId);
        PmsSkuInfo pmsSkuInfo1 = pmsSkuInfoMapper.selectOne(pmsSkuInfo);
        BigDecimal productPrice = pmsSkuInfo1.getPrice();
        if (price.compareTo(productPrice) == 0) {
            pass = true;
        }
        return pass;
    }

    @Override
    public PageInfo<PmsSkuInfo> getStoreSkuList(int page, String id) {
        PmsSkuInfo skuInfo = new PmsSkuInfo();
        skuInfo.setStoreId(id);
        try {
            // 调用pagehelper分页，采用starPage方式。starPage应放在Mapper查询函数之前
            PageHelper.startPage(page, 10); //每页的大小为pageSize，查询第page页的结果
            PageHelper.orderBy("id ASC "); //进行分页结果的排序
            List<PmsSkuInfo> skuInfoList = pmsSkuInfoMapper.select(skuInfo);
            PageInfo<PmsSkuInfo> skuList = new PageInfo<>(skuInfoList);
            return skuList;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public void modifySkuInfo(PmsSkuInfo pmsSkuInfo) {
        pmsSkuInfoMapper.updateByPrimaryKeySelective(pmsSkuInfo);
    }

    @Override
    public void delSkuInfo(String skuId) {
        PmsSkuImage skuImage = new PmsSkuImage();
        skuImage.setSkuId(skuId);
        pmsSkuImageMapper.delete(skuImage);

        PmsSkuAttrValue skuAttrValue = new PmsSkuAttrValue();
        skuAttrValue.setSkuId(skuId);
        pmsSkuAttrValueMapper.delete(skuAttrValue);

        PmsSkuSaleAttrValue skuSaleAttrValue = new PmsSkuSaleAttrValue();
        skuSaleAttrValue.setSkuId(skuId);
        pmsSkuSaleAttrValueMapper.delete(skuSaleAttrValue);

        pmsSkuInfoMapper.deleteByPrimaryKey(skuId);
    }

    @Override
    public PageInfo<PmsSkuInfo> getClassSearchPageRandomList(int pageNum) {
        try {
            System.out.println("get");
            // 调用pagehelper分页，采用starPage方式。starPage应放在Mapper查询函数之前
            PageHelper.startPage(pageNum, 12); //每页的大小为pageSize，查询第page页的结果
            PageHelper.orderBy("id ASC "); //进行分页结果的排序
            List<PmsSkuInfo> skuInfoList = pmsSkuInfoMapper.selectAll();
            PageInfo<PmsSkuInfo> skuList = new PageInfo<>(skuInfoList);
            return skuList;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public List<PmsSkuInfo> getPopularProduct() {
        List<PmsSkuInfo> popularList = new ArrayList<>();
        Jedis jedis = redisUtil.getJedis();
        String popularListStr = jedis.get("sku:popular:list");
        if (StringUtils.isNotBlank(popularListStr)) {
            popularList = JSON.parseArray(popularListStr, PmsSkuInfo.class);
        } else {
            List<PmsSkuInfo> skuInfoList = pmsSkuInfoMapper.selectAll();
            List<Integer> indes = new ArrayList<>();
            Random random = new Random();
            for (int i = 0; i < 9; i++) {
                int index = random.nextInt(skuInfoList.size());
                while (indes.contains(index)) {
                    index = random.nextInt(skuInfoList.size());
                }
                indes.add(index);
            }
            for (int i = 0; i < indes.size(); i++) {
                popularList.add(skuInfoList.get(indes.get(i)));
            }
            jedis.setex("sku:popular:list", 60 * 60 * 24, JSON.toJSONString(popularList));
        }
        return popularList;
    }

    @Override
    public void updateQuantity(List<OmsOrderItem> orderItemList) {
        for (OmsOrderItem orderItem : orderItemList) {
            PmsSkuInfo pmsSkuInfo = new PmsSkuInfo();
            pmsSkuInfo.setId(orderItem.getProductSkuId());
            PmsSkuInfo tmpPmsSkuInfo = pmsSkuInfoMapper.selectOne(pmsSkuInfo);
            String quantity = tmpPmsSkuInfo.getQuantity();
            Integer tmp = Integer.valueOf(quantity) - orderItem.getProductQuantity();
            pmsSkuInfo.setQuantity(Integer.toString(tmp));
            pmsSkuInfoMapper.updateByPrimaryKeySelective(pmsSkuInfo);
        }
    }

    private void syncCache(PmsSkuInfo skuInfo){
        String key = "sku:" + skuInfo.getId() + ":info";
        Jedis jedis = redisUtil.getJedis();
        String skuInfoStr = jedis.get(key);
        if (StringUtils.isNotBlank(skuInfoStr)) {
            jedis.del(key);
        }
        jedis.set(key, JSON.toJSONString(skuInfo));
        jedis.close();
    }

}
