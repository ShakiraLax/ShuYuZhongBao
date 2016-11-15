package com.sypm.shuyuzhongbao;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.sypm.shuyuzhongbao.utils.BaseActivity;

public class AddWorkTimeActivity extends BaseActivity {

    LinearLayout up, down, week;
    TextView upTime, downTime, weekText;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_work_time);
        initData();

    }

    private void initData() {
        upTime = (TextView) findViewById(R.id.textView6);
        downTime = (TextView) findViewById(R.id.textView);
        weekText = (TextView) findViewById(R.id.weeks);
        up = (LinearLayout) findViewById(R.id.up);
        down = (LinearLayout) findViewById(R.id.down);
        week = (LinearLayout) findViewById(R.id.week);
        up.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), TimePickerActivity.class);
                startActivityForResult(intent, 1000);

            }
        });
        down.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), TimePickerActivity.class);
                startActivityForResult(intent, 2000);
            }
        });
        week.setOnClickListener(new View.OnClickListener() {
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
            upTime.setText(time);
        }
        if (requestCode == 2000 && resultCode == RESULT_OK) {
            String time = data.getStringExtra("time");
            downTime.setText(time);
        }
        if (requestCode == 3000 && resultCode == RESULT_OK) {
            String weeks = data.getStringExtra("weeks");
            weekText.setText(weeks);
        }
    }
}
