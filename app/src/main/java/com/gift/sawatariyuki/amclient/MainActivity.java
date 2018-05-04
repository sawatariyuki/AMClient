package com.gift.sawatariyuki.amclient;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.gift.sawatariyuki.amclient.Bean.DefaultResponse;
import com.gift.sawatariyuki.amclient.ServerNetwork.RequestCenter;
import com.gift.sawatariyuki.amclient.Utils.okHttp.listener.DisposeDataListener;
import com.gift.sawatariyuki.amclient.Utils.okHttp.request.RequestParams;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class MainActivity extends AppCompatActivity {
    EditText MAIN_ET_NAME;
    EditText MAIN_ET_NAME_VALUE;
    EditText MAIN_ET_ACTIVATECODE;
    EditText MAIN_ET_ACTIVATECODE_VALUE;
    EditText MAIN_ED_URL;

    TextView MAIN_TV_RESLUT;
    Button MAIN_BTN_GET;
    Button MAIN_BTN_POST;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        MAIN_ET_NAME = findViewById(R.id.MAIN_ET_NAME);
        MAIN_ET_NAME_VALUE = findViewById(R.id.MAIN_ET_NAME_VALUE);
        MAIN_ET_ACTIVATECODE = findViewById(R.id.MAIN_ET_ACTIVATECODE);
        MAIN_ET_ACTIVATECODE_VALUE = findViewById(R.id.MAIN_ET_ACTIVATECODE_VALUE);
        MAIN_ED_URL = findViewById(R.id.MAIN_ED_URL);
        MAIN_TV_RESLUT = findViewById(R.id.MAIN_TV_RESLUT);

        MAIN_BTN_GET = findViewById(R.id.MAIN_BTN_GET);
        MAIN_BTN_POST = findViewById(R.id.MAIN_BTN_POST);

        MAIN_BTN_GET.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(MAIN_ED_URL.getText().toString().trim().equals("")){
                    RequestParams params = new RequestParams();
                    params.put(MAIN_ET_NAME.getText().toString(),
                            MAIN_ET_NAME_VALUE.getText().toString());
                    params.put(MAIN_ET_ACTIVATECODE.getText().toString(),
                            MAIN_ET_ACTIVATECODE_VALUE.getText().toString());

                    RequestCenter.activateUser_GET(new DisposeDataListener() {
                        @Override
                        public void onSuccess(Object responseObj) {
                            Log.d("DEBUG", "In MainActivity: ->GET onSuccess "
                                    + responseObj.toString());
                            DefaultResponse defaultResponse = (DefaultResponse) responseObj;
                            MAIN_TV_RESLUT.setText(defaultResponse.toString());
                        }

                        @Override
                        public void onFailure(Object responseObj) {
                            Log.d("DEBUG", "In MainActivity: ->GET onFailure"
                                    + responseObj.toString());
                            MAIN_TV_RESLUT.setText("onFailure");
                        }
                    }, params, MainActivity.this);
                }else{
                    String urlStr = "http://" + MAIN_ED_URL.getText().toString();
                    OkHttpClient okHttpClient = new OkHttpClient();
                    final Request request = new Request.Builder()
                            .url(urlStr).build();
                    Call call = okHttpClient.newCall(request);
                    call.enqueue(new Callback() {
                        @Override
                        public void onFailure(Call call, IOException e) {
                            Log.d("DEBUG", "In MainActivity: ->GET 连接失败");
                        }

                        @Override
                        public void onResponse(Call call, Response response) throws IOException {
                            final String htmlStr = response.body().string();
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    MAIN_TV_RESLUT.setText(htmlStr);
                                }
                            });
                        }
                    });
                }
            }
        });

        MAIN_BTN_POST.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                RequestParams params = new RequestParams();
                params.put(MAIN_ET_NAME.getText().toString(),
                        MAIN_ET_NAME_VALUE.getText().toString());
                params.put(MAIN_ET_ACTIVATECODE.getText().toString(),
                        MAIN_ET_ACTIVATECODE_VALUE.getText().toString());

                RequestCenter.activateUser_POST(new DisposeDataListener() {
                    @Override
                    public void onSuccess(Object responseObj) {
                        Log.d("DEBUG", "In MainActivity: ->POST onSuccess "
                                + responseObj.toString());
                        DefaultResponse defaultResponse = (DefaultResponse) responseObj;
                        MAIN_TV_RESLUT.setText(defaultResponse.toString());
                    }

                    @Override
                    public void onFailure(Object responseObj) {
                        Log.d("DEBUG", "In MainActivity: ->GET onFailure"
                                + responseObj.toString());
                        MAIN_TV_RESLUT.setText("onFailure");
                    }
                }, params, MainActivity.this);
            }
        });

    }
}
