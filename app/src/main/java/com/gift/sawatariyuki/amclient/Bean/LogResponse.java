package com.gift.sawatariyuki.amclient.Bean;

import java.io.Serializable;
import java.util.List;

public class LogResponse extends DefaultResponse implements Serializable {
    private List<MLog> data;

    public LogResponse(int code, String msg, List<MLog> data) {
        super(code, msg);
        this.data = data;
    }

    public List<MLog> getData() {
        return data;
    }

    public void setData(List<MLog> data) {
        this.data = data;
    }

    @Override
    public String toString() {
        return "LogResponse{" +
                "data=" + data +
                '}';
    }
}
