package com.ironaviation.traveller.mvp.model.entity.response;

import java.util.List;

/**
 * Created by Administrator on 2017/4/12 0012.
 */

public class RouteListResponse {

    /**
     * Items : [{"BID":"string","UID":"string","SeqNum":0,"OrderNo":"string","Channel":"string","PickupAddress":"string","DestAddress":"string","SeatNum":0,"ActualPrice":0,"TotalPrice":0,"Status":"string","IsDeleted":true,"TripType":"string","PickupTime":"2017-04-12T12:42:15.947Z","Cdt":"2017-04-12T12:42:15.947Z","IsComment":true,"IsPaied":true}]
     * TotalItemCount : 0
     * PageSize : 0
     * TotalPageCount : 0
     * CurrentPageIndex : 0
     */

    private int TotalItemCount;
    private int PageSize;
    private int TotalPageCount;
    private int CurrentPageIndex;
    private List<RouteItemResponse> Items;

    public int getTotalItemCount() {
        return TotalItemCount;
    }

    public void setTotalItemCount(int TotalItemCount) {
        this.TotalItemCount = TotalItemCount;
    }

    public int getPageSize() {
        return PageSize;
    }

    public void setPageSize(int PageSize) {
        this.PageSize = PageSize;
    }

    public int getTotalPageCount() {
        return TotalPageCount;
    }

    public void setTotalPageCount(int TotalPageCount) {
        this.TotalPageCount = TotalPageCount;
    }

    public int getCurrentPageIndex() {
        return CurrentPageIndex;
    }

    public void setCurrentPageIndex(int CurrentPageIndex) {
        this.CurrentPageIndex = CurrentPageIndex;
    }

    public List<RouteItemResponse> getItems() {
        return Items;
    }

    public void setItems(List<RouteItemResponse> Items) {
        this.Items = Items;
    }

    @Override
    public String toString() {
        return "RouteListResponse{" +
                "TotalItemCount=" + TotalItemCount +
                ", PageSize=" + PageSize +
                ", TotalPageCount=" + TotalPageCount +
                ", CurrentPageIndex=" + CurrentPageIndex +
                ", Items=" + Items +
                '}';
    }
}
