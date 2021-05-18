package com.montnets.emp.msgflow.biz;

import java.sql.Connection;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;

import com.montnets.emp.common.biz.BalanceLogBiz;
import com.montnets.emp.common.biz.BaseBiz;
import com.montnets.emp.common.biz.CommonBiz;
import com.montnets.emp.common.biz.ReviewBiz;
import com.montnets.emp.common.biz.ReviewRemindBiz;
import com.montnets.emp.common.biz.SendMessage;
import com.montnets.emp.common.biz.SendMmsTimerTask;
import com.montnets.emp.common.biz.SendSmsTimerTask;
import com.montnets.emp.common.biz.SuperBiz;
import com.montnets.emp.common.constant.SystemGlobals;
import com.montnets.emp.common.context.EmpExecutionContext;
import com.montnets.emp.common.timer.TaskManagerBiz;
import com.montnets.emp.entity.approveflow.LfFlowRecord;
import com.montnets.emp.entity.sms.LfMttask;
import com.montnets.emp.entity.system.LfTimer;
/**
 *   信息审批
 * @author Administrator
 *
 */
public class MsgExamineBiz  extends SuperBiz{
	
	
	
	/**
	 *   判断该对象是不是该级别实时表中最后的一个审批人
	 * @param flowRecord 需要全部生效的条件下
	 * @return
	 */
	public String judgeLevelLastChecker(LfFlowRecord flowRecord){
		String isLevelLastCheck = "no";
		try{
			//当前级别
			Integer rLevel = flowRecord.getRLevel(); 
			//全部通过生效 
			//查询该级别中的审核任务是否其他全部通过
			LinkedHashMap<String, String> conditionMap = new LinkedHashMap<String, String>();
		    conditionMap.put("FId", String.valueOf(flowRecord.getFId()));
		    conditionMap.put("mtId",  String.valueOf(flowRecord.getMtId()));
		    conditionMap.put("RLevel", String.valueOf(rLevel));
		    conditionMap.put("userCode&<>", String.valueOf(flowRecord.getUserCode()));
		    conditionMap.put("isComplete","2");
			List<LfFlowRecord> levelRecords =  new BaseBiz().getByCondition(LfFlowRecord.class, conditionMap, null);
			if(levelRecords != null && levelRecords.size()>0){
				/*//审批完成的人数
				int count = 0;
				for(LfFlowRecord temp:levelRecords){
					//如果有一个未完成则跳出
					if(temp.getIsComplete() == 2){
						isLevelLastCheck = "no";
						break;
					}else if(temp.getIsComplete() == 1){
						count = count + 1;
					}
				}
				if(count == levelRecords.size()){
					isLevelLastCheck = "yes";
				}else{
					isLevelLastCheck = "no";
				}*/
				isLevelLastCheck = "no";
			}else{
				isLevelLastCheck = "yes";
			}
		}catch (Exception e) {
			isLevelLastCheck = "no";
			EmpExecutionContext.error(e,"判断该对象是不是该级别实时表中最后的一个审批人出现异常！");
		}
		return isLevelLastCheck;
	}
	
	
	
