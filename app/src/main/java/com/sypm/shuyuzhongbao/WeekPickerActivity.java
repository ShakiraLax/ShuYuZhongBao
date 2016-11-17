package com.sypm.shuyuzhongbao;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.widget.Toast;

import com.sypm.shuyuzhongbao.utils.BaseActivity;

/**
 * 执行日期选择
 * */

public class WeekPickerActivity extends BaseActivity {

    String weeks = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_week_picker);

        AlertDialog.Builder builder = new AlertDialog.Builder(WeekPickerActivity.this);
        builder.setTitle("请选择周几执行");
        final String[] week = {"一", "二", "三", "四", "五", "六", "日"};
        //    设置一个多项选择下拉框
        /**
         * 第一个参数指定我们要显示的一组下拉多选框的数据集合
         * 第二个参数代表哪几个选项被选择，如果是null，则表示一个都不选择，如果希望指定哪一个多选选项框被选择，
         * 需要传递一个boolean[]数组进去，其长度要和第一个参数的长度相同，例如 {true, false, false, true};
         * 第三个参数给每一个多选项绑定一个监听器
         */
        builder.setMultiChoiceItems(week, null, new DialogInterface.OnMultiChoiceClickListener() {
            StringBuffer sb = new StringBuffer(100);

            @Override
            public void onClick(DialogInterface dialog, int which, boolean isChecked) {
                if (isChecked) {
                    sb.append(week[which]);
                }
                Toast.makeText(WeekPickerActivity.this, "你选择了：" + sb.toString(), Toast.LENGTH_SHORT).show();
                weeks = sb.toString();
            }
        });
        builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Intent intent = new Intent();
                intent.putExtra("weeks", weeks);
                setResult(RESULT_OK, intent);
                finish();
            }
        });
        builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });
        builder.show();
    }
}
