package com.montnets.emp.client.biz;

import java.sql.Connection;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;

import com.montnets.emp.client.dao.GenericClientGroupInfoVoDAO;
import com.montnets.emp.common.biz.SuperBiz;
import com.montnets.emp.common.context.EmpExecutionContext;
import com.montnets.emp.common.dao.AddrBookSpecialDAO;
import com.montnets.emp.common.vo.GroupInfoVo;
import com.montnets.emp.entity.group.LfList2gro;
import com.montnets.emp.entity.group.LfUdgroup;
import com.montnets.emp.util.PageInfo;

/**
 * 
 * @author Administrator
 *
 */
public class GroupsManagerBiz extends SuperBiz
{
	private GenericClientGroupInfoVoDAO groupInfoVoDAO;
	private AddrBookSpecialDAO empSpecialDAO;

	public GroupsManagerBiz()
	{
		groupInfoVoDAO = new GenericClientGroupInfoVoDAO();
		empSpecialDAO = new AddrBookSpecialDAO();
	}

	/**
	 * 添加群组人员
	 * @param udgId	群组ID
	 * @param personId	成员ID
	 * @param l2gType	群组类型
	 * @return
	 * @throws Exception
	 */
	public Integer addList2gro(String udgId, String[] personId, String l2gType)
			throws Exception
	{
		//结果
		Integer re = 0;
		//初始化
		List<LfList2gro> lfList2groList = new ArrayList<LfList2gro>();
		AddrBookBaseBiz baseBiz = new AddrBookBaseBiz();
		//设值条件MAP
		LinkedHashMap<String, String> conditionMap = new LinkedHashMap<String, String>();
		try
		{
			//循环
			for (int i = 0; i < personId.length; i++)
			{
				//判断是否存在该群组
				conditionMap.put("guId", personId[i]);
				conditionMap.put("udgId", udgId);
				//获取记录
				List<LfList2gro> r = baseBiz.getByCondition(LfList2gro.class,
						conditionMap, null);
				if (r != null && r.size() > 0)
				{
				} else
				{
					//新增
					LfList2gro lfList2gro = new LfList2gro();
					lfList2gro.setGuId(Long.parseLong(personId[i]));
					lfList2gro.setL2gType(new Integer(l2gType));
					lfList2gro.setUdgId(new Integer(udgId));
					lfList2groList.add(lfList2gro);
				}
				conditionMap.clear();
			}
			//有记录
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
			EmpExecutionContext.error(e,"添加群组人员出现异常！");
			re = 0;
			throw e;
			//异常
		}
		//返回
		return re;
	}

	// 验证群组是否存在成员
	public boolean checkGrMember(String udgId, String personId)
			throws Exception
	{
		// Integer re = 0;
		// List<LfList2gro> lfList2groList=new ArrayList<LfList2gro>();
		AddrBookBaseBiz baseBiz = new AddrBookBaseBiz();
		//结果
		boolean exists = false;
		//设值MAP
		LinkedHashMap<String, String> conditionMap = new LinkedHashMap<String, String>();
		try
		{
			//设值
			conditionMap.put("guId", personId);
			conditionMap.put("udgId", udgId);
			//查询
			List<LfList2gro> r = baseBiz.getByCondition(LfList2gro.class,
					conditionMap, null);
			//有记录，已存在
			if (r != null && r.size() > 0)
			{
				exists = true;
			}
			//清空
			conditionMap.clear();

		} catch (Exception e)
		{
			//异常处理
			EmpExecutionContext.error(e," 验证群组是否存在成员出现异常！");
			throw e;
		}
		//返回
		return exists;
	}

	/**
	 * 	删除群组信息
	 * @param udgId	群组
	 * @return
	 * @throws Exception
	 */
	public Integer delGroupsAllInfo(String udgId) throws Exception
	{
		//判断ID是否为空 
		if (udgId == null)
		{
			//返回
			return null;
		}
		//删除关联操作
		this.delGroupsRecords(udgId);
		//删除群组
		Integer result = empDao.delete(LfUdgroup.class, udgId);
		//返回结果
		return result;
	}

	/**
	 * 删除群组关联记录
	 * @param udgId
	 * @return
	 * @throws Exception
	 */
	private Integer delGroupsRecords(String udgId) throws Exception
	{
		//结果
		Integer result = 0;
		//设值MAP
		LinkedHashMap<String, String> conditionMap = new LinkedHashMap<String, String>();
		try
		{
			//设值
			conditionMap.put("udgId", udgId);
			//删除关联
			result = empDao.delete(LfList2gro.class, conditionMap);
		} catch (Exception e)
		{
			//异常处理
			EmpExecutionContext.error(e,"删除群组关联记录出现异常！");
			throw e;
		}
		//返回结果
		return result;
	}

