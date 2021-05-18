package com.montnets.emp.query.dao;

import com.montnets.emp.common.constant.StaticValue;
import com.montnets.emp.common.constant.SystemGlobals;
import com.montnets.emp.common.context.EmpExecutionContext;
import com.montnets.emp.common.dao.SuperDAO;
import com.montnets.emp.common.dao.impl.DataAccessDriver;
import com.montnets.emp.util.PageInfo;
import org.apache.commons.beanutils.DynaBean;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 
 * @project emp_std_192.169.1.81_new
 * @author huangzb
 * @company ShenZhen Montnets Technology CO.,LTD.
 * @datetime 2015-12-2 下午05:35:15
 * @description
 */
public class SysMtRecordHisDAO extends SuperDAO
{
	
	/**
	 * 查询日期格式化：yyyy-MM-dd HH:mm:ss
	 */
	private SimpleDateFormat sdfSeachTime = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	
	/**
	 * 查询日期格式化：yyyy
	 */
	private SimpleDateFormat sdf_yyyy = new SimpleDateFormat("yyyy");
	
	/**
	 * 查询日期格式化：MM
	 */
	private SimpleDateFormat sdf_MM = new SimpleDateFormat("MM");

	
	SimpleDateFormat sdf_yyyyMM = new SimpleDateFormat("yyyyMM");
	
	/**
	 * 下行历史记录表名map，验证过是否存在的表名放这里。key为表名；value为是否存在，true表示存在，false表示不存在，null表示未验证
	 */
	private static ConcurrentHashMap<String,Boolean> mtHisTableNameMap = new ConcurrentHashMap<String,Boolean>();
	
	/**
	 * 下行历史记录查询
	 * @param conditionMap 查询条件集合
	 * @param pageInfo 分页信息对象
	 * @return 返回下行历史记录动态bean集合
	 */
	public List<DynaBean> findMtTasksHis(LinkedHashMap<String, String> conditionMap, PageInfo pageInfo)
	{
		try
		{
			//获取查询的列名
			String fieldSql = "select mttaskxx.ID,mttaskxx.USERID,mttaskxx.SPGATE,mttaskxx.CPNO,mttaskxx.PHONE,mttaskxx.SENDSTATUS,mttaskxx.TASKID,mttaskxx.ERRORCODE,mttaskxx.SENDTIME,mttaskxx.MESSAGE,mttaskxx.UNICOM,mttaskxx.PKNUMBER,mttaskxx.PKTOTAL,mttaskxx.SVRTYPE,mttaskxx.RECVTIME,mttaskxx.P1,mttaskxx.P2,mttaskxx.usermsgid ";
			
			//获取查询表名的sql语句
			String tableName = getMttaskHisTableSql(conditionMap);
			if(tableName == null || tableName.length() == 0)
			{
				return null;
			}
			
			String withnolock;
			if(tableName.indexOf("union") == -1)
			{
				withnolock = StaticValue.getWITHNOLOCK();
			}
			else
			{
				withnolock = "";
			}
			
			//获取查询表名的sql语句
			String tableSql = new StringBuffer(" from ").append(tableName).append(" mttaskxx ").append(withnolock).toString();
			
			//组装过滤条件语句
			String conditionSql = SysMtRecordDAOSql.getConditionSql(conditionMap, "mttaskxx");
			//时间段的过滤条件
			List<String> timeList = SysMtRecordDAOSql.getTimeCondition(conditionMap);
			//组sql语句
			String sql = new StringBuffer(fieldSql).append(tableSql).append(conditionSql).toString();
			
			List<DynaBean> returnList;
			//组统计总条数sql语句
			//String countSql = new StringBuffer("select count(ID) totalcount").append(tableSql).append(conditionSql).toString();
			//MtTaskGenericDAO mtTaskDao = new MtTaskGenericDAO();
			
			//读取配置文件里是否启用备用服务器
			String use_backup_server = SystemGlobals.getValue("montnets.emp.use_backup_server");
			//启用备用服务器
			if("1".equals(use_backup_server))
			{
				try 
				{
					//用备用服务器查询获取数据
					//returnList = mtTaskDao.findPageDynaBeanBySQL(sql, null, pageInfo, StaticValue.EMP_BACKUP, timeList);
                    returnList =  new DataAccessDriver().getGenericDAO().findPageDynaBeanBySQL(sql, null, pageInfo, StaticValue.EMP_BACKUP, timeList);
                    return returnList;
				}
				catch (Exception e)
				{
					//如果备用服务器连接出错，则主服务器查询获取数据
					EmpExecutionContext.error(e, "DAO查询，下行历史记录，备用服务器连接异常。");
				}
			}
			EmpExecutionContext.info("系统下行历史记录，查询sql："+sql);
			//不启用备用服务器的情况
            //			returnList = mtTaskDao.findPageDynaBeanBySQL(sql, null, pageInfo, StaticValue.EMP_POOLNAME, timeList);
            returnList =  new DataAccessDriver().getGenericDAO().findPageDynaBeanBySQL(sql, null, pageInfo, StaticValue.EMP_POOLNAME, timeList);

            return returnList;
		}
		catch (Exception e)
		{
			EmpExecutionContext.error(e, "DAO查询，下行历史记录，异常。");
			return null;
		}
	}
	
