package com.montnets.emp.query.dao;

import java.util.Calendar;
import java.util.LinkedHashMap;
import java.util.List;

import org.apache.commons.beanutils.DynaBean;

import com.montnets.emp.common.biz.CommonBiz;
import com.montnets.emp.common.constant.StaticValue;
import com.montnets.emp.common.context.EmpExecutionContext;
import com.montnets.emp.common.dao.SuperDAO;
import com.montnets.emp.common.dao.impl.DataAccessDriver;
import com.montnets.emp.inbox.dao.ReciveBoxDao;
import com.montnets.emp.query.vo.MoTask01_12Vo;
import com.montnets.emp.table.query.TableMoTask01_12;
import com.montnets.emp.util.PageInfo;
/**
 * 上行历史记录查询
 * @project emp
 * @author 
 * @company ShenZhen Montnets Technology CO.,LTD.
 * @datetime 2011-4-1 上午09:54:02
 * @description
 */
public class GenericMoTask01_12VoDAO extends SuperDAO 
{

	/**
	 * 上行历史记录查询
	 * @param moTask01_12Vo
	 * @param pageInfo
	 * @return
	 * @throws Exception
	 */
	public List<MoTask01_12Vo> findMoTask01_12Vo(MoTask01_12Vo moTask01_12Vo,
			PageInfo pageInfo,String corpCode) throws Exception
	{
		//获取表名
		String tableName = getMotaskTableNameByTime(moTask01_12Vo);
		if(tableName==null || "".equals(tableName))
		{
			return null;
		}
	/*	//获取查询列名
		String fieldSql = GenericMoTask01_12VoSQL.getFieldSql();
		//组装查询的表名
		String tableSql = GenericMoTask01_12VoSQL.getTableSql(tableName);*/
		//获取查询列名
		String fieldSql = GenericMoTask01_12VoSQL.getFieldNoNameSql();
		//组装查询的表名
		String tableSql = GenericMoTask01_12VoSQL.getTableNoNameSql(tableName);
		//组装查询的过滤条件 
		String conditionSql = GenericMoTask01_12VoSQL
				.getConditionSql(moTask01_12Vo);
		//时间段
		List<String> timeList = GenericMoTask01_12VoSQL
				.getTimeCondition(moTask01_12Vo);
		//排序
		String orderBySql = GenericMoTask01_12VoSQL.getOrderBySql();
		//组装sql语句
		String sql = new StringBuffer(fieldSql).append(tableSql).append(
				conditionSql).append(orderBySql).toString();
		EmpExecutionContext.sql("execute sql："+sql);
		//组装统计总数的sql语句
		String countSql = new StringBuffer("select count(*) totalcount")
				.append(" from (").append(" select ").append(" mtt.").append(TableMoTask01_12.ID)
				.append(tableSql).append(conditionSql).append(") a").toString();
		//调用查询方法
		List<MoTask01_12Vo> returnVoList =new DataAccessDriver().getGenericDAO().findPageVoListBySQL(
						MoTask01_12Vo.class, sql, countSql, pageInfo,
						StaticValue.EMP_POOLNAME, timeList);
		addNameMoTask(returnVoList,corpCode);
		return returnVoList;
	}

	/**
	 * 上行历史记录查询 不带分页信息
	 * @param moTask01_12Vo
	 * @return
	 * @throws Exception
	 */
	public List<MoTask01_12Vo> findMoTask01_12Vo(MoTask01_12Vo moTask01_12Vo,String corpCode)
			throws Exception
	{
		//获取表名
		String tableName = getMotaskTableNameByTime(moTask01_12Vo);
		if(tableName==null || "".equals(tableName))
		{
			return null;
		}
		/*//获取查询列名
		String fieldSql = GenericMoTask01_12VoSQL.getFieldSql();
		//组装查询的表名
		String tableSql = GenericMoTask01_12VoSQL.getTableSql(tableName);*/
		//获取查询列名
		String fieldSql = GenericMoTask01_12VoSQL.getFieldNoNameSql();
		//组装查询的表名
		String tableSql = GenericMoTask01_12VoSQL.getTableNoNameSql(tableName);
		//组装过滤条件
		String conditionSql = GenericMoTask01_12VoSQL
				.getConditionSql(moTask01_12Vo);
		//时间段
		List<String> timeList = GenericMoTask01_12VoSQL
				.getTimeCondition(moTask01_12Vo);
		//排序sql
		String orderBySql = GenericMoTask01_12VoSQL.getOrderBySql();
		//组装sql语句
		String sql = new StringBuffer(fieldSql).append(tableSql).append(
				conditionSql).append(orderBySql).toString();
		
		EmpExecutionContext.sql("execute sql："+sql);
		//调用查询方法
		List<MoTask01_12Vo> returnVoList = findVoListBySQL(MoTask01_12Vo.class,
				sql, StaticValue.EMP_POOLNAME, timeList);
		
		addNameMoTask(returnVoList,corpCode);
		return returnVoList;
	}

