package com.sypm.shuyuzhongbao;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.CheckBox;
import android.widget.CompoundButton;

import com.sypm.shuyuzhongbao.utils.BaseActivity;

import java.util.ArrayList;
import java.util.List;

/**
 * 执行日期选择
 */

public class WeekPickerActivity extends BaseActivity {

    String weeks = "周";
    private CheckBox cbMonday;
    private CheckBox cbTuesday;
    private CheckBox cbWednesnday;
    private CheckBox cbThursday;
    private CheckBox cbFriday;
    private CheckBox cbSaturday;
    private CheckBox cbSunday;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_week_picker);
        AlertDialog.Builder builder = new AlertDialog.Builder(WeekPickerActivity.this, R.style.dialog);
//        final String[] week = {"一", "二", "三", "四", "五", "六", "日"};
        //    设置一个多项选择下拉框
        /**
         * 第一个参数指定我们要显示的一组下拉多选框的数据集合
         * 第二个参数代表哪几个选项被选择，如果是null，则表示一个都不选择，如果希望指定哪一个多选选项框被选择，
         * 需要传递一个boolean[]数组进去，其长度要和第一个参数的长度相同，例如 {true, false, false, true};
         * 第三个参数给每一个多选项绑定一个监听器
         */
        View inflate = LayoutInflater.from(getActivity()).inflate(R.layout.item_mydailog_week, null);
        cbMonday = (CheckBox) inflate.findViewById(R.id.cb_mydailog_monday);
        cbTuesday = (CheckBox) inflate.findViewById(R.id.cb_mydailog_tuesday);
        cbWednesnday = (CheckBox) inflate.findViewById(R.id.cb_mydailog_wednesday);
        cbThursday = (CheckBox) inflate.findViewById(R.id.cb_mydailog_thursday);
        cbFriday = (CheckBox) inflate.findViewById(R.id.cb_mydailog_friday);
        cbSaturday = (CheckBox) inflate.findViewById(R.id.cb_mydailog_saturday);
        cbSunday = (CheckBox) inflate.findViewById(R.id.cb_mydailog_sunday);

        CompoundButton.OnCheckedChangeListener occl = new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    weeks += buttonView.getText();
                } else {
                    weeks = weeks.replace(buttonView.getText(), "");
                }
            }
        };
        cbMonday.setOnCheckedChangeListener(occl);
        cbTuesday.setOnCheckedChangeListener(occl);
        cbWednesnday.setOnCheckedChangeListener(occl);
        cbThursday.setOnCheckedChangeListener(occl);
        cbFriday.setOnCheckedChangeListener(occl);
        cbSaturday.setOnCheckedChangeListener(occl);
        cbSunday.setOnCheckedChangeListener(occl);
//        builder.setTitle("执行日期");
        builder.setView(inflate);
//        builder.setMultiChoiceItems(week, null, new DialogInterface.OnMultiChoiceClickListener() {
//            StringBuffer sb = new StringBuffer(100);
//
//            @Override
//            public void onClick(DialogInterface dialog, int which, boolean isChecked) {
//                if (isChecked) {
//                    sb.append(week[which]);
//                }
////                Toast.makeText(WeekPickerActivity.this, "你选择了：" + sb.toString(), Toast.LENGTH_SHORT).show();
//                weeks = sb.toString();
//            }
//        });
//        builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
//            @Override
//            public void onClick(DialogInterface dialog, int which) {
//                Intent intent = new Intent();
//                intent.putExtra("weeks", weeks);
//                setResult(RESULT_OK, intent);
//                finish();
//            }
//        });
//        builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
//            @Override
//            public void onClick(DialogInterface dialog, int which) {
//                finish();
//            }
//        });
        WindowManager m = getWindowManager();
        Display d = m.getDefaultDisplay(); // 获取屏幕宽、高用
        WindowManager.LayoutParams p = getWindow().getAttributes(); // 获取对话框当前的参数值
        p.height = (int) (d.getHeight() * 0.4); // 高度设置为屏幕的0.5
        p.width = (int) (d.getWidth() * 0.9); // 宽度设置为屏幕的0.8
//        builder.show().getWindow().setAttributes(p);
        builder.show().getWindow().setAttributes(p);

    }

    public void onWeeksClick(View view) {
        switch (view.getId()) {
            case R.id.bt_mydailog_positive:
                if (weeks != null) {
                    Log.i(">>>>intentweeks", weeks);
                    Intent intent = new Intent();
                    intent.putExtra("weeks", weeks);
                    setResult(RESULT_OK, intent);
                    finish();
                }
                break;
            case R.id.bt_mydailog_negative:
                finish();
                break;
        }
    }
}
