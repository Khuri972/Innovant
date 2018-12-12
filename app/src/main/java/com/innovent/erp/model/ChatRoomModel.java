package com.innovent.erp.model;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by CRAFT BOX on 10/24/2017.
 */

public class ChatRoomModel implements Serializable{

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public ArrayList<UserModel> getParticipant() {
        return participant;
    }

    public void setParticipant(ArrayList<UserModel> participant) {
        this.participant = participant;
    }

    String id,title;

    public int getNew_message_count() {
        return new_message_count;
    }

    public void setNew_message_count(int new_message_count) {
        this.new_message_count = new_message_count;
    }

    int new_message_count;

    ArrayList<UserModel> participant = new ArrayList<>();
}
