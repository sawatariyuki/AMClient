package com.gift.sawatariyuki.amclient.Bean;

import java.io.Serializable;

public class LoginResponse extends DefaultResponse implements Serializable{
    private UserDefault data;

    public LoginResponse(int code, String msg, UserDefault data) {
        super(code, msg);
        this.data = data;
    }

    public UserDefault getData() {
        return data;
    }

    public void setData(UserDefault data) {
        this.data = data;
    }
}
