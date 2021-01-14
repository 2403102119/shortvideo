package com.lxkj.shortvideo.ui.fragment.competition;

import android.Manifest;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.iceteck.silicompressorr.SiliCompressor;
import com.luck.picture.lib.PictureSelector;
import com.luck.picture.lib.config.PictureMimeType;
import com.luck.picture.lib.entity.LocalMedia;
import com.lxkj.shortvideo.AppConsts;
import com.lxkj.shortvideo.R;
import com.lxkj.shortvideo.adapter.ApplyAdapter;
import com.lxkj.shortvideo.bean.DataListBean;
import com.lxkj.shortvideo.bean.ResultBean;
import com.lxkj.shortvideo.http.BaseCallback;
import com.lxkj.shortvideo.http.SpotsCallBack;
import com.lxkj.shortvideo.http.Url;
import com.lxkj.shortvideo.imageloader.GlideEngine;
import com.lxkj.shortvideo.ui.fragment.TitleFragment;
import com.lxkj.shortvideo.utils.FileSizeUtil;
import com.lxkj.shortvideo.utils.ListUtil;
import com.lxkj.shortvideo.utils.StringUtil;
import com.lxkj.shortvideo.utils.ToastUtil;
import com.lxkj.shortvideo.utils.VideoUtils;
import com.zhy.m.permission.MPermissions;
import com.zhy.m.permission.PermissionGrant;

import java.io.File;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import okhttp3.Request;
import okhttp3.Response;

import static android.app.Activity.RESULT_OK;
import static android.provider.MediaStore.Video.Thumbnails.FULL_SCREEN_KIND;
import static com.luck.picture.lib.config.PictureConfig.CHOOSE_REQUEST;

/**
 * Time:2021/1/6
 * <p>
 * Author:李迪迦
 * <p>
 * Interface:报名参赛
 */
public class ApplyFra extends TitleFragment implements View.OnClickListener {
    Unbinder unbinder;
    @BindView(R.id.etTitle)
    EditText etTitle;
    @BindView(R.id.imShipin)
    ImageView imShipin;
    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;
    @BindView(R.id.imAll)
    ImageView imAll;
    @BindView(R.id.imNoAll)
    ImageView imNoAll;
    @BindView(R.id.tvApply)
    TextView tvApply;
    private List<LocalMedia> selectList = new ArrayList<>();
    private List<DataListBean> listBeans = new ArrayList<>();
    private DataListBean dataListBean = new DataListBean();
    private ApplyAdapter applyAdapter;
    int mediaType;
    private int videoposition;
    public String video, coverImage, videotype, subVideos, visibilityType = "1", cid;
    @Override
    public String getTitleName() {
        return "我要参赛";
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
        rootView = inflater.inflate(R.layout.fra_apply, container, false);
        unbinder = ButterKnife.bind(this, rootView);
        initView();
        return rootView;
    }

