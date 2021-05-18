package com.montnets.emp.common.biz;

import java.util.Arrays;
import java.util.List;

import com.montnets.emp.common.context.EmpExecutionContext;
import com.montnets.emp.common.dao.SameMmsDao;

public class SameMmsBiz {
	/**
	 * serlvet中处理群组的整合
	 *
	 * @param phoneStr
	 * @param groupIds
	 * @return
	 */
	public String getGroupPhoneStrById(String phoneStr, String groupIds) {
		try{
			groupIds = groupIds.substring(0,groupIds.length()-1);
			String[] arr = groupIds.split(",");
			List<String> groupIdsList = Arrays.asList(arr);
			StringBuilder phoneStrBuilder = new StringBuilder(phoneStr);
			for(int a = 0; a<groupIdsList.size(); a++){
				String tempPhoneStr = this.getGroupUserPhoneByIds(groupIdsList.get(a));
				phoneStrBuilder.append(tempPhoneStr);
			}
			phoneStr = phoneStrBuilder.toString();
		}catch (Exception e) {
			EmpExecutionContext.error(e,"移动彩讯处理群组出现异常！");
		}
		return phoneStr;
	}
	
	/**
	 *   获取群组中的人员的手机号码LIST
	 * @param groupId
	 * @return
	 */
	private String getGroupUserPhoneByIds(String groupId){
		//存放手机号码的list
		//List<String> returnList = null;
		String phoneStr ="";
		try{
			phoneStr = new SameMmsDao().getClientGroupUserById(groupId);
		}catch (Exception e) {
			phoneStr = "";
			EmpExecutionContext.error(e,"移动彩讯获取群组中的人员的手机号码出现异常！");
		}
		//返回
		return phoneStr;
	}
}
