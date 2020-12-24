package com.lxkj.shortvideo.utils;

import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.widget.TextView;

import com.lxkj.shortvideo.view.CycleProgress;

import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by kxn on 2019/12/23 0023.
 */
public class Timeutils {
    private static String TAG = "<<<";
    private Timer mTimer = null;
    private TimerTask mTimerTask = null;
    private Handler mHandler = null;
    private static long count = 0;
    private boolean isPause = false;
    private static int delay = 1000;  //1s
    private static int period = 1000;  //1s
    private static final int UPDATE_TEXTVIEW = 0;
    CycleProgress cycleProgress;
    TextView tvCount;
    OnEndListener onEndListener;

    public Timeutils(CycleProgress cycleProgress, TextView tvCount,OnEndListener onEndListener) {
        this.cycleProgress = cycleProgress;
        this.tvCount = tvCount;
        this.onEndListener = onEndListener;
        mHandler = new Handler() {

            @Override
            public void handleMessage(Message msg) {
                switch (msg.what) {
                    case UPDATE_TEXTVIEW:
                        updateTextView();
                        break;
                    default:
                        break;
                }
            }
        };
    }

    public void puseTimer() {
        isPause = !isPause;
    }

    private void updateTextView() {
        cycleProgress.setAnimator((float) (count * 1.66));
        tvCount.setText(count + "");
        if (count>= 60){
            onEndListener.onEnd();
        }
    }

    public void startTimer() {
        if (mTimer == null) {
            mTimer = new Timer();
        }

        if (mTimerTask == null) {
            mTimerTask = new TimerTask() {
                @Override
                public void run() {
                    Log.i(TAG, "count: " + String.valueOf(count));
                    sendMessage(UPDATE_TEXTVIEW);

                    do {
                        try {
                            Log.i(TAG, "sleep(1000)...");
                            Thread.sleep(1000);
                        } catch (InterruptedException e) {
                        }
                    } while (isPause);

                    count++;
                }
            };
        }

        if (mTimer != null && mTimerTask != null)
            mTimer.schedule(mTimerTask, delay, period);

    }

    public void stopTimer() {
        isPause = true;
        if (mTimer != null) {
            mTimer.cancel();
            mTimer = null;
        }
        if (mTimerTask != null) {
            mTimerTask.cancel();
            mTimerTask = null;
        }
        count = 0;
        updateTextView();
    }

    public void sendMessage(int id) {
        if (mHandler != null) {
            Message message = Message.obtain(mHandler, id);
            mHandler.sendMessage(message);
        }
    }

    public interface OnEndListener{
        void onEnd();
    }
}
