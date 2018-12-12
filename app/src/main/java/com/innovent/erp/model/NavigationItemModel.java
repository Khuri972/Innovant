package com.innovent.erp.model;

/**
 * Created by CRAFT BOX on 4/30/2018.
 */

public class NavigationItemModel {

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

    String id,name;

    public Class getTarget() {
        return target;
    }

    public void setTarget(Class target) {
        this.target = target;
    }

    Class target;

    public int getImage_path() {
        return image_path;
    }

    public void setImage_path(int image_path) {
        this.image_path = image_path;
    }

    int image_path;

    public int getColor() {
        return color;
    }

    public void setColor(int color) {
        this.color = color;
    }

    int color;

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }



    int type;
}
