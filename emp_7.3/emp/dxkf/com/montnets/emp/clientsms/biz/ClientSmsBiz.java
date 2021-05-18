package com.montnets.emp.clientsms.biz;

import java.io.File;
import java.io.FileOutputStream;
import java.sql.Connection;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;

import org.apache.commons.beanutils.DynaBean;

import com.montnets.emp.clientsms.dao.LfClientSmsVoDAO;
import com.montnets.emp.common.biz.SuperBiz;
import com.montnets.emp.common.constant.CommonVariables;
import com.montnets.emp.common.constant.EMPException;
import com.montnets.emp.common.constant.IErrorCode;
import com.montnets.emp.common.constant.PreviewParams;
import com.montnets.emp.common.constant.StaticValue;
import com.montnets.emp.common.context.EmpExecutionContext;
import com.montnets.emp.common.dao.AddrBookSpecialDAO;
import com.montnets.emp.common.dao.SmsSpecialDAO;
import com.montnets.emp.common.vo.GroupInfoVo;
import com.montnets.emp.common.vo.LfCustFieldValueVo;
import com.montnets.emp.entity.client.LfClient;
import com.montnets.emp.entity.client.LfClientDep;
import com.montnets.emp.entity.client.LfCustField;
import com.montnets.emp.entity.client.LfCustFieldValue;
import com.montnets.emp.entity.clientsms.LfDfadvanced;
import com.montnets.emp.entity.group.LfList2gro;
import com.montnets.emp.entity.sysuser.LfSysuser;
import com.montnets.emp.security.blacklist.BlackListAtom;
import com.montnets.emp.table.group.TableLfList2gro;
import com.montnets.emp.util.PageInfo;
import com.montnets.emp.util.PhoneUtil;
import com.montnets.emp.util.TxtFileUtil;

public class ClientSmsBiz extends SuperBiz
{

	private final SmsSpecialDAO	smsSpecialDAO	= new SmsSpecialDAO();

	// 写文件时候要的换行符
	private final String			line			= StaticValue.systemProperty.getProperty(StaticValue.LINE_SEPARATOR);

	private final CommonVariables	cv				= new CommonVariables();

	private final PhoneUtil		phoneUtil		= new PhoneUtil();

	private final TxtFileUtil		txtFileUtil		= new TxtFileUtil();
	
	/**
	 * 获取属性值List
	 * @return
	 * @throws Exception
	 */
	
    public List<LfCustFieldValueVo> getCustVos(LfCustFieldValueVo lfCustFieldValueVo) throws Exception
	{
		List<LfCustFieldValueVo> dataVosList;
		try
		{
			dataVosList = new LfClientSmsVoDAO().findLfCustFieldValueVo(lfCustFieldValueVo);

		}
		catch (Exception e)
		{
			EmpExecutionContext.error(e,"获取属性值失败！");
			throw e;
		}
		return dataVosList;
	}

	/**
	 * 获取客户信息
	 * 
	 * @param depCode
	 * @return
	 * @throws Exception
	 */
	/*
	 * public List<String> getClientPhone(String corpcode,
	 * List<LfCustFieldValueVo> list) throws Exception {
	 * List<LfClient> clientList = null;
	 * List<String> numberList = null;
	 * try {
	 * // 获取客户信息
	 * clientList = new LfClientSmsVoDAO().findClientByCusField(corpcode, list);
	 * numberList = new ArrayList<String>();
	 * // 循环处理客户信息
	 * if (clientList != null) {
	 * for (int i = 0; i < clientList.size(); i++) {
	 * if (clientList.get(i).getMobile() == null
	 * || "".equals(clientList.get(i).getMobile().trim())) {
	 * continue;
	 * }
	 * numberList.add(clientList.get(i).getMobile());
	 * }
	 * }
	 * } catch (Exception e) {
	 * // 异常处理
	 * throw e;
	 * }
	 * return numberList;
	 * }
	 */

	/**
	 * 查询客户
	 */
	/*
	 * public List<LfClient> findClientByCusField(String corpCode,
	 * List<LfCustFieldValueVo> custFieldValueVo) throws Exception {
	 * //返回结果
	 * return new LfClientSmsVoDAO().findClientByCusField(corpCode,
	 * custFieldValueVo);
	 * }
	 */

	/**
	 *根据客户属性值查询客户
	 */
	
    public List<LfClient> findClientByCusFieldValue(String corpCode, LfCustFieldValueVo custfieldvalue) throws Exception
	{
		// 返回结果
		return new LfClientSmsVoDAO().findClientByCusFieldValue(corpCode, custfieldvalue);
	}

	// 写一个不需要分页的获取员工机构下面所有子机构的方法
	
    public List<LfClientDep> findClientDepsByDeppath(String corpCode, String deppath) throws Exception
	{
		return new LfClientSmsVoDAO().findClientDepsByDeppath(corpCode, deppath);
	}

	/**
	 * 根据机构id,企业编码查询客户信息
	 */
	
    public List<LfClient> findLfClient(String depId, String corpCode) throws Exception
	{
		return new LfClientSmsVoDAO().findLfClient(depId, corpCode);
	}

	/**
	 * 通过群组Id获取客户信息
	 * 
	 * @param udgId
	 * @param loginId
	 * @param conditionMap
	 * @return
	 * @throws Exception
	 */
	
    public List<LfClient> getClientListByUdgId(String udgId, String loginId, LinkedHashMap<String, String> conditionMap) throws Exception
	{
		LinkedHashMap<String, String> orderbyMap = new LinkedHashMap<String, String>();
		orderbyMap.put("name", StaticValue.ASC);
		return new LfClientSmsVoDAO().getClientByGuid(udgId, loginId, conditionMap, orderbyMap, null);
	}

	/**
	 * 查询客户
	 */
	
    public Long getClientCountByCusField(String corpCode, LfCustFieldValueVo custFieldValueVo) throws Exception
	{
		return new LfClientSmsVoDAO().getClientCountByCusField(corpCode, custFieldValueVo);
	}

