package com.ironaviation.traveller.mvp.model.entity.request;

/**
 * 项目名称：Traveller
 * 类描述：
 * 创建人：starRing
 * 创建时间：2017-04-10 20:37
 * 修改人：starRing
 * 修改时间：2017-04-10 20:37
 * 修改备注：
 */
public class UpdateAddressBookRequest {
    private String UABID;

    private String AddressName;

    private String Address;

    private double Longitude;

    private double Latitude;

    private boolean IsActive;

    private boolean IsDeleted;

    private String DetailAddress;
    public int getViewType() {
        return viewType;
    }

    public void setViewType(int viewType) {
        this.viewType = viewType;
    }

    private int viewType;//划分是否可以右滑

    public void setUABID(String UABID) {
        this.UABID = UABID;
    }

    public String getUABID() {
        return this.UABID;
    }

    public void setAddressName(String AddressName) {
        this.AddressName = AddressName;
    }

    public String getAddressName() {
        return this.AddressName;
    }

    public void setAddress(String Address) {
        this.Address = Address;
    }

    public String getAddress() {
        return this.Address;
    }

    public void setLongitude(double Longitude) {
        this.Longitude = Longitude;
    }

    public double getLongitude() {
        return this.Longitude;
    }

    public void setLatitude(double Latitude) {
        this.Latitude = Latitude;
    }

    public double getLatitude() {
        return this.Latitude;
    }

    public void setIsActive(boolean IsActive) {
        this.IsActive = IsActive;
    }

    public boolean getIsActive() {
        return this.IsActive;
    }

    public void setIsDeleted(boolean IsDeleted) {
        this.IsDeleted = IsDeleted;
    }

    public boolean getIsDeleted() {
        return this.IsDeleted;
    }

    public String getDetailAddress() {
        return DetailAddress;
    }

    public void setDetailAddress(String detailAddress) {
        DetailAddress = detailAddress;
    }

}
