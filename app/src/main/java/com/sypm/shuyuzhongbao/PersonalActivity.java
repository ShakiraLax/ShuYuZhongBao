package com.sypm.shuyuzhongbao;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.sypm.shuyuzhongbao.api.RetrofitClient;
import com.sypm.shuyuzhongbao.data.DataResult;
import com.sypm.shuyuzhongbao.data.UserInfo;
import com.sypm.shuyuzhongbao.utils.BaseActivity;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * 个人信息修改
 */

public class PersonalActivity extends BaseActivity {

    LinearLayout IDCard, name, personCode, storeAddress;
    TextView userName, number, store, IDCardNumber;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_personal);
        setupListView();
        initData();
    }

    private void initData() {

        userName = (TextView) findViewById(R.id.userName);
        number = (TextView) findViewById(R.id.number);
        store = (TextView) findViewById(R.id.store);
        IDCardNumber = (TextView) findViewById(R.id.IDCardNumber);

        /*获取当前用户信息*/
        Call<DataResult> call = RetrofitClient.getInstance().getSYService().getUserInfo();
        call.enqueue(new Callback<DataResult>() {
            @Override
            public void onResponse(Call<DataResult> call, Response<DataResult> response) {
                if (response.body() == null) {
                    String status = response.body().status;
                    if (status.equals("1")) {
                        Toast.makeText(getActivity(), "获取成功", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(getActivity(), "没有数据", Toast.LENGTH_SHORT).show();
                    }
                    return;
                }
            }

            @Override
            public void onFailure(Call<DataResult> call, Throwable t) {
                Toast.makeText(getActivity(), "失败了", Toast.LENGTH_SHORT).show();
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
                startActivityForResult(intent, 1000);
            }
        });
        name.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), RenameActivity.class);
                startActivityForResult(intent, 2000);
            }
        });
        personCode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), PersonCodingActivity.class);
                startActivityForResult(intent, 3000);
            }
        });
        storeAddress.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), StoreAddressActivity.class);
                startActivityForResult(intent, 4000);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1000 && resultCode == RESULT_OK) {

        }
        if (requestCode == 2000 && resultCode == RESULT_OK) {

        }
        if (requestCode == 3000 && resultCode == RESULT_OK) {

        }
        if (requestCode == 4000 && resultCode == RESULT_OK) {

        }
    }

    private void setupListView() {

    }
}
