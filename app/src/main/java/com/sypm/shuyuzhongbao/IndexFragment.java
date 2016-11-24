package com.sypm.shuyuzhongbao;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Toast;

import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationClient;
import com.amap.api.location.AMapLocationClientOption;
import com.amap.api.location.AMapLocationListener;
import com.amap.api.maps.AMap;
import com.amap.api.maps.CameraUpdateFactory;
import com.amap.api.maps.LocationSource;
import com.amap.api.maps.MapView;
import com.amap.api.maps.UiSettings;
import com.amap.api.maps.model.BitmapDescriptorFactory;
import com.amap.api.maps.model.LatLng;
import com.amap.api.maps.model.MyLocationStyle;
import com.sypm.shuyuzhongbao.api.RetrofitClient;
import com.sypm.shuyuzhongbao.data.DataResult;
import com.sypm.shuyuzhongbao.utils.BaseFragment;
import com.sypm.shuyuzhongbao.utils.MyBaseAdapter;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * 首页
 * 所有操作都是建立在已将上线的基础上
 * 上线后每隔20秒获取一次是否有<指派未接受的订单><获取现在执行的订单>并且上传<配送员位置信息><在线时长>
 * 如果有<指派未接受的订单>的话跳转到<接单界面>进行接单或者拒绝操作
 * 如接单跳转到<订单详情界面>
 * 订单配送完成访问<客户拒单接口>或<完成配送接口>
 * 如拒绝接单访问<拒绝单子接口>
 * 如果有<已经接单的订单>的话跳转到<订单详情界面>
 * 订单配送完成访问<客户拒单接口>或<完成配送接口>
 */

public class IndexFragment extends BaseFragment implements LocationSource, AMapLocationListener {

    ListView listView;
    LinearLayout linearLayout;
    private int recLen = 0;

    /*高德start*/
    //显示地图需要的变量
    private MapView mapView;//地图控件
    private AMap aMap;//地图对象

    //定位需要的声明
    private AMapLocationClient mLocationClient = null;//定位发起端
    private AMapLocationClientOption mLocationOption = null;//定位参数
    private OnLocationChangedListener mListener = null;//定位监听器

    //标识，用于判断是否只显示一次定位信息和用户重新定位
    private boolean isFirstLoc = true;
    /*高德end*/

    Handler handler = new Handler();
    Runnable runnable = new Runnable() {
        @Override
        public void run() {
            recLen++;
            updateInfo();
            //每隔20秒获取一次订单
            handler.postDelayed(this, 20000);
        }
    };

    private void updateInfo() {
        /*未指派订单*/
        Call<DataResult> getOrder = RetrofitClient.getInstance().getSYService().getOrder();
        getOrder.enqueue(new Callback<DataResult>() {
            @Override
            public void onResponse(Call<DataResult> call, Response<DataResult> response) {
                if (response.body() != null) {
                    if (response.body().status.equals("1")) {
                        /*跳转到接单界面*/
                        Intent intent = new Intent(getActivity(), GrabOrderActivity.class);
                        startActivity(intent);
                    } else {

                    }
                }
            }

            @Override
            public void onFailure(Call<DataResult> call, Throwable t) {

            }
        });
        /*现在执行订单*/
        Call<DataResult> callCurrentOrder = RetrofitClient.getInstance().getSYService().getCurrentOrder();
        callCurrentOrder.enqueue(new Callback<DataResult>() {
            @Override
            public void onResponse(Call<DataResult> call, Response<DataResult> response) {
                if (response.body() != null) {
                    if (response.body().status.equals("1")) {
                        /*跳转到订单详情界面*/
                        /*Intent intent = new Intent(getActivity(), OrderDetailActivity.class);
                        startActivity(intent);*/
                    } else {

                    }
                }
            }

            @Override
            public void onFailure(Call<DataResult> call, Throwable t) {

            }
        });
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        //允许刷新按钮
        setHasOptionsMenu(true);
        super.onCreate(savedInstanceState);
        runnable.run();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_index, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        setupListView();
        initData();
        //显示地图
        mapView = (MapView) getView().findViewById(R.id.mapView);
        //必须要写
        mapView.onCreate(savedInstanceState);
        //获取地图对象
        aMap = mapView.getMap();

        //设置显示定位按钮 并且可以点击
        UiSettings settings = aMap.getUiSettings();
        //设置定位监听
        aMap.setLocationSource(this);
        // 是否显示定位按钮
        settings.setMyLocationButtonEnabled(true);
        // 是否可触发定位并显示定位层
        aMap.setMyLocationEnabled(true);

        //定位的小图标 默认是蓝点 这里自定义一箭头，其实就是一张图片
        MyLocationStyle myLocationStyle = new MyLocationStyle();
        myLocationStyle.myLocationIcon(BitmapDescriptorFactory.fromResource(R.drawable.location_marker));//已更改
        myLocationStyle.radiusFillColor(android.R.color.transparent);
        myLocationStyle.strokeColor(android.R.color.transparent);
        aMap.setMyLocationStyle(myLocationStyle);

        //开始定位
        initLoc();
    }

    private void initData() {
        /*在线时长等信息(是否需要实时获取)*/
        Call<DataResult> summary = RetrofitClient.getInstance().getSYService().summary();
        summary.enqueue(new Callback<DataResult>() {
            @Override
            public void onResponse(Call<DataResult> call, Response<DataResult> response) {

            }

            @Override
            public void onFailure(Call<DataResult> call, Throwable t) {

            }
        });

        /*首页消息列表*/
        Call<DataResult> call = RetrofitClient.getInstance().getSYService().messageList("1");
        call.enqueue(new Callback<DataResult>() {
            @Override
            public void onResponse(Call<DataResult> call, Response<DataResult> response) {

            }

            @Override
            public void onFailure(Call<DataResult> call, Throwable t) {

            }
        });
    }

