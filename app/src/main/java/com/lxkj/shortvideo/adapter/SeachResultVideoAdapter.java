package com.lxkj.shortvideo.adapter;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.lxkj.shortvideo.AppConsts;
import com.lxkj.shortvideo.HcbApp;
import com.lxkj.shortvideo.R;
import com.lxkj.shortvideo.bean.DataListBean;
import com.lxkj.shortvideo.biz.ActivitySwitcher;
import com.lxkj.shortvideo.ui.fragment.shortvideo.VideoFra;
import com.lxkj.shortvideo.ui.tiktok.Utils;
import com.lxkj.shortvideo.utils.SharePrefUtil;
import com.lxkj.shortvideo.widget.component.TikTokView;
import com.makeramen.roundedimageview.RoundedImageView;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;
import cn.jzvd.JZDataSource;
import cn.jzvd.JzvdStd;

/**
 * Time:2021/1/28
 * <p>
 * Author:李迪迦
 * <p>
 * Interface:
 */
public class SeachResultVideoAdapter extends RecyclerView.Adapter<SeachResultVideoAdapter.MyHolder> {
    private Context context;
    private List<DataListBean> list;
    public SeachResultVideoAdapter(Context context, List<DataListBean> list) {
        this.context = context;
        this.list = list;

    }
    @Override
    public SeachResultVideoAdapter.MyHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_seachresulevideo, parent, false);
        return new SeachResultVideoAdapter.MyHolder(view);
    }

    @Override
    public void onBindViewHolder(SeachResultVideoAdapter.MyHolder holder, final int position) {
        holder.tvLaiyuan.setText("来源："+list.get(position).competition.name);
        holder.tvTitle.setText(list.get(position).title);
        holder.tvName.setText(list.get(position).member.nickname);
        holder.tvCommentName.setText(list.get(position).member.nickname);
        holder.tvTime.setText(list.get(position).uploadDate);

        if (list.get(position).collected.equals("1")){
            holder.imShoucang.setImageResource(R.mipmap.yishoucang);
        }else {
            holder.imShoucang.setImageResource(R.mipmap.dianzan);
        }

        LinkedHashMap map = new LinkedHashMap();
        String proxyUrl = HcbApp.getProxy(context).getProxyUrl(list.get(position).video);
        map.put("高清", proxyUrl);
        JZDataSource jzDataSource = new JZDataSource(map, "");
        jzDataSource.looping = true;
        holder.jzVideo.setUp(jzDataSource
                , JzvdStd.SCREEN_NORMAL);

//        holder.jzVideo.startVideo();
        Glide.with(context).applyDefaultRequestOptions(new RequestOptions()
                .error(R.mipmap.imageerror)
                .placeholder(R.mipmap.imageerror))
                .load(list.get(position).video+AppConsts.ViDEOEND)
                .into(holder.jzVideo.thumbImageView);

        holder.jzVideo.titleTextView.setVisibility(View.GONE);
        holder.jzVideo.replayTextView.setVisibility(View.GONE);
        holder.jzVideo.backButton.setVisibility(View.GONE);
        holder.jzVideo.batteryTimeLayout.setVisibility(View.GONE);
        holder.jzVideo.tinyBackImageView.setVisibility(View.GONE);
        holder.jzVideo.videoCurrentTime.setVisibility(View.GONE);
        holder.jzVideo.clarity.setVisibility(View.GONE);
        holder.jzVideo.mRetryLayout.setVisibility(View.GONE);
        holder.jzVideo.mRetryBtn.setVisibility(View.GONE);
        holder.jzVideo.clarity.setVisibility(View.GONE);
        holder.jzVideo.fullscreenButton.setVisibility(View.VISIBLE);
        holder.jzVideo.currentTimeTextView.setVisibility(View.GONE);
        holder.jzVideo.thumbImageView.setVisibility(View.VISIBLE);

        holder.jzVideo.fullscreenButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onItemClickListener.OnBigClickListener(position);
            }
        });

        holder.llDetail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onItemClickListener.OnItemClickListener(position);
            }
        });

        holder.imShoucang.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onItemClickListener.OnShoucangClickListener(position);
            }
        });

        holder.llPinglun.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onItemClickListener.OnPinglunClickListener(position);
            }
        });

        holder.tvLaiyuan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onItemClickListener.OnLaiyuanClickListener(position);
            }
        });


        Glide.with(context).applyDefaultRequestOptions(new RequestOptions()
                .error(R.mipmap.touxiang)
                .placeholder(R.mipmap.touxiang))
                .load(list.get(position).member.avatar)
                .into(holder.riIcon);
    }

    @Override
    public int getItemCount() {

        if (list == null) {
            return 0;
        } else {
            return list.size();
        }
    }


    public class MyHolder extends RecyclerView.ViewHolder {
        TextView tvLaiyuan;
        TextView tvTitle;
        TextView tvName;
        TextView tvTime;
        TextView tvCommentName;
        JzvdStd jzVideo;
        LinearLayout llDetail;
        LinearLayout llPinglun;
        RoundedImageView riIcon;
        ImageView imShoucang;
        ImageView imfenxiang;
        public MyHolder(View itemView) {
            super(itemView);
            tvLaiyuan = itemView.findViewById(R.id.tvLaiyuan);
            tvTitle = itemView.findViewById(R.id.tvTitle);
            jzVideo = itemView.findViewById(R.id.tiktok_View);
            llDetail = itemView.findViewById(R.id.llDetail);
            riIcon = itemView.findViewById(R.id.riIcon);
            tvName = itemView.findViewById(R.id.tvName);
            tvTime = itemView.findViewById(R.id.tvTime);
            llPinglun = itemView.findViewById(R.id.llPinglun);
            tvCommentName = itemView.findViewById(R.id.tvCommentName);
            imShoucang = itemView.findViewById(R.id.imShoucang);
            imfenxiang = itemView.findViewById(R.id.imfenxiang);
        }
    }
    private SeachResultVideoAdapter.OnItemClickListener onItemClickListener;

    public void setOnItemClickListener(SeachResultVideoAdapter.OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    public interface OnItemClickListener {
        void OnItemClickListener(int firstPosition);
        void OnBigClickListener(int firstPosition);
        void OnShoucangClickListener(int firstPosition);
        void OnPinglunClickListener(int firstPosition);
        void OnLaiyuanClickListener(int firstPosition);
    }
}