	/**
	 * 通过客户机构id查找树
	 *        机构Id集合字符串
	 * @return
	 * @throws Exception
	 */
	
    public List<LfClientDep> getCliSecondDepTreeByUserIdorDepId(String userId, String depId) throws Exception
	{
		List<LfClientDep> deps = null;
		try
		{
			deps = new LfClientSmsVoDAO().getCliSecondDepTreeByUserIdorDepId(userId, depId);
		}
		catch (Exception e)
		{
			EmpExecutionContext.error(e,"通过客户机构ID查找客户机构树失败！");
		}
		return deps;
	}

    /**
     * 通过客户机构id查找树
     * @param sysuser 当前操作员
     * @param depId 父机构
     * @return
     * @throws Exception
     */
    public List<LfClientDep> getCliSecondDepTreeBySysUser(LfSysuser sysuser, String depId) throws Exception
    {
        List<LfClientDep> deps = null;
        try
        {
            deps = new AddrBookSpecialDAO().getCliSecondDepTreeByUserIdorDepId(sysuser.getUserId()+"",depId,sysuser.getCorpCode());
        }
        catch (Exception e)
        {
            EmpExecutionContext.error(e,"通过客户机构ID查找客户机构树失败！");
        }
        return deps;
    }

	/**
	 * 通过客户基本信息或者属性搜索客户
	 * 
	 * @param corpCode
	 * @param conditionMap
	 * @param custFieldValueList
	 *        客户属性
	 * @param pageInfo
	 * @return
	 */
	
    public List<DynaBean> findAllClientByUserAttrsOrCusField(String corpCode, LinkedHashMap<String, String> conditionMap, List<LfCustFieldValueVo> custFieldValueList, PageInfo pageInfo)
	{
		List<DynaBean> clients = null;
		try
		{
			LinkedHashMap<String, String> orderbyMap = new LinkedHashMap<String, String>();
			orderbyMap.put("name", StaticValue.ASC);
			return new LfClientSmsVoDAO().findAllClientByUserAttrsOrCusField(corpCode, conditionMap, custFieldValueList, orderbyMap, pageInfo);
		}
		catch (Exception e)
		{
			EmpExecutionContext.error(e,"通过客户属性搜索客户失败！");
		}
		return clients;
	}

	
    public List<LfCustFieldValue> findClientCustFieldValueByIds(String vids)
	{
		List<LfCustFieldValue> custfieldValueList = null;
		String ids = handleIds(vids);
		try
		{
			return new LfClientSmsVoDAO().findClientCustFieldValueByIds(ids);
		}
		catch (Exception e)
		{
			EmpExecutionContext.error(e,"根据客户属性查找客户属性值失败！");
		}
		return custfieldValueList;
	}

	/**
	 * 等到符合条件的ids,如下：
	 * 
	 * @param vids
	 *        (4,6,7,8,9,16;17,,,,,,,,,,,,)
	 * @return ids(4,6,7,8,9,16,17)
	 */
	
    public String handleIds(String vids)
	{
		String ids = "";
		String[] arrayIds = vids.split(",|;");
		for (String id : arrayIds)
		{
			id = id.trim();
			if(id != null && id.matches("[1-9]\\d*"))
			{
				if("".equals(ids))
				{
					ids = id;
				}
				else
				{
					ids = ids + "," + id;
				}
			}
		}
		return ids;
	}

	/**
	 * 查询出客户群组中的信息
	 * 
	 * @param groupId
	 * @param pageInfo
	 * @return
	 * @throws Exception
	 */
	
    public List<GroupInfoVo> getCliGroupUser(Long groupId, String epname, PageInfo pageInfo) throws Exception
	{
		List<GroupInfoVo> groupVosList = null;
		try
		{
			// 查询客户群组
			groupVosList = new LfClientSmsVoDAO().findGroupClientByIds(groupId, epname, pageInfo);
		}
		catch (Exception e)
		{
			groupVosList = null;
			EmpExecutionContext.error(e,"查询客户群组的信息失败！");
		}
		return groupVosList;
	}
	
	/**
	 * 查询出客户群组中的信息
	 * 
	 * @param groupId
	 * @param pageInfo
	 * @return
	 * @throws Exception
	 */
	public List<GroupInfoVo> getCliGroupUser(Long groupId, String epname, PageInfo pageInfo,String corpCode) throws Exception
	{
		List<GroupInfoVo> groupVosList = null;
		try
		{
			// 查询客户群组
			groupVosList = new LfClientSmsVoDAO().findGroupClientByIds(groupId, epname, pageInfo,corpCode);
		}
		catch (Exception e)
		{
			groupVosList = null;
			EmpExecutionContext.error(e,"查询客户群组的信息失败！");
		}
		return groupVosList;
	}

	/**
	 * 计算员工/客户群组中的人数
	 * 
	 * @param udgIds
	 *        群组IDS
	 * @param type
	 *        群组类型 1是员工 2是客户群组
	 * @return
	 * @throws Exception
	 */
	
    public Map<String, String> getGroupCount(String udgIds, String type) throws Exception
	{
		HashMap<String, String> countMap = new HashMap<String, String>();
		LinkedHashMap<String, String> conditionMap = new LinkedHashMap<String, String>();
		// 群组类型 1是员工 2是客户群组
		if("1".equals(type))
		{
			// 添加类型(员工0,客户1,手工2)
			conditionMap.put("l2gType&in", "0,2");
		}
		else if("2".equals(type))
		{
			// 添加类型(员工0,客户1,手工2)
			conditionMap.put("l2gType&in", "1,2");
		}
		// 条件
		conditionMap.put("udgId&in", udgIds);
		// GROUPBY
		String groupColum = TableLfList2gro.UDG_ID;
		// 查询的列
		String columName = "count(" + TableLfList2gro.L2G_ID + ")," + TableLfList2gro.UDG_ID;
		// 查询
		List<String[]> countList = empDao.findListByConditionByColumNameWithGroupBy(LfList2gro.class, conditionMap, columName, null, groupColum);
		if(countList != null && countList.size() > 0)
		{
			for (int i = 0; i < countList.size(); i++)
			{
				String[] countArr = countList.get(i);
				countMap.put(countArr[1], countArr[0]);
			}
		}
		return countMap;
	}

