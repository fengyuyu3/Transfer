package com.ironaviation.traveller.mvp.model.entity;

import android.os.Parcel;
import android.os.Parcelable;

import com.baidu.mapapi.search.core.PoiInfo;

import java.io.Serializable;
import java.util.List;


/**
 * 项目名称：Traveller
 * 类描述：
 * 创建人：starRing
 * 创建时间：2017-04-07 16:55
 * 修改人：starRing
 * 修改时间：2017-04-07 16:55
 * 修改备注：
 */
public class AddressHistory implements Serializable {
    private List<PoiInfo>mPoiInfos;

    public List<PoiInfo> getPoiInfos() {
        return mPoiInfos;
    }

    public void setPoiInfos(List<PoiInfo> poiInfos) {
        mPoiInfos = poiInfos;
    }
}
