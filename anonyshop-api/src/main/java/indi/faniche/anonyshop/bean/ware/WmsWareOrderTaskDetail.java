package indi.faniche.anonyshop.bean.ware;

import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.io.Serializable;

/**
 * @file: WmsWareOrderTaskDetail
 * @author Faniche
 * @date: 2020-04-09 09:28:51
 */

public class WmsWareOrderTaskDetail implements Serializable {
    private static final long serialVersionUID = 208764272785729882L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private String id;
    private String skuId;
    private String skuName;
    private String skuNums;
    private String taskId;
    private String skuNum;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getSkuId() {
        return skuId;
    }

    public void setSkuId(String skuId) {
        this.skuId = skuId;
    }

    public String getSkuName() {
        return skuName;
    }

    public void setSkuName(String skuName) {
        this.skuName = skuName;
    }

    public String getSkuNums() {
        return skuNums;
    }

    public void setSkuNums(String skuNums) {
        this.skuNums = skuNums;
    }

    public String getTaskId() {
        return taskId;
    }

    public void setTaskId(String taskId) {
        this.taskId = taskId;
    }

    public String getSkuNum() {
        return skuNum;
    }

    public void setSkuNum(String skuNum) {
        this.skuNum = skuNum;
    }

}