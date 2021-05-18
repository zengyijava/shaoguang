package com.montnets.emp.common.timer.biz;

import java.net.InetAddress;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Random;

import com.montnets.emp.common.bean.CommandStatic;
import com.montnets.emp.common.bean.HttpReqParam;
import com.montnets.emp.common.biz.HttpBiz;
import com.montnets.emp.common.biz.SuperBiz;
import com.montnets.emp.common.constant.StaticValue;
import com.montnets.emp.common.context.EmpExecutionContext;
import com.montnets.emp.common.timer.TimerStaticValue;
import com.montnets.emp.common.timer.bean.TimeSerProperty;
import com.montnets.emp.common.timer.dao.TimeSerDAO;
import com.montnets.emp.common.timer.entity.LfTimeSerState;

public class TimeSerBiz extends SuperBiz
{
	private TimeSerDAO timeSerDAO = new TimeSerDAO();
	
	
	/**
	 * 
	 * @description 保存定时服务状态表
	 * @param serState 保存的定时服务状态记录对象
	 * @return 成功返回true
	 */
	public boolean SaveTimerSerState(TimeSerProperty property)
	{
		if(property == null)
		{
			EmpExecutionContext.error("保存定时服务状态表，传入参数对象为空。");
			return false;
		}
		try
		{
			//保存数据库，尝试3次
			int tryCount = 0;
			boolean res = false;
			while(!res)
			{
				if(tryCount > 3)
				{
					break;
				}
				res = timeSerDAO.SaveSerState(property);
				tryCount++;
			}
			return res;
		}
		catch (Exception e)
		{
			EmpExecutionContext.error(e, "保存定时服务状态表，异常。");
			return false;
		}
	}
	
	/**
	 * 
	 * @description 生成TimeServerID。格式：节点id+yyMMddHHmmss+6位随机数
	 * @return 返回TimeServerID
	 */
	public String GenerateTimeServerID()
	{
		try
		{
			//节点id
			String nodeId = StaticValue.getServerNumber();
			
			SimpleDateFormat sdf_yyyyMMddHHmmss = new SimpleDateFormat("yyyyMMddHHmmss");
			//时间戳
			String timestamp = sdf_yyyyMMddHHmmss.format(new Date());

			//6位随机数
			int radomInt = new Random().nextInt(999999);
			
			String timeServerID = nodeId + timestamp + radomInt;
			
			return timeServerID;
		}
		catch (Exception e)
		{
			EmpExecutionContext.error(e, "生成TimeServerID，异常。");
			return null;
		}
	}
	
	/**
	 * 
	 * @description 获取服务器IP
	 * @return 返回IP，异常返回null
	 */
	public String GetServerIP()
	{
		try
		{
			InetAddress adress = InetAddress.getLocalHost();
			return adress.getHostAddress();
		}
		catch (Exception e)
		{
			EmpExecutionContext.error(e, "获取服务器IP，异常。");
			return null;
		}
	}
	
	/**
	 * 
	 * @description 更新活动时间
	 * @param timeServerID 定时服务ID
	 * @return 成功返回true
	 * @author huangzb <huangzb@126.com>
	 * @datetime 2016-3-24 下午06:28:56
	 */
	public boolean updateSerStateActive(String timeServerID)
	{
		TimeSerProperty conditionObj = new TimeSerProperty();
		conditionObj.setTimeServerID(timeServerID);
		
		return timeSerDAO.UpdateTimeSerState(new TimeSerProperty(), conditionObj);
	}
	
	/**
	 * 
	 * @description 获取定时服务状态表记录
	 * @param timeServerID 定时服务ID
	 * @return 返回定时服务状态记录
	 * @author huangzb <huangzb@126.com>
	 * @datetime 2016-3-25 上午08:39:15
	 */
	public LfTimeSerState getTimeSerState(String timeServerID)
	{
		List<LfTimeSerState> serStateList = timeSerDAO.getTimeSerState(timeServerID);
		if(serStateList == null || serStateList.size() < 1)
		{
			return null;
		}
		return serStateList.get(0);
	}
	
