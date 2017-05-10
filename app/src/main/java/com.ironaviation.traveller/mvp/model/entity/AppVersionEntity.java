package com.ironaviation.traveller.mvp.model.entity;

/**
 * 项目名称：Transfer
 * 类描述：
 * 创建人：starRing
 * 创建时间：2017-03-10 16:36
 * 修改人：starRing
 * 修改时间：2017-03-10 16:36
 * 修改备注：
 */
public class AppVersionEntity {


    private String DownLoadUrl;

    private String Version;

    private String Description;

    private boolean IsCoerceUpdate;

    private String Notification;

    private int FileSize;

    private int AppType;

    private int OSType;

    private long ReleaseTime;


    private int VersionNum;

    public void setDownLoadUrl(String DownLoadUrl) {
        this.DownLoadUrl = DownLoadUrl;
    }

    public String getDownLoadUrl() {
        return this.DownLoadUrl;
    }

    public void setVersion(String Version) {
        this.Version = Version;
    }

    public String getVersion() {
        return this.Version;
    }

    public void setDescription(String Description) {
        this.Description = Description;
    }

    public String getDescription() {
        return this.Description;
    }

    public void setIsCoerceUpdate(boolean IsCoerceUpdate) {
        this.IsCoerceUpdate = IsCoerceUpdate;
    }

    public boolean getIsCoerceUpdate() {
        return this.IsCoerceUpdate;
    }

    public void setNotification(String Notification) {
        this.Notification = Notification;
    }

    public String getNotification() {
        return this.Notification;
    }

    public void setFileSize(int FileSize) {
        this.FileSize = FileSize;
    }

    public int getFileSize() {
        return this.FileSize;
    }

    public void setAppType(int AppType) {
        this.AppType = AppType;
    }

    public int getAppType() {
        return this.AppType;
    }

    public void setOSType(int OSType) {
        this.OSType = OSType;
    }

    public int getOSType() {
        return this.OSType;
    }

    public void setReleaseTime(long ReleaseTime) {
        this.ReleaseTime = ReleaseTime;
    }

    public long getReleaseTime() {
        return this.ReleaseTime;
    }
    public int getVersionNum() {
        return VersionNum;
    }

    public void setVersionInt(int VersionNum) {
        VersionNum = VersionNum;
    }


}