	/**
	 * 查询客户机构人员
	 * 
	 * @param clientDep
	 *        当前机构对象
	 * @param containType
	 *        是否包含 1包含 2不包含
	 * @param pageInfo
	 *        分页
	 * @return
	 * @throws Exception
	 */
	
    public List<DynaBean> getClientsByDepId(LfClientDep clientDep, LfClient client, Integer containType, PageInfo pageInfo) throws Exception
	{
		List<DynaBean> beanList = null;
		try
		{
			beanList = new LfClientSmsVoDAO().findClientsByDepId(clientDep, client, containType, pageInfo);
		}
		catch (Exception e)
		{
			beanList = null;
			EmpExecutionContext.error(e,"查询客户机构下的人员失败！");
		}
		return beanList;
	}

	/**
	 * 获取机构客户 人数
	 * 
	 * @param clientDep
	 *        机构对象
	 * @param containType
	 *        1包含 2是不包含
	 * @return
	 * @throws Exception
	 */
	
    public Integer getDepClientCount(LfClientDep clientDep, Integer containType) throws Exception
	{
		Integer count = 0;
		try
		{
			count = new LfClientSmsVoDAO().findClientsCountByDepId(clientDep, containType);
		}
		catch (Exception e)
		{
			EmpExecutionContext.error(e,"获取客户机构下的人数失败！");
		}
		return count;
	}

	/**
	 * serlvet中处理客户机构的整合
	 * @return
	 */
	
    public void getClientPhoneStrByDepId(List<String> phoneList, String cliDepIds)
	{
		try
		{
			LinkedHashMap<String, String> conditionMap = new LinkedHashMap<String, String>();
			cliDepIds = cliDepIds.substring(1, cliDepIds.length());
			String[] clientDepArr = cliDepIds.split(",");
			List<String> clientDepIdsList = Arrays.asList(clientDepArr);
			String noContainIds = "";
			String containIds = "";
			List<String> clientdepPathList = new ArrayList<String>();
			for (int a = 0; a < clientDepIdsList.size(); a++)
			{
				String id = clientDepIdsList.get(a).trim();
				if(!"".equals(id))
				{
					if(id.contains("e"))
					{
						// 处理包含子机构
						id = id.substring(1, id.length());
						containIds = containIds + id + ",";
					}
					else
					{
						// 处理不包含子机构
						noContainIds = noContainIds + id + ",";
					}
				}
			}
			if("".equals(noContainIds) && "".equals(containIds))
			{
				return;
			}
			if(!"".equals(containIds) && containIds.length() > 0)
			{
				containIds = containIds.substring(0, containIds.length() - 1);
				conditionMap.clear();
				conditionMap.put("depId&in", containIds);
				List<LfClientDep> clientDepList = empDao.findListBySymbolsCondition(LfClientDep.class, conditionMap, null);
				if(clientDepList != null && clientDepList.size() > 0)
				{
					for (int b = 0; b < clientDepList.size(); b++)
					{
						clientdepPathList.add(clientDepList.get(b).getDeppath());
					}
				}
			}
			new LfClientSmsVoDAO().getClientPhoneByDepIds(phoneList,noContainIds, clientdepPathList);
		}
		catch (Exception e)
		{
			EmpExecutionContext.error(e,"处理客户机构整合失败！");
		}
	}

	/**
	 * 客户的高级搜索。
	 * 
	 * @param client
	 *        客户
	 * @param conditionMap
	 *        时间条件
	 * @param custFieldValueList
	 *        属性条件
	 * @param pageInfo
	 *        分页
	 * @return
	 */
	
    public List<DynaBean> findAdvancedSearchClient(LfClient client, LinkedHashMap<String, String> conditionMap, List<LfCustFieldValueVo> custFieldValueList, PageInfo pageInfo)
	{
		List<DynaBean> clients = null;
		try
		{
			return new LfClientSmsVoDAO().findAdSearchClient(client, conditionMap, custFieldValueList, pageInfo);
		}
		catch (Exception e)
		{
			EmpExecutionContext.error(e,"高级搜索查询客户失败！");
		}
		return clients;
	}

	/**
	 * 根据机构id,获取该机构和该机构的子机构
	 * 
	 * @param depId
	 * @param corpCode
	 * @return
	 */
	
    public List<LfClientDep> findDepIdsAndSelf(String depId, String corpCode)
	{
		List<LfClientDep> clientDeps = null;
		try
		{
			return new LfClientSmsVoDAO().findDepIdsAndSelf(depId, corpCode);
		}
		catch (Exception e)
		{
			EmpExecutionContext.error(e, "获取该机构和该机构的子机构失败");
		}
		return clientDeps;
	}

	/**
	 * 查询该客户所对应的机构名称
	 * 
	 * @param clientid
	 *        客户ID
	 * @return
	 */
	
    public String getDepNameById(Long clientid)
	{
		String depname = "";
		try
		{
			depname = new LfClientSmsVoDAO().getDepnameByClineId(clientid);
		}
		catch (Exception e)
		{
			EmpExecutionContext.error(e,"查找客户机构对应的机构名称失败！");
			depname = "";
		}
		return depname;
	}

	/**
	 * 获取该企业的区域
	 * 
	 * @param corpCode
	 * @return
	 */
	
    public List<DynaBean> getAreas(String corpCode)
	{
		List<DynaBean> beans = null;
		try
		{
			beans = new LfClientSmsVoDAO().getClientArea(corpCode);
		}
		catch (Exception e)
		{
			EmpExecutionContext.error(e,"获取客户区域失败！");
		}
		return beans;
	}

