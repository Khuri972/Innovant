package com.desai.vatsal.mydynamiccalendar;

/**
 * Created by HCL on 02-10-2016.
 */
public class EventModel {

    private int event_type;
    private String eventId;
    private String strDate;
    private String strStartTime;
    private String strEndTime;


    private String strName;
    private int event_count;
    private int image = -1;

    public int getEvent_count() {
        return event_count;
    }

    public void setEvent_count(int event_count) {
        this.event_count = event_count;
    }

    public int getEvent_type() {
        return event_type;
    }

    public void setEvent_type(int event_type) {
        this.event_type = event_type;
    }

    public String getEventId() {
        return eventId;
    }

    public void setEventId(String eventId) {
        this.eventId = eventId;
    }

    public EventModel(String eventId, int event_type, String date, String name, int image) {
        this.strDate = date;
        this.strName = name;
        this.event_type = event_type;
        this.eventId = eventId;
        this.image = image;
    }

    public EventModel(String eventId, int event_type, String strDate, String strStartTime, String strEndTime, String strName) {
        this.strDate = strDate;
        this.strStartTime = strStartTime;
        this.strEndTime = strEndTime;
        this.strName = strName;
        this.event_type = event_type;
        this.eventId = eventId;
    }

    public EventModel(String eventId, String strDate, String strStartTime, String strEndTime, String strName, int image) {
        this.strDate = strDate;
        this.strStartTime = strStartTime;
        this.strEndTime = strEndTime;
        this.strName = strName;
        this.image = image;
        this.eventId = eventId;

    }

    public String getStrDate() {
        return strDate;
    }

    public void setStrDate(String strDate) {
        this.strDate = strDate;
    }

    public String getStrStartTime() {
        return strStartTime;
    }

    public void setStrStartTime(String strStartTime) {
        this.strStartTime = strStartTime;
    }

    public String getStrEndTime() {
        return strEndTime;
    }

    public void setStrEndTime(String strEndTime) {
        this.strEndTime = strEndTime;
    }

    public String getStrName() {
        return strName;
    }

    public void setStrName(String strName) {
        this.strName = strName;
    }

    public int getImage() {
        return image;
    }

    public void setImage(int image) {
        this.image = image;
    }
}
