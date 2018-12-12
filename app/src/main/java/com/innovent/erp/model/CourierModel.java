package com.innovent.erp.model;

import java.io.Serializable;

/**
 * Created by CRAFT BOX on 3/1/2018.
 */

public class CourierModel implements Serializable{

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getCourier_no() {
        return courier_no;
    }

    public void setCourier_no(String courier_no) {
        this.courier_no = courier_no;
    }

    public String getShipping_deliverd_date() {
        return shipping_deliverd_date;
    }

    public void setShipping_deliverd_date(String shipping_deliverd_date) {
        this.shipping_deliverd_date = shipping_deliverd_date;
    }

    public String getSender_company_id() {
        return sender_company_id;
    }

    public void setSender_company_id(String sender_company_id) {
        this.sender_company_id = sender_company_id;
    }

    public String getSender_person_id() {
        return sender_person_id;
    }

    public void setSender_person_id(String sender_person_id) {
        this.sender_person_id = sender_person_id;
    }

    public String getLabel_id() {
        return label_id;
    }

    public void setLabel_id(String label_id) {
        this.label_id = label_id;
    }

    public String getSender_company_name() {
        return sender_company_name;
    }

    public void setSender_company_name(String sender_company_name) {
        this.sender_company_name = sender_company_name;
    }

    public String getSender_person_name() {
        return sender_person_name;
    }

    public void setSender_person_name(String sender_person_name) {
        this.sender_person_name = sender_person_name;
    }

    public String getParcel_type_slug() {
        return parcel_type_slug;
    }

    public void setParcel_type_slug(String parcel_type_slug) {
        this.parcel_type_slug = parcel_type_slug;
    }

    public String getLabel_slug() {
        return label_slug;
    }

    public void setLabel_slug(String label_slug) {
        this.label_slug = label_slug;
    }

    public String getReceiver_company_name() {
        return receiver_company_name;
    }

    public void setReceiver_company_name(String receiver_company_name) {
        this.receiver_company_name = receiver_company_name;
    }

    public String getReceiver_person_name() {
        return receiver_person_name;
    }

    public void setReceiver_person_name(String receiver_person_name) {
        this.receiver_person_name = receiver_person_name;
    }

    public String getReceiver_address() {
        return receiver_address;
    }

    public void setReceiver_address(String receiver_address) {
        this.receiver_address = receiver_address;
    }

    public String getReceiver_city() {
        return receiver_city;
    }

    public void setReceiver_city(String receiver_city) {
        this.receiver_city = receiver_city;
    }

    public String getReceiver_state() {
        return receiver_state;
    }

    public void setReceiver_state(String receiver_state) {
        this.receiver_state = receiver_state;
    }

    public String getReceiver_country() {
        return receiver_country;
    }

    public void setReceiver_country(String receiver_country) {
        this.receiver_country = receiver_country;
    }

    public String getReceiver_pincode() {
        return receiver_pincode;
    }

    public void setReceiver_pincode(String receiver_pincode) {
        this.receiver_pincode = receiver_pincode;
    }

    public String getParcel_description() {
        return parcel_description;
    }

    public void setParcel_description(String parcel_description) {
        this.parcel_description = parcel_description;
    }

    public String getParcel_weight() {
        return parcel_weight;
    }

    public void setParcel_weight(String parcel_weight) {
        this.parcel_weight = parcel_weight;
    }

    public String getParcel_cost() {
        return parcel_cost;
    }

    public void setParcel_cost(String parcel_cost) {
        this.parcel_cost = parcel_cost;
    }

    public String getShipping_courier_name() {
        return shipping_courier_name;
    }

    public void setShipping_courier_name(String shipping_courier_name) {
        this.shipping_courier_name = shipping_courier_name;
    }

    public String getShipping_pickup_person() {
        return shipping_pickup_person;
    }

    public void setShipping_pickup_person(String shipping_pickup_person) {
        this.shipping_pickup_person = shipping_pickup_person;
    }

    public String getTracking_no() {
        return tracking_no;
    }

    public void setTracking_no(String tracking_no) {
        this.tracking_no = tracking_no;
    }

    String id;
    String courier_no;

    public String getShipping_date() {
        return shipping_date;
    }

    public void setShipping_date(String shipping_date) {
        this.shipping_date = shipping_date;
    }

    String shipping_date;
    String shipping_deliverd_date;
    String sender_company_id;
    String sender_person_id;
    String label_id;
    String sender_company_name;
    String sender_person_name;
    String parcel_type_slug;
    String label_slug;
    String receiver_company_name;
    String receiver_person_name;
    String receiver_contact_no;
    String receiver_address;

    public String getReceiver_area() {
        return receiver_area;
    }

    public void setReceiver_area(String receiver_area) {
        this.receiver_area = receiver_area;
    }

    public String getReceiver_district() {
        return receiver_district;
    }

    public void setReceiver_district(String receiver_district) {
        this.receiver_district = receiver_district;
    }

    public String getReceiver_zone() {
        return receiver_zone;
    }

    public void setReceiver_zone(String receiver_zone) {
        this.receiver_zone = receiver_zone;
    }

    public String getReceiver_city_name() {
        return receiver_city_name;
    }

    public void setReceiver_city_name(String receiver_city_name) {
        this.receiver_city_name = receiver_city_name;
    }

    public String getReceiver_district_name() {
        return receiver_district_name;
    }

