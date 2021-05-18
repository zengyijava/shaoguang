package com.montnets.emp.common.biz;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import com.montnets.emp.common.constant.StaticValue;
import com.montnets.emp.common.context.EmpExecutionContext;
import com.montnets.emp.common.dao.DepDAO;
import com.montnets.emp.common.dao.DepSpecialDAO;
import com.montnets.emp.entity.employee.LfEmployee;
import com.montnets.emp.entity.employee.LfEmployeeDep;
import com.montnets.emp.entity.group.LfUdgroup;
import com.montnets.emp.entity.system.LfdepPassUser;
import com.montnets.emp.entity.sysuser.LfDep;
import com.montnets.emp.entity.sysuser.LfDomination;
import com.montnets.emp.entity.sysuser.LfSysuser;
import com.montnets.emp.table.sysuser.TableLfDep;
import com.montnets.emp.table.sysuser.TableLfSysuser;
import com.montnets.emp.util.PageInfo;

/**
 * 
 * @author Administrator
 *
 */
public class DepBiz extends SuperBiz
{
	private DepSpecialDAO depSpecDao = new DepSpecialDAO();

	/**
	 * 通过父机构id获取父机构下的所有子机构
	 * @param superiorId
	 * @return
	 * @throws Exception
	 */
	public List<LfDep> getDepsByDepSuperId(Long superiorId) throws Exception
	{
		if (superiorId == null)
		{
			//如果父机构id为空，则返回null
			return null;
		}
		LinkedHashMap<String, String> conditionMap = new LinkedHashMap<String, String>();
		
		LinkedHashMap<String, String> orderByMap = new LinkedHashMap<String, String>();
		try
		{
			//父机构id
			conditionMap.put("superiorId", superiorId.toString());
			//机构状�?
			conditionMap.put("depState", "1");
			
			orderByMap.put("depName", StaticValue.ASC);
			
			List<LfDep> tempList = empDao.findListByCondition(LfDep.class,
					conditionMap, orderByMap);
			//返回结果
			return tempList;
		} catch (Exception e)
		{
			EmpExecutionContext.error(e, "通过父机构id获取父机构下的所有子机构异常。");
			throw e;
		}
	}
	
	/**
	 * 通过父机构id和企业编码获取父机构下的所有子机构
	 * @description    
	 * @param superiorId
	 * @param corpCode
	 * @return
	 * @throws Exception       			 
	 * @author chentingsheng <cts314@163.com>
	 * @datetime 2015-12-11 上午11:46:22
	 */
	public List<LfDep> getDepsByDepSuperIdAndCorpCode(Long superiorId, String corpCode) throws Exception
	{
		if (superiorId == null)
		{
			//如果父机构id为空，则返回null
			return null;
		}
		LinkedHashMap<String, String> conditionMap = new LinkedHashMap<String, String>();
		
		LinkedHashMap<String, String> orderByMap = new LinkedHashMap<String, String>();
		try
		{
			//父机构id
			conditionMap.put("superiorId", superiorId.toString());
			//企业编码
			conditionMap.put("corpCode", corpCode);
			//机构状态
			conditionMap.put("depState", "1");
			
			orderByMap.put("depName", StaticValue.ASC);
			
			List<LfDep> tempList = empDao.findListByCondition(LfDep.class,
					conditionMap, orderByMap);
			//返回结果
			return tempList;
		} catch (Exception e)
		{
			EmpExecutionContext.error(e, "通过父机构id和企业编码获取父机构下的所有子机构异常。");
			throw e;
		}
	}
	
