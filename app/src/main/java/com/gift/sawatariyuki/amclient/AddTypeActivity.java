package com.gift.sawatariyuki.amclient;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import com.gift.sawatariyuki.amclient.Bean.Type;

import java.util.List;

public class AddTypeActivity extends AppCompatActivity {

    //全局变量
    private String username = null;
    private List<Type> types = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_type);

        initView();
        initListener();
        initData();
    }

    private void initView() {
    }

    private void initListener() {
    }

    private void initData() {
        username = getIntent().getStringExtra("name");
        Log.d("DEBUG", username);
        types = (List<Type>) getIntent().getSerializableExtra("types");
    }


}
