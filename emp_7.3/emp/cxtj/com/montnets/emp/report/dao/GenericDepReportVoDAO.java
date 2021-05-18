/**
 * 
 */
package com.montnets.emp.report.dao;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Types;
import java.util.Calendar;
import java.util.List;
import java.util.ResourceBundle;

import org.apache.commons.beanutils.DynaBean;

import com.montnets.emp.common.constant.StaticValue;
import com.montnets.emp.common.constant.SystemGlobals;
import com.montnets.emp.common.context.EmpExecutionContext;
import com.montnets.emp.common.dao.SuperDAO;
import com.montnets.emp.common.dao.impl.DataAccessDriver;
import com.montnets.emp.database.ConnectionManagerImp;
import com.montnets.emp.database.IConnectionManager;
import com.montnets.emp.report.bean.RptStaticValue;
import com.montnets.emp.report.vo.DepAreaRptVo;
import com.montnets.emp.report.vo.DepRptVo;
import com.montnets.emp.report.vo.view.ViewDepRptVo;
import com.montnets.emp.table.pasroute.TableLfSpDepBind;
import com.montnets.emp.table.report.TableMmsDatareport;
import com.montnets.emp.table.report.TableMtDatareport;
import com.montnets.emp.table.sysuser.TableLfDep;
import com.montnets.emp.table.sysuser.TableLfSysuser;
import com.montnets.emp.util.PageInfo;

/**
 * 机构统计报表
 * @project p_cxtj
 * @author liaojirong <ljr0300@163.com>
 * @company ShenZhen Montnets Technology CO.,LTD.
 * @datetime 2013-11-21 下午07:19:58
 * @description
 */
public class GenericDepReportVoDAO extends SuperDAO{

	/**
	 * 根据当前操作员机构ID查询机构报表信息
	 * @param curUserId
	 * @param depid
	 * @param deprptvo
	 * @param corpCode
	 * @param pageInfo
	 * @return
	 * @throws Exception
	 */
	public List<DepRptVo> getDepReportList(Long curUserId,String depid,  DepRptVo deprptvo,String corpCode, PageInfo pageInfo) throws Exception 
	{
		//获取查询sql
		String sql = getSearchSql(depid, deprptvo, corpCode);
		if(sql == null || sql.trim().length() == 0)
		{
			EmpExecutionContext.error("机构统计报表，获取查询sql为空。depid="+depid+",corpCode="+corpCode);
			return null;
		}
		
		//加上排序
		String dataSql = sql + " order by tot.DEP_NAME desc ";
		
		List<DepRptVo> returnList;
		if(pageInfo != null)
		{
			//总条数语句
			String countSql = "select count(*) totalcount FROM (" + sql + " ) A";
			returnList = new DataAccessDriver().getGenericDAO().findPageVoListBySQLNoCount(DepRptVo.class, dataSql, countSql, pageInfo, StaticValue.EMP_POOLNAME);
		}
		else
		{
			returnList = findVoListBySQL(DepRptVo.class, dataSql, StaticValue.EMP_POOLNAME);
		}
		
		return returnList;
	}
	
	/**
	 * 获取查询sql语句
	 * @param depid
	 * @param deprptvo
	 * @param corpCode
	 * @return 异常返回null
	 */
	private String getSearchSql(String depid, DepRptVo deprptvo, String corpCode)
	{
		try
		{
			//根据不同类型查询不同表
			String tablename;
			//短信
			if(deprptvo.getMstype()==0)
			{
				tablename = "MT_DATAREPORT";
			}
			//彩信
			else if(deprptvo.getMstype()==1)
			{
				tablename = "MMS_DATAREPORT";
			}
			else
			{
				EmpExecutionContext.error("机构报表OracleSql，短信类型不正确。Mstype=" + deprptvo.getMstype());
				return null;
			}
			
			//获取是否开启p2参数配置
			String depcodethird2p2 = SystemGlobals.getValue("depcodethird2p2");
			//获取不到配置文件值
			if(depcodethird2p2 == null || depcodethird2p2.trim().length() == 0)
			{
				depcodethird2p2 = "false";
			}
			
			String sql;
			switch (StaticValue.DBTYPE) 
			{
				case 1:
					// oracle
					sql = getOracleSql(depid, deprptvo, corpCode, tablename, depcodethird2p2);
					break;
				case 2:
					// sqlserver2005
					sql = getMsSql(depid, deprptvo, corpCode, tablename, depcodethird2p2);
					break;
				case 3:
					// MYSQL
					sql = getMysqlSql(depid, deprptvo, corpCode, tablename, depcodethird2p2);
					break;
				case 4:
					// DB2
					sql = getDB2Sql(depid, deprptvo, corpCode, tablename, depcodethird2p2);
					break;
				default:
					EmpExecutionContext.error("机构统计报表，获取查询sql，未知数据库类型。DBTYPE="+StaticValue.DBTYPE);
					sql = null;
					break;
			}
			return sql;
		}
		catch (Exception e)
		{
			EmpExecutionContext.error(e, "机构报表，获取查询sql，异常。depid="+depid+",corpCode="+corpCode);
			return null;
		}
	}
	
	/**
	 * 根据当前操作员机构ID查询机构报表信息
	 * @param curUserId
	 * @param id
	 * @param deprptvo
	 * @param corpCode
	 * @param pageInfo
	 * @return
	 * @throws Exception
	 */
	public List<DynaBean> getDepDetailReportList(String id, DepRptVo deprptvo,String corpCode, PageInfo pageInfo) throws Exception 
	{
		String sql= this.getDetailSqlByIdType(id, deprptvo, corpCode);
		if(sql==null || sql.trim().length() == 0)
		{
			return null;
		}
		String oderbystr;
		if(deprptvo.getReporttype()!=null&&deprptvo.getReporttype()==2)
		{
			oderbystr=" Y DESC,IMONTH DESC ";
		}
		else
		{
			oderbystr=" IYMD DESC ";
		}
		
		//加上排序
		String dataSql = sql + " order by "+oderbystr+" ";
		
		List<DynaBean> returnList;
		if(pageInfo != null)
		{
			//总条数语句
			String countSql = "select count(*) totalcount FROM (" + sql + " ) A";
			returnList = new DataAccessDriver().getGenericDAO().findPageDynaBeanBySQLNoCount(dataSql, countSql, pageInfo, StaticValue.EMP_POOLNAME, null);
		}
		else
		{
			returnList = new DataAccessDriver().getGenericDAO().findDynaBeanBySql(dataSql);
		}
		
		return returnList;
	}
	

	
	/**
	 * 根据当前操作员机构ID查询机构报表信息
	 * @param curUserId
	 * @param id
	 * @param deparearptvo
	 * @param corpCode
	 * @param pageInfo
	 * @return
	 * @throws Exception
	 */
	public List<DynaBean> getAreaDepDetailReportList(String id, DepAreaRptVo deparearptvo,String corpCode, PageInfo pageInfo) throws Exception 
	{
		String sql= this.getAreaDetailSqlByIdType(id, deparearptvo, corpCode);
		if(sql==null || sql.trim().length() == 0)
		{
			return null;
		}
		//加上排序
		String dataSql = sql + " order by ac.AREACODE,ac.AREANAME,xg.SPGATE,xg.GATENAME ASC ";
		
		List<DynaBean> returnList;
		if(pageInfo != null)
		{
			//总条数语句
			String countSql = "select count(*) totalcount FROM (" + sql + " ) A";
			returnList = new DataAccessDriver().getGenericDAO().findPageDynaBeanBySQLNoCount(dataSql, countSql, pageInfo, StaticValue.EMP_POOLNAME, null);
		}
		else
		{
			returnList = new DataAccessDriver().getGenericDAO().findDynaBeanBySql(dataSql);
		}
		
		return returnList;
	}
	

	
	/**
	 * 
	 * 根据查询的条件，进行合计处理
	 * 
	 * @param curUserId
	 *            当前操作员ID
	 * @param depIds
	 *            查询条件 机构列表
	 * @param userIds
	 *            查询条件 操作员账号列表
	 * @param deprptvo
	 * 
	 * @param corpCode
	 *            企业编码
	 * @param pageInfo
	 * 
	 * @return
	 * 
	 * @throws Exception
	 */
	public long[] findSumCount(Long curUserId, String depIds, DepRptVo deprptvo, String corpCode) throws Exception 
	{
		Connection conn = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		try 
		{
			String fieldSql = "SELECT "+ReportDAO.getPublicCountSql("MDREPORT")+" FROM  ";

			//获取查询sql
			String searchSql = getSearchSql(depIds, deprptvo, corpCode);
			if(searchSql == null || searchSql.trim().length() == 0)
			{
				EmpExecutionContext.error("机构统计报表，获取查询sql为空。depIds="+depIds+",corpCode="+corpCode);
				return new long[]{0,0};
			}
			
			String sql = fieldSql + " (" + searchSql + " ) MDREPORT";
			
			//合计提交总数
			long icount = 0L;
			//合计接收成功数
			long rsucc = 0L;
			//合计发送失败数
			long rfail1 = 0L;
			//合计接收失败数
			long rfail2 = 0L;
			//合计未返数
			long rnret = 0L;
			
			conn = connectionManager.getDBConnection(StaticValue.EMP_POOLNAME);
			ps = conn.prepareStatement(sql);
			rs = ps.executeQuery();
			if (rs.next()) 
			{
				icount = rs.getLong("ICOUNT");
				rsucc = rs.getLong("RSUCC");
				rfail1 = rs.getLong("RFAIL1");
				rfail2 = rs.getLong("RFAIL2");
				rnret = rs.getLong("RNRET");
			}
			long[] returnLong = new long[5];
			returnLong[0] = icount;
			returnLong[1] = rsucc;
			returnLong[2] = rfail1;
			returnLong[3] = rfail2;
			returnLong[4] = rnret;
			return returnLong;
		} 
		catch (Exception e) 
		{
			EmpExecutionContext.error(e,"根据查询的条件，进行合计处理异常");
			return new long[]{0,0};
		} 
		finally 
		{
			close(rs, ps, conn);
		}
	}
	
	
	
	/**
	 * 
	 * 根据查询的条件，进行合计处理
	 * 
	 * @param curUserId
	 *            当前操作员ID
	 * @param idstr
	 *            查询条件 机构列表
	 * @param userIds
	 *            查询条件 操作员账号列表
	 * @param deprptvo
	 * 
	 * @param corpCode
	 *            企业编码
	 * @param pageInfo
	 * 
	 * @return
	 * 
	 * @throws Exception
	 */
	public long[] findDetailSumCount(String idstr, DepRptVo deprptvo, String corpCode)
			throws Exception {
		//合计提交总数
		long icount = 0L;
		//合计接收成功数
		long rsucc = 0L;
		//合计发送失败数
		long rfail1 = 0L;
		//合计接收失败数
		long rfail2 = 0L;
		//合计未返数
		long rnret = 0L;
		String fieldSql = "SELECT "+ReportDAO.getPublicCountSql("MDREPORT")+" FROM  ";
		String sql1= this.getDetailSqlByIdType(idstr, deprptvo, corpCode);
		if(sql1==null||"".equals(sql1)){;
			return null;
		}
		String sql = " (" + sql1	+ " ) MDREPORT";
		sql = fieldSql + sql;
		Connection conn = null;
		PreparedStatement ps = null;
		java.sql.ResultSet rs = null;
		try {
			conn = connectionManager.getDBConnection(StaticValue.EMP_POOLNAME);
			ps = conn.prepareStatement(sql);
			rs = ps.executeQuery();
			if (rs.next()) {
				icount = rs.getLong("ICOUNT");
				rsucc = rs.getLong("RSUCC");
				rfail1 = rs.getLong("RFAIL1");
				rfail2 = rs.getLong("RFAIL2");
				rnret = rs.getLong("RNRET");
			}
		} catch (Exception e) {
			EmpExecutionContext.error(e,"根据查询的条件，进行合计处理异常");
			throw e;
		} finally {
			close(rs, ps, conn);
		}
		long[] returnLong = new long[5];
		returnLong[0] = icount;
		returnLong[1] = rsucc;
		returnLong[2] = rfail1;
		returnLong[3] = rfail2;
		returnLong[4] = rnret;
		return returnLong;
	}
	
	/**
	 * 
	 * 根据查询的条件，进行合计处理
	 * 
	 * @param curUserId
	 *            当前操作员ID
	 * @param idstr
	 *            查询条件 机构列表
	 * @param userIds
	 *            查询条件 操作员账号列表
	 * @param deparearptvo
	 * 
	 * @param corpCode
	 *            企业编码
	 * @param pageInfo
	 * 
	 * @return
	 * 
	 * @throws Exception
	 */
	public long[] findAreaDetailSumCount(String idstr, DepAreaRptVo deparearptvo, String corpCode)
			throws Exception {
		//合计提交总数
		long icount = 0L;
		//合计接收成功数
		long rsucc = 0L;
		//合计发送失败数
		long rfail1 = 0L;
		//合计接收失败数
		long rfail2 = 0L;
		//合计未返数
		long rnret = 0L;
		String fieldSql = "SELECT "+ReportDAO.getPublicCountSql("MDREPORT")+" FROM  ";
		String sql1= this.getAreaDetailSqlByIdType(idstr, deparearptvo, corpCode);
		if(sql1==null||"".equals(sql1)){;
			return null;
		}
		String sql = " (" + sql1	+ " ) MDREPORT";
		sql = fieldSql + sql;
		Connection conn = null;
		PreparedStatement ps = null;
		java.sql.ResultSet rs = null;
		try {
			conn = connectionManager.getDBConnection(StaticValue.EMP_POOLNAME);
			ps = conn.prepareStatement(sql);
			rs = ps.executeQuery();
			if (rs.next()) {
				icount = rs.getLong("ICOUNT");
				rsucc = rs.getLong("RSUCC");
				rfail1 = rs.getLong("RFAIL1");
				rfail2 = rs.getLong("RFAIL2");
				rnret = rs.getLong("RNRET");
			}
		} catch (Exception e) {
			EmpExecutionContext.error(e,"根据查询的条件，进行合计处理异常");
			throw e;
		} finally {
			close(rs, ps, conn);
		}
		long[] returnLong = new long[5];
		returnLong[0] = icount;
		returnLong[1] = rsucc;
		returnLong[2] = rfail1;
		returnLong[3] = rfail2;
		returnLong[4] = rnret;
		return returnLong;
	}
	
/**
 * sqlserver调用机构报表存储过程
 * @param curUserId 当前登录id
 * @param depIds 传入机构id
 * @param deprptvo 查询条件对象
 * 
 */
	public List<DepRptVo> getDepRptVoListMsSql(Long curUserId,
			String depIds, DepRptVo deprptvo,
			String corpCode, PageInfo pageInfo) throws Exception {
		Connection conn = null;
		CallableStatement cstmt = null;
		List<DepRptVo> returnList = null;
		ResultSet rs = null;
		// EMP DEPREPORT存储过程
		try
		{
			IConnectionManager connectionManager = new ConnectionManagerImp();
			conn = connectionManager
					.getDBConnection(StaticValue.EMP_POOLNAME);
			//调用存储过程
			String procedure = "{call LF_DEPREPORT(?,?,?,?,?,?,?,?,?)}";
			cstmt = conn.prepareCall(procedure);
			//设置存储过程参数
			String datasourcetype=deprptvo.getDatasourcetype().toString();
			String spnumtype=deprptvo.getSpnumtype().toString();
			cstmt.setLong(1, Long.parseLong(depIds));
			cstmt.setLong(2, Long.parseLong(deprptvo.getSendTime()));
			cstmt.setLong(3, Long.parseLong(deprptvo.getEndTime()));
			cstmt.setString(4, corpCode);
			cstmt.setInt(5, Integer.parseInt(datasourcetype));
			cstmt.setInt(6, Integer.parseInt(spnumtype));
			if(pageInfo!=null){
				cstmt.setInt(7, pageInfo.getPageIndex());
				cstmt.setInt(8, pageInfo.getPageSize());
			}else{
				pageInfo=new PageInfo();
				cstmt.setInt(7, -1);
				cstmt.setInt(8, -1);
			}
			cstmt.registerOutParameter(9, Types.BIGINT);
			//执行存储过程
			rs = cstmt.executeQuery();
			//返回结果集
			returnList = rsToList(rs, DepRptVo.class, ViewDepRptVo.getORM());
			
			//当前页数
			int pageSize = pageInfo.getPageSize();
			//记录总条数
			int totalCount = cstmt.getInt(9);
			//总页数
			int totalPage = totalCount % pageSize == 0 ? totalCount
					/ pageSize : totalCount / pageSize + 1;
			pageInfo.setTotalRec(totalCount);
			pageInfo.setTotalPage(totalPage);
			// 当前页大于总页数则跳转到第一页
			if (pageInfo.getPageIndex() > totalPage)
			{
				pageInfo.setPageIndex(1);
			}
			
			
		} catch (Exception e)
		{
			//异常处理
			EmpExecutionContext.error(e,"机构统计报表执行存储过程DEPREPORT()出错！");
			EmpExecutionContext.debug("DEPREPORT()执行出错！");

		} finally
		{
			//关闭连接
			try
			{
				if (cstmt != null)
				{
					cstmt.close();
				}
				if (conn != null)
				{
					conn.close();
				}

				try{
					if(rs != null) {
						rs.close();
					}
				}catch(Exception e){
					EmpExecutionContext.error(e,"关闭资源异常");
				}

			} catch (Exception e)
			{
				EmpExecutionContext.error(e,"机构统计报表执行存储过程，连接关闭异常");
			}
		}
		return returnList;
	}
	

