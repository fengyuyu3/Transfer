package com.ironaviation.traveller.mvp.model.entity.response;

/**
 * 项目名称：Traveller
 * 类描述：
 * 创建人：starRing
 * 创建时间：2017-04-13 16:43
 * 修改人：starRing
 * 修改时间：2017-04-13 16:43
 * 修改备注：
 */
public class IdentificationResponse {
    private String IDCard;
    private String RealName;
    private boolean Result;
    private String Reason;

    public boolean isResult() {
        return Result;
    }

    public void setResult(boolean result) {
        Result = result;
    }

    public String getReason() {
        return Reason;
    }

    public void setReason(String reason) {
        Reason = reason;
    }

    public String getIDCard() {
        return IDCard;
    }

    public void setIDCard(String IDCard) {
        this.IDCard = IDCard;
    }

    public String getName() {
        return RealName;
    }

    public void setName(String name) {
        RealName = name;
    }

}