	/**
	 *   根据条件判断是否是一人生效还是全部生效
	 *   如果是一人生效  则判断是不是最后一级 的审核人
	 *   如果是全部生效 则判断是不是最后一级的最后一个审批人
	 * @param flowRecord
	 * @return  yes 是     no  不是
	 */
	public String judgeLastChecker(LfFlowRecord flowRecord){
		String isLastCheck = "no";
		try{
			//审批条件
			Integer auditCondition = flowRecord.getRCondition();
			//总审核级别
			Integer rLevelAmount = flowRecord.getRLevelAmount(); 
			//当前级别
			Integer rLevel = flowRecord.getRLevel(); 
			//全部通过生效 
			ReviewBiz reviewBiz= new ReviewBiz();
			boolean isLastLevel = (rLevel!=null&&rLevel.equals(rLevelAmount));
			if(isLastLevel && flowRecord.getRType() == 5)
			{
				isLastLevel = reviewBiz.checkZhujiIsOver(flowRecord.getPreRv().intValue(), flowRecord.getProUserCode());
			}
			if(auditCondition == 1){
				//如果是最后一级别
				//是否最后一级
				if(isLastLevel){
					//查询该级别中的审核任务是否其他全部通过
					LinkedHashMap<String, String> conditionMap = new LinkedHashMap<String, String>();
				    conditionMap.put("FId", String.valueOf(flowRecord.getFId()));
				    conditionMap.put("mtId",  String.valueOf(flowRecord.getMtId()));
				    conditionMap.put("RLevel", String.valueOf(rLevel));
				    conditionMap.put("userCode&<>", String.valueOf(flowRecord.getUserCode()));
				    conditionMap.put("isComplete","2");
					List<LfFlowRecord> levelRecords = new BaseBiz().getByCondition(LfFlowRecord.class, conditionMap, null);
					if(levelRecords != null && levelRecords.size()>0){
						/*//审批完成的人数
						int count = 0;
						for(LfFlowRecord temp:levelRecords){
							//如果有一个未完成则跳出
							if(temp.getIsComplete() == 2){
								isLastCheck = "no";
								break;
							}else{
								count = count + 1;
							}
						}
						if(count == levelRecords.size()){
							isLastCheck = "yes";
						}else{
							isLastCheck = "no";
						}*/
						isLastCheck = "no";
					}else{
						isLastCheck = "yes";
					}
				}
			//一人生效
			}else if(auditCondition == 2){
				//如果是最后一级  ,可以发送
				if(isLastLevel){
					isLastCheck = "yes";
					//是否最后一级
				}
			}
		}catch (Exception e) {
			isLastCheck = "no";
			EmpExecutionContext.error(e,"根据条件判断是否是一人生效还是全部生效出现异常！");
		}
		return isLastCheck;
	}
	
	
	
	
	
	
	
	
	
	
	
