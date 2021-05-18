package com.montnets.emp.client.biz;

import java.sql.Connection;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

import org.apache.commons.beanutils.DynaBean;

import com.montnets.emp.client.dao.AddrBookSuperDAO;
import com.montnets.emp.common.biz.SuperBiz;
import com.montnets.emp.common.constant.StaticValue;
import com.montnets.emp.common.context.EmpExecutionContext;
import com.montnets.emp.common.dao.AddrBookSpecialDAO;
import com.montnets.emp.entity.client.LfClient;
import com.montnets.emp.entity.employee.LfEmployee;
import com.montnets.emp.entity.group.LfMalist;
import com.montnets.emp.entity.segnumber.PbServicetype;
import com.montnets.emp.entity.sysuser.LfDep;
import com.montnets.emp.entity.sysuser.LfSysuser;
import com.montnets.emp.util.PageInfo;

public class AddrBookBiz extends SuperBiz
{
	
	protected AddrBookSpecialDAO  addrbookSpecialDAO ;
	
	public AddrBookBiz(){
		
		addrbookSpecialDAO = new AddrBookSpecialDAO();
	}
	
	/**
	 * 	查询操作员的审批人员
	 * @param depId	机构ID
	 * @return
	 * @throws Exception
	 */
	public List<LfSysuser> getDomSysuserByDepId(String depId)throws Exception
	{
		List<LfSysuser> userList = null;
		try
		{
			  userList = addrbookSpecialDAO.findDomSysuserByDepID(depId.toString(), null);
		} catch (Exception e)
		{
			EmpExecutionContext.error(e,"查询操作员的审批人员失败！");
			throw e;
		}
		return userList;
	}
	
	/**
	 * 	保存机构相关信息
	 * @param depCode	
	 * @param bizCode
	 * @param depType
	 * @return
	 * @throws Exception
	 */
	public Integer addEnterpriseProc(String depCode, String bizCode,Integer depType) throws Exception {
		Integer result = -1;
		Connection conn = empTransDao.getConnection();
		try{
		 
			empTransDao.beginTransaction(conn);
			result = empTransDao.saveEnterpriseProc(conn, depCode, bizCode,depType);
			empTransDao.commitTransaction(conn);
		}catch(Exception e){
			EmpExecutionContext.error(e,"保存机构相关信息失败！");
			result = -1;
			empTransDao.rollBackTransaction(conn);
			throw e;
		}finally{
			empTransDao.closeConnection(conn);
		}
		return result;
	}
	
	/**
	 * 返回所有的操作员机构
	 * @param userId
	 * @return
	 * @throws Exception
	 */
	public List<LfDep> getAllDeps(Long userId) throws Exception
	{
		List<LfDep> depList = new ArrayList<LfDep>();
		try
		{
			//设置序列
			LinkedHashMap<String,String> orderbyMap = new LinkedHashMap<String,String>(); 
			orderbyMap.put("deppath","asc");
			//查询 
			depList = addrbookSpecialDAO.findDomDepBySysuserID(userId.toString(), orderbyMap);

		} catch (Exception e)
		{
			EmpExecutionContext.error(e,"查询所有的操作员机构失败！");
			throw e;
		}
		return depList;
	}
	
	
	/**
	 * 返回所有的操作员机构
	 * @param userId
	 * @return
	 * @throws Exception
	 */
	public List<LfDep> getAllDeps(Long userId,String corpCode) throws Exception
	{
		List<LfDep> depList = new ArrayList<LfDep>();
		try
		{
			//设置序列
			LinkedHashMap<String,String> orderbyMap = new LinkedHashMap<String,String>(); 
			orderbyMap.put("deppath","asc");
			//查询 
			depList = addrbookSpecialDAO.findDomDepBySysuserID(userId.toString(),corpCode, orderbyMap);

		} catch (Exception e)
		{
			EmpExecutionContext.error(e,"查询所有的操作员机构失败！");
			throw e;
		}
		return depList;
	}
	
