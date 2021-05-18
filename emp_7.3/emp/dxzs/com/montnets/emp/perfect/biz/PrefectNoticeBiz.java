package com.montnets.emp.perfect.biz;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.sql.Connection;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.beanutils.BasicDynaBean;
import org.apache.commons.beanutils.BasicDynaClass;
import org.apache.commons.beanutils.DynaBean;
import org.apache.commons.beanutils.DynaProperty;

import com.montnets.emp.common.biz.BalanceLogBiz;
import com.montnets.emp.common.biz.BaseBiz;
import com.montnets.emp.common.biz.CommonBiz;
import com.montnets.emp.common.biz.SmsBiz;
import com.montnets.emp.common.biz.SubnoManagerBiz;
import com.montnets.emp.common.biz.SuperBiz;
import com.montnets.emp.common.constant.CommonVariables;
import com.montnets.emp.common.constant.Message;
import com.montnets.emp.common.constant.PreviewParams;
import com.montnets.emp.common.constant.StaticValue;
import com.montnets.emp.common.context.EmpExecutionContext;
import com.montnets.emp.common.vo.GroupInfoVo;
import com.montnets.emp.entity.dxzs.LfDfadvanced;
import com.montnets.emp.entity.employee.LfEmployee;
import com.montnets.emp.entity.employee.LfEmployeeDep;
import com.montnets.emp.entity.group.LfList2gro;
import com.montnets.emp.entity.group.LfMalist;
import com.montnets.emp.entity.group.LfUdgroup;
import com.montnets.emp.entity.pasroute.GtPortUsed;
import com.montnets.emp.entity.perfect.LfPerFileInfo;
import com.montnets.emp.entity.perfect.LfPerfectNotic;
import com.montnets.emp.entity.perfect.LfPerfectNoticUp;
import com.montnets.emp.entity.sysuser.LfSysuser;
import com.montnets.emp.perfect.dao.PerfectDao;
import com.montnets.emp.perfect.vo.PerfectNoticeVo;
import com.montnets.emp.security.blacklist.BlackListAtom;
import com.montnets.emp.security.wgmsgconfig.WgMsgConfigBiz;
import com.montnets.emp.smstask.HttpSmsSend;
import com.montnets.emp.smstask.WGParams;
import com.montnets.emp.util.EmpUtils;
import com.montnets.emp.util.GetSxCount;
import com.montnets.emp.util.PageInfo;
import com.montnets.emp.util.PhoneUtil;
import com.montnets.emp.util.TxtFileUtil;

/**
 * @description 完美通知BIZ
 * @project p_ydcx
 * @company ShenZhen Montnets Technology CO.,LTD.
 * @author yejiangmin <282905282@qq.com>
 * @datetime 2013-10-23 上午08:40:32
 */
public class PrefectNoticeBiz extends SuperBiz
{

	private String			line	= StaticValue.systemProperty.getProperty(StaticValue.LINE_SEPARATOR);

	private CommonVariables	cv		= new CommonVariables();

	/**
	 * 查询完美通知详情记录
	 * 
	 * @param username
	 *        用户名
	 * @param moblie
	 *        手机号码
	 * @param remsg
	 *        上行回复内容
	 * @param noticeId
	 *        完美通知主表 ID
	 * @param pageInfo
	 *        分页信息
	 * @return
	 */
	public List<LfPerfectNoticUp> getPerfectNoticeUpByNoticeId(String username, String moblie, String remsg, Long taskid, String isReAttr, String isGeAttr, String sendCount, PageInfo pageInfo)
	{
		LinkedHashMap<String, String> conditionMap = new LinkedHashMap<String, String>();
		LinkedHashMap<String, String> objectMap = new LinkedHashMap<String, String>();

		List<LfPerfectNoticUp> noticUps = null;
		conditionMap.put("taskId", String.valueOf(taskid));
		if(username != null && !"".equals(username))
		{
			conditionMap.put("name&like", username);
		}
		if(!"".equals(moblie) && moblie != null)
		{
			conditionMap.put("mobile&like", moblie);
		}
		if(!"".equals(remsg) && remsg != null)
		{
			conditionMap.put("content&like", remsg);
		}
		// 回复状态
		String conReAttr = isReAttr;
		if(!"".equals(conReAttr) && conReAttr != null)
		{
			if("1".equals(conReAttr))
			{
				conReAttr = "0";
			}
			else
			{
				conReAttr = "1";
			}
			conditionMap.put("isReply", conReAttr);
		}
		// 回执状态
		String conGeAttr = isGeAttr;
		if(conGeAttr != null && !"".equals(conGeAttr))
		{

			if("1".equals(conGeAttr))
			{
				// 已接收
				conGeAttr = "0";
			}
			else
				if("2".equals(conGeAttr))
				{
					// 未接收
					conGeAttr = "1";
				}
				else
					if("3".equals(conGeAttr))
					{
						// 未发送成功
						conGeAttr = "2";
					}
			conditionMap.put("isReceive", conGeAttr);
		}
		// 发送次数
		if(sendCount != null && !"".equals(sendCount))
		{
			conditionMap.put("receiveCount", sendCount);
		}
		// 处理排序的问题
		objectMap.put("isAtrred", StaticValue.ASC);
		objectMap.put("isReply", StaticValue.ASC);
		objectMap.put("pnupId", StaticValue.ASC);
		try
		{
			noticUps = empDao.findPageListBySymbolsCondition(null, LfPerfectNoticUp.class, conditionMap, objectMap, pageInfo);
		}
		catch (Exception e)
		{
			EmpExecutionContext.error(e, "完美通知详情查询失败！");
		}
		return noticUps;
	}

	/**
	 * 查询完美通知详情记录
	 * 
	 * @param username
	 *        接收人名称
	 * @param moblie
	 *        手机号码
	 * @param noticeId
	 *        完美通知ID
	 * @param pageInfo
	 *        分页信息
	 * @return
	 */
	public List<LfPerfectNoticUp> getPerfectNoticeUpByNoticeId(String username, String moblie, Long taskid, PageInfo pageInfo)
	{
		LinkedHashMap<String, String> map = new LinkedHashMap<String, String>();
		LinkedHashMap<String, String> objectMap = new LinkedHashMap<String, String>();

		List<LfPerfectNoticUp> noticUps = null;
		map.put("taskId", String.valueOf(taskid));
		if(!"".equals(username) && username != null)
		{
			map.put("name&like", username);
		}
		if(!"".equals(moblie) && moblie != null)
		{
			map.put("mobile&like", moblie);
		}

		objectMap.put("receiverId", StaticValue.ASC);
		objectMap.put("isReply", StaticValue.ASC);
		objectMap.put("pnupId", StaticValue.ASC);
		try
		{
			// noticUps = empDao.findPageListByCondition(null,
			// LfPerfectNoticUp.class, map, objectMap, pageInfo);
			noticUps = empDao.findPageListBySymbolsCondition(null, LfPerfectNoticUp.class, map, objectMap, pageInfo);
		}
		catch (Exception e)
		{
			EmpExecutionContext.error(e, "查询完美通知详情记录失败！");
		}
		return noticUps;
	}

	/**
	 * 查询单个完美通知详情
	 * 
	 * @param noticeId
	 *        完美通知主表ID
	 * @return
	 */
	public List<LfPerfectNoticUp> getPerfectNoticeUpByNoticeId(long noticeId)
	{
		LinkedHashMap<String, String> map = new LinkedHashMap<String, String>();
		List<LfPerfectNoticUp> noticUps = null;
		map.put("pnotic", String.valueOf(noticeId));
		try
		{
			noticUps = empDao.findListByCondition(LfPerfectNoticUp.class, map, null);
		}
		catch (Exception e)
		{
			EmpExecutionContext.error(e, "查询单个完美通知详情失败！");
		}
		return noticUps;
	}

	/**
	 * 更新或者新增完美通知更新上行状态
	 * 
	 * @param type
	 *        类型
	 * @param senderId
	 *        发送者GUID
	 * @param recevieId
	 *        接收者GUID
	 * @param content
	 *        上行内容
	 * @param noticeId
	 *        完美通知主表ID
	 */
	public void updateOrAddPnoticeUpMsgStart(String type, long senderId, long recevieId, String content, long noticeId)
	{
		BaseBiz baseBiz = new BaseBiz();
		LinkedHashMap<String, String> map = new LinkedHashMap<String, String>();
		List<LfPerfectNoticUp> noticups = null;
		LfPerfectNoticUp noticUp = null;
		map.put("pnotic", String.valueOf(noticeId));
		map.put("senderGuid", String.valueOf(senderId));
		map.put("receiverId", String.valueOf(recevieId));
		try
		{

			noticups = empDao.findListByCondition(LfPerfectNoticUp.class, map, null);
			if(noticups != null && noticups.size() > 0)
			{
				noticUp = noticups.get(0);
				noticUp.setIsReply(0);
				baseBiz.updateObj(noticUp);
			}
			if(noticUp == null){
				noticUp = new LfPerfectNoticUp();
			}
			String dialogId = type + "_" + senderId + "_" + recevieId + "_" + senderId + "" + noticUp.getTaskId();
			LfSysuser lfSysuser = baseBiz.getByGuId(LfSysuser.class, senderId);
			LfPerfectNoticUp notice = new LfPerfectNoticUp();
			notice.setName(lfSysuser.getName());
			notice.setUserType(1);
			notice.setPnotic(noticeId);
			notice.setSenderGuid(recevieId);
			notice.setSendTime(new Timestamp(System.currentTimeMillis()));
			notice.setReceiverId(senderId);
			notice.setIsReceive(-1);
			notice.setIsReply(0);
			notice.setDialogId(dialogId);
			notice.setTaskId(noticUp.getTaskId());
			notice.setMobile(lfSysuser.getMobile());
			notice.setContent(content);
			baseBiz.addObj(notice);
		}
		catch (Exception e)
		{
			EmpExecutionContext.error(e, "更新或者新增完美通知更新上行状态失败！");
		}
	}

	/**
	 * 更新完美通知内容
	 * 
	 * @param type
	 *        类型
	 * @param senderId
	 *        发送者GUID
	 * @param recevieId
	 *        接收者ID
	 * @param content
	 *        发送内容
	 * @param noticeId
	 *        完美通知ID
	 */
	public void updateSinglePerNoticeMsg(String type, long senderId, long recevieId, String content, long noticeId)
	{
		BaseBiz baseBiz = new BaseBiz();
		LinkedHashMap<String, String> map = new LinkedHashMap<String, String>();
		List<LfPerfectNoticUp> noticups = null;
		LfPerfectNoticUp noticUp = null;
		map.put("pnotic", String.valueOf(noticeId));
		map.put("senderGuid", String.valueOf(senderId));
		map.put("receiverId", String.valueOf(recevieId));
		try
		{

			noticups = empDao.findListByCondition(LfPerfectNoticUp.class, map, null);
			if(noticups != null && noticups.size() > 0)
			{
				noticUp = noticups.get(0);
				noticUp.setIsReply(0);
				String oldContent = noticUp.getContent();
				if(oldContent != null && !"".equals(oldContent))
				{
					content = oldContent + " && " + content;
				}
				noticUp.setContent(content);
				noticUp.setSendTime(new Timestamp(System.currentTimeMillis()));
				baseBiz.updateObj(noticUp);
			}

		}
		catch (Exception e)
		{
			EmpExecutionContext.error(e, "更新完美通知内容失败！");
		}
	}

