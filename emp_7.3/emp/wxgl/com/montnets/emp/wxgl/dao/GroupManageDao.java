/**
 * @description  
 * @author fanglu <fanglu@montnets.com>
 * @datetime 2013-11-29 下午04:00:07
 */
package com.montnets.emp.wxgl.dao;

import java.sql.Connection;
import java.util.LinkedHashMap;
import java.util.List;

import org.apache.commons.beanutils.DynaBean;

import com.montnets.emp.common.constant.StaticValue;
import com.montnets.emp.common.dao.SuperDAO;
import com.montnets.emp.common.dao.impl.DataAccessDriver;
import com.montnets.emp.entity.wxgl.LfWeiGroup;
import com.montnets.emp.entity.wxgl.LfWeiUserInfo;
import com.montnets.emp.table.wxgl.TableLfWeiAccount;
import com.montnets.emp.table.wxgl.TableLfWeiGroup;
import com.montnets.emp.util.PageInfo;


/**
 * @description 群组管理DAO
 * @project OTT
 * @company ShenZhen Montnets Technology CO.,LTD.
 * @author fanglu <fanglu@montnets.com>
 * @datetime 2013-11-29 下午04:00:07
 */

public class GroupManageDao extends SuperDAO
{
    /**
     * 默认构造函数
     * @description 默认构造函数          			 
     * @author fanglu <fanglu@montnets.com>
     * @datetime 2013-12-3 下午05:02:50
     */
    public GroupManageDao()
    {
        super();
    }
    
    /**
     * 查询群组信息列表
     * @description    
     * @param corpCode
     * @param conditionMap
     * @param orderbyMap
     * @param pageInfo
     * @return       			 
     * @author fanglu <fanglu@montnets.com>
     * @datetime 2013-12-3 下午05:02:07
     */
    public List<DynaBean> getGroupInfoList(String corpCode, LinkedHashMap<String, String> conditionMap, LinkedHashMap<String, String> orderbyMap, PageInfo pageInfo)
    {

        List<DynaBean> beans = null;
        String fieldSql = "SELECT lfGroup.G_ID as gid,lfGroup.NAME as gname,lfGroup.COUNT as gcount,lfGroup.A_ID as aid,lfGroup.CORP_CODE as corpCode,lfGroup.CREATETIME as createtime,lfGroup.MODIFYTIME modifytime";

        StringBuffer tableSql = new StringBuffer();
        tableSql.append(" FROM ").append(TableLfWeiGroup.TABLE_NAME).append(" lfGroup ").append(StaticValue.getWITHNOLOCK());
        tableSql.append(" JOIN ").append(TableLfWeiAccount.TABLE_NAME).append(" lfaccount ").append(StaticValue.getWITHNOLOCK()).append(" ON ");
        tableSql.append(" lfGroup.A_ID ").append("=").append("lfaccount." + TableLfWeiAccount.A_ID);
        
        StringBuffer conSql = new StringBuffer();
        conSql.append(" and lfGroup.").append(TableLfWeiGroup.CORP_CODE).append("='").append(corpCode).append("'");
        //wgid为0或者null时
        conSql.append(" and lfGroup.").append(TableLfWeiGroup.WG_ID).append(" not in(").append("0").append(")");
        conSql.append(" and lfGroup.").append(TableLfWeiGroup.WG_ID).append(" IS NOT NULL ");

        String conditionSql = getConditionSql(conditionMap);
        String orderbySql = " order by lfGroup.G_ID DESC";
        String sql = fieldSql + tableSql + conSql.toString() + conditionSql + orderbySql;
        String countSql = new StringBuffer("select count(*) totalcount ").append(tableSql).append(conSql.toString()).append(conditionSql).toString();
        beans = new DataAccessDriver().getGenericDAO().findPageDynaBeanBySQLNoCount(sql, countSql, pageInfo, StaticValue.EMP_POOLNAME, null);
        return beans;
    }
    
    /**
     * 条件拼装方法
     * @description 根据参数进行查询条件拼装   
     * @param conditionMap 查询条件集合
     * @return       			 
     * @author fanglu <fanglu@montnets.com>
     * @datetime 2013-12-3 下午05:18:13
     */
    public String getConditionSql(LinkedHashMap<String, String> conditionMap)
    {
        StringBuffer conditionSql = new StringBuffer();

        if(null != conditionMap.get("gname") && !"".equals(conditionMap.get("gname")))
        {
            conditionSql.append(" and lfGroup." + TableLfWeiGroup.NAME + " like '%" + conditionMap.get("gname") + "%'");
        }
        if(null != conditionMap.get("aid") && !"".equals(conditionMap.get("aid")))
        {
            conditionSql.append(" and lfGroup." + TableLfWeiGroup.A_ID + " = " + conditionMap.get("aid"));
        }

        return conditionSql.toString();
    }
    
    /**
     * 获取当前组的所有成员(如果组的otWeiGroup的wgId=0，则为“未分组”)
     * @description    
     * @param aId
     * @return
     * @throws Exception       			 
     * @author linzhihan <zhihanking@163.com>
     * @datetime 2014-2-25 上午10:56:16
     */
    public List<LfWeiUserInfo> getUnGroupMembers(String aId,LfWeiGroup otWeiGroup) throws Exception
    {
        if(otWeiGroup==null){
            return null;
        }
        StringBuffer sql = new StringBuffer();
        sql.append("select info.* from lf_wei_userinfo info "+StaticValue.getWITHNOLOCK()+" where info.g_id in ");
        if(otWeiGroup.getWGId()==null||otWeiGroup.getWGId()==0L){
            sql.append("(select gp.g_id from lf_wei_group gp where (gp.wg_id=0) and gp.a_id ="+aId+")");
            sql.append(" OR (info.g_id is null and info.a_id="+aId+")");
        }else{
            sql.append("(select gp.g_id from lf_wei_group gp where gp.wg_id='"+otWeiGroup.getWGId()+"' and gp.a_id ="+aId+")");
        }
        return findEntityListBySQL(LfWeiUserInfo.class, sql.toString(), StaticValue.EMP_POOLNAME);
    }
    
    /**
     * 更新群组数量
     * @description    
     * @param aId
     * @param gpIds
     * @return       			 
     * @author fanglu <fanglu@montnets.com>
     * @throws Exception 
     * @datetime 2014-2-25 下午05:50:54
     */
    public boolean updateGroupCount(Connection conn,String gpIds) throws Exception
    {
    	  String  sql ="update lf_wei_group set lf_wei_group.count = " +
        	  		"(select "+(StaticValue.DBTYPE==StaticValue.DB2_DBTYPE?"varchar(rtrim(char(count(g_id))))":"count(g_id)")+" from lf_wei_userInfo " +
        	  		"where g_id = lf_wei_group.g_id) where lf_wei_group.g_id in ("+gpIds+")";
          return executeBySQL(conn, sql);
    }
}
