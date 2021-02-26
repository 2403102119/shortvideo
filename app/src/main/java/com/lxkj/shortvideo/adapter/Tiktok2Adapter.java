package com.lxkj.shortvideo.adapter;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.lxkj.shortvideo.AppConsts;
import com.lxkj.shortvideo.R;
import com.lxkj.shortvideo.bean.DataListBean;
import com.lxkj.shortvideo.bean.ResultBean;
import com.lxkj.shortvideo.biz.ActivitySwitcher;
import com.lxkj.shortvideo.http.BaseCallback;
import com.lxkj.shortvideo.http.OkHttpHelper;
import com.lxkj.shortvideo.http.Url;
import com.lxkj.shortvideo.ui.fragment.login.LoginFra;
import com.lxkj.shortvideo.utils.PicassoUtil;
import com.lxkj.shortvideo.utils.SharePrefUtil;
import com.lxkj.shortvideo.utils.StringUtil;
import com.lxkj.shortvideo.utils.ToastUtil;
import com.lxkj.shortvideo.utils.cache.PreloadManager;
import com.lxkj.shortvideo.widget.component.TikTokView;
import com.makeramen.roundedimageview.RoundedImageView;
import com.zhy.view.flowlayout.TagFlowLayout;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import androidx.annotation.NonNull;
import androidx.viewpager.widget.PagerAdapter;
import de.hdodenhof.circleimageview.CircleImageView;
import okhttp3.Request;
import okhttp3.Response;

public class Tiktok2Adapter extends PagerAdapter {

    private Context context;
    /**
     * View缓存池，从ViewPager中移除的item将会存到这里面，用来复用
     */
    private List<View> mViewPool = new ArrayList<>();
    /**
     * 数据源
     */
    private List<DataListBean> mVideoBeans;

    public Tiktok2Adapter(List<DataListBean> videoBeans) {

        this.mVideoBeans = videoBeans;
    }

    @Override
    public int getCount() {
        return mVideoBeans == null ? 0 : mVideoBeans.size();
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object o) {
        return view == o;
    }

    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, int position) {
        context = container.getContext();
        View view = null;
        if (mViewPool.size() > 0) {//取第一个进行复用
            view = mViewPool.get(0);
            mViewPool.remove(0);
        }

        ViewHolder viewHolder;
        if (view == null) {
            view = LayoutInflater.from(context).inflate(R.layout.item_shortvideo, container, false);
            viewHolder = new ViewHolder(view);
        } else {
            viewHolder = (ViewHolder) view.getTag();
        }

        DataListBean item = mVideoBeans.get(position);
        //开始预加载
        PreloadManager.getInstance(context).addPreloadTask(item.video, position);

        Glide.with(context).applyDefaultRequestOptions(new RequestOptions()
                .error(R.mipmap.imageerror)
                .placeholder(R.mipmap.imageerror))
                .load(item.video+ AppConsts.ViDEOEND)
                .into(viewHolder.mThumb);

        Glide.with(context).applyDefaultRequestOptions(new RequestOptions()
                .error(R.mipmap.touxiang)
                .placeholder(R.mipmap.touxiang))
                .load(item.member.avatar)
                .into(viewHolder.riIcon);

        viewHolder.tvName.setText(item.member.nickname);
        viewHolder.tvCommentName.setText(item.member.nickname);
        viewHolder.tvTitle.setText(item.title);
        viewHolder.tvTime.setText(item.uploadDate);
        viewHolder.tvLaiyuan.setText("来源："+item.competition.name);

        if (item.collected.equals("1")){
            viewHolder.imShoucang.setImageResource(R.mipmap.yishoucang);
        }else {
            viewHolder.imShoucang.setImageResource(R.mipmap.dianzan);
        }

        viewHolder.imShoucang.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (StringUtil.isEmpty(SharePrefUtil.getString(context, AppConsts.UID, ""))){
                    ToastUtil.show("请先登录");
                    Bundle bundle = new Bundle();
                    ActivitySwitcher.startFragment(context, LoginFra.class,bundle);
                    return;
                }
                if (item.collected.equals("0")) {
                    viewHolder.imShoucang.setImageResource(R.mipmap.yishoucang);
                    collectWorks("1",item.id);
//                    tvShoucang.setText((Integer.parseInt(tvShoucang.getText().toString()) + 1) + "");
                } else {
                    viewHolder.imShoucang.setImageResource(R.mipmap.dianzan);
                    collectWorks("0",item.id);
//                    tvShoucang.setText((Integer.parseInt(tvShoucang.getText().toString()) - 1) + "");
                }
            }
        });

        viewHolder.mPosition = position;
        container.addView(view);
        return view;
    }



    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
        View itemView = (View) object;
        container.removeView(itemView);
        DataListBean item = mVideoBeans.get(position);
        //取消预加载
        PreloadManager.getInstance(container.getContext()).removePreloadTask(item.video);
        //保存起来用来复用
        mViewPool.add(itemView);
    }


    /**
     * 作品收藏/取消收藏
     */
    private void collectWorks(String type,String wid) {
        Map<String, Object> params = new HashMap<>();
        params.put("mid", SharePrefUtil.getString(context,AppConsts.UID,null));
        params.put("wid", wid);
        params.put("type", type);
        OkHttpHelper.getInstance().post_json(context, Url.collectWorks, params, new BaseCallback<ResultBean>() {
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


            }

            @Override
            public void onError(Response response, int code, Exception e) {
            }
        });
    }

    /**
     * 借鉴ListView item复用方法
     */
    public static class ViewHolder {

        public int mPosition;
        public ImageView mThumb;//封面图
        public ImageView imShoucang;
        public ImageView imfenxiang;
        public TikTokView mTikTokView;
        public FrameLayout mPlayerContainer;
        public TextView tvLaiyuan;
        public TextView tvTitle;
        public TextView tvName;
        public TextView tvGuanzhu;
        public TextView tvCommentName;
        public TextView tvTime;
        public RoundedImageView riIcon;
        public TagFlowLayout tagFlow;
        public LinearLayout llDetail;
        public LinearLayout llPinglun;
        ViewHolder(View itemView) {
            mTikTokView = itemView.findViewById(R.id.tiktok_View);
            mThumb = mTikTokView.findViewById(R.id.iv_thumb);
            mPlayerContainer = itemView.findViewById(R.id.container);
            tvLaiyuan = itemView.findViewById(R.id.tvLaiyuan);
            tvTitle = itemView.findViewById(R.id.tvTitle);
            riIcon = itemView.findViewById(R.id.riIcon);
            tvName = itemView.findViewById(R.id.tvName);
            tvGuanzhu = itemView.findViewById(R.id.tvGuanzhu);
            tvCommentName = itemView.findViewById(R.id.tvCommentName);
            imShoucang = itemView.findViewById(R.id.imShoucang);
            tagFlow = itemView.findViewById(R.id.tagFlow);
            tvTime = itemView.findViewById(R.id.tvTime);
            llDetail = itemView.findViewById(R.id.llDetail);
            llPinglun = itemView.findViewById(R.id.llPinglun);
            imfenxiang = itemView.findViewById(R.id.imfenxiang);

            itemView.setTag(this);
        }
    }
}
