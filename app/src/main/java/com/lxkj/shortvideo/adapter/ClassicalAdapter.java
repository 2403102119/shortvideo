package com.lxkj.shortvideo.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.lxkj.shortvideo.R;
import com.lxkj.shortvideo.bean.DataListBean;
import com.makeramen.roundedimageview.RoundedImageView;

import java.util.List;

import androidx.recyclerview.widget.RecyclerView;

/**
 * Time:2020/12/30
 * <p>
 * Author:李迪迦
 * <p>
 * Interface:
 */
public class ClassicalAdapter extends RecyclerView.Adapter<ClassicalAdapter.MyHolder> {
    private Context context;
    private List<DataListBean> list;
    public ClassicalAdapter(Context context, List<DataListBean> list) {
        this.context = context;
        this.list = list;

    }
    @Override
    public ClassicalAdapter.MyHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_classical, parent, false);
        return new ClassicalAdapter.MyHolder(view);
    }

    @Override
    public void onBindViewHolder(ClassicalAdapter.MyHolder holder, final int position) {
        Glide.with(context).applyDefaultRequestOptions(new RequestOptions()
                .error(R.mipmap.imageerror)
                .placeholder(R.mipmap.imageerror))
                .load(list.get(position).coverImage)
                .into(holder.riIcon);
        holder.tvTitle.setText(list.get(position).title);
        holder.tvTime.setText(list.get(position).uploadDate);
        holder.tvNumber.setText(list.get(position).collectCount);
        if (list.get(position).collected.equals("1")){
            holder.inShoucang.setImageResource(R.mipmap.yishoucang);
        }else {
            holder.inShoucang.setImageResource(R.mipmap.dianzan);
        }
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onItemClickListener.OnItemClickListener(position);
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
        TextView tvTitle;
        TextView tvTime;
        TextView tvNumber;
        ImageView inShoucang;
        public MyHolder(View itemView) {
            super(itemView);
            riIcon = itemView.findViewById(R.id.riIcon);
            tvTitle = itemView.findViewById(R.id.tvTitle);
            tvTime = itemView.findViewById(R.id.tvTime);
            tvNumber = itemView.findViewById(R.id.tvNumber);
            inShoucang = itemView.findViewById(R.id.inShoucang);
        }
    }
    private ClassicalAdapter.OnItemClickListener onItemClickListener;

    public void setOnItemClickListener(ClassicalAdapter.OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    public interface OnItemClickListener {
        void OnItemClickListener(int firstPosition);
    }
}

