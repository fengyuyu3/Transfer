package com.ironaviation.traveller.event;

/**
 * 项目名称：Transfer
 * 类描述：
 * 创建人：flq
 * 创建时间：2017/3/31 11:15
 * 修改人：
 * 修改时间：2017/3/31 11:15
 * 修改备注：
 */

public class TravelCancelEvent {
    public int event;
    private String bid;
    public TravelCancelEvent(int event){
        this.event = event;
    }

    public TravelCancelEvent(int event, String bid) {
        this.event = event;
        this.bid = bid;
    }

    public int getEvent() {
        return event;
    }

    public void setEvent(int event) {
        this.event = event;
    }

    public String getBid() {
        return bid;
    }

    public void setBid(String bid) {
        this.bid = bid;
    }
}
