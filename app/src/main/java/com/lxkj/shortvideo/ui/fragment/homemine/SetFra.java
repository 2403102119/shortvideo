package com.lxkj.shortvideo.ui.fragment.homemine;

import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.lxkj.shortvideo.AppConsts;
import com.lxkj.shortvideo.R;
import com.lxkj.shortvideo.biz.ActivitySwitcher;
import com.lxkj.shortvideo.biz.EventCenter;
import com.lxkj.shortvideo.ui.fragment.TitleFragment;
import com.lxkj.shortvideo.ui.fragment.login.LoginFra;
import com.lxkj.shortvideo.utils.DataCleanManager;
import com.lxkj.shortvideo.utils.SharePrefUtil;
import com.lxkj.shortvideo.utils.ToastUtil;
import com.lxkj.shortvideo.view.NormalDialog;

import androidx.annotation.Nullable;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

import static com.umeng.socialize.utils.ContextUtil.getPackageName;

/**
 * Time:2021/1/5
 * <p>
 * Author:李迪迦
 * <p>
 * Interface:设置
 */
public class SetFra extends TitleFragment implements View.OnClickListener {
    Unbinder unbinder;
    @BindView(R.id.llVersoncode)
    LinearLayout llVersoncode;
    @BindView(R.id.llClean)
    LinearLayout llClean;
    @BindView(R.id.tvLogout)
    TextView tvLogout;
    @BindView(R.id.tvCacheData)
    TextView tvCacheData;
    @BindView(R.id.tvverson)
    TextView tvverson;

    @Override
    public String getTitleName() {
        return "设置";
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
        rootView = inflater.inflate(R.layout.fra_set, container, false);
        unbinder = ButterKnife.bind(this, rootView);
        initView();
        return rootView;
    }

    public void initView() {


        try {
            tvverson.setText("V" + getVersionName());
        } catch (Exception e) {
            e.printStackTrace();
        }
        try {
            tvCacheData.setText(DataCleanManager.getTotalCacheSize(mContext));
        } catch (Exception e) {
            e.printStackTrace();
        }

        llClean.setOnClickListener(this);
        tvLogout.setOnClickListener(this);
    }
    private String getVersionName() throws Exception {
        // 获取packagemanager的实例
        PackageManager packageManager = act.getPackageManager();
        // getPackageName()是你当前类的包名，0代表是获取版本信息
        PackageInfo packInfo = packageManager.getPackageInfo(getPackageName(),0);
        String version = packInfo.versionName;
        return version;
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.llClean://清理缓存

                NormalDialog dialog1 = new NormalDialog(mContext, "您确定要清理缓存吗？", "取消", "确定", true);
                dialog1.show();
                dialog1.setOnButtonClickListener(new NormalDialog.OnButtonClick() {
                    @Override
                    public void OnRightClick() {
                        ToastUtil.show("清空缓存成功！");
                        DataCleanManager.clearAllCache(mContext);
                        tvCacheData.setText("0.0KB");
                    }

                    @Override
                    public void OnLeftClick() {
                    }
                });


                break;
            case R.id.tvLogout://退出登录
                NormalDialog dialog = new NormalDialog(mContext, "您确定要退出登录吗？", "取消", "确定", true);
                dialog.show();
                dialog.setOnButtonClickListener(new NormalDialog.OnButtonClick() {
                    @Override
                    public void OnRightClick() {
                        SharePrefUtil.saveString(mContext, AppConsts.UID, "");
                        eventCenter.sendType(EventCenter.EventType.EVT_LOGOUT);
                        ActivitySwitcher.startFragment(act, LoginFra.class);
                        act.finish();
                    }

                    @Override
                    public void OnLeftClick() {
                    }
                });
                break;
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }


}
