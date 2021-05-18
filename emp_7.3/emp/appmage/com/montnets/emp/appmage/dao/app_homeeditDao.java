/**
 * Program  : app_homeeditDao.java
 * Author   : zousy
 * Create   : 2014-6-13 上午08:40:09
 * company ShenZhen Montnets Technology CO.,LTD.
 *
 */

package com.montnets.emp.appmage.dao;

import com.montnets.emp.common.constant.StaticValue;
import com.montnets.emp.common.context.EmpExecutionContext;
import com.montnets.emp.common.dao.IGenericDAO;
import com.montnets.emp.common.dao.SuperDAO;
import com.montnets.emp.common.dao.impl.DataAccessDriver;
import com.montnets.emp.table.appmage.TableLfAppSitInfo;
import com.montnets.emp.table.sysuser.TableLfSysuser;
import com.montnets.emp.util.PageInfo;
import org.apache.commons.beanutils.DynaBean;
import org.apache.commons.lang.StringUtils;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.LinkedHashMap;
import java.util.List;

/**
 * 
 * @author   zousy <zousy999@qq.com>
 * @version  1.0.0
 * @2014-6-13 上午08:40:09
 */
public class app_homeeditDao extends SuperDAO
{
	public List<DynaBean> getSitInfos(LinkedHashMap<String, String> conditionMap,PageInfo pageInfo) throws Exception{
		List<DynaBean> beanList = null;
		long currTime = System.currentTimeMillis(); 
		IGenericDAO genericDao =new DataAccessDriver().getGenericDAO();
		String sql = "select info."+TableLfAppSitInfo.S_ID+",info."+TableLfAppSitInfo.NAME+",info."+TableLfAppSitInfo.SENDSTATE
		+",info."+TableLfAppSitInfo.PUBLISHTIME+",info."+TableLfAppSitInfo.STATUS+",info."+TableLfAppSitInfo.validity+",info."+TableLfAppSitInfo.CANCELTIME
		+",info."+TableLfAppSitInfo.CREATETIME+",sysuser."+TableLfSysuser.NAME+" as creater,sysuser."+TableLfSysuser.USER_NAME
		+" from "+TableLfAppSitInfo.TABLE_NAME+" info left join "+TableLfSysuser.TABLE_NAME+
		" sysuser on info."+TableLfAppSitInfo.USERID+" = sysuser."+TableLfSysuser.USER_ID;
		String countSql  = "select count(*) totalcount from "+TableLfAppSitInfo.TABLE_NAME +" info left join "+TableLfSysuser.TABLE_NAME+
		" sysuser on info."+TableLfAppSitInfo.USERID+" = sysuser."+TableLfSysuser.USER_ID;
		String orderSql = " order by "+getOrderSql(TableLfAppSitInfo.STATUS, "-9-321")+" desc,info."+TableLfAppSitInfo.PUBLISHTIME+" desc"
		+",info."+TableLfAppSitInfo.CANCELTIME+" desc,info."+TableLfAppSitInfo.CREATETIME+" desc";
		StringBuffer sqlCon = new StringBuffer(" where ");
		
		sqlCon.append(" info."+TableLfAppSitInfo.CORP_CODE+" = '"+conditionMap.get("lgcorpcode")+"' ");
		
		String sid = conditionMap.get("sid");
		if(StringUtils.isNotBlank(sid)){
			sqlCon.append(" and info."+TableLfAppSitInfo.S_ID+" = "+ sid);
		}
		
		String name = conditionMap.get("name");
		if(StringUtils.isNotBlank(name)){
			sqlCon.append(" and info."+TableLfAppSitInfo.NAME+" like '%"+ name+"%'");
		}
		
		String creater = conditionMap.get("creater");
		if(StringUtils.isNotBlank(creater)){
			sqlCon.append(" and (sysuser."+TableLfSysuser.NAME+" like '%"+ creater+"%'")
			.append(" or sysuser."+TableLfSysuser.USER_NAME+" like '%"+creater+"%')");
		}
		
		String status = conditionMap.get("status");
		if(StringUtils.isNotBlank(status)){
			sqlCon.append(getConSqlByStatus(status));
		}
		
		//起始时间
		String starttime = conditionMap.get("starttime");
		if(StringUtils.isNotBlank(starttime)){
			sqlCon.append(" and info."+TableLfAppSitInfo.CREATETIME+" >="+ genericDao.getTimeCondition(starttime));
		}
		//结束时间
		String endtime = conditionMap.get("endtime");
		if(StringUtils.isNotBlank(endtime)){
			sqlCon.append(" and info."+TableLfAppSitInfo.CREATETIME+" <="+genericDao.getTimeCondition(endtime));
		}
		
		
		sql = sql + sqlCon.toString() + orderSql;
		if(pageInfo == null){
			beanList = getListDynaBeanBySql(sql);
		}else{
			countSql = countSql + sqlCon;
			beanList = genericDao.findPageDynaBeanBySQLNoCount(sql, countSql, pageInfo, StaticValue.EMP_POOLNAME, null);
		}
		return beanList;
	}

