package com.ironaviation.traveller.mvp.model.entity.request;

/**
 * 项目名称：Traveller
 * 类描述：
 * 创建人：starRing
 * 创建时间：2017-04-13 16:43
 * 修改人：starRing
 * 修改时间：2017-04-13 16:43
 * 修改备注：
 */
public class IdentificationRequest {
    private String IDCard;
    private String Name;

    public String getIDCard() {
        return IDCard;
    }

    public void setIDCard(String IDCard) {
        this.IDCard = IDCard;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }


}
