package com.gift.sawatariyuki.amclient.Bean;

import java.io.Serializable;

public class UserDefault implements Serializable {
    private int pk;
    private UserDefaultFields fields;

    public UserDefault(int pk, UserDefaultFields fields) {
        this.pk = pk;
        this.fields = fields;
    }

    public int getPk() {
        return pk;
    }

    public void setPk(int pk) {
        this.pk = pk;
    }

    public UserDefaultFields getFields() {
        return fields;
    }

    public void setFields(UserDefaultFields fields) {
        this.fields = fields;
    }

    @Override
    public String toString() {
        return "UserDefault{" +
                "pk=" + pk +
                ", fields=" + fields +
                '}';
    }

}
