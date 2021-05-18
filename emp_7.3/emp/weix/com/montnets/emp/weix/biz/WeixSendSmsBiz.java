package com.montnets.emp.weix.biz;

import java.sql.Connection;
import java.util.LinkedHashMap;
import java.util.List;

import sun.awt.image.ImageWatched.Link;

import com.montnets.emp.common.biz.BalanceLogBiz;
import com.montnets.emp.common.biz.SuperBiz;
import com.montnets.emp.security.wgmsgconfig.WgMsgConfigBiz;
import com.montnets.emp.common.context.EmpExecutionContext;
import com.montnets.emp.entity.pasgroup.Userdata;
import com.montnets.emp.entity.sysuser.LfSysuser;
import com.montnets.emp.entity.weix.LfWcUlink;
import com.montnets.emp.entity.weix.LfWcUserInfo;
import com.montnets.emp.smstask.HttpSmsSend;
import com.montnets.emp.smstask.WGParams;
import com.montnets.emp.weix.biz.i.IWeixSendSmsBiz;

/**
 * 发送验证码
 * 
 * @author Administrator
 */
public class WeixSendSmsBiz extends SuperBiz implements IWeixSendSmsBiz
{

	/**
	 * 发送验证码的方法
	 * 
	 * @param lfExamineSms
	 *        null
	 * @param msgInfo
	 *        发送内容
	 * @param corpCode
	 *        企业编码
	 * @param sp
	 *        发送账户
	 * @param subno
	 *        尾号
	 * @param balanceBiz
	 * @param count
	 *        发送条数
	 * @param senderUser
	 *        发送者
	 * @param flag
	 *        是否计费
	 * @param Rptflag
	 *        是否需要返回状态报告
	 * @return
	 * @throws Exception
	 */
	public String sendOneSms(String phone, String msg, String sp, String subno, Integer count, LfSysuser senderUser, boolean flag, String Rptflag, String menucode) throws Exception
	{
		String result = "2";
		// 如果启用了计费
		if(flag)
		{
			Connection conn = empTransDao.getConnection();
			try
			{
				empTransDao.beginTransaction(conn);
				// 先进行扣费操作
				int resultMsg = new BalanceLogBiz().sendSmsAmountByGuid(conn, senderUser.getGuId(), count);
				if(resultMsg == 0)
				{

					// 扣费成功，则进行发送操作
					String[] wegGateResult = this.SendOne(senderUser, menucode, msg, phone, sp, subno, Rptflag);
					if(wegGateResult == null || wegGateResult.length == 0)
					{
						empTransDao.rollBackTransaction(conn);
						// 未发送成功
						result = "2";
					}
					else
						if("000".equals(wegGateResult[0]))
						{
							// 发送到网关成功
							result = "1," + wegGateResult[1];
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
						// 短信余额不足
						result = "6";
						empTransDao.rollBackTransaction(conn);
					}
					else
						if(resultMsg == -1)
						{
							// 短信扣费失败
							result = "7";
							empTransDao.rollBackTransaction(conn);
						}
						else
							if(resultMsg == -4)
							{
								// 用户所属机构没有充值
								result = "9";
								empTransDao.rollBackTransaction(conn);
							}
							else
							{
								empTransDao.rollBackTransaction(conn);
							}
			}
			catch (Exception e)
			{
				empTransDao.rollBackTransaction(conn);
				EmpExecutionContext.error(e,"EMP企业微信发送验证码失败！");
			}
			finally
			{
				empTransDao.closeConnection(conn);
			}
		}
		else
		{
			try
			{
				// 调用发送接口
				String[] wegGateResult = this.SendOne(senderUser, menucode, msg, phone, sp, subno, Rptflag);
				if(wegGateResult == null || wegGateResult.length == 0)
				{
					// 未发送成功
					result = "2";
					return result;
				}
				if("000".equals(wegGateResult[0]))
				{
					// 发送到网关成功
					result = "1," + wegGateResult[1];
				}
			}
			catch (Exception e)
			{
				EmpExecutionContext.error(e,"EMP企业微信发送验证码失败！");
			}
		}
		return result;
	}

	/**
	 * 发送一条短信接口
	 * 
	 * @param lfsysuser
	 * @param menucode
	 * @param msg
	 * @param phone
	 * @param sp
	 * @param subno
	 * @param Rptflag
	 * @return
	 * @throws Exception
	 */
	private String[] SendOne(LfSysuser lfsysuser, String menucode, String msg, String phone, String sp, String subno, String Rptflag) throws Exception
	{

		if((phone == null || "".equals(phone.trim())))
		{
			EmpExecutionContext.error("发送号码或文件为空！");
			return null;
		}
		WgMsgConfigBiz wgMsgBiz = new WgMsgConfigBiz();
		HttpSmsSend jobBiz = new HttpSmsSend();
		String[] resultReceive = new String[2];
		if(subno == null)
		{
			EmpExecutionContext.error("子号对象为空！");
			return null;
		}
		Userdata userdata = wgMsgBiz.getSmsUserdataByUserid(sp);
		String webGateResult;
		WGParams wgParams = new WGParams();
		wgParams.setCommand("MT_REQUEST");
		// spuser帐户
		wgParams.setSpid(sp);
		// 密码
		wgParams.setSppassword(userdata.getUserPassword());
		// 用户编码 p1
		wgParams.setParam1(String.valueOf(lfsysuser.getUserCode()));
		// p2
		//此处赋值错误，可以不设值 调用方法内部有为null时的赋值处理
//		wgParams.setParam2(String.valueOf(lfsysuser.getDepId()));
		// 拓展子号
		wgParams.setSa(subno);
		// 手机
		wgParams.setDa(phone);
		// 优先级（越小优先级越高）
		wgParams.setPriority("1");
		// 短信内容
		wgParams.setSm(msg);
		// 默认业务编码
		wgParams.setSvrtype("M00000");
		// 模块ID
		wgParams.setModuleid(menucode);
		// 设值为0的不需要状态报告，其他值是需要状态报告
		wgParams.setRptflag(Rptflag);
		// wgParams.setMsgid(taskId.toString());
		webGateResult = jobBiz.createbatchMtRequest(wgParams);
		resultReceive[1] = this.parseWebgateResult(webGateResult, "mtmsgid");
		int index = webGateResult.indexOf("mterrcode");
		resultReceive[0] = webGateResult.substring(index + 10, index + 13);
		if(!resultReceive[0].equals("000"))
		{
			resultReceive[0] = webGateResult.substring(index - 8, index - 1);
			EmpExecutionContext.debug("验证码发送到短信网关返回错误代码：" + resultReceive[0]);
		}
		return resultReceive;
	}

	/**
	 * 网关放回值
	 * 
	 * @param webGateResult
	 * @param param
	 * @return1
	 */
	private String parseWebgateResult(String webGateResult, String param)
	{

		String[] testArray = webGateResult.split("&");

		String strParam;
		int index = -1;
		String value = "";
		for (int i = 0; i < testArray.length; i++)
		{
			strParam = testArray[i];
			index = strParam.indexOf(param + "=");
			if(index == -1)
			{
				continue;
			}

			value = strParam.substring(index + 8);
			break;
		}

		return value;
	}

	/**
	 * 保存手机关联客户提交过来的信息(未使用，以后会用到)
	 * 
	 * @param userInfo
	 * @param ulink
	 * @return
	 * @throws Exception1
	 */
	public boolean saveInfo(LfWcUserInfo userInfo, LfWcUlink ulink) throws Exception
	{
		Connection conn = empTransDao.getConnection();
		try
		{
			empTransDao.beginTransaction(conn);
			if(ulink != null)
			{
				LinkedHashMap<String, String> map = new LinkedHashMap<String, String>();
				map.put("wcId", ulink.getWcId().toString());
				List<LfWcUlink> ulinkList = empDao.findListByCondition(LfWcUlink.class, map, null);
				if(ulinkList != null && ulinkList.size() > 0)
				{
					LinkedHashMap<String, String> objectMap = new LinkedHashMap<String, String>();
					objectMap.put("UId", ulink.getUId().toString());
					empTransDao.update(conn, LfWcUlink.class, objectMap, map);
				}
				else
				{
					empTransDao.save(conn, ulink);
				}
			}
			empTransDao.update(conn, userInfo);
			empTransDao.commitTransaction(conn);
			return true;
		}
		catch (Exception e)
		{
			empTransDao.rollBackTransaction(conn);
			EmpExecutionContext.error(e,"保存手机关联客户提交过来的信息失败！");
			throw e;
		}
		finally
		{
			empTransDao.closeConnection(conn);
		}
	}
}
