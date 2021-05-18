package com.montnets.emp.engine.biz;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.beanutils.DynaBean;

import com.montnets.emp.common.biz.BalanceLogBiz;
import com.montnets.emp.common.biz.CommonBiz;
import com.montnets.emp.common.biz.GlobalVariableBiz;
import com.montnets.emp.common.biz.SmsBiz;
import com.montnets.emp.common.biz.SuperBiz;
import com.montnets.emp.common.constant.ErrorCodeParam;
import com.montnets.emp.common.constant.StaticValue;
import com.montnets.emp.common.constant.SystemGlobals;
import com.montnets.emp.common.context.EmpExecutionContext;
import com.montnets.emp.engine.dao.MoHandleDao;
import com.montnets.emp.entity.datasource.LfDBConnect;
import com.montnets.emp.entity.engine.LfAutoreply;
import com.montnets.emp.entity.engine.LfMoService;
import com.montnets.emp.entity.engine.LfProCon;
import com.montnets.emp.entity.engine.LfProcess;
import com.montnets.emp.entity.engine.LfReply;
import com.montnets.emp.entity.engine.LfService;
import com.montnets.emp.entity.pasgroup.Userdata;
import com.montnets.emp.entity.sms.LfMttask;
import com.montnets.emp.entity.system.LfMotask;
import com.montnets.emp.entity.system.LfSysmttask;
import com.montnets.emp.entity.sysuser.LfSysuser;
import com.montnets.emp.smstask.HttpSmsSend;
import com.montnets.emp.smstask.WGParams;
import com.montnets.emp.util.CheckUtil;
import com.montnets.emp.util.JDBCUtil;
import com.montnets.emp.util.TxtFileUtil;

/**
 * 
 * @author Administrator
 * 
 */
public class MoAutoHandleBiz extends SuperBiz
{
	
	/**
	 * MoServideStart方法通过调用这个方法处理上行上来的信息
	 * @param moTask
	 * @throws Exception
	 */
	public boolean RunUpServiceAccess(LfMotask moTask)
	{
		try
		{
			EmpExecutionContext.info("上行业务接收到上行，尾号，处理开始。"
					+ "momsgid="+moTask.getPtMsgId()
					+ ",phone="+moTask.getPhone()
					+ ",spuser="+moTask.getSpUser()
					+ ",spnumber="+moTask.getSpnumber()
					+ ",exno="+moTask.getSubno()
					+ ",msg="+moTask.getMsgContent()
					+ ",cmdid="+moTask.getMoOrder()	
					+ ",menuCode="+moTask.getMenuCode()	
					+ ",depId="+moTask.getDepId()
					+ ",taskId="+moTask.getTaskId()	
					+ ",busCode="+moTask.getBusCode()
					+ ",corpCode="+moTask.getCorpCode()
			);
			//上行服务biz
			MoServiceBiz upSerBiz = new MoServiceBiz();
			// 通过指令代码获取服务业务
			LfService service = upSerBiz.getUpServiceByOrderCode(moTask.getMsgContent(), moTask.getCorpCode());
			
			boolean runResult = this.runMoSer(moTask, service);
			
			EmpExecutionContext.info("上行业务接收到上行，尾号，处理结束。" 
					+ "结果："+runResult
					+ ",momsgid="+moTask.getPtMsgId()
					+ ",phone="+moTask.getPhone()
					+ ",spuser="+moTask.getSpUser()
					+ ",spnumber="+moTask.getSpnumber()
					+ ",exno="+moTask.getSubno()
					+ ",msg="+moTask.getMsgContent()
					+ ",cmdid="+moTask.getMoOrder()	
					+ ",menuCode="+moTask.getMenuCode()	
					+ ",depId="+moTask.getDepId()
					+ ",taskId="+moTask.getTaskId()	
					+ ",busCode="+moTask.getBusCode()
					+ ",corpCode="+moTask.getCorpCode()		
			);
			
			return runResult;
		} 
		catch (Exception e)
		{
			//捕获异常
			EmpExecutionContext.error(e, "上行业务接收到上行，尾号，异常。"
					+ "momsgid="+moTask.getPtMsgId()
					+ ",phone="+moTask.getPhone()
					+ ",spuser="+moTask.getSpUser()
					+ ",spnumber="+moTask.getSpnumber()
					+ ",exno="+moTask.getSubno()
					+ ",msg="+moTask.getMsgContent()
					+ ",cmdid="+moTask.getMoOrder()	
					+ ",menuCode="+moTask.getMenuCode()	
					+ ",depId="+moTask.getDepId()
					+ ",taskId="+moTask.getTaskId()	
					+ ",busCode="+moTask.getBusCode()
					+ ",corpCode="+moTask.getCorpCode()		
			);
			return false;
		}
	}

	/**
	 * MoServideStart方法通过调用这个方法处理上行上来的信息
	 * @param moTask
	 * @throws Exception
	 */
	public boolean RunMoSerByOrderCode(LfMotask moTask)
	{
		try
		{
			EmpExecutionContext.info("上行业务，指令，处理开始。ptMsgId="+moTask.getPtMsgId());
			// 通过指令代码获取服务业务
			LfService service = this.getSerByOrder(moTask);
			
			boolean runResult = this.runMoSer(moTask, service);
			
			EmpExecutionContext.info("上行业务，指令，处理结束。ptMsgId="+moTask.getPtMsgId()+",结果："+runResult);
			
			return runResult;
		} catch (Exception e)
		{
			//捕获异常
			EmpExecutionContext.error(e,"上行业务处理上行信息异常。ptMsgId="+moTask.getPtMsgId());
			return false;
		}
	}
	
	/**
	 * 运行上行业务
	 * @param moTask 上行信息对象
	 * @param service 上行业务对象
	 * @return 成功返回true
	 */
	private boolean runMoSer(LfMotask moTask, LfService service)
	{
		try
		{
			//上行服务对象
			LfMoService moService = new LfMoService();
			moService.setPhone(moTask.getPhone());
			moService.setSpnumber(moTask.getSpnumber());
			moService.setMsgContent(moTask.getMsgContent());
			moService.setSpUser(moTask.getSpUser());
			//回复状态，1-成功，2和空-未回复，3-失败
			moService.setReplyState(2);
			moService.setCorpCode(moTask.getCorpCode());
			Long serMoId = null;
			// 实际流程是如果找不到service就return,因为process一定为空
			if (service == null)
			{
				EmpExecutionContext.info("执行上行业务，业务对象为null。"
						+ "momsgid="+moTask.getPtMsgId()
						+ ",phone="+moTask.getPhone()
						+ ",spuser="+moTask.getSpUser()
						+ ",spnumber="+moTask.getSpnumber()
						+ ",exno="+moTask.getSubno()
						+ ",msg="+moTask.getMsgContent()
						+ ",cmdid="+moTask.getMoOrder()	
						+ ",menuCode="+moTask.getMenuCode()	
						+ ",depId="+moTask.getDepId()
						+ ",taskId="+moTask.getTaskId()	
						+ ",busCode="+moTask.getBusCode()
						+ ",corpCode="+moTask.getCorpCode()
				);
				
				//保存到数据库
				serMoId = empDao.saveObjectReturnID(moService);
				
				//获取配置的自动回复
				String msg = this.getAutoReply(moTask.getCorpCode());
				
				if(msg == null)
				{
					return false;
				}
				else
				{
					this.sendErrorMsg(moTask.getPhone(), msg, moTask.getSpUser(), moTask.getCorpCode(), moTask.getSubno());
				}
				
				EmpExecutionContext.info("执行上行业务，未找到对应的业务对象。" 
						+ "momsgid="+moTask.getPtMsgId()
						+ ",phone="+moTask.getPhone()
						+ ",spuser="+moTask.getSpUser()
						+ ",spnumber="+moTask.getSpnumber()
						+ ",exno="+moTask.getSubno()
						+ ",msg="+moTask.getMsgContent()
						+ ",cmdid="+moTask.getMoOrder()	
						+ ",menuCode="+moTask.getMenuCode()	
						+ ",depId="+moTask.getDepId()
						+ ",taskId="+moTask.getTaskId()	
						+ ",busCode="+moTask.getBusCode()
						+ ",corpCode="+moTask.getCorpCode()
						+ ",serMoId：" + serMoId
				);
				
				return false;
			}
			
			EmpExecutionContext.info("执行上行业务，业务信息。" 
					+ "momsgid="+moTask.getPtMsgId()
					+ ",phone="+moTask.getPhone()
					+ ",spuser="+moTask.getSpUser()
					+ ",spnumber="+moTask.getSpnumber()
					+ ",exno="+moTask.getSubno()
					+ ",msg="+moTask.getMsgContent()
					+ ",cmdid="+moTask.getMoOrder()	
					+ ",menuCode="+moTask.getMenuCode()	
					+ ",depId="+moTask.getDepId()
					+ ",taskId="+moTask.getTaskId()	
					+ ",busCode="+moTask.getBusCode()
					+ ",corpCode="+moTask.getCorpCode()
					+ ",serId=" + service.getSerId()
					+ ",userId=" + service.getUserId()
					+ ",serName=" + service.getSerName()
					+ ",commnets=" + service.getCommnets()
					+ ",orderCode=" + service.getOrderCode()
					+ ",subNo=" + service.getSubNo()
					+ ",spUser=" + service.getSpUser()
					+ ",runState=" + service.getRunState()
					+ ",serType=" + service.getSerType()
					+ ",msgSeparated=" + service.getMsgSeparated()
					+ ",ownerId=" + service.getOwnerId()
					+ ",createTime=" + service.getCreateTime()
					+ ",busCode=" + service.getBusCode()
					+ ",menuCode=" + service.getMenuCode()
					+ ",corpCode=" + service.getCorpCode()
					+ ",identifyMode=" + service.getIdentifyMode()
					+ ",orderType=" + service.getOrderType()
					+ ",structcode=" + service.getStructcode()
			);
			
			//设进serid
			moService.setSerId(service.getSerId());
			//保存到数据库
			serMoId = empDao.saveObjectReturnID(moService);
			if(serMoId == null || serMoId < 0)
			{
				EmpExecutionContext.error("执行上行业务，保存记录失败。"
						+ "SerId="+service.getSerId()
						+ ",momsgid="+moTask.getPtMsgId()
						+ ",phone="+moTask.getPhone()
						+ ",spuser="+moTask.getSpUser()
						+ ",spnumber="+moTask.getSpnumber()
						+ ",exno="+moTask.getSubno()
						+ ",msg="+moTask.getMsgContent()
						+ ",cmdid="+moTask.getMoOrder()	
						+ ",menuCode="+moTask.getMenuCode()	
						+ ",depId="+moTask.getDepId()
						+ ",taskId="+moTask.getTaskId()	
						+ ",busCode="+moTask.getBusCode()
						+ ",corpCode="+moTask.getCorpCode()
				);
			}
			else
			{
				EmpExecutionContext.info("执行上行业务，保存记录成功。"
						+ "SerId="+service.getSerId()
						+ ",momsgid="+moTask.getPtMsgId()
						+ ",phone="+moTask.getPhone()
						+ ",spuser="+moTask.getSpUser()
						+ ",spnumber="+moTask.getSpnumber()
						+ ",exno="+moTask.getSubno()
						+ ",msg="+moTask.getMsgContent()
						+ ",cmdid="+moTask.getMoOrder()	
						+ ",menuCode="+moTask.getMenuCode()	
						+ ",depId="+moTask.getDepId()
						+ ",taskId="+moTask.getTaskId()	
						+ ",busCode="+moTask.getBusCode()
						+ ",corpCode="+moTask.getCorpCode()	
						+ ",serMoId=" + serMoId
				);
			}
			
			//业务为停止，则不运行
			if (service != null && service.getRunState() == 0)
			{
				EmpExecutionContext.info("执行上行业务，运行状态为停止，取消执行。"
						+ "SerId="+service.getSerId()
						+ ",momsgid="+moTask.getPtMsgId()
						+ ",phone="+moTask.getPhone()
						+ ",spuser="+moTask.getSpUser()
						+ ",spnumber="+moTask.getSpnumber()
						+ ",exno="+moTask.getSubno()
						+ ",msg="+moTask.getMsgContent()
						+ ",cmdid="+moTask.getMoOrder()	
						+ ",menuCode="+moTask.getMenuCode()	
						+ ",depId="+moTask.getDepId()
						+ ",taskId="+moTask.getTaskId()	
						+ ",busCode="+moTask.getBusCode()
						+ ",corpCode="+moTask.getCorpCode()	
						+ ",serMoId=" + serMoId
				);
				return false;
			}
			
			//运行任务
			boolean resRun = this.runUpServJob(service, moTask.getPhone(), moTask.getMsgContent(), serMoId);
			
			return resRun;
		} 
		catch (Exception e)
		{
			//捕获异常
			EmpExecutionContext.error(e, "执行上行业务，处理上行信息异常。"
					+ ",momsgid="+moTask.getPtMsgId()
					+ ",phone="+moTask.getPhone()
					+ ",spuser="+moTask.getSpUser()
					+ ",spnumber="+moTask.getSpnumber()
					+ ",exno="+moTask.getSubno()
					+ ",msg="+moTask.getMsgContent()
					+ ",cmdid="+moTask.getMoOrder()	
					+ ",menuCode="+moTask.getMenuCode()	
					+ ",depId="+moTask.getDepId()
					+ ",taskId="+moTask.getTaskId()	
					+ ",busCode="+moTask.getBusCode()
					+ ",corpCode="+moTask.getCorpCode()	
			);
			return false;
		}
	}

