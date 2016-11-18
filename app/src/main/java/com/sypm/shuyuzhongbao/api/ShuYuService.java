package com.sypm.shuyuzhongbao.api;

import com.sypm.shuyuzhongbao.data.DataResult;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;

public interface ShuYuService {

    /*账号登陆*/
    @FormUrlEncoded
    @POST("site/login.html")
    Call<DataResult> login(@Field("staffSn") String staffSn, @Field("password") String password);

    /*获取验证码*/
    @FormUrlEncoded
    @POST("site/sms.html")
    Call<DataResult> getPinFromNet(@Field("phone") String phone);

    /*手机号登陆*/
    @FormUrlEncoded
    @POST("site/mobilelogin.html")
    Call<DataResult> loginByPhone(@Field("phone") String phone, @Field("code") String code);

    /*首页消息队列*/
    @FormUrlEncoded
    @POST("site/message.html")
    Call<DataResult> messageList(@Field("page") String page);

    /*获取当前用户信息*/
    @GET("user/info.html")
    Call<DataResult> getUserInfo();

    /*修改当前用户信息*/
    @FormUrlEncoded
    @POST("user/update.html")
    Call<DataResult> updateUserInfo(
            @Field("mobile") String mobile
            , @Field("sn") String sn
            , @Field("name") String name
            , @Field("idNumber") String idNumber
            , @Field("idFront") String idFront
            , @Field("idBack") String idBack);

    /*获取指派未接受订单*//*之后请求接单或者拒绝单子*/
    @GET("shiporder/getorder.html")
    Call<DataResult> getOrder();

    /*获取现在执行的订单*/
    @GET("shiporder/getcurrent.html")
    Call<DataResult> getCurrentOrder();

    /*配送员拒绝单子*/
    @FormUrlEncoded
    @POST("shiporder/cancel.html")
    Call<DataResult> orderCancel(@Field("shipSn") String shipSn);

    /*接单*/
    @FormUrlEncoded
    @POST("shiporder/sure.html")
    Call<DataResult> orderSure(@Field("shipSn") String shipSn);

    /*客户拒单*/
    @FormUrlEncoded
    @POST("shiporder/reject.html")
    Call<DataResult> orderReject(@Field("shipSn") String shipSn);

    /*完成配送*/
    @FormUrlEncoded
    @POST("shiporder/finish.html")
    Call<DataResult> orderFinish(@Field("shipSn") String shipSn);

    /*人员上班时间列表*/
    @FormUrlEncoded
    @POST("worktime/list.html")
    Call<DataResult> getWorkTimeList(@Field("sort") String sort);

    /*修改密码*/
    @FormUrlEncoded
    @POST("user/password.html")
    Call<DataResult> midifyPassword(@Field("oldPassword") String oldPassword, @Field("password") String password);

    /*人员定位提交*/
    @FormUrlEncoded
    @POST("location/insert.html")
    Call<DataResult> locationInsert(
            @Field("lat") String lat
            , @Field("lng") String lng
            , @Field("shipId") String shipId);

    /*人员上班时间上传*/
    @FormUrlEncoded
    @POST("worktime/create.html")
    Call<DataResult> worktimeCreate(
            @Field("startTime") String startTime
            , @Field("endTime") String endTime
            , @Field("dayTime") String dayTime);

    /*人员上班时间修改*/
    @FormUrlEncoded
    @POST("worktime/update.html")
    Call<DataResult> worktimeUpdate(
            @Field("id") String id
            , @Field("startTime") String startTime
            , @Field("endTime") String endTime
            , @Field("dayTime") String dayTime
            , @Field("isOpen") String isOpen);

    /*退出登录*/
    @GET("site/logout.html")
    Call<DataResult> logout();

}