	/**
	 * 获取客户手机号
	 * 
	 * @param corpcode
	 *        企业编码
	 * @param conditionsql
	 *        查询条件
	 * @param unChioceUserIds
	 *        未选中的客户ID字符串
	 * @return
	 */
	
    public void getClientByConditionSql(List<String> phoneList, String corpcode, String conditionsql, String unChioceUserIds)
	{
		try
		{
			List<DynaBean> clientBeans = new LfClientSmsVoDAO().getClientByConditionSql(corpcode, conditionsql, unChioceUserIds);
			if(clientBeans != null && clientBeans.size() > 0)
			{
				for (DynaBean bean : clientBeans)
				{
					phoneList.add(String.valueOf(bean.get("mobile")));
				}
			}
		}
		catch (Exception e)
		{
			EmpExecutionContext.error(e, "高级搜索查询失败！");
		}
	}

	/**
	 * 获取微信用户
	 * 
	 * @param corpcode
	 *        企业编码
	 * @return
	 */
	
    public List<DynaBean> getWxuserByCorpCode(String corpcode, String ename)
	{
		List<DynaBean> wxuser = null;
		try
		{
			wxuser = new LfClientSmsVoDAO().getWxuser(corpcode, ename);
		}
		catch (Exception e)
		{
			wxuser = null;
			EmpExecutionContext.error(e,"获取微信用户失败！");
		}
		return wxuser;
	}

	/**
	 * 获取客户信息
	 * @return
	 * @throws Exception
	 */
	
    public int getClientCountByFieldRef(String corpcode, String fieldRef) throws Exception
	{
		int count = 0;
		List<LfClient> clientList = null;
		try
		{
			// 获取客户信息
			clientList = smsSpecialDAO.findClientByFieldRef(corpcode, fieldRef);
			count = clientList == null ? 0 : clientList.size();
		}
		catch (Exception e)
		{
			EmpExecutionContext.error(e,"获取客户信息失败！");
			// 异常处理
			throw e;
		}
		return count;
	}

	/**
	 * 根据客户属性值ID，查找客户的手机号码，手机号码以字符串逗号分隔。
	 * 
	 * @param corpCode
	 *        企业编码
	 * @param proIdValueStr
	 *        客户属性值ID以半角逗号隔开
	 * @return
	 */
	
    public void getClientByProValueIdStr(List<String> phoneList, String corpCode, String proIdValueStr)
	{
		try
		{
			// 通过群组获取电话号码字符串
			if(proIdValueStr != null && proIdValueStr.length() > 1)
			{
				EmpExecutionContext.info("[客户群组群发]选择的客户属性值ID为:"+proIdValueStr+"。");
				String[] proIds = proIdValueStr.substring(1, proIdValueStr.length() - 1).split(",");
				LfCustFieldValueVo custField = null;
				List<LfClient> clientList = null;
				for (String string : proIds)
				{
					String[] str = string.split("&");
					custField = new LfCustFieldValueVo();
					custField.setField_Ref(str[0]);
					custField.setId(Long.parseLong(str[1]));
					clientList = findClientByCusFieldValue(corpCode, custField);
					if(clientList != null && clientList.size() > 0)
					{
						for (LfClient client : clientList)
						{
							phoneList.add(client.getMobile());
						}
					}

				}
			}
		}
		catch (Exception e)
		{
			// 异常处理
			EmpExecutionContext.error(e, "查询数据失败！");
		}
	}

	/**
	 * 根据客户属性ID，查找客户的手机号码，手机号码以字符串逗号分隔。
	 * 
	 * @param corpCode
	 *        企业编码
	 * @param proIdStr
	 *        客户属性ID以半角逗号隔开
	 * @return
	 */
	
    public void getClientByProIdStr(List<String> phoneList, String corpCode, String proIdStr)
	{
		try
		{
			// 通过群组获取电话号码字符串
			if(proIdStr != null && proIdStr.length() > 1)
			{
				EmpExecutionContext.info("[客户群组群发]选择的客户属性ID为:"+proIdStr+"。");
				// 去掉结尾的逗号
				String[] proIds = proIdStr.substring(1, proIdStr.length() - 1).split(",");
				SmsSpecialDAO smsSpecialDAO = new SmsSpecialDAO();
				List<LfClient> lfClientList = null;
				for (String string : proIds)
				{
					lfClientList = smsSpecialDAO.findClientByFieldRef(corpCode, string);
					if(lfClientList != null && lfClientList.size() > 0)
					{
						for (LfClient client : lfClientList)
						{
							phoneList.add(client.getMobile());
						}
					}
				}
			}
		}
		catch (Exception e)
		{
			// 异常处理
			EmpExecutionContext.error(e, "查询数据失败！");
		}
	}

	/**
	 * 根据群组ID，查找客户的手机号码，手机号码以字符串逗号分隔。
	 * 
	 * @param groupStr
	 *        群组ID以半角逗号隔开
	 * @return
	 * @throws Exception
	 */
	
    public void getClientByGroupStr(List<String> phoneList, String groupStr) throws Exception
	{
		try
		{
			// 通过群组获取电话号码字符串
			if(groupStr != null && groupStr.length() > 1)
			{
				// 去掉结尾的逗号
				String udgId = groupStr.substring(1, groupStr.length() - 1);
				String[] udgs = udgId.split(",");
				List<GroupInfoVo> lfClientList = null;
				PageInfo pageInfo = new PageInfo();
				pageInfo.setPageSize(Integer.MAX_VALUE);
				for (String str : udgs)
				{
					lfClientList = getCliGroupUser(Long.valueOf(str), "", pageInfo);
					if(lfClientList != null && lfClientList.size() > 0)
					{
						for (GroupInfoVo client : lfClientList)
						{
							phoneList.add(client.getMobile());
						}
					}
				}
			}
		}
		catch (Exception e)
		{
			// 异常处理
			EmpExecutionContext.error(e, "查询数据失败！");
		}

	}
	
