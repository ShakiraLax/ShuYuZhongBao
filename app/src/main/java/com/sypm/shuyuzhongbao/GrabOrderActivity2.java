package com.sypm.shuyuzhongbao;

import android.content.DialogInterface;
import android.content.Intent;
import android.media.MediaPlayer;
import android.media.RingtoneManager;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.v7.app.AlertDialog;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

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

    TextView totalDistance, storeNameAddress, distanceA, distanceB, address, timer, reject;
    LinearLayout accept;
    OrderBySn order;
    OrderBySn orderFromJP;
    Button button;

    final MediaPlayer mp = new MediaPlayer();

    CountDownTimer countDownTimer = new CountDownTimer(6000, 1000) {
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
                        if (response.body().status.equals("1")) {
                            SimpleDateFormat formatter = new SimpleDateFormat("yyyy年MM月dd日 HH:mm:ss ");
                            Date curDate = new Date(System.currentTimeMillis());
                            String str = formatter.format(curDate);
                            timer.setText("已默认接单" + str);
                            accept.setVisibility(View.INVISIBLE);
                            reject.setVisibility(View.INVISIBLE);
                            mp.start();
                            Intent intent = new Intent();
                            setResult(RESULT_OK, intent);
                            button.setVisibility(View.VISIBLE);
                            button.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    mp.stop();
                                    finish();
                                }
                            });
                        } else {
                            reject.setVisibility(View.INVISIBLE);
                            accept.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    Toast.makeText(getActivity(), "订单状态异常，无法接单", Toast.LENGTH_LONG).show();
                                    finish();
                                }
                            });
                        }
                    } else {
                        reject.setVisibility(View.INVISIBLE);
                        accept.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                Toast.makeText(getActivity(), "订单状态异常，无法接单", Toast.LENGTH_LONG).show();
                                finish();
                            }
                        });
                    }
                }

                @Override
                public void onFailure(Call<DataResult> call, Throwable t) {
                    reject.setVisibility(View.INVISIBLE);
                    accept.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Toast.makeText(getActivity(), "订单状态异常，无法接单", Toast.LENGTH_LONG).show();
                            finish();
                        }
                    });
                }
            });
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL, WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_WATCH_OUTSIDE_TOUCH, WindowManager.LayoutParams.FLAG_WATCH_OUTSIDE_TOUCH);
        setContentView(R.layout.activity_dialog2);

        totalDistance = (TextView) findViewById(R.id.totalDistance);
        storeNameAddress = (TextView) findViewById(R.id.storeNameAddress);
        distanceA = (TextView) findViewById(R.id.distanceA);
        distanceB = (TextView) findViewById(R.id.distanceB);
        address = (TextView) findViewById(R.id.address);
        timer = (TextView) findViewById(R.id.timer);
        reject = (TextView) findViewById(R.id.reject);
        accept = (LinearLayout) findViewById(R.id.accept);
        button = (Button) findViewById(R.id.know);

        initDataFromJP();
        initData();
    }

    /*极光推送自动打开抢单界面*/
    private void initDataFromJP() {
        /*未接受订单*/
        Call<OrderBySn> getOrder = RetrofitClient.getInstance().getSYService().getOrder();
        getOrder.enqueue(new Callback<OrderBySn>() {
            @Override
            public void onResponse(Call<OrderBySn> call, Response<OrderBySn> response) {
                if (response.body() != null) {
                    if (response.body().status == 1) {
                        orderFromJP = response.body();
                        order = response.body();//接单拒单用
                        countDownTimer.start();
                        totalDistance.setText("订单总距离：" + (Integer.valueOf(orderFromJP.list.distanceA) + Integer.valueOf(orderFromJP.list.distanceB)) + "米");
                        distanceA.setText("距您：" + orderFromJP.list.distanceA + "米");
                        distanceB.setText("距发货地点：" + orderFromJP.list.distanceB + "米");
                        storeNameAddress.setText("发：" + orderFromJP.list.storeName + "  " + orderFromJP.list.storeAddress);
                        address.setText("收：" + orderFromJP.list.address);

                    } else {
                        Toast.makeText(getActivity(), response.body().msg, Toast.LENGTH_LONG).show();
                        finish();
                    }
                } else {
                    Toast.makeText(getActivity(), "程序已被后台清理，清重新打开", Toast.LENGTH_LONG).show();
                    finish();
                }
            }

            @Override
            public void onFailure(Call<OrderBySn> call, Throwable t) {
                Toast.makeText(getActivity(), "服务器获取失败", Toast.LENGTH_LONG).show();
                finish();
            }
        });
    }

    private void initData() {
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
                                Toast.makeText(getActivity(), "接单成功！", Toast.LENGTH_LONG).show();
                                countDownTimer.cancel();
                                Intent intent = new Intent(getActivity(), OrderDetailActivity2.class);
                                intent.putExtra("orderFromGrab", order.list.orderSn);
                                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                setResult(RESULT_OK);
                                startActivity(intent);
                                finish();
                            } else if (response.body().status.equals("2")) {
                                Toast.makeText(getActivity(), response.body().msg, Toast.LENGTH_LONG).show();
                                countDownTimer.cancel();
                                setResult(RESULT_OK);
                                finish();
                            } else if (response.body().status.equals("0")) {
                                Toast.makeText(getActivity(), response.body().msg, Toast.LENGTH_LONG).show();
                                countDownTimer.cancel();
                                setResult(RESULT_OK);
                                finish();
                            }
                        } else {
                            countDownTimer.cancel();
                            setResult(RESULT_OK);
                            finish();
                        }
                    }

                    @Override
                    public void onFailure(Call<DataResult> call, Throwable t) {
                        Toast.makeText(getActivity(), "接单操作失败", Toast.LENGTH_LONG).show();
                        countDownTimer.cancel();
                        setResult(RESULT_OK);
                        finish();
                    }
                });
            }
        });

        /*拒绝接单*/
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
                                Intent intent = new Intent(getActivity(), MainActivity.class);
                                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                setResult(RESULT_OK);
                                startActivity(intent);
                                finish();
                            } else {
                                Toast.makeText(getApplicationContext(), "拒单失败", Toast.LENGTH_LONG).show();
                                countDownTimer.cancel();
                                Intent intent = new Intent(getActivity(), MainActivity.class);
                                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                setResult(RESULT_OK);
                                startActivity(intent);
                                finish();

                            }
                        } else {
                            countDownTimer.cancel();
                            Intent intent = new Intent(getActivity(), MainActivity.class);
                            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                            setResult(RESULT_OK);
                            startActivity(intent);
                            finish();
                        }
                    }

                    @Override
                    public void onFailure(Call<DataResult> call, Throwable t) {
                        Toast.makeText(getApplicationContext(), "拒单操作失败", Toast.LENGTH_LONG).show();
                        countDownTimer.cancel();
                        Intent intent = new Intent(getActivity(), MainActivity.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        setResult(RESULT_OK);
                        startActivity(intent);
                        finish();
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
                .setMessage("请对订单进行操作")
                .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                })
                .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        setResult(RESULT_OK);
                        Intent intent = new Intent(getActivity(), MainActivity.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        countDownTimer.cancel();
                        mp.stop();
                        startActivity(intent);
                        finish();
                    }
                }).show();
    }
}
