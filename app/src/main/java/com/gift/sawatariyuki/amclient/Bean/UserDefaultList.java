package com.gift.sawatariyuki.amclient.Bean;

import java.io.Serializable;
import java.util.List;

public class UserDefaultList implements Serializable {
    private int code;
    private String msg;
    private List<UserDefault> data;

    public UserDefaultList(int code, String msg, List<UserDefault> data) {
        this.code = code;
        this.msg = msg;
        this.data = data;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public List<UserDefault> getData() {
        return data;
    }

    public void setData(List<UserDefault> data) {
        this.data = data;
    }

    @Override
    public String toString() {
        return "UserDefaultList{" +
                "code=" + code +
                ", msg='" + msg + '\'' +
                ", data=" + data +
                '}';
    }
}
