package com.innovent.erp.ErpModule.sales_management.SearchView;

/**
 * Created by CRAFT BOX on 4/24/2018.
 */

public class SearchItemModel {

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public double getSell_price() {
        return sell_price;
    }

    public void setSell_price(double sell_price) {
        this.sell_price = sell_price;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public double getQty() {
        return qty;
    }

    public void setQty(double qty) {
        this.qty = qty;
    }

    public boolean isChecked() {
        return isChecked;
    }

    public void setChecked(boolean checked) {
        isChecked = checked;
    }


    String id;
    String name;
    double sell_price;
    double qty;
    boolean isChecked;

}
