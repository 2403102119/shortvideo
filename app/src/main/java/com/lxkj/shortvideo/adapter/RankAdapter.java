package com.lxkj.shortvideo.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.lxkj.shortvideo.AppConsts;
import com.lxkj.shortvideo.R;
import com.lxkj.shortvideo.bean.DataListBean;
import com.lxkj.shortvideo.utils.StringUtil;
import com.makeramen.roundedimageview.RoundedImageView;

import java.util.List;

import androidx.recyclerview.widget.RecyclerView;

/**
 * Time:2021/1/6
 * <p>
 * Author:李迪迦
 * <p>
 * Interface:
 */
public class RankAdapter extends RecyclerView.Adapter<RankAdapter.MyHolder> {
    private Context context;
    private List<DataListBean> list;
    public RankAdapter(Context context, List<DataListBean> list) {
        this.context = context;
        this.list = list;

    }
    @Override
    public RankAdapter.MyHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_rank, parent, false);
        return new RankAdapter.MyHolder(view);
    }

    @Override
    public void onBindViewHolder(RankAdapter.MyHolder holder, final int position) {
       if (StringUtil.isEmpty(list.get(position).coverImage)){
           Glide.with(context).applyDefaultRequestOptions(new RequestOptions()
                   .error(R.mipmap.imageerror)
                   .placeholder(R.mipmap.imageerror))
                   .load(list.get(position).video+ AppConsts.ViDEOEND)
                   .into(holder.riIcon);
       }else {
           Glide.with(context).applyDefaultRequestOptions(new RequestOptions()
                   .error(R.mipmap.imageerror)
                   .placeholder(R.mipmap.imageerror))
                   .load(list.get(position).coverImage)
                   .into(holder.riIcon);
       }

        holder.tvNumber.setText((position+1)+"");
        holder.tvTitle.setText(list.get(position).title);
        holder.tvTime.setText(list.get(position).uploadDate);
        holder.tvCanping.setText(list.get(position).passNgCount);
        holder.tvCanping.setText(list.get(position).passNgCount);
        holder.tvXiAi.setText((Double.parseDouble(list.get(position).passRatio)*100)+"%");
        holder.tvXihuan.setText(list.get(position).collectCount);

        if (list.get(position).collected.equals("1")){
            holder.imXihuan.setImageResource(R.mipmap.yishoucang);
        }else {
            holder.imXihuan.setImageResource(R.mipmap.dianzan);
        }
        if (position==0){
            holder.imJiao.setImageResource(R.mipmap.rank1);
        }else if (position==1){
            holder.imJiao.setImageResource(R.mipmap.rank2);
        }else if (position == 2){
            holder.imJiao.setImageResource(R.mipmap.rank3);
        }else {
            holder.imJiao.setImageResource(R.mipmap.rank1);
        }

        holder.tvName.setText("作者："+list.get(position).member.nickname);
        holder.llLike.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onItemClickListener.OnItemClickListener(position);
            }
        });

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onItemClickListener.OnDetailClickListener(position);
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
        LinearLayout llLike;
        RoundedImageView riIcon;
        TextView tvNumber;
        TextView tvTitle;
        TextView tvTime;
        TextView tvCanping;
        TextView tvName;
        TextView tvXiAi;
        TextView tvXihuan;
        ImageView imXihuan;
        ImageView imJiao;
        public MyHolder(View itemView) {
            super(itemView);
            llLike = itemView.findViewById(R.id.llLike);
            riIcon = itemView.findViewById(R.id.riIcon);
            tvNumber = itemView.findViewById(R.id.tvNumber);
            tvTitle = itemView.findViewById(R.id.tvTitle);
            tvTime = itemView.findViewById(R.id.tvTime);
            tvName = itemView.findViewById(R.id.tvName);
            tvCanping = itemView.findViewById(R.id.tvCanping);
            tvXiAi = itemView.findViewById(R.id.tvXiAi);
            tvXihuan = itemView.findViewById(R.id.tvXihuan);
            imXihuan = itemView.findViewById(R.id.imXihuan);
            imJiao = itemView.findViewById(R.id.imJiao);
        }
    }
    private RankAdapter.OnItemClickListener onItemClickListener;

    public void setOnItemClickListener(RankAdapter.OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    public interface OnItemClickListener {
        void OnItemClickListener(int firstPosition);
        void OnDetailClickListener(int firstPosition);
    }
}


