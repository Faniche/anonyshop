package indi.faniche.anonyshop.ware.service.impl;

/* File:   WareServiceImpl.java
 * -------------------------
 * Author: faniche
 * Date:   5/14/20
 */

import com.alibaba.dubbo.config.annotation.Service;
import indi.faniche.anonyshop.bean.ware.WmsWareInfo;
import indi.faniche.anonyshop.service.WareService;
import indi.faniche.anonyshop.ware.mapper.WmsWareInfoMapper;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

@Service
public class WareServiceImpl implements WareService {

    @Autowired
    WmsWareInfoMapper wmsWareInfoMapper;

    @Override
    public List<WmsWareInfo> getAllWares(String storeId) {
        return wmsWareInfoMapper.selectAll();
    }
}
