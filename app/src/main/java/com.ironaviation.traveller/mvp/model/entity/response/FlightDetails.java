package com.ironaviation.traveller.mvp.model.entity.response;

/**
 * 项目名称：Transfer
 * 类描述：
 * 创建人：flq
 * 创建时间：2017/4/5 10:02
 * 修改人：
 * 修改时间：2017/4/5 10:02
 * 修改备注：
 */

public class FlightDetails {
    /**
     * TakeOff : string
     * Arrive : string
     * TakeOffTime : 2017-04-05T01:44:42.413Z
     * RealityTakeOffTime : 2017-04-05T01:44:42.413Z
     * ArriveTime : 2017-04-05T01:44:42.413Z
     * RealityArriveTime : 2017-04-05T01:44:42.413Z
     * State : string
     * StateId : 0
     * Gate : string
     * CheckInCounter : string
     * PackageGate : string
     */

    private String TakeOff;
    private String Arrive;
    private Long TakeOffTime;
    private Long RealityTakeOffTime;
    private Long ArriveTime;
    private Long RealityArriveTime;
    private String State;
    private int StateId;
    private String Gate;
    private String CheckInCounter;
    private String PackageGate;

    public String getTakeOff() {
        return TakeOff;
    }

    public void setTakeOff(String TakeOff) {
        this.TakeOff = TakeOff;
    }

    public String getArrive() {
        return Arrive;
    }

    public void setArrive(String Arrive) {
        this.Arrive = Arrive;
    }

    public Long getTakeOffTime() {
        return TakeOffTime;
    }

    public void setTakeOffTime(Long TakeOffTime) {
        this.TakeOffTime = TakeOffTime;
    }

    public Long getRealityTakeOffTime() {
        return RealityTakeOffTime;
    }

    public void setRealityTakeOffTime(Long RealityTakeOffTime) {
        this.RealityTakeOffTime = RealityTakeOffTime;
    }

    public Long getArriveTime() {
        return ArriveTime;
    }

    public void setArriveTime(Long ArriveTime) {
        this.ArriveTime = ArriveTime;
    }

    public Long getRealityArriveTime() {
        return RealityArriveTime;
    }

    public void setRealityArriveTime(Long RealityArriveTime) {
        this.RealityArriveTime = RealityArriveTime;
    }

    public String getState() {
        return State;
    }

    public void setState(String State) {
        this.State = State;
    }

    public int getStateId() {
        return StateId;
    }

    public void setStateId(int StateId) {
        this.StateId = StateId;
    }

    public String getGate() {
        return Gate;
    }

    public void setGate(String Gate) {
        this.Gate = Gate;
    }

    public String getCheckInCounter() {
        return CheckInCounter;
    }

    public void setCheckInCounter(String CheckInCounter) {
        this.CheckInCounter = CheckInCounter;
    }

    public String getPackageGate() {
        return PackageGate;
    }

    public void setPackageGate(String PackageGate) {
        this.PackageGate = PackageGate;
    }

    @Override
    public String toString() {
        return "FlightDetails{" +
                "TakeOff='" + TakeOff + '\'' +
                ", Arrive='" + Arrive + '\'' +
                ", TakeOffTime='" + TakeOffTime + '\'' +
                ", RealityTakeOffTime='" + RealityTakeOffTime + '\'' +
                ", ArriveTime='" + ArriveTime + '\'' +
                ", RealityArriveTime='" + RealityArriveTime + '\'' +
                ", State='" + State + '\'' +
                ", StateId=" + StateId +
                ", Gate='" + Gate + '\'' +
                ", CheckInCounter='" + CheckInCounter + '\'' +
                ", PackageGate='" + PackageGate + '\'' +
                '}';
    }
}
