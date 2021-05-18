package com.montnets.emp.report.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.beanutils.DynaBean;

import com.montnets.emp.common.constant.StaticValue;
import com.montnets.emp.common.context.EmpExecutionContext;
import com.montnets.emp.common.dao.SuperDAO;
import com.montnets.emp.common.dao.impl.DataAccessDriver;
import com.montnets.emp.report.vo.SpMtDataDetailVo;
import com.montnets.emp.report.vo.SpMtDataNationVo;
import com.montnets.emp.report.vo.SpMtDataReportVo;
import com.montnets.emp.report.vo.SpMtNationVo;
import com.montnets.emp.util.PageInfo;

/**
 * 发送账号下行报表
 * @project p_cxtj
 * @author liaojirong <ljr0300@163.com>
 * @company ShenZhen Montnets Technology CO.,LTD.
 * @datetime 2013-11-22 下午02:42:51
 * @description
 */
public class GenericSpMtDataReportVoDAO extends SuperDAO{

	/**
	 * 发送账号下行报表--月报表（不分页）
	 * @param operatorsMtDataReportVo
	 * @return
	 * @throws Exception
	 */
	public List<SpMtDataReportVo> findOperatorsMtDataReportVoByMonth(
			SpMtDataReportVo operatorsMtDataReportVo)
			throws Exception {
		//字段
		String fieldSql = GenericSpMtDataReportVoSQL.getMonthFieldSql();
		//表格
		String tableSql ="";
		if(operatorsMtDataReportVo.getMstype()!=null
				&&operatorsMtDataReportVo.getMstype()==0){
			//短信表头
			tableSql=GenericSpMtDataReportVoSQL.getMonthTableSql(operatorsMtDataReportVo);
		}else if(operatorsMtDataReportVo.getMstype()!=null
				&&operatorsMtDataReportVo.getMstype()==1){
			//彩信表头
			tableSql=GenericSpMtDataReportVoSQL.getMonthMMSTableSql(operatorsMtDataReportVo);
		}else{
			return null;
		}
		//分组
		String groupBySql = GenericSpMtDataReportVoSQL.getMonthGroupBySql();
		//排序
		String orderBySql = GenericSpMtDataReportVoSQL.getMonthOrderBySql();
		String sql = fieldSql + tableSql + groupBySql + orderBySql;

		return findVoListBySQL(
				SpMtDataReportVo.class, sql, StaticValue.EMP_POOLNAME);
	}

