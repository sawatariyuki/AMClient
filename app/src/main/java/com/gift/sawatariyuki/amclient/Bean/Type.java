package com.gift.sawatariyuki.amclient.Bean;

import java.io.Serializable;

public class Type implements Serializable {
    private int pk;
    private TypeFields fields;

    public Type(int pk, TypeFields fields) {
        this.pk = pk;
        this.fields = fields;
    }

    public int getPk() {
        return pk;
    }

    public void setPk(int pk) {
        this.pk = pk;
    }

    public TypeFields getFields() {
        return fields;
    }

    public void setFields(TypeFields fields) {
        this.fields = fields;
    }

    @Override
    public String toString() {
        return "Type{" +
                "pk=" + pk +
                ", fields=" + fields +
                '}';
    }
}
