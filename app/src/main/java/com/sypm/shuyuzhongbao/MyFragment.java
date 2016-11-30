package com.sypm.shuyuzhongbao;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.sypm.shuyuzhongbao.api.RetrofitClient;
import com.sypm.shuyuzhongbao.data.DataResult;
import com.sypm.shuyuzhongbao.utils.BaseFragment;
import com.sypm.shuyuzhongbao.utils.RememberHelper;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * “我的”界面
 */

public class MyFragment extends BaseFragment {

    LinearLayout personal, feedback, modifyPass, workTime, exit;
    TextView number, phone;

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        number = (TextView) view.findViewById(R.id.number);
        phone = (TextView) view.findViewById(R.id.phone);
        number.setText("NO." + RememberHelper.getNumber());
        phone.setText(RememberHelper.getPhone());
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        //允许刷新按钮
        setHasOptionsMenu(true);
        super.onCreate(savedInstanceState);


    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_my, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        setupListView();
        initData();
    }

    private void initData() {
        personal = (LinearLayout) getView().findViewById(R.id.personal);
        feedback = (LinearLayout) getView().findViewById(R.id.feedback);
        modifyPass = (LinearLayout) getView().findViewById(R.id.modifyPass);
        workTime = (LinearLayout) getView().findViewById(R.id.workTime);
        exit = (LinearLayout) getView().findViewById(R.id.exit);
        personal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), PersonalActivity.class);
                startActivity(intent);
            }
        });
        feedback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), FeedbackActivity.class);
                startActivity(intent);
            }
        });
        modifyPass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), ModifyPasswordActivity.class);
                startActivity(intent);
            }
        });
        workTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), WorkTimeActivity.class);
                startActivity(intent);
            }
        });
        exit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new AlertDialog.Builder(getActivity())
                        .setTitle("提示")
                        .setMessage("确定退出？")
                        .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                Call<DataResult> logout = RetrofitClient.getInstance().getSYService().logout();
                                logout.enqueue(new Callback<DataResult>() {
                                    @Override
                                    public void onResponse(Call<DataResult> call, Response<DataResult> response) {
                                        if (response.body() != null) {
                                            String status = response.body().status;
                                            if (status.equals("1")) {
                                                Toast.makeText(getActivity(), "已退出，请重新登陆", Toast.LENGTH_SHORT).show();
                                                Intent intent = new Intent(getActivity(), LoginByAccountActivity.class);
                                                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                                startActivity(intent);
                                            } else {
                                                Toast.makeText(getActivity(), response.body().message, Toast.LENGTH_SHORT).show();
                                            }
                                            return;
                                        }
                                        //failed
                                        Toast.makeText(getActivity(), "修改失败", Toast.LENGTH_SHORT).show();
                                    }

                                    @Override
                                    public void onFailure(Call<DataResult> call, Throwable t) {
                                        Toast.makeText(getActivity(), "修改失败", Toast.LENGTH_SHORT).show();
                                    }
                                });
                            }
                        })
                        .setNegativeButton("取消", null).create().show();
            }
        });
    }


    private void setupListView() {

    }


}
