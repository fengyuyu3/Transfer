package com.ironaviation.traveller.mvp.model.entity;


import com.baidu.trace.T;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;

import java.io.Serializable;

/**
 * 项目名称：Traveller
 * 类描述：
 * 创建人：starRing
 * 创建时间：2017-04-14 20:25
 * 修改人：starRing
 * 修改时间：2017-04-14 20:25
 * 修改备注：
 */
public class Ext<T> implements Serializable {
    private String Name;

    private T Data;

    private String JsonData;

    public void setJsonData(String data) {
        this.JsonData = data;
    }

    public void setName(String Name) {
        this.Name = Name;
    }

    public String getName() {
        return this.Name;
    }

    public void setData(T Data) {
        this.Data = Data;
    }

    public T getData() {
        return this.Data;
    }

    public String getJsonData() {
        return this.JsonData;
    }

}