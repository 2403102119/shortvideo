package com.lxkj.shortvideo.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.lxkj.shortvideo.R;
import com.lxkj.shortvideo.bean.DataListBean;

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
    private List<String> images = new ArrayList<>();
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
        StaggeredGridLayoutManager layoutManager = new StaggeredGridLayoutManager(3, StaggeredGridLayoutManager.VERTICAL);
        holder.recyclerView.setLayoutManager(layoutManager);
        Recycle_one_itemAdapter recycletwoItemAdapter=new Recycle_one_itemAdapter(context,images);
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

//        if (list == null) {
//            return 0;
//        } else {
//            return list.size();
//        }
        return 8;
    }


    public class MyHolder extends RecyclerView.ViewHolder {
        RecyclerView recyclerView;
        public MyHolder(View itemView) {
            super(itemView);
            recyclerView = itemView.findViewById(R.id.recyclerView);
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
