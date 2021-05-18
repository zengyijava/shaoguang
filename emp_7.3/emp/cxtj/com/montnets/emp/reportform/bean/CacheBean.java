package com.montnets.emp.reportform.bean;

/**
 * CacheBean
 * @author lianghuageng
 * @date 2018/12/26 08:44
 */
public class CacheBean<T> {

    /**
     * 缓存时间
     */
    private long cacheTime;

    /**
     * 缓存内容
     */
    private T cache;

    public CacheBean(long cacheTime, T cache) {
        this.cacheTime = cacheTime;
        this.cache = cache;
    }

    public long getCacheTime() {
        return cacheTime;
    }

    public void setCacheTime(long cacheTime) {
        this.cacheTime = cacheTime;
    }

    public T getCache() {
        return cache;
    }

    public void setCache(T cache) {
        this.cache = cache;
    }
}
