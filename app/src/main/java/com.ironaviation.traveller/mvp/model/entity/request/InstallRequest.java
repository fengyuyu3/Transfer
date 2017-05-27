package com.ironaviation.traveller.mvp.model.entity.request;

/**
 * Created by Administrator on 2017/5/26.
 */

public class InstallRequest {

    private String ClientId;
    private String code;

    public String getClientId() {
        return ClientId;
    }

    public void setClientId(String clientId) {
        ClientId = clientId;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }
}
