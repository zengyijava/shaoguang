package com.montnets.emp.perfect.biz;

import com.montnets.emp.common.biz.SuperBiz;
import com.montnets.emp.common.biz.receive.IBusinessStart;
import com.montnets.emp.entity.system.LfMotask;
import com.montnets.emp.perfect.dao.PerfectDao;




/**
 * 完美通知上行消息处理
 * @description 
 * @project p_dxzs
 * @company ShenZhen Montnets Technology CO.,LTD.
 * @author chentingsheng <cts314@163.com>
 * @datetime 2013-10-29 下午06:21:03
 */

public class PrefectNoticeUpMsgStart extends SuperBiz implements IBusinessStart 
{
	public boolean start(LfMotask moTask) 
	{
		//设置完美通知上行信息
		PerfectDao perfect = new PerfectDao();
		return(perfect.setPrefectNoticeUpMsg(moTask));
	}

}

















