package com.ironaviation.traveller.mvp.model.entity;

/**
 * 项目名称：Traveller
 * 类描述：
 * 创建人：starRing
 * 创建时间：2017-03-23 13:55
 * 修改人：starRing
 * 修改时间：2017-03-23 13:55
 * 修改备注：
 */
public class Login {

    private String PhoneNumber;

    private String ValidateCode;
    private String ClientId;

    public String getValidateCode() {
        return ValidateCode;
    }

    public void setValidateCode(String validateCode) {
        ValidateCode = validateCode;
    }

    public String getPhoneNumber() {
        return PhoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        PhoneNumber = phoneNumber;
    }


    public String getClientId() {
        return ClientId;
    }

    public void setClientId(String clientId) {
        ClientId = clientId;
    }
}
