package com.onemenu.server.util;

/**
 * 地图计算相关的方法
 * 
 * @author lin
 *
 */
public class MapUtils {

    /**
     * google maps的脚本里代码
     */
    public static final double EARTH_RADIUS = 6378.137;
    //1 kilometre(km)公里 = 0.621 mile英里
    public static final double mileOfKm = 0.621;

    private static double rad(double d) {
        return d * Math.PI / 180.0;
    }

    /**
     * 根据两点间经纬度坐标（double值），计算两点间距离，单位为km
     * modify by file 单位改成mile
     */
    public static double getDistance(double lat1, double lng1, double lat2, double lng2) {
        double radLat1 = rad(lat1);
        double radLat2 = rad(lat2);
        double a = radLat1 - radLat2;
        double b = rad(lng1) - rad(lng2);
        double s =
                2 * Math.asin(Math.sqrt(Math.pow(Math.sin(a / 2), 2) + Math.cos(radLat1)
                        * Math.cos(radLat2) * Math.pow(Math.sin(b / 2), 2)));
        s = s * EARTH_RADIUS;
        s = Math.round(s * 100) / 100.0;
        return s * mileOfKm;
    }



}
