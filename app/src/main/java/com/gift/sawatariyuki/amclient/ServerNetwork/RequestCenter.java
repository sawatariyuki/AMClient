package com.gift.sawatariyuki.amclient.ServerNetwork;

import android.util.Log;

import com.gift.sawatariyuki.amclient.Bean.DefaultResponse;
import com.gift.sawatariyuki.amclient.Bean.LoginResponse;
import com.gift.sawatariyuki.amclient.Bean.UserDefaultList;
import com.gift.sawatariyuki.amclient.Utils.okHttp.CommonOkHttpClient;
import com.gift.sawatariyuki.amclient.Utils.okHttp.listener.DisposeDataHandle;
import com.gift.sawatariyuki.amclient.Utils.okHttp.listener.DisposeDataListener;
import com.gift.sawatariyuki.amclient.Utils.okHttp.request.CommonRequest;
import com.gift.sawatariyuki.amclient.Utils.okHttp.request.RequestParams;


public class RequestCenter {
    //根据参数发送所有的get请求
    private static void getRequest(String url, RequestParams params,
                                   DisposeDataListener listener,
                                   Class<?> clazz){
        CommonOkHttpClient.get(CommonRequest.createGetRequest(url, params),
                new DisposeDataHandle(listener, clazz));
    }

    //根据参数发送所有的post请求
    private static void postRequest(String url, RequestParams params,
                                    DisposeDataListener listener,
                                    Class<?> clazz){
        CommonOkHttpClient.post(CommonRequest.createPostRequest(url, params),
                new DisposeDataHandle(listener, clazz));
    }

    public static void login_POST(DisposeDataListener listener, RequestParams params){
        Log.d("DEBUG", "In RequestCenter: ->login_POST " + ServerApi.login);
        RequestCenter.postRequest(ServerApi.login, params, listener, LoginResponse.class);
    }

    public static void getAllUser(DisposeDataListener listener){
        Log.d("DEBUG", "In RequestCenter: ->getAllUser " + ServerApi.getAll);
        RequestCenter.getRequest(ServerApi.getAll, null, listener, UserDefaultList.class);
    }

    public static void registerUser(DisposeDataListener listener, RequestParams params){
        Log.d("DEBUG", "In RequestCenter: ->registerUser " + ServerApi.register);
        RequestCenter.postRequest(ServerApi.register, params, listener, DefaultResponse.class);
    }

    public static void activateUser_GET(DisposeDataListener listener, RequestParams params){
        Log.d("DEBUG", "In RequestCenter: ->activateUser_GET " + ServerApi.activate_GET);
        RequestCenter.getRequest(ServerApi.activate_GET, params, listener, DefaultResponse.class);
    }

    public static void activateUser_POST(DisposeDataListener listener, RequestParams params){
        Log.d("DEBUG", "In RequestCenter: ->activateUser_POST " + ServerApi.activate_POST);
        RequestCenter.postRequest(ServerApi.activate_POST, params, listener, DefaultResponse.class);
    }
}