    public void setReceiver_district_name(String receiver_district_name) {
        this.receiver_district_name = receiver_district_name;
    }

    public String getReceiver_state_name() {
        return receiver_state_name;
    }

    public void setReceiver_state_name(String receiver_state_name) {
        this.receiver_state_name = receiver_state_name;
    }

    public String getReceiver_country_name() {
        return receiver_country_name;
    }

    public void setReceiver_country_name(String receiver_country_name) {
        this.receiver_country_name = receiver_country_name;
    }

    public String getReceiver_zone_name() {
        return receiver_zone_name;
    }

    public void setReceiver_zone_name(String receiver_zone_name) {
        this.receiver_zone_name = receiver_zone_name;
    }

    public String getSender_area() {
        return sender_area;
    }

    public void setSender_area(String sender_area) {
        this.sender_area = sender_area;
    }

    public String getSender_district() {
        return sender_district;
    }

    public void setSender_district(String sender_district) {
        this.sender_district = sender_district;
    }

    public String getSender_zone() {
        return sender_zone;
    }

    public void setSender_zone(String sender_zone) {
        this.sender_zone = sender_zone;
    }

    public String getSender_city_name() {
        return sender_city_name;
    }

    public void setSender_city_name(String sender_city_name) {
        this.sender_city_name = sender_city_name;
    }

    public String getSender_district_name() {
        return sender_district_name;
    }

    public void setSender_district_name(String sender_district_name) {
        this.sender_district_name = sender_district_name;
    }

    public String getSender_state_name() {
        return sender_state_name;
    }

    public void setSender_state_name(String sender_state_name) {
        this.sender_state_name = sender_state_name;
    }

    public String getSender_country_name() {
        return sender_country_name;
    }

    public void setSender_country_name(String sender_country_name) {
        this.sender_country_name = sender_country_name;
    }

    public String getSender_zone_name() {
        return sender_zone_name;
    }

    public void setSender_zone_name(String sender_zone_name) {
        this.sender_zone_name = sender_zone_name;
    }

    String receiver_area;
    String receiver_city;
    String receiver_district;
    String receiver_state;
    String receiver_country;
    String receiver_zone;
    String receiver_city_name;
    String receiver_district_name;
    String receiver_state_name;
    String receiver_country_name;
    String receiver_zone_name;
    String receiver_pincode;

    String parcel_description;
    String parcel_weight;
    String parcel_cost;
    String shipping_courier_name;
    String shipping_pickup_person;
    String tracking_no;

    public String getDelivery_status() {
        return delivery_status;
    }

    public void setDelivery_status(String delivery_status) {
        this.delivery_status = delivery_status;
    }

    String delivery_status;
    public String getReceiver_contact_no() {
        return receiver_contact_no;
    }

    public void setReceiver_contact_no(String receiver_contact_no) {
        this.receiver_contact_no = receiver_contact_no;
    }



    public String getAssigned_person_mobile() {
        return assigned_person_mobile;
    }

    public void setAssigned_person_mobile(String assigned_person_mobile) {
        this.assigned_person_mobile = assigned_person_mobile;
    }

    public String getAssigned_person_name() {
        return assigned_person_name;
    }

    public void setAssigned_person_name(String assigned_person_name) {
        this.assigned_person_name = assigned_person_name;
    }

    public String getKg_unit() {
        return kg_unit;
    }

    public void setKg_unit(String kg_unit) {
        this.kg_unit = kg_unit;
    }

    public String getKg_value() {
        return kg_value;
    }

    public void setKg_value(String kg_value) {
        this.kg_value = kg_value;
    }

    public String getGm_unit() {
        return gm_unit;
    }

    public void setGm_unit(String gm_unit) {
        this.gm_unit = gm_unit;
    }

    public String getGm_value() {
        return gm_value;
    }

    public void setGm_value(String gm_value) {
        this.gm_value = gm_value;
    }

    public String getCourier_type() {
        return courier_type;
    }

    public void setCourier_type(String courier_type) {
        this.courier_type = courier_type;
    }

    public String getSender_contact_no() {
        return sender_contact_no;
    }

    public void setSender_contact_no(String sender_contact_no) {
        this.sender_contact_no = sender_contact_no;
    }

    public String getSender_address() {
        return sender_address;
    }

    public void setSender_address(String sender_address) {
        this.sender_address = sender_address;
    }

    public String getSender_city() {
        return sender_city;
    }

    public void setSender_city(String sender_city) {
        this.sender_city = sender_city;
    }

    public String getSender_state() {
        return sender_state;
    }

    public void setSender_state(String sender_state) {
        this.sender_state = sender_state;
    }

    public String getSender_country() {
        return sender_country;
    }

    public void setSender_country(String sender_country) {
        this.sender_country = sender_country;
    }

    public String getSender_pincode() {
        return sender_pincode;
    }

    public void setSender_pincode(String sender_pincode) {
        this.sender_pincode = sender_pincode;
    }

    String sender_contact_no;
    String sender_address;

    String sender_area;
    String sender_city;
    String sender_district;
    String sender_state;
    String sender_country;
    String sender_zone;

    String sender_city_name;
    String sender_district_name;
    String sender_state_name;
    String sender_country_name;
    String sender_zone_name;
    String sender_pincode;

    String assigned_person_mobile;
    String assigned_person_name;
    String kg_unit,kg_value,gm_unit,gm_value,courier_type;
}
