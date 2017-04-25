package com.ironaviation.traveller.app.utils;

import com.baidu.mapapi.map.Overlay;
import com.baidu.mapapi.model.LatLng;
import com.baidu.mapapi.utils.CoordinateConverter;
import com.baidu.trace.model.CoordType;

/**
 * Created by baidu on 17/2/9.
 */

public class MapUtil {

    private static MapUtil INSTANCE = new MapUtil();

    private MapUtil() {
    }

    public static MapUtil getInstance() {
        return INSTANCE;
    }


    /**
     * 将轨迹实时定位点转换为地图坐标
     *
     * @param location
     *
     * @return
   /*  *//*
    public static LatLng convertTraceLocation2Map(TraceLocation location) {
        if (null == location) {
            return null;
        }
        double latitude = location.getLatitude();
        double longitude = location.getLongitude();
        if (Math.abs(latitude - 0.0) < 0.000001 && Math.abs(longitude - 0.0) < 0.000001) {
            return null;
        }
        LatLng currentLatLng = new LatLng(latitude, longitude);
        if (CoordType.wgs84 == location.getCoordType()) {
            LatLng sourceLatLng = currentLatLng;
            CoordinateConverter converter = new CoordinateConverter();
            converter.from(CoordinateConverter.CoordType.GPS);
            converter.coord(sourceLatLng);
            currentLatLng = converter.convert();
        }
        return currentLatLng;
    }
    *//**
     * 将地图坐标转换轨迹坐标
     *
     * @param latLng
     *
     * @return
     *//*
    public static com.baidu.trace.model.LatLng convertMap2Trace(LatLng latLng) {
        return new com.baidu.trace.model.LatLng(latLng.latitude, latLng.longitude);
    }

    *//**
     * 将轨迹坐标对象转换为地图坐标对象
     *
     * @param traceLatLng
     *
     * @return
     *//*
    public static LatLng convertTrace2Map(com.baidu.trace.model.LatLng traceLatLng) {
        return new LatLng(traceLatLng.latitude, traceLatLng.longitude);
    }
*/
}

