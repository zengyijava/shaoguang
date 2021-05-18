package com.montnets.emp.mmstask.biz;

import java.sql.Connection;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;

import com.montnets.emp.common.biz.BalanceLogBiz;
import com.montnets.emp.common.biz.MttaskBiz;
import com.montnets.emp.common.biz.ReviewBiz;
import com.montnets.emp.common.biz.ReviewRemindBiz;
import com.montnets.emp.common.biz.SendMmsTimerTask;
import com.montnets.emp.common.biz.SuperBiz;
import com.montnets.emp.common.constant.SystemGlobals;
import com.montnets.emp.common.context.EmpExecutionContext;
import com.montnets.emp.common.timer.TaskManagerBiz;
import com.montnets.emp.common.vo.LfFlowRecordVo;
import com.montnets.emp.common.vo.LfMttaskVo2;
import com.montnets.emp.entity.approveflow.LfFlowRecord;
import com.montnets.emp.entity.sms.LfMttask;
import com.montnets.emp.entity.system.LfTimer;
import com.montnets.emp.entity.sysuser.LfFlow;
import com.montnets.emp.entity.sysuser.LfSysuser;
import com.montnets.emp.mmstask.dao.MmsTaskDao;
import com.montnets.emp.util.PageInfo;

/**
 *   彩信任务BIZ
 * @author Administrator
 *
 */
public class MmsTaskBiz extends SuperBiz{
	
	
	
	private String getMmsFeeMsg(Integer index){
		String msg = "彩信扣费失败";
		try{
			if(index != null){
				if(index == -2){
					msg = "彩信余额不足";
				}else if(index == -4){
					msg = "用户所属机构没有充值";
				}else if(index == -5){
					msg = "彩信发送条数不能为空";
				}else if(index == -9999){
					msg = "彩信发送扣费回收接口调用异常";
				}
			}
		}catch (Exception e) {
			EmpExecutionContext.error(e, "彩信扣费返回值对应信息错误！");
		}
		return msg;
	}
	
	
	/**
	 *   彩信发送
	 * @param mt
	 * @return
	 * @throws Exception
	 *  返回值信息 			
	 * 		    1:彩信回收成功
	 * 			0:彩信扣费成功
	 * 		   -1:彩信扣费失败
	 * 		   -2:彩信余额不足
	 *  	   -4:用户所属机构没有充值
	 *  	   -5:彩信发送或回收条数不能为空
	 *  	-9999:彩信发送扣费回收接口调用异常
	 *  	   -8:彩信回收已发送条数异常
	 * 		   -9:彩信回收失败
	 * 		
	 */
	 
