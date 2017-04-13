package com.ironaviation.traveller.mvp.model.entity.request;

/**
 * Created by Administrator on 2017/4/12 0012.
 */

public class RouteListMoreRequest {
    private int PageIndex;
    private int PageSize;
    private boolean IsPaged;

    public int getPageIndex() {
        return PageIndex;
    }

    public void setPageIndex(int pageIndex) {
        PageIndex = pageIndex;
    }

    public int getPageSize() {
        return PageSize;
    }

    public void setPageSize(int pageSize) {
        PageSize = pageSize;
    }

    public boolean isPaged() {
        return IsPaged;
    }

    public void setPaged(boolean paged) {
        IsPaged = paged;
    }

    @Override
    public String toString() {
        return "RouteListMoreRequest{" +
                "PageIndex=" + PageIndex +
                ", PageSize=" + PageSize +
                ", IsPaged=" + IsPaged +
                '}';
    }
}
