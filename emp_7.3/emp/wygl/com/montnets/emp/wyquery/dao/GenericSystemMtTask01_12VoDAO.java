package com.montnets.emp.wyquery.dao;

import java.util.Calendar;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;

import org.apache.commons.beanutils.DynaBean;

import com.montnets.emp.common.biz.CommonBiz;
import com.montnets.emp.common.constant.StaticValue;
import com.montnets.emp.common.context.EmpExecutionContext;
import com.montnets.emp.common.dao.SuperDAO;
import com.montnets.emp.common.dao.impl.DataAccessDriver;
import com.montnets.emp.table.sms.TableMtTask01_12;
import com.montnets.emp.util.PageInfo;
import com.montnets.emp.wyquery.vo.SystemMtTask01_12Vo;
/**
 * 历史记录查询
 * @project emp
 * @author 
 * @company ShenZhen Montnets Technology CO.,LTD.
 * @datetime 2011-5-5 下午03:51:48
 * @description
 */
public class GenericSystemMtTask01_12VoDAO extends SuperDAO
{

	/**
	 * 不带分条件的系统下行历史记录查询
	 * @param systemMtTask01_12Vo
	 * @return
	 * @throws Exception
	 */
	public List<DynaBean> findSystemMtTask01_12Vo(
			SystemMtTask01_12Vo systemMtTask01_12Vo) throws Exception
	{
		//获取查询的表名
		String tableName = getMttaskTableNameByTime(systemMtTask01_12Vo);
		if(tableName==null || "".equals(tableName))
		{
			return null;
		}
		//获取查询的列名
		String fieldSql = GenericSystemMtTask01_12VoSQL.getFieldSql(tableName);
		//获取查询表名的sql语句
		String tableSql = GenericSystemMtTask01_12VoSQL.getTableSql(tableName);
		//组装过滤条件语句
		String inter="";
		String conditionSql = GenericSystemMtTask01_12VoSQL
				.getConditionSql(systemMtTask01_12Vo,inter);
		conditionSql = conditionSql.replaceFirst("^(\\s*)(?i)and", "$1where");
		//时间段
		List<String> timeList = GenericSystemMtTask01_12VoSQL
				.getTimeCondition(systemMtTask01_12Vo);
		//排序
		String orderbySql = GenericSystemMtTask01_12VoSQL.getOrderBySql();
		//组装sql语句
		String sql = new StringBuffer(fieldSql).append(tableSql).append(
				conditionSql).append(orderbySql).toString();
		
		EmpExecutionContext.sql("execute sql："+sql);
		/**
		 * 读取配置文件里是否启用备用服务器
		 */
		ResourceBundle rb = ResourceBundle.getBundle("SystemGlobals");
		int backup = Integer.parseInt(rb.getString("montnets.emp.use_backup_server"));
		List<DynaBean> returnList = null;
		//启用备用服务器
		if(1==backup){
			try {
				//用备用服务器查询获取数据
				returnList = this.getListDynaBeanBySql(sql);
//						SystemMtTask01_12Vo.class, sql, StaticValue.EMP_BACKUP,
//						timeList);
			} catch (Exception e) {
				//如果备用服务器连接出错，则从主服务器查询获取数据
				EmpExecutionContext.error(e,"下行实时记录备用服务器连接异常");
			}
		}else{
			//不启用备用服务器的情况
			returnList = this.getListDynaBeanBySql(sql);
//			returnList = findVoListBySQL(
//					SystemMtTask01_12Vo.class, sql, StaticValue.EMP_POOLNAME,
//					timeList);
		}
		return returnList;
	}

