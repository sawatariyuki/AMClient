package com.gift.sawatariyuki.amclient;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.gift.sawatariyuki.amclient.Adapter.RecyclerViewAdapterForLog;
import com.gift.sawatariyuki.amclient.Bean.DefaultResponse;
import com.gift.sawatariyuki.amclient.Bean.LogResponse;
import com.gift.sawatariyuki.amclient.ServerNetwork.RequestCenter;
import com.gift.sawatariyuki.amclient.Utils.okHttp.listener.DisposeDataListener;
import com.gift.sawatariyuki.amclient.Utils.okHttp.request.RequestParams;

public class UserLogActivity extends AppCompatActivity {

    private RecyclerView user_log_RV;
    private LinearLayoutManager layoutManager;
    private RecyclerViewAdapterForLog adapter;
    private TextView user_log_no_data;

    //全局变量
    private String username = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_log);

        initView();
//        initListener();
        initData();
    }

    private void initView() {
        user_log_RV = findViewById(R.id.user_log_RV);
        layoutManager = new LinearLayoutManager(this);
        user_log_RV.setLayoutManager(layoutManager);
        user_log_no_data = findViewById(R.id.user_log_no_data);
    }

//    private void initListener() {
//
//    }

    private void initData() {
        username = getIntent().getStringExtra("name");
        getData();
    }

    private void getData(){
        RequestParams params = new RequestParams();
        params.put("name", username);
        RequestCenter.getUserLog(new DisposeDataListener() {
            @Override
            public void onSuccess(Object responseObj) {
                if (responseObj instanceof LogResponse){
                    LogResponse response = (LogResponse) responseObj;
                    if (adapter==null) {
                        adapter = new RecyclerViewAdapterForLog(response.getData());
                        user_log_RV.setAdapter(adapter);
                    } else {
                        adapter.updateData(response.getData());
                        adapter.notifyDataSetChanged();
                    }
                    user_log_no_data.setVisibility(View.INVISIBLE);

                }else if(responseObj instanceof DefaultResponse){
                    DefaultResponse response = (DefaultResponse) responseObj;
                    Toast.makeText(UserLogActivity.this, response.getMsg(), Toast.LENGTH_SHORT).show();
                    user_log_no_data.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onFailure(Object responseObj) {

            }
        }, params, UserLogActivity.this);
    }
}