	/**
	 * 根据群组ID，查找客户的手机号码，手机号码以字符串逗号分隔。
	 * 
	 * @param groupStr
	 *        群组ID以半角逗号隔开
	 * @return
	 * @throws Exception
	 */
	public void getClientByGroupStr(List<String> phoneList,String groupStr,String corpCode) throws Exception
	{
		try
		{
			// 通过群组获取电话号码字符串
			if(groupStr != null && groupStr.length() > 1)
			{
				// 去掉结尾的逗号
				String udgId = groupStr.substring(1, groupStr.length() - 1);
				EmpExecutionContext.info("[客户群组群发]选择的客户群组ID为:"+udgId+"。");
				String[] udgs = udgId.split(",");
				List<GroupInfoVo> lfClientList = null;
				PageInfo pageInfo = new PageInfo();
				pageInfo.setPageSize(Integer.MAX_VALUE);
				for (String str : udgs)
				{
					lfClientList = getCliGroupUser(Long.valueOf(str), "", pageInfo,corpCode);
					if(lfClientList != null && lfClientList.size() > 0)
					{
						for (GroupInfoVo client : lfClientList)
						{
							phoneList.add(client.getMobile());
						}
					}
				}
			}
		}
		catch (Exception e)
		{
			// 异常处理
			EmpExecutionContext.error(e, "查询数据失败！");
		}

	}
	
	/**
	 * 根据群组ID，查找客户的手机号码，手机号码以字符串逗号分隔。
	 *
	 * @return
	 * @throws Exception
	 */
	public void getClientByYdywGroupStr(List<String> phoneList,String tcCodes,String corpCode) throws Exception
	{
		try
		{
			// 通过群组获取电话号码字符串
			if(tcCodes != null && tcCodes.trim().length() > 2)
			{
				// 去掉结尾的逗号
				String tcCodeStr = tcCodes.substring(1, tcCodes.length() - 1);
				EmpExecutionContext.info("[客户群组群发]选择的套餐编号为："+tcCodeStr);
				String[] tcCodeArr = tcCodeStr.split(",");
				//套餐编码查询条件
				String tcCodesCondition="";
				if(tcCodeArr!=null&&tcCodeArr.length>0){
					for (int i = 0; i < tcCodeArr.length; i++)
					{
						if(tcCodeArr[i]!=null&&!"".equals(tcCodeArr[i].trim())){
							tcCodesCondition+="'"+tcCodeArr[i].trim()+"',";
						}
					}
					if(tcCodesCondition!=null&&tcCodesCondition.length()>0){
						tcCodesCondition = tcCodesCondition.substring(0, tcCodesCondition.length() - 1);
					}
					if(tcCodesCondition!=null&&!"".equals(tcCodesCondition.trim())){
						List<DynaBean> ydywGroupMemberList =new LfClientSmsVoDAO().getYdywGroupMember(tcCodesCondition,corpCode);
							if(ydywGroupMemberList != null && ydywGroupMemberList.size() > 0)
							{
								for (DynaBean ydywGroupMember : ydywGroupMemberList)
								{
									phoneList.add(String.valueOf(ydywGroupMember.get("mobile")));
								}
							}
					}else{
						EmpExecutionContext.error("查询发送手机号码，传入套餐编号存在问题！tcCodes:"+tcCodes);
					}
				}
			}else{
				EmpExecutionContext.error("查询发送手机号码，传入套餐编号存在问题！tcCodes:"+tcCodes);
			}
		}
		catch (Exception e)
		{
			// 异常处理
			EmpExecutionContext.error(e, "查询签约用户手机号码失败！");
		}

	}

	// 得到扩展的客户属性的具体值的list
	
    public List<LfCustFieldValue> getValueVo(Long fieldID)
	{

		List<LfCustFieldValue> dataVoList = new ArrayList<LfCustFieldValue>();
		LinkedHashMap<String, String> conditionMap = new LinkedHashMap<String, String>();
		try
		{
			conditionMap.put("field_ID", fieldID.toString());
			// 根据id获取属性值列表
			dataVoList = empDao.findListByCondition(LfCustFieldValue.class, conditionMap, null);

		}
		catch (Exception e)
		{
			EmpExecutionContext.error(e, "查询数据失败！");
		}
		return dataVoList;
	}

	/**
	 * @description 得到扩展的客户属性list
	 * @return
	 * @throws Exception
	 */
	
    public List<LfCustField> getdatalist(String corpCode) throws Exception
	{
		// 得到扩展的客户属性list
		List<LfCustField> dataList = new ArrayList<LfCustField>();
		LinkedHashMap<String, String> conditionMap = new LinkedHashMap<String, String>();
		LinkedHashMap<String, String> orderbyMap = new LinkedHashMap<String, String>();
		try
		{
			conditionMap.put("corp_code", corpCode);
			orderbyMap.put("field_Name", "asc");
			// 找出所有的自定义属性列表
			dataList = empDao.findListByCondition(LfCustField.class, conditionMap, orderbyMap);
		}
		catch (Exception e)
		{
			EmpExecutionContext.error(e, "查询数据失败！");
		}
		return dataList;
	}

	// 判断一个机构是否被包含在其它机构
	
    public boolean isDepAcontainsDepB(String depIdAs, String depIdB, String corpCode)
	{
		boolean result = false;
		List<LfClientDep> lfEmployeeDepList = new ArrayList<LfClientDep>();
		LinkedHashSet<Long> depIdSet = new LinkedHashSet<Long>();
		// String[] depIdAsTemp =depIdAs.split(",");
		LinkedHashMap<String, String> conditionMap = new LinkedHashMap<String, String>();
		conditionMap.put("depId&in", depIdAs);
		try
		{
			List<LfClientDep> clientDeps = empDao.findListBySymbolsCondition(LfClientDep.class, conditionMap, null);
			for (LfClientDep lfClientDep : clientDeps)
			{
				if(lfClientDep.getDeppath() != null && !"".equals(lfClientDep.getDeppath()))
				{
					lfEmployeeDepList = findClientDepsByDeppath(corpCode, lfClientDep.getDeppath());
					for (int i = 0; i < lfEmployeeDepList.size(); i++)
					{
						depIdSet.add(lfEmployeeDepList.get(i).getDepId());
					}
				}
			}
			result = depIdSet.contains(Long.valueOf(depIdB));
		}
		catch (Exception e)
		{
			EmpExecutionContext.error(e, "查询数据失败！");
		}
		return result;
	}

