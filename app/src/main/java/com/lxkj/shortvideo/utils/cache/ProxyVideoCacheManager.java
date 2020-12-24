package com.lxkj.shortvideo.utils.cache;

import android.content.Context;

import com.danikula.cachevideo.HttpProxyCacheServer;

public class ProxyVideoCacheManager {

    private static HttpProxyCacheServer sharedProxy;

    private ProxyVideoCacheManager() {
    }

    public static HttpProxyCacheServer getProxy(Context context) {
        return sharedProxy == null ? (sharedProxy = newProxy(context)) : sharedProxy;
    }

    private static HttpProxyCacheServer newProxy(Context context) {
        return new HttpProxyCacheServer.Builder(context)
                .maxCacheSize(512 * 1024 * 1024)       // 512MB for cache
                //缓存路径，不设置默认在sd_card/Android/data/[app_package_name]/cache中
//                .cacheDirectory()
                .build();
    }



}