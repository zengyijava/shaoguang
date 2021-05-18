package com.montnets.emp.engine.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Timestamp;
import java.util.*;

import com.montnets.emp.common.constant.StaticValue;
import com.montnets.emp.common.constant.SystemGlobals;
import com.montnets.emp.common.context.EmpExecutionContext;
import com.montnets.emp.common.dao.DepDAO;
import com.montnets.emp.common.dao.SuperDAO;
import com.montnets.emp.common.dao.impl.DataAccessDriver;
import com.montnets.emp.common.vo.LfFlowRecordVo;
import com.montnets.emp.common.vo.SendedMttaskVo;
import com.montnets.emp.engine.vo.LfMttaskVo;
import com.montnets.emp.entity.sysuser.LfDep;
import com.montnets.emp.entity.sysuser.LfSysuser;
import com.montnets.emp.table.approveflow.TableLfFlowRecord;
import com.montnets.emp.table.pasgroup.TableUserdata;
import com.montnets.emp.table.sms.TableLfMttask;
import com.montnets.emp.table.sms.TableMtTask;
import com.montnets.emp.table.sysuser.TableLfDep;
import com.montnets.emp.table.sysuser.TableLfDomination;
import com.montnets.emp.table.sysuser.TableLfSysuser;
import com.montnets.emp.util.PageInfo;


/**
 * 下行业务记录dao
 * @project emp_std_192.169.1.81
 * @author huangzb
 * @company ShenZhen Montnets Technology CO.,LTD.
 * @datetime 2013-11-22 上午09:49:01
 * @description
 */
public class SerMtTaskDAO extends SuperDAO{

	/**
	 * 获取审批信息
	 * @param mtID
	 * @param rLevel
	 * @return
	 * @throws Exception
	 */
	public List<LfFlowRecordVo> findLfFlowRecordVo(String mtID, String rLevel,String reviewType)
			throws Exception
	{
		//字段
		String fieldSql = "select flowrecord.FR_ID,flowrecord.MT_ID,flowrecord.F_ID,flowrecord.REVIEW_TYPE,flowrecord.R_TIME,flowrecord.R_LEVEL," +
							"flowrecord.R_LEVELAMOUNT,flowrecord.R_CONTENT,flowrecord.R_STATE,flowrecord.COMMENTS,prerevisysuser.NAME preReviName,revisysuser.NAME reviName," +
							"sysuser.NAME,sysuser.USER_NAME,dep.DEP_NAME,mttask.TIMER_STATUS,mttask.TIMER_TIME,mttask.TITLE,mttask.TASKNAME,mttask.BMTTYPE,mttask.SP_USER," +
							"mttask.SUBMITTIME,mttask.MOBILE_URL,mttask.MSG_TYPE,mttask.EFF_COUNT,mttask.MSG,mttask.TMPL_PATH,userdata.STAFFNAME,sysuser.USER_STATE";
		//表名
		String tableSql =  new StringBuffer(" from ").append(
				TableLfFlowRecord.TABLE_NAME).append(" flowrecord ").append(
						StaticValue.getWITHNOLOCK()).append(" left join ").append(
						TableLfSysuser.TABLE_NAME).append(" prerevisysuser ").append(
						StaticValue.getWITHNOLOCK()).append(" on flowrecord.").append(
						TableLfFlowRecord.PRE_RV).append("=prerevisysuser.").append(
						TableLfSysuser.USER_ID).append(" inner join ").append(
						TableLfSysuser.TABLE_NAME).append(" revisysuser ").append(
						StaticValue.getWITHNOLOCK()).append(" on flowrecord.").append(
						TableLfFlowRecord.REVIEWER).append("=revisysuser.").append(
						TableLfSysuser.USER_ID).append(" inner join ").append(
						TableLfMttask.TABLE_NAME).append(" mttask ").append(
						StaticValue.getWITHNOLOCK()).append(" on mttask.").append(
						TableLfMttask.MT_ID).append("=flowrecord.").append(
						TableLfFlowRecord.MT_ID).append(" inner join ").append(
						TableLfSysuser.TABLE_NAME).append(" sysuser ").append(
						StaticValue.getWITHNOLOCK()).append(" on sysuser.").append(
						TableLfSysuser.USER_ID).append("=mttask.").append(
						TableLfMttask.USER_ID).append(" inner join ").append(
						TableLfDep.TABLE_NAME).append(" dep ").append(
						StaticValue.getWITHNOLOCK()).append(" on dep.").append(
						TableLfDep.DEP_ID).append("=sysuser.").append(
						TableLfSysuser.DEP_ID).append(" left join ").append(
						TableUserdata.TABLE_NAME).append(" userdata ").append(
						StaticValue.getWITHNOLOCK()).append(" on mttask.").append(
						TableLfMttask.SP_USER).append("=userdata.").append(
						TableUserdata.USER_ID).toString();
		
		tableSql = tableSql + " and userdata."+TableUserdata.ACCOUNTTYPE + " = " + reviewType;
		//条件
		StringBuffer conditionSql = new StringBuffer(" where flowrecord.").append(
				TableLfFlowRecord.MT_ID).append("=").append(mtID);
		//审批级别
		if(rLevel != null)
		{
			conditionSql = conditionSql.append(" and flowrecord.").append(TableLfFlowRecord.R_LEVEL).append(
					"<=").append(rLevel);
		}
		//类型
		if(reviewType != null)
		{
			conditionSql = conditionSql.append(" and flowrecord.").append(TableLfFlowRecord.REVIEW_TYPE).append(
					"=").append(reviewType);
		}
		//排序
		String orderBySql = new StringBuffer(" order by flowrecord.").append(
				TableLfFlowRecord.R_LEVEL).append(" asc").toString();
		//查询sql
		String sql = new StringBuffer(fieldSql).append(tableSql).append(
				conditionSql).append(orderBySql).toString();
		//调用公共方法查询
		List<LfFlowRecordVo> returnVoList = findVoListBySQL(
				LfFlowRecordVo.class, sql, StaticValue.EMP_POOLNAME);
		//返回查询的数据
		return returnVoList;
	}
	
	
	/**
	 * 查询短信发送任务
	 * @param conditionMap
	 * @param orderMap
	 * @param pageInfo
	 * @param tableName
	 * @return
	 * @throws Exception
	 */
	public List<SendedMttaskVo> findMtTaskVo(LinkedHashMap<String, String> conditionMap,LinkedHashMap<String,String> orderMap,PageInfo pageInfo,String tableName) 
	throws Exception
	{
		//查询字段拼接
	    String fieldSql = SerMtTaskSql.getPartmttaskFieldSql();
	    //查询表拼接
	    String tableSql = SerMtTaskSql.getmttaskTableSql(tableName);
	    //组装过滤条件
	    String conditionSql = SerMtTaskSql.getmttaksConditionSql(conditionMap);
	    //组装SQL语句
	    String sql = new StringBuffer(fieldSql).append(tableSql).append(conditionSql).toString();
	    //组装统计语句
	    String countSql = new StringBuffer("select count(*) totalcount").append(tableSql).append(conditionSql)
	    .toString();
	    //调用查询语句
	    List<SendedMttaskVo> mttaskList = new DataAccessDriver().getGenericDAO().findPageVoListBySQL(SendedMttaskVo.class, sql, countSql, pageInfo, StaticValue.EMP_POOLNAME);
	    //返回结果集
	    return mttaskList;
	}
	
