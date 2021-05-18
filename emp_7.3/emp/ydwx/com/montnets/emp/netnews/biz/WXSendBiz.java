package com.montnets.emp.netnews.biz;

import java.sql.Connection;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.beanutils.DynaBean;

import com.montnets.emp.common.biz.SuperBiz;
import com.montnets.emp.common.constant.StaticValue;
import com.montnets.emp.common.context.EmpExecutionContext;
import com.montnets.emp.common.dao.SmsSpecialDAO;
import com.montnets.emp.common.vo.GroupInfoVo;
import com.montnets.emp.entity.client.LfClientDep;
import com.montnets.emp.entity.employee.LfEmployee;
import com.montnets.emp.entity.employee.LfEmployeeDep;
import com.montnets.emp.entity.group.LfList2gro;
import com.montnets.emp.netnews.common.CompressEncodeing;
import com.montnets.emp.netnews.dao.WXSendDao;
import com.montnets.emp.netnews.entity.LfDfadvanced;
import com.montnets.emp.netnews.entity.LfWXBASEINFO;
import com.montnets.emp.netnews.entity.LfWXPAGE;
import com.montnets.emp.table.group.TableLfList2gro;
import com.montnets.emp.util.PageInfo;

public class WXSendBiz extends SuperBiz{
	SmsSpecialDAO smsSpecialDAO = new SmsSpecialDAO();
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
			returnVoList = new WXSendDao().findEmployeeDepsByDepId(corpCode, depId);
		}catch (Exception e) {
			EmpExecutionContext.error(e, "查询员工机构异常!");
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
			returnVoList = new WXSendDao().findEmpMobileByDepIds(corpCode, depId);
		}catch (Exception e) {
			EmpExecutionContext.error(e, "查询机构下的手机号码异常!");
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
		return new WXSendDao().getEmployeeByGuid(udgId, loginId, conditionMap, orderbyMap, null);
	}
	
	/**
	 *  查询出客户群组的成员    个人 / 共享
	 * @param groupId
	 * @return
	 * @throws Exception
	 */
	public List<DynaBean> findGroupClientByIds(String groupId) throws Exception{
		return new WXSendDao().findGroupClientByIds(groupId);
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
			deps = new WXSendDao().getEmpSecondDepTreeByUserIdorDepId( userId,depId);
		}catch (Exception e) {
			EmpExecutionContext.error(e, "获取员工机构查找树异常!");
		}
		return deps;
	}
	
	/**
	 * 通过员工机构id查找树
	 * @param lfSysuser
	 * @param depIds 机构Id集合字符串
	 * @param corpCode 公司编码         
	 * @return
	 * @throws Exception
	 */
	public List<LfEmployeeDep> getEmpSecondDepTreeByUserIdorDepId(String userId,String depId,String corpCode) throws Exception {
		List<LfEmployeeDep> deps = null;
		try{
			deps = new WXSendDao().getEmpSecondDepTreeByUserIdorDepId( userId,depId,corpCode);
		}catch (Exception e) {
			EmpExecutionContext.error(e, "获取员工机构查找树异常!");
		}
		return deps;
	}
	
	/**
	 * 获取网讯模板
	 * @param userId
	 * @param depId
	 * @return
	 * @throws Exception
	 */
	public List<DynaBean> getNetTemplate(LfWXBASEINFO info,PageInfo pageInfo) throws Exception {
		List<DynaBean> list = null;
		try{
			list = new WXSendDao().getNetTemplate(info,pageInfo);
		}catch (Exception e) {
			EmpExecutionContext.error(e, "获取网讯模板异常!");
		}
		return list;
	}
	
	/**
	 * 显示接收者信息
	 * 
	 * @param request
	 * @param response
	 */
	public String sendMsgInfo(String netid, String taskId) {
		
		try {
			LinkedHashMap<String, String> conditionMap = new LinkedHashMap<String, String>();
			conditionMap.put("NETID", netid);
			conditionMap.put("PARENTID", "0");
			List<LfWXPAGE> pages = empDao.findListByCondition(LfWXPAGE.class,conditionMap, null);
			if(pages == null || pages.size() == 0){
				EmpExecutionContext.error("网讯模板获取失败！");
				return null;
			}
			
			String w = CompressEncodeing.CompressNumber(pages.get(0).getID(), 6);
			
			String url = w+"-";
			
			String t = CompressEncodeing.CompressNumber(Long.valueOf(taskId), 6);
			url += t;
			
			return url;
		} catch (Exception e) {
			EmpExecutionContext.error(e, "获取网讯地址URL异常!");
			return null;
		}
		
	}
	
	/**
	 * 通过客户机构id查找树
	 * @param lfSysuser
	 * @param depIds 机构Id集合字符串
	 * @return
	 * @throws Exception
	 */
	public List<LfClientDep> getCliSecondDepTreeByUserIdorDepId(String userId,String depId) throws Exception {
		List<LfClientDep> deps = null;
		try{
			deps = new WXSendDao().getCliSecondDepTreeByUserIdorDepId(userId,depId);
		}catch (Exception e) {
			EmpExecutionContext.error(e,"通过客户机构id查找树");
		}
		return deps;
	}
	
	/**
	 * 通过客户机构id查找树
	 * @param lfSysuser
	 * @param depIds 机构Id集合字符串
	 * @param corpCode 公司编码       
	 * @return
	 * @throws Exception
	 */
	public List<LfClientDep> getCliSecondDepTreeByUserIdorDepId(String userId,String depId,String corpCode) throws Exception {
		List<LfClientDep> deps = null;
		try{
			deps = new WXSendDao().getCliSecondDepTreeByUserIdorDepId(userId,depId,corpCode);
		}catch (Exception e) {
			EmpExecutionContext.error(e,"通过客户机构id查找树");
		}
		return deps;
	}
	/**
	 *    查询客户机构人员
	 * @param clientDep	当前机构对象
	 * @param containType	是否包含  1包含   2不包含
	 * @param pageInfo	分页
	 * @return
	 * @throws Exception
	 */
	public List<DynaBean> getClientsByDepId(LfClientDep clientDep,Integer containType,PageInfo pageInfo) throws Exception{
		List<DynaBean> beanList = null;
		try{
			beanList = new WXSendDao().findClientsByDepId(clientDep, containType, pageInfo);
		}catch (Exception e) {
			beanList = null;
			EmpExecutionContext.error(e, "获取客户机构人员异常!");
		}
		return beanList;
	}
	
	
	public List<DynaBean> getClients(String name,String lgcorpcode,Integer containType,PageInfo pageInfo) throws Exception{
		List<DynaBean> beanList = null;
		try{
			beanList = new WXSendDao().findClients(name,lgcorpcode,containType, pageInfo);
		}catch (Exception e) {
			beanList = null;
			EmpExecutionContext.error(e, "查询客户群组异常!");
		}
		return beanList;
	}
	
	/**
	 * 处理群组中分页选择人员  
	 * @param groupId	群组ID
	 * @param pageInfo	分页信息
	 * @param type	员工  /客户  群组
	 * @return
	 * @throws Exception
	 */
	public List<GroupInfoVo> getGroupUser(Long groupId,PageInfo pageInfo,String type) throws Exception
	{
		List<GroupInfoVo> groupVosList = null;
		try
		{
			//查询员工群组 
			if("1".equals(type)){
				groupVosList = new WXSendDao().findGroupUserByIds(groupId,pageInfo);
			}else if("2".equals(type)){
			//查询客户群组 
				groupVosList = new WXSendDao().findGroupClientByIds(groupId, pageInfo);
			}
		} catch (Exception e){
			groupVosList = null;
			EmpExecutionContext.error(e, "获取群组分页异常!");
		}
		return groupVosList;
	}
	
	public String getClientPhoneStrByDepId(String phoneStr,String cliDepIds){
		try{
			LinkedHashMap<String, String> conditionMap = new LinkedHashMap<String, String>();
			String[] clientDepArr = cliDepIds.split(",");
			List<String> clientDepIdsList = Arrays.asList(clientDepArr);
			String noContainIds = "";
			String containIds = "";
			List<String> clientdepPathList = new ArrayList<String>();
			for(int a=0;a<clientDepIdsList.size();a++)
			{
				String id = clientDepIdsList.get(a);
				if(!"".equals(id))
				{
					if(id.contains("e"))
					{
						//处理包含子机构 
						id = id.substring(1,id.length());
						containIds = containIds + id +",";
					}else {
						//处理不包含子机构
						noContainIds = noContainIds + id+",";
					}
				}
			}
			if(!"".equals(containIds) && containIds.length()>0){
				containIds = containIds.substring(0,containIds.length()-1);
				conditionMap.clear();
				conditionMap.put("depId&in", containIds);
				List<LfClientDep> clientDepList  = empDao.findListBySymbolsCondition(LfClientDep.class, conditionMap, null);
				if(clientDepList != null && clientDepList.size()>0){
					for(int b=0;b<clientDepList.size();b++){
						clientdepPathList.add(clientDepList.get(b).getDeppath());
					}
				}
			}
			String returnClinetPhones = this.getClientPhoneByDepIds(noContainIds, clientdepPathList);
			phoneStr = phoneStr + returnClinetPhones;
		}catch (Exception e) {
			EmpExecutionContext.error(e, "获取子机构信息失败!");
		}
		return phoneStr;
	}
	
	/**
	 * 客户ID值查询客户电话号码 （方法重载，增加企业编码条件）
	 * @description    
	 * @param phoneStr
	 * @param cliDepIds
	 * @param corpCode
	 * @return       			 
	 * @author chentingsheng <cts314@163.com>
	 * @datetime 2015-2-7 下午12:03:11
	 */
	public String getClientPhoneStrByDepId(String phoneStr,String cliDepIds, String corpCode){
		try{
			LinkedHashMap<String, String> conditionMap = new LinkedHashMap<String, String>();
			String[] clientDepArr = cliDepIds.split(",");
			List<String> clientDepIdsList = Arrays.asList(clientDepArr);
			String noContainIds = "";
			String containIds = "";
			List<String> clientdepPathList = new ArrayList<String>();
			for(int a=0;a<clientDepIdsList.size();a++)
			{
				String id = clientDepIdsList.get(a);
				if(!"".equals(id))
				{
					if(id.contains("e"))
					{
						//处理包含子机构 
						id = id.substring(1,id.length());
						containIds = containIds + id +",";
					}else {
						//处理不包含子机构
						noContainIds = noContainIds + id+",";
					}
				}
			}
			if(!"".equals(containIds) && containIds.length()>0){
				containIds = containIds.substring(0,containIds.length()-1);
				conditionMap.clear();
				conditionMap.put("depId&in", containIds);
				List<LfClientDep> clientDepList  = empDao.findListBySymbolsCondition(LfClientDep.class, conditionMap, null);
				if(clientDepList != null && clientDepList.size()>0){
					for(int b=0;b<clientDepList.size();b++){
						clientdepPathList.add(clientDepList.get(b).getDeppath());
					}
				}
			}
			String returnClinetPhones = this.getClientPhoneByDepIds(noContainIds, clientdepPathList, corpCode);
			phoneStr = phoneStr + returnClinetPhones;
		}catch (Exception e) {
			EmpExecutionContext.error(e, "获取子机构信息失败!");
		}
		return phoneStr;
	}
	
	public String getClientPhoneByDepIds(String depIdStr ,List<String> depPathList){
		//存放手机号码的list
		//List<String> returnList = null;
		String phoneStr ="";
		try{
			//查询值
			/*phoneStr = new GenericEmpSpecialDAO().findClientPhoneByDepIds(depIdStr, depPathList);*/
			//phoneStr = new SameMmsDao().findClientPhoneByDepIds(depIdStr, depPathList);
			phoneStr = new WXSendDao().getClientPhoneByDepIds(depIdStr, depPathList);
			
		}catch (Exception e) {
			phoneStr = "";
			EmpExecutionContext.error(e, "获取子机构信息异常!");
		}
		//返回
		return phoneStr;
	}
	
	/**
	 * 获取子机构信息 （方法重载，增加企业编码条件）
	 * @description    
	 * @param depIdStr
	 * @param depPathList
	 * @param corpCode
	 * @return       			 
	 * @author chentingsheng <cts314@163.com>
	 * @datetime 2015-2-7 下午12:04:08
	 */
	public String getClientPhoneByDepIds(String depIdStr ,List<String> depPathList, String corpCode){
		//存放手机号码的list
		//List<String> returnList = null;
		String phoneStr ="";
		try{
			//查询值
			/*phoneStr = new GenericEmpSpecialDAO().findClientPhoneByDepIds(depIdStr, depPathList);*/
			//phoneStr = new SameMmsDao().findClientPhoneByDepIds(depIdStr, depPathList);
			phoneStr = new WXSendDao().getClientPhoneByDepIds(depIdStr, depPathList, corpCode);
			
		}catch (Exception e) {
			phoneStr = "";
			EmpExecutionContext.error(e, "获取子机构信息异常!");
		}
		//返回
		return phoneStr;
	}
	
	/**
	 *   获取机构客户 人数
	 * @param clientDep	机构对象
	 * @param containType 1包含  2是不包含
	 * @return
	 * @throws Exception
	 */
	public Integer getDepClientCount(LfClientDep clientDep,Integer containType) throws Exception{
		Integer count = 0;
		try{
			count = new WXSendDao().findClientsCountByDepId(clientDep, containType);
		}catch (Exception e) {
			EmpExecutionContext.error(e, "获取机构客户 人数异常!");
		}
		return count;
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
			conditionMap.put("l2gType&in", "1,2");
		}
		//条件
		conditionMap.put("udgId&in", udgIds);
		//GROUPBY
		String groupColum = TableLfList2gro.UDG_ID;
		//查询的列
		String columName = "count("+TableLfList2gro.L2G_ID+"),"+TableLfList2gro.UDG_ID;
		//查询
		List<String[]> countList = empDao.findListByConditionByColumNameWithGroupBy(LfList2gro.class, conditionMap, columName, null,groupColum);
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
	 * 高级设置存为默认
	 * @param conditionMap 删除原来设置条件
	 * @param lfDfadvanced 更新默认高级设置对象
	 * @return
	 */
	public String setDefault(LinkedHashMap<String, String> conditionMap, LfDfadvanced lfDfadvanced){
		String result = "fail";
		Connection conn = null;
		try {
			conn = empTransDao.getConnection();
			empTransDao.beginTransaction(conn);
			
			//删除原有的设置
			empTransDao.delete(conn, LfDfadvanced.class, conditionMap);
			
			//新增默认高级设置信息
			boolean saveResult = empTransDao.save(conn, lfDfadvanced);
			
			//成功
			if(saveResult){
				result = "success";
				empTransDao.commitTransaction(conn);
			}
			else{
				empTransDao.rollBackTransaction(conn);
			}
			return result;
		} catch (Exception e) {
			EmpExecutionContext.error(e, "高级设置存为默认异常！");
			empTransDao.rollBackTransaction(conn);
			return result;
		}
		finally{
			// 关闭连接
			if(conn != null){
				empTransDao.closeConnection(conn);
			}
		}
	}

	public List<DynaBean> getUserDrafts(LinkedHashMap<String, String> condMap, PageInfo pageInfo) {
		return this.smsSpecialDAO.getUserDrafts(condMap, pageInfo);
	}

	public List<DynaBean> getGroupMemberByNameAndId(Long userId, LinkedHashMap<String, String> conditionMap) {
		List<DynaBean> dynaBeans = new ArrayList<DynaBean>();
		try {
			dynaBeans = new WXSendDao().getGroupMemberByNameAndId(userId, conditionMap);
		}catch (Exception e){
			EmpExecutionContext.error(e, "企业网讯->静态网讯发送->选择人员->根据名字跟群组Id获取群组成员信息异常!");
		}
		return dynaBeans;
	}
}
