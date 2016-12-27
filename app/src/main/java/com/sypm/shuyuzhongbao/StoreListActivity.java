package com.sypm.shuyuzhongbao;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.provider.Settings;
import android.support.v7.app.AlertDialog;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.sypm.shuyuzhongbao.api.RetrofitClient;
import com.sypm.shuyuzhongbao.data.DataResult;
import com.sypm.shuyuzhongbao.data.OrderBySn;
import com.sypm.shuyuzhongbao.data.OrderList;
import com.sypm.shuyuzhongbao.data.SelecteOrder;
import com.sypm.shuyuzhongbao.utils.BaseActivity;
import com.sypm.shuyuzhongbao.utils.MyBaseAdapter;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class StoreListActivity extends BaseActivity {

    private EditText searchText;
    private Button search;
    private ListView listView;
    private String SHIPSN, STORESN;
    private OrderList orderList;
    private ListAdapter adapter;
    private List<OrderList.ListBean> list;
    private ProgressDialog dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_store_list);
        dialog = new ProgressDialog(this);
        dialog.setMessage("正在获取门店信息...");
        /*接单界面传过来的sn*/
        SHIPSN = getIntent().getStringExtra("shipSnFromOrderDetail2");
        if (SHIPSN != null) {
            Log.d("shipSnFromOrderDetail2", SHIPSN);
            initData(SHIPSN);
        }
        initView();
    }

    private void initData(String shipSn) {
        dialog.show();
        final Call<OrderList> storeList = RetrofitClient.getInstance().getSYService().storeList(shipSn, "5000");
        storeList.enqueue(new Callback<OrderList>() {
            @Override
            public void onResponse(Call<OrderList> call, Response<OrderList> response) {
                if (dialog != null && dialog.isShowing()) {
                    dialog.cancel();
                }
                if (response.body() != null) {
                    if (response.body().status == 1) {
                        orderList = response.body();
                        listView.setAdapter(new ListAdapter(getActivity(), orderList.list));
                    }
                }
            }

            @Override
            public void onFailure(Call<OrderList> call, Throwable t) {
                if (dialog != null && dialog.isShowing()) {
                    dialog.cancel();
                }
                Toast.makeText(getActivity(), "获取失败", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void initView() {
        searchText = (EditText) findViewById(R.id.searchText);
        search = (Button) findViewById(R.id.search);
        /*搜索门店*/
        search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!searchText.getText().toString().equals("")) {
                    final Call<OrderList> searchStore = RetrofitClient.getInstance().getSYService().searchStore(searchText.getText().toString());
                    searchStore.enqueue(new Callback<OrderList>() {
                        @Override
                        public void onResponse(Call<OrderList> call, Response<OrderList> response) {
                            if (response.body() != null) {
                                if (response.body().status == 1) {
                                    listView.setAdapter(new ListAdapter(getActivity(), response.body().list));
                                    Toast.makeText(getActivity(), response.body().msg, Toast.LENGTH_SHORT).show();
                                } else if (response.body().status == 0) {
                                    Toast.makeText(getActivity(), response.body().msg, Toast.LENGTH_SHORT).show();
                                } else {
                                    Toast.makeText(getActivity(), response.body().msg, Toast.LENGTH_SHORT).show();
                                }
                            }
                        }

                        @Override
                        public void onFailure(Call<OrderList> call, Throwable t) {
                            Toast.makeText(getActivity(), "获取失败", Toast.LENGTH_SHORT).show();
                        }
                    });
                } else {
                    Toast.makeText(getActivity(), "请输入门店名或者门店编码", Toast.LENGTH_SHORT).show();
                }
            }
        });
        /*修改门店*/
        listView = (ListView) findViewById(R.id.listView);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, final int position, final long id) {
                ListAdapter adapter = (ListAdapter) listView.getAdapter();
                OrderList.ListBean item = (OrderList.ListBean) adapter.getItem(position);
                STORESN = item.storeSn;
                AlertDialog.Builder dialog = new AlertDialog.Builder(getActivity());
                dialog.setMessage("是否修改为" + item.storeName);
                dialog.setPositiveButton("确定",
                        new android.content.DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface arg0, int arg1) {
                                final Call<DataResult> setStore = RetrofitClient.getInstance().getSYService().setStore(STORESN, SHIPSN);
                                setStore.enqueue(new Callback<DataResult>() {
                                    @Override
                                    public void onResponse(Call<DataResult> call, Response<DataResult> response) {
                                        if (response.body() != null) {
                                            if (response.body().status.equals("1")) {
//                                                list = response.body().list;
                                                Toast.makeText(getActivity(), response.body().msg, Toast.LENGTH_SHORT).show();
                                                setResult(RESULT_OK);
                                                finish();
                                            }
                                        }
                                    }

                                    @Override
                                    public void onFailure(Call<DataResult> call, Throwable t) {
                                        Toast.makeText(getActivity(), "获取失败", Toast.LENGTH_SHORT).show();
                                    }
                                });
                            }
                        });
                dialog.setNeutralButton("取消", new android.content.DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface arg0, int arg1) {
                        arg0.dismiss();
                    }
                });
                dialog.show();
            }
        });
    }

    public void searchOnClick(View view) {

    }

    public static class ListAdapter extends MyBaseAdapter {

        public ListAdapter(Context context, List list) {
            super(context, list);
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            ViewHolder holder;
            if (convertView == null) {
                convertView = mInflater.inflate(R.layout.item_search_list, parent, false);
                holder = new ViewHolder();
                holder.storeName = (TextView) convertView.findViewById(R.id.storeName);
                holder.distance = (TextView) convertView.findViewById(R.id.distance);
                convertView.setTag(holder);
            } else {
                holder = (ViewHolder) convertView.getTag();
            }
            final OrderList.ListBean item = (OrderList.ListBean) getItem(position);
            holder.storeName.setText(item.storeName);
            holder.distance.setText(item.distance + "米");
            return convertView;
        }
    }

    private static class ViewHolder {
        public TextView storeName;
        public TextView distance;
    }
}
