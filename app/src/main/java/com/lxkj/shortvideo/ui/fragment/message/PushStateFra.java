package com.lxkj.shortvideo.ui.fragment.message;

import android.Manifest;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.hys.utils.AppUtils;
import com.luck.picture.lib.PictureSelector;
import com.luck.picture.lib.config.PictureMimeType;
import com.luck.picture.lib.entity.LocalMedia;
import com.lxkj.shortvideo.AppConsts;
import com.lxkj.shortvideo.R;
import com.lxkj.shortvideo.adapter.PostArticleImgAdapter;
import com.lxkj.shortvideo.bean.ResultBean;
import com.lxkj.shortvideo.http.BaseCallback;
import com.lxkj.shortvideo.http.OkHttpHelper;
import com.lxkj.shortvideo.http.SpotsCallBack;
import com.lxkj.shortvideo.http.Url;
import com.lxkj.shortvideo.imageloader.GlideEngine;
import com.lxkj.shortvideo.ui.fragment.TitleFragment;
import com.lxkj.shortvideo.utils.ListUtil;
import com.lxkj.shortvideo.utils.SharePrefUtil;
import com.lxkj.shortvideo.utils.StringUtil;
import com.lxkj.shortvideo.utils.ToastUtil;
import com.zhy.m.permission.MPermissions;
import com.zhy.m.permission.PermissionGrant;

import java.io.File;
import java.io.IOException;
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
import top.zibin.luban.Luban;

import static android.app.Activity.RESULT_OK;
import static com.luck.picture.lib.config.PictureConfig.CHOOSE_REQUEST;

/**
 * Time:2021/1/5
 * <p>
 * Author:李迪迦
 * <p>
 * Interface:发布动态
 */
public class PushStateFra extends TitleFragment implements View.OnClickListener {
    Unbinder unbinder;
    @BindView(R.id.et_itea)
    EditText etItea;
    @BindView(R.id.rcvImg)
    RecyclerView rcvImg;
    @BindView(R.id.tv_login)
    TextView tvLogin;
    @BindView(R.id.imAll)
    ImageView imAll;
    @BindView(R.id.imJinjin)
    ImageView imJinjin;
    @BindView(R.id.imHaoyou)
    ImageView imHaoyou;

    private List<String> mBannerSelectPath = new ArrayList<>();
    private PostArticleImgAdapter postArticleImgAdapter;
    private int select_number = 6;
    String plusPath;
    private String type = "1", sendtype = "1", lon, lat, province_city_town, address;
    private ArrayList<String> imagelist = new ArrayList<>();
    private List<LocalMedia> selectList = new ArrayList<>();
    private String timetype, carousel;
    private String html;
    private String beforehtml;
    private String imageList;
    public static final int IMAGE_SIZE = 6;//可添加图片最大数
    private String visibilityType = "1";

    @Override
    public String getTitleName() {
        return "发布动态";
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
        rootView = inflater.inflate(R.layout.fra_pushstate, container, false);
        unbinder = ButterKnife.bind(this, rootView);
        initView();
        return rootView;
    }

