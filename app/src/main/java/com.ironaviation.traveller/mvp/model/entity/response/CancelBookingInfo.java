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




    private List<Reasons> Reasons ;

    private boolean IsFreeCancel;

    private double CancelPrice;


    public List<CancelBookingInfo.Reasons> getReasons() {
        return Reasons;
    }

    public void setReasons(List<CancelBookingInfo.Reasons> reasons) {
        Reasons = reasons;
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

    public class Reasons {
        private String Code;

        private String Reason;

        private boolean type;

        public void setCode(String Code){
            this.Code = Code;
        }
        public String getCode(){
            return this.Code;
        }
        public void setReason(String Reason){
            this.Reason = Reason;
        }
        public String getReason(){
            return this.Reason;
        }

        public boolean isType() {
            return type;
        }

        public void setType(boolean type) {
            this.type = type;
        }

    }
}
