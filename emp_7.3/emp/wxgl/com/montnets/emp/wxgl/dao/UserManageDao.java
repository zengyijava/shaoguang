/**
 * @description  
 * @author fanglu <fanglu@montnets.com>
 * @datetime 2013-11-29 下午03:26:08
 */
package com.montnets.emp.wxgl.dao;

import java.util.LinkedHashMap;
import java.util.List;

import org.apache.commons.beanutils.DynaBean;

import com.montnets.emp.common.constant.StaticValue;
import com.montnets.emp.common.context.EmpExecutionContext;
import com.montnets.emp.common.dao.SuperDAO;
import com.montnets.emp.common.dao.impl.DataAccessDriver;
import com.montnets.emp.entity.wxgl.LfWeiGroup;
import com.montnets.emp.table.wxgl.TableLfWeiGroup;
import com.montnets.emp.table.wxgl.TableLfWeiUserInfo;
import com.montnets.emp.util.PageInfo;

/**
 * @description
 * @project OTT
 * @company ShenZhen Montnets Technology CO.,LTD.
 * @author fanglu <fanglu@montnets.com>
 * @datetime 2013-11-29 下午03:26:08
 */
public class UserManageDao extends SuperDAO
{
    
    /**
     * @description 默认构造函数          			 
     * @author fanglu <fanglu@montnets.com>
     * @datetime 2013-11-29 下午03:26:08
     */
    public UserManageDao()
    {
        super();
    }
    
    /**
     * 获取用户信息
     * @description 获取用户信息
     * @param corpCode
     * @param conditionMap
     * @param orderbyMap
     * @param pageInfo
     * @return List<DynaBean>      			 
     * @author fanglu <fanglu@montnets.com>
     * @datetime 2013-11-29 下午03:27:58
     */
    public List<DynaBean> getUserInfoList(String corpCode, LinkedHashMap<String, String> conditionMap, LinkedHashMap<String, String> orderbyMap, PageInfo pageInfo)
    {

        List<DynaBean> beans = null;
        String fieldSql = "SELECT lfuser.WC_ID as wcid,lfuser.NICK_NAME as nickname,lfuser.UNAME as uname,lfuser.HEAD_IMG_URL as headimgurl,lfuser.PHONE as phone,lfuser.G_ID as gid," +
        		"lfuser.DESCR as descr,lfuser.CREATETIME as createtime,lfuser.VERIFYTIME as verifytime,lfuser.OPEN_ID as openid,lfuser.A_ID as aid,lfuser.SUBSCRIBE as subscribe," +
        		"lfuser.SEX as sex,lfuser.COUNTRY as country,lfuser.CITY as city,lfuser.PROVINCE as province,lfuser.SUBSCRIBE_TIME as subscribetime," +
        		"lfuser.LOCAL_IMG_URL as localimgurl,lfgroup.NAME as groupname ";

        StringBuffer tableSql = new StringBuffer();
        tableSql.append(" FROM ").append(TableLfWeiUserInfo.TABLE_NAME).append(" lfuser ").append(StaticValue.getWITHNOLOCK());
        tableSql.append(" LEFT JOIN ").append(TableLfWeiGroup.TABLE_NAME).append(" lfgroup ").append(StaticValue.getWITHNOLOCK());
        tableSql.append(" ON ").append("lfuser.").append(TableLfWeiUserInfo.G_ID).append(" = ").append("lfgroup.").append(TableLfWeiGroup.G_ID);

        tableSql.append(" WHERE  ");

        StringBuffer conSql = new StringBuffer();
        conSql.append("  lfuser.").append(TableLfWeiUserInfo.CORP_CODE).append("='").append(corpCode).append("'");

        String conditionSql = getConditionSql(conditionMap);
        String orderbySql = " order by lfuser.WC_ID DESC";
        String sql = fieldSql + tableSql + conSql.toString() + conditionSql + orderbySql;
        String countSql = new StringBuffer("select count(*) totalcount ").append(tableSql).append(conSql.toString()).append(conditionSql).toString();
        beans = new DataAccessDriver().getGenericDAO().findPageDynaBeanBySQLNoCount(sql, countSql, pageInfo, StaticValue.EMP_POOLNAME, null);
        return beans;
    }
    
