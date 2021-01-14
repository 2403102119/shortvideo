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

/**
 * Time:2021/1/14
 * <p>
 * Author:李迪迦
 * <p>
 * Interface:
 */
public class ChatDetailAdapter extends RecyclerView.Adapter<ChatDetailAdapter.MyHolder> {
    private Context context;
    private List<DataListBean> list;
    private List<String> images = new ArrayList<>();
    public ChatDetailAdapter(Context context, List<DataListBean> list) {
        this.context = context;
        this.list = list;

    }
    @Override
    public ChatDetailAdapter.MyHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_chat_detail, parent, false);
        return new ChatDetailAdapter.MyHolder(view);
    }

    @Override
    public void onBindViewHolder(ChatDetailAdapter.MyHolder holder, final int position) {
        Glide.with(context).applyDefaultRequestOptions(new RequestOptions()
                .error(R.mipmap.touxiang)
                .placeholder(R.mipmap.touxiang))
                .load(list.get(position).avatar)
                .into(holder.riIcon);
        holder.tvTime.setText(list.get(position).sendDate);
        holder.tvTitle.setText(list.get(position).nickname);
        holder.tvContent.setText(list.get(position).msgContent+"");

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
        TextView tvContent;
        public MyHolder(View itemView) {
            super(itemView);
            riIcon = itemView.findViewById(R.id.riIcon);
            tvTitle = itemView.findViewById(R.id.tvTitle);
            tvTime = itemView.findViewById(R.id.tvTime);
            tvContent = itemView.findViewById(R.id.tvContent);
        }
    }
    private ChatDetailAdapter.OnItemClickListener onItemClickListener;

    public void setOnItemClickListener(ChatDetailAdapter.OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    public interface OnItemClickListener {
        void OnItemClickListener(int firstPosition);
    }
}

