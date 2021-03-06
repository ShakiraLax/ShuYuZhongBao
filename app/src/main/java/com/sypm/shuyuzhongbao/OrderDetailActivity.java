package com.sypm.shuyuzhongbao;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
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
import com.amap.api.maps.overlay.WalkRouteOverlay;
import com.amap.api.services.core.LatLonPoint;
import com.amap.api.services.route.BusRouteResult;
import com.amap.api.services.route.DriveRouteResult;
import com.amap.api.services.route.RideRouteResult;
import com.amap.api.services.route.RouteSearch;
import com.amap.api.services.route.WalkPath;
import com.amap.api.services.route.WalkRouteResult;
import com.sypm.shuyuzhongbao.api.RetrofitClient;
import com.sypm.shuyuzhongbao.data.DataResult;
import com.sypm.shuyuzhongbao.data.MoneyList;
import com.sypm.shuyuzhongbao.data.OrderBySn;
import com.sypm.shuyuzhongbao.utils.BaseActivity;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.R.attr.mode;

/**
 * 订单详情
 */

public class OrderDetailActivity extends BaseActivity implements LocationSource, AMapLocationListener, RouteSearch.OnRouteSearchListener {

    private MoneyList.ListBean moneyList;
    private OrderBySn orderByShipSn;
    private OrderBySn orderByIndex;
    private TextView shipSn, name, phone, address, storeName, amount, feeWay, txt_orderStatus_detail, isGet;
    private Button customerReject, dispatchingDone;
    private String phoneNumber;