    public String addMmsLfMttask(LfMttask mt) throws Exception
	{
		String returnStr = "";
		//这里在新建的时候注释掉mtid 
		//Long mtId = null;
		// 创建中
		if (mt.getSubState() == 1) 
		{
			//保存任务
			empDao.saveObjectReturnID(mt);
			returnStr = "saveSuccess";
		} else
		// 提交
		{
			ReviewBiz reviewBiz = new ReviewBiz();
			BalanceLogBiz balanceLogBiz = new BalanceLogBiz();
			String isre=SystemGlobals.getSysParam("isRemind");
			//获取审批信息
			LfFlow flow = reviewBiz.checkUserFlow(mt.getUserId(), mt.getCorpCode(), mt.getMsType().toString(),mt.getEffCount());
			//假如需要审批
			if (flow != null)
			{
				//做事务
				//获取连接
				Connection conn = empTransDao.getConnection();
				try
				{
					//开启事务
					empTransDao.beginTransaction(conn);
					//设置状态
					mt.setReState(-1); 
					// 未审批
					empTransDao.saveObjectReturnID(conn, mt);
					/*reviewBiz.addFlowRecords(conn, mtId, mt.getTitle(), mt.getSubmitTime(), mt.getMsType()
							, flow, mt.getUserId(), "1");*/
					//初始化审核记录的时候，传入taskid
					reviewBiz.addFlowRecords(conn, mt.getTaskId(), mt.getTitle(), mt.getSubmitTime(), mt.getMsType()
							, flow, mt.getUserId(), "1");
					String wgresult = balanceLogBiz.checkGwFee(mt.getSpUser(), Integer.parseInt(mt.getEffCount().toString()),mt.getCorpCode(),true,2);
					//处理运营商扣费
					if("nogwfee".equals(wgresult) || "feefail".equals(wgresult) || "lessgwfee".equals(wgresult.substring(0, 9)))
					{
						empTransDao.commitTransaction(conn);
						return wgresult;
					}else{
						//判断是否计费
						if(SystemGlobals.isDepBilling(mt.getUserId())){
							//进行扣费
							Integer result = balanceLogBiz.sendMmsAmountByUserId(conn, mt.getUserId(), mt.getEffCount());
							//扣费是否成功
							if(result != null && result == 0){
								//提交事务
								empTransDao.commitTransaction(conn);
								returnStr = "createSuccess";
									//进行审批处理
							}else if(result != null && result == -2)
							{  
								empTransDao.rollBackTransaction(conn);
								return "mmsyuebuzu";
							}else{
								empTransDao.rollBackTransaction(conn);
								String str = getMmsFeeMsg(result);
								return str;
							}
						}else{
							//提交事务
							empTransDao.commitTransaction(conn);
							returnStr = "createSuccess";
						}	
					}
				} catch (Exception e)
				{
					//回滚事务
					empTransDao.rollBackTransaction(conn);
					EmpExecutionContext.error(e, "彩信发送出现异常！");
				} finally
				{
					//关闭连接
					empTransDao.closeConnection(conn);
					if(returnStr == "createSuccess" && "0".equals(isre)){
						LfFlowRecord flowrecord = new LfFlowRecord();
						flowrecord.setInfoType(2);
						//短信审批提醒的时候，传入的是taskid
						flowrecord.setMtId(mt.getTaskId());
						flowrecord.setRLevel(0);
						flowrecord.setRLevelAmount(flow.getRLevelAmount());
						flowrecord.setProUserCode(mt.getUserId());
						new ReviewRemindBiz().flowReviewRemind(flowrecord);
						//调用短信审批提醒功能
						new ReviewRemindBiz().sendMail(flowrecord);
					}
				}
			} else
			{
				// 定时发送
				if (mt.getTimerStatus().intValue() == 1) 
				{
					//判断定时时间
					SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:00");
					String dateStr = sdf.format(new Date());
					if (mt.getTimerTime().getTime() < Timestamp.valueOf(dateStr).getTime())
					{
						returnStr = "timeError";
						return returnStr;
					}
				}
				//设置状态
				mt.setReState(0); 
				// 无需审批
				// 实时发送
				if (mt.getTimerStatus().intValue() == 0) 
				{
					empDao.saveObjectReturnID(mt);
					MttaskBiz mttaskBiz = new MttaskBiz();
					String wgresult = balanceLogBiz.checkGwFee(mt.getSpUser(), Integer.parseInt(mt.getEffCount().toString()),mt.getCorpCode(),true,2);
					//处理运营商扣费
					if("nogwfee".equals(wgresult) || "feefail".equals(wgresult) || "lessgwfee".equals(wgresult.substring(0, 9)))
					{
						return wgresult;
					}else{
							//判断是否计费
							if(SystemGlobals.isDepBilling(mt.getUserId())){
								//获取连接
								Connection conn = empTransDao.getConnection();
								try
								{
									//开启事务
								empTransDao.beginTransaction(conn);
								//进行扣费
								Integer result = balanceLogBiz.sendMmsAmountByUserId(conn, mt.getUserId(), mt.getEffCount());
								//判断是否扣费情况
								if(result != null && result == 0){
									//进行彩信发送
									returnStr = mttaskBiz.sendMms(mt.getTaskId());
									if("000".equals(returnStr)){
										//扣费成功
										empTransDao.commitTransaction(conn);
									}else if("通道账户余额不足，请联系通道提供商！".equals(returnStr)){
										//扣费失败
										empTransDao.rollBackTransaction(conn);
									}else{
										//扣费失败
										empTransDao.rollBackTransaction(conn);
									}
								}else if(result != null && result == -2)
								{  
									//扣费失败
									empTransDao.rollBackTransaction(conn);
									return "mmsyuebuzu";
								}else{
									//扣费失败
									empTransDao.rollBackTransaction(conn);
									String str = getMmsFeeMsg(result);
									return str;
								}
								} catch (Exception e)
								{
									//扣费失败
									empTransDao.rollBackTransaction(conn);
									EmpExecutionContext.error(e,"扣费失败！");
									throw e;
								} finally
								{
									//关闭连接
									empTransDao.closeConnection(conn);
								}
							}else{
									//发送彩信
								try{
									returnStr = mttaskBiz.sendMms(mt.getTaskId());
								}catch (Exception e) {
									EmpExecutionContext.error(e, "发送彩信失败！");
								}
							}
					}
				} else
				{
					//做事务
					Connection conn = empTransDao.getConnection();
					try
					{
						//开启事务
						empTransDao.beginTransaction(conn);
						//保存彩信任务
						empTransDao.saveObjectReturnID(conn, mt);
						
					String wgresult = balanceLogBiz.checkGwFee(mt.getSpUser(), Integer.parseInt(mt.getEffCount().toString()),mt.getCorpCode(),true,2);
					//处理运营商扣费
					if("nogwfee".equals(wgresult) || "feefail".equals(wgresult) || "lessgwfee".equals(wgresult.substring(0, 9)))
					{
						empTransDao.commitTransaction(conn);
						return wgresult;
					}else{		
							//判断是否需要计费
							if(SystemGlobals.isDepBilling(mt.getUserId())){
	
								//进行扣费
								//进行扣费
								Integer result = balanceLogBiz.sendMmsAmountByUserId(conn, mt.getUserId(), mt.getEffCount());
								//扣费情况反馈
								if(result != null && result == 0){
									//添加进定时任务
									TaskManagerBiz tm = new TaskManagerBiz();
									SendMmsTimerTask sendMmsTimerTask = new SendMmsTimerTask(
											mt.getTitle(), 1, new Date(mt.getTimerTime()
													.getTime()), 0, String.valueOf(mt.getTaskId()));
									sendMmsTimerTask.setTaskId(mt.getTaskId());
									//设置定时任务是否成功
									boolean flag = tm.setJob(sendMmsTimerTask);
									if (flag)
									{
										//提交事务
										empTransDao.commitTransaction(conn);
										returnStr = "timerSuccess";
									} else
									{
										//回滚
										empTransDao.rollBackTransaction(conn);
										// empDao.delete(LfMttask.class,
										// String.valueOf(mtId));
										returnStr = "timerFail";
									}
									//TODO
								} else{
                                    //扣费失败
                                    empTransDao.rollBackTransaction(conn);
                                    String str = getMmsFeeMsg(result);
                                    return str;
                                }
//                              一个 Integer 和 String 作 equals 比较， 我也不知道是哪个天才想出来的办法，
//                              反正这个逻辑永远也不会执行， 还要浪费时间去进行一下判断
//                              向这位大哥致敬；
//								else if("彩信余额不足".equals(result) )
//								{
//									//扣费失败
//									empTransDao.rollBackTransaction(conn);
//									return "mmsyuebuzu";
//								}

							}else{
								//设置定时情况
								TaskManagerBiz tm = new TaskManagerBiz();
								SendMmsTimerTask sendMmsTimerTask = new SendMmsTimerTask(
										mt.getTitle(), 1, new Date(mt.getTimerTime()
												.getTime()), 0, String.valueOf(mt.getTaskId()));
								sendMmsTimerTask.setTaskId(mt.getTaskId());
								//设置定时任务是否成功
								boolean flag = tm.setJob(sendMmsTimerTask);
								if (flag)
								{
									//提交事务
									empTransDao.commitTransaction(conn);
									returnStr = "timerSuccess";
								} else
								{
									//回滚
									empTransDao.rollBackTransaction(conn);
									// empDao.delete(LfMttask.class,
									// String.valueOf(mtId));
									returnStr = "timerFail";
								}
							}
						}	
					} catch (Exception e)
					{
						//或滚
						empTransDao.rollBackTransaction(conn);
						EmpExecutionContext.error(e,"发送彩信失败！");
						throw e;
					} finally
					{
						//关闭连接
						empTransDao.closeConnection(conn);
					}
				}

			}
		}
		return returnStr;
	}
	
	
	
	
	
