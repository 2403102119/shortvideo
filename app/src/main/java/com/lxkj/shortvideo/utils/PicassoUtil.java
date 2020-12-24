package com.lxkj.shortvideo.utils;

import android.content.Context;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.lxkj.shortvideo.R;
import com.lxkj.shortvideo.http.Url;
import com.squareup.picasso.Picasso;

/**
 * Created by kxn on 2018/7/17 0017.
 */
public class PicassoUtil {
    public static void setImag(Context context, String url, ImageView iv) {
        if (!StringUtil.isEmpty(url)) {
            if (url.startsWith("http"))
                Glide.with(context).load(url).apply(new RequestOptions().error(R.mipmap.ic_defaut).placeholder(R.mipmap.ic_defaut)).into(iv);
//                Picasso.with(context).load(url).placeholder(R.mipmap.ic_logo).error(R.mipmap.ic_logo).into(iv);
            else
                Glide.with(context).load(Url.IP + url).apply(new RequestOptions().placeholder(R.mipmap.ic_defaut).error(R.mipmap.ic_defaut)).into(iv);
//                Picasso.with(context).load(Url.IP + url).placeholder(R.mipmap.ic_logo).error(R.mipmap.ic_logo).into(iv);
        } else
            Picasso.with(context).load(R.mipmap.ic_defaut).into(iv);
    }

    public static void setImag(Context context, int res, ImageView iv) {
        Picasso.with(context).load(res).placeholder(R.mipmap.ic_defaut).error(R.mipmap.ic_defaut).into(iv);
    }

    public static void setHeadImag(Context context, String url, ImageView iv) {
        if (!StringUtil.isEmpty(url))
            Picasso.with(context).load(url).placeholder(R.mipmap.ic_head).error(R.mipmap.ic_head).into(iv);
        else
            Picasso.with(context).load(R.mipmap.ic_head).into(iv);
    }
}
