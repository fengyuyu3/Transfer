package com.ironaviation.traveller.mvp.model.entity;

import com.ironaviation.traveller.mvp.model.entity.response.PushResponse;

/**
 * Created by Administrator on 2017/4/30.
 */

public class BasePushData{
    private int Type;
    private String Message;
    private PushResponse Data;

    public int getType() {
        return Type;
    }

    public void setType(int type) {
        Type = type;
    }

    public String getMessage() {
        return Message;
    }

    public void setMessage(String message) {
        Message = message;
    }

    public PushResponse getData() {
        return Data;
    }

    public void setData(PushResponse data) {
        Data = data;
    }

    @Override
    public String toString() {
        return "BasePushData{" +
                "Type=" + Type +
                ", Message='" + Message + '\'' +
                ", Data=" + Data +
                '}';
    }
}
