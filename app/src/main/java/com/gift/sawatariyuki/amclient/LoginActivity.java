package com.gift.sawatariyuki.amclient;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ActivityOptions;
import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.util.Pair;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.gift.sawatariyuki.amclient.Bean.LoginResponse;
import com.gift.sawatariyuki.amclient.ServerNetwork.RequestCenter;
import com.gift.sawatariyuki.amclient.Utils.dataRecoder.DataRecorder;
import com.gift.sawatariyuki.amclient.Utils.okHttp.listener.DisposeDataListener;
import com.gift.sawatariyuki.amclient.Utils.okHttp.request.RequestParams;

import java.util.ArrayList;
import java.util.List;

public class LoginActivity extends AppCompatActivity {
    private TextView activity_login_TV_name;
    private TextView activity_login_TV_pw;
    private EditText activity_login_ET_name;
    private EditText activity_login_ET_pw;
    private Switch activity_login_SW_remember;
    private Button activity_login_BTN_login;
    private Button activity_login_BTN_register;


    private DataRecorder recorder;

    private final int REQUESTCODE = 102;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        initView();
        recorder = new DataRecorder(this);
        initListener();
        initData();
    }

    private void initData(){
        //查看是否存储过用户登录信息
        Boolean isRemember;
        String username, password;
        isRemember = (Boolean) recorder.get("isRemember", false);
        if(isRemember){
            username = (String) recorder.get("username","");
            password = (String) recorder.get("password", "");
            activity_login_ET_name.setText(username);
            activity_login_ET_pw.setText(password);
            activity_login_ET_pw.setSelection(password.length());
            activity_login_SW_remember.setChecked(true);
            ChangeColor(true);
        }else{
            activity_login_ET_name.requestFocus();
            activity_login_ET_name.setFocusable(true);
            activity_login_ET_name.setFocusableInTouchMode(true);
            ChangeColor(false);
        }
    }

    private void initView() {
        activity_login_TV_name = findViewById(R.id.activity_login_TV_name);
        activity_login_TV_pw = findViewById(R.id.activity_login_TV_pw);
        activity_login_ET_name = findViewById(R.id.activity_login_ET_name);
        activity_login_ET_pw = findViewById(R.id.activity_login_ET_pw);
        activity_login_SW_remember = findViewById(R.id.activity_login_SW_remember);
        activity_login_BTN_login = findViewById(R.id.activity_login_BTN_login);
        activity_login_BTN_register = findViewById(R.id.activity_login_BTN_register);
    }

    private void initListener() {
        TextWatcher textWatcher = new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                checkValid();
            }
        };

        activity_login_ET_name.addTextChangedListener(textWatcher);
        activity_login_ET_pw.addTextChangedListener(textWatcher);

        activity_login_BTN_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String username = activity_login_ET_name.getText().toString().trim();
                String password = activity_login_ET_pw.getText().toString().trim();
                if(password.length()<8){
                    Toast.makeText(view.getContext(), "密码至少需要8位",Toast.LENGTH_SHORT).show();
                    return;
                }
                if(activity_login_SW_remember.isChecked()){
                    recorder.save("username", username);
                    recorder.save("password", password);
                    recorder.save("isRemember", true);
                }else{
                    recorder.save("isRemember", false);
                }
                //SEND POST REQUEST
                RequestParams params = new RequestParams();
                params.put("name", username);
                params.put("pw", password);
                RequestCenter.login_POST(new DisposeDataListener() {
                    @Override
                    public void onSuccess(Object responseObj) {
                        LoginResponse response = (LoginResponse) responseObj;
                        String msg = response.getMsg();
                        Toast.makeText(LoginActivity.this, msg, Toast.LENGTH_SHORT).show();

                        if(msg.equals("登陆成功") || msg.equals("请先激活用户")){
                            recorder.save("loggedUsername", response.getData().getFields().getName());
                            recorder.save("email", response.getData().getFields().getEmail());
                            recorder.save("isActivated", response.getData().getFields().getActivated());
                            Intent data = new Intent();
                            data.putExtra("response", response);
                            setResult(201, data);
                            finish();
                        }
                    }

                    @Override
                    public void onFailure(Object responseObj) {

                    }
                }, params);
            }
        });

        activity_login_BTN_register.setOnClickListener(new View.OnClickListener() {

            @SuppressLint("RestrictedApi")
            @Override
            public void onClick(View view) {
                List<Pair<View,String>> pairs = new ArrayList<Pair<View, String>>();
                pairs.add(Pair.create((View)activity_login_TV_name, "TV_name"));
                pairs.add(Pair.create((View)activity_login_TV_pw, "TV_pw"));
                pairs.add(Pair.create((View)activity_login_ET_name, "ET_name"));
                pairs.add(Pair.create((View)activity_login_ET_pw, "ET_pw"));
                pairs.add(Pair.create((View)activity_login_BTN_register, "BTN"));

                Bundle bundle = ActivityOptions.makeSceneTransitionAnimation(LoginActivity.this, pairs.toArray(new Pair[]{})).toBundle();
                Intent intent = new Intent(LoginActivity.this, RegisterActivity.class);
                startActivityForResult(intent, REQUESTCODE, bundle);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==REQUESTCODE && resultCode==202){
            String name = data.getStringExtra("name");
            String pw = data.getStringExtra("pw");
            activity_login_ET_name.setText(name);
            activity_login_ET_pw.setText(pw);
        }
    }


    private void checkValid(){
        if(!activity_login_ET_pw.getText().toString().trim().isEmpty() &&
                !activity_login_ET_name.getText().toString().trim().isEmpty()){
            ChangeColor(true);
        }else{
            ChangeColor(false);
        }
    }

    private void ChangeColor(Boolean canLogin){
        if(canLogin){
            activity_login_BTN_login.setClickable(true);
            activity_login_SW_remember.setClickable(true);
            activity_login_BTN_login.setBackgroundColor(getResources().getColor(R.color.colorPinkMain));
            activity_login_SW_remember.setTextColor(getResources().getColor(R.color.colorPinkMain));
        }else{
            activity_login_BTN_login.setClickable(false);
            activity_login_SW_remember.setClickable(false);
            activity_login_BTN_login.setBackgroundColor(getResources().getColor(R.color.light_gray));
            activity_login_SW_remember.setTextColor(getResources().getColor(R.color.light_gray));
        }
    }
}
