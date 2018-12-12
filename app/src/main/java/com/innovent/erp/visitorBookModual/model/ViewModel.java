package com.innovent.erp.visitorBookModual.model;

/**
 * Created by Craftbox-4 on 30-Oct-17.
 */

public class ViewModel {

    String id;
    String name;
    String email;
    String mobile_no;

    String reference;

    public String getReference() {
        return reference;
    }

    public void setReference(String reference) {
        this.reference = reference;
    }

    public String getTotal_Visitor() {
        return Total_Visitor;
    }

    public void setTotal_Visitor(String total_Visitor) {
        Total_Visitor = total_Visitor;
    }

    String detail;
    String created_date;
    String category_name;
    String Total_Visitor;

    public String getInquiryBy() {
        return inquiryBy;
    }

    public void setInquiryBy(String inquiryBy) {
        this.inquiryBy = inquiryBy;
    }

    String inquiryBy;
    public String getProject_manager_name() {
        return project_manager_name;
    }

    public void setProject_manager_name(String project_manager_name) {
        this.project_manager_name = project_manager_name;
    }

    String project_manager_name;

    public int getRating() {
        return rating;
    }

    public void setRating(int rating) {
        this.rating = rating;
    }

    int rating;

    public String getCategory_name() {
        return category_name;
    }

    public void setCategory_name(String category_name) {
        this.category_name = category_name;
    }

    public ViewModel() {
    }

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

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getMobile_no() {
        return mobile_no;
    }

    public void setMobile_no(String mobile_no) {
        this.mobile_no = mobile_no;
    }

    public String getDetail() {
        return detail;
    }

    public void setDetail(String detail) {
        this.detail = detail;
    }

    public String getCreated_date() {
        return created_date;
    }

    public void setCreated_date(String created_date) {
        this.created_date = created_date;
    }
}
