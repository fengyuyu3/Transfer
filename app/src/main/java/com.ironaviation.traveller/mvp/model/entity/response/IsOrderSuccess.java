package com.ironaviation.traveller.mvp.model.entity.response;

/**
 * Created by Administrator on 2017/4/14 0014.
 */

public class IsOrderSuccess {

    private boolean isSuccess;

    public boolean isSuccess() {
        return isSuccess;
    }

    public void setSuccess(boolean success) {
        isSuccess = success;
    }

    @Override
    public String toString() {
        return "IsOrderSuccess{" +
                "isSuccess=" + isSuccess +
                '}';
    }
}
