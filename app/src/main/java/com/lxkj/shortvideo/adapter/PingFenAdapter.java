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

import java.util.ArrayList;
import java.util.List;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import me.zhanghai.android.materialratingbar.MaterialRatingBar;

/**
 * Time:2021/1/25
 * <p>
 * Author:李迪迦
 * <p>
 * Interface:
 */
public class PingFenAdapter extends RecyclerView.Adapter<PingFenAdapter.MyHolder> {
    private Context context;
    private List<DataListBean> list;

    public PingFenAdapter(Context context, List<DataListBean> list) {
        this.context = context;
        this.list = list;

    }
    @Override
    public PingFenAdapter.MyHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_pingfen, parent, false);
        return new PingFenAdapter.MyHolder(view);
    }

    @Override
    public void onBindViewHolder(PingFenAdapter.MyHolder holder, final int position) {
        holder.tvTitle.setText(list.get(position).name);

        holder.mr_score.setOnRatingChangeListener(new MaterialRatingBar.OnRatingChangeListener() {
            @Override
            public void onRatingChanged(MaterialRatingBar ratingBar, float rating) {
              onItemClickListener.OnItemClickListener(position,rating+"");
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
        TextView tvTitle;
        MaterialRatingBar mr_score;
        public MyHolder(View itemView) {
            super(itemView);
            tvTitle = itemView.findViewById(R.id.tvTitle);
            mr_score = itemView.findViewById(R.id.mr_score);
        }
    }
    private PingFenAdapter.OnItemClickListener onItemClickListener;

    public void setOnItemClickListener(PingFenAdapter.OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    public interface OnItemClickListener {
        void OnItemClickListener(int firstPosition,String rating);
    }
}

