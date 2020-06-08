package indi.faniche.anonyshop.bean.store;

import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.util.Date;
import java.io.Serializable;

/**
 * @file: SmsStore
 * @author Faniche
 * @date: 2020-04-09 09:28:51
 */

public class SmsStore implements Serializable {
    private static final long serialVersionUID = -27522768592422197L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private String id;
    private String ownerId;
    private Date createDate;
    private String point;
    private String status;
    private String name;
    private String description;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getOwnerId() {
        return ownerId;
    }

    public void setOwnerId(String ownerId) {
        this.ownerId = ownerId;
    }

    public Date getCreateDate() {
        return createDate;
    }

    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }

    public String getPoint() {
        return point;
    }

    public void setPoint(String point) {
        this.point = point;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}