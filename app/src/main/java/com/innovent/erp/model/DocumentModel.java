package com.innovent.erp.model;

/**
 * Created by CRAFT BOX on 2/27/2018.
 */

public class DocumentModel {

    String id;
    String type;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    String name;

    public String getVisibility() {
        return visibility;
    }

    public void setVisibility(String visibility) {
        this.visibility = visibility;
    }

    public String getResource_media_url() {
        return resource_media_url;
    }

    public void setResource_media_url(String resource_media_url) {
        this.resource_media_url = resource_media_url;
    }

    String visibility,resource_media_url;
}
