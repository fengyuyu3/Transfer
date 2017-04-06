package com.ironaviation.traveller.mvp.model.entity.response;

/**
 * 项目名称：Traveller
 * 类描述：
 * 创建人：starRing
 * 创建时间：2017-03-31 11:02
 * 修改人：starRing
 * 修改时间：2017-03-31 11:02
 * 修改备注：
 */
public class CancelSuccessResponse {
    private String name;
    private boolean type;

    public CancelSuccessResponse(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public boolean isType() {
        return type;
    }

    public void setType(boolean type) {
        this.type = type;
    }


}
