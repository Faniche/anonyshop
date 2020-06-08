package indi.faniche.anonyshop.modol;

/* File:   ClassSearch.java
 * -------------------------
 * Author: faniche
 * Date:   4/27/20
 */

import java.io.Serializable;
import java.util.List;

public class ClassSearch implements Serializable {

    private List<FilterItem> filterList;

    private List<String> brandList;

    public List<FilterItem> getFilterList() {
        return filterList;
    }

    public void setFilterList(List<FilterItem> filterList) {
        this.filterList = filterList;
    }

    public List<String> getBrandList() {
        return brandList;
    }

    public void setBrandList(List<String> brandList) {
        this.brandList = brandList;
    }
}
