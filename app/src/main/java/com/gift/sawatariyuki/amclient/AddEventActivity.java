package com.gift.sawatariyuki.amclient;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.gift.sawatariyuki.amclient.Adapter.TypeSelectorAdapter;
import com.gift.sawatariyuki.amclient.Bean.DefaultResponse;
import com.gift.sawatariyuki.amclient.Bean.Type;
import com.gift.sawatariyuki.amclient.ServerNetwork.RequestCenter;
import com.gift.sawatariyuki.amclient.Utils.DateTimePicker;
import com.gift.sawatariyuki.amclient.Utils.TimeZoneChanger;
import com.gift.sawatariyuki.amclient.Utils.okHttp.listener.DisposeDataListener;
import com.gift.sawatariyuki.amclient.Utils.okHttp.request.RequestParams;
import com.melnykov.fab.FloatingActionButton;

import java.io.Serializable;
import java.util.Calendar;
import java.util.List;

public class AddEventActivity extends AppCompatActivity {

    private TextView activity_addevent_TV_name;

    private EditText activity_addevent_ET_title;
    private EditText activity_addevent_ET_description;
    private EditText activity_addevent_ET_start;
    private EditText activity_addevent_ET_end;
    private EditText activity_addevent_ET_length;

    private TextView activity_addevent_TV_addtype;

    private Button activity_addevent_BTN_submit;
    private Button activity_addevent_BTN_reset;

    private Spinner activity_addevent_type_selector;
    private RadioGroup activity_addevent_emergency;

    private FloatingActionButton activity_addevent_fab;

    //全局变量
    private String username = null;
    private List<Type> types = null;
    private int userLevel;
    private String typeName = null;

    private final int REQUESTCODE = 103;

    private TypeSelectorAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
        setContentView(R.layout.activity_addevent);

