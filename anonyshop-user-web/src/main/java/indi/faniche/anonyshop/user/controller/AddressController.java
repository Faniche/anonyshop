package indi.faniche.anonyshop.user.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import indi.faniche.anonyshop.bean.address.AddrCity;
import indi.faniche.anonyshop.bean.address.AddrCounty;
import indi.faniche.anonyshop.bean.address.AddrProvince;
import indi.faniche.anonyshop.bean.user.UmsDefaultAddress;
import indi.faniche.anonyshop.service.CascadeAddressService;
import indi.faniche.anonyshop.service.UmsDefaultAddressService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

@Controller
public class AddressController {
    @Reference
    UmsDefaultAddressService defaultAddressService;

    @Reference
    CascadeAddressService cascadeAddressService;

    @RequestMapping("addDefaultAddress")
    @ResponseBody
    public String addDefaultAddress(UmsDefaultAddress defaultAddress) {
        int status = defaultAddressService.addDefaultAddress(defaultAddress);
        return status != 0 ? "已成功设置默认地址" : "设置失败，请稍后重试";
    }

    @RequestMapping("delDefaultAddress")
    @ResponseBody
    public String delDefaultAddress(String loginId) {
        int status = defaultAddressService.delDefaultAddressByLoginId(loginId);
        return status != 0 ? "已删除设置默认地址" : "删除失败，请稍后重试";
    }

    @RequestMapping("modiftDefaultAddress")
    @ResponseBody
    public String modiftDefaultAddress(UmsDefaultAddress defaultAddress) {
        int status = defaultAddressService.modifyDefaultAddress(defaultAddress);
        return status != 0 ? "已修改设置默认地址" : "修改失败，请稍后重试";
    }

    @RequestMapping("getDefaultAddressByLoginId")
    @ResponseBody
    public UmsDefaultAddress getDefaultAddressByLoginId(String loginId) {
        UmsDefaultAddress defaultAddress = defaultAddressService.getDefaultAddressByLoginId(loginId);
        return defaultAddress;
    }

    /*查询全部的省*/
    @RequestMapping("getAllProvince")
    @ResponseBody
    public List<AddrProvince> getAllProvince() {
        List<AddrProvince> provinceList = cascadeAddressService.getAllProvince();
        return provinceList;
    }

    /*根据省id查询市*/
    @RequestMapping("getAllCityByProvinceId")
    @ResponseBody
    public List<AddrCity> getAllCityByProvinceId(String privinceId) {
        List<AddrCity> cityList = cascadeAddressService.getAllCityByProvinceId(privinceId);
        return cityList;
    }

    /*根据市id查询县*/
    @RequestMapping("getAllCountyByCityId")
    @ResponseBody
    public List<AddrCounty> getAllCountyByCityId(String cityId) {
        List<AddrCounty> countyList = cascadeAddressService.getAllCountyByCityId(cityId);
        return countyList;
    }
}
