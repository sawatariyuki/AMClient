package com.gift.sawatariyuki.amclient;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.OrientationHelper;
import android.support.v7.widget.RecyclerView;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.gift.sawatariyuki.amclient.Adapter.RecyclerViewAdapterForEventSimple;
import com.gift.sawatariyuki.amclient.Adapter.TypeSelectorAdapter;
import com.gift.sawatariyuki.amclient.Bean.DefaultResponse;
import com.gift.sawatariyuki.amclient.Bean.Event;
import com.gift.sawatariyuki.amclient.Bean.Type;
import com.gift.sawatariyuki.amclient.Listener.OnItemClickListener;
import com.gift.sawatariyuki.amclient.ServerNetwork.RequestCenter;
import com.gift.sawatariyuki.amclient.Utils.TimeZoneChanger;
import com.gift.sawatariyuki.amclient.Utils.okHttp.listener.DisposeDataListener;
import com.gift.sawatariyuki.amclient.Utils.okHttp.request.RequestParams;
import com.melnykov.fab.FloatingActionButton;


import java.io.Serializable;
import java.util.Calendar;
import java.util.List;

public class UpdateEventActivity extends AppCompatActivity {
    private TextView TV_name;
    private TextView TV_back;

    private EditText ET_title;
    private EditText ET_description;
    private EditText ET_start;
    private EditText ET_end;
    private EditText ET_length;

    private TextView TV_recommend_start;
    private TextView TV_recommend_end;
    private TextView TV_ctime;
    private TextView TV_addtype;

    private Button BTN_delete;
    private Button BTN_reset;
    private Button BTN_cancelOrReactive;
    private FloatingActionButton fab;

    private Spinner type_selector;
    private RadioGroup emergency;

    //header
    private RecyclerView activity_update_event_RV;
    private LinearLayoutManager layoutManager;
    private RecyclerViewAdapterForEventSimple RVAdapter;

    //全局变量
    private List<Event> events = null;
    private Event eventShowed = null;
    private String username = null;
    private List<Type> types = null;
    private int userLevel;
    private String typeName = null;
    private Boolean isCancelled;

    private final int REQUESTCODE = 104;

