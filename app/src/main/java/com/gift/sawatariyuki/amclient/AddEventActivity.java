package com.gift.sawatariyuki.amclient;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.WindowManager;
import android.widget.TextView;

public class AddEventActivity extends AppCompatActivity {

    private TextView activity_addevent_TV_name;


    //全局变量
    private String username = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
        setContentView(R.layout.activity_addevent);

        initView();
        initListener();
        initData();



    }


    private void initView() {
        activity_addevent_TV_name = findViewById(R.id.activity_addevent_TV_name);
    }

    private void initListener() {

    }

    private void initData() {
        username = getIntent().getStringExtra("name");
        activity_addevent_TV_name.setText("@" + username);
    }

}
