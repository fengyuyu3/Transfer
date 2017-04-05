package com.ironaviation.traveller.mvp.model.entity;

import java.util.List;

/**
 * 项目名称：Transfer
 * 类描述：
 * 创建人：flq
 * 创建时间：2017/4/5 9:51
 * 修改人：
 * 修改时间：2017/4/5 9:51
 * 修改备注：
 */

public class BaseError {

    private String Field;
    private List<String> ErrorMessages;

    public String getField() {
        return Field;
    }

    public void setField(String Field) {
        this.Field = Field;
    }

    public List<String> getErrorMessages() {
        return ErrorMessages;
    }

    public void setErrorMessages(List<String> ErrorMessages) {
        this.ErrorMessages = ErrorMessages;
    }
}
