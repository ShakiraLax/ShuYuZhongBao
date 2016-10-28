package com.sypm.shuyuzhongbao;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.sypm.shuyuzhongbao.utils.BaseActivity;

public class LoginActivity extends BaseActivity {

    Button login;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        initData();
    }

    private void initData() {
        login = (Button) findViewById(R.id.login);
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), LoginByAccountActivity.class);
                startActivity(intent);
                finish();
            }
        });
    }

    public void loginByAccount(View view) {
        Intent intent = new Intent(getActivity(), LoginByAccountActivity.class);
        startActivity(intent);
        finish();
    }

    /*--------------------高德----------------------------*/

}
