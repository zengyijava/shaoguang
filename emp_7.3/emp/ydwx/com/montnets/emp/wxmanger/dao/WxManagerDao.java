package com.montnets.emp.wxmanger.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.sql.Types;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.beanutils.DynaBean;

import com.montnets.emp.common.biz.BaseBiz;
import com.montnets.emp.common.constant.StaticValue;
import com.montnets.emp.common.constant.SystemGlobals;
import com.montnets.emp.common.context.EmpExecutionContext;
import com.montnets.emp.common.dao.SuperDAO;
import com.montnets.emp.common.dao.impl.DataAccessDriver;
import com.montnets.emp.netnews.biz.TrustDataBiz;
import com.montnets.emp.netnews.entity.LfWXBASEINFO;
import com.montnets.emp.netnews.entity.LfWXData;
import com.montnets.emp.netnews.table.TableLfWXBASEINFO;
import com.montnets.emp.netnews.table.TableLfWXData;
import com.montnets.emp.netnews.table.TableLfWXDataBind;
import com.montnets.emp.netnews.table.TableLfWXPAGE;
import com.montnets.emp.table.sysuser.TableLfDomination;
import com.montnets.emp.table.sysuser.TableLfSysuser;
import com.montnets.emp.util.PageInfo;

public class WxManagerDao extends SuperDAO {
	
	
	/**
	 * 根据网讯ID 查询 网讯有效时间
	 * 
	 * @param pageid页面ID
	 * @return 返回数组，0：timeout,1:netid
	 */
	public String[] getPdByTimeOut(int pageid) {
		
		//返回结果
		String[] resultArray = new String[2];
		
		try 
		{
			String sql = "select netid, timeout from LF_WX_BASEINFO where netid =(select netid from lf_wx_page where id="+pageid+" )";
			
			List<DynaBean> resultList = new DataAccessDriver().getGenericDAO().findDynaBeanBySql(sql);
			if(resultList == null || resultList.size() == 0)
			{
				return null;
			}
			resultArray[0] = resultList.get(0).get("timeout").toString();
			resultArray[1] = resultList.get(0).get("netid").toString();
			
		} 
		catch (Exception e) 
		{
			//异常处理
			EmpExecutionContext.error(e,"根据网讯ID查询 网讯有效时间");
		}
		
		//返回结果
		return resultArray;
	}
	
	/***
	 * 获得网讯基本信息集合
	 */
	public List<DynaBean> getBaseInfos(LinkedHashMap<String, String> conditionMap,PageInfo pageInfo,String userId)
	{
		List<DynaBean> beans = null;
		String fieldSql ="select a.* ,c.name as creatname,c.user_name as creatcode,corp.corp_name corp_name";
		
		String tableSql = " From LF_WX_BASEINFO a "+
							"left join lf_sysuser c on a.creatid=c.USER_ID left join lf_corp corp on a.corp_code=corp.corp_code";
		String dominationSql=null;
		if(userId==null){
			// 无需审核或者审核通过 
			dominationSql =" where (a.status=2 or a.status=4)";
		}
		else{
			dominationSql = getDominationSql(userId);
			}
		String conditionSql=getConditionSql(conditionMap);
		String orderbySql = " order by a.NETID DESC";
		String sql=fieldSql+tableSql+dominationSql+conditionSql+orderbySql;
		String countSql = new StringBuffer("select count(*) totalcount ").append(tableSql).append(dominationSql).append(conditionSql).toString(); 
		List<String> timeList =getTimeCondition(conditionMap);
		beans = new DataAccessDriver().getGenericDAO().findPageDynaBeanBySQLNoCount(sql, countSql, pageInfo, StaticValue.EMP_POOLNAME, timeList);
		return beans;
	}

	/** 
	 * 
	* @Description: 获得查询集合
	* @param @param conditionMap  条件对象
	* @param @return 
	* @return List<String>
	 */
	private List<String> getTimeCondition(LinkedHashMap<String, String> conditionMap)
	{
		List<String> timeList = new ArrayList<String>();
		if(conditionMap.get("startdate")!=null)
		{
			timeList.add(conditionMap.get("startdate"));
		}
		if(conditionMap.get("enddate")!=null)
		{
			timeList.add(conditionMap.get("enddate"));
		}
		return timeList;
	}
	
	
	//获取权限
	public  String getDominationSql(String userId)
	{
		StringBuffer dominationSql = new StringBuffer("select ").append(
				TableLfDomination.DEP_ID).append(" from ").append(
				TableLfDomination.TABLE_NAME).append(" where ").append(
				TableLfDomination.USER_ID).append("=").append(userId);
		String sql = new StringBuffer(" where (c.").append(
				TableLfSysuser.USER_ID).append("=").append(userId).append(
				" or c.").append(TableLfSysuser.DEP_ID).append(" in (")
				.append(dominationSql).append(")")
				.append(")").toString();
		return sql;
	}
	
