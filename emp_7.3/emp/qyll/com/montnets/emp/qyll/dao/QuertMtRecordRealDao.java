package com.montnets.emp.qyll.dao;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;

import org.apache.commons.beanutils.DynaBean;

import com.montnets.emp.common.constant.StaticValue;
import com.montnets.emp.common.constant.SystemGlobals;
import com.montnets.emp.common.context.EmpExecutionContext;
import com.montnets.emp.common.dao.SuperDAO;
import com.montnets.emp.util.PageInfo;

public class QuertMtRecordRealDao  extends SuperDAO {
   
	
	/**
	 * 流量模块下行记录查询
	 * @param conditionMap
	 * @param pageInfo
	 * @return
	 */
	private SimpleDateFormat sdf = new SimpleDateFormat("yyyy");
	String year = sdf.format(new Date());
	public List<DynaBean> findMtTasksReal(LinkedHashMap<String, String> conditionMap, PageInfo pageInfo)
	{
		try
		{
			//获取要查询的表名
			String tableName = conditionMap.get("realTableName");
			//没找到则用默认
			if(tableName == null || tableName.trim().length() < 1)
			{
				tableName = "gw_mt_task_bak";
			}
			//两个表都不是定义的，则用默认
			else if(!"gw_mt_task_bak".equals(tableName) && !"mt_task".equals(tableName))
			{
				tableName = "gw_mt_task_bak";
			}
			
			//实时记录查询，修改成备份表和实时表联合查询
			String fieldSql = "select ID,USERID,SPGATE,CPNO,PHONE,TASKID,SENDSTATUS,ERRORCODE,SENDTIME,MESSAGE,UNICOM,RECVTIME,SVRTYPE,PKNUMBER,PKTOTAL,MSGFMT,P1,P2,usermsgid ";
			
			if(StaticValue.DBTYPE==StaticValue.MYSQL_DBTYPE)
			{
				//mysql实时表mt_task有两条多余的下行记录，使用PTMSGID < 0 OR  PTMSGID > 30000000000000条件排除
				tableName = new StringBuffer()
				.append("(")
				//加一个字段custid的值
				.append(fieldSql).append(",custid ").append(" from ").append("gw_mt_task_bak gw").append(StaticValue.getWITHNOLOCK())
				.append(" where  EXISTS ( select * from  LL_ORDER_DETAIL"+year+" L WHERE gw.CUSTID = L.ORDERNO ) ")
				.append(" union all ")
				//加一个字段custid的值,mt_task表里没有这个字段，赋值为NULL
				.append(fieldSql).append(",NULL ").append(" from ").append("mt_task ").append(StaticValue.getWITHNOLOCK()).append(" WHERE PTMSGID < 0 OR  PTMSGID > 30000000000000 ")
				.append(")").toString();
			}else
			{
				//db2 oracle sqlserver 
				tableName = new StringBuffer()
				.append("(")
				.append(fieldSql).append(",custid ").append(" from ").append("gw_mt_task_bak gw").append(StaticValue.getWITHNOLOCK())
				.append(" where  EXISTS ( select * from  LL_ORDER_DETAIL"+year+"  L WHERE GW.CUSTID = L.ORDERNO ) ")
				.append(" union all ")
				.append(fieldSql).append(",NULL ").append(" from ").append("mt_task").append(StaticValue.getWITHNOLOCK())
				.append(")").toString();
			}
			
			//查询表名 
			String tableSql = new StringBuffer(" from ").append(tableName).append(" mttask ").toString();
			//组装过滤条件
			String conditionSql = SysMtRecordDAOSql.getConditionSql(conditionMap, "mttask");
			//获得语句
			String sql = getSql(conditionSql,tableSql);
			//时间段
			List<String> timeList = SysMtRecordDAOSql.getTimeCondition(conditionMap);
			//返回list
			List<DynaBean> returnList;
			MtTaskGenericDAO mtTaskDao = new MtTaskGenericDAO();
			//组装统计语句
			String countSql = new StringBuffer("select count(ID) totalcount").append(tableSql).append(conditionSql).toString();
			
			//读取配置文件里是否启用备用服务器
			String use_backup_server = SystemGlobals.getValue("montnets.emp.use_backup_server");
			// 启用备用服务器
			if ("1".equals(use_backup_server)) 
			{
				try 
				{
					// 用备用服务器查询获取数据
					returnList = mtTaskDao.findPageDynaBeanBySQL(sql, countSql, pageInfo, StaticValue.EMP_BACKUP, timeList);
					return returnList;
				}
				catch (Exception e) 
				{
					//如果备用服务器连接出错，则从主服务器查询获取数据
					EmpExecutionContext.error(e, "DAO查询，下行实时记录，备用服务器连接异常。");
				}
			} 
			//记录查询语句
			EmpExecutionContext.info("系统下行实时记录，查询sql："+sql);
			
			//不启用备用服务器的情况
			returnList = mtTaskDao.findPageDynaBeanBySQL(sql, null, pageInfo, StaticValue.EMP_POOLNAME, timeList);
			//返回LIST
			return returnList;
		}
		catch (Exception e)
		{
			EmpExecutionContext.error(e, "DAO查询，下行实时记录，异常。");
			return null;
		}
	}
	