	/**
	 * 	删除群组成员
	 * @param l2gId	关联ID
	 * @return
	 * @throws Exception
	 */
	public Integer delGroupMembers(String l2gId) throws Exception
	{
		//结果
		Integer result = 0;
		//设值MAP
		LinkedHashMap<String, String> conditionMap = new LinkedHashMap<String, String>();
		try
		{
			//设值
			conditionMap.put("l2gId&in", l2gId);
			//删除操作
			result = empDao.delete(LfList2gro.class, conditionMap);
		} catch (Exception e)
		{
			//异常处理
			EmpExecutionContext.error(e,"删除群组成员出现异常！");
			throw e;
		}
		//返回
		return result;
	}

	/**
	 * 通过操作员用户ID 查询群组
	 * @param userId
	 * @return
	 * @throws Exception
	 */
	public List<LfUdgroup> getGroupsByUserId(Long userId, String corpCode)
			throws Exception
	{
		//用户ID
		if (userId == null)
		{
			//返回
			return null;
		}
		//群组LIST
		List<LfUdgroup> groupsList;
		try
		{
			//查询出所有的群组
			groupsList = empSpecialDAO.findUdgroupInfo(userId.toString());
		} catch (Exception e)
		{
			//异常处理
			EmpExecutionContext.error(e,"查询群组出现异常！");
			throw e;
		}
		//返回
		return groupsList;
	}

