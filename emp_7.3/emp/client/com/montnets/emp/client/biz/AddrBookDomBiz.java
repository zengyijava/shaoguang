package com.montnets.emp.client.biz;

import java.util.ArrayList;
import java.util.List;

import com.montnets.emp.client.dao.GenericAddrBookPermissionsVoDAO;
import com.montnets.emp.client.vo.AddrBookPermissionsVo;
import com.montnets.emp.common.constant.CommonVariables;
import com.montnets.emp.entity.employee.LfEmployeeDep;
import com.montnets.emp.entity.sysuser.LfSysuser;
import com.montnets.emp.util.PageInfo;

public class AddrBookDomBiz
{
	
	private GenericAddrBookPermissionsVoDAO addrBookPermissionsVoDAO;
	
	public AddrBookDomBiz(){
		addrBookPermissionsVoDAO=new GenericAddrBookPermissionsVoDAO();
	}
	/**
	 * 
	 * @param loginUserId
	 * @param addrBookPermissionsVo
	 * @param pageInfo
	 * @return
	 * @throws Exception
	 */
	public List<AddrBookPermissionsVo> getEmpBindPermissionsList(Long loginUserId, String corpCode,AddrBookPermissionsVo addrBookPermissionsVo, PageInfo pageInfo) throws Exception {
		return addrBookPermissionsVoDAO.getEmpBindPermissionsList(loginUserId, corpCode,addrBookPermissionsVo, pageInfo);
	}
	
	public List<AddrBookPermissionsVo> getEmpBindPermissionsListByDepIds(Long loginUserId, String corpCode,AddrBookPermissionsVo addrBookPermissionsVo, PageInfo pageInfo) throws Exception {
		return addrBookPermissionsVoDAO.getEmpBindPermissionsListByDepIds(loginUserId, corpCode,addrBookPermissionsVo, pageInfo);
	}
	
	/**
	 * 
	 * @param loginUserId
	 * @param addrBookPermissionsVo
	 * @param pageInfo
	 * @return
	 * @throws Exception
	 */
	public List<AddrBookPermissionsVo> getClientBindPermissionsList(Long loginUserId,String corpCode, AddrBookPermissionsVo addrBookPermissionsVo, PageInfo pageInfo) throws Exception {
		return addrBookPermissionsVoDAO.getClientBindPermissionsList(loginUserId, corpCode, addrBookPermissionsVo, pageInfo);
	}
	
	/**
	 * 
	 * @param loginUserId
	 * @param addrBookPermissionsVo
	 * @param pageInfo
	 * @return
	 * @throws Exception
	 */
	public List<LfSysuser> getEmpUnBindPermissionsSysuserList(Long loginUserId, String corpCode,AddrBookPermissionsVo addrBookPermissionsVo, PageInfo pageInfo) throws Exception {
		return addrBookPermissionsVoDAO.getEmpUnBindPermissionsSysuserList(loginUserId, corpCode,addrBookPermissionsVo, pageInfo);
	}
	
	/**
	 * 
	 * @param loginUserId
	 * @param addrBookPermissionsVo
	 * @param pageInfo
	 * @return
	 * @throws Exception
	 */
	public List<LfSysuser> getClientUnBindPermissionsSysuserList(Long loginUserId,String cropcode, AddrBookPermissionsVo addrBookPermissionsVo, PageInfo pageInfo) throws Exception {
		return addrBookPermissionsVoDAO.getClientUnBindPermissionsSysuserList(loginUserId,cropcode, addrBookPermissionsVo, pageInfo);
	}
	
	/**
	 * ???????????????????????????????????????ID??????
	 * @param depList
	 * @param depId
	 * @param cv
	 * @param depIds
	 */
	private void diguiEmployeeDep(List<LfEmployeeDep> depList,String depId,CommonVariables cv,String depIds)
	{
		List<LfEmployeeDep> depChildList = new ArrayList<LfEmployeeDep>();
		for(int i=0;i<depList.size();i++)
		{
			LfEmployeeDep empDep = depList.get(i);
			if(empDep.getParentId().toString().equals(depId))
			{
				depChildList.add(empDep);
				depList.remove(i);
				i--;
			}
		}
		
		for(LfEmployeeDep empDep : depChildList)
		{
			diguiEmployeeDep(depList,empDep.getDepId().toString(),cv,depIds);
			depIds = cv.getDepIdStrs() + "," + empDep.getDepId().toString();
			cv.setDepIdStrs(depIds);
		}
		cv.setDepIdStrs(depIds);
	}
	
	
	
	/**
	 *  ?????????????????????????????????
	 * @param loginUserId	???????????????ID
	 * @param depPath	????????????
	 * @param corpCode	????????????
	 * @param name	???????????????
	 * @param pageInfo	????????????
	 * @return
	 * @throws Exception
	 */
	public List<AddrBookPermissionsVo> getEmpBindPermissionsList(Long loginUserId,String depPath, String corpCode,String name,PageInfo pageInfo) throws Exception {
		return addrBookPermissionsVoDAO.getPermissionsVo(loginUserId,depPath, corpCode,name,pageInfo);
	}
	
	/**
	 *  ?????????????????????????????????
	 * @param loginUserId	???????????????ID
	 * @param depPath	????????????
	 * @param corpCode	????????????
	 * @param name	???????????????
	 * @param pageInfo	????????????
	 * @return
	 * @throws Exception
	 */
	public List<AddrBookPermissionsVo> getClientBindPermissionsList(Long loginUserId,String depPath, String corpCode,String name,PageInfo pageInfo) throws Exception {
		return addrBookPermissionsVoDAO.getPermissionsVo2(loginUserId,depPath, corpCode,name,pageInfo);
	}	
	
	
	
	
}
