package com.montnets.emp.util;

import com.montnets.emp.common.constant.StaticValue;

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
	   //生成序列方法加上服务器节点
	   return Count.toString()+"_"+ StaticValue.getServerNumber();
	   
/*	   //集群标识
	   int ISCLUSTER = StaticValue.ISCLUSTER;
	   //如果是集群，则返回服务器编号
	   if(ISCLUSTER == 1)
	   {
		   return Count.toString()+"_"+StaticValue.SERVER_NUMBER;
	   }else
	   {
		   return Count.toString();
	   }*/
   }
}