	/**
	 * ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~彩信发送 end 
	 */
	
	/**
	 * ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~彩信任务查询记录begin
	 */
	
	/**
	 *  彩信发送任务信息查询 1
	 */
	 
    public List<LfMttaskVo2> getMmsLfMttaskVoWithoutDomination(
			LfMttaskVo2 lfMttaskVo, PageInfo pageInfo) throws Exception
	{
		List<LfMttaskVo2> mtTaskVosList;
		try
		{
			if(pageInfo == null)
			{
				mtTaskVosList = new MmsTaskDao().findLfMttaskVoWithoutDomination(
						lfMttaskVo);
			}
			else
			{
		  	   mtTaskVosList = new MmsTaskDao().findLfMttaskVoWithoutDomination(
					lfMttaskVo, pageInfo);
			}
		} catch (Exception e)
		{
			EmpExecutionContext.error(e,"彩信发送任务查询失败！");
			throw e;
		}
		return mtTaskVosList;
	}
	
	
	/**
	 *  彩信发送任务信息查询  2
	 */
	 
    public List<LfMttaskVo2> getMmsLfMttaskVo(Long curLoginedUser,
                                              LfMttaskVo2 lfMttaskVo, PageInfo pageInfo) throws Exception
	{
		List<LfMttaskVo2> mtTaskVosList;
		try
		{
			if (pageInfo == null)
			{
				mtTaskVosList = new MmsTaskDao().findLfMttaskVo(curLoginedUser,
						lfMttaskVo);
			} else
			{
				mtTaskVosList = new MmsTaskDao().findLfMttaskVo(curLoginedUser,
						lfMttaskVo, pageInfo);
			}
		} catch (Exception e)
		{
			EmpExecutionContext.error(e,"彩信发送任务查询失败！");
			//异常处理
			throw e;
		}
		return mtTaskVosList;
	}
	
	
	/**
	 *  彩信发送任务信息查看审批信息
	 * @param mtID
	 * @param rLevel
	 * @return
	 * @throws Exception
	 */
	 
