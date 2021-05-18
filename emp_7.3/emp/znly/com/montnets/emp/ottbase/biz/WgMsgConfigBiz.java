package com.montnets.emp.ottbase.biz;

import com.montnets.emp.common.biz.SuperBiz;
import com.montnets.emp.ottbase.constant.WXStaticValue;

/**
 * 
 * @project sf_new
 * @author huangzhibin <307260621@qq.com>
 * @company ShenZhen Montnets Technology CO.,LTD.
 * @datetime 2011-7-21 下午02:02:59
 * @description
 */
public class WgMsgConfigBiz extends SuperBiz
{

	BaseBiz baseBiz = new BaseBiz();
	//号段集合 
	private static String[] haoduan = new String[3];
	//号段更新时间
	private static long hdUpdateTime = System.currentTimeMillis();

	/**
	 * 获取当前系统的号段
	 * @return
	 * @throws Exception
	 */
	public void setHaoduan() 
	{
		/*String[] phoneNo = new String[3];
		//查询条件map
		LinkedHashMap<String, String> conditionMap = new LinkedHashMap<String, String>();
		try
		{
			//查询
			List<PbServicetype> haoduanList = empDao.findListByCondition(
					PbServicetype.class, conditionMap, null);
			//循环
			for (int i = 0; i < haoduanList.size(); i++)
			{
				PbServicetype pbSer = haoduanList.get(i);
				//移动
				if (pbSer.getSpisuncm() == 0)
				{
					phoneNo[0] = pbSer.getServiceno();
				} 
				//联通
				else if (pbSer.getSpisuncm() == 1)
				{
					phoneNo[1] = pbSer.getServiceno();
				} 
				//电信
				else if (pbSer.getSpisuncm() == 21)
				{
					phoneNo[2] = pbSer.getServiceno();
				}
			}
			haoduan = phoneNo;
			//号段更新时间赋值
			hdUpdateTime = System.currentTimeMillis();
		} catch (Exception e)
		{
			EmpExecutionContext.error(e, "获取系统中的合法号段失败。错误码："+IErrorCode.V10022);
		}*/
	
	}

	/**
	 * 获取系统号段
	 * @return
	 * @throws Exception
	 */
	public String[] getHaoduan()
	{
		if(haoduan == null || haoduan[0] == null || haoduan[1] == null || haoduan[2] == null)
		{
			setHaoduan();
		}
		//如果当前时间减去上次同步的时间大于同步时间间隔
		else if(System.currentTimeMillis() - hdUpdateTime > WXStaticValue.SYNC_TIMER_INTERVAL)
		{
			setHaoduan();
		}
		
		return haoduan;
	}
	
	/**
	 * 清空号段内存的值 
	 */
	public void cleanHaoduan()
	{
		//如果当前时间与上次更新时间大于同步时间间隔，则清空haoduan
		if(System.currentTimeMillis() - hdUpdateTime > WXStaticValue.SYNC_TIMER_INTERVAL)
		{
			haoduan = null;
		}
	}
	
	/**
	 * 检查手机号是否合法
	 * @param mobile
	 * @param spiscumu
	 * @param haoduan
	 * @return
	 * @throws Exception
	 */
	public int checkMobile(String mobile, Integer spiscumu, String[] haoduan)
			throws Exception
	{
		//检查手机长度
		if (mobile.length() != 11)
			return 0;
		
		for (int k = mobile.length(); --k >= 0;)
		{
			if (!Character.isDigit(mobile.charAt(k)))
			{
				return 0;
			}
		}
		//号段的字符串拼接
		String number = "";
		if (spiscumu - 0 == 0)
		{
			number = haoduan[0];
		} 
		else if (spiscumu - 1 == 0)
		{
			number = haoduan[1];
		} 
		else if (spiscumu - 21 == 0)
		{
			number = haoduan[2];
		}
		//是否合法号段
		if (number.replace(mobile.substring(0, 3), "").length() == number
				.length())
		{
			//不合法
			return 0;
		}
		//返回1 合法
		return 1;
	}

	/**
	 * 验证手机号码
	 * @param mobile
	 * @param haoduan
	 * @return
	 * @throws Exception
	 */
	public int checkMobile(String mobile, String[] haoduan) throws Exception
	{
		if (mobile.length() != 11)
			return 0;
		for (int k = mobile.length(); --k >= 0;)
		{
			if (!Character.isDigit(mobile.charAt(k)))
			{
				return 0;
			}
		}
        //号段
		String number = haoduan[0] + "," + haoduan[1] + "," + haoduan[2];
		if (number.replace(mobile.substring(0, 3), "").length() == number
				.length())
		{
			return 0;
		}
		return 1;
	}

	/**
	 * 获取手机号码的号段
	 * @param number
	 * @return
	 * @throws Exception
	 */
	public Integer getPhoneSpisuncm(String number) throws Exception
	{
		//截取手机号前三位
		number = number.substring(number.length() - 11, number.length());
		String numTit = number.substring(0, 3);
        //获取所有号段
		String[] haoduan = getHaoduan();
        //判断号段
		if (haoduan[0].indexOf(numTit) > -1)
		{
			return 0;
		} 
		else if (haoduan[1].indexOf(numTit) > -1)
		{
			return 1;
		} 
		else if (haoduan[2].indexOf(numTit) > -1)
		{
			return 21;
		} 
		else
		{
			return null;
		}

	}
}
