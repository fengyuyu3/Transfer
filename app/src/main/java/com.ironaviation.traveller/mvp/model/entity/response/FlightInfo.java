package com.ironaviation.traveller.mvp.model.entity.response;

/**
 * 项目名称：Transfer
 * 类描述：
 * 创建人：flq
 * 创建时间：2017/4/5 10:01
 * 修改人：
 * 修改时间：2017/4/5 10:01
 * 修改备注：
 */

public class FlightInfo {

    /**
     * From : string
     * To : string
     * FlightNo : string
     * Company : string
     * Date : 2017-04-05T01:44:42.413Z
     * Rate : 0
     */

    private String From;
    private String To;
    private String FlightNo;
    private String Company;
    private String Date;
    private int Rate;

    public String getFrom() {
        return From;
    }

    public void setFrom(String From) {
        this.From = From;
    }

    public String getTo() {
        return To;
    }

    public void setTo(String To) {
        this.To = To;
    }

    public String getFlightNo() {
        return FlightNo;
    }

    public void setFlightNo(String FlightNo) {
        this.FlightNo = FlightNo;
    }

    public String getCompany() {
        return Company;
    }

    public void setCompany(String Company) {
        this.Company = Company;
    }

    public String getDate() {
        return Date;
    }

    public void setDate(String Date) {
        this.Date = Date;
    }

    public int getRate() {
        return Rate;
    }

    public void setRate(int Rate) {
        this.Rate = Rate;
    }

    @Override
    public String toString() {
        return "FlightInfo{" +
                "From='" + From + '\'' +
                ", To='" + To + '\'' +
                ", FlightNo='" + FlightNo + '\'' +
                ", Company='" + Company + '\'' +
                ", Date='" + Date + '\'' +
                ", Rate=" + Rate +
                '}';
    }
}