	/**
	 * 获取自动回复短信内容
	 * @param corpCode 企业编码
	 * @return 返回短信内容，无需发送则返回null
	 */
	private String getAutoReply(String corpCode)
	{
		try
		{
			LinkedHashMap<String, String> conditionMap = new LinkedHashMap<String, String>();
			//状态：1-启用；2-关闭
			conditionMap.put("state", "1");
			//回复类型：1-无效指令回复
			conditionMap.put("type", "1");
			conditionMap.put("corpcode", corpCode);
			List<LfAutoreply> replysList = empDao.findListByCondition(LfAutoreply.class, conditionMap, null);
			if(replysList != null && replysList.size() > 0)
			{
				return replysList.get(0).getReplycontent();
			}
			return null;
		}
		catch (Exception e)
		{
			EmpExecutionContext.error(e, "上行业务处理上行信息，指令未找到下发短信，获取下发短信配置异常。"
					+ "corpCode=" + corpCode
			);
			return null;
		}
	}
	
	/**
	 * 找不到上行业务，下发提醒短信
	 * @param phone 手机号码
	 * @param msg 短信内容
	 * @param spUser 发送账号
	 * @param corpCode 企业编码
	 * @param subno 尾号
	 * @return
	 */
	private boolean sendErrorMsg(String phone, String msg, String spUser, String corpCode, String subno) 
	{
		EmpExecutionContext.info("上行业务发送通知短信，参数信息。" 
				+ "phone=" + phone
				+ ",msg=" + msg
				+ ",spUser=" + spUser
				+ ",corpCode=" + corpCode
				+ ",subno=" + subno
		);
		
		BalanceLogBiz balanceBiz = new BalanceLogBiz();
		//系统发送记录
		LfSysmttask sysMttask = new LfSysmttask();
		//是否为异常错误 :false不是  true是
		boolean isDepFeeFlag = SystemGlobals.isDepBilling(corpCode);
		//Connection conn = null;
		
		LfSysuser admin = getAdminInCorp(corpCode);
		if(admin == null)
		{
			EmpExecutionContext.error("上行业务发送通知短信，根据企业编码获取不到企业管理员。" 
					+ "phone=" + phone
					+ ",msg=" + msg
					+ ",spUser=" + spUser
					+ ",corpCode=" + corpCode
					+ ",subno=" + subno
			);
			return false;
		}
		//统计预发送条数
		Integer icount = 0;
		try
		{
			SmsBiz smsBiz = new SmsBiz();
			//统计预发送条数
			icount = smsBiz.countAllOprSmsNumber(spUser, msg, 1, null, phone);
			
			//conn = empTransDao.getConnection();
			//empTransDao.beginTransaction(conn);
			//如果开启机构计费，则补回机构费用
			if(isDepFeeFlag)
			{
			// 机构扣费
				int depResult = balanceBiz.sendSmsAmountByUserId(null, admin.getUserId(), icount);
				// -3：不存在机构信息 ，-2：余额不足 ，-1:扣费失败，0：成功
				if(depResult < 0)
				{
					EmpExecutionContext.error("上行业务发送通知短信，机构扣费失败。"
							+ "phone=" + phone
							+ ",msg=" + msg
							+ ",spUser=" + spUser
							+ ",corpCode=" + corpCode
							+ ",subno=" + subno	
							+ ",机构扣费返回=" + depResult
					);
					return false;
				}
			}
			
			//SP账号余额检查
			int spResult = balanceBiz.checkSpUserFee(spUser, icount, 1);
			//余额不足或者其他错误情况则不能再继续流程
			if(spResult < 0)
			{
				EmpExecutionContext.error("上行业务发送通知短信，SP账号余额不足。"
						+ "phone=" + phone
						+ ",msg=" + msg
						+ ",spUser=" + spUser
						+ ",corpCode=" + corpCode
						+ ",subno=" + subno	
						+ ",icount=" + icount
						+ ",SP账号余额检查返回=" + spResult
				);
				return false;
			}
			
			//扣费并接收返回状态
			String wgresult = balanceBiz.checkGwFee(spUser, icount, corpCode,true,1);
			if("nogwfee".equals(wgresult) || "feefail".equals(wgresult) || "lessgwfee".equals(wgresult.substring(0, 9)))
			{
				EmpExecutionContext.error("上行业务发送通知短信，获取运营商余额失败。"
						+ "phone=" + phone
						+ ",msg=" + msg
						+ ",spUser=" + spUser
						+ ",corpCode=" + corpCode
						+ ",subno=" + subno
						+ ",icount=" + icount
						+ ",wgresult=" + wgresult
				);
				return false;
			}
			
			//设置初值为发送失败，后续发送成功时则更改为3
			sysMttask.setTitle("智能引擎上行业务指令未找到回复");
			//发送状态。0是未提交网关，1提交网关成功,2提交网关失败,3网关处理完成
			sysMttask.setSendState(1);
			sysMttask.setUserid(admin.getUserId());
			sysMttask.setSpuser(spUser);
			sysMttask.setPhone(phone);
			sysMttask.setMsg(msg);
			//发送类型（1-文件发送，2-单发）
			sysMttask.setSendtype(2);
			sysMttask.setSubmittime(new Timestamp(System.currentTimeMillis()));
			sysMttask.setSubcount(1);
			//群发类型（相同1，不同2，动模3）
			sysMttask.setBmttype(1);
			//信息类型（1-审批提醒；2-上行业务回复；3-找回密码；4-登录动态口令；5-机构余额阀值提醒）
			sysMttask.setMstype(2);
			sysMttask.setCorpcode(corpCode);
			//任务ID
			CommonBiz commonBiz = new CommonBiz();
			Long taskId = commonBiz.getAvailableTaskId();
			sysMttask.setTaskId(taskId);
			sysMttask.setBusCode("M00000");
			sysMttask.setUserCode(admin.getUserCode());
			//empTransDao.save(conn, sysMttask);
			empDao.save(sysMttask);
			
			//empTransDao.commitTransaction(conn);
		}
		catch (Exception e)
		{
			//empTransDao.rollBackTransaction(conn);
			EmpExecutionContext.error(e, "上行业务发送通知短信，保存任务记录异常。"
					+ "phone=" + phone
					+ ",msg=" + msg
					+ ",spUser=" + spUser
					+ ",corpCode=" + corpCode
					+ ",subno=" + subno		
			);
			return false;
		}
		finally
		{
			//empTransDao.closeConnection(conn);
		}
		
		//发送短信
		boolean sendRes = this.sendSysMsg(sysMttask, subno);
		
		//发送失败，补费
		if(!sendRes)
		{
			//机构补费结果返回值
			int depFeeHuiRes = 1;
			//有开启机构计费，则要补费
			if(isDepFeeFlag)
			{
				depFeeHuiRes = balanceBiz.sendSmsAmountByUserId(null, admin.getUserId(), icount*-1);
			}
			//运营商补费
			balanceBiz.huishouFee(icount, spUser, 1);
			EmpExecutionContext.info("上行业务发送通知短信，发送失败后补费。"
					+ "phone=" + phone
					+ ",msg=" + msg
					+ ",spUser=" + spUser
					+ ",corpCode=" + corpCode
					+ ",subno=" + subno	
					+ ",icount=" + icount
					+ ",机构补费返回=" + depFeeHuiRes
			);
		}
		
		return sendRes;
	}
	
	/**
	 * 发送短信
	 * @param sysMttask 任务对象
	 * @param subno 尾号
	 * @return
	 */
	private boolean sendSysMsg(LfSysmttask sysMttask, String subno)
	{
		String wgcode = null;
		boolean returnRes = false;
		try 
		{
			HttpSmsSend smsSend = new HttpSmsSend();
			WGParams params = new WGParams();
			//发送账号
			params.setSpid(sysMttask.getSpuser());
			
			//获取发送账号对象
            Userdata userData = this.getSpUser(sysMttask.getSpuser());
            if(userData!=null){
            //设置发送账号密码
			params.setSppassword(userData.getUserPassword());
            }
			//相同内容是1，不同内容是2
			params.setBmttype(sysMttask.getBmttype().toString());
			//任务id
			params.setTaskid(sysMttask.getTaskId().toString());
			//短信主题
			params.setTitle(sysMttask.getTitle());
			//设置发送优先级
			params.setPriority("1");  
			//p1参数，传操作员的内部用户编码，这里设为admin
			params.setParam1(sysMttask.getUserCode());
			//业务类型，传默认业务
			params.setSvrtype(sysMttask.getBusCode());
			//是否需要状态报告
			params.setRptflag("0");
			//设置尾号，1：操作员固定尾号，2：回复本次任务
			//params.setSa(lfMttask.getSubNo());
			//设置发送请求为号码群发，不是文件群发
			params.setCommand("MULTI_MT_REQUEST");
			//设置短信内容
			params.setSm(sysMttask.getMsg());
			//发送号码
			params.setDas(sysMttask.getPhone());
			
			//有尾号，则设置尾号
			if(subno != null && subno.trim().length() > 0 && !"*".equals(subno) )
			{
				params.setSa(subno);
			}
		
			//不是相同内容发送，或是相同内容发送且有效号码数大于50这采用文件发送
			//设置文件地址
			//params.setUrl(lfMttask.getMobileUrl());
			//设置发送内容
			//params.setContent(lfMttask.getMsg());
			
            //调用发送接口 
			String wgresult = smsSend.createbatchMtRequest(params);
			if(wgresult == null || wgresult.trim().length() < 1)
			{
				EmpExecutionContext.info("上行业务发送通知短信，调用发送接口，返回结果为空。"
						+ "wgresult=" + wgresult
						+ ",spUser=" + sysMttask.getSpuser()
						+ ",Bmttype=" + sysMttask.getBmttype()
						+ ",TaskId=" + sysMttask.getTaskId()
						+ ",Title=" + sysMttask.getTitle()
						+ ",UserCode=" + sysMttask.getUserCode()
						+ ",BusCode=" + sysMttask.getBusCode()
						+ ",Msg=" + sysMttask.getMsg()
						+ ",Phone=" + sysMttask.getPhone()
						+ ",subno=" + subno
				);
				return false;
			}
			
			EmpExecutionContext.info("上行业务发送通知短信，调用发送接口。"
					+ "wgresult=" + wgresult
					+ ",spUser=" + sysMttask.getSpuser()
					+ ",Bmttype=" + sysMttask.getBmttype()
					+ ",TaskId=" + sysMttask.getTaskId()
					+ ",Title=" + sysMttask.getTitle()
					+ ",UserCode=" + sysMttask.getUserCode()
					+ ",BusCode=" + sysMttask.getBusCode()
					+ ",Msg=" + sysMttask.getMsg()
					+ ",Phone=" + sysMttask.getPhone()
					+ ",subno=" + subno
			);
			
			//截取网关返回状态码
			int index = wgresult.indexOf("mterrcode");
			wgcode = wgresult.substring(index + 10, index + 13);
			
			//将返回状态记录到文件里
			EmpExecutionContext.requestInfo(sysMttask.getTaskId(), wgresult);
		}
		catch (Exception e) 
		{
			EmpExecutionContext.error(e,"上行业务发送通知短信，调用发送接口，异常。"
					+ "spUser=" + sysMttask.getSpuser()
					+ ",Bmttype=" + sysMttask.getBmttype()
					+ ",TaskId=" + sysMttask.getTaskId()
					+ ",Title=" + sysMttask.getTitle()
					+ ",UserCode=" + sysMttask.getUserCode()
					+ ",BusCode=" + sysMttask.getBusCode()
					+ ",Msg=" + sysMttask.getMsg()
					+ ",Phone=" + sysMttask.getPhone()
					+ ",subno=" + subno
			);
		} 
		finally 
		{
			if("000".equals(wgcode))
			{
				returnRes = true;
			}
			else
			{
				//发送状态。0是未提交网关，1提交网关成功,2提交网关失败,3网关处理完成
				sysMttask.setSendState(2);
			}
			sysMttask.setErrorCodes(wgcode);
			this.updateSendAfter(sysMttask);
		}
		return returnRes;
	}

