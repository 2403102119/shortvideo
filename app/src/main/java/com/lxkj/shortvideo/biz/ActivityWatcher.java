package com.lxkj.shortvideo.biz;

import androidx.fragment.app.FragmentActivity;

public class ActivityWatcher {
    private static FragmentActivity curAct;

    public static void setCurAct(FragmentActivity act) {
        curAct = act;
    }

    public static FragmentActivity curAct() {
        return curAct;
    }
}
