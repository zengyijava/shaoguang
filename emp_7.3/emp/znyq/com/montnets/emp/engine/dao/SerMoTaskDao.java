package com.montnets.emp.engine.dao;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.beanutils.DynaBean;

import com.montnets.emp.common.constant.StaticValue;
import com.montnets.emp.common.context.EmpExecutionContext;
import com.montnets.emp.common.dao.IGenericDAO;
import com.montnets.emp.common.dao.SuperDAO;
import com.montnets.emp.common.dao.impl.DataAccessDriver;
import com.montnets.emp.engine.vo.LfMoServiceVo;
import com.montnets.emp.table.sysuser.TableLfDomination;
import com.montnets.emp.table.sysuser.TableLfSysuser;
import com.montnets.emp.util.PageInfo;

/**
 * 上行业务记录dao
 * @project p_znyq
 * @author huangzb
 * @company ShenZhen Montnets Technology CO.,LTD.
 * @datetime 2013-11-16 上午10:12:30
 * @description
 */
public class SerMoTaskDao extends SuperDAO
{
	public List<DynaBean> getSerMoTaskList_back(LinkedHashMap<String, String> conditionMap, String lguserid, String corpCode, PageInfo pageInfo){
		
		try{
			String fieldSql = " select mo.*, ser.SER_NAME, ser.ORDER_CODE, client.NAME, sysuser.NAME as createrName ";
			//获取查询表格sql
			String tableSql = this.getSerMoTaskTableSql();
			//获取权限sql
			String domSql = this.getSerMoTaskDomSql(lguserid);
			//获取查询条件sql
			String conditionSql = this.getSerMoTaskCondition(conditionMap, corpCode);
			List<String> timeList = this.getTimeCondition(conditionMap);
			
			String orderbySql = " order by mo.DELIVERTIME desc ";
			
			String sql = fieldSql + tableSql + domSql + conditionSql + orderbySql;
			
			//分页
			String countSql = new StringBuffer("select count(*) totalcount")
					.append(tableSql).append(domSql).append(conditionSql)
					.toString();
			
			List<DynaBean> moList = new DataAccessDriver().getGenericDAO().findPageDynaBeanBySQL(sql, countSql, pageInfo, StaticValue.EMP_POOLNAME, timeList);
			
			return moList;
		}catch(Exception e){
			EmpExecutionContext.error(e, "查询上行业务记录异常。");
			return null;
		}
		
	}
	
	public List<LfMoServiceVo> getSerMoTaskList(LinkedHashMap<String, String> conditionMap, String lguserid, String corpCode, PageInfo pageInfo){
		
		IGenericDAO gDao;
		//List<DynaBean> moList = new DataAccessDriver().getGenericDAO().findPageDynaBeanBySQL(sql, countSql, pageInfo, StaticValue.EMP_POOLNAME, timeList);
		List<LfMoServiceVo> moList;
		try
		{
			String fieldSql = " select mo.*, ser.SER_NAME, ser.ORDER_CODE, sysuser.NAME as createrName ";
			//获取查询表格sql
			String tableSql = this.getSerMoTableSql();
			//获取权限sql
			String domSql = this.getSerMoTaskDomSql(lguserid);
			//获取查询条件sql
			String conditionSql = this.getSerMoCondition(conditionMap, corpCode);
			List<String> timeList = this.getTimeCondition(conditionMap);
			
			String orderbySql = " order by mo.DELIVERTIME desc, mo.ms_id desc ";
			
			String sql = fieldSql + tableSql + domSql + conditionSql + orderbySql;
			gDao = new DataAccessDriver().getGenericDAO();
			if(pageInfo == null){
				moList =  findVoListBySQL(LfMoServiceVo.class, sql, StaticValue.EMP_POOLNAME, timeList);
			}
			else{
				//分页
				String countSql = new StringBuffer("select count(*) totalcount")
						.append(tableSql).append(domSql).append(conditionSql)
						.toString();
				moList = gDao.findPageVoListBySQL (LfMoServiceVo.class, sql, countSql, pageInfo, StaticValue.EMP_POOLNAME, timeList);
			}
			
			if(moList == null || moList.size() == 0){
				return moList;
			}
		}
		catch (Exception e)
		{
			EmpExecutionContext.error(e, "查询上行业务记录dao异常。");
			return null;
		}
		
		try
		{
			StringBuffer sbSql = new StringBuffer();
			Map<String,String> phoneMap = new HashMap<String,String>();
			for(LfMoServiceVo moVo : moList){
				//判断号码是否已存在
				if(phoneMap.containsKey(moVo.getPhone())){
					continue;
				}
				else
				{
					sbSql.append("'").append(moVo.getPhone()).append("'").append(",");
					phoneMap.put(moVo.getPhone(), moVo.getPhone());
				}
			}
			//去掉最后一个逗号
			sbSql.delete(sbSql.length()-1, sbSql.length());
			
			String clientNameSql = "select mobile,name from LF_CLIENT where MOBILE in ("+sbSql.toString()+")";
			List<DynaBean> clientNameList = gDao.findDynaBeanBySql(clientNameSql);
			//没姓名，则不需要拼
			if(clientNameList == null || clientNameList.size() == 0){
				return moList;
			}
			
			//key为手机号，value为姓名
			Map<String,String> clientNameMap = new HashMap<String,String>();
			for(DynaBean bean : clientNameList)
			{
				if(bean.get("mobile") == null || bean.get("name") == null)
				{
					continue;
				}
				clientNameMap.put(bean.get("mobile").toString(), bean.get("name").toString());
			}
			
			for(int i=0;i<moList.size();i++){
				moList.get(i).setClientName(clientNameMap.get(moList.get(i).getPhone()));
			}
			
			return moList;
		}
		catch (Exception e)
		{
			EmpExecutionContext.error(e, "查询上行业务记录dao，查询客户姓名异常。");
			return moList;
		}
		
	}
	
