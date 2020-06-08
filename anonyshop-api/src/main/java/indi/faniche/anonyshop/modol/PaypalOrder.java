package indi.faniche.anonyshop.modol;

/* File:   PaypalOrder.java
 * -------------------------
 * Author: faniche
 * Date:   5/5/20
 */

import java.math.BigDecimal;

public class PaypalOrder {
    private BigDecimal price;
    private String currency;
    private String method;
    private String intent;
    private String description;

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public String getMethod() {
        return method;
    }

    public void setMethod(String method) {
        this.method = method;
    }

    public String getIntent() {
        return intent;
    }

    public void setIntent(String intent) {
        this.intent = intent;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