	/**
	 * 
	 * 根据企业编码，或者对应的操作员统计报表信息 （如果企业编码为空，则显示所有）(不带分页的查询）
	 * 
	 * @param curUserId
	 *            当期登录的操作员ID
	 * 
	 * @param userIds
	 *            管辖范围内的操作员ID
	 * 
	 * @param depIds
	 *            所管辖的机构ids
	 * @param deprptvo
	 * 
	 * @param corpCode
	 *            企业编码
	 * 
	 * @return
	 * 
	 * 
	 * @throws Exception
	 */
	public List<DepRptVo> getDeprptVoListUnPage(Long curUserId,String depIds, DepRptVo deprptvo,String corpCode) throws Exception {

		// String sql = this.getSql(curUserId, depIds, userIds, deprptvo,
		// corpCode);//获取操作员报表用的sql语句
		// 获取操作员报表用的sql语句
		String sql= this.getSqlByType(depIds, deprptvo, corpCode);
		if(sql==null||"".equals(sql)){;
			return null;
		}
		String dataSql = sql + " order by tot." + TableLfDep.DEP_NAME
				+ " desc ";
		//分页语句
		String countSql = "select count(*) totalcount FROM (";
		countSql += sql;
		countSql += " ) A";
		List<DepRptVo> returnList = findVoListBySQL(DepRptVo.class,
				dataSql, StaticValue.EMP_POOLNAME);

		return returnList;
	}





	
	/**
	 * 短信发送详情  sql语句拼接
	 * @param idstr
	 * @param deprptvo
	 * @param corpCode
	 * @return
	 * @throws Exception
	 */
	public String getDetailSqlByIdType(String idstr, DepRptVo deprptvo, String corpCode) throws Exception 
	{
		if(idstr==null || idstr.trim().length() == 0)
		{
			EmpExecutionContext.error("机构统计报表，详情，获取sql，id为空。");
			return null;
		}
		String tablename;
		if(deprptvo.getMstype()!=null &&deprptvo.getMstype()==0)
		{
			tablename = "MT_DATAREPORT";
		
		}
		else if(deprptvo.getMstype()!=null &&deprptvo.getMstype()==1)
		{
			tablename = "MMS_DATAREPORT";
		}
		else
		{
			EmpExecutionContext.error("机构统计报表，详情，信息类型未知。Mstype="+deprptvo.getMstype());
			return null;
		}
		
		String groupcloumnname;
		//年报表
		if(deprptvo.getReporttype()!=null&&deprptvo.getReporttype()==2)
		{
			groupcloumnname=" Y,IMONTH ";
		}
		else
		{
			groupcloumnname=" IYMD ";
		}
		
		// 数据库的判空函数 默认为oracle的判断NULL函数
		String nullFunction = "NVL";
		//数据的连接符号码
		switch (StaticValue.DBTYPE) {
		case 1:
			// oracle
			nullFunction = "NVL";
			break;
		case 2:
			// sqlserver2005
			nullFunction = "ISNULL";
			break;
		case 3:
			// MYSQL
			nullFunction = "IFNULL";
			break;
		case 4:
			// DB2
			nullFunction = "COALESCE";
			break;
		default:
			break;
		}
		
		// 报表数据查询条件
		String conditionSql = "  ";
		
		//发送类型
		if(deprptvo.getDatasourcetype() != null && deprptvo.getDatasourcetype() != 0)
		{
			conditionSql += " AND SENDTYPE="+deprptvo.getDatasourcetype();
		}
		
		//运营商   0国内；  1国外； 为null或其他值则为全部；
		if(deprptvo.getSpnumtype() != null)
		{
			if(deprptvo.getSpnumtype()==0)
			{
				conditionSql += " AND SPISUNCM IN (0,1,21) ";
			}
			else if(deprptvo.getSpnumtype()==1)
			{
				conditionSql += " AND SPISUNCM=5 ";
			}
		}
		
		// 时间条件的查询
		String timewheresql = " WHERE IYMD>="+ deprptvo.getSendTime()+ " AND IYMD<="+ deprptvo.getEndTime() + " ";
		
		//详情语句
		String deporuserorweizhisql = null;
		
		//获取是否开启p2参数配置
		String depcodethird2p2 = SystemGlobals.getValue("depcodethird2p2");
		//获取不到配置文件值
		if(depcodethird2p2 == null || depcodethird2p2.trim().length() == 0)
		{
			depcodethird2p2 = "false";
		}

		//机构
		if(deprptvo.getIdtype()!=null && "1".equals(deprptvo.getIdtype()))
		{
			String deppath = getString("DEP_PATH", "SELECT DEP_PATH FROM LF_DEP WHERE DEP_ID="+idstr, StaticValue.EMP_POOLNAME);
			deporuserorweizhisql = "select "+groupcloumnname+","+getPublicNumSql(nullFunction,"dp")+",0 USER_ID,0 NAME FROM (SELECT ICOUNT,RSUCC,RFAIL1,RFAIL2,RNRET,P1,P2,IYMD,Y,IMONTH FROM "
						+ tablename+timewheresql+conditionSql+")  dp ";
			
			if ("true".equals(depcodethird2p2)) 
			{
				deporuserorweizhisql += " LEFT JOIN LF_DEP dep on dep.DEP_CODE_THIRD=dp.P2 "; 
			}
			else
			{
				deporuserorweizhisql += " LEFT JOIN LF_SYSUSER su on su.USER_CODE=dp.P1 left join LF_DEP dep on dep.DEP_ID=su.DEP_ID "; 
			}
			deporuserorweizhisql += " WHERE dep.DEP_PATH LIKE '"+deppath+"%' AND dep.DEP_STATE=1 ";
		}
		//操作员
		else if(deprptvo.getIdtype()!=null && "2".equals(deprptvo.getIdtype()))
		{
			if ("true".equals(depcodethird2p2)) 
			{
				deporuserorweizhisql ="select "+groupcloumnname+","+getPublicNumSql(nullFunction,"dp")+",0 USER_ID,0 NAME FROM (select IYMD,Y,IMONTH,ICOUNT,RSUCC,RFAIL1,RFAIL2,RNRET,P1,P2 from " + tablename+timewheresql+conditionSql
				+") dp left join LF_DEP dep on dep.DEP_CODE_THIRD=dp.P2 LEFT JOIN (SELECT USER_ID,USER_NAME,USER_STATE,USER_CODE FROM LF_SYSUSER) LS ON LS.USER_CODE=dp.P1 WHERE dep.dep_id=" + deprptvo.getDepName()+" ";
				if("0".equals(idstr)){
					deporuserorweizhisql=deporuserorweizhisql+" AND LS.USER_ID IS NULL ";
				}else{
					deporuserorweizhisql=deporuserorweizhisql+" AND LS.USER_ID="+idstr+" ";
				}
			}else{
				deporuserorweizhisql ="select "+groupcloumnname+","+getPublicNumSql(nullFunction,"dp")+",0 USER_ID,0 NAME FROM "
						 +tablename+" dp INNER JOIN (SELECT USER_ID,USER_NAME,USER_STATE,USER_CODE FROM LF_SYSUSER WHERE USER_ID="+idstr+") LS ON lS.USER_CODE=dp.P1 "+timewheresql+conditionSql;
			}
		}
		//未知
		else if(deprptvo.getIdtype()!=null&&"3".equals(deprptvo.getIdtype()))
		{
			//mySql
			if (StaticValue.DBTYPE == 3||StaticValue.DBTYPE == 4) 
			{
				if ("true".equals(depcodethird2p2)) 
				{
					deporuserorweizhisql = "select "+groupcloumnname+","+getPublicNumSql(nullFunction,"dp")+",0 USER_ID,0 NAME  from "+tablename+" dp "
					+ " LEFT JOIN LF_DEP dep on dep.DEP_CODE_THIRD=dp.P2 "
					+ timewheresql + conditionSql+" AND COALESCE(dep.DEP_CODE_THIRD,0)=0 ";
				}else{
					deporuserorweizhisql = "select "+groupcloumnname+","+getPublicNumSql(nullFunction,"dp")+",0 USER_ID,0 NAME  from "+tablename+" dp "
					+ "left join LF_SYSUSER su on su.USER_CODE=dp.p1 "
					+ timewheresql + conditionSql+" AND COALESCE(su.user_id,0)=0 ";
				}
			}else if(StaticValue.DBTYPE == 2){
				if ("true".equals(depcodethird2p2)) 
				{
					deporuserorweizhisql = "select "+groupcloumnname+","+getPublicNumSql(nullFunction,"dp")+",0 USER_ID,0 NAME  from (SELECT d.* FROM "+tablename+" d "
					+" left join (select corp_code,spuser from lf_sp_dep_bind group by corp_code,spuser) lsdb on d.userid=lsdb.spuser where lsdb.corp_code='"+corpCode+"') dp "
					+ " LEFT JOIN LF_DEP dep on dep.DEP_CODE_THIRD=dp.P2 "
					+ timewheresql + conditionSql+" AND dep.DEP_CODE_THIRD IS NULL ";
				}else{
					deporuserorweizhisql = "select "+groupcloumnname+","+getPublicNumSql(nullFunction,"dp")+",0 USER_ID,0 NAME  from (SELECT d.* FROM "+tablename+" d "
					+" left join (select corp_code,spuser from lf_sp_dep_bind group by corp_code,spuser) lsdb on d.userid=lsdb.spuser where lsdb.corp_code='"+corpCode+"') dp "
					+ "left join LF_SYSUSER su on su.USER_CODE=dp.p1 "
					+ timewheresql + conditionSql+" AND su.user_id IS NULL ";
				}
			}
			else 
			{
				if ("true".equals(depcodethird2p2)) 
				{
					deporuserorweizhisql = "select "+groupcloumnname+","+getPublicNumSql(nullFunction,"dp")+",0 USER_ID,0 NAME  from "+tablename+" dp "
					+ " LEFT JOIN LF_DEP dep on dep.DEP_CODE_THIRD=dp.P2 "
					+ timewheresql + conditionSql+" AND dep.DEP_CODE_THIRD IS NULL ";
				}else{
					deporuserorweizhisql = "select "+groupcloumnname+","+getPublicNumSql(nullFunction,"dp")+",0 USER_ID,0 NAME  from "+tablename+" dp "
					+ "left join LF_SYSUSER su on su.USER_CODE=dp.p1 "
					+ timewheresql + conditionSql+" AND su.user_id IS NULL ";
				}
			}
		}
		else
		{
			EmpExecutionContext.error("机构统计报表，详情，id类型未知。Idtype="+deprptvo.getIdtype());
			return null;
		}
		
		deporuserorweizhisql += " GROUP BY "+groupcloumnname+"   ";

		return deporuserorweizhisql;
	}
	
	
	public String getPublicNumSql(String nullFunction,String asname){
		if(asname!=null&&!"".equals(asname)){
			return nullFunction + "(SUM(" + asname + ".ICOUNT),0) ICOUNT,"
			  +nullFunction + "(SUM(" + asname + ".RSUCC),0) RSUCC,"
			  +nullFunction + "(SUM(" + asname + ".RFAIL1),0) RFAIL1,"
			  +nullFunction + "(SUM(" + asname + ".RFAIL2),0) RFAIL2,"
			  +nullFunction + "(SUM(" + asname + ".RNRET),0) RNRET ";
		}else{
			return nullFunction + "(SUM(ICOUNT),0) ICOUNT,"
			  +nullFunction + "(SUM(RSUCC),0) RSUCC,"
			  +nullFunction + "(SUM(RFAIL1),0) RFAIL1,"
			  +nullFunction + "(SUM(RFAIL2),0) RFAIL2,"
			  +nullFunction + "(SUM(RNRET),0) RNRET ";
		}
	}
	
	
	/**
	 * 短信机构报表各国详情sql语句生成
	 * @param idstr
	 * @param deparearptvo
	 * @param corpCode
	 * @return
	 * @throws Exception
	 */
	public String getAreaDetailSqlByIdType(String idstr, DepAreaRptVo deparearptvo,String corpCode) throws Exception 
	{
		if(idstr==null || idstr.trim().length() == 0)
		{
			EmpExecutionContext.error("机构统计报表，各国详情，获取sql，id为空。");
			return null;
		}
		
		String tablename="";
		String gatetype="";
		if(deparearptvo.getMstype()!=null && deparearptvo.getMstype()==0)
		{
			tablename = "MT_DATAREPORT";
			gatetype=" AND xg.GATETYPE=1 ";
		}
		else if(deparearptvo.getMstype()!=null && deparearptvo.getMstype()==1)
		{
			tablename = "MMS_DATAREPORT";
			gatetype=" AND xg.GATETYPE=2 ";
		}
		else
		{
			EmpExecutionContext.error("机构统计报表，各国详情，信息类型未知。Mstype="+deparearptvo.getMstype());
			return null;
		}
		
		// 数据库的判空函数 默认为oracle的判断NULL函数
		String nullFunction = "NVL";
		//数据的连接符号码
		switch (StaticValue.DBTYPE) {
		case 1:
			// oracle
			nullFunction = "NVL";
			break;
		case 2:
			// sqlserver2005
			nullFunction = "ISNULL";
			break;
		case 3:
			// MYSQL
			nullFunction = "IFNULL";
			break;
		case 4:
			// DB2
			nullFunction = "COALESCE";
			break;
		default:
			break;
		}
		
		// 报表数据查询条件
		String conditionSql = "  ";
		
		//发送类型
		if(deparearptvo.getDatasourcetype() != null && deparearptvo.getDatasourcetype() != 0)
		{
			conditionSql += " AND SENDTYPE="+deparearptvo.getDatasourcetype();
		}
		//运营商   0国内；  1国外； 为null或其他值则为全部；
		if(deparearptvo.getSpnumtype() != null)
		{
			if(deparearptvo.getSpnumtype()==0)
			{
				conditionSql += " AND dp.SPISUNCM IN (0,1,21) ";
			}
			else if(deparearptvo.getSpnumtype()==1)
			{
				conditionSql += " AND dp.SPISUNCM=5 ";
			}
		}
		//国家代码
		if(deparearptvo.getAreacode() != null && deparearptvo.getAreacode().trim().length() > 0)
		{
			conditionSql += " AND ac.AREACODE LIKE '%"+deparearptvo.getAreacode().trim()+"%' ";
		}
		//国家名称
		if(deparearptvo.getAreaname() != null && deparearptvo.getAreaname().trim().length() > 0)
		{
			conditionSql += " AND ac.AREANAME LIKE '%"+deparearptvo.getAreaname().trim()+"%' ";
		}
		
		// 时间条件的查询
		conditionSql += " AND IYMD>="+ deparearptvo.getSendtime()+ " AND IYMD<="+ deparearptvo.getEndtime() + " ";
		
		//获取是否开启p2参数配置
		String depcodethird2p2 = SystemGlobals.getValue("depcodethird2p2");
		//获取不到配置文件值
		if(depcodethird2p2 == null || depcodethird2p2.trim().length() == 0)
		{
			depcodethird2p2 = "false";
		}

		//详情语句
		String deporuserorweizhisql;
		
		//机构
		if(deparearptvo.getIdtype()!=null&&"1".equals(deparearptvo.getIdtype()))
		{
			String deppath = getString("DEP_PATH", "SELECT DEP_PATH FROM LF_DEP WHERE DEP_ID="+idstr, StaticValue.EMP_POOLNAME);
			deporuserorweizhisql = "select ac.AREACODE,ac.AREANAME,xg.SPGATE,xg.GATENAME,"+getPublicNumSql(nullFunction,"dp")+",0 USER_ID,0 NAME FROM (SELECT ICOUNT,RSUCC,RFAIL1,RFAIL2,RNRET,P1,P2,IYMD,Y,IMONTH,AREACODE,SPGATE,SPISUNCM,SENDTYPE FROM "
						+ tablename+" )  dp LEFT JOIN A_AREACODE ac ON ac.CODE=dp.AREACODE LEFT JOIN XT_GATE_QUEUE xg ON xg.SPGATE=dp.SPGATE AND xg.SPISUNCM=5 "+gatetype;
			if ("true".equals(depcodethird2p2))
			{
				deporuserorweizhisql += " LEFT JOIN LF_DEP dep on dep.DEP_CODE_THIRD=dp.P2 "; 
			}
			else
			{
				deporuserorweizhisql += " LEFT JOIN LF_SYSUSER su on su.USER_CODE=dp.P1 left join LF_DEP dep on dep.DEP_ID=su.DEP_ID "; 
			}
			deporuserorweizhisql += " WHERE dep.DEP_PATH LIKE '"+deppath+"%' AND dep.DEP_STATE=1 "+conditionSql+" ";
		}
		//操作员
		else if(deparearptvo.getIdtype()!=null&&"2".equals(deparearptvo.getIdtype()))
		{
			deporuserorweizhisql ="select ac.AREACODE,ac.AREANAME,xg.SPGATE,xg.GATENAME,"+getPublicNumSql(nullFunction,"dp")+",0 USER_ID,0 NAME FROM "
						 +tablename+" dp INNER JOIN (SELECT USER_ID,USER_NAME,USER_STATE,USER_CODE FROM LF_SYSUSER WHERE USER_ID="+idstr+") LS ON lS.USER_CODE=dp.P1" +
						 		" LEFT JOIN A_AREACODE ac ON ac.CODE=dp.AREACODE LEFT JOIN XT_GATE_QUEUE xg ON xg.SPGATE=dp.SPGATE AND xg.SPISUNCM=5 "+gatetype+" WHERE LS.USER_ID="+idstr+" "+conditionSql;
		}
		//未知
		else if(deparearptvo.getIdtype()!=null&&"3".equals(deparearptvo.getIdtype()))
		{
			//mySql
			if (StaticValue.DBTYPE == 3||StaticValue.DBTYPE == 4) 
			{
				if ("true".equals(depcodethird2p2)) 
				{
					deporuserorweizhisql = " select ac.AREACODE,ac.AREANAME,xg.SPGATE,xg.GATENAME,"+getPublicNumSql(nullFunction,"dp")+",0 USER_ID,0 NAME FROM "+tablename+" dp "
					+ " LEFT JOIN LF_DEP dep on dep.DEP_CODE_THIRD=dp.P2 LEFT JOIN A_AREACODE ac ON ac.CODE=dp.AREACODE LEFT JOIN XT_GATE_QUEUE xg ON xg.SPGATE=dp.SPGATE AND xg.SPISUNCM=5 "+gatetype
					+ " WHERE COALESCE(dep.DEP_CODE_THIRD,0)=0 " + conditionSql;
				}else{
					deporuserorweizhisql = " select ac.AREACODE,ac.AREANAME,xg.SPGATE,xg.GATENAME,"+getPublicNumSql(nullFunction,"dp")+",0 USER_ID,0 NAME FROM "+tablename+" dp "
					+ "left join LF_SYSUSER su on su.USER_CODE=dp.p1  LEFT JOIN A_AREACODE ac ON ac.CODE=dp.AREACODE LEFT JOIN XT_GATE_QUEUE xg ON xg.SPGATE=dp.SPGATE AND xg.SPISUNCM=5 "+gatetype
					+ " WHERE COALESCE(su.user_id,0)=0 " + conditionSql;
				}
			}else if(StaticValue.DBTYPE == 2){
				if ("true".equals(depcodethird2p2)) 
				{
					deporuserorweizhisql = " select ac.AREACODE,ac.AREANAME,xg.SPGATE,xg.GATENAME,"+getPublicNumSql(nullFunction,"dp")+",0 USER_ID,0 NAME FROM (SELECT d.* FROM "+tablename+" d "
					+" left join (select corp_code,spuser from lf_sp_dep_bind group by corp_code,spuser) lsdb on d.userid=lsdb.spuser where lsdb.corp_code='"+corpCode+"') dp "
					+ " LEFT JOIN LF_DEP dep on dep.DEP_CODE_THIRD=dp.P2 LEFT JOIN A_AREACODE ac ON ac.CODE=dp.AREACODE LEFT JOIN XT_GATE_QUEUE xg ON xg.SPGATE=dp.SPGATE AND xg.SPISUNCM=5 "+gatetype
					+ " WHERE dep.DEP_CODE_THIRD IS NULL " + conditionSql;
				}else{
					deporuserorweizhisql = " select ac.AREACODE,ac.AREANAME,xg.SPGATE,xg.GATENAME,"+getPublicNumSql(nullFunction,"dp")+",0 USER_ID,0 NAME FROM (SELECT d.* FROM "+tablename+" d "
					+" left join (select corp_code,spuser from lf_sp_dep_bind group by corp_code,spuser) lsdb on d.userid=lsdb.spuser where lsdb.corp_code='"+corpCode+"') dp "
					+ "left join LF_SYSUSER su on su.USER_CODE=dp.p1  LEFT JOIN A_AREACODE ac ON ac.CODE=dp.AREACODE LEFT JOIN XT_GATE_QUEUE xg ON xg.SPGATE=dp.SPGATE AND xg.SPISUNCM=5 "+gatetype
					+ " WHERE su.user_id IS NULL " + conditionSql;
				}
			}
			else 
			{
				if ("true".equals(depcodethird2p2)) 
				{
					deporuserorweizhisql = " select ac.AREACODE,ac.AREANAME,xg.SPGATE,xg.GATENAME,"+getPublicNumSql(nullFunction,"dp")+",0 USER_ID,0 NAME FROM "+tablename+" dp "
					+ " LEFT JOIN LF_DEP dep on dep.DEP_CODE_THIRD=dp.P2 LEFT JOIN A_AREACODE ac ON ac.CODE=dp.AREACODE LEFT JOIN XT_GATE_QUEUE xg ON xg.SPGATE=dp.SPGATE AND xg.SPISUNCM=5 "+gatetype
					+ " WHERE dep.DEP_CODE_THIRD IS NULL " + conditionSql;
					
				}else{
					deporuserorweizhisql = " select ac.AREACODE,ac.AREANAME,xg.SPGATE,xg.GATENAME,"+getPublicNumSql(nullFunction,"dp")+",0 USER_ID,0 NAME FROM "+tablename+" dp "
					+ "left join LF_SYSUSER su on su.USER_CODE=dp.p1  LEFT JOIN A_AREACODE ac ON ac.CODE=dp.AREACODE LEFT JOIN XT_GATE_QUEUE xg ON xg.SPGATE=dp.SPGATE AND xg.SPISUNCM=5 "+gatetype
					+ " WHERE su.user_id IS NULL " + conditionSql;
				}
			}
		}
		else
		{
			EmpExecutionContext.error("机构统计报表，各国详情，id类型未知。Idtype="+deparearptvo.getIdtype());
			return null;
		}
		
		deporuserorweizhisql += " GROUP BY ac.AREACODE,ac.AREANAME,xg.SPGATE,xg.GATENAME  ";

		return deporuserorweizhisql;
	}
	
