package com.innovent.erp.employeeManagement.model;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by CRAFT BOX on 5/3/2018.
 */

public class DailySalesReportModel implements Serializable{

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getCompany_name() {
        return company_name;
    }

    public void setCompany_name(String company_name) {
        this.company_name = company_name;
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

    public String getDiscuss() {
        return discuss;
    }

    public void setDiscuss(String discuss) {
        this.discuss = discuss;
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
    String date;
    String company_name;
    String address;
    String person_name;
    String discuss;

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
