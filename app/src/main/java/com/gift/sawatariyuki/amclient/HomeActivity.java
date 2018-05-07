package com.gift.sawatariyuki.amclient;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Handler;
import android.support.constraint.ConstraintLayout;
import android.support.v4.view.animation.FastOutLinearInInterpolator;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.OrientationHelper;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.ViewAnimationUtils;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.gift.sawatariyuki.amclient.Adapter.RecyclerViewAdapterForEvent;
import com.gift.sawatariyuki.amclient.Bean.DefaultResponse;
import com.gift.sawatariyuki.amclient.Bean.Event;
import com.gift.sawatariyuki.amclient.Bean.GetEventResponse;
import com.gift.sawatariyuki.amclient.Bean.GetTypeResponse;
import com.gift.sawatariyuki.amclient.Bean.LoginResponse;
import com.gift.sawatariyuki.amclient.Bean.Type;
import com.gift.sawatariyuki.amclient.Listener.OnItemClickListener;
import com.gift.sawatariyuki.amclient.Listener.OnItemLongClickListener;
import com.gift.sawatariyuki.amclient.ServerNetwork.RequestCenter;
import com.gift.sawatariyuki.amclient.Utils.dataRecoder.DataRecorder;
import com.gift.sawatariyuki.amclient.Utils.okHttp.listener.DisposeDataListener;
import com.gift.sawatariyuki.amclient.Utils.okHttp.request.RequestParams;

import java.io.Serializable;
import java.util.List;

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
    private RecyclerView activity_home_RV_event;
    private TextView activity_home_TV_noEventData;
    private Spinner activity_home_state_selector;
    private ConstraintLayout activity_home_CL_addEvent;

    private DataRecorder recorder;

    private final int REQUESTCODE = 101;

    //全局变量
    private String username = null;
    private List<Event> events = null;
    private List<Type> types = null;
    String stateStr[] = {"4", "5", "0", "1", "2", "3"};
    int statePosition[] = {2, 3, 4, 5, 0, 1};
    private String selectedState = null;
    Boolean isLogin;

    private RecyclerViewAdapterForEvent adapter;

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

        // main
        activity_home_RV_event = findViewById(R.id.activity_home_RV_event);
        final LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        activity_home_RV_event.setLayoutManager(layoutManager);
        layoutManager.setOrientation(OrientationHelper.VERTICAL);
        activity_home_TV_noEventData = findViewById(R.id.activity_home_TV_noEventData);
        activity_home_state_selector = findViewById(R.id.activity_home_state_selector);
        int position = Integer.valueOf((String) recorder.get("selectedEventState", "4"));
        activity_home_state_selector.setSelection(statePosition[position]);
        activity_home_CL_addEvent = findViewById(R.id.activity_home_CL_addEvent);

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
                if(time>2000){
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
                Toast.makeText(HomeActivity.this, "You must activate your account first", Toast.LENGTH_SHORT).show();
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

        //添加事件 +Add
        activity_home_CL_addEvent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(types==null){
                    //TODO 提示用户要先添加事务类型
                    Toast.makeText(HomeActivity.this, "提示用户要先添加事务类型", Toast.LENGTH_SHORT).show();
                }else {
                    Intent intent = new Intent(HomeActivity.this, AddEventActivity.class);
                    intent.putExtra("name", username);
                    intent.putExtra("types", (Serializable) types);
                    startActivityForResult(intent, REQUESTCODE);

                    //TODO
                }

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
            }else{  //用户已激活
                left_drawer_TV_inactivate.setVisibility(View.INVISIBLE);
                //获取用户事务信息并显示在RecyclerView中
                getEventData();
            }

            setVisibilityInLeftDrawer(true);
            left_drawer_TV_username.setText(username);
            left_drawer_TV_email.setText((String)recorder.get("email",""));
        }
    }



    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==REQUESTCODE && resultCode==201){    // 在主界面点击登录，在登录后返回主界面
            LoginResponse response = (LoginResponse) data.getSerializableExtra("response");
            username = response.getData().getFields().getName();
            left_drawer_TV_username.setText(username);
            left_drawer_TV_email.setText(response.getData().getFields().getEmail());
            if(response.getData().getFields().getActivated()){
                left_drawer_TV_inactivate.setVisibility(View.INVISIBLE);
                //获取用户事务信息并显示在RecyclerView中
                getEventData();
            }else{
                left_drawer_TV_inactivate.setVisibility(View.VISIBLE);
            }
            setVisibilityInLeftDrawer(true);
        }else if(requestCode==REQUESTCODE && resultCode==202){  // 在主界面点击注册，在注册后返回主界面
            username = data.getStringExtra("name");
            String email = data.getStringExtra("email");
            left_drawer_TV_username.setText(username);
            left_drawer_TV_email.setText(email);
            left_drawer_TV_inactivate.setVisibility(View.VISIBLE);
            setVisibilityInLeftDrawer(true);

            //no event data
            setVisibilityInHomeActivity(false, false);
        }else if(requestCode==REQUESTCODE && resultCode==301){
            activity_home_state_selector.setSelection(2);
            getEventData();
        }

    }


    private void getEventData(){
        final OnItemClickListener onItemClickListener = new OnItemClickListener() {
            @Override
            public void onItemClick(Object itemValue, int position) {
                Toast.makeText(HomeActivity.this, "you click the "+position+"th item", Toast.LENGTH_SHORT).show();
            }
        };
        final OnItemLongClickListener onItemLongClickListener = new OnItemLongClickListener() {
            @Override
            public void onItemLongClick(Object itemValue, int position) {
                Event event = (Event) itemValue;
                Toast.makeText(HomeActivity.this, event.toString(), Toast.LENGTH_SHORT).show();
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
                        adapter = new RecyclerViewAdapterForEvent(events, types, HomeActivity.this);
                        activity_home_RV_event.setAdapter(adapter);
                        adapter.setOnItemClickListener(onItemClickListener);
                        adapter.setOnItemLongClickListener(onItemLongClickListener);
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
                        adapter = new RecyclerViewAdapterForEvent(events, types, HomeActivity.this);
                        activity_home_RV_event.setAdapter(adapter);
                        adapter.setOnItemClickListener(onItemClickListener);
                        adapter.setOnItemLongClickListener(onItemLongClickListener);
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
