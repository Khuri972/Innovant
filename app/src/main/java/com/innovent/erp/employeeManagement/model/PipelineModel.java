package com.innovent.erp.employeeManagement.model;

import com.innovent.erp.model.TaskAttachmentModel;
import com.innovent.erp.model.TaskNoteModel;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by CRAFT BOX on 3/16/2018.
 */

public class PipelineModel implements Serializable{

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

    ArrayList<TaskNoteModel> noteModels=new ArrayList<>();

    public ArrayList<TaskAttachmentModel> getAttachmentModels() {
        return attachmentModels;
    }

    public void setAttachmentModels(ArrayList<TaskAttachmentModel> attachmentModels) {
        this.attachmentModels = attachmentModels;
    }

    ArrayList<TaskAttachmentModel> attachmentModels=new ArrayList<>();

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
}