	/**
	 * 发送后更新任务对象
	 * @param sysMttask 任务对象
	 * @return 成功更新返回true
	 */
	private boolean updateSendAfter(LfSysmttask sysMttask){
		int a = 1;
		boolean updateRes = false;
		//如果多次更新失败，则重复更新
		while (a<4) {
			try {
				if(updateMttaskByTaskid(sysMttask))
				{
					a=4;
					updateRes = true;
				}
				else 
				{
					a++;
				}
			} catch (Exception e2) {
				try {
					Thread.sleep(500L);
				} catch (Exception e3) {
					EmpExecutionContext.error(e3, "上行业务发送后更新任务对象，睡眠异常。");
				}finally
				{
					EmpExecutionContext.error(e2,"更新任务表失败：第"+a+"次");
					a++;
				}
			}
		}
		return updateRes;
	}
	
	/**
	 * 获取企业管理员
	 * @param corpCode 企业编码
	 * @return 返回企业管理员对象，没有或异常返回null
	 */
	private LfSysuser getAdminInCorp(String corpCode){
		try {
			LinkedHashMap<String, String> conditionMap = new LinkedHashMap<String, String>();
			//企业编码
			conditionMap.put("corpCode", corpCode);
			conditionMap.put("userName","admin");
		
			//admin的用户信息
			List<LfSysuser> sysUsersList = empDao.findListByCondition(LfSysuser.class, conditionMap,null );
			if(sysUsersList != null && sysUsersList.size() > 0){
				return sysUsersList.get(0);
			}
			return null;
		} catch (Exception e1) {
			EmpExecutionContext.error(e1, "查询操作员失败！");
			return null;
		}
	}
	
	/**
	 * 获取发送账号对象
	 * @param spUser
	 * @return
	 */
	private Userdata getSpUser(String spUser){
		try
		{
			//加载过滤条件
			LinkedHashMap<String, String> conditionMap = new LinkedHashMap<String, String>();
			conditionMap.put("userId", spUser);
			//SP账户类型   1:短信SP账号;2:彩信SP账号
			conditionMap.put("accouttype", "1");
			//调用查询方法获取发送账号
			List<Userdata> tempList = empDao.findListByCondition(Userdata.class, conditionMap, null);
			
			if (tempList != null && tempList.size() > 0) {
				return tempList.get(0);
			}else{
				EmpExecutionContext.error("上行业务处理上行信息，指令未找到下发短信，spuser找不到。spUser："+spUser);
				return null;
			}
		}
		catch (Exception e)
		{
			EmpExecutionContext.error(e, "上行业务处理上行信息，指令未找到下发短信，获取发送账号异常。spUser："+spUser);
			return null;
		}
		
	}
	
	/**
	 * @description 发送完成后更新任务表
	 * @param mt 任务对象
	 * @return true-修改成功,false-修改失败
	 * @throws Exception       			
	 */
	private boolean updateMttaskByTaskid(LfSysmttask sysMttask)
	{
		try
		{
			LinkedHashMap<String, String> conditionMap = new LinkedHashMap<String, String>();
			LinkedHashMap<String, String> objectMap = new LinkedHashMap<String, String>();
			
			conditionMap.put("taskId", sysMttask.getTaskId().toString());
			objectMap.put("sendState", sysMttask.getSendState().toString());
			
			objectMap.put("errorCodes", sysMttask.getErrorCodes());
			
			return empDao.update(LfSysmttask.class, objectMap, conditionMap);
		}
		catch (Exception e)
		{
			EmpExecutionContext.error(e, "上行业务处理上行信息，指令未找到下发短信，下发失败后更新，更新异常。taskid:"+sysMttask.getTaskId());
			return false;
		}
	}
	
	/**
	 * 根据指令id和发送账号获取对应的上行业务对象
	 * @param moTask 上行信息对象
	 * @return 返回指令和发送账户对应的上行业务对象
	 */
	private LfService getSerByOrder(LfMotask moTask){

		return new MoHandleDao().getSerByOrder(moTask);
	}
	