	/**
	 * 解析号码，检验号码合法，过滤黑名单、过滤重号
	 *
	 * @param haoduan
	 * @param lguserid
	 * @param corpCode
	 * @throws Exception
	 */
	
    public void parsePhone(List<String> phoneList, PreviewParams preParams, String[] haoduan, String lguserid, String corpCode, String busCode, String empLangName) throws Exception
	{
		String illegalFormat = "zh_HK".equals(empLangName)?"Illegal format：":"格式非法：";
		String blacklistNumber = "zh_HK".equals(empLangName)?"Blacklist number：":"zh_TW".equals(empLangName)?"黑名單號碼：":"黑名单号码：";
		String repeatedNumber = "zh_HK".equals(empLangName)?"Repeated number：":"zh_TW".equals(empLangName)?"重複號碼：":"重复号码：";
		// 运营商有效号码数
		int[] oprValidPhone = preParams.getOprValidPhone();
		// 运营商标识。0:移动号码 ;1:联通号码;2:电信号码
		int oprIndex = 0;
		
		// 过滤黑名单业务类
		BlackListAtom blackBiz = new BlackListAtom();
		// 发送号码字符串
		StringBuffer contentSb = new StringBuffer();
		// 非法号码字符串
		StringBuffer badContentSb = new StringBuffer();
		//每批次的有效号码数，在该批号码写入文件后重置为0
		int perEffCount = 0;
		//每批次的无效号码数，在该批号码写入文件后重置为0
		int perBadCount = 0;
		//号码返回状态
		int resultStatus = 0;
		//有效号码文件
		File perEffFile = null;
		//无效号码文件
		File perBadFile = null;
		//合法号码文件流
		FileOutputStream perEffOS = null;
		//无效号码文件流
		FileOutputStream perBadOS = null;
		try
		{
			// 过滤黑名单号码，过滤重复号码，过滤格式非法号码
			if(phoneList!=null&&phoneList.size()>0)
			{
				String mobile=null;
				for (int i=0;i<phoneList.size();i++)
				{
					//获取手机号码
					mobile=phoneList.get(i);
					
					//手机号码去空格  modify by tanglili 20140807
					if(mobile!=null){
						mobile=mobile.trim();
					}
					
					if(mobile != null && mobile.length()>6&&mobile.length() < 22)
					{
						// 提交总数加1
						preParams.setSubCount(preParams.getSubCount() + 1);

						if(blackBiz.checkBlackList(corpCode, mobile, busCode))
						{
							// 过滤黑名单
							badContentSb.append(blacklistNumber).append(mobile).append(line);
							// 黑名单数加1
							preParams.setBlackCount(preParams.getBlackCount() + 1);
							// 非法数加1
							preParams.setBadCount(preParams.getBadCount() + 1);
							perBadCount ++;
							continue;
						}
						else if((oprIndex = phoneUtil.getPhoneType(mobile, haoduan)) < 0)
						{
							// 过滤格式非法号码
							badContentSb.append(illegalFormat).append(mobile).append(line);
							// 格式非法数加1
							preParams.setBadModeCount(preParams.getBadModeCount() + 1);
							// 非法数加1
							preParams.setBadCount(preParams.getBadCount() + 1);
							perBadCount ++;
							continue;
						}
						else if((resultStatus = phoneUtil.checkRepeat(mobile, preParams.getValidPhone())) != 0)
						{
							//返回1为重复号码
							if(resultStatus == 1)
							{
								badContentSb.append(repeatedNumber).append(mobile).append(line);
								// 重复号码数加1
								preParams.setRepeatCount(preParams.getRepeatCount() + 1);
								// 非法数加1
								preParams.setBadCount(preParams.getBadCount() + 1);
								perBadCount ++;
								continue;
							}
							else
							{
								// 过滤格式非法号码
								badContentSb.append(illegalFormat).append(mobile).append(line);
								// 格式非法数加1
								preParams.setBadModeCount(preParams.getBadModeCount() + 1);
								// 非法数加1
								preParams.setBadCount(preParams.getBadCount() + 1);
								perBadCount ++;
								continue;
							}
						}

						// 将发送号码添加到字符串中，到时用于写文件。
						contentSb.append(mobile).append(line);
						// 有效号码数目加1
						preParams.setEffCount(preParams.getEffCount() + 1);
						perEffCount ++;
						
						// 累加运营商有效号码数(区分运营商)
						//oprIndex = smsBiz.getSmsPhoneOperator(mobile, haoduan);
						oprValidPhone[oprIndex] += 1;

						// 拼接预览的手机号码
						if(preParams.getEffCount() < 11)
						{
							// 如果是隐藏号码权限，则将手机号码做处理，以免预览看到手机号码。
							if(mobile != null && !"".equals(mobile) && preParams.getIshidephone() == 0)
							{
								mobile = cv.replacePhoneNumber(mobile);
							}
							// 拼接手机号码
							preParams.setPreviewPhone(preParams.getPreviewPhone() + mobile + StaticValue.MSG_SPLIT_CHAR + oprIndex + ";");
						}
					}
					
					// 一万条存储一次
					if (perEffCount >= StaticValue.PER_PHONE_NUM)
					{
						if(perEffFile == null)
						{
							//有效号码文件
							perEffFile = new File(preParams.getPhoneFilePath()[0]);
							//判断文件是否存在，不存在就新建一个
							if (!perEffFile.exists())
							{
                                boolean flag = perEffFile.createNewFile();
                                if (!flag) {
                                    EmpExecutionContext.error("创建文件失败！");
                                }
							}
						}
						if(perEffOS == null)
						{
							//合法号码文件输出流
							perEffOS = new FileOutputStream(preParams.getPhoneFilePath()[0], true);
						}
						//写入有效号码文件
						txtFileUtil.repeatWriteToTxtFile(perEffOS, contentSb.toString());
						//txtFileUtil.writeToTxtFile(preParams.getPhoneFilePath()[0], contentSb.toString());
						contentSb.setLength(0);
						perEffCount = 0;
					}
					if (perBadCount >= StaticValue.PER_PHONE_NUM)
					{
						if(perBadFile == null)
						{
							//非法号码文件
							perBadFile = new File(preParams.getPhoneFilePath()[2]);
							//判断文件是否存在，不存在就新建一个
							if (!perBadFile.exists())
							{
								boolean state = perBadFile.createNewFile();
								if(!state){
									EmpExecutionContext.error("创建文件失败");
								}
							}
						}
						if(perBadOS == null)
						{
							//非法号码文件输出流
							perBadOS = new FileOutputStream(preParams.getPhoneFilePath()[2], true);
						}
						//写入非法号码写文件
						txtFileUtil.repeatWriteToTxtFile(perBadOS, badContentSb.toString());
						//txtFileUtil.writeToTxtFile(preParams.getPhoneFilePath()[2], badContentSb.toString());
						badContentSb.setLength(0);
						perBadCount = 0;
					}
				}
			}
			
			// 设置各运营商有效号码数
			preParams.setOprValidPhone(oprValidPhone);
			
			// 如果有剩余有效号码，写有效号码文件
			if(contentSb.length() > 0)
			{
				if(perEffFile == null)
				{
					//有效号码文件
					perEffFile = new File(preParams.getPhoneFilePath()[0]);
					//判断文件是否存在，不存在就新建一个
					if (!perEffFile.exists())
					{
						boolean state = perEffFile.createNewFile();
						if(!state){
							EmpExecutionContext.error("创建文件失败");
						}

					}
				}
				if(perEffOS == null)
				{
					//合法号码文件输出流
					perEffOS = new FileOutputStream(preParams.getPhoneFilePath()[0], true);
				}
				// 剩余的有效号码写文件
				txtFileUtil.repeatWriteToTxtFile(perEffOS, contentSb.toString());
				//txtFileUtil.writeToTxtFile(preParams.getPhoneFilePath()[0], contentSb.toString());
				contentSb.setLength(0);
			}
			// 如果有剩余非法号码，写非法号码文件
			if(badContentSb.length() > 0)
			{
				if(perBadFile == null)
				{
					//非法号码文件
					perBadFile = new File(preParams.getPhoneFilePath()[2]);
					//判断文件是否存在，不存在就新建一个
					if (!perBadFile.exists())
					{
						boolean state = perBadFile.createNewFile();
						if(!state){
							EmpExecutionContext.error("创建文件失败");
						}
					}
				}
				if(perBadOS == null)
				{
					//非法号码文件输出流
					perBadOS = new FileOutputStream(preParams.getPhoneFilePath()[2], true);
				}
				// 剩余的非法号码写文件
				txtFileUtil.repeatWriteToTxtFile(perBadOS, badContentSb.toString());
				//txtFileUtil.writeToTxtFile(preParams.getPhoneFilePath()[2], badContentSb.toString());
				badContentSb.setLength(0);
			}
		}
		catch (EMPException e)
		{
			txtFileUtil.deleteFile(preParams.getPhoneFilePath()[0]);
			EmpExecutionContext.error(e, lguserid, corpCode);
			throw e;
		}
		catch (Exception e)
		{
			txtFileUtil.deleteFile(preParams.getPhoneFilePath()[0]);
			EmpExecutionContext.error(e, lguserid, corpCode);
			throw new EMPException(IErrorCode.B20005, e);
		}
		finally
		{
			if(perEffOS != null)
			{
				try
				{
					perEffOS.close();
				}
				catch (Exception e)
				{
					EmpExecutionContext.error(e, "客户群组群发，关闭有效号码文件输入流异常！");
				}
			}
			if(perBadOS != null)
			{
				try
				{
					perBadOS.close();
				}
				catch (Exception e)
				{
					EmpExecutionContext.error(e, "客户群组群发，关闭无效号码文件输入流异常！");
				}
			}
		}
	}
	
