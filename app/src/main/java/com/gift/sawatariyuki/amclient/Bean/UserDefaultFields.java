package com.gift.sawatariyuki.amclient.Bean;

import com.gift.sawatariyuki.amclient.Utils.TimeZoneChanger;

import java.io.Serializable;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;


public class UserDefaultFields implements Serializable {
    private String name;
    private String email;
    private Boolean isActivated;
    private String activateCode;
    private Date date_joined;
    private Date last_joined;

    public UserDefaultFields(String name, String email, Boolean isActivated, String activateCod, String date_joined, String last_joined) {
        this.name = name;
        this.email = email;
        this.isActivated = isActivated;
        this.activateCode = activateCod;
        this.date_joined = TimeZoneChanger.StringUTCToDateLocal(date_joined);
        this.last_joined = TimeZoneChanger.StringUTCToDateLocal(last_joined);
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

    public Boolean getActivated() {
        return isActivated;
    }

    public void setActivated(Boolean activated) {
        isActivated = activated;
    }

    public String getActivateCode() {
        return activateCode;
    }

    public void setActivateCode(String activateCode) {
        this.activateCode = activateCode;
    }

    public void setDate_joined(Date date_joined) {
        this.date_joined = date_joined;
    }

    public Date getLast_joined() {
        return last_joined;
    }

    public void setLast_joined(Date last_joined) {
        this.last_joined = last_joined;
    }

    public Date getDate_joined() {
        return date_joined;
    }

    @Override
    public String toString() {
        return "UserDefaultFields{" +
                "name='" + name + '\'' +
                ", email='" + email + '\'' +
                ", isActivated=" + isActivated +
                ", activateCode='" + activateCode + '\'' +
                ", date_joined=" + date_joined +
                ", last_joined=" + last_joined +
                '}';
    }
}
