package com.gift.sawatariyuki.amclient.Utils.okHttp.listener;

/**
 * 描述: 封装回调接口和要转换的实体对象
 */

@SuppressWarnings("WeakerAccess")
public class DisposeDataHandle {

    public DisposeDataListener mListener = null;
    public Class<?> mClass = null;
    public Class<?> mDefaultClass = null;

    public DisposeDataHandle(DisposeDataListener listener) {
        this.mListener = listener;
    }

    public DisposeDataHandle(DisposeDataListener listener, Class<?> clazz, Class<?> defaultClazz) {
        this.mListener = listener;
        this.mClass = clazz;
        this.mDefaultClass = defaultClazz;
    }

}
