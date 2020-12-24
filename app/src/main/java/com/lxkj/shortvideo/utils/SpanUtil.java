package com.lxkj.shortvideo.utils;

import android.content.Context;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextPaint;
import android.text.style.AbsoluteSizeSpan;
import android.text.style.ForegroundColorSpan;
import android.text.style.StrikethroughSpan;
import android.text.style.UnderlineSpan;
import android.widget.TextView;

/**
 * Created by kxn on 2017/12/15.
 */

public class SpanUtil {

    private Context context;

    public SpanUtil(Context context){this.context = context.getApplicationContext();}

    public void setColorSpan(int colorResource, String str, int start, int end, TextView textView){
        SpannableString sp = new SpannableString(str);
        sp.setSpan(new ForegroundColorSpan(colorResource ),start,end, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        textView.setText(sp);
    }

    public void setSizeSpan(int size, String str, int start, int end, TextView textView){
        SpannableString sp = new SpannableString(str);
        sp.setSpan(new AbsoluteSizeSpan(DisplayUtil.dip2px(context,size)),
                start,end, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        textView.setText(sp);
    }

    public void setUnderLineSpan(String str, TextView textView){
        SpannableString content = new SpannableString(str);
        content.setSpan(new UnderlineSpan(), 0, str.length(), 0);
        textView.setText(content);
    }

    /*废除效果span*/
    public void setStrikeThroughSpan(String str, TextView textView){
        SpannableString content = new SpannableString(str);
        content.setSpan(new StrikethroughSpan(), 0, str.length(), 0);
        textView.setText(content);
    }

    public void setStrikeThroughSpan(final int color, String str, TextView textView){
        SpannableString mstr = new SpannableString(str);
        mstr.setSpan(new StrikethroughSpan(){
            @Override
            public void updateDrawState(TextPaint ds) {
                super.updateDrawState(ds);
                ds.setColor(color);
                ds.setStrikeThruText(true);
            }
        }, 0, mstr.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        textView.setText(mstr);
    }


}