	/**
	 * 上行历史记录查询
	 * @param time
	 * @return
	 * @throws Exception 
	 */
	protected String getMotaskTableNameByTime(MoTask01_12Vo moTask01_12Vo) throws Exception
	{
		String startTime = moTask01_12Vo.getStartSubmitTime();
		String endTime = moTask01_12Vo.getEndSubmitTime();
		String tableName = "";
		//页面输入时间段为空时
		if ((startTime == null || "".equals(startTime)) && (endTime != null
				&& !"".equals(endTime)))
		{
			moTask01_12Vo.setStartSubmitTime(endTime.substring(0,7) + "-01 00:00:00");
			tableName = "MOTASK" + endTime.substring(0,7).replace("-", "");
			tableName = new CommonBiz().getTableName(tableName);
			//上行记录增加withnolock
			if(!"".equals(tableName))
			{
				tableName = tableName + " mtt " + StaticValue.getWITHNOLOCK()+"";
			}
			
//			//获取当前时间
//			Calendar calendar = Calendar.getInstance();
//			//获取当前时间的年月
//			int year = calendar.get(Calendar.YEAR);
//			int startmonth = calendar.get(Calendar.MONTH) + 1;
//			//获取结束时间，及年月
//			calendar.add(Calendar.MONTH, 1);
//			int endyear = calendar.get(Calendar.YEAR);
//			int endmonth =calendar.get(Calendar.MONTH)+1;		
//			//处理月份小于10的
//			String startmonthstr = startmonth > 9 ? String.valueOf(startmonth)
//					: "0" + String.valueOf(startmonth);
//			String endmonthstr = endmonth > 9 ? String.valueOf(endmonth) : "0"
//					+ String.valueOf(endmonth);
//			//将时间设置到vo对象中
//			moTask01_12Vo.setStartSubmitTime(year + "-" + startmonthstr
//					+ "-01 00:00:00");
//			moTask01_12Vo.setEndSubmitTime(endyear + "-" + endmonthstr
//					+ "-01 00:00:00");
//			//获取表名
//			tableName = "MOTASK" +year+ startmonthstr;
//			tableName = new CommonBiz().getTableName(tableName);
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
			moTask01_12Vo.setEndSubmitTime(startTime.substring(0,4) + "-" + startmonthstr
					+ "-01 00:00:00");
			tableName = "MOTASK" + startTime.substring(0,7).replace("-", "");
			tableName = new CommonBiz().getTableName(tableName);
			//上行记录增加withnolock
			if(!"".equals(tableName))
			{
				tableName = tableName + " mtt " + StaticValue.getWITHNOLOCK()+" ";
			}
		}
		else if((startTime != null && !"".equals(startTime)) && (endTime != null
				&& !"".equals(endTime)))
		{
			//获取开始月及结束月
			String startmonthstr = startTime.substring(5, 7);
			String endmonthstr = endTime.substring(5, 7);
			//如果开始月与结束月在同一个月份内，则只需要查询一张表
			if (startmonthstr.equals(endmonthstr))
			{
				tableName = "MOTASk"+ startTime.substring(0,7).replace("-", "");
				tableName = new CommonBiz().getTableName(tableName);
				//上行记录增加withnolock
				if(!"".equals(tableName))
				{
					tableName = tableName + " mtt " + StaticValue.getWITHNOLOCK()+"";
				}
				
			} else
			{
				//如果结束时间是-01 00:00:00这种的，就可以只查一个表
				if("-01 00:00:00".equals(endTime.substring(7)))
				{
					tableName = "MOTASK" + startTime.substring(0,7).replace("-", "");
					tableName = new CommonBiz().getTableName(tableName);
					//上行记录增加withnolock
					if(!"".equals(tableName))
					{
						tableName = tableName + " mtt " + StaticValue.getWITHNOLOCK()+" ";
					}
				}
				else
				{
					
					//否则则需要查询两张表的记录
					String startTableName = "MOTASk"+ startTime.substring(0,7).replace("-", "");
					startTableName = new CommonBiz().getTableName(startTableName);
					String endTableName = "MOTASk"+ endTime.substring(0,7).replace("-", "");
					endTableName = new CommonBiz().getTableName(endTableName);
					if(!"".equals(startTableName)&& !"".equals(endTableName))
					{
						
						tableName = new StringBuffer("(select * from ").append(
								startTableName+" "+StaticValue.getWITHNOLOCK()).append(" union all select * from ")
								.append(endTableName+" "+StaticValue.getWITHNOLOCK()).append(") mtt ").toString();
					}
					else if(!"".equals(startTableName))
					{
						tableName = startTableName;
						//上行记录增加withnolock
						if(!"".equals(tableName))
						{
							tableName = tableName + " mtt " + StaticValue.getWITHNOLOCK()+"";
						}
					}
					else if(!"".equals(endTableName))
					{
						tableName = endTableName;
						//上行记录增加withnolock
						if(!"".equals(tableName))
						{
							tableName = tableName + " mtt " + StaticValue.getWITHNOLOCK()+"";
						}
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
			//获取当前时间的年月
			int year = calendar.get(Calendar.YEAR);
			int startmonth = calendar.get(Calendar.MONTH) + 1;
			//获取结束时间，及年月
			calendar.add(Calendar.MONTH, 1);
			int endyear = calendar.get(Calendar.YEAR);
			int endmonth =calendar.get(Calendar.MONTH)+1;		
			//处理月份小于10的
			String startmonthstr = startmonth > 9 ? String.valueOf(startmonth)
					: "0" + String.valueOf(startmonth);
			String endmonthstr = endmonth > 9 ? String.valueOf(endmonth) : "0"
					+ String.valueOf(endmonth);
			//将时间设置到vo对象中
			moTask01_12Vo.setStartSubmitTime(year + "-" + startmonthstr
					+ "-01 00:00:00");
			moTask01_12Vo.setEndSubmitTime(endyear + "-" + endmonthstr
					+ "-01 00:00:00");
			//获取表名
			tableName = "MOTASK" +year+ startmonthstr;
			tableName = new CommonBiz().getTableName(tableName);
			//上行记录增加withnolock.l
			if(!"".equals(tableName))
			{
				tableName = tableName + " mtt " + StaticValue.getWITHNOLOCK()+"";
			}
		}
		if("".equals(tableName))
		{
			return "";
		}
		return tableName;
	}
	
	
	/**
	 *   将查询出来的数据增加 名字
	 * @return
	 */
	private void addNameMoTask(List<MoTask01_12Vo> moTasksList,String corpCode){
		try{
			if(moTasksList != null && moTasksList.size()>0){
				StringBuffer sb = new StringBuffer();
				for(MoTask01_12Vo taskVo:moTasksList){
					sb.append("'").append(taskVo.getPhone()).append("',");
				}
				LinkedHashMap<String, String> userMap = new LinkedHashMap<String, String>();
				if(sb.length() > 0){
					String phoneStr = sb.toString().substring(0, sb.toString().lastIndexOf(","));
					//conditionMap.put("mobile&in",phoneStr);
					//List<LfEmployee> lfEmployees = baseBiz.getByCondition(LfEmployee.class, conditionMap, null);
					List<DynaBean> lfEmployees =new ReciveBoxDao().findLfEmployeesByMobiles(phoneStr,corpCode);
					if(lfEmployees != null && lfEmployees.size()>0){
						for(DynaBean employee:lfEmployees){
//							userMap.put(employee.getMobile().trim(), employee.getName());
							if(employee.get("mobile")!=null&& employee.get("name")!=null){
								userMap.put(employee.get("mobile").toString(), employee.get("name").toString());
							}
						}
					}
					//conditionMap.clear();
					//conditionMap.put("mobile&in",phoneStr);
					//List<LfClient> lfclients = baseBiz.getByCondition(LfClient.class, conditionMap, null);
					List<DynaBean> lfclients =new ReciveBoxDao().findLfClientsByMobiles(phoneStr,corpCode);
					if(lfclients != null && lfclients.size()>0){
						for(DynaBean lfclient:lfclients){
							if(lfclient.get("mobile")!=null&& lfclient.get("name")!=null){
								userMap.put(lfclient.get("mobile").toString(), lfclient.get("name").toString());
							}
						}
					}
					String name = "";
					for(MoTask01_12Vo taskVo:moTasksList){
						if(userMap != null && userMap.size()>0){
							name = userMap.get(taskVo.getPhone());
							if(name != null && !"".equals(name)){
								taskVo.setName(name);
								name = "";
							}
						}else{
								break;
						}
					}
				}
				userMap.clear();
				userMap=null;
			}
		}catch (Exception e) {
			EmpExecutionContext.error(e,"为上行历史数据获取号码对应名字异常");
		}
	}
}
