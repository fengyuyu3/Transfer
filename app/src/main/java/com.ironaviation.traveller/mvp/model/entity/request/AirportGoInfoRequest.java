package com.ironaviation.traveller.mvp.model.entity.request;

import java.util.List;

/**
 * Created by Administrator on 2017/4/12 0012.
 */

public class AirportGoInfoRequest {
    /**
     * BID : string
     * FlightNo : string
     * FlightDate : string
     * TakeOffDateTime : 2017-04-12T07:52:38.191Z
     * ArriveDateTime : 2017-04-12T07:52:38.191Z
     * TakeOffAddress : string
     * ArriveAddress : string
     * PickupAddress : string
     * PickupTime : 2017-04-12T07:52:38.191Z
     * PickupLongitude : 0
     * PickupLatitude : 0
     * DestAddress : string
     * DestLongitude : 0
     * DestLatitude : 0
     * SeatNum : 0
     * TotalPrice : 0
     * ActurlPrice : 0
     * Passengers : [{"Name":"string","IDCardNo":"string","IDCardType":"string","Gender":"string","Phone":"string","Notes":"string","Price":0,"IsValid":true}]
     */

    private String BID;
    private String FlightNo;
    private String FlightDate;
    private Long TakeOffDateTime;
    private Long ArriveDateTime;
    private String TakeOffAddress;
    private String ArriveAddress;
    private String PickupAddress;
    private long PickupTime;
    private double PickupLongitude;
    private double PickupLatitude;
    private String DestAddress;
    private double DestLongitude;
    private double DestLatitude;
    private int SeatNum;
    private int TotalPrice;
    private int ActurlPrice;
    private String CallNumber;
    private String City;
    private List<PassengersRequest> Passengers;
    private boolean IsEnterPort;

    public boolean isEnterPort() {
        return IsEnterPort;
    }

    public void setEnterPort(boolean enterPort) {
        IsEnterPort = enterPort;
    }

    public String getCity() {
        return City;
    }

    public void setCity(String city) {
        City = city;
    }

    public String getCallNumber() {
        return CallNumber;
    }

    public void setCallNumber(String callNumber) {
        CallNumber = callNumber;
    }

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

    public Long getTakeOffDateTime() {
        return TakeOffDateTime;
    }

    public void setTakeOffDateTime(Long TakeOffDateTime) {
        this.TakeOffDateTime = TakeOffDateTime;
    }

    public Long getArriveDateTime() {
        return ArriveDateTime;
    }

    public void setArriveDateTime(Long ArriveDateTime) {
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

    public String getDestAddress() {
        return DestAddress;
    }

    public void setDestAddress(String DestAddress) {
        this.DestAddress = DestAddress;
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

    public int getActurlPrice() {
        return ActurlPrice;
    }

    public void setActurlPrice(int ActurlPrice) {
        this.ActurlPrice = ActurlPrice;
    }

    public List<PassengersRequest> getPassengers() {
        return Passengers;
    }

    public void setPassengers(List<PassengersRequest> Passengers) {
        this.Passengers = Passengers;
    }

    @Override
    public String toString() {
        return "AirportGoInfoRequest{" +
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
                ", ActurlPrice=" + ActurlPrice +
                ", Passengers=" + Passengers +
                '}';
    }
}
