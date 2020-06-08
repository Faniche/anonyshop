package indi.faniche.anonyshop.user.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.alibaba.fastjson.JSON;
import indi.faniche.anonyshop.bean.user.UmsInfo;
import indi.faniche.anonyshop.bean.user.UmsLoginRole;
import indi.faniche.anonyshop.service.UserService;
import indi.faniche.anonyshop.bean.user.UmsLogin;
import indi.faniche.anonyshop.user.mapper.UmsLoginRoleMapper;
import indi.faniche.anonyshop.user.mapper.UserMapper;
import indi.faniche.anonyshop.util.RedisUtil;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import redis.clients.jedis.Jedis;
import tk.mybatis.mapper.entity.Example;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class UserServiceImpl implements UserService {
    @Autowired
    UserMapper userMapper;

    @Autowired
    RedisUtil redisUtil;

    @Resource
    BCryptPasswordEncoder bCryptPasswordEncoder;

    @Autowired
    UmsLoginRoleMapper umsLoginRoleMapper;

    @Override
    public List<UmsLogin> getAllUmsLogin() {
        return userMapper.selectAllLogin();
    }

    @Override
    public UmsLogin login(UmsLogin umsLogin) {
        Jedis jedis = null;
        try {
            jedis = redisUtil.getJedis();
            UmsLogin queryLogin = null;
            if (jedis != null) {
                String loginInfo = jedis.get("user:" + umsLogin.getUsername() + ":info");
                if (StringUtils.isNotBlank(loginInfo)) {
                    queryLogin = JSON.parseObject(loginInfo, UmsLogin.class);
                }
            }
            if (queryLogin == null) {
                UmsLogin tmp = new UmsLogin();
                if (umsLogin.getUsername().isEmpty()) {
                    tmp.setEmail(umsLogin.getEmail());
                    queryLogin = getFromDb(tmp);

                } else {
                    tmp.setUsername(umsLogin.getUsername());
                    queryLogin = getFromDb(tmp);
                }
            }
            if (queryLogin != null) {
                boolean pass = bCryptPasswordEncoder.matches(umsLogin.getPassword(), queryLogin.getPassword());
                if (pass) {
                    jedis.setex("user:" + queryLogin.getUsername() + ":info", 60 * 60 * 2, JSON.toJSONString(queryLogin));
                    return queryLogin;
                }
            }
            return null;
        } finally {
            jedis.close();
        }

    }

    @Override
    public void addToken(String token, String userId) {
        Jedis jedis = redisUtil.getJedis();
        jedis.setex("user:" + userId + ":token", 60 * 60, token);
        jedis.close();
    }

    @Override
    public void deleteToken(String userId) {
        Jedis jedis = redisUtil.getJedis();
        jedis.del("user:" + userId + "token");
        jedis.close();
    }

    @Override
    public UmsLogin addUser(UmsLogin umsLogin) {
        Jedis jedis = null;
        try {
            jedis = redisUtil.getJedis();
            UmsLogin queryLogin = null;
            if (jedis != null) {
                String loginInfo = jedis.get("user:" + umsLogin.getUsername() + ":info");
                if (StringUtils.isNotBlank(loginInfo)) {
                    queryLogin = JSON.parseObject(loginInfo, UmsLogin.class);
                } else {
                    UmsLogin tmp = new UmsLogin();
                    tmp.setUsername(umsLogin.getUsername());
                    queryLogin = getFromDb(tmp);
                }
            }
            if (queryLogin == null) {
                umsLogin.setPassword(bCryptPasswordEncoder.encode(umsLogin.getPassword()));
                userMapper.insertSelective(umsLogin);
                UmsLoginRole umsLoginRole = new UmsLoginRole();
                umsLoginRole.setLoginId(umsLogin.getId());
                umsLoginRole.setRoleId(umsLogin.getRoleId());
                umsLoginRoleMapper.insertSelective(umsLoginRole);
                UmsInfo umsInfo = new UmsInfo();
                umsInfo.setLoginId(umsLogin.getId());
                umsInfo.setIntegration(100);
                jedis.setex("user:" + umsLogin.getUsername() + ":info", 60 * 60 * 2, JSON.toJSONString(umsLogin));
            }
        } finally {
            jedis.close();
        }
        return umsLogin;
    }

    @Override
    public void activateAccount(String userId) {
        UmsLogin umsLogin = new UmsLogin();
        umsLogin.setStatus("1");
        Example example = new Example(UmsLogin.class);
        example.createCriteria().andEqualTo("id", userId);
        userMapper.updateByExampleSelective(umsLogin, example);
        syncCache(userId);
    }

    @Override
    public void resetPass(UmsLogin login) {
        login.setPassword(bCryptPasswordEncoder.encode(login.getPassword()));
        Example example = new Example(UmsLogin.class);
        example.createCriteria().andEqualTo("email", login.getEmail());
        userMapper.updateByExampleSelective(login, example);
    }

    @Override
    public UmsLogin getUserByUsername(String username) {
        Jedis jedis = null;
        UmsLogin umsLogin = null;
        try {
            jedis = redisUtil.getJedis();
            if (jedis != null) {
                String loginInfo = jedis.get("user:" + username + ":info");
                if (StringUtils.isNotBlank(loginInfo)) {
                    umsLogin = JSON.parseObject(loginInfo, UmsLogin.class);
                }
            }
            if (umsLogin == null) {
                UmsLogin tmp = new UmsLogin();
                tmp.setUsername(username);
                umsLogin = getFromDb(tmp);
                jedis.setex("user:" + umsLogin.getUsername() + ":info", 60 * 60 * 2, JSON.toJSONString(umsLogin));
            }
        } finally {
            jedis.close();
        }
        return umsLogin;
    }

    @Override
    public UmsLogin modifyUsernameOrEmail(UmsLogin umsLogin) {
        // 如果修改了用户名
        UmsLogin origin = new UmsLogin();
        origin.setId(umsLogin.getId());
        origin = getFromDb(origin);
        if (umsLogin.getUsername() != null) {
            Jedis jedis = null;
            try {
                jedis = redisUtil.getJedis();
                if (jedis != null) {
                    // String key = jedis.get("user:" + origin.getUsername() + ":info");
                    String key = "user:" + origin.getUsername() + ":info";
                    jedis.del(key);
                    deleteToken(umsLogin.getId());
                    userMapper.updateByPrimaryKeySelective(umsLogin);
                    origin = getFromDb(umsLogin);
                    jedis.setex("user:" + umsLogin.getUsername() + ":info", 60 * 60 * 2, JSON.toJSONString(origin));
                }
                return origin;
            } finally {
                jedis.close();
            }
        } else {
            userMapper.updateByPrimaryKeySelective(umsLogin);
            syncCache(umsLogin.getId());
            return getFromDb(umsLogin);
        }
    }

    private void syncCache(String userId) {
        UmsLogin tmp = new UmsLogin();
        tmp.setId(userId);
        tmp = getFromDb(tmp);
        Jedis jedis = null;
        try {
            jedis = redisUtil.getJedis();
            if (jedis != null) {
                // String key = jedis.get("user:" + tmp.getUsername() + ":info");
                String key = "user:" + tmp.getUsername() + ":info";
                jedis.del(key);
                jedis.setex("user:" + tmp.getUsername() + ":info", 60 * 60 * 2, JSON.toJSONString(tmp));
            }
        } finally {
            jedis.close();
        }
    }

    private UmsLogin getFromDb(UmsLogin umsLogin) {
        List<UmsLogin> umsLogins = userMapper.select(umsLogin);
        if (!umsLogins.isEmpty()) {
            umsLogin = umsLogins.get(0);
            UmsLoginRole loginRole = new UmsLoginRole();
            loginRole.setLoginId(umsLogin.getId());
            loginRole = umsLoginRoleMapper.selectOne(loginRole);
            umsLogin.setRoleId(loginRole.getRoleId());
            return umsLogins.get(0);
        }
        return null;
    }
}