    private TypeSelectorAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_event);

        initView();
        initListener();
        initData();

    }

    private void initView() {
        TV_name = findViewById(R.id.TV_name);
        TV_back = findViewById(R.id.TV_back);

        ET_title = findViewById(R.id.ET_title);
        ET_description = findViewById(R.id.ET_description);
        ET_start = findViewById(R.id.ET_start);
        ET_end = findViewById(R.id.ET_end);
        ET_length = findViewById(R.id.ET_length);

        TV_recommend_start = findViewById(R.id.TV_recommend_start);
        TV_recommend_end = findViewById(R.id.TV_recommend_end);
        TV_ctime = findViewById( R.id.TV_ctime);
        TV_addtype = findViewById(R.id.TV_addtype);

        BTN_delete = findViewById(R.id.BTN_delete);
        BTN_reset = findViewById(R.id.BTN_reset);
        BTN_cancelOrReactive = findViewById(R.id.BTN_cancelOrReactive);
        fab = findViewById(R.id.fab);

        type_selector = findViewById(R.id.type_selector);
        emergency = findViewById(R.id.emergency);

        ChangeColor(true, true);

        //header
        activity_update_event_RV = findViewById(R.id.activity_update_event_RV);
        layoutManager = new LinearLayoutManager(this);
        activity_update_event_RV.setLayoutManager(layoutManager);
        layoutManager.setOrientation(OrientationHelper.HORIZONTAL);

    }

    private void initListener() {
        //返回
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setResult(401);
                onBackPressed();
            }
        });

        //紧要性选择
        emergency.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (group.getCheckedRadioButtonId()){
                    case R.id.emergency_0:
                        userLevel = 0;
                        break;
                    case R.id.emergency_1:
                        userLevel = 1;
                        break;
                    case R.id.emergency_2:
                        userLevel = 2;
                        break;
                    case R.id.emergency_3:
                        userLevel = 3;
                        break;
                    default:
                        userLevel = 0;
                        break;
                }
            }
        });

        //删除
        BTN_delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                postDeleteData();
            }
        });

        //重置
        BTN_reset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                reset();
            }
        });

        //选择日期时间
        ET_start.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DateTimePicker(ET_start);
            }
        });
        ET_end.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DateTimePicker(ET_end);
            }
        });

        //选择type
        type_selector.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                typeName = types.get(position).getFields().getName();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        //取消事务
        BTN_cancelOrReactive.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                postCancelOrReactiveDate();
            }
        });

        //添加事务类型
        TV_addtype.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(UpdateEventActivity.this, AddTypeActivity.class);
                intent.putExtra("name", username);
                intent.putExtra("types", (Serializable) types);
                startActivityForResult(intent, REQUESTCODE);
            }
        });
    }

    private void initData() {
        username = getIntent().getStringExtra("name");
        types = (List<Type>) getIntent().getSerializableExtra("types");
        events = (List<Event>) getIntent().getSerializableExtra("events");
        int position = getIntent().getIntExtra("position", 0);
        eventShowed = events.get(position);

        TV_name.setText("@" + username);
        adapter = new TypeSelectorAdapter(types, UpdateEventActivity.this);
        type_selector.setAdapter(adapter);
        showData();

        //header
        RVAdapter = new RecyclerViewAdapterForEventSimple(events, types, UpdateEventActivity.this);
        activity_update_event_RV.setAdapter(RVAdapter);
        RVAdapter.moveToPosition(layoutManager, activity_update_event_RV, position);    //跳转到相应位置

        RVAdapter.setOnItemClickListener(new OnItemClickListener<Event>() {
            @Override
            public void onItemClick(Event itemValue, int position) {
                eventShowed = events.get(position);
                showData();
            }
        });
    }

    //显示数据
    private void showData(){
        for(int i=0; i<types.size(); i++){
            if(eventShowed.getFields().getEventType() == types.get(i).getPk()){
                type_selector.setSelection(i);
                typeName = types.get(i).getFields().getName();
            }
        }
        switch(eventShowed.getFields().getUserLevel()){
            case 0:
                emergency.check(R.id.emergency_0);
                userLevel = 0;
                break;
            case 1:
                emergency.check(R.id.emergency_1);
                userLevel = 1;
                break;
            case 2:
                emergency.check(R.id.emergency_2);
                userLevel = 2;
                break;
            case 3:
                emergency.check(R.id.emergency_3);
                userLevel = 3;
                break;
            default:
                emergency.check(R.id.emergency_0);
                userLevel = 0;
                break;
        }
        ET_title.setText(eventShowed.getFields().getTitle());
        ET_description.setText(eventShowed.getFields().getDescription());
        ET_length.setText(String.valueOf(eventShowed.getFields().getLength()));
        ET_start.setText(TimeZoneChanger.DateLocalTOStringLocalEN(eventShowed.getFields().getUserStartTime()));
        ET_end.setText(TimeZoneChanger.DateLocalTOStringLocalEN(eventShowed.getFields().getUserEndTime()));

        TV_recommend_start.setText(TimeZoneChanger.DateLocalTOStringLocalEN(eventShowed.getFields().getSysStartTime()));
        TV_recommend_end.setText(TimeZoneChanger.DateLocalTOStringLocalEN(eventShowed.getFields().getSysEndTime()));
        TV_ctime.setText(TimeZoneChanger.DateLocalTOStringLocalEN(eventShowed.getFields().getCtime()));


        checkValid();
    }

    //取消或重新激活请求
    private void postCancelOrReactiveDate(){
        RequestParams params = new RequestParams();
        params.put("name", username);
        params.put("pk", String.valueOf(eventShowed.getPk()));
        if(isCancelled){
            params.put("cancelOrReactive", "1");
        }else{
            params.put("cancelOrReactive", "0");
        }
        RequestCenter.cancelOrReactiveEvent(new DisposeDataListener() {
            @Override
            public void onSuccess(Object responseObj) {
                DefaultResponse response = (DefaultResponse) responseObj;
                Toast.makeText(UpdateEventActivity.this, response.getMsg(), Toast.LENGTH_SHORT).show();
                int pos = 0;
                for(int i=0; i<events.size(); i++){
                    if(events.get(i).getPk() == eventShowed.getPk()){
                        pos = i;
                        break;
                    }
                }
                if(isCancelled){
                    events.get(pos).getFields().setState(0);
                }else{
                    events.get(pos).getFields().setState(2);
                }
                RVAdapter.refreshData(events, types);
                checkValid();
            }

            @Override
            public void onFailure(Object responseObj) {

            }
        }, params, UpdateEventActivity.this);
    }

    //删除
    private void postDeleteData(){
        RequestParams params = new RequestParams();
        params.put("name", username);
        params.put("pk", String.valueOf(eventShowed.getPk()));
        RequestCenter.deleteEvent(new DisposeDataListener() {
            @Override
            public void onSuccess(Object responseObj) {
                DefaultResponse response = (DefaultResponse) responseObj;
                Toast.makeText(UpdateEventActivity.this, response.getMsg(), Toast.LENGTH_SHORT).show();
                for(int i=0; i<events.size(); i++){
                    if(events.get(i).getPk() == eventShowed.getPk()) {
                        events.remove(i);
                        RVAdapter.refreshData(events, types);
                        checkValid();
                        break;
                    }
                }
            }
            @Override
            public void onFailure(Object responseObj) {

            }
        }, params, UpdateEventActivity.this);
    }


    //重置
    private void reset(){
        for(int i=0; i<types.size(); i++){
            if(eventShowed.getFields().getEventType() == types.get(i).getPk()){
                type_selector.setSelection(i);
                typeName = types.get(i).getFields().getName();
            }
        }
        switch(eventShowed.getFields().getUserLevel()){
            case 0:
                emergency.check(R.id.emergency_0);
                userLevel = 0;
                break;
            case 1:
                emergency.check(R.id.emergency_1);
                userLevel = 1;
                break;
            case 2:
                emergency.check(R.id.emergency_2);
                userLevel = 2;
                break;
            case 3:
                emergency.check(R.id.emergency_3);
                userLevel = 3;
                break;
            default:
                emergency.check(R.id.emergency_0);
                userLevel = 0;
                break;
        }
        ET_title.setText(eventShowed.getFields().getTitle());
        ET_description.setText(eventShowed.getFields().getDescription());
        ET_length.setText(String.valueOf(eventShowed.getFields().getLength()));
        ET_start.setText(TimeZoneChanger.DateLocalTOStringLocalEN(eventShowed.getFields().getUserStartTime()));
        ET_end.setText(TimeZoneChanger.DateLocalTOStringLocalEN(eventShowed.getFields().getUserEndTime()));
    }

    //日期时间选择器
    private void DateTimePicker(final EditText editText){
        final Calendar calendar = Calendar.getInstance();
        DatePickerDialog datePickerDialog = new DatePickerDialog(UpdateEventActivity.this, new DatePickerDialog.OnDateSetListener() {

            @Override
            public void onDateSet(DatePicker view, final int year, final int month, final int dayOfMonth) {

                calendar.set(year, month, dayOfMonth);
                TimePickerDialog timePickerDialog = new TimePickerDialog(UpdateEventActivity.this, new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                        calendar.set(year, month, dayOfMonth, hourOfDay, minute);
                        String time = (String) DateFormat.format("yyyy-MM-dd HH:mm", calendar);
                        editText.setText(time);
                        editText.setSelection(time.length());
                    }
                }, calendar.get(Calendar.HOUR_OF_DAY), calendar.get(Calendar.MINUTE), true);
                timePickerDialog.show();
            }
        }, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH));
        datePickerDialog.show();
    }

    private void checkValid(){
        //state 0:等待被安排 1:已安排 2:已取消 3:已完成
        if(eventShowed.getFields().getState()==3){
            ChangeColor(true, false);
        }else if(eventShowed.getFields().getState()==0 || eventShowed.getFields().getState()==1){
            isCancelled = false;
            BTN_cancelOrReactive.setText(getResources().getText(R.string.cancel_));
            ChangeColor(true, true);
        }else if(eventShowed.getFields().getState()==2){
            isCancelled = true;
            BTN_cancelOrReactive.setText(getResources().getText(R.string.reactive_));
            ChangeColor( true, true);
        }
    }

    private void ChangeColor(Boolean canReset, Boolean canCancel){
        if(canReset){
            BTN_reset.setClickable(true);
            BTN_reset.setBackgroundColor(getResources().getColor(R.color.colorPinkMain));
        }else{
            BTN_reset.setClickable(false);
            BTN_reset.setBackgroundColor(getResources().getColor(R.color.light_gray));
        }
        if(canCancel){
            BTN_cancelOrReactive.setClickable(true);
            BTN_cancelOrReactive.setBackgroundColor(getResources().getColor(R.color.colorPinkMain));
        }else{
            BTN_cancelOrReactive.setClickable(false);
            BTN_cancelOrReactive.setBackgroundColor(getResources().getColor(R.color.light_gray));
        }
    }
}
