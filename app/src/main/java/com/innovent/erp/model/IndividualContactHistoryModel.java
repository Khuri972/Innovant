package com.innovent.erp.model;

import java.io.Serializable;

/**
 * Created by CRAFT BOX on 3/1/2018.
 */

public class IndividualContactHistoryModel implements Serializable{

    String id;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getLabel_id() {
        return label_id;
    }

    public void setLabel_id(String label_id) {
        this.label_id = label_id;
    }

    public String getTag_id() {
        return tag_id;
    }

    public void setTag_id(String tag_id) {
        this.tag_id = tag_id;
    }

    public String getPerson_name() {
        return person_name;
    }

    public void setPerson_name(String person_name) {
        this.person_name = person_name;
    }

    public String getPerson_jobtitle() {
        return person_jobtitle;
    }

    public void setPerson_jobtitle(String person_jobtitle) {
        this.person_jobtitle = person_jobtitle;
    }

    public String getPerson_home_address() {
        return person_home_address;
    }

    public void setPerson_home_address(String person_home_address) {
        this.person_home_address = person_home_address;
    }

    public String getPerson_city() {
        return person_city;
    }

    public void setPerson_city(String person_city) {
        this.person_city = person_city;
    }

    public String getPerson_state() {
        return person_state;
    }

    public void setPerson_state(String person_state) {
        this.person_state = person_state;
    }

    public String getPerson_country() {
        return person_country;
    }

    public void setPerson_country(String person_country) {
        this.person_country = person_country;
    }

    public String getPerson_pincode() {
        return person_pincode;
    }

    public void setPerson_pincode(String person_pincode) {
        this.person_pincode = person_pincode;
    }

    public String getPerson_home_phone() {
        return person_home_phone;
    }

    public void setPerson_home_phone(String person_home_phone) {
        this.person_home_phone = person_home_phone;
    }

    public String getPerson_mobile() {
        return person_mobile;
    }

    public void setPerson_mobile(String person_mobile) {
        this.person_mobile = person_mobile;
    }

    public String getPerson_whatsapp() {
        return person_whatsapp;
    }

    public void setPerson_whatsapp(String person_whatsapp) {
        this.person_whatsapp = person_whatsapp;
    }

    public String getPerson_email() {
        return person_email;
    }

    public void setPerson_email(String person_email) {
        this.person_email = person_email;
    }

    public String getPerson_website() {
        return person_website;
    }

    public void setPerson_website(String person_website) {
        this.person_website = person_website;
    }

    public String getPerson_birthdate() {
        return person_birthdate;
    }

    public void setPerson_birthdate(String person_birthdate) {
        this.person_birthdate = person_birthdate;
    }

    public String getPerson_anniversary() {
        return person_anniversary;
    }

    public void setPerson_anniversary(String person_anniversary) {
        this.person_anniversary = person_anniversary;
    }

    public String getPerson_event() {
        return person_event;
    }

    public void setPerson_event(String person_event) {
        this.person_event = person_event;
    }

    public String getPerson_note() {
        return person_note;
    }

    public void setPerson_note(String person_note) {
        this.person_note = person_note;
    }

    public String getPerson_adhar_no() {
        return person_adhar_no;
    }

    public void setPerson_adhar_no(String person_adhar_no) {
        this.person_adhar_no = person_adhar_no;
    }

    public String getPerson_pan_no() {
        return person_pan_no;
    }

    public void setPerson_pan_no(String person_pan_no) {
        this.person_pan_no = person_pan_no;
    }

    public String getCourier_address() {
        return courier_address;
    }

    public void setCourier_address(String courier_address) {
        this.courier_address = courier_address;
    }

    public String getPrint_label() {
        return print_label;
    }

    public void setPrint_label(String print_label) {
        this.print_label = print_label;
    }

    String label_id;
    String tag_id;
    String person_name;
    String person_jobtitle;
    String person_home_address;
    String person_area;

    String person_city;
    String person_district;
    String person_state;
    String person_country;

    String person_city_name;
    String person_district_name;
    String person_state_name;
    String person_country_name;

    String person_pincode;
    String person_home_phone;
    String person_mobile;
    String person_whatsapp;
    String person_email;
    String person_website;
    String person_birthdate;
    String person_anniversary;
    String person_event;

    public String getPerson_eventNote() {
        return person_eventNote;
    }

    public void setPerson_eventNote(String person_eventNote) {
        this.person_eventNote = person_eventNote;
    }

    String person_eventNote;
    String person_note;
    String person_adhar_no;
    String person_pan_no;
    String courier_address;
    String print_label;

    public String getPerson_middle_name() {
        return person_middle_name;
    }

    public void setPerson_middle_name(String person_middle_name) {
        this.person_middle_name = person_middle_name;
    }

    public String getPerson_surname() {
        return person_surname;
    }

    public void setPerson_surname(String person_surname) {
        this.person_surname = person_surname;
    }

    String person_middle_name,person_surname;

    public String getImage_path() {
        return image_path;
    }

    public void setImage_path(String image_path) {
        this.image_path = image_path;
    }