	/**
	 * 通过机构id获取机构对象
	 * @param superiorId
	 * @return
	 * @throws Exception
	 */
	public LfDep getDepById(Long depId) throws Exception
	{
		if (depId == null)
		{
			//如果机构id为空，则返回null
			return null;
		}
		LfDep dep = null;
		LinkedHashMap<String, String> conditionMap = new LinkedHashMap<String, String>();
		try
		{
			//机构id
			conditionMap.put("depId", depId.toString());
			List<LfDep> tempList = empDao.findListByCondition(LfDep.class,
					conditionMap, null);
			if (tempList != null && tempList.size() == 1)
			{
				dep = tempList.get(0);
			}
		} catch (Exception e)
		{
			EmpExecutionContext.error(e, "通过机构id获取机构对象异常。");
		}
		return dep;
	}
	
	
	/**
	 * 通过机构id获取该机构以及其子机
	 * @param depId
	 * @return
	 * @throws Exception
	 */
	public List<Long> getChildDepAndItself(Long depId,String corpCode) throws Exception
	{
		if(depId == null){
			return null;
		}
		
		
		String deps = new DepDAO().getChildUserDepByParentID(null,depId);
		List<Long> depIds = new ArrayList<Long>();
		String array[] = deps.split(",");
		for (int index = 0; index < array.length; index++) {
			depIds.add(Long.valueOf(array[index]));
		}
		return depIds;
	}

	/**
	 * 通过机构id获取其所有子机构的id
	 * @param depId
	 * @return
	 * @throws Exception
	 */
	public List<Long> getChildDepIds(Long depId,String corpCode) throws Exception
	{
		List<Long> depIds = getChildDepAndItself(depId,corpCode);
		
		return depIds;
	}

	/**
	 * 通过操作员id获取其管辖机
	 * @param userId
	 * @return
	 * @throws Exception
	 */
	public List<LfDomination> getDominations(Long userId) throws Exception
	{
		List<LfDomination> lfDominationList = new ArrayList<LfDomination>();
		LinkedHashMap<String, String> conditionMap = new LinkedHashMap<String, String>();
		try
		{
			conditionMap.put("userId", userId.toString());
			lfDominationList = empDao.findListBySymbolsCondition(
					LfDomination.class, conditionMap, null);
		} catch (Exception e)
		{
			EmpExecutionContext.error(e, "通过操作员id获取其管辖机异常。");
		}
		return lfDominationList;

	}

	/**
	 * 通过操作员id获取其所以管辖机构的机构对象
	 * @param userId
	 * @return
	 * @throws Exception
	 */
	public List<LfDep> getAllDeps(Long userId) throws Exception
	{
		List<LfDep> depList = new ArrayList<LfDep>();
		try
		{

			LinkedHashMap<String,String> orderbyMap = new LinkedHashMap<String,String>(); 
			orderbyMap.put("depId","asc");
			 
			depList = new DepSpecialDAO().findDomDepBySysuserID(userId.toString(), orderbyMap);

		} catch (Exception e)
		{
			EmpExecutionContext.error(e, "通过操作员id获取其所以管辖机构的机构对象异常。");
		}
		return depList;
	}

	/**
	 * 通过操作员id和企业编码获取其所以管辖机构的机构对象
	 * @description    
	 * @param userId  操作员id
	 * @param corpCode  企业编码
	 * @return
	 * @throws Exception       			 
	 * @author chentingsheng <cts314@163.com>
	 * @datetime 2015-12-11 上午11:21:29
	 */
	public List<LfDep> getAllDepByUserIdAndCorpCode(Long userId, String corpCode) throws Exception
	{
		List<LfDep> depList = new ArrayList<LfDep>();
		try
		{
			LinkedHashMap<String,String> orderbyMap = new LinkedHashMap<String,String>(); 
			orderbyMap.put("depId","asc");
			 
			depList = new DepSpecialDAO().findDomDepByUserIdAndCorpCode(userId.toString(), corpCode, orderbyMap);

		} catch (Exception e)
		{
			EmpExecutionContext.error(e, "通过操作员id和企业编码获取其所以管辖机构的机构对象异常。");
		}
		return depList;
	}
	
	/**
	 * 操作员机构查
	 * @param depIdCon
	 * @param conditionMap
	 * @param orderbyMap
	 * @param pageInfo
	 * @return
	 * @throws Exception
	 */
	public List<LfDep> getDepListByDepIdCon(Long depId,
			Map<String, String> conditionMap, Map<String, String> orderbyMap,
			PageInfo pageInfo) throws Exception {
		String depIdCon = new DepDAO().getChildUserDepByParentID(depId,TableLfDep.DEP_ID);
		return new DepSpecialDAO().getDepListByDepIdCon(depIdCon, conditionMap, orderbyMap, pageInfo);
	}
	