	/**
	 * 发送账号下行报表--月报表（不分页）国家
	 * @param operatorsMtDataReportVo
	 * @return
	 * @throws Exception
	 */
	public List<SpMtNationVo> findMtDataVoByMonthNation(
			SpMtDataReportVo operatorsMtDataReportVo)
			throws Exception {
		//字段
		String fieldSql = GenericSpMtDataReportVoSQL.getMonthFieldNationSql();
		//表格
		String tableSql ="";
		if(operatorsMtDataReportVo.getMstype()!=null
				&&operatorsMtDataReportVo.getMstype()==0){
			//短信表头
			tableSql=GenericSpMtDataReportVoSQL.getMonthTableNationSql(operatorsMtDataReportVo); //已经 xiugai
		}else if(operatorsMtDataReportVo.getMstype()!=null
				&&operatorsMtDataReportVo.getMstype()==1){
			//彩信表头
			tableSql=GenericSpMtDataReportVoSQL.getMonthMMSTableNationSql(operatorsMtDataReportVo);
		}else{
			return null;
		}

		//分组
		String groupBySql = GenericSpMtDataReportVoSQL.getMonthGroupNationBySql();
		//排序
		String orderBySql = GenericSpMtDataReportVoSQL.getMonthOrderBySql();
		String sql = new StringBuffer(fieldSql).append(tableSql)
//		.append(conditionSql)
		.append(groupBySql).append(orderBySql).toString();

		List<SpMtNationVo> returnList = findVoListBySQL(
				SpMtNationVo.class, sql, StaticValue.EMP_POOLNAME);
		return returnList;
	}
	
	
	
	
	/**
	 * 发送账号下行报表--月报表（分页）
	 * @param operatorsMtDataReportVo
	 * @param pageInfo
	 * @return
	 * @throws Exception
	 */
	public List<SpMtDataReportVo> findOperatorsMtDataReportVoByMonth(
			SpMtDataReportVo operatorsMtDataReportVo, PageInfo pageInfo) throws Exception {
		//字段
		String fieldSql = GenericSpMtDataReportVoSQL.getMonthFieldSql();

		//表格
		String tableSql ="";
		if(operatorsMtDataReportVo.getMstype()!=null
				&&operatorsMtDataReportVo.getMstype()==0){
			//短信表头
			tableSql=GenericSpMtDataReportVoSQL.getMonthTableSql(operatorsMtDataReportVo); //已经 xiugai
		}else if(operatorsMtDataReportVo.getMstype()!=null
				&&operatorsMtDataReportVo.getMstype()==1){
			//彩信表头
			tableSql=GenericSpMtDataReportVoSQL.getMonthMMSTableSql(operatorsMtDataReportVo);
		}else{
			return null;
		}

		//条件
//		String conditionSql = GenericSpMtDataReportVoSQL
//				.getMonthConditionSql(operatorsMtDataReportVo);
		//分组
		String groupBySql = GenericSpMtDataReportVoSQL
				.getMonthGroupBySql();

		
		//排序
		String orderBySql = GenericSpMtDataReportVoSQL
				.getMonthOrderBySql();

		String sql = new StringBuffer(fieldSql).append(tableSql)
		.append(groupBySql).append(" ").append(orderBySql).toString();

		String countSql = new StringBuffer("select count(*) totalcount from (")
				.append("select count(*) tcount").append(tableSql)
//				.append(conditionSql)
				.append(groupBySql).append(") A")
				.toString();
		List<SpMtDataReportVo> returnList = new DataAccessDriver().getGenericDAO().findPageVoListBySQL(
				SpMtDataReportVo.class, sql, countSql, pageInfo,
				StaticValue.EMP_POOLNAME);
		return returnList;
	}
	/**
	 * 发送账号下行报表--月报表（分页）国家
	 * @param operatorsMtDataReportVo
	 * @param pageInfo
	 * @return
	 * @throws Exception
	 */
	public List<SpMtNationVo> findMtDataByMonthNation(
			SpMtDataReportVo operatorsMtDataReportVo, PageInfo pageInfo) throws Exception {
		//字段
		String fieldSql = GenericSpMtDataReportVoSQL.getMonthFieldNationSql();

		//表格
		String tableSql ="";
		if(operatorsMtDataReportVo.getMstype()!=null
				&&operatorsMtDataReportVo.getMstype()==0){
			//短信表头
			tableSql=GenericSpMtDataReportVoSQL.getMonthTableNationSql(operatorsMtDataReportVo); //已经 xiugai
		}else if(operatorsMtDataReportVo.getMstype()!=null
				&&operatorsMtDataReportVo.getMstype()==1){
			//彩信表头
			tableSql=GenericSpMtDataReportVoSQL.getMonthMMSTableNationSql(operatorsMtDataReportVo);
		}else{
			return null;
		}
		//分组
		String groupBySql = GenericSpMtDataReportVoSQL
				.getMonthGroupNationBySql();
		//排序
		String orderBySql = GenericSpMtDataReportVoSQL
				.getMonthNationOrderBySql();

		String sql = new StringBuffer(fieldSql).append(tableSql)
		.append(groupBySql).append(" ").append(orderBySql).toString();

		String countSql = new StringBuffer("select count(*) totalcount from (")
				.append("select count(*) tcount").append(tableSql)
//				.append(conditionSql)
				.append(groupBySql).append(") A")
				.toString();
		List<SpMtNationVo> returnList = new DataAccessDriver().getGenericDAO().findPageVoListBySQL(
				SpMtNationVo.class, sql, countSql, pageInfo,
				StaticValue.EMP_POOLNAME);
		return returnList;
	}
	
	
	
