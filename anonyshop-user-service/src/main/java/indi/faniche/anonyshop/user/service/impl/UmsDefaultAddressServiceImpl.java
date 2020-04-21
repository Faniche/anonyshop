package indi.faniche.anonyshop.user.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import indi.faniche.anonyshop.bean.user.UmsDefaultAddress;
import indi.faniche.anonyshop.user.mapper.UmsDefaultAddressMapper;
import indi.faniche.anonyshop.service.UmsDefaultAddressService;
import org.springframework.beans.factory.annotation.Autowired;
import tk.mybatis.mapper.entity.Example;

@Service
public class UmsDefaultAddressServiceImpl implements UmsDefaultAddressService {

    @Autowired
    UmsDefaultAddressMapper addressMapper;

    @Override
    public UmsDefaultAddress getDefaultAddressByLoginId(String loginId) {
//        UmsDefaultAddress address = new UmsDefaultAddress();
//        address.setLoginId(loginId);
//        return addressMapper.selectOne(address);

        Example example = new Example(UmsDefaultAddress.class);
        example.createCriteria().andEqualTo("loginId", loginId);
        return addressMapper.selectOneByExample(example);
    }

    @Override
    public int addDefaultAddress(UmsDefaultAddress defaultAddress) {
        return addressMapper.insert(defaultAddress);
    }

    @Override
    public int delDefaultAddressByLoginId(String loginId) {
        Example example = new Example(UmsDefaultAddress.class);
        example.createCriteria().andEqualTo("loginId", loginId);
        return addressMapper.deleteByExample(example);
    }

    /*未完！！！！！！！！！！！！！！！！！！！！！！！！！*/
    @Override
    public int modifyDefaultAddress(UmsDefaultAddress defaultAddress) {
        Example example = new Example(UmsDefaultAddress.class);
        example.createCriteria().andEqualTo("");
        return addressMapper.updateByExample(defaultAddress, null);
    }
}
