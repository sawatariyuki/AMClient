package com.gift.sawatariyuki.amclient.Bean;

import java.io.Serializable;
import java.util.List;

public class GetTypeResponse extends DefaultResponse implements Serializable {
    private List<Type> data;

    public GetTypeResponse(int code, String msg, List<Type> data) {
        super(code, msg);
        this.data = data;
    }

    public List<Type> getData() {
        return data;
    }

    public void setData(List<Type> data) {
        this.data = data;
    }

    @Override
    public String toString() {
        return "GetTypeResponse{" +
                "data=" + data +
                '}';
    }
}
