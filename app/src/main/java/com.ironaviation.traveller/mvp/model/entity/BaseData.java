package com.ironaviation.traveller.mvp.model.entity;

import java.io.Serializable;

import com.ironaviation.traveller.mvp.model.api.Api;

/**
 * 如果你服务器返回的数据固定为这种方式(字段名可根据服务器更改)
 * 替换范型即可重用BaseJson
 * Created by jess on 26/09/2016 15:19
 * Contact with jess.yan.effort@gmail.com
 */

public class BaseData<T> implements Serializable {
    private T data;
    private int Status;
    private int ServerStatus;
    private String Message;
    private String Url;

    public T getData() {
        return data;
    }

    public int getStatus() {
        return Status;
    }

    public String getMsg() {
        return Message;
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
