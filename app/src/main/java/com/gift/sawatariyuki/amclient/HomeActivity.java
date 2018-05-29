package com.gift.sawatariyuki.amclient;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.SuppressLint;
import android.app.ActivityOptions;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.opengl.Visibility;
import android.os.Build;
import android.os.Handler;
import android.os.VibrationEffect;
import android.os.Vibrator;
import android.support.annotation.RequiresApi;
import android.support.constraint.ConstraintLayout;
import android.support.v4.view.animation.FastOutLinearInInterpolator;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.app.AppCompatDelegate;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.OrientationHelper;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.util.DisplayMetrics;
import android.util.Log;
import android.util.Pair;
import android.view.Gravity;
import android.view.View;
import android.view.ViewAnimationUtils;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.gift.sawatariyuki.amclient.Adapter.RecyclerViewAdapterForEvent;
import com.gift.sawatariyuki.amclient.Adapter.SimpleItemTouchHelperCallback;
import com.gift.sawatariyuki.amclient.Bean.DefaultResponse;
import com.gift.sawatariyuki.amclient.Bean.Event;
import com.gift.sawatariyuki.amclient.Bean.GetEventResponse;
import com.gift.sawatariyuki.amclient.Bean.GetTypeResponse;
import com.gift.sawatariyuki.amclient.Bean.LoginResponse;
import com.gift.sawatariyuki.amclient.Bean.Type;
import com.gift.sawatariyuki.amclient.Bean.UserDefault;
import com.gift.sawatariyuki.amclient.Bean.UserInfoResponse;
import com.gift.sawatariyuki.amclient.Listener.OnItemClickListener;
import com.gift.sawatariyuki.amclient.Listener.OnItemLongClickListener;
import com.gift.sawatariyuki.amclient.ServerNetwork.RequestCenter;
import com.gift.sawatariyuki.amclient.Utils.TimeZoneChanger;
import com.gift.sawatariyuki.amclient.Utils.dataRecoder.DataRecorder;
import com.gift.sawatariyuki.amclient.Utils.okHttp.listener.DisposeDataListener;
import com.gift.sawatariyuki.amclient.Utils.okHttp.request.RequestParams;
import com.gift.sawatariyuki.amclient.ViewHolder.ViewHolderForEvent;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.melnykov.fab.FloatingActionButton;

