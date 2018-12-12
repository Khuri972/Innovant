package com.innovent.erp.employeeManagement.model;

import java.io.Serializable;

/**
 * Created by CRAFT BOX on 5/3/2018.
 */

public class DailyNoteModel implements Serializable{

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    String id,note,date;
}
