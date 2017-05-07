package com.ironaviation.traveller.mvp.model.entity.request;

/**
 * 项目名称：Transfer
 * 类描述：
 * 创建人：flq
 * 创建时间：2017/3/29 8:51
 * 修改人：
 * 修改时间：2017/3/29 8:51
 * 修改备注：
 */

public class AirPortRequest {

    private String idCard;
    private int status;
    private boolean hasBooked;

    public boolean isHasBooked() {
        return hasBooked;
    }

    public void setHasBooked(boolean hasBooked) {
        this.hasBooked = hasBooked;
    }

    public String getIdCard() {
        return idCard;
    }

    public void setIdCard(String idCard) {
        this.idCard = idCard;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }
}
