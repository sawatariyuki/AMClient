package com.gift.sawatariyuki.amclient.Bean;

import java.io.Serializable;

public class UserDetail implements Serializable {
    private int pk;
    private UserDetailFields fields;

    public UserDetail(int pk, UserDetailFields fields) {
        this.pk = pk;
        this.fields = fields;
    }

    public int getPk() {
        return pk;
    }

    public void setPk(int pk) {
        this.pk = pk;
    }

    public UserDetailFields getFields() {
        return fields;
    }

    public void setFields(UserDetailFields fields) {
        this.fields = fields;
    }

    @Override
    public String toString() {
        return "UserDetail{" +
                "pk=" + pk +
                ", fields=" + fields +
                '}';
    }
}