	/**
	 * 
	 * @description 更新定时服务状态为未处理
	 * @param timeServerID 定时服务ID
	 * @return 成功返回true
	 * @author huangzb <huangzb@126.com>
	 * @datetime 2016-3-25 上午09:26:40
	 */
	public boolean updateSerStateStop(String timeServerID)
	{
		TimeSerProperty updateObj = new TimeSerProperty();
		//更新为未处理
		updateObj.setDealState(0);

		TimeSerProperty conditionObj = new TimeSerProperty();
		//条件为定时服务id
		conditionObj.setTimeServerID(timeServerID);
		//条件为定时服务处理中状态
		conditionObj.setDealState(1);
		
		return timeSerDAO.UpdateTimeSerState(updateObj, conditionObj);
	}
	
	/**
	 * 
	 * @description 发送http接管信息
	 * @param senderTimeServerID
	 * @param recerTimeServerID
	 * @param recerHttpUrl
	 * @return       			 
	 * @author huangzb <huangzb@126.com>
	 * @datetime 2016-3-26 下午02:04:47
	 */
	public boolean sendHttpReq(String senderTimeServerID, String recerTimeServerID, String recerHttpUrl)
	{
		try
		{
			// + TimerStaticValue.TIME_SER_REC_SVT
			if(recerHttpUrl == null || recerHttpUrl.trim().length() < 1)
			{
				EmpExecutionContext.info("定时服务处理，发送接管处理权，HttpUrl为空。" 
						+ "senderTimeServerID="+senderTimeServerID
						+ ",recerTimeServerID="+recerTimeServerID
						+ ",recerHttpUrl="+recerHttpUrl
						);
				return false;
			}
			
			//看最后没有/，没这补上
			if(!recerHttpUrl.substring(recerHttpUrl.length()-1).equals("/"))
			{
				recerHttpUrl += "/";
			}
			
			recerHttpUrl += CommandStatic.Check_Resp_SVT;
			
			HttpReqParam reqParam = new HttpReqParam();
			reqParam.setCommand(CommandStatic.CMD_TimeSer);
			reqParam.setSenderSerID(senderTimeServerID);
			reqParam.setRecerSerID(recerTimeServerID);
			reqParam.setDealState(TimerStaticValue.TIME_SER_DEAL_STATE_TRUE);
			
			HttpBiz httpBiz = new HttpBiz();
			
			int tryCount = 0;
			while(tryCount < 3)
			{
				tryCount++;
				
				String respRes = httpBiz.SendRequest(reqParam, recerHttpUrl, 5000, 5000);
				//没响应返回
				if(respRes == null || respRes.length() < 1)
				{
					EmpExecutionContext.error("定时服务处理，发送Http接管信息，无响应。"
							+ "senderTimeServerID="+senderTimeServerID
							+ ",recerTimeServerID="+recerTimeServerID
							+ ",recerHttpUrl="+recerHttpUrl
							);
					continue;
				}
				
				//返回响应为已接收
				if(TimerStaticValue.TIME_SER_RECE.equals(respRes))
				{
					EmpExecutionContext.info("定时服务处理，发送接管处理权成功。" 
							+ "senderTimeServerID="+senderTimeServerID
							+ ",recerTimeServerID="+recerTimeServerID
							+ ",recerHttpUrl="+recerHttpUrl
							);
					return true;
				}
				//返回响应为异常
				else if(TimerStaticValue.TIME_SER_RESP_ERR.equals(respRes))
				{
					EmpExecutionContext.info("定时服务处理，发送接管响应消息为异常。" 
							+ "senderTimeServerID="+senderTimeServerID
							+ ",recerTimeServerID="+recerTimeServerID
							+ ",recerHttpUrl="+recerHttpUrl
							);
					return false;
				}
				
				EmpExecutionContext.info("定时服务处理，发送接管处理权不成功。"
						+ "senderTimeServerID="+senderTimeServerID
						+ ",recerTimeServerID="+recerTimeServerID
						+ ",recerHttpUrl="+recerHttpUrl
						+ ",respRes="+respRes
						);
				
				SleepTime(1000);
			}
			
			return false;
		}
		catch (Exception e)
		{
			EmpExecutionContext.error(e, "定时服务处理，发送接管处理权，异常。");
			return false;
		}
	}
	
	private void SleepTime(long time)
	{
		try
		{
			Thread.sleep(time);
		}
		catch (Exception e)
		{
			EmpExecutionContext.info("强制退出休眠等待。");
		}
	}
}
