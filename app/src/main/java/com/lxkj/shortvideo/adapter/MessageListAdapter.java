package com.lxkj.shortvideo.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.lxkj.shortvideo.R;
import com.lxkj.shortvideo.bean.DataListBean;
import com.lxkj.shortvideo.utils.StringUtil;
import com.makeramen.roundedimageview.RoundedImageView;

import java.util.List;

import androidx.recyclerview.widget.RecyclerView;

/**
 * Time:2020/9/30
 * <p>
 * Author:李迪迦
 * <p>
 * Interface:消息列表
 */
public class MessageListAdapter extends RecyclerView.Adapter<MessageListAdapter.MyHolder> {
    private Context context;
    private List<DataListBean> list;
    public MessageListAdapter(Context context, List<DataListBean> list) {
        this.context = context;
        this.list = list;

    }
    @Override
    public MessageListAdapter.MyHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_messagelist, parent, false);
        return new MessageListAdapter.MyHolder(view);
    }

    @Override
    public void onBindViewHolder(MessageListAdapter.MyHolder holder, final int position) {
        holder.tvTitle.setText(list.get(position).nick);
        holder.tvCreateDate.setText(list.get(position).chatDate);
        holder.tvContent.setText(list.get(position).content);
        holder.tvUnreadCount.setText(list.get(position).unreadCount);
        if (StringUtil.isEmpty(list.get(position).unreadCount)||list.get(position).unreadCount.equals("0")){
            holder.tvUnreadCount.setVisibility(View.GONE);
        }else {
            holder.tvUnreadCount.setVisibility(View.VISIBLE);
        }
        Glide.with(context).applyDefaultRequestOptions(new RequestOptions()
                .placeholder(R.mipmap.logo)
                .error(R.mipmap.logo))
                .load(list.get(position).avatar)
                .into(holder.imAvatar);
        holder.ll_item.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onItemClickListener.OnItemClickListener(position);
            }
        });
        holder.btnDel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onItemClickListener.OnDelateClickListener(position);
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
        private TextView tvTitle,tvCreateDate,btnDel,tvContent,tvUnreadCount;
        private LinearLayout ll_item;
        private RoundedImageView imAvatar;
        public MyHolder(View itemView) {
            super(itemView);
            tvTitle = itemView.findViewById(R.id.tvTitle);
            tvCreateDate = itemView.findViewById(R.id.tvCreateDate);
            imAvatar = itemView.findViewById(R.id.imAvatar);
            tvUnreadCount = itemView.findViewById(R.id.tvUnreadCount);
            ll_item = itemView.findViewById(R.id.ll_item);
            btnDel = itemView.findViewById(R.id.btnDel);
            tvContent = itemView.findViewById(R.id.tvContent);
        }
    }
    private MessageListAdapter.OnItemClickListener onItemClickListener;

    public void setOnItemClickListener(MessageListAdapter.OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    public interface OnItemClickListener {
        void OnItemClickListener(int firstPosition);
        void OnDelateClickListener(int firstPosition);
    }
}
