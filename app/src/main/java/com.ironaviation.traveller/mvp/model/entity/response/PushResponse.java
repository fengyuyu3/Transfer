package com.ironaviation.traveller.mvp.model.entity.response;

/**
 * Created by Administrator on 2017/4/27.
 */

public class PushResponse {


    /**
     * BID : 1C7D34A7-CC16-4C4E-948C-B7A2C785BB1F
     * BLID : 1C7D34A7-CC16-4C4E-948C-B7A2C785BB1F
     * POID : 201704300004
     * Status : BookSuccess
     * StatusName : 派单成功
     * Code : 201
     * TripType : ClearPort
     */

    private String BID;
    private String BLID;
    private String POID;
    private String Status;
    private String StatusName;
    private int Code;
    private String TripType;
    private boolean IsComment;
    private String UID;

    public String getUID() {
        return UID;
    }

    public void setUID(String UID) {
        this.UID = UID;
    }

    public boolean isComment() {
        return IsComment;
    }

    public void setComment(boolean comment) {
        IsComment = comment;
    }

    public String getBID() {
        return BID;
    }

    public void setBID(String BID) {
        this.BID = BID;
    }

    public String getBLID() {
        return BLID;
    }

    public void setBLID(String BLID) {
        this.BLID = BLID;
    }

    public String getPOID() {
        return POID;
    }

    public void setPOID(String POID) {
        this.POID = POID;
    }

    public String getStatus() {
        return Status;
    }

    public void setStatus(String Status) {
        this.Status = Status;
    }

    public String getStatusName() {
        return StatusName;
    }

    public void setStatusName(String StatusName) {
        this.StatusName = StatusName;
    }

    public int getCode() {
        return Code;
    }

    public void setCode(int Code) {
        this.Code = Code;
    }

    public String getTripType() {
        return TripType;
    }

    public void setTripType(String TripType) {
        this.TripType = TripType;
    }

    @Override
    public String toString() {
        return "PushResponse{" +
                "BID='" + BID + '\'' +
                ", BLID='" + BLID + '\'' +
                ", POID='" + POID + '\'' +
                ", Status='" + Status + '\'' +
                ", StatusName='" + StatusName + '\'' +
                ", Code=" + Code +
                ", TripType='" + TripType + '\'' +
                ", IsComment=" + IsComment +
                ", UID='" + UID + '\'' +
                '}';
    }
}
