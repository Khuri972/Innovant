package com.innovent.erp.helpDesk.model;

import java.io.Serializable;

public class SerialNoModel implements Serializable {

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getBrand() {
        return brand;
    }

    public void setBrand(String brand) {
        this.brand = brand;
    }

    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public String getSerialNo() {
        return serialNo;
    }

    public void setSerialNo(String serialNo) {
        this.serialNo = serialNo;
    }

    public String getPurchaseDate() {
        return purchaseDate;
    }

    public void setPurchaseDate(String purchaseDate) {
        this.purchaseDate = purchaseDate;
    }

    String id;
    String name;

    public String getHsn_code() {
        return hsn_code;
    }

    public void setHsn_code(String hsn_code) {
        this.hsn_code = hsn_code;
    }

    String hsn_code;
    String brand;
    String model;
    String color;
    String serialNo;
    String purchaseDate;

    public String getGaurantee_period() {
        return gaurantee_period;
    }

    public void setGaurantee_period(String gaurantee_period) {
        this.gaurantee_period = gaurantee_period;
    }

    public String getParts_gaurantee() {
        return parts_gaurantee;
    }

    public void setParts_gaurantee(String parts_gaurantee) {
        this.parts_gaurantee = parts_gaurantee;
    }

    public String getFull_body_gaurantee() {
        return full_body_gaurantee;
    }

    public void setFull_body_gaurantee(String full_body_gaurantee) {
        this.full_body_gaurantee = full_body_gaurantee;
    }

    String gaurantee_period;
    String parts_gaurantee;
    String full_body_gaurantee;


    public int getQty() {
        return qty;
    }

    public void setQty(int qty) {
        this.qty = qty;
    }

    int qty;

    public int getAck() {
        return ack;
    }

    public void setAck(int ack) {
        this.ack = ack;
    }

    int ack;
}
