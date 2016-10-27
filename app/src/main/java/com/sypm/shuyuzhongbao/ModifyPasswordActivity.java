package com.sypm.shuyuzhongbao;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.sypm.shuyuzhongbao.utils.BaseActivity;
import com.sypm.shuyuzhongbao.utils.RememberHelper;


public class ModifyPasswordActivity extends BaseActivity {

    EditText number, password;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_modify_pass);
        initViews();
    }

    private void initViews() {
        number = (EditText) findViewById(R.id.number);
        password = (EditText) findViewById(R.id.password);
    }

    public void loginOnclick(View view) {

        if (TextUtils.isEmpty(number.getText().toString()) || TextUtils.isEmpty(password.getText().toString())) {
            Toast.makeText(this, "密码不能为空...", Toast.LENGTH_SHORT).show();
            return;
        }
        if (!RememberHelper.getPassword().equals(number.getText().toString())) {
            Toast.makeText(this, "旧密码输入错误...", Toast.LENGTH_SHORT).show();
            return;
        }
        /*IOCheck.check(new IOCheck.IOCallback() {
            @Override
            public void onCallback(boolean isInner) {
                Call<DataResult> modifyPass = RetrofitClient
                        .getInstance()
                        .getSYService()
                        .modifyPass(MD5Utils.md5Encode(number.getText().toString())
                                , MD5Utils.md5Encode(password.getText().toString()));
                modifyPass.enqueue(new Callback<DataResult>() {
                    @Override
                    public void onResponse(Call<DataResult> call, Response<DataResult> response) {
                        if (response.body()!=null){
                            String status=response.body().status;
                            if (status.equals("1")){
                                logout();
                            }else {
                                Toast.makeText(ModifyPasswordActivity.this,response.body().message,Toast.LENGTH_SHORT).show();
                            }
                            return;
                        }
                        //failed
                        Toast.makeText(ModifyPasswordActivity.this,"修改失败...",Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onFailure(Call<DataResult> call, Throwable t) {
                        Toast.makeText(ModifyPasswordActivity.this," 修改失败...",Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });*/
    }

    private void logout() {
        /*Call<DataResult> logout = RetrofitClient
                .getInstance()
                .getSYService()
                .logout();
        logout.enqueue(new Callback<DataResult>() {
            @Override
            public void onResponse(Call<DataResult> call, Response<DataResult> response) {
                if (response.body()!=null){
                    String status=response.body().status;
                    if (status.equals("1")){
                        Toast.makeText(ModifyPasswordActivity.this,"修改成功,请重新登陆...",Toast.LENGTH_SHORT).show();
                        Intent intent=new Intent(ModifyPasswordActivity.this,LogInActivity.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK|Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        startActivity(intent);
                    }else {
                        Toast.makeText(getActivity(),response.body().message,Toast.LENGTH_SHORT).show();
                    }
                    return;
                }
                //failed
                Toast.makeText(getActivity(),"修改失败...",Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onFailure(Call<DataResult> call, Throwable t) {
                Toast.makeText(getActivity(),"修改失败",Toast.LENGTH_SHORT).show();
            }
        });*/
    }
}
