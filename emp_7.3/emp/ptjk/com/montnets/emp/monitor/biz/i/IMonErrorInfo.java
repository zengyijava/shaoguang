/**
 * @description
 * @author chentingsheng <cts314@163.com>
 * @datetime 2014-1-11 下午06:57:29
 */
package com.montnets.emp.monitor.biz.i;

import java.sql.Timestamp;

import com.montnets.emp.entity.monitor.LfMonErr;

/**告警信息BIZ接口
 * @description
 * @project p_ptjk
 * @company ShenZhen Montnets Technology CO.,LTD.
 * @author chentingsheng <cts314@163.com>
 * @datetime 2014-1-11 下午06:57:29
 */

public interface IMonErrorInfo
{

	/**
	 * 设置告警基本信息
	 * 
	 * @description
	 * @param lfMonErr
	 * @param lfMonErrhis
	 * @param hostid
	 * @param proceId
	 * @param appType
	 * @param monStatus
	 * @param updateTime
	 * @author chentingsheng <cts314@163.com>
	 * @datetime 2014-1-4 下午06:12:40
	 */
	public void setHostErrObjectInfo(LfMonErr lfMonErr, Long hostid, Long proceId, String spId, String gateId, int appType, int monStatus, Timestamp updateTime);

	/**
	 * 保存告警信息
	 * 
	 * @description
	 * @param lfMonErr
	 * @param lfMonErrhis
	 * @author chentingsheng <cts314@163.com>
	 * @datetime 2014-1-4 下午05:36:28
	 */
	public void addErrorInfo(LfMonErr lfMonErr, String msg, int monFlag, int entType);
}
