package com.montnets.emp.monitor.constant;

import com.montnets.emp.common.context.EmpExecutionContext;

/**
 * 监控前台显示处理
 * @description 
 * @project p_ptjk
 * @company ShenZhen Montnets Technology CO.,LTD.
 * @author zhangmin
 * @datetime 2014-1-22 下午02:57:19
 */
public class MonViewParams
{

	/**
	 * 转换显示条数  1000-10000用k条  10000+用万条显示
	 * @description    
	 * @param count
	 * @return       			 
	 * @author zhangmin
	 * @datetime 2014-1-20 上午09:26:33
	 */
	public static String countView(int count)
	{
		String viewStr = "0条";
		try
		{
			if(count<10000)
			{
				viewStr = count+"条";
			}
			else if(count>=10000)
			{
				viewStr =(double)count%10000+"万条";
				
			}
		}
		catch (Exception e) {
			EmpExecutionContext.error(e,"监控界面短信条数转换显示错误！");
		}
		return viewStr;
	}
	
	/**
	 * 监控主界面账号名称显示处理
	 * @description    
	 * @param count
	 * @return       			 
	 * @author zhangmin
	 * @datetime 2014-1-20 上午09:26:33
	 */
	public static String getAcctName(String name)
	{
		String viewStr = "";
		try
		{
			if(name.length()>6)
			{
				viewStr = name.substring(0,6)+"...";
			}
			else
			{
				viewStr = name+"接口";
			}
		}
		catch (Exception e) {
			EmpExecutionContext.error(e,"监控界面接口显示处理错误！");
		}
		return viewStr;
	}
	
	/**
	 * 监控主界面账号名称显示处理
	 * @description    
	 * @param count
	 * @return       			 
	 * @author zhangmin
	 * @datetime 2014-1-20 上午09:26:33
	 */
	public static String getGtAcctName(String name)
	{
		String viewStr = "";
		try
		{
			if(name.length()>5)
			{
				viewStr = name.substring(0,5)+"...";
			}
			else
			{
				viewStr = name+"运营商";
			}
		}
		catch (Exception e) {
			EmpExecutionContext.error(e,"监控界面运营商显示处理错误！");
		}
		return viewStr;
	}
}
