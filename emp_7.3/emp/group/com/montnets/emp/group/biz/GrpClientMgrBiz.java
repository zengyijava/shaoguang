package com.montnets.emp.group.biz;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.beanutils.DynaBean;

import com.montnets.emp.common.constant.StaticValue;
import com.montnets.emp.common.context.EmpExecutionContext;
import com.montnets.emp.common.dao.AddrBookSpecialDAO;
import com.montnets.emp.common.vo.GroupInfoVo;
import com.montnets.emp.entity.client.LfClient;
import com.montnets.emp.entity.client.LfClientDep;
import com.montnets.emp.entity.group.LfList2gro;
import com.montnets.emp.entity.sysuser.LfSysuser;
import com.montnets.emp.group.dao.GenericGroupInfoVoDAO;
import com.montnets.emp.group.dao.GroupManagerDao;
import com.montnets.emp.group.vo.LfClientVo;
import com.montnets.emp.group.vo.LfEmployeeVo;
import com.montnets.emp.group.vo.LfList2groVo;
import com.montnets.emp.table.group.TableLfList2gro;
import com.montnets.emp.util.PageInfo;

public class GrpClientMgrBiz extends GrpBaseMgrBiz{
	
	
//数据库操作dao
	
	private GenericGroupInfoVoDAO groupInfoVoDAO;
	public GrpClientMgrBiz()
	{
		//初始化数据库操作dao
		groupInfoVoDAO = new GenericGroupInfoVoDAO();
	}
	/**
	 * 获取群组成员信息
	 * @param loginUserId	登录者用户ID
	 * @param groupInfoVo	群组成员VO
	 * @param pageInfo		分页信息
	 * @return
	 * @throws Exception
	 */
	public List<GroupInfoVo> getGroupVos(Long loginUserId,
			GroupInfoVo groupInfoVo, PageInfo pageInfo) throws Exception
	{
		//群组对象集合
		List<GroupInfoVo> groupVosList;
		try
		{
			//查询
			groupVosList = groupInfoVoDAO.findGroupInfoVo(loginUserId,
					groupInfoVo, pageInfo);
		} catch (Exception e)
		{
			//异常处理
			EmpExecutionContext.error(e,"获取群组成员信息失败 ！");
			throw e;
		}
		//返回
		return groupVosList;
	}
	
	
	/**
	 * 通过机构ids查询通讯录信息
	 * 
	 * @param loginUserId
	 * @param employeeVo
	 * @param pageInfo
	 * @return
	 * @throws Exception
	 */
	public List<LfEmployeeVo> findEmployeeVoByDepIds(Long loginUserId,
			String corpCode, LfEmployeeVo employeeVo, PageInfo pageInfo)
			throws Exception
	{
		List<LfEmployeeVo> employeeVosList;
		try
		{
			if (pageInfo == null)
			{
				employeeVosList = new GroupManagerDao().findEmployeeVoByDepIds(
						loginUserId, corpCode, employeeVo);
			} else
			{
				employeeVosList = new GroupManagerDao().findEmployeeVoByDepIds(
						loginUserId, corpCode, employeeVo, pageInfo);
			}
		} catch (Exception e)
		{
			EmpExecutionContext.error(e, "通过机构查询通讯录信息异常！");
			throw e;
		}
		return employeeVosList;
	}
	
	
	
	/**
	 * 通过客户机构id查找树
	 * @param userId
	 * @param depId 机构Id
	 * @return
	 * @throws Exception
	 */
	public List<LfClientDep> getCliSecondDepTreeByUserIdorDepId(String userId,String depId) throws Exception {
		List<LfClientDep> deps = null;
		try{
			deps =new GroupManagerDao().getCliSecondDepTreeByUserIdorDepId(userId,depId);
		}catch (Exception e) {
			EmpExecutionContext.error(e,"查询客户机构树失败！");
		}
		return deps;
	}

    /**
     * 获取当前操作员权限下 当前机构下的机构树
     * @param sysuser
     * @param depId
     * @return
     * @throws Exception
     */
    public List<LfClientDep> getCliSecondDepTreeByUserIdorDepId(LfSysuser sysuser,String depId) throws Exception {
        List<LfClientDep> deps = null;
        try{
            deps =new GroupManagerDao().getCliSecondDepTreeByUserIdorDepId(sysuser,depId);
        }catch (Exception e) {
            EmpExecutionContext.error(e,"查询客户机构树失败！");
        }
        return deps;
    }
	
