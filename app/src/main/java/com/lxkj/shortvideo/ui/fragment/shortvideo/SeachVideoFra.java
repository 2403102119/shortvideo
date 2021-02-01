package com.lxkj.shortvideo.ui.fragment.shortvideo;

import android.content.Context;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.lxkj.shortvideo.AppConsts;
import com.lxkj.shortvideo.R;
import com.lxkj.shortvideo.adapter.SeachResultVideoAdapter;
import com.lxkj.shortvideo.adapter.SeachVideoFAfapter;
import com.lxkj.shortvideo.bean.ResultBean;
import com.lxkj.shortvideo.biz.ActivitySwitcher;
import com.lxkj.shortvideo.http.BaseCallback;
import com.lxkj.shortvideo.http.OkHttpHelper;
import com.lxkj.shortvideo.http.Url;
import com.lxkj.shortvideo.ui.fragment.TitleFragment;
import com.lxkj.shortvideo.utils.SharePrefUtil;
import com.lxkj.shortvideo.utils.ToastUtil;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Time:2021/1/28
 * <p>
 * Author:李迪迦
 * <p>
 * Interface:搜索短视频
 */
public class SeachVideoFra extends TitleFragment {
    Unbinder unbinder;
    @BindView(R.id.llSeach)
    LinearLayout llSeach;
    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;
    @BindView(R.id.etSousuo)
    EditText etSousuo;
    private List<String> listBeans = new ArrayList<>();
    private SeachVideoFAfapter seachVideoFAfapter;

    @Override
    public String getTitleName() {
        return "搜索";
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
        rootView = inflater.inflate(R.layout.fra_seachvideo, container, false);
        unbinder = ButterKnife.bind(this, rootView);
        initView();
        return rootView;
    }

    public void initView() {
        seachVideoFAfapter = new SeachVideoFAfapter(getContext(), listBeans);
        recyclerView.setLayoutManager(new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL));
        recyclerView.setAdapter(seachVideoFAfapter);
        seachVideoFAfapter.setOnItemClickListener(new SeachVideoFAfapter.OnItemClickListener() {
            @Override
            public void OnItemClickListener(int firstPosition) {
                Bundle bundle = new Bundle();
                bundle.putString("key", listBeans.get(firstPosition));
                ActivitySwitcher.startFragment(getActivity(), SeachResultVideoFra.class, bundle);
            }
        });
        hotKeywords();

        etSousuo.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                /*判断是否是“GO”键*/
                if (actionId == EditorInfo.IME_ACTION_GO) {
                    /*隐藏软键盘*/
                    InputMethodManager imm = (InputMethodManager) v.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
                    if (imm.isActive()) {
                        imm.hideSoftInputFromWindow(v.getApplicationWindowToken(), 0);
                    }
                    return true;
                }

                if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                    /*隐藏软键盘*/
                    InputMethodManager imm = (InputMethodManager) v.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
                    if (imm.isActive()) {
                        imm.hideSoftInputFromWindow(v.getApplicationWindowToken(), 0);
                    }
                    if (!TextUtils.isEmpty(etSousuo.getText().toString())) {

                        String edStr = etSousuo.getText().toString().trim();
                        Bundle bundle = new Bundle();
                        bundle.putString("key", edStr);
                        ActivitySwitcher.startFragment(getActivity(), SeachResultVideoFra.class, bundle);
//                        //隐藏软键盘
//                      hideInputMethod();
                    } else {
                        ToastUtil.show("关键字不能为空");
                    }
                    return true;
                }
                return false;
            }
        });
    }

    /**
     * 热搜关键词
     */
    private void hotKeywords() {
        Map<String, Object> params = new HashMap<>();
        params.put("sid", SharePrefUtil.getString(getContext(), AppConsts.UID, ""));
        OkHttpHelper.getInstance().post_json(getContext(), Url.hotKeywords, params, new BaseCallback<ResultBean>() {
            @Override
            public void onBeforeRequest(Request request) {
            }

            @Override
            public void onFailure(Request request, Exception e) {
            }

            @Override
            public void onResponse(Response response) {
            }

            @Override
            public void onSuccess(Response response, ResultBean resultBean) {
                listBeans.clear();
                listBeans.addAll(resultBean.keys);
                seachVideoFAfapter.notifyDataSetChanged();

            }

            @Override
            public void onError(Response response, int code, Exception e) {
            }
        });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }
}
