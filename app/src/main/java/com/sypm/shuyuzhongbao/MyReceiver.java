package com.sypm.shuyuzhongbao;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;

import com.sypm.shuyuzhongbao.utils.RememberHelper;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Iterator;

import cn.jpush.android.api.JPushInterface;

/**
 * Created by Administrator on 2016/11/8.
 * 极光推送服务
 */

public class MyReceiver extends BroadcastReceiver {
    private static final String TAG = "JPush";

    final MediaPlayer mp = new MediaPlayer();


    @Override
    public void onReceive(Context context, Intent intent) {
        Bundle bundle = intent.getExtras();
//        Log.d(TAG, "[MyReceiveronReceive] onReceive - " + intent.getAction() + ", extras: " + printBundle(bundle));

            /*NotificationCompat.Builder builder = new NotificationCompat.Builder(context);
            builder.setSound(Uri.parse("android.resource://" + context.getPackageName() + "/" + R.raw.notify));
            Notification notification = builder.build();
            int notifyId = (int) System.currentTimeMillis();
            NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
            notificationManager.notify(notifyId, notification);*/

        if (JPushInterface.ACTION_MESSAGE_RECEIVED.equals(intent.getAction())) {
//            Log.d(TAG, "[MyReceiverACTION_MESSAGE_RECEIVED] 接收到推送下来的自定义消息: " + bundle.getString(JPushInterface.EXTRA_MESSAGE));
//            Log.d(TAG, "[MyReceiver] 接收到推送下来的自定义消息: " + bundle.getString(JPushInterface.EXTRA_TITLE));
            String type = bundle.getString(JPushInterface.EXTRA_TITLE);

            if (type.equals("zhipai")) {
                try {
                    mp.setDataSource(context, Uri.parse("android.resource://" + context.getPackageName() + "/" + R.raw.notify));
                    mp.prepare();
                } catch (Exception e) {
                    e.printStackTrace();
                }
                mp.start();
//                Intent intentGrab = new Intent(context, GrabOrderActivity.class);
                Intent intentGrab = new Intent(context, GrabOrderActivity2.class);
                intentGrab.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(intentGrab);
            } else if (type.equals("mendian")) {
                Intent intent1 = new Intent(context, OrderStatusActivity.class);
//                intent1.putExtra("mendian", Sn);
                intent1.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(intent1);
            } else if (type.equals("quhuo")) {
                String shipSn = bundle.getString(JPushInterface.EXTRA_MESSAGE);
                String Sn = shipSn.substring(3, 19);
                Log.d("Sn", Sn);
                Intent intent2 = new Intent(context, OrderStatusActivity.class);
//                Intent intent2 = new Intent(context, OrderDetailActivity.class);
                intent2.putExtra("quhuo", Sn);
                intent2.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//                intent2.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_SINGLE_TOP);
                context.startActivity(intent2);
            } else if (type.equals("kefu")) {
                String shipSn = bundle.getString(JPushInterface.EXTRA_MESSAGE);
                String Sn = shipSn.substring(17, 33);
                Log.d("Sn", Sn);
                Intent intent3 = new Intent(context, OrderStatusActivity.class);
                intent3.putExtra("kefu", Sn);
                intent3.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(intent3);
            }
//            processCustomMessage(context, bundle);

            /*原本放在第一位*/
        } else if (JPushInterface.ACTION_REGISTRATION_ID.equals(intent.getAction())) {
            String regId = bundle.getString(JPushInterface.EXTRA_REGISTRATION_ID);
            Log.d(TAG, "[MyReceiverACTION_REGISTRATION_ID] 接收Registration Id : " + regId);
            //保存registrationId:160a3797c80a1a27a63
            RememberHelper.saveRegistrationId(regId);
            //send the Registration Id to your server...

        } else if (JPushInterface.ACTION_NOTIFICATION_RECEIVED.equals(intent.getAction())) {
            Log.d(TAG, "[MyReceiverACTION_NOTIFICATION_RECEIVED] 接收到推送下来的通知");
            int notifactionId = bundle.getInt(JPushInterface.EXTRA_NOTIFICATION_ID);
            Log.d(TAG, "[MyReceiverACTION_NOTIFICATION_RECEIVED] 接收到推送下来的通知的ID: " + notifactionId);

        } else if (JPushInterface.ACTION_NOTIFICATION_OPENED.equals(intent.getAction())) {
            Log.d(TAG, "[MyReceiverACTION_NOTIFICATION_OPENED] 用户点击打开了通知");
//          //打开自定义的Activity
            /*Intent i = new Intent(context, MainActivity.class);
            i.putExtras(bundle);
            i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//            i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
            context.startActivity(i);*/

        } else if (JPushInterface.ACTION_RICHPUSH_CALLBACK.equals(intent.getAction())) {
            Log.d(TAG, "[MyReceiverACTION_RICHPUSH_CALLBACK] 用户收到到RICH PUSH CALLBACK: " + bundle.getString(JPushInterface.EXTRA_EXTRA));
            //在这里根据 JPushInterface.EXTRA_EXTRA 的内容处理代码，比如打开新的Activity， 打开一个网页等..

        } else if (JPushInterface.ACTION_CONNECTION_CHANGE.equals(intent.getAction())) {
            boolean connected = intent.getBooleanExtra(JPushInterface.EXTRA_CONNECTION_CHANGE, false);
            Log.w(TAG, "[MyReceiverACTION_CONNECTION_CHANGE]" + intent.getAction() + " connected state change to " + connected);
        } else {
            Log.d(TAG, "[MyReceiverACTION_CONNECTION_CHANGE] Unhandled intent - " + intent.getAction());
        }
    }

