package com.lxkj.shortvideo.adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.lxkj.shortvideo.R;
import com.makeramen.roundedimageview.RoundedImageView;

import java.util.List;

import androidx.recyclerview.widget.RecyclerView;

/**
 * Created by Administrator on 2019/10/31 0031.
 */

public class Recycle_one_itemAdapter extends  RecyclerView.Adapter<Recycle_one_itemAdapter.MyHolder>{
    private Context context;
    private List<String> list;
    private static final String TAG = "Recycle_one_itemAdapter";

    public Recycle_one_itemAdapter(Context context, List<String> list) {
        this.context = context;
        this.list = list;

    }
    @Override
    public Recycle_one_itemAdapter.MyHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_item_copy, parent, false);
        return new Recycle_one_itemAdapter.MyHolder(view);
    }

    @Override
    public void onBindViewHolder(Recycle_one_itemAdapter.MyHolder holder, final int position) {
//        if (0!=list.size())
////            Glide.with(context).load(list.get(position)).into(holder.image1);
//            Glide.with(context).applyDefaultRequestOptions(new RequestOptions()
//                    .error(R.mipmap.logo)
//                    .placeholder(R.mipmap.logo))
//                    .load(list.get(position))
//                    .into(holder.image1);
//
//        Log.i(TAG, "onBindViewHolder: "+list);
//
//        holder.itemView.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                onItemClickListener.OnItemClickListener(position);
//            }
//        });

    }

    @Override
    public int getItemCount() {

//        if (list == null) {
//            return 0;
//        } else {
//            return list.size();
//        }
        return 5;
    }


    public class MyHolder extends RecyclerView.ViewHolder {
        RoundedImageView image1;
        public MyHolder(View itemView) {
            super(itemView);
            image1 = itemView.findViewById(R.id.image1);
        }
    }
    private Recycle_one_itemAdapter.OnItemClickListener onItemClickListener;

    public void setOnItemClickListener(Recycle_one_itemAdapter.OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    public interface OnItemClickListener {
        void OnItemClickListener(int firstPosition);

    }
}
