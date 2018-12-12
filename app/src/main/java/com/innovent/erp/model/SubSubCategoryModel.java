package com.innovent.erp.model;

import java.io.Serializable;

/**
 * Created by CRAFT BOX on 11/23/2016.
 */

public class SubSubCategoryModel implements Serializable{

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getCid() {
        return cid;
    }

    public void setCid(int cid) {
        this.cid = cid;
    }

    public int getSid() {
        return sid;
    }

    public void setSid(int sid) {
        this.sid = sid;
    }

    public int getDisplay_order() {
        return display_order;
    }

    public void setDisplay_order(int display_order) {
        this.display_order = display_order;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSlug() {
        return slug;
    }

    public void setSlug(String slug) {
        this.slug = slug;
    }

    public String getImage_path() {
        return image_path;
    }

    public void setImage_path(String image_path) {
        this.image_path = image_path;
    }

    public String getBanner_image_path() {
        return banner_image_path;
    }

    public void setBanner_image_path(String banner_image_path) {
        this.banner_image_path = banner_image_path;
    }

    public String getAttr() {
        return attr;
    }

    public void setAttr(String attr) {
        this.attr = attr;
    }

    public String getDescr() {
        return descr;
    }

    public void setDescr(String descr) {
        this.descr = descr;
    }

    int  id,cid,sid,display_order;
    String name,slug,image_path,banner_image_path,attr,descr;

    public SubSubCategoryModel()
    {

    }
}