    public void initView() {

        postArticleImgAdapter = new PostArticleImgAdapter(mContext, mBannerSelectPath);
        rcvImg.setLayoutManager(new StaggeredGridLayoutManager(3, StaggeredGridLayoutManager.VERTICAL));
        rcvImg.setAdapter(postArticleImgAdapter);
        postArticleImgAdapter.setOnItemClickListener(new PostArticleImgAdapter.OnItemClickListener() {
            @Override
            public void OnItemClickListener(int positiom) {
                mBannerSelectPath.remove(positiom);
                postArticleImgAdapter.notifyDataSetChanged();
                select_number = 6 - (mBannerSelectPath.size() - 1);
                Log.i("TAG", "delImageAtPostion: " + imagelist);

                Log.i("TAG", "onClick: " + imagelist.size());
            }

            @Override
            public void Onbig(int position) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    checkPmsExternalStorageDeatil();
                } else {
                    pmsExternalStorageSuccess();
                }
            }

        });

        //添加按钮图片资源
        plusPath = getString(R.string.glide_plus_icon_string) + AppUtils.getPackageInfo(mContext).packageName + "/mipmap/" + R.mipmap.tianjiatupian;
        mBannerSelectPath.add(plusPath);//添加按键，超过9张时在adapter中隐藏

        imAll.setOnClickListener(this);
        imJinjin.setOnClickListener(this);
        imHaoyou.setOnClickListener(this);
        tvLogin.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.imAll://所有人可见
                imAll.setImageResource(R.mipmap.yixuan);
                imJinjin.setImageResource(R.mipmap.weixuan);
                imHaoyou.setImageResource(R.mipmap.weixuan);
                visibilityType = "1";
                break;
            case R.id.imJinjin://仅好友可见
                imAll.setImageResource(R.mipmap.weixuan);
                imJinjin.setImageResource(R.mipmap.yixuan);
                imHaoyou.setImageResource(R.mipmap.weixuan);
                visibilityType = "2";
                break;
            case R.id.imHaoyou://好友的好友
                imAll.setImageResource(R.mipmap.weixuan);
                imJinjin.setImageResource(R.mipmap.weixuan);
                imHaoyou.setImageResource(R.mipmap.yixuan);
                visibilityType = "3";
                break;
            case R.id.tv_login://发布
                if (StringUtil.isEmpty(etItea.getText().toString())){
                    ToastUtil.show("请输入动态内容");
                    return;
                }
                publishFriendMoments();
                break;
        }
    }


    /**
     * 发布动态
     */
    private void publishFriendMoments() {
        Map<String, Object> params = new HashMap<>();
        params.put("mid", SharePrefUtil.getString(getContext(), AppConsts.UID, ""));
        params.put("content", etItea.getText().toString());
        params.put("images", carousel);
        params.put("visibilityType", visibilityType);
        OkHttpHelper.getInstance().post_json(getContext(), Url.publishFriendMoments, params, new BaseCallback<ResultBean>() {
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
                     act.finishSelf();
            }

            @Override
            public void onError(Response response, int code, Exception e) {
            }
        });
    }

    private void checkPmsExternalStorageDeatil() {
        MPermissions.requestPermissions(this, AppConsts.PMS_LOCATION,
                Manifest.permission.WRITE_EXTERNAL_STORAGE,
                Manifest.permission.READ_EXTERNAL_STORAGE,
                Manifest.permission.CAMERA
        );
    }

    @PermissionGrant(AppConsts.PMS_LOCATION)
    public void pmsExternalStorageSuccess() {
        //权限授权成功
        PictureSelector.create(act)//在Fragment中使用则是 fragment.this
                .openGallery(PictureMimeType.ofImage())//全部.PictureMimeType.ofAll()、图片.ofImage()、视频.ofVideo()、音频.ofAudio()
                .theme(R.style.picture_default_style)//主题样式(不设置为默认样式) 也可参考demo values/styles下 例如：R.style.picture.white.style
                .loadImageEngine(GlideEngine.createGlideEngine())
                .maxSelectNum(select_number)
                .minSelectNum(1)
                .imageSpanCount(3)// 每行显示个数 int
                .isCamera(true)// 是否显示拍照按钮 true or false
                .sizeMultiplier(0.5f)// glide 加载图片大小 0~1之间 如设置 .glideOverride()无效
                .compress(true)// 是否压缩 true or false
                .minimumCompressSize(100)// 小于100kb的图片不压缩
                .enableCrop(false)//是否裁剪
                .freeStyleCropEnabled(true)// 裁剪框是否可拖拽 true or false
                .showCropFrame(true)
                .synOrAsy(true)//同步true或异步false 压缩 默认同步
                .forResult(CHOOSE_REQUEST);//结果回调onActivityResult code
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == CHOOSE_REQUEST && resultCode == RESULT_OK) {
            ArrayList<String> photoLists = new ArrayList<>();
            selectList = PictureSelector.obtainMultipleResult(data);
            for (int i = 0; i < selectList.size(); i++) {
                if (Build.VERSION.SDK_INT > Build.VERSION_CODES.P) {
                    photoLists.add(selectList.get(i).getAndroidQToPath());
                } else {
                    if (StringUtil.isEmpty(selectList.get(i).getPath())) {
                        photoLists.add(selectList.get(i).getRealPath());
                    } else {
                        photoLists.add(selectList.get(i).getPath());
                    }
                }
            }
            if (photoLists != null && !photoLists.isEmpty()) {
                File file = new File(photoLists.get(0));//获取第一张图片
                if (file.exists()) {
                    mBannerSelectPath.remove(mBannerSelectPath.size() - 1);
                    mBannerSelectPath.addAll(photoLists);
                    uploadImage(mBannerSelectPath);
                    mBannerSelectPath.add(plusPath);//添加按键，超过9张时在adapter中隐藏
                    Log.i("TAG", "onSuccess:------- " + mBannerSelectPath);
                    select_number = 5 - (mBannerSelectPath.size() - 1);
                    postArticleImgAdapter.notifyDataSetChanged();
                }
            }
        }
    }


    /**
     * 上传图片
     */
    private void uploadImage(List<String> path) {
        for (int i = 0; i < path.size(); i++) {
            if (path.get(i).contains(getString(R.string.glide_plus_icon_string))) {
                path.remove(i);
            }
        }

        List<String> reason = new ArrayList<>();
        List<File> reasonFile = new ArrayList<>();
        reason.addAll(path);
        try {
            reasonFile.addAll(Luban.with(getContext()).load(reason).get());
        } catch (IOException e) {
            e.printStackTrace();
        }
        Map<String, List<File>> files = new HashMap<>();
        if (!ListUtil.isEmpty(reasonFile))
            files.put("file", reasonFile);
        mOkHttpHelper.post_file(mContext, Url.fileUpload, files, new SpotsCallBack<ResultBean>(mContext) {
            @Override
            public void onSuccess(Response response, ResultBean resultBean) {
                for (int i = 0; i < resultBean.urls.size(); i++) {
                    carousel += resultBean.urls.get(i) + "|";
                }
//                carousel = carousel.replaceAll("null", "");
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
