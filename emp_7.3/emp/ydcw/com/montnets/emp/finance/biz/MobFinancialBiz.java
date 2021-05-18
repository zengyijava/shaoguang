package com.montnets.emp.finance.biz;

import java.sql.Connection;
import java.sql.Timestamp;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import com.montnets.emp.common.biz.BalanceLogBiz;
import com.montnets.emp.common.biz.SendMessage;
import com.montnets.emp.common.biz.SubnoManagerBiz;
import com.montnets.emp.common.constant.StaticValue;
import com.montnets.emp.common.constant.SystemGlobals;
import com.montnets.emp.common.constant.WebgatePropInfo;
import com.montnets.emp.common.context.EmpExecutionContext;
import com.montnets.emp.common.dao.DepSpecialDAO;
import com.montnets.emp.common.dao.IEmpDAO;
import com.montnets.emp.common.dao.impl.DataAccessDriver;
import com.montnets.emp.common.dao.impl.GenericEmpTransactionDAO;
import com.montnets.emp.common.timer.TaskManagerBiz;
import com.montnets.emp.entity.approveflow.LfFlowRecord;
import com.montnets.emp.entity.sms.LfMttask;
import com.montnets.emp.entity.sms.LfTask;
import com.montnets.emp.entity.system.LfTimer;
import com.montnets.emp.entity.sysuser.LfFlow;
import com.montnets.emp.entity.sysuser.LfReviewer2level;
import com.montnets.emp.entity.sysuser.LfSysuser;
import com.montnets.emp.finance.util.YdcwErrorStatus;
import com.montnets.emp.smstask.HttpSmsSend;
import com.montnets.emp.smstask.WGParams;

public class MobFinancialBiz
{
	protected IEmpDAO					ydcwDAO			= new DataAccessDriver().getEmpDAO();

	protected GenericEmpTransactionDAO	tranDao			= new GenericEmpTransactionDAO();

	private SubnoManagerBiz				subnoManagerBiz	= new SubnoManagerBiz();

	public MobFinancialBiz()
	{

	}

