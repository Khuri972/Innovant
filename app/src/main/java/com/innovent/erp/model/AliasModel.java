package com.innovent.erp.model;

/**
 * Created by CRAFT BOX on 5/22/2018.
 */

public class AliasModel {

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

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getWorking_end_time() {
        return working_end_time;
    }

    public void setWorking_end_time(String working_end_time) {
        this.working_end_time = working_end_time;
    }

    public String getMax_working_time() {
        return max_working_time;
    }

    public void setMax_working_time(String max_working_time) {
        this.max_working_time = max_working_time;
    }

    public String getMin_working_time() {
        return min_working_time;
    }

    public void setMin_working_time(String min_working_time) {
        this.min_working_time = min_working_time;
    }

    public String getRights() {
        return rights;
    }

    public void setRights(String rights) {
        this.rights = rights;
    }

    public String getProject() {
        return project;
    }

    public void setProject(String project) {
        this.project = project;
    }

    String id;

    public String getAlias_name() {
        return alias_name;
    }

    public void setAlias_name(String alias_name) {
        this.alias_name = alias_name;
    }

    String alias_name;
    String name;
    String email;
    String mobile;
    String working_end_time;
    String max_working_time;
    String min_working_time;
    String rights;
    String project;

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    String type;

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    String currency;
}
