package com.montnets.emp.rms.report.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.util.List;

import com.montnets.emp.common.constant.StaticValue;
import com.montnets.emp.common.context.EmpExecutionContext;
import com.montnets.emp.common.dao.SuperDAO;
import com.montnets.emp.common.dao.impl.DataAccessDriver;
import com.montnets.emp.rms.report.biz.RltcReportBiz;
import com.montnets.emp.rms.report.table.TableLfRmsReport;
import com.montnets.emp.rms.report.vo.LfRmsReportVo;
import com.montnets.emp.util.PageInfo;

/**
 * 档位统计报表
 * @project p_ydcx
 * @author lvxin 
 * @company ShenZhen Montnets Technology CO.,LTD.
 * @datetime 2018-1-12
 * @description
 */
public class GenericOperatorsMtDataReportVoDAO extends SuperDAO{

	/**
	 * 档位统计报表 年月 无分页
	 * @param operatorsMtDataReportVo
	 * @return
	 * @throws Exception
	 */
	public List<LfRmsReportVo> findOperatorsMtDataReportVoByMonth(
			LfRmsReportVo operatorsMtDataReportVo)
			throws Exception {

		//获取查询字段
		String fieldSql = GenericOperatorsMtDataReportVoSQL.getMonthFieldSql();

		//获取查询表
		String tableSql = GenericOperatorsMtDataReportVoSQL
				.getMonthTableSql();

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
		List<LfRmsReportVo> returnList = findVoListBySQL(
				LfRmsReportVo.class, sql, StaticValue.EMP_POOLNAME);
		
		return returnList;
	}

	/**
	 * 档位统计报表   月报表 分页
	 * @param operatorsMtDataReportVo
	 * @param pageInfo
	 * @return
	 * @throws Exception
	 */
	public List<LfRmsReportVo> findOperatorsMtDataReportVoByMonth(
			LfRmsReportVo operatorsMtDataReportVo, PageInfo pageInfo) throws Exception {
		//查询字段
		String fieldSql = GenericOperatorsMtDataReportVoSQL.getMonthFieldSql();

		//查询表
		String tableSql = GenericOperatorsMtDataReportVoSQL
				.getMonthTableSql();

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
		List<LfRmsReportVo> returnList = new DataAccessDriver().getGenericDAO().findPageVoListBySQL(
				LfRmsReportVo.class, sql, countSql, pageInfo,
				StaticValue.EMP_POOLNAME);
		return returnList;
	}

	/**
	 *档位统计报表 年报表  无分页
	 * @param operatorsMtDataReportVo
	 * @return
	 * @throws Exception
	 */
	public List<LfRmsReportVo> findOperatorsMtDataReportVoByYear(
			LfRmsReportVo operatorsMtDataReportVo)
			throws Exception {

		//查询字段
		String fieldSql = GenericOperatorsMtDataReportVoSQL.getYearFieldSql();

		//查询表
		String tableSql = GenericOperatorsMtDataReportVoSQL
				.getYearTableSql();

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

		List<LfRmsReportVo> returnList = findVoListBySQL(
				LfRmsReportVo.class, sql, StaticValue.EMP_POOLNAME);
		
		return returnList;
	}

	/**
	 * 档位报表 年报表 分页
	 * @param operatorsMtDataReportVo
	 * @param pageInfo
	 * @return
	 * @throws Exception
	 */
	public List<LfRmsReportVo> findOperatorsMtDataReportVoByYear(
			LfRmsReportVo operatorsMtDataReportVo, PageInfo pageInfo) throws Exception {
		
		//查询字段
		String fieldSql = GenericOperatorsMtDataReportVoSQL.getYearFieldSql();

		//查询表
		String tableSql = GenericOperatorsMtDataReportVoSQL
				.getYearTableSql();
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
		
		List<LfRmsReportVo> returnList = new DataAccessDriver().getGenericDAO().findPageVoListBySQL(
				LfRmsReportVo.class, sql, countSql, pageInfo,
				StaticValue.EMP_POOLNAME);
		
		return returnList;
	}

