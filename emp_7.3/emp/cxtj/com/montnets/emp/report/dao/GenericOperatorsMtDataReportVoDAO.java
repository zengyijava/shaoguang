package com.montnets.emp.report.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.util.List;

import com.montnets.emp.common.constant.StaticValue;
import com.montnets.emp.common.context.EmpExecutionContext;
import com.montnets.emp.common.dao.SuperDAO;
import com.montnets.emp.common.dao.impl.DataAccessDriver;
import com.montnets.emp.inbox.dao.ReciveBoxDao;
import com.montnets.emp.report.vo.OperatorsAreaMtDataReportVo;
import com.montnets.emp.report.vo.OperatorsMtDataReportVo;
import com.montnets.emp.table.report.TableMtDatareport;
import com.montnets.emp.util.PageInfo;

/**
 * 运营商统计报表
 * @project p_cxtj
 * @author liaojirong <ljr0300@163.com>
 * @company ShenZhen Montnets Technology CO.,LTD.
 * @datetime 2013-11-22 下午02:47:08
 * @description
 */
public class GenericOperatorsMtDataReportVoDAO extends SuperDAO{

	/**
	 * 运营商统计报表 年月 无分页
	 * @param operatorsMtDataReportVo
	 * @return
	 * @throws Exception
	 */
	public List<OperatorsMtDataReportVo> findOperatorsMtDataReportVoByMonth(
			OperatorsMtDataReportVo operatorsMtDataReportVo)
			throws Exception {

		//获取查询字段
		String fieldSql = GenericOperatorsMtDataReportVoSQL.getMonthFieldSql();

		//获取查询表
		String tableSql = GenericOperatorsMtDataReportVoSQL
				.getMonthTableSql(operatorsMtDataReportVo.getMstype());

		//获取查询条件
		String conditionSql = GenericOperatorsMtDataReportVoSQL
				.getMonthConditionSql(operatorsMtDataReportVo);
		// 处理SQL条件,不允许使用1 =1方式
		conditionSql =new ReciveBoxDao().getConditionSql(conditionSql);
		//获取分组条件
		String groupBySql = GenericOperatorsMtDataReportVoSQL
				.getMonthGroupBySql(operatorsMtDataReportVo);

		//获取排序条件
		String orderBySql = GenericOperatorsMtDataReportVoSQL
				.getMonthOrderBySql(operatorsMtDataReportVo);

		//总sql
		String sql = new StringBuffer(fieldSql).append(tableSql).append(
				conditionSql).append(groupBySql).append(orderBySql).toString();

		//返回集合
		List<OperatorsMtDataReportVo> returnList = findVoListBySQL(
				OperatorsMtDataReportVo.class, sql, StaticValue.EMP_POOLNAME);
		return returnList;
	}

	/**
	 * 运营商统计报表   月报表 分页
	 * @param operatorsMtDataReportVo
	 * @param pageInfo
	 * @return
	 * @throws Exception
	 */
	public List<OperatorsMtDataReportVo> findOperatorsMtDataReportVoByMonth(
			OperatorsMtDataReportVo operatorsMtDataReportVo, PageInfo pageInfo) throws Exception {
		//查询字段
		String fieldSql = GenericOperatorsMtDataReportVoSQL.getMonthFieldSql();

		//查询表
		String tableSql = GenericOperatorsMtDataReportVoSQL
				.getMonthTableSql(operatorsMtDataReportVo.getMstype());

		//查询条件
		String conditionSql = GenericOperatorsMtDataReportVoSQL
				.getMonthConditionSql(operatorsMtDataReportVo);
		// 处理SQL条件,不允许使用1 =1方式
		conditionSql =new ReciveBoxDao().getConditionSql(conditionSql);

		//分组sql
		String groupBySql = GenericOperatorsMtDataReportVoSQL
				.getMonthGroupBySql(operatorsMtDataReportVo);

		//排序sql
		String orderBySql = GenericOperatorsMtDataReportVoSQL
				.getMonthOrderBySql(operatorsMtDataReportVo);

		//总sql
		String sql = new StringBuffer(fieldSql).append(tableSql).append(
				conditionSql).append(groupBySql).append(orderBySql).toString();
		//分页sql
		String countSql = new StringBuffer("select count(*) totalcount from (")
				.append("select count(*) tcount").append(tableSql).append(
						conditionSql).append(groupBySql).append(") A")
				.toString();
		//查询结果集
		List<OperatorsMtDataReportVo> returnList = new DataAccessDriver().getGenericDAO().findPageVoListBySQL(
				OperatorsMtDataReportVo.class, sql, countSql, pageInfo,
				StaticValue.EMP_POOLNAME);
		return returnList;
	}