    //显示地图需要的变量
    private MapView mapView;//地图控件
    private AMap aMap;//地图对象
    //定位需要的声明
    private AMapLocationClient mLocationClient = null;//定位发起端
    private AMapLocationClientOption mLocationOption = null;//定位参数
    private OnLocationChangedListener mListener = null;//定位监听器
    //标识，用于判断是否只显示一次定位信息和用户重新定位
    private boolean isFirstLoc = true;
    private String WD, JD;
    private RouteSearch mRouteSearch;
    private WalkRouteResult mWalkRouteResult;
    private TextView txtNote;
    private String shipSnFromFirstPage;
    private String shipSnFromGrab;
    private String shipSnFromJP;
    private String SHIPSN;
    private LinearLayout layoutOfOrderAndGoods, goods;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_detail);

        shipSn = (TextView) findViewById(R.id.shipSn);
        name = (TextView) findViewById(R.id.name);
        phone = (TextView) findViewById(R.id.phone);
        address = (TextView) findViewById(R.id.address);
        amount = (TextView) findViewById(R.id.amount);
        storeName = (TextView) findViewById(R.id.storeName);
        feeWay = (TextView) findViewById(R.id.feeWay);
        txtNote = (TextView) findViewById(R.id.txt_note);
        txt_orderStatus_detail = (TextView) findViewById(R.id.txt_orderStatus_detail);
        isGet = (TextView) findViewById(R.id.isGet);
        customerReject = (Button) findViewById(R.id.customerReject);
        dispatchingDone = (Button) findViewById(R.id.dispatchingDone);
        layoutOfOrderAndGoods = (LinearLayout) findViewById(R.id.layoutOfOrderAndGoods);
        goods = (LinearLayout) findViewById(R.id.goods);

        mRouteSearch = new RouteSearch(this);

        phone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_DIAL);
                Uri data = Uri.parse("tel:" + phoneNumber);
                intent.setData(data);
                startActivity(intent);
            }
        });

        /*接单界面传过来的sn*/
        shipSnFromGrab = getIntent().getStringExtra("orderFromGrab");
        if (shipSnFromGrab != null) {
            Log.d("shipSnFromGrab", shipSnFromGrab);
            SHIPSN = shipSnFromGrab;
            setupOrderDetail();
        }

        /*流水列表传过来的sn*/
        moneyList = (MoneyList.ListBean) getIntent().getSerializableExtra("item");
        if (moneyList != null) {
            Log.d("根据订单号获取订单详情", moneyList.shipSn);
            initOrderData();
        }

        /*首页列表item传过来的sn*/
        shipSnFromFirstPage = getIntent().getStringExtra("shipSn");
        if (shipSnFromFirstPage != null) {
            Log.d("shipSnFromFirstPage", shipSnFromFirstPage);
            SHIPSN = shipSnFromFirstPage;
            setupOrderDetail();
        }

        /*极光推送传过来的sn*/
        shipSnFromJP = getIntent().getStringExtra("quhuo");
        if (shipSnFromJP != null) {
            Log.d("shipSnFromJP", shipSnFromJP);
            SHIPSN = shipSnFromJP;
            setupOrderDetail();
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
        //设置数据回调监听器
        mRouteSearch.setRouteSearchListener(this);
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

    //根据首页订单号展示订单详情包括商品信息
    private void setupOrderDetail() {
        final Call<OrderBySn> orderDetailCall = RetrofitClient.getInstance().getSYService().getOrderDetail(SHIPSN);
        orderDetailCall.enqueue(new Callback<OrderBySn>() {
            @Override
            public void onResponse(Call<OrderBySn> call, Response<OrderBySn> response) {
                if (response.body() != null) {
                    if (response.body().status == 1) {
                        orderByIndex = response.body();
                        WD = orderByIndex.list.lat;
                        JD = orderByIndex.list.lng;

                        address.setText("地址：" + orderByIndex.list.address);
                        name.setText("姓名：" + orderByIndex.list.name);
                        phone.setText("电话：" + orderByIndex.list.mobile);
                        shipSn.setText("单号：" + orderByIndex.list.orderSn);
                        amount.setText("订单金额：" + orderByIndex.list.amount);
                        feeWay.setText("支付方式：" + orderByIndex.list.payCode);
                        storeName.setText("门店名称：" + orderByIndex.list.storeName);
                        txtNote.setText("备注：" + orderByIndex.list.note);
                        txt_orderStatus_detail.setText("订单状态：" + orderByIndex.list.orderStatus);
                        if (orderByIndex.list.isGet == 1) {
                            isGet.setText("是否取货：" + "已取货");
                        } else {
                            isGet.setText("是否取货：" + "未取货");
                        }
                        phoneNumber = orderByIndex.list.mobile;
                        List<OrderBySn.DataBean.GoodsListBean> goodsList = orderByIndex.list.goodsList;
                        if (goodsList != null) {
                            goods.setVisibility(View.VISIBLE);
                            for (int i = 0; i < orderByIndex.list.goodsList.size(); i++) {
                                TextView goodsTitle = new TextView(getApplicationContext());
                                goodsTitle.setTextColor(0xff000000);
                                goodsTitle.setTextSize(14);
                                goodsTitle.setLines(2);
                                goodsTitle.setEllipsize(TextUtils.TruncateAt.MIDDLE);
                                //"商品编码：" +
                                TextView goodsSn = new TextView(getApplicationContext());
                                goodsSn.setText(goodsList.get(i).goodsSn);
                                goodsSn.setTextColor(0xff000000);
                                goodsSn.setTextSize(14);
                                //"商品数量："
                                TextView goodsNumber = new TextView(getApplicationContext());
                                goodsNumber.setText(goodsList.get(i).goodsNumber + "");
                                goodsNumber.setTextColor(0xff000000);
                                goodsNumber.setGravity(Gravity.CENTER);
                                goodsNumber.setTextSize(14);
                                //"商品优惠价格：" +
                                TextView preferentialPrice = new TextView(getApplicationContext());
                                preferentialPrice.setText(goodsList.get(i).preferentialPrice + "");
                                preferentialPrice.setTextColor(0xff000000);
                                preferentialPrice.setGravity(Gravity.CENTER);
                                preferentialPrice.setTextSize(14);
                                if (goodsList.get(i).isGift == 1) {
                                    goodsTitle.setText(goodsList.get(i).goodsTitle + "（赠品）");


                                } else {
                                    goodsTitle.setText(goodsList.get(i).goodsTitle);
                                }
                                LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(0, ViewGroup.LayoutParams.WRAP_CONTENT, 1.0f);
                                LinearLayout.LayoutParams paramsTitle = new LinearLayout.LayoutParams(0, ViewGroup.LayoutParams.WRAP_CONTENT, 3.0f);
                                params.setMargins(0, 10, 0, 10);
                                paramsTitle.setMargins(20, 10, 0, 10);
                                LinearLayout linearLayout = new LinearLayout(getApplicationContext());
                                linearLayout.setGravity(Gravity.HORIZONTAL_GRAVITY_MASK);
                                linearLayout.addView(goodsSn, params);
//                                linearLayout.addView(goodsSn, params);
                                linearLayout.addView(goodsTitle, paramsTitle);
                                linearLayout.addView(preferentialPrice, params);
                                linearLayout.addView(goodsNumber, params);
                                layoutOfOrderAndGoods.addView(linearLayout);
                            }
                        }
                        if (orderByIndex.list.status != 1 || !orderByIndex.list.orderStatus.equals("派送中")) {
                            customerReject.setVisibility(View.INVISIBLE);
                            dispatchingDone.setVisibility(View.INVISIBLE);
                        }
                    } else {
                        Toast.makeText(getActivity(), "此单已无数据", Toast.LENGTH_LONG).show();
                        customerReject.setVisibility(View.INVISIBLE);
                        dispatchingDone.setVisibility(View.INVISIBLE);
                    }
                }
            }

            @Override
            public void onFailure(Call<OrderBySn> call, Throwable t) {

            }
        });
    }

    /*根据收入列表传输过来的shipSn获取订单详情*/
    private void initOrderData() {
        Call<OrderBySn> call = RetrofitClient.getInstance().getSYService().getOrderDetail(moneyList.shipSn);
        call.enqueue(new Callback<OrderBySn>() {
            @Override
            public void onResponse(Call<OrderBySn> call, Response<OrderBySn> response) {
                if (response.body() != null) {
                    if (response.body().status == 1) {
//                        Toast.makeText(getActivity(), "成功" + moneyList.shipSn, Toast.LENGTH_LONG).show();
                        orderByShipSn = response.body();
                        WD = orderByShipSn.list.lat;
                        JD = orderByShipSn.list.lng;
                        address.setText("地址：" + orderByShipSn.list.address);
                        name.setText("姓名：" + orderByShipSn.list.name);
                        phone.setText("电话：" + orderByShipSn.list.mobile);
                        shipSn.setText("单号：" + orderByShipSn.list.orderSn);
                        amount.setText("订单金额：" + orderByShipSn.list.amount);
                        feeWay.setText("支付方式：" + orderByShipSn.list.payCode);
                        storeName.setText("门店名称：" + orderByShipSn.list.storeName);
                        txtNote.setText("备注：" + orderByShipSn.list.note);
                        txt_orderStatus_detail.setText("订单状态：" + orderByShipSn.list.orderStatus);
                        if (orderByShipSn.list.isGet == 1) {
                            isGet.setText("是否取货：" + "已取货");
                        } else {
                            isGet.setText("是否取货：" + "未取货");
                        }
                        phoneNumber = orderByShipSn.list.mobile;

                        List<OrderBySn.DataBean.GoodsListBean> goodsList = orderByShipSn.list.goodsList;
                        if (goodsList != null) {
                            goods.setVisibility(View.VISIBLE);
                            for (int i = 0; i < orderByShipSn.list.goodsList.size(); i++) {
                                TextView goodsTitle = new TextView(getApplicationContext());
                                goodsTitle.setTextColor(0xff000000);
                                goodsTitle.setTextSize(14);
                                goodsTitle.setLines(2);
                                goodsTitle.setEllipsize(TextUtils.TruncateAt.MIDDLE);
                                //"商品编码：" +
                                TextView goodsSn = new TextView(getApplicationContext());
                                goodsSn.setText(goodsList.get(i).goodsSn);
                                goodsSn.setTextColor(0xff000000);
                                goodsSn.setTextSize(14);
                                //"商品数量："
                                TextView goodsNumber = new TextView(getApplicationContext());
                                goodsNumber.setText(goodsList.get(i).goodsNumber + "");
                                goodsNumber.setTextColor(0xff000000);
                                goodsNumber.setGravity(Gravity.CENTER);
                                goodsNumber.setTextSize(14);
                                //"商品优惠价格：" +
                                TextView preferentialPrice = new TextView(getApplicationContext());
                                preferentialPrice.setText(goodsList.get(i).preferentialPrice + "");
                                preferentialPrice.setTextColor(0xff000000);
                                preferentialPrice.setGravity(Gravity.CENTER);
                                preferentialPrice.setTextSize(14);
                                if (goodsList.get(i).isGift == 1) {
                                    goodsTitle.setText(goodsList.get(i).goodsTitle + "（赠品）");

                                } else {
                                    goodsTitle.setText(goodsList.get(i).goodsTitle);
                                }
                                LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(0, ViewGroup.LayoutParams.WRAP_CONTENT, 1.0f);
                                LinearLayout.LayoutParams paramsTitle = new LinearLayout.LayoutParams(0, ViewGroup.LayoutParams.WRAP_CONTENT, 3.0f);
                                params.setMargins(0, 10, 0, 10);
                                paramsTitle.setMargins(20, 10, 0, 10);
                                LinearLayout linearLayout = new LinearLayout(getApplicationContext());
                                linearLayout.setGravity(Gravity.HORIZONTAL_GRAVITY_MASK);
                                linearLayout.addView(goodsSn, params);
//                                linearLayout.addView(goodsSn, params);
                                linearLayout.addView(goodsTitle, paramsTitle);
                                linearLayout.addView(preferentialPrice, params);
                                linearLayout.addView(goodsNumber, params);
                                layoutOfOrderAndGoods.addView(linearLayout);
                            }
                        }

                        if (orderByShipSn.list.status != 1) {
                            customerReject.setVisibility(View.INVISIBLE);
                            dispatchingDone.setVisibility(View.INVISIBLE);
                        }
                    } else {
                        Toast.makeText(getActivity(), "订单无数据", Toast.LENGTH_LONG).show();
                        customerReject.setVisibility(View.INVISIBLE);
                        dispatchingDone.setVisibility(View.INVISIBLE);
                    }

                } else {
                    Toast.makeText(getActivity(), "无数据", Toast.LENGTH_LONG).show();
                    customerReject.setVisibility(View.INVISIBLE);
                    dispatchingDone.setVisibility(View.INVISIBLE);
                }
            }

            @Override
            public void onFailure(Call<OrderBySn> call, Throwable t) {
                Toast.makeText(getActivity(), "获取失败", Toast.LENGTH_LONG).show();
                customerReject.setVisibility(View.INVISIBLE);
                dispatchingDone.setVisibility(View.INVISIBLE);
            }
        });
    }

    private void initData() {
        /*客户拒单*/
        customerReject.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Call<DataResult> orderReject = RetrofitClient.getInstance().getSYService().orderReject(SHIPSN);
                orderReject.enqueue(new Callback<DataResult>() {
                    @Override
                    public void onResponse(Call<DataResult> call, Response<DataResult> response) {
                        if (response.body() != null) {
                            if (response.body().status.equals("1")) {
                                Intent intent = new Intent(getActivity(), MainActivity.class);
                                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                                setResult(RESULT_OK);
                                Toast.makeText(getActivity(), "客户拒单提交成功", Toast.LENGTH_LONG).show();
                                startActivity(intent);
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
                Call<DataResult> orderFinish = RetrofitClient.getInstance().getSYService().orderFinish(SHIPSN);
                orderFinish.enqueue(new Callback<DataResult>() {
                    @Override
                    public void onResponse(Call<DataResult> call, Response<DataResult> response) {
                        if (response.body() != null) {
                            if (response.body().status.equals("1")) {
                                Intent intent = new Intent(getActivity(), MainActivity.class);
                                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                                setResult(RESULT_OK);
                                Toast.makeText(getActivity(), "提交成功", Toast.LENGTH_LONG).show();
                                startActivity(intent);
                                finish();
                            } else {
                                Toast.makeText(getActivity(), "提交失败", Toast.LENGTH_LONG).show();
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

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        setResult(RESULT_OK);
        finish();
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
                if (isFirstLoc) {
                    mListener.onLocationChanged(amapLocation);
                    StringBuffer buffer = new StringBuffer();
                    Log.d("第一次定位信息", buffer.toString());
                    aMap.moveCamera(CameraUpdateFactory.zoomTo(13));
                    LatLng latLng = new LatLng(Double.valueOf(WD), Double.valueOf(JD));
                    LatLonPoint endLatLonPoint = new LatLonPoint(Double.valueOf(WD), Double.valueOf(JD));
                    LatLonPoint startLatLonPoint = new LatLonPoint(amapLocation.getLatitude(), amapLocation.getLongitude());
//                    Log.d("配送点", WD + JD);
                    final RouteSearch.FromAndTo fromAndTo = new RouteSearch.FromAndTo(startLatLonPoint, endLatLonPoint);
//                    RouteSearch.RideRouteQuery query = new RouteSearch.RideRouteQuery(fromAndTo, mode);
                    RouteSearch.WalkRouteQuery query = new RouteSearch.WalkRouteQuery(fromAndTo, mode);
                    mRouteSearch.calculateWalkRouteAsyn(query);
                    aMap.moveCamera(CameraUpdateFactory.changeLatLng(new LatLng(Double.valueOf(WD), Double.valueOf(JD))));
                    final Marker marker = aMap.addMarker(new MarkerOptions().position(latLng).title("配送点").snippet("DefaultMarker"));
                    isFirstLoc = false;
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

    @Override
    public void onBusRouteSearched(BusRouteResult busRouteResult, int i) {

    }

    @Override
    public void onDriveRouteSearched(DriveRouteResult driveRouteResult, int i) {

    }

    @Override
    public void onWalkRouteSearched(WalkRouteResult result, int errorCode) {
        aMap.clear();// 清理地图上的所有覆盖物
        if (errorCode == 1000) {
            if (result != null && result.getPaths() != null) {
                if (result.getPaths().size() > 0) {
                    mWalkRouteResult = result;
                    final WalkPath walkPath = mWalkRouteResult.getPaths().get(0);
                    WalkRouteOverlay walkRouteOverlay = new WalkRouteOverlay(this, aMap, walkPath, mWalkRouteResult.getStartPos(), mWalkRouteResult.getTargetPos());
                    walkRouteOverlay.removeFromMap();
                    walkRouteOverlay.setNodeIconVisibility(false);
                    walkRouteOverlay.addToMap();
                    walkRouteOverlay.zoomToSpan();
                }
            } else if (result != null && result.getPaths() == null) {
                Toast.makeText(OrderDetailActivity.this, "对不起，没有搜索到相关数据！", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(OrderDetailActivity.this, "对不起，没有搜索到相关数据！", Toast.LENGTH_SHORT).show();
            }
        } else {
            /*ToastUtil.showerror(this.getApplicationContext(), errorCode);*/
        }
    }

    @Override
    public void onRideRouteSearched(RideRouteResult result, int errorCode) {

    }
}

