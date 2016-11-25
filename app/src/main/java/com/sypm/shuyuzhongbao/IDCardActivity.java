package com.sypm.shuyuzhongbao;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.sypm.shuyuzhongbao.utils.BaseActivity;
import com.yuyh.library.imgsel.ImageLoader;
import com.yuyh.library.imgsel.ImgSelActivity;
import com.yuyh.library.imgsel.ImgSelConfig;

import java.io.File;
import java.util.List;

/*
* 图片选择器
* */

public class IDCardActivity extends BaseActivity {

    private static final int REQUEST_CODE = 0;
    private static final int REQUEST_CODE1 = 666;

    ImageView imageView1, imageView2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_idcard);
        initData();
    }

    private void initData() {
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

    private ImageLoader loader = new ImageLoader() {
        @Override
        public void displayImage(Context context, String path, ImageView imageView) {
            Glide.with(context).load(path).into(imageView);
        }
    };

    public void Single(View view) {
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
                .title("身份证正反两张图片")
                .titleColor(Color.WHITE)
                .titleBgColor(Color.parseColor("#FF4081"))
                .cropSize(1, 1, 200, 200)
                .needCrop(false)
                // 第一个是否显示相机
                .needCamera(false)
                // 最大选择图片数量
                .maxNum(1)
                .build();
        // 跳转到图片选择器
        ImgSelActivity.startActivity(this, config, REQUEST_CODE);
    }

    public void Double(View view) {
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
                .title("身份证正反两张图片")
                .titleColor(Color.WHITE)
                .titleBgColor(Color.parseColor("#FF4081"))
                .cropSize(1, 1, 200, 200)
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
            Uri uri=Uri.fromFile(new File(pathList.get(0)));
            imageView1.setImageURI(uri);


        }
        if (requestCode == REQUEST_CODE1 && resultCode == RESULT_OK && data != null) {
            List<String> pathList = data.getStringArrayListExtra(ImgSelActivity.INTENT_RESULT);

//            imageView2.setImageURI(Uri.parse("file://" + pathList.get(0)));
            Uri uri=Uri.fromFile(new File(pathList.get(0)));
            imageView2.setImageURI(uri);

        }
    }
}