	/**
	 * 获取OracleSql
	 * @param depId
	 * @param depreportvo
	 * @param corpCode
	 * @return
	 * @throws Exception
	 */
	private String getOracleSql(String depId, DepRptVo depreportvo, String corpCode, String tablename, String depcodethird2p2) throws Exception 
	{
		// 报表数据查询条件
		String conditionSql = " ";
		
		//发送类型
		if(depreportvo.getDatasourcetype() != null && depreportvo.getDatasourcetype() != 0)
		{
			conditionSql += " AND SENDTYPE="+depreportvo.getDatasourcetype();
		}
		
		//运营商   0国内；  1国外； 为null或其他值则为全部；
		if(depreportvo.getSpnumtype() != null)
		{
			if(depreportvo.getSpnumtype()==0)
			{
				conditionSql += " AND SPISUNCM IN (0,1,21) ";
			}
			else if(depreportvo.getSpnumtype()==1)
			{
				conditionSql += " AND SPISUNCM=5 ";
			}
		}
		
		// 时间条件的查询
		String timewheresql = " WHERE IYMD>="+ depreportvo.getSendTime()+ " AND IYMD<="+ depreportvo.getEndTime() + " ";

		//机构语句
		StringBuffer depsql = new StringBuffer("select DEP_ID,'[机构]'||NVL(DEP_NAME,'未知机构') DEP_NAME,null USERSTATE,'1' IDTYPE");
		
		//机构成功数和失败数，用A分隔
		String depsucc = "select NVL(sum(ICOUNT),0) || '"+RptStaticValue.SUCC_AND_FAIL_SPLIT+"' || NVL(sum(RSUCC),0) || '"+RptStaticValue.SUCC_AND_FAIL_SPLIT+"' || NVL(sum(RFAIL1),0) || '"+RptStaticValue.SUCC_AND_FAIL_SPLIT+"' || NVL(sum(RFAIL2),0) || '"+RptStaticValue.SUCC_AND_FAIL_SPLIT+"' || NVL(sum(RNRET),0) from (select ICOUNT,RSUCC,RFAIL1,RFAIL2,RNRET,P1,P2 from " + tablename+timewheresql+conditionSql+") dp ";
		//有p2参数，即机构
		if ("true".equals(depcodethird2p2)) 
		{
			depsucc += " left join LF_DEP dep on dep.DEP_CODE_THIRD=dp.P2 ";
		}
		//没开启p2参数，则通过p1的操作员id来关联
		else
		{
			depsucc += " left join LF_SYSUSER su on su.USER_CODE=dp.P1 left join LF_DEP dep on dep.DEP_ID=su.DEP_ID "; 
		}
		depsucc += " where dep.DEP_ID<>"+ depId + " and  d.DEP_PATH=SUBSTR(dep.DEP_PATH,0,length(d.DEP_PATH)) AND dep.DEP_STATE=1 ";

		//关联的部门sql
		depsql.append(",").append("(" + depsucc + ") SUCCANDFAIL")
				.append(" from LF_DEP d where SUPERIOR_ID=" + depId+" AND d.DEP_STATE=1 " + " AND d.CORP_CODE = '" + corpCode + "'");
		
		StringBuffer usersql =new StringBuffer();
		if("true".equals(depcodethird2p2)){
			usersql=new StringBuffer("select LS.USER_ID,'[操作员]'||NVL(MAX(LS.USER_NAME),'未知操作员') USER_NAME,MAX(LS.USER_STATE) USERSTATE,'2' IDTYPE");
			//操作员成功数和失败数，用A分隔
			String usersuccs = "(NVL(sum(ICOUNT),0) || '"+RptStaticValue.SUCC_AND_FAIL_SPLIT+"' || NVL(sum(RSUCC),0) || '"+RptStaticValue.SUCC_AND_FAIL_SPLIT+"' || NVL(sum(RFAIL1),0) || '"+RptStaticValue.SUCC_AND_FAIL_SPLIT+"' || NVL(sum(RFAIL2),0) || '"+RptStaticValue.SUCC_AND_FAIL_SPLIT+"' || NVL(sum(RNRET),0)) SUCCANDFAIL";
			//操作员语句
			usersql.append(",").append(usersuccs)
			.append(" from (select ICOUNT,RSUCC,RFAIL1,RFAIL2,RNRET,P1,P2 from " + tablename+timewheresql+conditionSql+") dp left join LF_DEP dep on dep.DEP_CODE_THIRD=dp.P2 LEFT JOIN LF_SYSUSER LS ON LS.USER_CODE=dp.P1 WHERE dep.dep_id=" + depId+" GROUP BY LS.USER_ID ");
		}else{
			usersql=new StringBuffer("select USER_ID,'[操作员]'||NVL(USER_NAME,'未知操作员') USER_NAME,USER_STATE USERSTATE,'2' IDTYPE");
			
			//操作员成功数和失败数，用A分隔
			String usersuccs = "(select NVL(sum(ICOUNT),0) || '"+RptStaticValue.SUCC_AND_FAIL_SPLIT+"' || NVL(sum(RSUCC),0) || '"+RptStaticValue.SUCC_AND_FAIL_SPLIT+"' || NVL(sum(RFAIL1),0) || '"+RptStaticValue.SUCC_AND_FAIL_SPLIT+"' || NVL(sum(RFAIL2),0) || '"+RptStaticValue.SUCC_AND_FAIL_SPLIT+"' || NVL(sum(RNRET),0) from " + tablename + " dp " + timewheresql + " and dp.P1=sysuser.USER_CODE" + conditionSql + ") SUCCANDFAIL";
			//操作员语句
			usersql.append(",").append(usersuccs)
					.append(" from LF_SYSUSER sysuser where sysuser.USER_ID<>1 and sysuser.DEP_ID=" + depId + " " + " AND sysuser.CORP_CODE = '" + corpCode + "'");
			
		}
		
		
		//拼接部门sql和操作员sql
		StringBuffer sql = new StringBuffer().append(depsql).append(" UNION ALL ").append(usersql);
		
		//获取企业的顶级机构id
		int dep_id = getInt("dep_Id",
				"select dep_id from  lf_dep where corp_code='" + corpCode + "' and dep_level=1", StaticValue.EMP_POOLNAME);
		//企业顶级机构id
		String dep = dep_id + "";
		//如果选择的机构是顶级机构，则要把未知机构的查出来
		if (dep.equals(depId)) 
		{
			//有p2参数，即机构
			if ("true".equals(depcodethird2p2)) 
			{
				sql.append(" UNION ALL SELECT null,'未知机构',-1 USERSTATE,'3' IDTYPE, NVL(sum(ICOUNT),0) || '"+RptStaticValue.SUCC_AND_FAIL_SPLIT+"' || NVL(sum(RSUCC),0) || '"+RptStaticValue.SUCC_AND_FAIL_SPLIT+"' || NVL(sum(RFAIL1),0) || '"+RptStaticValue.SUCC_AND_FAIL_SPLIT+"' || NVL(sum(RFAIL2),0) || '"+RptStaticValue.SUCC_AND_FAIL_SPLIT+"' || NVL(sum(RNRET),0) SUCCANDFAIL  from "+tablename+" dp left join LF_DEP dep on dep.DEP_CODE_THIRD=dp.P2  "
						+ timewheresql+ conditionSql+" AND dep.DEP_CODE_THIRD is null ");
			}else{
				sql.append(" UNION ALL SELECT null,'未知机构',-1 USERSTATE,'3' IDTYPE, NVL(sum(ICOUNT),0) || '"+RptStaticValue.SUCC_AND_FAIL_SPLIT+"' || NVL(sum(RSUCC),0) || '"+RptStaticValue.SUCC_AND_FAIL_SPLIT+"' || NVL(sum(RFAIL1),0) || '"+RptStaticValue.SUCC_AND_FAIL_SPLIT+"' || NVL(sum(RFAIL2),0) || '"+RptStaticValue.SUCC_AND_FAIL_SPLIT+"' || NVL(sum(RNRET),0) SUCCANDFAIL  from "+tablename+" dp left join LF_SYSUSER su on su.USER_CODE=dp.p1 "
						+ timewheresql+ conditionSql+" AND su.user_id is null ");
			}
		}
		
		String totalsql = "select tot.DEP_ID, tot.DEP_NAME, REGEXP_SUBSTR(tot.SUCCANDFAIL, '[^"+RptStaticValue.SUCC_AND_FAIL_SPLIT+"]+',1,1) ICOUNT," +
				"REGEXP_SUBSTR(tot.SUCCANDFAIL, '[^"+RptStaticValue.SUCC_AND_FAIL_SPLIT+"]+',1,2) RSUCC," +
						"REGEXP_SUBSTR(tot.SUCCANDFAIL, '[^"+RptStaticValue.SUCC_AND_FAIL_SPLIT+"]+',1,3) RFAIL1," +
								"REGEXP_SUBSTR(tot.SUCCANDFAIL, '[^"+RptStaticValue.SUCC_AND_FAIL_SPLIT+"]+',1,4) RFAIL2," +
										"REGEXP_SUBSTR(tot.SUCCANDFAIL, '[^"+RptStaticValue.SUCC_AND_FAIL_SPLIT+"]+',1,5) RNRET, tot.USERSTATE USER_STATE,tot.IDTYPE IDTYPE,null USER_ID,null NAME from (" + sql.toString() + ") tot ";
		
		return totalsql;
	}
	
	/**
	 * 短信机构报表sql语句生成
	 * @param depId
	 * @param depreportvo
	 * @param corpCode
	 * @return
	 * @throws Exception
	 */
	public String getMysqlSql(String depId, DepRptVo depreportvo,String corpCode, String tablename, String depcodethird2p2) throws Exception 
	{
		// 报表数据查询条件
		String conditionSql = " ";
		
		//发送类型
		if(depreportvo.getDatasourcetype() != null && depreportvo.getDatasourcetype() != 0)
		{
			conditionSql += " AND SENDTYPE="+depreportvo.getDatasourcetype();
		}
		
		//运营商   0国内；  1国外； 为null或其他值则为全部；
		if(depreportvo.getSpnumtype() != null)
		{
			if(depreportvo.getSpnumtype()==0)
			{
				conditionSql += " AND SPISUNCM IN (0,1,21) ";
			}
			else if(depreportvo.getSpnumtype()==1)
			{
				conditionSql += " AND SPISUNCM=5 ";
			}
		}
		
		// 时间条件的查询
		String timewheresql = " WHERE IYMD>="+ depreportvo.getSendTime()+ " AND IYMD<="+ depreportvo.getEndTime() + " ";
		
		//机构语句
		StringBuffer depsql = new StringBuffer("select DEP_ID,CONCAT('[机构]',IFNULL(DEP_NAME,'未知机构')) DEP_NAME,null USERSTATE,'1' IDTYPE");
		
		String depsucc = "select CONCAT(IFNULL(sum(ICOUNT),0), '"+RptStaticValue.SUCC_AND_FAIL_SPLIT+
				"' , IFNULL(sum(RSUCC),0) , '"+RptStaticValue.SUCC_AND_FAIL_SPLIT+
				"' , IFNULL(sum(RFAIL1),0) , '"+RptStaticValue.SUCC_AND_FAIL_SPLIT+
				"' , IFNULL(sum(RFAIL2),0) , '"+RptStaticValue.SUCC_AND_FAIL_SPLIT+
				"' , IFNULL(sum(RNRET),0)) from (select ICOUNT,RSUCC,RFAIL1,RFAIL2,RNRET,P1,P2 from " + tablename + timewheresql + conditionSql+") dp ";
		
		if ("true".equals(depcodethird2p2)) 
		{
			depsucc += " left join LF_DEP dep on dep.DEP_CODE_THIRD=dp.P2 ";
		}
		else
		{
			depsucc += " left join LF_SYSUSER su on su.USER_CODE=dp.P1 left join LF_DEP dep on dep.DEP_ID=su.DEP_ID ";
		}
		
		depsucc += " where dep.DEP_ID<>" + depId + " and d.DEP_PATH=SUBSTRING(dep.DEP_PATH,1,length(d.DEP_PATH)) AND dep.DEP_STATE=1 ";

		//关联的部门sql
		depsql.append(",").append("(" + depsucc + ") SUCCANDFAIL")
				.append(" from LF_DEP d where SUPERIOR_ID=" + depId + " AND d.DEP_STATE=1 AND d.CORP_CODE = '" + corpCode + "'");
		//操作员语句
		StringBuffer usersql = new StringBuffer();
		if("true".equals(depcodethird2p2)){
			usersql=new StringBuffer("select LS.USER_ID DEP_ID,CONCAT('[操作员]',IFNULL(MAX(LS.USER_NAME),'未知操作员')) DEP_NAME,MAX(LS.USER_STATE) USERSTATE,'2' IDTYPE");
			//操作员成功数和失败数，用A分隔
			String usersuccs = " (CONCAT(IFNULL(sum(ICOUNT),0), '"+RptStaticValue.SUCC_AND_FAIL_SPLIT+
				"' , IFNULL(sum(RSUCC),0) , '"+RptStaticValue.SUCC_AND_FAIL_SPLIT+
				"' , IFNULL(sum(RFAIL1),0) , '"+RptStaticValue.SUCC_AND_FAIL_SPLIT+
				"' , IFNULL(sum(RFAIL2),0) , '"+RptStaticValue.SUCC_AND_FAIL_SPLIT+
				"' , IFNULL(sum(RNRET),0))) SUCCANDFAIL";
			//操作员语句
			usersql.append(",").append(usersuccs)
			.append(" from (select ICOUNT,RSUCC,RFAIL1,RFAIL2,RNRET,P1,P2 from " + tablename+timewheresql+conditionSql+") dp left join LF_DEP dep on dep.DEP_CODE_THIRD=dp.P2 LEFT JOIN LF_SYSUSER LS ON LS.USER_CODE=dp.P1 WHERE dep.dep_id=" + depId+" GROUP BY LS.USER_ID ");
		}else{
			usersql = new StringBuffer("select USER_ID DEP_ID,CONCAT('[操作员]',IFNULL(USER_NAME,'未知操作员')) DEP_NAME,USER_STATE USERSTATE,'2' IDTYPE");
			//操作员成功数
			String usersuccs = "(select CONCAT(IFNULL(sum(ICOUNT),0), '"+RptStaticValue.SUCC_AND_FAIL_SPLIT+
				"' , IFNULL(sum(RSUCC),0) , '"+RptStaticValue.SUCC_AND_FAIL_SPLIT+
				"' , IFNULL(sum(RFAIL1),0) , '"+RptStaticValue.SUCC_AND_FAIL_SPLIT+
				"' , IFNULL(sum(RFAIL2),0) , '"+RptStaticValue.SUCC_AND_FAIL_SPLIT+
				"' , IFNULL(sum(RNRET),0)) from " + tablename + " dp " + timewheresql + " and dp.P1=sysuser.USER_CODE" + conditionSql + ") SUCCANDFAIL";
			//操作员语句
			usersql.append(",").append(usersuccs)
				.append(" from LF_SYSUSER sysuser where sysuser.USER_ID<>1 and sysuser.DEP_ID=" + depId + " AND sysuser.CORP_CODE = '" + corpCode + "'");
		}
			
		//拼接部门sql和操作员sql
		StringBuffer sql = new StringBuffer().append(depsql).append(" UNION ALL ").append(usersql);
		
		int dep_id = getInt("dep_Id",
				"select dep_id from  lf_dep where corp_code='" + corpCode + "' and dep_level=1", StaticValue.EMP_POOLNAME);
		String dep = dep_id + "";
		
		//如果选择的机构是顶级机构，则要把未知机构的查出来
		if (dep.equals(depId)) 
		{
			if ("true".equals(depcodethird2p2)) 
			{
				sql.append(" UNION ALL SELECT 0 DEP_ID,'未知机构' DEP_NAME,-1 USERSTATE,'3' IDTYPE, CONCAT(IFNULL(sum(ICOUNT),0), '"+RptStaticValue.SUCC_AND_FAIL_SPLIT+
						"' , IFNULL(sum(RSUCC),0) , '"+RptStaticValue.SUCC_AND_FAIL_SPLIT+
						"' , IFNULL(sum(RFAIL1),0) , '"+RptStaticValue.SUCC_AND_FAIL_SPLIT+
						"' , IFNULL(sum(RFAIL2),0) , '"+RptStaticValue.SUCC_AND_FAIL_SPLIT+
						"' , IFNULL(sum(RNRET),0)) SUCCANDFAIL from "+tablename+" dp left join LF_DEP dep on dep.DEP_CODE_THIRD=dp.P2 " 
							+ timewheresql + conditionSql + " AND COALESCE(dep.DEP_CODE_THIRD,0)=0 ");
			}else{
				sql.append(" UNION ALL SELECT 0 DEP_ID,'未知机构' DEP_NAME,-1 USERSTATE,'3' IDTYPE, CONCAT(IFNULL(sum(ICOUNT),0), '"+RptStaticValue.SUCC_AND_FAIL_SPLIT+
					"' , IFNULL(sum(RSUCC),0) , '"+RptStaticValue.SUCC_AND_FAIL_SPLIT+
					"' , IFNULL(sum(RFAIL1),0) , '"+RptStaticValue.SUCC_AND_FAIL_SPLIT+
					"' , IFNULL(sum(RFAIL2),0) , '"+RptStaticValue.SUCC_AND_FAIL_SPLIT+
					"' , IFNULL(sum(RNRET),0)) SUCCANDFAIL from "+tablename+" dp left join LF_SYSUSER su on su.USER_CODE=dp.p1 " 
						+ timewheresql + conditionSql + " AND COALESCE(su.user_id,0)=0 ");
			}
		}
		
		String totalsql = "select tot.DEP_ID, tot.DEP_NAME,SUBSTRING_INDEX(tot.SUCCANDFAIL, '"+RptStaticValue.SUCC_AND_FAIL_SPLIT+"', 1) ICOUNT," +
				"SUBSTRING_INDEX(SUBSTRING_INDEX(tot.SUCCANDFAIL, '"+RptStaticValue.SUCC_AND_FAIL_SPLIT+"', 2), '"+RptStaticValue.SUCC_AND_FAIL_SPLIT+"', -1) RSUCC," +
						"SUBSTRING_INDEX(SUBSTRING_INDEX(tot.SUCCANDFAIL, '"+RptStaticValue.SUCC_AND_FAIL_SPLIT+"', 3), '"+RptStaticValue.SUCC_AND_FAIL_SPLIT+"', -1) RFAIL1," +
								"SUBSTRING_INDEX(SUBSTRING_INDEX(tot.SUCCANDFAIL, '"+RptStaticValue.SUCC_AND_FAIL_SPLIT+"', 4), '"+RptStaticValue.SUCC_AND_FAIL_SPLIT+"', -1) RFAIL2," +
										"SUBSTRING_INDEX(tot.SUCCANDFAIL, '"+RptStaticValue.SUCC_AND_FAIL_SPLIT+"', -1) RNRET, tot.USERSTATE USER_STATE,tot.IDTYPE IDTYPE,null USER_ID,null NAME from (" + sql.toString() + ") tot ";
		
		return totalsql;
	}

