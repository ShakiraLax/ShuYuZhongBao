package com.sypm.shuyuzhongbao;

import android.os.Bundle;
import android.widget.Toast;

import com.sypm.shuyuzhongbao.api.RetrofitClient;
import com.sypm.shuyuzhongbao.data.MoneyList;
import com.sypm.shuyuzhongbao.data.OrderDetail;
import com.sypm.shuyuzhongbao.utils.BaseActivity;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * 订单详情
 * */

public class OrderDetailActivity extends BaseActivity {

    private MoneyList.ListBean  moneyList ;
    OrderDetail orderDetail;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_detail);
        moneyList= (MoneyList.ListBean) getIntent().getSerializableExtra("item");

        if (moneyList ==null){
            Toast.makeText(this,"此订单无数据...",Toast.LENGTH_SHORT).show();
            return;
        }
        RetrofitClient.getInstance().getSYService().getOrderDetail(moneyList.shipSn).enqueue(new Callback<OrderDetail>() {
            @Override
            public void onResponse(Call<OrderDetail> call, Response<OrderDetail> response) {
                if (response.body() != null){
                    if (response.isSuccessful()){
                        orderDetail = response.body();
                        Toast.makeText(getActivity(),response.body().msg + "success",Toast.LENGTH_SHORT).show();
                    }else{
                        Toast.makeText(getActivity(),response.body().msg,Toast.LENGTH_SHORT).show();
                    }

                }
            }

            @Override
            public void onFailure(Call<OrderDetail> call, Throwable t) {

            }
        });
    }
}
