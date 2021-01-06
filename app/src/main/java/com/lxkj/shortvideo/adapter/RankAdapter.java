package com.lxkj.shortvideo.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.lxkj.shortvideo.R;
import com.lxkj.shortvideo.bean.DataListBean;

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
        holder.llLike.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onItemClickListener.OnItemClickListener(position);
            }
        });

    }

    @Override
    public int getItemCount() {

//        if (list == null) {
//            return 0;
//        } else {
//            return list.size();
//        }
        return 8;
    }


    public class MyHolder extends RecyclerView.ViewHolder {
        LinearLayout llLike;
        public MyHolder(View itemView) {
            super(itemView);
            llLike = itemView.findViewById(R.id.llLike);
        }
    }
    private RankAdapter.OnItemClickListener onItemClickListener;

    public void setOnItemClickListener(RankAdapter.OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    public interface OnItemClickListener {
        void OnItemClickListener(int firstPosition);
    }
}