    /**
     * 根据参数进行查询条件拼装
     * @param conditionMap 查询条件集合
     * @return       			 
     * @author fanglu <fanglu@montnets.com>
     * @datetime 2013-11-30 上午11:31:29
     */
    public String getConditionSql(LinkedHashMap<String, String> conditionMap)
    {
        StringBuffer conditionSql = new StringBuffer();

        if(null != conditionMap.get("nickname") && !"".equals(conditionMap.get("nickname")))
        {
            conditionSql.append(" and lfuser." + TableLfWeiUserInfo.NICK_NAME + " like '%" + conditionMap.get("nickname") + "%'");
        }
        if(null != conditionMap.get("aid") && !"".equals(conditionMap.get("aid")))
        {
            conditionSql.append(" and lfuser." + TableLfWeiUserInfo.A_ID + " = " + conditionMap.get("aid"));
        }
        if(null != conditionMap.get("gid") && !"".equals(conditionMap.get("gid")))
        {
        	
        	//先判断该值是不是未分组
        	boolean  isnot=false;
        	List<LfWeiGroup> group=null;
        	LinkedHashMap<String, String> link= new LinkedHashMap<String, String>();
        	try {
        		link.put("GId", conditionMap.get("gid"));
    			group = new DataAccessDriver().getEmpDAO().findListBySymbolsCondition(LfWeiGroup.class, link, null);
    		} catch (Exception e) {
    			EmpExecutionContext.error(e,"查询群组信息失败！");
    		}
        	if(group!=null&&group.size()>0){
        		String name=group.get(0).getName();
        		if("未分组".equals(name)){
        			isnot=true;
        		}
        	}
        	if(isnot){
        		conditionSql.append(" and (lfuser." + TableLfWeiUserInfo.G_ID + " = " + conditionMap.get("gid") +"  or lfuser.G_ID is null) " );
        	}else {
        		conditionSql.append(" and lfuser." + TableLfWeiUserInfo.G_ID + " = " + conditionMap.get("gid") );
        	}
        }
        if(null != conditionMap.get("subscribe") && !"".equals(conditionMap.get("subscribe")))
        {
            conditionSql.append(" and lfuser." + TableLfWeiUserInfo.SUBSCRIBE + " = '" + conditionMap.get("subscribe") + "'");
        }
        if(null != conditionMap.get("sex") && !"".equals(conditionMap.get("sex")))
        {
            conditionSql.append(" and lfuser." + TableLfWeiUserInfo.SEX + " = '" + conditionMap.get("sex") + "'");
        }
        if(null != conditionMap.get("place") && !"".equals(conditionMap.get("place")))
        {
            conditionSql.append(" and (lfuser." + TableLfWeiUserInfo.COUNTRY + " like '%" + conditionMap.get("place") + "%'");
            conditionSql.append(" or lfuser." + TableLfWeiUserInfo.CITY + " like '%" + conditionMap.get("place") + "%'");
            conditionSql.append(" or lfuser." + TableLfWeiUserInfo.PROVINCE + " like '%" + conditionMap.get("place") + "%'");
            if(StaticValue.DBTYPE == StaticValue.ORACLE_DBTYPE || StaticValue.DBTYPE == StaticValue.DB2_DBTYPE)
            {
            	conditionSql.append(" or (lfuser." + TableLfWeiUserInfo.COUNTRY +"||"+ "lfuser."+ TableLfWeiUserInfo.PROVINCE +"||"+ "lfuser."+TableLfWeiUserInfo.CITY + ") like '%" + conditionMap.get("place") + "%')");
            }else
            {
            	conditionSql.append(" or (lfuser." + TableLfWeiUserInfo.COUNTRY +"+"+ "lfuser."+ TableLfWeiUserInfo.PROVINCE +"+"+ "lfuser."+TableLfWeiUserInfo.CITY + ") like '%" + conditionMap.get("place") + "%')");
            }
        }

        return conditionSql.toString();
    }
    
}
