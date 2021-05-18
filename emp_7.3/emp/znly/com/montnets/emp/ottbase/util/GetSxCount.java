package com.montnets.emp.ottbase.util;

import com.montnets.emp.ottbase.constant.WXStaticValue;

/**
 * 生成5位顺序号单例模式类
 * @author LIN ZHIHAN
 */
public class GetSxCount {
   private GetSxCount(){}
   
   private static  GetSxCount single = null;
   
   private static Integer Count = 10000;
   
   synchronized public  static GetSxCount getInstance() 
   {
	   if(single == null)
	   {
		   single = new GetSxCount();
	   }
	   return single;
   }
   
	
   synchronized public String getCount()
   {
	   if(Count == 99999)
	   {
		   Count = 10000;
	   }
	   Count ++;
	   //集群标识
	   int ISCLUSTER = WXStaticValue.ISCLUSTER;
	   //如果是集群，则返回服务器编号
	   if(ISCLUSTER == 1)
	   {
		   return Count.toString()+"_"+WXStaticValue.SERVER_NUMBER;
	   }else
	   {
		   return Count.toString();
	   }
   }
}
