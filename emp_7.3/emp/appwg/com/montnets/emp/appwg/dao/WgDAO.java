package com.montnets.emp.appwg.dao;

import java.sql.Connection;
import java.util.Map;

import com.montnets.emp.common.constant.StaticValue;
import com.montnets.emp.common.context.EmpExecutionContext;
import com.montnets.emp.common.dao.SuperDAO;
import com.montnets.emp.table.appmage.TableLfAppMwClient;
import com.montnets.emp.table.appmage.TableLfAppMwGpmem;

/**
 * 
 * @project p_appwg_new
 * @author huangzb
 * @company ShenZhen Montnets Technology CO.,LTD.
 * @datetime 2014-6-20 上午10:05:45
 * @description app网关dao
 */
public class WgDAO extends SuperDAO
{
	/**
	 * 删除用户群组关系
	 * @param conn
	 * @param corpCode 企业编码
	 * @param appCode 用户账户
	 * @return
	 */
	public boolean delGroupUser(Connection conn, String corpCode, String appCode) 
	{
		try
		{
			//如果未预付费，则更新
			String sql = "delete from "+ TableLfAppMwGpmem.TABLE_NAME +" where GM_USER = " +
					"(select "+TableLfAppMwClient.WC_ID+" from LF_APP_MW_CLIENT where CORP_CODE="+corpCode+" and APP_CODE="+appCode+")";
			
			return executeBySQL(conn, sql);
		}
		catch (Exception e)
		{
			EmpExecutionContext.error(e, "删除用户群组关系异常。");
			return false;
		}
		
	}
	
	public boolean updateReadedState(String conReadState, String conSendState, String conMinId, String conMaxId){
		try
		{
			String sql = "update LF_APP_MSGCACHE set READ_STATE = 2 where " +
						"READ_STATE = "+conReadState+" " +
						"and SEND_STATE in ("+conSendState+") and id >= "+conMinId+" and id <= "+conMaxId;
			
			boolean res = executeBySQL(sql, StaticValue.EMP_POOLNAME);
			
			return res;
		}
		catch (Exception e)
		{
			EmpExecutionContext.error(e, "更新消息数据库已读状态异常。");
			return false;
		}
	}
	
	public boolean updateSendedState(String id, String sendedCount){
		try
		{
			String sql = "update LF_APP_MSGCACHE set SEND_STATE = 4,SENDED_COUNT = "+sendedCount+" where " +
						" id = "+id;
			
			boolean res = executeBySQL(sql, StaticValue.EMP_POOLNAME);
			
			return res;
		}
		catch (Exception e)
		{
			EmpExecutionContext.error(e, "更新消息数据库已读状态异常。");
			return false;
		}
	}
	
	public boolean updateSendResult(String id, String sendState, String readState){
		try
		{
			String sql = "update LF_APP_MSGCACHE set SEND_STATE = "+sendState+",READ_STATE="+readState+"  where " +
						" id = "+id;
			
			boolean res = executeBySQL(sql, StaticValue.EMP_POOLNAME);
			
			return res;
		}
		catch (Exception e)
		{
			EmpExecutionContext.error(e, "更新消息数据库已读状态异常。");
			return false;
		}
	}
	
	public boolean updateRead(){
		try
		{
			String sql = "update LF_APP_MSGCACHE set READ_STATE = 1 where READ_STATE = 2 and SEND_STATE in (1,3,4)";
			
			boolean res = executeBySQL(sql, StaticValue.EMP_POOLNAME);
			
			return res;
		}
		catch (Exception e)
		{
			EmpExecutionContext.error(e, "更新消息数据库已读状态异常。");
			return false;
		}
	}
	
	public boolean updateSendFail(){
		try
		{
			String sql = "update LF_APP_MSGCACHE set READ_STATE = 1 where READ_STATE = 2 and SEND_STATE in (1,3,4)";
			
			boolean res = executeBySQL(sql, StaticValue.EMP_POOLNAME);
			
			return res;
		}
		catch (Exception e)
		{
			EmpExecutionContext.error(e, "更新消息数据库已读状态异常。");
			return false;
		}
	}
	
	public boolean updateMoSendedState(String id, String sendedCount){
		try
		{
			String sql = "update LF_APP_MOCACHE set SEND_STATE = 4,SENDED_COUNT = "+sendedCount+" where " +
						" id = "+id;
			
			boolean res = executeBySQL(sql, StaticValue.EMP_POOLNAME);
			
			return res;
		}
		catch (Exception e)
		{
			EmpExecutionContext.error(e, "更新消息数据库已读状态异常。");
			return false;
		}
	}
	