import java.io.Serializable;
import java.sql.Time;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class HomeActivity extends AppCompatActivity {
    private Button left_drawer_BTN_login;
    private Button left_drawer_BTN_register;
    private DrawerLayout activity_home_drawerLayout;
    private TextView left_drawer_TV_username;
    private TextView left_drawer_TV_email;
    private TextView left_drawer_TV_inactivate;
    private ImageView left_drawer_IV_transition;
    private ImageView left_drawer_IV_exit;
    private ConstraintLayout left_drawer_CL;
    private TextView left_drawer_join_;
    private TextView left_drawer_TV_joined;
    private TextView left_drawer_lase_;
    private TextView left_drawer_TV_last_seen;
    private ConstraintLayout left_drawer_CL_userInfo;
    private ImageView left_drawer_IV_edit;
    private ImageView left_drawer_IV_setting;
    private ImageView left_drawer_IV_nightMode;

    private RecyclerView activity_home_RV_event;
    private LinearLayoutManager layoutManager;
    private TextView activity_home_TV_noEventData;
    private Spinner activity_home_state_selector;
    private ConstraintLayout activity_home_CL_user;
    private TextView activity_home_TV_arrange;

    private FloatingActionButton activity_home_fab;

    private DataRecorder recorder;

    private final int REQUESTCODE = 101;

    //全局变量
    private String username = null;
    private List<Event> events = null;
    private List<Type> types = null;
    private String stateStr[] = {"4", "5", "0", "1", "2", "3"};
    private int statePosition[] = {2, 3, 4, 5, 0, 1};
    private String selectedState = null;
    private Boolean isLogin;

    private static String EVENT_ALARM_ACTION = "EVENT_ALARM";

    private RecyclerViewAdapterForEvent adapter;
    private ItemTouchHelper.Callback callback;
    private ItemTouchHelper touchHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        recorder = new DataRecorder(this);
        initView();
        initListener();
        initData();

    }

    private void initView() {
        // left drawer
        activity_home_drawerLayout = findViewById(R.id.activity_home_drawerLayout);
        left_drawer_BTN_login = findViewById(R.id.left_drawer_BTN_login);
        left_drawer_BTN_register = findViewById(R.id.left_drawer_BTN_register);
        left_drawer_TV_username = findViewById(R.id.left_drawer_TV_username);
        left_drawer_TV_email = findViewById(R.id.left_drawer_TV_email);
        left_drawer_TV_inactivate = findViewById(R.id.left_drawer_TV_inactivate);
        left_drawer_IV_transition = findViewById(R.id.left_drawer_IV_transition);
        left_drawer_IV_exit = findViewById(R.id.left_drawer_IV_exit);
        left_drawer_CL = findViewById(R.id.left_drawer_CL);
        left_drawer_join_ = findViewById(R.id.left_drawer_join_);
        left_drawer_TV_joined = findViewById(R.id.left_drawer_TV_joined);
        left_drawer_lase_ = findViewById(R.id.left_drawer_lase_);
        left_drawer_TV_last_seen = findViewById(R.id.left_drawer_TV_last_seen);
        left_drawer_CL_userInfo = findViewById(R.id.left_drawer_CL_userInfo);
        left_drawer_IV_edit = findViewById(R.id.left_drawer_IV_edit);
        left_drawer_IV_setting = findViewById(R.id.left_drawer_IV_setting);
        left_drawer_IV_nightMode = findViewById(R.id.left_drawer_IV_nightMode);

        // main
        activity_home_RV_event = findViewById(R.id.activity_home_RV_event);
        layoutManager = new LinearLayoutManager(this);
        activity_home_RV_event.setLayoutManager(layoutManager);
        layoutManager.setOrientation(OrientationHelper.VERTICAL);
        activity_home_TV_noEventData = findViewById(R.id.activity_home_TV_noEventData);
        activity_home_state_selector = findViewById(R.id.activity_home_state_selector);
        int position = Integer.valueOf((String) recorder.get("selectedEventState", "4"));
        activity_home_state_selector.setSelection(statePosition[position]);
        activity_home_CL_user = findViewById(R.id.activity_home_CL_user);
        activity_home_TV_arrange = findViewById(R.id.activity_home_TV_arrange);

        activity_home_fab = findViewById(R.id.activity_home_fab);
        activity_home_fab.attachToRecyclerView(activity_home_RV_event);

        String mode = (String) recorder.get("colorMode", "common_day");
        switch (mode){
            case "common_day":
                getDelegate().setLocalNightMode(AppCompatDelegate.MODE_NIGHT_NO);
                break;
            case "common_night":
                getDelegate().setLocalNightMode(AppCompatDelegate.MODE_NIGHT_YES);
                break;
        }
    }

    private void initListener(){
        //登录
        left_drawer_BTN_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int X = (int) (left_drawer_BTN_login.getX()+left_drawer_BTN_login.getWidth()/2);
                int Y = (int) (left_drawer_BTN_login.getY()+left_drawer_BTN_login.getHeight()/2);
                roundLoad(X, Y, LoginActivity.class);
            }
        });

        //注册
        left_drawer_BTN_register.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("RestrictedApi")
            @Override
            public void onClick(View view) {
                int X = (int) (left_drawer_BTN_register.getX()+left_drawer_BTN_register.getWidth()/2);
                int Y = (int) (left_drawer_BTN_register.getY()+left_drawer_BTN_register.getHeight()/2);
                roundLoad(X, Y, RegisterActivity.class);
            }
        });

        // 登出
        left_drawer_IV_exit.setOnClickListener(new View.OnClickListener() {
            long preTime;
            @Override
            public void onClick(View view) {
                long time = System.currentTimeMillis() - preTime;
                if (time > 2000) {
                    Toast.makeText(HomeActivity.this, "Press again to logout", Toast.LENGTH_SHORT).show();
                    preTime = System.currentTimeMillis();
                    return;
                }
                username = null;
                recorder.remove("loggedUsername");
                recorder.remove("email");
                recorder.remove("isActivated");

                setVisibilityInLeftDrawer(false);

                //no event data
                setVisibilityInHomeActivity(false, false);
            }
        });

        //激活提示
        left_drawer_TV_inactivate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(HomeActivity.this, "You must activate your account first\nCheck your email box for activating", Toast.LENGTH_SHORT).show();
            }
        });

        //日夜模式切换
        left_drawer_IV_nightMode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int mode = getResources().getConfiguration().uiMode & Configuration.UI_MODE_NIGHT_MASK;
                if (mode == Configuration.UI_MODE_NIGHT_YES) {
                    getDelegate().setLocalNightMode(AppCompatDelegate.MODE_NIGHT_NO);
                    recorder.save("colorMode", "common_day");
                } else if (mode == Configuration.UI_MODE_NIGHT_NO) {
                    getDelegate().setLocalNightMode(AppCompatDelegate.MODE_NIGHT_YES);
                    recorder.save("colorMode", "common_night");
                }
                recreate();
            }
        });

        //用户应用设置
        left_drawer_IV_setting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(HomeActivity.this, SettingsActivity.class);
                startActivity(intent);
            }
        });

        //修改用户信息
        left_drawer_IV_edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                List<Pair<View,String>> pairs = new ArrayList<Pair<View, String>>();
                pairs.add(Pair.create((View) left_drawer_TV_username, "left_drawer_username"));
                pairs.add(Pair.create((View) left_drawer_TV_joined, "left_drawer_joined"));
                pairs.add(Pair.create((View) left_drawer_TV_last_seen, "left_drawer_last"));
                pairs.add(Pair.create((View) left_drawer_TV_email, "left_drawer_email"));
                pairs.add(Pair.create((View) left_drawer_join_, "left_drawer_join_"));
                pairs.add(Pair.create((View) left_drawer_lase_, "left_drawer_lase_"));

                Bundle bundle = ActivityOptions.makeSceneTransitionAnimation(HomeActivity.this, pairs.toArray(new Pair[]{})).toBundle();
                Intent intent = new Intent(HomeActivity.this, UserInfoActivity.class);
                intent.putExtra("name", username);
                startActivity(intent, bundle);
            }
        });

        //事务状态选择器
        activity_home_state_selector.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                selectedState = stateStr[position];
                if(isLogin) {
                    recorder.save("selectedEventState", selectedState);
                    getEventData();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        //打开侧边栏
        activity_home_CL_user.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                activity_home_drawerLayout.openDrawer(Gravity.LEFT);
            }
        });

        //添加事件 +Add
        activity_home_fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!isLogin) {    //是否已登录
                    Toast.makeText(HomeActivity.this, "Login first", Toast.LENGTH_SHORT).show();
                    return;
                }
                if(left_drawer_TV_inactivate.getVisibility() == View.VISIBLE){
                    Toast.makeText(HomeActivity.this, "You must activate your account first\nCheck your email box for activating", Toast.LENGTH_SHORT).show();
                    return;
                }
                if(types==null){
                    Toast.makeText(HomeActivity.this, "Add your event type first", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(HomeActivity.this, AddTypeActivity.class);
                    intent.putExtra("name", username);
                    startActivityForResult(intent, REQUESTCODE);
                }else {
                    Intent intent = new Intent(HomeActivity.this, AddEventActivity.class);
                    intent.putExtra("name", username);
                    intent.putExtra("types", (Serializable) types);
                    startActivityForResult(intent, REQUESTCODE);
                }
            }
        });

        //自动安排事务
        activity_home_TV_arrange.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                RequestParams params = new RequestParams();
                params.put("name", username);
                RequestCenter.arrange(new DisposeDataListener() {
                    @Override
                    public void onSuccess(Object responseObj) {
                        DefaultResponse response = (DefaultResponse) responseObj;
                        Toast.makeText(HomeActivity.this, response.getMsg(), Toast.LENGTH_SHORT).show();
                        activity_home_state_selector.setSelection(3);   //切换STATE为Arranged
                        getEventData();
                    }

                    @Override
                    public void onFailure(Object responseObj) {

                    }
                }, params, HomeActivity.this);
            }
        });
    }

    private void initData(){
        username = (String) recorder.get("loggedUsername","");
        //0:等待被安排 1:已安排 2:已取消 3:已完成 4:未完成 5:所有
        selectedState = (String) recorder.get("selectedEventState", "4");
        if(username.equals("")){
            setVisibilityInLeftDrawer(false);
            setVisibilityInHomeActivity(false, false);
        }else{
            if(!(Boolean) recorder.get("isActivated", false)){
                left_drawer_TV_inactivate.setVisibility(View.VISIBLE);
                left_drawer_CL_userInfo.setVisibility(View.INVISIBLE);
            }else{  //用户已激活
                left_drawer_TV_inactivate.setVisibility(View.INVISIBLE);
                left_drawer_CL_userInfo.setVisibility(View.VISIBLE);
                //获取用户事务信息并显示在RecyclerView中
                getEventData();
                //获取用户基本信息显示在left drawer中
                userLogin(username);
            }
            setVisibilityInLeftDrawer(true);
            left_drawer_TV_username.setText(username);
            left_drawer_TV_email.setText((String)recorder.get("email",""));
        }
    }



    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==REQUESTCODE && resultCode==201){    //在主界面点击登录，在登录后返回主界面
            LoginResponse response = (LoginResponse) data.getSerializableExtra("response");
            username = response.getData().getFields().getName();
            left_drawer_TV_username.setText(username);
            left_drawer_TV_email.setText(response.getData().getFields().getEmail());

            if(response.getData().getFields().getActivated()){
                left_drawer_TV_inactivate.setVisibility(View.INVISIBLE);
                left_drawer_CL_userInfo.setVisibility(View.VISIBLE);
                //获取用户事务信息并显示在RecyclerView中
                getEventData();
                //获取用户基本信息显示在left drawer中
                userLogin(username);
            }else{
                left_drawer_TV_inactivate.setVisibility(View.VISIBLE);
                left_drawer_CL_userInfo.setVisibility(View.INVISIBLE);
            }
            setVisibilityInLeftDrawer(true);
        }else if(requestCode==REQUESTCODE && resultCode==202){  //在主界面点击注册，在注册后返回主界面
            username = data.getStringExtra("name");
            String email = data.getStringExtra("email");
            String pw = data.getStringExtra("pw");
            recorder.save("isRemember", false);
            left_drawer_TV_username.setText(username);
            left_drawer_TV_email.setText(email);
            left_drawer_TV_inactivate.setVisibility(View.VISIBLE);
            left_drawer_CL_userInfo.setVisibility(View.INVISIBLE);
            setVisibilityInLeftDrawer(true);
            //no event data
            setVisibilityInHomeActivity(false, false);
        }else if(requestCode==REQUESTCODE && resultCode==301){  //在主界面点击添加事务，添加后返回主界面
            if(data.getBooleanExtra("new", false)) {
                activity_home_state_selector.setSelection(2);   //切换STATE为Arranging
            }
            getEventData();
        }else if(requestCode==REQUESTCODE && resultCode==401){  //取消事务后返回
            getEventData();
        }else if(requestCode==REQUESTCODE && resultCode==501){  //事务类型界面返回
            getEventData();
        }
    }

    private void userLogin(String username){
        //SEND POST REQUEST
        RequestParams params = new RequestParams();
        params.put("name", username);
        RequestCenter.getUserInfo(new DisposeDataListener() {
            @Override
            public void onSuccess(Object responseObj) {
                if(responseObj instanceof UserInfoResponse){
                    UserInfoResponse response = (UserInfoResponse) responseObj;
                    UserDefault user = response.getData().getU_default();
                    left_drawer_TV_joined.setText(TimeZoneChanger.DateLocalTOStringLocalCN(user.getFields().getDate_joined()));
                    left_drawer_TV_last_seen.setText(TimeZoneChanger.DateLocalTOStringLocalCN(user.getFields().getLast_joined()));
                }else if(responseObj instanceof DefaultResponse){
                    Toast.makeText(HomeActivity.this, ((DefaultResponse) responseObj).getMsg(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Object responseObj) {

            }
        }, params, HomeActivity.this);
    }


    private void setAlarm(Event event, int requestCode) {
        Intent intent = new Intent(EVENT_ALARM_ACTION);
        intent.putExtra("event_title", event.getFields().getTitle());
        PendingIntent pendingIntent = PendingIntent.getBroadcast(HomeActivity.this, requestCode, intent, 0);
        AlarmManager alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);
        alarmManager.set(AlarmManager.RTC_WAKEUP, event.getFields().getSysStartTime().getTime(), pendingIntent);
        Toast.makeText(HomeActivity.this, "Notification applied", Toast.LENGTH_SHORT).show();
    }

    private void cancelAlarm(Event event, int requestCode) {
        Intent intent = new Intent(EVENT_ALARM_ACTION);
        intent.putExtra("event_title", event.getFields().getTitle());
        PendingIntent pendingIntent = PendingIntent.getBroadcast(HomeActivity.this, requestCode, intent, 0);
        AlarmManager alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);
        alarmManager.cancel(pendingIntent);
        Toast.makeText(HomeActivity.this, "Notification cancelled", Toast.LENGTH_SHORT).show();
    }

    private void getEventData() {
        activity_home_fab.show();
        final OnItemClickListener onItemClickListener = new OnItemClickListener() {
            long preTime = System.currentTimeMillis();

            @Override
            public void onItemClick(Object itemValue, int position) {
//                recorder.remove("vibrate");   //TODO reset the sharedPreference
                if (events.get(position).getFields().getState()!=1) {   //非已安排的事务 无法设置震动提醒
                    return;
                }

                ViewHolderForEvent holder = (ViewHolderForEvent) activity_home_RV_event.findViewHolderForAdapterPosition(position);
                long time = System.currentTimeMillis() - preTime;
                if (time > 2000) {
                    if (holder.getRv_event_item_IV_vibrate().getVisibility() == View.INVISIBLE) {
                        Toast.makeText(HomeActivity.this, "Press again to add a notification", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(HomeActivity.this, "Press again to remove a notification", Toast.LENGTH_SHORT).show();
                    }
                    preTime = System.currentTimeMillis();
                    return;
                }

                Set<String> vibrateEvent;
                Gson gson = new Gson();
                String strJson = (String) recorder.get("vibrate", null);
                if (null == strJson) {
                    vibrateEvent = new HashSet<>();
                } else {
                    vibrateEvent = gson.fromJson(strJson, new TypeToken<Set<String>>(){}.getType());
                }

                if (holder.getRv_event_item_IV_vibrate().getVisibility() == View.INVISIBLE) {
                    //add a notification
                    holder.getRv_event_item_IV_vibrate().setVisibility(View.VISIBLE);
                    vibrateEvent.add(String.valueOf(events.get(position).getPk()));
                    recorder.save("vibrate", gson.toJson(vibrateEvent));
                    //设置提醒闹钟
                    setAlarm(events.get(position), events.get(position).getPk());
                    preTime = 0;
                } else {
                    //remove a notification
                    holder.getRv_event_item_IV_vibrate().setVisibility(View.INVISIBLE);
                    vibrateEvent.remove(String.valueOf(events.get(position).getPk()));
                    recorder.save("vibrate", gson.toJson(vibrateEvent));
                    //取消提醒闹钟
                    cancelAlarm(events.get(position), events.get(position).getPk());
                    preTime = 0;
                }
            }
        };

        final OnItemLongClickListener onItemLongClickListener = new OnItemLongClickListener() {
            @SuppressLint("RestrictedApi")
            @Override
            public void onItemLongClick(Object itemValue, int position) {
                List<Pair<View, String>> pairs = new ArrayList<Pair<View, String>>();
                ViewHolderForEvent holder = (ViewHolderForEvent) activity_home_RV_event.findViewHolderForAdapterPosition(position);
                View view = holder.getRv_event_item_CL();
                pairs.add(Pair.create(view, "select_CL"));

                view = holder.getRv_event_item_TV_title();
                pairs.add(Pair.create(view, "selected_title"));
                view = holder.getRv_event_item_TV_description();
                pairs.add(Pair.create(view, "selected_description"));
                view = holder.getRv_event_item_TV_startTime();
                pairs.add(Pair.create(view, "selected_start"));
                view = holder.getRv_event_item_TV_endTime();
                pairs.add(Pair.create(view, "selected_end"));
                view = holder.getRv_event_item_TV_type();
                pairs.add(Pair.create(view, "selected_type"));
                view = holder.getRv_event_item_TV_length();
                pairs.add(Pair.create(view, "selected_length"));

                Bundle bundle = ActivityOptions.makeSceneTransitionAnimation(HomeActivity.this, pairs.toArray(new Pair[]{})).toBundle();
                Intent intent = new Intent(HomeActivity.this, UpdateEventActivity.class);
                intent.putExtra("name", username);
                intent.putExtra("types", (Serializable) types);
                intent.putExtra("events", (Serializable) events);
                intent.putExtra("position", position);
                startActivityForResult(intent, REQUESTCODE, bundle);
            }
        };

        //SEND GET REQUEST
        //获取用户事务
        RequestParams params = new RequestParams();
        params.put("name", username);
        params.put("state", selectedState);
        RequestCenter.getEvent(new DisposeDataListener() {
            @Override
            public void onSuccess(Object responseObj) {
                if(responseObj instanceof GetEventResponse){
                    GetEventResponse response = (GetEventResponse) responseObj;
                    events = response.getData();
                    if(types != null){
                        setVisibilityInHomeActivity(true, true);
                        Set<String> vibrateEvent;
                        Gson gson = new Gson();
                        String strJson = (String) recorder.get("vibrate", null);
                        if (null == strJson) {
                            vibrateEvent = new HashSet<>();
                        } else {
                            vibrateEvent = gson.fromJson(strJson, new TypeToken<Set<String>>(){}.getType());
                        }
                        if(adapter==null){
                            adapter = new RecyclerViewAdapterForEvent(events, types, username, vibrateEvent, HomeActivity.this);
                            activity_home_RV_event.setAdapter(adapter);
                            adapter.setOnItemClickListener(onItemClickListener);
                            adapter.setOnItemLongClickListener(onItemLongClickListener);
                        }else{
                            adapter.updateData(events, types, username, vibrateEvent);
                            adapter.notifyDataSetChanged();
                        }
                        if(callback==null){
                            //先实例化Callback
                            callback = new SimpleItemTouchHelperCallback(adapter);
                            //用Callback构造ItemTouchHelper
                            touchHelper = new ItemTouchHelper(callback);
                            //调用ItemTouchHelper的attachToRecyclerView方法建立联系
                            touchHelper.attachToRecyclerView(activity_home_RV_event);
                        }
                    }

                    //每次更新显示用户事务信息后 若存在未安排的事务 则显示安排按钮
                    activity_home_TV_arrange.setVisibility(View.INVISIBLE);
                    for (int i=0; i<events.size(); i++) {
                        if (events.get(i).getFields().getState() == 0) {
                            activity_home_TV_arrange.setVisibility(View.VISIBLE);
                            break;
                        }
                    }
                }else if(responseObj instanceof DefaultResponse){
                    //no event data
                    if(types != null){
                        setVisibilityInHomeActivity(false, true);
                    }else{
                        setVisibilityInHomeActivity(false, false);
                    }
                }
            }

            @Override
            public void onFailure(Object responseObj) {

            }
        }, params, HomeActivity.this);

        //获取用户事务类型
        RequestCenter.getType(new DisposeDataListener() {
            @Override
            public void onSuccess(Object responseObj) {
                if(responseObj instanceof GetTypeResponse){
                    GetTypeResponse response = (GetTypeResponse) responseObj;
                    types = response.getData();
                    if(events != null){
                        setVisibilityInHomeActivity(true, true);
                        Set<String> vibrateEvent;
                        Gson gson = new Gson();
                        String strJson = (String) recorder.get("vibrate", null);
                        if (null == strJson) {
                            vibrateEvent = new HashSet<>();
                        } else {
                            vibrateEvent = gson.fromJson(strJson, new TypeToken<Set<String>>() {
                            }.getType());
                        }
                        if(adapter==null){
                            adapter = new RecyclerViewAdapterForEvent(events, types, username, vibrateEvent, HomeActivity.this);
                            activity_home_RV_event.setAdapter(adapter);
                            adapter.setOnItemClickListener(onItemClickListener);
                            adapter.setOnItemLongClickListener(onItemLongClickListener);
                        }else{
                            adapter.updateData(events, types, username, vibrateEvent);
                            adapter.notifyDataSetChanged();
                        }
                        if(callback==null){
                            //先实例化Callback
                            callback = new SimpleItemTouchHelperCallback(adapter);
                            //用Callback构造ItemTouchHelper
                            touchHelper = new ItemTouchHelper(callback);
                            //调用ItemTouchHelper的attachToRecyclerView方法建立联系
                            touchHelper.attachToRecyclerView(activity_home_RV_event);
                        }
                    }
                }else if(responseObj instanceof DefaultResponse){
                    //no type data
                    setVisibilityInHomeActivity(false, false);
                }
            }

            @Override
            public void onFailure(Object responseObj) {

            }
        }, params, HomeActivity.this);
    }

    /**
     * 圆形扩散转场动画
     */
    private void  roundLoad(int startX, int startY, final Class<?> cls){
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {
            DisplayMetrics metrics =new DisplayMetrics();
            getWindowManager().getDefaultDisplay().getRealMetrics(metrics);
            Animator circularReveal;
            circularReveal = ViewAnimationUtils.createCircularReveal(left_drawer_IV_transition,
                    startX,
                    startY,
                    0,
                    metrics.heightPixels);
            circularReveal.setDuration(750);
            circularReveal.setInterpolator(new FastOutLinearInInterpolator());
            left_drawer_IV_transition.setVisibility(View.VISIBLE);
            left_drawer_BTN_login.setVisibility(View.INVISIBLE);
            left_drawer_BTN_register.setVisibility(View.INVISIBLE);
            circularReveal.start();

            circularReveal.addListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    super.onAnimationEnd(animation);
                    Intent intent = new Intent(HomeActivity.this, cls);
                    startActivityForResult(intent, REQUESTCODE);
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            left_drawer_IV_transition.setVisibility(View.INVISIBLE);
                            left_drawer_BTN_login.setVisibility(View.VISIBLE);
                            left_drawer_BTN_register.setVisibility(View.VISIBLE);
                        }
                    },500);
                }
            });
        }
    }

    private void setVisibilityInLeftDrawer(Boolean isLogin){
        this.isLogin = isLogin;
        if(isLogin){    //has logged in
            left_drawer_BTN_login.setVisibility(View.GONE);
            left_drawer_BTN_register.setVisibility(View.GONE);
            left_drawer_CL.setVisibility(View.VISIBLE);
        }else{
            left_drawer_BTN_login.setVisibility(View.VISIBLE);
            left_drawer_BTN_register.setVisibility(View.VISIBLE);
            left_drawer_CL.setVisibility(View.INVISIBLE);
        }
    }

    private void setVisibilityInHomeActivity(Boolean hasEventData, Boolean hasTypeData){
        if(hasEventData){
            activity_home_RV_event.setVisibility(View.VISIBLE);
            activity_home_TV_noEventData.setVisibility(View.INVISIBLE);
        }else{
            activity_home_RV_event.setVisibility(View.INVISIBLE);
            activity_home_TV_noEventData.setVisibility(View.VISIBLE);
            events = null;
        }
        if(!hasTypeData){
            types = null;
        }
    }
}
