package com.ironaviation.traveller.mvp.model.entity.response;

import java.util.List;

/**
 * 项目名称：Traveller
 * 类描述：
 * 创建人：starRing
 * 创建时间：2017-04-25 18:58
 * 修改人：starRing
 * 修改时间：2017-04-25 18:58
 * 修改备注：
 */
public class CommentsResponse {
    private String Notes;

    private int Rate;

    private List<CommentTag> Tags;

    public void setNotes(String Notes) {
        this.Notes = Notes;
    }

    public String getNotes() {
        return this.Notes;
    }

    public void setRate(int Rate) {
        this.Rate = Rate;
    }

    public int getRate() {
        return this.Rate;
    }

    public void setTags(List<CommentTag> Tags) {
        this.Tags = Tags;
    }

    public List<CommentTag> getTags() {
        return this.Tags;
    }

    public class Tags {
        private String CTID;

        private int Rate;

        private String Tag;

        private boolean IsEnabled;

        private int Cdt;

        private String Cby;

        public void setCTID(String CTID) {
            this.CTID = CTID;
        }

        public String getCTID() {
            return this.CTID;
        }

        public void setRate(int Rate) {
            this.Rate = Rate;
        }

        public int getRate() {
            return this.Rate;
        }

        public void setTag(String Tag) {
            this.Tag = Tag;
        }

        public String getTag() {
            return this.Tag;
        }

        public void setIsEnabled(boolean IsEnabled) {
            this.IsEnabled = IsEnabled;
        }

        public boolean getIsEnabled() {
            return this.IsEnabled;
        }

        public void setCdt(int Cdt) {
            this.Cdt = Cdt;
        }

        public int getCdt() {
            return this.Cdt;
        }

        public void setCby(String Cby) {
            this.Cby = Cby;
        }

        public String getCby() {
            return this.Cby;
        }

    }


}
