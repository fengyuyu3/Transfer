package com.ironaviation.traveller.mvp.model.entity.request;

import java.util.List;

/**
 * Created by Administrator on 2017/4/10 0010.
 */

public class ClearanceOrderRequest {

    /**
     * BID : string
     * FlightNo : string
     * FlightDate : string
     * TakeOffDateTime : 2017-04-10T02:06:56.310Z
     * ArriveDateTime : 2017-04-10T02:06:56.310Z
     * TakeOffAddress : string
     * ArriveAddress : string
     * PickupAddress : string
     * PickupTime : 2017-04-10T02:06:56.310Z
     * PickupLongitude : 0
     * PickupLatitude : 0
     * DestAddress : string
     * DestLongitude : 0
     * DestLatitude : 0
     * SeatNum : 0
     * TotalPrice : 0
     * Passengers : [{"Name":"string","IDCardNo":"string","IDCardType":"string","Gender":"string","Phone":"string","Notes":"string","Price":0,"IsValid":true}]
     */

    private String BID;
    private String FlightNo;
    private String FlightDate;
    private String TakeOffDateTime;
    private String ArriveDateTime;
    private String TakeOffAddress;
    private String ArriveAddress;
    private String PickupAddress;
    private String PickupTime;
    private int PickupLongitude;
    private int PickupLatitude;
    private String DestAddress;
    private int DestLongitude;
    private int DestLatitude;
    private int SeatNum;
    private int TotalPrice;
    private List<PassengersRequest> Passengers;

    public String getBID() {
        return BID;
    }

    public void setBID(String BID) {
        this.BID = BID;
    }

    public String getFlightNo() {
        return FlightNo;
    }

    public void setFlightNo(String FlightNo) {
        this.FlightNo = FlightNo;
    }

    public String getFlightDate() {
        return FlightDate;
    }

    public void setFlightDate(String FlightDate) {
        this.FlightDate = FlightDate;
    }

    public String getTakeOffDateTime() {
        return TakeOffDateTime;
    }

    public void setTakeOffDateTime(String TakeOffDateTime) {
        this.TakeOffDateTime = TakeOffDateTime;
    }

    public String getArriveDateTime() {
        return ArriveDateTime;
    }

    public void setArriveDateTime(String ArriveDateTime) {
        this.ArriveDateTime = ArriveDateTime;
    }

    public String getTakeOffAddress() {
        return TakeOffAddress;
    }

    public void setTakeOffAddress(String TakeOffAddress) {
        this.TakeOffAddress = TakeOffAddress;
    }

    public String getArriveAddress() {
        return ArriveAddress;
    }

    public void setArriveAddress(String ArriveAddress) {
        this.ArriveAddress = ArriveAddress;
    }

    public String getPickupAddress() {
        return PickupAddress;
    }

    public void setPickupAddress(String PickupAddress) {
        this.PickupAddress = PickupAddress;
    }

    public String getPickupTime() {
        return PickupTime;
    }

    public void setPickupTime(String PickupTime) {
        this.PickupTime = PickupTime;
    }

    public int getPickupLongitude() {
        return PickupLongitude;
    }

    public void setPickupLongitude(int PickupLongitude) {
        this.PickupLongitude = PickupLongitude;
    }

    public int getPickupLatitude() {
        return PickupLatitude;
    }

    public void setPickupLatitude(int PickupLatitude) {
        this.PickupLatitude = PickupLatitude;
    }

    public String getDestAddress() {
        return DestAddress;
    }

    public void setDestAddress(String DestAddress) {
        this.DestAddress = DestAddress;
    }

    public int getDestLongitude() {
        return DestLongitude;
    }

    public void setDestLongitude(int DestLongitude) {
        this.DestLongitude = DestLongitude;
    }

    public int getDestLatitude() {
        return DestLatitude;
    }

    public void setDestLatitude(int DestLatitude) {
        this.DestLatitude = DestLatitude;
    }

    public int getSeatNum() {
        return SeatNum;
    }

    public void setSeatNum(int SeatNum) {
        this.SeatNum = SeatNum;
    }

    public int getTotalPrice() {
        return TotalPrice;
    }

    public void setTotalPrice(int TotalPrice) {
        this.TotalPrice = TotalPrice;
    }

    public List<PassengersRequest> getPassengers() {
        return Passengers;
    }

    public void setPassengers(List<PassengersRequest> Passengers) {
        this.Passengers = Passengers;
    }

    @Override
    public String toString() {
        return "ClearanceOrderRequest{" +
                "BID='" + BID + '\'' +
                ", FlightNo='" + FlightNo + '\'' +
                ", FlightDate='" + FlightDate + '\'' +
                ", TakeOffDateTime='" + TakeOffDateTime + '\'' +
                ", ArriveDateTime='" + ArriveDateTime + '\'' +
                ", TakeOffAddress='" + TakeOffAddress + '\'' +
                ", ArriveAddress='" + ArriveAddress + '\'' +
                ", PickupAddress='" + PickupAddress + '\'' +
                ", PickupTime='" + PickupTime + '\'' +
                ", PickupLongitude=" + PickupLongitude +
                ", PickupLatitude=" + PickupLatitude +
                ", DestAddress='" + DestAddress + '\'' +
                ", DestLongitude=" + DestLongitude +
                ", DestLatitude=" + DestLatitude +
                ", SeatNum=" + SeatNum +
                ", TotalPrice=" + TotalPrice +
                ", Passengers=" + Passengers +
                '}';
    }
}
