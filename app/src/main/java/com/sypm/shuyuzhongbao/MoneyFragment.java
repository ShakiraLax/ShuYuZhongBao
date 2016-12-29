package com.sypm.shuyuzhongbao;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.sypm.shuyuzhongbao.api.RetrofitClient;
import com.sypm.shuyuzhongbao.data.MoneyList;
import com.sypm.shuyuzhongbao.utils.BaseFragment;
import com.sypm.shuyuzhongbao.utils.LoadingFooter;
import com.sypm.shuyuzhongbao.utils.MyBaseAdapter;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * 收入列表
 */

public class MoneyFragment extends BaseFragment {

    ListView listView;
    TextView today, total;
    List<MoneyList.ListBean> moneyList;

    LinearLayout refresh, refresh2;

    /*下拉加载更多*/
    private LoadingFooter mLoadingFooter;
    private MoneyListAdapter moneyListAdapter;
    private int page = 1;

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
        refresh = (LinearLayout) getView().findViewById(R.id.refresh);
        refresh2 = (LinearLayout) getView().findViewById(R.id.refresh2);
        refresh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
        refresh2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
        setupListView();
        setupLoadingFooter();
        initData();
    }

    private void setupLoadingFooter() {
        mLoadingFooter = new LoadingFooter(getActivity());
        listView.addFooterView(mLoadingFooter);
        listView.setFooterDividersEnabled(false);
        listView.setOnScrollListener(mLoadingFooter);
        mLoadingFooter.setOnLoadMoreListener(new LoadingFooter.OnLoadMoreListener() {
            @Override
            public void onLoadMore() {
                mLoadingFooter.setLoadState(LoadingFooter.State.Loading);
                page++;
                loadMoreData();
            }
        });
    }

    private void loadMoreData() {
        Call<MoneyList> call = RetrofitClient.getInstance().getSYService().salaryList(String.valueOf(page), "20");
        call.enqueue(new Callback<MoneyList>() {
            @Override
            public void onResponse(Call<MoneyList> call, final Response<MoneyList> response) {
                if (response.body() == null || response.body().list == null || response.body().list.isEmpty()) {
                    mLoadingFooter.setLoadState(LoadingFooter.State.End);
                    return;
                }
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        moneyListAdapter.addAll(response.body().list);
                        mLoadingFooter.setLoadState(LoadingFooter.State.Default);
                    }
                }, 1000);
            }

            //由于数据结构发生变化造成解析失败
            @Override
            public void onFailure(Call<MoneyList> call, Throwable t) {
                mLoadingFooter.setLoadState(LoadingFooter.State.End);
                /*mLoadingFooter.setLoadState(LoadingFooter.State.Error);
                page--;*/
            }
        });
    }

    private void initData() {
        today = (TextView) getView().findViewById(R.id.today);
        total = (TextView) getView().findViewById(R.id.total);

        Call<MoneyList> call = RetrofitClient.getInstance().getSYService().salaryList("1", "20");
        call.enqueue(new Callback<MoneyList>() {
            @Override
            public void onResponse(Call<MoneyList> call, Response<MoneyList> response) {
                if (response.body() != null) {
                    if (response.isSuccessful()) {
                        moneyList = response.body().list;
                        moneyListAdapter.refresh(moneyList);
//                        listView.setAdapter(new MoneyListAdapter(getActivity(), moneyList));
                        today.setText(response.body().todaytotal + "元");
                        total.setText(response.body().total + "元");
                    } else {
                        Toast.makeText(getActivity(), "无数据", Toast.LENGTH_SHORT).show();
                    }
                }
            }

            @Override
            public void onFailure(Call<MoneyList> call, Throwable t) {
                Toast.makeText(getActivity(), "无数据", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void setupListView() {
        listView = (ListView) getView().findViewById(R.id.listView);
        listView.setAdapter(new MoneyListAdapter(getContext(), null));
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (moneyList != null) {
                    Intent intent = new Intent(getActivity(), OrderDetailActivity2.class);
                    intent.putExtra("item", moneyList.get(position));
                    startActivity(intent);
                }
            }
        });
        moneyListAdapter = new MoneyListAdapter(getActivity(), moneyList);
        listView.setAdapter(moneyListAdapter);
    }

    public static class MoneyListAdapter extends MyBaseAdapter {

        public MoneyListAdapter(Context context, List list) {
            super(context, list);
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            ViewHolder holder;
            if (convertView == null) {
                convertView = mInflater.inflate(R.layout.item_order_list, parent, false);
                holder = new ViewHolder();

                holder.createTime = (TextView) convertView.findViewById(R.id.createTime);
                holder.fee = (TextView) convertView.findViewById(R.id.fee);
                holder.status = (TextView) convertView.findViewById(R.id.status);
                holder.shipSn = (TextView) convertView.findViewById(R.id.shipSn);
                convertView.setTag(holder);
            } else {
                holder = (ViewHolder) convertView.getTag();
            }
            MoneyList.ListBean item = (MoneyList.ListBean) getItem(position);
            holder.createTime.setText(item.createTime);
            holder.fee.setText(item.fee);
            holder.status.setText(item.status);
            holder.shipSn.setText(item.shipSn);
            return convertView;
        }
    }

    private static class ViewHolder {
        public TextView fee;
        public TextView status;
        public TextView createTime;
        public TextView shipSn;
    }

}
