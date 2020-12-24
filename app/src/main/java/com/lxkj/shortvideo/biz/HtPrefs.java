package com.lxkj.shortvideo.biz;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;


public class HtPrefs {


    private final static String USER_PROVINCE = "cur_prvc";
    private final static String USER_CITY = "cur_city";

    private final static String USER_UUID = "user_id";
    private final static String ACCESS_TOKEN = "access_token";
    private final static String CHAT_TOKEN = "chat_token";
    private final static String USER_PWD = "user_pwd"; //用户密码
    private final static String USER_PHONE = "user_phone"; //用户账号
    private final static String USER_WEIGHT = "user_weight";
    private final static String USER_UNREAD = "unread_";

    private final static String HISTORY_CITY1 = "history_city1";
    private final static String HISTORY_CITY2 = "history_city2";
    private final static String HISTORY_CITY3 = "history_city3";

    private final static String ORDER_UUID = "order_uuid";
    private final static String SEND_MONEY_TIME = "send_money_time";//记录展示赠送弹窗的日期
    public final static String SHAKE_TYPE = "shake_type";


    // default
    private static SharedPreferences getDefault(final Context ctx) {
        return PreferenceManager.getDefaultSharedPreferences(ctx);
    }


    public static boolean getBoolValue(final Context ctx, final String key) {
        return getDefault(ctx).getBoolean(key, false);
    }

    public static void setBoolValue(final Context ctx, final String key, final boolean b) {
        getDefault(ctx).edit().putBoolean(key, b).commit();
    }

    public static void setUid(final Context ctx, final String uid) {
        getDefault(ctx).edit().putString(USER_UUID, uid).commit();
    }

    public static String getUid(final Context ctx) {
        return getDefault(ctx).getString(USER_UUID, null);
    }

    public static void setToken(final Context ctx, final String token) {
        getDefault(ctx).edit().putString(ACCESS_TOKEN, token).commit();
    }

    public static String getToken(final Context ctx) {
        return getDefault(ctx).getString(ACCESS_TOKEN, null);
    }

    public static void setChatToken(final Context ctx, final String token) {
        getDefault(ctx).edit().putString(CHAT_TOKEN, token).commit();
    }

    public static String getChatToken(final Context ctx) {
        return getDefault(ctx).getString(CHAT_TOKEN, null);
    }

    public static void setPassword(final Context ctx, final String password) {
        getDefault(ctx).edit().putString(USER_PWD, password).commit();
    }

    public static String getPassword(final Context ctx) {
        return getDefault(ctx).getString(USER_PWD, null);
    }

    public static void setProvince(final Context ctx, final String prvc) {
        getDefault(ctx).edit().putString(USER_PROVINCE, prvc).commit();
    }

    public static String getProvince(final Context ctx) {
        return getDefault(ctx).getString(USER_PROVINCE, null);
    }

    public static void setCity(final Context ctx, final String city) {
        getDefault(ctx).edit().putString(USER_CITY, city).commit();
    }

    public static String getCity(final Context ctx) {
        return getDefault(ctx).getString(USER_CITY, null);
    }

    public static void setUnread(final Context ctx, final String uid, final int c) {
        getDefault(ctx).edit().putInt(USER_UNREAD + uid, c).commit();
    }

    public static int getUnread(final Context ctx, final String uid) {
        return getDefault(ctx).getInt(USER_UNREAD + uid, 0);
    }

    public static void clearLoginInfo(final Context ctx) {
        getDefault(ctx).edit().remove(USER_UUID).remove(USER_PWD).commit();
    }

    public static String getPhone(Context ctx) {
        return getDefault(ctx).getString(USER_PHONE, null);
    }

    public static void setPhone(Context ctx, String phone) {
        getDefault(ctx).edit().putString(USER_PHONE, phone).commit();
    }

    public static float getWeight(Context ctx) {
        return getDefault(ctx).getFloat(USER_WEIGHT, 0);
    }

    public static void setWeight(Context ctx, float w) {
        getDefault(ctx).edit().putFloat(USER_WEIGHT, w).commit();
    }


    public static String getHistoryCity1(Context ctx) {
        return getDefault(ctx).getString(HISTORY_CITY1, null);
    }

    public static void setHistoryCity1(Context ctx, String city) {
        getDefault(ctx).edit().putString(HISTORY_CITY1, city).commit();
    }
    public static String getHistoryCity2(Context ctx) {
        return getDefault(ctx).getString(HISTORY_CITY2, null);
    }

    public static void setHistoryCity2(Context ctx, String city) {
        getDefault(ctx).edit().putString(HISTORY_CITY2, city).commit();
    }
    public static String getHistoryCity3(Context ctx) {
        return getDefault(ctx).getString(HISTORY_CITY3, null);
    }

    public static void setHistoryCity3(Context ctx, String city) {
        getDefault(ctx).edit().putString(HISTORY_CITY3, city).commit();
    }


    public static String getOrderUuid(final Context ctx) {
        return getDefault(ctx).getString(ORDER_UUID, null);
    }
    public static void setOrderUuid(final Context ctx, String orderUuid) {
        getDefault(ctx).edit().putString(ORDER_UUID, orderUuid).commit();
    }

    public static String getSendMoneyTime(final Context ctx) {
        return getDefault(ctx).getString(SEND_MONEY_TIME, null);
    }
    public static void setSendMoneyTime(final Context ctx, String time) {
        getDefault(ctx).edit().putString(SEND_MONEY_TIME, time).commit();
    }


}
