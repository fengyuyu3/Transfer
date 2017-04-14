package com.ironaviation.traveller.mvp.model.entity.response;

import java.util.List;

/**
 * 项目名称：Traveller
 * 类描述：
 * 创建人：starRing
 * 创建时间：2017-04-14 13:31
 * 修改人：starRing
 * 修改时间：2017-04-14 13:31
 * 修改备注：
 */
public class CancelBookingInfo {


    private List<String> Reasons;

    private boolean IsFreeCancel;

    private double CancelPrice;

    public void setString(List<String> Reasons) {
        this.Reasons = Reasons;
    }

    public List<String> getString() {
        return this.Reasons;
    }

    public void setIsFreeCancel(boolean IsFreeCancel) {
        this.IsFreeCancel = IsFreeCancel;
    }

    public boolean getIsFreeCancel() {
        return this.IsFreeCancel;
    }

    public void setCancelPrice(double CancelPrice) {
        this.CancelPrice = CancelPrice;
    }

    public double getCancelPrice() {
        return this.CancelPrice;
    }


}
