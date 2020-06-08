package indi.faniche.anonyshop.service;

import indi.faniche.anonyshop.bean.user.UmsDefaultAddress;

public interface UmsDefaultAddressService {
    UmsDefaultAddress getDefaultAddressByLoginId(String loginId);

    int addDefaultAddress(UmsDefaultAddress defaultAddress);

    int delDefaultAddressByLoginId(String loginId);

    UmsDefaultAddress modifyDefaultAddress(UmsDefaultAddress defaultAddress);

}