	/**
	 * 执行上行业务
	 * @param service
	 * @param process
	 * @param phone
	 * @param msg
	 * @throws Exception
	 */
	private boolean runUpServJob(LfService service, String phone, String msg, Long serMoId)
	{
		if(service == null)
		{
			EmpExecutionContext.error("上行业务执行失败，服务对象为null。" 
					+ "serMoId=" + serMoId
					+ ",phone=" + phone
					+ ",msg=" + msg
			);
			return false;
		}
		
		//服务日志biz
		AppLogBiz serLogBiz = new AppLogBiz();
		//步骤配置biz
		ProcessConfigBiz processBiz = new ProcessConfigBiz();
		//日志id
		Long slId = null;
		try
		{
			//获取该服务id下的所有步骤集合
			List<LfProcess> processesList = processBiz.getProcescBySerId(service.getSerId());
			
			//没步骤
			if (processesList == null || processesList.size() == 0)
			{
				EmpExecutionContext.error("执行上行业务，服务配置不正确。"
						+ "serId=" + service.getSerId()
						+ ",userId=" + service.getUserId()
						+ ",serName=" + service.getSerName()
						+ ",commnets=" + service.getCommnets()
						+ ",orderCode=" + service.getOrderCode()
						+ ",subNo=" + service.getSubNo()
						+ ",spUser=" + service.getSpUser()
						+ ",runState=" + service.getRunState()
						+ ",serType=" + service.getSerType()
						+ ",msgSeparated=" + service.getMsgSeparated()
						+ ",ownerId=" + service.getOwnerId()
						+ ",createTime=" + service.getCreateTime()
						+ ",busCode=" + service.getBusCode()
						+ ",menuCode=" + service.getMenuCode()
						+ ",corpCode=" + service.getCorpCode()
						+ ",identifyMode=" + service.getIdentifyMode()
						+ ",orderType=" + service.getOrderType()
						+ ",structcode=" + service.getStructcode()	
						+ ",serMoId=" + serMoId
						+ ",phone=" + phone
						+ ",msg=" + msg
				);
				//记录日志并返回，4为服务配置不正确
				serLogBiz.addNewServiceLog(4, service.getSerId(), "");
				return false;
			}
			//获取短信分隔符
			String msgSeparated = service.getMsgSeparated();
			
			//获取创建者
			LfSysuser sysuser = empDao.findObjectByID(LfSysuser.class, service.getUserId());
			if (sysuser == null)
			{
				//没获取到创建者则报异常返回否
				EmpExecutionContext.error("执行上行业务，获取操作员对象为空。"
						+ "serId=" + service.getSerId()
						+ ",userId=" + service.getUserId()
						+ ",serName=" + service.getSerName()
						+ ",commnets=" + service.getCommnets()
						+ ",orderCode=" + service.getOrderCode()
						+ ",subNo=" + service.getSubNo()
						+ ",spUser=" + service.getSpUser()
						+ ",runState=" + service.getRunState()
						+ ",serType=" + service.getSerType()
						+ ",msgSeparated=" + service.getMsgSeparated()
						+ ",ownerId=" + service.getOwnerId()
						+ ",createTime=" + service.getCreateTime()
						+ ",busCode=" + service.getBusCode()
						+ ",menuCode=" + service.getMenuCode()
						+ ",corpCode=" + service.getCorpCode()
						+ ",identifyMode=" + service.getIdentifyMode()
						+ ",orderType=" + service.getOrderType()
						+ ",structcode=" + service.getStructcode()	
						+ ",serMoId=" + serMoId
						+ ",phone=" + phone
						+ ",msg=" + msg		
				);
				return false;
			} 
			else if (sysuser.getUserState() != 1)
			{
				// 禁用的状态
				EmpExecutionContext.info("执行上行业务，操作员状态为禁用/注销，不允许执行。"
						+ "serId=" + service.getSerId()
						+ ",userId=" + service.getUserId()
						+ ",serName=" + service.getSerName()
						+ ",commnets=" + service.getCommnets()
						+ ",orderCode=" + service.getOrderCode()
						+ ",subNo=" + service.getSubNo()
						+ ",spUser=" + service.getSpUser()
						+ ",runState=" + service.getRunState()
						+ ",serType=" + service.getSerType()
						+ ",msgSeparated=" + service.getMsgSeparated()
						+ ",ownerId=" + service.getOwnerId()
						+ ",createTime=" + service.getCreateTime()
						+ ",busCode=" + service.getBusCode()
						+ ",menuCode=" + service.getMenuCode()
						+ ",corpCode=" + service.getCorpCode()
						+ ",identifyMode=" + service.getIdentifyMode()
						+ ",orderType=" + service.getOrderType()
						+ ",structcode=" + service.getStructcode()	
						+ ",serMoId=" + serMoId
						+ ",phone=" + phone
						+ ",msg=" + msg		
				);
				//建一个新的操作记录，8为操作员被禁用/注销，取消执行
				serLogBiz.addNewServiceLog(8, service.getSerId(), "");
				return false;
			}
			
			CheckUtil checkUtil = new CheckUtil();
			//验证操作员企业发送账户是否一致性
			boolean checkSendResult = checkUtil.checkSysuserInCorp(sysuser, service.getCorpCode(), service.getSpUser(), null);
			if(!checkSendResult)
			{
				EmpExecutionContext.error("执行上行业务，验证操作员企业发送账户是否一致性不通过，当次执行取消。"
						+ "serId=" + service.getSerId()
						+ ",userId=" + service.getUserId()
						+ ",serName=" + service.getSerName()
						+ ",commnets=" + service.getCommnets()
						+ ",orderCode=" + service.getOrderCode()
						+ ",subNo=" + service.getSubNo()
						+ ",spUser=" + service.getSpUser()
						+ ",runState=" + service.getRunState()
						+ ",serType=" + service.getSerType()
						+ ",msgSeparated=" + service.getMsgSeparated()
						+ ",ownerId=" + service.getOwnerId()
						+ ",createTime=" + service.getCreateTime()
						+ ",busCode=" + service.getBusCode()
						+ ",menuCode=" + service.getMenuCode()
						+ ",corpCode=" + service.getCorpCode()
						+ ",identifyMode=" + service.getIdentifyMode()
						+ ",orderType=" + service.getOrderType()
						+ ",structcode=" + service.getStructcode()	
						+ ",serMoId=" + serMoId
						+ ",phone=" + phone
						+ ",msg=" + msg			
				);
				return false;
			}

			//创建新的操作日志
			slId = serLogBiz.addServiceLogReturnId(6, service.getSerId(), "");

			//创建日志失败
			if (slId == null || slId == 0)
			{
				EmpExecutionContext.error("执行上行业务，创建日志失败。"
						+ "serId=" + service.getSerId()
						+ ",userId=" + service.getUserId()
						+ ",serName=" + service.getSerName()
						+ ",commnets=" + service.getCommnets()
						+ ",orderCode=" + service.getOrderCode()
						+ ",subNo=" + service.getSubNo()
						+ ",spUser=" + service.getSpUser()
						+ ",runState=" + service.getRunState()
						+ ",serType=" + service.getSerType()
						+ ",msgSeparated=" + service.getMsgSeparated()
						+ ",ownerId=" + service.getOwnerId()
						+ ",createTime=" + service.getCreateTime()
						+ ",busCode=" + service.getBusCode()
						+ ",menuCode=" + service.getMenuCode()
						+ ",corpCode=" + service.getCorpCode()
						+ ",identifyMode=" + service.getIdentifyMode()
						+ ",orderType=" + service.getOrderType()
						+ ",structcode=" + service.getStructcode()	
						+ ",serMoId=" + serMoId
						+ ",phone=" + phone
						+ ",msg=" + msg			
				);
				//更新日志，5为其他异常
				serLogBiz.addNewServiceLog(5, service.getSerId(), "");
				return false;
			}
			
			// 将上行短信内容按分隔符拆分，索引0为手机号，1为参数值1，2为参数值2等
			String[] msgPara = msg.split("\\" + msgSeparated);
			//存入号码
			msgPara[0] = phone;
			
			//存储结果map，key为步骤id，value为步骤执行结果集合
			Map<String, List<LinkedHashMap<String, String>>> resultsMap = new HashMap<String, List<LinkedHashMap<String, String>>>();
			//短信参数内容集合
			List<LinkedHashMap<String, String>> msgParasList = new ArrayList<LinkedHashMap<String, String>>();
			//短信参数内容集合
			LinkedHashMap<String, String> msgParasMap = new LinkedHashMap<String, String>();
			
			// 将上行短信内容中的参数值放在msgParasMap里面
			for (int i = 0; i < msgPara.length; i++)
			{
				msgParasMap.put("userpara_" + i, msgPara[i]);
			}
			//传入参数到集合
			msgParasList.add(msgParasMap);
			
			//key为0时，表示该记录为上行短信内容参数值
			resultsMap.put("0", msgParasList);
			
			int size = processesList.size();
			//循环按顺序执行上行业务的每个步骤
			for (int i = 0; i < size; i++)
			{
				// 步骤类型：查询 select
				if (processesList.get(i).getPrType() == 4)
				{
					String sql = processesList.get(i).getSql();
					//未填sql语句
					if (sql == null || "".equals(sql.trim()))
					{
						EmpExecutionContext.error("执行上行业务，服务配置不正确，select步骤Sql为空。"
								+ "PrId=" + processesList.get(i).getPrId()
								+ ",PrName=" + processesList.get(i).getPrName()
								+ ",PrType=" + processesList.get(i).getPrType()
								+ ",FinalState=" + processesList.get(i).getFinalState()
								+ ",serId=" + service.getSerId()
								+ ",userId=" + service.getUserId()
								+ ",serName=" + service.getSerName()
								+ ",commnets=" + service.getCommnets()
								+ ",orderCode=" + service.getOrderCode()
								+ ",subNo=" + service.getSubNo()
								+ ",spUser=" + service.getSpUser()
								+ ",runState=" + service.getRunState()
								+ ",serType=" + service.getSerType()
								+ ",msgSeparated=" + service.getMsgSeparated()
								+ ",ownerId=" + service.getOwnerId()
								+ ",createTime=" + service.getCreateTime()
								+ ",busCode=" + service.getBusCode()
								+ ",menuCode=" + service.getMenuCode()
								+ ",corpCode=" + service.getCorpCode()
								+ ",identifyMode=" + service.getIdentifyMode()
								+ ",orderType=" + service.getOrderType()
								+ ",structcode=" + service.getStructcode()	
								+ ",serMoId=" + serMoId
								+ ",phone=" + phone
								+ ",msg=" + msg			
						);
						//更新日志，4为服务配置不正确
						serLogBiz.updateServiceLog(slId, 4, "");
						return false;
					}
					EmpExecutionContext.info("执行上行业务，select步骤信息。"
							+ "PrId=" + processesList.get(i).getPrId()
							+ ",PrName=" + processesList.get(i).getPrName()
							+ ",PrType=" + processesList.get(i).getPrType()
							+ ",Sql=" + processesList.get(i).getSql()
							+ ",FinalState=" + processesList.get(i).getFinalState()
							+ ",serId=" + service.getSerId()
							+ ",userId=" + service.getUserId()
							+ ",serName=" + service.getSerName()
							+ ",commnets=" + service.getCommnets()
							+ ",orderCode=" + service.getOrderCode()
							+ ",subNo=" + service.getSubNo()
							+ ",spUser=" + service.getSpUser()
							+ ",runState=" + service.getRunState()
							+ ",serType=" + service.getSerType()
							+ ",msgSeparated=" + service.getMsgSeparated()
							+ ",ownerId=" + service.getOwnerId()
							+ ",createTime=" + service.getCreateTime()
							+ ",busCode=" + service.getBusCode()
							+ ",menuCode=" + service.getMenuCode()
							+ ",corpCode=" + service.getCorpCode()
							+ ",identifyMode=" + service.getIdentifyMode()
							+ ",orderType=" + service.getOrderType()
							+ ",structcode=" + service.getStructcode()	
							+ ",serMoId=" + serMoId
							+ ",phone=" + phone
							+ ",msg=" + msg			
					);
					//根据条件上行信息参数值组装sql语句
					sql = this.conbineSql(sql, msgPara);
					if(sql == null || "".equals(sql.trim())){
						EmpExecutionContext.error("执行上行业务，服务配置不正确，select步骤组装后的Sql为空。"
								+ "PrId=" + processesList.get(i).getPrId()
								+ ",PrName=" + processesList.get(i).getPrName()
								+ ",PrType=" + processesList.get(i).getPrType()
								+ ",Sql=" + processesList.get(i).getSql()
								+ ",FinalState=" + processesList.get(i).getFinalState()
								+ ",serId=" + service.getSerId()
								+ ",userId=" + service.getUserId()
								+ ",serName=" + service.getSerName()
								+ ",commnets=" + service.getCommnets()
								+ ",orderCode=" + service.getOrderCode()
								+ ",subNo=" + service.getSubNo()
								+ ",spUser=" + service.getSpUser()
								+ ",runState=" + service.getRunState()
								+ ",serType=" + service.getSerType()
								+ ",msgSeparated=" + service.getMsgSeparated()
								+ ",ownerId=" + service.getOwnerId()
								+ ",createTime=" + service.getCreateTime()
								+ ",busCode=" + service.getBusCode()
								+ ",menuCode=" + service.getMenuCode()
								+ ",corpCode=" + service.getCorpCode()
								+ ",identifyMode=" + service.getIdentifyMode()
								+ ",orderType=" + service.getOrderType()
								+ ",structcode=" + service.getStructcode()	
								+ ",serMoId=" + serMoId
								+ ",phone=" + phone
								+ ",msg=" + msg	
						);
						//更新日志，4为服务配置不正确
						serLogBiz.updateServiceLog(slId, 4, "");
						return false;
					}
					processesList.get(i).setSql(sql);
					//数据库每一行记录的list集合，里面的map为每一行记录里的列值，key为列名，value为列值
					List<LinkedHashMap<String, String>> selsResult = this.queryProcessSql(processesList.get(i), msgPara);
					if(selsResult == null)
					{
						EmpExecutionContext.error("执行上行业务，select步骤数据库操作异常。"
								+ "PrId=" + processesList.get(i).getPrId()
								+ ",PrName=" + processesList.get(i).getPrName()
								+ ",PrType=" + processesList.get(i).getPrType()
								+ ",Sql=" + processesList.get(i).getSql()
								+ ",FinalState=" + processesList.get(i).getFinalState()
								+ ",serId=" + service.getSerId()
								+ ",userId=" + service.getUserId()
								+ ",serName=" + service.getSerName()
								+ ",commnets=" + service.getCommnets()
								+ ",orderCode=" + service.getOrderCode()
								+ ",subNo=" + service.getSubNo()
								+ ",spUser=" + service.getSpUser()
								+ ",runState=" + service.getRunState()
								+ ",serType=" + service.getSerType()
								+ ",msgSeparated=" + service.getMsgSeparated()
								+ ",ownerId=" + service.getOwnerId()
								+ ",createTime=" + service.getCreateTime()
								+ ",busCode=" + service.getBusCode()
								+ ",menuCode=" + service.getMenuCode()
								+ ",corpCode=" + service.getCorpCode()
								+ ",identifyMode=" + service.getIdentifyMode()
								+ ",orderType=" + service.getOrderType()
								+ ",structcode=" + service.getStructcode()	
								+ ",serMoId=" + serMoId
								+ ",phone=" + phone
								+ ",msg=" + msg		
						);
						//更新日志，3为数据库操作异常
						serLogBiz.updateServiceLog(slId, 3, "");
						return false;
					}
					
					//保存执行结果，key为步骤id，value为步骤执行结果集合
					resultsMap.put(processesList.get(i).getPrId().toString(), selsResult);
					
					// 是否为最后处理步骤(0否1是)
					if (processesList.get(i).getFinalState() == 1)
					{
						break;
					}
				}
				// 步骤类型：回复 reply
				else if (processesList.get(i).getPrType() == 5)
				{
					//获取回复步骤对象
					LfReply reply = processBiz.getReplyByPrId(processesList.get(i).getPrId());
					//检查该步骤是否可运行
					boolean isAllowRun = this.checkProcessProCons(resultsMap, processesList.get(i).getPrId());
					if (!isAllowRun)
					{
						EmpExecutionContext.error("执行上行业务，检测reply步骤不可运行。"
								+ "PrId=" + processesList.get(i).getPrId()
								+ ",PrName=" + processesList.get(i).getPrName()
								+ ",PrType=" + processesList.get(i).getPrType()
								+ ",FinalState=" + processesList.get(i).getFinalState()
								+ ",serId=" + service.getSerId()
								+ ",userId=" + service.getUserId()
								+ ",serName=" + service.getSerName()
								+ ",commnets=" + service.getCommnets()
								+ ",orderCode=" + service.getOrderCode()
								+ ",subNo=" + service.getSubNo()
								+ ",spUser=" + service.getSpUser()
								+ ",runState=" + service.getRunState()
								+ ",serType=" + service.getSerType()
								+ ",msgSeparated=" + service.getMsgSeparated()
								+ ",ownerId=" + service.getOwnerId()
								+ ",createTime=" + service.getCreateTime()
								+ ",busCode=" + service.getBusCode()
								+ ",menuCode=" + service.getMenuCode()
								+ ",corpCode=" + service.getCorpCode()
								+ ",identifyMode=" + service.getIdentifyMode()
								+ ",orderType=" + service.getOrderType()
								+ ",structcode=" + service.getStructcode()	
								+ ",serMoId=" + serMoId
								+ ",phone=" + phone
								+ ",msg=" + msg		
						);
						//不可运行则跳过
						continue;
					}
					String key = "-1";
					//没步骤对象
					if (reply == null)
					{
						EmpExecutionContext.error("执行上行业务，服务配置不正确，reply为null。"
								+ "PrId=" + processesList.get(i).getPrId()
								+ ",PrName=" + processesList.get(i).getPrName()
								+ ",PrType=" + processesList.get(i).getPrType()
								+ ",FinalState=" + processesList.get(i).getFinalState()
								+ ",serId=" + service.getSerId()
								+ ",userId=" + service.getUserId()
								+ ",serName=" + service.getSerName()
								+ ",commnets=" + service.getCommnets()
								+ ",orderCode=" + service.getOrderCode()
								+ ",subNo=" + service.getSubNo()
								+ ",spUser=" + service.getSpUser()
								+ ",runState=" + service.getRunState()
								+ ",serType=" + service.getSerType()
								+ ",msgSeparated=" + service.getMsgSeparated()
								+ ",ownerId=" + service.getOwnerId()
								+ ",createTime=" + service.getCreateTime()
								+ ",busCode=" + service.getBusCode()
								+ ",menuCode=" + service.getMenuCode()
								+ ",corpCode=" + service.getCorpCode()
								+ ",identifyMode=" + service.getIdentifyMode()
								+ ",orderType=" + service.getOrderType()
								+ ",structcode=" + service.getStructcode()	
								+ ",serMoId=" + serMoId
								+ ",phone=" + phone
								+ ",msg=" + msg
						);
						//更新日志，4为服务配置不正确
						serLogBiz.updateServiceLog(slId, 4, "");
						return false;
					}
					EmpExecutionContext.info("执行上行业务，reply步骤信息。"
							+ "PrId=" + processesList.get(i).getPrId()
							+ ",PrName=" + processesList.get(i).getPrName()
							+ ",PrType=" + processesList.get(i).getPrType()
							+ ",FinalState=" + processesList.get(i).getFinalState()
							+ ",msgMain=" + reply.getMsgMain()
							+ ",msgLoopId=" + reply.getMsgLoopId()
							+ ",serId=" + service.getSerId()
							+ ",userId=" + service.getUserId()
							+ ",serName=" + service.getSerName()
							+ ",commnets=" + service.getCommnets()
							+ ",orderCode=" + service.getOrderCode()
							+ ",subNo=" + service.getSubNo()
							+ ",spUser=" + service.getSpUser()
							+ ",runState=" + service.getRunState()
							+ ",serType=" + service.getSerType()
							+ ",msgSeparated=" + service.getMsgSeparated()
							+ ",ownerId=" + service.getOwnerId()
							+ ",createTime=" + service.getCreateTime()
							+ ",busCode=" + service.getBusCode()
							+ ",menuCode=" + service.getMenuCode()
							+ ",corpCode=" + service.getCorpCode()
							+ ",identifyMode=" + service.getIdentifyMode()
							+ ",orderType=" + service.getOrderType()
							+ ",structcode=" + service.getStructcode()	
							+ ",serMoId=" + serMoId
							+ ",phone=" + phone
							+ ",msg=" + msg		
					);
					//获取reply步骤所使用的其他步骤
					if (reply.getMsgLoopId() != null)
					{
						key = reply.getMsgLoopId().toString();
					}
					//发送短信
					String sendResult = this.sendMsg(service, processesList.get(i), phone, reply
							.getMsgMain(), resultsMap.get(key), slId, sysuser, serMoId);
					
					//发送失败
					if (sendResult == null || !sendResult.equals("000"))
					{
						EmpExecutionContext.error("执行上行业务，reply步骤发送失败。"
								+ "errcode=" + sendResult
								+ ",PrId=" + processesList.get(i).getPrId()
								+ ",PrName=" + processesList.get(i).getPrName()
								+ ",PrType=" + processesList.get(i).getPrType()
								+ ",FinalState=" + processesList.get(i).getFinalState()
								+ ",serId=" + service.getSerId()
								+ ",userId=" + service.getUserId()
								+ ",serName=" + service.getSerName()
								+ ",commnets=" + service.getCommnets()
								+ ",orderCode=" + service.getOrderCode()
								+ ",subNo=" + service.getSubNo()
								+ ",spUser=" + service.getSpUser()
								+ ",runState=" + service.getRunState()
								+ ",serType=" + service.getSerType()
								+ ",msgSeparated=" + service.getMsgSeparated()
								+ ",ownerId=" + service.getOwnerId()
								+ ",createTime=" + service.getCreateTime()
								+ ",busCode=" + service.getBusCode()
								+ ",menuCode=" + service.getMenuCode()
								+ ",corpCode=" + service.getCorpCode()
								+ ",identifyMode=" + service.getIdentifyMode()
								+ ",orderType=" + service.getOrderType()
								+ ",structcode=" + service.getStructcode()	
								+ ",serMoId=" + serMoId
								+ ",phone=" + phone
								+ ",msg=" + msg
						);
						return false;
					}
					else
					{
						EmpExecutionContext.info("执行上行业务，reply步骤发送成功。"
								+ "errcode=" + sendResult
								+ ",PrId=" + processesList.get(i).getPrId()
								+ ",PrName=" + processesList.get(i).getPrName()
								+ ",PrType=" + processesList.get(i).getPrType()
								+ ",FinalState=" + processesList.get(i).getFinalState()
								+ ",serId=" + service.getSerId()
								+ ",userId=" + service.getUserId()
								+ ",serName=" + service.getSerName()
								+ ",commnets=" + service.getCommnets()
								+ ",orderCode=" + service.getOrderCode()
								+ ",subNo=" + service.getSubNo()
								+ ",spUser=" + service.getSpUser()
								+ ",runState=" + service.getRunState()
								+ ",serType=" + service.getSerType()
								+ ",msgSeparated=" + service.getMsgSeparated()
								+ ",ownerId=" + service.getOwnerId()
								+ ",createTime=" + service.getCreateTime()
								+ ",busCode=" + service.getBusCode()
								+ ",menuCode=" + service.getMenuCode()
								+ ",corpCode=" + service.getCorpCode()
								+ ",identifyMode=" + service.getIdentifyMode()
								+ ",orderType=" + service.getOrderType()
								+ ",structcode=" + service.getStructcode()	
								+ ",serMoId=" + serMoId
								+ ",phone=" + phone
								+ ",msg=" + msg		
						);
					}
					
					//如果是最后一步，则跳出
					if (processesList.get(i).getFinalState() == 1)
					{
						break;
					}
				}
				// 步骤类型为增/删/改
				else if (processesList.get(i).getPrType() == 1
						|| processesList.get(i).getPrType() == 2
						|| processesList.get(i).getPrType() == 3)
				{
					//获取执行的sql语句
					String sql = processesList.get(i).getSql();
					
					if (sql == null || "".equals(sql.trim()))
					{
						EmpExecutionContext.error("执行上行业务，服务配置不正确，增/删/改步骤Sql为空。"
								+ "PrId=" + processesList.get(i).getPrId()
								+ ",PrName=" + processesList.get(i).getPrName()
								+ ",PrType=" + processesList.get(i).getPrType()
								+ ",FinalState=" + processesList.get(i).getFinalState()
								+ ",serId=" + service.getSerId()
								+ ",userId=" + service.getUserId()
								+ ",serName=" + service.getSerName()
								+ ",commnets=" + service.getCommnets()
								+ ",orderCode=" + service.getOrderCode()
								+ ",subNo=" + service.getSubNo()
								+ ",spUser=" + service.getSpUser()
								+ ",runState=" + service.getRunState()
								+ ",serType=" + service.getSerType()
								+ ",msgSeparated=" + service.getMsgSeparated()
								+ ",ownerId=" + service.getOwnerId()
								+ ",createTime=" + service.getCreateTime()
								+ ",busCode=" + service.getBusCode()
								+ ",menuCode=" + service.getMenuCode()
								+ ",corpCode=" + service.getCorpCode()
								+ ",identifyMode=" + service.getIdentifyMode()
								+ ",orderType=" + service.getOrderType()
								+ ",structcode=" + service.getStructcode()	
								+ ",serMoId=" + serMoId
								+ ",phone=" + phone
								+ ",msg=" + msg			
						);
						//更新日志，4为服务配置不正确
						serLogBiz.updateServiceLog(slId, 4, "");
						return false;
					}
					
					//用参数组装sql语句
					sql = this.conbineSql(sql, msgPara);
					//设置sql语句到步骤对象
					processesList.get(i).setSql(sql);
					//执行sql并获取返回结果
					List<LinkedHashMap<String, String>> execuesResult = this
							.execueProcessSql(processesList.get(i), msgPara);
					if(execuesResult == null)
					{
						EmpExecutionContext.error("执行上行业务，增删改步骤数据库操作异常。"
								+ "PrId=" + processesList.get(i).getPrId()
								+ ",PrName=" + processesList.get(i).getPrName()
								+ ",PrType=" + processesList.get(i).getPrType()
								+ ",FinalState=" + processesList.get(i).getFinalState()
								+ ",serId=" + service.getSerId()
								+ ",userId=" + service.getUserId()
								+ ",serName=" + service.getSerName()
								+ ",commnets=" + service.getCommnets()
								+ ",orderCode=" + service.getOrderCode()
								+ ",subNo=" + service.getSubNo()
								+ ",spUser=" + service.getSpUser()
								+ ",runState=" + service.getRunState()
								+ ",serType=" + service.getSerType()
								+ ",msgSeparated=" + service.getMsgSeparated()
								+ ",ownerId=" + service.getOwnerId()
								+ ",createTime=" + service.getCreateTime()
								+ ",busCode=" + service.getBusCode()
								+ ",menuCode=" + service.getMenuCode()
								+ ",corpCode=" + service.getCorpCode()
								+ ",identifyMode=" + service.getIdentifyMode()
								+ ",orderType=" + service.getOrderType()
								+ ",structcode=" + service.getStructcode()	
								+ ",serMoId=" + serMoId
								+ ",phone=" + phone
								+ ",msg=" + msg			
						);
						//更新日志，3为数据库操作异常
						serLogBiz.updateServiceLog(slId, 3, null);
						return false;
					}
					
					//保存执行结果到集合
					resultsMap.put(processesList.get(i).getPrId().toString(), execuesResult);
					
					//如果是最后一步，则跳出
					if (processesList.get(i).getFinalState() == 1)
					{
						break;
					}
				}
			}
			
			//更新日志，10为执行成功
			serLogBiz.updateServiceLog(slId, 10, null);
			return true;
		}
		catch(Exception e)
		{
			EmpExecutionContext.error(e,"执行上行业务异常。"
					+ "serId=" + service.getSerId()
					+ ",userId=" + service.getUserId()
					+ ",serName=" + service.getSerName()
					+ ",commnets=" + service.getCommnets()
					+ ",orderCode=" + service.getOrderCode()
					+ ",subNo=" + service.getSubNo()
					+ ",spUser=" + service.getSpUser()
					+ ",runState=" + service.getRunState()
					+ ",serType=" + service.getSerType()
					+ ",msgSeparated=" + service.getMsgSeparated()
					+ ",ownerId=" + service.getOwnerId()
					+ ",createTime=" + service.getCreateTime()
					+ ",busCode=" + service.getBusCode()
					+ ",menuCode=" + service.getMenuCode()
					+ ",corpCode=" + service.getCorpCode()
					+ ",identifyMode=" + service.getIdentifyMode()
					+ ",orderType=" + service.getOrderType()
					+ ",structcode=" + service.getStructcode()	
					+ ",serMoId=" + serMoId
					+ ",phone=" + phone
					+ ",msg=" + msg		
			);
			
			if (slId == null || slId == 0)
			{
				//没日志id则新建一个日志记录，5为其他异常
				serLogBiz.addNewServiceLog(5, service.getSerId(), "");
			} else
			{
				//有日志id则更新记录，5为其他异常
				serLogBiz.updateServiceLog(slId, 5, "");
			}
			return false;
		}
	}