	/**
	 *  获取群组成员信息
	 * @param groupInfoVo
	 * @return
	 * @throws Exception
	 */
	public List<GroupInfoVo> getGroupInfoVos(GroupInfoVo groupInfoVo)
			throws Exception
	{
		//判断是否为NULL
		if (groupInfoVo == null)
		{
			//返回
			return null;
		}
		//群组成员LIST
		List<GroupInfoVo> groupVosList;
		try
		{
			//获取群组成员信息
			groupVosList = groupInfoVoDAO.findGroupInfoVo(groupInfoVo);
		} catch (Exception e)
		{
			//异常处理
			EmpExecutionContext.error(e,"获取群组成员信息出现异常！");
			throw e;
		}
		//返回
		return groupVosList;
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
			EmpExecutionContext.error(e,"获取群组成员信息出现异常！");
			throw e;
		}
		//返回
		return groupVosList;
	}

	/**
	 *  查询群组成员信息
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
			//查询
			groupVosList = groupInfoVoDAO.findLikeGroupInfoVo(loginUserId,
					groupInfoVo, pageInfo);

		} catch (Exception e)
		{
			//异常处理
			EmpExecutionContext.error(e,"查询群组成员信息出现异常！");
			throw e;
		}
		//返回
		return groupVosList;
	}

	/**
	 * 更新群组成员信息
	 * @param l2gId
	 * @param udgId
	 * @return
	 * @throws Exception
	 */
	public boolean updateGroupInfo(String l2gId, String udgId) throws Exception
	{
		//结果
		boolean updateok = false;
		//更新字段
		LinkedHashMap<String, String> objectMap = new LinkedHashMap<String, String>();
		//更新条件
		LinkedHashMap<String, String> conditionMap = new LinkedHashMap<String, String>();

		try
		{
			//设值
			objectMap.put("udgId", udgId);
			conditionMap.put("l2gId", l2gId);
			//执行数据库操作
			updateok = empDao.update(LfList2gro.class, objectMap, conditionMap);

		} catch (Exception e)
		{
			//异常处理
			EmpExecutionContext.error(e,"更新群组成员信息出现异常！");
			throw e;
		}
		//返回
		return updateok;
	}

	/**
	 * 判断群组是否存在 
	 * @param udgId	群组ID
	 * @return
	 * @throws Exception
	 */
	public boolean groupMemberExists(String udgId) throws Exception
	{
		//结果
		boolean exists = false;
		//群组成员对象集合
		List<LfList2gro> lfList2groList = new ArrayList<LfList2gro>();
		//设值MAP
		LinkedHashMap<String, String> conditionMap = new LinkedHashMap<String, String>();
		try
		{
			//设值
			conditionMap.put("udgId", udgId);
			//查询
			lfList2groList = empDao.findListByCondition(LfList2gro.class,
					conditionMap, null);
			//判断是否为空
			if (lfList2groList.size() > 0)
			{
				//返回
				exists = true;
			}
		} catch (Exception e)
		{
			//异常处理
			EmpExecutionContext.error(e,"判断群组是否存在 出现异常！");
			throw e;
		}
		//返回结果
		return exists;
	}

	/**
	 * 复制自建群组添加人员的方法，并且加过滤手机号码
	 * 
	 * @param udgId
	 * @param personId
	 * @param l2gType
	 * @return
	 * @throws Exception
	 */
	public String addList2groCheckMoblie(String udgId, String[] personId,
			String l2gType, Long userId) throws Exception
	{
		// 成功条数
		Integer result = 0; 
		String returnMsg = "";
		//群组成员对象集合
		List<LfList2gro> lfList2groList = new ArrayList<LfList2gro>();
		AddrBookBaseBiz baseBiz = new AddrBookBaseBiz();
		//条件
		LinkedHashMap<String, String> conditionMap = new LinkedHashMap<String, String>();
		try
		{
			GroupInfoVo groupInfoVo = new GroupInfoVo();
			groupInfoVo.setUdgId(Long.valueOf(udgId));
			// 把该群组下的所有人员的手机放在HASHSET中
			groupInfoVo.setUserId(userId);
			//分页对象
			PageInfo pageinfo = new PageInfo();
			pageinfo.setPageIndex(1);
			pageinfo.setPageSize(Integer.MAX_VALUE);
			//查询记录
			List<GroupInfoVo> groupVosList = groupInfoVoDAO.findGroupInfoVo(
					userId, groupInfoVo, pageinfo);
			HashSet<String> repeatList = new HashSet<String>();
			if (groupVosList != null && groupVosList.size() > 0)
			{
				for (int i = 0; i < groupVosList.size(); i++)
				{
					GroupInfoVo groupinfo = groupVosList.get(i);
					String mobile = groupinfo.getMobile();
					if (mobile != null && !"".equals(mobile))
					{
						repeatList.add(mobile);
					}
				}
			}
			StringBuffer badStr = new StringBuffer("");
			for (int i = 0; i < personId.length; i++)
			{
				// 装的分别的GUID和手机号码
				String guidMoblie[] = personId[i].split("&"); 
				conditionMap.put("guId", guidMoblie[0]);
				conditionMap.put("udgId", udgId);
				List<LfList2gro> person = baseBiz.getByCondition(
						LfList2gro.class, conditionMap, null);
				if (person == null || person.size() == 0)
				{
					if (this.checkRepeat(repeatList, guidMoblie[1]))
					{
						LfList2gro lfList2gro = new LfList2gro();
						lfList2gro.setGuId(Long.parseLong(guidMoblie[0]));
						lfList2gro.setL2gType(new Integer(l2gType));
						lfList2gro.setUdgId(new Integer(udgId));
						lfList2groList.add(lfList2gro);
					} else
					{
						badStr.append(guidMoblie[1]).append(",");
					}
				}
				conditionMap.clear();
			}
			if (lfList2groList.size() > 0)
			{
				result = empDao.save(lfList2groList, LfList2gro.class);
			}
			String badMoblie = badStr.toString();
			if (badMoblie.length() > 0)
			{
				returnMsg = badMoblie.substring(0, badMoblie.length() - 1);
				returnMsg = returnMsg + "&" + result;
			} else
			{
				returnMsg = "&" + result;
			}
		} catch (Exception e)
		{
			returnMsg = "&-1";
			//异常处理
			EmpExecutionContext.error(e,"操作群组以及获取");
		}
		//返回结果
		return returnMsg;
	}

	/**
	 * 验证重复
	 * 
	 * @param aa
	 * @param ee
	 * @return
	 */
	private boolean checkRepeat(HashSet<String> aa, String ee)
	{
		try
		{
			//判断是否包含
			if (aa.contains(ee))
			{
				//返回
				return false;
			} else
			{
				aa.add(ee);
			}
		} catch (Exception e)
		{
			//异常处理
			EmpExecutionContext.error(e,"验证重复出现异常！");
			return false;
		}
		//返回结果
		return true;
	}

	/**
	 * 通过群组ID 以及登录远的用户ID 查询出该群组里的人员的信息
	 * 
	 * @param udgId
	 * @param userId
	 * @return
	 */
	public List<GroupInfoVo> getGroupVoInfo(Long udgId, Long userId)
	{
		//群组成员列表
		List<GroupInfoVo> groupVosList = null;
		try
		{
			//群组对象
			GroupInfoVo groupInfoVo = new GroupInfoVo();
			groupInfoVo.setUdgId(udgId);
			groupInfoVo.setUserId(userId);
			//分页对象
			PageInfo pageinfo = new PageInfo();
			pageinfo.setPageIndex(1);
			pageinfo.setPageSize(Integer.MAX_VALUE);
			//查询
			groupVosList = groupInfoVoDAO.findGroupInfoVo(userId, groupInfoVo,
					pageinfo);
		} catch (Exception e)
		{
			//异常处理
			EmpExecutionContext.error(e,"查询群组成员信息出现异常！");
		}
		//返回
		return groupVosList;
	}

	
	
	/**
	 * 删除个人群组
	 * @param udgIdStr	群组ID
	 * @param loginUserId	登录者ID
	 * @return
	 */
	public boolean delPersonGroup(String udgIdStr,String loginUserId) {
		//个人群组
		StringBuffer perGroupStr = new StringBuffer();
		//我的共享群组
		StringBuffer mineGxGroupStr = new StringBuffer();
		boolean isFlag = false;
		try{
			String[] arr = udgIdStr.split(",");
			LfUdgroup udgGroup = null;
			for(int i=0;i<arr.length;i++){
				String id = arr[i];
				//获取群组Id对应实体类对象
				udgGroup = empDao.findObjectByID(LfUdgroup.class, Long.valueOf(id));
				if(udgGroup != null){
					Integer groupType = udgGroup.getSharetype();
					if(groupType == 0){
						perGroupStr.append(id).append(",");
					}else if(groupType == 1){
						mineGxGroupStr.append(id).append(",");
					}
				}
			}
			LinkedHashMap<String, String> conditionMap = new LinkedHashMap<String, String>();
			//群组分类操作
			String str1 = "";
			String str2 = "";
			if(perGroupStr != null && perGroupStr.length()>0){
				str1 = perGroupStr.toString().substring(0, perGroupStr.length()-1);
			}
			if(mineGxGroupStr != null && mineGxGroupStr.length()>0){
				str2 = mineGxGroupStr.toString().substring(0, mineGxGroupStr.length()-1);
			}
			Connection conn = empTransDao.getConnection();
			
			try
			{
				//开始事务
				empTransDao.beginTransaction(conn);
				if(!"".equals(str1)){
					conditionMap.put("udgId", str1);
					//删除群组成员
					empTransDao.delete(conn, LfList2gro.class, conditionMap);
					conditionMap.clear();
					conditionMap.put("udgId", str1);
					conditionMap.put("gpAttribute", "0");
					conditionMap.put("userId", loginUserId);
					//删除个人群组
					empTransDao.delete(conn, LfUdgroup.class, conditionMap);
					conditionMap.clear();
					conditionMap.put("groupid", str1);
					conditionMap.put("gpAttribute", "0");
					//删除共享群组
					empTransDao.delete(conn, LfUdgroup.class, conditionMap);
				}
				if(!"".equals(str2)){
					conditionMap.clear();
					conditionMap.put("gpAttribute", "0");
					conditionMap.put("groupid", str2);
					conditionMap.put("receiver", loginUserId);
					empTransDao.delete(conn, LfUdgroup.class, conditionMap);
				}
				//提交事务
				empTransDao.commitTransaction(conn);
				isFlag = true;
			} catch (Exception e)
			{
				//异常处理
				EmpExecutionContext.error(e,"删除个人群组出现异常！");
				//回滚事务
				empTransDao.rollBackTransaction(conn);
				isFlag = false;
			} finally
			{
				//关闭连接
				empTransDao.closeConnection(conn);
			}
		}catch (Exception e) {
			EmpExecutionContext.error(e,"删除个人群组出现异常！");
			isFlag = false;
		}
		return isFlag;
	}
	
	/**
	 * 共享群组
	 * @param addList 新添加的共享关系
	 * @param deleteList 已经去除共享的集合
	 * @return
	 */
	public boolean shareGroup(List<LfUdgroup> addList,String deleteList) {
		Connection conn = empTransDao.getConnection();
		//新添加的个数统计
		int addCount = 0;
		//删除的个数统计
		int deleteCount = 0;
		boolean result = false;
		try {
			//开始事务
			empTransDao.beginTransaction(conn);
			if(addList != null && addList.size()>0){
				addCount = empTransDao.save(conn, addList, LfUdgroup.class);
				result = addCount > 0;
			}
			if(deleteList != null && !"".equals(deleteList)){
				deleteCount = empTransDao.delete(conn, LfUdgroup.class, deleteList);
				result = deleteCount > 0;
			}
			//提交事务
			empTransDao.commitTransaction(conn);
		} catch (Exception e) {
			//异常处理
			EmpExecutionContext.error(e,"共享群组共享操作出现异常！");
			//回滚事务
			empTransDao.rollBackTransaction(conn);
			return false;
		} finally {
			//关闭连接
			empTransDao.closeConnection(conn);
		}
		return result;
	}
	
	
	/**
	 * 处理群组中分页选择人员  
	 * @param groupId	群组ID
	 * @param pageInfo	分页信息
	 * @param type	员工  /客户  群组
	 * @return
	 * @throws Exception
	 */
	public List<GroupInfoVo> getGroupUser(Long groupId,PageInfo pageInfo,String name) throws Exception
	{
		List<GroupInfoVo> groupVosList = null;
		try
		{
			groupVosList =groupInfoVoDAO.findGroupUserByIds(groupId,pageInfo,name);
		} catch (Exception e){
			groupVosList = null;
			EmpExecutionContext.error(e,"处理群组人员出现异常！");
		}
		return groupVosList;
	}
}