	/**
	 * 短信机构报表sql语句生成
	 * @param depId
	 * @param depreportvo
	 * @param corpCode
	 * @return
	 * @throws Exception
	 */
	public String getDB2Sql(String depId, DepRptVo depreportvo,String corpCode, String tablename, String depcodethird2p2) throws Exception 
	{
		// 报表数据查询条件
		String conditionSql = " ";
		
		//发送类型
		if(depreportvo.getDatasourcetype() != null && depreportvo.getDatasourcetype() != 0)
		{
			conditionSql += " AND SENDTYPE="+depreportvo.getDatasourcetype();
		}
		
		//运营商   0国内；  1国外； 为null或其他值则为全部；
		if(depreportvo.getSpnumtype() != null)
		{
			if(depreportvo.getSpnumtype()==0)
			{
				conditionSql += " AND SPISUNCM IN (0,1,21) ";
			}
			else if(depreportvo.getSpnumtype()==1)
			{
				conditionSql += " AND SPISUNCM=5 ";
			}
		}
		
		// 时间条件的查询
		String timewheresql = " WHERE IYMD>="+ depreportvo.getSendTime()+ " AND IYMD<="+ depreportvo.getEndTime() + " ";

		//机构语句
		StringBuffer depsql = new StringBuffer("select DEP_ID,'[机构]'||COALESCE(DEP_NAME,'未知机构') DEP_NAME,-1 USERSTATE,'1' IDTYPE");
		
		String depsucc = "select COALESCE(sum(ICOUNT),0)||'"
			+RptStaticValue.SUCC_AND_FAIL_SPLIT+"'||COALESCE(sum(RSUCC),0)||'"
			+RptStaticValue.SUCC_AND_FAIL_SPLIT+"'||COALESCE(sum(RFAIL1),0)||'"
			+RptStaticValue.SUCC_AND_FAIL_SPLIT+"'||COALESCE(sum(RFAIL2),0)||'"
			+RptStaticValue.SUCC_AND_FAIL_SPLIT+"'||COALESCE(sum(RNRET),0) from (select ICOUNT,RSUCC,RFAIL1,RFAIL2,RNRET,P1,P2 from " + tablename + timewheresql + conditionSql + ") dp ";
		if ("true".equals(depcodethird2p2)) 
		{
			depsucc += " left join LF_DEP dep on dep.DEP_CODE_THIRD=dp.P2 ";
		}
		else
		{
			depsucc += " left join LF_SYSUSER su on su.USER_CODE=dp.P1 left join LF_DEP dep on dep.DEP_ID=su.DEP_ID ";
		} 
		depsucc += " where dep.DEP_ID<>" + depId + " and  d.DEP_PATH=SUBSTR(dep.DEP_PATH,1,length(d.DEP_PATH)) AND dep.DEP_STATE=1 ";
		
		//关联的部门sql
		depsql.append(",").append("(" + depsucc + ") SUCCANDFAIL")
				.append(" from LF_DEP d where SUPERIOR_ID=" + depId + " AND d.DEP_STATE=1 AND d.CORP_CODE = '" + corpCode + "'");

		//操作员语句
		StringBuffer usersql = new StringBuffer();
		if("true".equals(depcodethird2p2)){
			usersql=new StringBuffer("select LS.USER_ID DEP_ID,'[操作员]'||COALESCE(MAX(LS.USER_NAME),'未知操作员') DEP_NAME,MAX(LS.USER_STATE) USERSTATE,'2' IDTYPE");
			//操作员成功数和失败数，用A分隔
			String usersuccs = " (COALESCE(sum(ICOUNT),0)||'"
				+RptStaticValue.SUCC_AND_FAIL_SPLIT+"'||COALESCE(sum(RSUCC),0)||'"
				+RptStaticValue.SUCC_AND_FAIL_SPLIT+"'||COALESCE(sum(RFAIL1),0)||'"
				+RptStaticValue.SUCC_AND_FAIL_SPLIT+"'||COALESCE(sum(RFAIL2),0)||'"
				+RptStaticValue.SUCC_AND_FAIL_SPLIT+"'||COALESCE(sum(RNRET),0)) SUCCANDFAIL";
			//操作员语句
			usersql.append(",").append(usersuccs)
			.append(" from (select ICOUNT,RSUCC,RFAIL1,RFAIL2,RNRET,P1,P2 from " + tablename+timewheresql+conditionSql+") dp left join LF_DEP dep on dep.DEP_CODE_THIRD=dp.P2 LEFT JOIN LF_SYSUSER LS ON LS.USER_CODE=dp.P1 WHERE dep.dep_id=" + depId+" GROUP BY LS.USER_ID ");
		}else{
			usersql = new StringBuffer("select USER_ID DEP_ID,'[操作员]'||COALESCE(USER_NAME,'未知操作员') DEP_NAME,USER_STATE USERSTATE,'2' IDTYPE");
			String usersucc = "select COALESCE(sum(ICOUNT),0)||'"
				+RptStaticValue.SUCC_AND_FAIL_SPLIT+"'||COALESCE(sum(RSUCC),0)||'"
				+RptStaticValue.SUCC_AND_FAIL_SPLIT+"'||COALESCE(sum(RFAIL1),0)||'"
				+RptStaticValue.SUCC_AND_FAIL_SPLIT+"'||COALESCE(sum(RFAIL2),0)||'"
				+RptStaticValue.SUCC_AND_FAIL_SPLIT+"'||COALESCE(sum(RNRET),0) from " + tablename + " dp " + timewheresql + " and dp.P1=sysuser.USER_CODE";
			//操作员语句
			usersql.append(",").append("(" + usersucc + conditionSql + ") SUCCANDFAIL")
					.append(" from LF_SYSUSER sysuser where sysuser.USER_ID<>1 and sysuser.DEP_ID=" + depId + " AND sysuser.CORP_CODE = '" + corpCode + "'");
		}
		
		StringBuffer sql = new StringBuffer().append(depsql).append(" UNION ALL ").append(usersql);
		
		int dep_id = getInt("dep_Id",
				"select dep_id from  lf_dep where corp_code='" + corpCode + "' and dep_level=1", StaticValue.EMP_POOLNAME);
		String dep = dep_id + "";
		
		if (dep.equals(depId)) 
		{
			if ("true".equals(depcodethird2p2)) 
			{
				sql.append(" UNION ALL SELECT 0 DEP_ID,'未知机构' DEP_NAME,-1 USERSTATE,'3' IDTYPE, COALESCE(sum(ICOUNT),0)||'"
				+RptStaticValue.SUCC_AND_FAIL_SPLIT+"'||COALESCE(sum(RSUCC),0)||'"
				+RptStaticValue.SUCC_AND_FAIL_SPLIT+"'||COALESCE(sum(RFAIL1),0)||'"
				+RptStaticValue.SUCC_AND_FAIL_SPLIT+"'||COALESCE(sum(RFAIL2),0)||'"
				+RptStaticValue.SUCC_AND_FAIL_SPLIT+"'||COALESCE(sum(RNRET),0) SUCCANDFAIL  from "+tablename+" dp left join LF_DEP dep on dep.DEP_CODE_THIRD=dp.P2 "
						+ timewheresql+ conditionSql+" AND COALESCE(dep.DEP_CODE_THIRD,0)=0 ");
			}else{
				sql.append(" UNION ALL SELECT 0 DEP_ID,'未知机构' DEP_NAME,-1 USERSTATE,'3' IDTYPE, COALESCE(sum(ICOUNT),0)||'"
					+RptStaticValue.SUCC_AND_FAIL_SPLIT+"'||COALESCE(sum(RSUCC),0)||'"
					+RptStaticValue.SUCC_AND_FAIL_SPLIT+"'||COALESCE(sum(RFAIL1),0)||'"
					+RptStaticValue.SUCC_AND_FAIL_SPLIT+"'||COALESCE(sum(RFAIL2),0)||'"
					+RptStaticValue.SUCC_AND_FAIL_SPLIT+"'||COALESCE(sum(RNRET),0) SUCCANDFAIL  from "+tablename+" dp left join LF_SYSUSER su on su.USER_CODE=dp.p1 "
							+ timewheresql+ conditionSql+" AND COALESCE(su.user_id,0)=0 ");
			}
		}
		
		String totalsql = "select tot.DEP_ID, tot.DEP_NAME, SUBSTR(tot.SUCCANDFAIL,1,INSTR(tot.SUCCANDFAIL,'"+RptStaticValue.SUCC_AND_FAIL_SPLIT+"',1,1)-1) ICOUNT," +
				"SUBSTR(SUBSTR(tot.SUCCANDFAIL,INSTR(tot.SUCCANDFAIL,'"+RptStaticValue.SUCC_AND_FAIL_SPLIT+"',1,1)+1),1,INSTR(SUBSTR(tot.SUCCANDFAIL,INSTR(tot.SUCCANDFAIL,'"+RptStaticValue.SUCC_AND_FAIL_SPLIT+"',1,1)+1),'"+RptStaticValue.SUCC_AND_FAIL_SPLIT+"',1,1)-1) RSUCC," +
						"SUBSTR(SUBSTR(tot.SUCCANDFAIL,INSTR(tot.SUCCANDFAIL,'"+RptStaticValue.SUCC_AND_FAIL_SPLIT+"',1,2)+1),1,INSTR(SUBSTR(tot.SUCCANDFAIL,INSTR(tot.SUCCANDFAIL,'"+RptStaticValue.SUCC_AND_FAIL_SPLIT+"',1,2)+1),'"+RptStaticValue.SUCC_AND_FAIL_SPLIT+"',1,1)-1) RFAIL1," +
								"SUBSTR(SUBSTR(tot.SUCCANDFAIL,INSTR(tot.SUCCANDFAIL,'"+RptStaticValue.SUCC_AND_FAIL_SPLIT+"',1,3)+1),1,INSTR(SUBSTR(tot.SUCCANDFAIL,INSTR(tot.SUCCANDFAIL,'"+RptStaticValue.SUCC_AND_FAIL_SPLIT+"',1,3)+1),'"+RptStaticValue.SUCC_AND_FAIL_SPLIT+"',1,1)-1) RFAIL2," +
										"SUBSTR(tot.SUCCANDFAIL,INSTR(tot.SUCCANDFAIL,'"+RptStaticValue.SUCC_AND_FAIL_SPLIT+"',1,4)+1) RNRET , tot.USERSTATE USER_STATE, tot.IDTYPE IDTYPE,0 USER_ID,0 NAME from (" + sql.toString() + ") tot ";

		return totalsql;
	}
	
	
	