	/**
	 * 通过主表ID以及 接收者ID获取对象的完美通知
	 * 
	 * @param senderId
	 *        发送者GUID
	 * @param recevieId
	 *        接收者ID
	 * @param noticeId
	 *        完美通知主表ID
	 * @return
	 */
	public LfPerfectNoticUp getPnoticeUpByNcIdAndrecevieId(String senderId, String recevieId, String noticeId)
	{
		LinkedHashMap<String, String> map = new LinkedHashMap<String, String>();
		List<LfPerfectNoticUp> noticups = null;
		LfPerfectNoticUp noticUp = null;
		map.put("pnotic", noticeId);
		map.put("senderGuid", senderId);
		map.put("receiverId", recevieId);
		try
		{
			noticups = empDao.findListByCondition(LfPerfectNoticUp.class, map, null);
		}
		catch (Exception e)
		{
			EmpExecutionContext.error(e, "获取完美通知详情失败！");
		}
		if(noticups != null && noticups.size() > 0)
		{
			noticUp = noticups.get(0);
		}
		return noticUp;
	}

	/**
	 * 完美通知历史记录
	 * 
	 * @param senderId
	 *        发送者ID
	 * @param type
	 *        类型
	 * @param userOrDepOrGp
	 *        用户 机构 群组 ID
	 * @param pageInfo
	 *        分页信息
	 * @return
	 */
	public List<LfPerfectNotic> getPreNoticeHistroy(long senderId, Integer type, long userOrDepOrGp, PageInfo pageInfo)
	{
		LinkedHashMap<String, String> map = new LinkedHashMap<String, String>();
		LinkedHashMap<String, String> orderMap = new LinkedHashMap<String, String>();
		List<LfPerfectNotic> perfectNotics = null;
		map.put("senderGuid", String.valueOf(senderId));
		map.put("recevierId", String.valueOf(userOrDepOrGp));
		map.put("recevierType", String.valueOf(type));
		orderMap.put("submitTime", StaticValue.DESC);
		try
		{
			perfectNotics = empDao.findPageListByCondition(null, LfPerfectNotic.class, map, orderMap, pageInfo);
		}
		catch (Exception e)
		{
			EmpExecutionContext.error(e, "完美通知历史记录查询失败！");
		}
		return perfectNotics;
	}

	/**
	 * 获取完美通知详情记录
	 * 
	 * @param noticId
	 *        完美通知主表ID
	 * @return
	 */
	public List<LfPerfectNoticUp> getPreNoticUpByNoticId(long noticId)
	{
		LinkedHashMap<String, String> map = new LinkedHashMap<String, String>();
		List<LfPerfectNoticUp> noticUps = null;
		map.put("pnotic", String.valueOf(noticId));
		try
		{
			noticUps = empDao.findListByCondition(LfPerfectNoticUp.class, map, null);
		}
		catch (Exception e)
		{
			EmpExecutionContext.error(e, "获取完美通知详情记录失败！");
		}
		return noticUps;
	}

	/**
	 * 完美通知详情记录
	 * 
	 * @param noticId
	 *        完美通知详情ID
	 * @return
	 */
	public List<LfPerfectNoticUp> getPnUpByNoticIdAndUserId(long noticId)
	{
		LinkedHashMap<String, String> map = new LinkedHashMap<String, String>();
		List<LfPerfectNoticUp> noticUps = null;
		map.put("pnotic", String.valueOf(noticId));
		try
		{
			noticUps = empDao.findListByCondition(LfPerfectNoticUp.class, map, null);
		}
		catch (Exception e)
		{
			EmpExecutionContext.error(e, "完美通知详情记录查询失败！");
		}
		return noticUps;
	}

	/**
	 * 获取员工列表
	 * 
	 * @param depCode
	 *        机构编码
	 * @param corpCode
	 *        企业编码
	 * @return
	 */
	public List<LfEmployee> getEmpLikeDepCode(String depCode, String corpCode)
	{
		List<LfEmployee> employees = null;
		LinkedHashMap<String, String> conditionMap = new LinkedHashMap<String, String>();
		LinkedHashMap<String, String> orderbyMap = new LinkedHashMap<String, String>();
		conditionMap.put("depCode&" + StaticValue.LIKE2, EmpUtils.getPartDepCode(depCode, "0"));
		conditionMap.put("corpCode", corpCode);
		orderbyMap.put("name", StaticValue.ASC);
		try
		{
			employees = empDao.findListBySymbolsCondition(null, LfEmployee.class, conditionMap, orderbyMap);
		}
		catch (Exception e)
		{
			EmpExecutionContext.error(e, "获取完美通知员工列表失败　！");
		}
		if(employees == null)
		{
			employees = new ArrayList<LfEmployee>();
		}
		return employees;
	}

	/**
	 * 获取群组的关联列表
	 * 
	 * @param groupId
	 *        群组ID
	 * @return
	 */
	public List<LfList2gro> getGroupUsers(String groupId)
	{
		BaseBiz baseBiz = new BaseBiz();
		LinkedHashMap<String, String> conditionMap = new LinkedHashMap<String, String>();
		conditionMap.put("udgId", groupId);
		List<LfList2gro> list2groes = null;
		try
		{
			list2groes = baseBiz.getByCondition(LfList2gro.class, conditionMap, null);
		}
		catch (Exception e)
		{
			EmpExecutionContext.error(e, "获取群组的关联列表失败！");
		}
		if(list2groes == null)
		{
			list2groes = new ArrayList<LfList2gro>();
		}
		return list2groes;
	}

	/**
	 * 根据 类型 与ID 获取 成员信息
	 * 
	 * @param guid
	 *        成员ID
	 * @param type
	 *        类型
	 * @return
	 */
	public GroupInfoVo getUserInfoByTypeAndGuId(long guid, String type)
	{
		BaseBiz baseBiz = new BaseBiz();
		GroupInfoVo groupInfoVo = null;
		try
		{
			if("1".equals(type))
			{
				LfEmployee employee = baseBiz.getByGuId(LfEmployee.class, guid);
				if(employee != null)
				{
					groupInfoVo = new GroupInfoVo();
					groupInfoVo.setGuId(guid);
					groupInfoVo.setL2gType(1);
					groupInfoVo.setMobile(employee.getMobile());
					groupInfoVo.setName(employee.getName());
				}
				else
				{
					LfSysuser lfSysuser = baseBiz.getByGuId(LfSysuser.class, guid);
					if(lfSysuser != null)
					{
						groupInfoVo = new GroupInfoVo();
						groupInfoVo.setGuId(guid);
						groupInfoVo.setL2gType(1);
						groupInfoVo.setMobile(lfSysuser.getMobile());
						groupInfoVo.setUserId(lfSysuser.getUserId());
						groupInfoVo.setName(lfSysuser.getName());
					}
				}
			}
			else
				if("2".equals(type))
				{
					LfMalist malist = baseBiz.getByGuId(LfMalist.class, guid);
					if(malist != null)
					{
						groupInfoVo = new GroupInfoVo();
						groupInfoVo.setGuId(guid);
						groupInfoVo.setL2gType(2);
						groupInfoVo.setMobile(malist.getMobile());
						groupInfoVo.setName(malist.getName());
					}
				}
		}
		catch (Exception e)
		{
			EmpExecutionContext.error(e, "完美通知获取群组成员信息失败！");
		}
		return groupInfoVo;
	}

	/**
	 * 通过机构编码获取员工列表
	 * 
	 * @param depcode
	 *        机构编码
	 * @param pageInfo
	 *        分页信息
	 * @return
	 */
	public List<LfEmployee> getEmpByDepCode(String depcode, PageInfo pageInfo)
	{
		List<LfEmployee> employees = null;
		LinkedHashMap<String, String> conditionMap = new LinkedHashMap<String, String>();
		LinkedHashMap<String, String> orderbyMap = new LinkedHashMap<String, String>();
		conditionMap.put("depCode", depcode);
		orderbyMap.put("name", StaticValue.ASC);
		try
		{
			employees = empDao.findPageListByCondition(null, LfEmployee.class, conditionMap, orderbyMap, pageInfo);
		}
		catch (Exception e)
		{
			EmpExecutionContext.error(e, "完美通知获取员工列表失败！");
		}
		if(employees == null)
		{
			employees = new ArrayList<LfEmployee>();
		}
		return employees;
	}

	/**
	 * 通过操作员用户ID 获取群组列表
	 * 
	 * @param userId
	 *        用户ID
	 * @param type
	 *        类型
	 * @return
	 */
	public List<LfUdgroup> getUdgroupByUserGuId(long userId, Integer type)
	{
		LinkedHashMap<String, String> conditionMap = new LinkedHashMap<String, String>();
		if(1 == type)
		{
			conditionMap.put("groupType", String.valueOf(type));
		}
		else
			if(2 == type)
			{
				conditionMap.put("groupType", String.valueOf(type));
			}
		conditionMap.put("userId", String.valueOf(userId));
		List<LfUdgroup> lfUdgroups = null;
		try
		{
			lfUdgroups = empDao.findListByCondition(LfUdgroup.class, conditionMap, null);
		}
		catch (Exception e)
		{
			EmpExecutionContext.error(e, "完美通知获取群组列表失败！");
		}
		return lfUdgroups;
	}

	/**
	 * 通过用户ID去获取群组 列表
	 * 
	 * @param userId
	 *        用户ID
	 * @param type
	 *        类型
	 * @param gpAttribute
	 *        群组类型
	 * @return
	 */
	public List<LfUdgroup> getUdgroupByUserGuId(long userId, Integer type, Integer gpAttribute)
	{
		LinkedHashMap<String, String> conditionMap = new LinkedHashMap<String, String>();
		if(1 == type)
		{
			conditionMap.put("groupType", String.valueOf(type));
		}
		else
			if(2 == type)
			{
				conditionMap.put("groupType", String.valueOf(type));
			}
		conditionMap.put("userId", String.valueOf(userId));
		conditionMap.put("gpAttribute", String.valueOf(gpAttribute));

		List<LfUdgroup> lfUdgroups = null;
		try
		{
			lfUdgroups = empDao.findListByCondition(LfUdgroup.class, conditionMap, null);
		}
		catch (Exception e)
		{
			EmpExecutionContext.error(e, "完美通知获取群组 列表失败！");
		}
		return lfUdgroups;
	}

	public List<GroupInfoVo> getGroupUserById(Long loginUserId, String groupId, PageInfo pageInfo) throws Exception
	{
		GroupInfoVo groupInfoVo = new GroupInfoVo();
		groupInfoVo.setUdgId(Long.valueOf(groupId));
		return this.getGroupVos(loginUserId, groupInfoVo, pageInfo);
	}

	/**
	 * 获取群组成员信息
	 * 
	 * @param loginUserId
	 *        登录者GUID
	 * @param groupInfoVo
	 *        成员信息
	 * @param pageInfo
	 *        分页信息
	 * @return
	 * @throws Exception
	 */
	public List<GroupInfoVo> getGroupVos(Long loginUserId, GroupInfoVo groupInfoVo, PageInfo pageInfo) throws Exception
	{
		List<GroupInfoVo> groupVosList;
		try
		{
			/*
			 * groupVosList = new
			 * GenericGroupInfoVoDAO().findGroupInfoVo(loginUserId, groupInfoVo,
			 * pageInfo);
			 */

			groupVosList = new PerfectDao().findGroupInfoVo(loginUserId, groupInfoVo, pageInfo);

		}
		catch (Exception e)
		{
			EmpExecutionContext.error(e, "获取群组成员信息异常。");
			throw e;
		}
		return groupVosList;
	}

