package com.gift.sawatariyuki.amclient.ServerNetwork;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import com.gift.sawatariyuki.amclient.Bean.DefaultResponse;
import com.gift.sawatariyuki.amclient.Bean.GetEventResponse;
import com.gift.sawatariyuki.amclient.Bean.GetTypeResponse;
import com.gift.sawatariyuki.amclient.Bean.LoginResponse;
import com.gift.sawatariyuki.amclient.Bean.UserDefaultList;
import com.gift.sawatariyuki.amclient.LoginActivity;
import com.gift.sawatariyuki.amclient.Utils.okHttp.CommonOkHttpClient;
import com.gift.sawatariyuki.amclient.Utils.okHttp.listener.DisposeDataHandle;
import com.gift.sawatariyuki.amclient.Utils.okHttp.listener.DisposeDataListener;
import com.gift.sawatariyuki.amclient.Utils.okHttp.request.CommonRequest;
import com.gift.sawatariyuki.amclient.Utils.okHttp.request.RequestParams;
import com.gift.sawatariyuki.amclient.Utils.validation.NetworkValidation;


public class RequestCenter {
    //根据参数发送所有的get请求
    private static void getRequest(String url, RequestParams params,
                                   Context context,
                                   DisposeDataListener listener,
                                   Class<?> clazz,
                                   Class<?> defaultClazz){
        if(!NetworkValidation.isNetworkAvailable(context)){
            Toast.makeText(context, "No network available", Toast.LENGTH_SHORT).show();
            return;
        }
        CommonOkHttpClient.get(CommonRequest.createGetRequest(url, params),
                new DisposeDataHandle(listener, clazz, defaultClazz));
    }

    //根据参数发送所有的post请求
    private static void postRequest(String url, RequestParams params,
                                    Context context,
                                    DisposeDataListener listener,
                                    Class<?> clazz,
                                    Class<?> defaultClazz){
        if(!NetworkValidation.isNetworkAvailable(context)){
            Toast.makeText(context, "No network available", Toast.LENGTH_SHORT).show();
            return;
        }
        CommonOkHttpClient.post(CommonRequest.createPostRequest(url, params),
                new DisposeDataHandle(listener, clazz, defaultClazz));
    }

    public static void login_POST(DisposeDataListener listener, RequestParams params, Context context){
        RequestCenter.postRequest(ServerApi.login, params, context, listener, LoginResponse.class, DefaultResponse.class);
    }

    public static void registerUser(DisposeDataListener listener, RequestParams params, Context context){
        RequestCenter.postRequest(ServerApi.register, params, context, listener, DefaultResponse.class, DefaultResponse.class);
    }

    public static void getEvent(DisposeDataListener listener, RequestParams params, Context context){
        RequestCenter.getRequest(ServerApi.getEvent, params, context, listener, GetEventResponse.class, DefaultResponse.class);
    }

    public static void getType(DisposeDataListener listener, RequestParams params, Context context){
        RequestCenter.getRequest(ServerApi.getEventType, params, context, listener, GetTypeResponse.class, DefaultResponse.class);
    }

    public static void addEvent(DisposeDataListener listener, RequestParams params, Context context){
        RequestCenter.postRequest(ServerApi.addEvent, params, context, listener, DefaultResponse.class, DefaultResponse.class);
    }

    public static void deleteEvent(DisposeDataListener listener, RequestParams params, Context context) {
        RequestCenter.postRequest(ServerApi.deleteEvent, params, context, listener, DefaultResponse.class, DefaultResponse.class);
    }

    public static void cancelOrReactiveEvent(DisposeDataListener listener, RequestParams params, Context context) {
        RequestCenter.postRequest(ServerApi.cancelEvent, params, context, listener, DefaultResponse.class, DefaultResponse.class);
    }
    //---------------------------------------------------------------------------------------------

    public static void activateUser_GET(DisposeDataListener listener, RequestParams params, Context context){
//        Log.d("DEBUG", "In RequestCenter: ->activateUser_GET " + ServerApi.activate_GET);
        RequestCenter.getRequest(ServerApi.activate_GET, params, context, listener, DefaultResponse.class, DefaultResponse.class);
    }

    public static void activateUser_POST(DisposeDataListener listener, RequestParams params, Context context){
//        Log.d("DEBUG", "In RequestCenter: ->activateUser_POST " + ServerApi.activate_POST);
        RequestCenter.postRequest(ServerApi.activate_POST, params, context, listener, DefaultResponse.class, DefaultResponse.class);
    }


    public static void getAllUser(DisposeDataListener listener, Context context){
//        Log.d("DEBUG", "In RequestCenter: ->getAllUser " + ServerApi.getAll);
        RequestCenter.getRequest(ServerApi.getAll, null, context, listener, UserDefaultList.class, DefaultResponse.class);
    }
}