    public List<LfFlowRecordVo> getFlowRecordVos(String mtID, String rLevel, String reviewType)
			throws Exception
	{
		List<LfFlowRecordVo> frVosList;
		try
		{
			/*frVosList = lfFlowRecordVoDAO.findLfFlowRecordVo(mtID, rLevel,reviewType);*/
			frVosList = new MmsTaskDao().findLfFlowRecordVo(mtID, rLevel,reviewType);
		} catch (Exception e)
		{
			EmpExecutionContext.error(e,"彩信发送任务查询查看审批信息失败！");
			throw e;
		}
		return frVosList;
	}
	
	
	/**
	 * ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~彩信任务查询记录 end 
	 */
	
	/**
	 * ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~彩信定时任务查询记录begin
	 */
	
	/**
	 * 获取当前登录用户的审批流信息
	 * @param curLoginedUserId
	 * @param lfFlowRecordVo
	 * @param pageInfo
	 * @return
	 * @throws Exception
	 */
	 
    public List<LfFlowRecordVo> getFlowRecordVos(Long curLoginedUserId,
                                                 LfFlowRecordVo lfFlowRecordVo, PageInfo pageInfo) throws Exception
	{
		List<LfFlowRecordVo> frVosList;
		try
		{
			//没有分页
			if (pageInfo == null)
			{
				
				/*frVosList = lfFlowRecordVoDAO
						.findLfFlowRecordVo(curLoginedUserId, lfFlowRecordVo);*/
				frVosList = new MmsTaskDao().findLfFlowRecordVo(curLoginedUserId, lfFlowRecordVo);
			} 
			//分页
			else
			{
				/*frVosList = lfFlowRecordVoDAO
						.findLfFlowRecordVo(curLoginedUserId, lfFlowRecordVo,
								pageInfo);*/
				frVosList = new MmsTaskDao().findLfFlowRecordVo(curLoginedUserId, lfFlowRecordVo,pageInfo);
			}
		} catch (Exception e)
		{
			EmpExecutionContext.error(e,"获取当前登录用户的审批流信息失败！");
			//抛出异常
			throw e;
		}
		//返回查询结果
		return frVosList;
	}
	
	
	/**
	 *  更改彩信状态
	 * @param mtId
	 * @param sendstate
	 * @param subStateValue
	 * @return
	 * @throws Exception
	 */
	 