	/**
	 * 	获取操作员下的所有操作员
	 * @param userId
	 * @return
	 * @throws Exception
	 */
	public List<LfSysuser> getAllUserByCurUserId(Long userId) throws Exception {
		LinkedHashMap<String, String> orderbyMap = new LinkedHashMap<String, String>();
		orderbyMap.put("deppath", "asc");
		List<LfDep> depsList = addrbookSpecialDAO.findDomDepBySysuserID(userId
				.toString(), orderbyMap);
		if (depsList == null || depsList.size() == 0) {
			return null;
		}
		String strDepIds = "";
		for (int i = 0; i < depsList.size(); i++) {
			strDepIds += depsList.get(i).getDepCodeThird() + ",";
			if (strDepIds == null || strDepIds.trim().length() == 0) {
				continue;
			}
		}
		if (strDepIds != null && strDepIds.trim().length() != 0) {
			strDepIds = strDepIds.substring(0, strDepIds.lastIndexOf(","));
		}

		LinkedHashMap<String, String> conditionMap = new LinkedHashMap<String, String>();
		conditionMap.put("depCode&in", strDepIds);
		List<LfSysuser> usersList = empDao.findListBySymbolsCondition(
				LfSysuser.class, conditionMap, null);
		return usersList;
	}
	
	/**
	 * 	获取操作员下的所有操作员
	 * @param userId
	 * @return
	 * @throws Exception
	 */
	public List<LfSysuser> getAllUserByCurUserId(Long userId,String corpCode) throws Exception {
		LinkedHashMap<String, String> orderbyMap = new LinkedHashMap<String, String>();
		orderbyMap.put("deppath", "asc");
		List<LfDep> depsList = addrbookSpecialDAO.findDomDepBySysuserID(userId
				.toString(),corpCode, orderbyMap);
		if (depsList == null || depsList.size() == 0) {
			return null;
		}
		String strDepIds = "";
		for (int i = 0; i < depsList.size(); i++) {
			strDepIds += depsList.get(i).getDepCodeThird() + ",";
			if (strDepIds == null || strDepIds.trim().length() == 0) {
				continue;
			}
		}
		if (strDepIds != null && strDepIds.trim().length() != 0) {
			strDepIds = strDepIds.substring(0, strDepIds.lastIndexOf(","));
		}

		LinkedHashMap<String, String> conditionMap = new LinkedHashMap<String, String>();
		conditionMap.put("depCode&in", strDepIds);
		List<LfSysuser> usersList = empDao.findListBySymbolsCondition(
				LfSysuser.class, conditionMap, null);
		return usersList;
	}
	
	/**
	 * 获得所有用户信息
	 * @param userId
	 * @return
	 * @throws Exception
	 */
	public List<LfSysuser> getAllSysusers(Long userId) throws Exception {
		List<LfSysuser> lfSysuserList = new ArrayList<LfSysuser>();
		try {
			//查询
			if (null != userId)
				lfSysuserList = addrbookSpecialDAO.findDomUserBySysuserID(userId
						.toString());

		} catch (Exception e) {
			//异常
			EmpExecutionContext.error(e,"获得所有用户信息失败！");
			throw e;
		}
		return lfSysuserList;
	}
	
	/**获取所有的 号段
	 * 
	 * @return
	 * @throws Exception
	 */
	public String[] getHaoduan() throws Exception
	{
		String[] haoduans = new String[3];
		LinkedHashMap<String, String> conditionMap = new LinkedHashMap<String, String>();
		try
		{
			List<PbServicetype> haoduanList = empDao.findListByCondition(
					PbServicetype.class, conditionMap, null);
			for (int i = 0; i < haoduanList.size(); i++)
			{
				PbServicetype pbSer = haoduanList.get(i);
				if (pbSer.getSpisuncm() == 0)
				{
					haoduans[0] = pbSer.getServiceno();
				} else if (pbSer.getSpisuncm() == 1)
				{
					haoduans[1] = pbSer.getServiceno();
				} else if (pbSer.getSpisuncm() == 21)
				{
					haoduans[2] = pbSer.getServiceno();
				}
			}
		} catch (Exception e)
		{
			EmpExecutionContext.error(e,"获取所有的 号段失败！");
			throw e;
		}
		return haoduans;
	}