	/**
	 * 下行历史记录查询
	 * @description    
	 * @param conditionMap 查询条件集合
	 * @param pageInfo 分页信息对象
	 * @param realDbpageInfo  实时库分页对象
	 * @return 返回下行历史记录动态bean集合
	 * @author chentingsheng <cts314@163.com>
	 * @datetime 2016-10-22 下午09:24:36
	 */
	public List<DynaBean> findMtTasksHis(LinkedHashMap<String, String> conditionMap, PageInfo pageInfo, PageInfo realDbpageInfo) {
		try {
			//获取查询的列名
			String fieldSql = "select mttaskxx.ID,mttaskxx.USERID,mttaskxx.SPGATE,mttaskxx.CPNO,mttaskxx.PHONE," +
					"mttaskxx.SENDSTATUS,mttaskxx.TASKID,mttaskxx.ERRORCODE,mttaskxx.SENDTIME,mttaskxx.MESSAGE," +
					"mttaskxx.UNICOM,mttaskxx.PKNUMBER,mttaskxx.PKTOTAL,mttaskxx.SVRTYPE,mttaskxx.RECVTIME," +
					"mttaskxx.P1,mttaskxx.P2,mttaskxx.usermsgid,mttaskxx.custid ";
			//获取查询表名的sql语句
			String tableName = getMttaskHisTableSql(conditionMap);
			if(tableName == null || tableName.length() == 0) {
				return null;
			}
			//查询的数据时间
			String date = "";
			String aliasName = "";
			if(!tableName.contains("union")) {
				date = tableName.substring(6, 12);
			} else {
				aliasName = "mttaskxx";
				//结束时间
				Calendar caEndTime = Calendar.getInstance();
				caEndTime.setTime(sdfSeachTime.parse(conditionMap.get("recvtime")));
				date = sdf_yyyy.format(caEndTime.getTime()) + sdf_MM.format(caEndTime.getTime());
			}
			
			//根据时间确定查询数据的数据库,true:查历史库；false:查实时库
			boolean isBackDb = false;
			//未开启实时数据保留实时库时间
			if(StaticValue.getReadDataSaveTime() == -1) {
				if(Integer.parseInt(date) < Integer.parseInt(StaticValue.getUseHistoryDBTime())) {
					//查历史库
					isBackDb = true;
				}
			} else {
				int curDate = Integer.parseInt(sdf_yyyyMM.format(System.currentTimeMillis()));
				//当前日期减去数据日期大于数据保留实时库时间，查询历史数据库
				if(curDate - Integer.parseInt(date) > StaticValue.getReadDataSaveTime()) {
					isBackDb = true;
				}
			}
			
			//获取查询表名的sql语句
			String tableSql = " from " + tableName + aliasName;
			//组sql语句
			String sql = fieldSql + tableSql;
			List<DynaBean> returnList;
			//读取配置文件里是否启用备用服务器
			String hisServer = SystemGlobals.getValue("montnets.emp.use_history_server");
			EmpExecutionContext.info("查询下行记录，date:"+date +"，use_history_server:"+hisServer+"，sql:"+sql);
			//组装统计语句
			String countSql = "select count(ID) totalcount from " + tableName + aliasName;
            if(tableName.contains("union all")) {
                tableName = tableName.replaceAll("aa.ID,aa.userid,aa.spgate,aa.cpno,aa.phone,aa.sendstatus,aa.taskid,aa.errorcode,aa.sendtime,aa.message,aa.unicom,aa.pknumber,aa.pktotal,aa.svrtype,aa.recvtime,aa.p1,aa.p2,aa.usermsgid,aa.custid", " count(*) as cnt ");
			    countSql = "select sum(cnt) totalcount from " + tableName + aliasName;
            }

			//如果是配置日期的表
		    if((StaticValue.getUseHistoryDBTime().equals(date) && "1".equals(hisServer))) {
		    	//查询实时库数据
                returnList = new DataAccessDriver().getGenericDAO().findPageDynaBeanBySQL(sql, countSql, pageInfo, StaticValue.EMP_POOLNAME, null);
                //有数据直接返回
				if(returnList != null && returnList.size() > 0) {
					return returnList;
				} else {
					EmpExecutionContext.info("查询下行记录，实时库无数据，date:"+date+"，SQL:"+sql);
					PageInfo pageInfoTemp = new PageInfo();
	    			pageInfoTemp.setPageSize(pageInfo.getPageSize());
	    			//实时库的总记录数为0
	    			if(realDbpageInfo.getTotalRec() == 0) {
	    				//以实际的查询页数
	    				pageInfoTemp.setPageIndex(pageInfo.getPageIndex());
	    			}
	    			else {
	    				//查询页数减去实时总页数
	    				pageInfoTemp.setPageIndex(pageInfo.getPageIndex() - realDbpageInfo.getTotalPage());
	    			}
                    //返回历史库查询结果
                    returnList = new DataAccessDriver().getGenericDAO().findPageDynaBeanBySQL(sql, countSql, pageInfo, StaticValue.EMP_POOLNAME, null);
                    return returnList;
				}
		    } else {
		    	String poolName = StaticValue.EMP_POOLNAME;
		    	//查询历史库
		    	if(isBackDb && "1".equals(hisServer)) {
		    		poolName = StaticValue.EMP_BACKUP;
		    	}
                returnList = new DataAccessDriver().getGenericDAO().findPageDynaBeanBySQL(sql, countSql, pageInfo, poolName, null);
                return returnList;
            }
		} catch (Exception e) {
			EmpExecutionContext.error(e, "DAO查询，下行历史记录，异常。");
			return null;
		}
	}
	
