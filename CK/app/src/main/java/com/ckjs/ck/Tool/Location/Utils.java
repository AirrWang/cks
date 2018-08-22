package com.ckjs.ck.Tool.Location;

import com.amap.api.location.AMapLocationClientOption;

/**
 * Created by NiPing and Airr Wang
 * Copyright  www.chaokongs.com. All rights reserved.
 */
public class Utils {


    public static AMapLocationClientOption initLocation() {
        //声明mLocationOption对象 //初始化定位参数

        AMapLocationClientOption mLocationOption = new AMapLocationClientOption();

        //设置返回地址信息，默认为true
        mLocationOption.setNeedAddress(true);

        //设置定位模式为高精度模式，Battery_Saving为低功耗模式，Device_Sensors是仅设备模式
        mLocationOption.setLocationMode(AMapLocationClientOption.AMapLocationMode.Hight_Accuracy);
        //设置是否只定位一次,默认为false
        mLocationOption.setOnceLocation(true);
        return mLocationOption;
    }

    public static AMapLocationClientOption initLocationMore(int n) {
        //声明mLocationOption对象   //初始化定位参数
        AMapLocationClientOption mLocationOption = new AMapLocationClientOption();

        //设置返回地址信息，默认为true
        mLocationOption.setNeedAddress(true);
        //Hight_Accuracy设置定位模式为高精度模式，Battery_Saving为低功耗模式，Device_Sensors是仅设备模式
        mLocationOption.setLocationMode(AMapLocationClientOption.AMapLocationMode.Device_Sensors);
        //设置定位间隔,单位毫秒,默认为2000ms
        mLocationOption.setInterval(n);
        //设置定位参数
        // 此方法为每隔固定时间会发起一次定位请求，为了减少电量消耗或网络流量消耗，
        // 注意设置合适的定位时间的间隔（最小间隔支持为2000ms），并且在合适时间调用stopLocation()方法来取消定位请求
        // 在定位结束后，在合适的生命周期调用onDestroy()方法
        // 在单次定位情况下，定位无论成功与否，都无需调用stopLocation()方法移除请求，定位sdk内部会移除
        return mLocationOption;

    }
}
