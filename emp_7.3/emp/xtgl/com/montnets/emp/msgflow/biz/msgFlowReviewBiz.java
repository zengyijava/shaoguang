package com.montnets.emp.msgflow.biz;

import java.sql.Connection;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import com.montnets.emp.common.biz.BalanceLogBiz;
import com.montnets.emp.common.biz.ReviewRemindBiz;
import com.montnets.emp.common.biz.SendMessage;
import com.montnets.emp.common.biz.SendSmsTimerTask;
import com.montnets.emp.common.biz.SuperBiz;
import com.montnets.emp.common.constant.SystemGlobals;
import com.montnets.emp.common.context.EmpExecutionContext;
import com.montnets.emp.common.timer.TaskManagerBiz;
import com.montnets.emp.common.vo.LfFlowRecordVo;
import com.montnets.emp.entity.approveflow.LfFlowRecord;
import com.montnets.emp.entity.sms.LfMttask;
import com.montnets.emp.entity.system.LfTimer;
import com.montnets.emp.msgflow.vo.dao.GenericLfFlowRecordVoDAO;
import com.montnets.emp.util.PageInfo;

public class msgFlowReviewBiz extends SuperBiz{
	
	private GenericLfFlowRecordVoDAO lfFlowRecordVoDAO;
	//private GenericLfFlowRecordTemplateVoDAO lfFlowRecordTemplateVoDAO;
	
	public msgFlowReviewBiz()
	{
		lfFlowRecordVoDAO=new GenericLfFlowRecordVoDAO();
		//lfFlowRecordTemplateVoDAO=new GenericLfFlowRecordTemplateVoDAO();
	}
	
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
				
