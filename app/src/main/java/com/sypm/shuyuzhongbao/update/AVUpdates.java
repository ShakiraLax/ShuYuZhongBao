package com.sypm.shuyuzhongbao.update;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Environment;
import android.os.Handler;
import android.os.Looper;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.webkit.URLUtil;
import android.widget.Toast;

import com.koushikdutta.async.future.FutureCallback;
import com.koushikdutta.ion.Ion;
import com.sypm.shuyuzhongbao.api.RetrofitClient;

import java.io.File;

import retrofit2.Callback;

/**
 * Created by Jack Wang on 2016/5/18.
 */
public class AVUpdates {

    private static final String TAG = "AVUpdates";

    public interface Logger {
        void log(String message);
    }

    private Context mContext;

    private Logger logger;

    private boolean logEnable;

    private boolean updatePromptEnable;

    AppInfo appInfo;

    public AVUpdates(Context context) {
        this(context, new Logger() {
            @Override
            public void log(String message) {
                Log.d(TAG, message);
            }
        });
    }

    public AVUpdates(Context context, Logger logger) {
        this.mContext = context;
        this.logger = logger;
    }

    public void setLogEnable(boolean logEnable) {
        this.logEnable = logEnable;
    }

    public void setUpdatePromptEnable(boolean updatePromptEnable) {
        this.updatePromptEnable = updatePromptEnable;
    }

    public void checkForUpdates(String url) {
        Log.d("版本更新", "checkForUpdates");
        if (!AndroidUtils.isOnline(mContext)) {
            if (updatePromptEnable) {
                Toast.makeText(mContext, "当前没有网络！", Toast.LENGTH_SHORT).show();
            }
            return;
        }

        if (!URLUtil.isNetworkUrl(url)) {
            Log.d("版本更新", "isNetworkUrl");
            return;
        }

        retrofit2.Call<AppInfo> call = RetrofitClient.getInstance().getSYService().checkVersion();
        call.enqueue(new Callback<AppInfo>() {
            @Override
            public void onResponse(retrofit2.Call<AppInfo> call, retrofit2.Response<AppInfo> response) {
                Log.d("版本更新", "onResponse");
                appInfo = response.body();

                try {
                    //此处切换回主线程
                    new Handler(Looper.getMainLooper()).post(new Runnable() {
                        @Override
                        public void run() {
                            onCompareVersion(appInfo);
                        }
                    });

                } catch (Exception e) {

                }

            }

            @Override
            public void onFailure(retrofit2.Call<AppInfo> call, Throwable t) {

            }
        });
    }

    private void onCompareVersion(AppInfo appInfo) {
        Log.d("版本更新", "onCompareVersion");
        int appVersionCode = getAppVersionCode(mContext);
        if (appInfo != null) {
            if (Integer.valueOf(appInfo.versionCode) > appVersionCode) {
                onNewVersion(appInfo);
            } else {
                onLatestVersion();
            }
        }
    }

    private void onLatestVersion() {
        Log.d("版本更新", "onLatestVersion");
        if (updatePromptEnable) {
//            Toast.makeText(mContext, "当前已是最新版本！", Toast.LENGTH_SHORT).show();
        }
    }

    private void onNewVersion(final AppInfo appInfo) {
        Log.d("版本更新", "onNewVersion");
        new AlertDialog.Builder(mContext)
                .setTitle("新版本提示")
                .setMessage(appInfo.appDescription)
                .setPositiveButton("更新", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        downloadNewVersion(appInfo);
                    }
                }).setNegativeButton("取消", null).show();
    }

    private void downloadNewVersion(AppInfo appInfo) {
        Log.d("版本更新", "downloadNewVersion");
        final String apkPath = getDefaultSaveRootPath(mContext) + File.separator + String.valueOf(System.currentTimeMillis()) + ".apk";
        final File file = new File(apkPath);
        final String appUrl = appInfo.apkUrl;
        Log.d("版本更新", appUrl);

        final ProgressDialog progressDialog = new ProgressDialog(mContext);
        progressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
        progressDialog.setTitle("正在更新");
        progressDialog.show();
        Ion.with(mContext).load(appUrl)
                .progressDialog(progressDialog)
                .write(file)
                .setCallback(new FutureCallback<File>() {
                    @Override
                    public void onCompleted(Exception e, File result) {
                        progressDialog.cancel();
                        if (e != null) {
                            if (logEnable) {
                                logger.log("e=" + e.toString());
                            }
                            return;
                        }
                        install(mContext, result.getPath());
                    }
                });

    }

    //获取当前程序的版本号
    public static int getAppVersionCode(Context context) {
        Log.d("版本更新", "getAppVersionCode");
        int versionCode = 0;
        try {
            versionCode = context.getPackageManager().getPackageInfo(
                    context.getPackageName(), 0).versionCode;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return versionCode;
    }

    public static void install(Context context, String filePath) {
        Log.d("版本更新", "install");
        Intent i = new Intent(Intent.ACTION_VIEW);
        i.setDataAndType(Uri.parse("file://" + filePath), "application/vnd.android.package-archive");
        i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(i);
    }

    public static String getDefaultSaveRootPath(Context context) {
        Log.d("版本更新", "getDefaultSaveRootPath");
        if (context.getExternalCacheDir() == null) {
            return Environment.getDownloadCacheDirectory().getAbsolutePath();
        } else {
            return context.getExternalCacheDir().getAbsolutePath();
        }
    }
}
