package com.innovent.erp.ErpModule.sales_management.model;

import java.io.Serializable;

/**
 * Created by CRAFT BOX on 4/23/2018.
 */

public class QuatationRequestModel implements Serializable {

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getQuotation_request_no() {
        return quotation_request_no;
    }

    public void setQuotation_request_no(String quotation_request_no) {
        this.quotation_request_no = quotation_request_no;
    }

    public String getCustomer_id() {
        return customer_id;
    }

    public void setCustomer_id(String customer_id) {
        this.customer_id = customer_id;
    }

    public String getCustomer_name() {
        return customer_name;
    }

    public void setCustomer_name(String customer_name) {
        this.customer_name = customer_name;
    }

    public String getCustomer_email() {
        return customer_email;
    }

    public void setCustomer_email(String customer_email) {
        this.customer_email = customer_email;
    }

    public String getCustomer_phone_1() {
        return customer_phone_1;
    }

    public void setCustomer_phone_1(String customer_phone_1) {
        this.customer_phone_1 = customer_phone_1;
    }

    public String getGrandtotal() {
        return grandtotal;
    }

    public void setGrandtotal(String grandtotal) {
        this.grandtotal = grandtotal;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getStatus_name() {
        return status_name;
    }

    public void setStatus_name(String status_name) {
        this.status_name = status_name;
    }

    public String getQuotation_request_date_format() {
        return quotation_request_date_format;
    }

    public void setQuotation_request_date_format(String quotation_request_date_format) {
        this.quotation_request_date_format = quotation_request_date_format;
    }

    public String getCustomer_phone_2() {
        return customer_phone_2;
    }

    public void setCustomer_phone_2(String customer_phone_2) {
        this.customer_phone_2 = customer_phone_2;
    }

    public String getCustomer_address_1() {
        return customer_address_1;
    }

    public void setCustomer_address_1(String customer_address_1) {
        this.customer_address_1 = customer_address_1;
    }

    public String getCustomer_address_2() {
        return customer_address_2;
    }

    public void setCustomer_address_2(String customer_address_2) {
        this.customer_address_2 = customer_address_2;
    }

    public String getCustomer_landmark() {
        return customer_landmark;
    }

    public void setCustomer_landmark(String customer_landmark) {
        this.customer_landmark = customer_landmark;
    }

    public String getCustomer_country() {
        return customer_country;
    }

    public void setCustomer_country(String customer_country) {
        this.customer_country = customer_country;
    }

    public String getCustomer_state() {
        return customer_state;
    }

    public void setCustomer_state(String customer_state) {
        this.customer_state = customer_state;
    }

    public String getCustomer_city() {
        return customer_city;
    }

    public void setCustomer_city(String customer_city) {
        this.customer_city = customer_city;
    }

    public String getCustomer_pincode() {
        return customer_pincode;
    }

    public void setCustomer_pincode(String customer_pincode) {
        this.customer_pincode = customer_pincode;
    }

    public String getCustomer_gst_no() {
        return customer_gst_no;
    }

    public void setCustomer_gst_no(String customer_gst_no) {
        this.customer_gst_no = customer_gst_no;
    }

    public String getCustomer_pancard_no() {
        return customer_pancard_no;
    }

    public void setCustomer_pancard_no(String customer_pancard_no) {
        this.customer_pancard_no = customer_pancard_no;
    }

    public String getCustomer_zone_name() {
        return customer_zone_name;
    }

    public void setCustomer_zone_name(String customer_zone_name) {
        this.customer_zone_name = customer_zone_name;
    }

    public String getCustomer_area_name() {
        return customer_area_name;
    }

    public void setCustomer_area_name(String customer_area_name) {
        this.customer_area_name = customer_area_name;
    }


    public String getCustomer_zone() {
        return customer_zone;
    }

    public void setCustomer_zone(String customer_zone) {
        this.customer_zone = customer_zone;
    }


    public String getCustomer_district() {
        return customer_district;
    }

    public void setCustomer_district(String customer_district) {
        this.customer_district = customer_district;
    }


    public String getCustomer_country_name() {
        return customer_country_name;
    }

    public void setCustomer_country_name(String customer_country_name) {
        this.customer_country_name = customer_country_name;
    }

    public String getCustomer_state_name() {
        return customer_state_name;
    }

    public void setCustomer_state_name(String customer_state_name) {
        this.customer_state_name = customer_state_name;
    }

    public String getCustomer_city_name() {
        return customer_city_name;
    }

    public void setCustomer_city_name(String customer_city_name) {
        this.customer_city_name = customer_city_name;
    }

    public String getCustomer_district_name() {
        return customer_district_name;
    }

    public void setCustomer_district_name(String customer_district_name) {
        this.customer_district_name = customer_district_name;
    }


    public String getNarration() {
        return narration;
    }

    public void setNarration(String narration) {
        this.narration = narration;
    }

    public String getTerms_condition() {
        return terms_condition;
    }

    public void setTerms_condition(String terms_condition) {
        this.terms_condition = terms_condition;
    }

    public int getTotal_qty() {
        return total_qty;
    }

    public void setTotal_qty(int total_qty) {
        this.total_qty = total_qty;
    }

    public double getSubtotal() {
        return subtotal;
    }

    public void setSubtotal(double subtotal) {
        this.subtotal = subtotal;
    }

    public double getDiscount() {
        return discount;
    }

    public void setDiscount(double discount) {
        this.discount = discount;
    }

    public double getDiscount_amount() {
        return discount_amount;
    }

    public void setDiscount_amount(double discount_amount) {
        this.discount_amount = discount_amount;
    }


    public String getDiscount_type() {
        return discount_type;
    }

    public void setDiscount_type(String discount_type) {
        this.discount_type = discount_type;
    }

    String id;
    String quotation_request_no;
    String customer_id;
    String customer_name;
    String customer_email;
    String customer_phone_1;
    String customer_phone_2;
    String customer_address_1;
    String customer_address_2;
    String customer_landmark;
    String customer_country;
    String customer_state;
    String customer_city;
    String customer_zone;
    String customer_district;
    String customer_country_name;
    String customer_state_name;
    String customer_city_name;
    String customer_district_name;
    String customer_pincode;
    String customer_gst_no;
    String customer_pancard_no;
    String customer_zone_name;
    String customer_area_name;
    String grandtotal;
    String status;
    String status_name;
    String quotation_request_date_format;
    String narration;
    String discount_type;
    String terms_condition;
    int total_qty;
    double subtotal;
    double discount;
    double discount_amount;


}
