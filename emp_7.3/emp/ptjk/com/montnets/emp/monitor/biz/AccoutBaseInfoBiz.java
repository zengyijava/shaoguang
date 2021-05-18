/**
 * @description  
 * @author chentingsheng <cts314@163.com>
 * @datetime 2014-1-8 下午05:41:10
 */
package com.montnets.emp.monitor.biz;

import java.util.List;

import com.montnets.emp.common.biz.SuperBiz;
import com.montnets.emp.common.context.EmpExecutionContext;
import com.montnets.emp.entity.gateway.AgwAccount;
import com.montnets.emp.entity.pasgroup.Userdata;
import com.montnets.emp.monitor.biz.i.IAccoutBaseInfoBiz;
import com.montnets.emp.monitor.constant.MonitorStaticValue;

/**
 * @description 
 * @project p_ptjk
 * @company ShenZhen Montnets Technology CO.,LTD.
 * @author chentingsheng <cts314@163.com>
 * @datetime 2014-1-8 下午05:41:10
 */

public class AccoutBaseInfoBiz extends SuperBiz implements IAccoutBaseInfoBiz
{

	/**
	 * 获取SP账号、通道账号信息存入缓存
	 * @description           			 
	 * @author chentingsheng <cts314@163.com>
	 * @datetime 2014-1-8 下午06:27:25
	 */
	public void getSpAndGateInfo()
	{
		try
		{
			//获取SP账号、通道账号信息
			List<Userdata> userDataList = empDao.findListByCondition(MonDbConnection.getInstance().getConnection(), true, Userdata.class, null, null);
			Userdata userdata = null;
			if(userDataList != null && userDataList.size()>0){
				for(int i=0;i<userDataList.size();i++)
				{
					userdata = userDataList.get(i);
					// SP账号信息
					if(userdata.getUserType() == 0)
					{
						// SP账号VALUE值
						String[] spAccoutInfo = new String[3];
						// SP账号名称
						spAccoutInfo[0] = userdata.getStaffName();
						// SP账号类型
						spAccoutInfo[1] = userdata.getSptype().toString();
						// SP账号发送级别
						spAccoutInfo[2] = userdata.getRiseLevel().toString();
						// 存入缓存
						MonitorStaticValue.getSPACCOUTN_INFO().put(userdata.getUserId(), spAccoutInfo);
					}
					// 通道账号信息
					else
					{
						// 通道账号VALUE值
						String[] gateAccountInfo = new String[2];
						// 通道账号名称
						gateAccountInfo[0] = userdata.getStaffName();
						// 付费类型
						gateAccountInfo[1] = "2";
						// 存入缓存
						MonitorStaticValue.getGATEACCOUTN_INFO().put(userdata.getUserId(), gateAccountInfo);
					}
				}
			}
			//获取通道账号付费类型
			List<AgwAccount> agwAccountList = empDao.findListByCondition(MonDbConnection.getInstance().getConnection(), true, AgwAccount.class, null, null);
			if(agwAccountList != null && agwAccountList.size()>0)
			{
				for(int i=0; i<agwAccountList.size();i++)
				{
					AgwAccount agwAccount = agwAccountList.get(i);
					String ptAccId = agwAccount.getPtAccId();
					String spFeeFlag = agwAccount.getSpFeeFlag().toString();
					if(MonitorStaticValue.getGATEACCOUTN_INFO().containsKey(ptAccId))
					{
						String[] gateAccount = MonitorStaticValue.getGATEACCOUTN_INFO().get(ptAccId);
						gateAccount[1] = spFeeFlag;
						MonitorStaticValue.getGATEACCOUTN_INFO().put(ptAccId, gateAccount);
					}
				}
			}
			
		}
		catch (Exception e)
		{
			EmpExecutionContext.error(e, "获取SP账号、通道账号信息失败！");
		}
	}
}
