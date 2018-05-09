package com.gift.sawatariyuki.amclient;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.gift.sawatariyuki.amclient.Bean.DefaultResponse;
import com.gift.sawatariyuki.amclient.Bean.UserDefault;
import com.gift.sawatariyuki.amclient.Bean.UserDetail;
import com.gift.sawatariyuki.amclient.Bean.UserInfoResponse;
import com.gift.sawatariyuki.amclient.ServerNetwork.RequestCenter;
import com.gift.sawatariyuki.amclient.Utils.DateTimePicker;
import com.gift.sawatariyuki.amclient.Utils.TimeZoneChanger;
import com.gift.sawatariyuki.amclient.Utils.okHttp.listener.DisposeDataListener;
import com.gift.sawatariyuki.amclient.Utils.okHttp.request.RequestParams;
import com.melnykov.fab.FloatingActionButton;

import java.util.Calendar;


public class UserInfoActivity extends AppCompatActivity {
    private TextView user_info_username;
    private TextView user_info_email;
    private TextView user_info_joined;
    private TextView user_info_last_seen;
    private EditText user_info_ET_weight;
    private EditText user_info_ET_birthday;
    private TextView user_info_TV_age;
    private EditText user_info_ET_birthplace;
    private EditText user_info_ET_address;
    private RadioGroup user_info_RG;

    private ImageView user_info_IV_reset;
    private Button user_info_BTN_submit;

    private FloatingActionButton user_info_fab;

