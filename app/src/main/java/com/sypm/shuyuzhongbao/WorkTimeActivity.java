package com.sypm.shuyuzhongbao;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;

import com.sypm.shuyuzhongbao.utils.BaseActivity;

public class WorkTimeActivity extends BaseActivity {

    LinearLayout add;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_work_time);
        initData();
    }

    private void initData() {
        add = (LinearLayout) findViewById(R.id.add);
        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), AddWorkTimeActivity.class);
                startActivity(intent);
            }
        });
    }
}