	/**
	 * 获取机构Id字符串中有效的机构Id
	 * @param depIds
	 * @return
	 * @throws Exception
	 */
	public String getDepCountByDepId(String depIds) throws Exception
	{
		LinkedHashMap<String, String> conditionMap = new LinkedHashMap<String, String>();
		//设置条件
		conditionMap.put("depId&in", depIds);
		conditionMap.put("userState", "1");
		//定义列名
		String columName = "count("+TableLfSysuser.USER_ID+")";
		//查询结果
		List<String[]> countList = new DepSpecialDAO().findListByConditionByColumNameWithGroupBy(LfSysuser.class, 
				conditionMap, columName, null,null);
		
		//结果判断
		if(countList != null && countList.size() > 0)
		{
			return countList.get(0)[0];
		}else
		{
			return "0";
		}
	}
	
	//通过id字符串获取员工成员列表(改)depIds为,e1,3,10,e23,这种类型的字符串
	public String getEmpByDepId(String depIds, String corpCode)
	{
		StringBuffer phones= new StringBuffer();
		try {
			LinkedHashMap<String, String> conditionMap = new LinkedHashMap<String, String>();
			String[] tempDepIds = depIds.split(",");
			List<String> depIdsList = Arrays.asList(tempDepIds);
			List<String> depIdsList2= new ArrayList<String>();
			List<String> depIdsList3= new ArrayList<String>();
			
			for(int a=depIdsList.size()-1;a>=0;a--)
			{
				if(!"".equals(depIdsList.get(a)))
				{
					if(depIdsList.get(a).indexOf("e")>-1)
					{
						//不包含子机构的
						depIdsList3.add(depIdsList.get(a));
					}
					else {
						depIdsList2.add(depIdsList.get(a));
					}
				}
			}
			StringBuffer buffer = new StringBuffer("");
			List<LfEmployee> employees =null;
			int j=0;
			//先遍历不包含子机构的
			for(int i=0;i<depIdsList2.size();i++)
			{
			    if(depIdsList2.get(i)!=null)
			    {
			    	buffer.append(depIdsList2.get(i)+",");
			    	j++;
			    }
			    if(j>=999)
			    {
			    	j=0;
			    	conditionMap.put("depId&in", buffer.toString());
			    	conditionMap.put("corpCode", corpCode);
			    	employees = empDao.findListBySymbolsCondition(LfEmployee.class, conditionMap, null);
			    	if(employees!=null && employees.size()>0)
					{
						for(LfEmployee employee : employees)
						{
							phones.append(employee.getMobile()).append(",");
						}
					}
			    	buffer=new StringBuffer("");
			    }else if(i==depIdsList2.size()-1)
			    {
			    	conditionMap.put("depId&in", buffer.toString());
			    	conditionMap.put("corpCode", corpCode);
			    	employees = empDao.findListBySymbolsCondition(LfEmployee.class, conditionMap, null);
			    	if(employees!=null && employees.size()>0)
					{
						for(LfEmployee employee : employees)
						{
							phones.append(employee.getMobile()).append(",");
						}
					}
			    }
			}
			//再遍历包含子机构的
			for(int y=0;y<depIdsList3.size();y++)
			{
				if(depIdsList3.get(y)!=null && !"".equals(depIdsList3.get(y)) && depIdsList3.get(y).indexOf("e")>-1)
				{
/*					List<LfEmployee> employeeList = new GenericLfEmployeeVoDAO().findEmpMobileByDepIds(corpCode, depIdsList3.get(y).substring(1));
*/					
					List<LfEmployee> employeeList = findEmpMobilesByDepIds(corpCode, depIdsList3.get(y).substring(1));

					for(LfEmployee employee:employeeList)
					{
						phones.append(employee.getMobile()).append(",");
					}
				}
			}
			
		} catch (Exception e) {
			EmpExecutionContext.error(e, "通过id字符串获取员工成员列表异常。");
		}
		return phones.toString();
	}
	