	private String getSerMoTableSql()
	{
		//查询表格
		String sql = new StringBuffer(" from lf_mo_service mo ")
					.append(" inner join LF_SERVICE ser on mo.SER_ID = ser.SER_ID ")
					.append(" inner join LF_SYSUSER sysuser on sysuser.USER_ID = ser.USER_ID")
					//.append(" left join LF_CLIENT client on client.MOBILE = mo.phone ")
					.toString();
		//返回sql
		return sql;
	}

	private String getSerMoTaskTableSql()
	{
		//查询表格
		String sql = new StringBuffer(" from lf_mo_service mo ")
					.append(" inner join LF_SERVICE ser on mo.SER_ID = ser.SER_ID ")
					.append(" inner join LF_SYSUSER sysuser on sysuser.USER_ID = ser.USER_ID")
					.append(" left join LF_CLIENT client on client.MOBILE = mo.phone ")
					.toString();
		//返回sql
		return sql;
	}
	
	/**
	 * 获取权限sql
	 * @param loginUserId
	 * @return
	 */
	public String getSerMoTaskDomSql(String loginUserId)
	{
		//拼接sql
		StringBuffer domination = new StringBuffer("select ").append(
				TableLfDomination.DEP_ID).append(" from ").append(
				TableLfDomination.TABLE_NAME).append(" where ").append(
				TableLfDomination.USER_ID).append("=").append(loginUserId);
		String dominationSql = new StringBuffer(" where (sysuser.").append(
				TableLfSysuser.USER_ID).append("=").append(loginUserId).append(
				" or sysuser.").append(TableLfSysuser.DEP_ID).append(" in (")
				.append(domination).append("))").toString();
		return dominationSql;
	}
	
	private String getSerMoCondition(LinkedHashMap<String, String> conditionMap, String corpCode){
		try{
			StringBuffer conditionSql = new StringBuffer();
			
			//手机号
			if (conditionMap.get("phone") != null && !"".equals(conditionMap.get("phone")))
			{
				conditionSql.append(" and mo.phone = '").append(conditionMap.get("phone")).append("'");
			}
	        //指定时间段开始
			if (conditionMap.get("moRecBeginTime") != null && !"".equals(conditionMap.get("moRecBeginTime")))
			{
				conditionSql.append(" and mo.DELIVERTIME").append(">=?");
			}
	        //指定时间段结束
			if (conditionMap.get("moRecEndTime") != null
					&& !"".equals(conditionMap.get("moRecEndTime")))
			{
				conditionSql.append(" and mo.DELIVERTIME").append("<=?");
			}
			
			if (conditionMap.get("name") != null && !"".equals(conditionMap.get("name")))
			{
				//conditionSql.append(" and  client.NAME LIKE '%").append(conditionMap.get("name")).append("%' ");
				String sql = "select MOBILE from LF_CLIENT where name = '" + conditionMap.get("name") + "' ";
				List<DynaBean> phoneList = new DataAccessDriver().getGenericDAO().findDynaBeanBySql(sql);
				if(phoneList != null && phoneList.size() > 0){
					conditionSql.append(" and mo.phone in(");
					for(int i=0; i<phoneList.size();i++){
						conditionSql.append("'").append(phoneList.get(i).get("mobile").toString()).append("'");
						if(i < phoneList.size() - 1){
							conditionSql.append(",");
						}
					}
					conditionSql.append(")");
				}
				else{
					//查询没这个姓名的客户，列表不显示
					conditionSql.append(" and 1<>1 ");
				}
			}
			//短信内容
			if (conditionMap.get("msgContent") != null && !"".equals(conditionMap.get("msgContent")))
			{
				conditionSql.append(" and mo.MSGCONTENT like '%").append(conditionMap.get("msgContent")).append("%' ");
			}
			//回复状态
			if (conditionMap.get("replyState") != null && !"".equals(conditionMap.get("replyState")))
			{
				conditionSql.append(" and mo.REPLY_STATE = ").append(conditionMap.get("replyState"));
			}
			//sp账号
			if (conditionMap.get("spUser") != null && !"".equals(conditionMap.get("spUser")))
			{
				conditionSql.append(" and mo.SP_USER = '").append(conditionMap.get("spUser")).append("' ");
			}
			//业务名称
			if (conditionMap.get("serName") != null && !"".equals(conditionMap.get("serName")))
			{
				conditionSql.append(" and ser.SER_NAME like '%").append(conditionMap.get("serName")).append("%' ");
			}
			//业务id
			if (conditionMap.get("serId") != null && !"".equals(conditionMap.get("serId")))
			{
				conditionSql.append(" and mo.SER_ID = ").append(conditionMap.get("serId"));
			}
			//上行指令
			if (conditionMap.get("orderCode") != null && !"".equals(conditionMap.get("orderCode")))
			{
				conditionSql.append(" and ser.ORDER_CODE = '").append(conditionMap.get("orderCode")).append("' ");
			}
			// 创建者名称
			if (conditionMap.get("createrName") != null && !"".equals(conditionMap.get("createrName")))
			{
				conditionSql.append(" and sysuser.NAME like '%").append(conditionMap.get("createrName")).append("%' ");
			}
			return conditionSql.toString();
		}catch(Exception e){
			EmpExecutionContext.error(e, "查询上行业务记录，构造查询条件异常。");
			return null;
		}
	}
	
