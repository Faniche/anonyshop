package indi.faniche.anonyshop.bean.checkout;

import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Transient;
import java.io.Serializable;
import java.util.Date;

/**
 * @file: OmsDelivery
 * @author Faniche
 * @date: 2020-04-09 09:28:50
 */

public class OmsDelivery implements Serializable {
    private static final long serialVersionUID = -36865534403927034L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private String id;
    private String orderId;
    private String storeId;
    private String deliveryCompany;
    private String deliverySeq;
    private String currentProvince;
    private String currentCity;
    private String currentCounty;
    private String status;
    private Date createDate;
    private String destProvince;
    private String destCity;
    private String destRegion;
    private String destDetailAddr;
    private String contact;

    private String wareId;
    private Date sendDate;

    private String userId;

    private String note;

    @Transient
    private String statusStr;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public String getStoreId() {
        return storeId;
    }

    public void setStoreId(String storeId) {
        this.storeId = storeId;
    }

    public String getDeliveryCompany() {
        return deliveryCompany;
    }

    public void setDeliveryCompany(String deliveryCompany) {
        this.deliveryCompany = deliveryCompany;
    }

    public String getDeliverySeq() {
        return deliverySeq;
    }

    public void setDeliverySeq(String deliverySeq) {
        this.deliverySeq = deliverySeq;
    }

    public String getCurrentProvince() {
        return currentProvince;
    }

    public void setCurrentProvince(String currentProvince) {
        this.currentProvince = currentProvince;
    }

    public String getCurrentCity() {
        return currentCity;
    }

    public void setCurrentCity(String currentCity) {
        this.currentCity = currentCity;
    }

    public String getCurrentCounty() {
        return currentCounty;
    }

    public void setCurrentCounty(String currentCounty) {
        this.currentCounty = currentCounty;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getDestProvince() {
        return destProvince;
    }

    public void setDestProvince(String destProvince) {
        this.destProvince = destProvince;
    }

    public String getDestCity() {
        return destCity;
    }

    public void setDestCity(String destCity) {
        this.destCity = destCity;
    }

    public String getDestRegion() {
        return destRegion;
    }

    public void setDestRegion(String destRegion) {
        this.destRegion = destRegion;
    }

    public String getDestDetailAddr() {
        return destDetailAddr;
    }

    public void setDestDetailAddr(String destDetailAddr) {
        this.destDetailAddr = destDetailAddr;
    }

    public String getContact() {
        return contact;
    }

    public void setContact(String contact) {
        this.contact = contact;
    }

    public String getWareId() {
        return wareId;
    }

    public void setWareId(String wareId) {
        this.wareId = wareId;
    }

    public Date getSendDate() {
        return sendDate;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public void setSendDate(Date sendDate) {
        this.sendDate = sendDate;
    }

    public String getStatusStr() {
        return statusStr;
    }

    public void setStatusStr(String statusStr) {
        this.statusStr = statusStr;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public Date getCreateDate() {
        return createDate;
    }

    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }
}