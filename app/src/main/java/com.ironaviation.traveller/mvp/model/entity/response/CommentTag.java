package com.ironaviation.traveller.mvp.model.entity.response;

/**
 * Created by Administrator on 2017/4/17.
 */

public class CommentTag {

    /**
     * CTID : string
     * Rate : 0
     * Tag : string
     * IsEnabled : true
     */

    private String CTID;
    private int Rate;
    private String Tag;
    private boolean IsEnabled;

    public String getCTID() {
        return CTID;
    }

    public void setCTID(String CTID) {
        this.CTID = CTID;
    }

    public int getRate() {
        return Rate;
    }

    public void setRate(int Rate) {
        this.Rate = Rate;
    }

    public String getTag() {
        return Tag;
    }

    public void setTag(String Tag) {
        this.Tag = Tag;
    }

    public boolean isIsEnabled() {
        return IsEnabled;
    }

    public void setIsEnabled(boolean IsEnabled) {
        this.IsEnabled = IsEnabled;
    }
}
