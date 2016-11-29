package com.sypm.shuyuzhongbao;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
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
import com.amap.api.maps.model.Marker;
import com.amap.api.maps.model.MarkerOptions;
import com.amap.api.maps.model.MyLocationStyle;
import com.amap.api.maps.model.Polyline;
import com.amap.api.maps.model.PolylineOptions;
import com.sypm.shuyuzhongbao.api.RetrofitClient;
import com.sypm.shuyuzhongbao.data.DataResult;
import com.sypm.shuyuzhongbao.data.MoneyList;
import com.sypm.shuyuzhongbao.data.Order;
import com.sypm.shuyuzhongbao.utils.BaseActivity;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * 订单详情
 */

public class OrderDetailActivity extends BaseActivity implements LocationSource, AMapLocationListener {

    private MoneyList.ListBean moneyList;
    Order order;
    Order orderByShipSn;
    Order ordering;
    TextView shipSn, name, phone, address, storeName, fee, feeWay;
    Button customerReject, dispatchingDone;
    String phoneNumber;

    /*高德地图*/
    //显示地图需要的变量
    private MapView mapView;//地图控件
    private AMap aMap;//地图对象

    //定位需要的声明
    private AMapLocationClient mLocationClient = null;//定位发起端
    private AMapLocationClientOption mLocationOption = null;//定位参数
    private OnLocationChangedListener mListener = null;//定位监听器

    //标识，用于判断是否只显示一次定位信息和用户重新定位
    private boolean isFirstLoc = true;

