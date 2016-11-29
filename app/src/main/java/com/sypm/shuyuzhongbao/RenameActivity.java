package com.sypm.shuyuzhongbao;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.sypm.shuyuzhongbao.api.RetrofitClient;
import com.sypm.shuyuzhongbao.data.DataResult;
import com.sypm.shuyuzhongbao.utils.BaseActivity;
import com.sypm.shuyuzhongbao.utils.ToastUtils;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * 修改姓名
 */

public class RenameActivity extends BaseActivity {

    private EditText editText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rename);
        initView();
    }

    public void onClick(View view) {
        String name = editText.getText().toString();
        Call<DataResult> reNameCall = RetrofitClient.getInstance().getSYService().updateUserName(name);
        reNameCall.enqueue(new Callback<DataResult>() {
            @Override
            public void onResponse(Call<DataResult> call, Response<DataResult> response) {
                if (response.isSuccessful()) {
                    if (response.body().status.equals("1")) {
                        ToastUtils.show("操作成功");
                    } else {
                        ToastUtils.show("操作失败");
                    }
                }
            }

            @Override
            public void onFailure(Call<DataResult> call, Throwable t) {

            }
        });
        finish();
    }

    private void initView() {
        editText = (EditText) findViewById(R.id.edit_rename);
    }


}
