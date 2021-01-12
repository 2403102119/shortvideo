package com.lxkj.shortvideo.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
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

/**
 * Time:2021/1/5
 * <p>
 * Author:李迪迦
 * <p>
 * Interface:
 */
public class CommentItemAdapter extends RecyclerView.Adapter<CommentItemAdapter.MyHolder> {
    private Context context;
    private List<DataListBean.SubCommentList> list;
    public CommentItemAdapter(Context context, List<DataListBean.SubCommentList> list) {
        this.context = context;
        this.list = list;

    }
    @Override
    public CommentItemAdapter.MyHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_comment, parent, false);
        return new CommentItemAdapter.MyHolder(view);
    }

    @Override
    public void onBindViewHolder(CommentItemAdapter.MyHolder holder, final int position) {
        holder.llComment.setVisibility(View.GONE);
        Glide.with(context).applyDefaultRequestOptions(new RequestOptions()
                .error(R.mipmap.touxiang)
                .placeholder(R.mipmap.touxiang))
                .load(list.get(position).member.avatar)
                .into(holder.ri_icon);
        holder.tv_name.setText(list.get(position).member.nickname);
        holder.tv_time.setText(list.get(position).createDate);
        holder.tv_content.setText(list.get(position).content);
        holder.tvDianzan.setText(list.get(position).likedCount);
        if (list.get(position).liked.equals("1")){
            holder.imDianzan.setImageResource(R.mipmap.yidianzan);
        }else {
            holder.imDianzan.setImageResource(R.mipmap.weidianzan);
        }
        holder.imDianzan.setOnClickListener(new View.OnClickListener() {
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
        LinearLayout llComment;
        RoundedImageView ri_icon;
        TextView tv_name;
        TextView tv_time;
        TextView tv_content;
        TextView tvDianzan;
        TextView imsubCommentList;
        ImageView imDianzan;
        public MyHolder(View itemView) {
            super(itemView);
            llComment = itemView.findViewById(R.id.llComment);
            ri_icon = itemView.findViewById(R.id.ri_icon);
            tv_name = itemView.findViewById(R.id.tv_name);
            tv_time = itemView.findViewById(R.id.tv_time);
            tv_content = itemView.findViewById(R.id.tv_content);
            imDianzan = itemView.findViewById(R.id.imDianzan);
            tvDianzan = itemView.findViewById(R.id.tvDianzan);
            imsubCommentList = itemView.findViewById(R.id.imsubCommentList);
        }
    }
    private CommentItemAdapter.OnItemClickListener onItemClickListener;

    public void setOnItemClickListener(CommentItemAdapter.OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    public interface OnItemClickListener {
        void OnItemClickListener(int firstPosition);
    }
}