    public boolean changeState(Long mtId, String sendstate, String subStateValue)
			throws Exception
	{
		boolean result = false;
		LinkedHashMap<String, String> conditionMap = new LinkedHashMap<String, String>();
		try
		{
			LinkedHashMap<String, String> lfMttaskMap = new LinkedHashMap<String, String>();
			if (sendstate != null)
			{
				lfMttaskMap.put(sendstate, "1");
			}
			if (subStateValue != null)
			{
				lfMttaskMap.put("subState", subStateValue);
				if (subStateValue.equals("3"))
				{
					lfMttaskMap.put("reState", "0");
				}
			}
			conditionMap.put("mtId", mtId.toString());
			result = empDao.update(LfMttask.class, lfMttaskMap, conditionMap);
		} catch (Exception e)
		{
			EmpExecutionContext.error(e,"更新彩信状态失败！");
			throw e;
		}
		return result;
	}
	
	
	
	
	
	/**
	 * 提交彩信任务
	 * @param mtId	任务ID
	 * @param subState	提交状态
	 * @return
	 * @throws Exception
	 */
	 
    public String submitMmsLfMttask(String mtId, Integer subState)
			throws Exception
	{
		String returnStr = "";
		//获取起彩信任务
		LfMttask mt = empDao.findObjectByID(LfMttask.class, Long
				.parseLong(mtId));
		//设置起状态
		mt.setSubState(subState);

		if (subState == 2)
		{
			//设置提交时间
			mt.setSubmitTime(new Timestamp(Calendar.getInstance().getTime()
					.getTime()));
			/*MsgReviewConfigBiz flowBiz = new MsgReviewConfigBiz();
			MsgReviewBiz examineBiz = new MsgReviewBiz();*/
			ReviewBiz reviewBiz = new ReviewBiz();
			//获取其审批信息
			/*LfFlow flow = flowBiz.getFlowBySysUserId(mt.getUserId());*/
			LfFlow flow = reviewBiz.checkUserFlow(mt.getUserId(), mt.getCorpCode(), mt.getMsType().toString(),mt.getEffCount());
			//getFlowBySysUserId(mt.getUserId());
			//判断是否需要审批
			if (flow != null)
			{
				//获取连接
				Connection conn = empTransDao.getConnection();
				try
				{
					//开启事务
					empTransDao.beginTransaction(conn);
					//设置起状态
					mt.setReState(-1);
					//添加到其审批信息中 
					/*examineBiz.addFlowRecords(conn, mt.getMtId(), mt
							.getMsType(), flow);*/
					reviewBiz.addFlowRecords(conn, mt.getTaskId(), mt.getTitle(), mt.getSubmitTime(), mt.getMsType()
							, flow, mt.getUserId(), "1");//addFlowRecords(conn, mtId, mt.getMsType(), flow)
					//更新其彩信任务
					empTransDao.update(conn, mt);
					//提交事务
					empTransDao.commitTransaction(conn);
					returnStr = "success";
				} catch (Exception e)
				{
					EmpExecutionContext.error(e,"提交彩信任务失败！");
					//回滚
					empTransDao.rollBackTransaction(conn);
					throw e;
				} finally
				{
					//关闭连接
					empTransDao.closeConnection(conn);
				}
			} else
			{
				//判断是否定时
				if (mt.getTimerStatus().intValue() == 1)
				{
					//获取其时间
					SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:00");
					String dateStr = sdf.format(new Date());
					//判断设置的时间是否是合法
					if (mt.getTimerTime().getTime() < Timestamp.valueOf(dateStr).getTime())
					{
						returnStr = "timeError";
						return returnStr;
					}
				}
				//设置其提交状态
				mt.setReState(0);
				//判断是否定时
				if (mt.getTimerStatus().intValue() == 0)
				{
					//更新其任务
					//发送彩信
					empDao.update(mt);
					MttaskBiz mttaskBiz = new MttaskBiz();
					returnStr = mttaskBiz.sendMms(mt.getTaskId());
				} else
				{
					//获取其连接
					Connection conn = empTransDao.getConnection();
					try
					{
						//开启事务
						empTransDao.beginTransaction(conn);
						//更新彩信任务
						empTransDao.update(conn, mt);
						//设置彩信定时任务
						TaskManagerBiz tm = new TaskManagerBiz();
						SendMmsTimerTask sendMmsTimerTask = new SendMmsTimerTask(
								mt.getTitle(), 1, new Date(mt.getTimerTime()
										.getTime()), 0, String.valueOf(mt
										.getTaskId()));
						sendMmsTimerTask.setTaskId(mt.getTaskId());
						//设置定时任务是否成功
						boolean flag = tm.setJob(sendMmsTimerTask);
						if (flag)
						{
							//提交事务
							empTransDao.commitTransaction(conn);
							returnStr = "timerSuccess";
						} else
						{
							//回滚
							empTransDao.rollBackTransaction(conn);
							returnStr = "timerFail";
						}
					} catch (Exception e)
					{
						EmpExecutionContext.error(e,"提交彩信任务失败！");
						//回滚
						empTransDao.rollBackTransaction(conn);
						throw e;
					} finally
					{
						//关闭连接
						empTransDao.closeConnection(conn);
					}
				}
			}
		} else
		{
			
			BalanceLogBiz balanceLogBiz = new BalanceLogBiz();
			//判断其发送状态以及其定时状态
			if (mt.getSendstate() == 0 && mt.getTimerStatus() == 1)
			{
				//取消其任务
				cancelLfMttask(mt.getTaskId());
					//更新起任务
					boolean isSuccess = empDao.update(mt);
					/*if(isSuccess){
						//判断是否计费用
						if(SystemGlobals.isDepBilling(mt.getUserId())){
							//扣费是否成功
							Integer str= balanceLogBiz.sendMmsAmountByUserId(null, mt.getUserId(), mt.getEffCount()*-1);
		                    if(str == 1)
		                    {
		                    	returnStr = "cancelSuccess";
		                    }
		                    else
		                    {
								returnStr = "cancelSuccess";
		                    }
						}else{
							returnStr = "cancelSuccess";
						}
						
					returnStr = "cancelSuccess";
					}*/
					

					if(isSuccess){
						//判断是否计费用
						if(SystemGlobals.isDepBilling(mt.getUserId())){
							//扣费是否成功
							Integer str= balanceLogBiz.sendMmsAmountByUserId(null, mt.getUserId(), mt.getEffCount()*-1);
						}
					}
					
					returnStr = "cancelSuccess";
				
			} else
			{
				boolean isSuccess = empDao.update(mt);
				/*if(isSuccess){
					//判断是否计费
					if(SystemGlobals.isDepBilling(mt.getUserId())){
						//判断是否扣费是否成功
						Integer str= balanceLogBiz.sendMmsAmountByUserId(null, mt.getUserId(), mt.getEffCount()*-1);
	                    if(str == 1)
	                    {
	                    	returnStr = "cancelSuccess";
	                    }
	                    else
	                    {
							returnStr = "cancelSuccess";
	                    }
					}else{
						returnStr = "cancelSuccess";
					}
				}
				returnStr = "cancelSuccess";*/
				
				if(isSuccess){
					//判断是否计费
					if(SystemGlobals.isDepBilling(mt.getUserId())){
						//判断是否扣费是否成功
						Integer str= balanceLogBiz.sendMmsAmountByUserId(null, mt.getUserId(), mt.getEffCount()*-1);
					}
				}
				returnStr = "cancelSuccess";
			}
		}
		return returnStr;
	}
	
