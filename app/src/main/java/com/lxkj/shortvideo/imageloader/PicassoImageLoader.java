package com.lxkj.shortvideo.imageloader;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Environment;
import androidx.core.content.ContextCompat;
import android.view.View;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.lxkj.shortvideo.R;
import com.lxkj.shortvideo.utils.StringUtil;
import com.lxkj.shortvideo.utils.ToastUtil;
import com.lxkj.shortvideo.view.NormalDialog;
import com.lzy.ninegrid.NineGridView;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * Created by kxn on 2018/8/2 0002.
 */

public class PicassoImageLoader implements NineGridView.ImageLoader {
    Context context;

    @Override
    public void onDisplayImage(Context context, ImageView imageView, String url) {
        this.context = context;
        if (!StringUtil.isEmpty(url)) {
            Glide.with(context).load(url).apply(new RequestOptions().error(R.mipmap.ic_defaut).placeholder(R.mipmap.ic_defaut)).into(imageView);
//            Picasso.with(context).load(url)//
//                    .placeholder(R.mipmap.ic_logo)//
//                    .error(R.mipmap.ic_logo)//
//                    .into(imageView);
            imageView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View view) {
                    new NormalDialog(context, "是否保存图片到相册", "取消", "保存", true).setOnButtonClickListener(new NormalDialog.OnButtonClick() {
                        @Override
                        public void OnRightClick() {
                            //检测是否有写的权限
                            int permission = ContextCompat.checkSelfPermission(context,
                                    "android.permission.WRITE_EXTERNAL_STORAGE");
                            if (permission != PackageManager.PERMISSION_GRANTED) {
                                // 没有写的权限，去申请写的权限，会弹出对话框
                                ToastUtil.show("请先设置读写权限！");
                            } else {
                                ToastUtil.show("保存成功！");
                                new Thread() {
                                    public void run() {
                                        try {
                                            Bitmap myBitmap = Glide.with(context)
                                                    .asBitmap()
                                                    .load(url)
                                                    .submit().get();
                                            Bitmap bitmap = Bitmap.createBitmap(myBitmap, 0, 0, myBitmap.getWidth(), myBitmap.getHeight());
                                            saveImageToGallery(context, bitmap);
                                        } catch (Exception e) {
                                            e.printStackTrace();
                                        }
                                    }
                                }.start();
                            }
                        }
                        @Override
                        public void OnLeftClick() {
                        }
                    }).show();
                    return false;
                }
            });
        } else {
            Picasso.with(context).load(R.mipmap.ic_defaut)//
                    .into(imageView);
        }

    }

    private void saveImageToGallery(Context context, Bitmap bmp) {
        // 首先保存图片
        File appDir = new File(Environment.getExternalStorageDirectory(), "fyb");
        if (!appDir.exists()) {
            appDir.mkdir();
        }
        String fileName = System.currentTimeMillis() + ".jpg";
        File file = new File(appDir, fileName);
        try {
            FileOutputStream fos = new FileOutputStream(file);
            bmp.compress(Bitmap.CompressFormat.JPEG, 100, fos);
            fos.flush();
            fos.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        // 其次把文件插入到系统图库
//        try {
//            MediaStore.Images.Media.insertImage(context.getContentResolver(),
//                    file.getAbsolutePath(), fileName, null);
//        } catch (FileNotFoundException e) {
//            e.printStackTrace();
//        }
        // 最后通知图库更新
        // context.sendBroadcast(new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, Uri.parse("file://" + path)));
        context.sendBroadcast(new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, Uri.fromFile(new File(file.getPath()))));
    }

    @Override
    public Bitmap getCacheImage(String url) {
        return null;
    }
}