	/**
	 * 完美通知历史记录查询
	 * 
	 * @param corpCode
	 *        企业编码
	 * @param senderName
	 *        发送者名称
	 * @param sendType
	 *        发送类型
	 * @param content
	 *        内容
	 * @param sendTime
	 *        时间段
	 * @param recvTime
	 * @param userId
	 *        用户ID
	 * @param pageInfo
	 *        分页信息
	 * @return
	 */
	public List<PerfectNoticeVo> getPerfectNoticeVoHistroy(String corpCode, String senderName, String sendType, String content, String sendTime, String recvTime, Long userId, PageInfo pageInfo)
	{
		List<PerfectNoticeVo> perfectNoticeVos = null;
		try
		{
			perfectNoticeVos = new PerfectDao().findlfImPerfectNoticeVos(corpCode, senderName, sendType, content, sendTime, recvTime, userId, pageInfo);
		}
		catch (Exception e)
		{
			EmpExecutionContext.error(e, " 完美通知历史记录查询 失败！");
		}
		return perfectNoticeVos;
	}

	/**
	 * 统计该短信的内容拆分条数
	 * 
	 * @param sqUserid
	 * @param msg
	 *        发送内容
	 * @param phone
	 *        手机号码
	 * @param haoduan
	 *        号段
	 * @param portsList
	 *        路由列表
	 * @return
	 * @throws Exception
	 */
	public int countSmsNumber2(String msg, String phone, String[] haoduan, List<GtPortUsed> gtPortsList) throws Exception
	{
		GtPortUsed gtPort = new GtPortUsed();

		int[] sendCount = new int[] {1,1,1};
		int len;
		int maxLen;
		int totalLen;
		int lastLen;
		int signLen;
		int index = 0;

		for (int g = 0; g < gtPortsList.size(); g++)
		{
			gtPort = gtPortsList.get(g);
			// 如果运营商是电信:21.则index为2，移动则为0，联通则为1
			index = gtPort.getSpisuncm() - 2 > 0 ? 2 : gtPort.getSpisuncm();
			maxLen = gtPort.getMaxwords();
			totalLen = gtPort.getMultilen1();
			lastLen = gtPort.getMultilen2();
			signLen = gtPort.getSignlen() == 0 ? gtPort.getSignstr().trim().length() : gtPort.getSignlen();
			// 如果设定的短信签名长度为0则为实际短信签名内容的长度
			len = msg.length();
			if(len <= maxLen - signLen)
			{
				len = msg.getBytes("unicode").length - 2;
				if(len <= (totalLen - signLen + 3) * 2)
					sendCount[index] = 1;
				else
					sendCount[index] = 1 + (len - lastLen * 2 + totalLen * 2 - 1) / (totalLen * 2);
			}
		}

		if(haoduan[0].indexOf(phone.substring(0, 3)) > -1)
		{
			return sendCount[0];
		}
		if(haoduan[1].indexOf(phone.substring(0, 3)) > -1)
		{
			return sendCount[1];
		}
		if(haoduan[2].indexOf(phone.substring(0, 3)) > -1)
		{
			return sendCount[2];
		}
		return 0;
	}

	/**
	 * 完美通知重复号过滤
	 * 
	 * @param aa
	 * @param ee
	 * @return
	 */
	public boolean checkRepeat(HashSet<String> aa, String ee)
	{
		try
		{
			if(aa.contains(ee))
			{
				return false;
			}
			else
			{
				aa.add(ee);
			}
		}
		catch (Exception e)
		{
			EmpExecutionContext.error(e, "完美通知重复号过滤出现异常！");
			return false;
		}
		return true;
	}

	/**
	 * 对手机号码做处理
	 * 
	 * @param moblie
	 *        手机号码
	 * @param effectMoblie
	 *        有效的手机号码
	 * @param haoduans
	 *        手机号段
	 * @param repeatList
	 *        处理重复手机号码LIST
	 * @param blList
	 *        黑名单
	 * @param map
	 *        SP帐号对应路由所生成的子号详情MAP
	 * @param blBiz
	 *        处理黑名单的BIZ
	 * @param wgMsgConfigBiz处理号段的BIZ
	 * @param line
	 *        换行
	 * @param noticUp
	 *        完美通知的单条详情
	 * @return
	 */
	public String checkMobile(Message lfImMessage, String moblie, StringBuffer effectMoblie, String[] haoduans, HashSet<String> repeatList, String corpCode, Map<Integer, String> map, List<GtPortUsed> portsList, BlackListAtom blBiz, WgMsgConfigBiz wgMsgConfigBiz, String line, LfPerfectNoticUp noticUp)
	{
		String isMsg = "0&0";
		try
		{
			if(moblie == null || "".equals(moblie))
			{
				noticUp.setIsReceive(4);
				isMsg = "4&0";
				// 手机号码为空并且短信拆分为0
			}
			else
			{
				Integer isValid = wgMsgConfigBiz.checkMobile(moblie, haoduans);
				if(isValid != null && isValid == 1)
				{
					boolean isBlackNum = blBiz.checkBlackList(corpCode, moblie, corpCode);
					if(!isBlackNum)
					{
						boolean isRePeat = false;
						isRePeat = this.checkRepeat(repeatList, moblie);
						if(isRePeat)
						{
							noticUp.setIsReceive(2);
							// 未发送状态
							noticUp.setMobile(moblie);
							Integer spisuncm = 0;
							if("+".equals(moblie.substring(0, 1)) || "00".equals(moblie.substring(0, 2)))
							{
								spisuncm = 5;
							}
							else
							{
								spisuncm = wgMsgConfigBiz.getPhoneSpisuncm(moblie);
							}
							String spNumber = map.get(spisuncm);
							if(spNumber != null)
							{
								noticUp.setSpUser(lfImMessage.getMessageId());
								noticUp.setSpNumber(spNumber);
								noticUp.setReceiveCount(1);
								effectMoblie.append(moblie).append(line);
								Integer messageCount = this.countSmsNumber2(lfImMessage.getContent(), moblie, haoduans, portsList);
								isMsg = "2&" + messageCount;
							}
							else
							{
								noticUp.setIsReceive(8);
								isMsg = "8&0";
								// 运营商不符合，没有绑定对应的路由
							}
						}
						else
						{
							noticUp.setIsReceive(-2);
							noticUp.setMobile(moblie);
							isMsg = "-2&0";
							// 重复号码
						}
					}
					else
					{
						noticUp.setIsReceive(5);
						noticUp.setMobile(moblie);
						isMsg = "5&0";
						// 黑名单
					}
				}
				else
				{
					noticUp.setIsReceive(3);
					isMsg = "3&0";
					// 手机号码异常
				}
			}
			String[] msg = isMsg.split("&");
			if(!"2".equals(msg[0]))
			{
				noticUp.setReceiveCount(0);
			}
		}
		catch (Exception e)
		{
			EmpExecutionContext.error(e, "完美通知单个手机验证合法出现异常！");
			noticUp.setIsReceive(3);
			noticUp.setReceiveCount(0);
			isMsg = "3&0";
		}
		return isMsg;
	}

	/**
	 * 获取该机构下的所有员工
	 * 
	 * @param depPath
	 * @return
	 * @throws Exception
	 */
	public List<LfEmployee> findEmployeeByDepPath(String depPath) throws Exception
	{
		return new PerfectDao().findEmployeeByDepPath(depPath);
	}

	/**
	 * 把群组的人区分出来
	 * 
	 * @param groupId
	 *        群组ID
	 * @param allEmployeeList
	 *        所有员工LIST
	 * @param allMalist
	 *        所有外部人员LIST
	 * @param repeatEmployeeList
	 *        重复的员工LIST
	 * @param repeatMalist
	 *        重复的外部人员LIST
	 * @param repeatUserList
	 *        重复的人员
	 * @param baseBiz
	 */
	public void findGroupUser(String groupId, List<LfEmployee> allEmployeeList, List<LfMalist> allMalist, List<LfEmployee> repeatEmployeeList, List<LfMalist> repeatMalist, HashSet<Long> repeatUserList, BaseBiz baseBiz)
	{

		try
		{
			LinkedHashMap<String, String> conditionMap = new LinkedHashMap<String, String>();
			conditionMap.put("udgId", groupId);
			List<LfList2gro> list2groes = baseBiz.getByCondition(LfList2gro.class, conditionMap, null);
			if(list2groes != null && list2groes.size() > 0)
			{
				for (int k = 0; k < list2groes.size(); k++)
				{
					LfList2gro temp = list2groes.get(k);
					Long temp_GrpGuid = 0L;
					temp_GrpGuid = temp.getGuId();
					if(temp.getL2gType() == 0)
					{ // 员工
						LfEmployee g_emp = baseBiz.getByGuId(LfEmployee.class, temp_GrpGuid);
						if(!repeatUserList.contains(temp_GrpGuid))
						{
							repeatUserList.add(temp_GrpGuid);
							allEmployeeList.add(g_emp);
						}
						else
						{
							repeatEmployeeList.add(g_emp);
						}
					}
					else
						if(temp.getL2gType() == 2)
						{ // 自定义
							LfMalist g_malist = baseBiz.getByGuId(LfMalist.class, temp_GrpGuid);
							if(!repeatUserList.contains(temp_GrpGuid))
							{
								repeatUserList.add(temp_GrpGuid);
								allMalist.add(g_malist);
							}
							else
							{
								repeatMalist.add(g_malist);
							}

						}
				}
			}
		}
		catch (Exception e)
		{
			EmpExecutionContext.error(e, "完美通知操作群组成员出现异常！");
		}
	}

	/**
	 * 传ID过来查询该机构下的员工，包含 和 包含子机构的2个情况 返回该机构下的员工
	 * 
	 * @param depId
	 *        机构ID
	 * @param allEmployeeList
	 *        所有员工LIST
	 * @param baseBiz
	 * @throws Exception
	 */
	public List<LfEmployee> findEmpUserByDepId(String depIds, String corpCode) throws Exception
	{
		List<LfEmployee> employeeList = null;
		try
		{

			LinkedHashMap<String, String> conditionMap = new LinkedHashMap<String, String>();
			// if(depIds.contains("c_")){
			if(depIds.contains("e"))
			{
				// String[] temp_depId = depIds.split("_");
				// conditionMap.put("depId", temp_depId[1]);
				String depid = depIds.substring(1);
				conditionMap.put("depId", depid);
				conditionMap.put("corpCode", corpCode);
				LfEmployeeDep dep = empDao.findListByCondition(LfEmployeeDep.class, conditionMap, null).get(0);
				conditionMap.clear();
				employeeList = this.findEmployeeByDepPath(dep.getDeppath());
				// TODO 调用存储过程处理子机构
			}
			else
			{
				conditionMap.put("depId", depIds);
				conditionMap.put("corpCode", corpCode);
				employeeList = empDao.findListByCondition(LfEmployee.class, conditionMap, null);
			}
		}
		catch (Exception e)
		{
			EmpExecutionContext.error(e, "完美通知操作员机构包含出现异常！");
		}
		return employeeList;

	}

	/**
	 * 将一个员工填加到一个LIST，判断它是否存在
	 * 
	 * @param employee
	 *        员工
	 * @param allEmployeeList
	 *        所有员工LIST
	 * @param repeatEmployeeList
	 *        重复的员工LIST
	 * @param repeatUserList
	 *        重复的发送人员
	 * @throws Exception
	 */
	public void addEmployeeToList(LfEmployee employee, List<LfEmployee> allEmployeeList, List<LfEmployee> repeatEmployeeList, HashSet<Long> repeatUserList)
	{
		try
		{
			if(!repeatUserList.contains(employee.getGuId()))
			{
				repeatUserList.add(employee.getGuId());
				allEmployeeList.add(employee);
			}
			else
			{
				repeatEmployeeList.add(employee);
			}
		}
		catch (Exception e)
		{
			EmpExecutionContext.error(e, "完美通知判断该集合包含该员工出现异常！");
		}

	}