	/**
	 * 发送账号下行报表--年报表（不分页）
	 * @param operatorsMtDataReportVo
	 * @return
	 * @throws Exception
	 */
	public List<SpMtDataReportVo> findOperatorsMtDataReportVoByYear(
			SpMtDataReportVo operatorsMtDataReportVo)
			throws Exception {

		//字段
		String fieldSql = GenericSpMtDataReportVoSQL.getYearFieldSql();

		//表格
		String tableSql ="";
		if(operatorsMtDataReportVo.getMstype()!=null
				&&operatorsMtDataReportVo.getMstype()==0){
			//短信表头
			tableSql=GenericSpMtDataReportVoSQL.getYearTableSql(operatorsMtDataReportVo); //已经 xiugai
		}else if(operatorsMtDataReportVo.getMstype()!=null
				&&operatorsMtDataReportVo.getMstype()==1){
			//彩信表头
			tableSql=GenericSpMtDataReportVoSQL.getYearMMSTableSql(operatorsMtDataReportVo);
		}else{
			return null;
		}

		//条件
//		String conditionSql = GenericSpMtDataReportVoSQL
//				.getYearConditionSql(operatorsMtDataReportVo);
		//分组
		String groupBySql = GenericSpMtDataReportVoSQL
				.getYearGroupBySql();

		//排序
		String orderBySql = GenericSpMtDataReportVoSQL
				.getYearOrderBySql();

		String sql = new StringBuffer(fieldSql).append(tableSql)
//		.append(conditionSql)
		.append(groupBySql).append(orderBySql).toString();

		List<SpMtDataReportVo> returnList = findVoListBySQL(
				SpMtDataReportVo.class, sql, StaticValue.EMP_POOLNAME);
		return returnList;
	}
/**
 * 发送账号下行报表--年报表（分页）
 * @param operatorsMtDataReportVo
 * @param pageInfo
 * @return
 * @throws Exception
 */
	public List<SpMtDataReportVo> findOperatorsMtDataReportVoByYear(SpMtDataReportVo operatorsMtDataReportVo, PageInfo pageInfo) throws Exception {
		//字段
		String fieldSql = GenericSpMtDataReportVoSQL.getYearFieldSql();

		//表格
		String tableSql ="";
		if(operatorsMtDataReportVo.getMstype()!=null
				&&operatorsMtDataReportVo.getMstype()==0){
			//短信表头
			tableSql=GenericSpMtDataReportVoSQL.getYearTableSql(operatorsMtDataReportVo); //已经 xiugai
		}else if(operatorsMtDataReportVo.getMstype()!=null
				&&operatorsMtDataReportVo.getMstype()==1){
			//彩信表头
			tableSql=GenericSpMtDataReportVoSQL.getYearMMSTableSql(operatorsMtDataReportVo);
		}else{
			return null;
		}

		//分组
		String groupBySql = GenericSpMtDataReportVoSQL
				.getYearGroupBySql();

		//排序
 	   String orderBySql = GenericSpMtDataReportVoSQL
 				.getYearOrderBySql();

		String sql = fieldSql + tableSql + groupBySql + " " + orderBySql;
		String countSql = "select count(*) totalcount from (" +
				"select count(*) tcount" + tableSql +
				groupBySql + ") A";

		return new DataAccessDriver().getGenericDAO().findPageVoListBySQL(
				SpMtDataReportVo.class, sql, countSql, pageInfo,
				StaticValue.EMP_POOLNAME);
	}
	/**
	 * 发送账号下行报表--日报表（不分页）
	 * @param operatorsMtDataReportVo
	 * @param pageInfo
	 * @return
	 * @throws Exception
	 */
		public List<SpMtDataReportVo> findOperatorsMtDataReportVoByDay( //日报表
				SpMtDataReportVo operatorsMtDataReportVo) throws Exception {
			//字段
			String fieldSql = GenericSpMtDataReportVoSQL.getDayFieldSql();

			//表格
			String tableSql ="";
			if(operatorsMtDataReportVo.getMstype()!=null
					&&operatorsMtDataReportVo.getMstype()==0){
				//短信表头
				tableSql=GenericSpMtDataReportVoSQL.getDayTableSql(operatorsMtDataReportVo); //已经 xiugai
			}else if(operatorsMtDataReportVo.getMstype()!=null
					&&operatorsMtDataReportVo.getMstype()==1){
				//彩信表头
				tableSql=GenericSpMtDataReportVoSQL.getDayMMSTableSql(operatorsMtDataReportVo);
			}else{
				return null;
			}

			//条件
//			String conditionSql = GenericSpMtDataReportVoSQL
//					.getYearConditionSql(operatorsMtDataReportVo);
			//分组
			String groupBySql = GenericSpMtDataReportVoSQL
					.getDayGroupBySql();

			//排序
	 	   String orderBySql = GenericSpMtDataReportVoSQL
	 				.getDayOrderBySql();

			String sql = new StringBuffer(fieldSql).append(tableSql)
//			.append(conditionSql)
			.append(groupBySql).append(" ").append(orderBySql).toString();
			String countSql = new StringBuffer("select count(*) totalcount from (")
					.append("select count(*) tcount").append(tableSql)
					.append(groupBySql).append(") A")
					.toString();

			List<SpMtDataReportVo> returnList = findVoListBySQL(
					SpMtDataReportVo.class, sql, StaticValue.EMP_POOLNAME);
			
			return returnList;
		}
	
	
	
