package com.ironaviation.traveller.mvp.model.entity.response;

import java.math.BigDecimal;

/**
 * Created by Administrator on 2017/4/12 0012.
 */

public class RouteItemResponse {
    /**
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
     * PickupTime : 2017-04-12T12:42:15.947Z
     * Cdt : 2017-04-12T12:42:15.947Z
     * IsComment : true
     * IsPaied : true
     */
    private String BID;
    private String UID;
    private int SeqNum;
    private String OrderNo;
    private String Channel;
    private String PickupAddress;
    private String DestAddress;
    private int SeatNum;
    private float ActualPrice;
    private float TotalPrice;
    private String Status;
    private boolean IsDeleted;
    private String TripType;
    private String PickupTime;
    private String Cdt;
    private boolean IsComment;
    private boolean IsPaied;
    private long CurrentTime;
    private long ExpireAt;

    public boolean isDeleted() {
        return IsDeleted;
    }

    public void setDeleted(boolean deleted) {
        IsDeleted = deleted;
    }

    public boolean isComment() {
        return IsComment;
    }

    public void setComment(boolean comment) {
        IsComment = comment;
    }

    public boolean isPaied() {
        return IsPaied;
    }

    public void setPaied(boolean paied) {
        IsPaied = paied;
    }

    public long getCurrentTime() {
        return CurrentTime;
    }

    public void setCurrentTime(long currentTime) {
        CurrentTime = currentTime;
    }

    public long getExpireAt() {
        return ExpireAt;
    }

    public void setExpireAt(long expireAt) {
        ExpireAt = expireAt;
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

    public float getActualPrice() {
        return ActualPrice;
    }

    public void setActualPrice(float ActualPrice) {
        this.ActualPrice = ActualPrice;
    }

    public float getTotalPrice() {
        return TotalPrice;
    }

    public void setTotalPrice(float TotalPrice) {
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

    @Override
    public String toString() {
        return "RouteItemResponse{" +
                "BID='" + BID + '\'' +
                ", UID='" + UID + '\'' +
                ", SeqNum=" + SeqNum +
                ", OrderNo='" + OrderNo + '\'' +
                ", Channel='" + Channel + '\'' +
                ", PickupAddress='" + PickupAddress + '\'' +
                ", DestAddress='" + DestAddress + '\'' +
                ", SeatNum=" + SeatNum +
                ", ActualPrice=" + ActualPrice +
                ", TotalPrice=" + TotalPrice +
                ", Status='" + Status + '\'' +
                ", IsDeleted=" + IsDeleted +
                ", TripType='" + TripType + '\'' +
                ", PickupTime='" + PickupTime + '\'' +
                ", Cdt='" + Cdt + '\'' +
                ", IsComment=" + IsComment +
                ", IsPaied=" + IsPaied +
                '}';
    }
}
