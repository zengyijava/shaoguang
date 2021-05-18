/**
 * Program  : AlarmSMSTask.java
 * Author   : zousy
 * Create   : 2013-11-19 下午07:33:17
 * company ShenZhen Montnets Technology CO.,LTD.
 *
 */

package com.montnets.emp.contract.biz;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.LinkedHashMap;
import java.util.List;

import com.montnets.emp.common.biz.BalanceLogBiz;
import com.montnets.emp.common.biz.BaseBiz;
import com.montnets.emp.common.biz.GlobalVariableBiz;
import com.montnets.emp.common.biz.SmsBiz;
import com.montnets.emp.common.biz.SubnoManagerBiz;
import com.montnets.emp.common.constant.EMPException;
import com.montnets.emp.common.constant.ErrorCodeParam;
import com.montnets.emp.common.constant.IErrorCode;
import com.montnets.emp.common.constant.StaticValue;
import com.montnets.emp.common.context.EmpExecutionContext;
import com.montnets.emp.common.dao.IEmpTransactionDAO;
import com.montnets.emp.common.dao.impl.DataAccessDriver;
import com.montnets.emp.entity.pasgroup.Userdata;
import com.montnets.emp.entity.sms.LfMttask;
import com.montnets.emp.entity.sysuser.LfSysuser;
import com.montnets.emp.security.wgmsgconfig.WgMsgConfigBiz;
import com.montnets.emp.smstask.HttpSmsSend;
import com.montnets.emp.smstask.WGParams;
/**
 * 
 * @author   zousy
 * @version  1.0.0
 * @2013-11-19 下午07:33:17
 */
public class sendCrmSms extends Thread
{

	private IEmpTransactionDAO	smsTransDao = new DataAccessDriver().getEmpTransDAO();

	private SmsBiz smsbiz = new SmsBiz();
	