	/**
	 * 将一个自定义填加到一个LIST，判断它是否存在
	 * 
	 * @param maList
	 *        外部人员
	 * @param allMalist
	 *        所有的外部人员
	 * @param repeatMalist
	 *        重复的外部人员
	 * @param repeatUserList
	 *        重复人员
	 */
	public void addMalistToList(LfMalist maList, List<LfMalist> allMalist, List<LfMalist> repeatMalist, HashSet<Long> repeatUserList)
	{
		try
		{
			if(!repeatUserList.contains(maList.getGuId()))
			{
				repeatUserList.add(maList.getGuId());
				allMalist.add(maList);
			}
			else
			{
				repeatMalist.add(maList);
			}
		}
		catch (Exception e)
		{
			EmpExecutionContext.error(e, "完美通知判断该集合包含该自定义人员出现异常！");
		}

	}

	/**
	 * 这里是检查电话号码是否能被用。
	 * 
	 * @param lfImMessage
	 *        短信
	 * @param moblie
	 *        手机号码
	 * @param effectMoblie
	 *        有限手机号
	 * @param haoduans
	 *        号段
	 * @param repeatList
	 *        重复LIST
	 * @param blList
	 *        黑名单列表
	 * @param map
	 * @param portsList
	 *        路由LIST
	 * @param blBiz
	 * @param wgMsgConfigBiz
	 * @param line
	 * @param noticUp
	 * @return
	 */
	public String inspectMobile(Message lfImMessage, String moblie, StringBuffer effectMoblie, String[] haoduans, HashSet<String> repeatList, String corpCode, String busCode, Map<Integer, String> map, List<GtPortUsed> portsList, BlackListAtom blBiz, WgMsgConfigBiz wgMsgConfigBiz, String line, LfPerfectNoticUp noticUp)
	{
		String isMsg = "0&0";
		try
		{
			Integer isValid = wgMsgConfigBiz.checkMobile(moblie, haoduans);
			if(isValid != null && isValid == 1)
			{
				if(!blBiz.checkBlackList(corpCode, moblie, busCode))
				{
					if(this.checkRepeat(repeatList, moblie))
					{
						noticUp.setIsReceive(2);
						// 未发送状态
						noticUp.setMobile(moblie);
						Integer spisuncm = 0;
						if("+".equals(moblie.substring(0, 1)) || "00".equals(moblie.substring(0, 2)))
						{
							spisuncm = 5;
						}
						else
						{
							spisuncm = wgMsgConfigBiz.getPhoneSpisuncm(moblie);
						}
						String spNumber = map.get(spisuncm);
						if(spNumber != null)
						{
							noticUp.setSpUser(lfImMessage.getMessageId());
							noticUp.setSpNumber(spNumber);
							noticUp.setReceiveCount(1);
							effectMoblie.append(moblie).append(line);
							Integer messageCount = this.countSmsNumber2(lfImMessage.getContent(), moblie, haoduans, portsList);
							isMsg = "2&" + messageCount;
						}
						else
						{
							noticUp.setIsReceive(8);
							isMsg = "8&0";
							// 运营商不符合，没有绑定对应的路由
						}
					}
					else
					{
						noticUp.setIsReceive(-2);
						noticUp.setMobile(moblie);
						isMsg = "-2&0";
						// 重复号码
					}
				}
				else
				{
					noticUp.setIsReceive(5);
					noticUp.setMobile(moblie);
					isMsg = "5&0";
					// 黑名单
				}
			}
			else
			{
				noticUp.setIsReceive(3);
				isMsg = "3&0";
				// 手机号码异常
			}
			String[] msg = isMsg.split("&");
			if(!"2".equals(msg[0]))
			{
				noticUp.setReceiveCount(0);
			}
		}
		catch (Exception e)
		{
			EmpExecutionContext.error(e, "完美通知检查电话号码是否能被用出现异常！");
			noticUp.setIsReceive(3);
			noticUp.setReceiveCount(0);
			isMsg = "3&0";
		}
		return isMsg;
	}

	/**
	 * 返回所有ID的字符
	 * 
	 * @param depIds
	 *        机构ID
	 * @param groupIds
	 *        群组ID
	 * @param empUserIds
	 *        员工ID
	 * @param deUserIds
	 *        自定义ID
	 * @return
	 */
	public String getChoiceIds(String depIds, String groupIds, String empUserIds, String deUserIds)
	{
		StringBuffer sb = new StringBuffer();
		if(!"".equals(depIds))
		{
			sb.append(depIds).append("#");
		}
		else
		{
			sb.append("n#");
		}
		if(!"".equals(groupIds))
		{
			sb.append(groupIds).append("#");
		}
		else
		{
			sb.append("n#");
		}
		if(!"".equals(empUserIds))
		{
			sb.append(empUserIds).append("#");
		}
		else
		{
			sb.append("n#");
		}
		if(!"".equals(deUserIds))
		{
			sb.append(deUserIds);
		}
		else
		{
			sb.append("n");
		}
		return sb.toString();
	}

	/**
	 * 获取需要重新发送的完美通知详情的LIST列表信息
	 * 
	 * @param noticeUpIds
	 * @return
	 */
	public List<LfPerfectNoticUp> getSenderNotice(String noticeUpIds, String pnoticeId)
	{
		List<LfPerfectNoticUp> noticeUpList = null;
		LinkedHashMap<String, String> conditionMap = new LinkedHashMap<String, String>();
		try
		{
			conditionMap.put("pnupId&in", noticeUpIds);
			conditionMap.put("pnotic", pnoticeId);
			// noticeUpList = empDao.findListByCondition(LfPerfectNoticUp.class,
			// conditionMap, null);
			noticeUpList = empDao.findListBySymbolsCondition(LfPerfectNoticUp.class, conditionMap, null);
		}
		catch (Exception e)
		{
			EmpExecutionContext.error(e, "获取需要重新发送的完美通知详情失败！");
		}
		return noticeUpList;
	}

	public Integer sendContentMsgCount(Message lfImMessage, String moblie, String[] haoduans, List<GtPortUsed> portsList)
	{
		Integer messageCount = 0;
		try
		{
			messageCount = this.countSmsNumber2(lfImMessage.getContent(), moblie, haoduans, portsList);
		}
		catch (Exception e)
		{
			EmpExecutionContext.error(e, "获取完美通知内容拆分条数出现异常！");
		}
		return messageCount;
	}

	/**
	 * 获取群组中的人员信息
	 * 
	 * @param groupId
	 * @return
	 * @throws Exception
	 */
	public List<GroupInfoVo> getGroupUser(Long groupId) throws Exception
	{
		List<GroupInfoVo> groupVosList = null;
		try
		{
			/*
			 * groupVosList = new
			 * GenericGroupInfoVoDAO().findGroupUserByIds(groupId);
			 */

			groupVosList = new PerfectDao().findGroupUserByIds(groupId);

		}
		catch (Exception e)
		{
			EmpExecutionContext.error(e, "获取群组中的人员信息失败！");
		}
		return groupVosList;
	}

	/**
	 * 处理群组中分页选择人员
	 * 
	 * @param groupId
	 *        群组ID
	 * @param pageInfo
	 *        分页信息
	 * @param type
	 *        员工 /客户 群组
	 * @return
	 * @throws Exception
	 */
	public List<GroupInfoVo> getGroupUser(Long groupId, String searchname, PageInfo pageInfo, String type) throws Exception
	{
		List<GroupInfoVo> groupVosList = null;
		try
		{
			// 查询员工群组
			if("1".equals(type))
			{
				/*
				 * groupVosList = new
				 * GenericGroupInfoVoDAO().findGroupUserByIds(groupId,pageInfo);
				 */
				groupVosList = new PerfectDao().findGroupUserByIds(groupId, searchname, pageInfo);
			}
			else
				if("2".equals(type))
				{
					// 查询客户群组
					/*
					 * groupVosList = new
					 * GenericGroupInfoVoDAO().findGroupClientByIds(groupId,
					 * pageInfo);
					 */
					groupVosList = new PerfectDao().findGroupClientByIds(groupId, pageInfo);
				}
		}
		catch (Exception e)
		{
			groupVosList = null;
			EmpExecutionContext.error(e, "完美通知处理群组人员失败！");
		}
		return groupVosList;
	}

	/**
	 * 扣费
	 * 
	 * @param wmInfo
	 * @param balanceBiz
	 * @param maxcout
	 * @return
	 *         * 1:短信回收成功
	 *         0:短信扣费成功
	 *         -1:短信扣费失败
	 *         -2:短信余额不足
	 *         -4:用户所属机构没有充值
	 *         -5:短信发送或回收条数不能为空
	 *         -9:短信回收失败
	 *         -9999:短信发送扣费回收接口调用异常
	 */
	public String koufei(Long guid, int maxcout)
	{
		String result = "2";
		try
		{
			BalanceLogBiz balanceBiz = new BalanceLogBiz();
			Integer resultMsg = balanceBiz.sendSmsAmountByGuid(null, guid, maxcout);
			if(resultMsg == 0)
			{
				result = "1";
			}
			else
				if(resultMsg == -2)
				{
					result = "6";
					// 短信余额不足
				}
				else
					if(resultMsg == -1)
					{
						result = "7";
						// 短信扣费失败
					}
					else
						if(resultMsg == -4)
						{
							result = "9";
							// 用户所属机构没有充值
						}
		}
		catch (Exception e)
		{
			EmpExecutionContext.error(e, "完美通知扣费失败");
		}
		return result;
	}

	/**
	 * 文件发送完美通知
	 * 
	 * @param wmInfo
	 *        短信载体
	 * @param balanceBiz
	 *        短信计费扣费的BIZ
	 * @param wgMsgConfigBiz
	 *        网关路由的BIZ
	 * @return
	 *         扣费的返回值
	 *         1:短信回收成功
	 *         0:短信扣费成功
	 *         -1:短信扣费失败
	 *         -2:短信余额不足
	 *         -4:用户所属机构没有充值
	 *         -5:短信发送或回收条数不能为空
	 *         -9:短信回收失败
	 *         -9999:短信发送扣费回收接口调用异常
	 */
	public String wmSendMsgByFile(Message wmInfo, BalanceLogBiz balanceBiz, WgMsgConfigBiz wgMsgConfigBiz)
	{
		String result = "2";
		if(wmInfo.getIsSingle())
		{
			// 这里判断是否启用计费功能
			Connection conn = empTransDao.getConnection();
			try
			{
				empTransDao.beginTransaction(conn);
				Integer resultMsg = balanceBiz.sendSmsAmountByGuid(conn, Long.valueOf(wmInfo.getTargetId()), wmInfo.getRecordCount());
				
				//SP账号余额检查
				int spFeeResult = balanceBiz.checkSpUserFee(wmInfo.getMessageId(), wmInfo.getRecordCount(), 1);
				EmpExecutionContext.info("完美通知发送" 
						+ "，机构扣费返回：" + resultMsg
						+ "，SP账号余额检查返回：" + spFeeResult
						+ "，guid：" + wmInfo.getTargetId()
						+ "，spUser：" + wmInfo.getMessageId()
						+ "，sendCount：" + wmInfo.getRecordCount()
						);
				
				//扣费成功且SP账号余额检查成功
				if(resultMsg == 0 && spFeeResult > -1)
				{
					String wegGateResult = this.wmSendMsg(wmInfo, wgMsgConfigBiz);
					if(wegGateResult == null || "".equals(wegGateResult))
					{
						empTransDao.rollBackTransaction(conn);
						result = "2";
						// 未发送成功
					}
					else
						if("000".equals(wegGateResult))
						{
							result = "1";
							// 发送到网关
							empTransDao.commitTransaction(conn);
						}
						else
						{
							empTransDao.rollBackTransaction(conn);
						}
				}
				else
					if(resultMsg == -2)
					{
						result = "6";
						// 短信余额不足
						empTransDao.rollBackTransaction(conn);
					}
					else
						if(resultMsg == -1)
						{
							result = "7";
							// 短信扣费失败
							empTransDao.rollBackTransaction(conn);
						}
						else
							if(resultMsg == -4)
							{
								result = "9";
								// 用户所属机构没有充值
								empTransDao.rollBackTransaction(conn);
							}
							else
								if(spFeeResult < 0)
								{
									// 余额不足
									result = "6";
									empTransDao.rollBackTransaction(conn);
								}
							else
							{
								result = resultMsg + "";
								// 事务回滚
								empTransDao.rollBackTransaction(conn);
							}
			}
			catch (Exception e)
			{
				// 事务回滚
				empTransDao.rollBackTransaction(conn);
				EmpExecutionContext.error(e, "完美通知请求网关失败！");
			}
			finally
			{
				// 这里处理更新发送的那批手机号码的接收状态。
				empTransDao.closeConnection(conn);
			}
		}
		else
		{
			try
			{
				String wegGateResult = this.wmSendMsg(wmInfo, wgMsgConfigBiz);
				if(wegGateResult == null || "".equals(wegGateResult))
				{
					result = "2";
					// 未发送成功
				}
				if("000".equals(wegGateResult))
				{
					result = "1";
					// 发送到网关
				}
			}
			catch (Exception e)
			{
				EmpExecutionContext.error(e, "完美通知请求网关失败！");
			}
		}
		return result;
	}

