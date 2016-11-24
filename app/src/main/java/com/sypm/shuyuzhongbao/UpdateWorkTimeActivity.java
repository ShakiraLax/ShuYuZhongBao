package com.sypm.shuyuzhongbao;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.sypm.shuyuzhongbao.api.RetrofitClient;
import com.sypm.shuyuzhongbao.data.DataResult;
import com.sypm.shuyuzhongbao.utils.BaseActivity;
import com.sypm.shuyuzhongbao.utils.ToastUtils;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class UpdateWorkTimeActivity extends BaseActivity {

    private LinearLayout onWorkUpdate;
    private LinearLayout offWorkUpdate;
    private LinearLayout doingWeeks;
    private TextView txtOnWork;
    private TextView txtOffWork;
    private TextView txtWeeks;
    //需要上传的id
    private int id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_work_time);
        initView();
        setData();
        setOnListener();
    }

    private void setData() {
        Bundle bundle = getIntent().getExtras();
        id = bundle.getInt("id");
        txtOnWork.setText(bundle.getString("startTime"));
        txtOffWork.setText(bundle.getString("endTime"));
        txtWeeks.setText(bundle.getString("dayTime"));
    }

    private void setOnListener() {
        onWorkUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), TimePickerActivity.class);
                startActivityForResult(intent, 1000);
            }
        });
        offWorkUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), TimePickerActivity.class);
                startActivityForResult(intent, 2000);
            }
        });
        doingWeeks.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), WeekPickerActivity.class);
                startActivityForResult(intent, 3000);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1000 && resultCode == RESULT_OK) {
            String time = data.getStringExtra("time");
            txtOnWork.setText(time);
        }
        if (requestCode == 2000 && resultCode == RESULT_OK) {
            String time = data.getStringExtra("time");
            txtOffWork.setText(time);
        }
        if (requestCode == 3000 && resultCode == RESULT_OK) {
            String weeks = data.getStringExtra("weeks");
            txtWeeks.setText(weeks);
        }
    }

    private void initView() {
        onWorkUpdate = (LinearLayout) findViewById(R.id.onWork_updateWorkTime);
        offWorkUpdate = (LinearLayout) findViewById(R.id.offWork_updateWorkTime);
        doingWeeks = (LinearLayout) findViewById(R.id.doingWeeks_updateWorkTime);
        txtOnWork = (TextView) findViewById(R.id.txt_onWork);
        txtOffWork = (TextView) findViewById(R.id.txt_offWork);
        txtWeeks = (TextView) findViewById(R.id.txt_weeks);
    }

    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.bt_updatetime_down:
                //人员上班时间修改
                Call<DataResult> updateTime = RetrofitClient.getInstance().getSYService()
                        .worktimeUpdate(id, txtOnWork.getText().toString(),
                                txtOffWork.getText().toString(),
                                txtWeeks.getText().toString());
                updateTime.enqueue(new Callback<DataResult>() {
                    @Override
                    public void onResponse(Call<DataResult> call, Response<DataResult> response) {
                        if (response.isSuccessful()) {
                            if (response.body().msg.equals("操作成功")) {
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
                break;
            case R.id.bt_updatetime_cancel:
                finish();
                break;
        }
    }
}
