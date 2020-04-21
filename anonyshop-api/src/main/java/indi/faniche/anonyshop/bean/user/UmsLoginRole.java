package indi.faniche.anonyshop.bean.user;

import java.io.Serializable;
/**
 * @File: UmsLoginRole
 * @author: Faniche
 * @since: 2020-04-03 18:44:03
 */

public class UmsLoginRole implements Serializable {
    private static final long serialVersionUID = -78809036560586104L;
    
    private String id;
    
    private String loginId;

    private String roleId;
    

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getLoginId() {
        return loginId;
    }

    public void setLoginId(String loginId) {
        this.loginId = loginId;
    }

    public String getRoleId() {
        return roleId;
    }

    public void setRoleId(String roleId) {
        this.roleId = roleId;
    }

}