	public String sendMsg(String corpCode,String phone,String msg){
		LinkedHashMap<String, String> conditionMap = new LinkedHashMap<String, String>();
		//sp账号
		String sp = "";
		LfSysuser AdminUser = null; 
		//获取sp账号
		try
		{
			conditionMap.put("userName", "admin");//admin用户
			conditionMap.put("corpCode", corpCode);//当前登录用户的企业编码
			List<LfSysuser> AdminLfSysuerList = new BaseBiz().findListByCondition(LfSysuser.class, conditionMap, null) ;
			if(AdminLfSysuerList!=null && AdminLfSysuerList.size()>0)
			{
				AdminUser = AdminLfSysuerList.get(0);
			}
			String isUsable="";
			ErrorCodeParam errorCode =new ErrorCodeParam();
			//扩展尾号
			String subno=GlobalVariableBiz.getInstance().getValidSubno(StaticValue.VERIFYREMIND_MENUCODE, 0, corpCode,errorCode);
			//获取sp账号
			List<Userdata> spUserList=new SmsBiz().getSpUserListForReviewRemind(AdminUser);
			//spuserlist为空说明没有可用的账号
			//验证该发送账号的全通道号是否长度超过20位，绑定是否绑定了一条上下行路由
			//	发送账号
			//isUsable	1通过 	2失败  没有绑定一条上下行路由/下行路由	3失败 	发送账号没有绑定路由	 4 该发送账号的全通道号有一个超过20位
			if(spUserList.size()>0)
			{
				for(Userdata spUser : spUserList)
				{
					if(subno!=null)
					{
						isUsable=new SubnoManagerBiz().checkPortUsed(spUser.getUserId(), subno);
						if("1".equals(isUsable))
						{
							sp=spUser.getUserId();
							break;
						}
					}
					else
					{
						EmpExecutionContext.error("子号已使用完！");
					}
				}
			}
			
			if("".equals(sp)||sp==null){
				EmpExecutionContext.error("没有可用的sp账号！");
				return "nosp";
			}
		}
		catch (Exception e2)
		{
			EmpExecutionContext.error(e2,"获取sp账号失败！");
			return null;
		}

		// 总流程2:扣费
		BalanceLogBiz balanceBiz = new BalanceLogBiz();
		
		//是否扣费成功
		boolean isChargeSuc = false;
		// 判断流程是否结束的标识
		boolean okToSend = true;
		int icount = 0;
		try
		{
			String[] phoneList = null;
			if(phone.indexOf(",") != -1)
			{
				phoneList = phone.split(",");
			}
			else
			{
				phoneList = new String[1];
				phoneList[0] = phone;
			}
			//运营商号段
			String[] haoduans = new WgMsgConfigBiz().getHaoduan();
			icount = getSMSCount(sp, msg, phoneList, haoduans);
			LfMttask mTask = new LfMttask();
			mTask.setIcount(String.valueOf(icount));// 发送短信总数(网关发送总数)
			mTask.setSpUser(sp);
			mTask.setCorpCode(corpCode);
			String wgresult = "";
			try
			{
				wgresult = balanceBiz.wgKoufei(mTask);
			}
			catch (Exception e)
			{
				EmpExecutionContext.error(e, "签约状态短信：" + IErrorCode.B20011);
				throw new EMPException("签约状态短信：" + IErrorCode.B20011);
			}
			if("nogwfee".equals(wgresult) || "feefail".equals(wgresult) || "lessgwfee".equals(wgresult.substring(0, 9)))
			{
				okToSend = false;
			}
			if(okToSend && balanceBiz.IsChargings(AdminUser.getUserId()))
			{
				int feeResult = sendAlarmSmsAmountByUserId(AdminUser.getUserId(), 1);
				// 0:短信扣费成功
				if(feeResult == 0)
				{
					isChargeSuc = true;
				}
				// -2:短信余额不足
				else
					if(feeResult == -2)
					{
						EmpExecutionContext.error("短信余额不足");
						okToSend = false;
					}
					else
					{
						EmpExecutionContext.error("短信扣费失败未知错误:" + feeResult);
						okToSend = false;
					}
			}
		}
		catch (Exception e)
		{
			okToSend = false;
			EmpExecutionContext.error(e, "签约状态短信发送异常。");
			return null;
		}
		// 总流程3：发送短信
		String[] result = null;
		if(okToSend){
			try
			{
				result = sendmsg(sp, "", msg, phone, AdminUser.getUserCode());
			}
			catch (Exception e)
			{
				EmpExecutionContext.error(e, "签约状态短信发送异常！");
				result =new String[]{"exception"};
			}
			
			//发送成功
			if(result != null&&"000".equals(result[0])){
			}else{
				// 运营商余额回收
				if(okToSend)
				{
					balanceBiz.huishouFee(icount,sp, 1);
				}
				//机构费用回收
				if(isChargeSuc)
				{
					sendAlarmSmsAmount(AdminUser, 0-icount);
				}
			}
		}
		if(result != null){
			return result[0];
		}else{
			return null;
		}
	}
	/**
	 * 发送接口，下行阀值提醒的结果
	 * @param sp sp账号
	 * @param subno 子号
	 * @param msg 内容
	 * @param phone 手机号
	 * @param userCord 用户编码
	 * @return
	 * @throws Exception
	 */
	public String[] sendmsg(String sp,String subno,String msg,String phone,String userCord) throws Exception {
		
		WgMsgConfigBiz wgMsgBiz = new WgMsgConfigBiz();
		HttpSmsSend jobBiz = new HttpSmsSend();
		String[] resultReceive = new String[2];
		Userdata userdata = wgMsgBiz.getSmsUserdataByUserid(sp);
		String webGateResult;
		WGParams wgParams = new WGParams();
		wgParams.setCommand("MT_REQUEST");
		//spuser帐户
		wgParams.setSpid(sp);
		//密码
		wgParams.setSppassword(userdata.getUserPassword());	
		//用户编码
		wgParams.setParam1(userCord);	
		//拓展子号
		wgParams.setSa(subno);		
		//手机
		wgParams.setDa(phone);			
		wgParams.setPriority("1");
		wgParams.setSm(msg);
		//默认业务编码
		wgParams.setSvrtype("M00000");	
		//模块ID
		wgParams.setModuleid(StaticValue.VERIFYREMIND_MENUCODE);	
		//设值为0的不需要状态报告，其他值是需要状态报告
		wgParams.setRptflag("0");		
		webGateResult = jobBiz.createbatchMtRequest(wgParams);
		resultReceive[1] = parseWebgateResult(webGateResult, "mtmsgid");
		int index = webGateResult.indexOf("mterrcode");
		resultReceive[0] = webGateResult.substring(index+10,index+13);
		if(!resultReceive[0].equals("000")
		){
			resultReceive[0] = webGateResult.substring(index-8,index-1);
			EmpExecutionContext.debug("签约状态短信到短信网关返回错误代码："+resultReceive[0]);
		}
			
		return resultReceive;
	}
	private String parseWebgateResult(String webGateResult, String param) {
		
		String[] testArray = webGateResult.split("&");
		
		String strParam;
		int index = -1;
		String value ="";
		for(int i = 0; i < testArray.length; i++){
			strParam = testArray[i];
			index = strParam.indexOf(param+"=");
			if(index == -1){
				continue;
			}
			
			value = strParam.substring(index+8);
			break;
		}
		
		return value;
	}
	
