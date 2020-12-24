package com.lxkj.shortvideo.ui.fragment.map;

import android.Manifest;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.OvershootInterpolator;
import android.view.animation.TranslateAnimation;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;

import androidx.annotation.Nullable;

import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationClient;
import com.amap.api.location.AMapLocationClientOption;
import com.amap.api.location.AMapLocationListener;
import com.amap.api.maps.AMap;
import com.amap.api.maps.CameraUpdateFactory;
import com.amap.api.maps.MapView;
import com.amap.api.maps.model.CameraPosition;
import com.amap.api.maps.model.LatLng;
import com.amap.api.services.core.LatLonPoint;
import com.amap.api.services.core.PoiItem;
import com.amap.api.services.geocoder.GeocodeResult;
import com.amap.api.services.geocoder.GeocodeSearch;
import com.amap.api.services.geocoder.RegeocodeQuery;
import com.amap.api.services.geocoder.RegeocodeResult;
import com.lxkj.shortvideo.AppConsts;
import com.lxkj.shortvideo.R;
import com.lxkj.shortvideo.biz.ActivitySwitcher;
import com.lxkj.shortvideo.ui.activity.NaviActivity;
import com.lxkj.shortvideo.ui.fragment.TitleFragment;
import com.lxkj.shortvideo.ui.fragment.map.adapter.PoiAdapter;
import com.lxkj.shortvideo.utils.ToastUtil;
import com.zhy.m.permission.MPermissions;
import com.zhy.m.permission.PermissionDenied;
import com.zhy.m.permission.PermissionGrant;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * Created by kxn on 2018/8/27 0027.
 * 选择地址
 */

public class SelectAddressFra extends TitleFragment implements NaviActivity.NaviRigthImageListener, GeocodeSearch.OnGeocodeSearchListener {
    @BindView(R.id.mapView)
    MapView mapView;
    Unbinder unbinder;
    AMapLocationClientOption mLocationOption = null;
    AMap aMap;
    @BindView(R.id.iv_center)
    ImageView ivCenter;
    TranslateAnimation animation;
    GeocodeSearch geocoderSearch; //逆地理编码
    AMapLocationClient mLocationClient = null;
    @BindView(R.id.lvPoi)
    ListView lvPoi;
    @BindView(R.id.ivToLocation)
    ImageView ivToLocation;
    private String province,city,district,address;

    private List<PoiItem> poiItems = new ArrayList<PoiItem>();
    private PoiAdapter poiAdapter;

    LatLng selectLatLng, currentLatLng;

    @Override
    public String getTitleName() {
        return "选择地址";
    }


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        act.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
        rootView = inflater.inflate(R.layout.fra_map_select_address, container, false);
        unbinder = ButterKnife.bind(this, rootView);
        mapView.onCreate(savedInstanceState);// 此方法必须重写

        aMap = mapView.getMap();
        aMap.getUiSettings().setZoomControlsEnabled(false);
        initView();
        return rootView;
    }

    @Override
    public void onResume() {
        super.onResume();
        //在activity执行onResume时执行mMapView.onResume ()，重新绘制加载地图
        mapView.onResume();
    }
    @Override
    public void onPause() {
        super.onPause();
        //在activity执行onPause时执行mMapView.onPause ()，暂停地图的绘制
        mapView.onPause();
    }
    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        //在activity执行onSaveInstanceState时执行mMapView.onSaveInstanceState (outState)，保存地图当前的状态
        mapView.onSaveInstanceState(outState);
    }

    private void initView() {
        EventBus.getDefault().register(this);
        LatLng location = new LatLng(34.75, 113.70);
        aMap.moveCamera(CameraUpdateFactory.newLatLngZoom(location, 10));
        aMap.setOnCameraChangeListener(new AMap.OnCameraChangeListener() {
            @Override
            public void onCameraChange(CameraPosition cameraPosition) {
            }

            @Override
            public void onCameraChangeFinish(CameraPosition cameraPosition) {
                ivCenter.startAnimation(animation);
                CameraPosition p = aMap.getCameraPosition();
                lat = p.target.latitude + "";
                lng = p.target.longitude + "";
                LatLonPoint latLonPoint = new LatLonPoint(p.target.latitude, p.target.longitude);
                selectLatLng = p.target;
                // 第一个参数表示一个Latlng，第二参数表示范围多少米，第三个参数表示是火系坐标系还是GPS原生坐标系
                RegeocodeQuery query = new RegeocodeQuery(latLonPoint, 500, GeocodeSearch.AMAP);
                geocoderSearch.getFromLocationAsyn(query);
            }
        });

        mLocationClient = new AMapLocationClient(mContext);
        mLocationClient.setLocationListener(mLocationListener);
        //初始化AMapLocationClientOption对象
        mLocationOption = new AMapLocationClientOption();
        //设置定位模式为AMapLocationMode.Battery_Saving，低功耗模式。
        mLocationOption.setLocationMode(AMapLocationClientOption.AMapLocationMode.Battery_Saving);
        //获取一次定位结果：
        //该方法默认为false。
        mLocationOption.setOnceLocation(true);
        //给定位客户端对象设置定位参数
        mLocationClient.setLocationOption(mLocationOption);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            MPermissions.requestPermissions(this, AppConsts.PMS_LOCATION,
                    Manifest.permission.ACCESS_COARSE_LOCATION
            );
        } else {
            pmsLocationSuccess();
        }

        animation = new TranslateAnimation(0, 0, 0, -10);
        animation.setInterpolator(new OvershootInterpolator());
        animation.setDuration(200);
        animation.setRepeatCount(1);
        animation.setRepeatMode(Animation.REVERSE);

        geocoderSearch = new GeocodeSearch(act);
        geocoderSearch.setOnGeocodeSearchListener(this);


        poiAdapter = new PoiAdapter(mContext, poiItems);
        lvPoi.setAdapter(poiAdapter);


        lvPoi.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                PoiItem item = poiItems.get(i);
