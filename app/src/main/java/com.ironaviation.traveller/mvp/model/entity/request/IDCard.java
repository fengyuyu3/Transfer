package com.ironaviation.traveller.mvp.model.entity.request;

/**
 * 项目名称：Transfer
 * 类描述：
 * 创建人：flq
 * 创建时间：2017/3/30 19:34
 * 修改人：
 * 修改时间：2017/3/30 19:34
 * 修改备注：
 */

public class IDCard {

    private String IDCard;
    private int position;
    private int status;

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getIDCard() {
        return IDCard;
    }

    public void setIDCard(String IDCard) {
        this.IDCard = IDCard;
    }

    public int getPosition() {
        return position;
    }

    public void setPosition(int position) {
        this.position = position;
    }
}
