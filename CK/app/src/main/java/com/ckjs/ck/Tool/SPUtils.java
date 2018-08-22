package com.ckjs.ck.Tool;

import android.content.Context;
import android.content.SharedPreferences;

import com.ckjs.ck.Application.CkApplication;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Map;

/**
 * Created by NiPing and Airr Wang
 * Copyright  www.chaokongs.com. All rights reserved.
 */
public class SPUtils {

    public static final String FILE_NAME = "share_data";


    public static void put(Context context, String key, Object object) {
        SharedPreferences sp = CkApplication.getInstance().getSharedPreferences(FILE_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();

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

        SharedPreferencesCompat.apply(editor);
        return;
    }


    public static Object get(Context context, String key, Object defaultObject) {
        try {
            SharedPreferences sp = CkApplication.getInstance().getSharedPreferences(FILE_NAME, Context.MODE_PRIVATE);

            if (defaultObject instanceof String) {
                return sp.getString(key, (String) defaultObject);
            } else if (defaultObject instanceof Integer) {
                return sp.getInt(key, (Integer) defaultObject);
            } else if (defaultObject instanceof Boolean) {
                return sp.getBoolean(key, (Boolean) defaultObject);
            } else if (defaultObject instanceof Float) {
                return sp.getFloat(key, (Float) defaultObject);
            } else if (defaultObject instanceof Long) {
                return sp.getLong(key, (Long) defaultObject);
            }
        } catch (Exception e) {
            if (key.equals("user_id")) {
                return AppConfig.user_id;
            } else if (key.equals("token")) {
                return AppConfig.token;
            } else if (key.equals("name")) {
                return AppConfig.name;
            } else if (key.equals("url")) {
                return AppConfig.url;
            } else if (key.equals("gym_id")) {
                return AppConfig.gym_id;
            } else if (key.equals("height")) {
                return AppConfig.height;
            } else if (key.equals("weight")) {
                return AppConfig.weight;
            } else if (key.equals("sex")) {
                return AppConfig.sex;
            } else if (key.equals("fanssum")) {
                return AppConfig.fanssum;
            } else if (key.equals("motto")) {
                return AppConfig.motto;
            } else if (key.equals("vip")) {
                return AppConfig.vip;
            } else if (key.equals("age")) {
                return AppConfig.age;
            } else if (key.equals("isbind")) {
                return "0";
            } else if (key.equals("first")) {
                return false;
            }
        }
        return null;
    }

    public static Object get(String key, Object defaultObject) {
        try {
            SharedPreferences sp = CkApplication.getInstance().getSharedPreferences(FILE_NAME, Context.MODE_PRIVATE);

            if (defaultObject instanceof String) {
                return sp.getString(key, (String) defaultObject);
            } else if (defaultObject instanceof Integer) {
                return sp.getInt(key, (Integer) defaultObject);
            } else if (defaultObject instanceof Boolean) {
                return sp.getBoolean(key, (Boolean) defaultObject);
            } else if (defaultObject instanceof Float) {
                return sp.getFloat(key, (Float) defaultObject);
            } else if (defaultObject instanceof Long) {
                return sp.getLong(key, (Long) defaultObject);
            }
        } catch (Exception e) {
            if (key.equals("user_id")) {
                return AppConfig.user_id;
            } else if (key.equals("token")) {
                return AppConfig.token;
            } else if (key.equals("name")) {
                return AppConfig.name;
            } else if (key.equals("url")) {
                return AppConfig.url;
            } else if (key.equals("gym_id")) {
                return AppConfig.gym_id;
            } else if (key.equals("height")) {
                return AppConfig.height;
            } else if (key.equals("weight")) {
                return AppConfig.weight;
            } else if (key.equals("sex")) {
                return AppConfig.sex;
            } else if (key.equals("fanssum")) {
                return AppConfig.fanssum;
            } else if (key.equals("motto")) {
                return AppConfig.motto;
            } else if (key.equals("vip")) {
                return AppConfig.vip;
            } else if (key.equals("age")) {
                return AppConfig.age;
            } else if (key.equals("first")) {
                return false;
            }
        }
        return null;
    }


    public static void remove(Context context, String key) {
        SharedPreferences sp = CkApplication.getInstance().getSharedPreferences(FILE_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        editor.remove(key);
        SharedPreferencesCompat.apply(editor);
    }


    public static void clear(Context context) {
        SharedPreferences sp = CkApplication.getInstance().getSharedPreferences(FILE_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        editor.clear();
        SharedPreferencesCompat.apply(editor);
    }


    public static boolean contains(Context context, String key) {
        SharedPreferences sp = CkApplication.getInstance().getSharedPreferences(FILE_NAME, Context.MODE_PRIVATE);
        return sp.contains(key);
    }


    public static Map<String, ?> getAll(Context context) {
        SharedPreferences sp = CkApplication.getInstance().getSharedPreferences(FILE_NAME, Context.MODE_PRIVATE);
        return sp.getAll();
    }


    private static class SharedPreferencesCompat {
        private static final Method sApplyMethod = findApplyMethod();

        private static Method findApplyMethod() {
            try {
                Class clz = SharedPreferences.Editor.class;
                return clz.getMethod("apply");
            } catch (NoSuchMethodException e) {
            }
            return null;
        }


        public static void apply(SharedPreferences.Editor editor) {
            try {
                if (sApplyMethod != null) {
                    sApplyMethod.invoke(editor);
                    return;
                }
            } catch (IllegalArgumentException e) {
            } catch (IllegalAccessException e) {
            } catch (InvocationTargetException e) {
            }
            editor.commit();
        }
    }
}

