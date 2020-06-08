package indi.faniche.anonyshop.bean.checkout;

/**
 * @file: OmsOrder
 * @author Faniche
 * @date: 2020-04-09 09:28:50
 */

import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Transient;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

public class OmsOrder implements Serializable {
    private static final long serialVersionUID = -31454743474994241L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private String id;
    private String memberId;
    private String orderSn;
    private Date createTime;
    private Date paymentTime;
    private BigDecimal totalAmount;
    private BigDecimal payAmount;
    private BigDecimal freightAmount;
    private BigDecimal integrationAmount;
    private String status;

    private String receiverName;
    private String receiverPhone;
    private String receiverEmail;
    private String receiverProvince;
    private String receiverCity;
    private String receiverRegion;
    private String receiverDetailAddress;
    private String useIntegration;
    private String autoConfirmDay;
    private Date receiveTime;
    private String note;

    private Integer billType;
    private String billHeader;
    private String billContent;
    private String billReceiverPhone;
    private String billReceiverEmail;

    private Integer confirmStatus;
    private String storeId;

    @Transient
    private String setDefaultAddress;

    @Transient
    private String tradeCode;

    @Transient
    private List<OmsOrderItem> omsOrderItemList;

    @Transient
    private String orderStatusStr;

    @Transient
    private String storeName;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getMemberId() {
        return memberId;
    }

    public void setMemberId(String memberId) {
        this.memberId = memberId;
    }

    public String getOrderSn() {
        return orderSn;
    }

    public void setOrderSn(String orderSn) {
        this.orderSn = orderSn;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Date getPaymentTime() {
        return paymentTime;
    }

    public void setPaymentTime(Date paymentTime) {
        this.paymentTime = paymentTime;
    }

    public BigDecimal getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(BigDecimal totalAmount) {
        this.totalAmount = totalAmount;
    }

    public BigDecimal getPayAmount() {
        return payAmount;
    }

    public void setPayAmount(BigDecimal payAmount) {
        this.payAmount = payAmount;
    }

    public BigDecimal getFreightAmount() {
        return freightAmount;
    }

    public void setFreightAmount(BigDecimal freightAmount) {
        this.freightAmount = freightAmount;
    }

    public BigDecimal getIntegrationAmount() {
        return integrationAmount;
    }

    public void setIntegrationAmount(BigDecimal integrationAmount) {
        this.integrationAmount = integrationAmount;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getReceiverName() {
        return receiverName;
    }

    public void setReceiverName(String receiverName) {
        this.receiverName = receiverName;
    }

    public String getReceiverPhone() {
        return receiverPhone;
    }

    public void setReceiverPhone(String receiverPhone) {
        this.receiverPhone = receiverPhone;
    }

    public String getReceiverEmail() {
        return receiverEmail;
    }

    public void setReceiverEmail(String receiverEmail) {
        this.receiverEmail = receiverEmail;
    }

    public String getReceiverProvince() {
        return receiverProvince;
    }

    public void setReceiverProvince(String receiverProvince) {
        this.receiverProvince = receiverProvince;
    }

    public String getReceiverCity() {
        return receiverCity;
    }

    public void setReceiverCity(String receiverCity) {
        this.receiverCity = receiverCity;
    }

    public String getReceiverRegion() {
        return receiverRegion;
    }

    public void setReceiverRegion(String receiverRegion) {
        this.receiverRegion = receiverRegion;
    }

    public String getReceiverDetailAddress() {
        return receiverDetailAddress;
    }

    public void setReceiverDetailAddress(String receiverDetailAddress) {
        this.receiverDetailAddress = receiverDetailAddress;
    }

    public String getUseIntegration() {
        return useIntegration;
    }

    public void setUseIntegration(String useIntegration) {
        this.useIntegration = useIntegration;
    }

    public String getAutoConfirmDay() {
        return autoConfirmDay;
    }

    public void setAutoConfirmDay(String autoConfirmDay) {
        this.autoConfirmDay = autoConfirmDay;
    }

    public Date getReceiveTime() {
        return receiveTime;
    }

    public void setReceiveTime(Date receiveTime) {
        this.receiveTime = receiveTime;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public String getSetDefaultAddress() {
        return setDefaultAddress;
    }

    public void setSetDefaultAddress(String setDefaultAddress) {
        this.setDefaultAddress = setDefaultAddress;
    }

    public String getTradeCode() {
        return tradeCode;
    }

    public void setTradeCode(String tradeCode) {
        this.tradeCode = tradeCode;
    }

    public List<OmsOrderItem> getOmsOrderItemList() {
        return omsOrderItemList;
    }

    public void setOmsOrderItemList(List<OmsOrderItem> omsOrderItemList) {
        this.omsOrderItemList = omsOrderItemList;
    }

    public Integer getBillType() {
        return billType;
    }

    public void setBillType(Integer billType) {
        this.billType = billType;
    }

    public String getBillHeader() {
        return billHeader;
    }

    public void setBillHeader(String billHeader) {
        this.billHeader = billHeader;
    }

    public String getBillContent() {
        return billContent;
    }

    public void setBillContent(String billContent) {
        this.billContent = billContent;
    }

    public String getBillReceiverPhone() {
        return billReceiverPhone;
    }

    public void setBillReceiverPhone(String billReceiverPhone) {
        this.billReceiverPhone = billReceiverPhone;
    }

    public String getBillReceiverEmail() {
        return billReceiverEmail;
    }

    public void setBillReceiverEmail(String billReceiverEmail) {
        this.billReceiverEmail = billReceiverEmail;
    }

    public Integer getConfirmStatus() {
        return confirmStatus;
    }

    public void setConfirmStatus(Integer confirmStatus) {
        this.confirmStatus = confirmStatus;
    }

    public String getStoreId() {
        return storeId;
    }

    public void setStoreId(String storeId) {
        this.storeId = storeId;
    }

    public String getOrderStatusStr() {
        return orderStatusStr;
    }

    public void setOrderStatusStr(String orderStatusStr) {
        this.orderStatusStr = orderStatusStr;
    }

    public String getStoreName() {
        return storeName;
    }

    public void setStoreName(String storeName) {
        this.storeName = storeName;
    }
}
