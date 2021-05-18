package com.montnets.emp.znly.biz;

import org.json.simple.JSONObject;

import com.montnets.emp.appwg.wginterface.IProcPMessage;
import com.montnets.emp.common.context.EmpExecutionContext;
import com.montnets.emp.ottbase.util.StringUtils;

public class GetAppMsgImpl implements IProcPMessage {

	public boolean processPMessage(String jsonStr) {
		
		JSONObject jsonObj = StringUtils.parsJsonObj(jsonStr);
		if(jsonObj == null)
		{
			EmpExecutionContext.error("获取app推送的消息内容失败！");
			return false;
		}
		CustomChatBiz.getAppMsg(jsonObj);
		return true;
	}
}