	/***
	 * 拼装SQL查询条件
	 */
	public String getConditionSql(LinkedHashMap<String, String> conditionMap)
	{
		String buffer=new String(" and wxTYPE in (0,1) ");
		if(conditionMap.get("corpcode")!=null)
		{
			buffer = buffer+" and a.corp_code = '"+conditionMap.get("corpcode")+"' ";
		}

		if(conditionMap.get("wxid")!=null)
		{
			buffer = buffer+" and a.NETID ="+conditionMap.get("wxid")+"";
		}
		
		if(conditionMap.get("wxname")!=null)
		{
			buffer = buffer+" and a.name like '%"+conditionMap.get("wxname")+"%'";
		}
		
		if(conditionMap.get("corpname")!=null)
		{
			buffer = buffer+" and corp.corp_name like '%"+conditionMap.get("corpname")+"%'";
		}
		
		if(conditionMap.get("status")!=null)
		{
			if(!"".equals(conditionMap.get("status")))
			{
				buffer = buffer+" and status="+conditionMap.get("status");
			}
		}
		
		if(conditionMap.get("operState")!=null)
		{
			if("0".equals(conditionMap.get("operState"))){
				buffer = buffer+" and (OPERAPPSTATUS is null or OPERAPPSTATUS=0)";	
			}else{
				buffer = buffer+" and OPERAPPSTATUS="+conditionMap.get("operState");
			}
		}
		//添加运营商审批条件
		if(conditionMap.get("operStatus")!=null)
		{
			if(!"".equals(conditionMap.get("operStatus")))
			{
				if("0".equals(conditionMap.get("operStatus"))){
					buffer = buffer+" and (OPERAPPSTATUS is null or OPERAPPSTATUS=0)";	
				}else{
					buffer = buffer+" and OPERAPPSTATUS="+conditionMap.get("operStatus");
				}
			}
		}
		if(conditionMap.get("temptype")!=null)
		{
			if(!"".equals(conditionMap.get("temptype")))
			{
				buffer = buffer+" and TEMPTYPE="+conditionMap.get("temptype");
			}
		}
		if(conditionMap.get("startdate")!=null)
		{
			buffer = buffer+" and a.CREATDATE >=?";
		}
		if(conditionMap.get("enddate")!=null)
		{
			buffer = buffer+" and a.CREATDATE <=?";
		}
        if(conditionMap.get("flowId")!=null)
        {
            buffer = buffer+" and a.netid in ( SELECT DISTINCT MT_ID FROM LF_FLOWRECORD WHERE F_ID = "+conditionMap.get("flowId")+" and INFO_TYPE = 6 ) ";
        }
		return buffer;
	}
	
	/**
	 * 按条件查询问卷管理记录
	 * @param userId 当前登录操作员id，用于权限查询
	 * @param conditionMap 查询条件map
	 * @param pageInfo 分页对象
	 * @return 返回记录动态bean集合
	 */
	public List<DynaBean> getSurvey(String userId, LinkedHashMap<String, String> conditionMap, PageInfo pageInfo) {
		
		List<DynaBean> resultList = null;
		
		try 
		{
			
			//查询字段
			StringBuffer fieldSql = new StringBuffer(" select baseinfo.*, sysuser.user_Name, sysuser.name as uname, dep.dep_name ");

			//查询表
			StringBuffer tableSql = new StringBuffer(" from LF_WX_BASEINFO baseinfo left join lf_sysuser sysuser on baseinfo.creatid=sysuser.user_Id ")
									.append(" left join lf_dep dep on sysuser.dep_id=dep.dep_id where  ")
									.append("  baseinfo.wxtype=2  ");
			
			//权限
			String dominationSql = getDomSql(userId);
			
			//查询条件
			String conditionSql=this.getSurveyCondition(conditionMap);
			
			//排序
			String orderbySql = " order by baseinfo.id desc ";
			
			//查询语句
			String sql = fieldSql.append(tableSql).append(dominationSql).append(conditionSql).append(orderbySql).toString();
			
			//分页语句
			String countSql = new StringBuffer(" select count(*) totalcount ").append(tableSql).append(dominationSql).append(conditionSql).toString();
			
			//执行查询，获取结果记录
			resultList = new DataAccessDriver().getGenericDAO().findPageDynaBeanBySQL(sql, countSql, pageInfo, StaticValue.EMP_POOLNAME, null);
			
		} 
		catch (Exception e) 
		{
			//异常处理
			EmpExecutionContext.error(e,"DAO查询问卷管理异常：");
			return null;
		}
		
		//返回结果
		return resultList;
	}
	
