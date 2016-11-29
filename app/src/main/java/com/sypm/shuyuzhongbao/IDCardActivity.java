package com.sypm.shuyuzhongbao;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.sypm.shuyuzhongbao.api.RetrofitClient;
import com.sypm.shuyuzhongbao.data.DataResult;
import com.sypm.shuyuzhongbao.utils.BaseActivity;
import com.sypm.shuyuzhongbao.utils.IDCardValidate;
import com.sypm.shuyuzhongbao.utils.ToastUtils;
import com.yuyh.library.imgsel.ImageLoader;
import com.yuyh.library.imgsel.ImgSelActivity;
import com.yuyh.library.imgsel.ImgSelConfig;

import java.io.File;
import java.text.ParseException;
import java.util.List;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


/*
* 图片选择器
* */

public class IDCardActivity extends BaseActivity {

    private static final int REQUEST_CODE = 0;
    private static final int REQUEST_CODE1 = 666;

    private ImageView imageView1, imageView2;
    private EditText editIdcard;
    private String idFrontPath;
    private String idBackPath;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_idcard);
        initData();
    }

    private void initData() {
        editIdcard = (EditText) findViewById(R.id.edit_insert_idcard);
        imageView1 = (ImageView) findViewById(R.id.image1);
        imageView2 = (ImageView) findViewById(R.id.image2);
        imageView1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Single(imageView1);
            }

        });
        imageView2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Double(imageView2);
            }
        });
    }

    public void addSuggestionClick(View view) {
        String idNumber = editIdcard.getText().toString();
        //上传身份证号
        IDCardValidate idCardValidate = new IDCardValidate();
        try {
            String s = idCardValidate.IDCardRe(idNumber);
            if (!TextUtils.isEmpty(idNumber)&&s.equals("")) {
                Call<DataResult> idNumberCall = RetrofitClient.getInstance().getSYService().updateUserIdCard(idNumber);
                idNumberCall.enqueue(new Callback<DataResult>() {
                    @Override
                    public void onResponse(Call<DataResult> call, Response<DataResult> response) {
                        if (response.isSuccessful()) {
                            if (response.body().status.equals("1")) {
//                        ToastUtils.show("操作成功");
                            }
                        }
                    }

                    @Override
                    public void onFailure(Call<DataResult> call, Throwable t) {
                        ToastUtils.show("操作失败");
                    }
                });
            }else {
                ToastUtils.show("输入的身份证号有误！");
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }


        //上传身份证号和前后两张照片
//        /storage/sdcard1/DCIM/Camera/IMG20161111215920.jpg
//        /storage/sdcard1/DCIM/Camera/IMG20161111205045.jpg
        if (!TextUtils.isEmpty(idFrontPath) && !TextUtils.isEmpty(idBackPath)) {
            File fileFront = new File(idFrontPath);
//            Log.i(">>>idFrontPath", idFrontPath);
            File fileBack = new File(idBackPath);
//            Log.i(">>>idBackPath", idBackPath);
            RequestBody requestFront = RequestBody.create(MediaType.parse("multipart/form-data"), fileFront);
            RequestBody requestBack = RequestBody.create(MediaType.parse("multipart/form-data"), fileBack);
            MultipartBody.Part frontBody = MultipartBody.Part.createFormData("idFront", fileFront.getName(), requestFront);
            MultipartBody.Part backBody = MultipartBody.Part.createFormData("idBack", fileBack.getName(), requestBack);
            Call<DataResult> fileCall = RetrofitClient.getInstance().getSYService()
                    .postIdFrontFile(frontBody);
            Call<DataResult> backCall = RetrofitClient.getInstance().getSYService().postIdFrontFile(backBody);

            fileCall.enqueue(new Callback<DataResult>() {
                @Override
                public void onResponse(Call<DataResult> call, Response<DataResult> response) {

                }

                @Override
                public void onFailure(Call<DataResult> call, Throwable t) {

                }
            });
            backCall.enqueue(new Callback<DataResult>() {
                @Override
                public void onResponse(Call<DataResult> call, Response<DataResult> response) {
                    if (response.isSuccessful()) {
                        ToastUtils.show(response.body().msg);
                    }
                }

                @Override
                public void onFailure(Call<DataResult> call, Throwable t) {

                }
            });
        }

        finish();
    }

    private ImageLoader loader = new ImageLoader() {
        @Override
        public void displayImage(Context context, String path, ImageView imageView) {
            Glide.with(context).load(path).into(imageView);
        }
    };

    public void Single(ImageView img) {

        ImgSelConfig config = new ImgSelConfig.Builder(loader)
                // 是否多选
                .multiSelect(false)
                // 确定按钮背景色
                .btnBgColor(Color.parseColor("#FF4081"))
                // 确定按钮文字颜色
                .btnTextColor(Color.WHITE)
                // 使用沉浸式状态栏
                .statusBarColor(Color.parseColor("#FF4081"))
                // 返回图标ResId
                .backResId(android.support.v7.appcompat.R.drawable.abc_ic_ab_back_material)
                .title("身份证正面图片")
                .titleColor(Color.WHITE)
                .titleBgColor(Color.parseColor("#FF4081"))
//                .cropSize(1, 1, 200, 200)
                .needCrop(false)
                // 第一个是否显示相机
                .needCamera(true)
                // 最大选择图片数量
                .maxNum(1)
                .build();
        // 跳转到图片选择器
        ImgSelActivity.startActivity(this, config, REQUEST_CODE);
    }

    public void Double(ImageView img) {
        ImgSelConfig config = new ImgSelConfig.Builder(loader)
                // 是否多选
                .multiSelect(false)
                // 确定按钮背景色
                .btnBgColor(Color.parseColor("#FF4081"))
                // 确定按钮文字颜色
                .btnTextColor(Color.WHITE)
                // 使用沉浸式状态栏
                .statusBarColor(Color.parseColor("#FF4081"))
                // 返回图标ResId
                .backResId(android.support.v7.appcompat.R.drawable.abc_ic_ab_back_material)
                .title("身份证反面图片")
                .titleColor(Color.WHITE)
                .titleBgColor(Color.parseColor("#FF4081"))
//                .cropSize(1, 1, 200, 200)
                .needCrop(false)
                // 第一个是否显示相机
                .needCamera(true)
                // 最大选择图片数量
                .maxNum(1)
                .build();
        // 跳转到图片选择器
        ImgSelActivity.startActivity(this, config, REQUEST_CODE1);
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CODE && resultCode == RESULT_OK && data != null) {
            List<String> pathList = data.getStringArrayListExtra(ImgSelActivity.INTENT_RESULT);
//            imageView1.setImageURI(Uri.parse("file://" + pathList.get(0)));
            Log.i(">>>>img1", pathList.get(0));
            idFrontPath = pathList.get(0);
            Uri uri = Uri.fromFile(new File(idFrontPath));
            imageView1.setImageURI(uri);

        }
        if (requestCode == REQUEST_CODE1 && resultCode == RESULT_OK && data != null) {
            List<String> pathList = data.getStringArrayListExtra(ImgSelActivity.INTENT_RESULT);
            Log.i(">>>>img2", pathList.get(0));
//            imageView2.setImageURI(Uri.parse("file://" + pathList.get(0)));
            idBackPath = pathList.get(0);
            Uri uri = Uri.fromFile(new File(idBackPath));
            imageView2.setImageURI(uri);
        }
    }
}
