package com.montnets.emp.shorturl.task.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import com.montnets.emp.common.constant.StaticValue;
import com.montnets.emp.common.context.EmpExecutionContext;
import com.montnets.emp.common.dao.DepDAO;
import com.montnets.emp.common.dao.IGenericDAO;
import com.montnets.emp.common.dao.SuperDAO;
import com.montnets.emp.common.dao.impl.DataAccessDriver;
import com.montnets.emp.shorturl.surlmanage.entity.LfUrlTask;
import com.montnets.emp.shorturl.surlmanage.vo.LfUrlTaskVo;
import com.montnets.emp.table.sysuser.TableLfDep;
import com.montnets.emp.util.PageInfo;
import org.apache.commons.lang.StringUtils;

public class SurlTaskRecordDao extends SuperDAO{

	
	/**
	 * 
	 * @param conditionMap
	 * @param pageInfo
	 * @return
	 */
	public List<LfUrlTaskVo> geturlTaskVos(LinkedHashMap<String, String> conditionMap, PageInfo pageInfo) throws Exception{
		String fieldSql = "select ta.ID,ta.CREATE_UID,us.NAME,dep.DEP_NAME,dep.DEP_ID,ta.spuser," +
				"ta.utype,ta.TITLE,ta.TASKID,ta.CREATE_TM,ta.SEND_TM,ta.SUB_COUNT,ta.MSG,ta.status," +
				"ta.neturl,ta.neturl_id,ta.src_file,ta.url_file " +
				"from LF_URLTASK ta " +
				"left join LF_SYSUSER us on ta.CREATE_UID = us.USER_ID " +
				"left join LF_DEP dep on us.DEP_ID = dep.DEP_ID ";

		String conditionSql = getConditionSql(conditionMap);
		fieldSql = fieldSql + conditionSql;
		if(!"".equals(fieldSql)){
			//将第一个and 改为 where
			fieldSql = fieldSql.replaceFirst("and|AND","where");
		}
		String countSql = "select count(*) totalcount FROM (";
		countSql += fieldSql;
	    countSql += " ) A";
		fieldSql += " order by ta.CREATE_TM desc";
	    List<LfUrlTaskVo> returnList = new DataAccessDriver().getGenericDAO().findPageVoListBySQL(
	    		LfUrlTaskVo.class, fieldSql.toString(), countSql, pageInfo, StaticValue.EMP_POOLNAME);
	    
		return returnList;
	}
	
	
	
	
	
