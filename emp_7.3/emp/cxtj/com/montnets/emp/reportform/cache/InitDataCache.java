package com.montnets.emp.reportform.cache;

import com.montnets.emp.reportform.bean.CacheBean;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 初始化数据的缓存类, 用来存放缓存数据, 每2分钟更新一次, 采用时间戳作为更新的凭证
 * @author lianghuageng
 * @date 2018/12/24 16:22
 */
public enum InitDataCache {
    /**
     * 枚举的单例模式
     */
    INSTANCE;

    /**
     * String 是操作员ID, List<Object> index == 0 是时间戳, index == 1 是缓存内容
     */
    private final static Map<Long, Map<String, CacheBean<String>>> INIT_DATA = new ConcurrentHashMap<Long, Map<String, CacheBean<String>>>();


    public Map<Long, Map<String, CacheBean<String>>> getCache() {
        return INIT_DATA;
    }

    /**
     * 添加缓存
     *
     * @param id 操作员ID
     * @param map 存放缓存内容的Map
     */
    public void put(Long id, Map<String, CacheBean<String>> map) {
        INIT_DATA.put(id, map);
    }

    /**
     * 根据 key 从缓存中取值
     *
     * @param id 操作员ID
     * @return 存放缓存内容的Map
     */
    public Map<String, CacheBean<String>> get(Long id) {
        return INIT_DATA.get(id);
    }


    /**
     * 删除缓存
     *
     * @param id 操作员ID
     * @return 存放缓存内容的Map
     */
    public Map<String, CacheBean<String>> delete(Long id) {
        return INIT_DATA.remove(id);
    }


    public void clear() {
        INIT_DATA.clear();
    }
}
