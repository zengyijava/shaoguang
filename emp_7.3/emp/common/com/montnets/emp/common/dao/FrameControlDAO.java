package com.montnets.emp.common.dao;

import org.apache.commons.beanutils.DynaBean;

import java.util.List;

/**
 *  框架相关dao层
 */
public class FrameControlDAO extends SuperDAO {

    /**
     * 根据程序编号获得版本号信息
     * @param ids 程序编号 可以为空
     * @return
     */
    public List<DynaBean> getVersionInfos(String ids){
        String sql = "SELECT PROCESS_ID , VERSION ,MEMO,UPDATETIME FROM LF_VERSION";
        if(ids != null && !"".equals(ids.trim())) {
            sql += " WHERE PROCESS_ID IN (" + ids + ")";
        }
        return getListDynaBeanBySql(sql);
    }

    /**
     * 获取数据库脚本 最新版本号
     */
    public String getDbVersion(){
        String sql = "SELECT VERSION FROM LF_DB_SCRIPT ORDER BY ID DESC";
        List<DynaBean> lists = getListDynaBeanBySql(sql);
        if(lists != null && lists.size()>0){
            return lists.get(0).get("version").toString();
        }
        return null;
    }

    /**
     * 获取emp版本升级历史信息
     * @return
     */
    public List<DynaBean> getVersionHis(){
        String sql = "SELECT VERSION,UPDATETIME,VERSIONINFO FROM LF_VERSION_HIS";
        sql += " WHERE PROCESS_ID = 1000 AND ISRELEASE=1 ORDER BY VERSION DESC";

        return getListDynaBeanBySql(sql);
    }
}