    private void setupListView() {
        List list = new ArrayList();
        for (int i = 0; i < 5; i++) {
            list.add(i);
        }
        listView = (ListView) getView().findViewById(R.id.listView);
        listView.setAdapter(new ListAdapter(getContext(), list));
        linearLayout = (LinearLayout) getView().findViewById(R.id.of);
        linearLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                /*上线按钮点击事件*/

                Intent intent = new Intent(getActivity(), GrabOrderActivity.class);
                startActivity(intent);
            }
        });
    }

    public static class ListAdapter extends MyBaseAdapter {

        public ListAdapter(Context context, List list) {
            super(context, list);
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            ViewHolder holder;
            if (convertView == null) {
                convertView = mInflater.inflate(R.layout.item_pay_list, parent, false);
                holder = new ViewHolder();
                convertView.setTag(holder);
            } else {
                holder = (ViewHolder) convertView.getTag();
            }

            return convertView;
        }
    }

    private static class ViewHolder {

    }

    /*高德地图相关类*/

    //定位
    private void initLoc() {
        //初始化定位
        mLocationClient = new AMapLocationClient(getContext());//getApplicationContext
        //设置定位回调监听
        mLocationClient.setLocationListener(this);
        //初始化定位参数
        mLocationOption = new AMapLocationClientOption();
        //设置定位模式为高精度模式，Battery_Saving为低功耗模式，Device_Sensors是仅设备模式
        mLocationOption.setLocationMode(AMapLocationClientOption.AMapLocationMode.Hight_Accuracy);
        //设置是否返回地址信息（默认返回地址信息）
        mLocationOption.setNeedAddress(true);
        //设置是否只定位一次,默认为false
        mLocationOption.setOnceLocation(false);
        //设置是否强制刷新WIFI，默认为强制刷新
        mLocationOption.setWifiActiveScan(true);
        //设置是否允许模拟位置,默认为false，不允许模拟位置
        mLocationOption.setMockEnable(false);
        //设置定位间隔,单位毫秒,默认为2000ms
        mLocationOption.setInterval(2000);
        //给定位客户端对象设置定位参数
        mLocationClient.setLocationOption(mLocationOption);
        //启动定位
        mLocationClient.startLocation();
    }

    @Override
    public void onLocationChanged(AMapLocation amapLocation) {
        if (amapLocation != null) {
            if (amapLocation.getErrorCode() == 0) {

                //定位成功回调信息，设置相关消息
                amapLocation.getLocationType();//获取当前定位结果来源，如网络定位结果，详见官方定位类型表
                amapLocation.getLatitude();//获取纬度
                amapLocation.getLongitude();//获取经度
                amapLocation.getAccuracy();//获取精度信息
                SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                Date date = new Date(amapLocation.getTime());
                df.format(date);//定位时间
                amapLocation.getAddress();//地址，如果option中设置isNeedAddress为false，则没有此结果，网络定位结果中会有地址信息，GPS定位不返回地址信息。
                amapLocation.getCountry();//国家信息
                amapLocation.getProvince();//省信息
                amapLocation.getCity();//城市信息
                amapLocation.getDistrict();//城区信息
                amapLocation.getStreet();//街道信息
                amapLocation.getStreetNum();//街道门牌号信息
                amapLocation.getCityCode();//城市编码
                amapLocation.getAdCode();//地区编码

                // 如果不设置标志位，此时再拖动地图时，它会不断将地图移动到当前的位置
                if (isFirstLoc) {
                    //将地图移动到定位点
                    aMap.moveCamera(CameraUpdateFactory.changeLatLng(new LatLng(amapLocation.getLatitude(), amapLocation.getLongitude())));
                    //点击定位按钮 能够将地图的中心移动到定位点
                    mListener.onLocationChanged(amapLocation);
                    //获取定位信息
                    StringBuffer buffer = new StringBuffer();
                    buffer.append(df.format(date) + " 定位类型：" + amapLocation.getLocationType() + " 纬度：" + amapLocation.getLatitude() + " 经度：" + amapLocation.getLongitude() + " 精度：" + amapLocation.getAccuracy() + " 城市编码：" + amapLocation.getCityCode() + " 国家：" + amapLocation.getCountry() + " 省份：" + amapLocation.getProvince() + "     " + amapLocation.getProvince() + "" + amapLocation.getCity() + "" + amapLocation.getDistrict() + "" + amapLocation.getStreet() + "" + amapLocation.getStreetNum());
                    Toast.makeText(getContext(), buffer.toString(), Toast.LENGTH_LONG).show();//getApplicationContext
                    Log.d("定位信息", buffer.toString());
                    isFirstLoc = false;

                    //设置缩放级别
                    aMap.moveCamera(CameraUpdateFactory.zoomTo(15));
                }


            } else {
                //显示错误信息ErrCode是错误码，errInfo是错误信息，详见错误码表。
                Log.e("AmapError", "location Error, ErrCode:" + amapLocation.getErrorCode() + ", errInfo:" + amapLocation.getErrorInfo());
                Toast.makeText(getContext(), "定位失败", Toast.LENGTH_LONG).show();//getApplicationContext
            }
        }
    }

    @Override
    public void activate(OnLocationChangedListener onLocationChangedListener) {
        mListener = onLocationChangedListener;
    }

    @Override
    public void deactivate() {
        mListener = null;
    }

}
