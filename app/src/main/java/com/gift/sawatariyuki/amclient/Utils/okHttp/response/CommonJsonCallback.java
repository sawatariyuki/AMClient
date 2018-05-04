package com.gift.sawatariyuki.amclient.Utils.okHttp.response;

import android.os.Handler;
import android.os.Looper;
import android.support.annotation.NonNull;
import android.util.Log;

import com.gift.sawatariyuki.amclient.Utils.okHttp.exception.OkHttpException;
import com.gift.sawatariyuki.amclient.Utils.okHttp.listener.DisposeDataHandle;
import com.gift.sawatariyuki.amclient.Utils.okHttp.listener.DisposeDataListener;
import com.google.gson.Gson;


import org.json.JSONObject;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

/**
 * 描述: 处理JSON数据的回调响应
 */

@SuppressWarnings("UnnecessaryReturnStatement")
public class CommonJsonCallback implements Callback{

    //与服务器的字段的一个对应关系
    protected final String RESULT_CODE = "code"; //有返回则对于http请求来说是成功的，但还有可能是业务逻辑上的错误
    protected final int RESULT_ERROR_CODE_VALUE = 666;
    protected final String ERROR_MSG = "msg";
    protected final String EMPTY_MSG = "";

    //自定义异常类型
    protected final int NETWORK_ERROR = -1; //the network relative error
    protected final int JSON_ERROR = -2; //the JSON relative error
    protected final int OTHER_ERROR = -3; //the unknow error

    private Handler mDeliveryHandler; //进行消息的转发
    private DisposeDataListener mListener;
    private Class<?> mClass;
    private Class<?> mDefaultClass;

    public CommonJsonCallback(DisposeDataHandle handle){
        this.mListener = handle.mListener;
        this.mClass = handle.mClass;
        this.mDefaultClass = handle.mDefaultClass;
        this.mDeliveryHandler = new Handler(Looper.getMainLooper());
    }

    // 请求失败的处理
    @Override
    public void onFailure(@NonNull Call call, @NonNull final IOException e){
        mDeliveryHandler.post(new Runnable() {
            @Override
            public void run() {
                mListener.onFailure(new OkHttpException(NETWORK_ERROR, e));
            }
        });
    }

    // 请求成功的处理
    @Override
    public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException{
        final String result = response.body().string();
        mDeliveryHandler.post(new Runnable() {
            @Override
            public void run() {
                handleResponse(result);
            }
        });
    }

    // 处理成功的响应
    private void handleResponse(Object responseObj){
        Log.d("DEBUG", "In CommonJsonCallback: " + responseObj.toString());

        if(responseObj == null && responseObj.toString().trim().equals("")){
            mListener.onFailure(new OkHttpException(NETWORK_ERROR, EMPTY_MSG));
            return;
        }
        try{
            JSONObject result = new JSONObject(responseObj.toString());
            Log.d("DEBUG", "In CommonJsonCallback: ->try" + result.toString());
            if(result.has(RESULT_CODE)){
                //从JSON对象中取出我们的响应码，如果不为666，则是正确的响应
                if(result.getInt(RESULT_CODE) != RESULT_ERROR_CODE_VALUE){
                    if(mClass == null){
                        mListener.onSuccess(responseObj);
                    }else{ //需要转化为实体对象
                        Object obj = null;
                        //响应码为 0 则使用正常类解析;响应码为 1 则用默认类解析
                        if(result.getInt(RESULT_CODE) == 0){
                            Log.d("DEBUG", "In CommonJsonCallback: ->try Using Full class");
                            obj = new Gson().fromJson((String) responseObj, mClass);
                        }else if(result.getInt(RESULT_CODE) == 1){
                            Log.d("DEBUG", "In CommonJsonCallback: ->try Using default class");
                            obj = new Gson().fromJson((String) responseObj, mDefaultClass);
                        }
                        if(obj != null){ //表明正确的转为了实体对象
                            mListener.onSuccess(obj);
                        }else{
                            mListener.onFailure(new OkHttpException(JSON_ERROR, EMPTY_MSG));
                        }
                    }
                }else{ //将服务端返回的异常回调到应用层去处理
                    mListener.onFailure(new OkHttpException(OTHER_ERROR, result.get(RESULT_CODE)));
                }
            }
        }catch (Exception e){
            e.printStackTrace();
            mListener.onFailure(new OkHttpException(OTHER_ERROR, e.getMessage()));
        }
    }

}
