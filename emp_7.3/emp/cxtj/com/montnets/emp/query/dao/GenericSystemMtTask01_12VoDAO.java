package com.montnets.emp.query.dao;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.ResourceBundle;

import com.montnets.emp.common.biz.CommonBiz;
import com.montnets.emp.common.constant.StaticValue;
import com.montnets.emp.common.constant.SystemGlobals;
import com.montnets.emp.common.context.EmpExecutionContext;
import com.montnets.emp.common.dao.SuperDAO;
import com.montnets.emp.query.vo.SystemMtTask01_12Vo;
import com.montnets.emp.util.PageInfo;

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
	 * 查询日期格式化：yyyy-MM-dd HH:mm:ss
	 */
	private  SimpleDateFormat sdfSeachTime = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	
	/**
	 * 查询日期格式化：MM
	 */
	private  SimpleDateFormat sdfSeachDate = new SimpleDateFormat("yyyyMM");

	/**
	 * 不带分条件的系统下行历史记录查询
	 * @param systemMtTask01_12Vo
	 * @return
	 * @throws Exception
	 */
	public List<SystemMtTask01_12Vo> findSystemMtTask01_12Vo(
			SystemMtTask01_12Vo systemMtTask01_12Vo) throws Exception
	{
		//获取查询的表名
		String tableName = getMttaskTableNameByTime(systemMtTask01_12Vo);
		if(tableName==null || "".equals(tableName))
		{
			return null;
		}
		//获取查询的列名
		String fieldSql = GenericSystemMtTask01_12VoSQL.getFieldSql();
		//获取查询表名的sql语句
		String tableSql = GenericSystemMtTask01_12VoSQL.getTableSql(tableName);
		//组装过滤条件语句
		String conditionSql = GenericSystemMtTask01_12VoSQL
				.getConditionSql(systemMtTask01_12Vo);
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
		List<SystemMtTask01_12Vo> returnList = new ArrayList<SystemMtTask01_12Vo>();
		//启用备用服务器
		if(1==backup){
			try {
				//用备用服务器查询获取数据
				returnList = findVoListBySQL(
						SystemMtTask01_12Vo.class, sql, StaticValue.EMP_BACKUP,
						timeList);
			} catch (Exception e) {
				//如果备用服务器连接出错，则从主服务器查询获取数据
				returnList = findVoListBySQL(
						SystemMtTask01_12Vo.class, sql, StaticValue.EMP_POOLNAME,
						timeList);
				EmpExecutionContext.error(e,"下行实时记录备用服务器连接异常");
			}
		}else{
			//不启用备用服务器的情况
			returnList = findVoListBySQL(
					SystemMtTask01_12Vo.class, sql, StaticValue.EMP_POOLNAME,
					timeList);
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
	public List<SystemMtTask01_12Vo> findSystemMtTask01_12Vo(SystemMtTask01_12Vo systemMtTask01_12Vo, PageInfo pageInfo) throws Exception
	{
		//获取查询的表名
		String tableName = getMttaskTableNameByTime(systemMtTask01_12Vo);
		if(tableName == null || tableName.length() == 0)
		{
			return null;
		}
		//获取查询的列名
		//String fieldSql = GenericSystemMtTask01_12VoSQL.getFieldSql();
		String fieldSql = "select mttaskxx.ID,mttaskxx.USERID,mttaskxx.SPGATE,mttaskxx.CPNO,mttaskxx.PHONE,mttaskxx.SENDSTATUS,mttaskxx.TASKID,mttaskxx.ERRORCODE,mttaskxx.SENDTIME,mttaskxx.MESSAGE,mttaskxx.UNICOM,mttaskxx.PKNUMBER,mttaskxx.PKTOTAL,mttaskxx.SVRTYPE,mttaskxx.RECVTIME ";
		
		//获取查询表名的sql语句
		//String tableSql = GenericSystemMtTask01_12VoSQL.getTableSql(tableName);
		
		String withnolock ="";
		if(tableName.indexOf("union") == -1)
		{
			withnolock = StaticValue.getWITHNOLOCK();
		}
		//获取查询表名的sql语句
		String tableSql = new StringBuffer(" from ").append(tableName).append(" mttaskxx ").append(withnolock).append(" ").toString();

		//组装过滤条件语句
		String conditionSql = GenericSystemMtTask01_12VoSQL.getConditionSql(systemMtTask01_12Vo);
		//时间段的过滤条件
		List<String> timeList = GenericSystemMtTask01_12VoSQL.getTimeCondition(systemMtTask01_12Vo);
		String orderbySql = "";//GenericSystemMtTask01_12VoSQL.getOrderBySql();
		//组sql语句
		String sql = new StringBuffer(fieldSql).append(tableSql).append(conditionSql).append(orderbySql).toString();

		EmpExecutionContext.sql("execute sql："+sql);
		/**
		 * 读取配置文件里是否启用备用服务器
		 */
		String use_backup_server = SystemGlobals.getValue("montnets.emp.use_backup_server");
		/*ResourceBundle rb = ResourceBundle.getBundle("SystemGlobals");
		int backup = Integer.parseInt(rb.getString("montnets.emp.use_backup_server"));*/
		List<SystemMtTask01_12Vo> returnList;
		//有分页
		if(pageInfo != null)
		{
			//组统计总条数sql语句
			//String countSql = new StringBuffer("select count(ID) totalcount").append(tableSql).append(conditionSql).toString();
			MtTaskHistoryGenericDAO mtTaskDao = new MtTaskHistoryGenericDAO();
			//启用备用服务器
			if("1".equals(use_backup_server))
			{
				try
				{
					//用备用服务器查询获取数据
					returnList = mtTaskDao.findPageVoListBySQL(
							SystemMtTask01_12Vo.class, sql, null, pageInfo, StaticValue.EMP_BACKUP, timeList);
					return returnList;
				}
				catch (Exception e)
				{
					//如果备用服务器连接出错，则主服务器查询获取数据
					/*returnList = new DataAccessDriver().getGenericDAO().findPageVoListBySQL(
							SystemMtTask01_12Vo.class, sql, countSql, pageInfo, StaticValue.EMP_POOLNAME, timeList);*/
					EmpExecutionContext.error(e,"下行历史记录查询备用服务器连接异常");
				}
			}

			//不启用备用服务器的情况
			returnList = mtTaskDao.findPageVoListBySQL(
					SystemMtTask01_12Vo.class, sql, null, pageInfo, StaticValue.EMP_POOLNAME, timeList);

			return returnList;
		}

		//没分页
		//启用备用服务器
		if("1".equals(use_backup_server))
		{
			try
			{
				//用备用服务器查询获取数据
				returnList = findVoListBySQL(SystemMtTask01_12Vo.class, sql, StaticValue.EMP_BACKUP, timeList);
				return returnList;
			}
			catch (Exception e)
			{
				//如果备用服务器连接出错，则从主服务器查询获取数据
				//returnList = findVoListBySQL(SystemMtTask01_12Vo.class, sql, StaticValue.EMP_POOLNAME, timeList);
				EmpExecutionContext.error(e,"下行实时记录备用服务器连接异常");
			}
		}
		//不启用备用服务器的情况
		returnList = findVoListBySQL(SystemMtTask01_12Vo.class, sql, StaticValue.EMP_POOLNAME,timeList);

		return returnList;
	}

	/**
	 * 获取下行记录历史分页信息
	 * @param systemMtTask01_12Vo
	 * @param pageInfo
	 * @return
	 */
	public boolean findSystemMtTask01_12PageInfo(SystemMtTask01_12Vo systemMtTask01_12Vo, PageInfo pageInfo)
	{
		//获取查询的表名
		String tableName = getMttaskTableNameByTime(systemMtTask01_12Vo);
		if(tableName == null || tableName.length() == 0)
		{
			return false;
		}

		String withnolock ="";
		if(tableName.indexOf("union") == -1)
		{
			withnolock = StaticValue.getWITHNOLOCK();
		}
		//获取查询表名的sql语句
		String tableSql = new StringBuffer(" from ").append(tableName).append(" mttaskxx ").append(withnolock).append(" ").toString();

		//组装过滤条件语句
		String conditionSql = GenericSystemMtTask01_12VoSQL.getConditionSql(systemMtTask01_12Vo);

		//时间段的过滤条件
		List<String> timeList = GenericSystemMtTask01_12VoSQL.getTimeCondition(systemMtTask01_12Vo);
		//读取配置文件里是否启用备用服务器
		String use_backup_server = SystemGlobals.getValue("montnets.emp.use_backup_server");

		//组统计总条数sql语句
		String countSql = new StringBuffer("select count(ID) totalcount").append(tableSql).append(conditionSql).toString();
		MtTaskHistoryGenericDAO mtTaskDao = new MtTaskHistoryGenericDAO();
		//启用备用服务器
		if("1".equals(use_backup_server))
		{
			try
			{
				//用备用服务器查询获取数据
				return mtTaskDao.findPageInfoBySQL(countSql, pageInfo, StaticValue.EMP_BACKUP, timeList);
			}
			catch (Exception e)
			{
				//如果备用服务器连接出错，则主服务器查询获取数据
				EmpExecutionContext.error(e,"下行历史记录查询备用服务器连接异常");
			}
		}

		//不启用备用服务器的情况
		return mtTaskDao.findPageInfoBySQL(countSql, pageInfo, StaticValue.EMP_POOLNAME, timeList);
	}


	public PageInfo getPageInfo(int preIndex, int pageSize,
			SystemMtTask01_12Vo systemMtTask01_12Vo) throws Exception
	{
		PageInfo pageInfo = new PageInfo();
		int totalCount = 0;

		String tableName = getMttaskTableNameByTime(systemMtTask01_12Vo);

		String fieldSql = "select count(ID) totalcount";

		String tableSql = GenericSystemMtTask01_12VoSQL.getTableSql(tableName);

		String conditionSql = GenericSystemMtTask01_12VoSQL
				.getConditionSql(systemMtTask01_12Vo);
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
	protected String getMttaskTableNameByTime(SystemMtTask01_12Vo systemMtTask01_12Vo)
	{
		try
		{
			//开始时间
			Calendar caStartTime = Calendar.getInstance();
			caStartTime.setTime(sdfSeachTime.parse(systemMtTask01_12Vo.getStartTime()));

			//结束时间
			Calendar caEndTime = Calendar.getInstance();
			caEndTime.setTime(sdfSeachTime.parse(systemMtTask01_12Vo.getEndTime()));

			String tableName = "";
			//如果是同一个月
			if(caStartTime.get(Calendar.MONTH) == caEndTime.get(Calendar.MONTH))
			{
				tableName = "MTTASK" + sdfSeachDate.format(caStartTime.getTime());
				tableName = new CommonBiz().getTableName(tableName);
				return tableName;
			}

			//不是同一个月，则需要查询两张表的记录
			String startTableName = "MTTASK" + sdfSeachDate.format(caStartTime.getTime());
			startTableName = new CommonBiz().getTableName(startTableName);

			String endTableName = "MTTASK" + sdfSeachDate.format(caEndTime.getTime());
			endTableName = new CommonBiz().getTableName(endTableName);

			//两个表都存在，则组合查询sql
			if(startTableName != null && endTableName != null && startTableName.length() > 0 && endTableName.length() > 0)
			{
				tableName = new StringBuffer("(select ID,userid,spgate,cpno,phone,sendstatus,taskid,errorcode,sendtime,message,unicom,pknumber,pktotal," +
						"svrtype,recvtime from ").append(
						startTableName+" "+StaticValue.getWITHNOLOCK()).append(" union all select ID,userid,spgate,cpno,phone,sendstatus,taskid,errorcode,sendtime,message," +
								"unicom,pknumber,pktotal,svrtype,recvtime from ")
						.append(endTableName+" "+StaticValue.getWITHNOLOCK()).append(")").toString();
			}
			//只有开始时间的表
			else if(startTableName != null && startTableName.length() > 0)
			{
				tableName = startTableName;
			}
			//只有开始时间的表
			else if(endTableName != null && endTableName.length() > 0)
			{
				tableName = endTableName;
			}
			//都没用，则返回空
			else
			{
				return "";
			}
			
			return tableName;
		}
		catch (Exception e)
		{
			EmpExecutionContext.error(e, "查询下行历史记录，获取sql表名异常。");
			return null;
		}
	}
}
