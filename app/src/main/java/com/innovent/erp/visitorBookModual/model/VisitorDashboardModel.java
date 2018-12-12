package com.innovent.erp.visitorBookModual.model;

/**
 * Created by CRAFT BOX on 4/24/2018.
 */

public class VisitorDashboardModel {

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

    public int getOnline_ofline_state() {
        return online_ofline_state;
    }

    public void setOnline_ofline_state(int online_ofline_state) {
        this.online_ofline_state = online_ofline_state;
    }

    int online_ofline_state;

    public int getImage_path() {
        return image_path;
    }

    public void setImage_path(int image_path) {
        this.image_path = image_path;
    }

    int image_path;

}
