package com.lxkj.shortvideo.utils;


import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;


import com.lxkj.shortvideo.R;
import com.lxkj.shortvideo.bean.BaseModel;
import com.lxkj.shortvideo.bean.BasePresenter;

import androidx.annotation.IdRes;
import androidx.annotation.LayoutRes;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import butterknife.ButterKnife;
import butterknife.Unbinder;


/**
 * Fragment 的大基类
 */
public abstract class BaseFragment<P extends BasePresenter, M extends BaseModel> extends Fragment implements BaseView {

    public String TAG = getClass().getSimpleName();

    private Unbinder unbinder;
    public P mPresenter;
    public M mModel;
    protected View view;
    private static final String STATE_SAVE_IS_HIDDEN = "STATE_SAVE_IS_HIDDEN";
    public Context mContext;
    public int nowPage=1;
    public int totalPage=1;


    public BaseFragment() {

    }
    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        this.mContext = context;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(setContentView(), container, false);
        unbinder = ButterKnife.bind(this, view);
//        if (savedInstanceState != null) {
//            boolean isSupportHidden = savedInstanceState.getBoolean(STATE_SAVE_IS_HIDDEN);
//
//            FragmentTransaction ft = getFragmentManager().beginTransaction();
//            if (isSupportHidden) {
//                ft.hide(this);
//            } else {
//                ft.show(this);
//            }
//            ft.commit();
//        }
//        Logger.d(TAG + " onCreateView");

        initView(view);
        return view;
    }




    /**
     * 设置Fragment要显示的布局
     *
     * @return 布局的layoutId
     */
    protected abstract @LayoutRes
    int setContentView();



    /**
     * 初始化控件操作
     */
    protected abstract void initView(View view);

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }


    /**
     * @param ids      fragement id
     * @param fragment 添加的Fragment
     * @param bundle   传递的参数
     */
    public void switchFragment(@IdRes int ids, Fragment fragment, Bundle bundle) {
        try {
            FragmentManager manager = getChildFragmentManager();
            FragmentTransaction ft = manager.beginTransaction();
            if (bundle != null) {
                fragment.setArguments(bundle);
            }
            ft.replace(ids, fragment);
            ft.commit();
        } catch (Exception exceptione) {
        }
    }




    /**
     * 启动activity
     */
    public void startBaseActivity(Class<?> class_, Bundle bundle) {
        if (bundle == null)
            bundle = new Bundle();
        Intent intent = new Intent(getActivity(), class_);
        intent.putExtras(bundle);
        startActivity(intent);
        ((Activity) getActivity()).overridePendingTransition(R.anim.push_right_in, R.anim.push_left_out);
    }

    /**
     * 启动activity
     */
    public void startBaseActivity(Class<?> class_) {
        Intent intent = new Intent(getActivity(), class_);
        startActivity(intent);
        getActivity().overridePendingTransition(R.anim.push_right_in, R.anim.push_left_out);
    }
    /**
     * [含有Bundle通过Class打开编辑界面]
     *
     * @param cls
     * @param requestCode 回传的标记，我在B中回传的是RESULT_OK
     */
    public void startBaseActivityForResult(Class<?> cls,
                                           int requestCode) {
        Intent intent = new Intent();
        intent.setClass(getActivity(), cls);
        startActivityForResult(intent, requestCode);
    }

    /**
     * [含有Bundle通过Class打开编辑界面]
     *
     * @param cls
     * @param bundle
     * @param requestCode 回传的标记，我在B中回传的是RESULT_OK
     */
    public void startBaseActivityForResult(Class<?> cls, Bundle bundle,
                                           int requestCode) {
        Intent intent = new Intent();
        intent.setClass(getActivity(), cls);
        if (bundle != null) {
            intent.putExtras(bundle);
        }
        startActivityForResult(intent, requestCode);
    }
    @Override
    public Context _getContext() {
        return getActivity();
    }
}
