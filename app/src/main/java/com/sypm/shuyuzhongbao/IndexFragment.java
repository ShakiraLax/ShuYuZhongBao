package com.sypm.shuyuzhongbao;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationClient;
import com.amap.api.location.AMapLocationClientOption;
import com.amap.api.location.AMapLocationListener;
import com.amap.api.maps.AMap;
import com.amap.api.maps.CameraUpdateFactory;
import com.amap.api.maps.LocationSource;
import com.amap.api.maps.MapView;
import com.amap.api.maps.UiSettings;
import com.amap.api.maps.model.BitmapDescriptorFactory;
import com.amap.api.maps.model.MyLocationStyle;
import com.sypm.shuyuzhongbao.api.RetrofitClient;
import com.sypm.shuyuzhongbao.data.DataResult;
import com.sypm.shuyuzhongbao.data.OrderBySn;
import com.sypm.shuyuzhongbao.data.SelecteOrder;
import com.sypm.shuyuzhongbao.data.TotalLine;
import com.sypm.shuyuzhongbao.utils.BaseFragment;
import com.sypm.shuyuzhongbao.utils.MyBaseAdapter;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.app.Activity.RESULT_OK;

/**
 * 首页
 */

public class IndexFragment extends BaseFragment implements LocationSource, AMapLocationListener {

    private ListView listView;
    private int recLen = 0;
    private boolean isOnline = true;
    private List<SelecteOrder.ListBean> list;
    private TextView endure, accept_num, salary, percent;
    private LinearLayout refresh;
    private TotalLine totalLine;
    private double WD, JD;//纬度lat,经度lng
    private boolean isFirstPass = true;

    /*高德start*/
    //显示地图需要的变量
    private MapView mapView;//地图控件
    private AMap aMap;//地图对象

    //定位需要的声明
    private AMapLocationClient mLocationClient = null;//定位发起端
    private AMapLocationClientOption mLocationOption = null;//定位参数
    private OnLocationChangedListener mListener = null;//定位监听器

    //标识，用于判断是否只显示一次定位信息和用户重新定位
    private boolean isFirstLoc = true;
    /*高德end*/

    Handler handler = new Handler();
    Runnable runnable = new Runnable() {
        @Override
        public void run() {
            recLen++;
            updateInfo();
            //每隔60秒上传一次位置信息
            handler.postDelayed(this, 60000);
        }
    };
    private TextView txtMyOrder;
    private TextView txtWorkingOrder;
    private TextView txtPassOrder;
    private TextView txtPassOrder2;
    private View viewLine1;
    private View viewLine2;
    private View viewLine3;
    private View viewLine4;
    private Calendar calendar;
    private ListAdapter listAdapter;
    private TextView txtOnlinLeft;
    private TextView txtOnlinRight;

    private void updateInfo() {
        /*上线后开始上传位置信息*/
        if (isOnline) {
            Call<DataResult> call = RetrofitClient.getInstance().getSYService().locationInsert(String.valueOf(WD), String.valueOf(JD), null);
            call.enqueue(new Callback<DataResult>() {
                @Override
                public void onResponse(Call<DataResult> call, Response<DataResult> response) {
                    if (response.body() != null) {
                        if (response.body().status.equals("1")) {
                            Log.d("上线后定位信息上传", "纬度" + String.valueOf(WD) + "经度" + String.valueOf(JD));
                        } else {

                        }
                    }
                }

                @Override
                public void onFailure(Call<DataResult> call, Throwable t) {

                }
            });
        }
        return;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        //允许刷新按钮
        setHasOptionsMenu(true);
        super.onCreate(savedInstanceState);
        runnable.run();
    }