	/**
	 *   短信审批
	 * @param flowRecord   当前审批流程对象
	 * @param rState   审批状态  同意  1  拒绝 2	
	 * @param comments	审批意见
	 * @param timerStatus	定时状态
	 * @param timerTime	定时时间
	 * @param isLastCheck 是否为最后一个审批人
	 * @return
	 * @throws Exception
	 */
	public String examineSMS(LfFlowRecord flowRecord,Integer timerStatus, String timerTime,String isLastCheck) throws Exception{
		String returnStr = "";
		try{
			BalanceLogBiz biz = new BalanceLogBiz();
			String approve="";  //作为审核处理标志 0 表示同意，1表示拒绝
			//查找当前审批的短信任务
			//LfMttask mtTask = empDao.findObjectByID(LfMttask.class, flowRecord.getMtId());
			LfMttask mtTask = new CommonBiz().getLfMttaskbyTaskId(flowRecord.getMtId());
			if(mtTask == null){
				return "norecord";
			}
			if(mtTask != null && mtTask.getSubState() != null){
				if(mtTask.getSubState() == 3){
					//该任务已被撤销
					return "isrevoke";
				}else if(mtTask.getSubState() == 4){
					//该任务已冻结
					return "freeze";
				}
			}
			//在全部审核生效的情况下      标识的是  该级下是否是最后一个审批人
			String isLevelLastChecker = "no";
			if(flowRecord.getRCondition() == 1){
				isLevelLastChecker = judgeLevelLastChecker(flowRecord);
			}
			boolean iszjLastLevel = false;
			ReviewBiz reviewBiz = new ReviewBiz();
			if(flowRecord.getRType() == 5)
			{
				iszjLastLevel = reviewBiz.checkZhujiIsOver(flowRecord.getPreRv().intValue(), flowRecord.getProUserCode());
			}else{
				iszjLastLevel = true;
			}
			//是否可以发送该短信任务
			boolean isFlagSendSms = false;
			//判断是否下发短信给审批提醒
			String isFlagSendRemind = "nosend";
			//获取一个连接
			Connection conn = empTransDao.getConnection();
			try{
				empTransDao.beginTransaction(conn);
				//同意
				if(flowRecord.getRState() == 1){
					//全部通过生效
					if(flowRecord.getRCondition() == 1){
						//判断是不是该级的最后一个审批人
						if("yes".equals(isLevelLastChecker)){
							//判断是不是最后一级
							if(flowRecord.getRLevel()!=null&&flowRecord.getRLevel().equals(flowRecord.getRLevelAmount()) && iszjLastLevel){
								//是  则发送时处理lfmttask
								returnStr = sendAgreeLfMaTask(conn, mtTask, timerStatus, timerTime);
								if("success".equals(returnStr)){
									 approve="0";  
//									new ReviewRemindBiz().ReviewRemindForSender(flowRecord,"");
									isFlagSendSms = true;
								}
							}else{
								//不是的话则  实例化 下一级
								boolean flag = reviewBiz.addFlowRecordsForNext(conn, flowRecord);
								if(flag){
									returnStr = "success";
								}else{
									return "addChildFail";
								}
								returnStr = updateAgreeLfMaTask(conn, mtTask, timerTime);
								if("success".equals(returnStr)){
									isFlagSendRemind = "yessend";
								}
								
							}
						}else if("no".equals(isLevelLastChecker)){
							//更新该流程  。
							returnStr = "success";
						}
					//一人通过生效
					}else if(flowRecord.getRCondition() == 2){
						//更新所有审核级别
						returnStr = updateCurrentRecord(conn, flowRecord);
						//更新该等级下的record出错
						if("fail".equals(returnStr)){
							return returnStr;
						}
						//是最后一级的审批人
						if("yes".equals(isLastCheck)){
							//发送时处理lfmttask
							returnStr = sendAgreeLfMaTask(conn, mtTask, timerStatus, timerTime);
							if("success".equals(returnStr)){
								approve="0";
//								new ReviewRemindBiz().ReviewRemindForSender(flowRecord,"");
								isFlagSendSms = true;
							}
						}else if("no".equals(isLastCheck)){
							//则实例化 下一级 RECORD
							boolean flag = reviewBiz.addFlowRecordsForNext(conn, flowRecord);
							if(flag){
								returnStr = "success";
							}else{
								return "addChildFail";
							}
							returnStr = updateAgreeLfMaTask(conn, mtTask, timerTime);
							if("success".equals(returnStr)){
								isFlagSendRemind = "yessend";
							}
						}
					}
				//拒绝
				}else if(flowRecord.getRState() == 2){
					//处理更新当前级别的所有状态改为已完成
					returnStr = updateCurrentRecord(conn, flowRecord);
					//更新该等级下的record出错
					if("fail".equals(returnStr)){
						return returnStr;
					}
					//处理短信任务状态   拒绝退费
					returnStr = refuseSmsLfMtTask(conn, mtTask, biz);
					//拒绝时候给出
					approve="1";
//					new ReviewRemindBiz().ReviewRemindForSender(flowRecord,flowRecord.getRLevel()+"");
				}
				//如果更新状态及扣费操作都成功
				if(returnStr.equals("success")){
					returnStr = updateRecord(conn, flowRecord);
				} else {
					//回滚事务
					empTransDao.rollBackTransaction(conn);
					return returnStr;//如果出现错误直接跳出
				}
			}catch (Exception e) {
				//回滚事务
				empTransDao.rollBackTransaction(conn);
				EmpExecutionContext.error(e,"短信审批出现异常！");
			}finally
			{
				//关闭连接
				empTransDao.closeConnection(conn);
			}
			
			//有可能为空，不做处理 新加的要求，最后发送给第一个发送者信息
			if("0".equals(approve)){//同意
				new ReviewRemindBiz().ReviewRemindForSender(flowRecord,"");
				new ReviewRemindBiz().sendMailForSender(flowRecord,"");
			}else if("1".equals(approve)){//拒绝
				new ReviewRemindBiz().ReviewRemindForSender(flowRecord,flowRecord.getRLevel()+"");
				new ReviewRemindBiz().sendMailForSender(flowRecord,flowRecord.getRLevel()+"");
			}
			//调用短信提醒方法
			this.reviewRemindMsg(flowRecord, isFlagSendRemind);
			//调用邮件发送方法
			this.reviewSendE_mail(flowRecord, isFlagSendRemind);
			//如果是最后可以发送该短信任务了， 则进行发送
			if(isFlagSendSms){
				returnStr = SendSms(mtTask, biz);
			}
		}catch (Exception e) {
			returnStr = "fail";
			EmpExecutionContext.error(e,"短信审批出现异常！");
		}
		return returnStr;
	}
	
	
	/**
	 *   处理更新当前级别的审核实时记录
	 * @param conn
	 * @param flowRecord
	 * @throws Exception
	 */
	public String updateCurrentRecord(Connection conn,LfFlowRecord flowRecord)  throws Exception{
		String returnStr = "fail";
		try{
			LinkedHashMap<String, String> conditionMap = new LinkedHashMap<String, String>();
			LinkedHashMap<String, String> orderByMap = new LinkedHashMap<String, String>();
			
			//如果是逐级审核，则追加一个条件。
			if(flowRecord.getRType() != null && flowRecord.getRType() == 5)
			{
				conditionMap.put("preRv", flowRecord.getPreRv().toString());
			}
			conditionMap.put("FId", String.valueOf(flowRecord.getFId()));
			conditionMap.put("mtId", String.valueOf(flowRecord.getMtId()));
			conditionMap.put("RLevel", String.valueOf(flowRecord.getRLevel()));
		//	conditionMap.put("userCode&<>", String.valueOf(flowRecord.getUserCode()));
			conditionMap.put("infoType", String.valueOf(flowRecord.getInfoType()));
			orderByMap.put("isComplete", "1");
			empTransDao.update(conn, LfFlowRecord.class, orderByMap, conditionMap);
			returnStr = "success";
		}catch (Exception e) {
			empTransDao.rollBackTransaction(conn);
			returnStr = "fail";
			EmpExecutionContext.error(e,"更新当前级别的审核实时记录出现异常！");
		}
		return returnStr;
	}
	
	
	/**
	 *   处理短信审核拒绝的情况
	 * @param conn	连接
	 * @param mtTask	短信任务
	 * @param biz	计费BIZ
	 * @return
	 * @throws Exception
	 * 
	 * 				扣费返回值
	 * 	 * 		1:短信回收成功
	 * 			0:短信扣费成功
	 * 		   -1:短信扣费失败
	 * 		   -2:短信余额不足
	 *  	   -4:用户所属机构没有充值
	 *  	   -5:短信发送或回收条数不能为空
	 * 		   -9:短信回收失败
	 * 		-9999:短信发送扣费回收接口调用异常
	 */
	public String refuseSmsLfMtTask(Connection conn,LfMttask mtTask,BalanceLogBiz biz)  throws Exception{
		String returnStr = "fail";
		try{
			mtTask.setReState(2);
			empTransDao.update(conn, mtTask);
			//判断此条任务的操作员所在机构是否启用了计费功能
			if (biz.IsChargings(mtTask.getUserId())) {
				//拒绝操作需要补回扣费记录
				Integer str= biz.sendSmsAmountByUserId(conn, mtTask.getUserId(), Integer.parseInt(mtTask.getIcount())*-1);
				if (str == 1) {
					returnStr = "success";
				} else {
					returnStr = "feefail";
				}
			} else {
				returnStr = "success";
			}
		}catch (Exception e) {
			empTransDao.rollBackTransaction(conn);
			returnStr = "fail";
			EmpExecutionContext.error(e,"处理短信审核拒绝出现异常！");
		}
		return returnStr;
	}
	
