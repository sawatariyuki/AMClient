package com.gift.sawatariyuki.amclient.Utils.dataRecoder;

import android.content.Context;
import android.content.SharedPreferences;

import java.util.Map;

public class DataRecorder {
    /***
     * 用于少量数据的存储工具类
     */
    private static final String DATA_FILE_NAME = "preferences";
    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor editor;

    public DataRecorder(Context context){
        sharedPreferences = context.getSharedPreferences(DATA_FILE_NAME, Context.MODE_PRIVATE);
        editor = sharedPreferences.edit();
    }

    /***
     * 存储
     */
    public void save(String key, Object object){
        if (object instanceof String) {
            editor.putString(key, (String) object);
        } else if (object instanceof Integer) {
            editor.putInt(key, (Integer) object);
        } else if (object instanceof Boolean) {
            editor.putBoolean(key, (Boolean) object);
        } else if (object instanceof Float) {
            editor.putFloat(key, (Float) object);
        } else if (object instanceof Long) {
            editor.putLong(key, (Long) object);
        } else {
            editor.putString(key, object.toString());
        }
        editor.commit();
    }

    /***
     * 取值
     */
    public Object get(String key, Object defaultObject){
        if (defaultObject instanceof String) {
            return sharedPreferences.getString(key, (String) defaultObject);
        } else if (defaultObject instanceof Integer) {
            return sharedPreferences.getInt(key, (Integer) defaultObject);
        } else if (defaultObject instanceof Boolean) {
            return sharedPreferences.getBoolean(key, (Boolean) defaultObject);
        } else if (defaultObject instanceof Float) {
            return sharedPreferences.getFloat(key, (Float) defaultObject);
        } else if (defaultObject instanceof Long) {
            return sharedPreferences.getLong(key, (Long) defaultObject);
        } else {
            return sharedPreferences.getString(key, null);
        }
    }

    /***
     * 删除某个值
     */
    public void remove(String key) {
        editor.remove(key);
        editor.commit();
    }

    /***
     * 查看某个值是否存在
     */
    public Boolean contains(String key) {
        return sharedPreferences.contains(key);
    }

    /**
     * 返回所有的键值对
     */
    public Map<String, ?> getAll() {
        return sharedPreferences.getAll();
    }

    /**
     * 清除所有数据
     */
    public void clear() {
        editor.clear();
        editor.commit();
    }
}

/***
 *
 * loggedUsername   HomeActivity中的用户名
 *
 * email        HomeActivity中的email
 *
 * isActivated  HomeActivity中的激活状况
 *
 * isRemember   LoginActivity中是否记住密码
 *
 * username     LoginActivity中记住的用户名
 *
 * password     LoginActivity中记住的密码
 *
 * selectedEventState   HomeActivity中的事务的state
 *
 * colorMode    应用颜色主题: common_day common_night
 */
