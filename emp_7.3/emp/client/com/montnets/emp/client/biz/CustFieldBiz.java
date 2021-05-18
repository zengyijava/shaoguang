package com.montnets.emp.client.biz;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

import com.montnets.emp.client.dao.GenericClientGroupInfoVoDAO;
import com.montnets.emp.client.dao.GenericCustFieldVoDAO;
import com.montnets.emp.client.vo.CustFieldValueVo;
import com.montnets.emp.common.biz.SuperBiz;
import com.montnets.emp.common.context.EmpExecutionContext;
import com.montnets.emp.common.dao.AddrBookSpecialDAO;
import com.montnets.emp.common.vo.GroupInfoVo;
import com.montnets.emp.entity.client.LfCustField;
import com.montnets.emp.entity.client.LfCustFieldValue;
import com.montnets.emp.entity.group.LfList2gro;
import com.montnets.emp.entity.group.LfUdgroup;
import com.montnets.emp.util.PageInfo;


/**
 * 
 * @author Administrator
 * 
 */
public class CustFieldBiz extends SuperBiz
{
	//数据库操作dao
	private GenericCustFieldVoDAO custFieldVoDAO;
	//数据库操作dao
	private GenericClientGroupInfoVoDAO groupInfoVoDAO;
	private AddrBookSpecialDAO empSpecialDAO;

	public CustFieldBiz()
	{
		custFieldVoDAO = new GenericCustFieldVoDAO();
		groupInfoVoDAO = new GenericClientGroupInfoVoDAO();
		empSpecialDAO = new AddrBookSpecialDAO();
	}

	/**
	 * 获取属性值List
	 * 
	 * @param custFieldValueVo
	 * @param pageInfo
	 * @return
	 * @throws Exception
	 */
	public List<CustFieldValueVo> getCustVos(CustFieldValueVo custFieldValueVo,
			PageInfo pageInfo) throws Exception
	{
		//属性值对象集合
		List<CustFieldValueVo> dataVosList;
		try
		{
			//获取记录
			dataVosList = custFieldVoDAO.findCustInfoVo(custFieldValueVo,
					pageInfo);

		} catch (Exception e)
		{
			//异常处理
			EmpExecutionContext.error(e,"获取属性值出现异常！");
			throw e;
		}
		//返回结果
		return dataVosList;
	}

	/**
	 * 修改属性值
	 * 
	 * @param fieldID
	 * @param fieldValue
	 * @return
	 * @throws Exception
	 */
	public boolean updateCustFieldValue(String ID, String fieldValue)
			throws Exception
	{
		//结果
		boolean op = false;
		//排序
		LinkedHashMap<String, String> objectMap = new LinkedHashMap<String, String>();
		//条件
		LinkedHashMap<String, String> conditionMap = new LinkedHashMap<String, String>();

		try
		{
			objectMap.put("field_Value", fieldValue);
			conditionMap.put("id", ID);
			//执行更新
			op = empDao.update(LfCustFieldValue.class, objectMap, conditionMap);

		} catch (Exception e)
		{
			//异常处理
			EmpExecutionContext.error(e,"修改属性值出现异常！");
			throw e;
		}
		//返回结果
		return op;
	}

	/**
	 * 删除单个属性值
	 * 
	 * @param ID
	 * @return
	 * @throws Exception
	 */
	public int delCustFieldValue(String ID) throws Exception
	{
		//结果
		int result = 0;
		//条件
		LinkedHashMap<String, String> conditionMap = new LinkedHashMap<String, String>();
		try
		{
			//id
			conditionMap.put("id&in", ID);
			//执行删除
			result = empDao.delete(LfCustFieldValue.class, conditionMap);
		} catch (Exception e)
		{
			//异常处理
			EmpExecutionContext.error(e,"删除单个属性值出现异常！");
			throw e;
		}
		//返回结果
		return result;
	}

	/**
	 * 
	 * @param udgId
	 * @param personId
	 * @param l2gType
	 * @return
	 * @throws Exception
	 */
	public Integer addList2gro(String udgId, String[] personId, String l2gType)
			throws Exception
	{
		//结果
		Integer re = 0;
		List<LfList2gro> lfList2groList = new ArrayList<LfList2gro>();
		AddrBookBaseBiz baseBiz = new AddrBookBaseBiz();
		//条件
		LinkedHashMap<String, String> conditionMap = new LinkedHashMap<String, String>();
		try
		{
			for (int i = 0; i < personId.length; i++)
			{
				conditionMap.put("guId", personId[i]);
				conditionMap.put("udgId", udgId);
				//获取记录
				List<LfList2gro> r = baseBiz.getByCondition(LfList2gro.class,
						conditionMap, null);
				if (r != null && r.size() > 0)
				{
				} else
				{
					//初始化群组记录对象
					LfList2gro lfList2gro = new LfList2gro();
					lfList2gro.setGuId(Long.parseLong(personId[i]));
					lfList2gro.setL2gType(new Integer(l2gType));
					lfList2gro.setUdgId(new Integer(udgId));
					lfList2groList.add(lfList2gro);
				}
				conditionMap.clear();
			}
			if (lfList2groList.size() > 0)
			{
				//保存到数据库
				re = empDao.save(lfList2groList, LfList2gro.class);
			}
			if (re != personId.length)
			{
				re = -1;
			}
		} catch (Exception e)
		{
			//异常处理
			EmpExecutionContext.error(e,"新增群组关联表出现异常！");
			re = 0;
			throw e;
		}
		//返回结果
		return re;
	}

