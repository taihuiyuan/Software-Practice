package com.example.mywardrobe.activity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.mywardrobe.R;
import com.example.mywardrobe.base.BaseActivity;
import com.example.mywardrobe.presenter.ModifyInfoPresenter;

public class ModifyInfoActivity extends BaseActivity<ModifyInfoPresenter> {
    String TAG = "ModifyInfo";
    EditText etModifyEmail;
    Button btnModifyInfo;
    Button btnBack;
    TextView originalEmail;

    @Override
    public void onCreate(Bundle saveInstanceState) {
        super.onCreate(saveInstanceState);
        setContentView(R.layout.activity_modify_info);
        initView();

        etModifyEmail = (EditText) findViewById(R.id.etModifyEmail);
        btnModifyInfo = (Button) findViewById(R.id.btnModifyInfoSubmit);
        btnBack = findViewById(R.id.btnBack);
        originalEmail = findViewById(R.id.originalEmail);

        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                back();
            }
        });

        btnModifyInfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String email = etModifyEmail.getText().toString();
                if (!isValidEmail(email)){
                    Toast.makeText(getApplicationContext(), "邮箱格式错误", Toast.LENGTH_LONG).show();
                    return;
                }
                mPresenter.email = email;
                doModifyInfo();
            }
        });

    }

    public boolean isValidEmail(CharSequence target) {
        if (target == null) {
            return false;
        } else {
            return android.util.Patterns.EMAIL_ADDRESS.matcher(target).matches();
        }
    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_modify_info;
    }

    @Override
    public void initView() {
        mPresenter = new ModifyInfoPresenter();
        mPresenter.attachView(this);

        SharedPreferences sharedPreferences = getSharedPreferences("loginData", Context.MODE_PRIVATE);
        mPresenter.username = sharedPreferences.getString("username", "");
        showUserInfo();
    }

    private void showUserInfo() {
        mPresenter.getUserInfo(new ModifyInfoPresenter.GetUserInfoCallBack() {
            @Override
            public void onGetUserInfoCallBackSuccess(String username, String email) {
                originalEmail.setText("原邮箱："+email);
            }

            @Override
            public void onGetUserInfoCallBackFail(String msg) {
                Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void doModifyInfo() {
        mPresenter.modifyInfo(new ModifyInfoPresenter.ModifyInfoCallBack() {
            @Override
            public void onModifyInfoSuccess(String message, int status) {
                if (status == 200) {
                    String msg = "修改成功";
                    Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_SHORT).show();
                    back();
                }
            }

            @Override
            public void onModifyInfoFail(String msg) {
                Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void back(){
        ModifyInfoActivity.this.finish();
    }
}
