package indi.faniche.anonyshop.delivery.service.impl;

/* File:   DeliveryServiceImpl.java
 * -------------------------
 * Author: faniche
 * Date:   5/11/20
 */

import com.alibaba.dubbo.config.annotation.Service;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import indi.faniche.anonyshop.bean.checkout.OmsDelivery;
import indi.faniche.anonyshop.bean.checkout.OmsOrder;
import indi.faniche.anonyshop.delivery.mapper.OmsDeliveryMapper;
import indi.faniche.anonyshop.service.DeliveryService;
import org.springframework.beans.factory.annotation.Autowired;
import tk.mybatis.mapper.entity.Example;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Random;

@Service
public class DeliveryServiceImpl implements DeliveryService {

    @Autowired
    OmsDeliveryMapper omsDeliveryMapper;

    @Override
    public void addNewDelivery(OmsOrder omsOrder) {
        OmsDelivery delivery = new OmsDelivery();
        delivery.setOrderId(omsOrder.getId());
        delivery.setStatus("0");
        delivery.setStoreId(omsOrder.getStoreId());
        delivery.setDestProvince(omsOrder.getReceiverProvince());
        delivery.setDestCity(omsOrder.getReceiverCity());
        delivery.setDestRegion(omsOrder.getReceiverRegion());
        delivery.setDestDetailAddr(omsOrder.getReceiverDetailAddress());
        delivery.setContact(omsOrder.getReceiverName() + " " + omsOrder.getReceiverPhone());
        delivery.setUserId(omsOrder.getMemberId());
        delivery.setNote(omsOrder.getNote());
        delivery.setCreateDate(new Date());
        omsDeliveryMapper.insertSelective(delivery);
    }

    @Override
    public void sendDelivery(OmsDelivery delivery) {
        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat sdf = new SimpleDateFormat("MMDDHHmmss");
        Random random = new Random();
        int rand = random.nextInt(900) + 100;
        String time =  sdf.format(calendar.getTime());
        String seq = time + delivery.getStoreId() + String.valueOf(rand);
        delivery.setDeliverySeq(seq);
        delivery.setStatus("1");
        delivery.setSendDate(new Date());
        omsDeliveryMapper.updateByPrimaryKeySelective(delivery);
    }

    @Override
    public OmsDelivery getDeliveryByOrderId(String orderId) {
        OmsDelivery delivery = new OmsDelivery();
        delivery.setOrderId(orderId);
        OmsDelivery omsDelivery = omsDeliveryMapper.selectOne(delivery);
        setStatusStr(omsDelivery);
        return omsDeliveryMapper.selectOne(delivery);
    }

    @Override
    public PageInfo<OmsDelivery> getStoreProgressDelivery(Integer page, String storeId) {
        OmsDelivery delivery = new OmsDelivery();
        delivery.setStoreId(storeId);
        PageHelper.startPage(page, 10);
        PageHelper.orderBy("id ASC ");
        List<OmsDelivery> deliveries = omsDeliveryMapper.select(delivery);
        for (OmsDelivery tmp: deliveries) {
            setStatusStr(tmp);
        }
        PageInfo<OmsDelivery> deliveryList = new PageInfo<>(deliveries);
        return deliveryList;
    }

    @Override
    public PageInfo<OmsDelivery> getUserProgressDelivery(Integer page, String userId) {
        OmsDelivery delivery = new OmsDelivery();
        delivery.setUserId(userId);
        PageHelper.startPage(page, 10);
        PageHelper.orderBy("id ASC ");
        List<OmsDelivery> deliveries = omsDeliveryMapper.select(delivery);
        for (OmsDelivery tmp: deliveries) {
            setStatusStr(tmp);
        }
        PageInfo<OmsDelivery> deliveryList = new PageInfo<>(deliveries);
        return deliveryList;
    }

    @Override
    public OmsDelivery updateDeliveryByOrderId(OmsDelivery delivery) {
        Example example = new Example(OmsDelivery.class);
        example.createCriteria().andEqualTo("orderId", delivery.getOrderId());
        omsDeliveryMapper.updateByExampleSelective(delivery, example);
        return delivery;
    }

    @Override
    public void delDeliveryByOrderId(OmsDelivery delivery) {
        omsDeliveryMapper.delete(delivery);
    }

    private void setStatusStr(OmsDelivery omsDelivery){
        switch (omsDelivery.getStatus()){
            case "0":
                omsDelivery.setStatusStr("待发货");
                break;
            case "1":
                omsDelivery.setStatusStr("运输中");
                break;
            case "2":
                omsDelivery.setStatusStr("待签收");
                break;
            case "3":
                omsDelivery.setStatusStr("已完成");
                break;
        }
    }
}
