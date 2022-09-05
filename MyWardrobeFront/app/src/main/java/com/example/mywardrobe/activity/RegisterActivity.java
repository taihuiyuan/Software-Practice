package com.example.mywardrobe.activity;

import android.content.Intent;
import android.os.Bundle;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.mywardrobe.R;
import com.example.mywardrobe.base.*;
import com.example.mywardrobe.presenter.LoginPresenter;
import com.example.mywardrobe.presenter.RegisterPresenter;

import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.regex.PatternSyntaxException;

public class RegisterActivity extends BaseActivity <RegisterPresenter>{
    String TAG = "RegisterActivity";

    EditText etUsername;
    EditText etPassword;
    EditText etEmail;
    EditText etRepeatPassword;
    CheckBox cbEye;
    Button btnRegister;


    @Override
    public int getLayoutId() {
        return R.layout.activity_register;
    }

    @Override
    public void initView() {
        mPresenter = new RegisterPresenter();
        mPresenter.attachView(this);
    }

    @Override
    protected void onCreate(Bundle saveInstanceState) {
        super.onCreate(saveInstanceState);
        setContentView(R.layout.activity_register);
        initView();
        etUsername = (EditText) findViewById(R.id.etUsername);
        etPassword = (EditText) findViewById(R.id.etPassword);
        etEmail = (EditText) findViewById(R.id.etEmail);
        etRepeatPassword = (EditText) findViewById(R.id.etRepeatPassword);
        cbEye = (CheckBox) findViewById(R.id.cbEye);
        btnRegister = (Button) findViewById(R.id.btnRegister);

        etPassword.setTransformationMethod(PasswordTransformationMethod.getInstance());
        etRepeatPassword.setTransformationMethod(PasswordTransformationMethod.getInstance());

        cbEye.setChecked(false);
        cbEye.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked){
                    etPassword.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                    etRepeatPassword.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                }else {
                    etPassword.setTransformationMethod(PasswordTransformationMethod.getInstance());
                    etRepeatPassword.setTransformationMethod(PasswordTransformationMethod.getInstance());
                }
            }
        });

        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String username = etUsername.getText().toString();
                String password = etPassword.getText().toString();
                String email = etEmail.getText().toString();
                String repeatPassword = etRepeatPassword.getText().toString();

                if (!repeatPassword.equals(password)){
                    Toast.makeText(getApplicationContext(), "密码不一致", Toast.LENGTH_LONG).show();
                    return;
                }

                if (!isValidEmail(email)){
                    Toast.makeText(getApplicationContext(), "邮箱格式错误", Toast.LENGTH_LONG).show();
                    return;
                }

                if (!username.equals(stringFilter(username)) || !password.equals(stringFilter(password))){
                    Toast.makeText(getApplicationContext(), "用户名或密码格式错误，只能输入字母/数字", Toast.LENGTH_LONG).show();
                    return;
                }

                mPresenter.username = username;
                mPresenter.password = password;
                mPresenter.email = email;

                doRegister();
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

    public boolean isValidEmail(CharSequence target) {
        if (target == null) {
            return false;
        } else {
            return android.util.Patterns.EMAIL_ADDRESS.matcher(target).matches();
        }
    }

    public void toLogin(View view) {
        Intent intent =  new Intent(this, LoginActivity.class);
        startActivity(intent);
    }

    private void doRegister() {
        mPresenter.register(new RegisterPresenter.RegisterCallback() {
            @Override
            public void onRegisterSuccess(String message, int status) {
                if (status == 200) {
                    String msg = "注册成功";
                    Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_LONG).show();
                    Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);
                    startActivity(intent);
                }
                else {
                    Toast.makeText(getApplicationContext(), message, Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onRegisterFail(String msg) {
                Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_LONG).show();
            }
        });
    }

}
