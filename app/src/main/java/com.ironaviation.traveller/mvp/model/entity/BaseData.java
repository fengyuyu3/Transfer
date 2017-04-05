package com.ironaviation.traveller.mvp.model.entity;

import java.io.Serializable;
import java.util.List;

import com.ironaviation.traveller.mvp.model.api.Api;

/**
 * 如果你服务器返回的数据固定为这种方式(字段名可根据服务器更改)
 * 替换范型即可重用BaseJson
 * Created by jess on 26/09/2016 15:19
 * Contact with jess.yan.effort@gmail.com
 */

public class BaseData<T> implements Serializable {

    private T Data;
    private String ServerStatus;
    private int Status;
    private String Message;
    private String Url;
    private List<BaseError> Errors;

    public T getData() {
        return Data;
    }

    public void setData(T data) {
        Data = data;
    }

    public String getUrl() {
        return Url;
    }

    public void setUrl(String url) {
        Url = url;
    }

    public List<BaseError> getErrors() {
        return Errors;
    }

    public void setErrors(List<BaseError> errors) {
        Errors = errors;
    }

    public String getServerStatus() {
        return ServerStatus;
    }

    public void setServerStatus(String serverStatus) {
        ServerStatus = serverStatus;
    }

    public int getStatus() {
        return Status;
    }

    public void setStatus(int status) {
        Status = status;
    }

    public String getMessage() {
        return Message;
    }

    public void setMessage(String message) {
        Message = message;
    }

    /**
     * 请求是否成功
     *
     * @return
     */
    public boolean isSuccess() {
        if (Status == Api.RequestSuccess) {
            return true;
        } else {
            return false;
        }
    }
}
