package com.ironaviation.traveller.mvp.model.entity.request;

import java.util.List;

/**
 * Created by flq on 2017/6/19.
 */

public class PreViewRequest {


    /**
     * ID : string
     * Phone : string
     * FlightNo : string
     * FlightDate : string
     * PickupTime : 2017-06-19T01:32:11.021Z
     * PickupAddress : string
     * PickupDetailAddress : string
     * ArriveAddress : string
     * ArriveDetailAddress : string
     * CarType : string
     * IDCardNos : ["string"]
     * PortType : 0
     * ServiceType : 0
     */

    private String ID;
    private String Phone;
    private String FlightNo;
    private String FlightDate;
    private long PickupTime;
    private String PickupAddress;
    private String PickupDetailAddress;
    private String ArriveAddress;
    private String ArriveDetailAddress;
    private String CarType;
    private int PortType;
    private int ServiceType;
    private List<String> IDCardNos;

    public String getID() {
        return ID;
    }

    public void setID(String ID) {
        this.ID = ID;
    }

    public String getPhone() {
        return Phone;
    }

    public void setPhone(String Phone) {
        this.Phone = Phone;
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

    public long getPickupTime() {
        return PickupTime;
    }

    public void setPickupTime(long PickupTime) {
        this.PickupTime = PickupTime;
    }

    public String getPickupAddress() {
        return PickupAddress;
    }

    public void setPickupAddress(String PickupAddress) {
        this.PickupAddress = PickupAddress;
    }

    public String getPickupDetailAddress() {
        return PickupDetailAddress;
    }

    public void setPickupDetailAddress(String PickupDetailAddress) {
        this.PickupDetailAddress = PickupDetailAddress;
    }

    public String getArriveAddress() {
        return ArriveAddress;
    }

    public void setArriveAddress(String ArriveAddress) {
        this.ArriveAddress = ArriveAddress;
    }

    public String getArriveDetailAddress() {
        return ArriveDetailAddress;
    }

    public void setArriveDetailAddress(String ArriveDetailAddress) {
        this.ArriveDetailAddress = ArriveDetailAddress;
    }

    public String getCarType() {
        return CarType;
    }

    public void setCarType(String CarType) {
        this.CarType = CarType;
    }

    public int getPortType() {
        return PortType;
    }

    public void setPortType(int PortType) {
        this.PortType = PortType;
    }

    public int getServiceType() {
        return ServiceType;
    }

    public void setServiceType(int ServiceType) {
        this.ServiceType = ServiceType;
    }

    public List<String> getIDCardNos() {
        return IDCardNos;
    }

    public void setIDCardNos(List<String> IDCardNos) {
        this.IDCardNos = IDCardNos;
    }
}