    //全局变量
    private String username = null;
    private String gender = "男";
    private String birthday_date_end = null; //生日只显示前10位(1990-02-02) 后6位暂存 防止转时区时出错

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_info);

        initView();
        initListener();
        initData();
    }

    private void initView() {
        user_info_username = findViewById(R.id.user_info_username);
        user_info_email = findViewById(R.id.user_info_email);
        user_info_joined = findViewById(R.id.user_info_joined);
        user_info_last_seen = findViewById(R.id.user_info_last_seen);
        user_info_ET_weight = findViewById(R.id.user_info_ET_weight);
        user_info_ET_birthday = findViewById(R.id.user_info_ET_birthday);
        user_info_TV_age = findViewById(R.id.user_info_TV_age);
        user_info_ET_birthplace = findViewById(R.id.user_info_ET_birthplace);
        user_info_ET_address = findViewById(R.id.user_info_ET_address);
        user_info_RG = findViewById(R.id.user_info_RG);

        user_info_IV_reset = findViewById(R.id.user_info_IV_reset);
        user_info_BTN_submit = findViewById(R.id.user_info_BTN_submit);

        user_info_fab = findViewById(R.id.user_info_fab);
    }

    private void initListener() {
        //返回
        user_info_fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        TextWatcher watcher = new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                checkValid();
            }
        };

        user_info_ET_weight.addTextChangedListener(watcher);
        user_info_ET_birthday.addTextChangedListener(watcher);
        user_info_ET_birthplace.addTextChangedListener(watcher);
        user_info_ET_address.addTextChangedListener(watcher);

        //选择出生日期
        user_info_ET_birthday.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DateTimePicker.GetDatePicker(user_info_ET_birthday, UserInfoActivity.this);
            }
        });

        //性别选择
        user_info_RG.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (group.getCheckedRadioButtonId()){
                    case R.id.user_info_RB_male:
                        gender = "男";
                        break;
                    case R.id.user_info_RB_female:
                        gender = "女";
                        break;
                    default:
                        gender = "男";
                        break;
                }
            }
        });

        //重置
        user_info_IV_reset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getUserData();
            }
        });

        //submit 更新用户数据
        user_info_BTN_submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                RequestParams params = new RequestParams();
                params.put("name", username);
                params.put("gender", gender);
                params.put("weight", user_info_ET_weight.getText().toString().trim());
                params.put("birthplace", user_info_ET_birthplace.getText().toString().trim());
                params.put("liveplace", user_info_ET_address.getText().toString().trim());
                String birthday = TimeZoneChanger.StringLocalToStringUTC(user_info_ET_birthday.getText().toString()+birthday_date_end);
                params.put("birthday", birthday.substring(0,10));
                RequestCenter.updateUserDetail(new DisposeDataListener() {
                    @Override
                    public void onSuccess(Object responseObj) {
                        DefaultResponse response = (DefaultResponse) responseObj;
                        String msg = response.getMsg();
                        Toast.makeText(UserInfoActivity.this, msg, Toast.LENGTH_SHORT).show();
                        if(msg.equals("用户详细资料已更新")){
                            getUserData();
                        }
                    }

                    @Override
                    public void onFailure(Object responseObj) {

                    }
                }, params, UserInfoActivity.this);
            }
        });
    }

    private void initData() {
        username = getIntent().getStringExtra("name");
        user_info_username.setText(username);
        getUserData();
    }

    private void getUserData(){
        //SEND POST REQUEST
        RequestParams params = new RequestParams();
        params.put("name", username);
        RequestCenter.getUserInfo(new DisposeDataListener() {
            @Override
            public void onSuccess(Object responseObj) {
                if(responseObj instanceof UserInfoResponse){
                    UserInfoResponse response = (UserInfoResponse) responseObj;

                    UserDefault u = response.getData().getU_default();
                    UserDetail ud = response.getData().getU_detail();
                    user_info_email.setText(u.getFields().getEmail());
                    user_info_joined.setText(TimeZoneChanger.DateLocalTOStringLocalCN(u.getFields().getDate_joined()));
                    user_info_last_seen.setText(TimeZoneChanger.DateLocalTOStringLocalCN(u.getFields().getLast_joined()));
                    user_info_ET_weight.setText(String.valueOf(ud.getFields().getWeight()));
                    String birthdayStr = TimeZoneChanger.DateLocalTOStringLocalEN(ud.getFields().getBirthday());
                    birthday_date_end = birthdayStr.substring(10, birthdayStr.length());
                    user_info_ET_birthday.setText(birthdayStr.substring(0,10));
                    user_info_TV_age.setText(String.valueOf(ud.getFields().getAge()));
                    user_info_ET_birthplace.setText(ud.getFields().getBirthplace());
                    user_info_ET_address.setText(ud.getFields().getLiveplace());
                    user_info_ET_address.setSelection(ud.getFields().getLiveplace().length());
                    switch (ud.getFields().getGender()){
                        case "男":
                            user_info_RG.check(R.id.user_info_RB_male);
                            break;
                        case "女":
                            user_info_RG.check(R.id.user_info_RB_female);
                            break;
                        default:
                            user_info_RG.check(R.id.user_info_RB_male);
                            break;
                    }
                }else if(responseObj instanceof DefaultResponse){
                    Toast.makeText(UserInfoActivity.this, ((DefaultResponse) responseObj).getMsg(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Object responseObj) {

            }
        }, params, UserInfoActivity.this);
    }


    private void checkValid(){
        if(!user_info_ET_weight.getText().toString().trim().isEmpty() &&
                !user_info_ET_birthday.getText().toString().trim().isEmpty() &&
                !user_info_ET_birthplace.getText().toString().trim().isEmpty() &&
                !user_info_ET_address.getText().toString().trim().isEmpty()){
            ChangeColor(true);
        }else{
            ChangeColor(false);
        }
    }

    private void ChangeColor(Boolean canSubmit){
        if(canSubmit){
            user_info_BTN_submit.setClickable(true);
            user_info_BTN_submit.setBackgroundColor(getResources().getColor(R.color.colorPinkMain));
        }else{
            user_info_BTN_submit.setClickable(false);
            user_info_BTN_submit.setBackgroundColor(getResources().getColor(R.color.light_gray));
        }
    }
}
