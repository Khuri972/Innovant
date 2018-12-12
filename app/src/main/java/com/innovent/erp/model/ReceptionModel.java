package com.innovent.erp.model;

import java.io.Serializable;

/**
 * Created by CRAFT BOX on 4/13/2018.
 */

public class ReceptionModel implements Serializable {

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTag() {
        return tag;
    }

    public void setTag(String tag) {
        this.tag = tag;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCompany_name() {
        return company_name;
    }

    public void setCompany_name(String company_name) {
        this.company_name = company_name;
    }

    public String getMobile_no() {
        return mobile_no;
    }

    public void setMobile_no(String mobile_no) {
        this.mobile_no = mobile_no;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getWhome_to_meet() {
        return whome_to_meet;
    }

    public void setWhome_to_meet(String whome_to_meet) {
        this.whome_to_meet = whome_to_meet;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getTag_slug() {
        return tag_slug;
    }

    public void setTag_slug(String tag_slug) {
        this.tag_slug = tag_slug;
    }

    public String getLabel_slug() {
        return label_slug;
    }

    public void setLabel_slug(String label_slug) {
        this.label_slug = label_slug;
    }


    String id;
    String tag;
    String tag_slug;
    String label_slug;
    String label;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getPerson_name() {
        return person_name;
    }

    public void setPerson_name(String person_name) {
        this.person_name = person_name;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    String title;
    String person_name;
    String company_name;
    String mobile_no;
    String email;
    String whome_to_meet;
    String description;
    String city;
    String name;
    String date;
    String type;

}
