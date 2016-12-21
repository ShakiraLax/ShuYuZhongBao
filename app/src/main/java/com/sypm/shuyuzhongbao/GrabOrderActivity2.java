package com.sypm.shuyuzhongbao;

import android.content.DialogInterface;
import android.content.Intent;
import android.media.MediaPlayer;
import android.media.RingtoneManager;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
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
import com.sypm.shuyuzhongbao.api.RetrofitClient;
import com.sypm.shuyuzhongbao.data.DataResult;
import com.sypm.shuyuzhongbao.data.OrderBySn;
import com.sypm.shuyuzhongbao.utils.BaseActivity;

import java.text.SimpleDateFormat;
import java.util.Date;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * 接单
 */

public class GrabOrderActivity2 extends BaseActivity {

    /*TextView shipSn, name, phone, address, timer, reject;
    LinearLayout accept;
    OrderBySn order;
    OrderBySn orderFromJP;
    String orderFromYiPaiDan;*/

    final MediaPlayer mp = new MediaPlayer();

    /*CountDownTimer countDownTimer = new CountDownTimer(60000, 1000) {
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
            *//*设置倒计时完毕时自动接单*//*
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
//                            Toast.makeText(getActivity(), "自动接单失败或您已接单", Toast.LENGTH_LONG).show();
                        }
                    }
                }

                @Override
                public void onFailure(Call<DataResult> call, Throwable t) {
                    Toast.makeText(getActivity(), "自动接单操作失败", Toast.LENGTH_LONG).show();
                }
            });
        }
    };*/
    private TextView txtDialogNote;
    private String shipSnUnacceptStr;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL, WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_WATCH_OUTSIDE_TOUCH, WindowManager.LayoutParams.FLAG_WATCH_OUTSIDE_TOUCH);
        setContentView(R.layout.activity_dialog2);

        /*shipSn = (TextView) findViewById(R.id.shipSn);
        name = (TextView) findViewById(R.id.name);
        phone = (TextView) findViewById(R.id.phone);
        address = (TextView) findViewById(R.id.address);
        timer = (TextView) findViewById(R.id.timer);
        txtDialogNote = (TextView) findViewById(R.id.txt_dialog_note);
        reject = (TextView) findViewById(R.id.reject);
        accept = (LinearLayout) findViewById(R.id.accept);
        orderFromYiPaiDan = getIntent().getStringExtra("yipaidan");
        if (orderFromYiPaiDan != null) {
            Log.d("首页已派单", orderFromYiPaiDan);
            initOrder(orderFromYiPaiDan);
        } else {
            initDataFromJP();
        }*/

//        initData();
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (MotionEvent.ACTION_OUTSIDE == event.getAction()) {
            Log.d("点击外部事件", "点击了");
            return true;
        }

        // Delegate everything else to Activity.
        return super.onTouchEvent(event);
    }

    /*极光推送自动打开抢单界面*/
    /*private void initDataFromJP() {
        *//*未接受订单*//*
        Call<OrderBySn> getOrder = RetrofitClient.getInstance().getSYService().getOrder();
        getOrder.enqueue(new Callback<OrderBySn>() {
            @Override
            public void onResponse(Call<OrderBySn> call, Response<OrderBySn> response) {
                if (response.body() != null) {
                    if (response.body().status == 1) {
                        orderFromJP = response.body();
                        order = response.body();//接单拒单用
                        countDownTimer.start();
                        shipSn.setText("单号：" + orderFromJP.list.orderSn);
                        name.setText("姓名：" + orderFromJP.list.name);
                        address.setText("地址：" + orderFromJP.list.address);
                        phone.setText("电话：" + orderFromJP.list.mobile);
                        txtDialogNote.setText("备注：" + orderFromJP.list.note);
                        WD = Double.valueOf(orderFromJP.list.lat);
                        JD = Double.valueOf(orderFromJP.list.lng);
                    } else {
                        Toast.makeText(getActivity(), "订单无数据", Toast.LENGTH_LONG).show();
                    }
                } else {
                    Toast.makeText(getActivity(), "无数据", Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(Call<OrderBySn> call, Throwable t) {
                Toast.makeText(getActivity(), "服务器获取失败", Toast.LENGTH_LONG).show();
            }
        });
    }*/

    /*public void initOrder(String orderFromYiPaiDan) {
        Call<OrderBySn> call = RetrofitClient.getInstance().getSYService().getOrderDetail(orderFromYiPaiDan);
        call.enqueue(new Callback<OrderBySn>() {
            @Override
            public void onResponse(Call<OrderBySn> call, Response<OrderBySn> response) {
                if (response.body() != null) {
                    if (response.body().status == 1) {
                        countDownTimer.start();
                        order = response.body();
                        shipSn.setText("单号：" + order.list.orderSn);
                        name.setText("姓名：" + order.list.name);
                        address.setText("地址：" + order.list.address);
                        phone.setText("电话：" + order.list.mobile);
                        txtDialogNote.setText("备注：" + order.list.note);
                        WD = Double.valueOf(order.list.lat);
                        JD = Double.valueOf(order.list.lng);
                    }
                } else {
                    Log.d("首页已派单", "获取失败");
                }
            }

            @Override
            public void onFailure(Call<OrderBySn> call, Throwable t) {
                Log.d("首页已派单", "服务器获取失败");
            }
        });

    }*/

    /*private void initData() {
        *//*接单*//*
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
                                Intent intent = new Intent(getActivity(), OrderDetailActivity.class);
                                intent.putExtra("orderFromGrab", order.list.orderSn);
                                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                setResult(RESULT_OK);
                                startActivity(intent);
                                finish();
                            } else if (response.body().status.equals("2")) {
                                countDownTimer.cancel();
                                setResult(RESULT_OK);
                                Toast.makeText(getActivity(), response.body().msg, Toast.LENGTH_LONG).show();
                                finish();
                            } else if (response.body().status.equals("0")) {
                                countDownTimer.cancel();
                                setResult(RESULT_OK);
                                Toast.makeText(getActivity(), response.body().msg, Toast.LENGTH_LONG).show();
                                finish();
                            }
                        }
                    }

                    @Override
                    public void onFailure(Call<DataResult> call, Throwable t) {
                        countDownTimer.cancel();
                        Toast.makeText(getActivity(), "接单操作失败", Toast.LENGTH_LONG).show();
                        finish();
                    }
                });
            }
        });

        *//*拒单*//*
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
                                countDownTimer.cancel();
                                setResult(RESULT_OK);
                                finish();
                                Toast.makeText(getApplicationContext(), "拒单失败", Toast.LENGTH_LONG).show();
                            }
                        }
                    }

                    @Override
                    public void onFailure(Call<DataResult> call, Throwable t) {
                        countDownTimer.cancel();
                        Toast.makeText(getApplicationContext(), "拒单操作失败", Toast.LENGTH_LONG).show();
                    }
                });
            }
        });

    }*/

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        new AlertDialog.Builder(getActivity())
                .setTitle("提示")
                .setMessage("请对订单进行操作")
                .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                })
                .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        /*Intent intent = new Intent(getActivity(), MainActivity.class);
                        setResult(RESULT_OK);
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        countDownTimer.cancel();
                        mp.stop();
                        startActivity(intent);*/
                        finish();
                    }
                }).show();
    }
}
