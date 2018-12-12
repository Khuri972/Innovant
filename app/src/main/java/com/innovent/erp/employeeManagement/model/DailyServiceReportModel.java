package com.innovent.erp.employeeManagement.model;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by CRAFT BOX on 5/8/2018.
 */

public class DailyServiceReportModel implements Serializable{

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getCustomer_name() {
        return customer_name;
    }

    public void setCustomer_name(String customer_name) {
        this.customer_name = customer_name;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getPurpose() {
        return purpose;
    }

    public void setPurpose(String purpose) {
        this.purpose = purpose;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public ArrayList<DailyNoteModel> getDailyNoteModels() {
        return dailyNoteModels;
    }

    public void setDailyNoteModels(ArrayList<DailyNoteModel> dailyNoteModels) {
        this.dailyNoteModels = dailyNoteModels;
    }

    public ArrayList<DailyFileAttachModel> getDailyFileAttachModels() {
        return dailyFileAttachModels;
    }

    public void setDailyFileAttachModels(ArrayList<DailyFileAttachModel> dailyFileAttachModels) {
        this.dailyFileAttachModels = dailyFileAttachModels;
    }

    String id;
    String customer_name;
    String address;
    String purpose;

    public String getPurpose_slug() {
        return purpose_slug;
    }

    public void setPurpose_slug(String purpose_slug) {
        this.purpose_slug = purpose_slug;
    }

    String purpose_slug;
    String remark;
    String date;

    public String getIsSubmitted() {
        return isSubmitted;
    }

    public void setIsSubmitted(String isSubmitted) {
        this.isSubmitted = isSubmitted;
    }

    String isSubmitted;
    ArrayList<DailyNoteModel> dailyNoteModels = new ArrayList<>();
    ArrayList<DailyFileAttachModel> dailyFileAttachModels = new ArrayList<>();


}