        initView();
        initListener();
        initData();
        ChangeColor(false, false);
    }


    private void initView() {
        activity_addevent_TV_name = findViewById(R.id.activity_addevent_TV_name);

        activity_addevent_ET_title = findViewById(R.id.activity_addevent_ET_title);
        activity_addevent_ET_description =findViewById(R.id.activity_addevent_ET_description);
        activity_addevent_ET_start = findViewById(R.id.activity_addevent_ET_start);
        activity_addevent_ET_end = findViewById(R.id.activity_addevent_ET_end);
        activity_addevent_ET_length = findViewById(R.id.activity_addevent_ET_length);

        activity_addevent_TV_addtype = findViewById(R.id.activity_addevent_TV_addtype);

        activity_addevent_BTN_submit = findViewById(R.id.activity_addevent_BTN_submit);
        activity_addevent_BTN_reset = findViewById(R.id.activity_addevent_BTN_reset);

        activity_addevent_type_selector = findViewById(R.id.activity_addevent_type_selector);
        activity_addevent_emergency = findViewById(R.id.activity_addevent_emergency);
        activity_addevent_emergency.check(R.id.activity_addevent_emergency_0);

        ChangeColor(false, false);

        activity_addevent_fab = findViewById(R.id.activity_addevent_fab);
    }

    private void initListener() {
        //返回
        activity_addevent_fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        //紧要性选择
        activity_addevent_emergency.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (group.getCheckedRadioButtonId()){
                    case R.id.activity_addevent_emergency_0:
                        userLevel = 0;
                        break;
                    case R.id.activity_addevent_emergency_1:
                        userLevel = 1;
                        break;
                    case R.id.activity_addevent_emergency_2:
                        userLevel = 2;
                        break;
                    case R.id.activity_addevent_emergency_3:
                        userLevel = 3;
                        break;
                    default:
                        userLevel = 0;
                        break;
                }
            }
        });

        //文本改变
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
        activity_addevent_ET_title.addTextChangedListener(watcher);
        activity_addevent_ET_description.addTextChangedListener(watcher);
        activity_addevent_ET_start.addTextChangedListener(watcher);
        activity_addevent_ET_end.addTextChangedListener(watcher);
        activity_addevent_ET_length.addTextChangedListener(watcher);

        //提交
        activity_addevent_BTN_submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                postData();
            }
        });

        //重置
        activity_addevent_BTN_reset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                reset();
            }
        });

        //选择日期时间
        activity_addevent_ET_start.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DateTimePicker.GetDateTimePicker(activity_addevent_ET_start, AddEventActivity.this);
            }
        });
        activity_addevent_ET_end.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DateTimePicker.GetDateTimePicker(activity_addevent_ET_end, AddEventActivity.this);
            }
        });

        //选择type
        activity_addevent_type_selector.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                typeName = types.get(position).getFields().getName();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        //添加事务类型
        activity_addevent_TV_addtype.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AddEventActivity.this, AddTypeActivity.class);
                intent.putExtra("name", username);
                startActivityForResult(intent, REQUESTCODE);
            }
        });
    }

    private void initData() {
        username = getIntent().getStringExtra("name");
        activity_addevent_TV_name.setText("@" + username);
        types = (List<Type>) getIntent().getSerializableExtra("types");
        adapter = new TypeSelectorAdapter(types, AddEventActivity.this);
        activity_addevent_type_selector.setAdapter(adapter);

        typeName = types.get(activity_addevent_type_selector.getSelectedItemPosition()).getFields().getName();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==REQUESTCODE && resultCode==501){   //添加 事件类型 后返回到 添加事件 界面
            types = (List<Type>) data.getSerializableExtra("types");
            if(types == null){
                onBackPressed();
            }else{
                adapter.updateData(types);
                adapter.notifyDataSetChanged();
            }
        }
    }

    @Override
    public void onBackPressed() {
        setResult(301);
        super.onBackPressed();
    }

    //重置
    private void reset(){
        activity_addevent_ET_title.setText("");
        activity_addevent_ET_description.setText("");
        activity_addevent_ET_start.setText("");
        activity_addevent_ET_end.setText("");
        activity_addevent_ET_length.setText("");
        activity_addevent_emergency.check(R.id.activity_addevent_emergency_0);
        activity_addevent_type_selector.setSelection(0);
    }

    //发送新增事务的POST
    private void postData(){
        String title = activity_addevent_ET_title.getText().toString().trim();
        String description = activity_addevent_ET_description.getText().toString().trim();
        String start = TimeZoneChanger.StringLocalToStringUTC(activity_addevent_ET_start.getText().toString());
        String end = TimeZoneChanger.StringLocalToStringUTC(activity_addevent_ET_end.getText().toString());
        String length = activity_addevent_ET_length.getText().toString();

        //SEND POST REQUEST
        RequestParams params = new RequestParams();
        params.put("name", username);
        params.put("title", title);
        params.put("description", description);
        params.put("typeName", typeName);
        params.put("userLevel", String.valueOf(userLevel));
        params.put("userStartTime", start);
        params.put("userEndTime", end);
        params.put("length", length);
        RequestCenter.addEvent(new DisposeDataListener() {
            @Override
            public void onSuccess(Object responseObj) {
                DefaultResponse response = (DefaultResponse) responseObj;
                String msg = response.getMsg();
                Toast.makeText(AddEventActivity.this, msg, Toast.LENGTH_SHORT).show();
                if(msg.equals("事务已新增")){
                    setResult(301);
                    finish();
                }
            }

            @Override
            public void onFailure(Object responseObj) {

            }
        }, params, AddEventActivity.this);

    }

    private void checkValid(){
        if(!activity_addevent_ET_title.getText().toString().trim().isEmpty() &&
                !activity_addevent_ET_description.getText().toString().trim().isEmpty() &&
                !activity_addevent_ET_start.getText().toString().trim().isEmpty() &&
                !activity_addevent_ET_end.getText().toString().trim().isEmpty() &&
                !activity_addevent_ET_length.getText().toString().trim().isEmpty()){
            ChangeColor(true, true);
        }else if(!activity_addevent_ET_title.getText().toString().trim().isEmpty() ||
                !activity_addevent_ET_description.getText().toString().trim().isEmpty() ||
                !activity_addevent_ET_start.getText().toString().trim().isEmpty() ||
                !activity_addevent_ET_end.getText().toString().trim().isEmpty() ||
                !activity_addevent_ET_length.getText().toString().trim().isEmpty()){
            ChangeColor(false, true);
        }else{
            ChangeColor(false, false);
        }
    }

    private void ChangeColor(Boolean canSubmit, Boolean canReset){
        if(canSubmit){
            activity_addevent_BTN_submit.setClickable(true);
            activity_addevent_BTN_submit.setBackgroundColor(getResources().getColor(R.color.colorPinkMain));
        }else{
            activity_addevent_BTN_submit.setClickable(false);
            activity_addevent_BTN_submit.setBackgroundColor(getResources().getColor(R.color.light_gray));
        }
        if(canReset){
            activity_addevent_BTN_reset.setClickable(true);
            activity_addevent_BTN_reset.setBackgroundColor(getResources().getColor(R.color.colorPinkMain));
        }else{
            activity_addevent_BTN_reset.setClickable(false);
            activity_addevent_BTN_reset.setBackgroundColor(getResources().getColor(R.color.light_gray));
        }
    }
}
