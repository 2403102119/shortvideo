package com.lxkj.shortvideo.ui.fragment.competition;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

import com.lxkj.shortvideo.R;
import com.lxkj.shortvideo.adapter.CommentAdapter;
import com.lxkj.shortvideo.bean.DataListBean;
import com.lxkj.shortvideo.ui.fragment.TitleFragment;
import com.makeramen.roundedimageview.RoundedImageView;

import java.util.ArrayList;

import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * Time:2021/1/6
 * <p>
 * Author:李迪迦
 * <p>
 * Interface:
 */
public class WorkDetails extends TitleFragment implements View.OnClickListener {
    Unbinder unbinder;
    @BindView(R.id.riIcon)
    RoundedImageView riIcon;
    @BindView(R.id.tvGuanzhu)
    TextView tvGuanzhu;
    @BindView(R.id.rycomment)
    RecyclerView rycomment;
    @BindView(R.id.navi_title)
    TextView naviTitle;
    @BindView(R.id.navi_left)
    ImageView naviLeft;

    private ArrayList<DataListBean> listBeans;
    private int page = 1, totalPage = 1;
    private CommentAdapter commentAdapter;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
        rootView = inflater.inflate(R.layout.fra_workdetaile, container, false);
        unbinder = ButterKnife.bind(this, rootView);
        act.hindNaviBar();
        initView();
        return rootView;
    }

    public void initView() {

        listBeans = new ArrayList<DataListBean>();
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        rycomment.setLayoutManager(layoutManager);
        commentAdapter = new CommentAdapter(getContext(), listBeans);
        rycomment.setAdapter(commentAdapter);
        commentAdapter.setOnItemClickListener(new CommentAdapter.OnItemClickListener() {
            @Override
            public void OnItemClickListener(int firstPosition) {
            }
        });


        naviLeft.setOnClickListener(this);
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.navi_left:
                act.finishSelf();
                break;
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }
}
