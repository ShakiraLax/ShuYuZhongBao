package com.sypm.shuyuzhongbao;

import android.app.Service;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Looper;
import android.os.Message;
import android.util.Log;

import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationClient;
import com.amap.api.location.AMapLocationClientOption;
import com.amap.api.location.AMapLocationListener;
import com.sypm.shuyuzhongbao.api.RetrofitClient;
import com.sypm.shuyuzhongbao.data.DataResult;

import java.text.SimpleDateFormat;
import java.util.Date;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LocationService extends Service {
    private static final String TAG = "LocationService";

    private int recLen = 0;
    //声明AMapLocationClient类对象
    AMapLocationClient mLocationClient = null;
    //声明AMapLocationClientOption对象
    public AMapLocationClientOption mLocationOption = null;


    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        Log.i(TAG, "start LocationService!");
        netThread.start();
        //初始化定位
        mLocationClient = new AMapLocationClient(getApplicationContext());
        //设置定位回调监听
        mLocationClient.setLocationListener(mLocationListener);
        //初始化AMapLocationClientOption对象
        mLocationOption = new AMapLocationClientOption();
        //设置定位模式为AMapLocationMode.Battery_Saving，省电模式。
        mLocationOption.setLocationMode(AMapLocationClientOption.AMapLocationMode.Hight_Accuracy);
        //设置是否只定位一次,默认为false
        mLocationOption.setOnceLocation(false);
        //设置是否强制刷新WIFI，默认为强制刷新
        mLocationOption.setWifiActiveScan(true);
        //设置定位间隔,单位毫秒,默认为2000ms
        mLocationOption.setInterval(30000);
//        mLocationOption.setOnceLocationLatest(true);
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.i(TAG, "StartCommand LocationService!");
        getPosition();
        return super.onStartCommand(intent, flags, startId);

    }

    Handler netHandler = null;

    /**
     * 收发网络数据的线程
     */
    Thread netThread = new Thread() {
        @Override
        public void run() {
            Looper.prepare();
            netHandler = new Handler() {
                public void dispatchMessage(Message msg) {
                    Bundle data = msg.getData();
                    switch (msg.what) {
                        case 0x1: //发送位置
                            updateInfo(data.getString("latitude"), data.getString("longitude"));
                            break;

                    }
                }
            };
            Looper.loop();
        }
    };


    private void updateInfo(final String Lat, final String Lng) {
        /*上线后开始上传位置信息*/
        Call<DataResult> call = RetrofitClient.getInstance().getSYService().locationInsert(Lat, Lng, null);
        call.enqueue(new Callback<DataResult>() {
            @Override
            public void onResponse(Call<DataResult> call, Response<DataResult> response) {
                if (response.body() != null) {
                    if (response.body().status.equals("1")) {
                        Log.d("上线后定位信息上传", "纬度" + Lat + "经度" + Lng);
                    } else {

                    }
                } else {

                }
            }

            @Override
            public void onFailure(Call<DataResult> call, Throwable t) {

            }
        });
    }


    public void getPosition() {
        //给定位客户端对象设置定位参数
        mLocationClient.setLocationOption(mLocationOption);
        //启动定位
        mLocationClient.startLocation();
    }

    //声明定位回调监听器
    public AMapLocationListener mLocationListener = new AMapLocationListener() {

        @Override
        public void onLocationChanged(AMapLocation amapLocation) {
            if (amapLocation == null) {
                Log.i(TAG, "amapLocation is null!");
                return;
            }
            if (amapLocation.getErrorCode() != 0) {
                Log.i(TAG, "amapLocation has exception errorCode:" + amapLocation.getErrorCode());
                return;
            }

            Double longitude = amapLocation.getLongitude();//获取经度
            Double latitude = amapLocation.getLatitude();//获取纬度
            String longitudestr = String.valueOf(longitude);
            String latitudestr = String.valueOf(latitude);
            SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            Date date = new Date(amapLocation.getTime());
            String timestr = df.format(date);
            Log.i(TAG, "longitude,latitude:" + longitude + "," + latitude);
            Log.i(TAG, "time:" + timestr);
            Message msg = new Message();
            Bundle data = new Bundle();
            data.putString("longitude", longitudestr);
            data.putString("latitude", latitudestr);
            data.putString("timestr", timestr);
            msg.setData(data);
            msg.what = 0x1;
            netHandler.sendMessage(msg);

//            RememberHelper.saveLocation(latitudestr, longitudestr);
        }
    };

}