	/**
	 * 下行记录历史查询
	 * @param systemMtTask01_12Vo
	 * @param pageInfo
	 * @return
	 * @throws Exception
	 */
	public List<DynaBean> findSystemMtTask01_12Vo(
			SystemMtTask01_12Vo systemMtTask01_12Vo, PageInfo pageInfo)
			throws Exception
	{
		//获取查询的表名
		String tableName = getMttaskTableNameByTime(systemMtTask01_12Vo);
		if(tableName==null || "".equals(tableName))
		{
			return null;
		}
		//获取查询的列名
		String fieldSql = GenericSystemMtTask01_12VoSQL.getFieldSql(tableName);
		//获取查询表名的sql语句
		String tableSql = GenericSystemMtTask01_12VoSQL.getTableSql(tableName);
		//组装过滤条件语句
		String inter="";
		String conditionSql = GenericSystemMtTask01_12VoSQL
				.getConditionSql(systemMtTask01_12Vo,inter);
		conditionSql = conditionSql.replaceFirst("^(\\s*)(?i)and", "$1where");
		//时间段的过滤条件
		List<String> timeList = GenericSystemMtTask01_12VoSQL
				.getTimeCondition(systemMtTask01_12Vo);
		String orderbySql = "";//GenericSystemMtTask01_12VoSQL.getOrderBySql();
		//组sql语句
		String sql = new StringBuffer(fieldSql).append(tableSql).append(
				conditionSql).append(orderbySql).toString();
		//组统计总条数sql语句
		String countSql = new StringBuffer("select count(*) totalcount").append(tableSql).append(conditionSql).toString();
       
		EmpExecutionContext.sql("execute sql："+sql);
		/**
		 * 读取配置文件里是否启用备用服务器
		 */
		ResourceBundle rb = ResourceBundle.getBundle("SystemGlobals");
		int backup = Integer.parseInt(rb.getString("montnets.emp.use_backup_server"));
		List<DynaBean> returnList = null;
		//启用备用服务器
		if(1==backup){
			try {
				//用备用服务器查询获取数据
				returnList = new DataAccessDriver().getGenericDAO().findPageDynaBeanBySQL(sql, countSql, pageInfo, StaticValue.EMP_BACKUP, timeList);
			} catch (Exception e) {
				//如果备用服务器连接出错，则主服务器查询获取数据
				returnList = new DataAccessDriver().getGenericDAO().findPageDynaBeanBySQL(sql, countSql, pageInfo, StaticValue.EMP_POOLNAME, timeList);
				EmpExecutionContext.error(e,"下行历史记录查询备用服务器连接异常");
			}
			
		}else{
			//不启用备用服务器的情况
			 returnList = new DataAccessDriver().getGenericDAO().findPageDynaBeanBySQL(
					 sql, countSql, pageInfo, StaticValue.EMP_POOLNAME, null);
		}
		
		return returnList;
	}
	
