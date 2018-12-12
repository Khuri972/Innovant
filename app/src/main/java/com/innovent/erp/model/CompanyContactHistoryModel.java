package com.innovent.erp.model;

import java.io.Serializable;

/**
 * Created by CRAFT BOX on 2/24/2018.
 */

public class CompanyContactHistoryModel implements Serializable {

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getEmployee_id() {
        return employee_id;
    }

    public void setEmployee_id(String employee_id) {
        this.employee_id = employee_id;
    }

    public String getTag_id() {
        return tag_id;
    }

    public void setTag_id(String tag_id) {
        this.tag_id = tag_id;
    }

    public String getCompany_name() {
        return company_name;
    }

    public void setCompany_name(String company_name) {
        this.company_name = company_name;
    }

    public String getCompany_address() {
        return company_address;
    }

    public void setCompany_address(String company_address) {
        this.company_address = company_address;
    }

    public String getCompany_country() {
        return company_country;
    }

    public void setCompany_country(String company_country) {
        this.company_country = company_country;
    }

    public String getCompany_state() {
        return company_state;
    }

    public void setCompany_state(String company_state) {
        this.company_state = company_state;
    }

    public String getCompany_city() {
        return company_city;
    }

    public void setCompany_city(String company_city) {
        this.company_city = company_city;
    }

    public String getCompany_pincode() {
        return company_pincode;
    }

    public void setCompany_pincode(String company_pincode) {
        this.company_pincode = company_pincode;
    }

    public String getCompany_office_phone() {
        return company_office_phone;
    }

    public void setCompany_office_phone(String company_office_phone) {
        this.company_office_phone = company_office_phone;
    }

    public String getCompany_mobile() {
        return company_mobile;
    }

    public void setCompany_mobile(String company_mobile) {
        this.company_mobile = company_mobile;
    }

    public String getCompany_email() {
        return company_email;
    }

    public void setCompany_email(String company_email) {
        this.company_email = company_email;
    }

    public String getCompany_website() {
        return company_website;
    }

    public void setCompany_website(String company_website) {
        this.company_website = company_website;
    }

    public String getCompany_wharehouse_address() {
        return company_wharehouse_address;
    }

    public void setCompany_wharehouse_address(String company_wharehouse_address) {
        this.company_wharehouse_address = company_wharehouse_address;
    }

    public String getCompany_wharehouse_country() {
        return company_wharehouse_country;
    }

    public void setCompany_wharehouse_country(String company_wharehouse_country) {
        this.company_wharehouse_country = company_wharehouse_country;
    }

    public String getCompany_wharehouse_state() {
        return company_wharehouse_state;
    }

    public void setCompany_wharehouse_state(String company_wharehouse_state) {
        this.company_wharehouse_state = company_wharehouse_state;
    }

    public String getCompany_wharehouse_city() {
        return company_wharehouse_city;
    }

    public void setCompany_wharehouse_city(String company_wharehouse_city) {
        this.company_wharehouse_city = company_wharehouse_city;
    }

    public String getCompany_wharehouse_pincode() {
        return company_wharehouse_pincode;
    }

    public void setCompany_wharehouse_pincode(String company_wharehouse_pincode) {
        this.company_wharehouse_pincode = company_wharehouse_pincode;
    }

    public String getCompany_wharehouse_phone() {
        return company_wharehouse_phone;
    }

    public void setCompany_wharehouse_phone(String company_wharehouse_phone) {
        this.company_wharehouse_phone = company_wharehouse_phone;
    }

    public String getCompany_wharehouse_mobile() {
        return company_wharehouse_mobile;
    }

    public void setCompany_wharehouse_mobile(String company_wharehouse_mobile) {
        this.company_wharehouse_mobile = company_wharehouse_mobile;
    }

    public String getCompany_wharehouse_email() {
        return company_wharehouse_email;
    }

    public void setCompany_wharehouse_email(String company_wharehouse_email) {
        this.company_wharehouse_email = company_wharehouse_email;
    }

    public String getCompany_gst_no() {
        return company_gst_no;
    }

    public void setCompany_gst_no(String company_gst_no) {
        this.company_gst_no = company_gst_no;
    }

    public String getCompany_pan_no() {
        return company_pan_no;
    }

