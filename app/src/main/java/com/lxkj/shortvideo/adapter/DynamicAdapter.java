package com.lxkj.shortvideo.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.lxkj.shortvideo.AppConsts;
import com.lxkj.shortvideo.R;
import com.lxkj.shortvideo.bean.DataListBean;
import com.lxkj.shortvideo.utils.SharePrefUtil;
import com.makeramen.roundedimageview.RoundedImageView;

import java.util.ArrayList;
import java.util.List;

import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

/**
 * Time:2020/12/30
 * <p>
 * Author:李迪迦
 * <p>
 * Interface:
 */
public class DynamicAdapter extends RecyclerView.Adapter<DynamicAdapter.MyHolder> {
    private Context context;
    private List<DataListBean> list;
    private List<String> images = new ArrayList<>();
    public DynamicAdapter(Context context, List<DataListBean> list) {
        this.context = context;
        this.list = list;

    }
    @Override
    public DynamicAdapter.MyHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_dynamic, parent, false);
        return new DynamicAdapter.MyHolder(view);
    }

    @Override
    public void onBindViewHolder(DynamicAdapter.MyHolder holder, final int position) {

        Glide.with(context).applyDefaultRequestOptions(new RequestOptions()
                .error(R.mipmap.imageerror)
                .placeholder(R.mipmap.imageerror))
                .load(list.get(position).member.avatar)
                .into(holder.riIcon);
        holder.tvTitle.setText(list.get(position).member.nickname);
        holder.tvTime.setText(list.get(position).createDate);
        holder.tvConten.setText(list.get(position).content);
        holder.tvcommentCount.setText(list.get(position).commentCount);
        holder.tvcollectCount.setText(list.get(position).collectCount);
        holder.tvshareCount.setText(list.get(position).shareCount);
        if (SharePrefUtil.getString(context, AppConsts.UID,null).equals(list.get(position).member.id)){
            holder.tvGuanzhu.setVisibility(View.GONE);
        }else {
            holder.tvGuanzhu.setVisibility(View.VISIBLE);
        }
        if (list.get(position).focused.equals("1")){
            if (list.get(position).beFocused.equals("1")){
                holder.tvGuanzhu.setText("互相关注");
            }else {
                holder.tvGuanzhu.setText("已关注");
            }
        }else {
            holder.tvGuanzhu.setText("+关注");
        }
        if (list.get(position).collected.equals("1")){
            holder.imShoucang.setImageResource(R.mipmap.yishoucang);
        }else {
            holder.imShoucang.setImageResource(R.mipmap.dianzan);
        }


        holder.tvGuanzhu.setOnClickListener(new View.OnClickListener() {
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

        holder.imfenxiang.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onItemClickListener.OnFenxiangClickListener(position);
            }
        });

        StaggeredGridLayoutManager layoutManager = new StaggeredGridLayoutManager(3, StaggeredGridLayoutManager.VERTICAL);
        holder.recyclerView.setLayoutManager(layoutManager);
        Recycle_one_itemAdapter recycletwoItemAdapter=new Recycle_one_itemAdapter(context,list.get(position).images);
        holder.recyclerView.setAdapter(recycletwoItemAdapter);
        recycletwoItemAdapter.setOnItemClickListener(new Recycle_one_itemAdapter.OnItemClickListener() {
            @Override
            public void OnItemClickListener(int firstPosition) {
//                onItemClickListener.Onchakandatu(firstPosition,position);
            }
        });

        recycletwoItemAdapter.notifyDataSetChanged();
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
        RecyclerView recyclerView;
        RoundedImageView riIcon;
        TextView tvTitle;
        TextView tvTime;
        TextView tvConten;
        TextView tvcommentCount;
        TextView tvcollectCount;
        TextView tvshareCount;
        TextView tvGuanzhu;
        ImageView imShoucang;
        ImageView imfenxiang;
        public MyHolder(View itemView) {
            super(itemView);
            recyclerView = itemView.findViewById(R.id.recyclerView);
            riIcon = itemView.findViewById(R.id.riIcon);
            tvTitle = itemView.findViewById(R.id.tvTitle);
            tvTime = itemView.findViewById(R.id.tvTime);
            tvConten = itemView.findViewById(R.id.tvConten);
            tvcommentCount = itemView.findViewById(R.id.tvcommentCount);
            tvcollectCount = itemView.findViewById(R.id.tvcollectCount);
            tvshareCount = itemView.findViewById(R.id.tvshareCount);
            tvGuanzhu = itemView.findViewById(R.id.tvGuanzhu);
            imShoucang = itemView.findViewById(R.id.imShoucang);
            imfenxiang = itemView.findViewById(R.id.imfenxiang);
        }
    }
    private DynamicAdapter.OnItemClickListener onItemClickListener;

    public void setOnItemClickListener(DynamicAdapter.OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    public interface OnItemClickListener {
        void OnItemClickListener(int firstPosition);
        void OnDetailClickListener(int firstPosition);
        void OnFenxiangClickListener(int firstPosition);
    }
}


