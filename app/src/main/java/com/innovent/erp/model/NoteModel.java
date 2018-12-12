package com.innovent.erp.model;

import java.io.Serializable;

/**
 * Created by CRAFT BOX on 4/11/2018.
 */

public class NoteModel implements Serializable{

    String id;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    String title;
    String desc;

    public String getNotes_date() {
        return notes_date;
    }

    public void setNotes_date(String notes_date) {
        this.notes_date = notes_date;
    }

    String notes_date;
}
