package com.montnets.emp.appwg.util;


/**
 * 
 * @project p_appwg
 * @author huangzb
 * @company ShenZhen Montnets Technology CO.,LTD.
 * @datetime 2014-6-14 上午11:22:46
 * @description
 */
public class RandomCount {
   private RandomCount(){}
   
   private static  RandomCount single = null;
   
   private static Integer Count = 1000000;
   
   private static Integer MsgIdCount = 10000;
   
   synchronized public static RandomCount getInstance() 
   {
	   if(single == null)
	   {
		   single = new RandomCount();
	   }
	   return single;
   }
	
   synchronized public int getCount()
   {
	   if(Count == 9999999)
	   {
		   Count = 1000000;
	   }
	   Count ++;
	   return Count;
   }
   
   synchronized public int getMsgIdCont()
   {
	   MsgIdCount ++;
	   return MsgIdCount;
   }
}