	/**
	 * 
	 * @param udgId
	 * @return 删除客户属性
	 * @throws Exception
	 */
	public Integer delGroupsAllInfo(String udgId) throws Exception
	{
		//id不能为空
		if (udgId == null)
		{
			//返回
			return null;
		}
		//删除群组
		this.delGroupsRecords(udgId);
		//执行删除
		Integer result = empDao.delete(LfCustField.class, udgId);
		//返回结果
		return result;
	}

	/**
	 * 
	 * @param udgId
	 * @returnd 删除客户属性值
	 * @throws Exception
	 */
	private Integer delGroupsRecords(String udgId) throws Exception
	{
		//结果
		Integer result = 0;
		//条件
		LinkedHashMap<String, String> conditionMap = new LinkedHashMap<String, String>();
		try
		{
			//id
			conditionMap.put("field_ID", udgId);
			//执行删除
			result = empDao.delete(LfCustFieldValue.class, conditionMap);
		} catch (Exception e)
		{
			//异常处理
			EmpExecutionContext.error(e,"删除客户属性值出现异常！");
			throw e;
		}
		//返回结果
		return result;
	}

	/**
	 * 
	 * @param userId
	 * @return
	 * @throws Exception
	 */
	public List<LfUdgroup> getGroupsByUserId(Long userId) throws Exception
	{
		//操作员id不能为空
		if (userId == null)
		{
			//返回
			return null;
		}
		//群组对象集合
		List<LfUdgroup> groupsList;
		try
		{
			//获取记录
			groupsList = empSpecialDAO.findUdgroupInfo(userId.toString());
		} catch (Exception e)
		{
			//异常处理
			EmpExecutionContext.error(e,"获取群组成员信息出现异常！");
			throw e;
		}
		//返回结果
		return groupsList;
	}

	/**
	 * 
	 * @param groupInfoVo
	 * @return
	 * @throws Exception
	 */
	public List<GroupInfoVo> getGroupInfoVos(GroupInfoVo groupInfoVo)
			throws Exception
	{
		//群组对象不能为空
		if (groupInfoVo == null)
		{
			//返回空
			return null;
		}
		//群组对象集合
		List<GroupInfoVo> groupVosList;
		try
		{
			//获取记录
			groupVosList = groupInfoVoDAO.findGroupInfoVo(groupInfoVo);
		} catch (Exception e)
		{
			//异常处理
			EmpExecutionContext.error(e,"获取群组成员信息出现异常！");
			throw e;
		}
		//返回结果
		return groupVosList;
	}

	/**
	 * 
	 * @param loginUserId
	 * @param groupInfoVo
	 * @param pageInfo
	 * @return
	 * @throws Exception
	 */
	public List<GroupInfoVo> getLikeGroupVos(Long loginUserId,
			GroupInfoVo groupInfoVo, PageInfo pageInfo) throws Exception
	{
		//群组对象集合
		List<GroupInfoVo> groupVosList;
		try
		{
			//获取记录
			groupVosList = groupInfoVoDAO.findLikeGroupInfoVo(loginUserId,
					groupInfoVo, pageInfo);

		} catch (Exception e)
		{
			//异常处理
			EmpExecutionContext.error(e,"获取群组成员信息出现异常！");
			throw e;
		}
		//返回结果
		return groupVosList;
	}

	/**
	 * 
	 * @param l2gId
	 * @param udgId
	 * @return
	 * @throws Exception
	 */
	public boolean updateGroupInfo(String l2gId, String udgId) throws Exception
	{
		//结果
		boolean updateok = false;
		//排序
		LinkedHashMap<String, String> objectMap = new LinkedHashMap<String, String>();
		//条件
		LinkedHashMap<String, String> conditionMap = new LinkedHashMap<String, String>();

		try
		{
			//群组id
			objectMap.put("udgId", udgId);
			//记录id
			conditionMap.put("l2gId", l2gId);
			//执行更新
			updateok = empDao.update(LfList2gro.class, objectMap, conditionMap);

		} catch (Exception e)
		{
			//异常处理
			EmpExecutionContext.error(e,"更新群组成员信息出现异常！");
			throw e;
		}
		//返回结果
		return updateok;
	}

	/**
	 * 
	 * @param udgId
	 * @return
	 * @throws Exception
	 */
	public boolean groupMemberExists(String udgId) throws Exception
	{
		//结果
		boolean exists = false;
		//群组记录对象集合
		List<LfList2gro> lfList2groList = new ArrayList<LfList2gro>();
		//条件
		LinkedHashMap<String, String> conditionMap = new LinkedHashMap<String, String>();
		try
		{
			conditionMap.put("udgId", udgId);
			//获取记录
			lfList2groList = empDao.findListByCondition(LfList2gro.class,
					conditionMap, null);

			if (lfList2groList.size() > 0)
			{
				exists = true;
			}
		} catch (Exception e)
		{
			//异常处理
			EmpExecutionContext.error(e,"获取群组成员是否存在出现异常！");
			throw e;
		}
		//返回结果
		return exists;
	}

}
