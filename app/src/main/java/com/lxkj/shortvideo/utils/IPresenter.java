package com.lxkj.shortvideo.utils;


import androidx.lifecycle.Lifecycle;
import androidx.lifecycle.LifecycleObserver;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.OnLifecycleEvent;

/**
 * @author Administrator
 * @since 2018/6/29
 * function：
 */
public interface IPresenter extends LifecycleObserver {

    @OnLifecycleEvent(Lifecycle.Event.ON_CREATE)
    void onCreate(LifecycleOwner owner);

    @OnLifecycleEvent(Lifecycle.Event.ON_START)
    void onStart(LifecycleOwner owner);

    @OnLifecycleEvent(Lifecycle.Event.ON_STOP)
    void stop(LifecycleOwner owner);

    @OnLifecycleEvent(Lifecycle.Event.ON_DESTROY)
    void onDestroy(LifecycleOwner owner);

}
