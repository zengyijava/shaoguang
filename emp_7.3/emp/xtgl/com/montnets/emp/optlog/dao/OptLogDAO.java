package com.montnets.emp.optlog.dao;

import java.util.List;
import java.util.Map;

import com.montnets.emp.common.constant.StaticValue;
import com.montnets.emp.common.dao.SuperDAO;
import com.montnets.emp.common.dao.impl.DataAccessDriver;
import com.montnets.emp.entity.system.LfOpratelog;
import com.montnets.emp.entity.sysuser.LfSysuser;
import com.montnets.emp.table.system.TableLfOpratelog;
import com.montnets.emp.util.PageInfo;
/**
 * 操作日志dao
 * @author zm
 *
 */
public class OptLogDAO extends SuperDAO{

	/**
	 * 
	 * @param GL_UserID
	 * @param pageInfo
	 * @return
	 * @throws Exception
	 */
	//获取操作日志
	public List<LfOpratelog> findUserOpratelog(LfSysuser lfSysuser,Map<String,String> conditionMap,PageInfo pageInfo) throws Exception
	{
		//拼接sql
		StringBuffer str = new StringBuffer();
		str.append("from LF_OPRATELOG log where CORP_CODE = '"+lfSysuser.getCorpCode()+"' and (OP_USER in (");
		str.append("select u.user_name from LF_SYSUSER  u "+StaticValue.getWITHNOLOCK()+" where CORP_CODE = '"+lfSysuser.getCorpCode()+"' and DEP_ID in");
		str.append("(select dom.dep_id  from LF_DOMINATION dom where USER_ID = "+lfSysuser.getUserId()+") or OP_USER='"+lfSysuser.getUserName()+"'))");
		
		String opSendtime = conditionMap.get("opSendtime");
		String opRecvtime = conditionMap.get("opRecvtime");
		if (StaticValue.DBTYPE == StaticValue.ORACLE_DBTYPE) {
			//oracle数据库
			if(opSendtime != null && !"".equals(opSendtime.trim())){
				str.append(" and OP_TIME >= ").append(
				"to_date('").append(opSendtime).append(
						"','yyyy-MM-dd HH24:mi:ss')");
			}
			if(opRecvtime != null && !"".equals(opRecvtime.trim())){
				str.append(" and OP_TIME <= ").append(
				"to_date('").append(opRecvtime).append(
						"','yyyy-MM-dd HH24:mi:ss')");
			}
		} else if (StaticValue.DBTYPE == StaticValue.SQLSERVER_DBTYPE) {
			//sqlserver数据库
			if(opSendtime != null && !"".equals(opSendtime.trim())){
				str.append(" and OP_TIME >= '").append(opSendtime).append("' ");
			}
			if(opRecvtime != null && !"".equals(opRecvtime.trim())){
				str.append(" and OP_TIME <= '").append(opRecvtime).append("' ");
			}
		}else if(StaticValue.DBTYPE == StaticValue.DB2_DBTYPE){
			//DB2数据库
			if(opSendtime != null && !"".equals(opSendtime.trim())){
				str.append(" and OP_TIME >= '").append(opSendtime).append("' ");
			}
			if(opRecvtime != null && !"".equals(opRecvtime.trim())){
				str.append(" and OP_TIME <= '").append(opRecvtime).append("' ");
			}
		}else if(StaticValue.DBTYPE == StaticValue.MYSQL_DBTYPE){
			//mysql数据库
			if(opSendtime != null && !"".equals(opSendtime.trim())){
				str.append(" and OP_TIME >= '").append(opSendtime).append("' ");
			}
			if(opRecvtime != null && !"".equals(opRecvtime.trim())){
				str.append(" and OP_TIME <= '").append(opRecvtime).append("' ");
			}
		}
		
		String opAction = conditionMap.get("opAction");
		//查询条件：操作类型
		if(opAction != null && !"".equals(opAction.trim())){
			str.append(" and OP_ACTION = '"+opAction+"'");
		}
		//查询条件：操作结果
		String opResult = conditionMap.get("opResult");
		if(opResult != null && !"".equals(opResult.trim())){
			str.append(" and OP_RESULT = "+opResult);
		}
		//查询条件：操作内容
		String opContent = conditionMap.get("opContent");
		if(opContent != null && !"".equals(opContent.trim())){
			str.append(" and OP_CONTENT like '%"+opContent+"%'");
		}
		//查询条件：操作用户
		String opUser = conditionMap.get("opUser");
		if(opUser != null && !"".equals(opUser.trim())){
			str.append(" and UPPER(OP_USER) like '%"+opUser.toUpperCase()+"%'");
		}
		String sql = "select  *  " + str.toString();
		String countSql = "select count(*) totalcount ";
		//排序条件
		String orderSql = " order by log." + TableLfOpratelog.LOG_ID
				+ " desc";
		countSql += str.toString();
		sql += orderSql;
		
		List<LfOpratelog> returnList = new DataAccessDriver().getGenericDAO().findPageEntityListBySQLNoCount(
				LfOpratelog.class, sql, countSql, pageInfo,
						StaticValue.EMP_POOLNAME);
		//返回查询结果
		return returnList;
	}
}
