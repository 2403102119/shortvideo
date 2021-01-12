package com.lxkj.shortvideo.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.lxkj.shortvideo.HcbApp;
import com.lxkj.shortvideo.R;
import com.lxkj.shortvideo.bean.DataListBean;
import com.makeramen.roundedimageview.RoundedImageView;

import java.util.LinkedHashMap;
import java.util.List;

import androidx.recyclerview.widget.RecyclerView;
import cn.jzvd.JZDataSource;
import cn.jzvd.JzvdStd;

/**
 * Time:2020/12/29
 * <p>
 * Author:李迪迦
 * <p>
 * Interface:
 */
public class ShortVideoAdapter extends RecyclerView.Adapter<ShortVideoAdapter.MyHolder> {
    private Context context;
    private List<DataListBean> list;
    public ShortVideoAdapter(Context context, List<DataListBean> list) {
        this.context = context;
        this.list = list;

    }
    @Override
    public ShortVideoAdapter.MyHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_shortvideo, parent, false);
        return new ShortVideoAdapter.MyHolder(view);
    }

    @Override
    public void onBindViewHolder(ShortVideoAdapter.MyHolder holder, final int position) {
        Glide.with(context).applyDefaultRequestOptions(new RequestOptions()
                .error(R.mipmap.touxiang)
                .placeholder(R.mipmap.touxiang))
                .load(list.get(position).member.avatar)
                .into(holder.riIcon);
        holder.tvName.setText(list.get(position).member.nickname);
        holder.tvTitle.setText(list.get(position).title);
        holder.tvcommentCount.setText(list.get(position).commentCount);
        holder.tvcollectCount.setText(list.get(position).collectCount);
        holder.tvshareCount.setText(list.get(position).shareCount);
        holder.tvcompetitionName.setText("来源："+list.get(position).competition.name+">");

        LinkedHashMap map = new LinkedHashMap();
        String proxyUrl = HcbApp.getProxy(context).getProxyUrl(list.get(position).video);
        map.put("高清", proxyUrl);
        JZDataSource jzDataSource = new JZDataSource(map, "");
        jzDataSource.looping = true;
        holder.jzVideo.setUp(jzDataSource
                , JzvdStd.SCREEN_NORMAL);
        Glide.with(context).applyDefaultRequestOptions(new RequestOptions()
                .error(R.mipmap.imageerror)
                .placeholder(R.mipmap.imageerror))
                .load(list.get(position).coverImage)
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
        holder.jzVideo.fullscreenButton.setVisibility(View.GONE);
        holder.jzVideo.currentTimeTextView.setVisibility(View.GONE);
        holder.jzVideo.thumbImageView.setVisibility(View.VISIBLE);

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onItemClickListener.OnItemClickListener(position);
            }
        });

        holder.tvcompetitionName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onItemClickListener.OnBigClickListener(position);
            }
        });

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
        RoundedImageView riIcon;
        TextView tvName;
        TextView tvcompetitionName;
        TextView tvTitle;
        TextView tvcommentCount;
        TextView tvcollectCount;
        TextView tvshareCount;
        JzvdStd jzVideo;
        public MyHolder(View itemView) {
            super(itemView);
            riIcon = itemView.findViewById(R.id.riIcon);
            tvName = itemView.findViewById(R.id.tvName);
            tvcompetitionName = itemView.findViewById(R.id.tvcompetitionName);
            tvTitle = itemView.findViewById(R.id.tvTitle);
            tvcommentCount = itemView.findViewById(R.id.tvcommentCount);
            tvcollectCount = itemView.findViewById(R.id.tvcollectCount);
            tvshareCount = itemView.findViewById(R.id.tvshareCount);
            jzVideo = itemView.findViewById(R.id.jzVideo);
        }
    }
    private ShortVideoAdapter.OnItemClickListener onItemClickListener;

    public void setOnItemClickListener(ShortVideoAdapter.OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    public interface OnItemClickListener {
        void OnItemClickListener(int firstPosition);
        void OnBigClickListener(int firstPosition);
    }
}