	/**
	 * 获取下行历史记录分页信息
	 * @param conditionMap 查询条件集合
	 * @param pageInfo 分页信息对象，查询到的新信息会设置到这个对象内
	 * @return 成功返回true
	 */
	public boolean findMtTasksHisPageInfo(LinkedHashMap<String, String> conditionMap, PageInfo pageInfo)
	{
		try
		{
			//获取查询的表名
			String tableName = getMttaskHisTableSql(conditionMap);
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
			String conditionSql = SysMtRecordDAOSql.getConditionSql(conditionMap, "mttaskxx");
			
			//时间段的过滤条件
			List<String> timeList = SysMtRecordDAOSql.getTimeCondition(conditionMap);
			
			//组统计总条数sql语句
			String countSql = new StringBuffer("select count(ID) totalcount").append(tableSql).append(conditionSql).toString();
			MtTaskGenericDAO mtTaskDao = new MtTaskGenericDAO();
			
			//读取配置文件里是否启用备用服务器
			String use_backup_server = SystemGlobals.getValue("montnets.emp.use_backup_server");
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
					EmpExecutionContext.error(e, "DAO查询，下行历史记录分页信息，备用服务器连接异常。");
				}
			}
			
			//不启用备用服务器的情况
			return mtTaskDao.findPageInfoBySQL(countSql, pageInfo, StaticValue.EMP_POOLNAME, timeList);
		}
		catch (Exception e)
		{
			EmpExecutionContext.error(e, "DAO查询，下行历史记录分页信息，异常。");
			return false;
		}
	}
	
	/**
	 * 获取下行历史记录分页信息
	 * @description    
	 * @param conditionMap 查询条件集合
	 * @param pageInfo 分页信息对象，查询到的新信息会设置到这个对象内
	 * @param realDbpageInfo  实时库分页对象
	 * @return 成功返回true
	 * @author chentingsheng <cts314@163.com>
	 * @datetime 2016-10-22 下午09:34:16
	 */
	public boolean findMtTasksHisPageInfo(LinkedHashMap<String, String> conditionMap, PageInfo pageInfo, PageInfo realDbpageInfo)
	{
		try
		{
			//获取查询的表名
			String tableName = getMttaskHisTableSql(conditionMap);
			if(tableName == null || tableName.length() == 0)
			{
				return false;
			}
			//查询的数据时间
			String date = "";
			String withnolock ="";
			if(!tableName.contains("union"))
			{
				withnolock = StaticValue.getWITHNOLOCK();
				date = tableName.substring(6, 12);
			}
			else
			{
				//结束时间
				Calendar caEndTime = Calendar.getInstance();
				caEndTime.setTime(sdfSeachTime.parse(conditionMap.get("recvtime")));
				date = sdf_yyyy.format(caEndTime.getTime()) + sdf_MM.format(caEndTime.getTime());
			}
			
			//根据时间确定查询数据的数据库,true:查历史库；false:查实时库
			boolean isBackDb = false;
			//未开启实时数据保留实时库时间
			if(StaticValue.getReadDataSaveTime() == -1)
			{
				if(Integer.parseInt(date) < Integer.parseInt(StaticValue.getUseHistoryDBTime()))
				{
					//查历史库
					isBackDb = true;
				}
			}
			else
			{
				int curDate = Integer.parseInt(sdf_yyyyMM.format(System.currentTimeMillis()));
				//当前日期减去数据日期大于数据保留实时库时间，查询历史数据库
				if(curDate - Integer.parseInt(date) > StaticValue.getReadDataSaveTime())
				{
					isBackDb = true;
				}
			}
			
			//获取查询表名的sql语句
			String tableSql = " from " + tableName;// + " mttaskxx " + withnolock + " ";
			
			//组装过滤条件语句
//			String conditionSql = SysMtRecordDAOSql.getConditionSql(conditionMap, "mttaskxx");

			//时间段的过滤条件
			//List<String> timeList = SysMtRecordDAOSql.getTimeCondition(conditionMap);
			
			//组统计总条数sql语句
			String countSql = "select count(*) totalcount" + tableSql; //+ conditionSql;
			MtTaskGenericDAO mtTaskDao = new MtTaskGenericDAO();
			
			//读取配置文件里是否启用备用服务器
			String use_his_server = SystemGlobals.getValue("montnets.emp.use_history_server");
			EmpExecutionContext.info("查询下行记录，tableName:"+tableName
					 				+"，use_history_server:"+use_his_server+"，countSql:"+countSql);
			if((StaticValue.getUseHistoryDBTime().equals(date) && "1".equals(use_his_server)))
			{
	    		mtTaskDao.findPageInfoBySQL(countSql, pageInfo, StaticValue.EMP_POOLNAME, null);
    			//设置实时库的总页数
    			realDbpageInfo.setTotalPage(pageInfo.getTotalPage());
    			//设置实时库的总记录数
    			realDbpageInfo.setTotalRec(pageInfo.getTotalRec());
    			
		    	//获取历史库的总数
	    		PageInfo pageInfoTemp = new PageInfo();
		    	//设置查询一页条数
		    	pageInfoTemp.setPageSize(pageInfo.getPageSize());
		    	mtTaskDao.findPageInfoBySQL(countSql, pageInfoTemp, StaticValue.EMP_BACKUP, null);
		    	if(pageInfoTemp.getTotalRec() > 0)
		    	{
		    		if(pageInfo.getTotalRec() == 0)
		    		{
		    			//直接赋值
		    			pageInfo.setTotalPage(pageInfoTemp.getTotalPage());
		    		}
		    		else
		    		{
		    			//累加总页数
		    			pageInfo.setTotalPage(pageInfo.getTotalPage() + pageInfoTemp.getTotalPage());
		    		}
		    		//累加总记录数
		    		pageInfo.setTotalRec(pageInfo.getTotalRec() + pageInfoTemp.getTotalRec());
		    	}
		    	return true;
			}
			else
		    {
		    	String poolName = StaticValue.EMP_POOLNAME;
		    	//查询历史库
		    	if(isBackDb && "1".equals(use_his_server))
		    	{
		    		poolName = StaticValue.EMP_BACKUP;
		    	}
		    	return mtTaskDao.findPageInfoBySQL(countSql, pageInfo, poolName, null);
		    }
			
		}
		catch (Exception e)
		{
			EmpExecutionContext.error(e, "DAO查询，下行历史记录分页信息，异常。");
			return false;
		}
	}
	
	/**
	 * 获取表名sql
	 * @param conditionMap 查询条件
	 * @return 表名sql
	 */
	private String getMttaskHisTableSql(LinkedHashMap<String, String> conditionMap) {
		try {
			//开始时间
			Calendar caStartTime = Calendar.getInstance();
			caStartTime.setTime(sdfSeachTime.parse(conditionMap.get("sendtime")));
			
			//结束时间
			Calendar caEndTime = Calendar.getInstance();
			caEndTime.setTime(sdfSeachTime.parse(conditionMap.get("recvtime")));
			
			//获取查询表名
			String tableName = getMttaskHisTableName("MTTASK", sdf_yyyy.format(caStartTime.getTime()), sdf_MM.format(caStartTime.getTime()));
			//表不存在
			if(tableName == null) {
				EmpExecutionContext.info("DAO查询下行历史记录，获取表名sql，表不存在。" + "tableName=" + "MTTASK" + sdf_yyyy.format(caStartTime.getTime()) + sdf_MM.format(caStartTime.getTime()) );
			}
			
			//如果是同一个月，则查单表
			if(caStartTime.get(Calendar.MONTH) == caEndTime.get(Calendar.MONTH)) {
				//组装过滤条件语句
				String conditionSql = SysMtRecordDAOSql.getConditionSql(conditionMap, "mttaskxx");
				return tableName + " mttaskxx " + conditionSql;
			}
			
			//跨月查询，则需要查询两张表的记录，只允许查两个月以内的数据
			//获取第二张历史表表名
			String secondTableName = getMttaskHisTableName("MTTASK", sdf_yyyy.format(caEndTime.getTime()), sdf_MM.format(caEndTime.getTime()));
			
			//两个表都不存在
			if(tableName == null && secondTableName == null) {
				EmpExecutionContext.info("DAO查询下行历史记录，获取表名sql，两个表都不存在。" 
						+ "tableName=" + "MTTASK" + sdf_yyyy.format(caStartTime.getTime()) + sdf_MM.format(caStartTime.getTime())
						+ ",secondTableName=" + "MTTASK" + sdf_yyyy.format(caEndTime.getTime()) + sdf_MM.format(caEndTime.getTime())
						);
				return null;
			} else if(tableName == null) {
				//第二个表存在
				EmpExecutionContext.info("DAO查询下行历史记录，获取表名sql，表不存在。" 
						+ "tableName=" + "MTTASK" + sdf_yyyy.format(caStartTime.getTime()) + sdf_MM.format(caStartTime.getTime())
						);
				//组装过滤条件语句
				String conditionSql = SysMtRecordDAOSql.getConditionSql(conditionMap, "mttaskxx");
				return secondTableName +  " mttaskxx " + conditionSql;
			} else if(secondTableName == null) {
				//第一个表存在（界面控制好的话，这种情况应该不会发生）
				EmpExecutionContext.info("DAO查询下行历史记录，获取表名sql，表不存在。" 
						+ ",secondTableName=" + "MTTASK" + sdf_yyyy.format(caEndTime.getTime()) + sdf_MM.format(caEndTime.getTime())
						);
				//组装过滤条件语句
				String conditionSql = SysMtRecordDAOSql.getConditionSql(conditionMap, "mttaskxx");
				return tableName + " mttaskxx " + conditionSql;
			}
			
			//两个表都存在，则用联合查询
			//组装过滤条件语句
			String conditionSql = SysMtRecordDAOSql.getConditionSql(conditionMap, "aa");
			String fieldSql = "select aa.ID,aa.userid,aa.spgate,aa.cpno,aa.phone,aa.sendstatus,aa.taskid,aa.errorcode,aa.sendtime," +
					"aa.message,aa.unicom,aa.pknumber,aa.pktotal,aa.svrtype,aa.recvtime,aa.p1,aa.p2,aa.usermsgid,aa.custid ";
			return "(" +
					fieldSql + " from " + tableName + " aa " + conditionSql + StaticValue.getWITHNOLOCK() +
					" union all " +
					fieldSql + " from " + secondTableName + " aa " + conditionSql + StaticValue.getWITHNOLOCK() + ")";
		} catch (Exception e) {
			EmpExecutionContext.error(e, "DAO查询下行历史记录，获取表名sql，异常。");
			return null;
		}
	}
	
	/**
	 * 验证是否升级了历史表。先按新的历史表名去找，若新的历史表不存在，则找历史表名。
	 * 
	 * @param tableName 表名
	 * @param year 年份，4位
	 * @param month 月份，2位
	 * @return 返回下行历史记录表名;表不存在这返回null，异常则返回新表名
	 */
	private String getMttaskHisTableName(String tableName, String year, String month)
	{
		//新的表名，如：MTTASK201512
		String newTableName = tableName + year + month;
		try
		{
			//老的表名，如：MTTASK12
			String oldTableName = tableName + month;
			
			/*//新表名之前验证过的话，且是存在的，就直接返回新表名
			if(mtHisTableNameMap.get(newTableName) != null && mtHisTableNameMap.get(newTableName))
			{
				return newTableName;
			}
			//老表名之前验证过的话，且是存在的，就直接返回老表名
			else if(mtHisTableNameMap.get(oldTableName) != null && mtHisTableNameMap.get(oldTableName))
			{
				return oldTableName;
			}
			//新老表名之前都验证过，且都不存在，直接返回null
			else if(mtHisTableNameMap.get(newTableName) != null && mtHisTableNameMap.get(oldTableName) != null 
					&& !mtHisTableNameMap.get(newTableName) && !mtHisTableNameMap.get(oldTableName))
			{
				return null;
			}*/
			
			//0：不存在；1：存在；小于0：异常情况。
			int newTableExists = getMtHisTableExists(newTableName);
			//表存在
			if(newTableExists > 0)
			{
				//表名存在，则放进内存集合中，下次直接用
				mtHisTableNameMap.put(newTableName, true);
				return newTableName;
			}
			//表不存在
			else if(newTableExists == 0)
			{
				//表名不存在，则放进内存集合中，下次直接用
				mtHisTableNameMap.put(newTableName, false);
			}
			//异常情况
			else
			{
				return newTableName;
			}
			
			//0：不存在；1：存在；小于0：异常情况。
			int oldTableExists = getMtHisTableExists(oldTableName);
			if(oldTableExists > 0)
			{
				//表存在，则放进内存集合中，下次直接用
				mtHisTableNameMap.put(oldTableName, true);
				return oldTableName;
			}
			else if(oldTableExists == 0)
			{
				//表不存在，则放进内存集合中，下次直接用
				mtHisTableNameMap.put(oldTableName, false);
				//来到这里，则新旧表都不存在
				return null;
			}
			//异常情况
			else
			{
				//异常情况
				return null;
			}
		}
		catch (Exception e)
		{
			EmpExecutionContext.error(e, "验证并获取下行历史表名，异常。");
			return newTableName;
		}
	}
	
	/**
	 * 获取下行历史记录表名是否存在。
	 * @param tableName 下行历史记录表名
	 * @return 0：不存在；1：存在；小于0：异常情况。
	 */
	private int getMtHisTableExists(String tableName)
	{
		try
		{
			//查询sql
			String sql;
			
			switch (StaticValue.DBTYPE)
			{
				case 1:
					// oracle
					sql = "select count(*) ICOUNT from all_tables where table_name=upper('" + tableName + "') and OWNER=upper('" + SystemGlobals.getValue("montnets.emp.user") + "')";
					break;
				case 2:
					// sqlserver2005
					sql = "select count(*) ICOUNT from sysobjects where id = object_id('" + tableName + "') and type = 'u'";
					break;
				case 3:
					// MYSQL
					sql = "select count(*) ICOUNT from INFORMATION_SCHEMA.TABLES where TABLE_SCHEMA='" + SystemGlobals.getValue("montnets.emp.databaseName") + "' and TABLE_NAME=upper('" + tableName + "')";
					break;
				case 4:
					// DB2
					sql = "select count(*) ICOUNT from syscat.tables where tabname=upper('" + tableName + "')";
					break;
				default:
					EmpExecutionContext.error("获取下行历史记录表名是否存在，不支持的数据库类型。dbType=" + StaticValue.DBTYPE);
					return -1;
			}
			
			//查询新表是否存在
			List<DynaBean> resultList = getListDynaBeanBySql(sql);
			//出异常查不到记录
			if(resultList == null || resultList.size() == 0)
			{
				EmpExecutionContext.error("获取下行历史记录表名是否存在，查询新表是否存在失败。sql="+sql);
				return -2;
			}
			//出异常取不到结果（这种情况不太可能出现）
			if(resultList.get(0).get("icount") == null)
			{
				EmpExecutionContext.error("获取下行历史记录表名是否存在，查询新表是否存在，获取结果为空。sql="+sql);
				return -3;
			}
			
			String icount = resultList.get(0).get("icount").toString();
			return Integer.parseInt(icount);
		}
		catch (Exception e)
		{
			EmpExecutionContext.error("获取下行历史记录表名是否存在，异常。");
			return -9999;
		}
	}
	
}
