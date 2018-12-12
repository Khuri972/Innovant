package com.innovent.erp.model;

/**
 * Created by CRAFT BOX on 4/12/2018.
 */

public class CurrencyModel {

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public String getCurrency_object() {
        return currency_object;
    }

    public void setCurrency_object(String currency_object) {
        this.currency_object = currency_object;
    }

    public String getTotal() {
        return total;
    }

    public void setTotal(String total) {
        this.total = total;
    }

    String id,title,desc,currency_object,total;
}
