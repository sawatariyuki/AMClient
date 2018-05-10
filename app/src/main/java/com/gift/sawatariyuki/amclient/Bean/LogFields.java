package com.gift.sawatariyuki.amclient.Bean;

import com.gift.sawatariyuki.amclient.Utils.TimeZoneChanger;

import java.io.Serializable;
import java.util.Date;

public class LogFields implements Serializable {
    private String location;
    private String ip;
    private Date ctime;
    private String content;

    public LogFields(String location, String ip, String ctime, String content) {
        this.location = location;
        this.ip = ip;
        this.ctime = TimeZoneChanger.StringUTCToDateLocal(ctime);
        this.content = content;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public Date getCtime() {
        return ctime;
    }

    public void setCtime(Date ctime) {
        this.ctime = ctime;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    @Override
    public String toString() {
        return "LogFields{" +
                "location='" + location + '\'' +
                ", ip='" + ip + '\'' +
                ", ctime=" + ctime +
                ", content='" + content + '\'' +
                '}';
    }
}
