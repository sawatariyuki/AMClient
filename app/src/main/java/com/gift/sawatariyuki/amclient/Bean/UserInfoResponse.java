package com.gift.sawatariyuki.amclient.Bean;

import java.io.Serializable;

public class UserInfoResponse extends DefaultResponse implements Serializable {
    private UserInfo data;

    public UserInfoResponse(int code, String msg, UserInfo data) {
        super(code, msg);
        this.data = data;
    }

    public UserInfo getData() {
        return data;
    }

    public void setData(UserInfo data) {
        this.data = data;
    }

    @Override
    public String toString() {
        return "UserInfoResponse{" +
                "data=" + data +
                '}';
    }
}