	public void updateStatus(Connection conn,String lgcorpcode,String sid,long validity) throws SQLException{
		String baseSql = "update "+TableLfAppSitInfo.TABLE_NAME+" set "+TableLfAppSitInfo.STATUS+"= ? ,"
		+TableLfAppSitInfo.PUBLISHTIME+" = ? ,"+TableLfAppSitInfo.validity +"= ? where "+TableLfAppSitInfo.CORP_CODE +" = ?";
		//可以同时存在多条发布 不去处理已发布的状态
//		String sql1 = baseSql + " and "+TableLfAppSitInfo.STATUS +" = ?";
		String sql2 = baseSql + " and "+TableLfAppSitInfo.S_ID +" = ?";
		PreparedStatement ps = null;
//		ps = conn.prepareStatement(sql1);
//		ps.setInt(1, 1);
//		ps.setTimestamp(2, null);
//		ps.setString(3, null);
//		ps.setString(4, lgcorpcode);
//		ps.setInt(5, 2);
//		ps.executeUpdate();
		try{
			ps = conn.prepareStatement(sql2);
			ps.setInt(1, 2);
			ps.setTimestamp(2, new Timestamp(System.currentTimeMillis()));
			ps.setLong(3, validity);
			ps.setString(4, lgcorpcode);
			ps.setString(5, sid);
			ps.executeUpdate();
		}catch(Exception e){
            EmpExecutionContext.error(e, "发现异常！");
		}finally{
			if(ps != null){
				ps.close();
			}
		}


	}

	public int unpublish(Connection conn,String lgcorpcode,String sid) throws SQLException{
		String baseSql = "update "+TableLfAppSitInfo.TABLE_NAME+" set "+TableLfAppSitInfo.STATUS+"= ? ,"
//		+TableLfAppSitInfo.PUBLISHTIME+" = ?,"+TableLfAppSitInfo.validity +"= ?,"
		+TableLfAppSitInfo.CANCELTIME +"= ? where "+TableLfAppSitInfo.CORP_CODE +" = ?";
		String sql = baseSql + " and "+TableLfAppSitInfo.S_ID +" = ?";
		PreparedStatement ps = null;
		int result = 0;
		try{
			ps = conn.prepareStatement(sql);
			//取消发布 状态置为已下架
			ps.setInt(1, -3);
//		ps.setTimestamp(2, null);
//		ps.setString(3, null);
			//设置下架时间
			ps.setTimestamp(2, new Timestamp(System.currentTimeMillis()));
			ps.setString(3, lgcorpcode);
			ps.setString(4, sid);
			result =  ps.executeUpdate();
		}catch(Exception e){
            EmpExecutionContext.error(e, "发现异常");
		}finally{
			if(ps != null){
				ps.close();
			}
		}
		return result;
	}
	
	public static String getOrderSql(String field,String str){
		if(StaticValue.DBTYPE == StaticValue.SQLSERVER_DBTYPE){
			return "CHARINDEX(CAST("+field+" AS VARCHAR),'"+str+"')";
		}
		if(StaticValue.DBTYPE == StaticValue.ORACLE_DBTYPE){
			return "INSTR('"+str+"',"+field+",1,1)";
		}
		if(StaticValue.DBTYPE == StaticValue.DB2_DBTYPE){
			return "LOCATE(trim(char("+field+")),'"+str+"')";
		}
		if(StaticValue.DBTYPE == StaticValue.MYSQL_DBTYPE){
			return "LOCATE("+field+",'"+str+"')";
		}
		return "1";
	}
	
	public String getConSqlByStatus(String status){
		StringBuffer sql = new StringBuffer("");
		long currTime = System.currentTimeMillis(); 
		if("-9".equals(status)){//过期 查询已发布并且有效期超出当前时间的
			sql.append(" and info."+TableLfAppSitInfo.validity+" <= "+currTime);
			sql.append(" and info."+TableLfAppSitInfo.STATUS+" = 2");
			sql.append(" and info."+TableLfAppSitInfo.SENDSTATE+" = 0");
		}else if("-3".equals(status)){//下架失败
			sql.append(" and info."+TableLfAppSitInfo.STATUS+" = -3");
			sql.append(" and info."+TableLfAppSitInfo.SENDSTATE+" = 1");
		}else if("3".equals(status)){//下架成功
			sql.append(" and info."+TableLfAppSitInfo.STATUS+" = -3");
			sql.append(" and info."+TableLfAppSitInfo.SENDSTATE+" != 1");
		}else if("-2".equals(status)){//发布失败
			sql.append(" and info."+TableLfAppSitInfo.STATUS+" = 2");
			sql.append(" and info."+TableLfAppSitInfo.SENDSTATE+" = 1");
		}else if("2".equals(status)){//发布成功
			sql.append(" and info."+TableLfAppSitInfo.STATUS+" = 2");
			sql.append(" and info."+TableLfAppSitInfo.SENDSTATE+" = 0");
			sql.append(" and info."+TableLfAppSitInfo.validity+" > "+currTime);
		}else if("-1".equals(status)){//未知
			sql.append(" and info."+TableLfAppSitInfo.STATUS+" = 2");
			sql.append(" and (info."+TableLfAppSitInfo.SENDSTATE+" is null");
			sql.append(" or info."+TableLfAppSitInfo.SENDSTATE+" not in(0,1))");
		}else if("1".equals(status)){//待发布
			sql.append(" and info."+TableLfAppSitInfo.STATUS+" = 1");
		}
		return sql.toString();
	}
}

