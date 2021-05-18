/**
 * @description  
 * @author chentingsheng <cts314@163.com>
 * @datetime 2014-9-3 下午05:42:38
 */
package com.montnets.emp.znly.biz;

import com.montnets.emp.appwg.wginterface.IProcRptMessage;
import com.montnets.emp.common.context.EmpExecutionContext;

/**
 * @description 
 * @project znly
 * @company ShenZhen Montnets Technology CO.,LTD.
 * @author chentingsheng <cts314@163.com>
 * @datetime 2014-9-3 下午05:42:38
 */

public class GetAppMsgRptImpl implements IProcRptMessage
{

	/* (non-Javadoc)
	 * @see com.montnets.emp.appwg.wginterface.IProcRptMessage#processRptMessage(java.lang.String)
	 * @author chentingsheng <cts314@163.com>
	 * @datetime 2013-9-26 上午11:23:48
	 */
	public boolean processRptMessage(String json)
	{
		//记录日志
		EmpExecutionContext.appRequestInfoLog("客服接收Rpt:" + json);
/*		//解析内容
		JSONObject jsonObj = StringUtils.parsJsonObj(json);
		if(jsonObj == null)
		{
			EmpExecutionContext.error("获取app消息状态报告内容失败！");
			return false;
		}*/
		return true;
	}

}
