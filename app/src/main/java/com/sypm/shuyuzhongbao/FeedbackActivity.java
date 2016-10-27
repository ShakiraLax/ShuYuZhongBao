package com.sypm.shuyuzhongbao;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.sypm.shuyuzhongbao.utils.BaseActivity;

public class FeedbackActivity extends BaseActivity {


    EditText content;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_suggestion);
        initView();

    }

    private void initView() {
        content = (EditText) findViewById(R.id.content);
    }

    public void addSuggestionClick(View view) {
        String suggestion = content.getText().toString();
        if (TextUtils.isEmpty(suggestion)) {
            Toast.makeText(getActivity(), "内容不能为空！", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(getActivity(), "提交成功！", Toast.LENGTH_SHORT).show();
        }
    }

}