	/**
	 * 发送账号下行报表--日报表（分页）
	 * @param operatorsMtDataReportVo
	 * @param pageInfo
	 * @return
	 * @throws Exception
	 */
		public List<SpMtDataReportVo> findOperatorsMtDataReportVoByDay( //日报表
				SpMtDataReportVo operatorsMtDataReportVo, PageInfo pageInfo) throws Exception {
			//字段
			String fieldSql = GenericSpMtDataReportVoSQL.getDayFieldSql();

			//表格
			String tableSql ="";
			if(operatorsMtDataReportVo.getMstype()!=null
					&&operatorsMtDataReportVo.getMstype()==0){
				//短信表头
				tableSql=GenericSpMtDataReportVoSQL.getDayTableSql(operatorsMtDataReportVo);
			}else if(operatorsMtDataReportVo.getMstype()!=null
					&&operatorsMtDataReportVo.getMstype()==1){
				//彩信表头
				tableSql=GenericSpMtDataReportVoSQL.getDayMMSTableSql(operatorsMtDataReportVo);
			}else{
				return null;
			}

			//分组
			String groupBySql = GenericSpMtDataReportVoSQL
					.getDayGroupBySql();

			//排序
	 	   String orderBySql = GenericSpMtDataReportVoSQL
	 				.getDayOrderBySql();

			String sql = fieldSql + tableSql + groupBySql + " " + orderBySql;
			String countSql = "select count(*) totalcount from (" + "select count(*) tcount" + tableSql + groupBySql + ") A";

			return new DataAccessDriver().getGenericDAO().findPageVoListBySQL(
					SpMtDataReportVo.class, sql, countSql, pageInfo,
					StaticValue.EMP_POOLNAME);
			
		}
	
	
		/**
		 * 发送账号下行报表--日报表（不分页）详细
		 * @param operatorsMtDataReportVo
		 * @param pageInfo
		 * @return
		 * @throws Exception
		 */
			public List<SpMtDataDetailVo> findMtDataReportDayDetailUnpage( //日报表
					SpMtDataReportVo operatorsMtDataReportVo) throws Exception {
				//字段
				String fieldSql = GenericSpMtDataReportVoSQL.getDayDetailFieldSql();

				//表格
				String tableSql ="";
				if(operatorsMtDataReportVo.getMstype()!=null
						&&operatorsMtDataReportVo.getMstype()==0){
					//短信表头
					tableSql=GenericSpMtDataReportVoSQL.getDayDetailTableSql(operatorsMtDataReportVo); //已经 xiugai
				}else if(operatorsMtDataReportVo.getMstype()!=null
						&&operatorsMtDataReportVo.getMstype()==1){
					//彩信表头
					tableSql=GenericSpMtDataReportVoSQL.getDayDetailMMSTableSql(operatorsMtDataReportVo);
				}else{
					return null;
				}

				//条件
//				String conditionSql = GenericSpMtDataReportVoSQL
//						.getYearConditionSql(operatorsMtDataReportVo);
				//分组
				String groupBySql = GenericSpMtDataReportVoSQL
						.getDayDetailGroupBySql();

				//排序
		 	   String orderBySql = GenericSpMtDataReportVoSQL
		 				.getDayPageOrderBySql();

				String sql = new StringBuffer(fieldSql).append(tableSql)
//				.append(conditionSql)
				.append(groupBySql).append(" ").append(orderBySql).toString();
//				String countSql = new StringBuffer("select count(*) totalcount from (")
//						.append("select count(*) tcount").append(tableSql)
//						.append(groupBySql).append(") A")
//						.toString();

				List<SpMtDataDetailVo> returnList = findVoListBySQL(
						SpMtDataDetailVo.class, sql, StaticValue.EMP_POOLNAME);
				
//				List<SpMtDataDetailVo> returnList =  new DataAccessDriver().getGenericDAO().findPageVoListBySQL(
//						SpMtDataDetailVo.class, sql, countSql, pageInfo,
//						StaticValue.EMP_POOLNAME);
				return returnList;
			}
			
