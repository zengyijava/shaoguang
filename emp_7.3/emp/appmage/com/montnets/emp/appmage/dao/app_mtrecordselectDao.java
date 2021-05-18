package com.montnets.emp.appmage.dao;

import java.util.LinkedHashMap;
import java.util.List;

import org.apache.commons.beanutils.DynaBean;

import com.montnets.emp.common.biz.BaseBiz;
import com.montnets.emp.common.constant.StaticValue;
import com.montnets.emp.common.dao.IGenericDAO;
import com.montnets.emp.common.dao.impl.DataAccessDriver;
import com.montnets.emp.table.appmage.TableLfAppMtmsg;
import com.montnets.emp.table.sysuser.TableLfDomination;
import com.montnets.emp.table.sysuser.TableLfSysuser;
import com.montnets.emp.util.PageInfo;

public class app_mtrecordselectDao extends BaseBiz
{
	/**
	 * 查询app下行消息
	 * @param corpCode
	 * @param conditionMap
	 * @param orderbyMap
	 * @param pageInfo
	 * @return
	 */
	public List<DynaBean> query(String corpCode, LinkedHashMap<String, String> conditionMap, LinkedHashMap<String, String> orderbyMap,PageInfo pageInfo)
	{
		IGenericDAO genericDao =new DataAccessDriver().getGenericDAO();
		// 拼SQL语句
		String fieldSql = "SELECT lfMtmsg.*,c.name";
		StringBuffer tableSql = new StringBuffer();
		tableSql.append(" FROM ").append(TableLfAppMtmsg.TABLE_NAME).append(" lfMtmsg LEFT JOIN   ").append(TableLfSysuser.TABLE_NAME).append(" c on lfMtmsg.UserId=c.USER_ID ");
		// 查询条件
		StringBuffer conSql = new StringBuffer();
		//由于企业编码是类似于SZ1000的，不是全部是数字的
//		conSql.append(" and lfMtmsg.").append(TableLfAppMtmsg.CORP_CODE).append("='").append(corpCode).append("'");
		
		//是否有where，默认为false即没where
		boolean hasWhere = false;
		if(conditionMap != null && !conditionMap.entrySet().isEmpty())
		{
			
//			if(conditionMap.get("lguserid") != null && !"".equals(conditionMap.get("lguserid")))
//			{
//				conSql.append(" and (c.USER_ID =" +conditionMap.get("lguserid") +" or domina.USER_ID="+conditionMap.get("lguserid")+")");
//			}
//			if(conditionMap.get("corpCode") != null && !"".equals(conditionMap.get("userid")))
//			{
//				conSql.append(" and c." + TableLfSysuser.NAME + " like '%" + conditionMap.get("opuser") + "%' ");
//			}
			
			// 操作员
			if(conditionMap.get("userid") != null && !"".equals(conditionMap.get("userid")))
			{
				hasWhere = addWhereOrAnd(conSql, hasWhere);
				conSql.append(" c." + TableLfSysuser.USER_ID + " in (" +conditionMap.get("userid")+ ") ");
			}else{
				if(conditionMap.get("corpCode")!=null&&"100000".equals(conditionMap.get("corpCode"))){
					
				}else{
					if("1".equals(conditionMap.get("permissionType"))){
						hasWhere = addWhereOrAnd(conSql, hasWhere);
						conSql.append(" c." + TableLfSysuser.USER_ID + " in (" +conditionMap.get("lguserid")+ ") ");
					}else{
						hasWhere = addWhereOrAnd(conSql, hasWhere);
						conSql.append(" (c.USER_ID="+conditionMap.get("lguserid")+" OR c.USER_ID IN (SELECT USER_ID FROM LF_SYSUSER WHERE ") ;
						conSql.append(" DEP_ID IN (SELECT DEP_ID FROM LF_DOMINATION WHERE USER_ID="+conditionMap.get("lguserid")+")))");
					}
				}
			}
			
			if(conditionMap.get("msg_type") != null && !"".equals(conditionMap.get("msg_type")))
			{
				hasWhere = addWhereOrAnd(conSql, hasWhere);
				conSql.append(" lfMtmsg." + TableLfAppMtmsg.MSG_TYPE + " = " + conditionMap.get("msg_type") + " ");
			}
			
			if(conditionMap.get("tousername") != null && !"".equals(conditionMap.get("tousername")))
			{
				hasWhere = addWhereOrAnd(conSql, hasWhere);
				conSql.append(" lfMtmsg." + TableLfAppMtmsg.TOUSERNAME + " like '%" + conditionMap.get("tousername") + "%' ");
			}
			
			if(conditionMap.get("sendstate") != null && !"".equals(conditionMap.get("sendstate")))
			{
				if("1".equals(conditionMap.get("sendstate"))){
					hasWhere = addWhereOrAnd(conSql, hasWhere);
					conSql.append(" RPT_STATE IN ('0') ");
				}else if("2".equals(conditionMap.get("sendstate"))){
					hasWhere = addWhereOrAnd(conSql, hasWhere);
					conSql.append(" RPT_STATE IN ('1') ");
				}else if("3".equals(conditionMap.get("sendstate"))){
					hasWhere = addWhereOrAnd(conSql, hasWhere);
					conSql.append(" RPT_STATE NOT IN ('0','1') ");
				}
			}
			if(conditionMap.get("createtime") != null && !"".equals(conditionMap.get("createtime")))
			{
				hasWhere = addWhereOrAnd(conSql, hasWhere);
				conSql.append(" lfMtmsg." + TableLfAppMtmsg.CREATETIME + "  >= "+ genericDao.getTimeCondition(conditionMap.get("createtime")));
			}
			if(conditionMap.get("endtime") != null && !"".equals(conditionMap.get("endtime")))
			{
				hasWhere = addWhereOrAnd(conSql, hasWhere);
				conSql.append(" lfMtmsg." + TableLfAppMtmsg.CREATETIME + "  <= " + genericDao.getTimeCondition(conditionMap.get("endtime")));
			}
			if(conditionMap.get("rptstate") != null && !"".equals(conditionMap.get("rptstate")))
			{
				if("-1".equals(conditionMap.get("rptstate"))){
						hasWhere = addWhereOrAnd(conSql, hasWhere);
						conSql.append(" lfMtmsg." + TableLfAppMtmsg.RPT_STATE + " NOT IN ('0','1') ");
				}else{
					hasWhere = addWhereOrAnd(conSql, hasWhere);
					conSql.append(" lfMtmsg." + TableLfAppMtmsg.RPT_STATE + "  = '" + conditionMap.get("rptstate") + "' ");
				}
			}
			if(conditionMap.get("title") != null && !"".equals(conditionMap.get("title")))
			{
				hasWhere = addWhereOrAnd(conSql, hasWhere);
				conSql.append(" lfMtmsg." + TableLfAppMtmsg.TITLE + " like '%" + conditionMap.get("title") + "%' ");
			}
			
		}
		// 排序
		String orderbySql = " order by lfMtmsg.ID DESC";
		// 返回结果
		String sql = fieldSql + tableSql.toString() + conSql.toString()+orderbySql;
		String countSql = "select count(*) totalcount " + tableSql.toString() + conSql.toString();
		return new DataAccessDriver().getGenericDAO().findPageDynaBeanBySQLNoCount(sql, countSql, pageInfo, StaticValue.EMP_POOLNAME, null);
	}
	
	//获取权限
	public  static String getDominationSql(String userId)
	{
		StringBuffer dominationSql = new StringBuffer("select ").append(
				TableLfDomination.DEP_ID).append(" from ").append(
				TableLfDomination.TABLE_NAME).append(" where ").append(
				TableLfDomination.USER_ID).append("=").append(userId);
		String sql = new StringBuffer(" and c.").append(
				TableLfSysuser.USER_ID).append("=").append(userId).append(
				" or c.").append(TableLfSysuser.DEP_ID).append(" in (")
				.append(dominationSql).append(")")
				.append("").toString();
		return sql;
	}
	
	/**
	 * 按情况拼接where或and
	 * @param sql
	 * @param hasWhere false为没where关键词
	 * @return 第一次拼接后将会有where，直接返回true
	 */
	private boolean addWhereOrAnd(StringBuffer sql, boolean hasWhere)
	{
		if(!hasWhere)
		{
			sql.append(" where ");
		}
		else
		{
			sql.append(" and ");
		}
		return true;
	}
	
}