	/**
	 * 短信机构报表sql语句生成
	 * @param depId
	 * @param depreportvo
	 * @param corpCode
	 * @return
	 * @throws Exception
	 */
	public String getMsSql(String depId, DepRptVo depreportvo,String corpCode, String tablename, String depcodethird2p2) throws Exception {
		// 报表数据查询条件
		String conditionSql = " ";
		
		//发送类型
		if(depreportvo.getDatasourcetype() != null && depreportvo.getDatasourcetype() != 0)
		{
			conditionSql += " AND SENDTYPE="+depreportvo.getDatasourcetype();
		}
		
		//运营商   0国内；  1国外； 为null或其他值则为全部；
		if(depreportvo.getSpnumtype() != null)
		{
			if(depreportvo.getSpnumtype()==0)
			{
				conditionSql += " AND SPISUNCM IN (0,1,21) ";
			}
			else if(depreportvo.getSpnumtype()==1)
			{
				conditionSql += " AND SPISUNCM=5 ";
			}
		}
		
		// 时间条件的查询
		String timewheresql = " WHERE IYMD>="+ depreportvo.getSendTime()+ " AND IYMD<="+ depreportvo.getEndTime() + " ";

		//机构语句
		StringBuffer depsql =new StringBuffer("select DEP_ID,'[机构]'+ISNULL(DEP_NAME,'未知机构') DEP_NAME,null USERSTATE,'1' IDTYPE");
		
		String depsucc = "select CAST(ISNULL(SUM(ICOUNT),0) AS VARCHAR)+'"
			+RptStaticValue.SUCC_AND_FAIL_SPLIT+"'+CAST(ISNULL(SUM(RSUCC),0) AS VARCHAR)+'"
			+RptStaticValue.SUCC_AND_FAIL_SPLIT+"'+CAST(ISNULL(SUM(RFAIL1),0) AS VARCHAR)+'"
			+RptStaticValue.SUCC_AND_FAIL_SPLIT+"'+CAST(ISNULL(SUM(RFAIL2),0) AS VARCHAR)+'"
			+RptStaticValue.SUCC_AND_FAIL_SPLIT+"'+CAST(ISNULL(SUM(RNRET),0) AS VARCHAR) from (select ICOUNT,RSUCC,RFAIL1,RFAIL2,RNRET,P1,P2 from " + tablename + timewheresql + conditionSql + ") dp ";
		
		if("true".equals(depcodethird2p2))
		{
			depsucc += " left join LF_DEP dep on dep.DEP_CODE_THIRD=dp.P2 ";
		}
		else
		{
			depsucc += " left join LF_SYSUSER su on su.USER_CODE=dp.P1 left join LF_DEP dep on dep.DEP_ID=su.DEP_ID ";
		}
				
		depsucc += " where dep.DEP_ID<>" + depId + " and  d.DEP_PATH=SUBSTRING(dep.DEP_PATH,0,len(d.DEP_PATH)+1) AND dep.DEP_STATE=1 ";

		//关联的部门sql
		depsql.append(",").append("(" + depsucc + ") SUCCANDFAIL")
				.append(" from LF_DEP d where  SUPERIOR_ID=" + depId + "  AND d.DEP_STATE=1 AND d.CORP_CODE = '" + corpCode + "'");
		//操作员语句
		StringBuffer usersql = new StringBuffer();
		if("true".equals(depcodethird2p2)){
			usersql=new StringBuffer("select LS.USER_ID,'[操作员]'+ISNULL(MAX(LS.USER_NAME),'未知操作员') USER_NAME,MAX(LS.USER_STATE) USERSTATE,'2' IDTYPE");
			//操作员成功数和失败数，用A分隔
			String usersuccs = " (CAST(ISNULL(SUM(ICOUNT),0) AS VARCHAR)+'"
				+RptStaticValue.SUCC_AND_FAIL_SPLIT+"'+CAST(ISNULL(SUM(RSUCC),0) AS VARCHAR)+'"
				+RptStaticValue.SUCC_AND_FAIL_SPLIT+"'+CAST(ISNULL(SUM(RFAIL1),0) AS VARCHAR)+'"
				+RptStaticValue.SUCC_AND_FAIL_SPLIT+"'+CAST(ISNULL(SUM(RFAIL2),0) AS VARCHAR)+'"
				+RptStaticValue.SUCC_AND_FAIL_SPLIT+"'+CAST(ISNULL(SUM(RNRET),0) AS VARCHAR)) SUCCANDFAIL";
			//操作员语句
			usersql.append(",").append(usersuccs)
			.append(" from (select ICOUNT,RSUCC,RFAIL1,RFAIL2,RNRET,P1,P2 from " + tablename+timewheresql+conditionSql+") dp left join LF_DEP dep on dep.DEP_CODE_THIRD=dp.P2 LEFT JOIN LF_SYSUSER LS ON LS.USER_CODE=dp.P1 WHERE dep.dep_id=" + depId+" GROUP BY LS.USER_ID ");
		}else{
			usersql = new StringBuffer("select USER_ID,'[操作员]'+ISNULL(USER_NAME,'未知操作员') USER_NAME,USER_STATE USERSTATE,'2' IDTYPE");
			String usersucc = "select CAST(ISNULL(SUM(ICOUNT),0) AS VARCHAR)+'"
				+RptStaticValue.SUCC_AND_FAIL_SPLIT+"'+CAST(ISNULL(SUM(RSUCC),0) AS VARCHAR)+'"
				+RptStaticValue.SUCC_AND_FAIL_SPLIT+"'+CAST(ISNULL(SUM(RFAIL1),0) AS VARCHAR)+'"
				+RptStaticValue.SUCC_AND_FAIL_SPLIT+"'+CAST(ISNULL(SUM(RFAIL2),0) AS VARCHAR)+'"
				+RptStaticValue.SUCC_AND_FAIL_SPLIT+"'+CAST(ISNULL(SUM(RNRET),0) AS VARCHAR) from " + tablename + " dp " + timewheresql + " and dp.P1=sysuser.USER_CODE";
			//操作员语句
			usersql.append(",").append("(" + usersucc + conditionSql + ") SUCCANDFAIL")
					.append(" from LF_SYSUSER sysuser where sysuser.USER_ID<>1  and sysuser.DEP_ID=" + depId + " AND sysuser.CORP_CODE = '" + corpCode + "'");
		}
		

		StringBuffer sql = new StringBuffer().append(depsql).append(" UNION ALL ").append(usersql);
		
		int dep_id = getInt("dep_Id",
				"select dep_id from  lf_dep where corp_code='" + corpCode + "' and dep_level=1", StaticValue.EMP_POOLNAME);
		String dep = dep_id + "";
		
		if (dep.equals(depId)) 
		{
			if("true".equals(depcodethird2p2))
			{
				sql.append(" UNION ALL SELECT null,'未知机构',-1 USERSTATE,'3' IDTYPE,CAST(ISNULL(SUM(ICOUNT),0) AS VARCHAR)+'"
						+RptStaticValue.SUCC_AND_FAIL_SPLIT+"'+CAST(ISNULL(SUM(RSUCC),0) AS VARCHAR)+'"
						+RptStaticValue.SUCC_AND_FAIL_SPLIT+"'+CAST(ISNULL(SUM(RFAIL1),0) AS VARCHAR)+'"
						+RptStaticValue.SUCC_AND_FAIL_SPLIT+"'+CAST(ISNULL(SUM(RFAIL2),0) AS VARCHAR)+'"
						+RptStaticValue.SUCC_AND_FAIL_SPLIT+"'+CAST(ISNULL(SUM(RNRET),0) AS VARCHAR)  from (SELECT d.* FROM "+tablename+" d "
						+" left join (select corp_code,spuser from lf_sp_dep_bind group by corp_code,spuser) lsdb on d.userid=lsdb.spuser where lsdb.corp_code='"+corpCode+"') dp "
						+" left join LF_DEP dep on dep.DEP_CODE_THIRD=dp.P2 "
						+ timewheresql+ conditionSql+" AND dep.DEP_CODE_THIRD is null ");
			}else{
				sql.append(" UNION ALL SELECT null,'未知机构',-1 USERSTATE,'3' IDTYPE,CAST(ISNULL(SUM(ICOUNT),0) AS VARCHAR)+'"
				+RptStaticValue.SUCC_AND_FAIL_SPLIT+"'+CAST(ISNULL(SUM(RSUCC),0) AS VARCHAR)+'"
				+RptStaticValue.SUCC_AND_FAIL_SPLIT+"'+CAST(ISNULL(SUM(RFAIL1),0) AS VARCHAR)+'"
				+RptStaticValue.SUCC_AND_FAIL_SPLIT+"'+CAST(ISNULL(SUM(RFAIL2),0) AS VARCHAR)+'"
				+RptStaticValue.SUCC_AND_FAIL_SPLIT+"'+CAST(ISNULL(SUM(RNRET),0) AS VARCHAR)  from (SELECT d.* FROM "+tablename+" d "
				+" left join (select corp_code,spuser from lf_sp_dep_bind group by corp_code,spuser) lsdb on d.userid=lsdb.spuser where lsdb.corp_code='"+corpCode+"') dp "
				+" left join LF_SYSUSER su on su.USER_CODE=dp.p1 "
				+ timewheresql+ conditionSql+" AND su.user_id is null ");
			}
		}
		
		String totalsql = "select tot.DEP_ID, tot.DEP_NAME, CAST(SUBSTRING(tot.SUCCANDFAIL,0,CHARINDEX('"+RptStaticValue.SUCC_AND_FAIL_SPLIT+"',tot.SUCCANDFAIL)) AS BIGINT) ICOUNT," +
				"CAST(PARSENAME(REPLACE(SUBSTRING(tot.SUCCANDFAIL,CHARINDEX('A',tot.SUCCANDFAIL)+1,LEN(tot.SUCCANDFAIL)),'"+RptStaticValue.SUCC_AND_FAIL_SPLIT+"','.'),4) AS BIGINT) RSUCC," +
						"CAST(PARSENAME(REPLACE(SUBSTRING(tot.SUCCANDFAIL,CHARINDEX('A',tot.SUCCANDFAIL)+1,LEN(tot.SUCCANDFAIL)),'"+RptStaticValue.SUCC_AND_FAIL_SPLIT+"','.'),3) AS BIGINT) RFAIL1," +
								"CAST(PARSENAME(REPLACE(SUBSTRING(tot.SUCCANDFAIL,CHARINDEX('A',tot.SUCCANDFAIL)+1,LEN(tot.SUCCANDFAIL)),'"+RptStaticValue.SUCC_AND_FAIL_SPLIT+"','.'),2) AS BIGINT) RFAIL2," +
										"CAST(PARSENAME(REPLACE(SUBSTRING(tot.SUCCANDFAIL,CHARINDEX('A',tot.SUCCANDFAIL)+1,LEN(tot.SUCCANDFAIL)),'"+RptStaticValue.SUCC_AND_FAIL_SPLIT+"','.'),1) AS BIGINT) RNRET ," +
												" tot.USERSTATE USER_STATE, tot.IDTYPE IDTYPE,0 USER_ID,0 NAME from (" + sql.toString() + ") tot ";

		return totalsql;
	}
	
	
	

	
	/**
	 * 短信机构报表sql语句生成
	 * @param depId
	 * @param depreportvo
	 * @param corpCode
	 * @return
	 * @throws Exception
	 */
	public String getSqlByType(String depId, DepRptVo depreportvo,String corpCode) throws Exception {
		String tablename="";
		if(depreportvo.getMstype()!=null
				&&depreportvo.getMstype()==0){
			tablename= TableMtDatareport.TABLE_NAME;
		}else if(depreportvo.getMstype()!=null
				&&depreportvo.getMstype()==1){
			tablename=TableMmsDatareport.TABLE_NAME;
		}else{
			return null;
		}
		StringBuffer sql = new StringBuffer("");
		// 数据库的判空函数 默认为oracle的判断NULL函数
		String nullFunction = "NVL";
		//数据的连接符号码
		String lianjiefuhao = "||";
		switch (StaticValue.DBTYPE) {
		case 1:
			// oracle
			nullFunction = "NVL";
			lianjiefuhao = "||";
			break;
		case 2:
			// sqlserver2005
			nullFunction = "ISNULL";
			lianjiefuhao = "+";
			break;
		case 3:
			// MYSQL
			nullFunction = "IFNULL";
			lianjiefuhao = "CONCAT(";
			break;
		case 4:
			// DB2
			nullFunction = "COALESCE";
			lianjiefuhao = "||";
			break;
		default:
			break;
		}
		
		
		// 是否多企业的条件条件查询
		String conditionSqldep = "  ";
		// 是否多企业的条件条件查询
		String conditionSqluser = "  ";

		if (null != corpCode && 0 != corpCode.length()
				&& StaticValue.getCORPTYPE() == 1) {
			conditionSqldep += " AND d." + TableLfDep.CORP_CODE + " = '"
					+ corpCode + "'";
			conditionSqluser += " AND sysuser." + TableLfSysuser.CORP_CODE
					+ " = '" + corpCode + "'";
		}
		// 报表数据查询条件
		String conditionSql = "  ";
		
		
		
		//发送类型
		if(depreportvo.getDatasourcetype()!=null&&depreportvo.getDatasourcetype()==1){
			conditionSql=conditionSql+" AND SENDTYPE=1 ";
		}else if(depreportvo.getDatasourcetype()!=null&&depreportvo.getDatasourcetype()==2){
			conditionSql=conditionSql+" AND SENDTYPE=2 ";
		}else if(depreportvo.getDatasourcetype()!=null&&depreportvo.getDatasourcetype()==3){
			conditionSql=conditionSql+" AND SENDTYPE=3 ";
		}else if(depreportvo.getDatasourcetype()!=null&&depreportvo.getDatasourcetype()==4){
			conditionSql=conditionSql+" AND SENDTYPE=4 ";
		}
		
		//运营商   0国内  1国外   ""全部
		if(depreportvo.getSpnumtype()!=null&&depreportvo.getSpnumtype()==0){
			conditionSql=conditionSql+" AND SPISUNCM IN (0,1,21) ";
		}else if(depreportvo.getSpnumtype()!=null&&depreportvo.getSpnumtype()==1){
			conditionSql=conditionSql+" AND SPISUNCM=5 ";
		}
		
		
		
		//时间查询条件
		String[] timeStrCondition = new String[3];
		String sendtime=depreportvo.getSendTime().replaceAll("-", "");
		String endtime=depreportvo.getEndTime().replaceAll("-", "");
		timeStrCondition[0] = " WHERE " + TableMtDatareport.IYMD+ ">="+ sendtime + " ";
		timeStrCondition[1] = " WHERE " + TableMtDatareport.IYMD+ "<="+ endtime +" ";
		timeStrCondition[2] = " WHERE IYMD>="+ sendtime+ " AND IYMD<="+ endtime + " ";
		String timewheresql="";
		// 时间条件的查询
		if ((0 != depreportvo.getSendTime().length() && 0 == depreportvo
				.getEndTime().length())) {
			timewheresql += timeStrCondition[0];
		} else if (0 == depreportvo.getSendTime().length()
				&& 0 != depreportvo.getEndTime().length()) {
			timewheresql += timeStrCondition[1];

		} else if (0 != depreportvo.getSendTime().length()
				&& 0 != depreportvo.getEndTime().length()) {
			timewheresql += timeStrCondition[2];
		} else {
			// 如果没有时间查询条件，则默认查询当前年月份的
			String yearAndMonth[] = this.getYearAndMonth();

			if (null != yearAndMonth) {
				timewheresql += " WHERE dp." + TableMtDatareport.Y + "="
						+ yearAndMonth[0] + " " + " AND dp."
						+ TableMtDatareport.IMONTH + "=" + yearAndMonth[1] + "";
			}
		}

		/**
		 * MySql连接
		 */
		//机构语句
		StringBuffer depsql = null;
		if(StaticValue.DBTYPE==3){
			//mysql
			depsql = new StringBuffer("select " + TableLfDep.DEP_ID
					+ ","+ lianjiefuhao +"'[机构]'," + nullFunction + "("
					+ TableLfDep.DEP_NAME + ",'未知机构')) DEP_NAME,null USERSTATE,'1' IDTYPE");
		}else{
			//其他数据库
			if(StaticValue.DBTYPE==4){
				//db2数据库
				depsql = new StringBuffer("select " + TableLfDep.DEP_ID
						+ ",'[机构]'" + lianjiefuhao + nullFunction + "("
						+ TableLfDep.DEP_NAME + ",'未知机构') DEP_NAME,-1 USERSTATE,'1' IDTYPE");
			}else{
				depsql = new StringBuffer("select " + TableLfDep.DEP_ID
						+ ",'[机构]'" + lianjiefuhao + nullFunction + "("
						+ TableLfDep.DEP_NAME + ",'未知机构') DEP_NAME,null USERSTATE,'1' IDTYPE");
			}
		}
		//部门成功sql
		String depsucc = "";
		//部门失败sql
		String depfail = "";
		//未知sql
		String nullSql = "";
		
		String P_propFile = "SystemGlobals";
		ResourceBundle rb = ResourceBundle.getBundle(P_propFile);
		String depcodethird2p2 = rb.getString("depcodethird2p2");

		
		//各种不同数据库
		switch (StaticValue.DBTYPE) {
		case 1:
			// oracle
			depsucc = "select sum("
					+ TableMtDatareport.ICOUNT
					+ "-"
					+ TableMtDatareport.RFAIL1
					+ ") from (select ICOUNT,RSUCC,RFAIL1,RFAIL2,RNRET,P1,P2 from "
					+ tablename+timewheresql+conditionSql+")  dp ";
					if ("true".equals(depcodethird2p2)) {
						depsucc=depsucc+ " left join LF_DEP dep on dep.DEP_CODE_THIRD=dp.P2 "; 
					}else{
						depsucc=depsucc+ " left join LF_SYSUSER su on su.USER_CODE=dp.P1 left join LF_DEP dep on dep.DEP_ID=su.DEP_ID "; 
					}
					depsucc=depsucc+ " where dep.DEP_ID<>"+ depId
					+ " and  d.DEP_PATH=SUBSTR(dep.DEP_PATH,0,length(d.DEP_PATH)) AND dep."+TableLfDep.DEP_STATE+"=1 ";

			depfail = "select sum("
					+ TableMtDatareport.RFAIL2
					+ ") from (select RFAIL2,P1,P2 from "
					+ tablename+timewheresql+conditionSql+") dp ";
					if ("true".equals(depcodethird2p2)) {
						depfail=depfail+ " left join LF_DEP dep on dep.DEP_CODE_THIRD=dp.P2 "; 
					}else{
						depfail=depfail+ " left join LF_SYSUSER su on su.USER_CODE=dp.P1 left join LF_DEP dep on dep.DEP_ID=su.DEP_ID "; 
					}
					depfail=depfail+ " where dep.DEP_ID<>"	+ depId
					+ " and  d.DEP_PATH=SUBSTR(dep.DEP_PATH,0,length(d.DEP_PATH)) AND dep."+TableLfDep.DEP_STATE+"=1 ";

			break;
		case 2:
			// sqlserver
			depsucc = "select sum("
					+ TableMtDatareport.ICOUNT
					+ "-"
					+ TableMtDatareport.RFAIL1
					+ ") from (select ICOUNT,RSUCC,RFAIL1,RFAIL2,RNRET,P1,P2 from "
					+ tablename+timewheresql+conditionSql+") dp "; 
					if ("true".equals(depcodethird2p2)) {
						depsucc=depsucc+ " left join LF_DEP dep on dep.DEP_CODE_THIRD=dp.P2 "; 
					}else{
						depsucc=depsucc+ " left join LF_SYSUSER su on su.USER_CODE=dp.P1 left join LF_DEP dep on dep.DEP_ID=su.DEP_ID "; 
					}
					
					depsucc=depsucc+ " where dep.DEP_ID<>"	+ depId
					+ " and  d.DEP_PATH=SUBSTRING(dep.DEP_PATH,0,len(d.DEP_PATH)+1) AND dep."+TableLfDep.DEP_STATE+"=1 ";

			depfail = "select sum("
					+ TableMtDatareport.RFAIL2
					+ ") from (select RFAIL2,P1,P2 from "
					+ tablename+timewheresql+conditionSql+") dp ";
					if ("true".equals(depcodethird2p2)) {
						depfail=depfail+ " left join LF_DEP dep on dep.DEP_CODE_THIRD=dp.P2 "; 
					}else{
						depfail=depfail+ " left join LF_SYSUSER su on su.USER_CODE=dp.P1 left join LF_DEP dep on dep.DEP_ID=su.DEP_ID "; 
					} 
					depfail=depfail+  " where dep.DEP_ID<>"	+ depId
					+ " and  d.DEP_PATH=SUBSTRING(dep.DEP_PATH,0,len(d.DEP_PATH)+1) AND dep."+TableLfDep.DEP_STATE+"=1 ";

			break;
		case 3:
			// MYSQL
			depsucc = "select sum("
				+ TableMtDatareport.ICOUNT
				+ "-"
				+ TableMtDatareport.RFAIL1
				+ ") from (select ICOUNT,RFAIL1,P1,P2 from "
				+ tablename+timewheresql+conditionSql+") dp "; 
				if ("true".equals(depcodethird2p2)) {
					depsucc=depsucc+ " left join LF_DEP dep on dep.DEP_CODE_THIRD=dp.P2 "; 
				}else{
					depsucc=depsucc+ " left join LF_SYSUSER su on su.USER_CODE=dp.P1 left join LF_DEP dep on dep.DEP_ID=su.DEP_ID "; 
				} 
				depsucc=depsucc+ " where dep.DEP_ID<>" + depId
				+ " and d.DEP_PATH=SUBSTRING(dep.DEP_PATH,1,length(d.DEP_PATH)) AND dep."+TableLfDep.DEP_STATE+"=1 ";

			depfail = "select sum("
				+ TableMtDatareport.RFAIL2
				+ ") from (select RFAIL2,P1,P2 from "
				+ tablename+timewheresql+conditionSql+") dp ";
				if ("true".equals(depcodethird2p2)) {
					depfail=depfail+ " left join LF_DEP dep on dep.DEP_CODE_THIRD=dp.P2 "; 
				}else{
					depfail=depfail+ " left join LF_SYSUSER su on su.USER_CODE=dp.P1 left join LF_DEP dep on dep.DEP_ID=su.DEP_ID "; 
				} 
				depfail=depfail+ " where dep.DEP_ID<>" + depId
				+ " and  d.DEP_PATH=SUBSTRING(dep.DEP_PATH,1,length(d.DEP_PATH)) AND dep."+TableLfDep.DEP_STATE+"=1 ";
			break;
		case 4:
			// DB2
			depsucc = "select sum("
					+ TableMtDatareport.ICOUNT
					+ "-"
					+ TableMtDatareport.RFAIL1
					+ ") from (select ICOUNT,RFAIL1,P1,P2 from "
					+ tablename+timewheresql+conditionSql+") dp " 
					+ " left join LF_SYSUSER su on su.USER_CODE=dp.P1 left join LF_DEP dep on dep.DEP_ID=su.DEP_ID " 
					+ " where dep.DEP_ID<>"	+ depId
					//+ " and  SUBSTR(dep.DEP_CODE,1,d.DEP_LEVEL*2+1)=SUBSTR(d.DEP_CODE,1,d.DEP_LEVEL*2+1) AND dep."+TableLfDep.DEP_STATE+"=1 ";
					+ " and  d.DEP_PATH=SUBSTR(dep.DEP_PATH,1,length(d.DEP_PATH)) AND dep."+TableLfDep.DEP_STATE+"=1 ";

			depfail = "select sum("
					+ TableMtDatareport.RFAIL2
					+ ") from (select RFAIL2,P1,P2 from "
					+ tablename+timewheresql+conditionSql+") dp ";
					if ("true".equals(depcodethird2p2)) {
						depfail=depfail+ " left join LF_DEP dep on dep.DEP_CODE_THIRD=dp.P2 "; 
					}else{
						depfail=depfail+ " left join LF_SYSUSER su on su.USER_CODE=dp.P1 left join LF_DEP dep on dep.DEP_ID=su.DEP_ID "; 
					} 
					depfail=depfail+ " where dep.DEP_ID<>" + depId
					//+ " and  SUBSTR(dep.DEP_CODE,1,d.DEP_LEVEL*2+1)=SUBSTR(d.DEP_CODE,1,d.DEP_LEVEL*2+1) AND dep."+TableLfDep.DEP_STATE+"=1 ";
					+ " and  d.DEP_PATH=SUBSTR(dep.DEP_PATH,1,length(d.DEP_PATH)) AND dep."+TableLfDep.DEP_STATE+"=1 ";
			break;
		default:
			break;
		}

		String depsuccs = nullFunction + "((" + depsucc + "),0) SUCC";

		String depfails = nullFunction + "((" + depfail + "),0) RFAIL2";
		String depi = "";
		if (null != depId) {
			depi = " SUPERIOR_ID=" + depId + " ";
		}
		//关联的部门sql
		depsql.append(",").append(depsuccs).append(",").append(depfails)
				.append(" from LF_DEP d where " + depi+" AND d."+TableLfDep.DEP_STATE+"=1 " + conditionSqldep);

		
		StringBuffer usersql = null;
		//mySql
		if (StaticValue.DBTYPE == 3) {
			usersql = new StringBuffer("select USER_ID DEP_ID,"+ lianjiefuhao+"'[操作员]',"
					 + nullFunction + "("
					+ TableLfSysuser.USER_NAME + ",'未知操作员')) DEP_NAME,USER_STATE USERSTATE,'2' IDTYPE");
			nullSql = " UNION ALL SELECT 0 DEP_ID,'未知机构' DEP_NAME,-1 USERSTATE,'3' IDTYPE," + nullFunction
					+ "(sum(ICOUNT-RFAIL1),0) SUCC," + nullFunction
					+ "(sum(RFAIL2),0) RFAIL2  from "+tablename+" dp "
					+ "left join LF_SYSUSER su on su.USER_CODE=dp.p1 "
					+ timewheresql + conditionSql+" AND COALESCE(su.user_id,0)=0 ";
		}else if(StaticValue.DBTYPE == 4) {
		// DB2
			usersql = new StringBuffer("select USER_ID DEP_ID,'[操作员]'"
					+ lianjiefuhao + nullFunction + "("
					+ TableLfSysuser.USER_NAME + ",'未知操作员') DEP_NAME,USER_STATE USERSTATE,'2' IDTYPE");
			nullSql = " UNION ALL SELECT 0 DEP_ID,'未知机构' DEP_NAME,-1 USERSTATE,'3' IDTYPE," + nullFunction
					+ "(sum(ICOUNT-RFAIL1),0) SUCC," + nullFunction
					+ "(sum(RFAIL2),0) RFAIL2  from "+tablename+" dp "
					+ "left join LF_SYSUSER su on su.USER_CODE=dp.p1 "
					+ timewheresql+ conditionSql+" AND COALESCE(su.user_id,0)=0 ";
		}else {
		//其他数据库
			usersql = new StringBuffer("select USER_ID,'[操作员]'" + lianjiefuhao
					+ nullFunction + "(" + TableLfSysuser.USER_NAME
					+ ",'未知操作员') USER_NAME,USER_STATE USERSTATE,'2' IDTYPE");
			nullSql = " UNION ALL SELECT null,'未知机构',-1 USERSTATE,'3' IDTYPE," + nullFunction
					+ " (sum(ICOUNT-RFAIL1),0) SUCC," + nullFunction
					+ "(sum(RFAIL2),0) RFAIL2  from "+tablename+" dp "
					+ "left join LF_SYSUSER su on su.USER_CODE=dp.p1 "
					+ timewheresql+ conditionSql+" AND su.user_id is null ";
		}
		// StringBuffer usersql = new
		// StringBuffer("select USER_ID,'[操作员]'"+lianjiefuhao+nullFunction+"("+TableLfSysuser.USER_NAME+",'未知操作员') USER_NAME");
		String usersucc = "select sum(" + TableMtDatareport.ICOUNT + "-"
				+ TableMtDatareport.RFAIL1 + ") from "
				+ tablename
				+ " dp where dp.P1=sysuser.USER_CODE";
		//操作员成功数
		String usersuccs = nullFunction + "((" + usersucc + conditionSql
				+ "),0) SUCC";
		//操作员失败数
		String userfail = "select sum(" + TableMtDatareport.RFAIL2 + ") from "
				+ tablename
				+ " dp where dp.P1=sysuser.USER_CODE";
		//操作员失败数
		String userfails = nullFunction + "((" + userfail + conditionSql
				+ "),0) RFAIL2";
		String depIDadd = "";
		//判断是否选择部门的条件
		if (null != depId) {
			depIDadd = " and sysuser.DEP_ID=" + depId;
		}
		
		//操作员语句
		usersql.append(",").append(usersuccs).append(",").append(userfails)
				.append(
						" from LF_SYSUSER sysuser where sysuser.USER_ID<>1 "
								+ depIDadd + " " + conditionSqluser);
		String dep = "";
		//判断是单企业还是多企业
		if (StaticValue.getCORPTYPE() == 1) {
			int dep_id = getInt("dep_Id",
					"select dep_id from  lf_dep where corp_code='" + corpCode
							+ "' and dep_level=1", StaticValue.EMP_POOLNAME);
			dep = dep_id + "";
		}else{
			dep="1";
		}
		if ("-3".equals(depId) || dep.equals(depId)) {
			sql.append(depsql).append(" UNION ALL ").append(usersql)
					.append(nullSql);
		} else {
			sql.append(depsql).append(" UNION ALL ").append(usersql);
		}
		// db2
		String totalsql = "";
		if (StaticValue.DBTYPE == 4) {
			totalsql = "select tot." + TableLfDep.DEP_ID + ",tot."
					+ TableLfDep.DEP_NAME
					+ ",tot.SUCC,tot.RFAIL2 ,tot.USERSTATE USER_STATE,tot.IDTYPE IDTYPE,0 USER_ID,0 "
					+ TableLfSysuser.NAME +" from (" + sql.toString() + ") tot ";
		} else {
			totalsql = "select tot." + TableLfDep.DEP_ID + ",tot."
					+ TableLfDep.DEP_NAME
					+ ",tot.SUCC,tot.RFAIL2 ,tot.USERSTATE USER_STATE,tot.IDTYPE IDTYPE,null USER_ID,null "
					+ TableLfSysuser.NAME 
					+ " from (" + sql.toString() + ") tot ";
		}

		// System.out.println(totalsql);
		return totalsql;
	}
	
	
	/**
	 * 短信机构报表sql语句生成
	 * @param depId
	 * @param deprptvo
	 * @param corpCode
	 * @return
	 * @throws Exception
	 */
	public String getMMSSql(String depId, DepRptVo deprptvo,
			String corpCode) throws Exception {
		StringBuffer sql = new StringBuffer("");
		String nullFunction = "NVL";// 数据库的判空函数 默认为oracle的判断NULL函数
		String lianjiefuhao = "||";
		String[] timeStrCondition = new String[3];
		String sendtime = deprptvo.getSendTime().replaceAll("-", "");
		String endtime=deprptvo.getEndTime().replaceAll("-", "");

		switch (StaticValue.DBTYPE) {
		case 1:
			// oracle
			nullFunction = "NVL";
			lianjiefuhao = "||";
//			timeStrCondition[0] = " AND TO_DATE(dp." + TableMmsDatareport.IYMD+ ",'YYYY-MM-DD')>=TO_DATE('"+ deprptvo.getSendTime() + "','YYYY-MM-DD') ";
//			timeStrCondition[1] = " AND TO_DATE(dp." + TableMmsDatareport.IYMD+ ",'YYYY-MM-DD')<=TO_DATE('" + deprptvo.getEndTime() + "','YYYY-MM-DD') ";
//			timeStrCondition[2] = " AND TO_DATE(dp." + TableMmsDatareport.IYMD+ ",'YYYY-MM-DD') BETWEEN TO_DATE('"+ deprptvo.getSendTime()
//					+ "','YYYY-MM-DD')  AND TO_DATE('"+ deprptvo.getEndTime() + "','YYYY-MM-DD') ";

			break;
		case 2:
			// sqlserver2005
			nullFunction = "ISNULL";
			lianjiefuhao = "+";
//			timeStrCondition[0] = " AND CAST(STR(dp." + TableMmsDatareport.IYMD+ ") AS DATETIME) >= CAST('" + deprptvo.getSendTime()+ "'  AS DATETIME)";
//			timeStrCondition[1] = " AND CAST(STR(dp." + TableMmsDatareport.IYMD+ ") AS DATETIME) <= CAST('" + deprptvo.getEndTime()+ "'   AS DATETIME)";
//			timeStrCondition[2] = " AND CAST(STR(dp." + TableMmsDatareport.IYMD+ ") AS DATETIME) BETWEEN  CAST('"+ deprptvo.getSendTime()
//					+ "'  AS DATETIME)  AND  CAST('"+ deprptvo.getEndTime() + "'  AS DATETIME)";

			break;
		case 3:
			// MYSQL
			nullFunction = "IFNULL";
			lianjiefuhao = "CONCAT(";
//			timeStrCondition[0] = " AND CAST(dp." + TableMmsDatareport.IYMD+ " AS DATETIME) >= CAST('" + deprptvo.getSendTime()+ "'  AS DATETIME)";
//			timeStrCondition[1] = " AND CAST(dp." + TableMmsDatareport.IYMD+ " AS DATETIME) <= CAST('" + deprptvo.getEndTime()+ "'   AS DATETIME)";
//			timeStrCondition[2] = " AND CAST(dp." + TableMmsDatareport.IYMD+ " AS DATETIME) BETWEEN  CAST('"+ deprptvo.getSendTime()
//					+ "'  AS DATETIME)  AND  CAST('"+ deprptvo.getEndTime() + "'  AS DATETIME)";

			break;
		case 4:
			// DB2
			nullFunction = "COALESCE";
			lianjiefuhao = "||";
//			timeStrCondition[0] = " AND dp." + TableMmsDatareport.IYMD+ ">=" + deprptvo.getSendTime().replace("-", "") + "";
//			timeStrCondition[1] = " AND dp." + TableMmsDatareport.IYMD+ "<=" + deprptvo.getEndTime().replace("-", "") + "";
//			timeStrCondition[2] = " AND dp." + TableMmsDatareport.IYMD+ " BETWEEN " + deprptvo.getSendTime().replace("-", "") 
//			+ "  AND "+ deprptvo.getEndTime().replace("-", "") + "";

			break;
		default:
			break;
		}
		
		// 机构是否多企业的条件条件查询
		String conditionSqldep = "  ";
		// 操作员是否多企业的条件条件查询
		String conditionSqluser = "  ";

		if (null != corpCode && 0 != corpCode.length()
				&& StaticValue.getCORPTYPE() == 1) {
			conditionSqldep += " AND d." + TableLfDep.CORP_CODE + " = '"+ corpCode + "'";
			conditionSqluser += " AND sysuser." + TableLfSysuser.CORP_CODE+ " = '" + corpCode + "'";
		}
		
		timeStrCondition[0] = " WHERE " + TableMmsDatareport.IYMD+ ">="+ sendtime + " ";
		timeStrCondition[1] = " WHERE " + TableMmsDatareport.IYMD+ "<="+ endtime +" ";
		timeStrCondition[2] = " WHERE IYMD>="+ sendtime+ " AND IYMD<="+ endtime + " ";
		// 报表数据查询条件
		String conditionSql = "  ";
		String timewheresql="";
		// 时间条件的查询
		if ((0 != deprptvo.getSendTime().length() && 0 == deprptvo
				.getEndTime().length())) {
			timewheresql += timeStrCondition[0];
		} else if (0 == deprptvo.getSendTime().length()
				&& 0 != deprptvo.getEndTime().length()) {
			timewheresql += timeStrCondition[1];
		} else if (0 != deprptvo.getSendTime().length()
				&& 0 != deprptvo.getEndTime().length()) {
			timewheresql += timeStrCondition[2];
		} else {
			// 如果没有时间查询条件，则默认查询当前年月份的
			String yearAndMonth[] = this.getYearAndMonth();
			if (null != yearAndMonth) {
				timewheresql += " WHERE dp." + TableMmsDatareport.Y + "=" + yearAndMonth[0] + " " + " AND dp."
						+ TableMmsDatareport.IMONTH + "=" + yearAndMonth[1] + "";
			}
		}

		/**
		 * MySql连接
		 */
		//机构语句
		StringBuffer depsql = null;
		if(StaticValue.DBTYPE==3){
			//mysql
			depsql = new StringBuffer("select " + TableLfDep.DEP_ID	+ ","+ lianjiefuhao +"'[机构]'," + nullFunction + "("
					+ TableLfDep.DEP_NAME + ",'未知机构')) DEP_NAME,null USERSTATE");
		}else{
			//其他数据库
			if(StaticValue.DBTYPE==4){
				//db2数据库
				depsql = new StringBuffer("select " + TableLfDep.DEP_ID	+ ",'[机构]'" + lianjiefuhao + nullFunction + "("
						+ TableLfDep.DEP_NAME + ",'未知机构') DEP_NAME,-1 USERSTATE");
			}else{
				depsql = new StringBuffer("select " + TableLfDep.DEP_ID	+ ",'[机构]'" + lianjiefuhao + nullFunction + "("
						+ TableLfDep.DEP_NAME + ",'未知机构') DEP_NAME,null USERSTATE");
			}
		}
		//部门成功sql
		String depsucc = "";
		//部门失败sql
		String depfail = "";
		//未知sql
		String nullSql = "";
		
		String P_propFile = "SystemGlobals";
		ResourceBundle rb = ResourceBundle.getBundle(P_propFile);
		String depcodethird2p2 = rb.getString("depcodethird2p2");
		
		//各种不同数据库
		switch (StaticValue.DBTYPE) {
		case 1:
			// oracle
			depsucc = "select sum("	+ TableMmsDatareport.ICOUNT	+ "-"+ TableMmsDatareport.RFAIL1
					+ ") from (select ICOUNT,RFAIL1,P1,P2 from "+ TableMmsDatareport.TABLE_NAME+timewheresql+conditionSql+") dp "; 
					if ("true".equals(depcodethird2p2)) {
						depsucc=depsucc+ " left join LF_DEP dep on dep.DEP_CODE_THIRD=dp.P2 "; 
					}else{
						depsucc=depsucc+ " left join LF_SYSUSER su on su.USER_CODE=dp.P1 left join LF_DEP dep on dep.DEP_ID=su.DEP_ID "; 
					} 
					depsucc=depsucc+ " where dep.DEP_ID<>"	+ depId	
					+ " and  d.DEP_PATH=SUBSTR(dep.DEP_PATH,0,length(d.DEP_PATH)) AND dep."+TableLfDep.DEP_STATE+"=1 ";

			depfail = "select sum("+ TableMmsDatareport.RFAIL2+ ") from (select RFAIL2,P1,P2 from "	
					+ TableMmsDatareport.TABLE_NAME+timewheresql+conditionSql+") dp " ;
					if ("true".equals(depcodethird2p2)) {
						depfail=depfail+ " left join LF_DEP dep on dep.DEP_CODE_THIRD=dp.P2 "; 
					}else{
						depfail=depfail+ " left join LF_SYSUSER su on su.USER_CODE=dp.P1 left join LF_DEP dep on dep.DEP_ID=su.DEP_ID "; 
					} 
					depfail=depfail+ " where dep.DEP_ID<>"	+ depId 
					+ " and  d.DEP_PATH=SUBSTR(dep.DEP_PATH,0,length(d.DEP_PATH)) AND dep."+TableLfDep.DEP_STATE+"=1 ";

			break;
		case 2:
			// sqlserver
			depsucc = "select sum("	+ TableMmsDatareport.ICOUNT	+ "-" + TableMmsDatareport.RFAIL1
					+ ") from (select ICOUNT,RFAIL1,P1,P2 from " + TableMmsDatareport.TABLE_NAME+timewheresql+conditionSql+") dp "; 
					if ("true".equals(depcodethird2p2)) {
						depsucc=depsucc+ " left join LF_DEP dep on dep.DEP_CODE_THIRD=dp.P2 "; 
					}else{
						depsucc=depsucc+ " left join LF_SYSUSER su on su.USER_CODE=dp.P1 left join LF_DEP dep on dep.DEP_ID=su.DEP_ID "; 
					}  
					depsucc=depsucc+ " where dep.DEP_ID<>"	+ depId	
					+ " and  d.DEP_PATH=SUBSTRING(dep.DEP_PATH,0,len(d.DEP_PATH)+1) AND dep."+TableLfDep.DEP_STATE+"=1 ";

			depfail = "select sum("	+ TableMmsDatareport.RFAIL2	
					+ ") from (select RFAIL2,P1,P2 from " + TableMmsDatareport.TABLE_NAME+timewheresql+conditionSql+") dp "; 
					if ("true".equals(depcodethird2p2)) {
						depfail=depfail+ " left join LF_DEP dep on dep.DEP_CODE_THIRD=dp.P2 "; 
					}else{
						depfail=depfail+ " left join LF_SYSUSER su on su.USER_CODE=dp.P1 left join LF_DEP dep on dep.DEP_ID=su.DEP_ID "; 
					}  
					depfail=depfail+ " where dep.DEP_ID<>"	+ depId	
					+ " and  d.DEP_PATH=SUBSTRING(dep.DEP_PATH,0,len(d.DEP_PATH)+1) AND dep."+TableLfDep.DEP_STATE+"=1 ";

			break;
		case 3:
			// MYSQL
			depsucc = "select sum("	+ TableMmsDatareport.ICOUNT	+ "-"+ TableMmsDatareport.RFAIL1
					+ ") from (select ICOUNT,RFAIL1,P1,P2 from "+ TableMmsDatareport.TABLE_NAME+timewheresql+conditionSql+") dp "; 
					if ("true".equals(depcodethird2p2)) {
						depsucc=depsucc+ " left join LF_DEP dep on dep.DEP_CODE_THIRD=dp.P2 "; 
					}else{
						depsucc=depsucc+ " left join LF_SYSUSER su on su.USER_CODE=dp.P1 left join LF_DEP dep on dep.DEP_ID=su.DEP_ID "; 
					}  
					depsucc=depsucc+ " where dep.DEP_ID<>"	+ depId	
					+ " and d.DEP_PATH=SUBSTRING(dep.DEP_PATH,1,length(d.DEP_PATH)) AND dep."+TableLfDep.DEP_STATE+"=1 ";
			
			depfail = "select sum(" + TableMmsDatareport.RFAIL2 
					+ ") from (select RFAIL2,P1,P2 from " + TableMmsDatareport.TABLE_NAME+timewheresql+conditionSql+") dp "; 
					if ("true".equals(depcodethird2p2)) {
						depfail=depfail+ " left join LF_DEP dep on dep.DEP_CODE_THIRD=dp.P2 "; 
					}else{
						depfail=depfail+ " left join LF_SYSUSER su on su.USER_CODE=dp.P1 left join LF_DEP dep on dep.DEP_ID=su.DEP_ID "; 
					}  
					depfail=depfail+ " where dep.DEP_ID<>"	+ depId	
					+ " and  d.DEP_PATH=SUBSTRING(dep.DEP_PATH,1,length(d.DEP_PATH)) AND dep."+TableLfDep.DEP_STATE+"=1 ";
			break;
		case 4:
			// DB2
			depsucc = "select sum("	+ TableMmsDatareport.ICOUNT	+ "-"+ TableMmsDatareport.RFAIL1
					+ ") from (select ICOUNT,RFAIL1,P1,P2 from " + TableMmsDatareport.TABLE_NAME+timewheresql+conditionSql+") dp "; 
					if ("true".equals(depcodethird2p2)) {
						depsucc=depsucc+ " left join LF_DEP dep on dep.DEP_CODE_THIRD=dp.P2 "; 
					}else{
						depsucc=depsucc+ " left join LF_SYSUSER su on su.USER_CODE=dp.P1 left join LF_DEP dep on dep.DEP_ID=su.DEP_ID "; 
					}  
					depsucc=depsucc+ " where dep.DEP_ID<>" + depId	
					+ " and  d.DEP_PATH=SUBSTR(dep.DEP_PATH,1,length(d.DEP_PATH)) AND dep."+TableLfDep.DEP_STATE+"=1 ";

			depfail = "select sum("	+ TableMmsDatareport.RFAIL2	
					+ ") from (select RFAIL2,P1,P2 from " + TableMmsDatareport.TABLE_NAME+timewheresql+conditionSql+") dp "; 
					if ("true".equals(depcodethird2p2)) {
						depfail=depfail+ " left join LF_DEP dep on dep.DEP_CODE_THIRD=dp.P2 "; 
					}else{
						depfail=depfail+ " left join LF_SYSUSER su on su.USER_CODE=dp.P1 left join LF_DEP dep on dep.DEP_ID=su.DEP_ID "; 
					}  
					depfail=depfail+  " where dep.DEP_ID<>" + depId	
					+ " and  d.DEP_PATH=SUBSTR(dep.DEP_PATH,1,length(d.DEP_PATH)) AND dep."+TableLfDep.DEP_STATE+"=1 ";
			break;
		default:
			break;
		}

//		String depsuccs = nullFunction + "((" + depsucc + conditionSql+ "),0) SUCC";
//
//		String depfails = nullFunction + "((" + depfail + conditionSql+ "),0) RFAIL2";
		String depsuccs = nullFunction + "((" + depsucc  + "),0) SUCC";

		String depfails = nullFunction + "((" + depfail  + "),0) RFAIL2";
		String depi = "";
		if (null != depId) {
			depi = " SUPERIOR_ID=" + depId + " ";
		}
		//关联的部门sql
		depsql.append(",").append(depsuccs).append(",").append(depfails).append(" from LF_DEP d where " + depi+" AND d."
				+TableLfDep.DEP_STATE+"=1 " + conditionSqldep);

		StringBuffer usersql = null;
		//mySql
		if (StaticValue.DBTYPE == 3) {
			usersql = new StringBuffer("select USER_ID DEP_ID,"+ lianjiefuhao+"'[操作员]'," + nullFunction + "("	+ TableLfSysuser.USER_NAME 
					+ ",'未知操作员')) DEP_NAME,USER_STATE USERSTATE");
			nullSql = " UNION ALL SELECT 0 DEP_ID,'未知机构' DEP_NAME,-1 USERSTATE," + nullFunction+ "(sum(ICOUNT-RFAIL1),0) SUCC," + nullFunction
					+ "(sum(RFAIL2),0) RFAIL2  from MMS_DATAREPORT dp "+ "left join LF_SYSUSER su on su.USER_CODE=dp.p1 "
					+ "where COALESCE(su.user_id,0)=0 " + conditionSql;
		}else if(StaticValue.DBTYPE == 4) {
		// DB2
			usersql = new StringBuffer("select USER_ID DEP_ID,'[操作员]'"+ lianjiefuhao + nullFunction + "("	+ TableLfSysuser.USER_NAME 
					+ ",'未知操作员') DEP_NAME,USER_STATE USERSTATE");
			nullSql = " UNION ALL SELECT 0 DEP_ID,'未知机构' DEP_NAME,-1 USERSTATE," + nullFunction+ "(sum(ICOUNT-RFAIL1),0) SUCC," + nullFunction
					+ "(sum(RFAIL2),0) RFAIL2  from MMS_DATAREPORT dp "+ "left join LF_SYSUSER su on su.USER_CODE=dp.p1 "
					+ "where COALESCE(su.user_id,0)=0 " + conditionSql;
		}else {
		//其他数据库
			usersql = new StringBuffer("select USER_ID,'[操作员]'" + lianjiefuhao+ nullFunction + "(" + TableLfSysuser.USER_NAME
					+ ",'未知操作员') USER_NAME,USER_STATE USERSTATE");
			nullSql = " UNION ALL SELECT null,'未知机构',-1 USERSTATE," + nullFunction	+ " (sum(ICOUNT-RFAIL1),0) SUCC," + nullFunction
					+ "(sum(RFAIL2),0) RFAIL2  from MMS_DATAREPORT dp "
					+ "left join LF_SYSUSER su on su.USER_CODE=dp.p1 "
					+ "where su.user_id is null " + conditionSql;
		}
		
		String usersucc = "select sum(" + TableMmsDatareport.ICOUNT + "-"+ TableMmsDatareport.RFAIL1 + ") from "+ TableMmsDatareport.TABLE_NAME
				+ " dp where dp.P1=sysuser.USER_CODE";
		//操作员成功数
		String usersuccs = nullFunction + "((" + usersucc + conditionSql+ "),0) SUCC";
		//操作员失败数
		String userfail = "select sum(" + TableMmsDatareport.RFAIL2 + ") from "	+ TableMmsDatareport.TABLE_NAME
				+ " dp where dp.P1=sysuser.USER_CODE";
		//操作员失败数
		String userfails = nullFunction + "((" + userfail + conditionSql
				+ "),0) RFAIL2";
		String depIDadd = "";
		//判断是否选择部门的条件
		if (null != depId) {
			depIDadd = " and sysuser.DEP_ID=" + depId;
		}
		
		//操作员语句
		usersql.append(",").append(usersuccs).append(",").append(userfails)
				.append(
						" from LF_SYSUSER sysuser where sysuser.USER_ID<>1 "
								+ depIDadd + " " + conditionSqluser);
		String dep = "";
		//判断是单企业还是多企业
		if (StaticValue.getCORPTYPE() == 1) {
			int dep_id = getInt("dep_Id","select dep_id from  lf_dep where corp_code='" + corpCode	+ "' and dep_level=1", StaticValue.EMP_POOLNAME);
			dep = dep_id + "";
		}else{
			dep="1";
		}
		if ("-3".equals(depId) || dep.equals(depId)) {
			sql.append(depsql).append(" UNION ALL ").append(usersql).append(nullSql);
		} else {
			sql.append(depsql).append(" UNION ALL ").append(usersql);
		}
		// db2
		String totalsql = "";
		if (StaticValue.DBTYPE == 4) {
			totalsql = "select tot." + TableLfDep.DEP_ID + ",tot."
					+ TableLfDep.DEP_NAME
					+ ",tot.SUCC,tot.RFAIL2 ,tot.USERSTATE USER_STATE,0 USER_ID,0 "
					+ TableLfSysuser.NAME + " from (" + sql.toString() + ") tot ";
		} else {
			totalsql = "select tot." + TableLfDep.DEP_ID + ",tot."
					+ TableLfDep.DEP_NAME
					+ ",tot.SUCC,tot.RFAIL2 ,tot.USERSTATE USER_STATE,null USER_ID,null "
					+ TableLfSysuser.NAME + " from (" + sql.toString() + ") tot ";
		}

		// System.out.println(totalsql);
		return totalsql;
	}
	
	
	
