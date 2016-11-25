package com.sypm.shuyuzhongbao.api;

import com.sypm.shuyuzhongbao.data.DataResult;
import com.sypm.shuyuzhongbao.data.MessageList;
import com.sypm.shuyuzhongbao.data.MoneyList;
import com.sypm.shuyuzhongbao.data.TotalLine;
import com.sypm.shuyuzhongbao.data.UserInfo;
import com.sypm.shuyuzhongbao.data.WorkTime;

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

    /*配送员上线下线*/
    @FormUrlEncoded
    @POST("site/site/line.html")
    Call<DataResult> line(@Field("type") String type);

    /*配送员个人信息汇总*/
    @GET("site/summary.html")
    Call<TotalLine> summary();

    /*首页消息队列*/
    @FormUrlEncoded
    @POST("site/message.html")
    Call<MessageList> messageList(@Field("page") String page);

    /*收入队列*/
    @FormUrlEncoded
    @POST("site/salary.html")
    Call<MoneyList> salaryList(@Field("page") String page, @Field("pagesize") String pagesize);

    /*获取当前用户信息*/
    @GET("user/info.html")
    Call<UserInfo> getUserInfo();

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

    /*根据orderSn来获取订单详情*/
    @FormUrlEncoded
    @POST("shiporder/order.html")
    Call<DataResult> getOrderDetail(@Field("orderSn") String orderSn);

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
    Call<DataResult<WorkTime>> getWorkTimeList(@Field("sort") String sort);

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
            @Field("id") int id
            , @Field("startTime") String startTime
            , @Field("endTime") String endTime
            , @Field("dayTime") String dayTime);

    /*人员上班时间删除*/
    @FormUrlEncoded
    @POST("worktime/delete.html")
    Call<DataResult> worktimedelete(@Field("id") int id);

    /*人员上班时间开关*/
    @FormUrlEncoded
    @POST("worktime/update.html")
    Call<DataResult> worktimeOffOn(
            @Field("id") int id,
            @Field("isOpen") String isOpen);

    /*退出登录*/
    @GET("site/logout.html")
    Call<DataResult> logout();

}
