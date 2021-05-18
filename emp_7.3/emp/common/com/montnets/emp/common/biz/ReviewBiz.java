package com.montnets.emp.common.biz;

import java.sql.Connection;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

import com.montnets.emp.common.constant.EMPException;
import com.montnets.emp.common.constant.IErrorCode;
import com.montnets.emp.common.constant.SystemGlobals;
import com.montnets.emp.common.context.EmpExecutionContext;
import com.montnets.emp.common.dao.DepSpecialDAO;
import com.montnets.emp.common.vo.ReviewFlowVo;
import com.montnets.emp.entity.approveflow.LfFlowRecord;
import com.montnets.emp.entity.sysuser.LfDep;
import com.montnets.emp.entity.sysuser.LfFlow;
import com.montnets.emp.entity.sysuser.LfFlowBindObj;
import com.montnets.emp.entity.sysuser.LfReviewSwitch;
import com.montnets.emp.entity.sysuser.LfReviewer2level;
import com.montnets.emp.entity.sysuser.LfSysuser;

/**
 * 审批相关的通用方法
 * @author zm
 *
 */
public class ReviewBiz extends SuperBiz{
	
	
	/**
	 * 通过用户ID查询审批信息
	 * @param userId	操作员用户ID
	 * @return
	 * @throws Exception
	 */
	public LfFlow getFlowBySysUserId(Long userId) throws Exception
	{
		LfFlow flow = null;
		//设置条件MAP
		LinkedHashMap<String, String> conditionMap = new LinkedHashMap<String, String>();

		String username = "";
		//LfSysuserVo lso = lfSysuserVoDAO
		//		.findLfSysuserVoByID(userId.toString());
		//获取操作员对象
		LfSysuser lso = empDao.findObjectByID(LfSysuser.class, userId);
		if (lso != null)
		{
			username = lso.getUserName();
		}
		try
		{
			//设值条件
			conditionMap.put("userId", username);
			conditionMap.put("corpCode", lso == null ? null : lso.getCorpCode());
			conditionMap.put("FType", "1");
			//查询
			List<LfFlow> flowsList = empDao.findListByCondition(LfFlow.class,
					conditionMap, null);
			//获取记录
			if (flowsList != null && flowsList.size() == 1)
			{
				flow = flowsList.get(0);
			}
		} catch (Exception e)
		{
			EmpExecutionContext.error(e, "查询审批信息异常，userId："+ userId);
			//异常
			throw e;
		}
		//返回
		return flow;
	}
	

	/**
	 * 添加审批流信息
	 * @param conn
	 * @param mtId
	 * @param reviewType
	 * @param flow
	 * @return
	 * @throws Exception
	 */
	public boolean addFlowRecords(Connection conn, Long mtId,
			Integer reviewType, LfFlow flow) throws Exception
	{
		//判断是否开启审批提醒功能
		String isre=SystemGlobals.getSysParam("isRemind");
		boolean result = false;
		try
		{
			//获取当前用户审批流程对应的所有审批流层级
			List<LfReviewer2level> r2lslist = this.getR2LByFidOrderByLevel(flow
					.getFId());
			StringBuffer reViewLevel = new StringBuffer("");
			LfFlowRecord record = null;
			LfSysuser user = null;
			//循环所有审批流层级
			if (r2lslist != null && r2lslist.size() > 0)
			{
				for (int i = 0; i < r2lslist.size(); i++)
				{
					//设置当前任务的审批流信息
					record = new LfFlowRecord();
					record.setMtId(mtId);   //任务id
					record.setFId(flow.getFId());  //对应审批流
					record.setReviewer(r2lslist.get(i).getUserId());  //审批用户
					record.setRLevel(r2lslist.get(i).getRLevel());		 //审批级次			
					record.setRTime(null);
					record.setRLevelAmount(flow.getRLevelAmount()); //审批总级次
					int mid = -1;
					record.setRState(mid);  //默认未审批
					//如果审批流的审批层次大于一级，即有多级人审批
					if (r2lslist.get(i).getRLevel() > 1)
					{
					    //设置上一级审批人信息
						record.setPreRv(r2lslist.get(i - 1).getUserId());
					}
					//如果开启了审批提醒功能
					if("0".equals(isre))
					{
						record.setBatchNumber(GlobalVariableBiz.getInstance().getNewNodeCode());
						record.setDisagreeNumber(GlobalVariableBiz.getInstance().getNewNodeCode());
					}
					//获取当前审批级次的用户信息
					user = empDao.findObjectByID(LfSysuser.class, record
							.getReviewer());
					reViewLevel.append(user.getName());
					record.setRContent(reViewLevel.toString());
					reViewLevel.append(",");
				
					record.setReviewType(reviewType);
					empTransDao.save(conn, record);
				}
			}
			result = true;
		} catch (Exception e)
		{
			EmpExecutionContext.error(e ,"添加审批流信息异常!");
			throw e;
		}
		return result;
	}
	
