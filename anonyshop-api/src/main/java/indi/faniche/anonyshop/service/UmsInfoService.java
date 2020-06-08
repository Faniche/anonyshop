package indi.faniche.anonyshop.service;

/* File:   UmsInfoService.java
 * -------------------------
 * Author: faniche
 * Date:   5/9/20
 */

import indi.faniche.anonyshop.bean.user.UmsInfo;

public interface UmsInfoService {
    UmsInfo getUserInfo(String userId);
}