	/**
	 *运营商统计报表 年报表  无分页
	 * @param operatorsMtDataReportVo
	 * @return
	 * @throws Exception
	 */
	public List<OperatorsMtDataReportVo> findOperatorsMtDataReportVoByYear(
			OperatorsMtDataReportVo operatorsMtDataReportVo)
			throws Exception {

		//查询字段
		String fieldSql = GenericOperatorsMtDataReportVoSQL.getYearFieldSql();

		//查询表
		String tableSql = GenericOperatorsMtDataReportVoSQL
				.getYearTableSql(operatorsMtDataReportVo.getMstype());

		//查询条件
		String conditionSql = GenericOperatorsMtDataReportVoSQL
				.getYearConditionSql(operatorsMtDataReportVo);
		// 处理SQL条件,不允许使用1 =1方式
		conditionSql =new ReciveBoxDao().getConditionSql(conditionSql);
		//分组sql
		String groupBySql = GenericOperatorsMtDataReportVoSQL
				.getYearGroupBySql(operatorsMtDataReportVo);

		//排序sql
		String orderBySql = GenericOperatorsMtDataReportVoSQL
				.getYearOrderBySql(operatorsMtDataReportVo);

		//总sql
		String sql = new StringBuffer(fieldSql).append(tableSql).append(
				conditionSql).append(groupBySql).append(orderBySql).toString();

		List<OperatorsMtDataReportVo> returnList = findVoListBySQL(
				OperatorsMtDataReportVo.class, sql, StaticValue.EMP_POOLNAME);
		return returnList;
	}

	/**
	 * 运营商报表 年报表 分页
	 * @param operatorsMtDataReportVo
	 * @param pageInfo
	 * @return
	 * @throws Exception
	 */
	public List<OperatorsMtDataReportVo> findOperatorsMtDataReportVoByYear(
			OperatorsMtDataReportVo operatorsMtDataReportVo, PageInfo pageInfo) throws Exception {
		
		//查询字段
		String fieldSql = GenericOperatorsMtDataReportVoSQL.getYearFieldSql();

		//查询表
		String tableSql = GenericOperatorsMtDataReportVoSQL
				.getYearTableSql(operatorsMtDataReportVo.getMstype());
		//查询条件
		String conditionSql = GenericOperatorsMtDataReportVoSQL
				.getYearConditionSql(operatorsMtDataReportVo);
		// 处理SQL条件,不允许使用1 =1方式
		conditionSql =new ReciveBoxDao().getConditionSql(conditionSql);
		//分组sql
		String groupBySql = GenericOperatorsMtDataReportVoSQL
				.getYearGroupBySql(operatorsMtDataReportVo);

		//排序sql
		String orderBySql = GenericOperatorsMtDataReportVoSQL
				.getYearOrderBySql(operatorsMtDataReportVo);

		//总sql
		String sql = new StringBuffer(fieldSql).append(tableSql).append(
				conditionSql).append(groupBySql).append(orderBySql).toString();

		//分页
		String countSql = new StringBuffer("select count(*) totalcount from (")
				.append("select count(*) tcount").append(tableSql).append(
						conditionSql).append(groupBySql).append(") A")
				.toString();
		List<OperatorsMtDataReportVo> returnList = new DataAccessDriver().getGenericDAO().findPageVoListBySQL(
				OperatorsMtDataReportVo.class, sql, countSql, pageInfo,
				StaticValue.EMP_POOLNAME);
		return returnList;
	}

