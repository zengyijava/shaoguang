/**
 * @description
 * @author chentingsheng <cts314@163.com>
 * @datetime 2014-1-15 上午08:45:17
 */
package com.montnets.emp.monitor.biz.i;

import com.montnets.emp.entity.monitor.LfMonErr;
import com.montnets.emp.entity.monitor.LfMonSgtacinfo;
import com.montnets.emp.entity.monitor.LfMonShost;
import com.montnets.emp.entity.monitor.LfMonSproce;
import com.montnets.emp.entity.monitor.LfMonSspacinfo;
import com.montnets.emp.monitor.constant.MonDgateacParams;
import com.montnets.emp.monitor.constant.MonDhostParams;
import com.montnets.emp.monitor.constant.MonDproceParams;
import com.montnets.emp.monitor.constant.MonDspAccountParams;

/**分析告警阀值BIZ接口
 * @description
 * @project p_ptjk
 * @company ShenZhen Montnets Technology CO.,LTD.
 * @author chentingsheng <cts314@163.com>
 * @datetime 2014-1-15 上午08:45:17
 */

public interface IMonParamsAnalysisBiz
{
	/**
	 * 主机参数分析
	 * 
	 * @description
	 * @param monShost
	 * @param monDhost
	 * @param shostId
	 * @param lfMonErr
	 * @param monPhone
	 * @author chentingsheng <cts314@163.com>
	 * @datetime 2014-1-15 上午09:21:42
	 */
	public void hostParamsAnalysis(LfMonShost monShost, MonDhostParams monDhost, Long shostId, LfMonErr lfMonErr, String effPhone);
	
	public void hostParamsAnalysis(LfMonShost monShost, MonDhostParams monDhost, Long shostId, LfMonErr lfMonErr, String effPhone,String effEmail);

	/**
	 * 程序参数分析
	 * 
	 * @description
	 * @param monSproce
	 * @param monDproce
	 * @param sproceId
	 * @param lfMonErr
	 * @param monPhone
	 * @param isVaild
	 * @author chentingsheng <cts314@163.com>
	 * @datetime 2014-1-15 上午10:07:49
	 */
	public void proceParamsAnalysis(LfMonSproce monSproce, MonDproceParams monDproce, Long sproceId, LfMonErr lfMonErr, String effPhone);
	
	public void proceParamsAnalysis(LfMonSproce monSproce, MonDproceParams monDproce, Long sproceId, LfMonErr lfMonErr, String effPhone ,String effEmail);

	/**
	 * SP账号参数分析
	 * 
	 * @description
	 * @param monSspacinfo
	 * @param monDspacinfo
	 * @param sSpAccountId
	 * @param lfMonErr
	 * @param monPhone
	 * @param isVaild
	 * @author chentingsheng <cts314@163.com>
	 * @datetime 2014-1-15 下午01:03:55
	 */
	public void spAccountParamsAnalysis(LfMonSspacinfo monSspacinfo, MonDspAccountParams monDspacinfo, String sSpAccountId, LfMonErr lfMonErr, String effPhone);
	
	public void spAccountParamsAnalysis(LfMonSspacinfo monSspacinfo, MonDspAccountParams monDspacinfo, String sSpAccountId, LfMonErr lfMonErr, String effPhone, Integer evtTypeFlag);

	public void spAccountParamsAnalysis(LfMonSspacinfo monSspacinfo, MonDspAccountParams monDspacinfo, String sSpAccountId, LfMonErr lfMonErr, String effPhone, Integer evtTypeFlag ,String effEmail);
	/**
	 * 通道账号参数分析
	 * 
	 * @description
	 * @param monSgtacinfo
	 * @param monDgtacinfo
	 * @param sGateAccountId
	 * @param lfMonErr
	 * @param monPhone
	 * @param isVaild
	 * @author chentingsheng <cts314@163.com>
	 * @datetime 2014-1-15 下午02:55:10
	 */
	public void gateAccountParamsAnalysis(LfMonSgtacinfo monSgtacinfo, MonDgateacParams monDgtacinfo, String sGateAccountId, LfMonErr lfMonErr, String effPhone);
	
	public void gateAccountParamsAnalysis(LfMonSgtacinfo monSgtacinfo, MonDgateacParams monDgtacinfo, String sGateAccountId, LfMonErr lfMonErr, String effPhone , Integer evtTypeFlag);
	
	public void gateAccountParamsAnalysis(LfMonSgtacinfo monSgtacinfo, MonDgateacParams monDgtacinfo, String sGateAccountId, LfMonErr lfMonErr, String effPhone, Integer evtTypeFlag ,String effEmail);
}
