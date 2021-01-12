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
        Glide.with(context).applyDefaultRequestOptions(new RequestOptions()
                .error(R.mipmap.touxiang)
                .placeholder(R.mipmap.touxiang))
                .load(list.get(position).avatar)
                .into(holder.riIcon);
        holder.tvName.setText(list.get(position).nickname);
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
        TextView tvGuanzhu;
        TextView tvName;
        RoundedImageView riIcon;
        public MyHolder(View itemView) {
            super(itemView);
            tvGuanzhu = itemView.findViewById(R.id.tvGuanzhu);
            riIcon = itemView.findViewById(R.id.riIcon);
            tvName = itemView.findViewById(R.id.tvName);
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