	/**
	 * 调用网关发送接口 文件发送
	 * 
	 * @param wmInfo
	 *        短信载体
	 * @param wgMsgConfigBiz
	 *        网关配置BIZ
	 * @return
	 * @throws Exception
	 */
	private String wmSendMsg(Message wmInfo, WgMsgConfigBiz wgMsgConfigBiz) throws Exception
	{
		String resultStr = "";
		String moblieUrl = wmInfo.getTargetPath().trim();
		String spUser = wmInfo.getMessageId();
		String userSubno = wmInfo.getToId();
		if(moblieUrl == null || "".equals(moblieUrl.trim()))
		{
			EmpExecutionContext.error("发送号码文件为空！");
			return null;
		}
		if(spUser == null || "".equals(spUser))
		{
			EmpExecutionContext.error("发送账号对象为空！");
			return null;
		}
		if(userSubno == null || "".equals(userSubno))
		{
			EmpExecutionContext.error("尾号为空！");
			return null;
		}
		// Userdata userdata = wgMsgConfigBiz.getUserdataByUserid(spUser);
		HttpSmsSend smsSend = new HttpSmsSend();
		WGParams params = new WGParams();
		params.setSpid(spUser);
		// SP帐号
		params.setSppassword(wmInfo.getMtmsgid());
		// SP密码
		params.setBmttype("1");
		// 群发类型 相同1 不同2
		params.setTitle("完美通知发送");
		// 群发标题
		params.setTaskid(wmInfo.getTaskId().toString());
		// 任务ID号
		params.setContent(wmInfo.getContent());
		// 群发内容
		params.setPriority("1");
		// 优化级 1 最高
		params.setUrl(moblieUrl);
		// 群发手机号码URL
		params.setParam1(wmInfo.getFromId());
		// 用户编码USERCODE
		params.setSvrtype(wmInfo.getName());
		// 业务类型
		params.setRptflag("1");
		// 需要状态报告
		params.setMsgid(wmInfo.getTaskId().toString());
		// 自定义消息ID
		// params.setModuleid(StaticValue.WMNOTICECODE);
		// 模块ID 10030
		params.setModuleid(StaticValue.WMNOTICECODE);
		// 拓展子号
		params.setSa(userSubno);
		String fileUri=null;
		// 判断是否使用集群
		if(StaticValue.getISCLUSTER() == 1)
		{
			CommonBiz commBiz = new CommonBiz();
			// 上传文件到文件服务器
//			if("success".equals(commBiz.uploadFileToFileCenter(moblieUrl)))
//			{
//				// 删除本地文件
//				//commBiz.deleteFile(moblieUrl);
//			}
//			else
//			{
//				resultStr = "error";
//			}
			fileUri=commBiz.uploadFileToFileServer(moblieUrl);
		}
		else
		{
			fileUri=StaticValue.BASEURL;
		}
		params.setUrl(fileUri+moblieUrl);
		EmpExecutionContext.info("完美通知发送taskid为:"+params.getTaskid()+",完美通知发送文件请求地址为："+params.getUrl());

		// 如果结果状态未被更改，则进行发送
		if("".equals(resultStr))
		{
			// 向网关发送请求
			String result = smsSend.createbatchMtRequest(params);
			int index = result.indexOf("mterrcode");
			if(index < 0)
			{
				resultStr = "error";
				// 下行状态设置为已发送失败
			}
			else
			{
				resultStr = result.substring(index + 10, index + 13);
				if(resultStr.equals("000"))
				{
					resultStr = "000";
					// 下行状态设置为已发送成功
				}
				else
				{
					resultStr = result.substring(index - 8, index - 1);
				}
			}
		}
		return resultStr;
	}

	/**
	 * 通过员工机构id查找树
	 * 
	 * @param lfSysuser
	 * @param depIds
	 *        机构Id集合字符串
	 * @return
	 * @throws Exception
	 */
	public List<LfEmployeeDep> getEmpSecondDepTreeByUserIdorDepId(String userId, String depId) throws Exception
	{
		List<LfEmployeeDep> deps = null;
		try
		{
			deps = new PerfectDao().getEmpSecondDepTreeByUserIdorDepId(userId, depId);
		}
		catch (Exception e)
		{
			EmpExecutionContext.error(e, "完美通知获取员工机构树出现异常！");
		}
		return deps;
	}
	
	/**
	 * 通过员工机构id查找树(重载方法)
	 * 
	 * @param lfSysuser
	 * @param depIds
	 *        机构Id集合字符串
	 * @param corpCode 企业编码
	 * @return
	 * @throws Exception
	 */
	public List<LfEmployeeDep> getEmpSecondDepTreeByUserIdorDepId(String userId, String depId,String corpCode) throws Exception
	{
		List<LfEmployeeDep> deps = null;
		try
		{
			//调用DAO方法查询数据
			deps = new PerfectDao().getEmpSecondDepTreeByUserIdorDepId(userId, depId,corpCode);
		}
		catch (Exception e)
		{
			EmpExecutionContext.error(e, "完美通知获取员工机构树出现异常！");
		}
		return deps;
	}

	public String[] getSaveUrl(Long id, Long perfectTaskId, int count)
	{
		TxtFileUtil txtfileutil = new TxtFileUtil();
		String strNYR = "";
		try
		{
			strNYR = txtfileutil.getCurNYR();
		}
		catch (Exception e1)
		{
			EmpExecutionContext.error(e1, "完美通知获取文件路径出现异常！");
			return null;
		}
		// 第一次创建其文件夹
		String fileDirUrl = StaticValue.PNOTICE_MTTASKS + strNYR;
		try
		{
			File perNoticeFile = new File(txtfileutil.getWebRoot() + fileDirUrl);
			if(!perNoticeFile.exists())
			{
				perNoticeFile.mkdirs();
			}
		}
		catch (Exception e)
		{
			EmpExecutionContext.error(e, "完美通知获取文件路径失败！");
			return null;
		}
		GetSxCount sx = GetSxCount.getInstance();
		Date time = Calendar.getInstance().getTime();
		String[] url = new String[6];
		String logicUrl;
		String physicsUrl = txtfileutil.getWebRoot();
		// 存放路径的数组
		String saveName = count + "_" + id + "_" + perfectTaskId + "_" + (new SimpleDateFormat("yyyyMMddHHmmssS")).format(time) + "_" + sx.getCount() + ".txt";
		physicsUrl = physicsUrl + fileDirUrl + saveName;
		logicUrl = fileDirUrl + saveName;
		url[0] = physicsUrl;
		url[1] = logicUrl;
		url[2] = url[0].replace(".txt", "_bad.txt");
		url[3] = url[0].replace(".txt", "_view.txt");
		url[4] = url[1].replace(".txt", "_view.txt");
		url[5] = url[1].replace(".txt", "_bad.txt");
		return url;
	}

	/**
	 * 获取完美通知号码
	 * 
	 * @param depIds
	 *        机构ID
	 * @param groupIds
	 *        群组ID
	 * @param empUserIds
	 *        员工ID
	 * @param deUserIds
	 *        自定义ID
	 * @param corpCode
	 *        企业编码
	 * @return List<DynaBean>集合 返回值为null代表出异常，返回值size()为0，代表没有记录。
	 */
	public List<DynaBean> findNoticePhone(String depIds, String groupIds, String empUserIds, String deUserIds, String addphonename, String corpCode)
	{
		List<DynaBean> phoneLists = new ArrayList<DynaBean>();
		PerfectDao perfectDao = new PerfectDao();
		List<DynaBean> tempPhoneLists = null;

		try
		{
			// 处理所选择的机构
			if(depIds != null && depIds.length() > 0)
			{
				depIds = depIds.substring(0, depIds.length() - 1);
				if(depIds != null && depIds.length() > 0)
				{
					tempPhoneLists = findEmployeeByDepIds(perfectDao, depIds, corpCode);
					if(tempPhoneLists != null)
					{
						if(tempPhoneLists.size() > 0)
						{
							phoneLists.addAll(tempPhoneLists);
							tempPhoneLists = null;
						}
					}
					else
					{
						EmpExecutionContext.error("完美通知查询所选员工机构号码异常！");
						return null;
					}
				}
			}
			// 处理群组
			if(groupIds != null && groupIds.length() > 0)
			{
				groupIds = groupIds.substring(0, groupIds.length() - 1);
				if(groupIds != null && groupIds.length() > 0)
				{
					EmpExecutionContext.info("[完美通知]选择的员工群组ID为："+groupIds+"。");
					tempPhoneLists = perfectDao.findEmployeeByGroupGuid(groupIds);
					if(tempPhoneLists != null)
					{
						if(tempPhoneLists.size() > 0)
						{
							phoneLists.addAll(tempPhoneLists);
						}
					}
					else
					{
						EmpExecutionContext.error("完美通知查询群组中员工的号码异常！");
						return null;
					}
					tempPhoneLists = perfectDao.findMaListByGroupGuid(groupIds);
					if(tempPhoneLists != null)
					{
						if(tempPhoneLists.size() > 0)
						{
							phoneLists.addAll(tempPhoneLists);
						}
					}
					else
					{
						EmpExecutionContext.error("完美通知查询群组中自定义人员的号码异常！");
						return null;
					}
				}
			}
			// 处理选择员工
			if(empUserIds != null && empUserIds.length() > 0)
			{
				empUserIds = empUserIds.substring(0, empUserIds.length() - 1);
				if(empUserIds != null && empUserIds.length() > 0)
				{
					tempPhoneLists = perfectDao.findEmployeeByGuid(empUserIds, corpCode);
					if(tempPhoneLists != null)
					{
						if(tempPhoneLists.size() > 0)
						{
							phoneLists.addAll(tempPhoneLists);
						}
					}
					else
					{
						EmpExecutionContext.error("完美通知根据GUID查找员工的号码异常！");
						return null;
					}
				}

			}
			// 处理自定义
			if(deUserIds != null && deUserIds.length() > 0)
			{
				deUserIds = deUserIds.substring(0, deUserIds.length() - 1);
				if(deUserIds != null && deUserIds.length() > 0)
				{
					tempPhoneLists = perfectDao.findMaListByGuid(deUserIds, corpCode);
					if(tempPhoneLists != null)
					{
						if(tempPhoneLists.size() > 0)
						{
							phoneLists.addAll(tempPhoneLists);
						}
					}
					else
					{
						EmpExecutionContext.error("完美通知根据GUID查找自定义人员的号码异常！");
						return null;
					}
				}
			}
			// 处理手工添加的用户
			if(addphonename != null && !"".equals(addphonename))
			{
				tempPhoneLists = findHandPhone(addphonename);
				if(tempPhoneLists != null)
				{
					if(tempPhoneLists.size() > 0)
					{
						phoneLists.addAll(tempPhoneLists);
					}
				}
				else
				{
					return null;
				}
			}
		}
		catch (Exception e)
		{
			EmpExecutionContext.error(e, "完美通知查询号码异常！");
			return null;
		}
		return phoneLists;

	}