    String image_path;

    public String getLabel_slug() {
        return label_slug;
    }

    public void setLabel_slug(String label_slug) {
        this.label_slug = label_slug;
    }

    public String getTag_slug() {
        return tag_slug;
    }

    public void setTag_slug(String tag_slug) {
        this.tag_slug = tag_slug;
    }

    String label_slug;
    String tag_slug;

    public String getEmployee_slug() {
        return employee_slug;
    }

    public void setEmployee_slug(String employee_slug) {
        this.employee_slug = employee_slug;
    }

    public String getEmployee_id() {
        return employee_id;
    }

    public void setEmployee_id(String employee_id) {
        this.employee_id = employee_id;
    }

    String employee_id;
    String employee_slug;
    String person_office_area;
    String person_office_address;

    String person_office_district;
    String person_office_city;
    String person_office_state;
    String person_office_country;

    String person_office_district_name;
    String person_office_city_name;
    String person_office_state_name;

    public String getPerson_city_name() {
        return person_city_name;
    }

    public void setPerson_city_name(String person_city_name) {
        this.person_city_name = person_city_name;
    }

    public String getPerson_district_name() {
        return person_district_name;
    }

    public void setPerson_district_name(String person_district_name) {
        this.person_district_name = person_district_name;
    }

    public String getPerson_state_name() {
        return person_state_name;
    }

    public void setPerson_state_name(String person_state_name) {
        this.person_state_name = person_state_name;
    }

    public String getPerson_country_name() {
        return person_country_name;
    }

    public void setPerson_country_name(String person_country_name) {
        this.person_country_name = person_country_name;
    }

    public String getPerson_office_district_name() {
        return person_office_district_name;
    }

    public void setPerson_office_district_name(String person_office_district_name) {
        this.person_office_district_name = person_office_district_name;
    }

    public String getPerson_office_city_name() {
        return person_office_city_name;
    }

    public void setPerson_office_city_name(String person_office_city_name) {
        this.person_office_city_name = person_office_city_name;
    }

    public String getPerson_office_state_name() {
        return person_office_state_name;
    }

    public void setPerson_office_state_name(String person_office_state_name) {
        this.person_office_state_name = person_office_state_name;
    }

    public String getPerson_office_country_name() {
        return person_office_country_name;
    }

    public void setPerson_office_country_name(String person_office_country_name) {
        this.person_office_country_name = person_office_country_name;
    }

    String person_office_country_name;

    public String getPerson_office_district() {
        return person_office_district;
    }

    public void setPerson_office_district(String person_office_district) {
        this.person_office_district = person_office_district;
    }

    public String getPerson_area() {
        return person_area;
    }

    public void setPerson_area(String person_area) {
        this.person_area = person_area;
    }

    public String getPerson_district() {
        return person_district;
    }

    public void setPerson_district(String person_district) {
        this.person_district = person_district;
    }

    public String getPerson_office_area() {
        return person_office_area;
    }

    public void setPerson_office_area(String person_office_area) {
        this.person_office_area = person_office_area;
    }


    String person_office_pincode;
    String person_bank_name;
    String person_account_name;
    String person_bank_acc_no;
    String person_bank_ifsc;

    public String getSame_as_office() {
        return same_as_office;
    }

    public void setSame_as_office(String same_as_office) {
        this.same_as_office = same_as_office;
    }

    public String getPerson_office_address() {
        return person_office_address;
    }

    public void setPerson_office_address(String person_office_address) {
        this.person_office_address = person_office_address;
    }

    public String getPerson_office_country() {
        return person_office_country;
    }

    public void setPerson_office_country(String person_office_country) {
        this.person_office_country = person_office_country;
    }

    public String getPerson_office_state() {
        return person_office_state;
    }

    public void setPerson_office_state(String person_office_state) {
        this.person_office_state = person_office_state;
    }

    public String getPerson_office_city() {
        return person_office_city;
    }

    public void setPerson_office_city(String person_office_city) {
        this.person_office_city = person_office_city;
    }

    public String getPerson_office_pincode() {
        return person_office_pincode;
    }

    public void setPerson_office_pincode(String person_office_pincode) {
        this.person_office_pincode = person_office_pincode;
    }

    public String getPerson_bank_name() {
        return person_bank_name;
    }

    public void setPerson_bank_name(String person_bank_name) {
        this.person_bank_name = person_bank_name;
    }

    public String getPerson_account_name() {
        return person_account_name;
    }

    public void setPerson_account_name(String person_account_name) {
        this.person_account_name = person_account_name;
    }

    public String getPerson_bank_acc_no() {
        return person_bank_acc_no;
    }

    public void setPerson_bank_acc_no(String person_bank_acc_no) {
        this.person_bank_acc_no = person_bank_acc_no;
    }

    public String getPerson_bank_ifsc() {
        return person_bank_ifsc;
    }

    public void setPerson_bank_ifsc(String person_bank_ifsc) {
        this.person_bank_ifsc = person_bank_ifsc;
    }

    String same_as_office;
}