			/**
			 * 发送账号下行报表--日报表（不分页）国家分类
			 * @param operatorsMtDataReportVo
			 * @param pageInfo
			 * @return
			 * @throws Exception
			 */
				public List<SpMtDataNationVo> findMtDataReportDayNationUnpage( //日报表
						SpMtDataReportVo operatorsMtDataReportVo) throws Exception {
					//字段
					String fieldSql = GenericSpMtDataReportVoSQL.getDayNationFieldSql();

					//表格
					String tableSql ="";
					if(operatorsMtDataReportVo.getMstype()!=null
							&&operatorsMtDataReportVo.getMstype()==0){
						//短信表头
						tableSql=GenericSpMtDataReportVoSQL.getDayNationTableSql(operatorsMtDataReportVo); //已经 xiugai
					}else if(operatorsMtDataReportVo.getMstype()!=null
							&&operatorsMtDataReportVo.getMstype()==1){
						//彩信表头
						tableSql=GenericSpMtDataReportVoSQL.getDayNationMMSTableSql(operatorsMtDataReportVo);
					}else{
						return null;
					}

					//条件
//					String conditionSql = GenericSpMtDataReportVoSQL
//							.getYearConditionSql(operatorsMtDataReportVo);
					//分组
					String groupBySql = GenericSpMtDataReportVoSQL
							.getDayNationGroupBySql();

					//排序
			 	   String orderBySql = GenericSpMtDataReportVoSQL
			 				.getDayOrderBySql();

					String sql = new StringBuffer(fieldSql).append(tableSql)
//					.append(conditionSql)
					.append(groupBySql).append(" ").append(orderBySql).toString();
//					String countSql = new StringBuffer("select count(*) totalcount from (")
//							.append("select count(*) tcount").append(tableSql)
//							.append(groupBySql).append(") A")
//							.toString();

					List<SpMtDataNationVo> returnList = findVoListBySQL(
							SpMtDataNationVo.class, sql, StaticValue.EMP_POOLNAME);
					
					return returnList;
				}
			
			
		
		/**
		 * 发送账号下行报表--日报表（分页）详细
		 * @param operatorsMtDataReportVo
		 * @param pageInfo
		 * @return
		 * @throws Exception
		 */
			public List<SpMtDataDetailVo> findMtDataReportDayDetail( //日报表
					SpMtDataReportVo operatorsMtDataReportVo, PageInfo pageInfo) throws Exception {
				//字段
				String fieldSql = GenericSpMtDataReportVoSQL.getDayDetailFieldSql();

				//表格
				String tableSql ="";
				if(operatorsMtDataReportVo.getMstype()!=null
						&&operatorsMtDataReportVo.getMstype()==0){
					//短信表头
					tableSql=GenericSpMtDataReportVoSQL.getDayDetailTableSql(operatorsMtDataReportVo); //已经 xiugai
				}else if(operatorsMtDataReportVo.getMstype()!=null
						&&operatorsMtDataReportVo.getMstype()==1){
					//彩信表头
					tableSql=GenericSpMtDataReportVoSQL.getDayDetailMMSTableSql(operatorsMtDataReportVo);
				}else{
					return null;
				}

				//条件
//				String conditionSql = GenericSpMtDataReportVoSQL
//						.getYearConditionSql(operatorsMtDataReportVo);
				//分组
				String groupBySql = GenericSpMtDataReportVoSQL
						.getDayDetailGroupBySql();

				//排序
		 	   String orderBySql = " ORDER BY mtdatareport.IYMD DESC ";//GenericSpMtDataReportVoSQL.getDayOrderBySql();

				String sql = new StringBuffer(fieldSql).append(tableSql)
//				.append(conditionSql)
				.append(groupBySql).append(" ").append(orderBySql).toString();
				String countSql = new StringBuffer("select count(*) totalcount from (")
						.append("select count(*) tcount").append(tableSql)
						.append(groupBySql).append(") A")
						.toString();

				List<SpMtDataDetailVo> returnList =  new DataAccessDriver().getGenericDAO().findPageVoListBySQL(
						SpMtDataDetailVo.class, sql, countSql, pageInfo,
						StaticValue.EMP_POOLNAME);
				return returnList;
			}
	