	/**
	 *运营商统计报表 日报表  无分页
	 * @param operatorsMtDataReportVo
	 * @return
	 * @throws Exception
	 */
	public List<OperatorsMtDataReportVo> findOperatorsMtDataReportVoByDays(
			OperatorsMtDataReportVo operatorsMtDataReportVo)
			throws Exception {

		//查询字段
		String fieldSql = GenericOperatorsMtDataReportVoSQL.getDaysFieldSql();

		//查询表
		String tableSql = GenericOperatorsMtDataReportVoSQL
				.getDaysTableSql(operatorsMtDataReportVo.getMstype());
		
		//查询条件
		String conditionSql = GenericOperatorsMtDataReportVoSQL
				.getDaysConditionSql(operatorsMtDataReportVo);
		// 处理SQL条件,不允许使用1 =1方式
		conditionSql =new ReciveBoxDao().getConditionSql(conditionSql);
		
		//分组sql
		String groupBySql = GenericOperatorsMtDataReportVoSQL
				.getDaysGroupBySql(operatorsMtDataReportVo);

		//排序sql
		String orderBySql = GenericOperatorsMtDataReportVoSQL
				.getDaysOrderBySql();

		//总sql
		String sql = "";
		if(operatorsMtDataReportVo.getIsDes()){
			sql = new StringBuffer(fieldSql).append(tableSql).append(
					conditionSql).append(groupBySql).append(orderBySql).toString();
		}
		else
		{
			sql = new StringBuffer(fieldSql).append(tableSql).append(
					conditionSql).append(groupBySql).toString();
		} 

		List<OperatorsMtDataReportVo> returnList = findVoListBySQL(
				OperatorsMtDataReportVo.class, sql, StaticValue.EMP_POOLNAME);
		return returnList;
	}

	/**
	 * 运营商报表 日报表 分页
	 * @param operatorsMtDataReportVo
	 * @param pageInfo
	 * @return
	 * @throws Exception
	 */
	public List<OperatorsMtDataReportVo> findOperatorsMtDataReportVoByDays(
			OperatorsMtDataReportVo operatorsMtDataReportVo, PageInfo pageInfo) throws Exception {
		
		//查询字段
		String fieldSql = GenericOperatorsMtDataReportVoSQL.getDaysFieldSql();

		//查询表
		String tableSql = GenericOperatorsMtDataReportVoSQL
				.getDaysTableSql(operatorsMtDataReportVo.getMstype());
		//查询条件
		String conditionSql = GenericOperatorsMtDataReportVoSQL
				.getDaysConditionSql(operatorsMtDataReportVo);
		// 处理SQL条件,不允许使用1 =1方式
		conditionSql =new ReciveBoxDao().getConditionSql(conditionSql);

		//分组sql
		String groupBySql = GenericOperatorsMtDataReportVoSQL
				.getDaysGroupBySql(operatorsMtDataReportVo);

		//排序sql
		String orderBySql = GenericOperatorsMtDataReportVoSQL
				.getDaysOrderBySql();

		//总sql
		String sql = "";
		if(operatorsMtDataReportVo.getIsDes()){
			sql = new StringBuffer(fieldSql).append(tableSql).append(
					conditionSql).append(groupBySql).append(orderBySql).toString();
		}
		else
		{
			sql = new StringBuffer(fieldSql).append(tableSql).append(
					conditionSql).append(groupBySql).toString();
		} 

		//分页
		String countSql = new StringBuffer("select count(*) totalcount from (")
				.append("select count(*) tcount").append(tableSql).append(
						conditionSql).append(groupBySql).append(") A")
				.toString();
		List<OperatorsMtDataReportVo> returnList = new DataAccessDriver().getGenericDAO().findPageVoListBySQL(
				OperatorsMtDataReportVo.class, sql, countSql, pageInfo,
				StaticValue.EMP_POOLNAME);
		return returnList;
	}

	
	
	
	/**
	 * 查询合计
	 * @param operatorsMtDataReportVo
	 * @return
	 * @throws Exception
	 */
	public long[] findSumCount(OperatorsMtDataReportVo operatorsMtDataReportVo) throws Exception {
		long icount = 0L;
		long rfail1 = 0L;
		long rfail2 = 0L;
		long rnret = 0L;
		long rsucc = 0L;

		//查询字段
		String fieldSql = new StringBuffer("select sum(").append(TableMtDatareport.ICOUNT).append(") ICOUNT")
				.append(",sum(").append(TableMtDatareport.RFAIL1).append(") RFAIL1")
				.append(",sum(").append(TableMtDatareport.RFAIL2).append(") RFAIL2")
				.append(",sum(").append(TableMtDatareport.RNRET).append(") RNRET")
				.append(",sum(").append(TableMtDatareport.RSUCC).append(") RSUCC").toString();

		//查询table
		String tableSql = "";

		//查询条件
		String conditionSql = "";
		
		Integer type=operatorsMtDataReportVo.getReporttype();
		//月报表
		if(type==0){
			tableSql = GenericOperatorsMtDataReportVoSQL
			.getMonthTableSql(operatorsMtDataReportVo.getMstype());
			
			conditionSql = GenericOperatorsMtDataReportVoSQL
			.getMonthConditionSql(operatorsMtDataReportVo);
			// 处理SQL条件,不允许使用1 =1方式
			conditionSql =new ReciveBoxDao().getConditionSql(conditionSql);
		}
		//年报表
		else if(type==1)
		{
			tableSql = GenericOperatorsMtDataReportVoSQL
			.getYearTableSql(operatorsMtDataReportVo.getMstype());
			
			conditionSql = GenericOperatorsMtDataReportVoSQL
			.getYearConditionSql(operatorsMtDataReportVo);
			// 处理SQL条件,不允许使用1 =1方式
			conditionSql =new ReciveBoxDao().getConditionSql(conditionSql);
		}
		//日报表
		else 
		{
			tableSql = GenericOperatorsMtDataReportVoSQL
			.getDaysTableSql(operatorsMtDataReportVo.getMstype());
			
			conditionSql = GenericOperatorsMtDataReportVoSQL
			.getDaysConditionSql(operatorsMtDataReportVo); 
			// 处理SQL条件,不允许使用1 =1方式
			conditionSql =new ReciveBoxDao().getConditionSql(conditionSql);
		}
		
		String sql = new StringBuffer(fieldSql).append(tableSql).append(
				conditionSql).toString();
		Connection conn = null;
		PreparedStatement ps = null;
		java.sql.ResultSet rs = null;
		try {
			//获取连接
			conn = connectionManager.getDBConnection(StaticValue.EMP_POOLNAME);
			//获取操作对象
			ps = conn.prepareStatement(sql);
			//获取结果集
			rs = ps.executeQuery();
			//判断是否有值
			if (rs.next()) {
				icount = rs.getLong("ICOUNT");
				rfail1 = rs.getLong("RFAIL1");
				rfail2 = rs.getLong("RFAIL2");
				rnret = rs.getLong("RNRET");
				rsucc = rs.getLong("RSUCC");
			}
		} catch (Exception e) {
			EmpExecutionContext.error(e," 查询合计处理异常");
			throw e;
		} finally {
			//关闭连接
			close(rs, ps, conn);
		}
		long[] returnLong = new long[5];
		returnLong[0] = icount;
		returnLong[1] = rfail1;
		returnLong[2] = rfail2;
		returnLong[3] = rnret;
		returnLong[4] = rsucc;
		return returnLong;
	}
	
	
	/**
	 *运营商统计报表  各国详情  无分页
	 * @param operatorsAreaMtDataReportVo
	 * @return
	 * @throws Exception
	 */
	