	/**
	 * 查询下行实时记录分页信息
	 * @param conditionMap 查询条件集合
	 * @param pageInfo 分页信息对象，查询到的新信息会设置到这个对象内
	 * @return 成功返回true
	 */
	public boolean findMtTasksRealPageInfo(LinkedHashMap<String, String> conditionMap, PageInfo pageInfo) 
	{
		try
		{
			//获取要查询的表名
			String tableName = conditionMap.get("realTableName");
			//没找到则用默认
			if(tableName == null || tableName.trim().length() < 1)
			{
				tableName = "gw_mt_task_bak";
			}
			//两个表都不是定义的，则用默认
			else if(!"gw_mt_task_bak".equals(tableName) && !"mt_task".equals(tableName))
			{
				tableName = "gw_mt_task_bak";
			}
			
			//实时记录查询，修改成备份表和实时表联合查询
			String fieldSql = "select CUSTID,ID,USERID,SPGATE,CPNO,PHONE,TASKID,SENDSTATUS,ERRORCODE,SENDTIME,MESSAGE,UNICOM,RECVTIME,SVRTYPE,PKNUMBER,PKTOTAL,MSGFMT,P1,P2,usermsgid ";
			String fieldSql1 = "select null as  CUSTID,ID,USERID,SPGATE,CPNO,PHONE,TASKID,SENDSTATUS,ERRORCODE,SENDTIME,MESSAGE,UNICOM,RECVTIME,SVRTYPE,PKNUMBER,PKTOTAL,MSGFMT,P1,P2,usermsgid ";
			if(StaticValue.DBTYPE==StaticValue.MYSQL_DBTYPE)
			{
				//mysql实时表mt_task有两条多余的下行记录，使用PTMSGID < 0 OR  PTMSGID > 30000000000000条件排除
				tableName = new StringBuffer()
						.append("(")
						.append(fieldSql).append(" from ").append("gw_mt_task_bak gw").append(StaticValue.getWITHNOLOCK())
						.append(" where  EXISTS ( select * from  LL_ORDER_DETAIL"+year+" L WHERE gw.CUSTID = L.ORDERNO ) ")
						.append(" union all ")
						.append(fieldSql1).append(" from ").append("mt_task").append(StaticValue.getWITHNOLOCK()).append(" WHERE PTMSGID < 0 OR  PTMSGID > 30000000000000 ")
						.append(")").toString();
			}else
			{
				//db2 oracle sqlserver
				tableName = new StringBuffer()
				.append("(")
				.append(fieldSql).append(" from ").append("gw_mt_task_bak gw").append(StaticValue.getWITHNOLOCK())
				.append(" where  EXISTS ( select * from  LL_ORDER_DETAIL"+year+" L WHERE gw.CUSTID = L.ORDERNO ) ")
				.append(" union all ")
				.append(fieldSql1).append(" from ").append("mt_task").append(StaticValue.getWITHNOLOCK())
				.append(")").toString();
			}
			//查询表名 
			String tableSql = new StringBuffer(" from ").append(tableName).append(" mttask ").append(" ").toString();
			//组装过滤条件
			String conditionSql = SysMtRecordDAOSql.getConditionSql(conditionMap, "mttask");
			
			//时间段
			List<String> timeList = SysMtRecordDAOSql.getTimeCondition(conditionMap);
			
			MtTaskGenericDAO mtTaskDao = new MtTaskGenericDAO();
			//组装统计语句
			String countSql = new StringBuffer("select count(ID) totalcount").append(tableSql).append(conditionSql).toString();
			
			//读取配置文件里是否启用备用服务器
			String use_backup_server = SystemGlobals.getValue("montnets.emp.use_backup_server");
			// 启用备用服务器
			if ("1".equals(use_backup_server)) 
			{
				try 
				{
					// 用备用服务器查询获取数据
					return mtTaskDao.findPageInfoBySQL(countSql, pageInfo, StaticValue.EMP_BACKUP, timeList);
				}
				catch (Exception e) 
				{
					//如果备用服务器连接出错，则从主服务器查询获取数据
					EmpExecutionContext.error(e, "DAO查询，下行实时记录分页信息，备用服务器连接异常。");
				}
			} 
			
			//不启用备用服务器的情况
			return mtTaskDao.findPageInfoBySQL(countSql, pageInfo, StaticValue.EMP_POOLNAME, timeList);
		}
		catch (Exception e)
		{
			EmpExecutionContext.error(e, "DAO查询，下行实时记录分页信息，异常。");
			return false;
		}
	}
	
