package com.sypm.shuyuzhongbao;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Toast;

import com.sypm.shuyuzhongbao.api.RetrofitClient;
import com.sypm.shuyuzhongbao.data.DataResult;
import com.sypm.shuyuzhongbao.utils.BaseActivity;
import com.sypm.shuyuzhongbao.utils.MD5Utils;
import com.sypm.shuyuzhongbao.utils.RememberHelper;
import com.sypm.shuyuzhongbao.utils.Utils;
import com.tumblr.remember.Remember;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class WelcomeActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);

        if (!Utils.isNetAvailable(this)) {
            Toast.makeText(this, "当前没有网络连接...", Toast.LENGTH_SHORT).show();
            intentLogin();
            return;
        }
        autoLogin();
    }

    private void autoLogin() {
        final String number = Remember.getString("number", "");
        final String password = Remember.getString("password", "");
        if (TextUtils.isEmpty(number) || TextUtils.isEmpty(password)) {
            intentLogin();
            return;
        }
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                requestLogin(number, password);
            }
        }, 1500);
    }

    private void intentLogin() {
        startActivity(new Intent(this, LoginByAccountActivity.class));
        finish();
    }

    private void requestLogin(final String number, final String password) {
        final Call<DataResult> login = RetrofitClient.getInstance().getSYService().login(number, MD5Utils.md5Encode(password));
        login.enqueue(new Callback<DataResult>() {
            @Override
            public void onResponse(Call<DataResult> call, Response<DataResult> response) {
                if (response.body() != null) {
                    String status = response.body().status;
                    if (status.equals("1")) {
                        RememberHelper.saveUserInfo(number, password);
                        intentMain();
                    } else if (status.equals("0")) {
                        intentLogin();
                        Toast.makeText(getActivity(), "登陆失败", Toast.LENGTH_SHORT).show();
                    }
                    return;
                }

            }

            @Override
            public void onFailure(Call<DataResult> call, Throwable t) {
                intentLogin();
                Toast.makeText(getActivity(), "登陆失败", Toast.LENGTH_SHORT).show();
                return;
            }
        });
    }

    private void intentMain() {
        startActivity(new Intent(this, MainActivity.class));
        finish();
    }
}
