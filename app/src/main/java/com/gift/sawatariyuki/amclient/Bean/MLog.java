package com.gift.sawatariyuki.amclient.Bean;

import java.io.Serializable;

public class MLog implements Serializable {
    private int pk;
    private LogFields fields;

    public MLog(int pk, LogFields fields) {
        this.pk = pk;
        this.fields = fields;
    }

    public int getPk() {
        return pk;
    }

    public void setPk(int pk) {
        this.pk = pk;
    }

    public LogFields getFields() {
        return fields;
    }

    public void setFields(LogFields fields) {
        this.fields = fields;
    }

    @Override
    public String toString() {
        return "MLog{" +
                "pk=" + pk +
                ", fields=" + fields +
                '}';
    }
}
