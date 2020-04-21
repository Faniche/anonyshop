package indi.faniche.anonyshop.service;

import indi.faniche.anonyshop.bean.address.AddrCity;
import indi.faniche.anonyshop.bean.address.AddrCounty;
import indi.faniche.anonyshop.bean.address.AddrProvince;

import java.util.List;

public interface CascadeAddressService {
    List<AddrProvince> getAllProvince();

    List<AddrCity> getAllCityByProvinceId(String privinceId);

    List<AddrCounty> getAllCountyByCityId(String cityId);
}
