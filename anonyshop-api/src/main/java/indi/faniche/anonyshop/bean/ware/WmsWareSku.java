package indi.faniche.anonyshop.bean.ware;

import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.io.Serializable;

/**
 * @file: WmsWareSku
 * @author Faniche
 * @date: 2020-04-09 09:28:51
 */

public class WmsWareSku implements Serializable {
    private static final long serialVersionUID = -25829290159410823L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private String id;
    private String skuId;
    private String warehouseId;
    private String stock;
    private String stockName;
    private String stockLocked;

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

    public String getWarehouseId() {
        return warehouseId;
    }

    public void setWarehouseId(String warehouseId) {
        this.warehouseId = warehouseId;
    }

    public String getStock() {
        return stock;
    }

    public void setStock(String stock) {
        this.stock = stock;
    }

    public String getStockName() {
        return stockName;
    }

    public void setStockName(String stockName) {
        this.stockName = stockName;
    }

    public String getStockLocked() {
        return stockLocked;
    }

    public void setStockLocked(String stockLocked) {
        this.stockLocked = stockLocked;
    }

}