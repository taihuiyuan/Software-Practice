package com.example.mywardrobe.activity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.Toast;


import com.example.mywardrobe.R;
import com.example.mywardrobe.base.*;
import com.example.mywardrobe.presenter.LoginPresenter;

import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.regex.PatternSyntaxException;

public class LoginActivity extends BaseActivity<LoginPresenter> {
    String TAG = "LoginActivity";

    EditText etUsername;
    EditText etPassword;
    CheckBox cbRemember;
    CheckBox cbEye;
    Button btnLogin;
    SharedPreferences.Editor editor, editor2 ;
    boolean isChecked = false;


    @Override
    protected void onCreate(Bundle saveInstanceState) {
        super.onCreate(saveInstanceState);
        setContentView(R.layout.activity_login);
        initView();

        etUsername = (EditText) findViewById(R.id.etUsername);
        etPassword = (EditText) findViewById(R.id.etPassword);
        cbRemember = (CheckBox) findViewById(R.id.cbRemember);
        cbEye = findViewById(R.id.cbEye);
        btnLogin = (Button) findViewById(R.id.btnLogin);

        editor =  getSharedPreferences("loginData", MODE_PRIVATE).edit();//存储登录状态下信息
        editor2 =  getSharedPreferences("userData", MODE_PRIVATE).edit();//存储用户名和密码

        etPassword.setTransformationMethod(PasswordTransformationMethod.getInstance());

        etPassword.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus){
                    SharedPreferences mSharedPreferences = getSharedPreferences("userData", Context.MODE_PRIVATE);
                    String password = mSharedPreferences.getString(etUsername.getText().toString()+"password","");
                    etPassword.setText(password);
                }
            }
        });

        cbEye.setChecked(false);
        cbEye.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked){
                    etPassword.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                }else {
                    etPassword.setTransformationMethod(PasswordTransformationMethod.getInstance());
                }
            }
        });

        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String username = etUsername.getText().toString();
                String password = etPassword.getText().toString();

                if (!username.equals(stringFilter(username)) || !password.equals(stringFilter(password))){
                    Toast.makeText(getApplicationContext(), "用户名或密码格式错误，只能输入字母/数字", Toast.LENGTH_LONG).show();
                    return;
                }

                mPresenter.username = username;
                mPresenter.password = password;
                doLogin();
            }
        });

        cbRemember.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                LoginActivity.this.isChecked = isChecked;
            }
        });
    }

    public String stringFilter(String str)throws PatternSyntaxException {
        // 只允许字母、数字
        String   regEx  =  "[^a-zA-Z0-9]";
        Pattern p   =   Pattern.compile(regEx);
        Matcher m   =   p.matcher(str);
        return   m.replaceAll("").trim();
    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_login;
    }


    public void initView() {
        mPresenter = new LoginPresenter();
        mPresenter.attachView(this);
    }

    private void doLogin() {

        mPresenter.login(new LoginPresenter.LoginCallback() {
            @Override
            public void onLoginSuccess(String message, int status) {
                if (status == 200) {
                    String msg = "登录成功";
                    Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_LONG).show();
                    editor.clear();
                    editor.putString("username", mPresenter.username);
                    editor.commit();

                    if (isChecked){
                        editor2.putString(mPresenter.username, mPresenter.username);
                        editor2.putString(mPresenter.username+"password", mPresenter.password);
                        editor2.commit();
                    }else {
                        editor2.putString(mPresenter.username+"password", "");
                        editor2.commit();
                    }

                    SharedPreferences mSharedPreferences = getSharedPreferences("loginData", Context.MODE_PRIVATE);
                    Log.i("存储",mSharedPreferences.getString("username",""));
                    toHome();
                }
                else {
                    Toast.makeText(getApplicationContext(), "登陆失败"+message, Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onLoginFail(String msg) {
                Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_LONG).show();
            }
        });
    }

    public void toRegister(View view) {
        Intent intent =  new Intent(LoginActivity.this, RegisterActivity.class);
        startActivity(intent);
    }

    public void toHome() {
        Intent intent = new Intent(LoginActivity.this, NavbarActivity.class);
//        Bundle bundle = new Bundle();
//        bundle.putString("username", mPresenter.username);
//        intent.putExtras(bundle);
        startActivity(intent);
    }

    public void toUpload() {
        Intent intent = new Intent(LoginActivity.this, UploadActivity.class);
//        Bundle bundle = new Bundle();
//        bundle.putString("username", mPresenter.username);
//        intent.putExtras(bundle);
        startActivity(intent);
    }

}
