package com.lxkj.shortvideo.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.lxkj.shortvideo.R;
import com.lxkj.shortvideo.bean.DataListBean;
import com.makeramen.roundedimageview.RoundedImageView;

import java.util.ArrayList;
import java.util.List;

import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

/**
 * Time:2021/1/4
 * <p>
 * Author:李迪迦
 * <p>
 * Interface:
 */
public class HomeDynamicAdapter extends RecyclerView.Adapter<HomeDynamicAdapter.MyHolder> {
    private Context context;
    private List<DataListBean> list;
    public HomeDynamicAdapter(Context context, List<DataListBean> list) {
        this.context = context;
        this.list = list;

    }
    @Override
    public HomeDynamicAdapter.MyHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_dynamic, parent, false);
        return new HomeDynamicAdapter.MyHolder(view);
    }

    @Override
    public void onBindViewHolder(HomeDynamicAdapter.MyHolder holder, final int position) {

        Glide.with(context).applyDefaultRequestOptions(new RequestOptions()
                .error(R.mipmap.touxiang)
                .placeholder(R.mipmap.touxiang))
                .load(list.get(position).member.avatar)
                .into(holder.riIcon);

        holder.tvTitle.setText(list.get(position).member.nickname);
        holder.tvTime.setText(list.get(position).createDate);
        holder.tvConten.setText(list.get(position).content);
        holder.tvcommentCount.setText(list.get(position).commentCount);
        holder.tvcollectCount.setText(list.get(position).collectCount);
        holder.tvshareCount.setText(list.get(position).shareCount);

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

        holder.tvGuanzhu.setVisibility(View.GONE);

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
        TextView tvGuanzhu;
        TextView tvTitle;
        TextView tvConten;
        TextView tvTime;
        TextView tvcollectCount;
        TextView tvcommentCount;
        TextView tvshareCount;
        RoundedImageView riIcon;
        public MyHolder(View itemView) {
            super(itemView);
            recyclerView = itemView.findViewById(R.id.recyclerView);
            tvGuanzhu = itemView.findViewById(R.id.tvGuanzhu);
            riIcon = itemView.findViewById(R.id.riIcon);
            tvTitle = itemView.findViewById(R.id.tvTitle);
            tvTime = itemView.findViewById(R.id.tvTime);
            tvConten = itemView.findViewById(R.id.tvConten);
            tvcommentCount = itemView.findViewById(R.id.tvcommentCount);
            tvcollectCount = itemView.findViewById(R.id.tvcollectCount);
            tvshareCount = itemView.findViewById(R.id.tvshareCount);
        }
    }
    private HomeDynamicAdapter.OnItemClickListener onItemClickListener;

    public void setOnItemClickListener(HomeDynamicAdapter.OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    public interface OnItemClickListener {
        void OnItemClickListener(int firstPosition);
    }
}

