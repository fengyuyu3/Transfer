package com.ironaviation.traveller.mvp.model.entity.response;

/**
 * Created by Administrator on 2017/4/28.
 */

public class PassengersResponse implements Comparable<PassengersResponse>{

    /**
     * OrderNo : string
     * PickupAddress : string
     * DestAddress : string
     * Status : string
     * ChildStatus : string
     * PickupTime : 2017-04-28T01:42:51.843Z
     * PickupLongitude : 0
     * PickupLatitude : 0
     * DestLongitude : 0
     * DestLatitude : 0
     */

    private String OrderNo;
    private String PickupAddress;
    private String DestAddress;
    private String Status;
    private String ChildStatus;
    private long PickupTime;
    private double PickupLongitude;
    private double PickupLatitude;
    private double DestLongitude;
    private double DestLatitude;
    private String UID;

    public String getUID() {
        return UID;
    }

    public void setUID(String UID) {
        this.UID = UID;
    }

    public String getOrderNo() {
        return OrderNo;
    }

    public void setOrderNo(String OrderNo) {
        this.OrderNo = OrderNo;
    }

    public String getPickupAddress() {
        return PickupAddress;
    }

    public void setPickupAddress(String PickupAddress) {
        this.PickupAddress = PickupAddress;
    }

    public String getDestAddress() {
        return DestAddress;
    }

    public void setDestAddress(String DestAddress) {
        this.DestAddress = DestAddress;
    }

    public String getStatus() {
        return Status;
    }

    public void setStatus(String Status) {
        this.Status = Status;
    }

    public String getChildStatus() {
        return ChildStatus;
    }

    public void setChildStatus(String ChildStatus) {
        this.ChildStatus = ChildStatus;
    }

    public long getPickupTime() {
        return PickupTime;
    }

    public void setPickupTime(long PickupTime) {
        this.PickupTime = PickupTime;
    }

    public double getPickupLongitude() {
        return PickupLongitude;
    }

    public void setPickupLongitude(double PickupLongitude) {
        this.PickupLongitude = PickupLongitude;
    }

    public double getPickupLatitude() {
        return PickupLatitude;
    }

    public void setPickupLatitude(double PickupLatitude) {
        this.PickupLatitude = PickupLatitude;
    }

    public double getDestLongitude() {
        return DestLongitude;
    }

    public void setDestLongitude(double DestLongitude) {
        this.DestLongitude = DestLongitude;
    }

    public double getDestLatitude() {
        return DestLatitude;
    }

    public void setDestLatitude(double DestLatitude) {
        this.DestLatitude = DestLatitude;
    }

    @Override
    public int compareTo(PassengersResponse o) {
        if(this.PickupTime - o.PickupTime > 0){
            return 1;
        }else if(this.PickupTime - o.PickupTime == 0){
            return 0;
        }else{
            return -1;
        }
    }
}