	/**
	 *   最后可以发送短信    
	 * @param conn	连接
	 * @param mtTask	 任务
	 * @param timerStatus	定时
	 * @param timerTime	时间
	 * @return
	 * @throws Exception
	 */
	public String sendAgreeLfMaTask(Connection conn,LfMttask mtTask,Integer timerStatus,String timerTime)  throws Exception{
		String returnStr = "fail";
		try{
			//如果当前审批流程是最后一级,&&是审批通过
			mtTask.setReState(1);
			mtTask.setTimerStatus(timerStatus);
			//如果是定时任务
			if (timerStatus.intValue() == 1){
				SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
				Date date = sdf.parse(timerTime);
				mtTask.setTimerTime(new Timestamp(date.getTime()));
				//判断定时时间是否大于当前时间
				if (mtTask.getTimerTime().getTime() < Calendar.getInstance().getTime().getTime()){
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
		}catch (Exception e) {
			empTransDao.rollBackTransaction(conn);
			returnStr = "fail";
			EmpExecutionContext.error(e,"更新任务失败！");
		}
		return returnStr;
	}
	/**
	 * 如果不是最后一级审批人且审批通过且是定时任务
	 * @param conn		连接
	 * @param mtTask	任务对象
	 * @param timerTime  定时时间
	 * @return
	 * @throws Exception
	 */
	public String updateAgreeLfMaTask(Connection conn,LfMttask mtTask,String timerTime)  throws Exception{
		String returnStr = "fail";
		try{
			//如果是定时
			if(mtTask.getTimerStatus() == 1){
				SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
				Date date = sdf.parse(timerTime);
				//修改定时发送时间
				mtTask.setTimerTime(new Timestamp(date.getTime()));
				empTransDao.update(conn, mtTask);
			}
			returnStr = "success";
		}catch (Exception e) {
			empTransDao.rollBackTransaction(conn);
			returnStr = "fail";
			EmpExecutionContext.error(e,"更新任务表失败！");
		}
		return returnStr;
	}
	
	/**
	 *  更新当前对象的实时记录  并且如果开启了短信审批提醒功能且是审批通过
	 * @param conn
	 * @param flowRecord
	 * @throws Exception
	 */
	public String updateRecord(Connection conn,LfFlowRecord flowRecord)  throws Exception{
		String returnStr = "fail";
		try{
			//标识该流程完成
			flowRecord.setIsComplete(1);
			empTransDao.update(conn, flowRecord);
			//提交事务
			empTransDao.commitTransaction(conn);
			returnStr = "success";
		}catch (Exception e) {
			empTransDao.rollBackTransaction(conn);
			returnStr = "fail";
			EmpExecutionContext.error(e,"更新当前对象的实时记录失败！");
		}
		return returnStr;
	}
	
	
	/**
	 *   发送短信
	 * @param mtTask 短信任务 
	 * @param biz	计费BIZ
	 * @return
	 * @throws Exception
	 */
	public String SendSms(LfMttask mtTask,BalanceLogBiz biz) throws Exception{
		String returnStr = "";
		try{
			//非定时任务
			if (mtTask.getTimerStatus().intValue() == 0) {
					SendMessage sendMessage = new SendMessage();
					//调用发送接口直接发送,如果失败且开启计费功能，则补回
					try{
						returnStr = sendMessage.sendSms(mtTask, null);
					}catch (Exception e) {
						returnStr = e.getMessage();
						EmpExecutionContext.error(e, "调用发送接口失败！");
					}
			}else{
					TaskManagerBiz tmb=new TaskManagerBiz();
					//获取当前任务定时信息
					List<LfTimer> timerlist=tmb.getTaskByExpression(String.valueOf(mtTask.getTaskId()));
					if(timerlist!=null&&timerlist.size()==0){
						TaskManagerBiz tm = new TaskManagerBiz();
						SendSmsTimerTask sendSmsTimerTask = new SendSmsTimerTask(mtTask
								.getTitle(), 1, new Date(mtTask.getTimerTime()
								.getTime()), 0, String.valueOf(mtTask.getTaskId()));
						sendSmsTimerTask.setMtId(mtTask.getTaskId());
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
						}
					}
			}
		}catch (Exception e) {
			EmpExecutionContext.error(e,"发送短信任务 出现异常！");
			returnStr = "fail";
		}
		return returnStr;
	}
	
	
	
	
	/**
	 *   彩信审批
	 * @param flowRecord   当前审批流程对象
	 * @param rState   审批状态  同意  1  拒绝 2	
	 * @param comments	审批意见
	 * @param timerStatus	定时状态
	 * @param timerTime	定时时间
	 * @param isLastCheck 是否为最后一个审批人
	 * @return
	 * @throws Exception
	 */
	public String examineMMS(LfFlowRecord flowRecord,Integer timerStatus, String timerTime,String isLastCheck) throws Exception{
		String returnStr = "";
		try{
			BalanceLogBiz biz = new BalanceLogBiz();
			String approve="";  //作为审核处理标志 0 表示同意，1表示拒绝
			//查找当前审批的短信任务
			/*LfMttask mtTask = empDao.findObjectByID(LfMttask.class, flowRecord.getMtId());*/
			LfMttask mtTask = new CommonBiz().getLfMttaskbyTaskId(flowRecord.getMtId());
			if(mtTask == null){
			    return "";
			}
			if(mtTask != null && mtTask.getSubState() != null){
				if(mtTask.getSubState() == 3){
					//该任务已被撤销
					return "isrevoke";
				}else if(mtTask.getSubState() == 4){
					//该任务已冻结
					return "freeze";
				}
			}
			//在全部审核生效的情况下      标识的是  该级下是否是最后一个审批人
			String isLevelLastChecker = "no";
			if(flowRecord.getRCondition() == 1){
				isLevelLastChecker = judgeLevelLastChecker(flowRecord);
			}
			boolean iszjLastLevel = false;
			ReviewBiz reviewBiz = new ReviewBiz();
			if(flowRecord.getRType() == 5)
			{
				iszjLastLevel = reviewBiz.checkZhujiIsOver(flowRecord.getPreRv().intValue(), flowRecord.getProUserCode());
			}else{
				iszjLastLevel = true;
			}
			//是否可以发送该短信任务
			boolean isFlagSendSms = false;
			//判断是否下发短信给审批提醒
			String isFlagSendRemind = "nosend";
			//获取一个连接
			Connection conn = empTransDao.getConnection();
			try{
				//同意
				if(flowRecord.getRState() == 1){
					//全部通过生效
					if(flowRecord.getRCondition() == 1){
						//判断是不是该级的最后一个审批人
						if("yes".equals(isLevelLastChecker)){
							//判断是不是最后一级
							if(flowRecord.getRLevel()!=null&&flowRecord.getRLevel().equals(flowRecord.getRLevelAmount()) && iszjLastLevel){
								//是  则发送时处理lfmttask
								returnStr = sendAgreeLfMaTask(conn, mtTask, timerStatus, timerTime);
								
//								new ReviewRemindBiz().ReviewRemindForSender(flowRecord,"");
								if("success".equals(returnStr)){
									approve="0";
									isFlagSendSms = true;
								}
							}else{
								//不是的话则  实例化 下一级
								boolean flag = reviewBiz.addFlowRecordsForNext(conn, flowRecord);
								if(flag){
									returnStr = "success";
								}else{
									return "addChildFail";
								}
								returnStr = updateAgreeLfMaTask(conn, mtTask, timerTime);
								if("success".equals(returnStr)){
									isFlagSendRemind = "yessend";
								}
							}
						}else if("no".equals(isLevelLastChecker)){
							//更新该流程  。
							returnStr = "success";
						}
					//一人通过生效
					}else if(flowRecord.getRCondition() == 2){
						//更新所有审核级别
						returnStr = updateCurrentRecord(conn, flowRecord);
						//更新该等级下的record出错
						if("fail".equals(returnStr)){
							return returnStr;
						}
						//是最后一级的审批人
						if("yes".equals(isLastCheck)){
							//发送时处理lfmttask
							returnStr = sendAgreeLfMaTask(conn, mtTask, timerStatus, timerTime);
							//主要是处理最后一级时候的提示
							if("success".equals(returnStr)){
								isFlagSendSms = true;
								approve="0";
//								new ReviewRemindBiz().ReviewRemindForSender(flowRecord,"");
							}
						}else if("no".equals(isLastCheck)){
							//则实例化 下一级 RECORD
							boolean flag = reviewBiz.addFlowRecordsForNext(conn, flowRecord);
							if(flag){
								returnStr = "success";
							}else{
								return "addChildFail";
							}
							returnStr = updateAgreeLfMaTask(conn, mtTask, timerTime);
							if("success".equals(returnStr)){
								isFlagSendRemind = "yessend";
							}
						}
					}
				//拒绝
				}else if(flowRecord.getRState() == 2){
					//处理更新当前级别的所有状态改为已完成
					returnStr = updateCurrentRecord(conn, flowRecord);
					//更新该等级下的record出错
					if("fail".equals(returnStr)){
						return returnStr;
					}
					//处理短信任务状态   拒绝退费
					returnStr = refuseMmsLfMtTask(conn, mtTask, biz);
					//拒绝时候给出  
					approve="1";
//					new ReviewRemindBiz().ReviewRemindForSender(flowRecord,flowRecord.getRLevel()+"");
				}
				//如果更新状态及扣费操作都成功
				if(returnStr.equals("success")){	
					updateRecord(conn, flowRecord);

				} else {
					//回滚事务
					empTransDao.rollBackTransaction(conn);
					return returnStr;//如果出现错误直接跳出
				}
			}catch (Exception e) {
				//回滚事务
				empTransDao.rollBackTransaction(conn);
				EmpExecutionContext.error(e,"彩信审批出现异常！");
			}finally
			{
				//关闭连接
				empTransDao.closeConnection(conn);
			}
			
			//有可能为空，不做处理 新加的要求，最后发送给第一个发送者信息
			if("0".equals(approve)){//同意
				new ReviewRemindBiz().ReviewRemindForSender(flowRecord,"");
				new ReviewRemindBiz().sendMailForSender(flowRecord,"");
			}else if("1".equals(approve)){//拒绝
				new ReviewRemindBiz().ReviewRemindForSender(flowRecord,flowRecord.getRLevel()+"");
				new ReviewRemindBiz().sendMailForSender(flowRecord,flowRecord.getRLevel()+"");
			}
			
			//判断是否下发短信提醒方法
			this.reviewRemindMsg(flowRecord, isFlagSendRemind);
			//调用邮件发送方法
			this.reviewSendE_mail(flowRecord, isFlagSendRemind);
			//如果是最后可以发送该短信任务了， 则进行发送
			if(isFlagSendSms){
				returnStr = SendMms(mtTask, biz);
			}
		}catch (Exception e) {
			returnStr = "fail";
			EmpExecutionContext.error(e,"彩信审批出现异常！");
		}
		return returnStr;
	}
	
	
	
	/**
	 *   彩信发送
	 * @param mtTask	该条任务
	 * @param biz	计费BIZ
	 * @return
	 * @throws Exception
	 *  返回值信息 			
	 * 		    1:彩信回收成功
	 * 			0:彩信扣费成功
	 * 		   -1:彩信扣费失败
	 * 		   -2:彩信余额不足
	 *  	   -4:用户所属机构没有充值
	 *  	   -5:彩信发送或回收条数不能为空
	 *  	   -8:彩信回收已发送条数异常
	 * 		   -9:彩信回收失败
	 * 		-9999:彩信发送扣费回收接口调用异常
	 */
	public String SendMms(LfMttask mtTask,BalanceLogBiz biz) throws Exception{
		String returnStr = "";
		try{
		    //非定时任务
			if (mtTask.getTimerStatus().intValue() == 0) {
				//直接调用 发送接口
				SendMessage sendMessage = new SendMessage();
				try{
					returnStr = sendMessage.sendMms(mtTask.getMtId());
				}catch (Exception e) {
					EmpExecutionContext.error(e,"提交彩信任务失败！");
				}
				//获取一个连接
				Connection connTemp = empTransDao.getConnection();
				try{
					//发送失败
					if(!"000".equals(returnStr)){
						//判断当前任务的所属操作员的所属机构是否开启计费功能
						if(SystemGlobals.isDepBilling(mtTask.getUserId())){
						    //开启一个事务
						    empTransDao.beginTransaction(connTemp);
	                        //调用 彩信扣费回收操作
							Integer str= biz.sendMmsAmountByUserId(connTemp, mtTask.getUserId(), mtTask.getEffCount()*-1);
		                    if(str != null && str == 1)
		                    {
		            			empTransDao.commitTransaction(connTemp);
							} else {
								empTransDao.rollBackTransaction(connTemp);
								returnStr = "feefail";
							}
						}
					}
				} catch (Exception e) {
					// 回滚事务
					empTransDao.rollBackTransaction(connTemp);
					EmpExecutionContext.error(e,"彩信扣费出现异常！");
				} finally {
					// 关闭连接
					if (connTemp != null) {
						empTransDao.closeConnection(connTemp);
					}
				}
			} else {
				//定时时间大于当前时间，则设置定时任务
				if ((mtTask.getTimerTime().getTime() + 60*1000) > Calendar.getInstance().getTime().getTime()) {
					TaskManagerBiz tm = new TaskManagerBiz();
					SendMmsTimerTask sendMmsTimerTask = new SendMmsTimerTask(
							mtTask.getTitle(), 1, new Date(mtTask
									.getTimerTime().getTime()), 0, String
									.valueOf(mtTask.getTaskId()));
					sendMmsTimerTask.setTaskId(mtTask.getTaskId());
					boolean flag = tm.setJob(sendMmsTimerTask);
					if (flag) {
						returnStr = "timerSuccess";
					} else {
						returnStr = "timerFail";
					}
				}
			}
		}catch (Exception e) {
			returnStr = "fail";
			EmpExecutionContext.error(e,"提交彩信任务失败！");
		}
		return returnStr;
	}
	
	
	
	
	/**
	 *    彩信拒绝
	 * @param conn	连接
	 * @param mtTask	任务 
	 * @param biz	
	 * @return
	 * @throws Exception
	 */
	public String refuseMmsLfMtTask(Connection conn,LfMttask mtTask,BalanceLogBiz biz)  throws Exception{
		String returnStr = "fail";
		try{
			//拒绝操作
			mtTask.setReState(2);
			empTransDao.update(conn, mtTask);
			//判断当前任务所属操作员所属机构是否开启计费功能
			if(SystemGlobals.isDepBilling(mtTask.getUserId())){
				//拒绝则需补回扣费记录
				Integer str= biz.sendMmsAmountByUserId(conn, mtTask.getUserId(), mtTask.getEffCount()*-1);
				if (str != null && str == 1) {
					returnStr = "success";
				} else {
					returnStr = "feefail";
				}
			}else{
				returnStr = "success";
			}
		}catch (Exception e) {
			returnStr = "fail";
			EmpExecutionContext.error(e,"审批拒绝彩信任务操作出现异常！");
		}
		return returnStr;
	}
	
	
	
	/**
	 *   调用短信提醒
	 * @param flowRecord	审核实时记录
	 * @param isFlagSendRemind	是否可以下发
	 */
	public void reviewRemindMsg(LfFlowRecord flowRecord,String isFlagSendRemind){
		try{
			String isre=SystemGlobals.getSysParam("isRemind");
			if("0".equals(isre)&& flowRecord.getRState() == 1 && "yessend".equals(isFlagSendRemind)){
	    		//调用短信审批提醒功能
				new ReviewRemindBiz().flowReviewRemind(flowRecord);
	    	}
		}catch (Exception e) {
			EmpExecutionContext.error(e,"调用短信提醒出现异常！");
		}
	}
	
	/**
	 *   调用邮件提醒
	 * @param flowRecord	审核实时记录
	 * @param isFlagSendRemind	是否可以下发
	 */
	public void reviewSendE_mail(LfFlowRecord flowRecord,String isFlagSendRemind){
		try{
//			if(flowRecord.getRState() == 1 && "yessend".equals(isFlagSendRemind)){
	    		//调用短信审批提醒功能
				new ReviewRemindBiz().sendMail(flowRecord);
//	    	}
		}catch (Exception e) {
			EmpExecutionContext.error(e,"调用短信提醒出现异常！");
		}
	}
	
	
	
	
	
	
	
	
	
	
	

}
