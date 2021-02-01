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
import com.chad.library.adapter.base.viewholder.BaseViewHolder;
import com.lxkj.shortvideo.AppConsts;
import com.lxkj.shortvideo.R;
import com.lxkj.shortvideo.bean.DataListBean;
import com.makeramen.roundedimageview.RoundedImageView;
import com.xiao.nicevideoplayer.LogUtil;

import java.util.HashMap;
import java.util.List;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

/**
 * Time:2021/1/14
 * <p>
 * Author:李迪迦
 * <p>
 * Interface:
 */
public class ApplyAdapter extends RecyclerView.Adapter<ApplyAdapter.MyHolder> {
    private Context context;
    private List<DataListBean> list;
    public HashMap<Integer, String> contents = new HashMap<>();
    public ApplyAdapter(Context context, List<DataListBean> list) {
        this.context = context;
        this.list = list;

    }
    @Override
    public ApplyAdapter.MyHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_apply, parent, false);
        return new ApplyAdapter.MyHolder(view);
    }

    @Override
    public void onBindViewHolder(ApplyAdapter.MyHolder holder, final int position) {
        holder.etLable.setText(list.get(position).title);
        Glide.with(context).applyDefaultRequestOptions(new RequestOptions()
                .error(R.mipmap.shangchuanshipin)
                .placeholder(R.mipmap.shangchuanshipin))
                .load(list.get(position).video+ AppConsts.ViDEOEND)
                .into(holder.imLableVideo);
        holder.imLableVideo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onItemClickListener.OnItemClickListener(position);
            }
        });

        holder.etLable.addTextChangedListener(new MyTextChangedListener(holder,contents));
    }

    @Override
    public int getItemCount() {
        if (list == null) {
            return 0;
        } else {
            return list.size();
        }
    }

    public class MyTextChangedListener implements TextWatcher {

        public ApplyAdapter.MyHolder holder;
        public HashMap<Integer, String> contents;

        public MyTextChangedListener(ApplyAdapter.MyHolder holder,HashMap<Integer, String> contents){
            this.holder = holder;
            this.contents = contents;
        }

        @Override
        public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

        }

        @Override
        public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

        }

        @Override
        public void afterTextChanged(Editable editable) {
            if(holder != null && contents != null){
                int adapterPosition = holder.getAdapterPosition();
                contents.put(adapterPosition,editable.toString());
            }
        }
    }


    public class MyHolder extends RecyclerView.ViewHolder {
        ImageView imLableVideo;
        EditText etLable;
        public MyHolder(View itemView) {
            super(itemView);
            imLableVideo = itemView.findViewById(R.id.imLableVideo);
            etLable = itemView.findViewById(R.id.etLable);
        }
    }
    private ApplyAdapter.OnItemClickListener onItemClickListener;

    public void setOnItemClickListener(ApplyAdapter.OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    public interface OnItemClickListener {
        void OnItemClickListener(int firstPosition);
        void onEvaluate(int position, String point, String content);
    }
}
