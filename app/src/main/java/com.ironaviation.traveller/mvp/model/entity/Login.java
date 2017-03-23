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
    String UserName;
    String Password;
    String ClientID;

    public String getUserName() {
        return UserName;
    }

    public void setUserName(String userName) {
        UserName = userName;
    }

    public String getPassword() {
        return Password;
    }

    public void setPassword(String password) {
        Password = password;
    }

    public String getClientID() {
        return ClientID;
    }

    public void setClientID(String clientID) {
        ClientID = clientID;
    }
}