	/**
	 * 获取短信发送任务
	 * @param conditionMap
	 * @param orderbyMap
	 * @param tableName
	 * @return
	 * @throws Exception
	 */
	public List<SendedMttaskVo> findMtTaskVo(LinkedHashMap<String, String> conditionMap,LinkedHashMap<String,String> orderbyMap,String tableName)
	         throws Exception
	{
		//查询字段拼接
		    String fieldSql = SerMtTaskSql.getPartmttaskFieldSql();
		    String tableSql = SerMtTaskSql.getmttaskTableSql(tableName);
		    //组装过滤条件
		    String conditionSql = SerMtTaskSql.getmttaksConditionSql(conditionMap);
		    //组装SQL语句
		    String sql = new StringBuffer(fieldSql).append(tableSql).append(conditionSql).toString();
            //调用查询方法
		    List<SendedMttaskVo> mttaskList = findVoListBySQL(SendedMttaskVo.class, sql, StaticValue.EMP_POOLNAME);
		    //返回结果集
		    return mttaskList;
	}
	
	/**
	 * 发送详情的导入查询（同时查实时表和历史表）
	 * @param conditionMap
	 * @param orderbyMap
	 * @param tableName
	 * @return
	 * @throws Exception
	 */
	public List<SendedMttaskVo> findMtTaskVoTwoTable(LinkedHashMap<String, String> conditionMap,LinkedHashMap<String,String> orderbyMap,String tableName)
    throws Exception
	{
		//查询字段拼接
   	   	String fieldSql = SerMtTaskSql.getPartmttaskFieldSql();
   	   	//查询表拼接
	    String tableSql = SerMtTaskSql.getmttaskTableSqlTwo(tableName,conditionMap);
	    //查询条件拼接
	    String conditionSql =""; 
	    //组装SQL语句
	    String sql = new StringBuffer(fieldSql).append(" ,mt.").append(TableMtTask.TASK_ID).append(" from (").append(tableSql).append(") mt").append(conditionSql).toString();
        //调用查询方法
	    List<SendedMttaskVo> mttaskList = findVoListBySQL(SendedMttaskVo.class, sql, StaticValue.EMP_POOLNAME);
	   
	   return mttaskList;
	}
	
	/**
	 * 发送详情的详情查询（同时查实时表和历史表）
	 * @param conditionMap
	 * @param orderbyMap
	 * @param tableName
	 * @param pageInfo 分页
	 * @return
	 * @throws Exception
	 */
	public List<SendedMttaskVo> findMtTaskVoTwoTable(LinkedHashMap<String, String> conditionMap,LinkedHashMap<String,String> orderMap,PageInfo pageInfo,String tableName) 
	throws Exception
	{
		//查询字段拼接
	    String fieldSql = SerMtTaskSql.getPartmttaskFieldSql();
	    String tableSql = SerMtTaskSql.getmttaskTableSqlTwo(tableName,conditionMap);
	    //组装过滤条件
	    String conditionSql = "";//GenericLfMttaskVoSQL.getmttaksConditionSql(conditionMap);
	    //组装SQL语句
	    String sql = new StringBuffer(fieldSql).append(" ,mt.").append(TableMtTask.TASK_ID).append(" from (").append(tableSql).append(") mt").append(conditionSql).toString();
	    //组装统计语句
	    String countSql = new StringBuffer("select count(*) totalcount from (").append(tableSql).append(") mt").append(conditionSql)
	    .toString();
	    //调用查询方法 
	    List<SendedMttaskVo> mttaskList = new DataAccessDriver().getGenericDAO().findPageVoListBySQL(SendedMttaskVo.class, sql, countSql, pageInfo, StaticValue.EMP_POOLNAME);
	    return mttaskList;
	}
	
	/**
	 * 跟据taskid查询mtdatareport表中任务的icount和
	 * @param conditionMap
	 * @return
	 * @throws Exception
	 */
	public long findSumIcount(LinkedHashMap<String, String> conditionMap) 
	throws Exception
	{
		//查询字段拼接
	    String fieldSql = SerMtTaskSql.getSumIcountMtdatareportFieldSql();
	    String tableSql = SerMtTaskSql.getSumIcountMtdatareportTableSql();
	    //组装过滤条件 
	    String conditionSql = SerMtTaskSql.getSumIcountMtdatareportcConditionSql(conditionMap);
	    //组装SQL语句
	    String sql = new StringBuffer(fieldSql).append(tableSql).append(conditionSql).toString();
        //调用统计总条数语句
	    long  strCount=findCountBySQL(sql, null);
	    return strCount;
	}
	
