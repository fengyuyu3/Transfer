package com.ironaviation.traveller.mvp.model.entity.request;

/**
 * 项目名称：Traveller
 * 类描述：
 * 创建人：starRing
 * 创建时间：2017-04-14 11:54
 * 修改人：starRing
 * 修改时间：2017-04-14 11:54
 * 修改备注：
 */
public class CancelBookingRequest {
    private String Reason;

    public String getReason() {
        return Reason;
    }

    public void setReason(String reason) {
        Reason = reason;
    }
}