	/**
	 * 上行业务构造查询sql语句
	 * @param originalSql
	 * @param msgPara
	 * @return
	 * @throws Exception
	 */
	private String conbineSql(String originalSql, String[] msgPara)
	{
		try
		{
			//循环出参数内容
			for (int j = 0; j < msgPara.length; j++)
			{
				//爸参数拼到sql语句中
				originalSql = originalSql.replace("#p_" + j + "#", msgPara[j]);
				originalSql = originalSql.replace("#P_" + j + "#", msgPara[j]);
			}
			//返回sql语句
			return originalSql;
		}
		catch (Exception e)
		{
			EmpExecutionContext.error(e, "上行业务构造查询sql语句异常。"
					+ "sql=" + originalSql
			);
			return null;
		}
	}

	/**
	 * 
	 * @param resultsMap
	 * @param prId
	 * @return
	 * @throws Exception
	 */
	private boolean checkProcessProCons(
			Map<String, List<LinkedHashMap<String, String>>> resultsMap,
			Long prId) throws Exception
	{
		//步骤配置biz
		ProcessConfigBiz processBiz = new ProcessConfigBiz();
		//获取步骤执行条件
		List<LfProCon> proconsList = processBiz.getProCons(prId);
		//没条件则直接返回true
		if (proconsList == null || proconsList.size() == 0)
		{
			return true;
		}
		//数据库每一行记录的list集合，里面的map为每一行记录里的列值，key为列名，value为列值
		List<LinkedHashMap<String, String>> resultsList;
		//保存结果
		boolean result = false;
		//循环获取执行条件，并处理
		for (int i = 0; i < proconsList.size(); i++)
		{
			if (proconsList.get(i).getUsedPrId() == null)
			{
				resultsList = resultsMap.get("0");
			} else
			{
				resultsList = resultsMap.get(proconsList.get(i).getUsedPrId()
						.toString());
			}
			if (resultsList == null || resultsList.size() == 0)
			{
				EmpExecutionContext.error("上行业务执行失败，结果集为空。"
						+ "prId=" + prId
				);
				return false;
			}
			//为每一行记录里的列值，key为列名，value为列值
			Map<String, String> valuesMap = null;
			for (int j = 0; j < resultsList.size(); j++)
			{
				valuesMap = resultsList.get(j);
				if (proconsList.get(i).getConExpress() == null
						|| "".equals(proconsList.get(i).getConExpress().trim()))
				{
					return false;
				}
				String value = valuesMap.get(proconsList.get(i).getConExpress()
						.toLowerCase());
				if (value == null)
				{
					continue;
				}
				
				if (proconsList.get(i).getConOperate().equals("="))
				{
					//如果是等于操作符
					result = (value.equals(proconsList.get(i).getConValue()));
				} else if (proconsList.get(i).getConOperate().equals("!="))
				{
					//如果是不等于操作符
					result = (!value.equals(proconsList.get(i).getConValue()));
				} else if (value != null
						&& proconsList.get(i).getConOperate().equals(">"))
				{
					//如果是大于操作符
					result = Long.valueOf(value) > Long.valueOf(proconsList
							.get(i).getConValue());
				} else if (value != null
						&& proconsList.get(i).getConOperate().equals("<"))
				{
					//如果是小于操作符
					result = Long.valueOf(value) < Long.valueOf(proconsList
							.get(i).getConValue());
				} else if (value != null
						&& proconsList.get(i).getConOperate().equals(">="))
				{
					//如果是大于等于操作符
					result = Long.valueOf(value) >= Long.valueOf(proconsList
							.get(i).getConValue());
				} else if (value != null
						&& proconsList.get(i).getConOperate().equals("<="))
				{
					//如果是小于等于操作符
					result = Long.valueOf(value) <= Long.valueOf(proconsList
							.get(i).getConValue());
				}
				if (!result)
				{
					//没结果则返回false
					return false;
				}
			}
		}
		//返回结果
		return result;
	}

