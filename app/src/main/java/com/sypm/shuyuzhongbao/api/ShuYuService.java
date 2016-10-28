package com.sypm.shuyuzhongbao.api;

public interface ShuYuService {

    /*@FormUrlEncoded
    @POST("mobile-site-login.html")
    Call<DataResult> login(@Field("staffSn") String staffSn, @Field("password") String password);


    @GET("mobile-lesson-list.html")
    Call<DataResult<Lesson>> lessonList(@Query("page") int page);

    @GET("mobile-lesson-list.html")
    Call<DataResult<Lesson>> lessonListByFilter(@Query("page") int page, @Query("classesId") String classesId, @Query("category") String category);

    @FormUrlEncoded
    @POST("mobile-site-change.html")
    Call<DataResult> modifyPass(@Field("oldPassword") String oldPassword, @Field("password") String newPassword);

    @GET("mobile-site-logout.html")
    Call<DataResult> logout();

    @FormUrlEncoded
    @POST("mobile-video-addlog.html")
    Call<DataResult> addLog(@Field("videoId") String videoId
            , @Field("LessonId") String LessonId
            , @Field("relId") String relId
            , @Field("playedTime") String playedTime
            , @Field("videoTime") String videoTime
            , @Field("videoPlayTime") String videoPlayTime);

    @GET("mobile-classes-list.html")
    Call<DataResult<Classes>> classList(@Query("page") int page);

    *//*获取我的考试列表*//*
    @FormUrlEncoded
    @POST("mobile-exam-list.html")
    Call<DataResult<Exam>> examList(@Field("page") int page
            , @Field("classesId") String classesId
            , @Field("typeId") String typeId);

    *//*获取我的公告信息*//*
    @GET("mobile-site-note.html")
//    Call<DataResult<Note>> note();
    Call<Note> note();

    *//*提交我的考试*//*
    @FormUrlEncoded
    @POST("mobile-exam-submit.html")
    Call<AnswerResult> submitExamList(
            @Field("examid") String examid
            , @Field("subjectid") String subjectid
            , @Field("optionid") String optionid
            , @Field("flag") String flag);

    *//*提交我的考试*//*
    @FormUrlEncoded
    @POST("mobile-exam-submit.html")
    Call<AnswerResult> submitExamList(
            @Field("examid") String examid
            , @Field("subjectid") String subjectid
            , @Field("optionid") String optionid
            , @Field("flag") String flag, @Field("test") String test);


    *//*播放记录*//*
    @GET("mobile-player-record.html")
    Call<Record> record(@Query("page") int page);

    *//*获取系统时间*//*
    @GET("mobile-getservertime.html")
    Call<DataResult> getServerTime();

    *//*获取筛选内容*//*
    @GET("mobile-lesson-filter.html")
    Call<Filter> getFilterContent();

    *//*开口说列表*//*
    @FormUrlEncoded
    @POST("mobile-speech-index.html")
    Call<DataResult<Speech>> openingList(@Field("page") String page, @Field("search") String search);

    //开口说音频上传
    @Multipart
    @POST("mobile-speech-upload.html")
    Call<SpeechResult> uploadSpeech(@Part MultipartBody.Part file, @Part("speechId") RequestBody speechId, @Part("audioContent") RequestBody audioContent, @Part("volume") RequestBody volume, @Part("recordingTime") RequestBody recordingTime);

    *//*学分明细*//*
    @GET("mobile-credits-index.html")
    Call<Credit> credits(@Query("page") int page);

    *//*1.4版本*//*

    *//*社区分类列表*//*
    @GET("mobile-community-list.html")
    Call<PostListResult> communityList(@Query("page") int page);

    *//*评论列表*//*
    @GET("mobile-comment-list.html")
    Call<DataResult> commentList(@Query("page") int page);

    *//*打赏列表*//*
    @GET("mobile-community-reward.html")
    Call<DataResult> communityReward(@Query("page") int page);

    *//*帖子列表*//*
    *//*已经改为GET方式请求接口*//*
    @GET("mobile-post-list.html")
    Call<DataResult> postList(
            @Query("page") int page
            , @Query("classifyId") String classifyId
            , @Query("type") String type
            , @Query("search") String search);

    *//*帖子正文内容*//*
    @FormUrlEncoded
    @POST("mobile-post-details.html")
    Call<DataResult> postDetails(
            @Field("classifyId") String classifyId
            , @Field("postId") String postId);

    *//*提交帖子内容*//*
    @FormUrlEncoded
    @POST("mobile-post-submit.html")
    Call<DataResult> postSubmit(
            @Field("content") String content
            , @Field("classifyId") String classifyId
            , @Field("image") String image);

    *//*提交评论*//*
    @FormUrlEncoded
    @POST("mobile-comment-submit.html")
    Call<DataResult> commentSubmit(
            @Field("classifyId") String classifyId
            , @Field("postId") String postId
            , @Field("commentId") String commentId
            , @Field("comment") String comment);

    *//*提交赞和打赏*//*
    @FormUrlEncoded
    @POST("mobile-comment-praise.html")
    Call<DataResult> commentPraise(
            @Field("classifyId") String classifyId
            , @Field("postId") String postId
            , @Field("type") String type);

    *//*获取当前学分*//*
    @GET("mobile-staff-credits.html")
    Call<DataResult> staffCredits();*/
}