    String WD, JD;//纬度，经度
    private ArrayList<LatLng> latLngList = new ArrayList<>();
    private Polyline polyline;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_detail);

        shipSn = (TextView) findViewById(R.id.shipSn);
        name = (TextView) findViewById(R.id.name);
        phone = (TextView) findViewById(R.id.phone);
        address = (TextView) findViewById(R.id.address);
        fee = (TextView) findViewById(R.id.fee);
        storeName = (TextView) findViewById(R.id.storeName);
        feeWay = (TextView) findViewById(R.id.feeWay);

        customerReject = (Button) findViewById(R.id.customerReject);
        dispatchingDone = (Button) findViewById(R.id.dispatchingDone);
        ordering = (Order) getIntent().getSerializableExtra("ordering");
        phone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_DIAL);
                Uri data = Uri.parse("tel:" + phoneNumber);
                intent.setData(data);
                startActivity(intent);
            }
        });
        if (ordering != null) {
            WD = ordering.list.lat;
            JD = ordering.list.lng;
            initDataFromIndex();
        }

        moneyList = (MoneyList.ListBean) getIntent().getSerializableExtra("item");
        if (moneyList != null) {
            Log.d("根据订单号获取订单详情", moneyList.shipSn);
            initOrderData();
        }
        initData();

        //显示地图
        mapView = (MapView) findViewById(R.id.mapView);
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

    private void initDataFromIndex() {
        shipSn.setText("单号：" + ordering.list.orderSn);
        name.setText("姓名：" + ordering.list.name);
        address.setText("地址：" + ordering.list.address);
        phone.setText("电话：" + ordering.list.mobile);
        fee.setText("订单金额：" + ordering.list.amount);
        feeWay.setText("支付方式：" + ordering.list.payCode);
        storeName.setText("门店名称：" + ordering.list.storeName);
    }

    private void initOrderData() {
        /*根据收入列表传输过来的shipSn获取订单详情*/
        Call<Order> getOrderDetail = RetrofitClient.getInstance().getSYService().getOrderDetail(moneyList.shipSn);
        getOrderDetail.enqueue(new Callback<Order>() {
            @Override
            public void onResponse(Call<Order> call, Response<Order> response) {
                if (response.body() != null) {
                    if (response.body().status == 1) {
                        orderByShipSn = response.body();
                        shipSn.setText("单号：" + orderByShipSn.list.orderSn);
                        name.setText("姓名：" + orderByShipSn.list.name);
                        address.setText("地址：" + orderByShipSn.list.address);
                        phone.setText("电话：" + orderByShipSn.list.mobile);
                        fee.setText("订单金额：" + orderByShipSn.list.amount);
                        feeWay.setText("支付方式：" + orderByShipSn.list.payCode);
                        storeName.setText("门店名称：" + orderByShipSn.list.storeName);
                        phoneNumber = orderByShipSn.list.mobile;
                        WD = orderByShipSn.list.lat;
                        JD = orderByShipSn.list.lng;
                        if (orderByShipSn.list.status != 1) {
                            customerReject.setVisibility(View.INVISIBLE);
                            dispatchingDone.setVisibility(View.INVISIBLE);
                        }
                    } else {

                    }
                }
            }

            @Override
            public void onFailure(Call<Order> call, Throwable t) {

            }
        });
    }

    private void initData() {

        /*现在执行订单*/
        Call<Order> callCurrentOrder = RetrofitClient.getInstance().getSYService().getCurrentOrder();
        callCurrentOrder.enqueue(new Callback<Order>() {
            @Override
            public void onResponse(Call<Order> call, Response<Order> response) {
                if (response.body() != null) {
                    if (response.body().status == 1) {
                        order = response.body();
                        shipSn.setText("单号：" + order.list.orderSn);
                        name.setText("姓名：" + order.list.name);
                        address.setText("地址：" + order.list.address);
                        phone.setText("电话：" + order.list.mobile);
//                        fee.setText("订单金额：" + orderByShipSn.list.amount);
//                        feeWay.setText("支付方式：" + orderByShipSn.list.payCode);
//                        storeName.setText("门店名称：" + orderByShipSn.list.storeName);
                        phoneNumber = order.list.mobile;
                        if (order.list.status != 1) {
                            customerReject.setVisibility(View.INVISIBLE);
                            dispatchingDone.setVisibility(View.INVISIBLE);
                        }
                    } else {

                    }
                }
            }

            @Override
            public void onFailure(Call<Order> call, Throwable t) {

            }
        });

        /*客户拒单*/
        customerReject.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Call<DataResult> orderReject = RetrofitClient.getInstance().getSYService().orderReject(order.list.orderSn);
                orderReject.enqueue(new Callback<DataResult>() {
                    @Override
                    public void onResponse(Call<DataResult> call, Response<DataResult> response) {
                        if (response.body() != null) {
                            if (response.body().status.equals("1")) {
                                Toast.makeText(getActivity(), "客户拒单提交成功", Toast.LENGTH_LONG).show();
                                finish();
                            } else {
                                Toast.makeText(getActivity(), "提交失败", Toast.LENGTH_LONG).show();
                            }
                        }
                    }

                    @Override
                    public void onFailure(Call<DataResult> call, Throwable t) {
                        Toast.makeText(getActivity(), "客户拒单操作失败", Toast.LENGTH_LONG).show();
                    }
                });
            }
        });

        /*完成配送*/
        dispatchingDone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Call<DataResult> orderFinish = RetrofitClient.getInstance().getSYService().orderFinish(order.list.orderSn);
                orderFinish.enqueue(new Callback<DataResult>() {
                    @Override
                    public void onResponse(Call<DataResult> call, Response<DataResult> response) {
                        if (response.body() != null) {
                            if (response.body().status.equals("1")) {
                                Toast.makeText(getActivity(), "提交成功", Toast.LENGTH_LONG).show();
                                finish();
                            } else {
                                Toast.makeText(getActivity(), "提价失败", Toast.LENGTH_LONG).show();
                            }
                        }
                    }

                    @Override
                    public void onFailure(Call<DataResult> call, Throwable t) {
                        Toast.makeText(getActivity(), "完成配送操作失败", Toast.LENGTH_LONG).show();
                    }
                });
            }
        });
    }

    /*-----------------高德地图相关-----------------*/

    //定位
    private void initLoc() {
        //初始化定位
        mLocationClient = new AMapLocationClient(getApplicationContext());
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
                // 如果不设置标志位，此时再拖动地图时，它会不断将地图移动到当前的位置
                if (isFirstLoc) {
                    //将地图移动到定位点
//                    aMap.moveCamera(CameraUpdateFactory.changeLatLng(new LatLng(amapLocation.getLatitude(), amapLocation.getLongitude())));
                    //点击定位按钮 能够将地图的中心移动到定位点
                    mListener.onLocationChanged(amapLocation);
                    //获取定位信息
                    StringBuffer buffer = new StringBuffer();
//                    Toast.makeText(getApplicationContext(), buffer.toString(), Toast.LENGTH_LONG).show();
                    Log.d("定位信息", buffer.toString());
                    isFirstLoc = false;
                    //设置缩放级别
                    aMap.moveCamera(CameraUpdateFactory.zoomTo(13));

                    LatLng latLng = new LatLng(Double.valueOf(WD), Double.valueOf(JD));
                    Log.d("配送点", WD + JD);
                    //将地图移动到配送点
                    aMap.moveCamera(CameraUpdateFactory.changeLatLng(new LatLng(Double.valueOf(WD), Double.valueOf(JD))));
//                    latLngList.add(new LatLng(amapLocation.getLatitude(), amapLocation.getLongitude()));
//                    latLngList.add(new LatLng(Double.valueOf(WD), Double.valueOf(JD)));
                    final Marker marker = aMap.addMarker(new MarkerOptions().position(latLng).title("配送点").snippet("DefaultMarker"));
//                    polyline = aMap.addPolyline(new PolylineOptions().addAll(latLngList).color(R.color.orange).width(5));

                }


            } else {
                //显示错误信息ErrCode是错误码，errInfo是错误信息，详见错误码表。
                Log.e("AmapError", "location Error, ErrCode:" + amapLocation.getErrorCode() + ", errInfo:" + amapLocation.getErrorInfo());
                Toast.makeText(getApplicationContext(), "定位失败", Toast.LENGTH_LONG).show();
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

    /**
     * 方法必须重写
     */
    @Override
    protected void onResume() {
        super.onResume();
        mapView.onResume();
    }

    /**
     * 方法必须重写
     */
    @Override
    protected void onPause() {
        super.onPause();
        mapView.onPause();
    }

    /**
     * 方法必须重写
     */
    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        mapView.onSaveInstanceState(outState);
    }

    /**
     * 方法必须重写
     */
    @Override
    protected void onDestroy() {
        super.onDestroy();
        mapView.onDestroy();
    }
}

