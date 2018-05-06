package com.gift.sawatariyuki.amclient.Bean;

import java.io.Serializable;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class EventFields implements Serializable {
    private String title;
    private String description;
    private int eventType;
    private Date ctime;
    private int userLevel;
    private Date userStartTime;
    private Date userEndTime;
    private int length;
    private Date sysStartTime;
    private Date sysEndTime;
    private int sysLevel;
    private int state;

    public EventFields(String title, String description, int eventType, String ctime, int userLevel, String userStartTime, String userEndTime, int length, String sysStartTime, String sysEndTime, int sysLevel, int state) {
        this.title = title;
        this.description = description;
        this.eventType = eventType;
        this.userLevel = userLevel;
        this.length = length;
        this.sysLevel = sysLevel;
        this.state = state;

        try {
            this.ctime = SimpleDateFormat.getDateTimeInstance().parse(ctime);
            this.userStartTime = SimpleDateFormat.getDateTimeInstance().parse(userStartTime);
            this.userEndTime = SimpleDateFormat.getDateTimeInstance().parse(userEndTime);
            this.sysStartTime = SimpleDateFormat.getDateTimeInstance().parse(sysStartTime);
            this.sysEndTime = SimpleDateFormat.getDateTimeInstance().parse(sysEndTime);
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getEventType() {
        return eventType;
    }

    public void setEventType(int eventType) {
        this.eventType = eventType;
    }

    public int getUserLevel() {
        return userLevel;
    }

    public void setUserLevel(int userLevel) {
        this.userLevel = userLevel;
    }

    public int getLength() {
        return length;
    }

    public void setLength(int length) {
        this.length = length;
    }

    public int getSysLevel() {
        return sysLevel;
    }

    public void setSysLevel(int sysLevel) {
        this.sysLevel = sysLevel;
    }

    public int getState() {
        return state;
    }

    public void setState(int state) {
        this.state = state;
    }

    public Date getCtime() {
        return ctime;
    }

    public void setCtime(Date ctime) {
        this.ctime = ctime;
    }

    public Date getUserStartTime() {
        return userStartTime;
    }

    public void setUserStartTime(Date userStartTime) {
        this.userStartTime = userStartTime;
    }

    public Date getUserEndTime() {
        return userEndTime;
    }

    public void setUserEndTime(Date userEndTime) {
        this.userEndTime = userEndTime;
    }

    public Date getSysStartTime() {
        return sysStartTime;
    }

    public void setSysStartTime(Date sysStartTime) {
        this.sysStartTime = sysStartTime;
    }

    public Date getSysEndTime() {
        return sysEndTime;
    }

    public void setSysEndTime(Date sysEndTime) {
        this.sysEndTime = sysEndTime;
    }

    @Override
    public String toString() {
        return "EventFields{" +
                "title='" + title + '\'' +
                ", description='" + description + '\'' +
                ", eventType=" + eventType +
                ", ctime=" + ctime +
                ", userLevel=" + userLevel +
                ", userStartTime=" + userStartTime +
                ", userEndTime=" + userEndTime +
                ", length=" + length +
                ", sysStartTime=" + sysStartTime +
                ", sysEndTime=" + sysEndTime +
                ", sysLevel=" + sysLevel +
                ", state=" + state +
                '}';
    }
}
