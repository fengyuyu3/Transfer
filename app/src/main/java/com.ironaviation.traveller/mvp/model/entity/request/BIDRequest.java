package com.ironaviation.traveller.mvp.model.entity.request;

/**
 * Created by Administrator on 2017/4/14 0014.
 */

public class BIDRequest {
    private String BID;

    public String getBID() {
        return BID;
    }

    public void setBID(String BID) {
        this.BID = BID;
    }

    @Override
    public String toString() {
        return "BIDRequest{" +
                "BID='" + BID + '\'' +
                '}';
    }
}