	/**
	 *档位统计报表 日报表  无分页
	 * @param operatorsMtDataReportVo
	 * @return
	 * @throws Exception
	 */
	public List<LfRmsReportVo> findOperatorsMtDataReportVoByDays(
			LfRmsReportVo operatorsMtDataReportVo)
			throws Exception {

		//查询字段
		String fieldSql = GenericOperatorsMtDataReportVoSQL.getDaysFieldSql();

		//查询表
		String tableSql = GenericOperatorsMtDataReportVoSQL
				.getDaysTableSql();
		
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
				.getDaysOrderBySql(operatorsMtDataReportVo);

		//总sql
		String sql = new StringBuffer(fieldSql).append(tableSql).append(
				conditionSql).append(groupBySql).append(orderBySql).toString();
		

		List<LfRmsReportVo> returnList = findVoListBySQL(
				LfRmsReportVo.class, sql, StaticValue.EMP_POOLNAME);
		return returnList;
	}

	/**
	 * 档位报表 日报表 分页
	 * @param operatorsMtDataReportVo
	 * @param pageInfo
	 * @return
	 * @throws Exception
	 */
	public List<LfRmsReportVo> findOperatorsMtDataReportVoByDays(
			LfRmsReportVo operatorsMtDataReportVo, PageInfo pageInfo) throws Exception {
		
		//查询字段
		String fieldSql = GenericOperatorsMtDataReportVoSQL.getDaysFieldSql();

		//查询表
		String tableSql = GenericOperatorsMtDataReportVoSQL
				.getDaysTableSql();
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
				.getDaysOrderBySql(operatorsMtDataReportVo);

		//总sql
		String sql = new StringBuffer(fieldSql).append(tableSql).append(
					conditionSql).append(groupBySql).append(orderBySql).toString();
		//分页
		String countSql = new StringBuffer("select count(*) totalcount from (")
				.append("select count(*) tcount").append(tableSql).append(
						conditionSql).append(groupBySql).append(") A")
				.toString();
		
		List<LfRmsReportVo> returnList = new DataAccessDriver().getGenericDAO().findPageVoListBySQL(
				LfRmsReportVo.class, sql, countSql, pageInfo,
				StaticValue.EMP_POOLNAME);
		
		return returnList;
	}

	
	
	
	/**
	 * 查询合计
	 * @param operatorsMtDataReportVo
	 * @return
	 * @throws Exception
	 */
	public long[] findSumCount(LfRmsReportVo operatorsMtDataReportVo) throws Exception {
		long icount = 0L;
		long rfail = 0L;
		long rsucc = 0L;

		//查询字段
		String fieldSql = new StringBuffer("select sum(").append(TableLfRmsReport.ICOUNT).append(") ICOUNT")
				.append(",sum(").append(TableLfRmsReport.RFAIL).append(") RFAIL")
				.append(",sum(").append(TableLfRmsReport.RSUCC).append(")  RSUCC").toString();

		
		
		//查询table
		String tableSql = "";

		//查询条件
		String conditionSql = "";
		
		Integer type=operatorsMtDataReportVo.getReporttype();
		//月报表
		if(type==RltcReportBiz.MONTH_REPORT){
			tableSql = GenericOperatorsMtDataReportVoSQL
			.getMonthTableSql();
			
			conditionSql = GenericOperatorsMtDataReportVoSQL
			.getMonthConditionSql(operatorsMtDataReportVo);
			// 处理SQL条件,不允许使用1 =1方式
			conditionSql =new ReciveBoxDao().getConditionSql(conditionSql);
		}
		//年报表
		else if(type==RltcReportBiz.YEAR_REPORT)
		{
			tableSql = GenericOperatorsMtDataReportVoSQL
			.getYearTableSql();
			
			conditionSql = GenericOperatorsMtDataReportVoSQL
			.getYearConditionSql(operatorsMtDataReportVo);
			// 处理SQL条件,不允许使用1 =1方式
			conditionSql =new ReciveBoxDao().getConditionSql(conditionSql);
		}
		//日报表
		else 
		{
			tableSql = GenericOperatorsMtDataReportVoSQL
					.getDaysTableSql();
			
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
			if(rs.next()) {
				icount = rs.getLong("ICOUNT");
				rfail = rs.getLong("RFAIL");
				rsucc = rs.getLong("RSUCC");
				
			}
		} catch (Exception e) {
			EmpExecutionContext.error(e," 查询合计处理异常");
			throw e;
		} finally {
			//关闭连接
			close(rs, ps, conn);
		}
		long[] returnLong = new long[3];
		returnLong[0] = icount;
		returnLong[1] = rsucc;
		returnLong[2] = rfail;
		return returnLong;
	}
	
	

	
	
	
	
	

}