	/**
	 * 处理手工输入的号码
	 * 
	 * @param addphonename
	 * @return 返回bean集合
	 */
	private List<DynaBean> findHandPhone(String addphonename)
	{
		List<DynaBean> handBeanList = new ArrayList<DynaBean>();
		DynaProperty[] props = new DynaProperty[] {new DynaProperty("mobile", String.class),new DynaProperty("guid", Integer.class),new DynaProperty("name", String.class),new DynaProperty("usertype", Integer.class),};
		BasicDynaClass dynaClass = new BasicDynaClass("user", BasicDynaBean.class, props);
		DynaBean dynaBean = null;

		try
		{
			String[] phonenamearr = addphonename.split("#");
			for (String temp : phonenamearr)
			{
				if(temp != null && !"".equals(temp))
				{
					String[] phonename = temp.split("_");
					if(phonename != null)
					{
						dynaBean = dynaClass.newInstance();
						dynaBean.set("mobile", phonename[0]);
						dynaBean.set("guid", -1);
						dynaBean.set("name", phonename[1]);
						dynaBean.set("usertype", 3);
						handBeanList.add(dynaBean);
					}
				}
			}
		}
		catch (Exception e)
		{
			EmpExecutionContext.error(e, "完美通知处理手工添加的号码异常！");
			return null;
		}
		return handBeanList;
	}

	private boolean checkphoneRepeat(HashSet<Long> repeatSet, String phone)
	{
		if(repeatSet.contains(Long.parseLong(phone)))
		{
			return false;
		}
		else
		{
			repeatSet.add(Long.parseLong(phone));
		}
		return true;
	}

	/**
	 * @description 将数据增加到完美通知明细表中去
	 * @param arr
	 *        用户类型 1员工2自定义3手工
	 *        名字
	 *        手机号码
	 *        guid
	 * @param lfPerfectNotic
	 *        主表信息
	 * @param perfectNoticUpList
	 *        需要新增的明细
	 * @param wgMsgConfigBiz
	 *        spuser工具类
	 * @param map
	 *        通道号集合
	 * @param spId
	 *        spuser
	 * @param noticUp
	 *        明细对象
	 * @author yejiangmin <282905282@qq.com>
	 * @datetime 2013-10-23 下午05:02:23
	 */
	public void addPerfectNoticUp(String[] arr, LfPerfectNotic lfPerfectNotic, List<LfPerfectNoticUp> perfectNoticUpList, WgMsgConfigBiz wgMsgConfigBiz, Map<Integer, String> map, String spId, LfPerfectNoticUp noticUp, String spNumber)
	{
		try
		{
			// 格式是 guid，手机号码，用户类型，名字
			LfPerfectNoticUp noticUp2 = new LfPerfectNoticUp();
			noticUp2.setUserType(Integer.valueOf(arr[2]));
			if(arr[3] == null || "".equals(arr[3]))
			{
				arr[3] = "-";
			}
			noticUp2.setName(arr[3]);
			if(arr[1] == null || "".equals(arr[1]))
			{
				EmpExecutionContext.error("完美通知新增明细手机号码为空");
				return;
			}
			noticUp2.setMobile(arr[1]);
			//noticUp.setPnotic(lfPerfectNotic.getNoticId());
			noticUp2.setPnotic(0L);
			noticUp2.setSenderGuid(lfPerfectNotic.getSenderGuid());
			if(arr[0] == null || "".equals(arr[0]))
			{
				EmpExecutionContext.error("完美通知新增明细guid为空");
				return;
			}
			// 把接收者的GUID放进去。 3表示手工
			noticUp2.setReceiverId(Long.valueOf(arr[0]));
			noticUp2.setTaskId(lfPerfectNotic.getTaskId());
			noticUp2.setCreateTime(new Timestamp(System.currentTimeMillis()));
			// 未发送状态
			noticUp2.setIsReceive(2);
			noticUp2.setSpUser(spId);
			noticUp2.setReceiveCount(1);
			// 需要发送状态
			noticUp2.setDialogId("1");
			// 可以发送，初始化 状态报告未接收 1是接收 ，2是未接收，3是不可以发送
			noticUp2.setIsAtrred("2");
			// 是否回复 0 是 1 不是
			noticUp2.setIsReply(1);
			String spNumber2 = " ";
			try
			{
				int spIsuncm = 0;
				if("+".equals(arr[1].substring(0, 1)) || "00".equals(arr[1].substring(0, 2)))
				{
					spIsuncm = 5;
				}
				else
				{
					spIsuncm = wgMsgConfigBiz.getPhoneSpisuncm(arr[1]);
				}
				spNumber2 = map.get(spIsuncm);
				if(spNumber2 == null || "".equals(spNumber2.trim()))
				{
					// 运营商不符合
					noticUp2.setIsReceive(8);
					noticUp2.setReceiveCount(0);
					// 不需要发送状态
					noticUp2.setDialogId("2");
					// 可以发送，初始化 状态报告未接收 1是接收 ，2是未接收，3是不可以发送
					noticUp2.setIsAtrred("3");
					noticUp2.setIsReply(2);
				}
			}
			catch (Exception e)
			{
				spNumber2 = " ";
				EmpExecutionContext.error("完美通知获取该手机号码所对应的通道道出现异常！");
			}
			noticUp2.setSpNumber(spNumber2);
			perfectNoticUpList.add(noticUp2);
		}
		catch (Exception e)
		{
			EmpExecutionContext.error(e, "完美通知新增明细失败！");
		}
	}

	/**
	 * @description 完美通知处理异常
	 * @param returnMsg
	 *        返回的buffer
	 * @param objMap
	 *        存放KEY VALUE的集合
	 * @param preParams
	 *        预览参数类
	 * @param stage
	 *        返回页面的stage标识
	 * @param errormsg
	 *        错误信息
	 * @param errertype
	 *        错误类型 1参数获取失败 2是获取emp信息出现异常3 出现异常
	 * @param exception
	 *        异常
	 * @author yejiangmin <282905282@qq.com>
	 * @datetime 2013-10-25 上午09:48:55
	 */
	public void handlPerErrer(StringBuffer returnMsg, Map<String, Object> objMap, PreviewParams preParams, String stage, String errormsg)
	{
		try
		{
			objMap.put("stage", stage);
			objMap.put("errormsg", errormsg);
			returnMsg.append(preParams.getJsonStr(objMap));
		}
		catch (Exception e)
		{
			EmpExecutionContext.error(e, "完美通知处理异常方法发生异常。");
		}

	}

	/**
	 * @description 获取完美通知文件中需要发送的信息存放在集合中
	 * @param path
	 *        文件全路径
	 * @param lfPerfectNotic
	 *        完美通知主表对象
	 * @param perfectNoticUpList
	 *        完美通知明细集合
	 * @param wgMsgConfigBiz
	 * @param spId
	 *        spid
	 * @param usedSubno
	 *        尾号
	 * @return success 成功 nogtport 路由信息为空 nousedmap 全通道号为空 errer出现异常
	 * @author yejiangmin <282905282@qq.com>
	 * @datetime 2013-10-26 上午10:14:16
	 */
	public String getPerfectSendFileMsg(String path, LfPerfectNotic lfPerfectNotic, List<LfPerfectNoticUp> perfectNoticUpList, WgMsgConfigBiz wgMsgConfigBiz, String spId, String usedSubno, LfPerfectNoticUp noticUp)
	{
		SmsBiz smsBiz = new SmsBiz();
		SubnoManagerBiz subnoManagerBiz = new SubnoManagerBiz();
		BufferedReader br = null;
		File file = null;
		try
		{
			// 获取该SP帐号的路由信息
			List<GtPortUsed> portsList = smsBiz.getPortByUserId(spId);
			if(portsList == null || portsList.size() == 0)
			{
				EmpExecutionContext.error("完美通知获取" + spId + "帐号的路由信息为空！");
				return "nogtport";
			}
			Map<Integer, String> usedmap = subnoManagerBiz.getPortUserBySpuser(portsList, spId, usedSubno);
			if(usedmap == null || usedmap.size() == 0)
			{
				EmpExecutionContext.error("完美通知获取" + spId + "帐号的全通道号为空！");
				return "nousedmap";
			}
			//file = new File(path);
			//br = new BufferedReader(new FileReader(file));
			br = new BufferedReader(new InputStreamReader(new FileInputStream(path),"GBK"));
			String tempString = null;
			// 一次读入一行，直到读入null为文件结束
			String[] infoarr = null;
			// 初始化里面的spnumber
			String spnumber = "";
			while((tempString = br.readLine()) != null)
			{
				if("".equals(tempString))
				{
					continue;
				}
				infoarr = tempString.split("#");
				if(infoarr != null && infoarr.length == 4)
				{
					this.addPerfectNoticUp(infoarr, lfPerfectNotic, perfectNoticUpList, wgMsgConfigBiz, usedmap, spId, noticUp, spnumber);
				}
				infoarr = null;
			}
			//判断是否为空 或者为null
			if(perfectNoticUpList == null || perfectNoticUpList.size() == 0)
			{
				EmpExecutionContext.error("完美通知获取文件中的记录为空！");
				return "norecord";
			}
			// 插入完美通知明细数据
			if(empDao.save(perfectNoticUpList, LfPerfectNoticUp.class) == 0)
			{
				EmpExecutionContext.error("完美通知执行LfPerfectNoticUp新增失败！");
				return "addfail";
			}
			
		}
		catch (Exception e)
		{
			EmpExecutionContext.error(e, "完美通知读取文件出现异常！");
			return "errer";
		}
		finally
		{
			try
			{
				if(br != null)
				{
					br.close();
				}
			}
			catch (Exception e1)
			{
				EmpExecutionContext.error(e1, "完美通知关闭IO流出现异常！");
			}
			finally
			{
				if(file != null && file.exists())
				{
					boolean state = file.delete();
					if(!state){
						EmpExecutionContext.error("文件删除异常");
					}
				}
			}
		}
		return "success";
	}

