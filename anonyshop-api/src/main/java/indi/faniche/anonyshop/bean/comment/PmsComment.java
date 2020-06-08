package indi.faniche.anonyshop.bean.comment;

import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Transient;
import java.util.Date;
import java.io.Serializable;

/**
 * @file: PmsComment
 * @author Faniche
 * @date: 2020-04-09 09:28:50
 */

public class PmsComment implements Serializable {
    private static final long serialVersionUID = 681477496377575036L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private String id;
    private String productId;
    private String memberNickName;
    private String productName;
    private String star;
    private String memberIp;
    private Date createTime;
    private String showStatus;
    private String productAttribute;
    private String content;
    private String pics;
    private String memberIcon;
    private String storeId;

    @Transient
    private Integer productScore;
    @Transient
    private Integer serviceScore;
    @Transient
    private Integer deliveryScore;
    @Transient
    private int[] starArray;
    @Transient
    private int[] emptyStarArray;

    public String getStoreId() {
        return storeId;
    }

    public void setStoreId(String storeId) {
        this.storeId = storeId;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getProductId() {
        return productId;
    }

    public void setProductId(String productId) {
        this.productId = productId;
    }

    public String getMemberNickName() {
        return memberNickName;
    }

    public void setMemberNickName(String memberNickName) {
        this.memberNickName = memberNickName;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public String getStar() {
        return star;
    }

    public void setStar(String star) {
        this.star = star;
    }

    public String getMemberIp() {
        return memberIp;
    }

    public void setMemberIp(String memberIp) {
        this.memberIp = memberIp;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public String getShowStatus() {
        return showStatus;
    }

    public void setShowStatus(String showStatus) {
        this.showStatus = showStatus;
    }

    public String getProductAttribute() {
        return productAttribute;
    }

    public void setProductAttribute(String productAttribute) {
        this.productAttribute = productAttribute;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getPics() {
        return pics;
    }

    public void setPics(String pics) {
        this.pics = pics;
    }

    public String getMemberIcon() {
        return memberIcon;
    }

    public void setMemberIcon(String memberIcon) {
        this.memberIcon = memberIcon;
    }

    public Integer getProductScore() {
        return productScore;
    }

    public void setProductScore(Integer productScore) {
        this.productScore = productScore;
    }

    public Integer getServiceScore() {
        return serviceScore;
    }

    public void setServiceScore(Integer serviceScore) {
        this.serviceScore = serviceScore;
    }

    public Integer getDeliveryScore() {
        return deliveryScore;
    }

    public void setDeliveryScore(Integer deliveryScore) {
        this.deliveryScore = deliveryScore;
    }

    public int[] getStarArray() {
        return starArray;
    }

    public void setStarArray(int[] starArray) {
        this.starArray = starArray;
    }

    public int[] getEmptyStarArray() {
        return emptyStarArray;
    }

    public void setEmptyStarArray(int[] emptyStarArray) {
        this.emptyStarArray = emptyStarArray;
    }
}