	public boolean updateMoReadedState(String conReadState, String conSendState, String conMinId, String conMaxId){
		try
		{
			String sql = "update LF_APP_MOCACHE set READ_STATE = 2 where " +
						"READ_STATE = "+conReadState+" " +
						"and SEND_STATE in ("+conSendState+") and id >= "+conMinId+" and id <= "+conMaxId;
			
			boolean res = executeBySQL(sql, StaticValue.EMP_POOLNAME);
			
			return res;
		}
		catch (Exception e)
		{
			EmpExecutionContext.error(e, "更新消息数据库已读状态异常。");
			return false;
		}
	}
	
	public boolean updateMtDB(Map<String,String> objectMap, Map<String,String> conditionMap){
		try
		{
			if(objectMap == null || objectMap.size() == 0 || conditionMap == null || conditionMap.size() == 0){
				return false;
			}
			
			StringBuffer sqlSb = new StringBuffer("update LF_APP_MSGCACHE set ");
			
			if(objectMap.get("sendState") != null && objectMap.get("sendState").length() > 0){
				sqlSb.append(" SEND_STATE = ").append(objectMap.get("sendState")).append(",");
			}
			if(objectMap.get("sendedcount") != null && objectMap.get("sendedcount").length() > 0){
				sqlSb.append(" SENDED_COUNT = ").append(objectMap.get("sendedcount")).append(",");
			}
			
			sqlSb.delete(sqlSb.length()-1, sqlSb.length());
			
			sqlSb.append(" where ");
			
			sqlSb.append(" ID in (").append(conditionMap.get("id")).append(") ");
			
			if(conditionMap.get("sendState") != null && conditionMap.get("sendState").length() > 0){
				sqlSb.append(" and SEND_STATE in (").append(conditionMap.get("sendState")).append(") ");
			}
			
			boolean res = executeBySQL(sqlSb.toString(), StaticValue.EMP_POOLNAME);
			
			return res;
		}
		catch (Exception e)
		{
			EmpExecutionContext.error(e, "更新mt消息数据库异常。");
			return false;
		}
	}
	
	public boolean updateMtDBNoRead(Map<String,String> objectMap, Map<String,String> conditionMap){
		try
		{
			if(objectMap == null || objectMap.size() == 0 || conditionMap == null || conditionMap.size() == 0){
				return false;
			}
			
			StringBuffer sqlSb = new StringBuffer("update LF_APP_MSGCACHE set ");
			
			if(objectMap.get("sendState") != null && objectMap.get("sendState").length() > 0){
				sqlSb.append(" SEND_STATE = ").append(objectMap.get("sendState")).append(",");
			}
			if(objectMap.get("sendedcount") != null && objectMap.get("sendedcount").length() > 0){
				sqlSb.append(" SENDED_COUNT = ").append(objectMap.get("sendedcount")).append(",");
			}
			
			sqlSb.delete(sqlSb.length()-1, sqlSb.length());
			
			sqlSb.append(" where ");
			
			sqlSb.append(" SEND_STATE in (").append(conditionMap.get("sendState")).append(") ");
			
			boolean res = executeBySQL(sqlSb.toString(), StaticValue.EMP_POOLNAME);
			
			return res;
		}
		catch (Exception e)
		{
			EmpExecutionContext.error(e, "更新mt消息数据库异常。");
			return false;
		}
	}
	
	public boolean updateMoDB(Map<String,String> objectMap, Map<String,String> conditionMap){
		try
		{
			if(objectMap == null || objectMap.size() == 0 || conditionMap == null || conditionMap.size() == 0){
				return false;
			}
			
			StringBuffer sqlSb = new StringBuffer("update LF_APP_MOCACHE set ");
			
			if(objectMap.get("sendState") != null && objectMap.get("sendState").length() > 0){
				sqlSb.append(" SEND_STATE = ").append(objectMap.get("sendState")).append(",");
			}
			if(objectMap.get("sendedcount") != null && objectMap.get("sendedcount").length() > 0){
				sqlSb.append(" SENDED_COUNT = ").append(objectMap.get("sendedcount")).append(",");
			}
			
			sqlSb.delete(sqlSb.length()-1, sqlSb.length());
			
			sqlSb.append(" where ");
			
			sqlSb.append(" ID in (").append(conditionMap.get("id")).append(") ");
			
			if(conditionMap.get("sendState") != null && conditionMap.get("sendState").length() > 0){
				sqlSb.append(" and SEND_STATE in (").append(conditionMap.get("sendState")).append(") ");
			}
			
			boolean res = executeBySQL(sqlSb.toString(), StaticValue.EMP_POOLNAME);
			
			return res;
		}
		catch (Exception e)
		{
			EmpExecutionContext.error(e, "更新mo消息数据库异常。");
			return false;
		}
	}
	
