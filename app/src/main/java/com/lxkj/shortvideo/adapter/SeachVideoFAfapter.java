package com.lxkj.shortvideo.adapter;

import android.content.Context;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.lxkj.shortvideo.AppConsts;
import com.lxkj.shortvideo.R;
import com.lxkj.shortvideo.bean.DataListBean;

import java.util.HashMap;
import java.util.List;

import androidx.recyclerview.widget.RecyclerView;

/**
 * Time:2021/1/28
 * <p>
 * Author:李迪迦
 * <p>
 * Interface:
 */
public class SeachVideoFAfapter extends RecyclerView.Adapter<SeachVideoFAfapter.MyHolder> {
    private Context context;
    private List<String> list;
    public SeachVideoFAfapter(Context context, List<String> list) {
        this.context = context;
        this.list = list;

    }
    @Override
    public SeachVideoFAfapter.MyHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_seachvideo, parent, false);
        return new SeachVideoFAfapter.MyHolder(view);
    }

    @Override
    public void onBindViewHolder(SeachVideoFAfapter.MyHolder holder, final int position) {
          holder.tvName.setText(list.get(position));
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
        TextView tvName;
        public MyHolder(View itemView) {
            super(itemView);
            tvName = itemView.findViewById(R.id.tvName);
        }
    }
    private SeachVideoFAfapter.OnItemClickListener onItemClickListener;

    public void setOnItemClickListener(SeachVideoFAfapter.OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    public interface OnItemClickListener {
        void OnItemClickListener(int firstPosition);
    }
}
