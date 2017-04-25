package com.ironaviation.traveller.mvp.model.entity;

/**
 * 项目名称：Traveller
 * 类描述：
 * 创建人：starRing
 * 创建时间：2017-04-14 18:52
 * 修改人：starRing
 * 修改时间：2017-04-14 18:52
 * 修改备注：
 */
public class PayInfo {
    private String PIID;

    private String BID;

    private String PayMethod;

    private String PayAccount;

    private double Amount;

    private boolean IsPaied;

    private double Rebate;

    private boolean IsRebate;

    private boolean IsFreeCancel;

    public void setPIID(String PIID){
        this.PIID = PIID;
    }
    public String getPIID(){
        return this.PIID;
    }
    public void setBID(String BID){
        this.BID = BID;
    }
    public String getBID(){
        return this.BID;
    }
    public void setPayMethod(String PayMethod){
        this.PayMethod = PayMethod;
    }
    public String getPayMethod(){
        return this.PayMethod;
    }
    public void setPayAccount(String PayAccount){
        this.PayAccount = PayAccount;
    }
    public String getPayAccount(){
        return this.PayAccount;
    }
    public void setAmount(double Amount){
        this.Amount = Amount;
    }
    public double getAmount(){
        return this.Amount;
    }
    public void setIsPaied(boolean IsPaied){
        this.IsPaied = IsPaied;
    }
    public boolean getIsPaied(){
        return this.IsPaied;
    }
    public void setRebate(double Rebate){
        this.Rebate = Rebate;
    }
    public double getRebate(){
        return this.Rebate;
    }
    public void setIsRebate(boolean IsRebate){
        this.IsRebate = IsRebate;
    }
    public boolean getIsRebate(){
        return this.IsRebate;
    }
    public void setIsFreeCancel(boolean IsFreeCancel){
        this.IsFreeCancel = IsFreeCancel;
    }
    public boolean getIsFreeCancel(){
        return this.IsFreeCancel;
    }
}
