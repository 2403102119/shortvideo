package com.lxkj.shortvideo.utils;

import android.content.Context;
import android.os.Build;
import android.os.Environment;
import android.os.StatFs;
import android.telephony.TelephonyManager;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.Locale;

public class DeviceHelper {

    private final static int MAX_LEN = 32;

    // imsi 和 imei读不到时，此为默认值
    private final static String EMPTY = "123456789ABCDEF";

    private static final String KEY_CHANNEL = "UMENG_CHANNEL";
    private static final String DEFAULT_CHANNEL = "self";



    private static String getBrand() {
        try {
            return (Build.BRAND.length() > MAX_LEN
                    ? Build.BRAND.substring(0, MAX_LEN)
                    : Build.BRAND);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";

    }

    private static String getModel() {
        try {
            return (Build.MODEL.length() > MAX_LEN
                    ? Build.MODEL.substring(0, MAX_LEN)
                    : Build.MODEL);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";
    }


    private static int getRamSize(final Context context) {
        BufferedReader br = null;
        try {
            FileReader fr = new FileReader("/proc/meminfo");
            br = new BufferedReader(fr);
            String line = br.readLine();
            String[] arrayOfString = line.split("\\s+");
            return Integer.valueOf(arrayOfString[1]).intValue() / 1024;
        } catch (IOException e) {

        } finally {
            if (br != null) {
                try {
                    br.close();
                } catch (IOException e) {

                }
            }
        }
        return 0;
    }

    @SuppressWarnings("deprecation")
    private static int getRomSize(Context context) {
        try {
            final File root = Environment.getDataDirectory();
            final StatFs sf = new StatFs(root.getPath());
            final long total = sf.getBlockCount() * sf.getBlockSize();
            return (int) (total / 1024 / 1024);
        } catch (Exception e) {
        }
        return 0;
    }

    private static TelephonyManager getTelephonyManager(final Context context) {
        return (TelephonyManager) context
                .getSystemService(Context.TELEPHONY_SERVICE);
    }

    private static String getLocale() {
        return Locale.getDefault().toString();
    }
}
