package com.sypm.shuyuzhongbao;

import android.os.Bundle;

import com.sypm.shuyuzhongbao.api.RetrofitClient;
import com.sypm.shuyuzhongbao.data.DataResult;
import com.sypm.shuyuzhongbao.utils.BaseActivity;

import retrofit2.Call;
import retrofit2.Callback;

/**
 * 修改姓名
 */

public class RenameActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rename);
    }

}
