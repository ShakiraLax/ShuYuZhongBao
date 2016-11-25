package com.sypm.shuyuzhongbao;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.sypm.shuyuzhongbao.api.RetrofitClient;
import com.sypm.shuyuzhongbao.data.DataResult;
import com.sypm.shuyuzhongbao.data.WorkTime;
import com.sypm.shuyuzhongbao.utils.BaseActivity;
import com.sypm.shuyuzhongbao.utils.MyBaseAdapter;
import com.sypm.shuyuzhongbao.utils.ToastUtils;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * 上班时间列表
 */

public class WorkTimeActivity extends BaseActivity {

    LinearLayout add;
    ListView listView;
    private List<WorkTime> timeList;
    private ListAdapter listAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_work_time);
//        Log.i(">>>>>onCreate","onCreate");
        setupListView();
        initData();
        setOnListener();
    }

    private void setOnListener() {

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//                Toast.makeText(getApplicationContext(), "点击了第" + position + "条", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(getActivity(), UpdateWorkTimeActivity.class);
                Bundle bundle = new Bundle();
                bundle.putInt("id", timeList.get(position).getId());
                bundle.putString("startTime", timeList.get(position).getStartTime());
                bundle.putString("endTime", timeList.get(position).getEndTime());
                bundle.putString("dayTime", timeList.get(position).getDayTime());
                intent.putExtras(bundle);
                startActivity(intent);
            }
        });

        //实现listview的长按删除item
        listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, final int position, long id) {
//                Toast.makeText(getApplicationContext(), "长按了第" + position + "条", Toast.LENGTH_SHORT).show();
                new AlertDialog.Builder(getActivity())
                        .setTitle("提示")
                        .setIcon(R.mipmap.ic_launcher)
                        .setMessage("确定删除该条记录？")
                        .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                ToastUtils.show("确定");
//                                timeList.remove(position);
                                Call<DataResult> worktimedelete = RetrofitClient.getInstance().getSYService()
                                        .worktimedelete(timeList.get(position).getId());
                                worktimedelete.enqueue(new Callback<DataResult>() {
                                    @Override
                                    public void onResponse(Call<DataResult> call, Response<DataResult> response) {
                                        if (response.isSuccessful()) {
                                            if (response.body().msg.equals("操作成功")) {
                                                ToastUtils.show("操作成功");
                                            } else {
                                                ToastUtils.show("操作失败");
                                            }
                                        }
                                        timeList.remove(position);
                                        listAdapter.refresh(timeList);
                                    }

                                    @Override
                                    public void onFailure(Call<DataResult> call, Throwable t) {

                                    }
                                });
                            }
                        })
                        .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                ToastUtils.show("取消");
                            }
                        }).create().show();
                return true;
            }
        });
    }

    private void setupListView() {
//        List list = new ArrayList();
        timeList = new ArrayList();
//        for (int i = 0; i < 2; i++) {
//            list.add(i);
//        }
        listView = (ListView) findViewById(R.id.listView_worktime);
        listAdapter = new ListAdapter(this, timeList);
        listView.setAdapter(listAdapter);
//        listView.setAdapter(new ListAdapter(this, list));
    }


    private void initData() {
        /*人员上班时间列表*/
        Call<DataResult<WorkTime>> call = RetrofitClient.getInstance().getSYService().getWorkTimeList("asc");
        call.enqueue(new Callback<DataResult<WorkTime>>() {
            @Override
            public void onResponse(Call<DataResult<WorkTime>> call, Response<DataResult<WorkTime>> response) {
                if (response.isSuccessful()) {
                    if (response.body().list != null) {
                        timeList = response.body().list;
//                        Log.i(">>>>>>timeList",timeList.toString());
                        listAdapter.refresh(timeList);
//                        listView.setAdapter(listAdapter);
                    }
                }
            }

            @Override
            public void onFailure(Call<DataResult<WorkTime>> call, Throwable t) {

            }
        });

        add = (LinearLayout) findViewById(R.id.add);
        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), AddWorkTimeActivity.class);
                startActivityForResult(intent, 1000);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1000 && resultCode == RESULT_OK) {

        }
    }

    public class ListAdapter extends MyBaseAdapter {

        public ListAdapter(Context context, List list) {
            super(context, list);
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            ViewHolder holder;
            if (convertView == null) {
                convertView = mInflater.inflate(R.layout.item_work_time, parent, false);
                holder = new ViewHolder();
                holder.txtItemWorkTime = (TextView) convertView.findViewById(R.id.txt_item_worktime);
                holder.txtItemWorkDay = (TextView) convertView.findViewById(R.id.txt_item_workday);
                holder.aSwitch = (Switch) convertView.findViewById(R.id.switch1);
                convertView.setTag(holder);
            } else {
                holder = (ViewHolder) convertView.getTag();
            }
            //赋值
            final WorkTime workTime = timeList.get(position);
            holder.txtItemWorkTime.setText(workTime.getStartTime() + "-" + workTime.getEndTime());
            holder.txtItemWorkDay.setText(workTime.getDayTime());
            if (workTime.getIsOpen() == 1) {
                holder.aSwitch.setChecked(true);
            }
            //switch开关设置点击事件
            holder.aSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    if (isChecked) {
                        Call<DataResult> timeOffOn = RetrofitClient.getInstance().getSYService()
                                .worktimeOffOn(workTime.getId(), "1");
                        timeOffOn.enqueue(new Callback<DataResult>() {
                            @Override
                            public void onResponse(Call<DataResult> call, Response<DataResult> response) {
                                if (response.isSuccessful()) {
                                    if (response.body().status.equals("1")) {
                                        Toast.makeText(getApplicationContext(), "操作成功",
                                                Toast.LENGTH_SHORT).show();
                                    } else {
                                        Toast.makeText(getApplicationContext(), "操作失败",
                                                Toast.LENGTH_SHORT).show();
                                    }
                                }
                            }

                            @Override
                            public void onFailure(Call<DataResult> call, Throwable t) {

                            }
                        });
                    } else {
                        Call<DataResult> timeOffOn = RetrofitClient.getInstance().getSYService()
                                .worktimeOffOn(workTime.getId(), "2");
                        timeOffOn.enqueue(new Callback<DataResult>() {
                            @Override
                            public void onResponse(Call<DataResult> call, Response<DataResult> response) {
                                if (response.isSuccessful()) {
                                    if (response.body().status.equals("1")) {
                                        Toast.makeText(getApplicationContext(), response.body().msg,
                                                Toast.LENGTH_SHORT).show();
                                    } else {
                                        Toast.makeText(getApplicationContext(), "操作失败",
                                                Toast.LENGTH_SHORT).show();
                                    }
                                }
                            }

                            @Override
                            public void onFailure(Call<DataResult> call, Throwable t) {

                            }
                        });
                    }
                }
            });
            return convertView;
        }
    }

    private static class ViewHolder {
        TextView txtItemWorkTime;
        TextView txtItemWorkDay;
        Switch aSwitch;
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        //重新获取时间列表
        Call<DataResult<WorkTime>> call = RetrofitClient.getInstance().getSYService().getWorkTimeList("asc");
        call.enqueue(new Callback<DataResult<WorkTime>>() {
            @Override
            public void onResponse(Call<DataResult<WorkTime>> call, Response<DataResult<WorkTime>> response) {
                if (response.isSuccessful()) {
                    if (response.body().list != null) {
                        timeList = response.body().list;
//                        Log.i(">>>>>>timeList",timeList.toString());
                        listAdapter.refresh(timeList);
//                        listView.setAdapter(listAdapter);
                    }
                }
            }

            @Override
            public void onFailure(Call<DataResult<WorkTime>> call, Throwable t) {

            }
        });
    }
}
