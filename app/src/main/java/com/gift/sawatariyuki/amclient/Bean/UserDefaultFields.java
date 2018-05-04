package com.gift.sawatariyuki.amclient.Bean;

import java.io.Serializable;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;


public class UserDefaultFields implements Serializable {
    private String name;
    private String pw;
    private String email;
    private Boolean isActivated;
    private String activateCode;
    private Date date_joined;
    private Date last_joined;

    public UserDefaultFields(String name, String pw, String email, Boolean isActivated, String activateCode, String date_joined, String last_joined) {
        this.name = name;
        this.pw = pw;
        this.email = email;
        this.isActivated = isActivated;
        this.activateCode = activateCode;

        try {
            this.date_joined = SimpleDateFormat.getDateInstance().parse(date_joined);
            this.last_joined = SimpleDateFormat.getDateInstance().parse(last_joined);
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


    public void setDate_joined(Date date_joined) {
        this.date_joined = date_joined;
    }

    public Date getLast_joined() {
        return last_joined;
    }

    public void setLast_joined(Date last_joined) {
        this.last_joined = last_joined;
    }

    @Override
    public String toString() {
        return "UserDefaultFields{" +
                "name='" + name + '\'' +
                ", pw='" + pw + '\'' +
                ", email='" + email + '\'' +
                ", isActivated=" + isActivated +
                ", activateCode='" + activateCode + '\'' +
                ", date_joined=" + date_joined +
                ", last_joined=" + last_joined +
                '}';
    }
}