	/**
	 * 移动财务短信发送
	 * 
	 * @param mt 任务对象
	 * @param user 操作员对象，定时调用时上层传入
	 * @param isAdd 是否新增。定时调用时值为否
	 * @return 
	 * @throws Exception
	 *        
	 */
	public String addSmsLfMttask(LfMttask mt)
	{
		BalanceLogBiz b = new BalanceLogBiz();
		LfSysuser user = null;
		if(mt.getIsReply() == 1)
		{
			// 需要回复，设置尾号生命周期
			// boolean isSuccess =
			// subnoManagerBiz.updateSubnoStat(mt.getSubNo(), mt.getCorpCode(),
			// StaticValue.VALIDITY,null);
			boolean isSuccess = subnoManagerBiz.updateSubnoStat(mt.getSubNo(), mt.getCorpCode(), StaticValue.VALIDITY);
			if(!isSuccess)
			{
				EmpExecutionContext.error("尾号处理异常【拓展尾号：" + mt.getSubNo() + "------企业编码：" + mt.getCorpCode());
				return "subnoFailed";
			}
		}
		// 审批状态
		mt.setReState(0);
		// 发送状态
		mt.setSendstate(0);

		// 获得连接
		Connection taskConn = null;
		try
		{
			user = ydcwDAO.findObjectByID(LfSysuser.class, mt.getUserId());
			taskConn = tranDao.getConnection();
			// 开启事务
			tranDao.beginTransaction(taskConn);

			// 保存LfMttask对象
			if(!tranDao.save(taskConn, mt))
			{
				EmpExecutionContext.error("移动财务保存短信任务表失败！");
				tranDao.rollBackTransaction(taskConn);
				return YdcwErrorStatus.ECWB110;
			}
			
			int sendCount = Integer.parseInt(mt.getIcount());
			String spUser = mt.getSpUser();
			//检查sp余额是否足够发送
			int spResult = b.checkSpUserFee(spUser, sendCount, 1);
			if(spResult<0){
				//回滚
				tranDao.rollBackTransaction(taskConn);
				if(spResult == -3)
				{
					//没有账号信息
					EmpExecutionContext.error("查询不到sp账号信息。spuser:"+spUser);
					return "noSpInfo";
				}
				else if(spResult == -2)
				{
					//余额不足
					EmpExecutionContext.error("sp账号余额不足。spuser:"+spUser);
					return "noSuffiSpFee";
				}else{
					EmpExecutionContext.error("查询sp账号信息异常。spuser:"+spUser);
					return "spFail";
				}
			}
			
			
			boolean isDepFeeFlag = SystemGlobals.isDepBilling(mt.getCorpCode());
			Map<String,String> infoMap = new HashMap<String,String>();
			infoMap.put("feeFlag", String.valueOf(isDepFeeFlag));
			//机构扣费
			int depResult = b.depKoufei(taskConn, mt,sendCount , infoMap);
			//机构扣费失败
			if(depResult < 0)
			{
				EmpExecutionContext.error("机构扣费失败！");
				tranDao.rollBackTransaction(taskConn);
				if(depResult == -2)
				{
					return "noMoney";
				}
				else
				{
					return "payFailure";
				}
			}
			//运营商扣费
			String wgresult = b.wgKoufei(mt);
			// 运营商扣费失败
			if("nogwfee".equals(wgresult) || "feefail".equals(wgresult) || "lessgwfee".equals(wgresult.substring(0, 9)))
			{
				tranDao.rollBackTransaction(taskConn);
				//机构扣费成功需要回收
				if(depResult>=0){
					b.sendSmsAmountByUserId(null, mt.getUserId(), -1 * sendCount);
				}
				return wgresult;
			}
			
			
			if(mt.getTimerStatus().intValue() == 1)
			{
				TaskManagerBiz tm = new TaskManagerBiz();
				MobFinancialTimerTask mobFinancialTimerTask = new MobFinancialTimerTask(mt.getTitle(), new Date(mt.getTimerTime().getTime()), String.valueOf(mt.getTaskId()));
				mobFinancialTimerTask.setMtId(mt.getTaskId());
				boolean flag = tm.setJob(mobFinancialTimerTask);
				if(flag)
				{
					tranDao.commitTransaction(taskConn);
					return "timerSuccess";
				}
				else
				{
					//modify by tanglili20140510必须要先事务回滚，再进行其他操作。否则，会锁住表。之前操作了机构余额表，必须得回滚后，再操作机构余额表进行退费
					tranDao.rollBackTransaction(taskConn);
					//运营商扣费回收
					b.huishouFee(sendCount, spUser, 1);
					//机构扣费成功需要回收
					if(depResult>=0){
						b.sendSmsAmountByUserId(null, mt.getUserId(), -1 * sendCount);
					}
					return "定时发送失败！";
				}
			}else
			{
				tranDao.commitTransaction(taskConn);
			}
		}
		catch (Exception ee)
		{
			if(taskConn != null)
			{
				tranDao.rollBackTransaction(taskConn);
			}
			EmpExecutionContext.error(ee,"发送任务失败！");
		}
		finally
		{
			if(taskConn != null)
			{
				// 关闭连接对象
				tranDao.closeConnection(taskConn);
			}
		}
		//执行发送
		return sendSms(mt,user);
	}

	/**
	 * 移动财务短信重发
	 * 
	 * @param mt
	 * @return
	 * @throws Exception
	 */
	public String retryLfMttask(LfMttask mt) throws Exception
	{
		return addSmsLfMttask(mt);
	}

	/**
	 * 得到发送任务对象
	 * 
	 * @param mtId
	 * @return
	 * @throws Exception
	 */
	public LfTask getLfTaskObj(LfMttask lfMttask, LfSysuser user, long taskId) throws Exception
	{
		try
		{
			LfTask lfTask = new LfTask();
			lfTask.setMtId(lfMttask.getMtId());
			lfTask.setTaskType(1);
			lfTask.setSpUser(lfMttask.getSpUser());
			lfTask.setUserName(user.getUserName());
			lfTask.setDepId(user.getDepId());
			lfTask.setCorpCode(lfMttask.getCorpCode());
			lfTask.setUserId(lfMttask.getUserId());
			lfTask.setTaskId(taskId);
			return lfTask;
		}
		catch (Exception e)
		{
			EmpExecutionContext.error(e,"获取发送任务失败");
			return null;
		}
	}

