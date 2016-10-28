package com.sypm.shuyuzhongbao.api;


import com.sypm.shuyuzhongbao.utils.HttpLoggingInterceptor;
import com.sypm.shuyuzhongbao.utils.HttpsHelper;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;


public class RetrofitClient {

    public static final String CHECK_VERSION_INNER = "http://192.168.2.33/site/checkversion.html";

    public static final String CHECK_VERSION_OUT = "https://study.shuyupingmin.com/site/checkversion.html";

    private Retrofit retrofit;

    public static String OUTER_HOST = "https://study.shuyupingmin.com/";//外网访问的HOST

    public static final String INNER_HOST = "https://192.168.2.33/";//内网访问的HOST

    private OkHttpClient okHttpClient;

    public static String HOST = INNER_HOST;

    private RetrofitClient() {
        HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
        interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
        okHttpClient = new OkHttpClient
                .Builder()
                .sslSocketFactory(HttpsHelper.getSSLSocketFactory())
                .hostnameVerifier(HttpsHelper.getHostnameVerifier())
                .cookieJar(null)
//                .cookieJar(Injection.provideCookieJar())
                .connectTimeout(4, TimeUnit.SECONDS)
                .retryOnConnectionFailure(true)
                .addInterceptor(interceptor)
                .build();
        retrofit = new Retrofit.Builder()
                .client(okHttpClient)
                .baseUrl(HOST)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
    }

    /**
     * 切换内外网方法。
     * 如果当前HOST是内网，调用此方法切换回外网，
     * 如果当前HOST是外网，则调用此方法切换回内网。
     */
    public void switchInnerOrOuter() {
        if (HOST.equals(OUTER_HOST)) {
            HOST = INNER_HOST;
        } else {
            HOST = OUTER_HOST;
        }
        resetBaseUrl();
    }

    //当前是否为内网访问。
    public static boolean isInner() {
        return HOST.equals(INNER_HOST);
    }

    private void resetBaseUrl() {
        retrofit = new Retrofit.Builder()
                .client(okHttpClient)
                .baseUrl(HOST)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
    }

    public ShuYuService getSYService() {
        return retrofit.create(ShuYuService.class);
    }

    public static RetrofitClient getInstance() {
        return ClientInstance.sInstance;
    }

    private static class ClientInstance {
        private static RetrofitClient sInstance = new RetrofitClient();
    }

}