	/**
	 *  检测号码
	 * @param mobile
	 * @param haoduan
	 * @return
	 * @throws Exception
	 */
	public int checkMobile(String mobile, String[] haoduan) throws Exception
	{
		if (mobile.length() != 11)
			return 0;
		for (int k = mobile.length(); --k >= 0;)
		{
			if (!Character.isDigit(mobile.charAt(k)))
			{
				return 0;
			}
		}

		String number = haoduan[0] + "," + haoduan[1] + "," + haoduan[2];
		if (number.replace(mobile.substring(0, 3), "").length() == number
				.length())
		{
			return 0;
		}
		return 1;
	}
	/**
	 * 	检测号手机 号码
	 * @param mobile	手机号码
	 * @param spiscumu
	 * @param haoduan	号段
	 * @return
	 * @throws Exception
	 */
	public int checkMobile(String mobile, Integer spiscumu, String[] haoduan)
			throws Exception
	{
		if (mobile.length() != 11)
			return 0;
		for (int k = mobile.length(); --k >= 0;)
		{
			if (!Character.isDigit(mobile.charAt(k)))
			{
				return 0;
			}
		}
		String number = "";
		if (spiscumu - 0 == 0)
		{
			number = haoduan[0];
		} else if (spiscumu - 1 == 0)
		{
			number = haoduan[1];
		} else if (spiscumu - 21 == 0)
		{
			number = haoduan[2];
		}
		if (number.replace(mobile.substring(0, 3), "").length() == number
				.length())
		{
			return 0;
		}
		return 1;
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
		return addrbookSpecialDAO.getEmployeeByGuid(udgId, loginId, conditionMap, orderbyMap, null);
	}
	
	/**
	 * 通过群组Id获取客户信息
	 * @param udgId
	 * @param loginId
	 * @param conditionMap
	 * @return
	 * @throws Exception
	 */
	public List<LfClient> getClientListByUdgId(String udgId,String loginId,LinkedHashMap<String, String> conditionMap) throws Exception
	{
		LinkedHashMap<String, String> orderbyMap = new LinkedHashMap<String, String>();
		orderbyMap.put("name", StaticValue.ASC);
		return addrbookSpecialDAO.getClientByGuid(udgId, loginId, conditionMap, orderbyMap, null);
	}
	/**
	 * 通过群组Id获取员工信息,个人群组模块使用
	 * @param udgId
	 * @param loginId
	 * @param conditionMap
	 * @return
	 * @throws Exception
	 */
	public List<LfEmployee> getEmployeeByGuidForGroup(String udgId,String loginId,LinkedHashMap<String, String> conditionMap) throws Exception
	{
		LinkedHashMap<String, String> orderbyMap = new LinkedHashMap<String, String>();
		orderbyMap.put("name", StaticValue.ASC);
		return addrbookSpecialDAO.getEmployeeByGuidForGroup(udgId, loginId, conditionMap, orderbyMap, null);
	}
	
	/**
	 * 通过群组Id获取自定义通讯录内容
	 * @param udgId 群组Id
	 * @param loginId 操作员ID
	 * @param conditionMap 查询条件
	 * @param shareType 共享类型
	 * @return
	 * @throws Exception
	 */
	public List<LfMalist> getMalistListByUdgId(String udgId,String loginId,LinkedHashMap<String, String> conditionMap,Integer shareType) throws Exception
	{
		LinkedHashMap<String, String> orderbyMap = new LinkedHashMap<String, String>();
		orderbyMap.put("name", StaticValue.ASC);
		return addrbookSpecialDAO.getMalistByGuid(udgId, loginId, conditionMap, orderbyMap, null,shareType);
	}
	
	/**
	 * 通过guid获取客户签约信息
	 * @author WANGRUBIN
	 * @datatime 2015-1-20 上午11:04:22
	 * @description TODO 
	 * @param guid
	 * @param conditionMap
	 * @param pageInfo
	 * @return
	 */
	public List<DynaBean> getContractList(String guid,LinkedHashMap<String, String> conditionMap,PageInfo pageInfo) throws Exception {
		// TODO Auto-generated method stub
		return new AddrBookSuperDAO().getContractList(guid,conditionMap,pageInfo);
	}

	/**
	 * 根据guid集合，获取相关的签约客户套餐信息
	 * @author WANGRUBIN
	 * @datatime 2015-1-20 上午11:05:05
	 * @description TODO 
	 * @param contractIds
	 * @return
	 */
	public List<DynaBean> getContractRaocanList(String contractIds) throws Exception {
		// TODO Auto-generated method stub
		return new AddrBookSuperDAO().getContractRaocanList(contractIds);
	}
}
