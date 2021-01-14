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
import android.widget.LinearLayout;
import android.widget.TextView;

import com.hys.utils.MD5Utils;
import com.lxkj.shortvideo.AppConsts;
import com.lxkj.shortvideo.R;
import com.lxkj.shortvideo.bean.ResultBean;
import com.lxkj.shortvideo.biz.ActivitySwitcher;
import com.lxkj.shortvideo.http.BaseCallback;
import com.lxkj.shortvideo.http.OkHttpHelper;
import com.lxkj.shortvideo.http.SpotsCallBack;
import com.lxkj.shortvideo.http.Url;
import com.lxkj.shortvideo.ui.activity.MainActivity;
import com.lxkj.shortvideo.ui.fragment.TitleFragment;
import com.lxkj.shortvideo.ui.fragment.system.WebFra;
import com.lxkj.shortvideo.utils.SharePrefUtil;
import com.lxkj.shortvideo.utils.StringUtil;
import com.lxkj.shortvideo.utils.TimerUtil;
import com.lxkj.shortvideo.utils.ToastUtil;

import java.util.HashMap;
import java.util.Map;

import androidx.annotation.Nullable;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import cn.jpush.android.api.JPushInterface;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Time:2020/12/28
 * <p>
 * Author:李迪迦
 * <p>
 * Interface:注册账号
 */
public class RegisterFra extends TitleFragment implements View.OnClickListener {
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
    @BindView(R.id.imSelect)
    ImageView imSelect;
    @BindView(R.id.tvYonghu)
    TextView tvYonghu;
    @BindView(R.id.llPassword)
    LinearLayout llPassword;

    private boolean eyes = false;
    private boolean select = false;
    private String wx, nickName, userIcon, thirdUid;

    @Override
    public String getTitleName() {
        return "注册";
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fra_register, container, false);
        unbinder = ButterKnife.bind(this, rootView);
        initView();
        return rootView;
    }

    public void initView() {

        wx = getArguments().getString("wx");
        nickName = getArguments().getString("nickName");
        userIcon = getArguments().getString("userIcon");
        thirdUid = getArguments().getString("thirdUid");

        if (StringUtil.isEmpty(wx)) {
            act.titleTv.setText("注册");
            tvConfirm.setText("注册");
            llPassword.setVisibility(View.VISIBLE);
        } else {
            act.titleTv.setText("绑定手机号");
            tvConfirm.setText("绑定");
            llPassword.setVisibility(View.GONE);
        }

        tvConfirm.setOnClickListener(this);
        imEys.setOnClickListener(this);
        tvGetCode.setOnClickListener(this);
        imSelect.setOnClickListener(this);
        tvYonghu.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tvConfirm://注冊
                if (StringUtil.isEmpty(etAccount.getText().toString())) {
                    ToastUtil.show("请输入手机号");
                    return;
                }
                if (StringUtil.isEmpty(etCode.getText().toString())) {
                    ToastUtil.show("请输入验证码");
                    return;
                }
                if (StringUtil.isEmpty(wx)) {
                    if (StringUtil.isEmpty(etPsw.getText().toString())) {
                        ToastUtil.show("请输入密码");
                        return;
                    }
                }
                if (!select) {
                    ToastUtil.show("请阅读并同意《用户协议》");
                    return;
                }
                if (StringUtil.isEmpty(wx)) {
                    mobileExist();
                } else {
                    bindMobile();
                }

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
            case R.id.tvGetCode://获取验证码
                getAuthCode();
                break;
            case R.id.imSelect:
                if (select) {
                    select = false;
                    imSelect.setImageResource(R.mipmap.weixuan);
                } else {
                    select = true;
                    imSelect.setImageResource(R.mipmap.yixuan);
                }
                break;
            case R.id.tvYonghu://用户协议
                Bundle bundle = new Bundle();
                bundle.putString("title", "用户协议");
                bundle.putString("url", "http://122.114.49.242:8081/apiService/common/protocol/2");
                ActivitySwitcher.startFragment(getContext(), WebFra.class, bundle);
                break;
        }
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

                    ToastUtil.show("手机号已注册");
                } else {
                    register();
                }

            }

            @Override
            public void onError(Response response, int code, Exception e) {
            }
        });
    }

    //注册
    private void register() {
        Map<String, Object> params = new HashMap<>();
        params.put("mobile", etAccount.getText().toString());
        params.put("authCode", etCode.getText().toString());
        params.put("password", MD5Utils.md5(etPsw.getText().toString()));
        mOkHttpHelper.post_json(mContext, Url.register, params, new SpotsCallBack<ResultBean>(mContext) {
            @Override
            public void onSuccess(Response response, ResultBean resultBean) {
                ToastUtil.show("注册成功");
                act.finishSelf();

            }

            @Override
            public void onError(Response response, int code, Exception e) {

            }
        });
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

    //绑定手机号
    private void bindMobile() {
        Map<String, Object> params = new HashMap<>();
        params.put("mobile", etAccount.getText().toString());
        params.put("authCode", etCode.getText().toString());
        params.put("password", MD5Utils.md5(etPsw.getText().toString()));
        params.put("openid", thirdUid);
        params.put("avatar", userIcon);
        params.put("nickname", nickName);
        params.put("rid", JPushInterface.getRegistrationID(mContext));
        mOkHttpHelper.post_json(mContext, Url.bindMobile, params, new SpotsCallBack<ResultBean>(mContext) {
            @Override
            public void onSuccess(Response response, ResultBean resultBean) {
                ToastUtil.show("绑定成功");
                SharePrefUtil.saveString(mContext, AppConsts.UID, resultBean.mid);
                SharePrefUtil.saveString(mContext, AppConsts.username, resultBean.nickname);
                SharePrefUtil.saveString(mContext, AppConsts.user_icon, resultBean.avatar);
                AppConsts.userId = resultBean.mid;
                AppConsts.userName = resultBean.nickname;
                SharePrefUtil.saveString(mContext, AppConsts.PHONE, etAccount.getText().toString());
                ActivitySwitcher.start(act, MainActivity.class);
                act.finishSelf();

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