	/**
	 * 定时短信发送
	 * 
	 * @param taskid
	 * @return
	 * @throws Exception
	 */
	public String sendSms(LfMttask mt,LfSysuser user) 
	{
		// 模板ID
		String moduleid = "";
		if("MF0001".equals(mt.getBusCode()))
		{
			moduleid = "10260";
		}
		else
		if("MF0002".equals(mt.getBusCode()))
		{
			moduleid = "10270";
		}
		else
		if("MF0003".equals(mt.getBusCode()))
		{
			moduleid = "10280";
		}
		else
		{
			moduleid = "";
		}
		String returnStr = "";
		mt.setSendstate(2);
		try
		{
			String result = "";
			// 非定时短信发送(0-不定时发送)
			result = createbatchMtRequestWithCode(mt.getSpUser(), mt.getSpPwd(), mt.getSubNo(), "2", mt.getTaskId().toString(), mt.getTitle(), mt.getMsg(), "1", (mt.getFileuri()+mt.getMobileUrl()), mt.getParams(), mt.getBusCode(), user.getUserCode(), moduleid, "0");
			if(!result.equals(""))
			{
				int index = result.indexOf("mterrcode");
				returnStr = result.substring(index + 10, index + 13);
				if(returnStr.equals("000"))
				{
					mt.setSendstate(1);
					returnStr = "000";
				}
				else
				{
					BalanceLogBiz b = new BalanceLogBiz();
					b.huishouFee(Integer.parseInt(mt.getIcount()), mt.getSpUser(), 1);
					b.sendSmsAmountByUserId(null, mt.getUserId(), -1 * Integer.parseInt(mt.getIcount()));
					returnStr = result.substring(index - 8, index - 1);
				}
			}
			mt.setErrorCodes(returnStr);
			if(mt.getTimerStatus().intValue() == 0)
			{
				mt.setTimerTime(new Timestamp(Calendar.getInstance().getTime().getTime()));
			}
			
		}catch(Exception e)
		{
			EmpExecutionContext.error(e,"移动财务发送异常！");
			mt.setErrorCodes("sendError");
		}finally
		{
			//成功数
			mt.setSucCount(null);
			//失败数
			mt.setFaiCount(null);
		
			SendMessage sendMess = new SendMessage();
			int a = 1;
			//如果多次更新失败，则重复更新
			while (a<4) {
				try {
					if(sendMess.updateMttaskByTaskid(mt))
					{
						a=4;
					}
					else 
					{
						a++;
					}
				} catch (Exception e2) {
					try {
						Thread.sleep(500L);
					} catch (Exception e3) {
					}finally
					{
						EmpExecutionContext.error(e2,"移动财务更新任务表失败：第"+a+"次");
						a++;
					}
				}
			}
		}
		return returnStr;
	}

	/**
	 * 参数编码转换
	 * 
	 * @param code
	 * @return
	 * @return String
	 * @author Jinny.Ding
	 * @date May 30, 2012
	 */
	public String toHexString(String code)
	{
		String result = "";
		if("".equals(code))
		{
			return "";
		}
		for (int i = 0; i < code.length(); i++)
		{
			int ch = (int) code.charAt(i);
			String hexStr = Integer.toHexString(ch);
			result = result + hexStr;
		}
		return result;
	}

	/**
	 * @param mtID
	 * @return
	 * @throws Exception
	 */
	private boolean cancelLfMttask(Long mtID) throws Exception
	{
		TaskManagerBiz taskManagerBiz = new TaskManagerBiz();
		List<LfTimer> lfTimerList = taskManagerBiz.getTaskByExpression(String.valueOf(mtID));
		LfTimer lfTimer = null;
		if(lfTimerList != null && lfTimerList.size() > 0)
		{
			lfTimer = lfTimerList.get(0);
		}
		else
		{
			return true;
		}
		boolean flag = taskManagerBiz.stopTask(lfTimer.getTimerTaskId());
		return flag;
	}

