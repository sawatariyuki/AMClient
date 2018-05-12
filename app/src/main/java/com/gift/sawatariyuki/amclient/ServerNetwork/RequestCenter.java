package com.gift.sawatariyuki.amclient.ServerNetwork;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.provider.Settings;
import android.util.Log;
import android.widget.Toast;

import com.gift.sawatariyuki.amclient.Bean.DefaultResponse;
import com.gift.sawatariyuki.amclient.Bean.GetEventResponse;
import com.gift.sawatariyuki.amclient.Bean.GetTypeResponse;
import com.gift.sawatariyuki.amclient.Bean.LogResponse;
import com.gift.sawatariyuki.amclient.Bean.LoginResponse;
import com.gift.sawatariyuki.amclient.Bean.UserDefaultList;
import com.gift.sawatariyuki.amclient.Bean.UserInfoResponse;
import com.gift.sawatariyuki.amclient.HomeActivity;
import com.gift.sawatariyuki.amclient.LoginActivity;
import com.gift.sawatariyuki.amclient.R;
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
                                   Class<?> defaultClazz) {
        if(!NetworkValidation.isNetworkAvailable(context)) {
            Toast.makeText(context, "No network available", Toast.LENGTH_SHORT).show();
            sendNetworkNotification(context);
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
                                    Class<?> defaultClazz) {
        if(!NetworkValidation.isNetworkAvailable(context)) {
            Toast.makeText(context, "No network available", Toast.LENGTH_SHORT).show();
            sendNetworkNotification(context);
            return;
        }
        CommonOkHttpClient.post(CommonRequest.createPostRequest(url, params),
                new DisposeDataHandle(listener, clazz, defaultClazz));
    }

    private static void sendNetworkNotification(Context context) {
        NotificationManager manager = (NotificationManager) context.getSystemService(context.NOTIFICATION_SERVICE);
        Intent descIntent = new Intent(Settings.ACTION_WIRELESS_SETTINGS);
        PendingIntent intent = PendingIntent.getActivity(context, 0, descIntent, 0);

        Notification notification = new Notification.Builder(context)
                .setContentTitle("Network")
                .setContentText("Activate your network")
                .setSmallIcon(R.drawable.network_wifi_img)
                .setWhen(System.currentTimeMillis())
                .setAutoCancel(true)
                .setContentIntent(intent)
                .build();
        manager.notify(1, notification);
    }


    //----------------------------------------------------------------------------------------------
    public static void login_POST(DisposeDataListener listener, RequestParams params, Context context) {
        RequestCenter.postRequest(ServerApi.login, params, context, listener, LoginResponse.class, DefaultResponse.class);
    }

    public static void registerUser(DisposeDataListener listener, RequestParams params, Context context) {
        RequestCenter.postRequest(ServerApi.register, params, context, listener, LoginResponse.class, DefaultResponse.class);
    }

    public static void getUserInfo(DisposeDataListener listener, RequestParams params, Context context) {
        RequestCenter.getRequest(ServerApi.userInfo, params, context, listener, UserInfoResponse.class, DefaultResponse.class);
    }

    public static void updateUserDetail(DisposeDataListener listener, RequestParams params, Context context) {
        RequestCenter.postRequest(ServerApi.userDetail, params, context, listener, DefaultResponse.class, DefaultResponse.class);
    }

    public static void getEvent(DisposeDataListener listener, RequestParams params, Context context) {
        RequestCenter.getRequest(ServerApi.getEvent, params, context, listener, GetEventResponse.class, DefaultResponse.class);
    }

    public static void getType(DisposeDataListener listener, RequestParams params, Context context) {
        RequestCenter.getRequest(ServerApi.getEventType, params, context, listener, GetTypeResponse.class, DefaultResponse.class);
    }

    public static void addEvent(DisposeDataListener listener, RequestParams params, Context context) {
        RequestCenter.postRequest(ServerApi.addEvent, params, context, listener, DefaultResponse.class, DefaultResponse.class);
    }

    public static void deleteEvent(DisposeDataListener listener, RequestParams params, Context context) {
        RequestCenter.postRequest(ServerApi.deleteEvent, params, context, listener, DefaultResponse.class, DefaultResponse.class);
    }

    public static void cancelOrReactiveEvent(DisposeDataListener listener, RequestParams params, Context context) {
        RequestCenter.postRequest(ServerApi.cancelEvent, params, context, listener, DefaultResponse.class, DefaultResponse.class);
    }

    public static void addOrUpdateEventType(DisposeDataListener listener, RequestParams params, Context context) {
        RequestCenter.postRequest(ServerApi.addOrUpdateEventType, params, context, listener, DefaultResponse.class, DefaultResponse.class);
    }

    public static void deleteEventType(DisposeDataListener listener, RequestParams params, Context context) {
        RequestCenter.postRequest(ServerApi.deleteEventType, params, context, listener, DefaultResponse.class, DefaultResponse.class);
    }

    public static void arrange(DisposeDataListener listener, RequestParams params, Context context) {
        RequestCenter.getRequest(ServerApi.arrange, params, context, listener, DefaultResponse.class, DefaultResponse.class);
    }

    public static void getUserLog(DisposeDataListener listener, RequestParams params, Context context) {
        RequestCenter.getRequest(ServerApi.getLog, params, context, listener, LogResponse.class, DefaultResponse.class);
    }
    //----------------------------------------------------------------------------------------------

    public static void activateUser_GET(DisposeDataListener listener, RequestParams params, Context context) {
//        Log.d("DEBUG", "In RequestCenter: ->activateUser_GET " + ServerApi.activate_GET);
        RequestCenter.getRequest(ServerApi.activate_GET, params, context, listener, DefaultResponse.class, DefaultResponse.class);
    }

    public static void activateUser_POST(DisposeDataListener listener, RequestParams params, Context context) {
//        Log.d("DEBUG", "In RequestCenter: ->activateUser_POST " + ServerApi.activate_POST);
        RequestCenter.postRequest(ServerApi.activate_POST, params, context, listener, DefaultResponse.class, DefaultResponse.class);
    }


    public static void getAllUser(DisposeDataListener listener, Context context) {
//        Log.d("DEBUG", "In RequestCenter: ->getAllUser " + ServerApi.getAll);
        RequestCenter.getRequest(ServerApi.getAll, null, context, listener, UserDefaultList.class, DefaultResponse.class);
    }
}
