package com.gift.sawatariyuki.amclient.Bean;

import java.io.Serializable;

public class UserDefault implements Serializable {
    /**
     * data: {"model": "user.userdefault", "pk": 10, "fields":
     *             {"name": "yuki",
     *             "pw": "md5$2MqIU9IQcVvi$2370dcafcaeff5faa7f32089b2f0662f",
     *             "email": "cslzf96226@sina.com",
     *             "isActivated": true,
     *             "activateCode": "SBPLRG",
     *             "date_joined": "2018-04-20T16:23:01.195Z",
     *             "last_joined": "2018-04-20T16:23:01.195Z"}
     *      }
     */

    private int pk;
    private FieldsBean fields;

    public UserDefault(int pk, FieldsBean fields) {
        this.pk = pk;
        this.fields = fields;
    }

    public int getPk() {
        return pk;
    }

    public void setPk(int pk) {
        this.pk = pk;
    }

    public FieldsBean getFields() {
        return fields;
    }

    public void setFields(FieldsBean fields) {
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
