package com.sypm.shuyuzhongbao;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.Toolbar;

import com.sypm.shuyuzhongbao.api.RetrofitClient;
import com.sypm.shuyuzhongbao.data.DataResult;
import com.sypm.shuyuzhongbao.utils.BaseFragment;
import com.sypm.shuyuzhongbao.utils.MyBaseAdapter;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * 收入列表
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
        Call<DataResult> call = RetrofitClient.getInstance().getSYService().salaryList("1", null);
        call.enqueue(new Callback<DataResult>() {
            @Override
            public void onResponse(Call<DataResult> call, Response<DataResult> response) {

            }

            @Override
            public void onFailure(Call<DataResult> call, Throwable t) {

            }
        });
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
