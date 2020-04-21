package indi.faniche.anonyshop.user.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import indi.faniche.anonyshop.bean.address.AddrCity;
import indi.faniche.anonyshop.bean.address.AddrCounty;
import indi.faniche.anonyshop.bean.address.AddrProvince;
import indi.faniche.anonyshop.user.mapper.AddrCityMapper;
import indi.faniche.anonyshop.user.mapper.AddrCountyMapper;
import indi.faniche.anonyshop.user.mapper.AddrProvinceMapper;
import indi.faniche.anonyshop.service.CascadeAddressService;
import org.springframework.beans.factory.annotation.Autowired;
import tk.mybatis.mapper.entity.Example;

import java.util.List;

@Service
public class CascadeAddressServiceImpl implements CascadeAddressService {
    @Autowired
    AddrProvinceMapper provinceMapper;

    @Autowired
    AddrCityMapper cityMapper;

    @Autowired
    AddrCountyMapper countyMapper;


    @Override
    public List<AddrProvince> getAllProvince() {
        return provinceMapper.selectAll();
    }

    @Override
    public List<AddrCity> getAllCityByProvinceId(String privinceId) {
        Example example = new Example(AddrCity.class);
        example.createCriteria().andEqualTo("provinceId", privinceId);
        return cityMapper.selectByExample(example);
    }

    @Override
    public List<AddrCounty> getAllCountyByCityId(String cityId) {
        Example example = new Example(AddrCounty.class);
        example.createCriteria().andEqualTo("cityId", cityId);
        return countyMapper.selectByExample(example);
    }
}
