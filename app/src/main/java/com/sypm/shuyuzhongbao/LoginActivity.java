package com.sypm.shuyuzhongbao;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.design.widget.Snackbar;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.gson.reflect.TypeToken;
import com.sypm.shuyuzhongbao.api.RetrofitClient;
import com.sypm.shuyuzhongbao.data.DataResult;
import com.sypm.shuyuzhongbao.utils.FixKeyboardActivity;
import com.sypm.shuyuzhongbao.utils.RememberHelper;
import com.sypm.shuyuzhongbao.utils.ToastUtils;
import com.sypm.shuyuzhongbao.utils.Utils;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/*
* 手机快速登陆
* */

public class LoginActivity extends FixKeyboardActivity {

    private EditText mUIDEdit;
    private EditText mPinEdit;
    private Button mButton;

    Button login;

    private CountDownTimer timer = new CountDownTimer(60000, 1000) {
        @Override
        public void onTick(long millisUntilFinished) {
            mButton.setText((millisUntilFinished / 1000) + "秒后重发");
        }

        @Override
        public void onFinish() {
            mButton.setEnabled(true);
            mButton.setText("获取");
        }
    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        initData();
        initViews();
    }

    private void initViews() {
        mUIDEdit = (EditText) findViewById(R.id.uidEdit);
        mPinEdit = (EditText) findViewById(R.id.pinEdit);
        mButton = (Button) findViewById(R.id.button);
    }

    private void initData() {
        login = (Button) findViewById(R.id.login);
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!isInvalidInput()) {
                    validatecode();
                } else {
                    Snackbar snackbar = Snackbar.make(v, "手机号、或者验证码不能为空！", Snackbar.LENGTH_LONG)
                            .setAction("Action", null);
                    snackbar.getView().setBackgroundResource(R.color.colorAccent);
                    snackbar.setActionTextColor(Color.WHITE);
                    snackbar.show();
                }
            }
        });

    }

    void validatecode() {
        String phone = mUIDEdit.getText().toString().trim();
        String pin = mPinEdit.getText().toString().trim();
        /*请求接口*/
        Call<DataResult> login = RetrofitClient.getInstance().getSYService().loginByPhone(phone, pin);
        login.enqueue(new Callback<DataResult>() {
            @Override
            public void onResponse(Call<DataResult> call, Response<DataResult> response) {
                if (response.body() != null) {
                    String status = response.body().status;
                    if (status.equals("1")) {
                        startActivity(new Intent(LoginActivity.this, MainActivity.class));
                        Toast.makeText(LoginActivity.this, "登录成功", Toast.LENGTH_SHORT).show();
                        finish();
                        return;
                    }
                }
                ToastUtils.show("登陆失败...");
            }

            @Override
            public void onFailure(Call<DataResult> call, Throwable t) {

            }
        });

    }

    public void loginByAccount(View view) {
        Intent intent = new Intent(getActivity(), LoginByAccountActivity.class);
        startActivity(intent);
        finish();
    }

    //获取验证码检验
    public void getPinClick(View view) {
        String phone = mUIDEdit.getText().toString().trim();
        if (TextUtils.isEmpty(phone)) {
            Snackbar snackbar = Snackbar.make(view, "请先输入手机号码！", Snackbar.LENGTH_LONG)
                    .setAction("Action", null);
            snackbar.getView().setBackgroundResource(R.color.colorAccent);
            snackbar.setActionTextColor(Color.WHITE);
            snackbar.show();
            return;
        }
        if (!Utils.isMobileNO(phone)) {
            Snackbar snackbar = Snackbar.make(view, "请输入正确的手机号码！", Snackbar.LENGTH_LONG)
                    .setAction("Action", null);
            snackbar.getView().setBackgroundResource(R.color.colorAccent);
            snackbar.setActionTextColor(Color.WHITE);
            snackbar.show();
            return;
        }

        getPinFromNet(phone);
    }

    //验证输入字符是否无效
    public boolean isInvalidInput() {
        String uid = mUIDEdit.getText().toString().trim();
        String pin = mPinEdit.getText().toString().trim();
        return TextUtils.isEmpty(uid) || TextUtils.isEmpty(pin);
    }

    /*从接口获取手机验证码*/
    public void getPinFromNet(String phone) {

        Call<DataResult> getPoint = RetrofitClient.getInstance().getSYService().getPinFromNet(phone);
        getPoint.enqueue(new Callback<DataResult>() {
            @Override
            public void onResponse(Call<DataResult> call, Response<DataResult> response) {
                timer.start();
                mButton.setEnabled(false);
            }

            @Override
            public void onFailure(Call<DataResult> call, Throwable t) {

            }
        });
    }
}