	/**
	 * 获取短信发送任务
	 * @param GL_UserID
	 * @param lfMttaskVo
	 * @return lfMttaskVoList
	 * @throws Exception
	 */
	public List<LfMttaskVo> findLfMttaskVo(Long GL_UserID, LfMttaskVo lfMttaskVo)
			throws Exception
	{
		//查询字段拼接
		String fieldSql = SerMtTaskSql.getFieldSql();
		//查询表拼接
		String tableSql = SerMtTaskSql.getTableSql(lfMttaskVo.getCorpCode());
		//管辖范围拼接
		String dominationSql ="";
		String dominationSql2="";
		
		if((lfMttaskVo.getDepIds()==null||"".equals(lfMttaskVo.getDepIds()))&&(lfMttaskVo.getUserIds()==null||"".equals(lfMttaskVo.getUserIds()))){
			//管辖范围拼接
			dominationSql = SerMtTaskSql.getDominationSql(String
					.valueOf(GL_UserID));
			dominationSql2 = SerMtTaskSql.getDominationSql2(String
					.valueOf(GL_UserID));
		}
		
		
		if(lfMttaskVo.getDepIds()!=null&&!"".equals(lfMttaskVo.getDepIds()))
		{
			LfDep lfDep=new DataAccessDriver().getEmpDAO().findObjectByID(LfDep.class, Long.parseLong(lfMttaskVo.getDepIds()));
			if(lfDep!=null&&lfDep.getDepLevel().intValue()==1){
					lfMttaskVo.setDepIds("");
					lfMttaskVo.setCorpCode(lfDep.getCorpCode());
			}else{
				String depid=new DepDAO().getChildUserDepByParentID(Long.parseLong(lfMttaskVo.getDepIds()),TableLfDep.DEP_ID);
				lfMttaskVo.setDepIds(depid);
			}
		}
		
		//组装过滤条件
		String conditionSql = SerMtTaskSql.getConditionSql(lfMttaskVo);
		//组装排序条件
		String orderBySql = SerMtTaskSql.getOrderBySql();
		//组装SQL语句
		String sql="";
		if((lfMttaskVo.getDepIds()==null||"".equals(lfMttaskVo.getDepIds()))&&
				(lfMttaskVo.getUserIds()==null||"".equals(lfMttaskVo.getUserIds()))){
			sql = new StringBuffer(fieldSql+" from ( "+fieldSql).append(tableSql).append(
				dominationSql2).append(conditionSql).append("  union  ").append(fieldSql).append(tableSql).append(
						dominationSql).append(conditionSql+" ) mttask ").append(orderBySql)
				.toString();
		}else{
			//组装SQL语句
			sql = new StringBuffer(fieldSql).append(tableSql).append(
					dominationSql).append(conditionSql).append(orderBySql)
					.toString();
		}
		//调用查询方法
		List<LfMttaskVo> lfMttaskVoList = findVoListBySQL(LfMttaskVo.class,
				sql, StaticValue.EMP_POOLNAME);
		return lfMttaskVoList;
	}
	
	/**
	 * 获取短信发送任务
	 * @param GL_UserID
	 * @param lfMttaskVo
	 * @param pageInfo
	 * @return lfMttaskVoList
	 * @throws Exception
	 */
	public List<LfMttaskVo> findLfMttaskVo(Long GL_UserID,
			LfMttaskVo lfMttaskVo, PageInfo pageInfo) throws Exception
	{
		//查询字段拼接
		String fieldSql = SerMtTaskSql.getFieldSql();
		//查询表拼接
		String tableSql = SerMtTaskSql.getTableSql(lfMttaskVo.getCorpCode());
		String dominationSql ="";
		String dominationSql2="";
		
		
		if((lfMttaskVo.getDepIds()==null||"".equals(lfMttaskVo.getDepIds()))&&(lfMttaskVo.getUserIds()==null||"".equals(lfMttaskVo.getUserIds()))){
		//管辖范围拼接
		dominationSql = SerMtTaskSql.getDominationSql(String
				.valueOf(GL_UserID));
		dominationSql2 = SerMtTaskSql.getDominationSql2(String
				.valueOf(GL_UserID));
		}
		
		if(lfMttaskVo.getDepIds()!=null&&!"".equals(lfMttaskVo.getDepIds()))
		{
			LfDep lfDep=new DataAccessDriver().getEmpDAO().findObjectByID(LfDep.class, Long.parseLong(lfMttaskVo.getDepIds()));
			if(lfDep!=null&&lfDep.getDepLevel().intValue()==1){
					lfMttaskVo.setDepIds("");
					lfMttaskVo.setCorpCode(lfDep.getCorpCode());
			}else{
				String depid=new DepDAO().getChildUserDepByParentID(Long.parseLong(lfMttaskVo.getDepIds()),TableLfDep.DEP_ID);
				lfMttaskVo.setDepIds(depid);
			}
		}
		
		//组装过滤条件
		String conditionSql = SerMtTaskSql.getConditionSql(lfMttaskVo);
		//组装排序条件
		String orderBySql = SerMtTaskSql.getOrderBySql();
		//组装SQL语句
		String sql="";
		String countSql="";
		if((lfMttaskVo.getDepIds()==null||"".equals(lfMttaskVo.getDepIds()))&&
				(lfMttaskVo.getUserIds()==null||"".equals(lfMttaskVo.getUserIds()))){
			sql = new StringBuffer(fieldSql+" from ( "+fieldSql).append(tableSql).append(
				dominationSql2).append(conditionSql).append("  union  ").append(fieldSql).append(tableSql).append(
						dominationSql).append(conditionSql+" ) mttask ").append(orderBySql)
				.toString();
			countSql = new StringBuffer("select count(*) totalcount")
			.append("  from ((").append(fieldSql).append(tableSql).append(
					dominationSql2).append(conditionSql).append(") union (").append(fieldSql).append(tableSql).append(
							dominationSql).append(conditionSql).append(") ) A")
					.toString();
		}else{
			//组装SQL语句
			sql = new StringBuffer(fieldSql).append(tableSql).append(
					dominationSql).append(conditionSql).append(orderBySql)
					.toString();
			//组装总条数
			countSql = new StringBuffer("select count(*) totalcount")
					.append(tableSql).append(dominationSql).append(conditionSql)
					.toString();
		}
		List<LfMttaskVo> lfMttaskVoList =new ArrayList<LfMttaskVo>();
		//组装总条数
		if(StaticValue.DBTYPE == StaticValue.SQLSERVER_DBTYPE) {
			lfMttaskVoList=findPageVoListBySQLSqlServer(LfMttaskVo.class,sql,countSql,pageInfo,StaticValue.EMP_POOLNAME,null,null);
		}else{
			//调用 查询方法
			lfMttaskVoList = new DataAccessDriver().getGenericDAO().findPageVoListBySQL(
					LfMttaskVo.class, sql, countSql, pageInfo,
					StaticValue.EMP_POOLNAME);
		}
		//返回结果
		return lfMttaskVoList;
	}

