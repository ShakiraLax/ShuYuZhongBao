package com.sypm.shuyuzhongbao;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.sypm.shuyuzhongbao.api.RetrofitClient;
import com.sypm.shuyuzhongbao.data.DataResult;
import com.sypm.shuyuzhongbao.utils.BaseActivity;
import com.sypm.shuyuzhongbao.utils.MD5Utils;
import com.sypm.shuyuzhongbao.utils.RememberHelper;
import com.sypm.shuyuzhongbao.utils.ToastUtils;
import com.tumblr.remember.Remember;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/*
* 账号密码登陆
* */

public class LoginByAccountActivity extends BaseActivity {
    Button login;
    EditText number, password;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_by_account);
        autoLogin();
        initData();
        initViews();
    }

    private void autoLogin() {
        final String uid= Remember.getString("uid","");
        final String password=Remember.getString("password","");
        if (TextUtils.isEmpty(uid)||TextUtils.isEmpty(password)){
            return;
        }

    }

    private void initViews() {
        number = (EditText) findViewById(R.id.number);
        password = (EditText) findViewById(R.id.password);
        number.setText("100002");
        password.setText("123456");
    }

    private void initData() {
        login = (Button) findViewById(R.id.login);
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!isInvalidInput()) {
                    Call<DataResult> login = RetrofitClient.getInstance().getSYService()
                            .login(number.getText().toString(), MD5Utils.md5Encode(password.getText().toString()));
                    login.enqueue(new Callback<DataResult>() {
                        @Override
                        public void onResponse(Call<DataResult> call, Response<DataResult> response) {
                            if (response.body() != null) {
                                String status = response.body().status;
                                if (status.equals("1")) {
                                    RememberHelper.saveUserInfo(number.getText().toString(), password.getText().toString());
                                    startActivity(new Intent(LoginByAccountActivity.this, MainActivity.class));
                                    Toast.makeText(LoginByAccountActivity.this, "登录成功", Toast.LENGTH_SHORT).show();
                                    finish();
                                    return;
                                }
                            }
                            ToastUtils.show("登陆失败...");
                        }

                        @Override
                        public void onFailure(Call<DataResult> call, Throwable t) {
                            ToastUtils.show(t.toString());
                        }
                    });
                }
            }
        });
    }

    public void login(View view) {
        Intent intent = new Intent(getActivity(), LoginActivity.class);
        startActivity(intent);
        finish();
    }

    //验证输入字符是否无效
    public boolean isInvalidInput() {
        String uid = number.getText().toString().trim();
        String mPassword = password.getText().toString().trim();
        return TextUtils.isEmpty(uid) || TextUtils.isEmpty(mPassword);
    }

    private void intentMain(){
        startActivity(new Intent(this,MainActivity.class));
        finish();
    }
}