			/**
			 * 发送账号下行报表--日报表（分页）国家分类
			 * @param operatorsMtDataReportVo
			 * @param pageInfo
			 * @return
			 * @throws Exception
			 */
				public List<SpMtDataNationVo> findMtDataReportDayNation( //日报表
						SpMtDataReportVo operatorsMtDataReportVo, PageInfo pageInfo) throws Exception {
					//字段
					String fieldSql = GenericSpMtDataReportVoSQL.getDayNationFieldSql();

					//表格
					String tableSql ="";
					if(operatorsMtDataReportVo.getMstype()!=null
							&&operatorsMtDataReportVo.getMstype()==0){
						//短信表头
						tableSql=GenericSpMtDataReportVoSQL.getDayNationTableSql(operatorsMtDataReportVo); //已经 xiugai
					}else if(operatorsMtDataReportVo.getMstype()!=null
							&&operatorsMtDataReportVo.getMstype()==1){
						//彩信表头
						tableSql=GenericSpMtDataReportVoSQL.getDayNationMMSTableSql(operatorsMtDataReportVo);
					}else{
						return null;
					}
					//分组
					String groupBySql = GenericSpMtDataReportVoSQL
							.getDayNationGroupBySql();

					//排序
			 	   String orderBySql = GenericSpMtDataReportVoSQL
			 				.getDayOrderBySql();

					String sql = new StringBuffer(fieldSql).append(tableSql).append(groupBySql).append(" ").append(orderBySql).toString();
					String countSql = new StringBuffer("select count(*) totalcount from (")
							.append("select count(*) tcount").append(tableSql)
							.append(groupBySql).append(") A")
							.toString();

					List<SpMtDataNationVo> returnList =  new DataAccessDriver().getGenericDAO().findPageVoListBySQL(
							SpMtDataNationVo.class, sql, countSql, pageInfo,
							StaticValue.EMP_POOLNAME);
					return returnList;
				}
	
