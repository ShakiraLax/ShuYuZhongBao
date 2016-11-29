package com.sypm.shuyuzhongbao;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.sypm.shuyuzhongbao.api.RetrofitClient;
import com.sypm.shuyuzhongbao.api.ShuYuService;
import com.sypm.shuyuzhongbao.data.DataResult;
import com.sypm.shuyuzhongbao.data.UserInfo;
import com.sypm.shuyuzhongbao.utils.BaseActivity;

import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;


/**
 * 个人信息
 */

public class PersonalActivity extends BaseActivity {

    LinearLayout IDCard, name;
    private TextView txtName;
    private TextView txtIdCard;
    private UserInfo userInfo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_personal);
        initView();
        initData();
    }

    private void initView() {
        txtName = (TextView) findViewById(R.id.txt_name_userinfo);
        txtIdCard = (TextView) findViewById(R.id.txt_idCard_userinfo);
    }

    private void initData() {
        /*获取当前用户信息*/
        Call<UserInfo> userInfoCall = RetrofitClient.getInstance().getSYService().getUserInfo();
        userInfoCall.enqueue(new Callback<UserInfo>() {
            @Override
            public void onResponse(Call<UserInfo> call, Response<UserInfo> response) {
                if (response.isSuccessful()) {
                    if (response.body().getStatus() == 1) {
//                        Log.i(">>>>name+idcard",response.body().getList().getName()+response.body().getList().getIdNumber());
                        String name = response.body().getList().getName();
                        String idCard = response.body().getList().getIdNumber();
//                        String idCard="370126199102187118";
                        if (!TextUtils.isEmpty(idCard) && idCard.length() == 18) {
                            idCard = idCard.substring(0, 6) + "********" + idCard.substring(14);
                        }
                        txtName.setText(name);
                        txtIdCard.setText(idCard);
                    }

                }
            }

            @Override
            public void onFailure(Call<UserInfo> call, Throwable t) {

            }
        });

        IDCard = (LinearLayout) findViewById(R.id.IDCard);
        name = (LinearLayout) findViewById(R.id.name);
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


    }

    @Override
    protected void onRestart() {
        super.onRestart();
             /*获取当前用户信息*/
        Call<UserInfo> userInfoCall = RetrofitClient.getInstance().getSYService().getUserInfo();
        userInfoCall.enqueue(new Callback<UserInfo>() {
            @Override
            public void onResponse(Call<UserInfo> call, Response<UserInfo> response) {
                if (response.isSuccessful()) {
                    if (response.body().getStatus() == 1) {
//                        Log.i(">>>>name+idcard",response.body().getList().getName()+response.body().getList().getIdNumber());
                        String name = response.body().getList().getName();
                        String idCard = response.body().getList().getIdNumber();
//                        String idCard="370126199102187118";
                        if (!TextUtils.isEmpty(idCard)) {
                            idCard = idCard.substring(0, 6) + "********" + idCard.substring(14);
                        }
                        txtName.setText(name);
                        txtIdCard.setText(idCard);
                    }

                }
            }

            @Override
            public void onFailure(Call<UserInfo> call, Throwable t) {

            }
        });
    }
}