	/**
	 * 获取权限sql
	 * @param loginUserID 当前登录操作员id
	 * @return 返回权限sql
	 */
	private String getDomSql(String loginUserID)
	{
		if (loginUserID == null || "".equals(loginUserID.trim()))
		{
			return "";
		}
		
		String domDepSql = new StringBuffer(" select ").append(
				TableLfDomination.DEP_ID).append(" from ").append(
				TableLfDomination.TABLE_NAME).append(StaticValue.getWITHNOLOCK()).append(" where ").append(
				TableLfDomination.USER_ID).append("=").append(loginUserID)
				.toString();
		String sysuserSql = new StringBuffer(" select ").append(
				TableLfSysuser.USER_ID).append(" from ").append(
				TableLfSysuser.TABLE_NAME).append(StaticValue.getWITHNOLOCK()).append(" where ").append(
				TableLfSysuser.DEP_ID).append(" in (").append(domDepSql)
				.append(")").toString();
		String dom = new StringBuffer(" and (").append(" baseinfo.creatid ").append(" in (").append(sysuserSql)
				.append(") or ").append(" baseinfo.creatid ").append("=")
				.append(loginUserID).append(")").toString();

		return dom;
		
	}
	
	/**
	 * 获取问卷管理查询条件sql
	 * @param conditionMap 条件值
	 * @param tableName 别名
	 * @return
	 */
	private String getSurveyCondition(LinkedHashMap<String, String> conditionMap)
	{
		StringBuffer sbSql = new StringBuffer();
		try
		{
			if(conditionMap == null || conditionMap.size() == 0)
			{
				return "";
			}
			if(conditionMap.get("name") != null && !"".equals(conditionMap.get("name")))
			{
				String name = conditionMap.get("name");
				//问卷名称
				sbSql.append(" and baseinfo.name like '%").append(name).append("%' ");
			}
			if(conditionMap.get("creatid") != null && !"".equals(conditionMap.get("creatid")))
			{
				String creatid = conditionMap.get("creatid");
				//问卷名称
				sbSql.append(" and baseinfo.creatid = ").append(creatid).append(" ");
			}
			
			//创建时间起始
			if (conditionMap.get("creatdateBegin") != null && !"".equals(conditionMap.get("creatdateBegin").trim()))
			{
				String creatdateBegin = conditionMap.get("creatdateBegin");
				String startTime = new DataAccessDriver().getGenericDAO().getTimeCondition(creatdateBegin);
				sbSql.append(" and baseinfo.creatdate ").append(" >= ").append(startTime);
			}
			
			//创建时间结束时间
			if (conditionMap.get("creatdateEnd") != null && !"".equals(conditionMap.get("creatdateEnd").trim()))
			{
				String creatdateEnd = conditionMap.get("creatdateEnd");
				String endTime = new DataAccessDriver().getGenericDAO().getTimeCondition(creatdateEnd);
				sbSql.append(" and baseinfo.creatdate ").append(" <= ").append(endTime);
			}
		}
		catch(Exception e)
		{
			EmpExecutionContext.error(e,"获取问卷管理查询条件sql");
		}
		return sbSql.toString();
	}
	
