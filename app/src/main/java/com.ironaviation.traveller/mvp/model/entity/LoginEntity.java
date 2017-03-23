package com.ironaviation.traveller.mvp.model.entity;

/**
 * 项目名称：Transfer
 * 类描述：登录信息
 * 创建时间：2017-01-11 10:16
 * 修改时间：2017-01-11 10:16
 * 修改备注：
 */
public class LoginEntity {

    private String DID;//司机ID

    private String Name;//司机名称

    private String Code;//司机工号//百度实体//二维码

    private String Phone;//手机号码

    private String Avatar;//头像地址

    private String AccessToken;//访问令牌

    private String TokenType;//令牌类型

    private long ExpiresIn;//还有多久过期

    private long Issued;//令牌发行日期

    private long Expires;//过期时间


    public void setDID(String DID) {
        this.DID = DID;
    }

    public String getDID() {
        return this.DID;
    }

    public void setName(String Name) {
        this.Name = Name;
    }

    public String getName() {
        return this.Name;
    }

    public void setCode(String Code) {
        this.Code = Code;
    }

    public String getCode() {
        return this.Code;
    }

    public void setPhone(String Phone) {
        this.Phone = Phone;
    }

    public String getPhone() {
        return this.Phone;
    }

    public void setAvatar(String Avatar) {
        this.Avatar = Avatar;
    }

    public String getAvatar() {
        return this.Avatar;
    }

    public void setAccessToken(String AccessToken) {
        this.AccessToken = AccessToken;
    }

    public String getAccessToken() {
        return this.AccessToken;
    }

    public void setTokenType(String TokenType) {
        this.TokenType = TokenType;
    }

    public String getTokenType() {
        return this.TokenType;
    }

    public void setExpiresIn(int ExpiresIn) {
        this.ExpiresIn = ExpiresIn;
    }

    public long getExpiresIn() {
        return this.ExpiresIn;
    }

    public void setIssued(int Issued) {
        this.Issued = Issued;
    }

    public long getIssued() {
        return this.Issued;
    }

    public void setExpires(int Expires) {
        this.Expires = Expires;
    }

    public long getExpires() {
        return this.Expires;
    }


}
