package com.ironaviation.traveller.mvp.model.entity.request;

import java.io.Serializable;

/**
 * Created by Administrator on 2017/4/10 0010.
 */

public class PassengersRequest implements Serializable{
    /**
     * Name : string
     * IDCardNo : string
     * IDCardType : string
     * Gender : string
     * Phone : string
     * Notes : string
     * Price : 0
     * IsValid : true
     */

    private String Name;
    private String IDCardNo;
    private String IDCardType;
    private String Gender;
    private String Phone;
    private String Notes;
    private float Price;
    private boolean IsValid;
    private boolean HasBooked;
    private int SeatNumber;
    private String CabinSeat;

    public int getSeatNumber() {
        return SeatNumber;
    }

    public void setSeatNumber(int seatNumber) {
        SeatNumber = seatNumber;
    }

    public String getCabinSeat() {
        return CabinSeat;
    }

    public void setCabinSeat(String cabinSeat) {
        CabinSeat = cabinSeat;
    }

    public boolean isValid() {
        return IsValid;
    }

    public void setValid(boolean valid) {
        IsValid = valid;
    }

    public boolean isHasBooked() {
        return HasBooked;
    }

    public void setHasBooked(boolean hasBooked) {
        HasBooked = hasBooked;
    }

    public String getName() {
        return Name;
    }

    public void setName(String Name) {
        this.Name = Name;
    }

    public String getIDCardNo() {
        return IDCardNo;
    }

    public void setIDCardNo(String IDCardNo) {
        this.IDCardNo = IDCardNo;
    }

    public String getIDCardType() {
        return IDCardType;
    }

    public void setIDCardType(String IDCardType) {
        this.IDCardType = IDCardType;
    }

    public String getGender() {
        return Gender;
    }

    public void setGender(String Gender) {
        this.Gender = Gender;
    }

    public String getPhone() {
        return Phone;
    }

    public void setPhone(String Phone) {
        this.Phone = Phone;
    }

    public String getNotes() {
        return Notes;
    }

    public void setNotes(String Notes) {
        this.Notes = Notes;
    }

    public float getPrice() {
        return Price;
    }

    public void setPrice(float Price) {
        this.Price = Price;
    }

    public boolean isIsValid() {
        return IsValid;
    }

    public void setIsValid(boolean IsValid) {
        this.IsValid = IsValid;
    }

    @Override
    public String toString() {
        return "PassengersRequest{" +
                "Name='" + Name + '\'' +
                ", IDCardNo='" + IDCardNo + '\'' +
                ", IDCardType='" + IDCardType + '\'' +
                ", Gender='" + Gender + '\'' +
                ", Phone='" + Phone + '\'' +
                ", Notes='" + Notes + '\'' +
                ", Price=" + Price +
                ", IsValid=" + IsValid +
                '}';
    }
}