	/**
	 * 拼接SQL
	 * @param conditionMap
	 * @return
	 * @throws Exception 
	 * @throws NumberFormatException 
	 */
	private String getConditionSql(LinkedHashMap<String, String> conditionMap) throws NumberFormatException, Exception {
		String sql = "";
		if (conditionMap.size()<1) {
			return sql;
		}
		String userId = conditionMap.get("corpUserId");
		String corpCode = conditionMap.get("corpCode");

		if (userId == null || "".equals(userId)) {
			return sql;
		}
		if (corpCode == null || "".equals(corpCode)) {
			return sql;
		}

		//非管理账号查询自己企业的任务
		if (!"100000".equals(corpCode)) {
			sql += "and (us.USER_ID=" +userId+
					" or us.DEP_ID in (select DEP_ID from LF_DOMINATION where USER_ID = "+userId+") " +
					" or ta.CREATE_UID="+userId+")";
		}

		//机构  根据机构组装下级机构
		if (conditionMap.get("depid")!=null&&!"".equals(conditionMap.get("depid").trim())) {
			//包含子机构																												
			if (conditionMap.get("isContainsSun")!=null&&!"".equals(conditionMap.get("isContainsSun"))&&"1".equals(conditionMap.get("isContainsSun"))) {
				String depids=new DepDAO().getChildUserDepByParentID(Long.parseLong(conditionMap.get("depid")),TableLfDep.DEP_ID);
				sql += " and us."+depids;
			}else {
				sql += " and us.DEP_ID="+conditionMap.get("depid");
			}
		}
		
		//c操作员
		String ids = conditionMap.get("userid");
		if (StringUtils.isNotBlank(ids)) {
			if(ids.endsWith(",")){
				ids = ids.substring(0, ids.length() - 1);
			}
			sql += " and ta.CREATE_UID in ("+ ids + ")";
		}
		
		//SP账号
		if (conditionMap.get("spUser")!=null&&!"".equals(conditionMap.get("spUser").trim())) {
			sql += " and ta.SPUSER = '"+conditionMap.get("spUser")+"'";
		}
		
		// 任务状态
		if (conditionMap.get("taskState")!=null&&!"".equals(conditionMap.get("taskState").trim())) {
			if ("0".equals(conditionMap.get("taskState"))) {
				sql += " and ta.STATUS in (10,20)";
			}
			if ("1".equals(conditionMap.get("taskState"))) {
				sql += " and ta.STATUS in (11,21)";
			}
			if ("2".equals(conditionMap.get("taskState"))) {
				sql += " and ta.STATUS in (12,22)";
			}
			if ("3".equals(conditionMap.get("taskState"))) {
				sql += " and ta.STATUS in (13,23)";
			}
		}
		
		//发送类型
		if (conditionMap.get("taskType")!=null&&!"".equals(conditionMap.get("taskType").trim())) {
			sql += " and ta.UTYPE = "+conditionMap.get("taskType").trim();
		}
		//任务批次
		if (conditionMap.get("taskID")!=null&&!"".equals(conditionMap.get("taskID").trim())) {
			sql += " and ta.TASKID = "+conditionMap.get("taskID").trim();
		}
		
		//开始时间
		IGenericDAO genericDao =new DataAccessDriver().getGenericDAO();
		if (conditionMap.get("startSubmitTime")!=null&&!"".equals(conditionMap.get("startSubmitTime").trim())) {
			
			sql += " and ta.CREATE_TM >= "+genericDao.getTimeCondition(conditionMap.get("startSubmitTime").trim());
		}
		//结束时间
		if (conditionMap.get("endSubmitTime")!=null&&!"".equals(conditionMap.get("endSubmitTime").trim())) {
			sql += " and ta.CREATE_TM <= "+genericDao.getTimeCondition(conditionMap.get("endSubmitTime").trim());
		}
		//短信内容
		if (conditionMap.get("msg")!=null&&!"".equals(conditionMap.get("msg").trim())) {
			sql += " and ta.MSG like'%"+conditionMap.get("msg").trim()+"%'";
		}
		
		return sql;
	}







	/**
	 * 获取用户下拉框
	 * @param userId
	 * @return
	 */
	public Map<String, String> getUser(String userId) {
		Map<String, String> users = new HashMap<String, String>();
		String sql = "select sysuser.USER_ID,sysuser.USER_NAME from LF_SYSUSER sysuser " +
				"where ( sysuser.USER_ID = ?" +
				" or sysuser.DEP_ID in(select DEP_ID from LF_DOMINATION where USER_ID = ?))" +
				" and sysuser.USER_STATE > 0";
		Connection conn = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		try {
			conn = connectionManager.getDBConnection(StaticValue.EMP_POOLNAME);
			ps = conn.prepareStatement(sql);
			ps.setLong(1, Long.valueOf(userId));
			ps.setLong(2, Long.valueOf(userId));
			rs = ps.executeQuery();
			while(rs!=null && rs.next()){
				users.put(rs.getString(1), rs.getString(2));
			}
		} catch (Exception e) {
			EmpExecutionContext.error(e, "任务查询获取用户姓名下拉框异常！");
		}finally{
			try {
				close(rs, ps,conn);
			} catch (Exception e) {
				EmpExecutionContext.error(e, "任务查询获取用户姓名下拉框操作-关闭数据库连接异常");
			}
		}
		return users;
	}

    public List<LfUrlTask> findFailDetails(LinkedHashMap<String, String> conditionMap) throws Exception {
	    List<LfUrlTask> lfUrlTaskList = new DataAccessDriver().getEmpDAO().findListByCondition(LfUrlTask.class, conditionMap, null);
	    return lfUrlTaskList;
    }


    /**
	 * select ta.ID,ta.CREATE_UID,ta.spuser,ta.utype,ta.TITLE,ta.TASKID,ta.CREATE_TM,
ta.SEND_TM,ta.SUB_COUNT,ta.MSG,ta.status,ta.neturl,ta.neturl_id,ta.src_file,ta.url_file from LF_URLTASK ta
	 */
	
	
}
