package com.lxkj.shortvideo.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.lxkj.shortvideo.R;
import com.lxkj.shortvideo.bean.DataListBean;

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

    }

    @Override
    public int getItemCount() {

//        if (list == null) {
//            return 0;
//        } else {
//            return list.size();
//        }
        return 8;
    }


    public class MyHolder extends RecyclerView.ViewHolder {
        public MyHolder(View itemView) {
            super(itemView);
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


