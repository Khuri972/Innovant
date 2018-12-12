package com.innovent.erp.model;

import java.io.Serializable;

/**
 * Created by CRAFT BOX on 10/24/2017.
 */

public class UserModel implements Serializable{

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

    public String getImage_path() {
        return image_path;
    }

    public void setImage_path(String image_path) {
        this.image_path = image_path;
    }

    String id,name,image_path;
}
