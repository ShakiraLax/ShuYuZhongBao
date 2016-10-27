package com.sypm.shuyuzhongbao;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.sypm.shuyuzhongbao.utils.BaseFragment;
import com.sypm.shuyuzhongbao.utils.MyBaseAdapter;

import java.util.ArrayList;
import java.util.List;

/**
 * 首页
 */

public class MoneyFragment extends BaseFragment {

    ListView listView;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        //允许刷新按钮
        setHasOptionsMenu(true);
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_money, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        setupListView();
        initData();
    }

    private void initData() {

    }


    private void setupListView() {
        List list = new ArrayList();
        for (int i = 0; i < 20; i++) {
            list.add(i);
        }
        listView = (ListView) getView().findViewById(R.id.listView);
        listView.setAdapter(new ListAdapter(getContext(), list));
    }

    public static class ListAdapter extends MyBaseAdapter {

        public ListAdapter(Context context, List list) {
            super(context, list);
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            ViewHolder holder;
            if (convertView == null) {
                convertView = mInflater.inflate(R.layout.item_order_list, parent, false);
                holder = new ViewHolder();
                convertView.setTag(holder);
            } else {
                holder = (ViewHolder) convertView.getTag();
            }

            return convertView;
        }
    }

    private static class ViewHolder {

    }

}
