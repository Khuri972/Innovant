package com.innovent.erp.visitorBookModual.model;

import java.io.Serializable;

/**
 * Created by CRAFT BOX on 12/19/2017.
 */

public class FollowUpModel implements Serializable{

    String id;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getVisitor_id() {
        return visitor_id;
    }

    public void setVisitor_id(String visitor_id) {
        this.visitor_id = visitor_id;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getThrough() {
        return through;
    }

    public void setThrough(String through) {
        this.through = through;
    }

    public String getFollowup_date() {
        return followup_date;
    }

    public void setFollowup_date(String followup_date) {
        this.followup_date = followup_date;
    }

    public String getFuture_date() {
        return future_date;
    }

    public void setFuture_date(String future_date) {
        this.future_date = future_date;
    }

    public String getResponse_date() {
        return response_date;
    }

    public void setResponse_date(String response_date) {
        this.response_date = response_date;
    }

    public String getNext_action() {
        return next_action;
    }

    public void setNext_action(String next_action) {
        this.next_action = next_action;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    String visitor_id;
    String description;
    String through;

    public String getThrough_slug() {
        return through_slug;
    }

    public void setThrough_slug(String through_slug) {
        this.through_slug = through_slug;
    }

    String through_slug;
    String followup_date;
    String future_date;
    String response_date;
    String next_action;
    String status;

    public String getStatus_slug() {
        return status_slug;
    }

    public void setStatus_slug(String status_slug) {
        this.status_slug = status_slug;
    }

    String status_slug;

    public String getResponse() {
        return response;
    }

    public void setResponse(String response) {
        this.response = response;
    }

    String response;

    public String getFollowupBy() {
        return followupBy;
    }

    public void setFollowupBy(String followupBy) {
        this.followupBy = followupBy;
    }

    String followupBy;

    public String getCategoryName() {
        return categoryName;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }

    String categoryName;

    public String getVisitor_name() {
        return visitor_name;
    }

    public void setVisitor_name(String visitor_name) {
        this.visitor_name = visitor_name;
    }

    public String getVisitor_email() {
        return visitor_email;
    }

    public void setVisitor_email(String visitor_email) {
        this.visitor_email = visitor_email;
    }

    public String getInquiryBy() {
        return inquiryBy;
    }

    public void setInquiryBy(String inquiryBy) {
        this.inquiryBy = inquiryBy;
    }

    String visitor_name;

    public String getVisitor_mobile_no() {
        return visitor_mobile_no;
    }

    public void setVisitor_mobile_no(String visitor_mobile_no) {
        this.visitor_mobile_no = visitor_mobile_no;
    }

    String visitor_mobile_no;
    String visitor_email;
    String inquiryBy;

    public int getRating() {
        return rating;
    }

    public void setRating(int rating) {
        this.rating = rating;
    }

    int rating;
}
