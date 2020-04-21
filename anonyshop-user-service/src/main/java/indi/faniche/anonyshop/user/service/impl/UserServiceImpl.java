package indi.faniche.anonyshop.user.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import indi.faniche.anonyshop.service.UserService;
import indi.faniche.anonyshop.bean.user.UmsLogin;
import indi.faniche.anonyshop.user.mapper.UserMapper;
import org.springframework.beans.factory.annotation.Autowired;
import java.util.List;

@Service
public class UserServiceImpl implements UserService {
    @Autowired
    UserMapper userMapper;


    @Override
    public List<UmsLogin> getAllUmsLogin() {
        return userMapper.selectAllLogin();
    }


}
