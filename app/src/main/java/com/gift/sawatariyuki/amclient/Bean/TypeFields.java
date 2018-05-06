package com.gift.sawatariyuki.amclient.Bean;

import java.io.Serializable;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class TypeFields implements Serializable {
    private String name;
    private String description;
    private int userTimes;
    private int emergencyLevel;
    private Date ctime;
    private Date last_used;

    public TypeFields(String name, String description, int userTimes, int emergencyLevel, String ctime, String last_used) {
        this.name = name;
        this.description = description;
        this.userTimes = userTimes;
        this.emergencyLevel = emergencyLevel;

        try {
            this.ctime = SimpleDateFormat.getDateTimeInstance().parse(ctime);
            this.last_used = SimpleDateFormat.getDateTimeInstance().parse(last_used);
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getUserTimes() {
        return userTimes;
    }

    public void setUserTimes(int userTimes) {
        this.userTimes = userTimes;
    }

    public int getEmergencyLevel() {
        return emergencyLevel;
    }

    public void setEmergencyLevel(int emergencyLevel) {
        this.emergencyLevel = emergencyLevel;
    }

    public Date getCtime() {
        return ctime;
    }

    public void setCtime(Date ctime) {
        this.ctime = ctime;
    }

    public Date getLast_used() {
        return last_used;
    }

    public void setLast_used(Date last_used) {
        this.last_used = last_used;
    }

    @Override
    public String toString() {
        return "TypeFields{" +
                "name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", userTimes=" + userTimes +
                ", emergencyLevel=" + emergencyLevel +
                ", ctime=" + ctime +
                ", last_used=" + last_used +
                '}';
    }
}
