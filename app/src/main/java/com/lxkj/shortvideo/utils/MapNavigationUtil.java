package com.lxkj.shortvideo.utils;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.Uri;


import com.amap.api.maps.model.LatLng;

import java.util.List;

/**
 * Created by kxn on 2019/3/5 0005.
 * 地图导航工具类
 */
public class MapNavigationUtil {
    private Context mContext;

    public MapNavigationUtil(Context mContext) {
        this.mContext = mContext;
    }

    /**
     * 检测程序是否安装
     *
     * @param packageName
     * @return
     */
    private boolean isInstalled(String packageName) {
        PackageManager manager = mContext.getPackageManager();
        //获取所有已安装程序的包信息
        List<PackageInfo> installedPackages = manager.getInstalledPackages(0);
        if (installedPackages != null) {
            for (PackageInfo info : installedPackages) {
                if (info.packageName.equals(packageName))
                    return true;
            }
        }
        return false;
    }

    /**
     * 跳转百度地图
     */
    public void goToBaiduMap(String mLat,String mLng,String mAddressStr) {
        if (!isInstalled("com.baidu.BaiduMap")) {
            ToastUtil.show("请先安装百度地图客户端");
            return;
        }
        Intent intent = new Intent();
        intent.setData(Uri.parse("baidumap://map/direction?destination=latlng:"
                + mLat + ","
                + mLng + "|name:" + mAddressStr + // 终点
                "&mode=driving" + // 导航路线方式
                "&src=" + mContext.getPackageName()));
        mContext.startActivity(intent); // 启动调用
    }

    /**
     * 跳转高德地图
     */
    public void goToGaodeMap(String mLat,String mLng,String mAddressStr) {
        if (!isInstalled("com.autonavi.minimap")) {
            ToastUtil.show("请先安装高德地图客户端");
            return;
        }
        LatLng endPoint = BD2GCJ(new LatLng(Double.parseDouble(mLat), Double.parseDouble(mLng)));//坐标转换
        StringBuffer stringBuffer = new StringBuffer("androidamap://navi?sourceApplication=").append("amap");
        stringBuffer.append("&lat=").append(endPoint.latitude)
                .append("&lon=").append(endPoint.longitude).append("&keywords=" + mAddressStr)
                .append("&dev=").append(0)
                .append("&style=").append(2);
        Intent intent = new Intent("android.intent.action.VIEW", Uri.parse(stringBuffer.toString()));
        intent.setPackage("com.autonavi.minimap");
        mContext.startActivity(intent);
    }

    /**
     * BD-09 坐标转换成 GCJ-02 坐标
     */
    public static LatLng BD2GCJ(LatLng bd) {
        double x = bd.longitude - 0.0065, y = bd.latitude - 0.006;
        double z = Math.sqrt(x * x + y * y) - 0.00002 * Math.sin(y * Math.PI);
        double theta = Math.atan2(y, x) - 0.000003 * Math.cos(x * Math.PI);

        double lng = z * Math.cos(theta);//lng
        double lat = z * Math.sin(theta);//lat
        return new LatLng(lat, lng);
    }

    /**
     * GCJ-02 坐标转换成 BD-09 坐标
     */
    public static LatLng GCJ2BD(LatLng bd) {
        double x = bd.longitude, y = bd.latitude;
        double z = Math.sqrt(x * x + y * y) + 0.00002 * Math.sin(y * Math.PI);
        double theta = Math.atan2(y, x) + 0.000003 * Math.cos(x * Math.PI);
        double tempLon = z * Math.cos(theta) + 0.0065;
        double tempLat = z * Math.sin(theta) + 0.006;
        return new LatLng(tempLat, tempLon);
    }


}
