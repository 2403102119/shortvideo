package com.lxkj.shortvideo.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.lxkj.shortvideo.R;
import com.lxkj.shortvideo.bean.DataListBean;

import java.util.ArrayList;
import java.util.List;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

/**
 * Time:2021/1/5
 * <p>
 * Author:李迪迦
 * <p>
 * Interface:
 */
public class CommentAdapter extends RecyclerView.Adapter<CommentAdapter.MyHolder> {
    private Context context;
    private List<DataListBean> list;
    private CommentItemAdapter commentItemAdapter;
    public CommentAdapter(Context context, List<DataListBean> list) {
        this.context = context;
        this.list = list;

    }
    @Override
    public CommentAdapter.MyHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_comment, parent, false);
        return new CommentAdapter.MyHolder(view);
    }

    @Override
    public void onBindViewHolder(CommentAdapter.MyHolder holder, final int position) {
        list = new ArrayList<DataListBean>();
        LinearLayoutManager layoutManager = new LinearLayoutManager(context);
        holder.recyclerView.setLayoutManager(layoutManager);
        commentItemAdapter = new CommentItemAdapter(context, list);
        holder.recyclerView.setAdapter(commentItemAdapter);
        commentItemAdapter.setOnItemClickListener(new CommentItemAdapter.OnItemClickListener() {
            @Override
            public void OnItemClickListener(int firstPosition) {
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
        RecyclerView recyclerView;
        public MyHolder(View itemView) {
            super(itemView);
            recyclerView = itemView.findViewById(R.id.recyclerView);
        }
    }
    private CommentAdapter.OnItemClickListener onItemClickListener;

    public void setOnItemClickListener(CommentAdapter.OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    public interface OnItemClickListener {
        void OnItemClickListener(int firstPosition);
    }
}