	/**
	 * @description 通过id字符串获取员工成员列表(改)depIds为,e1,3,10,e23,这种类型的字符串
	 * @param depIds
	 *        机构IDS
	 * @param corpCode
	 *        企业编码
	 * @return
	 * @author tanglili <282905282@qq.com>
	 * @datetime 2013-10-23 下午07:28:12
	 */
	private List<DynaBean> findEmployeeByDepIds(PerfectDao perfectDao, String depIds, String corpCode)
	{
		List<DynaBean> retrunDynaBean=new ArrayList<DynaBean>();
		List<DynaBean> dynaBean=null;
		try
		{
			String[] depIdsArr = depIds.split(",");
			// 存放不包含子机构的机构ID
			StringBuffer depIdsBuffer = new StringBuffer("");
			// 存放包含子机构的机构ID
			StringBuffer containsSunDepIdsBuffer = new StringBuffer("");
			// 循环所有的机构ID
			for (int a = 0; a <depIdsArr.length; a++)
			{
				if(depIdsArr[a]!=null&&!"".equals(depIdsArr[a]))
				{
					if(depIdsArr[a].indexOf("e") > -1)
					{
						// 包含子机构的
						containsSunDepIdsBuffer.append(depIdsArr[a].substring(1)).append(",");
					}
					else
					{
						// 不包含子机构的
						depIdsBuffer.append(depIdsArr[a]).append(",");
					}
					EmpExecutionContext.info("[完美通知]包含子机构的机构ID为:"+containsSunDepIdsBuffer.toString()+";不包含子机构的机构ID为:"+depIdsBuffer.toString()+"。");
				}
			}
			// 先遍历不包含子机构的
			if(depIdsBuffer.length()>0){
				depIdsBuffer.deleteCharAt(depIdsBuffer.lastIndexOf(","));
				dynaBean=perfectDao.findEmployeeByDepIds(depIdsBuffer.toString(), corpCode);
				if(dynaBean!=null){
					if(dynaBean.size()>0){
						retrunDynaBean.addAll(dynaBean);
						dynaBean=null;
					}
				}else{
					EmpExecutionContext.error("完美通知通过不包含子机构的机构获取员工成员列表异常。");
					return null;
				}
			}
			
			// 再遍历包含子机构的
			if(containsSunDepIdsBuffer.length()>0){
				containsSunDepIdsBuffer.deleteCharAt(containsSunDepIdsBuffer.lastIndexOf(","));
				LinkedHashMap<String, String> conditionMap = new LinkedHashMap<String, String>();
				conditionMap.put("depId&in", containsSunDepIdsBuffer.toString());
				conditionMap.put("corpCode", corpCode);
				List<LfEmployeeDep> employeeDeps=empDao.findListBySymbolsCondition(LfEmployeeDep.class, conditionMap, null);
				if(employeeDeps != null && employeeDeps.size() > 0)
				{
					dynaBean=perfectDao.findEmployeeByDep(employeeDeps);
					if(dynaBean!=null){
						if(dynaBean.size()>0){
							retrunDynaBean.addAll(dynaBean);
							dynaBean=null;
						}
					}else{
						EmpExecutionContext.error("完美通知通过包含子机构的机构获取员工成员列表异常。");
						return null;
					}
				}
			}
		}
		catch (Exception e)
		{
			EmpExecutionContext.error(e, "完美通知通过机构获取员工成员列表异常。");
			return null;
		}
		return retrunDynaBean;
	}

	/**
	 * @description 解析所选的完美通知对象
	 * @param params
	 *        预览参数类
	 * @param depIds
	 *        机构IDS
	 * @param groupIds
	 *        群组ID
	 * @param empUserIds
	 *        员工IDS
	 * @param deUserIds
	 *        自定义IDS
	 * @param addphonename
	 *        手工输入
	 * @param haoduan
	 *        号段
	 * @param corpCode
	 *        企业编码
	 * @param busCode
	 *        业务编码
	 * @return success 成功 errer 出现异常 numerrer 号码文件写入失败 infofileerrer文件信息号码文件写入失败
	 * @author yejiangmin <282905282@qq.com>
	 * @datetime 2013-10-26 下午03:24:46
	 */
	public String parsePerfectPhone(PreviewParams params, String depIds, String groupIds, String empUserIds, String deUserIds, String addphonename, String[] haoduan, String corpCode, String busCode,String empLangName)
	{
		HashSet<Long> repeatSet = new HashSet<Long>();
		PhoneUtil phoneUtil = new PhoneUtil();
		BlackListAtom blBiz = new BlackListAtom();
		// 将数据格式 guid + 手机 + 类型 + 名字 写文件 "，"号分隔
		StringBuffer infoBuffer = new StringBuffer();
		// 有效号码
		StringBuffer contentSb = new StringBuffer();
		// 无效号码
		StringBuffer badContentSb = new StringBuffer();
		TxtFileUtil txtfileutil = new TxtFileUtil();
		String returnmsg = "";
		//号码返回状态
		int resultStatus = 0;
		try
		{
			// 获取完美通知 选择的对象信息，返回动态bean的集合
			List<DynaBean> beanList = this.findNoticePhone(depIds, groupIds, empUserIds, deUserIds, addphonename, corpCode);
			if(beanList == null)
			{
				EmpExecutionContext.error("完美通知获取手机号码的动态bean集合方法出现异常！");
				return "getPhoneError";
			}
			if(beanList.size() == 0)
			{
				return "nonum";
			}
			// 手机号码
			String mobile = null;
			// 动态bean
			DynaBean bean = null;
			// 循环
			for (int i = 0; i < beanList.size(); i++)
			{
				bean = beanList.get(i);
				if(bean == null || bean.get("mobile") == null)
				{
					continue;
				}
				mobile = bean.get("mobile").toString();
				// 号码数
				params.setSubCount(params.getSubCount() + 1);
				// 检测是否格式合法
				if(phoneUtil.checkMobile(mobile, haoduan) != 1&&!phoneUtil.isAreaCode(mobile))
				{
					badContentSb.append("zh_HK".equals(empLangName)?"Illegal format：":"格式非法"+"：").append(mobile).append(line);
					// 不合法号码
					params.setBadModeCount(params.getBadModeCount() + 1);
					params.setBadCount(params.getBadCount() + 1);
				}
				else
					if((resultStatus = phoneUtil.checkRepeat(mobile, repeatSet)) !=0)
					{
						if(resultStatus == 1)
						{
							// 检测是否重复号码
							badContentSb.append("zh_HK".equals(empLangName)?"Repeated number：":"zh_TW".equals(empLangName)?"重複號碼":"重复号码"+"：").append(mobile).append(line);
							// 重复号码数
							params.setRepeatCount(params.getRepeatCount() + 1);
							params.setBadCount(params.getBadCount() + 1);
						}
						else
						{
							badContentSb.append("zh_HK".equals(empLangName)?"Illegal format：":"格式非法"+"：").append(mobile).append(line);
							// 不合法号码
							params.setBadModeCount(params.getBadModeCount() + 1);
							params.setBadCount(params.getBadCount() + 1);							
						}

					}
					else
						if(blBiz.checkBlackList(corpCode, mobile, busCode))
						{
							// 是否黑名单
							badContentSb.append("zh_HK".equals(empLangName)?"Blacklist number：":"zh_TW".equals(empLangName)?"黑名單號碼":"黑名单号码"+"：").append(mobile).append(line);
							// 黑名单数
							params.setBlackCount(params.getBlackCount() + 1);
							params.setBadCount(params.getBadCount() + 1);
						}
						else
						{
							contentSb.append(mobile).append(line);
							// 有效号码数
							params.setEffCount(params.getEffCount() + 1);
							// 这里写到文件中的信息
							infoBuffer.append(bean.get("guid")).append("#").append(mobile).append("#").append(bean.get("usertype")).append("#").append(bean.get("name")).append(line);
							// 将号码可见
							if(params.getEffCount() < 11)
							{
								if(mobile != null && !"".equals(mobile) && params.getIshidephone() == 0)
								{
									mobile = cv.replacePhoneNumber(mobile);
								}
								params.setPreviewPhone(params.getPreviewPhone() + mobile + ";");
							}
						}
			}

			// 处理完成，将bean的集合清空。
			beanList = null;

			// 处理预览号码内容，有值则去掉最后一个分号
			String previewPhone = params.getPreviewPhone();
			if(previewPhone.lastIndexOf(";") > 0)
			{
				previewPhone = previewPhone.substring(0, previewPhone.lastIndexOf(";"));
				params.setPreviewPhone(previewPhone);
			}
			// 获取完美通知号码文件url
			String phoneFilePath = params.getPhoneFilePath()[0];
			// 写合法号码文件
			boolean flag = false;
			if(contentSb.length() > 0)
			{
				flag = txtfileutil.writeToTxtFile(phoneFilePath, contentSb.toString());
				contentSb.setLength(0);
				if(!flag)
				{
					EmpExecutionContext.error(" 完美通知 写合法号码文件失败！");
					return "numerrer";
				}
			}
			// 写不合法号码文件
			if(badContentSb.length() > 0)
			{
				flag = false;
				flag = txtfileutil.writeToTxtFile(params.getPhoneFilePath()[2], badContentSb.toString());
				badContentSb.setLength(0);
				if(!flag)
				{
					EmpExecutionContext.error(" 完美通知 写不合法号码文件失败！");
				}
			}
			// 写信息文件 格式guid ,号码 ,用户类型 , 名字
			if(infoBuffer.length() > 0)
			{
				flag = false;
				phoneFilePath = phoneFilePath.substring(0, phoneFilePath.length() - 4) + "_temp.txt";
				flag = txtfileutil.writeToTxtFile(phoneFilePath, infoBuffer.toString());
				infoBuffer.setLength(0);
				if(!flag)
				{
					EmpExecutionContext.error(" 完美通知 写信息文件失败！");
					return "infofileerrer";
				}
			}
			returnmsg = "success";
		}
		catch (Exception e)
		{
			returnmsg = "errer";
			EmpExecutionContext.error(e, "完美通知手机号码合法过滤出现异常！");
		}
		return returnmsg;

	}

	/**
	 * @description 第一次更新完美通知明细
	 * @param taskId
	 *        任务ID
	 * @param senderId
	 *        发送这guid
	 * @param isReceive
	 *        接收状态
	 * @param updatetype
	 *        needadd 需要加一个条件 noneed 不需要增加
	 * @param objectMap
	 *        更新map
	 * @param conditionMap
	 *        条件map
	 * @param baseBiz
	 *        基本逻辑类
	 * @author yejiangmin <282905282@qq.com>
	 * @datetime 2013-10-26 下午03:09:46
	 */
	public void updatePerfectNoticUp(Long taskId, Long senderId, String isReceive, String updatetype, LinkedHashMap<String, String> objectMap, LinkedHashMap<String, String> conditionMap, BaseBiz baseBiz)
	{
		try
		{
			conditionMap.clear();
			objectMap.clear();
			// 需要发送状态
			conditionMap.put("dialogId", "1");
			conditionMap.put("taskId", String.valueOf(taskId));
			conditionMap.put("senderGuid", String.valueOf(senderId));
			if("needadd".equals(updatetype))
			{
				conditionMap.put("isAtrred", "2");
			}
			objectMap.put("isReceive", isReceive);
			baseBiz.update(LfPerfectNoticUp.class, objectMap, conditionMap);
		}
		catch (Exception e)
		{
			EmpExecutionContext.error(e, "完美通知更新明细出现异常！");
		}
	}

	/**
	 * @description 第2次以及以上更新完美通知明细信息
	 * @param taskId
	 *        任务ID
	 * @param senderId
	 *        发送者guid
	 * @param isReceive
	 *        接收状态
	 * @param sendtime
	 *        发送次数
	 * @param objectMap
	 *        更新map
	 * @param conditionMap
	 *        条件map
	 * @param baseBiz
	 *        基本类
	 * @author yejiangmin <282905282@qq.com>
	 * @datetime 2013-10-26 下午03:08:44
	 */
	public void updateAgainPerfectNoticUp(Long taskId, Long senderId, String isReceive, int sendtime, LinkedHashMap<String, String> objectMap, LinkedHashMap<String, String> conditionMap, BaseBiz baseBiz)
	{
		try
		{
			// 这里先更新所有的发送次数
			conditionMap.clear();
			objectMap.clear();
			conditionMap.put("dialogId", "1");
			conditionMap.put("taskId", String.valueOf(taskId));
			conditionMap.put("senderGuid", String.valueOf(senderId));
			objectMap.put("receiveCount", String.valueOf(sendtime + 1));
			baseBiz.update(LfPerfectNoticUp.class, objectMap, conditionMap);
			// 这里更新的是所有 没有接收状态的 的完美通知
			conditionMap.put("isAtrred", "2"); // 标志没有接收到状态报告的完美通知
			// 标志没有接收到状态报告的完美通知
			objectMap.clear();
			objectMap.put("isReceive", isReceive);
			baseBiz.update(LfPerfectNoticUp.class, objectMap, conditionMap);
		}
		catch (Exception e)
		{
			EmpExecutionContext.error(e, "完美通知更新明细出现异常！");
		}
	}

