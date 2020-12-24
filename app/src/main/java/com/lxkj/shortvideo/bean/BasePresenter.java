package com.lxkj.shortvideo.bean;

import android.content.Context;

import com.lxkj.shortvideo.utils.IPresenter;

/**
 * @author Administrator
 * @since 2018/6/29
 * function：
 */
public abstract class BasePresenter<M, V> implements IPresenter {

    private static final String TAG = "BasePresenter";
    public M mModel;
    public V mView;
    public Context mContext;

    public void setMV(M m, V v){
        mModel = m;
        mView = v;
    }

    public void setContext(Context mContext) {
        this.mContext = mContext;
    }


}