//                Intent intent = new Intent();
//                intent.putExtra("lat", item.getLatLonPoint().getLatitude() + "");
//                intent.putExtra("lng", item.getLatLonPoint().getLongitude() + "");
//                intent.putExtra("address", item.getSnippet());
//                intent.putExtra("province", province);
//                intent.putExtra("city", city);
//                intent.putExtra("town", town);
//                intent.putExtra("province_city_town", province + city + town);
//                act.setResult(1, intent);
//                act.finishSelf();
                act.finishSelf();
            }
        });

        ivToLocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                aMap.moveCamera(CameraUpdateFactory.newLatLngZoom(currentLatLng, 17));
            }
        });
    }




    @PermissionGrant(AppConsts.PMS_LOCATION)
    public void pmsLocationSuccess() {
        //权限授权成功
        mLocationClient.startLocation();
    }

    @PermissionDenied(AppConsts.PMS_LOCATION)
    public void pmsLocationError() {
        ToastUtil.show("权限被拒绝，无法使用该功能！");
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        MPermissions.onRequestPermissionsResult(this, requestCode, permissions, grantResults);
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }


    /**
     * 定位监听
     */
    //声明定位回调监听器
    AMapLocationListener mLocationListener = new AMapLocationListener() {
        @Override
        public void onLocationChanged(AMapLocation amapLocation) {
            if (amapLocation != null) {
                if (amapLocation.getErrorCode() == 0) {
                    lat = amapLocation.getLatitude() + "";
                    lng = amapLocation.getLongitude() + "";

                    final LatLng location = new LatLng(amapLocation.getLatitude(), amapLocation.getLongitude());
                    currentLatLng = location;
                    aMap.moveCamera(CameraUpdateFactory.newLatLngZoom(location, 17));
                } else {
                    if (amapLocation.getErrorCode() == 12 || amapLocation.getErrorCode() == 13) {
                        ToastUtil.show("定位失败！");
                    }
                }
            }
        }
    };


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        mapView.onDestroy();
        unbinder.unbind();
        EventBus.getDefault().unregister(this);
    }

    @Override
    public void onRegeocodeSearched(RegeocodeResult regeocodeResult, int i) {
        AppConsts.province = regeocodeResult.getRegeocodeAddress().getProvince();
        AppConsts.city = regeocodeResult.getRegeocodeAddress().getCity();
        province = regeocodeResult.getRegeocodeAddress().getProvince();
        city = regeocodeResult.getRegeocodeAddress().getCity();
        district = regeocodeResult.getRegeocodeAddress().getDistrict();
        if (i == 1000) {
            poiItems.clear();
            poiItems.addAll(regeocodeResult.getRegeocodeAddress().getPois());
            poiAdapter.notifyDataSetChanged();
        }
    }

    @Override
    public void onGeocodeSearched(GeocodeResult geocodeResult, int i) {
    }

    @Override
    public int rightImg() {
        return R.mipmap.ic_search_map;
    }

    @Override
    public void onRightClicked(View v) {
        ActivitySwitcher.startFragment(act, SearchAddressFra.class);
    }


}