	/**
	 * 
	 * @param service
	 * @param process
	 * @param phone
	 * @param mainMsg
	 * @param resultsList
	 * @return
	 * @throws Exception
	 */
	private String sendMsg(LfService service, LfProcess process, String phone,
			String mainMsg, List<LinkedHashMap<String, String>> resultsList, Long slId, LfSysuser sysuser, Long msId)
	{
		
		//全局变量biz
		GlobalVariableBiz globalBiz = GlobalVariableBiz.getInstance();
		//获取任务id
		Long taskId = globalBiz.getValueByKey("taskId", 1L);
		String mobileUrl = null;
		//服务日志biz
		AppLogBiz serLogBiz = new AppLogBiz();
		//发送号码数
		long sendCount = 1l;
		
		//获取网讯id Map，key为#W_id#格式的网讯id，value为id，这个value后面要存放对应生成的网讯url
		Map<String,String> wxIdsMap = this.getWxId(mainMsg);
		String fileuri;
		try
		{
			//文件操作biz
			TxtFileUtil txtfileutil = new TxtFileUtil();
			
			//获取短信文件路径
			String[] urlValuesArray = txtfileutil.createUrlAndDir(2, service
					.getSerId().toString()
					+ "_" + process.getPrId() + "_" + taskId);
			//短息文件路径
			String newUrl = urlValuesArray[0];
			mobileUrl = urlValuesArray[1];
			String newline = System.getProperties().getProperty("line.separator");
			StringBuffer finalSb = new StringBuffer();

			if(wxIdsMap != null && wxIdsMap.size() > 0){
				//获取网讯url，存放到map的value中
				wxIdsMap = this.getWxUrl(wxIdsMap, taskId);
			}
			
			if (resultsList != null && resultsList.size() > 1)
			{
				sendCount = resultsList.size();
				String content = null;
				
				for (int i = 0; i < resultsList.size() - 1; i++)
				{
					//循环组装短信内容
					content = this.combineContent(resultsList.get(i),
							mainMsg);
					
					//替换短信内容中网讯占位符为url
					content = this.repWxMsg(content, wxIdsMap, phone);
					
					//循环组装短信文件每一行
					finalSb.append(phone + "," + content);
					finalSb.append(newline);
				}
			} else
			{
				//替换短信内容中网讯占位符为url
				String newMsg = this.repWxMsg(mainMsg, wxIdsMap, phone);
				
				finalSb.append(phone + "," + newMsg);
			}
			//按照地址写文件
			txtfileutil.writeToTxtFile(newUrl, finalSb.toString());
			
			//上传文件到文件服务器
			if(StaticValue.getISCLUSTER() ==1)
			{
				CommonBiz commBiz = new CommonBiz();
				
				//上传发送文件到文件服务器，并使用文件服务器地址
				fileuri = commBiz.uploadFileToFileServer(mobileUrl);
				EmpExecutionContext.info("上行业务发送短信，发送文件上传到文件服务器，上传后返回路径："+fileuri);
			}
			else
			{
				//使用本节点地址
				fileuri = StaticValue.BASEURL;
				EmpExecutionContext.info("上行业务发送短信，发送文件使用本地路径："+fileuri);
			}
		}
		catch(Exception e)
		{
			EmpExecutionContext.error(e,"上行业务发送短信，生成短信下发文件异常。"
					+ "PrId=" + process.getPrId()
					+ ",PrName=" + process.getPrName()
					+ ",PrType=" + process.getPrType()
					+ ",FinalState=" + process.getFinalState()
					+ ",serId=" + service.getSerId()
					+ ",userId=" + service.getUserId()
					+ ",serName=" + service.getSerName()
					+ ",commnets=" + service.getCommnets()
					+ ",orderCode=" + service.getOrderCode()
					+ ",subNo=" + service.getSubNo()
					+ ",spUser=" + service.getSpUser()
					+ ",runState=" + service.getRunState()
					+ ",serType=" + service.getSerType()
					+ ",msgSeparated=" + service.getMsgSeparated()
					+ ",ownerId=" + service.getOwnerId()
					+ ",createTime=" + service.getCreateTime()
					+ ",busCode=" + service.getBusCode()
					+ ",menuCode=" + service.getMenuCode()
					+ ",corpCode=" + service.getCorpCode()
					+ ",identifyMode=" + service.getIdentifyMode()
					+ ",orderType=" + service.getOrderType()
					+ ",structcode=" + service.getStructcode()	
					+ ",mobileUrl=" + mobileUrl
					+ ",phone=" + phone
					+ ",mainMsg=" + mainMsg
					+ ",serMoId=" + msId
			);
			//5为其他异常
			serLogBiz.updateServiceLog(slId, 5, "");
			return null;
		}
		
		//计费biz
		BalanceLogBiz balanceBiz = new BalanceLogBiz();
		//统计预发送条数
		Integer icount = 0;
		try
		{
			SmsBiz smsBiz = new SmsBiz();
			//统计预发送条数
			icount = smsBiz.countAllOprSmsNumber(service.getSpUser(), null, 2, mobileUrl, null);
			
			//SP账号余额检查
			int spResult = balanceBiz.checkSpUserFee(service.getSpUser(), icount, 1);
			//余额不足或其他错误情况则不能继续流程
			if(spResult < 0)
			{
				EmpExecutionContext.error("上行业务执行中，SP账号余额不足。"
						+ "serId=" + service.getSerId()
						+ ",userId=" + service.getUserId()
						+ ",spUser=" + service.getSpUser()
						+ ",corpCode=" + service.getCorpCode()
						+ ",mobileUrl=" + mobileUrl
						+ ",icount=" + icount
						+ ",SP账号余额检查=" + spResult
				);
				//14为发送账号余额不足
				serLogBiz.updateServiceLog(slId, 14, mobileUrl);
				boolean updateRes = this.updateSerMoTask(msId, null, mobileUrl);
				EmpExecutionContext.info("上行业务发送短信，更新上行记录结果："+updateRes+"。serMoId="+msId);
				return null;
			}
			
			//扣费并接收返回状态
			String wgresult = balanceBiz.checkGwFee(service.getSpUser(), icount, service.getCorpCode(),true,1);
			if("nogwfee".equals(wgresult) || "feefail".equals(wgresult) || "lessgwfee".equals(wgresult.substring(0, 9)))
			{
				EmpExecutionContext.error("上行业务执行中，运营商余额不足/获取运营商余额失败。"
						+ "serId=" + service.getSerId()
						+ ",userId=" + service.getUserId()
						+ ",spUser=" + service.getSpUser()
						+ ",corpCode=" + service.getCorpCode()
						+ ",mobileUrl=" + mobileUrl
						+ ",icount=" + icount
						+ ",wgresult=" + wgresult
				);
				//2为运营商余额不足/获取运营商余额失败
				serLogBiz.updateServiceLog(slId, 2, mobileUrl);
				boolean updateRes = this.updateSerMoTask(msId, null, mobileUrl);
				EmpExecutionContext.info("上行业务发送短信，更新上行记录结果："+updateRes+"。serMoId="+msId);
				return null;
			}
			
		}
		catch (Exception e)
		{
			EmpExecutionContext.error(e,"上行业务发送短信，扣费异常。"
					+ ",PrId=" + process.getPrId()
					+ ",PrName=" + process.getPrName()
					+ ",PrType=" + process.getPrType()
					+ ",FinalState=" + process.getFinalState()
					+ ",serId=" + service.getSerId()
					+ ",userId=" + service.getUserId()
					+ ",serName=" + service.getSerName()
					+ ",commnets=" + service.getCommnets()
					+ ",orderCode=" + service.getOrderCode()
					+ ",subNo=" + service.getSubNo()
					+ ",spUser=" + service.getSpUser()
					+ ",runState=" + service.getRunState()
					+ ",serType=" + service.getSerType()
					+ ",msgSeparated=" + service.getMsgSeparated()
					+ ",ownerId=" + service.getOwnerId()
					+ ",createTime=" + service.getCreateTime()
					+ ",busCode=" + service.getBusCode()
					+ ",menuCode=" + service.getMenuCode()
					+ ",corpCode=" + service.getCorpCode()
					+ ",identifyMode=" + service.getIdentifyMode()
					+ ",orderType=" + service.getOrderType()
					+ ",structcode=" + service.getStructcode()	
					+ ",mobileUrl=" + mobileUrl
					+ ",phone=" + phone
					+ ",mainMsg=" + mainMsg
					+ ",serMoId=" + msId		
			);
			//1为向网关发送请求失败
			serLogBiz.updateServiceLog(slId, 15, mobileUrl);
			return null;
		}
		
		//调用发送接口
		//发送短信并返回结果
		String result = null;
		try
		{
			// 将所有参数封装到参数类里面
			CommonBiz commonBiz = new CommonBiz();
			WGParams wg = new WGParams();
			wg.setSpid(service.getSpUser());
			wg.setSppassword(commonBiz.getSpPwdBySpUserId(service.getSpUser()));
			
			//用指令的，则不设置尾号
			if(service.getIdentifyMode() != 2){
				// 获取上行尾号，此方法的规则是，如果能找到这个模块绑定的子号就用，找不到就重新生成一个尾号返回
				String subno = GlobalVariableBiz.getInstance().getValidSubno(
						StaticValue.MOBUSCODE, 0, sysuser.getCorpCode(),
						new ErrorCodeParam());
				// 设置尾号
				wg.setSa(subno);
				EmpExecutionContext.info("上行业务发送短信，设置尾号。serMoId="+msId);
			}
			
			wg.setBmttype("2");
			wg.setTaskid(taskId.toString());
			wg.setTitle(service.getSerName());
			wg.setPriority("1");
			wg.setUrl(fileuri+mobileUrl);
			wg.setParam1(sysuser.getUserCode());
			// 上行模块编码
			wg.setModuleid(StaticValue.MOBUSCODE);
			// 业务类型
			wg.setSvrtype(service.getBusCode());
			// 不要状态报告
			wg.setRptflag("0");
			
			//短信发送biz
			HttpSmsSend jobBiz = new HttpSmsSend();
			//发送短信并返回结果
			result = jobBiz.createbatchMtRequest(wg);
			
			EmpExecutionContext.info("上行业务发送短信，提交短信接口，响应结果。"
					+ "result=" + result
					+ ",subno=" + wg.getSa()
					+ ",Taskid=" + wg.getTaskid()
					+ ",Title=" + wg.getTitle()
					+ ",Param1=" + wg.getParam1()
					+ ",PrId=" + process.getPrId()
					+ ",PrName=" + process.getPrName()
					+ ",PrType=" + process.getPrType()
					+ ",FinalState=" + process.getFinalState()
					+ ",serId=" + service.getSerId()
					+ ",userId=" + service.getUserId()
					+ ",serName=" + service.getSerName()
					+ ",commnets=" + service.getCommnets()
					+ ",orderCode=" + service.getOrderCode()
					+ ",subNo=" + service.getSubNo()
					+ ",spUser=" + service.getSpUser()
					+ ",runState=" + service.getRunState()
					+ ",serType=" + service.getSerType()
					+ ",msgSeparated=" + service.getMsgSeparated()
					+ ",ownerId=" + service.getOwnerId()
					+ ",createTime=" + service.getCreateTime()
					+ ",busCode=" + service.getBusCode()
					+ ",menuCode=" + service.getMenuCode()
					+ ",corpCode=" + service.getCorpCode()
					+ ",identifyMode=" + service.getIdentifyMode()
					+ ",orderType=" + service.getOrderType()
					+ ",structcode=" + service.getStructcode()	
					+ ",mobileUrl=" + mobileUrl
					+ ",phone=" + phone
					+ ",mainMsg=" + mainMsg
					+ ",serMoId=" + msId
			);
		}
		catch (Exception e)
		{
			//运营商补费
			balanceBiz.huishouFee(icount, service.getSpUser(), 1);
			
			EmpExecutionContext.error(e,"上行业务发送短信，短信发送异常。"
					+ ",PrId=" + process.getPrId()
					+ ",PrName=" + process.getPrName()
					+ ",PrType=" + process.getPrType()
					+ ",FinalState=" + process.getFinalState()
					+ ",serId=" + service.getSerId()
					+ ",userId=" + service.getUserId()
					+ ",serName=" + service.getSerName()
					+ ",commnets=" + service.getCommnets()
					+ ",orderCode=" + service.getOrderCode()
					+ ",subNo=" + service.getSubNo()
					+ ",spUser=" + service.getSpUser()
					+ ",runState=" + service.getRunState()
					+ ",serType=" + service.getSerType()
					+ ",msgSeparated=" + service.getMsgSeparated()
					+ ",ownerId=" + service.getOwnerId()
					+ ",createTime=" + service.getCreateTime()
					+ ",busCode=" + service.getBusCode()
					+ ",menuCode=" + service.getMenuCode()
					+ ",corpCode=" + service.getCorpCode()
					+ ",identifyMode=" + service.getIdentifyMode()
					+ ",orderType=" + service.getOrderType()
					+ ",structcode=" + service.getStructcode()	
					+ ",mobileUrl=" + mobileUrl
					+ ",phone=" + phone
					+ ",mainMsg=" + mainMsg
					+ ",serMoId=" + msId		
			);
			//1为向网关发送请求失败
			serLogBiz.updateServiceLog(slId, 1, mobileUrl);
			boolean updateRes = this.updateSerMoTask(msId, null, mobileUrl);
			EmpExecutionContext.info("上行业务发送短信，更新上行记录结果："+updateRes+"。serMoId="+msId);
			return null;
		}
		
		try
		{
			int index = result.indexOf("mterrcode");
			String resultReceive = result.substring(index + 10, index + 13);
			//发送失败
			if (!resultReceive.equals("000"))
			{
				//运营商补费
				balanceBiz.huishouFee(icount, service.getSpUser(), 1);
				
				resultReceive = result.substring(index - 8, index - 1);
				//1为向网关发送请求失败
				serLogBiz.updateServiceLog(slId, 1, mobileUrl);
				EmpExecutionContext.error("上行业务发送短信，发送失败，网关错误码："+resultReceive+"，serMoId="+msId);
			}
			else
			{
				//如果是网讯的发送，则插入任务表
				this.addWxLfMttask(service, taskId, mainMsg, mobileUrl, sendCount, wxIdsMap);
				//记录日志，0为成功向网关发送请求
				serLogBiz.updateServiceLog(slId, 0, mobileUrl);
				EmpExecutionContext.info("上行业务发送短信，发送成功。serMoId="+msId);
			}
			
			boolean updateRes = this.updateSerMoTask(msId, resultReceive, mobileUrl);
			EmpExecutionContext.info("上行业务发送短信，更新上行记录结果："+updateRes+"。serMoId="+msId);
			
			//返回结果
			return resultReceive;
		}
		catch(Exception e)
		{
			EmpExecutionContext.error(e,"上行业务发送短信，短信发送异常。"
					+ ",PrId=" + process.getPrId()
					+ ",PrName=" + process.getPrName()
					+ ",PrType=" + process.getPrType()
					+ ",FinalState=" + process.getFinalState()
					+ ",serId=" + service.getSerId()
					+ ",userId=" + service.getUserId()
					+ ",serName=" + service.getSerName()
					+ ",commnets=" + service.getCommnets()
					+ ",orderCode=" + service.getOrderCode()
					+ ",subNo=" + service.getSubNo()
					+ ",spUser=" + service.getSpUser()
					+ ",runState=" + service.getRunState()
					+ ",serType=" + service.getSerType()
					+ ",msgSeparated=" + service.getMsgSeparated()
					+ ",ownerId=" + service.getOwnerId()
					+ ",createTime=" + service.getCreateTime()
					+ ",busCode=" + service.getBusCode()
					+ ",menuCode=" + service.getMenuCode()
					+ ",corpCode=" + service.getCorpCode()
					+ ",identifyMode=" + service.getIdentifyMode()
					+ ",orderType=" + service.getOrderType()
					+ ",structcode=" + service.getStructcode()	
					+ ",mobileUrl=" + mobileUrl
					+ ",phone=" + phone
					+ ",mainMsg=" + mainMsg
					+ ",serMoId=" + msId		
			);
			//1为向网关发送请求失败
			serLogBiz.updateServiceLog(slId, 1, mobileUrl);
			return null;
		}
	}