	/**
	 * 阀值提醒短信扣费
	 * @description    
	 * @param connection
	 * @param userId
	 * @param sendCount
	 * @return       			 
	 * @author zousy <zousy999@qq.com>
	 * @datetime 2013-11-28 下午07:21:21
	 */
	public int sendAlarmSmsAmountByUserId(Long userId,int sendCount){
		try {
			return sendAlarmSmsAmount(new BaseBiz().getById(LfSysuser.class, userId), sendCount);
		} catch (Exception e) {
			EmpExecutionContext.error(e, "短信发送扣费回收接口调用异常！");
			return StaticValue.EXP_RETURN;
		}
		
	}
	/**
	 * 阀值提醒短信扣费回收 
	 * @description    
	 * @param connection
	 * @param lfSysuser
	 * @param sendCount 大于0扣费 
	 * @return       			 
	 * @author zousy <zousy999@qq.com>
	 * @datetime 2013-11-28 下午07:24:31
	 */
	private int sendAlarmSmsAmount(LfSysuser lfSysuser,int sendCount)
	{
		//默认返回失败
		int result = -1;
		//操作标识
  		long depId = lfSysuser.getDepId();
		CallableStatement cs = null;
		Connection connection = null;
		try {
				connection = smsTransDao.getConnection();
			//调用存储过程
			try {
				cs = connection.prepareCall("{call LF_FEEDEDTV0(?,?,?)}");
				cs.setLong(1, depId);
				cs.setLong(2, sendCount);
				cs.registerOutParameter(3, java.sql.Types.INTEGER);
				cs.execute();
				//返回值
				result = cs.getInt(3);
			} catch (SQLException e) {
				EmpExecutionContext.error(e, "签约状态短信发送扣费调用异常!");
				if(sendCount > 0)
				{
					//短信扣费失败
					result = -1;
				}
				else
				{
					//短信回收失败
					result = -9;
				}
			}
		} catch (Exception e) {
			EmpExecutionContext.error(e, "签约状态短信发送扣费调用异常!");
			result = -9999;
		}finally{		
			try
			{
				if (cs != null)
				{
					cs.close();
				}
				if(connection != null){
					smsTransDao.closeConnection(connection);
				}
			}
			catch (SQLException e)
			{
				EmpExecutionContext.error(e, "短信发送扣费回收接口关闭存储过程异常!");
			}

		}
		return result;
	}
	
	/**
	 * 短信条数
	 * @description    
	 * @param sp
	 * @param msg
	 * @param phoneList
	 * @return       			 
	 * @author chentingsheng <cts314@163.com>
	 * @datetime 2014-4-8 下午04:46:17
	 */
	public int getSMSCount(String sp, String msg, String[] phoneList, String[] haoduans)
	{
		
		int count = 0;
		try
		{
			for (int i = 0; i < phoneList.length; i++)
			{
				count += smsbiz.countAllOprSmsNumber(msg, phoneList[i], haoduans, sp);
			}
		}
		catch (Exception e)
		{
			EmpExecutionContext.error(e, "统计发送条数出现异常！");
			count = 0;
		}
		return count;
	}
	 @Override
     public void run() {
		 //TODO
	 }
}