	/**
	 * 如果用户禁用了，那它其下的到时的定时任务则变成未发送，已撤销
	 * 
	 * @param lfMttask
	 * @return
	 */
	public boolean ChangeSendState(LfMttask lfMttask)
	{
		boolean flag = false;
		BalanceLogBiz b = new BalanceLogBiz();
		// 获取连接
		Connection conn = tranDao.getConnection();

		try
		{
			// 开启事务
			tranDao.beginTransaction(conn);
			// lfMttask.setSendstate(2);
			lfMttask.setSubState(3);// 变成已撤销
			lfMttask.setSucCount(null);
			lfMttask.setFaiCount(null);
			if(b.IsChargings(lfMttask.getUserId()))
			{
				// 如果发送状态不等于发送成功,则需要在此补回
				if(lfMttask.getMsType() == 2)
				{
					b.sendMmsAmountByUserId(conn, lfMttask.getUserId(), lfMttask.getEffCount() * -1);
				}
				else
				{
					b.sendSmsAmountByUserId(conn, lfMttask.getUserId(), Integer.parseInt(lfMttask.getIcount()) * -1);
				}
			}
			// 修改
			flag = tranDao.update(conn, lfMttask);
			if(flag)
			{
				// 提交事务
				tranDao.commitTransaction(conn);
			}
			else
			{
				// 事务回滚
				tranDao.rollBackTransaction(conn);
			}
		}
		catch (Exception e)
		{
			// 事务回滚
			tranDao.rollBackTransaction(conn);
			flag = false;
			EmpExecutionContext.error(e, "移动财务更新状态失败！ ");
		}
		finally
		{
			// 关闭连接
			if(conn != null)
			{
				tranDao.closeConnection(conn);
			}
		}
		return flag;
	}
	
	 /**
     * 群发接口。群发参数未封转前传入
     * @param spUserid 发送账户
     * @param spPassword 发送账户密码
     * @param sa 下行源地址
     * @param bmttype 提交类型
     * @param taskId 任务id
     * @param title 标题
     * @param content 内容
     * @param priority 优先级
     * @param url 群发文件地址
     * @param verifycode 8字节验证码
     * @param svrtype 业务类型
     * @param userCode 用户编码
     * @param moduleid 模块id
     * @param rptflag 是否需要状态报告
     * @return 请求网关返回的字符串
     * @throws Exception
     */
	public String createbatchMtRequestWithCode(String spUserid,String spPassword, 
			String sa, String bmttype,String taskId,String title,String content,
			String priority,String url, String verifycode,String svrtype,String userCode,
			String moduleid,String rptflag) throws Exception
	{

	    //创建发送对象
	    WGParams wgParams = new WGParams();
		wgParams.setSpid(spUserid);
		wgParams.setSppassword(spPassword);
		if(sa != null && !"".equals(sa.trim())){
			wgParams.setSa(sa);
		}
		wgParams.setBmttype("2");
		wgParams.setTaskid(taskId);
		wgParams.setPriority(priority);
		wgParams.setTitle(title);
		
		wgParams.setUrl(url);
		
		
		wgParams.setParam1(userCode);
		wgParams.setSvrtype(svrtype);
		wgParams.setRptflag(rptflag);
		wgParams.setModuleid(moduleid);
		
		wgParams.setVerifycode(verifycode);
		wgParams.setDc("25");
		//当是相同内容群发时，需要设置发送内容
		switch(Integer.parseInt(bmttype))
		{
			case 1:wgParams.setContent(content);break;
			case 2:break;
			case 3:break;		
		}
		HttpSmsSend httpSmsSend = new HttpSmsSend();
		//调用发送接口
		return httpSmsSend.createbatchMtRequest(wgParams);
	}
}
