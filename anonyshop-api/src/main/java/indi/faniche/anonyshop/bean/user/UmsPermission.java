package indi.faniche.anonyshop.bean.user;

import java.io.Serializable;

/**
 * @File: UmsPermission
 * @author: Faniche
 * @since: 2020-04-03 18:44:03
 */

public class UmsPermission implements Serializable {
    private static final long serialVersionUID = -81633122759563254L;
    
    private String id;
    
    private String resourceName;
    
    private String resourceUrl;
    

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getResourceName() {
        return resourceName;
    }

    public void setResourceName(String resourceName) {
        this.resourceName = resourceName;
    }

    public String getResourceUrl() {
        return resourceUrl;
    }

    public void setResourceUrl(String resourceUrl) {
        this.resourceUrl = resourceUrl;
    }

}