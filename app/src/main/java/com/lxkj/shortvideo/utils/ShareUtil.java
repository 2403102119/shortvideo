package com.lxkj.shortvideo.utils;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;

import com.lxkj.shortvideo.R;
import com.umeng.socialize.ShareAction;
import com.umeng.socialize.UMShareListener;
import com.umeng.socialize.bean.SHARE_MEDIA;
import com.umeng.socialize.media.UMImage;
import com.umeng.socialize.media.UMWeb;
import com.umeng.socialize.shareboard.ShareBoardConfig;



/**
 * 分享工具类
 */
public class ShareUtil implements UMShareListener {

    private static ShareUtil shareUtil = null;
    private Context context;
    public static ShareUtil getInstance() {
        if (shareUtil == null) {
            synchronized (ShareUtil.class) {
                if (shareUtil == null) {
                    shareUtil = new ShareUtil();
                }
            }
        }
        return shareUtil;
    }
    public void share(Activity activity, String url, String description) {
        UMWeb web = new UMWeb(url);
        web.setTitle(activity.getResources().getString(R.string.app_name));//标题
        web.setDescription(description);//描述
        UMImage image = new UMImage(activity, R.mipmap.ic_logo);
        web.setThumb(image);
        ShareAction shareAction = new ShareAction(activity).withMedia(web).setDisplayList(SHARE_MEDIA.WEIXIN, SHARE_MEDIA.WEIXIN_CIRCLE,
                SHARE_MEDIA.QQ)
                .setCallback(this);
        ShareBoardConfig config = new ShareBoardConfig();
        config.setMenuItemBackgroundShape(ShareBoardConfig.BG_SHAPE_NONE);
        shareAction.open(config);
    }

    public void share(Activity activity, Bitmap bitmap,SHARE_MEDIA share_media) {
        UMImage image = new UMImage(activity, bitmap);
        image.compressStyle = UMImage.CompressStyle.SCALE;//大小压缩，默认为大小压缩，适合普通很大的图
        new ShareAction(activity)
                .setPlatform(share_media)//传入平台
                .withMedia(image)//分享内容

                .setCallback(this)//回调监听器
                .share();
    }


    public void share(Activity activity, String url, String description, SHARE_MEDIA share_media) {
        this.context = activity;
        UMWeb web = new UMWeb(url);
        web.setTitle(activity.getResources().getString(R.string.app_name));//标题
        web.setDescription(description);//描述
        UMImage image = new UMImage(activity, R.mipmap.ic_logo);
        web.setThumb(image);
        new ShareAction(activity)
                .setPlatform(share_media)//传入平台
                .withMedia(web)//分享内容
                .setCallback(this)//回调监听器
                .share();
    }



    @Override
    public void onStart(SHARE_MEDIA share_media) {

    }

    @Override
    public void onResult(SHARE_MEDIA share_media) {
//        ToastUtil.show("分享成功");
    }


    @Override
    public void onError(SHARE_MEDIA share_media, Throwable throwable) {
//        ToastUtil.show("分享失败");
    }

    @Override
    public void onCancel(SHARE_MEDIA share_media) {
//        ToastUtil.show("分享取消");
    }
}
