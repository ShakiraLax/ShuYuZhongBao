package com.sypm.shuyuzhongbao;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.sypm.shuyuzhongbao.utils.FragmentManagerActivity;

public class MainActivity extends FragmentManagerActivity {

    private static final int INDEX = 0;

    private static final int MONEY = 1;

    private static final int MY = 2;

    private TabLayout tabLayout;

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
    }
}
