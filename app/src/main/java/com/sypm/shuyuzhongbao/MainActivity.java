package com.sypm.shuyuzhongbao;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.sypm.shuyuzhongbao.api.RetrofitClient;
import com.sypm.shuyuzhongbao.data.DataResult;
import com.sypm.shuyuzhongbao.utils.ExampleUtil;
import com.sypm.shuyuzhongbao.utils.FragmentManagerActivity;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends FragmentManagerActivity {

    private static final int INDEX = 0;

    private static final int MONEY = 1;

    private static final int MY = 2;

    private TabLayout tabLayout;

    public static boolean isForeground = false;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView();
        if (savedInstanceState == null) {
            addFragment(INDEX);
        }
    }

    private void initView() {
        tabLayout = (TabLayout) findViewById(R.id.tabLayout);
        tabLayout.addTab(tabLayout.newTab().setCustomView(createCustomTab("首页", R.drawable.main_tab_one)), true);
        tabLayout.addTab(tabLayout.newTab().setCustomView(createCustomTab("收入", R.drawable.main_tab_two)));
        tabLayout.addTab(tabLayout.newTab().setCustomView(createCustomTab("我的", R.drawable.main_tab_three)));
        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                switch (tab.getPosition()) {
                    case INDEX:
                        addFragment(INDEX);
                        break;
                    case MONEY:
                        addFragment(MONEY);
                        break;
                    case MY:
                        addFragment(MY);
                        break;
                }
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
    }

    @Override
    public Fragment createFragment(int index) {
        switch (index) {
            case INDEX:
                return new IndexFragment();
            case MONEY:
                return new MoneyFragment();
            case MY:
                return new MyFragment();
        }
        return null;
    }


    View createCustomTab(String text, int iconRes) {
        View view = getLayoutInflater().inflate(R.layout.view_custom_tab, null);
        TextView textView = (TextView) view.findViewById(R.id.tabText);
        ImageView imageView = (ImageView) view.findViewById(R.id.tabIcon);
        textView.setText(text);
        imageView.setImageResource(iconRes);
        return view;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Call<DataResult> call = RetrofitClient.getInstance().getSYService().line("2");
        call.enqueue(new Callback<DataResult>() {
            @Override
            public void onResponse(Call<DataResult> call, Response<DataResult> response) {

            }

            @Override
            public void onFailure(Call<DataResult> call, Throwable t) {

            }
        });
    }


    @Override
    public void onBackPressed() {
        exitApp();
    }

    private long exitTime = 0;

    //再按一次退出
    private void exitApp() {
        if ((System.currentTimeMillis() - exitTime) > 2000) {
            Toast.makeText(this, "再按一次退出应用并下线！", Toast.LENGTH_SHORT).show();
            exitTime = System.currentTimeMillis();
        } else {
            finish();
        }
    }

    /*--------极光推送--------------*/
    //for receive customer msg from jpush server
    private MessageReceiver mMessageReceiver;
    public static final String MESSAGE_RECEIVED_ACTION = "com.example.jpushdemo.MESSAGE_RECEIVED_ACTION";
    public static final String KEY_TITLE = "title";
    public static final String KEY_MESSAGE = "message";
    public static final String KEY_EXTRAS = "extras";

    public void registerMessageReceiver() {
        mMessageReceiver = new MessageReceiver();
        IntentFilter filter = new IntentFilter();
        filter.setPriority(IntentFilter.SYSTEM_HIGH_PRIORITY);
        filter.addAction(MESSAGE_RECEIVED_ACTION);
        registerReceiver(mMessageReceiver, filter);
    }

    public class MessageReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            if (MESSAGE_RECEIVED_ACTION.equals(intent.getAction())) {
                String messge = intent.getStringExtra(KEY_MESSAGE);
                String extras = intent.getStringExtra(KEY_EXTRAS);
                StringBuilder showMsg = new StringBuilder();
                showMsg.append(KEY_MESSAGE + " : " + messge + "\n");
                if (!ExampleUtil.isEmpty(extras)) {
                    showMsg.append(KEY_EXTRAS + " : " + extras + "\n");
                }
                setCostomMsg(showMsg.toString());
            }
        }
    }

    private void setCostomMsg(String msg) {
        /*if (null != msgText) {
            msgText.setText(msg);
            msgText.setVisibility(android.view.View.VISIBLE);
        }*/
    }
}
