package com.innovent.erp.employeeManagement.model;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by CRAFT BOX on 5/8/2018.
 */

public class DailyWorkReportModel implements Serializable{

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getWork_type() {
        return work_type;
    }

    public void setWork_type(String work_type) {
        this.work_type = work_type;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getPerson_name() {
        return person_name;
    }

    public void setPerson_name(String person_name) {
        this.person_name = person_name;
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
    String work_type;
    String address;
    String person_name;
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