	public String getSql3(String depIds, DepRptVo deprptvo,String corpCode) throws Exception {
		StringBuffer sql = new StringBuffer("");
		String[] timeStrCondition = new String[3];

		switch (StaticValue.DBTYPE) {
		case 1:
			// oracle
			timeStrCondition[0] = " AND TO_DATE(dp." + TableMtDatareport.IYMD
					+ ",'YYYY-MM-DD')>=TO_DATE('"
					+ deprptvo.getSendTime() + "','YYYY-MM-DD') ";
			timeStrCondition[1] = " AND TO_DATE(dp." + TableMtDatareport.IYMD
					+ ",'YYYY-MM-DD')<=TO_DATE('" + deprptvo.getEndTime()
					+ "','YYYY-MM-DD') ";
			timeStrCondition[2] = " AND TO_DATE(dp." + TableMtDatareport.IYMD
					+ ",'YYYY-MM-DD') BETWEEN TO_DATE('"
					+ deprptvo.getSendTime()
					+ "','YYYY-MM-DD')  AND TO_DATE('"
					+ deprptvo.getEndTime() + "','YYYY-MM-DD') ";
			break;
		case 2:
			// sqlserver2005
			// timeFunction =
			// "  CAST(STR(DATAREPORT."+TableMtDatareport.IYMD+") AS DATETIME) ";
			timeStrCondition[0] = " AND CAST(STR(dp." + TableMtDatareport.IYMD
					+ ") AS DATETIME) >= CAST('" + deprptvo.getSendTime()
					+ "'  AS DATETIME)";
			timeStrCondition[1] = " AND CAST(STR(dp." + TableMtDatareport.IYMD
					+ ") AS DATETIME) <= CAST('" + deprptvo.getEndTime()
					+ "'   AS DATETIME)";
			timeStrCondition[2] = " AND CAST(STR(dp." + TableMtDatareport.IYMD
					+ ") AS DATETIME) BETWEEN  CAST('"
					+ deprptvo.getSendTime()
					+ "'  AS DATETIME)  AND  CAST('"
					+ deprptvo.getEndTime() + "'  AS DATETIME)";
			break;
		case 3:
			// MYSQL
			// timeFunction =
			// "  CAST(STR(DATAREPORT."+TableMtDatareport.IYMD+") AS DATETIME) ";
			timeStrCondition[0] = " AND CAST(dp." + TableMtDatareport.IYMD
					+ " AS DATETIME) >= CAST('" + deprptvo.getSendTime()
					+ "'  AS DATETIME)";
			timeStrCondition[1] = " AND CAST(dp." + TableMtDatareport.IYMD
					+ " AS DATETIME) <= CAST('" + deprptvo.getEndTime()
					+ "'   AS DATETIME)";
			timeStrCondition[2] = " AND CAST(dp." + TableMtDatareport.IYMD
					+ " AS DATETIME) BETWEEN  CAST('"
					+ deprptvo.getSendTime()
					+ "'  AS DATETIME)  AND  CAST('"
					+ deprptvo.getEndTime() + "'  AS DATETIME)";
			break;
		case 4:
			// DB2
			timeStrCondition[0] = " AND DATE(dp." + TableMtDatareport.IYMD
					+ ")>='" + deprptvo.getSendTime() + "'";
			timeStrCondition[1] = " AND DATE(dp." + TableMtDatareport.IYMD
					+ ")<='" + deprptvo.getEndTime() + "'";
			timeStrCondition[2] = " AND DATE(dp." + TableMtDatareport.IYMD
					+ ") BETWEEN '" + deprptvo.getSendTime() + "'  AND '"
					+ deprptvo.getEndTime() + "'";
			break;
		default:
			break;
		}
		String conditionSqldep = "  ";// 是否多企业的条件条件查询
		String conditionSqluser = "  ";// 是否多企业的条件条件查询

		if (null != corpCode && 0 != corpCode.length()
				&& StaticValue.getCORPTYPE() == 1) {
			conditionSqldep += " AND d." + TableLfDep.CORP_CODE + " = '"
					+ corpCode + "'";
			conditionSqluser += " AND sysuser." + TableLfSysuser.CORP_CODE
					+ " = '" + corpCode + "'";
		}
		
		//conditionSqldep +=" AND d."+TableLfDep.DEP_STATE+" = 1";
		
		String conditionSql = "  ";// 报表数据查询条件

		// 时间条件的查询
		if ((0 != deprptvo.getSendTime().length() && 0 == deprptvo
				.getEndTime().length())) {
			conditionSql += timeStrCondition[0];

		} else if (0 == deprptvo.getSendTime().length()
				&& 0 != deprptvo.getEndTime().length()) {
			conditionSql += timeStrCondition[1];

		} else if (0 != deprptvo.getSendTime().length()
				&& 0 != deprptvo.getEndTime().length()) {
			conditionSql += timeStrCondition[2];
		} else {
			// 如果没有时间查询条件，则默认查询当前年月份的
			String yearAndMonth[] = this.getYearAndMonth();

			if (null != yearAndMonth) {
				conditionSql += " AND dp." + TableMtDatareport.Y + "="
						+ yearAndMonth[0] + " " + " AND dp."
						+ TableMtDatareport.IMONTH + "=" + yearAndMonth[1] + "";
			}
		}

		
		String depsucc = "";
		String depidss[]=depIds.split(",");
		//int maxcount=StaticValue.inConditionMax;
		int maxcount=StaticValue.getInConditionMax();
		switch (StaticValue.DBTYPE) {
		case 1:
			// oracle
			depsucc = "select null DEP_ID,null DEP_NAME,sum("
					+ TableMtDatareport.ICOUNT
					+ "-"
					+ TableMtDatareport.RFAIL1
					+ ") SUCC,sum("
					+ TableMtDatareport.RFAIL2
					+ ") RFAIL2,null USER_ID,null NAME from "
					+ TableMtDatareport.TABLE_NAME
					+ " dp left join LF_SYSUSER su on su.USER_CODE=dp.P1 left join LF_DEP dep on dep.DEP_ID=su.DEP_ID"
					+" where dep."+TableLfDep.DEP_STATE+"=1 ";
			
			if(!"".equals(depIds)){
				if(depidss.length<maxcount){
					depsucc=depsucc+" and dep."+TableLfDep.DEP_ID +" in ("+depIds+")";
				}else{
					StringBuffer sb=new StringBuffer("");
					for (int i = 0; i < depidss.length; i++) {
						sb.append(",").append(depidss[i]);
						if((i+3)%maxcount==0){
							depsucc=depsucc+" or dep."+TableLfDep.DEP_ID +" in (0,"+sb.toString()+")";
							sb=new StringBuffer("");
						}
					}
				}
			}

			break;
		case 2:
			// sqlserver
			depsucc = "select null DEP_ID,null DEP_NAME,sum("
					+ TableMtDatareport.ICOUNT
					+ "-"
					+ TableMtDatareport.RFAIL1
					+ ") SUCC,sum("
					+ TableMtDatareport.RFAIL2
					+ ") RFAIL2,null USER_ID,null NAME from "
					+ TableMtDatareport.TABLE_NAME
					+ " dp left join LF_SYSUSER su on su.USER_CODE=dp.P1 left join LF_DEP dep on dep.DEP_ID=su.DEP_ID"
					+" where dep."+TableLfDep.DEP_STATE+"=1 ";
			if(!"".equals(depIds)){
				if(depidss.length<maxcount){
					depsucc=depsucc+" and dep."+TableLfDep.DEP_ID +" in ("+depIds+")";
				}else{
					StringBuffer sb=new StringBuffer("");
					for (int i = 0; i < depidss.length; i++) {
						
						sb.append(",").append(depidss[i]);
						if((i+3)%maxcount==0){
							depsucc=depsucc+" or dep."+TableLfDep.DEP_ID +" in (0,"+sb.toString()+")";
							sb=new StringBuffer("");
						}
					}
				}
			}
				break;
		case 3:
			// MYSQL
			depsucc = "select null DEP_ID,null DEP_NAME,sum("
				+ TableMtDatareport.ICOUNT
				+ "-"
				+ TableMtDatareport.RFAIL1
				+ ") SUCC,sum("
				+ TableMtDatareport.RFAIL2
				+ ") RFAIL2,null USER_ID,null NAME from "
				+ TableMtDatareport.TABLE_NAME
				+ " dp left join LF_SYSUSER su on su.USER_CODE=dp.P1 left join LF_DEP dep on dep.DEP_ID=su.DEP_ID where dep."+TableLfDep.DEP_STATE+"=1 ";
			if(!"".equals(depIds)){
				if(depidss.length<maxcount){
					depsucc=depsucc+" and dep."+TableLfDep.DEP_ID +" in ("+depIds+")";
				}else{
					StringBuffer sb=new StringBuffer("");
					for (int i = 0; i < depidss.length; i++) {
						sb.append(",").append(depidss[i]);
						if((i+3)%maxcount==0){
							depsucc=depsucc+" or dep."+TableLfDep.DEP_ID +" in (0,"+sb.toString()+")";
							sb=new StringBuffer("");
						}
					}
				}
			}

		
			break;
		case 4:
			// DB2
			depsucc = "select null DEP_ID,null DEP_NAME,sum("
					+ TableMtDatareport.ICOUNT
					+ "-"
					+ TableMtDatareport.RFAIL1
					+ ") SUCC,sum("
					+ TableMtDatareport.RFAIL2
					+ ") RFAIL2,null USER_ID,null NAME from "
					+ TableMtDatareport.TABLE_NAME
					+ " dp left join LF_SYSUSER su on su.USER_CODE=dp.P1 left join LF_DEP dep on dep.DEP_ID=su.DEP_ID"
					+" where dep."+TableLfDep.DEP_STATE+"=1 ";
			depIds=depIds.replace(",","','");
			depidss=depIds.split(",");
			if(!"".equals(depIds)){
				if(depidss.length<maxcount){
					depsucc=depsucc+" and dep."+TableLfDep.DEP_ID +" in ("+depIds+")";
				}else{
					StringBuffer sb=new StringBuffer("");
					for (int i = 0; i < depidss.length; i++) {
						sb.append(",").append(depidss[i]);
						if((i+3)%maxcount==0){
							depsucc=depsucc+" or dep."+TableLfDep.DEP_ID +" in (0,"+sb.toString()+")";
							sb=new StringBuffer("");
						}
					}
				}
			}

			break;
		default:
			break;
		}

		//关联的部门sql
		sql.append(depsucc).append(conditionSql).append(conditionSqldep);


		return sql.toString();
	}


