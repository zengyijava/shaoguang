package com.montnets.emp.common.biz;

import java.sql.Connection;
import java.sql.Timestamp;
import java.util.Calendar;

import com.montnets.emp.common.context.EmpExecutionContext;
import com.montnets.emp.entity.sms.LfMttask;
import com.montnets.emp.entity.sysuser.LfSysuser;
import com.montnets.emp.mmstask.DBMmsSend;
import com.montnets.emp.mmstask.IDBMmsSend;
import com.montnets.emp.mmstask.MmsWGParams;



/**
 * 彩信发送业务逻辑
 * @description 
 * @project p_ydcx
 * @company ShenZhen Montnets Technology CO.,LTD.
 * @author yejiangmin <282905282@qq.com>
 * @datetime 2013-10-18 上午09:21:53
 */
public class MttaskBiz extends SuperBiz
{

	/**
	 * 如果用户禁用了，那它其下的到时的定时任务则变成未发送，已撤销
	 * @param lfMttask
	 * @return
	 */
	public boolean ChangeSendState(LfMttask lfMttask)
	{
		boolean flag = false;
		BalanceLogBiz b = new BalanceLogBiz();
		//获取数据库连接
		Connection conn = empTransDao.getConnection();
		
		try {
			empTransDao.beginTransaction(conn);
			//lfMttask.setSendstate(2);
			//变成已撤销
			lfMttask.setSubState(3);
			lfMttask.setSucCount(null);
			lfMttask.setFaiCount(null);
			if(b.IsChargings(lfMttask.getUserId()))
			{
				//如果是彩信，则彩信退费
				if(lfMttask.getMsType()==2)
				{
					b.sendMmsAmountByUserId(conn, lfMttask.getUserId(), lfMttask.getEffCount()*-1);
				}
				else
				{
					 b.sendSmsAmountByUserId(conn, lfMttask.getUserId(), Integer.parseInt(lfMttask.getIcount())*-1);
				}
			}
			flag = empTransDao.update(conn, lfMttask);
			if(flag)
			{
				empTransDao.commitTransaction(conn);
			}
			else
			{
				empTransDao.rollBackTransaction(conn);
			}
		} catch (Exception e) {
			empTransDao.rollBackTransaction(conn);
			flag = false;
			EmpExecutionContext.error(e,"用户禁用了，撤销该用户的所有短彩任务异常！");
		}finally
		{
			if(conn !=null)
			{
				//关闭连接
				empTransDao.closeConnection(conn);
			}
		}
		return flag;
	}
	
	
	
