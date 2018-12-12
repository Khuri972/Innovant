package com.innovent.erp.employeeManagement.model;

import java.io.Serializable;

/**
 * Created by CRAFT BOX on 5/3/2018.
 */

public class DailyFileAttachModel implements Serializable{

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getFile_name() {
        return file_name;
    }

    public void setFile_name(String file_name) {
        this.file_name = file_name;
    }

    public String getFile_path() {
        return file_path;
    }

    public void setFile_path(String file_path) {
        this.file_path = file_path;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    String id,file_name,file_path,date;
}
