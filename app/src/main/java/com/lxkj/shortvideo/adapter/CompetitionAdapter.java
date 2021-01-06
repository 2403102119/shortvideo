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
import com.mcxtzhang.swipemenulib.SwipeMenuLayout;

import org.w3c.dom.Text;

import java.util.List;

import androidx.recyclerview.widget.RecyclerView;

/**
 * Time:2020/12/29
 * <p>
 * Author:李迪迦
 * <p>
 * Interface:
 */
public class CompetitionAdapter extends RecyclerView.Adapter<CompetitionAdapter.MyHolder> {
    private Context context;
    private List<DataListBean> list;
    public CompetitionAdapter(Context context, List<DataListBean> list) {
        this.context = context;
        this.list = list;

    }
    @Override
    public CompetitionAdapter.MyHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_competition, parent, false);
        return new CompetitionAdapter.MyHolder(view);
    }

    @Override
    public void onBindViewHolder(CompetitionAdapter.MyHolder holder, final int position) {
        Glide.with(context).applyDefaultRequestOptions(new RequestOptions()
                .error(R.mipmap.imageerror)
                .placeholder(R.mipmap.imageerror))
                .load(list.get(position).image)
                .into(holder.riIcon);
        holder.tvTitle.setText(list.get(position).name);
        holder.tvTime.setText("截止时间："+list.get(position).enterEndDate);
        holder.tvNumber.setText(list.get(position).enterCount+"人参赛");
        if (list.get(position).competitionState.equals("1")){
            holder.tvState.setText("进行中");
            holder.tvState.setBackgroundResource(R.drawable.login_20);
        }else {
            holder.tvState.setText("已结束");
            holder.tvState.setBackgroundResource(R.drawable.chakanxiangqing);
        }

        holder.itemView.setOnClickListener(new View.OnClickListener() {
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
        TextView tvTitle,tvTime,tvNumber,tvState;
        public MyHolder(View itemView) {
            super(itemView);
            riIcon = itemView.findViewById(R.id.riIcon);
            tvTitle = itemView.findViewById(R.id.tvTitle);
            tvTime = itemView.findViewById(R.id.tvTime);
            tvNumber = itemView.findViewById(R.id.tvNumber);
            tvState = itemView.findViewById(R.id.tvState);
        }
    }
    private CompetitionAdapter.OnItemClickListener onItemClickListener;

    public void setOnItemClickListener(CompetitionAdapter.OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    public interface OnItemClickListener {
        void OnItemClickListener(int firstPosition);
    }
}
