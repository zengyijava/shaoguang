package com.montnets.emp.shorturl.surlmanage.dao;

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
import com.montnets.emp.shorturl.surlmanage.vo.LfNeturlVo;
import com.montnets.emp.table.sysuser.TableLfDep;
import com.montnets.emp.util.PageInfo;

public class UrlManagerDao extends SuperDAO{
	public List<LfNeturlVo> getNeturlVos(LinkedHashMap<String, String> conditionMap,PageInfo pageInfo)throws Exception{
		String sql = "select net.ID,net.SRC_URL,net.URL_NAME,net.URL_MSG,net.CREATE_TM," +
				"net.URLSTATE,net.ISPASS,sysuser.name,lfdep.DEP_NAME,net.REMARKS,net.REMARKS1 " +
				" from LF_NETURL net " +
				"left join LF_SYSUSER sysuser on NET.CREATE_UID = sysuser.USER_ID  " +
				"left join LF_DEP lfdep on sysuser.DEP_ID = lfdep.DEP_ID " +
				"where (sysuser.USER_ID ="+conditionMap.get("lguserid")+
				" OR sysuser.DEP_ID in (select DEP_ID from LF_DOMINATION where USER_ID="+conditionMap.get("lguserid")+")" +
				" OR net.CREATE_UID = "+conditionMap.get("lguserid")+")" ;
		String conditionSql = getConditionSql(conditionMap);

		sql = sql + conditionSql;
		
		String countSql = "select count(*) totalcount FROM (";
        countSql += sql;
        countSql += " ) A";
        sql += " order by net.create_tm desc";
		List<LfNeturlVo> returnList = new DataAccessDriver().getGenericDAO().findPageVoListBySQL(
				LfNeturlVo.class, sql.toString(), countSql, pageInfo, StaticValue.EMP_POOLNAME);
		
		return returnList;
		
	}
	
	/**
	 * 组装SQL过滤语句
	 * @param conditionMap
	 * @return
	 */
	private String getConditionSql(LinkedHashMap<String, String> conditionMap) throws Exception{
		String sql = " ";
		if (conditionMap.size()<1) {
			return sql;
		}
		//连接地址名称
		if (conditionMap.get("urlname")!=null&&!"".equals(conditionMap.get("urlname").trim())) {
			sql += " and net.URL_NAME like'%"+conditionMap.get("urlname")+"%' ";
		}
		//连接地址
		if (conditionMap.get("srcurl")!=null&&!"".equals(conditionMap.get("srcurl").trim())) {
			sql += " and net.SRC_URL like'%"+conditionMap.get("srcurl").trim()+"%' ";
		}
		//连接状态
		String statue = conditionMap.get("urlstate");
		if (statue!=null&&!"".equals(statue)) {
			if ("-1".equals(statue)) {//-1,-2都是禁用
				sql += " and net.URLSTATE<="+statue;
			}else {
				sql += " and net.URLSTATE="+statue;
			}
		}
		//运营商审批状态
		if (conditionMap.get("ispass")!=null&&!"".equals(conditionMap.get("ispass").trim())) {
			String ispass = conditionMap.get("ispass").trim();
			
			if ("-2".equals(ispass)) {//-2,-3都是禁用
				sql += " and net.ISPASS<="+ispass;
			}else {
				sql += " and net.ISPASS="+ispass;
			}
			
		}
		//运营商审批状态
		if (conditionMap.get("ispass&in")!=null&&!"".equals(conditionMap.get("ispass&in").trim())) {
			String ispass = conditionMap.get("ispass&in").trim();
			sql += " and net.ISPASS in ("+ispass+")";
		}
		//创建人
		if (conditionMap.get("creatuser")!=null&&!"".equals(conditionMap.get("creatuser").trim())) {
			sql += " and sysuser.NAME like'%"+conditionMap.get("creatuser").trim()+"%'";
		}
		//机构  根据机构组装下级机构
		if (conditionMap.get("depId")!=null&&!"".equals(conditionMap.get("depId").trim())) {
			//包含子机构
			if (conditionMap.get("isContainsSun")!=null&&!"".equals(conditionMap.get("isContainsSun"))&&"1".equals(conditionMap.get("isContainsSun"))) {
				String depids=new DepDAO().getChildUserDepByParentID(Long.parseLong(conditionMap.get("depId")),TableLfDep.DEP_ID);
				sql += " and sysuser."+depids;
			}else {
				sql += " and sysuser.DEP_ID="+conditionMap.get("depId");
			}
		}
		
		//开始时间
		IGenericDAO genericDao =new DataAccessDriver().getGenericDAO();
		if (conditionMap.get("startTime")!=null&&!"".equals(conditionMap.get("startTime").trim())) {
			
			sql += " and net.CREATE_TM >= "+genericDao.getTimeCondition(conditionMap.get("startTime").trim());
		}
		//结束时间
		if (conditionMap.get("recvtime")!=null&&!"".equals(conditionMap.get("recvtime").trim())) {
			sql += " and net.CREATE_TM <= "+genericDao.getTimeCondition(conditionMap.get("recvtime").trim());
		}
		
		return sql ;
	}
	
	/**
	 * 逻辑删除
	 * @param urlid
	 * @param ispass
	 * @return
	 */
	public boolean update(String urlid, String ispass) {
		String sql = "update LF_NETURL SET ISPASS = -1 WHERE ID IN ("+urlid+") ";
		Connection conn = null;
		PreparedStatement ps = null;
		int rs = -1;
		boolean result = false;
		try {
			conn = connectionManager.getDBConnection(StaticValue.EMP_POOLNAME);
			ps = conn.prepareStatement(sql);
			rs = ps.executeUpdate();
			if (rs>0) {
				result = true;
			}
		} catch (Exception e) {
			EmpExecutionContext.error(e, "删除链接地址失败！");
		}finally{
			try {
				close(null,ps, conn);
			} catch (Exception e) {
				EmpExecutionContext.error(e, "删除链接地址操作-关闭数据库连接异常");
			}
		}
		return result;
	}
	
	/**
	 * 查询用户姓名
	 * @param userids
	 * @return
	 */
	public Map<String, String> getusers(String userids) {
		Map<String, String> users = new HashMap();
		String sql = "select USER_ID,NAME from LF_SYSUSER WHERE USER_ID IN ("+userids+") ";
		Connection conn = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		try {
			conn = connectionManager.getDBConnection(StaticValue.EMP_POOLNAME);
			ps = conn.prepareStatement(sql);
			rs = ps.executeQuery();
			while(rs!=null && rs.next()){
				users.put(rs.getString(1), rs.getString(2));
			}
		} catch (Exception e) {
			EmpExecutionContext.error(e, "获取创建者姓名异常！");
		}finally{
			try {
				close(rs, ps,conn);
			} catch (Exception e) {
				EmpExecutionContext.error(e, "获取创建者姓名操作-关闭数据库连接异常");
			}
		}
		return users;
	}
	
}