	/**
	 * 通过pageId获取网讯信息
	 * @param pageId
	 * @return
	 * @throws Exception
	 */
	public LfWXBASEINFO findNetInfoByPageId(String pageId)
	{
		if(pageId == null || pageId.trim().length() == 0){
			return null;
		}
		try{
			//sql
			StringBuffer sqlBuffer = new StringBuffer()
							.append(" select ").append("*").append(" from ").append(TableLfWXBASEINFO.TABLE_NAME).append(StaticValue.getWITHNOLOCK())
							.append(" where ").append(TableLfWXBASEINFO.NETID).append(" = ")
							.append("(select ").append(TableLfWXPAGE.NETID).append(" from ").append(TableLfWXPAGE.TABLE_NAME).append(StaticValue.getWITHNOLOCK())
							.append(" where ").append(TableLfWXPAGE.ID).append("=").append(pageId).append(") ");
			
			List<LfWXBASEINFO> basesList = findPartEntityListBySQL(LfWXBASEINFO.class, sqlBuffer.toString(), StaticValue.EMP_POOLNAME);
			if(basesList == null || basesList.size() ==0)
			{
				return null;
			}
			//返回结果
			return basesList.get(0);
		}catch(Exception e)
		{
			EmpExecutionContext.error(e,"通过pageId获取网讯信息");
			return null;
		}
		
	}
	
	/**
	 * 通过netId获取网讯绑定的互动数据
	 * @param netId
	 * @return
	 * @throws Exception
	 */
	public List<LfWXData> findDataByNetId(String netId)
	{
		try{
			//sql
			StringBuffer sqlBuffer = new StringBuffer()
							.append(" select ").append("*").append(" from ").append(TableLfWXData.TABLE_NAME).append(StaticValue.getWITHNOLOCK())
							.append(" where ").append(TableLfWXData.DID).append(" in ")
							.append("(select ").append(TableLfWXDataBind.DID).append(" from ").append(TableLfWXDataBind.TABLE_NAME).append(StaticValue.getWITHNOLOCK())
							.append(" where ").append(TableLfWXDataBind.NETID).append("=").append(netId).append(") ");
			
			List<LfWXData> datasList = findPartEntityListBySQL(LfWXData.class, sqlBuffer.toString(), StaticValue.EMP_POOLNAME);
			
			//返回结果
			return datasList;
		}catch(Exception e)
		{
			EmpExecutionContext.error(e,"通过netId获取网讯绑定的互动数据");
			return null;
		}
		
	}
	
	/**
	 * 插入互动数据到表
	 * @param netId 网讯模板id
	 * @param pageId 网讯页面id
	 * @param phone 访问手机号
	 * @param answerMap 用户回复内容，key为互动问题的name，value为选择的答案数组
	 * @param tableName 表名
	 * @return 成功返回true
	 */
	public boolean insertData(String netId, String pageId, String phone, Map<String,String[]> answerMap, String tableName, String taskId)
	{
		if(answerMap == null || answerMap.size() == 0 || tableName == null || tableName.trim().length()==0){
			return false;
		}
		//获取所有的互动问题的name
		Set<String> keySet = answerMap.keySet();
		String sql = null;
		Map<String,Integer> keyIndex = new HashMap<String,Integer>();
		try{
			String keySql = "";
			String wenhao = "?,?,?,?,?,?";
			int index = 6;
			
			//循环出互动问题的name，即列名，构造sql语句
			for(String key:keySet){
				index++;
				keyIndex.put(key, index);
				//遍历key
			    keySql += "," + key;
			    wenhao += ",?";
			}
			//sql
			StringBuffer sqlBuffer = new StringBuffer()
							.append(" insert into  ").append(tableName).append("(NETID,PAGEID,PHONE,ISSIMPLE,DATE_TIME,TASKID ").append(keySql).append(") ")
							.append(" values (").append(wenhao).append(") ");
			sql = sqlBuffer.toString();
			
		}catch(Exception e)
		{
			EmpExecutionContext.error(e,"插入互动数据到表");
			return false;
		}
		
		Connection conn = null;
		PreparedStatement ps = null;
		try{
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			//获取数据库连接
			conn = connectionManager.getDBConnection(StaticValue.EMP_POOLNAME);
			conn.setAutoCommit(false);
			
			EmpExecutionContext.sql("execute sql : " + sql);
			ps = conn.prepareStatement(sql);
			
			String[] answerArray = null;
			//列对应的索引
			Set<String> IndexKeySet = keyIndex.keySet();
			//循环拼sql
			for(String key:keySet)
			{
				//问题列的答案数组
				answerArray = answerMap.get(key);
				if(answerArray == null || answerArray.length == 0){
					continue;
				}
				//循环答案数组，预处理sql
				for(int i = 0;i<answerArray.length;i++)
				{
					ps.setInt(1, Integer.valueOf(netId));
					ps.setInt(2, Integer.valueOf(pageId));
					ps.setString(3, phone);
					if(i==0){
						ps.setInt(4, 1);
					}else{
						ps.setInt(4, 2);
					}
					ps.setTimestamp(5, Timestamp.valueOf(sdf.format(new Date())));
					//taskid
					ps.setLong(6, Long.valueOf(taskId));
					
					for(String indexKey:IndexKeySet){
						//答案对应该问题列的，则设值
						if(key == indexKey){
							ps.setString(keyIndex.get(key), answerArray[i]);
							continue;
						}
						ps.setNull(keyIndex.get(indexKey), Types.VARCHAR);
						//ps.setArray(keyIndex.get(indexKey), null);
					}
					
					ps.addBatch();
				}
			}
			ps.executeBatch();
			conn.commit();
			return true;
		}catch (Exception e)
		{
			//回滚
			try
			{
				conn.rollback();
			} catch (SQLException e1)
			{
				EmpExecutionContext.error(e1,"数据回滚异常");
			}
			EmpExecutionContext.error(e,"数据库处理异常");
			return false;
		} finally
		{
			try
			{
				conn.setAutoCommit(true);
				//关闭数据库资源
				super.close(null, ps, conn);
			} catch (Exception e)
			{
				EmpExecutionContext.error(e,"数据库关闭异常");
			}
			
		}
		
	}

