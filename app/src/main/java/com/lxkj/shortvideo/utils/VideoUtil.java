package com.lxkj.shortvideo.utils;

import android.graphics.Bitmap;
import android.media.MediaMetadataRetriever;

/**
 * Created by kxn on 2019/2/18 0018.
 */
public class VideoUtil {

    /**
     * 获取视频封面图
     * @param path
     * @return
     */
    public  static Bitmap getVideoThumb(String path) {

        MediaMetadataRetriever media = new MediaMetadataRetriever();

        media.setDataSource(path);

        return  media.getFrameAtTime();

    }
}