	/**
	 * @param <T>
	 * @param voClass
	 * @param sql
	 * @param countSql
	 * @param pageInfo
	 * @param POOLNAME
	 * @param timeList
	 * @param columns
	 * @return returnList
	 * @throws Exception
	 */
	private <T> List<T> findPageVoListBySQLSqlServer(Class<T> voClass, String sql,
													 String countSql, PageInfo pageInfo, String POOLNAME,
													 List<String> timeList, Map<String, String> columns)
			throws Exception
	{
		List<T> returnList = null;
		if (columns == null)
		{
			//  获取实体类字段与数据库字段映射的map集合
			columns = getVoORMMap(voClass);
		}
		Connection conn = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		try
		{
			// 获取数据库连接
			conn = connectionManager.getDBConnection(POOLNAME);
			if (countSql != null)
			{
				EmpExecutionContext.sql("execute sql : " + countSql);
				ps = conn.prepareStatement(countSql);
				// 查询条件为时间
				if (timeList != null && timeList.size() > 0)
				{
					int psIndex = 0;
					for (int i = 0; i < timeList.size(); i++)
					{
						psIndex++;
						String time = timeList.get(i);
						ps.setTimestamp(psIndex, Timestamp.valueOf(time));
					}
				}
				// 执行SQL查询语句，返回ResultSet
				rs = ps.executeQuery();
				if (rs.next())
				{
					//当前页数
					int pageSize = pageInfo.getPageSize();
					//总记录数
					int totalCount = rs.getInt("totalcount");
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
				}
			}
			// 开始行数
			int beginCount = pageInfo.getPageSize() * pageInfo.getPageIndex()
					- (pageInfo.getPageSize() - 1);// 开始行数
			// 结束行数
			int endCount = pageInfo.getPageSize() * pageInfo.getPageIndex();// 结束行数
			StringBuffer sqlSb = new StringBuffer();
			if (sql.indexOf("distinct") == -1 || sql.indexOf("distinct") > 20)
			{
//				if(sql.toUpperCase().contains("UNION")){
//					//sqlunion查询分页查询，union前后列数不一致报错
//					sql = new StringBuffer("select top ").append(endCount).append(
//							" 0 as tempColumn,* from ( ").append(sql).append(") x ").toString();
//				}else{
				sql = sql.substring(sql.indexOf("select") + 7, sql.length());
				sql = new StringBuffer("select top ").append(endCount).append(
						" 0 as tempColumn,").append(sql).toString();
//				}
			} else
			{
//				if(sql.toUpperCase().contains("UNION")){
//					sql = new StringBuffer("select distinct top ").append(endCount)
//							.append(" 0 as tempColumn,* from ( ").append(sql).append(" ) x ").toString();
//				}else{
				sql = sql.substring(sql.indexOf("distinct") + 9, sql.length());
				sql = new StringBuffer("select distinct top ").append(endCount)
						.append(" 0 as tempColumn,").append(sql).toString();
//				}
			}
			sqlSb
					.append(
							"select * from (select row_number() over(order by tempColumn) tempRowNumber,* from (")
					.append(sql).append(
					") t) tt where tempRowNumber>=" + beginCount);
			sql = sqlSb.toString();
			EmpExecutionContext.sql("execute sql : " + sql);
			ps = conn.prepareStatement(sql);
			// 查询条件为时间
			if (timeList != null && timeList.size() > 0)
			{
				int psIndex = 0;
				for (int i = 0; i < timeList.size(); i++)
				{
					psIndex++;
					String time = timeList.get(i);
					ps.setTimestamp(psIndex, Timestamp.valueOf(time));
				}
			}
			//执行SQL
			rs = ps.executeQuery();
			// 将ResultSet转换成List
			returnList = rsToList(rs, voClass, columns);
		} catch (Exception e)
		{
			EmpExecutionContext.error(e,"DAO方法"+Thread.currentThread().getStackTrace()[1].getMethodName()+"执行失败！");
			throw e;
		} finally
		{
			// 关闭数据库资源。
			close(rs, ps, conn);
		}
		return returnList;
	}

	/**
	 * @description  非数据权限范围查找短信任务
	 * @param lfMttaskVo
	 * @param pageInfo
	 * @return lfMttaskVoList
	 * @throws Exception
	 */
	public List<LfMttaskVo> findLfMttaskVoWithoutDomination(
			LfMttaskVo lfMttaskVo, PageInfo pageInfo) throws Exception
	{
		//查询字段拼接
		String fieldSql = SerMtTaskSql.getFieldSql();
		//查询表拼接
		String tableSql = SerMtTaskSql.getTableSql(lfMttaskVo.getCorpCode());
        //根据机构组装下级机构
		if(lfMttaskVo.getDepIds()!=null&&!"".equals(lfMttaskVo.getDepIds()))
		{
			LfDep lfDep=new DataAccessDriver().getEmpDAO().findObjectByID(LfDep.class, Long.parseLong(lfMttaskVo.getDepIds()));
			if(lfDep!=null&&lfDep.getDepLevel().intValue()==1){
					lfMttaskVo.setDepIds("");
					lfMttaskVo.setCorpCode(lfDep.getCorpCode());
			}else{
				String depid=new DepDAO().getChildUserDepByParentID(Long.parseLong(lfMttaskVo.getDepIds()),TableLfDep.DEP_ID);
				lfMttaskVo.setDepIds(depid);
			}
		}
		//组装过滤条件
		String conditionSql = SerMtTaskSql.getConditionSql(lfMttaskVo);
		//排序条件拼接
		String orderBySql = SerMtTaskSql.getOrderBySql();
		String sql = new StringBuffer(fieldSql).append(tableSql)
				.append(conditionSql).append(orderBySql)
				.toString();
		//组装统计SQL语句
		String countSql = new StringBuffer("select count(*) totalcount")
				.append(tableSql).append(conditionSql)
				.toString();
		//调用查询语句
		List<LfMttaskVo> lfMttaskVoList = new DataAccessDriver().getGenericDAO().findPageVoListBySQL(
						LfMttaskVo.class, sql, countSql, pageInfo,
						StaticValue.EMP_POOLNAME);
		return lfMttaskVoList;
	}

