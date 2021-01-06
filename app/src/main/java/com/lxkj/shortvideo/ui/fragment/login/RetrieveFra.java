package com.lxkj.shortvideo.ui.fragment.login;

import android.os.Bundle;
import android.text.TextUtils;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.hys.utils.MD5Utils;
import com.lxkj.shortvideo.R;
import com.lxkj.shortvideo.bean.ResultBean;
import com.lxkj.shortvideo.http.BaseCallback;
import com.lxkj.shortvideo.http.OkHttpHelper;
import com.lxkj.shortvideo.http.SpotsCallBack;
import com.lxkj.shortvideo.http.Url;
import com.lxkj.shortvideo.ui.fragment.TitleFragment;
import com.lxkj.shortvideo.utils.StringUtil;
import com.lxkj.shortvideo.utils.TimerUtil;
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
 * Created by kxn on 2020/1/10 0010.
 */
public class RetrieveFra extends TitleFragment implements View.OnClickListener {
    Unbinder unbinder;
    @BindView(R.id.etAccount)
    EditText etAccount;
    @BindView(R.id.etCode)
    EditText etCode;
    @BindView(R.id.tvGetCode)
    TextView tvGetCode;
    @BindView(R.id.etPsw)
    EditText etPsw;
    @BindView(R.id.tvConfirm)
    TextView tvConfirm;
    @BindView(R.id.imEys)
    ImageView imEys;
    private boolean eyes = false;

    @Override
    public String getTitleName() {
        return "重置密码";
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fra_retrieve, container, false);
        unbinder = ButterKnife.bind(this, rootView);
        initView();
        return rootView;
    }

    private void initView() {
        tvGetCode.setOnClickListener(this);
        tvConfirm.setOnClickListener(this);
        imEys.setOnClickListener(this);
    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    @Override
    public void onClick(View view) {
        Bundle bundle = new Bundle();
        switch (view.getId()) {
            case R.id.tvGetCode://获取验证码
                getAuthCode();
                break;
            case R.id.tvConfirm://重置
                if (StringUtil.isEmpty(etAccount.getText().toString())) {
                    ToastUtil.show("请输入手机号");
                    return;
                }
                if (StringUtil.isEmpty(etCode.getText().toString())) {
                    ToastUtil.show("请输入验证码");
                    return;
                }
                if (StringUtil.isEmpty(etPsw.getText().toString())) {
                    ToastUtil.show("请输入密码");
                    return;
                }
                mobileExist();
                break;
            case R.id.imEys:
                if (eyes) {
                    eyes = false;
                    imEys.setImageResource(R.mipmap.biyan);
                    etPsw.setTransformationMethod(PasswordTransformationMethod.getInstance());
                } else {
                    eyes = true;
                    imEys.setImageResource(R.mipmap.zhengyan);
                    etPsw.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                }
                break;
        }
    }


    //获取验证码
    private void getAuthCode() {
        if (TextUtils.isEmpty(etAccount.getText().toString())) {
            ToastUtil.show("请输入手机号");
            return;
        }
        Map<String, Object> params = new HashMap<>();
        params.put("mobile", etAccount.getText().toString());
        mOkHttpHelper.post_json(mContext, Url.getAuthCode, params, new SpotsCallBack<ResultBean>(mContext) {
            @Override
            public void onSuccess(Response response, ResultBean resultBean) {
                if (resultBean.getResult().equals("0")) {
                    TimerUtil mTimerUtil = new TimerUtil(tvGetCode);
                    mTimerUtil.timers();
                    ToastUtil.show("验证码已发送，请注意查收");
                } else
                    ToastUtil.show(resultBean.getResultNote());
            }

            @Override
            public void onError(Response response, int code, Exception e) {

            }
        });
    }

    /**
     * 手机号是否已注册
     */
    private void mobileExist() {
        Map<String, Object> params = new HashMap<>();
        params.put("mobile", etAccount.getText().toString());
        OkHttpHelper.getInstance().post_json(getContext(), Url.mobileExist, params, new BaseCallback<ResultBean>() {
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
                if (resultBean.state.equals("1")) {//1已注册，0未注册
                    changePasswordByCode();
                } else {
                    ToastUtil.show("手机号未注册");
                }

            }

            @Override
            public void onError(Response response, int code, Exception e) {
            }
        });
    }

    //通过验证码修改密码
    private void changePasswordByCode() {
        Map<String, Object> params = new HashMap<>();
        params.put("mobile", etAccount.getText().toString());
        params.put("authCode", etCode.getText().toString());
        params.put("password", MD5Utils.md5(etPsw.getText().toString()));
        mOkHttpHelper.post_json(mContext, Url.changePasswordByCode, params, new SpotsCallBack<ResultBean>(mContext) {
            @Override
            public void onSuccess(Response response, ResultBean resultBean) {
                ToastUtil.show("重置成功");
                act.finishSelf();

            }

            @Override
            public void onError(Response response, int code, Exception e) {

            }
        });
    }

}
