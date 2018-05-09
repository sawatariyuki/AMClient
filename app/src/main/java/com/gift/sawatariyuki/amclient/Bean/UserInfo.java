package com.gift.sawatariyuki.amclient.Bean;

import java.io.Serializable;

public class UserInfo implements Serializable {
    private UserDefault u_default;
    private UserDetail u_detail;

    public UserInfo(UserDefault u_default, UserDetail u_detail) {
        this.u_default = u_default;
        this.u_detail = u_detail;
    }

    public UserDefault getU_default() {
        return u_default;
    }

    public void setU_default(UserDefault u_default) {
        this.u_default = u_default;
    }

    public UserDetail getU_detail() {
        return u_detail;
    }

    public void setU_detail(UserDetail u_detail) {
        this.u_detail = u_detail;
    }

    @Override
    public String toString() {
        return "UserInfo{" +
                "u_default=" + u_default +
                ", u_detail=" + u_detail +
                '}';
    }
}
