package com.example.mywardrobe.activity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Toast;

import com.example.mywardrobe.R;
import com.example.mywardrobe.base.BaseActivity;
import com.example.mywardrobe.presenter.ModifyPwdPresenter;

public class ModifyPwdActivity extends BaseActivity<ModifyPwdPresenter> {
    String TAG = "ModifyPwd";
    EditText etOldPwd;
    EditText etNewPwd;
    Button btnBack;
    Button btnModifyPwdSubmit;
    CheckBox cbEye;

    @Override
    public void onCreate(Bundle saveInstanceState) {
        super.onCreate(saveInstanceState);
        setContentView(R.layout.activity_modify_pwd);
        initView();

        etOldPwd = (EditText) findViewById(R.id.etOldPwd);
        etNewPwd = (EditText) findViewById(R.id.etNewPwd);
        btnBack = findViewById(R.id.btnBack);
        btnModifyPwdSubmit = (Button) findViewById(R.id.btnModifyPwdSubmit);
        cbEye = findViewById(R.id.cbEye);

        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                back();
            }
        });

        btnModifyPwdSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String oldPassword = etOldPwd.getText().toString();
                String newPassword = etNewPwd.getText().toString();
                mPresenter.oldPassword = oldPassword;
                mPresenter.newPassword = newPassword;
                doModifyPwd();
            }
        });

        etOldPwd.setTransformationMethod(PasswordTransformationMethod.getInstance());
        etNewPwd.setTransformationMethod(PasswordTransformationMethod.getInstance());
        cbEye.setChecked(false);
        cbEye.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked){
                    etOldPwd.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                    etNewPwd.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                }else {
                    etOldPwd.setTransformationMethod(PasswordTransformationMethod.getInstance());
                    etNewPwd.setTransformationMethod(PasswordTransformationMethod.getInstance());
                }
            }
        });
    }
    @Override
    public int getLayoutId() {
        return R.layout.activity_modify_pwd;
    }

    @Override
    public void initView() {
        mPresenter = new ModifyPwdPresenter();
        mPresenter.attachView(this);

        SharedPreferences sharedPreferences = getSharedPreferences("loginData", Context.MODE_PRIVATE);
        mPresenter.username = sharedPreferences.getString("username", "");

    }

    private void doModifyPwd() {
        mPresenter.modifyPwd(new ModifyPwdPresenter.ModifyPwdCallBack() {
            @Override
            public void onModifyPwdSuccess(String message, int status) {
                if (status == 200) {
                    String msg = "修改密码成功";
                    Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_SHORT).show();

                    back();
                }
            }

            @Override
            public void onModifyPwdFail(String msg) {
                Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void back(){
        ModifyPwdActivity.this.finish();
    }
}
