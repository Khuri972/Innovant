package com.innovent.erp.model;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by CRAFT BOX on 3/16/2018.
 */

public class TaskModel implements Serializable {

    String id;

    public String getTask_assigned_by_name() {
        return task_assigned_by_name;
    }

    public void setTask_assigned_by_name(String task_assigned_by_name) {
        this.task_assigned_by_name = task_assigned_by_name;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTask_no() {
        return task_no;
    }

    public void setTask_no(String task_no) {
        this.task_no = task_no;
    }

    public String getTask_name() {
        return task_name;
    }

    public void setTask_name(String task_name) {
        this.task_name = task_name;
    }

    public String getTask_deadline() {
        return task_deadline;
    }

    public void setTask_deadline(String task_deadline) {
        this.task_deadline = task_deadline;
    }

    public String getTask_description() {
        return task_description;
    }

    public void setTask_description(String task_description) {
        this.task_description = task_description;
    }

    public String getTask_type() {
        return task_type;
    }

    public void setTask_type(String task_type) {
        this.task_type = task_type;
    }

    public String getTask_color_tag() {
        return task_color_tag;
    }

    public void setTask_color_tag(String task_color_tag) {
        this.task_color_tag = task_color_tag;
    }

    public String getTask_assigned_by() {
        return task_assigned_by;
    }

    public void setTask_assigned_by(String task_assigned_by) {
        this.task_assigned_by = task_assigned_by;
    }

    String task_no;
    String task_name;
    String task_deadline;
    String task_description;
    String task_type;
    String task_color_tag;
    String task_assigned_by;
    String task_assigned_by_name;

    public String getTask_color_tag_slug() {
        return task_color_tag_slug;
    }

    public void setTask_color_tag_slug(String task_color_tag_slug) {
        this.task_color_tag_slug = task_color_tag_slug;
    }

    String task_color_tag_slug;

    public String getTask_priority() {
        return task_priority;
    }

    public void setTask_priority(String task_priority) {
        this.task_priority = task_priority;
    }

    public String getTask_create_date() {
        return task_create_date;
    }

    public void setTask_create_date(String task_create_date) {
        this.task_create_date = task_create_date;
    }

    String task_priority;
    String task_create_date;
    String task_assigned_to;

    public String getTask_assigned_to_name() {
        return task_assigned_to_name;
    }

    public void setTask_assigned_to_name(String task_assigned_to_name) {
        this.task_assigned_to_name = task_assigned_to_name;
    }

    public String getTask_assigned_to() {
        return task_assigned_to;
    }

    public void setTask_assigned_to(String task_assigned_to) {
        this.task_assigned_to = task_assigned_to;
    }

    String task_assigned_to_name;

    public ArrayList<TaskNoteModel> getNoteModels() {
        return noteModels;
    }

    public void setNoteModels(ArrayList<TaskNoteModel> noteModels) {
        this.noteModels = noteModels;
    }

    ArrayList<TaskNoteModel> noteModels = new ArrayList<>();

    public ArrayList<TaskAttachmentModel> getAttachmentModels() {
        return attachmentModels;
    }

    public void setAttachmentModels(ArrayList<TaskAttachmentModel> attachmentModels) {
        this.attachmentModels = attachmentModels;
    }

    ArrayList<TaskAttachmentModel> attachmentModels = new ArrayList<>();

    public String getAttachment() {
        return attachment;
    }

    public void setAttachment(String attachment) {
        this.attachment = attachment;
    }

    String attachment;

    public String getIsActive() {
        return isActive;
    }

    public void setIsActive(String isActive) {
        this.isActive = isActive;
    }

    String isActive;


    /* pipeline code implement 21-07-208 */

    public String getPipelineType() {
        return pipelineType;
    }

    public void setPipelineType(String pipelineType) {
        this.pipelineType = pipelineType;
    }

    String pipelineType;

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
    String narration;
    String terms_condition;

    public String getDealer_id() {
        return dealer_id;
    }

    public void setDealer_id(String dealer_id) {
        this.dealer_id = dealer_id;
    }

    String dealer_id;

    public String getReplace_tach_remarks() {
        return replace_tach_remarks;
    }

    public void setReplace_tach_remarks(String replace_tach_remarks) {
        this.replace_tach_remarks = replace_tach_remarks;
    }

    public String getReplace_tach_created_date_format() {
        return replace_tach_created_date_format;
    }

    public void setReplace_tach_created_date_format(String replace_tach_created_date_format) {
        this.replace_tach_created_date_format = replace_tach_created_date_format;
    }

    String replace_tach_remarks;
    String replace_tach_created_date_format;
}