	/**
	 * 获取短信发送任务
	 * @param lfMttaskVo
	 * @return
	 * @throws Exception
	 */
	public List<LfMttaskVo> findLfMttaskVoWithoutDomination(LfMttaskVo lfMttaskVo)throws Exception
	{
		//查询字段拼接
		String fieldSql = SerMtTaskSql.getFieldSql();
		//查询表拼接
		String tableSql = SerMtTaskSql.getTableSql(lfMttaskVo.getCorpCode());

		if(lfMttaskVo.getDepIds()!=null&&!"".equals(lfMttaskVo.getDepIds()))
		{
			LfDep lfDep=new DataAccessDriver().getEmpDAO().findObjectByID(LfDep.class, Long.parseLong(lfMttaskVo.getDepIds()));
			if(lfDep!=null&&lfDep.getDepLevel().intValue()==1){
					lfMttaskVo.setDepIds("");
					lfMttaskVo.setCorpCode(lfDep.getCorpCode());
			}else{
				String depid=new DepDAO().getChildUserDepByParentID(Long.parseLong(lfMttaskVo.getDepIds()),TableLfDep.DEP_ID);
				lfMttaskVo.setDepIds(depid);
			}
		}
		//组装过滤条件
		String conditionSql = SerMtTaskSql.getConditionSql(lfMttaskVo);
		//组装排序条件
		String orderBySql = SerMtTaskSql.getOrderBySql();
		//组装SQL语句
		String sql = new StringBuffer(fieldSql).append(tableSql)
				.append(conditionSql).append(orderBySql)
				.toString();
        //调用查询方法
		List<LfMttaskVo> lfMttaskVoList = findVoListBySQL(LfMttaskVo.class,
				sql, StaticValue.EMP_POOLNAME);

		return lfMttaskVoList;
	}
	
	public List<LfSysuser> findDomUserBySysuserIDOfSmsTaskRecordByDep(String sysuserID,String depId)
    throws Exception {
		//拼接sql
		StringBuffer domination = new StringBuffer("select ").append(
			TableLfDomination.DEP_ID).append(" from ").append(
					TableLfDomination.TABLE_NAME).append(" where ").append(
							TableLfDomination.USER_ID).append("=").append(sysuserID);
		StringBuffer dominationSql = new StringBuffer(" where (").append(
			TableLfSysuser.USER_ID).append("=").append(sysuserID).append(
			" or ").append(TableLfSysuser.DEP_ID).append(" in (").append(
					domination).append(")) and ").append(TableLfSysuser.USER_ID)
					.append("<>1 and ").append(TableLfSysuser.DEP_ID).append("=").append(depId);
		String sql = new StringBuffer("select * from ").append(
			TableLfSysuser.TABLE_NAME).append(dominationSql).toString();
		//排序条件拼接
		sql += " order by " + TableLfSysuser.NAME + " asc";
		List<LfSysuser> returnList = findEntityListBySQL(LfSysuser.class, sql,
			StaticValue.EMP_POOLNAME);
		//返回结果
		return returnList;
	}
	
	/**
	 * 查询下行实时记录
	 * @param conditionMap 查询条件
	 * @param pageInfo 分页信息对象
	 * @return 返回下行实时记录对象集合
	 */
	public List<SendedMttaskVo> findMtTaskReal(LinkedHashMap<String, String> conditionMap, PageInfo pageInfo)
	{
		try
		{
			//查询字段拼接
			String fieldSql = "select mt.UNICOM,mt.PHONE,mt.MESSAGE,mt.ERRORCODE,mt.PKNUMBER,mt.PKTOTAL ";
			
			//查询表拼接
			String tableSql = findMtTaskRealTableSql();
			//组装过滤条件
			String conditionSql = getMttaskConditionSql(conditionMap, "mt");
			//组装SQL语句
			String sql = new StringBuffer(fieldSql).append(tableSql).append(conditionSql).toString();
			
			List<SendedMttaskVo> mttaskList;
			//有分页
			if(pageInfo != null)
			{
				//组装统计语句
				//String countSql = new StringBuffer("select count(*) totalcount").append(tableSql).append(conditionSql).toString();
				SerMtTaskGenericDAO mtTaskDao = new SerMtTaskGenericDAO();
				//调用查询语句
				mttaskList = mtTaskDao.findPageVoListBySQL(SendedMttaskVo.class, sql, null, pageInfo, StaticValue.EMP_POOLNAME, null);
			}
			else
			{
				//调用查询方法
			    mttaskList = findVoListBySQL(SendedMttaskVo.class, sql, StaticValue.EMP_POOLNAME);
			}
			//返回结果集
			return mttaskList;
		}
		catch (Exception e)
		{
			EmpExecutionContext.error(e, "下行业务记录查询，实时查询sql查询异常。");
			return null;
		}
	}
	
	/**
	 * 查询下行实时记录
	 * @param conditionMap 查询条件
	 * @param pageInfo 分页信息对象
	 * @return 返回下行实时记录对象集合
	 */
	public boolean findMtTaskRealPageInfo(LinkedHashMap<String, String> conditionMap, PageInfo pageInfo)
	{
		try
		{
			//查询表拼接
			String tableSql = findMtTaskRealTableSql();
			//组装过滤条件
			String conditionSql = getMttaskConditionSql(conditionMap, "mt");
			
			//组装统计语句
			String countSql = new StringBuffer("select count(*) totalcount").append(tableSql).append(conditionSql).toString();
			return new SerMtTaskGenericDAO().findPageInfoBySQL(countSql, pageInfo, StaticValue.EMP_POOLNAME, null);
		}
		catch (Exception e)
		{
			EmpExecutionContext.error(e, "下行业务记录查询，实时查询sql查询分页信息，异常。");
			return false;
		}
	}
	
	/**
	 * 获取下行实时记录表名
	 * @param conditionMap 查询条件
	 * @param pageInfo 分页信息对象
	 * @return 返回下行实时记录对象集合
	 */
	private String findMtTaskRealTableSql()
	{
		try
		{
			String tableName = "gw_mt_task_bak";
			//查询表拼接
			String tableSql = new StringBuffer(" from ").append(tableName).append(" mt ").append(StaticValue.getWITHNOLOCK()).toString();
			
			//返回结果集
			return tableSql;
		}
		catch (Exception e)
		{
			EmpExecutionContext.error(e, "下行业务记录查询，实时查询sql获取表名，异常。");
			return null;
		}
	}
	