	/**
	 * 获取某个审批流程的所有审批级次集合
	 * @param fId
	 * @return
	 * @throws Exception
	 */
	private List<LfReviewer2level> getR2LByFidOrderByLevel(Long fId)
			throws Exception
	{
		if (fId == null)
		{
			return null;
		}
		List<LfReviewer2level> re2LsList;
		LinkedHashMap<String, String> conditionMap = new LinkedHashMap<String, String>();
		LinkedHashMap<String, String> orderbyMap = new LinkedHashMap<String, String>();
		try
		{
			//加载过滤条件 
			conditionMap.put("FId", fId.toString());
			orderbyMap.put("RLevel", "asc");
			//根据过滤条件查询集合信息
			re2LsList = empDao.findListByCondition(LfReviewer2level.class,
					conditionMap, orderbyMap);
		} catch (Exception e)
		{
			EmpExecutionContext.error(e ,"获取审批流程的所有审批级次集合异常!");
			throw e;
		}
		return re2LsList;
	}
	
	/**
	 * 短信及彩信发送调用的实例化下一级审核方法
	 * @param conn
	 * @param mtId
	 * @param title
	 * @param submitTime
	 * @param infoType
	 * @param flow
	 * @param userId
	 * @param level
	 * @return
	 * @throws Exception
	 */
	public boolean addFlowRecords(Connection conn, Long mtId,String title ,Timestamp submitTime,
			Integer infoType, LfFlow flow,Long userId,String level) throws Exception
	{
		return  addFlowRecordChild( conn,  mtId, title , submitTime,
				 infoType,  flow.getFId(),flow.getRLevelAmount(), userId, level, null, null);
	}

	/**
	 * 实例化下一级的方法
	 * @param conn 事务外获取的连接
	 * @param record 上一级的审批记录
	 * @return 执行结果
	 * @throws Exception
	 */
	public boolean addFlowRecordsForNext(Connection conn, LfFlowRecord record) throws Exception
	{
		return  addFlowRecordChild( conn,  record.getMtId(), record.getRContent() , record.getSubmitTime(),
				 record.getInfoType(),  record.getFId(),record.getRLevelAmount(), 
				 record.getProUserCode(), String.valueOf(record.getRLevel() + 1), record.getReviewType(),record.getPreRv());
	}
	
