package com.lxkj.shortvideo.ui.fragment.login;

import android.content.Intent;
import android.os.Bundle;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.hys.utils.MD5Utils;
import com.lxkj.shortvideo.AppConsts;
import com.lxkj.shortvideo.R;
import com.lxkj.shortvideo.bean.ResultBean;
import com.lxkj.shortvideo.biz.ActivitySwitcher;
import com.lxkj.shortvideo.biz.EventCenter;
import com.lxkj.shortvideo.http.BaseCallback;
import com.lxkj.shortvideo.http.OkHttpHelper;
import com.lxkj.shortvideo.http.SpotsCallBack;
import com.lxkj.shortvideo.http.Url;
import com.lxkj.shortvideo.ui.activity.MainActivity;
import com.lxkj.shortvideo.ui.fragment.TitleFragment;
import com.lxkj.shortvideo.ui.fragment.system.WebFra;
import com.lxkj.shortvideo.utils.Md5;
import com.lxkj.shortvideo.utils.SharePrefUtil;
import com.lxkj.shortvideo.utils.StringUtil;
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
 * Created by kxn on 2020/1/9 0009.
 */
public class LoginFra extends TitleFragment implements View.OnClickListener, EventCenter.EventListener {

    Unbinder unbinder;
    @BindView(R.id.etPhone)
    EditText etPhone;
    @BindView(R.id.etPassword)
    EditText etPassword;
    @BindView(R.id.tvRetrieve)
    TextView tvRetrieve;
    @BindView(R.id.tvLogin)
    TextView tvLogin;
    @BindView(R.id.tvYonghu)
    TextView tvYonghu;
    @BindView(R.id.tvYinsi)
    TextView tvYinsi;
    @BindView(R.id.tvRegister)
    TextView tvRegister;
    @BindView(R.id.imWeChat)
    ImageView imWeChat;
    @BindView(R.id.imEys)
    ImageView imEys;

    private boolean eyes = false;


    @Override
    public String getTitleName() {
        return "";
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
        rootView = inflater.inflate(R.layout.fra_login, container, false);
        unbinder = ButterKnife.bind(this, rootView);
        act.hindNaviBar();
        initView();
        return rootView;
    }

    public void initView() {
        act.hindNaviBar();
        eventCenter.registEvent(this, EventCenter.EventType.EVT_BINDPHONE);
        eventCenter.registEvent(this, EventCenter.EventType.EVT_LOGIN);
        eventCenter.registEvent(this, EventCenter.EventType.EVT_REGISTER);

        tvRetrieve.setOnClickListener(this);
        tvRegister.setOnClickListener(this);
        tvLogin.setOnClickListener(this);
        imEys.setOnClickListener(this);
        tvYonghu.setOnClickListener(this);
        tvYinsi.setOnClickListener(this);
    }

    @Override
    public void onResume() {
        super.onResume();

    }


    @Override
    public void onClick(View view) {
        Bundle bundle = new Bundle();
        switch (view.getId()) {
            case R.id.tvRetrieve://忘记密码
                ActivitySwitcher.startFragment(getActivity(), RetrieveFra.class);
                break;
            case R.id.tvRegister://注册
                ActivitySwitcher.startFragment(getActivity(), RegisterFra.class);
                break;
            case R.id.tvLogin://登录
                if (StringUtil.isEmpty(etPhone.getText().toString())) {
                    ToastUtil.show("请输入手机号码");
                    return;
                }
                if (StringUtil.isEmpty(etPassword.getText().toString())) {
                    ToastUtil.show("请输入密码");
                    return;
                }
                mobileExist();
                break;
            case R.id.imEys://查看密碼
                 if (eyes){
                     eyes= false;
                     imEys.setImageResource(R.mipmap.biyan);
                     etPassword.setTransformationMethod(PasswordTransformationMethod.getInstance());
                 }else {
                     eyes= true;
                     imEys.setImageResource(R.mipmap.zhengyan);
                     etPassword.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                 }
                break;
            case R.id.tvYonghu://用户协议
                bundle.putString("title", "用户协议");
                bundle.putString("url", "http://122.114.49.242:8081/apiService/common/protocol/2");
                ActivitySwitcher.startFragment(getContext(), WebFra.class, bundle);
                break;
            case R.id.tvYinsi://隐私政策
                bundle.putString("title", "隐私政策");
                bundle.putString("url", "http://122.114.49.242:8081/apiService/common/protocol/1");
                ActivitySwitcher.startFragment(getContext(), WebFra.class, bundle);
                break;
        }
    }


    //登录
    private void userLogin() {
        Map<String, Object> params = new HashMap<>();
        params.put("mobile", etPhone.getText().toString());
        params.put("authCode", "");
        params.put("password", MD5Utils.md5(etPassword.getText().toString()));
        params.put("type", "1");
        params.put("openid", "");
        params.put("rid", JPushInterface.getRegistrationID(mContext));//推送标识
        mOkHttpHelper.post_json(mContext, Url.login, params, new SpotsCallBack<ResultBean>(mContext) {
            @Override
            public void onSuccess(Response response, ResultBean resultBean) {
                eventCenter.sendType(EventCenter.EventType.EVT_LOGOUT); //关闭 重新打开

                SharePrefUtil.saveString(mContext, AppConsts.UID, resultBean.mid);
                SharePrefUtil.saveString(mContext, AppConsts.username, resultBean.nickname);
                SharePrefUtil.saveString(mContext, AppConsts.user_icon, resultBean.avatar);
                AppConsts.userId = resultBean.mid;
                AppConsts.userName = resultBean.nickname;
                SharePrefUtil.saveString(mContext, AppConsts.PHONE, etPhone.getText().toString());
                ActivitySwitcher.start(act, MainActivity.class);
                act.finishSelf();

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
        params.put("mobile",etPhone.getText().toString());
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

                 if (resultBean.state.equals("1")){//1已注册，0未注册
                     userLogin();
                 }else {
                     ToastUtil.show("手机号未注册");
                 }

            }

            @Override
            public void onError(Response response, int code, Exception e) {
            }
        });
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        eventCenter.unregistEvent(this, EventCenter.EventType.EVT_BINDPHONE);
    }

    @Override
    public void onEvent(EventCenter.HcbEvent e) {
        switch (e.type) {
            case EVT_LOGIN:
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
