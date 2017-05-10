package com.ironaviation.traveller.mvp.model.entity.request;

/**
 * Created by Administrator on 2017/5/10.
 */

public class AddressLimitRequest {

    private String CityCode;
    private double Longitude;
    private double Latitude;

    public String getAddress() {
        return CityCode;
    }

    public void setAddress(String address) {
        this.CityCode = address;
    }

    public double getLongitude() {
        return Longitude;
    }

    public void setLongitude(double longitude) {
        this.Longitude = longitude;
    }

    public double getLatitude() {
        return Latitude;
    }

    public void setLatitude(double latitude) {
        this.Latitude = latitude;
    }
}
