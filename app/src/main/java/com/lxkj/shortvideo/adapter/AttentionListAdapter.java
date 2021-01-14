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
import com.makeramen.roundedimageview.RoundedImageView;

import java.util.List;

import androidx.recyclerview.widget.RecyclerView;

/**
 * Time:2021/1/4
 * <p>
 * Author:李迪迦
 * <p>
 * Interface:
 */
public class AttentionListAdapter extends RecyclerView.Adapter<AttentionListAdapter.MyHolder> {
    private Context context;
    private List<DataListBean> list;
    public AttentionListAdapter(Context context, List<DataListBean> list) {
        this.context = context;
        this.list = list;

    }
    @Override
    public AttentionListAdapter.MyHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_attentionlist, parent, false);
        return new AttentionListAdapter.MyHolder(view);
    }

    @Override
    public void onBindViewHolder(AttentionListAdapter.MyHolder holder, final int position) {
        Glide.with(context).applyDefaultRequestOptions(new RequestOptions()
                .error(R.mipmap.touxiang)
                .placeholder(R.mipmap.touxiang))
                .load(list.get(position).avatar)
                .into(holder.riIcon);
        holder.tvName.setText(list.get(position).nickname);
        if (list.get(position).focused.equals("1")){
            if (list.get(position).beFocused.equals("1")){
                holder.tvGuanzhu.setText("互相关注");
                holder.llSixin.setVisibility(View.VISIBLE);
            }else {
                holder.tvGuanzhu.setText("已关注");
            }
        }else {
            holder.tvGuanzhu.setText("+关注");
        }

        holder.tvGuanzhu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onItemClickListener.OnItemClickListener(position);
            }
        });
        holder.llSixin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onItemClickListener.OnSixinClickListener(position);
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
        TextView tvName;
        TextView tvGuanzhu;
        LinearLayout llSixin;
        public MyHolder(View itemView) {
            super(itemView);
            riIcon = itemView.findViewById(R.id.riIcon);
            tvName = itemView.findViewById(R.id.tvName);
            tvGuanzhu = itemView.findViewById(R.id.tvGuanzhu);
            llSixin = itemView.findViewById(R.id.llSixin);
        }
    }
    private AttentionListAdapter.OnItemClickListener onItemClickListener;

    public void setOnItemClickListener(AttentionListAdapter.OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    public interface OnItemClickListener {
        void OnItemClickListener(int firstPosition);
        void OnSixinClickListener(int firstPosition);
    }
}