	/**
	 * 实例化审批流程
	 * @param conn 事务外获取的链接
	 * @param mtId 任务Id
	 * @param title 短彩主题或模板名称
	 * @param submitTime 模板创建时间或任务提交时间
	 * @param infoType 类型：1-sms,2-mms,3-smstemp,4-mmstemp
	 * @param fId 审批流程Id
	 * @param levelAmount 总级别
	 * @param userId 发起人操作员ID
	 * @param level 需实例化的级别
	 * @param dsFlag 模板类型
	 * @return 执行结果，失败返回false
	 * @throws Exception
	 */
	public boolean addFlowRecordChild(Connection conn, Long mtId,String title ,Timestamp submitTime,
			Integer infoType, Long fId,int levelAmount,Long userId,String level,Integer dsFlag,Long preRev) 
	{

		boolean result = false;
		//获取是否开启审批提醒信息
		String isre=SystemGlobals.getSysParam("isRemind");
		try
		{
			//是否拥有下一级审核人
			boolean isHasNext = false;
			
			List<LfReviewer2level> r2lslist = this.getR2LByFidOrderByLevel(fId ,null);
			
			//短信审批流程
			LfFlowRecord record = new LfFlowRecord();
			//短信mtId
			record.setMtId(mtId);
			//主题
			record.setRContent(title);
			//提交时间
			record.setSubmitTime(submitTime);
			//是否完成--2:未完成
			record.setIsComplete(2);
			//任务提交人
			record.setProUserCode(userId);
			//流程id
			record.setFId(fId);
			//审批级别
			record.setRLevel(Integer.valueOf(level));
			//record.setRTime(new Timestamp(System.currentTimeMillis()));
			//审批时间
			record.setRTime(null);
			//审批总级数
			record.setRLevelAmount(levelAmount);
			//信息类型
			record.setInfoType(infoType);
			//审批状态
			record.setRState(-1);
			//模板类型
			record.setReviewType(dsFlag);
			record.setReviewer(0l);
			//获取一级审核
			LfReviewer2level r2l0 = r2lslist.get(0);
			int chooseLevel = Integer.parseInt(level);
			boolean isgoon = true;
			if(r2l0.getRType() == 5 && chooseLevel < 3 && ( preRev == null || 0l != preRev))
			{
				chooseLevel = 1;
				String depPath = new DepSpecialDAO().getDepPathByUserId(userId);
				if("".equals(depPath))
				{
					return false;
				}
				String[] depIdArray = depPath.split("/");
				//设置总级别
				int levelC = preRev==null?depIdArray.length-1:preRev.intValue()-1;
				//如果当前级别小于逐级审核的级别
				if(levelC >= 0 )
				{
					//1全部通过生效;2第一人审核生效
					record.setRCondition(r2l0.getRCondition());
					//审核人员类型
					record.setRType(r2l0.getRType());
					//是否开启审批提醒
					if("0".equals(isre))
					{
						//同意指令
						record.setBatchNumber(GlobalVariableBiz.getInstance().getNewNodeCode());
						//不同意指令
						record.setDisagreeNumber(GlobalVariableBiz.getInstance().getNewNodeCode());
					}
					int depIndex = levelC;
					while(depIndex >= 0)
					{
						String depId = depIdArray[depIndex];
						//查找具有审核权限的机构人员
						Long[] useridArray = new DepSpecialDAO().getUserOfDepReviwer2(depId,userId.toString());
						if(useridArray == null || useridArray.length == 0)
						{
							//如果当前层级找不到，则再继续往上一级查找人员
							depIndex --;
							if(depIndex < 0)
							{
								isgoon = true;
								chooseLevel = 2;
							}
						}else
						{
							record.setRLevel(1);
							record.setPreRv(Long.valueOf(depIndex));
							for(int j=0;j<useridArray.length;j++)
							{
								record.setUserCode(useridArray[j]);
								boolean saveResult = empTransDao.save(conn, record);
								if(saveResult)
								{
									isHasNext = true;
								}
							}
							isgoon = false;
							break;
						}
					}
				}else
				{
					chooseLevel = levelC + 2 - depIdArray.length;
				}
			}
			if (isgoon &&  r2lslist != null && r2lslist.size() > 0)
			{
				record.setRLevel(chooseLevel);
				record.setPreRv(0l);
				for (int i = 0; i < r2lslist.size(); i++)
				{
					LfReviewer2level lfr2l = r2lslist.get(i);
					if(lfr2l.getRLevel() != chooseLevel)
					{
						continue;
					}
					//1全部通过生效;2第一人审核生效
					record.setRCondition(lfr2l.getRCondition());
					//审核人员类型
					record.setRType(lfr2l.getRType());
					//是否开启审批提醒
					if("0".equals(isre))
					{
						//同意指令
						record.setBatchNumber(GlobalVariableBiz.getInstance().getNewNodeCode());
						//不同意指令
						record.setDisagreeNumber(GlobalVariableBiz.getInstance().getNewNodeCode());
						
					}
					
					Long userCode = 0L;
					//审批对象为操作员时
					if(lfr2l.getRType() == 1)
					{
						userCode = lfr2l.getUserId();
						record.setUserCode(userCode);
						boolean saveResult = empTransDao.save(conn, record);
						if(saveResult)
						{
							isHasNext = true;
						}
					}
					//审批对象为机构时
					else if(lfr2l.getRType() == 4)
					{
						//查找具有审核权限的机构人员
						Long[] useridArray = new DepSpecialDAO().getUserOfDepReviwer2(lfr2l.getUserId().toString(),userId.toString());
						for(int j=0;useridArray != null && j<useridArray.length;j++)
						{
							record.setUserCode(useridArray[j]);
							boolean saveResult = empTransDao.save(conn, record);
							if(saveResult)
							{
								isHasNext = true;
							}
						}
					}
					
				}
			}
			//判断是否有下一级，否则返回false
			if(isHasNext)
			{
				//返回true
				result = true;
			}else
			{
				EmpExecutionContext.error("无法找到审核流对象。");
				result = false;
			}
		} catch (Exception e)
		{
			//抛异常
			EmpExecutionContext.error(e, "实例化审核流失败。");
			return false;
		}
		//返回结果
		return result;
	}
	
