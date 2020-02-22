package com.qiyei.dmall.common;

import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;

/**
 * @author Created by qiyei2015 on 2020/2/22.
 * @version: 1.0
 * @email: 1273482124@qq.com
 * @description:
 */
public class TokenCache {

    private static final String TOKEN = "token_";

    /**
     * 定义日志logger
     */
    private static Logger logger = LoggerFactory.getLogger(TokenCache.class);


    private static LoadingCache<String,String> sTokenCache = CacheBuilder.newBuilder()
            .initialCapacity(1000)
            .maximumSize(10000)
            .expireAfterAccess(12, TimeUnit.HOURS)
            .build(new CacheLoader<String, String>() {
                //默认的数据加载实现,当调用get取值的时候,如果key没有对应的值,就调用这个方法进行加载.
                @Override
                public String load(String s) throws Exception {
                    return null;
                }
            });

    public static String getToken(String key){
        try {
            return sTokenCache.get(TOKEN + key);
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static void setToken(String key,String token){
        sTokenCache.put(TOKEN + key,token);
    }
}
