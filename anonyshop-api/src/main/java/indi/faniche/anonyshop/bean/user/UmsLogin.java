package indi.faniche.anonyshop.bean.user;

import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Transient;
import java.io.Serializable;

/**
 * @File: UmsLogin
 * @author: Faniche
 * @since: 2020-04-03 18:44:03
 */

public class UmsLogin implements Serializable {
    private static final long serialVersionUID = 993328303233726587L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private String id;
    
    private String username;
    
    private String password;
    
    private String email;
    
    private String publicKey;

    private String status;

    @Transient
    private String roleId;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPublicKey() {
        return publicKey;
    }

    public void setPublicKey(String publicKey) {
        this.publicKey = publicKey;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getRoleId() {
        return roleId;
    }

    public void setRoleId(String roleId) {
        this.roleId = roleId;
    }
}