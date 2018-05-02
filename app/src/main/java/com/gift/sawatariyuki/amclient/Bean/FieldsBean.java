package com.gift.sawatariyuki.amclient.Bean;

import java.io.Serializable;

public class FieldsBean implements Serializable {
    private String name;
    private String pw;
    private String email;
    private Boolean isActivated;
    private String activateCode;
    private String date_joined;
    private String last_joined;

    public FieldsBean(String name, String pw, String email, Boolean isActivated, String activateCode, String date_joined, String last_joined) {
        this.name = name;
        this.pw = pw;
        this.email = email;
        this.isActivated = isActivated;
        this.activateCode = activateCode;
        this.date_joined = date_joined;
        this.last_joined = last_joined;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPw() {
        return pw;
    }

    public void setPw(String pw) {
        this.pw = pw;
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

    public String getDate_joined() {
        return date_joined;
    }

    public void setDate_joined(String date_joined) {
        this.date_joined = date_joined;
    }

    public String getLast_joined() {
        return last_joined;
    }

    public void setLast_joined(String last_joined) {
        this.last_joined = last_joined;
    }

    @Override
    public String toString() {
        return "FieldsBean{" +
                "name='" + name + '\'' +
                ", pw='" + pw + '\'' +
                ", email='" + email + '\'' +
                ", isActivated=" + isActivated +
                ", activateCode='" + activateCode + '\'' +
                ", date_joined='" + date_joined + '\'' +
                ", last_joined='" + last_joined + '\'' +
                '}';
    }
}
