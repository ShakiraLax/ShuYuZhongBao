package com.sypm.shuyuzhongbao;

import android.os.Bundle;
import android.widget.Toast;

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

        /*接单*/
        Call<DataResult> callSure = RetrofitClient.getInstance().getSYService().orderSure("1");
        callSure.enqueue(new Callback<DataResult>() {
            @Override
            public void onResponse(Call<DataResult> call, Response<DataResult> response) {

            }

            @Override
            public void onFailure(Call<DataResult> call, Throwable t) {

            }
        });

        /*配送员拒单*/
        /*Call<DataResult> callCancel = RetrofitClient.getInstance().getSYService().orderReject("1");
        callCancel.enqueue(new Callback<DataResult>() {
            @Override
            public void onResponse(Call<DataResult> call, Response<DataResult> response) {

            }

            @Override
            public void onFailure(Call<DataResult> call, Throwable t) {

            }
        });*/
    }

}
