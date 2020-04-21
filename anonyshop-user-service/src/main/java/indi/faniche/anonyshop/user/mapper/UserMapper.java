package indi.faniche.anonyshop.user.mapper;

import indi.faniche.anonyshop.bean.user.UmsLogin;
import tk.mybatis.mapper.common.Mapper;

import java.util.List;

public interface UserMapper extends Mapper<UmsLogin> {
    List<UmsLogin> selectAllLogin();
}