	/**
	 * 判断逐级审核是否最后一级
	 * @param levelC
	 * @param userId
	 * @return
	 */
	public boolean checkZhujiIsOver(int levelC,Long userId)
	{
		if(levelC == 0)
		{
			return true;
		}
		String depPath = new DepSpecialDAO().getDepPathByUserId(userId);
		if("".equals(depPath))
		{
			return true;
		}
		String[] depIdArray = depPath.split("/");
		int depIndex = levelC - 1;
		while(depIndex >= 0)
		{
			String depId = depIdArray[depIndex];
			//查找具有审核权限的机构人员
			Long[] useridArray = new DepSpecialDAO().getUserOfDepReviwer2(depId,userId.toString());
			if(useridArray == null || useridArray.length == 0)
			{
				//如果当前层级找不到，则再继续往上一级查找人员
				depIndex --;
			}else
			{
				return false;
			}
		}
		return true;
	}

	/**
	 * 根据流程id获取操作员的审核流
	 * @param fId
	 * @return
	 * @throws Exception
	 */
	private List<LfReviewer2level> getR2LByFidOrderByLevel(Long fId,String level)
			throws Exception
	{
		if (fId == null)
		{
			return null;
		}
		List<LfReviewer2level> re2LsList;
		LinkedHashMap<String, String> conditionMap = new LinkedHashMap<String, String>();
		LinkedHashMap<String, String> orderbyMap = new LinkedHashMap<String, String>();
		try
		{
			//流程id
			conditionMap.put("FId", fId.toString());
			if(level != null)
			{
				conditionMap.put("RLevel", level);
			}
			//排序
			orderbyMap.put("RLevel", "asc");
			re2LsList = empDao.findListByCondition(LfReviewer2level.class,
					conditionMap, orderbyMap);
		} catch (Exception e)
		{
			EmpExecutionContext.error(e, "获取操作员审核流异常。流程编号:" + fId);
			//异常处理
			throw e;
		}
		return re2LsList;
	}	
	
