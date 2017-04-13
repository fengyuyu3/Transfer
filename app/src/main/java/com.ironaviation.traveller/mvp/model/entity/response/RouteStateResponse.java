package com.ironaviation.traveller.mvp.model.entity.response;

import com.ironaviation.traveller.mvp.model.entity.request.PassengersRequest;

import java.io.Serializable;
import java.util.List;

/**
 * Created by Administrator on 2017/4/12 0012.
 */

public class RouteStateResponse implements Serializable{

    /**
     * DriverName : string
     * DriverRate : string
     * DriverPhone : string
     * CarModel : string
     * CarLicense : string
     * CarColor : string
     * PickupLongitude : 0
     * PickupLatitude : 0
     * DestLongitude : 0
     * DestLagitude : 0
     * Passengers : [{"Name":"string","IDCardNo":"string","IDCardType":"string","Gender":"string","Phone":"string","Notes":"string","Price":0,"IsValid":true}]
     * BID : string
     * UID : string
     * SeqNum : 0
     * OrderNo : string
     * Channel : string
     * PickupAddress : string
     * DestAddress : string
     * SeatNum : 0
     * ActualPrice : 0
     * TotalPrice : 0
     * Status : string
     * IsDeleted : true
     * TripType : string
     * PickupTime : 2017-04-12T12:42:15.972Z
     * Cdt : 2017-04-12T12:42:15.972Z
     * IsComment : true
     * IsPaied : true
     */

    private String DriverName;
    private String DriverRate;
    private String DriverPhone;
    private String CarModel;
    private String CarLicense;
    private String CarColor;
    private int PickupLongitude;
    private int PickupLatitude;
    private int DestLongitude;
    private int DestLagitude;
    private String BID;
    private String UID;
    private int SeqNum;
    private String OrderNo;
    private String Channel;
    private String PickupAddress;
    private String DestAddress;
    private int SeatNum;
    private int ActualPrice;
    private int TotalPrice;
    private String Status;
    private boolean IsDeleted;
    private String TripType;
    private String PickupTime;
    private String Cdt;
    private boolean IsComment;
    private boolean IsPaied;
    private List<PassengersRequest> Passengers;

    public String getDriverName() {
        return DriverName;
    }

    public void setDriverName(String DriverName) {
        this.DriverName = DriverName;
    }

    public String getDriverRate() {
        return DriverRate;
    }

    public void setDriverRate(String DriverRate) {
        this.DriverRate = DriverRate;
    }

    public String getDriverPhone() {
        return DriverPhone;
    }

    public void setDriverPhone(String DriverPhone) {
        this.DriverPhone = DriverPhone;
    }

    public String getCarModel() {
        return CarModel;
    }

    public void setCarModel(String CarModel) {
        this.CarModel = CarModel;
    }

    public String getCarLicense() {
        return CarLicense;
    }

    public void setCarLicense(String CarLicense) {
        this.CarLicense = CarLicense;
    }

    public String getCarColor() {
        return CarColor;
    }

    public void setCarColor(String CarColor) {
        this.CarColor = CarColor;
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

    public int getDestLongitude() {
        return DestLongitude;
    }

    public void setDestLongitude(int DestLongitude) {
        this.DestLongitude = DestLongitude;
    }

    public int getDestLagitude() {
        return DestLagitude;
    }

    public void setDestLagitude(int DestLagitude) {
        this.DestLagitude = DestLagitude;
    }

    public String getBID() {
        return BID;
    }

    public void setBID(String BID) {
        this.BID = BID;
    }

    public String getUID() {
        return UID;
    }

    public void setUID(String UID) {
        this.UID = UID;
    }

    public int getSeqNum() {
        return SeqNum;
    }

    public void setSeqNum(int SeqNum) {
        this.SeqNum = SeqNum;
    }

    public String getOrderNo() {
        return OrderNo;
    }

    public void setOrderNo(String OrderNo) {
        this.OrderNo = OrderNo;
    }

    public String getChannel() {
        return Channel;
    }

    public void setChannel(String Channel) {
        this.Channel = Channel;
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

    public int getSeatNum() {
        return SeatNum;
    }

    public void setSeatNum(int SeatNum) {
        this.SeatNum = SeatNum;
    }

    public int getActualPrice() {
        return ActualPrice;
    }

    public void setActualPrice(int ActualPrice) {
        this.ActualPrice = ActualPrice;
    }

    public int getTotalPrice() {
        return TotalPrice;
    }

    public void setTotalPrice(int TotalPrice) {
        this.TotalPrice = TotalPrice;
    }

    public String getStatus() {
        return Status;
    }

    public void setStatus(String Status) {
        this.Status = Status;
    }

    public boolean isIsDeleted() {
        return IsDeleted;
    }

    public void setIsDeleted(boolean IsDeleted) {
        this.IsDeleted = IsDeleted;
    }

    public String getTripType() {
        return TripType;
    }

    public void setTripType(String TripType) {
        this.TripType = TripType;
    }

    public String getPickupTime() {
        return PickupTime;
    }

    public void setPickupTime(String PickupTime) {
        this.PickupTime = PickupTime;
    }

    public String getCdt() {
        return Cdt;
    }

    public void setCdt(String Cdt) {
        this.Cdt = Cdt;
    }

    public boolean isIsComment() {
        return IsComment;
    }

    public void setIsComment(boolean IsComment) {
        this.IsComment = IsComment;
    }

    public boolean isIsPaied() {
        return IsPaied;
    }

    public void setIsPaied(boolean IsPaied) {
        this.IsPaied = IsPaied;
    }

    public List<PassengersRequest> getPassengers() {
        return Passengers;
    }

    public void setPassengers(List<PassengersRequest> Passengers) {
        this.Passengers = Passengers;
    }

}
