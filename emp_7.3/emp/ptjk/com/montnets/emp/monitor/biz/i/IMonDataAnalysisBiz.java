/**
 * @description
 * @author chentingsheng <cts314@163.com>
 * @datetime 2014-1-2 下午02:33:56
 */
package com.montnets.emp.monitor.biz.i;


/**监控信息发析BIZ接口
 * @description
 * @project p_ptjk
 * @company ShenZhen Montnets Technology CO.,LTD.
 * @author chentingsheng <cts314@163.com>
 * @datetime 2014-1-2 下午02:33:56
 */

public interface IMonDataAnalysisBiz
{

	/**
	 * 数据分析
	 * 
	 * @description
	 * @author chentingsheng <cts314@163.com>
	 * @datetime 2014-1-7 下午07:11:44
	 */
	public void monDataAnalysis();

	/**
	 * 分析主机数据
	 * 
	 * @description
	 * @author chentingsheng <cts314@163.com>
	 * @datetime 2014-1-3 上午09:07:27
	 */
	public void hostDataAnalysis();

	/**
	 * 分析程序数据
	 * 
	 * @description
	 * @author chentingsheng <cts314@163.com>
	 * @datetime 2014-1-4 下午06:13:52
	 */
	public void proceDataAnalysis();

	/**
	 * 分析SP账号数据
	 * 
	 * @description
	 * @author chentingsheng <cts314@163.com>
	 * @datetime 2014-1-4 下午07:47:14
	 */
	public void spAccoutDataAnalysis();

	/**
	 * 分析通道账号数据
	 * 
	 * @description
	 * @author chentingsheng <cts314@163.com>
	 * @datetime 2014-1-6 上午11:33:25
	 */
	public void gateAccoutDataAnalysis();

	/**
	 * 分析在线用户数
	 * 
	 * @description
	 * @author chentingsheng <cts314@163.com>
	 * @datetime 2014-1-18 下午03:16:04
	 */
	public void onlineUserDataAnalysis();



	/**
	 * 告警手机号码有效性检查
	 * 
	 * @description
	 * @param phone
	 * @return true 有效; false无效
	 * @author chentingsheng <cts314@163.com>
	 * @datetime 2014-1-15 下午06:41:07
	 */
	public String checkMobleVaild(String phone);
	
	/**
	 * 
	 * @description    是否为EMP主机
	 * @param hostId
	 * @return  false 不是EMP主机； true 是EMP主机     			 
	 * @author chentingsheng <cts314@163.com>
	 * @datetime 2014-2-21 上午09:33:08
	 */
	public boolean getIsEempHost(Long hostId);
	
	/**
	 * 定时扫库,符合重发条件的记录使用网优发送
	 * @description           			 
	 * @author chentingsheng <cts314@163.com>
	 * @datetime 2014-4-3 下午02:10:48
	 */
	public void monSmsResend();
}