	/**
	 * 将手机号码转换成数组，并且添加到集合中去。
	 * @param phoneList
	 * @param phoneStr
	 */
	
    public void phoneStrAddList(List<String> phoneList, String phoneStr){
		//将字符串分割成字符串数组
		String[] phoneArr=phoneStr.split(",");
		//判断数组是否为空
		if(phoneArr!=null){
			//获取数组的大小
			int phoneArrSize=phoneArr.length;
			//如果数组大小大于0，则代表有手机号码
			if(phoneArrSize>0){
				for (int i = 0; i < phoneArrSize; i++)
				{
					//将手机号码添加到结合中去。
					phoneList.add(phoneArr[i]);
				}
			}
		}
	}
	
	public Map<String,String> getYdywGroupMemberCount(String tcCodes,String corpCode){
		//定义一个Map  key群组ID value群组对应的成员数量
		HashMap<String, String> countMap = new HashMap<String, String>();
		try
		{
			//查询出一个动态bean
			List<DynaBean>  groupMenberCountList = new LfClientSmsVoDAO().getYdywGroupMemberCount(tcCodes,corpCode);
			//动态bean不为空并且动态bean的size大于0，则进行循环，否则countMap就为空的map了。
			if(groupMenberCountList != null && groupMenberCountList.size()>0)
			{
				for(DynaBean groupMenberCount:groupMenberCountList)
				{
					//key为群组ID,Value为群组成员数量
					countMap.put(String.valueOf(groupMenberCount.get("taocancode")),String.valueOf(groupMenberCount.get("membercount")));
				}
			}
		}
		catch (Exception e)
		{
			EmpExecutionContext.error(e, "查询出套餐的成员数量异常！tcCodes:" + tcCodes );
		}
		return countMap;
	}
	
