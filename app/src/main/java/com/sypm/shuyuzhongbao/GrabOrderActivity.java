package com.sypm.shuyuzhongbao;

import android.os.Bundle;

import com.sypm.shuyuzhongbao.api.RetrofitClient;
import com.sypm.shuyuzhongbao.data.DataResult;
import com.sypm.shuyuzhongbao.utils.BaseActivity;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

/*
* 抢单
*
* */

public class GrabOrderActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dialog);
        initData();
    }

    private void initData() {
        /*未指派订单*/
        Call<DataResult> call = RetrofitClient.getInstance().getSYService().getOrder();
        call.enqueue(new Callback<DataResult>() {
            @Override
            public void onResponse(Call<DataResult> call, Response<DataResult> response) {

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

            }

            @Override
            public void onFailure(Call<DataResult> call, Throwable t) {

            }
        });

        /*获取当前用户信息*/
        Call<DataResult> callGetUserInfo = RetrofitClient.getInstance().getSYService().getUserInfo();
        callGetUserInfo.enqueue(new Callback<DataResult>() {
            @Override
            public void onResponse(Call<DataResult> call, Response<DataResult> response) {

            }

            @Override
            public void onFailure(Call<DataResult> call, Throwable t) {

            }
        });

        /*修改当前用户信息*/
        Call<DataResult> updateUserInfo = RetrofitClient.getInstance().getSYService().updateUserInfo(null, null, null, null, null, null);
        updateUserInfo.enqueue(new Callback<DataResult>() {
            @Override
            public void onResponse(Call<DataResult> call, Response<DataResult> response) {

            }

            @Override
            public void onFailure(Call<DataResult> call, Throwable t) {

            }
        });
    }

}