	/**
	 * 获取查询sql
	 * @param conditionSql 查询条件集合
	 * @param tableSql 表名
	 * @return 查询sql
	 */
	private String getSql(String conditionSql, String tableSql)
	{
		//获取查询列名
		String fieldSql = "select mttask.ID,mttask.USERID,mttask.SPGATE,mttask.CPNO,mttask.PHONE,mttask.TASKID,mttask.SENDSTATUS,mttask.ERRORCODE,mttask.SENDTIME,mttask.MESSAGE,mttask.UNICOM,mttask.RECVTIME,mttask.SVRTYPE,mttask.PKNUMBER,mttask.PKTOTAL,mttask.MSGFMT,mttask.P1,mttask.P2,mttask.usermsgid,mttask.custid ";
        //排序
		String orderbySql = " order by mttask.ID desc,mttask.PHONE desc ";
        //组装sql语句
		String sql = new StringBuffer(fieldSql).append(tableSql).append(conditionSql).append(orderbySql).toString();
		return sql;
	}
	
	/**
	 * 获取oracle分页sql（暂不使用）
	 * @param systemMtTaskVo
	 * @return
	 */
	private String getOraclePageSql(String conditionSql, String tableSql)
	{
		 //获取查询列名
		StringBuffer fieldSql = new StringBuffer("select mttask.ID,mttask.USERID,mttask.SPGATE,mttask.CPNO,mttask.PHONE,mttask.TASKID,mttask.SENDSTATUS,mttask.ERRORCODE,mttask.SENDTIME,mttask.MESSAGE,mttask.UNICOM,mttask.RECVTIME,mttask.SVRTYPE,mttask.PKNUMBER,mttask.PKTOTAL,mttask.MSGFMT,mttask.P1,mttask.P2,mttask.usermsgid ");
		
		//排序
		String orderbySql = " order by mttask.ID desc ";
		
		StringBuffer pageSql = new StringBuffer("WHERE rowid IN ( SELECT rid FROM ( SELECT rownum rn, rid FROM ( SELECT rowid rid ")
				.append(tableSql).append(conditionSql).append(orderbySql).append(") WHERE rownum <= ? ) WHERE rn >= ? )");
        
        //组装sql语句
		String sql = new StringBuffer(fieldSql).append(tableSql).append(
				pageSql).append(orderbySql).toString();
		return sql;
	}

