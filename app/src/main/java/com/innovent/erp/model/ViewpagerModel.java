package com.innovent.erp.model;

import java.io.Serializable;

/**
 * Created by CRAFT BOX on 8/3/2016.
 */
public class ViewpagerModel implements Serializable{


    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getImage_path() {
        return image_path;
    }

    public void setImage_path(String image_path) {
        this.image_path = image_path;
    }

    String title;
    String image_path;

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getPdf_file() {
        return pdf_file;
    }

    public void setPdf_file(String pdf_file) {
        this.pdf_file = pdf_file;
    }

    String description;
    String pdf_file;

    public ViewpagerModel()
    {

    }
}
