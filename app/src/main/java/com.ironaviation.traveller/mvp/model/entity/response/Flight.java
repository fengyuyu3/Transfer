package com.ironaviation.traveller.mvp.model.entity.response;

import java.util.List;

/**
 * 项目名称：Transfer
 * 类描述：
 * 创建人：flq
 * 创建时间：2017/4/5 10:04
 * 修改人：
 * 修改时间：2017/4/5 10:04
 * 修改备注：
 */

public class Flight {

    private FlightInfo Info;
    private java.util.List<FlightDetails> List;

    public FlightInfo getInfo() {
        return Info;
    }

    public void setInfo(FlightInfo info) {
        Info = info;
    }

    public java.util.List<FlightDetails> getList() {
        return List;
    }

    public void setList(java.util.List<FlightDetails> list) {
        List = list;
    }
}
