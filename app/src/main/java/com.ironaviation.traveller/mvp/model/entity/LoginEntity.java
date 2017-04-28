package com.ironaviation.traveller.mvp.model.entity;

import java.io.Serializable;

/**
 * 项目名称：Transfer
 * 类描述：登录信息
 * 创建时间：2017-01-11 10:16
 * 修改时间：2017-01-11 10:16
 * 修改备注：
 */
public class LoginEntity extends BaseData implements Serializable{

        private String Name;

        private String Gender;

        private long LastLoginDate;

        private String AccessToken;

        private String TokenType;

        private long ExpiresIn;

        private long Issued;

        private long Expires;

        private String Phone;

        private boolean IsRealValid;

        private String IDCard;

        public String getIDCard() {
            return IDCard;
        }

        public void setIDCard(String IDCard) {
            this.IDCard = IDCard;
        }

        public boolean isRealValid() {
                return IsRealValid;
            }

        public void setRealValid(boolean realValid) {
            IsRealValid = realValid;
        }

        public String getPhone() {
            return Phone;
        }

        public void setPhone(String phone) {
            this.Phone = phone;
        }

        public void setName(String Name){
            this.Name = Name;
        }
        public String getName(){
            return this.Name;
        }
        public void setGender(String Gender){
            this.Gender = Gender;
        }
        public String getGender(){
            return this.Gender;
        }
        public void setLastLoginDate(long LastLoginDate){
            this.LastLoginDate = LastLoginDate;
        }
        public long getLastLoginDate(){
            return this.LastLoginDate;
        }
        public void setAccessToken(String AccessToken){
            this.AccessToken = AccessToken;
        }
        public String getAccessToken(){
            return this.AccessToken;
        }
        public void setTokenType(String TokenType){
            this.TokenType = TokenType;
        }
        public String getTokenType(){
            return this.TokenType;
        }
        public void setExpiresIn(long ExpiresIn){
            this.ExpiresIn = ExpiresIn;
        }
        public long getExpiresIn(){
            return this.ExpiresIn;
        }
        public void setIssued(long Issued){
            this.Issued = Issued;
        }
        public long getIssued(){
            return this.Issued;
        }
        public void setExpires(long Expires){
            this.Expires = Expires;
        }
        public long getExpires(){
            return this.Expires;
        }

}