    private void initView(View inflate) {
        txtMyOrder = (TextView) inflate.findViewById(R.id.txt_myorder_index);
        txtWorkingOrder = (TextView) inflate.findViewById(R.id.txt_workingorder_index);
        txtPassOrder = (TextView) inflate.findViewById(R.id.txt_passorder_index);
        txtPassOrder2 = (TextView) inflate.findViewById(R.id.txt_passorder2_index);
        viewLine1 = inflate.findViewById(R.id.view_line1);
        viewLine2 = inflate.findViewById(R.id.view_line2);
        viewLine3 = inflate.findViewById(R.id.view_line3);
        viewLine4 = inflate.findViewById(R.id.view_line4);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View inflate = inflater.inflate(R.layout.fragment_index, container, false);
        initView(inflate);
        return inflate;

    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        calendar = Calendar.getInstance();
        endure = (TextView) getView().findViewById(R.id.endure);
        accept_num = (TextView) getView().findViewById(R.id.accept_num);
        salary = (TextView) getView().findViewById(R.id.salary);
        percent = (TextView) getView().findViewById(R.id.percent);
        listView = (ListView) getView().findViewById(R.id.listView);
        txtOnlinLeft = (TextView) getView().findViewById(R.id.txt_online_left);
        txtOnlinRight = (TextView) getView().findViewById(R.id.txt_online_right);
        refresh = (LinearLayout) getView().findViewById(R.id.refresh);
        refresh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                initData();
            }
        });
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (list != null) {
                    /*if (viewLine1.getVisibility() == View.VISIBLE) {
                        Intent intent = new Intent(getActivity(), GrabOrderActivity.class);
                        intent.putExtra("yipaidan", list.get(position).getShipSn());
                        Log.i("yipaidan", list.get(position).getShipSn());
                        startActivityForResult(intent, 1000);
                    } else if (viewLine2.getVisibility() == View.VISIBLE) {
                        Intent intent = new Intent(getActivity(), OrderDetailActivity.class);
                        intent.putExtra("shipSn", list.get(position).getShipSn());
                        Log.i("shipSn", list.get(position).getShipSn());
                        startActivityForResult(intent, 2000);
                    } else {
                        Toast.makeText(getActivity(), "已拒单不可查看", Toast.LENGTH_SHORT).show();
                    }*/
                    if (viewLine4.getVisibility() == View.VISIBLE) {
                        Toast.makeText(getActivity(), "已拒单不可查看", Toast.LENGTH_SHORT).show();
                    } else {
                        Intent intent = new Intent(getActivity(), OrderDetailActivity2.class);
                        intent.putExtra("shipSn", list.get(position).getShipSn());
                        startActivityForResult(intent, 2000);
                    }
                }
            }
        });
        list = new ArrayList<>();
        autoLine();
        initData();
        setupListView();

        //显示地图
        mapView = (MapView) getView().findViewById(R.id.mapView);
        //必须要写
        mapView.onCreate(savedInstanceState);
        //获取地图对象
        aMap = mapView.getMap();

        //设置显示定位按钮 并且可以点击
        UiSettings settings = aMap.getUiSettings();
        //设置定位监听
        aMap.setLocationSource(this);
        // 是否显示定位按钮
        settings.setMyLocationButtonEnabled(true);
        // 是否可触发定位并显示定位层
        aMap.setMyLocationEnabled(true);

        //定位的小图标 默认是蓝点 这里自定义一箭头，其实就是一张图片
        MyLocationStyle myLocationStyle = new MyLocationStyle();
        myLocationStyle.myLocationIcon(BitmapDescriptorFactory.fromResource(R.drawable.location_marker));//已更改
        myLocationStyle.radiusFillColor(android.R.color.transparent);
        myLocationStyle.strokeColor(android.R.color.transparent);
        aMap.setMyLocationStyle(myLocationStyle);

        //开始定位
        initLoc();


        //筛选订单
        txtMyOrder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                txtMyOrder.setTextColor(0xfff7ac47);
                txtWorkingOrder.setTextColor(0xff92928e);
                txtPassOrder.setTextColor(0xff92928e);
                txtPassOrder2.setTextColor(0xff92928e);
                viewLine1.setVisibility(View.VISIBLE);
                viewLine2.setVisibility(View.INVISIBLE);
                viewLine3.setVisibility(View.INVISIBLE);
                viewLine4.setVisibility(View.INVISIBLE);
                list.clear();
                listAdapter = new ListAdapter(getContext(), list, isOnline);
                listView.setAdapter(listAdapter);
                Call<SelecteOrder> selectCall = RetrofitClient.getInstance().getSYService().selecteOrder("5", getDate(), "1");
                selectCall.enqueue(new Callback<SelecteOrder>() {
                    @Override
                    public void onResponse(Call<SelecteOrder> call, Response<SelecteOrder> response) {
                        if (response.isSuccessful()) {
                            if (response.body().getStatus() == 1) {
                                list = response.body().getList();
//                                listView.setAdapter(new ListAdapter(getContext(), list));
                                listAdapter.refresh(list);
                            }
                        }
                    }

                    @Override
                    public void onFailure(Call<SelecteOrder> call, Throwable t) {

                    }
                });
            }
        });
        txtWorkingOrder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                txtMyOrder.setTextColor(0xff92928e);
                txtWorkingOrder.setTextColor(0xfff7ac47);
                txtPassOrder.setTextColor(0xff92928e);
                txtPassOrder2.setTextColor(0xff92928e);
                viewLine1.setVisibility(View.INVISIBLE);
                viewLine2.setVisibility(View.VISIBLE);
                viewLine3.setVisibility(View.INVISIBLE);
                viewLine4.setVisibility(View.INVISIBLE);
                list.clear();
                listAdapter = new ListAdapter(getContext(), list, isOnline);
                listView.setAdapter(listAdapter);
                Call<SelecteOrder> selectCall = RetrofitClient.getInstance().getSYService().selecteOrder("6", getDate(), "1");
                selectCall.enqueue(new Callback<SelecteOrder>() {
                    @Override
                    public void onResponse(Call<SelecteOrder> call, Response<SelecteOrder> response) {
                        if (response.isSuccessful()) {
                            if (response.body().getStatus() == 1) {
                                list = response.body().getList();
//                                listView.setAdapter(new ListAdapter(getContext(), list));
                                listAdapter.refresh(list);
                            }
                        }
                    }

                    @Override
                    public void onFailure(Call<SelecteOrder> call, Throwable t) {

                    }
                });
            }
        });
        txtPassOrder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                txtMyOrder.setTextColor(0xff92928e);
                txtWorkingOrder.setTextColor(0xff92928e);
                txtPassOrder.setTextColor(0xfff7ac47);
                txtPassOrder2.setTextColor(0xff92928e);
                viewLine1.setVisibility(View.INVISIBLE);
                viewLine2.setVisibility(View.INVISIBLE);
                viewLine3.setVisibility(View.VISIBLE);
                viewLine4.setVisibility(View.INVISIBLE);
                list.clear();
                listAdapter = new ListAdapter(getContext(), list, isOnline);
                listView.setAdapter(listAdapter);
                Call<SelecteOrder> selectCall = RetrofitClient.getInstance().getSYService().selecteOrder("4", getDate(), "1");
                selectCall.enqueue(new Callback<SelecteOrder>() {
                    @Override
                    public void onResponse(Call<SelecteOrder> call, Response<SelecteOrder> response) {
                        if (response.isSuccessful()) {
                            if (response.body().getStatus() == 1) {
                                list = response.body().getList();
//                                listView.setAdapter(new ListAdapter(getContext(), list));
                                listAdapter.refresh(list);
                            }
                        }
                    }

                    @Override
                    public void onFailure(Call<SelecteOrder> call, Throwable t) {

                    }
                });
            }
        });
        txtPassOrder2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                txtMyOrder.setTextColor(0xff92928e);
                txtWorkingOrder.setTextColor(0xff92928e);
                txtPassOrder.setTextColor(0xff92928e);
                txtPassOrder2.setTextColor(0xfff7ac47);
                viewLine1.setVisibility(View.INVISIBLE);
                viewLine2.setVisibility(View.INVISIBLE);
                viewLine3.setVisibility(View.INVISIBLE);
                viewLine4.setVisibility(View.VISIBLE);
                list.clear();
                listAdapter = new ListAdapter(getContext(), list, isOnline);
                listView.setAdapter(listAdapter);
                Call<SelecteOrder> selectCall = RetrofitClient.getInstance().getSYService().selecteOrder("3", getDate(), "1");
                selectCall.enqueue(new Callback<SelecteOrder>() {
                    @Override
                    public void onResponse(Call<SelecteOrder> call, Response<SelecteOrder> response) {
                        if (response.isSuccessful()) {
                            if (response.body().getStatus() == 1) {
                                list = response.body().getList();
//                                listView.setAdapter(new ListAdapter(getContext(), list));
                                listAdapter.refresh(list);
                            }
                        }
                    }

                    @Override
                    public void onFailure(Call<SelecteOrder> call, Throwable t) {

                    }
                });
            }
        });
    }

    /*fragment切换时执行*/
    /*@Override
    public void onHiddenChanged(boolean hidden) {
        Log.d("生命周期", "执行");
        super.onHiddenChanged(hidden);
        setupListView();
    }*/

    private void autoLine() {
        /*自动上线*/
        Call<DataResult> call = RetrofitClient.getInstance().getSYService().line("1");
        call.enqueue(new Callback<DataResult>() {
            @Override
            public void onResponse(Call<DataResult> call, Response<DataResult> response) {
//                Log.d("自动上线", response.body().msg);
            }

            @Override
            public void onFailure(Call<DataResult> call, Throwable t) {

            }
        });
    }

    private void initData() {
        /*未接受订单*/
        Call<OrderBySn> getOrder = RetrofitClient.getInstance().getSYService().getOrder();
        getOrder.enqueue(new Callback<OrderBySn>() {
            @Override
            public void onResponse(Call<OrderBySn> call, Response<OrderBySn> response) {
                if (response.body() != null) {
                    if (response.body().status == 1) {
                        //跳转到接单界面
                        Intent intent = new Intent(getActivity(), GrabOrderActivity2.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//                        startActivity(intent);
                        startActivityForResult(intent, 1000);
                    } else {

                    }
                }
            }

            @Override
            public void onFailure(Call<OrderBySn> call, Throwable t) {

            }
        });
        /*在线时长等信息(是否需要实时获取)*/
        Call<TotalLine> summary = RetrofitClient.getInstance().getSYService().summary();
        summary.enqueue(new Callback<TotalLine>() {
            @Override
            public void onResponse(Call<TotalLine> call, Response<TotalLine> response) {
                if (response.body() != null) {
                    if (response.isSuccessful()) {
//                        Toast.makeText(getActivity(), "总览信息获取成功", Toast.LENGTH_SHORT).show();
                        totalLine = response.body();
                        endure.setText("在线：" + totalLine.endure + "小时");
                        accept_num.setText("接单：" + totalLine.accept_num);
                        salary.setText("流水：" + totalLine.salary + "元");
                        percent.setText("成交率：" + totalLine.percent + "%");
                    } else {
                        Toast.makeText(getActivity(), "头部信息无数据", Toast.LENGTH_SHORT).show();
                    }
                }
            }

            @Override
            public void onFailure(Call<TotalLine> call, Throwable t) {

            }
        });

        /*上线按钮点击事件*/
        txtOnlinRight.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (txtOnlinRight.getText().equals("我要下线")) {
                    Call<DataResult> call = RetrofitClient.getInstance().getSYService().line("2");
                    call.enqueue(new Callback<DataResult>() {
                        @Override
                        public void onResponse(Call<DataResult> call, Response<DataResult> response) {
                            Snackbar snackbar = Snackbar.make(getView(), "下线成功！", Snackbar.LENGTH_LONG).setAction("Action", null);
                            snackbar.getView().setBackgroundResource(R.color.orange);
                            snackbar.setActionTextColor(Color.WHITE);
                            snackbar.show();
                            txtOnlinLeft.setText("我要上线");
                            txtOnlinLeft.setBackgroundResource(R.drawable.rect_offleft);
                            txtOnlinRight.setText("离线中");
                            txtOnlinRight.setBackgroundResource(R.drawable.rect_offright);
                            refresh.setBackgroundResource(R.color.translucent_background);
                            endure.setText("已离线");
                            endure.setTextColor(Color.GRAY);
                            accept_num.setTextColor(Color.GRAY);
                            salary.setTextColor(Color.GRAY);
                            percent.setTextColor(Color.GRAY);
                            txtMyOrder.setTextColor(Color.GRAY);
                            txtMyOrder.setClickable(false);
                            txtWorkingOrder.setTextColor(Color.GRAY);
                            txtWorkingOrder.setClickable(false);
                            txtPassOrder.setTextColor(Color.GRAY);
                            txtPassOrder.setClickable(false);
                            txtPassOrder2.setTextColor(Color.GRAY);
                            txtPassOrder2.setClickable(false);
                            viewLine1.setBackgroundResource(R.color.txtcolornoclick);
                            viewLine2.setBackgroundResource(R.color.txtcolornoclick);
                            viewLine3.setBackgroundResource(R.color.txtcolornoclick);
                            viewLine4.setBackgroundResource(R.color.txtcolornoclick);

                            isOnline = false;
                            listView.setAdapter(new ListAdapter(getContext(), list, isOnline));
                        }

                        @Override
                        public void onFailure(Call<DataResult> call, Throwable t) {

                        }
                    });
                }
            }
        });
        txtOnlinLeft.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (txtOnlinLeft.getText().equals("我要上线")) {
                    Call<DataResult> call = RetrofitClient.getInstance().getSYService().line("1");
                    call.enqueue(new Callback<DataResult>() {
                        @Override
                        public void onResponse(Call<DataResult> call, Response<DataResult> response) {
                            initData();
//                            setupListView();
                            Snackbar snackbar = Snackbar.make(getView(), "上线成功！", Snackbar.LENGTH_LONG).setAction("Action", null);
                            snackbar.getView().setBackgroundResource(R.color.orange);
                            snackbar.setActionTextColor(Color.WHITE);
                            snackbar.show();
                            txtOnlinLeft.setText("上线中");
                            txtOnlinLeft.setBackgroundResource(R.drawable.rect_onleft);
                            txtOnlinRight.setText("我要下线");
                            txtOnlinRight.setBackgroundResource(R.drawable.rect_onright);
                            refresh.setBackgroundResource(R.color.orange);
//                            endure.setText("在线：" + totalLine.endure + "小时");
                            endure.setTextColor(Color.WHITE);
                            accept_num.setTextColor(Color.WHITE);
                            salary.setTextColor(Color.WHITE);
                            percent.setTextColor(Color.WHITE);
                            if (viewLine1.getVisibility() == View.VISIBLE) {
                                txtMyOrder.setTextColor(getResources().getColor(R.color.orange));
                            }
                            txtMyOrder.setClickable(true);
                            if (viewLine2.getVisibility() == View.VISIBLE) {
                                txtWorkingOrder.setTextColor(getResources().getColor(R.color.orange));
                            }
                            txtWorkingOrder.setClickable(true);
                            if (viewLine3.getVisibility() == View.VISIBLE) {
                                txtPassOrder.setTextColor(getResources().getColor(R.color.orange));
                            }
                            txtPassOrder.setClickable(true);
                            if (viewLine4.getVisibility() == View.VISIBLE) {
                                txtPassOrder2.setTextColor(getResources().getColor(R.color.orange));
                            }
                            txtPassOrder2.setClickable(true);
                            viewLine1.setBackgroundResource(R.color.orange);
                            viewLine2.setBackgroundResource(R.color.orange);
                            viewLine3.setBackgroundResource(R.color.orange);
                            viewLine4.setBackgroundResource(R.color.orange);
                            isOnline = true;
                            listView.setAdapter(new ListAdapter(getContext(), list, isOnline));
                        }

                        @Override
                        public void onFailure(Call<DataResult> call, Throwable t) {

                        }
                    });
                }
            }
        });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 2000 && resultCode == RESULT_OK) {
            initData();
            if (viewLine1.getVisibility() == View.VISIBLE) {
                setupListView2("5");
            } else if (viewLine2.getVisibility() == View.VISIBLE) {
                setupListView2("6");
            } else if (viewLine3.getVisibility() == View.VISIBLE) {
                setupListView2("4");
            } else {
                setupListView2("3");
            }
        }
        if (requestCode == 1000 && resultCode == RESULT_OK) {
            initData();
        }
    }


    //获取当前日期
    public String getDate() {
        calendar.setTimeInMillis(System.currentTimeMillis());
        StringBuffer sb = new StringBuffer();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);
        Log.i(">>>>calendar", year + " -" + month + " -" + day);
        sb.append(year + "." + month + "." + day);
        return sb.toString();
    }

    //获取正在执行的订单
    private void setupListView() {
//        listView.setAdapter(new ListAdapter(getContext(), null));
        Call<SelecteOrder> selectCall = RetrofitClient.getInstance().getSYService().selecteOrder("5", getDate(), "1");
        selectCall.enqueue(new Callback<SelecteOrder>() {
            @Override
            public void onResponse(Call<SelecteOrder> call, Response<SelecteOrder> response) {
                if (response.isSuccessful()) {
                    if (response.body().getStatus() == 1) {
                        list = response.body().getList();
                        listView.setAdapter(new ListAdapter(getContext(), list, isOnline));
                    }
                }
            }

            @Override
            public void onFailure(Call<SelecteOrder> call, Throwable t) {

            }
        });
    }

    private void setupListView2(String type) {
//        listView.setAdapter(new ListAdapter(getContext(), null));
        Call<SelecteOrder> selectCall = RetrofitClient.getInstance().getSYService().selecteOrder(type, getDate(), "1");
        selectCall.enqueue(new Callback<SelecteOrder>() {
            @Override
            public void onResponse(Call<SelecteOrder> call, Response<SelecteOrder> response) {
                if (response.isSuccessful()) {
                    if (response.body().getStatus() == 1) {
                        list = response.body().getList();
                        listView.setAdapter(new ListAdapter(getContext(), list, isOnline));
                    }
                }
            }

            @Override
            public void onFailure(Call<SelecteOrder> call, Throwable t) {

            }
        });
    }

    public static class ListAdapter extends MyBaseAdapter {
        private boolean isOnline;

        public ListAdapter(Context context, List list, boolean isOnline) {
            super(context, list);
            this.isOnline = isOnline;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            ViewHolder holder;
            if (convertView == null) {
                convertView = mInflater.inflate(R.layout.item_pay_list, parent, false);
                holder = new ViewHolder();
                holder.txtAdress = (TextView) convertView.findViewById(R.id.txt_adress_show);
                holder.txtPhone = (TextView) convertView.findViewById(R.id.txt_phone_show);
                holder.txtShipStatus = (TextView) convertView.findViewById(R.id.txt_shipStatus_show);
                holder.txtName = (TextView) convertView.findViewById(R.id.txt_name_show);
                holder.txtPaytype = (TextView) convertView.findViewById(R.id.txt_paytype_show);
                holder.txtAmount = (TextView) convertView.findViewById(R.id.txt_amount_show);
                convertView.setTag(holder);
            } else {
                holder = (ViewHolder) convertView.getTag();
            }
            final SelecteOrder.ListBean item = (SelecteOrder.ListBean) getItem(position);
            holder.txtAdress.setText(item.getAddress());
            holder.txtPhone.setText(item.getMobile());
            holder.txtName.setText(item.getName());
            holder.txtShipStatus.setText(item.getShipStatus());
            holder.txtPaytype.setText(item.getPaycode() + ":");
            holder.txtAmount.setText("¥" + item.getAmount());
            if (!isOnline) {
                holder.txtAdress.setTextColor(Color.GRAY);
                holder.txtAmount.setTextColor(Color.GRAY);
            }
            return convertView;
        }
    }

    private static class ViewHolder {
        public TextView txtAdress;
        public TextView txtPhone;
        public TextView txtName;
        public TextView txtPaytype;
        public TextView txtAmount;
        public TextView txtShipStatus;

    }

    /*-----------------高德地图相关类----------------*/

    //定位
    private void initLoc() {
        //初始化定位
        mLocationClient = new AMapLocationClient(getContext());//getApplicationContext
        //设置定位回调监听
        mLocationClient.setLocationListener(this);
        //初始化定位参数
        mLocationOption = new AMapLocationClientOption();
        //设置定位模式为高精度模式，Battery_Saving为低功耗模式，Device_Sensors是仅设备模式
        mLocationOption.setLocationMode(AMapLocationClientOption.AMapLocationMode.Battery_Saving);
        //设置是否返回地址信息（默认返回地址信息）
        mLocationOption.setNeedAddress(true);
        //设置是否只定位一次,默认为false
        mLocationOption.setOnceLocation(false);
        //设置是否强制刷新WIFI，默认为强制刷新
        mLocationOption.setWifiActiveScan(true);
        //设置是否允许模拟位置,默认为false，不允许模拟位置
        mLocationOption.setMockEnable(false);
        //设置定位间隔,单位毫秒,默认为2000ms
        mLocationOption.setInterval(30000);
        //给定位客户端对象设置定位参数
        mLocationClient.setLocationOption(mLocationOption);
        //启动定位
        mLocationClient.startLocation();
    }

    @Override
    public void onLocationChanged(AMapLocation amapLocation) {
        if (amapLocation != null) {
            if (amapLocation.getErrorCode() == 0) {
                Double Lat = amapLocation.getLatitude();
                Double Lon = amapLocation.getLongitude();
                WD = Lat;
                JD = Lon;
                Log.d("实时定位：", "纬度" + String.valueOf(Lat) + "经度" + String.valueOf(Lon));
                if (isFirstLoc) {
                    mListener.onLocationChanged(amapLocation);
                    StringBuffer buffer = new StringBuffer();
                    buffer.append(" 定位类型：" + amapLocation.getLocationType() + " 纬度：" + amapLocation.getLatitude() + " 经度：" + amapLocation.getLongitude() + " 精度：" + amapLocation.getAccuracy() + " 城市编码：" + amapLocation.getCityCode() + " 国家：" + amapLocation.getCountry() + " 省份：" + amapLocation.getProvince() + "     " + amapLocation.getProvince() + "" + amapLocation.getCity() + "" + amapLocation.getDistrict() + "" + amapLocation.getStreet() + "" + amapLocation.getStreetNum());
                    Log.d("定位信息", buffer.toString());
                    isFirstLoc = false;
                    aMap.moveCamera(CameraUpdateFactory.zoomTo(15));

                }
                if (isFirstPass) {
                    updateInfo();
                    isFirstPass = false;
                    Log.i(">>>>isFirstPass", "" + isFirstPass);
//                    ToastUtils.show("定位成功！");
                }

            } else {
                //显示错误信息ErrCode是错误码，errInfo是错误信息，详见错误码表。
                /*Log.e("AmapError", "location Error, ErrCode:" + amapLocation.getErrorCode() + ", errInfo:" + amapLocation.getErrorInfo());
                Toast.makeText(getContext(), "定位失败", Toast.LENGTH_LONG).show();//getApplicationContext*/
            }
        }
    }

    @Override
    public void activate(OnLocationChangedListener onLocationChangedListener) {
        mListener = onLocationChangedListener;
    }

    @Override
    public void deactivate() {
        mListener = null;
        if (mLocationClient != null) {
            mLocationClient.stopLocation();
            mLocationClient.onDestroy();
        }
        mLocationClient = null;
    }

    /**
     * 方法必须重写
     */
    @Override
    public void onResume() {
        super.onResume();
        mapView.onResume();
    }

    /**
     * 方法必须重写
     */
    @Override
    public void onPause() {
        super.onPause();
        mapView.onPause();
    }

    /**
     * 方法必须重写
     */
    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        mapView.onSaveInstanceState(outState);
    }

    /**
     * 方法必须重写
     */
    @Override
    public void onDestroy() {
        super.onDestroy();
        mLocationClient.stopLocation();
        mapView.onDestroy();
        handler.removeCallbacks(runnable);
        /*退出应用时下线*/
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

}
