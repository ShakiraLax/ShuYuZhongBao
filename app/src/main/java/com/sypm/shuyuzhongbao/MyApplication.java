package com.sypm.shuyuzhongbao;

import android.app.AlarmManager;
import android.app.Application;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;

import com.sypm.shuyuzhongbao.utils.Injection;
import com.sypm.shuyuzhongbao.utils.ToastUtils;
import com.tumblr.remember.Remember;

import java.util.HashSet;
import java.util.Set;

import cn.jpush.android.api.JPushInterface;

/**
 * Created by Administrator on 2016/11/8.
 */

public class MyApplication extends Application {

    private IndexFragment.MyHandler handler = null;

    // set方法
    public void setHandler(IndexFragment.MyHandler handler) {
        this.handler = handler;
    }

    // get方法
    public IndexFragment.MyHandler getHandler() {
        return handler;
    }

    @Override
    public void onCreate() {
        super.onCreate();

        //启动闹铃服务
        startAlarm();

        Injection.setApplicationContext(getApplicationContext());
        Remember.init(this, "com.sypm.shuyuzhongbao");
        ToastUtils.init(this);

        //初始化sdk
        JPushInterface.setDebugMode(false);//正式版的时候设置false，关闭调试
        JPushInterface.init(this);
        //建议添加tag标签，发送消息的之后就可以指定tag标签来发送了
        Set<String> set = new HashSet<>();
        set.add("113802");//名字任意，可多添加几个
        JPushInterface.setTags(this, set, null);//设置标签
    }

    public void startAlarm(){
        /**
         首先获得系统服务
         */
        AlarmManager am = (AlarmManager)
                getSystemService(Context.ALARM_SERVICE);

        /** 设置闹钟的意图，我这里是去调用一个服务，该服务功能就是获取位置并且上传*/
        Intent intent = new Intent(this, LocationService.class);
        PendingIntent pendSender = PendingIntent.getService(this, 0, intent, 0);
        am.cancel(pendSender);

        /**AlarmManager.RTC_WAKEUP 这个参数表示系统会唤醒进程；我设置的间隔时间是10分钟 */
        am.setRepeating(AlarmManager.RTC_WAKEUP, System.currentTimeMillis(), 10*60*1000, pendSender);
    }

}