	/**
	 * 查询下行历史记录
	 * @param conditionMap 查询条件
	 * @param pageInfo 分页信息对象
	 * @param tableName 历史表名
	 * @return 返回下行实时记录对象集合
	 */
	public List<SendedMttaskVo> findMtTaskHis(LinkedHashMap<String, String> conditionMap, PageInfo pageInfo, String tableName)
	{
		try
		{
			//查询字段拼接
			String fieldSql = "select mt.UNICOM,mt.PHONE,mt.MESSAGE,mt.ERRORCODE,mt.PKNUMBER,mt.PKTOTAL ";
			//查询表拼接
			String tableSql = findMtTaskHisTable(tableName);
			//组装过滤条件
			String conditionSql = getMttaskConditionSql(conditionMap, "mt");
			//组装SQL语句
			String sql = new StringBuffer(fieldSql).append(tableSql).append(conditionSql).toString();
			
			List<SendedMttaskVo> mttaskList;
			//有分页
			if(pageInfo != null)
			{
				//组装统计语句
				//String countSql = new StringBuffer("select count(*) totalcount").append(tableSql).append(conditionSql).toString();
				SerMtTaskGenericDAO mtTaskDao = new SerMtTaskGenericDAO();
				//调用查询语句
				mttaskList = mtTaskDao.findPageVoListBySQL(SendedMttaskVo.class, sql, null, pageInfo, StaticValue.EMP_POOLNAME, null);
			}
			else
			{
				//调用查询方法
			    mttaskList = findVoListBySQL(SendedMttaskVo.class, sql, StaticValue.EMP_POOLNAME);
			}
			//返回结果集
			return mttaskList;
		}
		catch (Exception e)
		{
			EmpExecutionContext.error(e, "下行业务记录查询，历史查询sql查询异常。");
			return null;
		}
	}
	
	/**
	 * 查询下行历史记录
	 * @param conditionMap 查询条件
	 * @param pageInfo 分页信息对象
	 * @param tableName 历史表名
	 * @return 返回下行实时记录对象集合
	 */
	public List<SendedMttaskVo> findMtTaskHis(LinkedHashMap<String, String> conditionMap, PageInfo pageInfo, String tableName, boolean isBackDb, PageInfo realDbpageInfo)
	{
		try
		{
			//查询字段拼接
			String fieldSql = "select mt.UNICOM,mt.PHONE,mt.MESSAGE,mt.ERRORCODE,mt.PKNUMBER,mt.PKTOTAL ";
			//查询表拼接
			String tableSql = findMtTaskHisTable(tableName);
			//组装过滤条件
			String conditionSql = getMttaskConditionSql(conditionMap, "mt");
			//组装SQL语句
			String sql = new StringBuffer(fieldSql).append(tableSql).append(conditionSql).toString();
			
			//返回记录
		    List<SendedMttaskVo> mttaskList;
		    
		    SerMtTaskGenericDAO mtTaskDao = new SerMtTaskGenericDAO();
		    
		    String use_his_server = SystemGlobals.getValue("montnets.emp.use_history_server");
		    EmpExecutionContext.info("下行业务记录查询发送详情，tableName:"+tableName
		    +"，use_history_server:"+use_his_server);
		    
		    ///构造需要特殊查询的历史表名
			String hisTableName = "MTTASK" + StaticValue.getUseHistoryDBTime();
			//如果是配置日期的表且有开启历史库，特殊处理，实时库和历史库都要查
		    if(hisTableName.equals(tableName.toUpperCase()) && "1".equals(use_his_server))
		    {
		    	//有分页
				if(pageInfo != null)
				{
					//调用查询实时库数据
					mttaskList = mtTaskDao.findPageVoListBySQL(SendedMttaskVo.class, sql, null, pageInfo, StaticValue.EMP_POOLNAME, null);
				}
				//没分页
				else
				{
					//调用查询实时库数据
				    mttaskList = findVoListBySQL(SendedMttaskVo.class, sql, StaticValue.EMP_POOLNAME);
				}
				
	    		//如果实时库有数据
	    		if(mttaskList != null && mttaskList.size() > 0)
	    		{
	    			//返回实时库查询结果
	    			return mttaskList;
	    		}
	    		//实时库无数据，则查历史库
	    		//有分页
				if(pageInfo != null)
				{
	    			PageInfo pageInfoTemp = new PageInfo();
	    			pageInfoTemp.setPageSize(pageInfo.getPageSize());
	    			pageInfoTemp.setPageIndex(pageInfo.getPageIndex() - realDbpageInfo.getTotalPage());
	    			//返回历史库查询结果
	    			mttaskList = mtTaskDao.findPageVoListBySQL(SendedMttaskVo.class, sql, null, pageInfoTemp, StaticValue.EMP_BACKUP, null);
				}
				//没分页
				else
				{
					//查询历史库
				    mttaskList = findVoListBySQL(SendedMttaskVo.class, sql, StaticValue.EMP_BACKUP);
				}
				return mttaskList;
		    }
		    
		    //连接池名称
	    	String poolName = StaticValue.EMP_POOLNAME;
	    	//查询历史库
	    	if(isBackDb)
	    	{
	    		poolName = StaticValue.EMP_BACKUP;
	    	}
	    	
	    	//有分页
			if(pageInfo != null)
			{
				//调用查询语句
				mttaskList = mtTaskDao.findPageVoListBySQL(SendedMttaskVo.class, sql, null, pageInfo, poolName, null);
			}
			else
			{
				//调用查询方法
			    mttaskList = findVoListBySQL(SendedMttaskVo.class, sql, poolName);
			}
	    	//返回结果集
	    	return mttaskList;
			
			/*List<SendedMttaskVo> mttaskList;
			//有分页
			if(pageInfo != null)
			{
				//组装统计语句
				//String countSql = new StringBuffer("select count(*) totalcount").append(tableSql).append(conditionSql).toString();
				SerMtTaskGenericDAO mtTaskDao = new SerMtTaskGenericDAO();
				//调用查询语句
				mttaskList = mtTaskDao.findPageVoListBySQL(SendedMttaskVo.class, sql, null, pageInfo, StaticValue.EMP_POOLNAME, null);
			}
			else
			{
				//调用查询方法
			    mttaskList = findVoListBySQL(SendedMttaskVo.class, sql, StaticValue.EMP_POOLNAME);
			}
			//返回结果集
			return mttaskList;*/
		}
		catch (Exception e)
		{
			EmpExecutionContext.error(e, "下行业务记录查询，历史查询sql查询异常。");
			return null;
		}
	}
	