	/**
	 * 取消任务
	 * @param mtID
	 * @return
	 * @throws Exception
	 */
	private boolean cancelLfMttask(Long taskId) throws Exception
	{
		TaskManagerBiz taskManagerBiz = new TaskManagerBiz();
		List<LfTimer> lfTimerList = taskManagerBiz.getTaskByExpression(String
				.valueOf(taskId));
		LfTimer lfTimer = null;
		if (lfTimerList != null && lfTimerList.size() > 0)
		{
			lfTimer = lfTimerList.get(0);
			taskManagerBiz.stopTask(lfTimer.getTimerTaskId());
		}
		return true;
	}
	
	
	/**
	 * 
	 * @param userId
	 * @return
	 * @throws Exception
	 */
	 
    public List<LfSysuser> getAllSysusersOfSmsTaskRecordByDep(Long userId, Long depId) throws Exception {
		List<LfSysuser> lfSysuserList = new ArrayList<LfSysuser>();
		try {

			if (null != userId) {
                lfSysuserList = new MmsTaskDao().findDomUserBySysuserIDOfSmsTaskRecordByDep(userId
                        .toString(),depId.toString());
            }

		} catch (Exception e) {
			EmpExecutionContext.error(e,"获取操作员失败！");
			throw e;
		}
		return lfSysuserList;
	}
	
	

}
