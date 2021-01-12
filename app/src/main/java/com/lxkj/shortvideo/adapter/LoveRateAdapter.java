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
 * Time:2021/1/6
 * <p>
 * Author:李迪迦
 * <p>
 * Interface:
 */
public class LoveRateAdapter extends RecyclerView.Adapter<LoveRateAdapter.MyHolder> {
    private Context context;
    private List<DataListBean> list;
    public LoveRateAdapter(Context context, List<DataListBean> list) {
        this.context = context;
        this.list = list;

    }
    @Override
    public LoveRateAdapter.MyHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_loverate, parent, false);
        return new LoveRateAdapter.MyHolder(view);
    }

    @Override
    public void onBindViewHolder(LoveRateAdapter.MyHolder holder, final int position) {
        Glide.with(context).applyDefaultRequestOptions(new RequestOptions()
                .error(R.mipmap.touxiang)
                .placeholder(R.mipmap.touxiang))
                .load(list.get(position).avatar)
                .into(holder.riIcon);
        holder.tvName.setText(list.get(position).nickname);
        holder.tvCountTime.setText(list.get(position).duration+"’’");
        holder.tvTime.setText(list.get(position).updateDate);
        if (list.get(position).focused.equals("1")) {
            holder.tvGuanzhu.setText("已关注");
        }else {
            holder.tvGuanzhu.setText("+关注");
        }
        holder.tvGuanzhu.setOnClickListener(new View.OnClickListener() {
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
        RoundedImageView riIcon;
        TextView tvName;
        TextView tvTime;
        TextView tvCountTime;
        TextView tvGuanzhu;
        public MyHolder(View itemView) {
            super(itemView);
            riIcon = itemView.findViewById(R.id.riIcon);
            tvName = itemView.findViewById(R.id.tvName);
            tvTime = itemView.findViewById(R.id.tvTime);
            tvCountTime = itemView.findViewById(R.id.tvCountTime);
            tvGuanzhu = itemView.findViewById(R.id.tvGuanzhu);
        }
    }
    private LoveRateAdapter.OnItemClickListener onItemClickListener;

    public void setOnItemClickListener(LoveRateAdapter.OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    public interface OnItemClickListener {
        void OnItemClickListener(int firstPosition);
    }
}


