package com.sypm.shuyuzhongbao;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;

import com.sypm.shuyuzhongbao.api.RetrofitClient;
import com.sypm.shuyuzhongbao.data.DataResult;
import com.sypm.shuyuzhongbao.utils.BaseActivity;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * 个人信息修改
 */

public class PersonalActivity extends BaseActivity {

    LinearLayout IDCard, name, personCode, storeAddress;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_personal);
        setupListView();
        initData();
    }

    private void initData() {

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

        IDCard = (LinearLayout) findViewById(R.id.IDCard);
        name = (LinearLayout) findViewById(R.id.name);
        personCode = (LinearLayout) findViewById(R.id.personCode);
        storeAddress = (LinearLayout) findViewById(R.id.storeAddress);
        IDCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), IDCardActivity.class);
                startActivity(intent);
            }
        });
        name.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), RenameActivity.class);
                startActivity(intent);
            }
        });
        personCode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), PersonCodingActivity.class);
                startActivity(intent);
            }
        });
        storeAddress.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), StoreAddressActivity.class);
                startActivity(intent);
            }
        });
    }

    private void setupListView() {

    }
}
