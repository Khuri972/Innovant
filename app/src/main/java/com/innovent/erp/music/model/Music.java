package com.innovent.erp.music.model;

public class Music {

    String id, name, desc, imageUrl, musicUrl, subCategoryId;

    public String getImageUrl() {
        return imageUrl;
    }

    public String getSubCategoryId() {
        return subCategoryId;
    }

    public void setSubCategoryId(String subCategoryId) {
        this.subCategoryId = subCategoryId;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getMusicUrl() {
        return musicUrl;

    }

    public Music(String id, String subCategoryId, String name, String imageUrl, String musicUrl) {
        this.id = id;
        this.subCategoryId = subCategoryId;
        this.name = name;
        this.imageUrl = imageUrl;
        this.musicUrl = musicUrl;
    }

    public void setMusicUrl(String musicUrl) {
        this.musicUrl = musicUrl;
    }


    public Music() {

    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }
}