	public String getSql2(String depId, DepRptVo deprptvo,String corpCode) throws Exception {
		StringBuffer sql = new StringBuffer("");
		// 数据库的判空函数 默认为oracle的判断NULL函数
		String nullFunction = "NVL";
		// 数据库的连接符号默认为oracle的连接符号
		String lianjiefuhao = "||";

		String[] timeStrCondition = new String[3];

		//各数据库的时间查询
		switch (StaticValue.DBTYPE) {
		case 1:
			// oracle
			nullFunction = "NVL";
			lianjiefuhao = "||";
			timeStrCondition[0] = " AND TO_DATE(dp." + TableMtDatareport.IYMD
					+ ",'YYYY-MM-DD')>=TO_DATE('"
					+ deprptvo.getSendTime() + "','YYYY-MM-DD') ";
			timeStrCondition[1] = " AND TO_DATE(dp." + TableMtDatareport.IYMD
					+ ",'YYYY-MM-DD')<=TO_DATE('" + deprptvo.getEndTime()
					+ "','YYYY-MM-DD') ";
			timeStrCondition[2] = " AND TO_DATE(dp." + TableMtDatareport.IYMD
					+ ",'YYYY-MM-DD') BETWEEN TO_DATE('"
					+ deprptvo.getSendTime()
					+ "','YYYY-MM-DD')  AND TO_DATE('"
					+ deprptvo.getEndTime() + "','YYYY-MM-DD') ";
			break;
		case 2:
			// sqlserver2005
			nullFunction = "ISNULL";
			lianjiefuhao = "+";
			// timeFunction =
			// "  CAST(STR(DATAREPORT."+TableMtDatareport.IYMD+") AS DATETIME) ";
			timeStrCondition[0] = " AND CAST(STR(dp." + TableMtDatareport.IYMD
					+ ") AS DATETIME) >= CAST('" + deprptvo.getSendTime()
					+ "'  AS DATETIME)";
			timeStrCondition[1] = " AND CAST(STR(dp." + TableMtDatareport.IYMD
					+ ") AS DATETIME) <= CAST('" + deprptvo.getEndTime()
					+ "'   AS DATETIME)";
			timeStrCondition[2] = " AND CAST(STR(dp." + TableMtDatareport.IYMD
					+ ") AS DATETIME) BETWEEN  CAST('"
					+ deprptvo.getSendTime()
					+ "'  AS DATETIME)  AND  CAST('"
					+ deprptvo.getEndTime() + "'  AS DATETIME)";
			break;
		case 3:
			// MYSQL
			nullFunction = "IFNULL";
			lianjiefuhao = "CONCAT(";
			// timeFunction =
			// "  CAST(STR(DATAREPORT."+TableMtDatareport.IYMD+") AS DATETIME) ";
			timeStrCondition[0] = " AND CAST(dp." + TableMtDatareport.IYMD
					+ " AS DATETIME) >= CAST('" + deprptvo.getSendTime()
					+ "'  AS DATETIME)";
			timeStrCondition[1] = " AND CAST(dp." + TableMtDatareport.IYMD
					+ " AS DATETIME) <= CAST('" + deprptvo.getEndTime()
					+ "'   AS DATETIME)";
			timeStrCondition[2] = " AND CAST(dp." + TableMtDatareport.IYMD
					+ " AS DATETIME) BETWEEN  CAST('"
					+ deprptvo.getSendTime()
					+ "'  AS DATETIME)  AND  CAST('"
					+ deprptvo.getEndTime() + "'  AS DATETIME)";
			break;
		case 4:
			// DB2
			nullFunction = "COALESCE";
			lianjiefuhao = "||";
			timeStrCondition[0] = " AND DATE(dp." + TableMtDatareport.IYMD
					+ ")>='" + deprptvo.getSendTime() + "'";
			timeStrCondition[1] = " AND DATE(dp." + TableMtDatareport.IYMD
					+ ")<='" + deprptvo.getEndTime() + "'";
			timeStrCondition[2] = " AND DATE(dp." + TableMtDatareport.IYMD
					+ ") BETWEEN '" + deprptvo.getSendTime() + "'  AND '"
					+ deprptvo.getEndTime() + "'";
			break;
		default:
			break;
		}
		// 是否多企业的条件条件查询
		String conditionSqldep = "  ";
		// 是否多企业的条件条件查询
		String conditionSqluser = "  ";

		if (null != corpCode && 0 != corpCode.length()
				&& StaticValue.getCORPTYPE() == 1) {
			conditionSqldep += " AND d." + TableLfDep.CORP_CODE + " = '"
					+ corpCode + "'";
			conditionSqluser += " AND sysuser." + TableLfSysuser.CORP_CODE
					+ " = '" + corpCode + "'";
		}

		String conditionSql = "  ";// 报表数据查询条件

		// 时间条件的查询
		if ((0 != deprptvo.getSendTime().length() && 0 == deprptvo
				.getEndTime().length())) {
			conditionSql += timeStrCondition[0];

		} else if (0 == deprptvo.getSendTime().length()
				&& 0 != deprptvo.getEndTime().length()) {
			conditionSql += timeStrCondition[1];

		} else if (0 != deprptvo.getSendTime().length()
				&& 0 != deprptvo.getEndTime().length()) {
			conditionSql += timeStrCondition[2];
		} else {
			// 如果没有时间查询条件，则默认查询当前年月份的
			String yearAndMonth[] = this.getYearAndMonth();

			if (null != yearAndMonth) {
				conditionSql += " AND dp." + TableMtDatareport.Y + "="
						+ yearAndMonth[0] + " " + " AND dp."
						+ TableMtDatareport.IMONTH + "=" + yearAndMonth[1] + "";
			}
		}
		
		StringBuffer depsql = null;//部门查询
		if(StaticValue.DBTYPE==3){
			depsql = new StringBuffer("select 0 "+TableLfSysuser.USER_ID+"," + TableLfDep.DEP_ID
					+ ","+ lianjiefuhao +"'[机构]'," + nullFunction + "("
					+ TableLfDep.DEP_NAME + ",'未知机构')) DEP_NAME");
		}else{
			depsql = new StringBuffer("select 0 "+TableLfSysuser.USER_ID+","  + TableLfDep.DEP_ID
					+ ",'[机构]'" + lianjiefuhao + nullFunction + "("
					+ TableLfDep.DEP_NAME + ",'未知机构') DEP_NAME");
		}
		String succfails = "";
		succfails = "null SUCC,null RFAIL2";
		String depi = "";
		if (null != depId) {
			depi = " SUPERIOR_ID=" + depId + " ";
		}
		//关联的部门sql
		depsql.append(",").append(succfails).append(" from LF_DEP d where " + depi+" AND d."+TableLfDep.DEP_STATE+"=1 " + conditionSqldep);
		
		StringBuffer usersql = null;
		String nullSql=null;
		
		//mySql
		if (StaticValue.DBTYPE == 3) {
			usersql = new StringBuffer("select 1 "+TableLfSysuser.USER_ID+"," +"USER_ID DEP_ID," + lianjiefuhao
					+ "'[操作员]'," + nullFunction + "("
					+ TableLfSysuser.USER_NAME + ",'未知操作员')) DEP_NAME");
			//未知的sql语句
			nullSql = " UNION SELECT -1 USER_ID,0 DEP_ID,'未知机构' DEP_NAME," + nullFunction
					+ "(sum(ICOUNT-RFAIL1),0) SUCC," + nullFunction
					+ "(sum(RFAIL2),0) RFAIL2  from MT_DATAREPORT dp "
					+ "left join LF_SYSUSER su on su.USER_CODE=dp.p1 "
					+ "where COALESCE(su.user_id,0)=0 " + conditionSql;
			// DB2
		}else if(StaticValue.DBTYPE == 4) {
			usersql = new StringBuffer("select 1 "+TableLfSysuser.USER_ID+"," +"USER_ID DEP_ID,'[操作员]'"
					+ lianjiefuhao + nullFunction + "("
					+ TableLfSysuser.USER_NAME + ",'未知操作员') DEP_NAME");
			//未知的sql语句
			nullSql = " UNION SELECT -1 USER_ID,0 DEP_ID,'未知机构' DEP_NAME," + nullFunction
					+ "(sum(ICOUNT-RFAIL1),0) SUCC," + nullFunction
					+ "(sum(RFAIL2),0) RFAIL2  from MT_DATAREPORT dp "
					+ "left join LF_SYSUSER su on su.USER_CODE=dp.p1 "
					+ "where COALESCE(su.user_id,0)=0 " + conditionSql;
		}else {
			//其他数据库
			usersql = new StringBuffer("select 1 "+TableLfSysuser.USER_ID+"," +"USER_ID,'[操作员]'" + lianjiefuhao
					+ nullFunction + "(" + TableLfSysuser.USER_NAME
					+ ",'未知操作员') USER_NAME");
			//未知的sql语句
			nullSql = " UNION SELECT -1 USER_ID,null,'未知机构'," + nullFunction
					+ " (sum(ICOUNT-RFAIL1),0) SUCC," + nullFunction
					+ "(sum(RFAIL2),0) RFAIL2  from MT_DATAREPORT dp "
					+ "left join LF_SYSUSER su on su.USER_CODE=dp.p1 "
					+ "where su.user_id is null " + conditionSql;
		}
		// StringBuffer usersql = new
		// StringBuffer("select USER_ID,'[操作员]'"+lianjiefuhao+nullFunction+"("+TableLfSysuser.USER_NAME+",'未知操作员') USER_NAME");
		String usersucc = "select sum(" + TableMtDatareport.ICOUNT + "-"
				+ TableMtDatareport.RFAIL1 + ") from "
				+ TableMtDatareport.TABLE_NAME
				+ " dp where dp.P1=sysuser.USER_CODE";
		String usersuccs = nullFunction + "((" + usersucc + conditionSql
				+ "),0) SUCC";
		String userfail = "select sum(" + TableMtDatareport.RFAIL2 + ") from "
				+ TableMtDatareport.TABLE_NAME
				+ " dp where dp.P1=sysuser.USER_CODE";
		String userfails = nullFunction + "((" + userfail + conditionSql
				+ "),0) RFAIL2";
		//所属部门条件
		String depIDadd = "";
		if (null != depId) {
			depIDadd = " and sysuser.DEP_ID=" + depId;
		}
		usersql.append(",").append(usersuccs).append(",").append(userfails)
				.append(
						" from LF_SYSUSER sysuser where sysuser.USER_ID<>1 "
								+ depIDadd + " " + conditionSqluser);
		String dep = "";
		//判断是否是多企业
		if (StaticValue.getCORPTYPE() == 1) {
			int dep_id = getInt("dep_Id",
					"select dep_id from  lf_dep where corp_code='" + corpCode
							+ "' and dep_level=1", StaticValue.EMP_POOLNAME);
			dep = dep_id + "";
		}else{
			dep="1";
		}
		//如果选中的是未知机构或顶级机构则需要查询未知机构
		if ("-3".equals(depId) || dep.equals(depId)) {
			sql.append(depsql).append(" union ").append(usersql)
					.append(nullSql);
		} else {
			sql.append(depsql).append(" union ").append(usersql);
		}

		// db2
		String totalsql = "";
		if (StaticValue.DBTYPE == 4) {
			totalsql = "select tot."+TableLfSysuser.USER_ID+  ",tot." + TableLfDep.DEP_ID + ",tot."
					+ TableLfDep.DEP_NAME
					+ ",tot.SUCC,tot.RFAIL2,0 "
					+ TableLfSysuser.NAME + ",0 " + TableMtDatareport.ICOUNT
					+ " from (" + sql.toString() + ") tot ";
		} else {
			//其他数据库
			totalsql = "select tot."+TableLfSysuser.USER_ID+  ",tot." + TableLfDep.DEP_ID + ",tot."
					+ TableLfDep.DEP_NAME
					+ ",tot.SUCC,tot.RFAIL2,null "
					+ TableLfSysuser.NAME + ",null " + TableMtDatareport.ICOUNT
					+ " from (" + sql.toString() + ") tot ";
		}

		//返回报表总的sql语句
		return totalsql;
	}