	/**
	 * 检查操作是否绑定了审核流程
	 * @param userId 操作员ID
	 * @param corpCode 企业编码
	 * @param infoType 信息类型
	 * return 返回null则为无需审核，需审核时返回审核流对象
	 * @throws EMPException B20008:必审操作员未配置审核流
	 */
	public LfFlow checkUserFlow(Long userId,String corpCode,String infoType) throws EMPException
	{
		return this.checkUserFlow(userId, corpCode, infoType, 0l);
	}
	/**
	 * 检查操作是否绑定了审核流程
	 * @param userId 操作员ID
	 * @param corpCode 企业编码
	 * @param infoType 信息类型
	 * @param count 发送条数
	 * @return 返回null则为无需审核，需审核时返回审核流对象
	 * @throws EMPException B20008:必审操作员未配置审核流
	 */
	public LfFlow checkUserFlow(Long userId,String corpCode,String infoType,Long count) throws EMPException
	{
		LinkedHashMap<String, String> conditionMap = new LinkedHashMap<String, String>();
		conditionMap.put("corpCode", corpCode);
		conditionMap.put("infoType", infoType);
		try {
			LfSysuser sysuser = empDao.findObjectByID(LfSysuser.class, userId);
			//如果返回的是操作员为无需配置审核，则返回空
			if(sysuser.getIsAudited() != 1)
			{
				return null;
			}
			List<LfReviewSwitch> switchList = empDao.findListByCondition(LfReviewSwitch.class, conditionMap, null);
			if(switchList != null && switchList.size() >0)
			{
				LfReviewSwitch rswitch = switchList.get(0);
				//如果审核开关为关，则返回空
				if(rswitch.getSwitchType()==2)
				{
					return null;
				}
				Integer msgCount = rswitch.getMsgCount();
				if(count != null && count - 0 > 0 && msgCount != null && msgCount > 0)
				{
					//如果阀值大于条数，则无需审核
					if(msgCount - count > 0)
					{
						return null;
					}
				}
				//查找绑定了操作员的审核流对象
				conditionMap.clear();
				conditionMap.put("ObjCode", userId.toString());
				conditionMap.put("ObjType", "1");
				conditionMap.put("infoType", infoType);
				
				LfFlow flow = null;
				
				List<LfFlowBindObj> flowObjList = empDao.findListByCondition(LfFlowBindObj.class, conditionMap, null) ;
				if(flowObjList != null && flowObjList.size() > 0)
				{
					flow=empDao.findObjectByID(LfFlow.class, flowObjList.get(0).getFId());
				}else
				{
					//查找绑定了机构操作员机构的审核流对象
					String depPath = new DepSpecialDAO().getDepPathByUserId(userId);
					if("".equals(depPath))
					{
						return null;
					}else
					{
						String depIds = depPath.replace("/", ",").substring(0,depPath.length() - 1);
						conditionMap.clear();
						conditionMap.put("ObjCode&in", depIds);
						conditionMap.put("ObjType", "2");
						conditionMap.put("infoType", infoType);
						
						List<LfFlowBindObj> depObjList = empDao.findListBySymbolsCondition(LfFlowBindObj.class, conditionMap, null) ;
						if(depObjList != null && depObjList.size() > 0)
						{
							String[] depIdArray = depPath.split("/");
                            int len = depIdArray.length;
                            //操作员所在机构id
                            String userDepId = depIdArray[len-1];
							//逆序比较机构id对应的审核对象，找到审核流对象则返回
							for(int i = len-1; i>-1 && flow == null;i --)
							{
								//获取机构编码，即最低级别的机构
								String depId = depIdArray[i];
								if("".equals(depId))
								{
									continue;
								}
								for(LfFlowBindObj bindObj : depObjList)
								{
									//查找操作员所在机构的审核流 或者 上层机构（包含子机构）的审核流
                                    if(depId.equals(bindObj.getObjCode()) && (depId.equals(userDepId) || bindObj.getCtsubDep() - 1 == 0))
									{
										//只要找到一个机构的审核流则跳出循环 
										flow=empDao.findObjectByID(LfFlow.class, bindObj.getFId());
										break;
									}
								}
							}
						}
					}
				}
				if(flow!=null)
				{
					//审批流被禁用就抛异常
					if(flow.getFlowState()-2==0)
					{
						EmpExecutionContext.info("审核流已被禁用，创建审批任务失败，错误编码："+IErrorCode.B20017+"，corpCode:"+corpCode+"，userId:"+userId);
						throw new EMPException(IErrorCode.B20017);
					}
					else
					{
						return flow;
					}
				}
				else
				{
					EmpExecutionContext.info("必审操作员未配置审核流，创建审批任务失败，错误编码："+IErrorCode.B20008+"，corpCode:"+corpCode+"，userId:"+userId);
					throw new EMPException(IErrorCode.B20008);
				}
			}
		}catch(EMPException empex){
			//不是异常，不需要记录日志，在抛出前已记录INFO日志
			throw empex;
		} catch (Exception e) {
			EmpExecutionContext.error(e, "检查操作员审核流失败。");
			throw new EMPException(IErrorCode.B20019,e);
		}
		
		return null;
	}
	
	
	/**
	 *   审核详情中查询下一级审批对象
	 * @param lastrecord	审核实时记录
	 * @param corpCode	企业编码
	 * @return	审批对象名称   审批对象类型
	 */
	public String[] getNextFlowRecord(LfFlowRecord lastrecord,String corpCode){
		 String[] strarr = new String[2];
		try{
			LinkedHashMap<String,String> conditionMap = new LinkedHashMap<String, String>();
			conditionMap.put("RLevel",String.valueOf(lastrecord.getRLevel()+1));
			conditionMap.put("FId",String.valueOf(lastrecord.getFId()));
			BaseBiz baseBiz = new BaseBiz();
			List<LfReviewer2level> reviewerList = baseBiz.getByCondition(LfReviewer2level.class, conditionMap, null);
			String levelbuffer = "";
			Integer levelType = null;
			Integer rcondition = null;
			StringBuffer sb = new StringBuffer();
			if(reviewerList!= null && reviewerList.size()>0){
				for(LfReviewer2level level:reviewerList){
					levelbuffer = levelbuffer + level.getUserId() + ",";
					if(levelType == null){
						levelType = level.getRType();
						rcondition = level.getRCondition();
					}
				}
			}
			if(levelbuffer != null && levelbuffer.length()>0){
				levelbuffer = levelbuffer.substring(0, levelbuffer.length()-1);
				//操作员
				if(levelType != null && levelType == 1){
					  conditionMap.clear();
					  conditionMap.put("userId&in",levelbuffer);
					  conditionMap.put("corpCode",corpCode);
					  List<LfSysuser> lfsysuserList = baseBiz.getByCondition(LfSysuser.class, conditionMap, null);
					  if(lfsysuserList != null && lfsysuserList.size()>0){
						  for(int m=0;m<lfsysuserList.size();m++){
							  LfSysuser temp = lfsysuserList.get(m);
							  sb.append(temp.getName()).append("&nbsp;&nbsp;");
						  }
					  }
				//机构
				}else if(levelType != null && levelType == 4){
					  conditionMap.clear();
					  conditionMap.put("depId&in",levelbuffer);
					  conditionMap.put("corpCode",corpCode);
					  List<LfDep> lfdepList = baseBiz.getByCondition(LfDep.class, conditionMap, null);
					  if(lfdepList != null && lfdepList.size()>0){
						  for(int m=0;m<lfdepList.size();m++){
							  LfDep dep = lfdepList.get(m);
							  sb.append(dep.getDepName()).append("&nbsp;&nbsp;");
						  }
					  }
				}
			}
			strarr[0] = sb.toString();
			strarr[1] = rcondition + "";
		}catch (Exception e) {
			strarr[0] = "";
			strarr[1] = "";
			EmpExecutionContext.error(e, "审核详情中查询下一级审批对象异常。");
		}
		return strarr;
	}
	
	
	/**
	 * 当短信任务彩信任务撤消的时候 处理审核流程实时记录的状态
	 * @param mtid	任务ID
	 * @return	更新成功失败
	 */
	public boolean updateFlowRecordByMtId(String mtid){
		boolean isFlag = false;
		try{
			LinkedHashMap<String, String> conditionMap = new LinkedHashMap<String, String>();
			LinkedHashMap<String, String> objectMap = new LinkedHashMap<String, String>();
			conditionMap.put("mtId",mtid);
			conditionMap.put("isComplete","2");
			objectMap.put("RState","4");
			objectMap.put("isComplete","1");
			isFlag =  empDao.update(LfFlowRecord.class, objectMap, conditionMap);
		}catch (Exception e) {
			isFlag = false;
			EmpExecutionContext.error(e, "当短信任务彩信任务撤消的时候 处理审核流程实时记录的状态异常。");
		}
		return isFlag;
	}
	
	
	
	
	/**
	 * 查询当前审核级别下一级别的所有审批人员
	 * @param currLevel 当前审核级别，传入当前待审批的级别
	 * @param rLevelAmount 审核的总级别
	 * @param fId 审核流程ID
	 * @param corpCode 企业编码
	 * @param userId 流程发起人的ID
	 * @return  返回当前审核级别下一级别的所有审批人员
	 */
	public List<ReviewFlowVo> getNextFlowRecord(int currLevel,int rLevelAmount,String fId,String corpCode,Long userId){
		List<ReviewFlowVo> reviewFlowVos=null;
		try{
			//根据审核流程ID和审核级别，查找审核的流程模板
			LinkedHashMap<String,String> conditionMap = new LinkedHashMap<String, String>();
			//审核级别
			conditionMap.put("RLevel",String.valueOf(currLevel+1));
			//审核流程ID
			conditionMap.put("FId",fId);
			BaseBiz baseBiz = new BaseBiz();
			//调用通用方法查询审核的流程模板
			List<LfReviewer2level> reviewerList = baseBiz.getByCondition(LfReviewer2level.class, conditionMap, null);
			//审核的ID字符串，操作员ID或者机构ID字符串
			String levelbuffer = "";
			//审核类型，1-操作员，4-机构审核,5-逐级审核  这里不会有逐级审核。逐级审核不调用这个方法
			Integer levelType = null;
			//1全部通过生效;2第一人审核生效
			Integer rcondition = null;
			StringBuffer sb = new StringBuffer();
			//循环将审核的ID字符串组装。如果是操作员类型，就是操作员ID。如果是机构类型，就是机构ID。
			if(reviewerList!= null && reviewerList.size()>0){
				for(LfReviewer2level level:reviewerList){
					levelbuffer = levelbuffer + level.getUserId() + ",";
					if(levelType == null){
						//审核类型
						levelType = level.getRType();
						rcondition = level.getRCondition();
					}
				}
			}
			//审核的ID字符串必须有值，才循环。
			if(levelbuffer != null && levelbuffer.length()>0){
				levelbuffer = levelbuffer.substring(0, levelbuffer.length()-1);
				//levelType为1，操作员审核类型 
				if(levelType != null && levelType == 1){
					  //根据操作员ID和企业编码查询审批人员
					  conditionMap.clear();
					  conditionMap.put("userId&in",levelbuffer);
					  conditionMap.put("corpCode",corpCode);
					  List<LfSysuser> lfsysuserList = baseBiz.getByCondition(LfSysuser.class, conditionMap, null);
					  //将查询到的审批人员组装成审批人员Vo的list
					  if(lfsysuserList != null && lfsysuserList.size()>0){
						  reviewFlowVos=new ArrayList<ReviewFlowVo>();
						  ReviewFlowVo reviewFlowVo=null;
						  for(int m=0;m<lfsysuserList.size();m++){
							  LfSysuser temp = lfsysuserList.get(m);
							  reviewFlowVo=new ReviewFlowVo();
							  //审核级别
							  reviewFlowVo.setrLevel(currLevel+1);
							  //审核的总级别
							  reviewFlowVo.setrLevelAmount(rLevelAmount);
							  //从流程模板查找的肯定是未审批
							  reviewFlowVo.setReviewState("未审批");
							  //审核时间
							  reviewFlowVo.setReviewTime("-");
							  //审核人员姓名
							  reviewFlowVo.setReviewerName(temp.getName());
							  reviewFlowVo.setReviewComments("-");
							  reviewFlowVos.add(reviewFlowVo);
						  }
					  }
				//levelType为4，机构审核类型 
				}else if(levelType != null && levelType == 4){
					  //根据机构ID和企业编码查找机构的集合
					  conditionMap.clear();
					  conditionMap.put("depId&in",levelbuffer);
					  conditionMap.put("corpCode",corpCode);
					  List<LfDep> lfdepList = baseBiz.getByCondition(LfDep.class, conditionMap, null);
					  //如果机构的集合不为空
					  if(lfdepList != null && lfdepList.size()>0){
						  //循环机构的集合
						  for(int m=0;m<lfdepList.size();m++){
							  LfDep dep = lfdepList.get(m);
							  //根据机构的ID查找机构的审核人员，返回机构审核人员的userid
							  Long[] useridArray= new DepSpecialDAO().getUserOfDepReviwer2(String.valueOf(dep.getDepId()),userId.toString());
							  StringBuffer userIdBuffer=new StringBuffer();
							  if(useridArray!=null){
								  //组装成操作员ID字符串
								  for (int i = 0; i < useridArray.length; i++)
								  {
									  userIdBuffer.append(useridArray[i]).append(",");
								  }
								  userIdBuffer.deleteCharAt(userIdBuffer.lastIndexOf(","));
								  //根据机构审核人员的ID，查找机构审核人员的信息
								  conditionMap.clear();
								  conditionMap.put("userId&in",userIdBuffer.toString());
								  conditionMap.put("corpCode",corpCode);
								  List<LfSysuser> lfsysuserList = baseBiz.getByCondition(LfSysuser.class, conditionMap, null);
								  if(lfsysuserList != null && lfsysuserList.size()>0){
									  if(reviewFlowVos==null){
										  reviewFlowVos=new ArrayList<ReviewFlowVo>();
									  }
									  ReviewFlowVo reviewFlowVo=null;
									  for(int i=0;i<lfsysuserList.size();i++){
										  LfSysuser temp = lfsysuserList.get(i);
										  reviewFlowVo=new ReviewFlowVo();
										  //审核级别
										  reviewFlowVo.setrLevel(currLevel+1);
										  //审核的总级别
										  reviewFlowVo.setrLevelAmount(rLevelAmount);
										  //从流程模板查找的肯定是未审批
										  reviewFlowVo.setReviewState("未审批");
										  //审核时间
										  reviewFlowVo.setReviewTime("-");
										  //审核人员姓名
										  reviewFlowVo.setReviewerName(temp.getName());
										  reviewFlowVo.setReviewComments("-");
										  reviewFlowVos.add(reviewFlowVo);
									  }
								  }
							  }
						  }
					  }
				}
			}
		}catch (Exception e) {
			EmpExecutionContext.error(e, "查询当前审核级别下一级别的所有审批人员异常。");
		}
		//返回集合
		return reviewFlowVos;
	}
	/**
	 * 查询逐级审批的审批人员(如果逐级审批已经开始，就查询后续的审核人员。如果逐级审批未开始，就查询逐级审批的所有审批人员)
	 * @param level level为null,查找逐级审批所有人员；level不为null，逐级审批已经开始
	 * @param userId 发起人的 userid
	 * @param corpCode 发起人的企业编码
	 * @param rLevelAmount  审核的总级别
	 * @return 返回逐级审批的所有审核人
	 */
	public List<ReviewFlowVo> getZJAllFlowRecord(Integer level,Long userId,String corpCode,int rLevelAmount){
		//返回的Vo集合，里面包含审批人信息
		List<ReviewFlowVo> reviewFlowVos=null;
		try{
			BaseBiz baseBiz = new BaseBiz();
			//获取任务发起人的DEPPATH
			String depPath = new DepSpecialDAO().getDepPathByUserId(userId);
			//DEPPATH为空，则返回null
			if("".equals(depPath))
			{
				return null;
			}
			//将DEPPATH生成数组，初始化depIndex。
			String[] depIdArray = depPath.split("/");
			int depIndex=-1;
			//level为null，逐级审批未开始。会查找逐级审批的所有审批人员。
			//level不为null，逐级审批已经开始，level为审批到的级别。
			if(level==null){
				depIndex=depIdArray.length-1;
			}else{
				depIndex = level - 1;
			}
			//循环获取各级机构的审批人员
				while(depIndex >= 0)
				{
					String depId = depIdArray[depIndex];
					//查找具有审核权限的机构人员，返回审批人员ID的数组
					Long[] useridArray = new DepSpecialDAO().getUserOfDepReviwer2(depId,userId.toString());
					if(useridArray == null || useridArray.length == 0)
					{
						//如果当前层级找不到，则再继续往上一级查找人员
						depIndex --;
					}else
					{
						  //将审批人员ID数组组装成以逗号隔开的字符串。
						  StringBuffer userIdBuffer=new StringBuffer();
						  for (int i = 0; i < useridArray.length; i++)
						  {
							  userIdBuffer.append(useridArray[i]).append(",");
						  }
						  //根据审批人员ID和企业编码查找审批人员信息
						  LinkedHashMap<String,String> conditionMap = new LinkedHashMap<String, String>();
						  userIdBuffer.deleteCharAt(userIdBuffer.lastIndexOf(","));
						  conditionMap.clear();
						  conditionMap.put("userId&in",userIdBuffer.toString());
						  conditionMap.put("corpCode",corpCode);
						  List<LfSysuser> lfsysuserList = baseBiz.getByCondition(LfSysuser.class, conditionMap, null);
						  if(lfsysuserList != null && lfsysuserList.size()>0){
							  //如果要返回的集合为空，则初始化集合
							  if(reviewFlowVos==null){
								  reviewFlowVos=new ArrayList<ReviewFlowVo>();
							  }
							  ReviewFlowVo reviewFlowVo=null;
							  //将当前机构的审核人员添加到要返回的集合list中。
							  for(int i=0;i<lfsysuserList.size();i++){
								  LfSysuser temp = lfsysuserList.get(i);
								  reviewFlowVo=new ReviewFlowVo();
								  //逐级审批的审核级别为1级
								  reviewFlowVo.setrLevel(1);
								  //审核的总级别
								  reviewFlowVo.setrLevelAmount(rLevelAmount);
								  //审批状态都是未审批
								  reviewFlowVo.setReviewState("未审批");
								  //审核时间
								  reviewFlowVo.setReviewTime("-");
								  //审批人员姓名
								  reviewFlowVo.setReviewerName(temp.getName());
								  //审批意见
								  reviewFlowVo.setReviewComments("-");
								  reviewFlowVos.add(reviewFlowVo);
							  }
						  }
						  //索引递减，找上一级机构
						  depIndex --;
					}
				}
		}catch (Exception e) {
			EmpExecutionContext.error(e, "查询逐级审批的审批人员失败！");
		}
		//返回集合
		return reviewFlowVos;
	}
	
	
}
