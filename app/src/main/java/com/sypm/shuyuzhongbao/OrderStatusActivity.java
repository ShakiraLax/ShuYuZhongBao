package com.sypm.shuyuzhongbao;

import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
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

public class OrderStatusActivity extends BaseActivity {

    private OrderBySn orderByIndex;
    private TextView shipSn, name, phone, address, storeName, amount, feeWay, txt_orderStatus_detail, isGet;
    private String phoneNumber;

    private TextView txtNote;
    private String shipSnFromQuHuo;
    private String shipSnFromKeFu;
    private String SHIPSN;
    private String SHIPSN2;
    private LinearLayout layoutOfOrderAndGoods, goods;
    private Button know, cancel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_status);

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
        layoutOfOrderAndGoods = (LinearLayout) findViewById(R.id.layoutOfOrderAndGoods);
        know = (Button) findViewById(R.id.know);
        cancel = (Button) findViewById(R.id.cancel);
        goods = (LinearLayout) findViewById(R.id.goods);


        phone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_DIAL);
                Uri data = Uri.parse("tel:" + phoneNumber);
                intent.setData(data);
                startActivity(intent);
            }
        });

        /*极光推送传过来的shipSn*/
        shipSnFromQuHuo = getIntent().getStringExtra("quhuo");
        if (shipSnFromQuHuo != null) {
            Log.d("shipSnFromQuHuo", shipSnFromQuHuo);
            SHIPSN = shipSnFromQuHuo;
            Log.d("shipSnFromQuHuo", SHIPSN);
            setupOrderDetail();
        }
        shipSnFromKeFu = getIntent().getStringExtra("kefu");
        if (shipSnFromKeFu != null) {
            Log.d("shipSnFromKeFu", shipSnFromKeFu);
            SHIPSN2 = shipSnFromKeFu;
            Log.d("shipSnFromKeFu", SHIPSN2);
            setupOrder();
        }
        initData();
    }

    /*G3状态为取消时 强制获取订单*/
    private void setupOrder() {
        Call<OrderBySn> orderDetailCall = RetrofitClient.getInstance().getSYService().getOrderDetail(SHIPSN2, "1");
        orderDetailCall.enqueue(new Callback<OrderBySn>() {
            @Override
            public void onResponse(Call<OrderBySn> call, Response<OrderBySn> response) {
                if (response.body() != null) {
                    if (response.body().status == 1) {
                        orderByIndex = response.body();
                        address.setText("地址：" + orderByIndex.list.address);
                        name.setText("姓名：" + orderByIndex.list.name);
                        phone.setText("电话：" + orderByIndex.list.mobile);
                        shipSn.setText("单号：" + orderByIndex.list.orderSn);
                        amount.setText("订单金额：" + orderByIndex.list.amount);
                        feeWay.setText("支付方式：" + orderByIndex.list.payCode);
                        storeName.setText("门店名称：" + orderByIndex.list.storeName);
                        txtNote.setText("备注：" + orderByIndex.list.note);
                        txt_orderStatus_detail.setText("订单状态：" + orderByIndex.list.orderStatus);
                        isGet.setText("此订单已取消，请知晓！");
                        know.setVisibility(View.INVISIBLE);
                        cancel.setVisibility(View.VISIBLE);
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
                    } else {
                        Toast.makeText(getActivity(), "无数据", Toast.LENGTH_LONG).show();
                        finish();
                    }
                } else {
                    finish();
                }
            }

            @Override
            public void onFailure(Call<OrderBySn> call, Throwable t) {
                finish();
            }
        });
    }

    //根据极光推送传递过来的订单号展示订单详情包括商品信息
    private void setupOrderDetail() {
        Call<OrderBySn> orderDetailCall = RetrofitClient.getInstance().getSYService().getOrderDetail(SHIPSN);
        orderDetailCall.enqueue(new Callback<OrderBySn>() {
            @Override
            public void onResponse(Call<OrderBySn> call, Response<OrderBySn> response) {
                if (response.body() != null) {
                    if (response.body().status == 1) {
                        orderByIndex = response.body();
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
                    } else {
                        Toast.makeText(getActivity(), "此单已无数据", Toast.LENGTH_LONG).show();
                    }
                } else {
                    Toast.makeText(getActivity(), "程序已被后台清理，清重新打开", Toast.LENGTH_LONG).show();
                    finish();
                }
            }

            @Override
            public void onFailure(Call<OrderBySn> call, Throwable t) {
                finish();
            }
        });
    }

    private void initData() {
        know.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), OrderDetailActivity2.class);
                intent.putExtra("wozhidao", SHIPSN);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
                finish();
            }
        });
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), MainActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
                finish();
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
                .setMessage("订单状态发生变化")
                .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                })
                .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Intent intent = new Intent(getActivity(), MainActivity.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(intent);
                        finish();
                    }
                }).show();
    }
}

