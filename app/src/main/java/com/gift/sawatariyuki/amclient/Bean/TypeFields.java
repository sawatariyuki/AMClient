package com.gift.sawatariyuki.amclient.Bean;

import com.gift.sawatariyuki.amclient.Utils.TimeZoneChanger;

import java.io.Serializable;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class TypeFields implements Serializable {
    private String name;
    private String description;
    private int useTimes;
    private Date ctime;
    private Date last_used;

    public TypeFields(String name, String description, int useTimes, String ctime, String last_used) {
        this.name = name;
        this.description = description;
        this.useTimes = useTimes;

        this.ctime = TimeZoneChanger.StringUTCToDateLocal(ctime);
        this.last_used = TimeZoneChanger.StringUTCToDateLocal(last_used);

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

    public int getUseTimes() {
        return useTimes;
    }

    public void setUseTimes(int useTimes) {
        this.useTimes = useTimes;
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
                ", useTimes=" + useTimes +
                ", ctime=" + ctime +
                ", last_used=" + last_used +
                '}';
    }
}
