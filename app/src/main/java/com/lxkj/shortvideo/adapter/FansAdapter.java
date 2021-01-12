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
 * Time:2021/1/4
 * <p>
 * Author:李迪迦
 * <p>
 * Interface:
 */
public class FansAdapter extends RecyclerView.Adapter<FansAdapter.MyHolder> {
    private Context context;
    private List<DataListBean> list;
    public FansAdapter(Context context, List<DataListBean> list) {
        this.context = context;
        this.list = list;

    }
    @Override
    public FansAdapter.MyHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_fans, parent, false);
        return new FansAdapter.MyHolder(view);
    }

    @Override
    public void onBindViewHolder(FansAdapter.MyHolder holder, final int position) {
        Glide.with(context).applyDefaultRequestOptions(new RequestOptions()
                .error(R.mipmap.touxiang)
                .placeholder(R.mipmap.touxiang))
                .load(list.get(position).avatar)
                .into(holder.riIcon);
        holder.tvTitle.setText(list.get(position).nickname);
        holder.tvTime.setText(list.get(position).createDate);
        if (list.get(position).focused.equals("1")){
           if (list.get(position).beFocused.equals("1")){
               holder.tvGuanzhu.setText("互相关注");
           }else {
               holder.tvGuanzhu.setText("已关注");
           }
        }else {
            holder.tvGuanzhu.setText("+关注");
        }
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
        TextView tvGuanzhu;
        public MyHolder(View itemView) {
            super(itemView);
            riIcon = itemView.findViewById(R.id.riIcon);
            tvTitle = itemView.findViewById(R.id.tvTitle);
            tvTime = itemView.findViewById(R.id.tvTime);
            tvGuanzhu = itemView.findViewById(R.id.tvGuanzhu);
        }
    }
    private FansAdapter.OnItemClickListener onItemClickListener;

    public void setOnItemClickListener(FansAdapter.OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    public interface OnItemClickListener {
        void OnItemClickListener(int firstPosition);
    }
}

