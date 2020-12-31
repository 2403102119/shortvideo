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
 * Time:2020/12/30
 * <p>
 * Author:李迪迦
 * <p>
 * Interface:
 */
public class ClassicalAdapter extends RecyclerView.Adapter<ClassicalAdapter.MyHolder> {
    private Context context;
    private List<DataListBean> list;
    public ClassicalAdapter(Context context, List<DataListBean> list) {
        this.context = context;
        this.list = list;

    }
    @Override
    public ClassicalAdapter.MyHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_classical, parent, false);
        return new ClassicalAdapter.MyHolder(view);
    }

    @Override
    public void onBindViewHolder(ClassicalAdapter.MyHolder holder, final int position) {

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
    private ClassicalAdapter.OnItemClickListener onItemClickListener;

    public void setOnItemClickListener(ClassicalAdapter.OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    public interface OnItemClickListener {
        void OnItemClickListener(int firstPosition);
    }
}

