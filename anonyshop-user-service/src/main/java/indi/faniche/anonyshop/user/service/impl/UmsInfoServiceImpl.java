package indi.faniche.anonyshop.user.service.impl;

/* File:   UmsInfoServiceImpl.java
 * -------------------------
 * Author: faniche
 * Date:   5/9/20
 */

import com.alibaba.dubbo.config.annotation.Service;
import indi.faniche.anonyshop.bean.user.UmsInfo;
import indi.faniche.anonyshop.service.UmsInfoService;
import indi.faniche.anonyshop.user.mapper.UmsInfoMapper;
import org.springframework.beans.factory.annotation.Autowired;

@Service
public class UmsInfoServiceImpl implements UmsInfoService {

    @Autowired
    UmsInfoMapper umsInfoMapper;

    @Override
    public UmsInfo getUserInfo(String userId) {
        UmsInfo umsInfo = new UmsInfo();
        umsInfo.setLoginId(userId);
        return umsInfoMapper.selectOne(umsInfo);
    }
}
