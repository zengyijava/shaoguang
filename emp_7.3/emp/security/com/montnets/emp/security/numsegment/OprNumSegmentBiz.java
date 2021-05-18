/**
 * @description
 * @author chentingsheng <cts314@163.com>
 * @datetime 2014-5-7 上午10:31:57
 */
package com.montnets.emp.security.numsegment;

import com.montnets.emp.common.biz.SuperBiz;

/**
 * @description
 * @project p_comm
 * @company ShenZhen Montnets Technology CO.,LTD.
 * @author chentingsheng <cts314@163.com>
 * @datetime 2014-5-7 上午10:31:57
 */

public class OprNumSegmentBiz extends SuperBiz
{
	protected static final int[] numSegment = new int[1000];
	
	public static int[] getNumsegment() {
		return numSegment;
	}

	/**
	 * 获取号码运营商
	 * 
	 * @description
	 * @param phone
	 * @return -1: 非法号码 0:移动号码 1:联通号码 21:电信号码
	 * @author chentingsheng <cts314@163.com>
	 * @datetime 2014-5-7 下午01:30:29
	 */
	public int getphoneType(String phone)
	{
		int phoneType = -1;
		if(phone != null && phone.length() > 6 && "1".equals(phone.substring(0,1)))
		{
			int numSegmentTemp = Integer.parseInt(phone.substring(1, 4));
			if(numSegmentTemp > 0 && numSegmentTemp <= 999)
			{
				phoneType = numSegment[numSegmentTemp];
			}
		}
		return phoneType;
	}

	/**
	 * @description 获取数组中所有号码的运营商
	 * @param phoneArray
	 * @return 返回数组,下标值为:-1 非法号码 ; 0移动号码 ; 1联通号码 ; 21电信号码
	 * @author chentingsheng <cts314@163.com>
	 * @datetime 2014-5-7 下午01:38:40
	 */
	public int[] getphoneType(String[] phoneArray)
	{
		if(phoneArray != null)
		{
			int count = phoneArray.length;
			int[] phoneType = new int[count];
			String phoneStr;
			for (int i = 0; i < count; i++)
			{
				phoneStr = phoneArray[i];
				if(phoneStr != null && phoneStr.length() > 6 && "1".equals(phoneStr.substring(0,1)))
				{
					int numSegmentTemp = Integer.parseInt(phoneStr.substring(1, 4));
					if(numSegmentTemp > 0 && numSegmentTemp <= 999)
					{
						phoneType[i] = numSegment[numSegmentTemp];
					}
				}
			}
			return phoneType;
		}
		else
		{
			return null;
		}
	}
}
