package com.ironaviation.traveller.mvp.model.entity.response;

/**
 * Created by Administrator on 2017/5/10.
 */

public class AddressResponse {

    private boolean isValid;
    private String Message;

    public boolean isValid() {
        return isValid;
    }

    public void setValid(boolean valid) {
        isValid = valid;
    }

    public String getMessage() {
        return Message;
    }

    public void setMessage(String message) {
        Message = message;
    }
}
