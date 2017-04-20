package com.ironaviation.traveller.mvp.model.entity.response;

import java.util.List;

/**
 * Created by Administrator on 2017/4/17.
 */

public class CommentsInfo {

    /**
     * BID : string
     * DID : string
     * Rate : 0
     * Notes : string
     * TagIds : ["string"]
     */

    private String BID;
    private String DID;
    private int Rate;
    private String Notes;
    private List<String> TagIds;

    public String getBID() {
        return BID;
    }

    public void setBID(String BID) {
        this.BID = BID;
    }

    public String getDID() {
        return DID;
    }

    public void setDID(String DID) {
        this.DID = DID;
    }

    public int getRate() {
        return Rate;
    }

    public void setRate(int Rate) {
        this.Rate = Rate;
    }

    public String getNotes() {
        return Notes;
    }

    public void setNotes(String Notes) {
        this.Notes = Notes;
    }

    public List<String> getTagIds() {
        return TagIds;
    }

    public void setTagIds(List<String> TagIds) {
        this.TagIds = TagIds;
    }
}
