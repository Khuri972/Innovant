package com.innovent.erp.model;

/**
 * Created by CRAFT BOX on 10/24/2017.
 */

public class ChatModel {

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getMessage_type() {
        return message_type;
    }

    public void setMessage_type(String message_type) {
        this.message_type = message_type;
    }

    public String getMedia_title() {
        return media_title;
    }

    public void setMedia_title(String media_title) {
        this.media_title = media_title;
    }

    public String getMedia_extension() {
        return media_extension;
    }

    public void setMedia_extension(String media_extension) {
        this.media_extension = media_extension;
    }

    public String getMedia_url() {
        return media_url;
    }

    public void setMedia_url(String media_url) {
        this.media_url = media_url;
    }

    public String getDate_time() {
        return date_time;
    }

    public void setDate_time(String date_time) {
        this.date_time = date_time;
    }


    String id;
    String message;
    String message_type;
    String media_title;
    String media_extension;
    String media_url;
    String date_time;

    public void setIsOwnMessage(String isOwnMessage) {
        this.isOwnMessage = isOwnMessage;
    }

    public String getIsOwnMessage() {
        return isOwnMessage;
    }

    String isOwnMessage;
}