	//如果没有传递群组id，那么是以","开头的
	public String getEmpByGroupStr(String groupStr, String corpCode) throws Exception
	{
		StringBuffer sb = new StringBuffer();
		try
		{
			//通过群组获取电话号码字符串
			if(groupStr!=null && groupStr.length()>1)
			{
				//去掉结尾的逗号
				String udgId =  groupStr.substring(1,groupStr.length()-1);
				LinkedHashMap<String, String> conditionMap = new LinkedHashMap<String, String>();
				conditionMap.put("udgId&in", udgId);
				List<LfUdgroup> groupList = empDao.findListBySymbolsCondition(LfUdgroup.class, conditionMap, null);
				StringBuffer buffer=new StringBuffer();
				if(groupList!=null && groupList.size()>0)
				{
					for (int i =0 ;i<groupList.size();i++) 
					{
						buffer.append(groupList.get(i).getGroupid());
						if(i!=(groupList.size()-1))
						{
							buffer.append(",");
						}
					}
				}
				conditionMap.clear();
				conditionMap.put("corpCode", corpCode);
				/*List<LfEmployee> lfEmployeeList = new AddrBookBiz().getEmployeeListByUdgId(buffer.toString(), null, null);*/
				List<LfEmployee> lfEmployeeList = getEmployeeListByUdgId(buffer.toString(), null, conditionMap);
				
				
				if (lfEmployeeList != null && lfEmployeeList.size() > 0) {
					for (LfEmployee user : lfEmployeeList) {
						sb.append(user.getMobile()).append(",");
					}
				}
			}
		} catch (Exception e)
		{
			//异常处理
			EmpExecutionContext.error(e, "获取群组字符串异常。");
		}
		
		return sb.toString();
	}
	
	//如果没有传递群组id，那么是以","开头的
	public String getEmpByGroupStr(String groupStr) throws Exception
	{
		StringBuffer sb = new StringBuffer();
		try
		{
			//通过群组获取电话号码字符串
			if(groupStr!=null && groupStr.length()>1)
			{
				//去掉结尾的逗号
				String udgId =  groupStr.substring(1,groupStr.length()-1);
				LinkedHashMap<String, String> conditionMap = new LinkedHashMap<String, String>();
				conditionMap.put("udgId&in", udgId);
				List<LfUdgroup> groupList = empDao.findListBySymbolsCondition(LfUdgroup.class, conditionMap, null);
				StringBuffer buffer=new StringBuffer();
				if(groupList!=null && groupList.size()>0)
				{
					for (int i =0 ;i<groupList.size();i++) 
					{
						buffer.append(groupList.get(i).getGroupid());
						if(i!=(groupList.size()-1))
						{
							buffer.append(",");
						}
					}
				}
				/*List<LfEmployee> lfEmployeeList = new AddrBookBiz().getEmployeeListByUdgId(buffer.toString(), null, null);*/
				List<LfEmployee> lfEmployeeList = getEmployeeListByUdgId(buffer.toString(), null, null);
				
				
				if (lfEmployeeList != null && lfEmployeeList.size() > 0) {
					for (LfEmployee user : lfEmployeeList) {
						sb.append(user.getMobile()).append(",");
					}
				}
			}
		} catch (Exception e)
		{
			//异常处理
			EmpExecutionContext.error(e, "获取群组字符串异常。");
		}
		
		return sb.toString();
	}
	
	/**
	 *   员工机构查询
	 * @param corpCode	企业编码
	 * @param depId	机构ID
	 * @return
	 * @throws Exception
	 */
	public List<LfEmployeeDep> findEmpDepsByDepId(String corpCode,String depId) throws Exception {
		List<LfEmployeeDep> returnVoList = null;
		try{
			returnVoList = depSpecDao.findEmployeeDepsByDepId(corpCode, depId);
		}catch (Exception e) {
			EmpExecutionContext.error(e, "员工机构查询异常。");
		}
		return returnVoList;
	}
	/**
	 *  查询该机构下的手机号码
	 * @param corpCode	企业编码
	 * @param depId	机构ID
	 * @return
	 * @throws Exception
	 */
	public List<LfEmployee> findEmpMobilesByDepIds(String corpCode,
			String depId) throws Exception {
		
		List<LfEmployee> returnVoList = null;
		try{
			returnVoList = depSpecDao.findEmpMobileByDepIds(corpCode, depId);
		}catch (Exception e) {
			EmpExecutionContext.error(e, "查询该机构下的手机号码异常。");
		}
		return returnVoList;
		
	}
	
	
	/**
	 * 通过群组Id获取员工信息
	 * @param udgId
	 * @param loginId
	 * @param conditionMap
	 * @return
	 * @throws Exception
	 */
	public List<LfEmployee> getEmployeeListByUdgId(String udgId,String loginId,LinkedHashMap<String, String> conditionMap) throws Exception
	{
		LinkedHashMap<String, String> orderbyMap = new LinkedHashMap<String, String>();
		orderbyMap.put("name", StaticValue.ASC);
		return depSpecDao.getEmployeeByGuid(udgId, loginId, conditionMap, orderbyMap, null);
	}
	