	private String getSerMoTaskCondition(LinkedHashMap<String, String> conditionMap, String corpCode){
		try{
			StringBuffer conditionSql = new StringBuffer();
			
			//手机号
			if (conditionMap.get("phone") != null && !"".equals(conditionMap.get("phone")))
			{
				conditionSql.append(" and mo.phone = '").append(conditionMap.get("phone")).append("'");
			}
	        //指定时间段开始
			if (conditionMap.get("moRecBeginTime") != null && !"".equals(conditionMap.get("moRecBeginTime")))
			{
				conditionSql.append(" and mo.DELIVERTIME").append(">=?");
			}
	        //指定时间段结束
			if (conditionMap.get("moRecEndTime") != null
					&& !"".equals(conditionMap.get("moRecEndTime")))
			{
				conditionSql.append(" and mo.DELIVERTIME").append("<=?");
			}
			//姓名
			if (conditionMap.get("name") != null && !"".equals(conditionMap.get("name")))
			{
				conditionSql.append(" and  client.NAME LIKE '%").append(conditionMap.get("name")).append("%' ");
			}
			//短信内容
			if (conditionMap.get("msgContent") != null && !"".equals(conditionMap.get("msgContent")))
			{
				conditionSql.append(" and mo.MSGCONTENT like '%").append(conditionMap.get("msgContent")).append("%' ");
			}
			//回复状态
			if (conditionMap.get("replyState") != null && !"".equals(conditionMap.get("replyState")))
			{
				conditionSql.append(" and mo.REPLY_STATE = ").append(conditionMap.get("replyState"));
			}
			//sp账号
			if (conditionMap.get("spUser") != null && !"".equals(conditionMap.get("spUser")))
			{
				conditionSql.append(" and mo.SP_USER = '").append(conditionMap.get("spUser")).append("' ");
			}
			//业务名称
			if (conditionMap.get("serName") != null && !"".equals(conditionMap.get("serName")))
			{
				conditionSql.append(" and ser.SER_NAME like '%").append(conditionMap.get("serName")).append("%' ");
			}
			//业务id
			if (conditionMap.get("serId") != null && !"".equals(conditionMap.get("serId")))
			{
				conditionSql.append(" and mo.SER_ID = ").append(conditionMap.get("serId"));
			}
			//上行指令
			if (conditionMap.get("orderCode") != null && !"".equals(conditionMap.get("orderCode")))
			{
				conditionSql.append(" and ser.ORDER_CODE = '").append(conditionMap.get("orderCode")).append("' ");
			}
			// 创建者名称
			if (conditionMap.get("createrName") != null && !"".equals(conditionMap.get("createrName")))
			{
				conditionSql.append(" and sysuser.NAME like '%").append(conditionMap.get("createrName")).append("%' ");
			}
			return conditionSql.toString();
		}catch(Exception e){
			EmpExecutionContext.error(e, "查询上行业务记录，构造查询条件异常。");
			return null;
		}
	}
	
	private List<String> getTimeCondition(LinkedHashMap<String, String> conditionMap)
	{
		//任务集合
		List<String> timeList = new ArrayList<String>();
		//提交时间
		if (conditionMap.get("moRecBeginTime") != null && !"".equals(conditionMap.get("moRecBeginTime")))
		{
			timeList.add(conditionMap.get("moRecBeginTime"));
		}
		//结束时间
		if (conditionMap.get("moRecEndTime") != null && !"".equals(conditionMap.get("moRecEndTime")))
		{
			timeList.add(conditionMap.get("moRecEndTime"));
		}
		//返回任务
		return timeList;
	}
	
}