	public PageInfo getPageInfo(int preIndex, int pageSize,
			SystemMtTask01_12Vo systemMtTask01_12Vo) throws Exception
	{
		PageInfo pageInfo = new PageInfo();
		int totalCount = 0;
		
		String tableName = getMttaskTableNameByTime(systemMtTask01_12Vo);
		
		String fieldSql = "select count(*) totalcount";
		
		String tableSql = GenericSystemMtTask01_12VoSQL.getTableSql(tableName);
		String conditionSql = GenericSystemMtTask01_12VoSQL
				.getConditionSql(systemMtTask01_12Vo,"");
		conditionSql = conditionSql.replaceFirst("^(\\s*)(?i)and", "$1where");
		List<String> timeList = GenericSystemMtTask01_12VoSQL
				.getTimeCondition(systemMtTask01_12Vo);
		String countSql = new StringBuffer(fieldSql).append(tableSql).append(
				conditionSql).toString();
		
		totalCount = findCountBySQL(countSql, timeList);
		int totalPage = totalCount % pageSize == 0 ? totalCount / pageSize
				: totalCount / pageSize + 1;
		pageInfo.setTotalRec(totalCount);
		pageInfo.setTotalPage(totalPage);
		pageInfo.setPageSize(pageSize);
		
		pageInfo.setPageIndex(preIndex);
		return pageInfo;
	}
    /**
     * 获取系统下行历史记录
     * @param systemMtTask01_12Vo  带条件的查询对象
     * @return
     * @throws Exception 
     */
	protected String getMttaskTableNameByTime(
			SystemMtTask01_12Vo systemMtTask01_12Vo) throws Exception
	{
		String startTime = systemMtTask01_12Vo.getStartTime();
		String endTime = systemMtTask01_12Vo.getEndTime();
		String tableName = "";
		Map<String, String> map = TableMtTask01_12.getORM();
		//如果页面上没有输入查询的时间段
		if ((startTime == null || "".equals(startTime)) && (endTime != null
				&& !"".equals(endTime)))
		{
			systemMtTask01_12Vo.setStartTime(endTime.substring(0,7) + "-01 00:00:00");
			tableName = "MTTASK" + endTime.substring(0,7).replace("-", "");
			tableName = new CommonBiz().getTableName(tableName);
		}
		else  if((startTime != null && !"".equals(startTime)) && (endTime == null
				||"".equals(endTime)))
		{
			String startmonthstr = startTime.substring(5, 7);
			int month=Integer.parseInt(startmonthstr)+1;
			if(month<10)
			{
				startmonthstr="0"+month;
			}
			else
			{
				startmonthstr=String.valueOf(month);
			}
			systemMtTask01_12Vo.setEndTime(startTime.substring(0,4) + "-" + startmonthstr
					+ "-01 00:00:00");
			tableName = "MTTASK" + startTime.substring(0,7).replace("-", "");
			tableName = new CommonBiz().getTableName(tableName);
		}
		else if((startTime != null && !"".equals(startTime)) && (endTime != null
				&& !"".equals(endTime)))
		{
			//获取开始月，结束月
			String startmonthstr = startTime.substring(5, 7);
			String endmonthstr = endTime.substring(5, 7);
			//如果开始月与结束月是在同一个月份内，则只查询一张表
			if (startmonthstr.equals(endmonthstr))
			{
				tableName = "MTTASK" + startTime.substring(0,7).replace("-", "");
				tableName = new CommonBiz().getTableName(tableName);
			} else
			{
				//如果结束时间是-01 00:00:00这种的，就可以只查一个表
				if("-01 00:00:00".equals(endTime.substring(7)))
				{
					tableName = "MTTASK" + startTime.substring(0,7).replace("-", "");
					tableName = new CommonBiz().getTableName(tableName);
				}
				else
				{
					//否则则需要查询两张表的记录
					String startTableName = "MTTASK" + startTime.substring(0,7).replace("-", "");
					startTableName = new CommonBiz().getTableName(startTableName);
					
					String endTableName = "MTTASK" + endTime.substring(0,7).replace("-", "");
					endTableName = new CommonBiz().getTableName(endTableName);
					
					if(!"".equals(startTableName)&& !"".equals(endTableName))
					{
						tableName = new StringBuffer("(select * from ").append(
								startTableName).append(" union all select * from ")
								.append(endTableName).append(")").toString();
					}
					else if(!"".equals(startTableName))
					{
						tableName = startTableName;
					}
					else if(!"".equals(endTableName))
					{
						tableName = endTableName;
					}
					else
					{
						return "";
					}
				}
			}
		}
		else
		{
			//获取当前时间
			Calendar calendar = Calendar.getInstance();
			//获取当前年，月
			int year = calendar.get(Calendar.YEAR);
			int startmonth = calendar.get(Calendar.MONTH) + 1;
			//这里结束年月，即下一个月
			calendar.add(Calendar.MONTH, 1);
			int endyear = calendar.get(Calendar.YEAR);
			int endmonth =calendar.get(Calendar.MONTH)+1;	
			//处理小于10的月份
			String startmonthstr = startmonth > 9 ? String.valueOf(startmonth)
					: "0" + String.valueOf(startmonth);
			String endmonthstr = endmonth > 9 ? String.valueOf(endmonth) : "0"
				+ String.valueOf(endmonth);
            //设置时间的过滤条件
			systemMtTask01_12Vo.setStartTime(year + "-" + startmonthstr
					+ "-01 00:00:00");
			systemMtTask01_12Vo.setEndTime(endyear + "-" + endmonthstr
					+ "-01 00:00:00");
			//设置查询的表名
			tableName = "MTTASK" +year+ startmonthstr;
			tableName = new CommonBiz().getTableName(tableName);
		}
		if("".equals(tableName))
		{
			return "";
		}
		return tableName;
	}
}
