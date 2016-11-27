package com.sypm.shuyuzhongbao;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.sypm.shuyuzhongbao.api.RetrofitClient;
import com.sypm.shuyuzhongbao.data.DataResult;
import com.sypm.shuyuzhongbao.data.MoneyList;
import com.sypm.shuyuzhongbao.data.Order;
import com.sypm.shuyuzhongbao.data.OrderDetail;
import com.sypm.shuyuzhongbao.utils.BaseActivity;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * 订单详情
 */

public class OrderDetailActivity extends BaseActivity {

    private MoneyList.ListBean moneyList;
    OrderDetail orderDetail;
    Order order;
    TextView shipSn, name, phone, address;
    Button customerReject, dispatchingDone;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_detail);
        initData();
        moneyList = (MoneyList.ListBean) getIntent().getSerializableExtra("item");
        Log.d("根据订单号获取订单详情", moneyList.shipSn);
        initOrderData();
    }

    private void initOrderData() {
        /*根据收入列表传输过来的shipSn获取订单详情*/
        Call<Order> getOrderDetail = RetrofitClient.getInstance().getSYService().getOrderDetail(moneyList.shipSn);
        getOrderDetail.enqueue(new Callback<Order>() {
            @Override
            public void onResponse(Call<Order> call, Response<Order> response) {
                if (response.body() != null) {
                    if (response.body().status == 1) {
                        order = response.body();
                        shipSn.setText("单号：" + order.list.orderSn);
                        address.setText("地址：" + order.list.address);
                        phone.setText("电话：" + order.list.mobile);
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
    }

    private void initData() {
        shipSn = (TextView) findViewById(R.id.shipSn);
        name = (TextView) findViewById(R.id.name);
        phone = (TextView) findViewById(R.id.phone);
        address = (TextView) findViewById(R.id.address);
        customerReject = (Button) findViewById(R.id.customerReject);
        dispatchingDone = (Button) findViewById(R.id.dispatchingDone);

        /*现在执行订单*/
        Call<Order> callCurrentOrder = RetrofitClient.getInstance().getSYService().getCurrentOrder();
        callCurrentOrder.enqueue(new Callback<Order>() {
            @Override
            public void onResponse(Call<Order> call, Response<Order> response) {
                if (response.body() != null) {
                    if (response.body().status == 1) {
                        order = response.body();
                        shipSn.setText("单号：" + order.list.orderSn);
                        address.setText("地址：" + order.list.address);
                        phone.setText("电话：" + order.list.mobile);
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
                                Toast.makeText(getApplicationContext(), "客户拒单提交成功", Toast.LENGTH_LONG).show();
                            } else {
                                Toast.makeText(getApplicationContext(), "提交失败", Toast.LENGTH_LONG).show();
                            }
                        }
                    }

                    @Override
                    public void onFailure(Call<DataResult> call, Throwable t) {
                        Toast.makeText(getApplicationContext(), "客户拒单操作失败", Toast.LENGTH_LONG).show();
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
                                Toast.makeText(getApplicationContext(), "提交成功", Toast.LENGTH_LONG).show();
                            } else {
                                Toast.makeText(getApplicationContext(), "提价失败", Toast.LENGTH_LONG).show();
                            }
                        }
                    }

                    @Override
                    public void onFailure(Call<DataResult> call, Throwable t) {
                        Toast.makeText(getApplicationContext(), "完成配送操作失败", Toast.LENGTH_LONG).show();
                    }
                });
            }
        });
    }
}