	/**
	 * 
	 * 获取当前的年份和月份的数组，用于限制查询当前月份的操作员报表
	 * 
	 * @return
	 */
	public String[] getYearAndMonth() {
		//时间和月份的数组
		String[] datetime = new String[2];

		try {
			//定义一个系统时间对象
			Calendar cal = Calendar.getInstance();
			//获取月份
			Integer month = cal.get(Calendar.MONTH);
			month += 1;
			datetime[0] = String.valueOf(cal.get(Calendar.YEAR));
			datetime[1] = String.valueOf(month);
		} catch (Exception e) {
			EmpExecutionContext.error(e,"机构统计报表获取当前年份和月份数组异常");
		}

		return datetime;
	}

	
	/**
	 * 短信机构报表sql语句生成 (无用)
	 * @param depId
	 * @param depreportvo
	 * @param corpCode
	 * @return
	 * @throws Exception
	 */
	public String getSqlByType2(String depId, DepRptVo depreportvo,String corpCode, String tablename, String depcodethird2p2) throws Exception 
	{
		// 数据库的判空函数 默认为oracle的判断NULL函数
		String nullFunction = "NVL";
		//数据的连接符号码
		String lianjiefuhao = "||";
		switch (StaticValue.DBTYPE) {
		case 1:
			// oracle
			break;
		case 2:
			// sqlserver2005
			nullFunction = "ISNULL";
			lianjiefuhao = "+";
			break;
		case 3:
			// MYSQL
			break;
		case 4:
			// DB2
			nullFunction = "COALESCE";
			lianjiefuhao = "||";
			break;
		default:
			break;
		}

		// 报表数据查询条件
		String conditionSql = " ";
		
		//发送类型
		if(depreportvo.getDatasourcetype() != null && depreportvo.getDatasourcetype() != 0)
		{
			conditionSql += " AND SENDTYPE="+depreportvo.getDatasourcetype();
		}
		
		//运营商   0国内；  1国外； 为null或其他值则为全部；
		if(depreportvo.getSpnumtype() != null)
		{
			if(depreportvo.getSpnumtype()==0)
			{
				conditionSql += " AND SPISUNCM IN (0,1,21) ";
			}
			else if(depreportvo.getSpnumtype()==1)
			{
				conditionSql += " AND SPISUNCM=5 ";
			}
		}
		
		// 时间条件的查询
		String timewheresql = " WHERE IYMD>="+ depreportvo.getSendTime()+ " AND IYMD<="+ depreportvo.getEndTime() + " ";

		//机构语句
		StringBuffer depsql = null;
		//其他数据库
		if(StaticValue.DBTYPE==4){
			//db2数据库
			depsql = new StringBuffer("select DEP_ID,'[机构]'" + lianjiefuhao + nullFunction + "(DEP_NAME,'未知机构') DEP_NAME,-1 USERSTATE,'1' IDTYPE");
		}else{
			depsql = new StringBuffer("select DEP_ID,'[机构]'" + lianjiefuhao + nullFunction + "(DEP_NAME,'未知机构') DEP_NAME,null USERSTATE,'1' IDTYPE");
		}
		
		//部门成功sql
		String depsucc = "";
		//部门失败sql
		String depfail = "";
		//未知sql
		String nullSql = "";
		
		//各种不同数据库
		switch (StaticValue.DBTYPE) {
		case 1:
			// oracle
		case 2:
			// sqlserver
			depsucc = "select sum(ICOUNT-RFAIL1) from (select ICOUNT,RFAIL1,P1,P2 from " + tablename + timewheresql + conditionSql+") dp ";
			depfail = "select sum(RFAIL2) from (select RFAIL2,P1,P2 from " + tablename + timewheresql + conditionSql+") dp ";
			if ("true".equals(depcodethird2p2)) 
			{
				depsucc += " left join LF_DEP dep on dep.DEP_CODE_THIRD=dp.P2 ";
				depfail += " left join LF_DEP dep on dep.DEP_CODE_THIRD=dp.P2 ";
			}
			else
			{
				depsucc += " left join LF_SYSUSER su on su.USER_CODE=dp.P1 left join LF_DEP dep on dep.DEP_ID=su.DEP_ID ";
				depfail += " left join LF_SYSUSER su on su.USER_CODE=dp.P1 left join LF_DEP dep on dep.DEP_ID=su.DEP_ID ";
			}
			depsucc += " where dep.DEP_ID<>" + depId + " and  d.DEP_PATH=SUBSTRING(dep.DEP_PATH,0,len(d.DEP_PATH)+1) AND dep.DEP_STATE=1 ";
			depfail += " where dep.DEP_ID<>" + depId + " and  d.DEP_PATH=SUBSTRING(dep.DEP_PATH,0,len(d.DEP_PATH)+1) AND dep.DEP_STATE=1 ";

			break;
		case 3:
			// MYSQL
		case 4:
			// DB2
			depsucc = "select sum(ICOUNT-RFAIL1) from (select ICOUNT,RFAIL1,P1,P2 from "
					+ tablename+timewheresql+conditionSql+") dp left join LF_SYSUSER su on su.USER_CODE=dp.P1 left join LF_DEP dep on dep.DEP_ID=su.DEP_ID " 
					+ " where dep.DEP_ID<>"	+ depId + " and  d.DEP_PATH=SUBSTR(dep.DEP_PATH,1,length(d.DEP_PATH)) AND dep.DEP_STATE=1 ";

			depfail = "select sum(RFAIL2) from (select RFAIL2,P1,P2 from " + tablename+timewheresql+conditionSql+") dp ";
			if ("true".equals(depcodethird2p2)) {
				depfail=depfail+ " left join LF_DEP dep on dep.DEP_CODE_THIRD=dp.P2 "; 
			}else{
				depfail=depfail+ " left join LF_SYSUSER su on su.USER_CODE=dp.P1 left join LF_DEP dep on dep.DEP_ID=su.DEP_ID "; 
			} 
			depfail=depfail+ " where dep.DEP_ID<>" + depId + " and  d.DEP_PATH=SUBSTR(dep.DEP_PATH,1,length(d.DEP_PATH)) AND dep."+TableLfDep.DEP_STATE+"=1 ";
			break;
		default:
			break;
		}

		//关联的部门sql
		depsql.append(",").append(nullFunction + "((" + depsucc + "),0) SUCC").append(",").append(nullFunction + "((" + depfail + "),0) RFAIL2")
				.append(" from LF_DEP d where " + " SUPERIOR_ID=" + depId + " AND d."+TableLfDep.DEP_STATE+"=1 AND d.CORP_CODE = '" + corpCode + "'");

		StringBuffer usersql = null;
		
		if(StaticValue.DBTYPE == 4) {
		// DB2
			usersql = new StringBuffer("select USER_ID DEP_ID,'[操作员]'" + lianjiefuhao + nullFunction + "(USER_NAME,'未知操作员') DEP_NAME,USER_STATE USERSTATE,'2' IDTYPE");
			nullSql = " UNION ALL SELECT 0 DEP_ID,'未知机构' DEP_NAME,-1 USERSTATE,'3' IDTYPE," + nullFunction
					+ "(sum(ICOUNT-RFAIL1),0) SUCC," + nullFunction
					+ "(sum(RFAIL2),0) RFAIL2  from "+tablename+" dp "
					+ "left join LF_SYSUSER su on su.USER_CODE=dp.p1 "
					+ timewheresql+ conditionSql+" AND COALESCE(su.user_id,0)=0 ";
		}else {
		//其他数据库
			usersql = new StringBuffer("select USER_ID,'[操作员]'" + lianjiefuhao
					+ nullFunction + "(USER_NAME,'未知操作员') USER_NAME,USER_STATE USERSTATE,'2' IDTYPE");
			nullSql = " UNION ALL SELECT null,'未知机构',-1 USERSTATE,'3' IDTYPE," + nullFunction
					+ " (sum(ICOUNT-RFAIL1),0) SUCC," + nullFunction
					+ "(sum(RFAIL2),0) RFAIL2  from "+tablename+" dp "
					+ "left join LF_SYSUSER su on su.USER_CODE=dp.p1 "
					+ timewheresql+ conditionSql+" AND su.user_id is null ";
		}
		
		String usersucc = "select sum(ICOUNT-RFAIL1) from " + tablename + " dp where dp.P1=sysuser.USER_CODE";
		//操作员失败数
		String userfails = nullFunction + "((" + "select sum(RFAIL2) from " + tablename + " dp where dp.P1=sysuser.USER_CODE" + conditionSql + "),0) RFAIL2";
		
		//操作员语句
		usersql.append(",").append(nullFunction + "((" + usersucc + conditionSql + "),0) SUCC").append(",").append(userfails)
				.append(" from LF_SYSUSER sysuser where sysuser.USER_ID<>1 and sysuser.DEP_ID=" + depId + " AND sysuser.CORP_CODE = '" + corpCode + "'");
		
		StringBuffer sql = new StringBuffer().append(depsql).append(" UNION ALL ").append(usersql);
		
		int dep_id = getInt("dep_Id",
				"select dep_id from  lf_dep where corp_code='" + corpCode + "' and dep_level=1", StaticValue.EMP_POOLNAME);
		String dep = dep_id + "";
		
		if (dep.equals(depId)) 
		{
			sql.append(nullSql);
		}
		
		// db2
		String totalsql = "";
		if (StaticValue.DBTYPE == 4) 
		{
			totalsql = "select tot.DEP_ID,tot.DEP_NAME,tot.SUCC,tot.RFAIL2 ,tot.USERSTATE USER_STATE,tot.IDTYPE IDTYPE,0 USER_ID,0 NAME from (" + sql.toString() + ") tot ";
		}
		else 
		{
			totalsql = "select tot.DEP_ID,tot.DEP_NAME,tot.SUCC,tot.RFAIL2 ,tot.USERSTATE USER_STATE,tot.IDTYPE IDTYPE,null USER_ID,null NAME from (" + sql.toString() + ") tot ";
		}

		return totalsql;
	}
	
	
	/**
	 * 根据当前操作员机构ID查询机构报表信息  无分页
	 * @param id
	 * @param deprptvo
	 * @param corpCode
	 * @return
	 * @throws Exception
	 */
	public List<DynaBean> getDepDetailReportList(String id, DepRptVo deprptvo,String corpCode) throws Exception {
		String sql= this.getDetailSqlByIdType(id, deprptvo, corpCode);
		if(sql==null||"".equals(sql)){;
			return null;
		}
		String oderbystr="";
		if(deprptvo.getReporttype()!=null&&deprptvo.getReporttype()==2){
			oderbystr=" Y DESC,IMONTH DESC";
		}else{
			oderbystr=" IYMD DESC";
		}
		//加上排序
		String dataSql = sql + " order by "+oderbystr+" ";
		List<DynaBean> returnList = new DataAccessDriver().getGenericDAO().findDynaBeanBySql(dataSql);
		return returnList;
	}
	
	
	/**
	 * 根据当前操作员机构ID查询机构报表信息  无分页
	 * @param id
	 * @param deparearptvo
	 * @param corpCode
	 * @return
	 * @throws Exception
	 */
	public List<DynaBean> getAreaDepDetailReportList(String id, DepAreaRptVo deparearptvo,String corpCode) throws Exception {
		String sql= this.getAreaDetailSqlByIdType(id, deparearptvo, corpCode);
		if(sql==null||"".equals(sql)){;
			return null;
		}
		String oderbystr=" ac.AREACODE,ac.AREANAME,xg.SPGATE,xg.GATENAME ";
		
		//加上排序
		String dataSql = sql + " order by "+oderbystr+" ASC ";
		List<DynaBean> returnList = new DataAccessDriver().getGenericDAO().findDynaBeanBySql(dataSql);
		return returnList;
	}
	
	
	/**
	 * 
	 * 根据企业编码，或者对应的操作员统计报表信息 （如果企业编码为空，则显示所有）
	 * 
	 * @param curUserId
	 *            当期登录的操作员ID
	 * 
	 * @param userIds
	 *            管辖范围内的操作员ID
	 * 
	 * @param depIds
	 *            所管辖的机构ids
	 * @param deprptvo
	 * 
	 * @param corpCode
	 *            企业编码
	 * @param pageInfo
	 * 
	 * @return
	 * 
	 * 
	 * @throws Exception
	 */
	public DepRptVo getDepRptVoByDepIDs(String depIds, DepRptVo deprptvo,String corpCode,Connection conn) throws Exception {
		// 获取操作员报表用的sql语句
		String sql = this.getSql3(depIds,deprptvo,corpCode);
		
		//System.out.println("sql:   " + sql);
		List<DepRptVo> returnList =findVoListBySQL(conn,DepRptVo.class, sql, StaticValue.EMP_POOLNAME);
		DepRptVo rdeprptvo=null;
		if(returnList!=null&&returnList.size()>0){
			rdeprptvo=returnList.get(0);
		}
		returnList=null;
		return rdeprptvo;
	}
	
	/**
	 * 
	 * 个人权限用
	 * 
	 * @param curUserId
	 * @param deprptvo
	 * @param corpCode
	 * @param pageInfo
	 * @return
	 * @throws Exception
	 */
	public List<DepRptVo> getDepRptVoListPersonal(Long curUserId,
			DepRptVo deprptvo, String corpCode, PageInfo pageInfo)
			throws Exception {

		return this.getDepReportList(curUserId, null,deprptvo, corpCode, pageInfo);

	}

	/**
	 * 
	 * 获取查询操作员报表用的主SQL语句
	 * 
	 * @param curUserId
	 * @param depIds
	 * @param userIds
	 * @param deprptvo
	 * @param corpCode
	 * @return
	 * @throws Exception
	 * 
	 */
	public String getSql(Long curUserId, String depIds, String userIds,
			DepRptVo deprptvo, String corpCode) {
		String sql = "";

		String multiSql = "";

		String nullFunction = "NVL";// 数据库的判空函数 默认为oracle的判断NULL函数

		// String timeFunction =
		// " TO_DATE(DATAREPORT."+TableMtDatareport.IYMD+",'YYYY-MM-DD') ";//数据库的时间函数
		// 默认为oracle的

		String[] timeStrCondition = new String[3];

		switch (StaticValue.DBTYPE) {
		case 1:
			// oracle 是否为空函数
			nullFunction = "NVL";
			// timeFunction =
			// " TO_DATE(DATAREPORT."+TableMtDatareport.IYMD+",'YYYY-MM-DD') ";
			timeStrCondition[0] = " AND TO_DATE(DATAREPORT."
					+ TableMtDatareport.IYMD + ",'YYYY-MM-DD')>=TO_DATE('"
					+ deprptvo.getSendTime() + "','YYYY-MM-DD') ";
			timeStrCondition[1] = " AND TO_DATE(DATAREPORT."
					+ TableMtDatareport.IYMD + ",'YYYY-MM-DD')<=TO_DATE('"
					+ deprptvo.getEndTime() + "','YYYY-MM-DD') ";
			timeStrCondition[2] = " AND TO_DATE(DATAREPORT."
					+ TableMtDatareport.IYMD
					+ ",'YYYY-MM-DD') BETWEEN TO_DATE('"
					+ deprptvo.getSendTime()
					+ "','YYYY-MM-DD')  AND TO_DATE('"
					+ deprptvo.getEndTime() + "','YYYY-MM-DD') ";
			break;
		case 2:
			// sqlserver2005
			nullFunction = "ISNULL";
			// timeFunction =
			// "  CAST(STR(DATAREPORT."+TableMtDatareport.IYMD+") AS DATETIME) ";
			timeStrCondition[0] = " AND CAST(STR(DATAREPORT."
					+ TableMtDatareport.IYMD + ") AS DATETIME) >= CAST('"
					+ deprptvo.getSendTime() + "'  AS DATETIME)";
			timeStrCondition[1] = " AND CAST(STR(DATAREPORT."
					+ TableMtDatareport.IYMD + ") AS DATETIME) <= CAST('"
					+ deprptvo.getEndTime() + "'   AS DATETIME)";
			timeStrCondition[2] = " AND CAST(STR(DATAREPORT."
					+ TableMtDatareport.IYMD + ") AS DATETIME) BETWEEN  CAST('"
					+ deprptvo.getSendTime()
					+ "'  AS DATETIME)  AND  CAST('"
					+ deprptvo.getEndTime() + "'  AS DATETIME)";
			break;
		case 3:
			// MYSQL
			break;
		case 4:
			// DB2
			break;
		default:
			break;
		}

		switch (StaticValue.getCORPTYPE()) {
		case 0:
			// 单企业
			multiSql = "";
			break;
		case 1:
			// 多企业
			multiSql = " LEFT JOIN " + TableLfSpDepBind.TABLE_NAME
					+ " SPBIND ON UPPER(DATAREPORT."
					+ TableMtDatareport.USER_ID + ")=UPPER("
					+ TableLfSpDepBind.SP_USER + ")";
			break;
		default:
			break;
		}
		sql = "SELECT "
		// + timeFunction
				// +" IYMD,"
				// +" DATAREPORT."+TableMtDatareport.USER_ID+" SP"+TableMtDatareport.USER_ID+","
				+ " (SUM(DATAREPORT." + TableMtDatareport.ICOUNT
				+ ")-SUM(DATAREPORT." + TableMtDatareport.RFAIL1 + ")) SUCC,"
				+ " SUM(DATAREPORT." + TableMtDatareport.RFAIL2 + ")  RFAIL2,"
				+ " SUM(DATAREPORT." + TableMtDatareport.ICOUNT + ") ICOUNT,"
				+ " MAX(SYSUSER." + TableLfSysuser.USER_ID + ") USER_ID, "
				+ " DEP." + TableLfDep.DEP_ID + " DEP_ID, " + " MAX("
				+ nullFunction + "(SYSUSER." + TableLfSysuser.NAME
				+ ",'未知操作员')) NAME," + " MAX(" + nullFunction + "(DEP."
				+ TableLfDep.DEP_NAME + ",'未知机构')) DEP_NAME " + " FROM "
				+ TableMtDatareport.TABLE_NAME + " DATAREPORT " + multiSql
				+ " LEFT JOIN " + TableLfSysuser.TABLE_NAME
				+ " SYSUSER ON SYSUSER." + TableLfSysuser.USER_CODE
				+ " = DATAREPORT." + TableMtDatareport.P1 + " LEFT JOIN "
				+ TableLfDep.TABLE_NAME + " DEP ON SYSUSER."
				+ TableLfSysuser.DEP_ID + " = DEP." + TableLfDep.DEP_ID
				+ "";

		// String countSql = "select count(*) totalcount FROM (";
		String domination = " ";
		String conditionSql = "  ";// 条件查询

		String baseSql = "  GROUP BY "
		// +"DATAREPORT."+TableMtDatareport.IYMD+","
				+ "  DEP.DEP_ID ";

		if (!"".equals(multiSql)) {
			// 非单企业
			baseSql += "   ORDER BY DEP." + TableLfDep.DEP_ID + " ASC ";
		}

		if (null != curUserId)// 个人权限用，查询当前操作员的相关报表信息
		{
			domination += " AND (SYSUSER." + TableLfSysuser.USER_ID + " = "
					+ curUserId + "  OR SYSUSER." + TableLfSysuser.USER_ID
					+ " IS NULL )";
		}

		if (null != userIds && 0 != userIds.length()) {
			// domination +=
			// " AND (SYSUSER."+TableLfSysuser.USER_ID+" in ("+userIds+")  OR SYSUSER."+TableLfSysuser.USER_ID+" IS NULL )";

			if ("-1".equals(userIds)) {
				domination += " AND (SYSUSER." + TableLfSysuser.USER_ID
						+ " IS NULL )";
			} else if (!"-1".equals(userIds) && userIds.contains("-1")) {
				domination += " AND (SYSUSER." + TableLfSysuser.USER_ID
						+ " in (" + userIds + ")  OR SYSUSER."
						+ TableLfSysuser.USER_ID + " IS NULL )";
			} else {
				domination += " AND (SYSUSER." + TableLfSysuser.USER_ID
						+ " in (" + userIds + ") )";
			}

		}

		conditionSql += domination;

		if (null != corpCode && 0 != corpCode.length() && !"".equals(multiSql)) {
			conditionSql += " AND SPBIND." + TableLfSpDepBind.CORP_CODE
					+ " = '" + corpCode + "'";
		}
		if (null != depIds && 0 != depIds.length()) {

			if ("-3".equals(depIds)) {
				conditionSql += " AND (DEP." + TableLfDep.DEP_ID + " IS NULL)";
			} else if (!"-3".equals(depIds) && depIds.contains("-3")) {
				conditionSql += " AND (DEP." + TableLfDep.DEP_ID + " in ("
						+ depIds + ") OR DEP.DEP_ID IS NULL)";
			} else {
				conditionSql += " AND (DEP." + TableLfDep.DEP_ID + " in ("
						+ depIds + "))";
			}
			// conditionSql +=
			// " AND (DEP."+TableLfDep.DEP_ID+" in ("+depIds+") OR DEP.DEP_ID IS NULL)";

		}

		// 时间条件的查询
		if ((0 != deprptvo.getSendTime().length() && 0 == deprptvo
				.getEndTime().length())) {
			conditionSql += timeStrCondition[0];

		} else if (0 == deprptvo.getSendTime().length()
				&& 0 != deprptvo.getEndTime().length()) {
			conditionSql += timeStrCondition[1];

		} else if (0 != deprptvo.getSendTime().length()
				&& 0 != deprptvo.getEndTime().length()) {
			conditionSql += timeStrCondition[2];
		} else {
			// 如果没有时间查询条件，则默认查询当前年月份的
			String yearAndMonth[] = this.getYearAndMonth();

			if (null != yearAndMonth) {
				conditionSql += " AND DATAREPORT." + TableMtDatareport.Y + "="
						+ yearAndMonth[0] + " " + " AND DATAREPORT."
						+ TableMtDatareport.IMONTH + "=" + yearAndMonth[1] + "";
			}
		}

		sql += conditionSql;
		sql += baseSql;

		return sql;

	}

}
