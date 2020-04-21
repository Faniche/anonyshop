package indi.faniche.anonyshop.bean.user;

import java.io.Serializable;

/**
 * @File: UmsRole
 * @author: Faniche
 * @since: 2020-04-03 18:44:03
 */

public class UmsRole implements Serializable {
    private static final long serialVersionUID = -65004004722742466L;

    private String id;
    
    private String roleName;
    

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getRoleName() {
        return roleName;
    }

    public void setRoleName(String roleName) {
        this.roleName = roleName;
    }

}