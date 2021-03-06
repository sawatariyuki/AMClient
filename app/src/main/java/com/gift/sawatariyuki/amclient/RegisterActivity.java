package com.gift.sawatariyuki.amclient;

import android.content.Intent;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.gift.sawatariyuki.amclient.Bean.DefaultResponse;
import com.gift.sawatariyuki.amclient.Bean.LoginResponse;
import com.gift.sawatariyuki.amclient.ServerNetwork.RequestCenter;
import com.gift.sawatariyuki.amclient.Utils.MD5Encrypt;
import com.gift.sawatariyuki.amclient.Utils.dataRecoder.DataRecorder;
import com.gift.sawatariyuki.amclient.Utils.okHttp.listener.DisposeDataListener;
import com.gift.sawatariyuki.amclient.Utils.okHttp.request.RequestParams;
import com.gift.sawatariyuki.amclient.Utils.validation.InputValidation;
import com.gift.sawatariyuki.amclient.Utils.validation.NetworkValidation;

public class RegisterActivity extends AppCompatActivity {
    private TextView activity_register_TV_name;
    private TextView activity_register_TV_pw;
    private TextView activity_register_TV_pw_confirm;
    private TextView activity_register_TV_email;
    private EditText activity_register_ET_name;
    private EditText activity_register_ET_pw;
    private EditText activity_register_ET_pw_confirm;
    private EditText activity_register_ET_email;

    private Button activity_register_BTN_register;
    private Button activity_register_BTN_cancel;

    private DataRecorder recorder;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        initView();
        recorder = new DataRecorder(this);
        initListener();
    }

    private void initView() {
        activity_register_TV_name = findViewById(R.id.activity_register_TV_name);
        activity_register_TV_pw = findViewById(R.id.activity_register_TV_pw);
        activity_register_TV_pw_confirm = findViewById(R.id.activity_register_TV_pw_confirm);
        activity_register_TV_email = findViewById(R.id.activity_register_TV_email);
        activity_register_ET_name = findViewById(R.id.activity_register_ET_name);
        activity_register_ET_pw = findViewById(R.id.activity_register_ET_pw);
        activity_register_ET_pw_confirm = findViewById(R.id.activity_register_ET_pw_confirm);
        activity_register_ET_email = findViewById(R.id.activity_register_ET_email);
        activity_register_BTN_register = findViewById(R.id.activity_register_BTN_register);
        activity_register_BTN_cancel = findViewById(R.id.activity_register_BTN_cancel);
    }

    private void initListener(){
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

        activity_register_ET_name.addTextChangedListener(textWatcher);
        activity_register_ET_pw.addTextChangedListener(textWatcher);
        activity_register_ET_pw_confirm.addTextChangedListener(textWatcher);
        activity_register_ET_email.addTextChangedListener(textWatcher);

        activity_register_BTN_register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final String pw = activity_register_ET_pw.getText().toString().trim();
                String pwConfirm = activity_register_ET_pw_confirm.getText().toString().trim();
                if(!pw.equals(pwConfirm)){
                    Toast.makeText(RegisterActivity.this, "Two passwords are inconsistent", Toast.LENGTH_SHORT).show();
                    return;
                }
                if(pw.length()<8){
                    Toast.makeText(RegisterActivity.this, "Password needs 8 characters at least", Toast.LENGTH_SHORT).show();
                    return;
                }
                final String email = activity_register_ET_email.getText().toString().trim();
                if(!InputValidation.isEmail(email)){
                    Toast.makeText(RegisterActivity.this, "Incorrect format for email", Toast.LENGTH_SHORT).show();
                    return;
                }
                final String name = activity_register_ET_name.getText().toString().trim();

                //SEND POST REQUEST
                RequestParams params = new RequestParams();
                params.put("name", name);
                params.put("pw", MD5Encrypt.getMD5(pw));
                params.put("pwConfirm", MD5Encrypt.getMD5(pwConfirm));
                params.put("email", email);

                RequestCenter.registerUser(new DisposeDataListener() {
                    @Override
                    public void onSuccess(Object responseObj) {
                        if (responseObj instanceof LoginResponse){
                            LoginResponse response = (LoginResponse) responseObj;
                            String msg = response.getMsg();
                            Toast.makeText(RegisterActivity.this, msg, Toast.LENGTH_SHORT).show();
                            if(msg.equals("用户:" + name + " 注册成功，激活码已发送到注册邮箱")){
                                recorder.save("loggedUsername", name);
                                recorder.save("email", email);
                                recorder.save("isActivated", false);
                                recorder.save("ActivateCode", response.getData().getFields().getActivateCode());
                                Intent data = new Intent();
                                data.putExtra("name", name);
                                data.putExtra("pw", pw);
                                data.putExtra("email", email);
                                setResult(202, data);
                                finish();
                            }
                        } else if (responseObj instanceof DefaultResponse) {
                            DefaultResponse response = (DefaultResponse) responseObj;
                            Toast.makeText(RegisterActivity.this, response.getMsg(), Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onFailure(Object responseObj) {

                    }
                }, params, RegisterActivity.this);
            }
        });

        activity_register_BTN_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        ChangeColor(false);
    }

    private void checkValid(){
        if(!activity_register_ET_name.getText().toString().trim().isEmpty() &&
                !activity_register_ET_pw.getText().toString().trim().isEmpty() &&
                !activity_register_ET_pw_confirm.getText().toString().trim().isEmpty() &&
                !activity_register_ET_email.getText().toString().trim().isEmpty()){
            ChangeColor(true);
        }else{
            ChangeColor(false);
        }
    }

    private void ChangeColor(Boolean canRegister){
        if(canRegister){
            activity_register_BTN_register.setClickable(true);
            activity_register_BTN_register.setBackgroundColor(getResources().getColor(R.color.colorPinkMain));
        }else{
            activity_register_BTN_register.setClickable(false);
            activity_register_BTN_register.setBackgroundColor(getResources().getColor(R.color.light_gray));
        }
    }
}
