package com.gift.sawatariyuki.amclient;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.gift.sawatariyuki.amclient.Utils.dataRecoder.DataRecorder;

public class SettingsActivity extends AppCompatActivity {
    private Button settings_BTN_clear_data;

    private DataRecorder recorder;
    //全局变量

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        recorder = new DataRecorder(this);
        initView();
        initListener();
    }

    private void initView() {
        settings_BTN_clear_data = findViewById(R.id.settings_BTN_clear_data);

    }

    private void initListener() {
        //清除本地所有数据
        settings_BTN_clear_data.setOnClickListener(new View.OnClickListener() {
            long preTime;

            @Override
            public void onClick(View v) {
                long time = System.currentTimeMillis() - preTime;
                if (time > 2000) {
                    Toast.makeText(SettingsActivity.this, "Press again to clear all user's data", Toast.LENGTH_SHORT).show();
                    preTime = System.currentTimeMillis();
                    return;
                }
                recorder.clear();
                Toast.makeText(SettingsActivity.this, "Data has been cleared", Toast.LENGTH_SHORT).show();
            }
        });

    }
}
