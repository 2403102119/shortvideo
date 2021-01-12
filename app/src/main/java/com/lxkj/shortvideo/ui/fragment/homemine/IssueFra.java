package com.lxkj.shortvideo.ui.fragment.homemine;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.lxkj.shortvideo.AppConsts;
import com.lxkj.shortvideo.R;
import com.lxkj.shortvideo.bean.ResultBean;
import com.lxkj.shortvideo.http.BaseCallback;
import com.lxkj.shortvideo.http.OkHttpHelper;
import com.lxkj.shortvideo.http.Url;
import com.lxkj.shortvideo.ui.fragment.TitleFragment;
import com.lxkj.shortvideo.utils.SharePrefUtil;
import com.lxkj.shortvideo.utils.StringUtil;
import com.lxkj.shortvideo.utils.ToastUtil;

import java.util.HashMap;
import java.util.Map;

import androidx.annotation.Nullable;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Time:2021/1/5
 * <p>
 * Author:李迪迦
 * <p>
 * Interface:
 */
public class IssueFra extends TitleFragment implements View.OnClickListener {
    Unbinder unbinder;
    @BindView(R.id.etIdea)
    EditText etIdea;
    @BindView(R.id.tvIssue)
    TextView tvIssue;

    @Override
    public String getTitleName() {
        return "意见反馈";
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
        rootView = inflater.inflate(R.layout.fra_idea, container, false);
        unbinder = ButterKnife.bind(this, rootView);
        initView();
        return rootView;
    }

    public void initView() {

    }

    @Override
    public void onClick(View v) {
      switch (v.getId()){
          case R.id.tvIssue://提交
              if (StringUtil.isEmpty(etIdea.getText().toString())){
                  ToastUtil.show("请输入意见");
                  return;
              }
              feedback();
              break;
      }
    }


    /**
     * 意见反馈
     */
    private void feedback() {
        Map<String, Object> params = new HashMap<>();
        params.put("mid", SharePrefUtil.getString(getContext(), AppConsts.UID, ""));
        params.put("content", etIdea.getText().toString());
        OkHttpHelper.getInstance().post_json(getContext(), Url.feedback, params, new BaseCallback<ResultBean>() {
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

                ToastUtil.show(resultBean.resultNote);
                etIdea.setText("");
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
