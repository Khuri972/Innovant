package com.innovent.erp.schedualReminder.model;

public class EventData {

    public String eventId, name, desc, location, startDate, endDate, starttime, endTime;
    public int event_type;

    public EventData() {

    }

    public EventData(String name, String desc, String location, String startDate, String endDate, String starttime, String endTime, int event_type) {
        this.name = name;
        this.desc = desc;
        this.location = location;
        this.startDate = startDate;
        this.endDate = endDate;
        this.starttime = starttime;
        this.endTime = endTime;
        this.event_type = event_type;
    }

    public String getEventId() {
        return eventId;
    }

    public void setEventId(String eventId) {
        this.eventId = eventId;
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

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getStartDate() {
        return startDate;
    }

    public void setStartDate(String startDate) {
        this.startDate = startDate;
    }

    public String getEndDate() {
        return endDate;
    }

    public void setEndDate(String endDate) {
        this.endDate = endDate;
    }

    public String getStarttime() {
        return starttime;
    }

    public void setStarttime(String starttime) {
        this.starttime = starttime;
    }

    public String getEndTime() {
        return endTime;
    }

    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }

    public int getEvent_type() {
        return event_type;
    }

    public void setEvent_type(int event_type) {
        this.event_type = event_type;
    }
}
