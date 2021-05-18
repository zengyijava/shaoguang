package com.montnets.emp.netnews.common;

import java.util.HashMap;
import java.util.Map;



/**
* 使用memcached的缓存实用类.
* 
* @author 铁木箱子
*
*/
public class MemCached {
 
	    // 创建全局的唯一实例
	    
	    protected static MemCached memCached = new MemCached();
	    
	    protected static Map<String,Object> valuesMap = new HashMap<String,Object>();
	    
	    /**
	     * 保护型构造方法，不允许实例化！
	     *
	     */
	    protected MemCached()
	    {
	        
	    }
	    
	    /**
	     * 获取唯一实例.
	     * @return
	     */
	    public static MemCached getInstance()
	    {
	        return memCached  ;
	    }
	    /**
	     * 添加一个指定的值到缓存中.
	     * @param key
	     * @param value
	     * @return
	     */
	    public boolean add(String key, Object value)
	    {
	    	if(valuesMap.put(key, value) != null)
	    	{
	    		return true;
	    	}
	    	return false;
	    }
	    
	    
	    
	    public boolean replace(String key, Object value)
	    {
	    	return this.add(key, value);
	    }
	    
	    
	    
	    /**
	     * 根据指定的关键字获取对象.
	     * @param key
	     * @return
	     */
	    public Object get(String key)
	    {
	        return valuesMap.get(key);
	    }
	    



}
