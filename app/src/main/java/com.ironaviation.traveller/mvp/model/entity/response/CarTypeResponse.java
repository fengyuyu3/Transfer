package com.ironaviation.traveller.mvp.model.entity.response;

import java.io.Serializable;

/**
 * Created by flq on 2017/6/19.
 */

public class CarTypeResponse  implements Serializable{

    /**
     * CarType : HighLevel
     * SeatNum : 4
     * Name : 豪华轿车
     */

    private String CarType;
    private int SeatNum;
    private String Name;

    public String getCarType() {
        return CarType;
    }

    public void setCarType(String CarType) {
        this.CarType = CarType;
    }

    public int getSeatNum() {
        return SeatNum;
    }

    public void setSeatNum(int SeatNum) {
        this.SeatNum = SeatNum;
    }

    public String getName() {
        return Name;
    }

    public void setName(String Name) {
        this.Name = Name;
    }
}
