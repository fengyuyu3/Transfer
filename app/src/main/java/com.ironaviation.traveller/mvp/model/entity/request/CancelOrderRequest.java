package com.ironaviation.traveller.mvp.model.entity.request;

import java.util.List;

/**
 * 项目名称：Traveller
 * 类描述：
 * 创建人：starRing
 * 创建时间：2017-04-25 16:23
 * 修改人：starRing
 * 修改时间：2017-04-25 16:23
 * 修改备注：
 */
public class CancelOrderRequest {

    private String Reason;
    private List<String> ReasonCodes;

    public List<String> getReasonCodes() {
        return ReasonCodes;
    }

    public void setReasonCodes(List<String> reasonCodes) {
        ReasonCodes = reasonCodes;
    }

    public String getReason() {
        return Reason;
    }

    public void setReason(String reason) {
        Reason = reason;
    }


}
