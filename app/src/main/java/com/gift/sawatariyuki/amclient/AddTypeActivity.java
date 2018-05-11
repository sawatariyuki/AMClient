package com.gift.sawatariyuki.amclient;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.OrientationHelper;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Adapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.gift.sawatariyuki.amclient.Adapter.RecyclerViewAdapterForType;
import com.gift.sawatariyuki.amclient.Bean.DefaultResponse;
import com.gift.sawatariyuki.amclient.Bean.Event;
import com.gift.sawatariyuki.amclient.Bean.GetTypeResponse;
import com.gift.sawatariyuki.amclient.Bean.Type;
import com.gift.sawatariyuki.amclient.Listener.OnItemClickListener;
import com.gift.sawatariyuki.amclient.ServerNetwork.RequestCenter;
import com.gift.sawatariyuki.amclient.Utils.okHttp.listener.DisposeDataListener;
import com.gift.sawatariyuki.amclient.Utils.okHttp.request.RequestParams;
import com.melnykov.fab.FloatingActionButton;

import java.io.Serializable;
import java.util.List;

public class AddTypeActivity extends AppCompatActivity {
    private RecyclerView add_type_RV;
    private LinearLayoutManager layoutManager;

    private EditText add_type_name;
    private EditText add_type_description;
    private Button add_type_BTN_submit;
    private Button add_type_BTN_delete;

    private RecyclerViewAdapterForType adapter;
    private TextView add_type_no_data;

    private FloatingActionButton add_type_fab;

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
        ChangeColor(false);
    }

    private void initView() {
        add_type_RV = findViewById(R.id.add_type_RV);
        layoutManager = new LinearLayoutManager(this);
        add_type_RV.setLayoutManager(layoutManager);
        layoutManager.setOrientation(OrientationHelper.VERTICAL);
        add_type_no_data = findViewById(R.id.add_type_no_data);

        add_type_name = findViewById(R.id.add_type_name);
        add_type_description = findViewById(R.id.add_type_description);
        add_type_BTN_submit = findViewById(R.id.add_type_BTN_submit);
        add_type_BTN_delete = findViewById(R.id.add_type_BTN_delete);

        add_type_fab = findViewById(R.id.add_type_fab);
    }

    private void initListener() {
        //返回
        add_type_fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        //文本监听
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
        add_type_name.addTextChangedListener(watcher);
        add_type_description.addTextChangedListener(watcher);

        //更新
        add_type_BTN_submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updateTypeData();
            }
        });

        //删除
        add_type_BTN_delete.setOnClickListener(new View.OnClickListener() {
            long preTime;
            @Override
            public void onClick(View v) {
                long time = System.currentTimeMillis() - preTime;
                if (time > 2000) {
                    Toast.makeText(AddTypeActivity.this, "Press again to delete a type", Toast.LENGTH_SHORT).show();
                    preTime = System.currentTimeMillis();
                    return;
                }
                deleteType();
            }
        });
    }

    private void initData() {
        username = getIntent().getStringExtra("name");
        getTypeData();
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent();
        intent.putExtra("types", (Serializable) types);
        setResult(501, intent);
        super.onBackPressed();
    }

    //删除事务类型
    private void deleteType(){
        //SEND GET REQUEST
        RequestParams params = new RequestParams();
        params.put("name", username);
        params.put("typeName", add_type_name.getText().toString().trim());
        RequestCenter.deleteEventType(new DisposeDataListener() {
            @Override
            public void onSuccess(Object responseObj) {
                if(responseObj instanceof  DefaultResponse){
                    DefaultResponse response = (DefaultResponse) responseObj;
                    Toast.makeText(AddTypeActivity.this, response.getMsg(), Toast.LENGTH_SHORT).show();
                    getTypeData();
                }
            }

            @Override
            public void onFailure(Object responseObj) {

            }
        }, params, AddTypeActivity.this);

    }

    //更新或创建事务类型
    private void updateTypeData(){
        //SEND GET REQUEST
        RequestParams params = new RequestParams();
        params.put("name", username);
        params.put("typeName", add_type_name.getText().toString().trim());
        params.put("description", add_type_description.getText().toString().trim());
        RequestCenter.addOrUpdateEventType(new DisposeDataListener() {
            @Override
            public void onSuccess(Object responseObj) {
                if(responseObj instanceof DefaultResponse){
                    DefaultResponse response = (DefaultResponse) responseObj;
                    Toast.makeText(AddTypeActivity.this, response.getMsg(), Toast.LENGTH_SHORT).show();
                    getTypeData();
                }
            }

            @Override
            public void onFailure(Object responseObj) {

            }
        }, params, AddTypeActivity.this);
    }

    //请求事务类型信息 并 更新RV
    private void getTypeData(){
        //SEND GET REQUEST
        //获取用户事务类型
        RequestParams params = new RequestParams();
        params.put("name", username);
        RequestCenter.getType(new DisposeDataListener() {
            @Override
            public void onSuccess(Object responseObj) {
                if(responseObj instanceof GetTypeResponse){
                    GetTypeResponse response = (GetTypeResponse) responseObj;
                    types = response.getData();
                    add_type_no_data.setVisibility(View.INVISIBLE);
                    add_type_RV.setVisibility(View.VISIBLE);
                    if(adapter == null){
                        adapter = new RecyclerViewAdapterForType(types);
                        add_type_RV.setAdapter(adapter);
                        adapter.setOnItemClickListener(new OnItemClickListener<Type>() {
                            @Override
                            public void onItemClick(Type itemValue, int position) {
                                showData(position);
                            }
                        });
                    }else{
                        adapter.updateData(types);
                        adapter.notifyDataSetChanged();
                    }
                }else if (responseObj instanceof DefaultResponse){
                    DefaultResponse response = (DefaultResponse) responseObj;
                    if(response.getMsg().equals("未查询到事务类型")){
                        types = null;
                        add_type_RV.setVisibility(View.INVISIBLE);
                        add_type_no_data.setVisibility(View.VISIBLE);
                    }
                }
            }

            @Override
            public void onFailure(Object responseObj) {

            }
        }, params, AddTypeActivity.this);
    }


    private void showData(int position){
        Type type = types.get(position);
        add_type_name.setText(type.getFields().getName());
        add_type_description.setText(type.getFields().getDescription());
    }

    private void checkValid(){
        if(!add_type_name.getText().toString().trim().isEmpty() &&
                !add_type_description.getText().toString().trim().isEmpty()){
            ChangeColor(true);
        }else{
            ChangeColor(false);
        }
    }

    private void ChangeColor(Boolean hasData){
        if(hasData){
            add_type_BTN_submit.setClickable(true);
            add_type_BTN_delete.setClickable(true);
            add_type_BTN_submit.setBackgroundColor(getResources().getColor(R.color.colorPinkMain));
            add_type_BTN_delete.setBackgroundColor(getResources().getColor(R.color.colorPinkMain));
        }else{
            add_type_BTN_submit.setClickable(false);
            add_type_BTN_delete.setClickable(false);
            add_type_BTN_submit.setBackgroundColor(getResources().getColor(R.color.light_gray));
            add_type_BTN_delete.setBackgroundColor(getResources().getColor(R.color.light_gray));
        }
    }
}
