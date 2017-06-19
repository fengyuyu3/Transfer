package com.ironaviation.traveller.mvp.model.entity.request;

import java.io.Serializable;
import java.util.List;

/**
 * Created by flq on 2017/6/19.
 */

public class ValidatePassengerRequest implements Serializable{


    /**
     * FlightNo : 3U8889
     * FlightDate : 1497861000000
     * IDCardNos : ["510321198507245790"]
     */

    private String FlightNo;
    private String FlightDate;
    private List<String> IDCardNos;

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

    public List<String> getIDCardNos() {
        return IDCardNos;
    }

    public void setIDCardNos(List<String> IDCardNos) {
        this.IDCardNos = IDCardNos;
    }
}
