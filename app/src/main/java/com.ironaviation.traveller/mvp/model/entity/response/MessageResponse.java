package com.ironaviation.traveller.mvp.model.entity.response;

import java.util.List;

/**
 * 项目名称：Transfer
 * 类描述：我的消息返回类
 * 创建人：flq
 * 创建时间：2017/3/26 18:08
 * 修改人：
 * 修改时间：2017/3/26 18:08
 * 修改备注：
 */

public class MessageResponse {


    private List<Items> Items;

    private int TotalItemCount;

    private int PageSize;

    private int TotalPageCount;

    private int CurrentPageIndex;

    public void setItems(List<Items> Items) {
        this.Items = Items;
    }

    public List<Items> getItems() {
        return this.Items;
    }

    public void setTotalItemCount(int TotalItemCount) {
        this.TotalItemCount = TotalItemCount;
    }

    public int getTotalItemCount() {
        return this.TotalItemCount;
    }

    public void setPageSize(int PageSize) {
        this.PageSize = PageSize;
    }

    public int getPageSize() {
        return this.PageSize;
    }

    public void setTotalPageCount(int TotalPageCount) {
        this.TotalPageCount = TotalPageCount;
    }

    public int getTotalPageCount() {
        return this.TotalPageCount;
    }

    public void setCurrentPageIndex(int CurrentPageIndex) {
        this.CurrentPageIndex = CurrentPageIndex;
    }

    public int getCurrentPageIndex() {
        return this.CurrentPageIndex;
    }

    public class Items {
        private String Title;

        private String Contents;

        private String Data;

        private int MsgType;

        private String SendTime;

        public void setTitle(String Title) {
            this.Title = Title;
        }

        public String getTitle() {
            return this.Title;
        }

        public void setContents(String Contents) {
            this.Contents = Contents;
        }

        public String getContents() {
            return this.Contents;
        }

        public void setData(String Data) {
            this.Data = Data;
        }

        public String getData() {
            return this.Data;
        }

        public void setMsgType(int MsgType) {
            this.MsgType = MsgType;
        }

        public int getMsgType() {
            return this.MsgType;
        }

        public void setSendTime(String SendTime) {
            this.SendTime = SendTime;
        }

        public String getSendTime() {
            return this.SendTime;
        }

    }
}
