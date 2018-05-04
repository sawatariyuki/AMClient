package com.gift.sawatariyuki.amclient.Bean;

import java.io.Serializable;
import java.util.List;

public class GetEventResponse extends DefaultResponse implements Serializable{
    private List<Event> data;

    public GetEventResponse(int code, String msg, List<Event> data) {
        super(code, msg);
        this.data = data;
    }

    public List<Event> getData() {
        return data;
    }

    public void setData(List<Event> data) {
        this.data = data;
    }

    @Override
    public String toString() {
        return "GetEventResponse{" +
                "data=" + data +
                '}';
    }
}
