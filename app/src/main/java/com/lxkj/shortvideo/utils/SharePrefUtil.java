package com.lxkj.shortvideo.utils;

import android.content.Context;
import android.content.SharedPreferences;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;


public class SharePrefUtil {
	private static String tag = SharePrefUtil.class.getSimpleName();
	private final static String SP_NAME = "config";
	private static SharedPreferences sp;
	private static SharedPreferences.Editor editor;

	/**
	 * 保存布尔值
	 *
	 * @param context
	 * @param key
	 * @param value
	 */
	public static void saveBoolean(Context context, String key, boolean value) {
		if (sp == null)
			sp = context.getSharedPreferences(SP_NAME, 0);
		sp.edit().putBoolean(key, value).commit();
	}

	/**
	 * 保存字符串

	 * @param context
	 * @param key
	 * @param value
	 */

	public static void saveString(Context context, String key, String value) {
		if (sp == null)
			sp = context.getSharedPreferences(SP_NAME, 0);
		sp.edit().putString(key, value).commit();

	}

	public static void clear(Context context){
		if (sp == null)
			sp = context.getSharedPreferences(SP_NAME, 0);
		sp.edit().clear().commit();
	}


	public static void addSessionMap(String key, List<String> object) {
		editor = sp.edit();
		if (editor != null) {
			if (key != null && !"".equals(key)) {
				Gson gson = new Gson();
				//转换成json数据，再保存
				editor = sp.edit();
				String Json = gson.toJson(object);
				editor.putString(key, Json);
				editor.commit();
			}
		} else {
			throw new IllegalStateException("editor not initialized");
		}
	}

	/**
	 * 清除某一个sp
	 * @param context
	 * @param key
	 */
	public static void clear(Context context, String key){
		if(sp==null)
			sp = context.getSharedPreferences(SP_NAME, 0);
		sp.edit().remove(key).commit();
	}

	/**
	 * 保存long型
	 *
	 * @param context
	 * @param key
	 * @param value
	 */
	public static void saveLong(Context context, String key, long value) {
		if (sp == null)
			sp = context.getSharedPreferences(SP_NAME, 0);
		sp.edit().putLong(key, value).commit();
	}

	/**
	 * 保存int型
	 *
	 * @param context
	 * @param key
	 * @param value
	 */
	public static void saveInt(Context context, String key, int value) {
		if (sp == null)
			sp = context.getSharedPreferences(SP_NAME, 0);
		sp.edit().putInt(key, value).commit();
	}

	/**
	 * 保存float型
	 *
	 * @param context
	 * @param key
	 * @param value
	 */
	public static void saveFloat(Context context, String key, float value) {
		if (sp == null)
			sp = context.getSharedPreferences(SP_NAME, 0);
		sp.edit().putFloat(key, value).commit();
	}

	/**
	 * 获取字符值
	 *
	 * @param context
	 * @param key
	 * @param defValue
	 * @return
	 */
	public static String getString(Context context, String key, String defValue) {
		if (sp == null)
			sp = context.getSharedPreferences(SP_NAME, 0);
		return sp.getString(key, defValue);
	}
	/**
	 * 获取int值
	 *
	 * @param context
	 * @param key
	 * @param defValue
	 * @return
	 */
	public static int getInt(Context context, String key, int defValue) {
		if (sp == null)
			sp = context.getSharedPreferences(SP_NAME, 0);
		return sp.getInt(key, defValue);
	}

	/**
	 * 获取long值
	 *
	 * @param context
	 * @param key
	 * @param defValue
	 * @return
	 */
	public static long getLong(Context context, String key, long defValue) {
		if (sp == null)
			sp = context.getSharedPreferences(SP_NAME, 0);
		return sp.getLong(key, defValue);
	}

	/**
	 * 获取float值
	 *
	 * @param context
	 * @param key
	 * @param defValue
	 * @return
	 */
	public static float getFloat(Context context, String key, float defValue) {
		if (sp == null)
			sp = context.getSharedPreferences(SP_NAME, 0);
		return sp.getFloat(key, defValue);
	}

	/**
	 * 获取布尔值
	 *
	 * @param context
	 * @param key
	 * @param defValue
	 * @return
	 */
	public static boolean getBoolean(Context context, String key,
									 boolean defValue) {
		if (sp == null)
			sp = context.getSharedPreferences(SP_NAME, 0);
		return sp.getBoolean(key, defValue);
	}

	public static List<String> getDataList(String tag) {
		List<String> datalist = new ArrayList<>();
		String Json = sp.getString(tag, null);
		if (null == Json) {
			return datalist;
		}

		Gson gson = new Gson();
		datalist.addAll((Collection<? extends String>) gson.fromJson(Json, new TypeToken<List<String>>() {

		}.getType()));
		return datalist;

	}

}
