package indi.faniche.anonyshop.service;

import indi.faniche.anonyshop.bean.user.UmsLogin;

import java.util.List;

public interface UserService {
    List<UmsLogin> getAllUmsLogin();

    UmsLogin login(UmsLogin umsLogin);

    void addToken(String token, String userId);

    void deleteToken(String userId);

    UmsLogin addUser(UmsLogin umsLogin);

    void activateAccount(String userId);

    void resetPass(UmsLogin login);

    UmsLogin getUserByUsername(String username);

    UmsLogin modifyUsernameOrEmail(UmsLogin umsLogin);
}
