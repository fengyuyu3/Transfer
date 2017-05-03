package com.ironaviation.traveller.mvp.model.entity;

import com.baidu.mapapi.model.LatLng;
import com.baidu.mapapi.search.core.PoiInfo;

import java.io.Serializable;

/**
 * 项目名称：Traveller
 * 类描述：
 * 创建人：starRing
 * 创建时间：2017-04-10 13:49
 * 修改人：starRing
 * 修改时间：2017-04-10 13:49
 * 修改备注：
 */
public class HistoryPoiInfo implements Serializable {
    private boolean flagHistory;
    public String name;
    public String uid;
    public String address;
    public String city;
    public String phoneNum;
    public String postCode;
    public PoiInfo.POITYPE type;
    public LatLng location;
    public boolean hasCaterDetails;
    public boolean isPano;

    public HistoryPoiInfo(PoiInfo poiInfo,boolean flagHistory) {
        super();
        this.name=poiInfo.name;
        this.uid=poiInfo.uid;
        this.address=poiInfo.address;
        this.city=poiInfo.city;
        this.phoneNum=poiInfo.phoneNum;
        this.postCode=poiInfo.postCode;
        this.type=poiInfo.type;
        this.city=poiInfo.city;
        this.location=poiInfo.location;
        this.uid=poiInfo.uid;
        this.hasCaterDetails=poiInfo.hasCaterDetails;
        this.isPano=poiInfo.isPano;
        this.flagHistory=flagHistory;
    }

    public HistoryPoiInfo(String name, String address, LatLng location) {
        this.name = name;
        this.address = address;
        this.location = location;
    }

    public boolean isFlagHistory() {
        return flagHistory;
    }

    public void setFlagHistory(boolean flagHistory) {
        this.flagHistory = flagHistory;
    }
}
