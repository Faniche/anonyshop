package indi.faniche.anonyshop.bean.user;

import java.io.Serializable;

/**
 * @File: UmsRolePermission
 * @author: Faniche
 * @since: 2020-04-03 18:44:03
 */

public class UmsRolePermission implements Serializable {
    private static final long serialVersionUID = -60535551753489384L;
    
    private String id;
    
    private String roleId;
    
    private String permissionId;
    

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getRoleId() {
        return roleId;
    }

    public void setRoleId(String roleId) {
        this.roleId = roleId;
    }

    public String getPermissionId() {
        return permissionId;
    }

    public void setPermissionId(String permissionId) {
        this.permissionId = permissionId;
    }

}