package com.sypm.shuyuzhongbao.api;


import com.sypm.shuyuzhongbao.utils.HttpLoggingInterceptor;
import com.sypm.shuyuzhongbao.utils.HttpsHelper;
import com.sypm.shuyuzhongbao.utils.Injection;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;


public class RetrofitClient {

    private Retrofit retrofit;

    private OkHttpClient okHttpClient;

    public static final String FORMAL_HOST = "https://yys.sypm.cn/ship/";//正式访问用HOST

    public static final String TEST_HOST = "http://test.sypm.cn/ship/";//测试访问用HOST

    public static final String CHECK_VERSION = "http://test.sypm.cn/ship/site/checkversion.html";

    public static String HOST = FORMAL_HOST;

    private RetrofitClient() {
        HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
        interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);

        okHttpClient = new OkHttpClient
                .Builder()
                .sslSocketFactory(HttpsHelper.getSSLSocketFactory())
                .hostnameVerifier(HttpsHelper.getHostnameVerifier())
                .cookieJar(Injection.provideCookieJar())
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
