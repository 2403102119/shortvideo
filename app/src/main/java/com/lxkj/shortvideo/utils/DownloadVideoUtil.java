package com.lxkj.shortvideo.utils;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Environment;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by kxn on 2020/4/16 0016.
 */
public class DownloadVideoUtil {
    public static void downMp4(Context context,String path) {
        //正在下载更新
        new Thread() {
            @Override
            public void run() {
                try {
                    File file = getFileFromServer(path);
                    context.sendBroadcast(new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, Uri.parse("file://" + file)));
                    sleep(1000);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }.start();
    }


    public static File getFileFromServer(String path) throws Exception {
        // 如果相等的话表示当前的sdcard挂载在手机上并且是可用的
        if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
            URL url = new URL(path);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setConnectTimeout(5000);
            // 获取到文件的大小
            InputStream is = conn.getInputStream();
            File sd1 = Environment.getExternalStorageDirectory();
            String path1 = sd1.getPath() + "/fyb";
            File myfile1 = new File(path1);
            if (!myfile1.exists()) {
                myfile1.mkdir();
            }
            File file = new File(myfile1, System.currentTimeMillis()+".mp4");
            FileOutputStream fos = new FileOutputStream(file);
            BufferedInputStream bis = new BufferedInputStream(is);
            byte[] buffer = new byte[1024];
            int len;
            int total = 0;
            while ((len = bis.read(buffer)) != -1) {
                fos.write(buffer, 0, len);
                total += len;
                // 获取当前下载量
            }
            fos.close();
            bis.close();
            is.close();
            return file;
        } else {
            return null;
        }
    }
}