	public List<OperatorsAreaMtDataReportVo> findOperatorsMtDataReportVoByArea(
			OperatorsAreaMtDataReportVo operatorsAreaMtDataReportVo)
			throws Exception {
		
		//查询字段
		String fieldSql = GenericOperatorsMtDataReportVoSQL.getAreaFieldSql();
		//查询表
		String tableSql = GenericOperatorsMtDataReportVoSQL
					.getAreaTableSql(operatorsAreaMtDataReportVo.getMstype());
		
		//查询条件
		String conditionSql = GenericOperatorsMtDataReportVoSQL
					.getAreaConditionSql(operatorsAreaMtDataReportVo);

		//分组sql
		String groupBySql = GenericOperatorsMtDataReportVoSQL
		.getAreaGroupBySql(operatorsAreaMtDataReportVo);
		
		//排序
		String orderbyAreaMonthY="";
		if(operatorsAreaMtDataReportVo != null && operatorsAreaMtDataReportVo.getReporttype()!=null){
			int reportType = operatorsAreaMtDataReportVo.getReporttype();
			if(reportType==0){
				orderbyAreaMonthY=" ORDER BY mtdatareport.IMONTH DESC";
			}else if(reportType==1){
				orderbyAreaMonthY=" ORDER BY mtdatareport.Y  DESC";
			}else if(reportType==2){
				orderbyAreaMonthY="";
			}
		}

		//总sql
		String sql =  new StringBuffer(fieldSql).append(tableSql).append(
				conditionSql).append(groupBySql).append(orderbyAreaMonthY).toString();
		//返回集合
		List<OperatorsAreaMtDataReportVo> returnList = findVoListBySQL(OperatorsAreaMtDataReportVo.class, sql, StaticValue.EMP_POOLNAME);
		return returnList;
	}
	
	
	
	/**
	 *运营商统计报表  各国详情  分页
	 * @param operatorsAreaMtDataReportVo
	 * @return
	 * @throws Exception
	 */
	
