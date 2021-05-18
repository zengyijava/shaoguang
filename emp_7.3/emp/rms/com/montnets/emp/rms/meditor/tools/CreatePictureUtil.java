package com.montnets.emp.rms.meditor.tools;

import java.io.File;
import java.math.BigDecimal;

import com.montnets.emp.common.context.EmpExecutionContext;

public class CreatePictureUtil {
	
	public static String AssembleBarStr(String barValue) {
		String [] arrBar = barValue.split("@");
		String returnStr = "[";
		for(int i=0;i<arrBar.length;i++){
			returnStr = returnStr+"["+arrBar[i]+"]"+",";
		}
		String str="";
		if(barValue.contains("@")){
			str = returnStr.substring(0, returnStr.length()-1)+"]";
		}else{
			String barValues [] = barValue.split(",");
			for(int i=0;i<barValues.length;i++){
				str =str+ "'"+barValues[i]+"',";
			}
			str =str.substring(0, str.length()-1);
		}
		return str;
	}
	/**
	 * @param str1:饼状图块名称
	 * @param str2:饼状图块数据
	 * @param str3:饼状图数据类型
	 * @return
	 */
	public static String AssembleRowName(String str1,String str2,String str3){
		String resultStr = "";
		String [] arr1 = str1.split(",");
		String [] arr2 = str2.split(",");
		for(int i=0;i<arr1.length;i++){
			if(("1").equals(str3)){
				if(Integer.parseInt(arr2[i])>1000000){
					arr2[i] = BigDecimal.valueOf((float)Integer.parseInt(arr2[i])/10000).setScale(1, BigDecimal.ROUND_HALF_UP).doubleValue()+"万";
				}
				resultStr += arr1[i]+"("+arr2[i]+")"+",";
			}else{
				resultStr += arr1[i]+"(n)"+",";
			}
		}
		return resultStr.substring(0,resultStr.length()-1);
	}
	
	public static String AssembleStr(String str){
		String resultStr = "";
		String [] arr = str.split(",");
		for(int i=0;i<arr.length;i++){
			if(!str.contains(",")){
					resultStr += arr[i]+",";
				}else{
					resultStr += "'"+arr[i]+"',";
			}
		}
		return resultStr.substring(0,resultStr.length()-1);
	}
	// 删除文件夹中的图片
 	public static void deleteTmp(String path) {
 		// 获得该路径
 		File f = new File(path);
 		// 获得该文件夹下的所有文件
 		String[] list = f.list();
 		File temp;
 		// 删除结果
 		boolean b = true;
 		// 循环删除
 		for (String s : list) {
 			temp = new File(path + "/" + s);
 			b = temp.delete();
 			if (!b) {
 				EmpExecutionContext.error("存在的图片删除不成功");	
 			}else{
 				EmpExecutionContext.info("存在的图片删除成功");	
 			}
 		}
 	}
}
