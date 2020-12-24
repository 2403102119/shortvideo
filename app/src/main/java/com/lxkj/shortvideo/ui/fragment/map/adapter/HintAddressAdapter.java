package com.lxkj.shortvideo.ui.fragment.map.adapter;

import android.content.Context;
import android.graphics.Color;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.ForegroundColorSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.amap.api.services.help.Tip;
import com.lxkj.shortvideo.R;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


/**
 * Created by kxn on 2019/6/4 0004.
 */
public class HintAddressAdapter extends BaseAdapter {
    Context context;
    List<Tip> list;
    String keyWord="";

    public HintAddressAdapter(Context context, List<Tip> list) {
        this.context = context;
        this.list = list;
    }

    public void setKeyWord(String keyWord) {
        this.keyWord = keyWord;
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int i) {
        return list.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        ViewHolder holder;
        if (null == view) {
            view = LayoutInflater.from(context).inflate(R.layout.item_address_hint, null);
            holder = new ViewHolder();
            holder.tvAddress = view.findViewById(R.id.tvAddress);
            holder.tvName = view.findViewById(R.id.tvName);
            view.setTag(holder);
        } else {
            holder = ((ViewHolder) view.getTag());
        }

        holder.tvAddress.setText(list.get(i).getAddress());
        holder.tvName.setText(matcherSearchText(list.get(i).getName(),keyWord));
        return view;
    }


    public static SpannableString matcherSearchText( String text, String keyword) {
        SpannableString ss = new SpannableString(text);
        Pattern pattern = Pattern.compile(keyword);
        Matcher matcher = pattern.matcher(ss);
        while (matcher.find()) {
            int start = matcher.start();
            int end = matcher.end();
            ss.setSpan(new ForegroundColorSpan(Color.parseColor("#F7575B")), start, end, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        }
        return ss;
    }
    class ViewHolder {
        TextView tvName, tvAddress;
    }

}
