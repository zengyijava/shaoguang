package com.montnets.emp.common.biz;

import com.montnets.emp.common.dao.FrameControlDAO;
import org.apache.commons.beanutils.DynaBean;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * 框架相关biz层
 */
public class FrameControlBiz extends SuperBiz {

    private FrameControlDAO frameDao = new FrameControlDAO();

    /**
     * 获取各版本信息
     * @param ids 为null查询全部
     * @return map
     */
    public Map<String,String[]> getVersionInfos(String ids){
        List<DynaBean> lists = frameDao.getVersionInfos(ids);
        if(lists == null){
            return null;
        }
        Map<String,String[]> map = new LinkedHashMap<String, String[]>();
        for (DynaBean item:lists){
            map.put(item.get("process_id").toString(),new String[]{item.get("version").toString(),item.get("updatetime").toString(),item.get("memo").toString()});
        }
        return  map;
    }

    /**
     * 获取数据库脚本 最新版本号
     */
    public String getDbVersion(){
        return frameDao.getDbVersion();
    }

    /**
     * 获取emp版本升级历史信息
     * @return
     */
    public List<DynaBean> getVersionHis()
    {
        return frameDao.getVersionHis();
    }
}