	/**
	 * 获取短信内容中的网讯id，并返回map
	 * @param msg 短信内容
	 * @return 未使用网讯则返回null，否则返回网讯id Map，key为网讯id，value为网讯id，这个value后面要存放对应生成的网讯url
	 */
	private Map<String,String> getWxId(String msg){
		
		try{
			//key为id，value为id，这个value后面要存放对应的网讯url
			Map<String,String> wxIdsMap = new LinkedHashMap<String,String>();
			//判断是否有#W_1#格式中的网讯id
			Pattern pattern = Pattern.compile("(?<=#W_)(\\d+)(?=#)");
	        Matcher matcher = pattern.matcher(msg);
	        while(matcher.find()){
	        	wxIdsMap.put(matcher.group(),matcher.group());
	        	//由于网讯支持发送多个，所以这里只解析一个
	        	break;
	        }
	        
	        if(wxIdsMap == null || wxIdsMap.size() == 0){
	        	return null;
	        }

			return wxIdsMap;
		}catch(Exception e){
			EmpExecutionContext.error(e, "智能引擎获取网讯id异常。");
			return null;
		}
	}
	
	/**
	 * 获取网讯访问url
	 * @param wxIdsMap 网讯id Map，key为网讯id，value为对应生成的网讯url
	 * @param taskId 任务id
	 * @return 返回url字符串，异常返回null
	 */
	private Map<String,String> getWxUrl(Map<String,String> wxIdsMap, Long taskId){
		try
		{
			if(wxIdsMap == null || wxIdsMap.size() == 0){
				EmpExecutionContext.error("智能引擎获取网讯模板编码失败，网讯id Map集合为空。");
				return null;
			}
			
			String neturl = SystemGlobals.getValue("wx.pageurl") + "/w/";
			
			String netInfo = null;
			for(String key : wxIdsMap.keySet()){
				//根据网讯id编码
				netInfo = sendMsgInfo(key, taskId);
				if(netInfo == null || netInfo.length() == 0){
					continue;
				}
				//把id对应的url放到map的value中
				wxIdsMap.put(key, neturl + netInfo);
			}
			
			return wxIdsMap;
		}
		catch (Exception e)
		{
			EmpExecutionContext.error(e, "智能引擎获取网讯url编码异常。");
			return wxIdsMap;
		}
	}
	
	/**
	 * 把短信内容中的占位符替换成url
	 * @param msg 短信内容
	 * @param wxIdsMap 网讯id Map，key为#W_id#格式的网讯id，value为对应生成的网讯url
	 * @param phone 手机号
	 * @return 返回替换后的短信内容，无网讯则直接返回内容
	 */
	private String repWxMsg(String msg, Map<String,String> wxIdsMap, String phone){
		try
		{
			//无网讯则不替换短信内容
			if(wxIdsMap == null || wxIdsMap.size() == 0){
				return msg;
			}
			//编码手机号
			String phoneNum = CompressEncodeing.JieMPhone(phone);
			//网讯url
			String wxUrl = null;
			for(String key : wxIdsMap.keySet()){
				//value没值则跳过不替换
				if(wxIdsMap.get(key) == null || wxIdsMap.get(key).length() == 0){
					continue;
				}
				//生成网讯url
				wxUrl = wxIdsMap.get(key) + phoneNum+" ";
				//用网讯url替换短信内容中的占位符
				msg = msg.replace("#W_"+key+"#", wxUrl);
			}
			
			return msg;
		}
		catch (Exception e)
		{
			EmpExecutionContext.error(e, "智能引擎获取网讯模板编码异常。");
			return null;
		}
	}
	