	/**
	 * 通过机构ids查询客户通讯录信息
	 * 
	 * @param loginUserId
     * @param corpCode
	 * @param clientVo
	 * @param pageInfo
	 * @return
	 * @throws Exception
	 */
	public List<LfClientVo> findClientVoByDepIds(Long loginUserId,
			String corpCode, LfClientVo clientVo, PageInfo pageInfo)
			throws Exception
	{
		List<LfClientVo> clientVosList;
		try
		{
			if (pageInfo == null)
			{
				clientVosList = new GroupManagerDao().findClientVoByDepIds(
						loginUserId, corpCode, clientVo);
			} else
			{
				clientVosList = new GroupManagerDao().findClientVoByDepIds(
						loginUserId, corpCode, clientVo, pageInfo);
			}
		} catch (Exception e)
		{
			EmpExecutionContext.error(e,"通过机构查询客户通讯录信息异常！");
			throw e;
		}
		return clientVosList;
	}
	
	
	/**
	 * 获取员工个数
	 * @param udgIds
	 * @return
	 * @throws Exception
	 */
	public Map<String,String> getClientCount(String udgIds) throws Exception
	{
		HashMap<String, String> countMap = new HashMap<String, String>();
		LinkedHashMap<String, String> conditionMap = new LinkedHashMap<String, String>();
		
		conditionMap.put("udgId&in", udgIds);
		conditionMap.put("l2gType&in", "1,2");
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
	 * 通过群组Id获取员工信息,个人群组模块使用
	 * @param udgId
	 * @param loginId
	 * @param conditionMap
	 * @return
	 * @throws Exception
	 */
	public List<LfClient> getClientByGuidForGroup(String udgId,String loginId,LinkedHashMap<String, String> conditionMap) throws Exception
	{
		LinkedHashMap<String, String> orderbyMap = new LinkedHashMap<String, String>();
		orderbyMap.put("name", StaticValue.ASC);
		return new AddrBookSpecialDAO().getClientByGuid(udgId, loginId, conditionMap, orderbyMap, null);
	}
	
	/**
	 *  查询客户群组的详情 
	 * @param vo
	 * @param udgid
	 * @return
	 * @throws Exception
	 */
	public List<LfList2groVo> getClientShowMember(LfList2groVo vo,Long udgid) throws Exception
	{
		 List<LfList2groVo> list = null;
		try{
			list =  new GroupManagerDao().findCliShowMember(vo, udgid);
		}catch (Exception e) {
			EmpExecutionContext.error(e,"获取客户群组详情失败！");
			list = null;
		}
		return list;
	}

	/**
	 * 查询客户群组的详情 分页方法
	 * @param pageInfo
	 * @param vo
	 * @param udgid
	 * @return
	 * @throws Exception
	 */
	public List<LfList2groVo> getClientShowMember(PageInfo pageInfo,LfList2groVo vo,Long udgid) throws Exception
	{
		List<LfList2groVo> list = null;
		try{
			list =  new GroupManagerDao().findCliShowMember(pageInfo,vo, udgid);
		}catch (Exception e) {
			EmpExecutionContext.error(e,"获取客户群组详情失败！");
			list = null;
		}
		return list;
	}
	
	/**
	 *    查询客户机构人员
	 * @param clientDep	当前机构对象
	 * @param containType	是否包含  1包含   2不包含
	 * @param pageInfo	分页
	 * @return
	 * @throws Exception
	 */
	public List<DynaBean> getClientsByDepId(LfClientDep clientDep,LfClient client,Integer containType,PageInfo pageInfo) throws Exception{
		List<DynaBean> beanList = null;
		try{
			beanList = new GroupManagerDao().findClientsByDepId(clientDep, client, containType, pageInfo);
		}catch (Exception e) {
			beanList = null;
			EmpExecutionContext.error(e,"查询客户机构人员失败！");
		}
		return beanList;
	}
	/**
	 *   获取机构客户 人数
	 * @param clientDep	机构对象
	 * @param containType 1包含  2是不包含
	 * @return
	 * @throws Exception
	 */
	public  Integer getDepClientCount(LfClientDep clientDep,Integer containType) throws Exception{
		Integer count = 0;
		try{
			count = new GroupManagerDao().findClientsCountByDepId(clientDep, containType);
		}catch (Exception e) {
			EmpExecutionContext.error(e,"获取客户机构人数失败！");
		}
		return count;
	}
	
	
	/**
	 *   serlvet中处理客户机构的整合
	 * @param cliDepIds
	 * @return
	 */
	public String getClientPhoneStrByDepId(String cliDepIds){
		String returnClinetPhones = "";
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
			returnClinetPhones = new GroupManagerDao().getClientPhoneByDepIds(noContainIds, clientdepPathList);
		}catch (Exception e) {
			returnClinetPhones = "";
			EmpExecutionContext.error(e,"处理客户机构中客户的手机号码出现异常！");
		}
		return returnClinetPhones;
	}
	
	
	
	
	
}
