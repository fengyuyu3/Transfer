package com.ironaviation.traveller.mvp.model.entity;


import com.google.gson.JsonObject;

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
public class Ext implements Serializable {
    private String Name;

    private JsonObject Data;

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

    public void setData(JsonObject Data) {
        this.Data = Data;
    }

    public JsonObject getData() {
        return this.Data;
    }

    public String getJsonData() {
        return this.JsonData;
    }

}