package indi.faniche.anonyshop.modol;

/* File:   FilterItem.java
 * -------------------------
 * Author: faniche
 * Date:   4/28/20
 */

import java.io.Serializable;
import java.util.List;

public class FilterItem implements Serializable {
    private String key;
    private List<String> value;

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public List<String> getValue() {
        return value;
    }

    public void setValue(List<String> value) {
        this.value = value;
    }
}