    public void setCompany_pan_no(String company_pan_no) {
        this.company_pan_no = company_pan_no;
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

    String id;
    String employee_id;
    String tag_id;
    String company_name;
    String company_address;


    public String getCompany_zone() {
        return company_zone;
    }

    public void setCompany_zone(String company_zone) {
        this.company_zone = company_zone;
    }

    public String getCompany_area() {
        return company_area;
    }

    public void setCompany_area(String company_area) {
        this.company_area = company_area;
    }

    public String getCompany_wharehouse_zone() {
        return company_wharehouse_zone;
    }

    public void setCompany_wharehouse_zone(String company_wharehouse_zone) {
        this.company_wharehouse_zone = company_wharehouse_zone;
    }

    public String getCompany_wharehouse_area() {
        return company_wharehouse_area;
    }

    public void setCompany_wharehouse_area(String company_wharehouse_area) {
        this.company_wharehouse_area = company_wharehouse_area;
    }

    String company_area;
    String company_district;
    String company_city;
    String company_state;
    String company_country;
    String company_zone;
    String company_pincode;

    public String getCompany_city_name() {
        return company_city_name;
    }

    public void setCompany_city_name(String company_city_name) {
        this.company_city_name = company_city_name;
    }

    public String getCompany_district_name() {
        return company_district_name;
    }

    public void setCompany_district_name(String company_district_name) {
        this.company_district_name = company_district_name;
    }

    public String getCompany_state_name() {
        return company_state_name;
    }

    public void setCompany_state_name(String company_state_name) {
        this.company_state_name = company_state_name;
    }

    public String getCompany_country_name() {
        return company_country_name;
    }

    public void setCompany_country_name(String company_country_name) {
        this.company_country_name = company_country_name;
    }

    public String getCompany_zone_name() {
        return company_zone_name;
    }

    public void setCompany_zone_name(String company_zone_name) {
        this.company_zone_name = company_zone_name;
    }

    public String getCompany_wharehouse_city_name() {
        return company_wharehouse_city_name;
    }

    public void setCompany_wharehouse_city_name(String company_wharehouse_city_name) {
        this.company_wharehouse_city_name = company_wharehouse_city_name;
    }

    public String getCompany_wharehouse_district_name() {
        return company_wharehouse_district_name;
    }

    public void setCompany_wharehouse_district_name(String company_wharehouse_district_name) {
        this.company_wharehouse_district_name = company_wharehouse_district_name;
    }

    public String getCompany_wharehouse_state_name() {
        return company_wharehouse_state_name;
    }

    public void setCompany_wharehouse_state_name(String company_wharehouse_state_name) {
        this.company_wharehouse_state_name = company_wharehouse_state_name;
    }

    public String getCompany_wharehouse_country_name() {
        return company_wharehouse_country_name;
    }

    public void setCompany_wharehouse_country_name(String company_wharehouse_country_name) {
        this.company_wharehouse_country_name = company_wharehouse_country_name;
    }

    public String getCompany_wharehouse_zone_name() {
        return company_wharehouse_zone_name;
    }

    public void setCompany_wharehouse_zone_name(String company_wharehouse_zone_name) {
        this.company_wharehouse_zone_name = company_wharehouse_zone_name;
    }

    String company_city_name;
    String company_district_name;
    String company_state_name;
    String company_country_name;
    String company_zone_name;


    public String getCompany_district() {
        return company_district;
    }

    public void setCompany_district(String company_district) {
        this.company_district = company_district;
    }

    String company_office_phone;
    String company_mobile;
    String company_email;
    String company_website;
    String company_wharehouse_address;

    String company_wharehouse_area;

    String company_wharehouse_city;
    String company_wharehouse_district;
    String company_wharehouse_state;
    String company_wharehouse_country;
    String company_wharehouse_zone;

    String company_wharehouse_city_name;
    String company_wharehouse_district_name;
    String company_wharehouse_state_name;
    String company_wharehouse_country_name;
    String company_wharehouse_zone_name;

    String company_wharehouse_pincode;

    public String getCompany_wharehouse_district() {
        return company_wharehouse_district;
    }

    public void setCompany_wharehouse_district(String company_wharehouse_district) {
        this.company_wharehouse_district = company_wharehouse_district;
    }


    String company_wharehouse_phone;
    String company_wharehouse_mobile;
    String company_wharehouse_email;
    String company_gst_no;
    String company_pan_no;
    String courier_address;
    String print_label;

    public String getImage_path() {
        return image_path;
    }

    public void setImage_path(String image_path) {
        this.image_path = image_path;
    }

    String image_path;

    public String getEmployee_slug() {
        return employee_slug;
    }

    public void setEmployee_slug(String employee_slug) {
        this.employee_slug = employee_slug;
    }

    public String getTag_slug() {
        return tag_slug;
    }

    public void setTag_slug(String tag_slug) {
        this.tag_slug = tag_slug;
    }

    String employee_slug;
    String tag_slug;

    public String getLabel_id() {
        return label_id;
    }

    public void setLabel_id(String label_id) {
        this.label_id = label_id;
    }

    public String getLabel_slug() {
        return label_slug;
    }

    public void setLabel_slug(String label_slug) {
        this.label_slug = label_slug;
    }

    String label_id;
    String label_slug;

    public String getCourier_address_wherehouse() {
        return courier_address_wherehouse;
    }

    public void setCourier_address_wherehouse(String courier_address_wherehouse) {
        this.courier_address_wherehouse = courier_address_wherehouse;
    }

    String courier_address_wherehouse;

    public String getSame_as_company() {
        return same_as_company;
    }

    public void setSame_as_company(String same_as_company) {
        this.same_as_company = same_as_company;
    }

    public String getCompany_bank_name() {
        return company_bank_name;
    }

    public void setCompany_bank_name(String company_bank_name) {
        this.company_bank_name = company_bank_name;
    }

    public String getCompany_account_name() {
        return company_account_name;
    }

    public void setCompany_account_name(String company_account_name) {
        this.company_account_name = company_account_name;
    }

    public String getCompany_bank_acc_no() {
        return company_bank_acc_no;
    }

    public void setCompany_bank_acc_no(String company_bank_acc_no) {
        this.company_bank_acc_no = company_bank_acc_no;
    }

    public String getCompany_bank_ifsc() {
        return company_bank_ifsc;
    }

    public void setCompany_bank_ifsc(String company_bank_ifsc) {
        this.company_bank_ifsc = company_bank_ifsc;
    }

    String same_as_company;
    String company_bank_name;
    String company_account_name;
    String company_bank_acc_no;
    String company_bank_ifsc;
}
