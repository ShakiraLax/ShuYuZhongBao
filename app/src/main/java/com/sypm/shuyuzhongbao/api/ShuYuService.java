package com.sypm.shuyuzhongbao.api;

import com.sypm.shuyuzhongbao.data.DataResult;
import com.sypm.shuyuzhongbao.data.MessageList;
import com.sypm.shuyuzhongbao.data.MoneyList;
import com.sypm.shuyuzhongbao.data.OrderBySn;
import com.sypm.shuyuzhongbao.data.OrderList;
import com.sypm.shuyuzhongbao.data.OrderNew;
import com.sypm.shuyuzhongbao.data.SelecteOrder;
import com.sypm.shuyuzhongbao.data.TotalLine;
import com.sypm.shuyuzhongbao.data.UserInfo;
import com.sypm.shuyuzhongbao.data.WorkTime;
import com.sypm.shuyuzhongbao.update.AppInfo;

import okhttp3.MultipartBody;
import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;

public interface ShuYuService {

    /*账号登陆*/
    @FormUrlEncoded
    @POST("site/login.html")
    Call<DataResult> login(@Field("staffSn") String staffSn, @Field("password") String password, @Field("registrationId") String registrationId);

    /*获取验证码*/
    @FormUrlEncoded
    @POST("site/sms.html")
    Call<DataResult> getPinFromNet(@Field("phone") String phone);

    /*手机号登陆*/
    @FormUrlEncoded
    @POST("site/mobilelogin.html")
    Call<DataResult> loginByPhone(@Field("phone") String phone, @Field("code") String code);

    /*配送员上线下线(1为上线，2为下线)*/
    @FormUrlEncoded
    @POST("site/line.html")
    Call<DataResult> line(@Field("type") String type);

    /*配送员个人信息汇总*/
    @GET("site/summary.html")
    Call<TotalLine> summary();

    /*首页消息队列*/
    @FormUrlEncoded
    @POST("site/message.html")
    Call<MessageList> messageList(@Field("page") String page, @Field("pagesize") String pagesize);

    /*收入队列*/
    @FormUrlEncoded
    @POST("site/salary.html")
    Call<MoneyList> salaryList(@Field("page") String page, @Field("pagesize") String pagesize);

    /*获取当前用户信息*/
    @GET("user/info.html")
    Call<UserInfo> getUserInfo();

    /*修改当前用户姓名*/
    @FormUrlEncoded
    @POST("user/update.html")
    Call<DataResult> updateUserName(@Field("name") String name);

    /*修改当前用户身份证号*/
    @FormUrlEncoded
    @POST("user/update.html")
    Call<DataResult> updateUserIdCard(@Field("idNumber") String idNumber);

    /*上传身份证前后两张照片*/
    @Multipart
    @POST("user/update.html")
    Call<DataResult> postIdFrontFile(@Part MultipartBody.Part idFrontFile);

    /*获取指派未接受订单*//*之后请求接单或者拒绝单子*/
    @GET("shiporder/getorder.html")
    Call<OrderBySn> getOrder();

    /*获取现在执行的订单*/
    @GET("shiporder/getcurrent.html")
    Call<OrderNew> getCurrentOrder();

    /*根据orderSn来获取订单详情*/
    @FormUrlEncoded
    @POST("shiporder/order.html")
    Call<OrderBySn> getOrderDetail(@Field("orderSn") String orderSn);

    @FormUrlEncoded
    @POST("shiporder/order.html")
    Call<OrderBySn> getOrderDetail(@Field("orderSn") String orderSn, @Field("force") String force);

    /*筛选订单*/
    @FormUrlEncoded
    @POST("site/screening.html")
    Call<SelecteOrder> selecteOrder(@Field("type") String type
            , @Field("date") String date
            , @Field("page") String page);

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
            @Field("isOpen") int isOpen);

    /*退出登录*/
    @GET("site/logout.html")
    Call<DataResult> logout();

    /*搜索门店*/
    @FormUrlEncoded
    @POST("shiporder/searchstore.html")
    Call<OrderList> searchStore(@Field("key") String key);

    /*修改门店*/
    @FormUrlEncoded
    @POST("shiporder/setstore.html")
    Call<DataResult> setStore(@Field("storeSn") String storeSn, @Field("orderSn") String orderSn);

    /*门店列表*/
    @FormUrlEncoded
    @POST("shiporder/storelist.html")
    Call<OrderList> storeList(@Field("orderSn") String orderSn, @Field("radius") String radius);

    /*版本更新*/
    @GET("site/checkversion.html")
    Call<AppInfo> checkVersion();

}