	/**
	 * 查询下行历史记录分页信息
	 * @param conditionMap 查询条件
	 * @param pageInfo 分页信息对象
	 * @param tableName 历史表名
	 * @return 返回下行实时记录对象集合
	 */
	public boolean findMtTaskHisPageInfo(LinkedHashMap<String, String> conditionMap, PageInfo pageInfo, String tableName)
	{
		try
		{
			//查询表拼接
			String tableSql = findMtTaskHisTable(tableName);
			//组装过滤条件
			String conditionSql = getMttaskConditionSql(conditionMap, "mt");
			
			//组装统计语句
			String countSql = new StringBuffer("select count(*) totalcount").append(tableSql).append(conditionSql).toString();
			return new SerMtTaskGenericDAO().findPageInfoBySQL(countSql, pageInfo, StaticValue.EMP_POOLNAME, null);
		}
		catch (Exception e)
		{
			EmpExecutionContext.error(e, "下行业务记录查询，历史查询sql查询异常。");
			return false;
		}
	}
	
	/**
	 * 查询下行历史记录分页信息
	 * @param conditionMap 查询条件
	 * @param pageInfo 分页信息对象
	 * @param tableName 历史表名
	 * @return 返回下行实时记录对象集合
	 */
	public boolean findMtTaskHisPageInfo(LinkedHashMap<String, String> conditionMap, PageInfo pageInfo, String tableName, boolean isBackDb, PageInfo realDbpageInfo)
	{
		try
		{
			//查询表拼接
			String tableSql = findMtTaskHisTable(tableName);
			//组装过滤条件
			String conditionSql = getMttaskConditionSql(conditionMap, "mt");
			
			//组装统计语句
			String countSql = new StringBuffer("select count(*) totalcount").append(tableSql).append(conditionSql).toString();
			
			String use_his_server = SystemGlobals.getValue("montnets.emp.use_history_server");
		    EmpExecutionContext.info("下行业务记录查询发送详情，tableName:"+tableName
		    +"，use_history_server:"+use_his_server);
			
			SerMtTaskGenericDAO mtTaskDao = new SerMtTaskGenericDAO();
			
			//构造需要特殊查询的历史表名
			String hisTableName = "MTTASK" + StaticValue.getUseHistoryDBTime();
			//如果是配置日期的表且有开启历史库，特殊处理，实时库和历史库都要查
		    if(hisTableName.equals(tableName.toUpperCase()) && "1".equals(use_his_server))
		    {
				//调用查询实时库数据
		    	boolean res = mtTaskDao.findPageInfoBySQL(countSql, pageInfo, StaticValue.EMP_POOLNAME, null);
		    	
		    	realDbpageInfo.setTotalPage(pageInfo.getTotalPage());
	    		realDbpageInfo.setTotalRec(pageInfo.getTotalRec());
		    	
		    	//获取历史库的总数
		    	PageInfo pageInfoTemp = new PageInfo();
		    	pageInfoTemp.setPageSize(pageInfo.getPageSize());
		    	//调用查询历史库数据
		    	res = mtTaskDao.findPageInfoBySQL(countSql, pageInfoTemp, StaticValue.EMP_BACKUP, null);
		    	//历史库无数据，不累加总记录数和总页数
		    	if(pageInfoTemp.getTotalRec() > 0)
		    	{
		    		//实时库的查询没记录，则不需要累加页数，因为默认没记录时，页数为1
		    		if(pageInfo.getTotalRec() == 0)
		    		{
		    			//实时库没记录，则直接使用历史库的页数
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
		    	return res;
		    }
		    
		    //连接池名称
	    	String poolName = StaticValue.EMP_POOLNAME;
	    	//查询历史库
	    	if(isBackDb && "1".equals(use_his_server))
	    	{
	    		poolName = StaticValue.EMP_BACKUP;
	    	}
			
			return mtTaskDao.findPageInfoBySQL(countSql, pageInfo, poolName, null);
		}
		catch (Exception e)
		{
			EmpExecutionContext.error(e, "下行业务记录查询，历史查询sql查询异常。");
			return false;
		}
	}
	
	/**
	 * 获取下行历史表名
	 * @param conditionMap 查询条件
	 * @param pageInfo 分页信息对象
	 * @param tableName 历史表名
	 * @return 返回下行实时记录对象集合
	 */
	private String findMtTaskHisTable(String tableName)
	{
		try
		{
			//查询表拼接
			String tableSql = new StringBuffer(" from ").append(tableName).append(" mt ").append(StaticValue.getWITHNOLOCK()).toString();
			return tableSql;
		}
		catch (Exception e)
		{
			EmpExecutionContext.error(e, "下行业务记录查询，历史查询sql获取表名，异常。");
			return null;
		}
	}
	
	/**
	 * 查询下行业务记录，历史和实时查询
	 * @param conditionMap 查询条件
	 * @param tableName 历史表名
	 * @param pageInfo 分页信息对象
	 * @return 返回下行记录对象集合
	 */
	public List<SendedMttaskVo> findMtTaskHisAndReal(LinkedHashMap<String, String> conditionMap, PageInfo pageInfo, String tableName)
	{
		try
		{
			//查询字段拼接
			String fieldSql = "select mt.UNICOM,mt.PHONE,mt.MESSAGE,mt.ERRORCODE,mt.PKNUMBER,mt.PKTOTAL,mt.TASKID ";
			
			StringBuffer tableSql = findMtTaskHisAndRealTable(conditionMap, tableName);
			
			//组装SQL语句
			String sql = new StringBuffer(fieldSql).append(" from (").append(tableSql).append(") mt").toString();
			
			List<SendedMttaskVo> mttaskList;
			//要分页
			if(pageInfo != null)
			{
				//组装统计语句
			    //String countSql = new StringBuffer("select count(*) totalcount from (").append(tableSql).append(") mt").toString();
			    SerMtTaskGenericDAO mtTaskDao = new SerMtTaskGenericDAO();
			    //调用查询方法 
			    mttaskList = mtTaskDao.findPageVoListBySQL(SendedMttaskVo.class, sql, null, pageInfo, StaticValue.EMP_POOLNAME, null);
			}
			else
			{
				//调用查询方法 
				mttaskList = findVoListBySQL(SendedMttaskVo.class, sql, StaticValue.EMP_POOLNAME);
			}
			return mttaskList;
		}
		catch (Exception e)
		{
			EmpExecutionContext.error(e, "查询下行业务记录，历史和实时查询异常。");
			return null;
		}
	}
	
	/**
	 * 查询下行业务记录，历史和实时查询分页信息
	 * @param conditionMap 查询条件
	 * @param tableName 历史表名
	 * @param pageInfo 分页信息对象
	 * @return 返回下行记录对象集合
	 */
	public boolean findMtTaskHisAndRealPageInfo(LinkedHashMap<String, String> conditionMap, PageInfo pageInfo, String tableName)
	{
		try
		{
			StringBuffer tableSql = findMtTaskHisAndRealTable(conditionMap, tableName);
			
			//组装统计语句
		    String countSql = new StringBuffer("select count(*) totalcount from (").append(tableSql).append(") mt").toString();
		    return new SerMtTaskGenericDAO().findPageInfoBySQL(countSql, pageInfo, StaticValue.EMP_POOLNAME, null);
		}
		catch (Exception e)
		{
			EmpExecutionContext.error(e, "查询下行业务记录，历史和实时sql查询分页信息，异常。");
			return false;
		}
	}
	
	/**
	 * 获取历史和实时查询表名
	 * @param conditionMap
	 * @param tableName
	 * @return
	 */
	private StringBuffer findMtTaskHisAndRealTable(LinkedHashMap<String, String> conditionMap, String tableName)
	{
		try
		{
			//实时记录表名
			String realTableName = "gw_mt_task_bak";;
			
			//查询历史表sql
			StringBuffer sqlHis = new StringBuffer("select ms.UNICOM,ms.PHONE,ms.TASKID,ms.MESSAGE,ms.ERRORCODE,ms.PKNUMBER,ms.PKTOTAL")
			   .append(" from ").append(tableName).append(" ms ").append(StaticValue.getWITHNOLOCK())
			   .append(getMttaskConditionSql(conditionMap,"ms"));
			
			//查询实时表sql
			StringBuffer sqlReal = new StringBuffer("select ml.UNICOM,ml.PHONE,ml.TASKID,ml.MESSAGE,ml.ERRORCODE,ml.PKNUMBER,ml.PKTOTAL")
			   .append(" from ").append(realTableName).append(" ml ").append(StaticValue.getWITHNOLOCK())
			   .append(getMttaskConditionSql(conditionMap,"ml"));
			
			StringBuffer tableSql = new StringBuffer().append(sqlHis).append(" union all ").append(sqlReal);
			
			return tableSql;
		}
		catch (Exception e)
		{
			EmpExecutionContext.error(e, "查询下行业务记录，获取历史和实时查询表名，异常。");
			return null;
		}
	}
	
	/**
	 * 组装过滤条件
	 * @param conditionMap 查询条件
	 * @return 返回sql，包含where
	 */
	private String getMttaskConditionSql(LinkedHashMap<String, String> conditionMap, String tableName)
	{
		StringBuffer conditionSql = new StringBuffer();
		if(conditionMap.get("taskid") != null && conditionMap.get("taskid").length() > 0)
		{
			conditionSql.append(" where ").append(tableName).append(".TASKID").append("=").append(conditionMap.get("taskid"));
		}	
		else
		{
			conditionSql.append(" where 1=2");
		}
		
		//错误编码查询类型
		String type = conditionMap.get("type");
		//接收失败
        if(type.equals("2"))
        {
        	conditionSql
        	.append(" and ").append(tableName).append(".ERRORCODE not like 'E1:%' ")
			.append(" and ").append(tableName).append(".ERRORCODE not like 'E2:%' ")
			.append(" and ").append(tableName).append(".ERRORCODE not in ('DELIVRD' ,'0      ','       ')");
        }
        //提交失败
        else if(type.equals("3"))
        {
        	conditionSql.append(" and (").append(tableName).append(".ERRORCODE like 'E1:%' or ")
			.append(tableName).append(".ERRORCODE like 'E2:%' )");
        }
        //发送成功
        else if(type.equals("5"))
        {
        	//发送成功
			conditionSql.append(" and ").append(tableName).append(".ERRORCODE in ('DELIVRD' ,'0      ') ");
        }
        //状态未返
        else if(type.equals("6"))
        {
        	//状态未返
			conditionSql.append(" and ").append(tableName).append(".ERRORCODE = '       ' ");
        }
		
		if(conditionMap.get("phone") != null && conditionMap.get("phone").length() > 0)
		{
			conditionSql.append(" and ").append(tableName).append(".PHONE = '").append(conditionMap.get("phone")).append("'");
		}
		if(conditionMap.get("unicom") != null && conditionMap.get("unicom").length() > 0)
		{
			conditionSql.append(" and ").append(tableName).append(".UNICOM=").append(conditionMap.get("unicom"));
		}
		return conditionSql.toString();
	}
	
	/**
	 * 根据操作员id获取管辖机构的顶级机构对象集合
	 * @param sysuserID 操作员id
	 * @param corpCode 操作员所属企业
	 * @return 返回操作员管辖机构的顶级机构对象集合
	 */
	public List<LfDep> findTopDepByUserId(String sysuserID, String corpCode)
	{
		try
		{
			//拼接sql
			StringBuffer sql = new StringBuffer();
			sql.append("select dep.* from LF_DEP dep inner join LF_DOMINATION domination on dep.DEP_ID=domination.DEP_ID")
				.append(" where domination.USER_ID=").append(sysuserID)
				.append(" and dep.DEP_STATE=1")
				.append(" and dep.CORP_CODE='").append(corpCode).append("'")
				.append(" order by dep.DEP_ID asc");
			
			//只拿第一条顶级机构
			PageInfo pageInfo = new PageInfo();
			pageInfo.setPageSize(1);
			//返回结果
			return new DataAccessDriver().getGenericDAO().findPageEntityListBySQLNoCount(LfDep.class, sql.toString(), null, pageInfo, StaticValue.EMP_POOLNAME);
		}
		catch (Exception e)
		{
			EmpExecutionContext.error(e, "下行业务记录DAO，根据操作员id获取管辖机构的顶级机构对象集合，异常。sysuserID="+sysuserID+",corpCode="+corpCode);
			return null;
		}
	}
	
	
	
	
	
	
	
	
	
	
}
