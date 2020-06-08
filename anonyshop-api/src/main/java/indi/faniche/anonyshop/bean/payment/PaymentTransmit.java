package indi.faniche.anonyshop.bean.payment;

import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.math.BigDecimal;
import java.util.Date;
import java.io.Serializable;

/**
 * @file: PaymentTransmit
 * @author Faniche
 * @date: 2020-04-09 09:28:50
 */

public class PaymentTransmit implements Serializable {
    private static final long serialVersionUID = 997350397864700767L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private String id;
    private String salerId;
    private BigDecimal amount;
    private Integer count;
    private Date createDate;
    private String orderId;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getSalerId() {
        return salerId;
    }

    public void setSalerId(String salerId) {
        this.salerId = salerId;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public Integer getCount() {
        return count;
    }

    public void setCount(Integer count) {
        this.count = count;
    }

    public Date getCreateDate() {
        return createDate;
    }

    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }
}