package com.iceteck.silicompressorr;

import android.media.MediaMetadataRetriever;
import android.os.AsyncTask;

import com.iceteck.silicompressorr.videocompression.VideoController;

/**
 * Created by Vincent Woo
 * Date: 2017/8/16
 * Time: 15:15
 */

public class VideoCompress {
    private static final String TAG = VideoCompress.class.getSimpleName();

    public static VideoCompressTask compressVideoHigh(String srcPath, String destPath, CompressListener listener) {
        VideoCompressTask task = new VideoCompressTask(listener, VideoController.COMPRESS_QUALITY_HIGH);
        task.execute(srcPath, destPath);
        return task;
    }

    public static VideoCompressTask compressVideoMedium(String srcPath, String destPath, CompressListener listener) {
        MediaMetadataRetriever retr = new MediaMetadataRetriever();
        retr.setDataSource(srcPath);
        String height = retr.extractMetadata(MediaMetadataRetriever.METADATA_KEY_VIDEO_HEIGHT);// 视频高度
        String width = retr.extractMetadata(MediaMetadataRetriever.METADATA_KEY_VIDEO_WIDTH);
        VideoCompressTask task = null;
        if (Integer.valueOf(width) > 1920  ) {
            task = new VideoCompressTask(listener, VideoController.COMPRESS_QUALITY_LOW);
        } else if (Integer.valueOf(width) <= 1920 && Integer.valueOf(width) > 1281) {
            task = new VideoCompressTask(listener, VideoController.COMPRESS_QUALITY_MEDIUM);
        } else {
            task = new VideoCompressTask(listener, VideoController.COMPRESS_QUALITY_HIGH);
        }
        task.execute(srcPath, destPath);
        return task;
    }

    public static VideoCompressTask compressVideoLow(String srcPath, String destPath, CompressListener listener) {
        VideoCompressTask task = new VideoCompressTask(listener, VideoController.COMPRESS_QUALITY_LOW);
        task.execute(srcPath, destPath);
        return task;
    }

    private static class VideoCompressTask extends AsyncTask<String, Float, Boolean> {
        private CompressListener mListener;
        private int mQuality;

        public VideoCompressTask(CompressListener listener, int quality) {
            mListener = listener;
            mQuality = quality;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            if (mListener != null) {
                mListener.onStart();
            }
        }

        @Override
        protected Boolean doInBackground(String... paths) {
            return VideoController.getInstance().convertVideo(paths[0], paths[1], mQuality, new VideoController.CompressProgressListener() {
                @Override
                public void onProgress(float percent) {
                    publishProgress(percent);
                }
            });
        }

        @Override
        protected void onProgressUpdate(Float... percent) {
            super.onProgressUpdate(percent);
            if (mListener != null) {
                mListener.onProgress(percent[0]);
            }
        }

        @Override
        protected void onPostExecute(Boolean result) {
            super.onPostExecute(result);
            if (mListener != null) {
                if (result) {
                    mListener.onSuccess();
                } else {
                    mListener.onFail();
                }
            }
        }
    }

    public interface CompressListener {
        void onStart();

        void onSuccess();

        void onFail();

        void onProgress(float percent);
    }
}
