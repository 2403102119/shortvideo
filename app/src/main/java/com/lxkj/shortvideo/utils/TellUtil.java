package com.lxkj.shortvideo.utils;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import androidx.core.app.ActivityCompat;

import com.lxkj.shortvideo.view.NormalDialog;


/**
 * Created by kxn on 2018/5/15 0015.
 */

public class TellUtil {
    public static void tell(final Context context, final String phoneNum) {

        NormalDialog dialog = new NormalDialog(context, "是否给"+phoneNum+"拨打电话？","取消","确定" ,true);
        dialog.show();
        dialog.setOnButtonClickListener(new NormalDialog.OnButtonClick() {
            @Override
            public void OnRightClick() {
                Intent intent = new Intent(Intent.ACTION_CALL, Uri.parse("tel:" + phoneNum));
                if (ActivityCompat.checkSelfPermission(context, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                    return;
                }
                context.startActivity(intent);
            }

            @Override
            public void OnLeftClick() {

            }
        });

    }
}
