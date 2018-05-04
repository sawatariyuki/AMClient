package com.gift.sawatariyuki.amclient.Bean;

import java.io.Serializable;

public class Event implements Serializable{
    private int pk;
    private EventFields fields;

    public Event(int pk, EventFields fields) {
        this.pk = pk;
        this.fields = fields;
    }

    public int getPk() {
        return pk;
    }

    public void setPk(int pk) {
        this.pk = pk;
    }

    public EventFields getFields() {
        return fields;
    }

    public void setFields(EventFields fields) {
        this.fields = fields;
    }

    @Override
    public String toString() {
        return "Event{" +
                "pk=" + pk +
                ", fields=" + fields +
                '}';
    }
}
