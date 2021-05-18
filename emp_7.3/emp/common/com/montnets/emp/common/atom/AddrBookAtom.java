package com.montnets.emp.common.atom;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import com.montnets.emp.common.biz.SuperBiz;
import com.montnets.emp.entity.employee.LfEmployee;
import com.montnets.emp.entity.group.LfList2gro;
import com.montnets.emp.table.employee.TableLfEmployee;
import com.montnets.emp.table.group.TableLfList2gro;

public class AddrBookAtom extends SuperBiz
{
	
	/**
	 * 获取某机构下员工个数
	 * @param depIds
	 * @return
	 * @throws Exception
	 */
	public String getEmployeeCountByDepId(String depIds) throws Exception
	{
		LinkedHashMap<String, String> conditionMap = new LinkedHashMap<String, String>();
		
		conditionMap.put("depId&in", depIds);
		String columName = "count("+TableLfEmployee.EMPLOYEE_ID+")";
		List<String[]> countList = empDao.findListByConditionByColumNameWithGroupBy(LfEmployee.class, 
				conditionMap, columName, null,null);
		
		if(countList != null && countList.size() > 0)
		{
			return countList.get(0)[0];
		}else
		{
			return "0";
		}
	}
	
	/**
	 * 获取员工个数
	 * @param udgIds
	 * @return
	 * @throws Exception
	 */
	public Map<String,String> getEmployeeCount(String udgIds) throws Exception
	{
		HashMap<String, String> countMap = new HashMap<String, String>();
		LinkedHashMap<String, String> conditionMap = new LinkedHashMap<String, String>();
		
		conditionMap.put("udgId&in", udgIds);
		conditionMap.put("l2gType&in", "0,2");
		String groupColum = TableLfList2gro.UDG_ID;
		String columName = "count("+TableLfList2gro.L2G_ID+"),"+TableLfList2gro.UDG_ID;
		List<String[]> countList = empDao.findListByConditionByColumNameWithGroupBy(LfList2gro.class, 
				conditionMap, columName, null,groupColum);
		
		if(countList != null && countList.size() > 0)
		{
			for(int i=0;i<countList.size();i++)
			{
				String[] countArr = countList.get(i);
				countMap.put(countArr[1], countArr[0]);
			}
		}
		return countMap;
	}
	
	/**
	 * 计算员工/客户群组中的人数
	 * @param udgIds  群组IDS
	 * @param type	群组类型  1是员工   2是客户群组
	 * @return
	 * @throws Exception
	 */
	public Map<String,String> getGroupCount(String udgIds,String type) throws Exception
	{
		HashMap<String, String> countMap = new HashMap<String, String>();
		LinkedHashMap<String, String> conditionMap = new LinkedHashMap<String, String>();
		//群组类型   1是员工   2是客户群组
		if("1".equals(type)){
			// 添加类型(员工0,客户1,手工2)
			conditionMap.put("l2gType&in", "0,2");
		}else if("2".equals(type)){
			// 添加类型(员工0,客户1,手工2)
			conditionMap.put("l2gType", "1");
		}
		//条件
		conditionMap.put("udgId&in", udgIds);
		//GROUPBY
		String groupColum = TableLfList2gro.UDG_ID;
		//查询的列
		String columName = "count("+TableLfList2gro.L2G_ID+"),"+TableLfList2gro.UDG_ID;
		//查询
		List<String[]> countList = empDao.findListByConditionByColumNameWithGroupBy(LfList2gro.class, 
				conditionMap, columName, null,groupColum);
		if(countList != null && countList.size() > 0)
		{
			for(int i=0;i<countList.size();i++)
			{
				String[] countArr = countList.get(i);
				countMap.put(countArr[1], countArr[0]);
			}
		}
		return countMap;
	}
	
}