	/**
	 * @description 根据任务ID查询完美通知主表信息
	 * @param taskId
	 *        任务ID
	 * @param corpCode
	 *        企业编码
	 * @param conditionMap
	 *        条件
	 * @param baseBiz
	 *        基本逻辑类
	 * @return
	 * @author yejiangmin <282905282@qq.com>
	 * @datetime 2013-10-26 下午05:08:44
	 */
	public LfPerfectNotic findLfPerfectNoticByTaskId(Long taskId, String corpCode, LinkedHashMap<String, String> conditionMap, BaseBiz baseBiz)
	{
		LfPerfectNotic perfectNotic = null;
		try
		{
			conditionMap.clear();
			conditionMap.put("corpCode", corpCode);
			conditionMap.put("taskId", String.valueOf(taskId));
			perfectNotic = baseBiz.getByCondition(LfPerfectNotic.class, conditionMap, null).get(0);
		}
		catch (Exception e)
		{
			EmpExecutionContext.error(e, "完美通知更新明细出现异常！");
		}
		return perfectNotic;
	}

	/**
	 * 
	 * @description    	处理完美通知重新发送时的所选择对象的处理
	 * @param lfPerfectNotic	完美通知主表
	 * @param perfectNoticUpList	完美通知明细集合
	 * @param wgMsgConfigBiz	工具类
	 * @param noticeUp2	完美通知明细对象
	 * @param conditionMap	条件map
	 * @param noticeUpIds	所选择的字符串ID
	 * @param oldNoticeId	老的完美通知ID
	 * @param effectMoblie	有效号码buffer
	 * @param lfImMessage	号码信息传值参
	 * @param lfsysuser	当前对象
	 * @param txtfileutil	写文件工具类
	 * @param url	文件路径
	 * @return       			 
	 * @author yejiangmin <282905282@qq.com>
	 * @datetime 2013-10-30 上午09:50:37
	 */
	public String getReSendPerfectMsg(LfPerfectNotic lfPerfectNotic,
			WgMsgConfigBiz wgMsgConfigBiz, LfPerfectNoticUp noticeUp, LinkedHashMap<String, String> conditionMap,
			String noticeUpIds,String oldtaskid, StringBuffer effectMoblie,
			Message lfMessage, LfSysuser lfsysuser,TxtFileUtil txtfileutil,String[] url,String empLangName)
	{
		SmsBiz smsBiz = new SmsBiz();
		SubnoManagerBiz subnoManagerBiz = new SubnoManagerBiz();
		PhoneUtil phoneUtil = new PhoneUtil();
		BlackListAtom blBiz = new BlackListAtom();
		StringBuffer badContentSb = new StringBuffer();
		try
		{
			String[] haoduan = wgMsgConfigBiz.getHaoduan();
			if(haoduan == null || haoduan.length == 0)
			{
				EmpExecutionContext.error("完美通知 获取运营商号码段为空！");
				return "nohanduan";
			}
			// 获取该SP帐号的路由信息
			List<GtPortUsed> portsList = smsBiz.getPortByUserId(lfMessage.getMessageId());
			if(portsList == null || portsList.size() == 0)
			{
				EmpExecutionContext.error("完美通知获取" + lfMessage.getMessageId() + "帐号的路由信息为空！");
				return "nogtport";
			}
			Map<Integer, String> usedmap = subnoManagerBiz.getPortUserBySpuser(portsList, lfMessage.getMessageId(), lfMessage.getToId());
			if(usedmap == null || usedmap.size() == 0)
			{
				EmpExecutionContext.error("完美通知获取" + lfMessage.getMessageId() + "帐号的全通道号为空！");
				return "nousedmap";
			}
			conditionMap.clear();
			//查询老的记录，所对应的完美通知的记录
			//所选择的记录
			conditionMap.put("pnupId&in", noticeUpIds);
			//老记录所对应的序列ID
			conditionMap.put("taskId", oldtaskid);
			List<LfPerfectNoticUp> oldNoticUpList = empDao.findListBySymbolsCondition(LfPerfectNoticUp.class, conditionMap, null);
			if(oldNoticUpList == null || oldNoticUpList.size() == 0)
			{
				EmpExecutionContext.error("完美通知查询明细记录出现异常，没有查询出记录！");
				return "norecord";
			}
			// 这里装的是新添加进去需要添加到数据库的
			List<LfPerfectNoticUp> perfectNoticUpList = new ArrayList<LfPerfectNoticUp>();
			LfPerfectNoticUp sendNoticeUp = null;
			int effectNum = 0;
			boolean isflag = true;
			String spNumber = " ";
			int spIsuncm = 0;
			for (int j = 0; j < oldNoticUpList.size(); j++)
			{
				LfPerfectNoticUp noticeUp2 = oldNoticUpList.get(j);
				sendNoticeUp = new LfPerfectNoticUp();
				sendNoticeUp.setName(noticeUp2.getName());
				sendNoticeUp.setUserType(noticeUp2.getUserType());
				// 把接收者GUID存进去
				sendNoticeUp.setReceiverId(noticeUp2.getReceiverId());
				//sendNoticeUp.setPnotic(lfPerfectNotic.getNoticId());
				sendNoticeUp.setPnotic(0L);
				sendNoticeUp.setSenderGuid(lfsysuser.getGuId());
				sendNoticeUp.setTaskId(lfPerfectNotic.getTaskId());
				sendNoticeUp.setCreateTime(new Timestamp(System.currentTimeMillis()));
				sendNoticeUp.setMobile(noticeUp2.getMobile());
				sendNoticeUp.setSpUser(lfMessage.getMessageId());
				// 只判断黑名单以及合法 modify by tanglili 2014-04-16 新增国际号码判断
				if(phoneUtil.checkMobile(noticeUp2.getMobile(), haoduan) != 1&&!new PhoneUtil().isAreaCode(noticeUp2.getMobile()))
				{
					badContentSb.append("zh_HK".equals(empLangName)?"Illegal format：":"格式非法"+"：").append(noticeUp2.getMobile()).append(line);
					sendNoticeUp.setIsReceive(3);
					isflag = false;
				}
				else
					if(blBiz.checkBlackList(lfsysuser.getCorpCode(), noticeUp2.getMobile(), lfMessage.getName()))
					{
						// 是否黑名单
						badContentSb.append("zh_HK".equals(empLangName)?"Blacklist number：":"zh_TW".equals(empLangName)?"黑名單號碼":"黑名单号码"+"：").append(noticeUp2.getMobile()).append(line);
						sendNoticeUp.setIsReceive(5);
						isflag = false;
					}
				if(isflag)
				{
					++effectNum;
					// 有效号码数
					sendNoticeUp.setDialogId("1");
					// 可以发送，初始化 状态报告未接收 1是接收 ，2是未接收，3是不可以发送
					sendNoticeUp.setIsAtrred("2");
					sendNoticeUp.setIsReply(1);
					sendNoticeUp.setIsReceive(2);
					// 可以回复的号码 1 不可以回复的号码 2
					sendNoticeUp.setReceiveCount(1);
					effectMoblie.append(noticeUp2.getMobile()).append(line);
				}
				else
					{
						badContentSb.append(noticeUp2.getMobile()).append(line);
						sendNoticeUp.setDialogId("3");
						// 可以发送，初始化 状态报告未接收 1是接收 ，2是未接收，3是不可以发送
						sendNoticeUp.setIsAtrred("3");
						sendNoticeUp.setIsReply(2);
						sendNoticeUp.setReceiveCount(0);
						// 可以回复的号码 1 不可以回复的号码 2
					}
				try
				{
					if("+".equals(noticeUp2.getMobile().substring(0, 1)) || "00".equals(noticeUp2.getMobile().substring(0, 2)))
					{
						spIsuncm = 5;
					}
					else
					{
						spIsuncm = wgMsgConfigBiz.getPhoneSpisuncm(noticeUp2.getMobile());
					}
					// 运营商不符合
					spNumber = usedmap.get(spIsuncm);
					if(spNumber == null || "".equals(spNumber.trim()))
					{
						sendNoticeUp.setIsReceive(8);
					}
				}
				catch (Exception e)
				{
					spNumber = " ";
					EmpExecutionContext.error("完美通知获取该手机号码所对应的通道道出现异常！");
				}
				sendNoticeUp.setSpNumber(spNumber);
				perfectNoticUpList.add(sendNoticeUp);
				isflag = true;
				sendNoticeUp = null;
			}
			//引用上面的变量
			isflag = false;
			if(badContentSb.length() > 0)
			{
				isflag = txtfileutil.writeToTxtFile(url[2], badContentSb.toString());
				if(!isflag)
				{
					EmpExecutionContext.error(" 完美通知 写不合法号码文件失败！");
				}
			}
			// 插入完美通知明细数据
			if(perfectNoticUpList == null || perfectNoticUpList.size() == 0){
				EmpExecutionContext.error("完美通知新增LfPerfectNoticUp集合为空！");
				return "noadd";
			}
			if(empDao.save(perfectNoticUpList, LfPerfectNoticUp.class) == 0)
			{
				EmpExecutionContext.error("重新选择完美通知发送执行LfPerfectNoticUp新增失败 ,taskid："+lfPerfectNotic.getTaskId());
				return "addfail";
			}
			//初始化变量
			isflag = false;
			if(effectNum > 0)
			{
				lfPerfectNotic.setNoticCount(effectNum);
				isflag = txtfileutil.writeToTxtFile(url[0], effectMoblie.toString());
				effectMoblie.setLength(0);
				if(!isflag)
				{
					EmpExecutionContext.error(" 完美通知 写合法号码文件失败！");
					return "numerrer";
				}
			}else{
				return "nophone";
			}
	
		}
		catch (Exception e)
		{
			EmpExecutionContext.error("完美通知重新发送选择人员出现异常！");
			return "errer";
		}
		return "success";
	}

	
	/**
	 *   
	 * @description     新增完美通知号码文件与taskid对应
	 * @param perFileInfo	完美通知号码信息对象
	 * @return    success成功       	fail 失败    	errer 出现异常 	 
	 * @author yejiangmin <282905282@qq.com>
	 * @datetime 2013-10-29 下午02:52:51
	 */
	public String addPerFileInfo(LfPerFileInfo perFileInfo){
		try{
			if(empDao.save(perFileInfo)){
				return "success";
			}
		}catch (Exception e) {
			EmpExecutionContext.error(e,"完美通知新增LfPerFileInfo失败！");
			return "errer";
		}
		return "fail";
	}
	
	/**
	 *    
	 * @description   更新完美通知主表   
	 * @param conditionMap	条件map 
	 * @param objectMap	更新map 
	 * @param taskid	任务ID
	 * @param sendcount	发送次数
	 * @return       			 
	 * @author yejiangmin <282905282@qq.com>
	 * @datetime 2013-10-29 下午05:06:18
	 */
	public boolean updateLfPerfectNotic(LinkedHashMap<String,String> conditionMap,LinkedHashMap<String,String> objectMap,Long taskid,int sendcount){
		try{
			conditionMap.clear();
			objectMap.clear();
			conditionMap.put("taskId", String.valueOf(taskid));
			objectMap.put("arySendCount", String.valueOf(sendcount));
			return(empDao.update(LfPerfectNotic.class, objectMap, conditionMap));
		}catch (Exception e) {
			EmpExecutionContext.error(e,"完美通知更新LfPerfectNotic出现异常！");
			return false;
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