	public boolean updateMoDBNoRead(Map<String,String> objectMap, Map<String,String> conditionMap){
		try
		{
			if(objectMap == null || objectMap.size() == 0 || conditionMap == null || conditionMap.size() == 0){
				return false;
			}
			
			StringBuffer sqlSb = new StringBuffer("update LF_APP_MOCACHE set ");
			
			if(objectMap.get("sendState") != null && objectMap.get("sendState").length() > 0){
				sqlSb.append(" SEND_STATE = ").append(objectMap.get("sendState")).append(",");
			}
			if(objectMap.get("sendedcount") != null && objectMap.get("sendedcount").length() > 0){
				sqlSb.append(" SENDED_COUNT = ").append(objectMap.get("sendedcount")).append(",");
			}
			
			sqlSb.delete(sqlSb.length()-1, sqlSb.length());
			
			sqlSb.append(" where ");
			
			sqlSb.append(" SEND_STATE in (").append(conditionMap.get("sendState")).append(") ");
			
			if(conditionMap.get("id") != null && conditionMap.get("id").length() > 0){
				sqlSb.append(" and ID in (").append(conditionMap.get("id")).append(") ");
			}
			
			boolean res = executeBySQL(sqlSb.toString(), StaticValue.EMP_POOLNAME);
			
			return res;
		}
		catch (Exception e)
		{
			EmpExecutionContext.error(e, "更新mo消息数据库异常。");
			return false;
		}
	}
	
	public boolean updateRptDB(Map<String,String> objectMap, Map<String,String> conditionMap){
		try
		{
			if(objectMap == null || objectMap.size() == 0 || conditionMap == null || conditionMap.size() == 0){
				return false;
			}
			
			StringBuffer sqlSb = new StringBuffer("update LF_APP_RPTCACHE set ");
			
			if(objectMap.get("sendState") != null && objectMap.get("sendState").length() > 0){
				sqlSb.append(" SEND_STATE = ").append(objectMap.get("sendState")).append(",");
			}
			if(objectMap.get("sendedcount") != null && objectMap.get("sendedcount").length() > 0){
				sqlSb.append(" SENDED_COUNT = ").append(objectMap.get("sendedcount")).append(",");
			}
			
			sqlSb.delete(sqlSb.length()-1, sqlSb.length());
			
			sqlSb.append(" where ");
			
			sqlSb.append(" ID in (").append(conditionMap.get("id")).append(") ");
			
			if(conditionMap.get("sendState") != null && conditionMap.get("sendState").length() > 0){
				sqlSb.append(" and SEND_STATE in (").append(conditionMap.get("sendState")).append(") ");
			}
			
			boolean res = executeBySQL(sqlSb.toString(), StaticValue.EMP_POOLNAME);
			
			return res;
		}
		catch (Exception e)
		{
			EmpExecutionContext.error(e, "更新rpt消息数据库异常。");
			return false;
		}
	}
	
	public boolean updateRptDBNoRead(Map<String,String> objectMap, Map<String,String> conditionMap){
		try
		{
			if(objectMap == null || objectMap.size() == 0 || conditionMap == null || conditionMap.size() == 0){
				return false;
			}
			
			StringBuffer sqlSb = new StringBuffer("update LF_APP_RPTCACHE set ");
			
			if(objectMap.get("sendState") != null && objectMap.get("sendState").length() > 0){
				sqlSb.append(" SEND_STATE = ").append(objectMap.get("sendState")).append(",");
			}
			if(objectMap.get("sendedcount") != null && objectMap.get("sendedcount").length() > 0){
				sqlSb.append(" SENDED_COUNT = ").append(objectMap.get("sendedcount")).append(",");
			}
			
			sqlSb.delete(sqlSb.length()-1, sqlSb.length());
			
			sqlSb.append(" where ");
			
			sqlSb.append(" SEND_STATE in (").append(conditionMap.get("sendState")).append(") ");
			
			if(conditionMap.get("id") != null && conditionMap.get("id").length() > 0){
				sqlSb.append(" and ID in (").append(conditionMap.get("id")).append(") ");
			}
			
			boolean res = executeBySQL(sqlSb.toString(), StaticValue.EMP_POOLNAME);
			
			return res;
		}
		catch (Exception e)
		{
			EmpExecutionContext.error(e, "更新rpt消息数据库异常。");
			return false;
		}
	}

}
