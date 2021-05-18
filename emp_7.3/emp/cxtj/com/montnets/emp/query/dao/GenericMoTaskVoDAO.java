package com.montnets.emp.query.dao;

import java.util.LinkedHashMap;
import java.util.List;

import org.apache.commons.beanutils.DynaBean;

import com.montnets.emp.common.constant.StaticValue;
import com.montnets.emp.common.context.EmpExecutionContext;
import com.montnets.emp.common.dao.SuperDAO;
import com.montnets.emp.common.dao.impl.DataAccessDriver;
import com.montnets.emp.inbox.dao.ReciveBoxDao;
import com.montnets.emp.query.vo.MoTaskVo;
import com.montnets.emp.table.query.TableMoTask;
import com.montnets.emp.util.PageInfo;

/**
 * 上行实时记录查询
 * @project emp
 * @author 
 * @company ShenZhen Montnets Technology CO.,LTD.
 * @datetime 2011-4-1 上午11:24:16
 * @description
 */
public class GenericMoTaskVoDAO extends SuperDAO 
{

	/**
	 * 实时上行记录查询
	 * @param moTaskVo
	 * @return
	 * @throws Exception
	 */
	public List<MoTaskVo> findMoTaskVo(MoTaskVo moTaskVo,String lgcorpcode) throws Exception
	{
		/*//获取查询列名
		String fieldSql = GenericMoTaskVoSQL.getFieldSql();
		//组装查询表
		String tableSql = GenericMoTaskVoSQL.getTableSql();*/
		//获取查询列名
		String fieldSql = GenericMoTaskVoSQL.getFieldNoNameSql();
		//组装查询表
		String tableSql = GenericMoTaskVoSQL.getTableNoNameSql();
		//组装过滤条件
		String conditionSql = GenericMoTaskVoSQL.getConditionSql(moTaskVo);
		//时间段
		List<String> timeList = GenericMoTaskVoSQL.getTimeCondition(moTaskVo);
		//排序
		String orderBySql = GenericMoTaskVoSQL.getOrderBySql();
		//组装sql语句
		String sql = new StringBuffer(fieldSql).append(tableSql).append(
				conditionSql).append(orderBySql).toString();
		EmpExecutionContext.sql("execute sql："+sql);
		//调用查询方法
		List<MoTaskVo> returnVoList = findVoListBySQL(MoTaskVo.class, sql,
				StaticValue.EMP_POOLNAME, timeList);
		addNameMoTask(returnVoList,lgcorpcode);
		return returnVoList;
	}

	/**
	 * 带分页条件的实时上行实时记录查询
	 * @param moTaskVo
	 * @param pageInfo
	 * @return
	 * @throws Exception
	 */
	public List<MoTaskVo> findMoTaskVo(MoTaskVo moTaskVo, PageInfo pageInfo,String lgcorpcode)
			throws Exception
	{
	/*	//获取查询列名
		String fieldSql = GenericMoTaskVoSQL.getFieldSql();
		//组装查询表
		String tableSql = GenericMoTaskVoSQL.getTableSql();*/
		//获取查询列名
		String fieldSql = GenericMoTaskVoSQL.getFieldNoNameSql();
		//组装查询表
		String tableSql = GenericMoTaskVoSQL.getTableNoNameSql();
		//组装过滤条件
		String conditionSql = GenericMoTaskVoSQL.getConditionSql(moTaskVo);
		//时间段
		List<String> timeList = GenericMoTaskVoSQL.getTimeCondition(moTaskVo);
		//排序
		String orderBySql = GenericMoTaskVoSQL.getOrderBySql();
		//组装sql语句
		String sql = new StringBuffer(fieldSql).append(tableSql).append(
				conditionSql).append(orderBySql).toString();
		EmpExecutionContext.sql("execute sql："+sql);
		//组装统计总条数sql语句
		String countSql = new StringBuffer("select count(*) totalcount")
						.append(" from (").append(" select ").append(" motask.").append(TableMoTask.ID)
						.append(tableSql).append(conditionSql).append(") a").toString();
		//调用查询方法
		List<MoTaskVo> returnVoList =new DataAccessDriver().getGenericDAO().findPageVoListBySQL(
						MoTaskVo.class, sql, countSql, pageInfo,
						StaticValue.EMP_POOLNAME, timeList);
		addNameMoTask(returnVoList,lgcorpcode);
		
		return returnVoList;
	}
	
	
	
	/**
	 *   将查询出来的数据增加 名字
	 * @return
	 */
	private List<MoTaskVo> addNameMoTask(List<MoTaskVo> moTaskList,String lgcorpcode){
		try{
			if(moTaskList != null && moTaskList.size()>0){
				StringBuffer sb = new StringBuffer();
				for(MoTaskVo taskVo:moTaskList){
					sb.append("'").append(taskVo.getPhone()).append("',");
				}
				LinkedHashMap<String, String> userMap = new LinkedHashMap<String, String>();
				if(sb.length() > 0){
					String phoneStr = sb.toString().substring(0, sb.toString().lastIndexOf(","));
					//conditionMap.put("mobile&in", phoneStr);
					//List<LfEmployee> lfEmployees = baseBiz.getByCondition(LfEmployee.class, conditionMap, null);
					List<DynaBean> lfEmployees =new ReciveBoxDao().findLfEmployeesByMobiles(phoneStr,lgcorpcode);
					if(lfEmployees != null && lfEmployees.size()>0){
						for(DynaBean employee:lfEmployees){
							//userMap.put(employee.getMobile().trim(), employee.getName());
							if(employee.get("mobile")!=null&&employee.get("name")!=null){
								userMap.put(employee.get("mobile").toString(), employee.get("name").toString());
							}
						}
					}
//					conditionMap.clear();
//					conditionMap.put("mobile&in",phoneStr);
//					List<LfClient> lfclients = baseBiz.getByCondition(LfClient.class, conditionMap, null);
					List<DynaBean> lfclients =new ReciveBoxDao().findLfClientsByMobiles(phoneStr,lgcorpcode);
					if(lfclients != null && lfclients.size()>0){
						for(DynaBean lfclient:lfclients){
							if(lfclient.get("mobile")!=null&&lfclient.get("name")!=null){
								userMap.put(lfclient.get("mobile").toString(), lfclient.get("name").toString());
							}
						}
					}
					String name = "";
					for(MoTaskVo taskVo:moTaskList){
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
			EmpExecutionContext.error(e,"为上行实时数据获取号码对应名称异常");
		}
		return moTaskList;
	}

}