    // 打印所有的 intent extra 数据
    private static String printBundle(Bundle bundle) {
        StringBuilder sb = new StringBuilder();
        for (String key : bundle.keySet()) {
            if (key.equals(JPushInterface.EXTRA_NOTIFICATION_ID)) {
                sb.append("\nkey:" + key + ", value:" + bundle.getInt(key));
            } else if (key.equals(JPushInterface.EXTRA_CONNECTION_CHANGE)) {
                sb.append("\nkey:" + key + ", value:" + bundle.getBoolean(key));
            } else if (key.equals(JPushInterface.EXTRA_EXTRA)) {
                if (bundle.getString(JPushInterface.EXTRA_EXTRA).isEmpty()) {
                    Log.i(TAG, "This message has no Extra data");
                    continue;
                }

                try {
                    JSONObject json = new JSONObject(bundle.getString(JPushInterface.EXTRA_EXTRA));
                    Iterator<String> it = json.keys();

                    while (it.hasNext()) {
                        String myKey = it.next().toString();
                        sb.append("\nkey:" + key + ", value: [" +
                                myKey + " - " + json.optString(myKey) + "]");
                    }
                } catch (JSONException e) {
                    Log.e(TAG, "Get message extra JSON error!");
                }

            } else {
                sb.append("\nkey:" + key + ", value:" + bundle.getString(key));
            }
        }
        return sb.toString();
    }

    //send msg to MainActivity
    private void processCustomMessage(Context context, Bundle bundle) {
        /*if (MainActivity.isForeground) {
            String message = bundle.getString(JPushInterface.EXTRA_MESSAGE);
            String extras = bundle.getString(JPushInterface.EXTRA_EXTRA);
//            Log.d("自定义消息", message);
//            Log.d("自定义消息", extras);
            Intent msgIntent = new Intent(MainActivity.MESSAGE_RECEIVED_ACTION);
            msgIntent.putExtra(MainActivity.KEY_MESSAGE, message);
            if (!ExampleUtil.isEmpty(extras)) {
                try {
                    JSONObject extraJson = new JSONObject(extras);
                    if (null != extraJson && extraJson.length() > 0) {
                        msgIntent.putExtra(MainActivity.KEY_EXTRAS, extras);
                    }
                } catch (JSONException e) {

                }

            }
            context.sendBroadcast(msgIntent);
        }*/
    }
}