	/**
	 * 获取动态数据
	 * @param netId 
	 * @param taskId
	 * @param phone
	 * @param tableName
	 * @param params
	 * @return
	 */
	public Map<Integer,String> getDynDataInfo(String netId, String taskId, String phone, String tableName, String params)
	{
		try
		{
			Map<Integer,String> infosMap = new HashMap<Integer,String>();
			String sql = "select * from "+tableName+" where NETID="+netId+" and taskId="+taskId+" and phone='"+phone+"'";
			List<DynaBean> infosList =  new DataAccessDriver().getGenericDAO().findDynaBeanBySql(sql);
			if(infosList == null || infosList.size() == 0){
				return new HashMap<Integer,String>();
			}
			
			DynaBean bean = infosList.get(0);
			String[] paramsArray = params.split(",");
			Object value = null;
			for(int i=0;i<paramsArray.length;i++){
				value = bean.get("p"+paramsArray[i]);
				if(value == null){
					infosMap.put(Integer.valueOf(paramsArray[i]), "");
					continue;
				}
				infosMap.put(Integer.valueOf(paramsArray[i]), String.valueOf(value));
			}
			return infosMap;
		}catch(Exception e){
			EmpExecutionContext.error(e,"获取动态数据");
			return new HashMap<Integer,String>();
		}
	}
	