	/**
	 * 发送账号下行报表--月/年报表（共用合计）
	 * @param operatorsMtDataReportVo
	 * @return
	 * @throws Exception
	 */
	public long[] findSumCount(SpMtDataReportVo operatorsMtDataReportVo) throws Exception {
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
		//获取字段
		String fieldSql = new StringBuffer("select "+ReportDAO.getPublicCountSql("")+" ").toString();
		//表格
		String tableSql ="";
		if(operatorsMtDataReportVo.getMstype()!=null
				&&operatorsMtDataReportVo.getMstype()==0){
			//短信表头
			tableSql=GenericSpMtDataReportVoSQL.getMonthTableSql(operatorsMtDataReportVo); //已经 xiugai
		}else if(operatorsMtDataReportVo.getMstype()!=null
				&&operatorsMtDataReportVo.getMstype()==1){
			//彩信表头
			tableSql=GenericSpMtDataReportVoSQL.getMonthMMSTableSql(operatorsMtDataReportVo);
		}else{
			return null;
		}

		//查询条件
//		String conditionSql = GenericSpMtDataReportVoSQL
//				.getMonthConditionSql(operatorsMtDataReportVo);
		//总sql
		String sql = new StringBuffer(fieldSql).append(tableSql)
//		.append(conditionSql)
		.toString();
		
		//定义连接
		Connection conn = null;
		PreparedStatement ps = null;
		java.sql.ResultSet rs = null;
		try {
			//获取连接
			conn = connectionManager.getDBConnection(StaticValue.EMP_POOLNAME);
			//获取操作类
			ps = conn.prepareStatement(sql);
			//获取结果集
			rs = ps.executeQuery();
			//遍历
			if (rs.next()) {
				icount = rs.getLong("ICOUNT");
				rsucc = rs.getLong("RSUCC");
				rfail1 = rs.getLong("RFAIL1");
				rfail2 = rs.getLong("RFAIL2");
				rnret = rs.getLong("RNRET");
			}
		} catch (Exception e) {
			EmpExecutionContext.error(e,"发送账号下行报表--月/年报表（共用合计）异常");
			throw e;
		} finally {
			//关闭连接
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
	 * 发送账号下行报表--月/年报表（共用合计）---增加国家相关条件
	 * @param operatorsMtDataReportVo
	 * @return
	 * @throws Exception
	 */
	public long[] findSumCountNation(SpMtDataReportVo operatorsMtDataReportVo) throws Exception {
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

		//获取字段
		String fieldSql = new StringBuffer("select "+ReportDAO.getPublicCountSql("")+" ").toString();

		//表格
		String tableSql ="";
		if(operatorsMtDataReportVo.getMstype()!=null
				&&operatorsMtDataReportVo.getMstype()==0){
			//短信表头
			tableSql=GenericSpMtDataReportVoSQL.getMonthTableNationSql(operatorsMtDataReportVo); //已经 xiugai
		}else if(operatorsMtDataReportVo.getMstype()!=null
				&&operatorsMtDataReportVo.getMstype()==1){
			//彩信表头
			tableSql=GenericSpMtDataReportVoSQL.getMonthMMSTableNationSql(operatorsMtDataReportVo);
		}else{
			return null;
		}

		//查询条件
//		String conditionSql = GenericSpMtDataReportVoSQL
//				.getMonthConditionSql(operatorsMtDataReportVo);
		//总sql
		String sql = new StringBuffer(fieldSql).append(tableSql)
//		.append(conditionSql)
		.toString();
		
		//定义连接
		Connection conn = null;
		PreparedStatement ps = null;
		java.sql.ResultSet rs = null;
		try {
			//获取连接
			conn = connectionManager.getDBConnection(StaticValue.EMP_POOLNAME);
			//获取操作类
			ps = conn.prepareStatement(sql);
			//获取结果集
			rs = ps.executeQuery();
			//遍历
			if (rs.next()) {
				icount = rs.getLong("ICOUNT");
				rsucc = rs.getLong("RSUCC");
				rfail1 = rs.getLong("RFAIL1");
				rfail2 = rs.getLong("RFAIL2");
				rnret = rs.getLong("RNRET");
			}
		} catch (Exception e) {
			EmpExecutionContext.error(e,"发送账号下行报表--月/年报表（共用合计）异常");
			throw e;
		} finally {
			//关闭连接
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

	public List<String> findUserCodeByIds(String userIdStr) {
		List<String> list = new ArrayList<String>();
		String sql = "SELECT USER_CODE from lf_sysuser WHERE USER_ID in ("+ userIdStr +")";
		List<DynaBean> dynaBeans = new DataAccessDriver().getGenericDAO().findDynaBeanBySql(sql);
		for(Object o: dynaBeans){
			DynaBean bean = (DynaBean) o;
			list.add(bean.get("user_code").toString());
		}
		return list;
	}

	public List<String> findUserCodeBydepIds(String depIdStr, String corpCode ,boolean containSubDep) {
		String sql;
		List<String> list = new ArrayList<String>();
		if(containSubDep){
			//包含子机构
			sql = "SELECT USER_CODE from lf_sysuser WHERE DEP_ID in(select DEP_ID from lf_dep WHERE DEP_LEVEL >= (select DEP_LEVEL from lf_dep where DEP_ID = "+ depIdStr +") and CORP_CODE = "+ corpCode +")";
		}else {
			sql = "SELECT USER_CODE from lf_sysuser WHERE DEP_ID = " + depIdStr + " and CORP_CODE = " + corpCode;
		}
		List<DynaBean> dynaBeans = new DataAccessDriver().getGenericDAO().findDynaBeanBySql(sql);
		for(Object o: dynaBeans){
			DynaBean bean = (DynaBean) o;
			list.add(bean.get("user_code").toString());
		}
		return list;
	}
}