	/**
	 * 通过群组Id获取员工信息(重载getEmployeeListByUdgId方法，支持分页查询)
	 * @description    
	 * @param udgId
	 * @param loginId
	 * @param conditionMap
	 * @param pageInfo
	 * @return
	 * @throws Exception       			 
	 * @author chentingsheng <cts314@163.com>
	 * @datetime 2014-5-22 下午04:26:41
	 */
	public List<LfEmployee> getEmployeeListByUdgId(String udgId,String loginId,LinkedHashMap<String, String> conditionMap, PageInfo pageInfo) throws Exception
	{
		String epname =  conditionMap.get("name&like");
		return depSpecDao.getEmployeeByGuid(udgId, loginId, epname, pageInfo);
	}
	
	/**
	 * 通过群组Id和企业编码获取员工信息
	 * @description    
	 * @param udgId
	 * @param loginId
	 * @param conditionMap
	 * @param pageInfo
	 * @param lgcorpcode
	 * @return
	 * @throws Exception       			 
	 * @author chentingsheng <cts314@163.com>
	 * @datetime 2015-12-18 下午04:00:02
	 */
	public List<LfEmployee> getEmployeeListByUdgIdAndCorpCode(String udgId,String loginId,LinkedHashMap<String, String> conditionMap, PageInfo pageInfo, String lgcorpcode) throws Exception
	{
		String epname =  conditionMap.get("name&like");
		return depSpecDao.getEmployeeByGuidAndCorpCode(udgId, loginId, epname, pageInfo, lgcorpcode);
	}
	
	/**
	 * 通过员工机构id查找树
	 * @param lfSysuser
	 * @param depIds 机构Id集合字符串
	 * @return
	 * @throws Exception
	 */
	public List<LfEmployeeDep> getEmpSecondDepTreeByUserIdorDepId(String userId,String depId) throws Exception {
		List<LfEmployeeDep> deps = null;
		try{
			deps = depSpecDao.getEmpSecondDepTreeByUserIdorDepId( userId,depId);
		}catch (Exception e) {
			EmpExecutionContext.error(e, "通过员工机构id查找树异常。");
		}
		return deps;
	}
	
	/**
	 * 通过员工机构id查找树(重载方法)
	 * @param lfSysuser
	 * @param depIds 机构Id集合字符串
	 * @param corpCode 企业编码
	 * @return
	 * @throws Exception
	 */
	public List<LfEmployeeDep> getEmpSecondDepTreeByUserIdorDepId(String userId,String depId,String corpCode) throws Exception {
		List<LfEmployeeDep> deps = null;
		try{
			//必须带企业编码
			deps = depSpecDao.getEmpSecondDepTreeByUserIdorDepId( userId,depId,corpCode);
		}catch (Exception e) {
			EmpExecutionContext.error(e, "通过员工机构id查找树异常。");
		}
		return deps;
	}
	
	/**
	 *   获取其机构的密码接收人
	 * @param depId
	 * @param pageInfo
	 * @return
	 */
	public List<LfdepPassUser> getSysuserbyDepId(String depId,PageInfo pageInfo) 
	{
		List<LfdepPassUser> depPassUserVos = depSpecDao.getSysuserbyDepId( depId,pageInfo);
		return depPassUserVos;
	}

}