	/**
	 * 保存网讯发送任务
	 * @param service
	 * @param taskId
	 * @param msg
	 * @param mobileUrl
	 * @param sendCount
	 * @param wxIdsMap
	 * @return
	 */
	private boolean addWxLfMttask(LfService service, Long taskId, String msg, String mobileUrl, Long sendCount, Map<String,String> wxIdsMap){
		try
		{
			if(wxIdsMap == null || wxIdsMap.size() == 0){
				return true;
			}
			boolean result = false;
			int count=0;
			//循环插入lfmttask
			for(String key : wxIdsMap.keySet()){
				//由于网讯只支持发送一个，所以这里只保存一个网讯任务
				if(count==0){
					result = this.addLfMttask(service, key, taskId, msg, mobileUrl, sendCount);
				}
				count++;
			}
			return result;
		}
		catch (Exception e)
		{
			EmpExecutionContext.error(e, "上行业务初始化任务对象异常。");
			return false;
		}
	}
	
	/**
	 * 保存网讯发送任务
	 * @param service
	 * @param netid
	 * @param taskId
	 * @param msg
	 * @param mobileUrl
	 * @param sendCount
	 * @return
	 */
	private boolean addLfMttask(LfService service, String netid, Long taskId, String msg, String mobileUrl, Long sendCount){
		
		try{
			if(netid == null || netid.length() == 0){
				return true;
			}
			LfMttask mttask = new LfMttask();
			SmsBiz smsBiz = new SmsBiz();
			Integer icount = smsBiz.countAllOprSmsNumber(service.getSpUser(), msg, 2, mobileUrl, null);
			mttask.setSubCount(sendCount);
			mttask.setEffCount(sendCount);
			mttask.setIcount(String.valueOf(icount));
			
			mttask.setMobileUrl(mobileUrl);
			mttask.setTitle(service.getSerName());
			mttask.setSpUser(service.getSpUser());
			mttask.setBmtType(2);
			mttask.setTimerStatus(0);
			mttask.setMsgType(2);
			//网讯发送
			mttask.setMsType(6);
			mttask.setSubState(2);
			mttask.setBusCode(service.getBusCode());//默认业务类型
			mttask.setMsg(msg);
			mttask.setSendstate(1);
			mttask.setCorpCode(service.getCorpCode());
			mttask.setSendLevel(0);//发送优先级 默认0系统智能控制
			mttask.setIsReply(0);//默认不需要回复
			mttask.setTaskId(taskId);
			//网讯模板
			mttask.setTempid(Long.valueOf(netid));
			mttask.setTimerTime(mttask.getSubmitTime());
			mttask.setUserId(service.getUserId());
			// 默认无需审批:0
			mttask.setReState(0);
			
			return empDao.save(mttask);
		}catch(Exception e){
			EmpExecutionContext.error(e, "上行业务初始化任务对象异常。");
			return false;
		}
	}
	
	/**
	 * 显示接收者信息
	 * 
	 * @param request
	 * @param response
	 */
	private String sendMsgInfo(String netid, Long taskId) {
		
		try {
			
			List<DynaBean> pagesList = new MoHandleDao().getWxPageById(netid);
			if(pagesList == null || pagesList.size() == 0){
				EmpExecutionContext.error("网讯模板获取失败！");
				return null;
			}
			
			String w = CompressEncodeing.CompressNumber(Long.parseLong(pagesList.get(0).get("id").toString()), 6);
			
			String url = w+"-";
			
			String t = CompressEncodeing.CompressNumber(taskId, 6);
			url += t;
			
			return url;
		} catch (Exception e) {
			EmpExecutionContext.error(e, "获取网讯地址URL异常!");
			return null;
		}
		
	}
	
	/**
	 * 更新上行业务记录表
	 * @param msId
	 * @param wgResult
	 * @param mobileUrl
	 * @return
	 */
	private boolean updateSerMoTask(Long msId, String wgResult, String mobileUrl){
		try{
			if(msId == null){
				return false;
			}
			LinkedHashMap<String, String> objectMap = new LinkedHashMap<String, String>();
			if("000".equals(wgResult)){
				//回复状态，1-成功，2和空-未回复，3-失败
				objectMap.put("replyState", "1");
			}else{
				//回复状态，1-成功，2和空-未回复，3-失败
				objectMap.put("replyState", "3");
			}
			objectMap.put("replyUrl", mobileUrl);
			
			LinkedHashMap<String, String> conditionMap = new LinkedHashMap<String, String>();
			conditionMap.put("msId", msId.toString());
			
			return empDao.update(LfMoService.class, objectMap, conditionMap);
		}catch(Exception e){
			EmpExecutionContext.error(e, "处理上行业务，更新上行业务记录失败。msId:"+msId);
			return false;
		}
	}
	
	/**
	 * 
	 * @param valuesMap
	 * @param content
	 * @return
	 */
	private String combineContent(LinkedHashMap<String, String> valuesMap,
			String content)
	{
		//把换行替换成空格
		content = content.replace("\r\n", " ");
		int i = 1;
		//循环获取参数并拼到内容中
		for (Map.Entry<String, String> m : valuesMap.entrySet())
		{
			String repWork = "#P_" + i + "#";
			String repWorkLow = "#p_" + i + "#";
			i++;
			if (m.getValue() == null)
			{
				//过滤#P_1#
				content = content.replace(repWork, "");
				//过滤#p_1#
				content = content.replace(repWorkLow, "");
			} else
			{
				//过滤#P_1#
				content = content.replace(repWork, m.getValue());
				//过滤#p_1#
				content = content.replace(repWorkLow, m.getValue());
			}
		}
		//返回内容
		return content;
	}

	/**
	 * 获取查询结果
	 * @param process
	 * @param msgPara
	 * @return 返回 结果集合，resultset每一行记录的list集合，里面的map为resultset每一行记录里的列值，key为列名，value为列值
	 * @throws Exception
	 */
	private List<LinkedHashMap<String, String>> queryProcessSql(LfProcess process, String[] msgPara) 
	{
		//数据库连接biz
		JDBCUtil jdbcUtil = new JDBCUtil();
		Connection conn = null;
		PreparedStatement pState = null;
		ResultSet resultSet = null;
		
		try
		{
			EmpExecutionContext.info("上行业务，执行查询，select步骤信息。"
					+ "PrId=" + process.getPrId()
					+ ",PrName=" + process.getPrName()
					+ ",PrType=" + process.getPrType()
					+ ",Sql=" + process.getSql()
					+ ",DbId=" + process.getDbId()
					+ ",FinalState=" + process.getFinalState()
					+ ",serId=" + process.getSerId()
			);
			//数据库操作连接对象
			LfDBConnect dbConnect = empDao.findObjectByID(LfDBConnect.class, process.getDbId());
			if(dbConnect == null)
			{
				EmpExecutionContext.error("上行业务，执行查询，获取数据库连接对象为空。"
						+ "PrId=" + process.getPrId()
						+ ",PrName=" + process.getPrName()
						+ ",PrType=" + process.getPrType()
						+ ",Sql=" + process.getSql()
						+ ",DbId=" + process.getDbId()
						+ ",FinalState=" + process.getFinalState()
						+ ",serId=" + process.getSerId()
				);
				return null;
			}
			
			//驱动
			String driver = jdbcUtil.getClassNameByDBType(dbConnect.getDbType());
			//加载驱动
			Class.forName(driver);
			//获取连接
			conn = DriverManager.getConnection(dbConnect.getConStr(),
					dbConnect.getDbUser(), dbConnect.getDbPwd());
			//获取sql语句
			String sql = process.getSql();
			EmpExecutionContext.sql("execute sql : " + sql);
			//预处理sql
			pState = conn.prepareStatement(sql);
			//执行sql
			resultSet = pState.executeQuery();
			//结果集合，resultset每一行记录
			List<LinkedHashMap<String, String>> rsList = new ArrayList<LinkedHashMap<String, String>>();
			Integer count = 0;
			//resultset每一行记录里的列值，key为列名，value为列值
			LinkedHashMap<String, String> valuesMap = null;
			while (resultSet.next())
			{
				valuesMap = new LinkedHashMap<String, String>();
				for (int i = 0; i < resultSet.getMetaData().getColumnCount(); i++)
				{
					valuesMap.put(resultSet.getMetaData().getColumnName(i + 1)
							.toLowerCase(), resultSet.getString(i + 1));
				}
				//保存结果
				rsList.add(valuesMap);
				count++;
			}
			//存数据行数等公共内容
			valuesMap = new LinkedHashMap<String, String>();
			valuesMap.put("resultcount", count.toString());
			
			for (int i = 0; i < msgPara.length; i++)
			{
				valuesMap.put("userpara_" + i, msgPara[i]);
			}
			//保存结果
			rsList.add(valuesMap);
			//返回结果
			return rsList;
		}
		catch(Exception e)
		{
			//异常处理
			EmpExecutionContext.error(e, "上行业务，执行查询，异常。"
					+ "PrId=" + process.getPrId()
					+ ",PrName=" + process.getPrName()
					+ ",PrType=" + process.getPrType()
					+ ",Sql=" + process.getSql()
					+ ",DbId=" + process.getDbId()
					+ ",FinalState=" + process.getFinalState()
					+ ",serId=" + process.getSerId()
			);
			return null;
		}
		finally
		{
			//关闭连接
			jdbcUtil.closeAll(conn, resultSet, pState);
		}
	}

	/**
	 * 执行语句
	 * @param process
	 * @param msgPara
	 * @return
	 * @throws Exception
	 */
	private List<LinkedHashMap<String, String>> execueProcessSql(
			LfProcess process, String[] msgPara) 
	{
		JDBCUtil jdbcUtil = new JDBCUtil();
		Connection conn = null;
		PreparedStatement pState = null;
		try
		{
			//结果集
			List<LinkedHashMap<String, String>> rsList = new ArrayList<LinkedHashMap<String, String>>();
			
			LfDBConnect dbConnect = empDao.findObjectByID(LfDBConnect.class, process.getDbId());
			
			String driver = jdbcUtil.getClassNameByDBType(dbConnect.getDbType());
			Class.forName(driver);
			//获取连接
			conn = DriverManager.getConnection(dbConnect.getConStr(),
					dbConnect.getDbUser(), dbConnect.getDbPwd());
			//获取sql
			String sql = process.getSql();
			EmpExecutionContext.sql("execute sql : " + sql);
			//预处理sql
			pState = conn.prepareStatement(sql);
			Integer result = 0;
			//执行sql
			result = pState.executeUpdate();
			LinkedHashMap<String, String> valuesMap = new LinkedHashMap<String, String>();
			valuesMap.put("infcounts", result.toString());
			for (int i = 0; i < msgPara.length; i++)
			{
				valuesMap.put("userpara_" + i, msgPara[i]);
			}
			//保存结果
			rsList.add(valuesMap);
			
			//返回结果
			return rsList;
			
		}
		catch(Exception e)
		{
			//异常处理
			EmpExecutionContext.error(e, "上行业务，执行sql，异常。"
					+ "PrId=" + process.getPrId()
					+ ",PrName=" + process.getPrName()
					+ ",PrType=" + process.getPrType()
					+ ",Sql=" + process.getSql()
					+ ",DbId=" + process.getDbId()
					+ ",FinalState=" + process.getFinalState()
					+ ",serId=" + process.getSerId()
			);
			return null;
		}
		finally
		{
			//关闭连接
			jdbcUtil.closeAll(conn, null, pState);
		}
		
	}
	

}