	/**
	 * 根据签约ID获取签约ID和签约账号的MAP集合
	 * @param contractIDs  签约ID字符串
	 * @return
	 */
	public Map<String,String> getAccountNoByContractIds(String contractIDs){
		HashMap<String, String> contractMap = new HashMap<String, String>();
		try{
			List<DynaBean> contractBeanList=new LfClientSmsVoDAO().getAccountNoByContractIds(contractIDs);
			if(contractBeanList != null && contractBeanList.size()>0)
			{
				for(DynaBean contractBean:contractBeanList)
				{
					contractMap.put(String.valueOf(contractBean.get("contract_id")),String.valueOf(contractBean.get("acct_no")));
				}
			}
		}catch(Exception e){
			EmpExecutionContext.error(e, "根据签约ID获取签约ID和签约账号的MAP集合失败！");
		}
		return contractMap;
	}
	
	
	/**
	 * serlvet中处理客户机构的整合(重载方法)
	 * @param phoneList
	 * @param cliDepIds
	 * @param corpCode
	 */
	public void getClientPhoneStrByDepId(List<String> phoneList, String cliDepIds, String corpCode)
	{
		if(cliDepIds!=null&&cliDepIds.length() > 1&&!",".equals(cliDepIds)){
			try
			{
				
				LinkedHashMap<String, String> conditionMap = new LinkedHashMap<String, String>();
				cliDepIds = cliDepIds.substring(1, cliDepIds.length());
				String[] clientDepArr = cliDepIds.split(",");
				List<String> clientDepIdsList = Arrays.asList(clientDepArr);
				String noContainIds = "";
				String containIds = "";
				List<String> clientdepPathList = new ArrayList<String>();
				if(clientDepIdsList!=null&&clientDepIdsList.size()>0){
					for (int a = 0; a < clientDepIdsList.size(); a++)
					{
						String id =clientDepIdsList.get(a);
						if(id!=null && !"".equals(id.trim())){
								id=id.trim();
								if(id.contains("e"))
								{
									// 处理包含子机构
									if(id!=null&&id.length()>1){
										id = id.substring(1, id.length());
										containIds = containIds + id + ",";
									}
								}
								else
								{
									// 处理不包含子机构
									noContainIds = noContainIds + id + ",";
								}
							
						}else{
							EmpExecutionContext.error("客户群组群发机构ID为空！");
						}
					}
				}else{
					EmpExecutionContext.error("客户群组群发机构ID的集合为空或者小于1！");
					return ;
				}
				EmpExecutionContext.info("[客户群组群发]包含子机构的机构ID为:"+containIds+";不包含子机构的机构ID为:"+noContainIds+"。");
				if("".equals(noContainIds) && "".equals(containIds))
				{
					return;
				}
				if(!"".equals(containIds) && containIds.length() > 0)
				{
					containIds = containIds.substring(0, containIds.length() - 1);
					conditionMap.clear();
					conditionMap.put("depId&in", containIds);
					List<LfClientDep> clientDepList = empDao.findListBySymbolsCondition(LfClientDep.class, conditionMap, null);
					if(clientDepList != null && clientDepList.size() > 0)
					{
						for (int b = 0; b < clientDepList.size(); b++)
						{
							clientdepPathList.add(clientDepList.get(b).getDeppath());
						}
					}else{
						EmpExecutionContext.error("客户群组群发选择机构发送，不存在此机构，机构ID为:"+containIds);
						return;
					}
				}else{
					//没有包含子机构的机构
					clientdepPathList=null;
				}
				if(noContainIds==null||"".equals(noContainIds.trim())){
					//EmpExecutionContext.error("不包含子机构的机构ID为空！");
					noContainIds=null;
				}
				if(clientdepPathList==null||clientdepPathList.size()<1){
					//EmpExecutionContext.error("包含子机构的机构ID为空！");
					clientdepPathList=null;
				}
				//调用带企业编码的方法
				new LfClientSmsVoDAO().getClientPhoneByDepIds(phoneList,noContainIds, clientdepPathList,corpCode);
			}
			catch (Exception e)
			{
				EmpExecutionContext.error(e,"处理客户机构整合失败！");
			}
		}else{
			EmpExecutionContext.error("客户群组群发机构ID为空！");
			return;
		}
	}
	
	
	/**
	 * 客户的高级搜索。(重载方法)
	 * 
	 * @param client
	 *        客户
	 * @param conditionMap
	 *        时间条件
	 * @param custFieldValueList
	 *        属性条件
	 * @param pageInfo
	 *        分页
	 * @return
	 */
	public List<DynaBean> findAdvancedSearchClientNew(LfClient client, LinkedHashMap<String, String> conditionMap, List<LfCustFieldValueVo> custFieldValueList, PageInfo pageInfo)
	{
		List<DynaBean> clients = null;
		try
		{
			return new LfClientSmsVoDAO().findAdSearchClientNew(client, conditionMap, custFieldValueList, pageInfo);
		}
		catch (Exception e)
		{
			EmpExecutionContext.error(e,"高级搜索查询客户失败！");
		}
		return clients;
	}
	
	/**
	 * 获取客户手机号
	 * 
	 * @param corpcode
	 *        企业编码
	 * @param conditionsql
	 *        查询条件
	 * @param unChioceUserIds
	 *        未选中的客户ID字符串
	 * @return
	 */
	public void getClientByConditionSqlNew(List<String> phoneList,String corpcode, String conditionsql, String unChioceUserIds)
	{
		try
		{
			List<DynaBean> clientBeans = new LfClientSmsVoDAO().getClientByConditionSqlNew(corpcode, conditionsql, unChioceUserIds);
			if(clientBeans != null && clientBeans.size() > 0)
			{
				for (DynaBean bean : clientBeans)
				{
					phoneList.add(String.valueOf(bean.get("mobile")));
				}
			}
		}
		catch (Exception e)
		{
			EmpExecutionContext.error(e, "高级搜索查询失败！");
		}
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
				result = "seccuss";
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
}