	/**
	 * 下行记录查询条件sql（暂不使用）
	 * @param conditionMap 查询条件
	 * @return
	 */
	private String getConditionSql(LinkedHashMap<String, String> conditionMap) 
	{
		StringBuffer conditionSql = new StringBuffer();
		//是否有where，默认为false即没where
		boolean hasWhere = false;
		//如果未绑定sp账号，且是多企业则查询结果为空
		if(conditionMap.get("spUsers") == null && StaticValue.getCORPTYPE() == 1)
		{
			hasWhere = addWhereOrAnd(conditionSql, hasWhere);
			conditionSql.append(" 1=2 ");
		}
        //sp账号
		if (conditionMap.get("userid") != null && conditionMap.get("userid").trim().length() > 0) 
		{
			hasWhere = addWhereOrAnd(conditionSql, hasWhere);
			conditionSql.append(" mttask.USERID").append("='").append(conditionMap.get("userid").trim()).append("'");
		}			
		//如果绑定了sp账号，则需要查询此范围内的记录；而且查询条件没sp账号的情况下，才拼发送账号范围
		if (conditionMap.get("userid") == null && conditionMap.get("spUsers") != null && conditionMap.get("spUsers").length() > 0) 
		{
			String spusers = conditionMap.get("spUsers");
			//sp账号个数
			int length = 0;
			if(spusers.contains(","))
			{
				String[] useriday = spusers.split(",");
				length = useriday.length;
			}
			hasWhere = addWhereOrAnd(conditionSql, hasWhere);
			//如果个数小于50则用in
			if(length<50)
			{
				String insqlstr=new ReciveBoxDao().getSqlStr(spusers, "mttask.USERID");
				conditionSql.append(" (" + insqlstr + ") ");
			}
			else
			{
				conditionSql.append(" exists (select SPUSER from lf_sp_dep_bind "+StaticValue.getWITHNOLOCK()+" where SPUSER=mttask.USERID ");
				if(conditionMap.get("lgcorpcode") !=null && conditionMap.get("lgcorpcode").length() > 0)
				{
					conditionSql.append(" and CORP_CODE= '").append(conditionMap.get("lgcorpcode").trim()).append("' ");
				}
				conditionSql.append(") ");
			}
		}
        //通道号
		if (conditionMap.get("spgate") != null && conditionMap.get("spgate").trim().length() > 0) 
		{
			hasWhere = addWhereOrAnd(conditionSql, hasWhere);
			conditionSql.append(" mttask.SPGATE").append("='").append(conditionMap.get("spgate").trim()).append("'");
		}
		//运营商
		if (conditionMap.get("spisuncm") != null && conditionMap.get("spisuncm").trim().length() > 0) 
		{
			hasWhere = addWhereOrAnd(conditionSql, hasWhere);
			conditionSql.append(" mttask.UNICOM").append("=").append(conditionMap.get("spisuncm"));
		}
		//业务类型
		if (conditionMap.get("buscode") != null && conditionMap.get("buscode").trim().length() > 0) 
		{
			hasWhere = addWhereOrAnd(conditionSql, hasWhere);
			if("-1".equals(conditionMap.get("buscode").trim()))
			{
				conditionSql.append(" not exists (select BUS_CODE from LF_BUSMANAGER "+StaticValue.getWITHNOLOCK()+"  where BUS_CODE=mttask.SVRTYPE) ");
			}
			else
			{
				conditionSql.append(" mttask.SVRTYPE").append("='").append(conditionMap.get("buscode").trim()).append("'");
			}
		}
        //手机号码
		if (conditionMap.get("phone") != null && conditionMap.get("phone").trim().length() > 0) 
		{
			hasWhere = addWhereOrAnd(conditionSql, hasWhere);
			conditionSql.append(" mttask.PHONE").append(" = '").append(conditionMap.get("phone").trim()).append("'");
		}
		
        //任务批次
		if (conditionMap.get("taskid") != null && conditionMap.get("taskid").trim().length() > 0)
		{
			hasWhere = addWhereOrAnd(conditionSql, hasWhere);
			conditionSql.append(" mttask.TASKID").append("=").append(conditionMap.get("taskid")).append(" ");
		}
		//操作员
		if(conditionMap.get("p1") != null && conditionMap.get("p1").trim().length() > 0)
		{
			hasWhere = addWhereOrAnd(conditionSql, hasWhere);
			conditionSql.append(" exists (select USER_CODE from lf_sysuser "+StaticValue.getWITHNOLOCK()+" where USER_CODE=mttask.p1 and (USER_NAME like '%")
			.append(conditionMap.get("p1").trim()).append("%' or NAME like '%").append(conditionMap.get("p1").trim()).append("%') ");

			if(conditionMap.get("lgcorpcode") !=null && conditionMap.get("lgcorpcode").trim().length() > 0)
			{
				conditionSql.append(" and CORP_CODE= '").append(conditionMap.get("lgcorpcode").trim()).append("' ");
			}
			conditionSql.append(") ");
		}
        //指定时间段，开始时间
		if (conditionMap.get("sendtime") != null && conditionMap.get("sendtime").trim().length() > 0) 
		{
			hasWhere = addWhereOrAnd(conditionSql, hasWhere);
			conditionSql.append(" mttask.SENDTIME >=? ");
		}
		//指定时间段，结束时间
		if (conditionMap.get("recvtime") != null && conditionMap.get("recvtime").trim().length() > 0) 
		{
			hasWhere = addWhereOrAnd(conditionSql, hasWhere);
			conditionSql.append(" mttask.SENDTIME <=? ");
		}

		String sql = conditionSql.toString();
		return sql;
	}
	
	/**
	 * 设置查询时间对象
	 * @param systemMtTaskVo
	 * @return
	 */
	private List<String> getTimeCondition(LinkedHashMap<String, String> conditionMap) 
	{
		List<String> timeList = new ArrayList<String>();
		if (conditionMap.get("sendtime") != null && conditionMap.get("sendtime").trim().length() > 0)
		{
			timeList.add(conditionMap.get("sendtime").trim());
		}
		if (conditionMap.get("recvtime") != null && conditionMap.get("recvtime").trim().length() > 0) 
		{
			timeList.add(conditionMap.get("recvtime").trim());
		}
		return timeList;
	}
	
	/**
	 * 按情况拼接where或and
	 * @param sql
	 * @param hasWhere false为没where关键词
	 * @return 第一次拼接后将会有where，直接返回true
	 */
	private boolean addWhereOrAnd(StringBuffer sql, boolean hasWhere)
	{
		if(!hasWhere)
		{
			sql.append(" where ");
		}
		else
		{
			sql.append(" and ");
		}
		return true;
	}
	
}