				frVosList = lfFlowRecordVoDAO
						.findLfFlowRecordVo(curLoginedUserId, lfFlowRecordVo);
			} 
			//分页
			else
			{
				frVosList = lfFlowRecordVoDAO
						.findLfFlowRecordVo(curLoginedUserId, lfFlowRecordVo,
								pageInfo);
			}
		} catch (Exception e)
		{
			EmpExecutionContext.error(e, "获取审批流信息发生异常！");
			//抛出异常
			throw e;
		}
		//返回查询结果
		return frVosList;
	}
	
	/**
	 * 获取当前登录用户的审批流程信息
	 * @param curLoginedUserId
	 * @param lfFlowRecordVo
	 * @return
	 * @throws Exception
	 */
	public List<LfFlowRecordVo> getFlowRecordVos(Long curLoginedUserId,
			LfFlowRecordVo lfFlowRecordVo) throws Exception
	{
		List<LfFlowRecordVo> frVosList;
		try
		{
			frVosList = lfFlowRecordVoDAO
					.findLfFlowRecordVo(curLoginedUserId, lfFlowRecordVo);
		} catch (Exception e)
		{
			EmpExecutionContext.error(e, "获取登录用户审批流信息发生异常！");
			throw e;
		}
		return frVosList;
	}

	/**
	 * 
	 * @param mtID
	 * @param rLevel
	 * @return
	 * @throws Exception
	 */
	public List<LfFlowRecordVo> getFlowRecordVos(String mtID, String rLevel,String reviewType)
			throws Exception
	{
		List<LfFlowRecordVo> frVosList;
		try
		{
			frVosList = lfFlowRecordVoDAO
					.findLfFlowRecordVo(mtID, rLevel,reviewType);
		} catch (Exception e)
		{
			EmpExecutionContext.error(e, "获取审批流信息发生异常！");
			throw e;
		}
		return frVosList;
	}
	
	/**
	 * 审批短信方法(短信)
	 * @param frId
	 * @param rState
	 * @param comments
	 * @param timerStatus
	 * @param timerTime
	 * @return
	 * @throws Exception
	 */
	public String reviewSMS(Long frId, Integer rState, String comments,
			Integer timerStatus, String timerTime) throws Exception
	{
	
		String returnStr = "";
		boolean isOverTime = false;
		BalanceLogBiz biz = new BalanceLogBiz();
		//查询当前审批流信息
		LfFlowRecord flowRecord = empDao.findObjectByID(LfFlowRecord.class,
				frId);
		//设置审批状态，及审批意思，及审批时间
		flowRecord.setRState(rState);
		flowRecord.setComments(comments);
		flowRecord.setRTime(new Timestamp(System.currentTimeMillis()));

		if (flowRecord.getPreRv() - 0 == 0) {
			flowRecord.setPreRv(null);

		}
		//查找当前审批的短信任务
		LfMttask mtTask = empDao.findObjectByID(LfMttask.class, flowRecord
				.getMtId());
		
		//获取一个连接
		Connection conn = empTransDao.getConnection();
		try
		{
			//开启一个事务
			empTransDao.beginTransaction(conn);
			//如果不是最后一级审批且审批通过且是定时任务
			if (flowRecord.getRLevel() != flowRecord.getRLevelAmount()
					&& rState == 1&&timerStatus ==1){
				SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
				Date date = sdf.parse(timerTime);
				//修改定时发送时间
				mtTask.setTimerTime(new Timestamp(date.getTime()));
				empTransDao.update(conn, mtTask);
				returnStr = "success";
			}else if (flowRecord.getRLevel()!=null&&flowRecord.getRLevel().equals(flowRecord.getRLevelAmount())
					&& rState == 1){
				//如果当前审批流程是最后一级,&&是审批通过
				mtTask.setReState(rState);
				mtTask.setTimerStatus(timerStatus);
				//如果是定时任务
				if (timerStatus.intValue() == 1){
					SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
					Date date = sdf.parse(timerTime);
					mtTask.setTimerTime(new Timestamp(date.getTime()));
					//判断定时时间是否大于当前时间
					if (mtTask.getTimerTime().getTime() < Calendar.getInstance()
							.getTime().getTime()){
						returnStr = "overtime";
						return returnStr;
					} else {
						returnStr = "success";
					}
				}else{
					Date date = new Date();
					mtTask.setTimerTime(new Timestamp(date.getTime()));
					mtTask.setTimerStatus(timerStatus);
					returnStr = "success";
				}
				empTransDao.update(conn, mtTask);
			} else if (rState == 2){
				// 拒绝操作
				mtTask.setReState(rState);
				empTransDao.update(conn, mtTask);
				//判断此条任务的操作员所在机构是否启用了计费功能
				if (biz.IsChargings(mtTask.getUserId())) {
					//拒绝操作需要补回扣费记录
					Integer str= biz.sendSmsAmountByUserId(conn, mtTask.getUserId(), Integer.parseInt(mtTask.getIcount())*-1);
					if (str == 1) {
						returnStr = "success";
					} else {
						returnStr = "false";
					}
				} else {
					returnStr = "success";
				}
				//add end				
			} else {
				returnStr = "success";
			}
			//如果更新状态及扣费操作都成功
			if(returnStr.equals("success")){	
				//修改审批流信息
				empTransDao.update(conn, flowRecord);
				//提交事务
				empTransDao.commitTransaction(conn);
				//如果开启了短信审批提醒功能且是审批通过
				String isre=SystemGlobals.getSysParam("isRemind");
				if("0".equals(isre)&&rState==1){
            		ReviewRemindBiz rere=new ReviewRemindBiz();
            		//调用短信审批提醒功能
            		rere.reviewRemind(flowRecord,mtTask.getMtId(),1);
            	}
			} else {
				//回滚事务
				empTransDao.rollBackTransaction(conn);
				return returnStr;//如果出现错误直接跳出
			}
		} catch (Exception e)
		{
			EmpExecutionContext.error(e, "审批短信发生异常！");
			//回滚事务
			empTransDao.rollBackTransaction(conn);
			throw e;
		} finally
		{
			//关闭连接
			empTransDao.closeConnection(conn);
		}

        //如果是最后一级审批，且审批通过
		if (flowRecord.getRLevel()!=null&&flowRecord.getRLevel().equals(flowRecord.getRLevelAmount())
				&& rState == 1) {
			//非定时任务
			if (timerStatus.intValue() == 0) {
				SendMessage sendMessage = new SendMessage();
				//调用发送接口直接发送,如果失败且开启计费功能，则补回
				try{
					returnStr = sendMessage.sendSms(mtTask, null);
				}catch (Exception e) {
					returnStr = e.getMessage();
					EmpExecutionContext.error(e,"调用发送接口失败！");
				}
			} else {
					if (!isOverTime)//定时未超时时
					{
						TaskManagerBiz tmb=new TaskManagerBiz();
						//获取当前任务定时信息
						List<LfTimer> timerlist=tmb.getTaskByExpression(String.valueOf(mtTask.getMtId()));
						if(timerlist!=null&&timerlist.size()==0){
							TaskManagerBiz tm = new TaskManagerBiz();
							SendSmsTimerTask sendSmsTimerTask = new SendSmsTimerTask(mtTask
									.getTitle(), 1, new Date(mtTask.getTimerTime()
									.getTime()), 0, String.valueOf(mtTask.getMtId()));
							sendSmsTimerTask.setMtId(mtTask.getMtId());
							boolean flag = tm.setJob(sendSmsTimerTask);
							if (flag){
								//定时成功
								returnStr = "timerSuccess";
							} else{
								returnStr = "timerFail";
								//判断当前任务所属操作员所在机构是否开启计费功能
								if(biz.IsChargings(mtTask.getUserId())){
									//如果审批通过但创建定时任务失败,则需要在此补回
									 biz.sendSmsAmountByUserId(null, mtTask.getUserId(), Integer.parseInt(mtTask.getIcount())*-1);
								}
								mtTask.setSendstate(2);//设置为发送失败
								empDao.update(mtTask);
								//add end
							}
						}
					}
			}
		}
		return returnStr;
	}

}