	//判断是否存在动态数据库，如果不存在就插入
	public void setDyntable(String tableName,String params){
		String sql ="";
		String type=SystemGlobals.getValue("DBType"); 
		
		//sqlserver 不同数据库进行处理
		if("2".equals(type)){
		 sql = "select count(*) as result from sysobjects where id = object_id('"+tableName+"')";
		}else if("1".equals(type)){//oracle 
		 sql=" select count(*)  result from user_tables where table_name = '"+tableName+"' ";
		}else if("4".equals(type)){//DB2 
			sql=" select COUNT (*)  from Sysibm.tables where table_name = '"+tableName+"' ";
		}
		List<DynaBean> infosList =  new DataAccessDriver().getGenericDAO().findDynaBeanBySql(sql);
		if(infosList.size()>0){
			DynaBean bean = infosList.get(0);
			Object value=bean.get("result");
			if("0".equals(value+"")){
				String[] data=params.split(",");
				HashSet set =new HashSet();
				for (int i=0; i<data.length;i++){
					set.add(data[i]);
				}
				Iterator it=set.iterator();
				String para="";
				while(it.hasNext()){
					para=it.next()+","+para;
				}
				para=para.substring(0, para.length()-1);
				new TrustDataBiz().addDynTable(tableName, para);

			}
		}
	}
	
	
	/**
	 * 模板数据插入互动数据到表
	 * @param netId
	 * @return
	 * @throws Exception
	 */
	public boolean insertTempData(String netid, String taskId,String dtMsg,String fileContent)
	{
			Connection conn = null;
			PreparedStatement ps = null;
			//获取数据库连接
			conn = connectionManager.getDBConnection(StaticValue.EMP_POOLNAME);
		try{
		conn.setAutoCommit(false);
		String[] fileline= fileContent.split(";");
		for(int k=0;k<fileline.length;k++){
			String smsContent =fileline[k];
			String  tmp=fileline[k].trim();
			int temindex=tmp.indexOf(",");
			//防止数组越界异常
			if(temindex==-1){
				continue;
			}
			//过滤号码部分
			String phoneNum = fileline[k].substring(0,temindex);
			LinkedHashMap<String, String> conditionMap = new LinkedHashMap<String, String>();
			conditionMap.put("NETID", netid);
			BaseBiz empDao =new BaseBiz();
			Map<String,String> answerMap = new HashMap<String,String>();
			List<LfWXBASEINFO> li = empDao.getByCondition(LfWXBASEINFO.class,conditionMap, null);
				
			LfWXBASEINFO base=null;
			if (li != null && li.size() > 0) 
			{
				 base = li.get(0);
				String param=base.getParams();
				//判断动态表是否存在，如果不存在就创建
				if(base.getDynTableName()!=null&&"".equals(base.getDynTableName())&&param!=null&&"".equals(param)){//判断是否存在
					setDyntable(base.getDynTableName(), param);
				}
				if(param!=null&&!"".equals(param)){

				String[] conet=smsContent.split(",");
				String[] data=param.split(",");
				//通过表字段中的参数值1,2，等拼装成页面的#P_1#,#P_1# 进而对应数据库里面的P1，P2字段。
				int min = conet.length-1;
				HashSet set =new HashSet();
				for (int i=0; i<data.length;i++){
					set.add(data[i]);
				}
				
				for(int i=0;i<data.length;i++){
					//处理一些没有值的情况  防止数字转换异常
					if("null".equals(data[i])||"".equals(data[i])){
						continue;
					}
					String par="P"+data[i];
					int sva=Integer.parseInt(data[i]);
					if(sva<=min){
						answerMap.put(par, conet[sva]);
					}else{
						answerMap.put(par, "");
					}
				}
				}
			}
			
		String tableName=base.getDynTableName();
		if(answerMap == null || answerMap.size() == 0 || tableName == null || tableName.trim().length()==0){
			return false;
		}
		String type=SystemGlobals.getValue("DBType"); 
		//对拼装的数据插入数据库中
		Set<String> keySet = answerMap.keySet();
		String sql = null;
		Map<String,Integer> keyIndex = new HashMap<String,Integer>();
		try{
			String keySql = "";
			String wenhao = "?,?,?,?";
			int index = 4;
			
			for(String key:keySet){
				index++;
				keyIndex.put(key, index);
				//遍历key
			    keySql += "," + key;
			    wenhao += ",?";
			}
			//sql
			StringBuffer sqlBuffer = new StringBuffer()
							.append(" insert into  ").append(tableName).append("(NETID,TASKID,PHONE,DATE_TIME ").append(keySql).append(") ")
							.append(" values (").append(wenhao).append(") ");
			sql = sqlBuffer.toString();
			
		}catch(Exception e)
		{
			EmpExecutionContext.error(e,"数据库处理异常");
			return false;
		}

			EmpExecutionContext.sql("execute sql : " + sql);
			ps = conn.prepareStatement(sql);
			ps.setInt(1, new Integer(netid));
			ps.setInt(2, new Integer(taskId));
			ps.setString(3, phoneNum);
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			String dateStr = sdf.format(new Date());
			if("1".equals(type)){//oracle
				ps.setTimestamp(4, new Timestamp((new java.util.Date()).getTime())); 
				//测试了一下，下面的方式不对
//				ps.setString(4, "to_date('"+dateStr+"','yyyy-MM-dd HH24:MI:SS')");
			}else {//sqlserver DB2不同数据库进行处理
				ps.setString(4, dateStr);
			}
			for(String key:keySet)
			{				
				ps.setString(keyIndex.get(key),answerMap.get(key));
			}
			ps.addBatch();
			//把批处理放在循环之中  may 
			ps.executeBatch();
			ps.close();
		}
			
			conn.commit();
			return true;
		}catch (Exception e)
		{
			//回滚
			try
			{
				conn.rollback();
			} catch (SQLException e1)
			{
				EmpExecutionContext.error(e1,"数据库回滚异常");
			}
			EmpExecutionContext.error(e,"数据库处理异常");
			return false;
		} finally
		{
			try
			{
				conn.setAutoCommit(true);
				//关闭数据库资源
				super.close(null, ps, conn);
			} catch (Exception e)
			{
				EmpExecutionContext.error(e,"数据库关闭异常");
			}
			
		}
		
	}
	
}
