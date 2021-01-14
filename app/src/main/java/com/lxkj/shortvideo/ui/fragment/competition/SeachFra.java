package com.lxkj.shortvideo.ui.fragment.competition;

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
import android.widget.TextView;

import com.lxkj.shortvideo.AppConsts;
import com.lxkj.shortvideo.R;
import com.lxkj.shortvideo.biz.ActivitySwitcher;
import com.lxkj.shortvideo.ui.fragment.TitleFragment;
import com.lxkj.shortvideo.utils.SharePrefUtil;
import com.lxkj.shortvideo.utils.ToastUtil;
import com.zhy.view.flowlayout.FlowLayout;
import com.zhy.view.flowlayout.TagAdapter;
import com.zhy.view.flowlayout.TagFlowLayout;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

import androidx.annotation.Nullable;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * Time:2021/1/5
 * <p>
 * Author:李迪迦
 * <p>
 * Interface:搜索
 */
public class SeachFra extends TitleFragment {
    Unbinder unbinder;
    @BindView(R.id.etSearch)
    EditText etSearch;
    @BindView(R.id.tag)
    TagFlowLayout tag;
    private List<String> list = new ArrayList<>();
    TagAdapter<String> adapter;
    @Override
    public String getTitleName() {
        return "搜索";
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
        rootView = inflater.inflate(R.layout.fra_seach, container, false);
        unbinder = ButterKnife.bind(this, rootView);
        initView();
        return rootView;
    }

    public void initView() {
        etSearch.setOnEditorActionListener(new TextView.OnEditorActionListener() {
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
                    if (!TextUtils.isEmpty(etSearch.getText().toString())) {
                        list.add(etSearch.getText().toString());
                        SharePrefUtil.addSessionMap(AppConsts.History, list);
                        setData();
                        String edStr = etSearch.getText().toString().trim();
                        Bundle bundle = new Bundle();
                        bundle.putString("name", edStr);
                        ActivitySwitcher.startFragment(getActivity(), SearchResultFra.class, bundle);
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

        tag.setOnTagClickListener(new TagFlowLayout.OnTagClickListener() {
            @Override
            public boolean onTagClick(View view, int position, FlowLayout parent) {
//                list.add(etSearch.getText().toString());
//                SharePrefUtil.addSessionMap(AppConsts.History, list);
//                setData();
                String edStr = list.get(position);
                Bundle bundle = new Bundle();
                bundle.putString("name", edStr);
                ActivitySwitcher.startFragment(getActivity(), SearchResultFra.class, bundle);
                return true;
            }
        });

        setData();

    }

    public void setData(){
        list.clear();
        List<String> list1 = new ArrayList<>();
        HashSet<String> hs = new HashSet<String>(SharePrefUtil.getDataList(AppConsts.History)); //此时已经去掉重复的数据保存在hashset中
        for (String item : hs){
            list1.add(item);
        }
        if (list1.size()>10){
            for (int i = 0; i <11 ; i++) {
                list.add(list1.get(i));
            }
        }else {
            list.addAll(list1);
        }

        tag.setMaxSelectCount(1);
        adapter = new TagAdapter<String>(list) {
            @Override
            public View getView(FlowLayout parent, int position, String s) {
                TextView view = (TextView) LayoutInflater.from(getContext()).inflate(R.layout.item_seach, parent, false);
                view.setText(s);
                return view;
            }
        };
        tag.setAdapter(adapter);


    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }
}
