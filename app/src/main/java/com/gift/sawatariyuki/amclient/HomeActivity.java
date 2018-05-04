package com.gift.sawatariyuki.amclient;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.SuppressLint;
import android.app.ActivityOptions;
import android.content.Intent;
import android.os.Handler;
import android.support.v4.view.animation.FastOutLinearInInterpolator;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.util.Pair;
import android.view.View;
import android.view.ViewAnimationUtils;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.gift.sawatariyuki.amclient.Bean.LoginResponse;
import com.gift.sawatariyuki.amclient.ServerNetwork.RequestCenter;
import com.gift.sawatariyuki.amclient.Utils.dataRecoder.DataRecorder;
import com.gift.sawatariyuki.amclient.Utils.okHttp.listener.DisposeDataListener;
import com.gift.sawatariyuki.amclient.Utils.okHttp.request.RequestParams;
import com.gift.sawatariyuki.amclient.Utils.validation.NetworkValidation;

import java.util.ArrayList;
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
    private DataRecorder recorder;

    private final int REQUESTCODE = 101;

    private String username = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        initView();
        recorder = new DataRecorder(this);
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

        // main

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
                recorder.remove("loggedUsername");
                recorder.remove("email");
                recorder.remove("isActivated");
                setVisibility(false);
            }
        });

        //激活提示
        left_drawer_TV_inactivate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(HomeActivity.this, "You must activate your account first", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void initData(){
        username = (String) recorder.get("loggedUsername","");
        if(username.equals("")){
            setVisibility(false);
        }else{
            if(!(Boolean) recorder.get("isActivated", false)){
                left_drawer_TV_inactivate.setVisibility(View.VISIBLE);
            }else{  //用户已激活
                left_drawer_TV_inactivate.setVisibility(View.INVISIBLE);

                //SEND GET REQUEST
                RequestParams params = new RequestParams();
                params.put("name", username);
                RequestCenter.getEvent(new DisposeDataListener() {
                    @Override
                    public void onSuccess(Object responseObj) {

                        Log.d("DEBUG", responseObj.toString());
                        //TODO
                    }

                    @Override
                    public void onFailure(Object responseObj) {

                    }
                }, params, HomeActivity.this);
            }
            setVisibility(true);
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
            }else{
                left_drawer_TV_inactivate.setVisibility(View.VISIBLE);
            }
            setVisibility(true);

            //TODO

        }else if(requestCode==REQUESTCODE && resultCode==202){  // 在主界面点击注册，在注册后返回主界面
            username = data.getStringExtra("name");
            String email = data.getStringExtra("email");
            left_drawer_TV_username.setText(username);
            left_drawer_TV_email.setText(email);
            left_drawer_TV_inactivate.setVisibility(View.VISIBLE);
            setVisibility(true);
        }
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

    private void setVisibility(Boolean isLogin){
        if(isLogin){    //has logged in
            left_drawer_BTN_login.setVisibility(View.GONE);
            left_drawer_BTN_register.setVisibility(View.GONE);
            left_drawer_TV_username.setVisibility(View.VISIBLE);
            left_drawer_TV_email.setVisibility(View.VISIBLE);
            left_drawer_IV_exit.setVisibility(View.VISIBLE);
        }else{
            left_drawer_BTN_login.setVisibility(View.VISIBLE);
            left_drawer_BTN_register.setVisibility(View.VISIBLE);
            left_drawer_TV_username.setVisibility(View.GONE);
            left_drawer_TV_email.setVisibility(View.GONE);
            left_drawer_IV_exit.setVisibility(View.INVISIBLE);
            left_drawer_TV_inactivate.setVisibility(View.INVISIBLE);
        }
    }
}
