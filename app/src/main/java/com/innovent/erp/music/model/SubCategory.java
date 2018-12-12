package com.innovent.erp.music.model;

public class SubCategory {
    String id, cid, name, imageUrl;

    public String getImageUrl() {
        return imageUrl;
    }

    public SubCategory(String id, String cid, String name, String imageUrl) {
        this.id = id;
        this.cid = cid;
        this.name = name;
        this.imageUrl = imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getCid() {
        return cid;
    }


    public void setCid(String cid) {
        this.cid = cid;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public SubCategory() {

    }
}
