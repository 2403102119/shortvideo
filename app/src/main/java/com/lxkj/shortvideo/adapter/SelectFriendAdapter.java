package com.lxkj.shortvideo.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.lxkj.shortvideo.R;
import com.lxkj.shortvideo.bean.DataListBean;

import java.util.List;

import androidx.recyclerview.widget.RecyclerView;

/**
 * Time:2021/1/5
 * <p>
 * Author:李迪迦
 * <p>
 * Interface:
 */
public class SelectFriendAdapter extends RecyclerView.Adapter<SelectFriendAdapter.MyHolder> {
    private Context context;
    private List<DataListBean> list;
    public SelectFriendAdapter(Context context, List<DataListBean> list) {
        this.context = context;
        this.list = list;

    }
    @Override
    public SelectFriendAdapter.MyHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_attentionlist, parent, false);
        return new SelectFriendAdapter.MyHolder(view);
    }

    @Override
    public void onBindViewHolder(SelectFriendAdapter.MyHolder holder, final int position) {
           holder.tvGuanzhu.setVisibility(View.GONE);
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
        TextView tvGuanzhu;
        public MyHolder(View itemView) {
            super(itemView);
            tvGuanzhu = itemView.findViewById(R.id.tvGuanzhu);
        }
    }
    private SelectFriendAdapter.OnItemClickListener onItemClickListener;

    public void setOnItemClickListener(SelectFriendAdapter.OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    public interface OnItemClickListener {
        void OnItemClickListener(int firstPosition);
    }
}
