package com.lxkj.shortvideo.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.lxkj.shortvideo.R;
import com.lxkj.shortvideo.bean.DataListBean;
import com.mcxtzhang.swipemenulib.SwipeMenuLayout;

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
        public MyHolder(View itemView) {
            super(itemView);
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