    public void initView() {

        cid = getArguments().getString("cid");

        imShipin.setOnClickListener(this);
        imAll.setOnClickListener(this);
        imNoAll.setOnClickListener(this);
        tvApply.setOnClickListener(this);


        listBeans = new ArrayList<DataListBean>();

        listBeans.add(dataListBean);

        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(layoutManager);
        applyAdapter = new ApplyAdapter(getContext(), listBeans);
        recyclerView.setAdapter(applyAdapter);
        applyAdapter.setOnItemClickListener(new ApplyAdapter.OnItemClickListener() {
            @Override
            public void OnItemClickListener(int firstPosition) {
                videotype = "1";
                videoposition = firstPosition;
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    checkPmsExternalStorageVideo();
                } else {
                    pmsExternalStorageSuccessVideo();
                }
            }

            @Override
            public void onEvaluate(int position, String point, String content) {
                listBeans.get(position).title  = content;
            }
        });

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.imShipin://参赛视频
                videotype = "0";
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    checkPmsExternalStorageVideo();
                } else {
                    pmsExternalStorageSuccessVideo();
                }
                break;
            case R.id.imAll://所有人
                visibilityType = "1";
                imAll.setImageResource(R.mipmap.yixuan);
                imNoAll.setImageResource(R.mipmap.weixuan);
                break;
            case R.id.imNoAll://仅好友
                visibilityType = "2";
                imAll.setImageResource(R.mipmap.weixuan);
                imNoAll.setImageResource(R.mipmap.yixuan);
                break;
            case R.id.tvApply://发布

                if (StringUtil.isEmpty(etTitle.getText().toString())) {
                    ToastUtil.show("请输入视频标题");
                    return;
                }
                if (StringUtil.isEmpty(video)) {
                    ToastUtil.show("请上传参赛视频");
                    return;
                }
                for (int i = 0; i < listBeans.size(); i++) {

                    if (StringUtil.isEmpty(listBeans.get(i).video)){
                        listBeans.remove(i);
                    }
                }

                enterCompetitionWorks(listBeans);
                break;
        }
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
                    LocalMedia media = selectList.get(0);
                    String pictureType = media.getMimeType();

                    mediaType = PictureMimeType.getMimeType(pictureType);
                    if (mediaType == 2) {
//                        uploadVideo(media.getPath());
                        double size = 0.00;
                        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.P) {
                             size = FileSizeUtil.getFileOrFilesSize(media.getAndroidQToPath(), 3);
                        } else {
                            if (StringUtil.isEmpty(media.getPath())) {
                                 size = FileSizeUtil.getFileOrFilesSize(media.getRealPath(), 3);
                            } else {
                                 size = FileSizeUtil.getFileOrFilesSize(media.getPath(), 3);

                            }
                        }
                        if (size < 3) {
                            Message msg = mHandler.obtainMessage();
                            msg.what = 1;
                            Bundle bundle = new Bundle();
                            bundle.putString("path", media.getPath());
                            msg.setData(bundle);
                            mHandler.sendMessage(msg);
                        } else {
                            compcompressVideo(media.getPath());
                        }

                        Bitmap bitmap = VideoUtils.getInstance().getVideoThumbnail(media.getPath(), 720, 1280, FULL_SCREEN_KIND);
                        String imagePath = VideoUtils.getInstance().saveBitmap(bitmap, "video");
                        uploadImage(imagePath);
                        return;
                    }

                }
            }
        }
    }


    private Handler mHandler = new Handler(new Handler.Callback() {
        @Override
        public boolean handleMessage(Message msg) {
            switch (msg.what) {
                case 1:
                    uploadVideo(msg.getData().getString("path"));
                    break;
            }
            return true;
        }
    });

    private void compcompressVideo(final String videoPath) {
        final String outPutPath = Environment.getExternalStorageDirectory().getPath() + "/Android/data/com.lxkj.nongda/video/";
        File destDir = new File(outPutPath);
        if (!destDir.exists()) {//如果不存在则创建
            destDir.mkdirs();
        }
        new Thread() {
            @Override
            public void run() {
                super.run();
                try {
                    /**
                     * 视频压缩
                     * 第一个参数:视频源文件路径
                     * 第二个参数:压缩后视频保存的路径"
                     */
                    String comPressPath = SiliCompressor.with(mContext).compressVideo(videoPath, outPutPath);
                    Message msg = mHandler.obtainMessage();
                    msg.what = 1;
                    Bundle bundle = new Bundle();
                    bundle.putString("path", comPressPath);
                    msg.setData(bundle);
                    mHandler.sendMessage(msg);
                    Log.e("path", comPressPath);
                } catch (URISyntaxException e) {
                    e.printStackTrace();
                }
            }
        }.start();

    }


    /**
     * 上传视频
     */
    private void uploadVideo(String path) {
        List<String> reason = new ArrayList<>();
        List<File> reasonFile = new ArrayList<>();
        reason.add(path);

        File file = new File(path);
        reasonFile.add(file);
        Map<String, List<File>> files = new HashMap<>();
        if (!ListUtil.isEmpty(reasonFile))
            files.put("file", reasonFile);
        mOkHttpHelper.post_file(mContext, Url.fileUpload, files, new SpotsCallBack<ResultBean>(mContext) {
            @Override
            public void onSuccess(Response response, ResultBean resultBean) {
                for (int i = 0; i < resultBean.urls.size(); i++) {
                    video = resultBean.urls.get(i);
                }
                if (videotype.equals("0")) {

                } else {
                    listBeans.get(videoposition).title = applyAdapter.contents.get(videoposition);
                    listBeans.get(videoposition).video = video;
                    DataListBean dataListBean = new DataListBean();
                    listBeans.add(dataListBean);
                    applyAdapter.notifyDataSetChanged();
                }

            }

            @Override
            public void onError(Response response, int code, Exception e) {

            }
        });
    }


    /**
     * 上传封面图
     */
    private void uploadImage(String path) {
        List<String> reason = new ArrayList<>();
        List<File> reasonFile = new ArrayList<>();
        reason.add(path);

        File file = new File(path);
        reasonFile.add(file);
        Map<String, List<File>> files = new HashMap<>();
        if (!ListUtil.isEmpty(reasonFile))
            files.put("file", reasonFile);
        mOkHttpHelper.post_file(mContext, Url.fileUpload, files, new SpotsCallBack<ResultBean>(mContext) {
            @Override
            public void onSuccess(Response response, ResultBean resultBean) {
                for (int i = 0; i < resultBean.urls.size(); i++) {
                    coverImage = resultBean.urls.get(i);
                }
                if (videotype.equals("0")) {
                    Glide.with(getActivity()).load(coverImage).apply(new RequestOptions().error(R.mipmap.ic_defaut).placeholder(R.mipmap.ic_defaut)).into(imShipin);
                }else {
                    listBeans.get(videoposition).coverImage = coverImage;
                }

            }

            @Override
            public void onError(Response response, int code, Exception e) {

            }
        });
    }


    /**
     * 报名
     */
    private void enterCompetitionWorks(List<DataListBean> listBeans) {
        Map<String, Object> params = new HashMap<>();
        params.put("mid", userId);
        params.put("cid", cid);
        params.put("title", etTitle.getText().toString());
        params.put("coverImage", coverImage);
        params.put("video", video);
        params.put("subVideos", listBeans);
        params.put("visibilityType", visibilityType);
        mOkHttpHelper.post_json(getContext(), Url.enterCompetitionWorks, params, new BaseCallback<ResultBean>() {
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

    private void checkPmsExternalStorageVideo() {
        MPermissions.requestPermissions(this, AppConsts.PMS_LOCATION,
                Manifest.permission.WRITE_EXTERNAL_STORAGE,
                Manifest.permission.READ_EXTERNAL_STORAGE,
                Manifest.permission.RECORD_AUDIO,
                Manifest.permission.MODIFY_AUDIO_SETTINGS,
                Manifest.permission.CAMERA
        );
    }

    @PermissionGrant(AppConsts.PMS_LOCATION)
    public void pmsExternalStorageSuccessVideo() {
        PictureSelector.create(act)//在Fragment中使用则是 fragment.this
                .openGallery(PictureMimeType.ofVideo())//全部.PictureMimeType.ofAll()、图片.ofImage()、视频.ofVideo()、音频.ofAudio()
                .loadImageEngine(GlideEngine.createGlideEngine())
                .theme(R.style.picture_default_style)//主题样式(不设置为默认样式) 也可参考demo values/styles下 例如：R.style.picture.white.style
                .maxVideoSelectNum(1)
                .imageSpanCount(3)// 每行显示个数 int
                .previewVideo(true)// 是否可预览视频 true or false
                .isCamera(true)// 是否显示拍照按钮 true or false
                .sizeMultiplier(0.5f)// glide 加载图片大小 0~1之间 如设置 .glideOverride()无效
                .compress(true)// 是否压缩 true or false
                .hideBottomControls(true)// 是否显示uCrop工具栏，默认不显示 true or false
                .minimumCompressSize(100)// 小于100kb的图片不压缩
                .synOrAsy(true)//同步true或异步false 压缩 默认同步
                .videoMaxSecond(31)// 显示多少秒以内的视频or音频也可适用 int
                .forResult(CHOOSE_REQUEST);//结果回调onActivityResult code
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        MPermissions.onRequestPermissionsResult(this, requestCode, permissions, grantResults);
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }
}
