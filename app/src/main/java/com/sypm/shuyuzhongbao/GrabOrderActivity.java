package com.sypm.shuyuzhongbao;

import android.content.DialogInterface;
import android.content.Intent;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.RingtoneManager;
import android.media.SoundPool;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
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

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * 接单
 */

public class GrabOrderActivity extends BaseActivity implements LocationSource, AMapLocationListener {

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

    TextView shipSn, name, phone, address, timer, reject;
    LinearLayout accept;
    Order order;
    Order orderComing;
    final MediaPlayer mp = new MediaPlayer();

    CountDownTimer countDownTimer = new CountDownTimer(60000, 1000) {
        @Override
        public void onTick(long millisUntilFinished) {
            timer.setText("倒计时" + (millisUntilFinished / 1000));
        }

        @Override
        public void onFinish() {
            try {
                mp.setDataSource(getActivity(), RingtoneManager.getDefaultUri(RingtoneManager.TYPE_RINGTONE));
                mp.prepare();
            } catch (Exception e) {
                e.printStackTrace();
            }
            /*设置倒计时完毕时自动接单*/
            Call<DataResult> orderSure = RetrofitClient.getInstance().getSYService().orderSure(order.list.orderSn);
            orderSure.enqueue(new Callback<DataResult>() {
                @Override
                public void onResponse(Call<DataResult> call, Response<DataResult> response) {
                    if (response.body() != null) {
                        Log.d("自动接单状态", response.body().status);
                        if (response.body().status.equals("1")) {
                            SimpleDateFormat formatter = new SimpleDateFormat("yyyy年MM月dd日 HH:mm:ss ");
                            Date curDate = new Date(System.currentTimeMillis());
                            String str = formatter.format(curDate);
                            Log.d("自动接单成功", order.list.orderSn);
                            timer.setText("已默认接单" + str);
                            accept.setVisibility(View.INVISIBLE);
                            reject.setVisibility(View.INVISIBLE);
                            mp.start();
                            Intent intent = new Intent();
                            setResult(RESULT_OK, intent);
                        } else {
                            Log.d("自动接单失败", order.list.orderSn);
                            Toast.makeText(getActivity(), "自动接单失败或您已接单", Toast.LENGTH_LONG).show();
                        }
                    }
                }

                @Override
                public void onFailure(Call<DataResult> call, Throwable t) {
                    Toast.makeText(getActivity(), "自动接单操作失败", Toast.LENGTH_LONG).show();
                }
            });
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dialog);

        shipSn = (TextView) findViewById(R.id.shipSn);
        name = (TextView) findViewById(R.id.name);
        phone = (TextView) findViewById(R.id.phone);
        address = (TextView) findViewById(R.id.address);
        timer = (TextView) findViewById(R.id.timer);
        reject = (TextView) findViewById(R.id.reject);
        accept = (LinearLayout) findViewById(R.id.accept);
        orderComing = (Order) getIntent().getSerializableExtra("orderComing");

        if (orderComing != null) {
            WD = orderComing.list.lat;
            JD = orderComing.list.lng;
            initDataFromIndex();
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
//        shipSn.setText("单号：" + orderComing.list.orderSn);
//        name.setText("姓名：" + orderComing.list.name);
//        address.setText("地址：" + orderComing.list.address);
//        phone.setText("电话：" + orderComing.list.mobile);
    }

    private void initData() {
        /*未指派订单*/
        Call<Order> getOrder = RetrofitClient.getInstance().getSYService().getOrder();
        getOrder.enqueue(new Callback<Order>() {
            @Override
            public void onResponse(Call<Order> call, Response<Order> response) {
                if (response.body() != null) {
                    if (response.body().status == 1) {
//                        Toast.makeText(getApplicationContext(), "获取订单成功", Toast.LENGTH_LONG).show();
                        countDownTimer.start();
                        order = response.body();
                        shipSn.setText("单号：" + order.list.orderSn);
                        name.setText("姓名：" + order.list.name);
                        address.setText("地址：" + order.list.address);
                        phone.setText("电话：" + order.list.mobile);
                    } else {
                        Toast.makeText(getApplicationContext(), "订单无数据", Toast.LENGTH_LONG).show();
                    }
                }
            }

            @Override
            public void onFailure(Call<Order> call, Throwable t) {
                Toast.makeText(getApplicationContext(), "获取订单失败", Toast.LENGTH_LONG).show();
            }
        });

        /*接单*/
        accept.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Call<DataResult> orderSure = RetrofitClient.getInstance().getSYService().orderSure(order.list.orderSn);
                orderSure.enqueue(new Callback<DataResult>() {
                    @Override
                    public void onResponse(Call<DataResult> call, Response<DataResult> response) {
                        if (response.body() != null) {
                            if (response.body().status.equals("1")) {
                                countDownTimer.cancel();
                                Toast.makeText(getActivity(), "接单成功！", Toast.LENGTH_LONG).show();
//                                Intent intent = new Intent(getActivity(), OrderDetailActivity.class);
//                                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//                                startActivity(intent);
                                Intent intent = new Intent();
                                setResult(RESULT_OK, intent);
                                finish();
                            } else {
                                Toast.makeText(getActivity(), "接单失败", Toast.LENGTH_LONG).show();
                            }
                        }
                    }

                    @Override
                    public void onFailure(Call<DataResult> call, Throwable t) {
                        Toast.makeText(getActivity(), "接单操作失败", Toast.LENGTH_LONG).show();
                    }
                });
            }
        });

        /*拒单*/
        reject.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Call<DataResult> orderCancel = RetrofitClient.getInstance().getSYService().orderCancel(order.list.orderSn);
                orderCancel.enqueue(new Callback<DataResult>() {
                    @Override
                    public void onResponse(Call<DataResult> call, Response<DataResult> response) {
                        if (response.body() != null) {
                            if (response.body().status.equals("1")) {
                                Toast.makeText(getApplicationContext(), "拒单成功", Toast.LENGTH_LONG).show();
                                accept.setVisibility(View.INVISIBLE);
                                countDownTimer.cancel();
                                setResult(RESULT_OK);
                                finish();
                            } else {
                                Toast.makeText(getApplicationContext(), "拒单失败", Toast.LENGTH_LONG).show();
                            }
                        }
                    }

                    @Override
                    public void onFailure(Call<DataResult> call, Throwable t) {
                        Toast.makeText(getApplicationContext(), "拒单操作失败", Toast.LENGTH_LONG).show();
                    }
                });
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        new AlertDialog.Builder(getActivity())
                .setTitle("提示")
                .setMessage("确定要返回吗")
                .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                })
                .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        setResult(RESULT_OK);
                        countDownTimer.cancel();
                        mp.stop();
                        finish();
                    }
                }).show();
    }

    //定位
    private void initLoc() {
        //初始化定位
        mLocationClient = new AMapLocationClient(getApplicationContext());
        //设置定位回调监听
        mLocationClient.setLocationListener(this);
        //初始化定位参数
        mLocationOption = new AMapLocationClientOption();
        //设置定位模式为高精度模式，Battery_Saving为低功耗模式，Device_Sensors是仅设备模式
        mLocationOption.setLocationMode(AMapLocationClientOption.AMapLocationMode.Battery_Saving);
        //设置是否返回地址信息（默认返回地址信息）
        mLocationOption.setNeedAddress(true);
        //设置是否只定位一次,默认为false
        mLocationOption.setOnceLocation(false);
        //设置是否强制刷新WIFI，默认为强制刷新
        mLocationOption.setWifiActiveScan(true);
        //设置是否允许模拟位置,默认为false，不允许模拟位置
        mLocationOption.setMockEnable(false);
        //设置定位间隔,单位毫秒,默认为2000ms
        mLocationOption.setInterval(30000);
        //给定位客户端对象设置定位参数
        mLocationClient.setLocationOption(mLocationOption);
        //启动定位
        mLocationClient.startLocation();
    }

    @Override
    public void onLocationChanged(AMapLocation amapLocation) {
        if (amapLocation != null) {
            if (amapLocation.getErrorCode() == 0) {
                if (isFirstLoc) {
                    mListener.onLocationChanged(amapLocation);
                    StringBuffer buffer = new StringBuffer();
                    Log.d("定位信息", buffer.toString());
                    isFirstLoc = false;
                    aMap.moveCamera(CameraUpdateFactory.zoomTo(13));
                    LatLng latLng = new LatLng(Double.valueOf(WD), Double.valueOf(JD));
                    Log.d("抢单界面配送点", WD + JD);
                    aMap.moveCamera(CameraUpdateFactory.changeLatLng(new LatLng(Double.valueOf(WD), Double.valueOf(JD))));
                    final Marker marker = aMap.addMarker(new MarkerOptions().position(latLng).title("配送点").snippet("DefaultMarker"));
                }
            } else {
                //显示错误信息ErrCode是错误码，errInfo是错误信息，详见错误码表。
                /*Log.e("AmapError", "location Error, ErrCode:" + amapLocation.getErrorCode() + ", errInfo:" + amapLocation.getErrorInfo());
                Toast.makeText(getApplicationContext(), "定位失败", Toast.LENGTH_LONG).show();*/
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