	public List<OperatorsAreaMtDataReportVo> findOperatorsMtDataReportVoByArea(
			OperatorsAreaMtDataReportVo operatorsAreaMtDataReportVo, PageInfo pageInfo)
			throws Exception {
		
		//查询字段
		String fieldSql = GenericOperatorsMtDataReportVoSQL.getAreaFieldSql();
		//查询表
		String tableSql = GenericOperatorsMtDataReportVoSQL
					.getAreaTableSql(operatorsAreaMtDataReportVo.getMstype());
		
		//查询条件
		String conditionSql = GenericOperatorsMtDataReportVoSQL
					.getAreaConditionSql(operatorsAreaMtDataReportVo);

		//分组sql
		String groupBySql = GenericOperatorsMtDataReportVoSQL
					.getAreaGroupBySql(operatorsAreaMtDataReportVo);
		
		//排序
		String orderbyAreaMonthY="";
		if(operatorsAreaMtDataReportVo!=null&&operatorsAreaMtDataReportVo.getReporttype()!=null){
			int reportType = operatorsAreaMtDataReportVo.getReporttype();
			if(reportType==0){
				orderbyAreaMonthY=" ORDER BY mtdatareport.IMONTH DESC";
			}else if(reportType==1){
				orderbyAreaMonthY=" ORDER BY mtdatareport.Y DESC";
			}else if(reportType==2){
				orderbyAreaMonthY="";
			}
		}

		//总sql
		String sql =  new StringBuffer(fieldSql).append(tableSql).append(
				conditionSql).append(groupBySql).append(orderbyAreaMonthY).toString();
		
		//分页sql
		String countSql = new StringBuffer("select count(*) totalcount from (")
				.append("select count(*) tcount").append(tableSql).append(conditionSql).append(groupBySql).append(") A")
				.toString();
		
		//查询结果集			  
		List<OperatorsAreaMtDataReportVo> returnList = new DataAccessDriver().getGenericDAO().findPageVoListBySQL(
				OperatorsAreaMtDataReportVo.class, sql, countSql, pageInfo,
				StaticValue.EMP_POOLNAME);
		return returnList;
	}
	
	
	

	
	
	/**
	 * 查询合计--各国详情
	 * @param operatorsAreaMtDataReportVo
	 * @return
	 * @throws Exception
	 */
	public long[] findSumCount(OperatorsAreaMtDataReportVo operatorsAreaMtDataReportVo) throws Exception {

		long icount = 0L;
		long rfail1 = 0L;
		long rfail2 = 0L;
		long rnret = 0L;
		long rsucc = 0L;

		//查询字段
		String fieldSql = new StringBuffer("select sum(").append(TableMtDatareport.ICOUNT).append(") ICOUNT")
				.append(",sum(").append(TableMtDatareport.RFAIL1).append(") RFAIL1")
				.append(",sum(").append(TableMtDatareport.RFAIL2).append(") RFAIL2")
				.append(",sum(").append(TableMtDatareport.RNRET).append(") RNRET")
				.append(",sum(").append(TableMtDatareport.RSUCC).append(") RSUCC").toString();
		//查询table
		String tableSql = GenericOperatorsMtDataReportVoSQL
			.getAreaTableSql(operatorsAreaMtDataReportVo.getMstype());

		//查询条件
		String conditionSql = GenericOperatorsMtDataReportVoSQL
			.getAreaConditionSql(operatorsAreaMtDataReportVo);
		
		String sql = new StringBuffer(fieldSql).append(tableSql).append(
				conditionSql).toString();
		Connection conn = null;
		PreparedStatement ps = null;
		java.sql.ResultSet rs = null;
		try {
			//获取连接
			conn = connectionManager.getDBConnection(StaticValue.EMP_POOLNAME);
			//获取操作对象
			ps = conn.prepareStatement(sql);
			//获取结果集
			rs = ps.executeQuery();
			//判断是否有值
			if (rs.next()) {
				icount = rs.getLong("ICOUNT");
				rfail1 = rs.getLong("RFAIL1");
				rfail2 = rs.getLong("RFAIL2");
				rnret = rs.getLong("RNRET");
				rsucc = rs.getLong("RSUCC");
			}
		} catch (Exception e) {
			EmpExecutionContext.error(e," 查询合计处理异常");
			throw e;
		} finally {
			//关闭连接
			close(rs, ps, conn);
		}
		long[] returnLong = new long[5];
		returnLong[0] = icount;
		returnLong[1] = rfail1;
		returnLong[2] = rfail2;
		returnLong[3] = rnret;
		returnLong[4] = rsucc;
		return returnLong;
	}
	
	

}