	/**
	 *  *   
	 * @description     彩信发送调用接口
	 * @param mtId		任务ID
	 * @return			000表示成功
	 * @throws Exception       			 
	 * @author yejiangmin <282905282@qq.com>
	 * @datetime 2013-10-18 上午10:12:20
	 */
	public String sendMms(Long mtId) throws Exception{
	    //定义返回字符串
		String returnStr="subMmsErrer";
		//获取彩信任务对象
		//LfMttask lfMttask = empDao.findObjectByID(LfMttask.class, mtId);
		LfMttask lfMttask = new CommonBiz().getLfMttaskbyTaskId(mtId);
		if(lfMttask == null){
			EmpExecutionContext.error("查询彩信任务对象出现异常！");
			return returnStr;
		}
		lfMttask.setSendstate(2);
		//查找操作员
		LfSysuser lfSysuser=empDao.findObjectByID(LfSysuser.class, lfMttask.getUserId());
		
		//创建发送对象
		MmsWGParams mmsWGParams=new MmsWGParams();
		//设置发送账号
		mmsWGParams.setUserID(lfMttask.getSpUser());
		//LfMttask中bmtType字段 10普通彩信,11静态模板彩信,12动态模板彩信
		mmsWGParams.setMsgType(lfMttask.getBmtType());
		mmsWGParams.setTaskid(Integer.parseInt(String.valueOf(lfMttask.getTaskId())));
		//设置彩信标题
		mmsWGParams.setTitle(lfMttask.getTitle());
		//发送号码文件填写全路径
		mmsWGParams.setRemoteUrl(lfMttask.getFileuri()+lfMttask.getMobileUrl());
		mmsWGParams.setSendStatus(new Integer(1));
		if(lfMttask.getBmtType().intValue()==11||lfMttask.getBmtType().intValue()==12){
			//如果是模板彩信,则填模板ID
			mmsWGParams.setTmpID(Long.parseLong(lfMttask.getMsg()));
		}else{
		    //如果是普通彩信，则填彩信路径
			mmsWGParams.setMsg(lfMttask.getFileuri()+lfMttask.getMsg());
		}
		mmsWGParams.setSvrType(" ");
		mmsWGParams.setP1(lfSysuser.getUserCode());
		mmsWGParams.setP2(" ");
		mmsWGParams.setP3(" ");
		mmsWGParams.setP4(" ");
		mmsWGParams.setModuleID(0);
		//调用发送接口进行发送
		IDBMmsSend dbMmsSend=new DBMmsSend();
		returnStr=dbMmsSend.sendMms(mmsWGParams);
		//发送成功，则更新lfMttask的状态
		if(returnStr.equals("000")){
			lfMttask.setSendstate(1);
		}
		lfMttask.setSucCount(null);
		lfMttask.setFaiCount(null);
		lfMttask.setIcount(null);
		if (lfMttask.getTimerStatus().intValue() == 0) {
			lfMttask.setTimerTime(new Timestamp(Calendar.getInstance()
					.getTime().getTime()));
		}
		//更新lfMttask表
		empDao.update(lfMttask);
		return returnStr;
	}


	
	/**
	 *  *   
	 * @description     彩信发送调用接口
	 * @param mtId		任务ID
	 * @return			000表示成功
	 * @throws Exception       			 
	 * @author yejiangmin <282905282@qq.com>
	 * @datetime 2013-10-18 上午10:12:20
	 */
	public String sendMms(Long mtId,String fileUri) throws Exception{
	    //定义返回字符串
		String returnStr="subMmsErrer";
		//获取彩信任务对象
		//LfMttask lfMttask = empDao.findObjectByID(LfMttask.class, mtId);
		LfMttask lfMttask = new CommonBiz().getLfMttaskbyTaskId(mtId);
		if(lfMttask == null){
			EmpExecutionContext.error("查询彩信任务对象出现异常！");
			return returnStr;
		}
		else
		{
			//设置fileUri
			lfMttask.setFileuri(fileUri);
		}
		lfMttask.setSendstate(2);
		//查找操作员
		LfSysuser lfSysuser=empDao.findObjectByID(LfSysuser.class, lfMttask.getUserId());
		
		//创建发送对象
		MmsWGParams mmsWGParams=new MmsWGParams();
		//设置发送账号
		mmsWGParams.setUserID(lfMttask.getSpUser());
		//LfMttask中bmtType字段 10普通彩信,11静态模板彩信,12动态模板彩信
		mmsWGParams.setMsgType(lfMttask.getBmtType());
		mmsWGParams.setTaskid(Integer.parseInt(String.valueOf(lfMttask.getTaskId())));
		//设置彩信标题
		mmsWGParams.setTitle(lfMttask.getTitle());
		//发送号码文件填写全路径
		mmsWGParams.setRemoteUrl(lfMttask.getFileuri()+lfMttask.getMobileUrl());
		mmsWGParams.setSendStatus(new Integer(1));
		if(lfMttask.getBmtType().intValue()==11||lfMttask.getBmtType().intValue()==12){
			//如果是模板彩信,则填模板ID
			mmsWGParams.setTmpID(Long.parseLong(lfMttask.getMsg()));
		}else{
		    //如果是普通彩信，则填彩信路径
			mmsWGParams.setMsg(lfMttask.getFileuri()+lfMttask.getMsg());
		}
		mmsWGParams.setSvrType(" ");
		mmsWGParams.setP1(lfSysuser.getUserCode());
		mmsWGParams.setP2(" ");
		mmsWGParams.setP3(" ");
		mmsWGParams.setP4(" ");
		mmsWGParams.setModuleID(0);
		//调用发送接口进行发送
		IDBMmsSend dbMmsSend=new DBMmsSend();
		returnStr=dbMmsSend.sendMms(mmsWGParams);
		//发送成功，则更新lfMttask的状态
		if(returnStr.equals("000")){
			lfMttask.setSendstate(1);
		}
		lfMttask.setSucCount(null);
		lfMttask.setFaiCount(null);
		lfMttask.setIcount(null);
		if (lfMttask.getTimerStatus().intValue() == 0) {
			lfMttask.setTimerTime(new Timestamp(Calendar.getInstance()
					.getTime().getTime()));
		}
		//更新lfMttask表
		empDao.update(lfMttask);
		return returnStr;
	}
}
