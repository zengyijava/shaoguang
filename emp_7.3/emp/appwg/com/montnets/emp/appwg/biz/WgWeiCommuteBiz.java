package com.montnets.emp.appwg.biz;

import org.json.simple.JSONObject;

import com.montnets.emp.appwg.bean.AppMessage;
import com.montnets.emp.appwg.util.JsonUtil;
import com.montnets.emp.appwg.wginterface.IWgWeiCommuteBiz;
import com.montnets.emp.common.biz.SuperBiz;
import com.montnets.emp.common.context.EmpExecutionContext;
import com.montnets.emp.ottbase.service.WeixService;

/**
 * 
 * @project emp_std_192.169.1.81
 * @author huangzb
 * @company ShenZhen Montnets Technology CO.,LTD.
 * @datetime 2014-7-10 上午10:57:59
 * @description app网关与微信通信逻辑类
 */
public class WgWeiCommuteBiz extends SuperBiz implements IWgWeiCommuteBiz
{
	/**
	 * 发送微信消息
	 * @param appMsg 消息对象
	 * @return
	 * 		  000：	提交成功。
	 * 		   -2：	获取发送json格式字符串为空
	 * 		   -3：	发送微信消息异常
	 * 		-9999：	提交失败。
	 */
	public String SendWeixMsg(AppMessage appMsg){
		try
		{
			String jsondata = getSendWeixMsgJsonStr(appMsg);
			if(jsondata == null || jsondata.trim().length() == 0){
				EmpExecutionContext.error("发送微信消息失败，获取发送json格式字符串为空。");
				return "-2";
			}
			
			EmpExecutionContext.info("发送微信消息json格式字符串:"+jsondata);
			
			WeixService weiSvc = new WeixService();
			String respStr = weiSvc.sendAllMessage(jsondata);
			
			String errcode = parsRespMsgJson(respStr);
			
			return errcode;
		}
		catch (Exception e)
		{
			EmpExecutionContext.error(e, "发送微信消息异常。");
			return "-3";
		}
	}
	
	/**
	 * 获取发送微信消息json格式字符串
	 * @param appMsg 消息参数对象
	 * @return 返回json格式字符串
	 */
	private String getSendWeixMsgJsonStr(AppMessage appMsg){
		try
		{
			StringBuffer sb = new StringBuffer();
			sb.append("{");
			sb.append("\"lgcorpcode\":\"").append(appMsg.getEcode()).append("\",");
			sb.append("\"aid\":\"").append(appMsg.getFromUserName()).append("\",");
			sb.append("\"touser\":[");
			
			int size = appMsg.getToUserNameSet().size();
			int i = 0;
			//拼入openid
			for(String openId : appMsg.getToUserNameSet()){
				sb.append("\"").append(openId).append("\"");
				if(i < size -1){
					sb.append(",");
				}
				i++;
			}
			
			sb.append("],");
			
			//消息的类型。 	0：文本消息；1：图片消息；2：视频；3：语音；
			if(appMsg.getMsgType() == 0){
				
				sb.append("\"msgtype\":\"text\",");
				sb.append("\"content\":\"").append(appMsg.getMsgContent()).append("\"");
				
			}
			//图片消息
			else if(appMsg.getMsgType() == 1){
				sb.append("\"msgtype\":\"image\",");
				sb.append("\"title\":\"").append(appMsg.getTitle()==null?"":appMsg.getTitle()).append("\",");
				sb.append("\"imageUrl\":\"").append(appMsg.getLocalUrl()).append("\"");
			}
			
			sb.append("}");
			
			return sb.toString();
		}
		catch (Exception e)
		{
			EmpExecutionContext.error(e, "获取发送微信消息json格式字符串异常。");
			return null;
		}
	}
	
	/**
	 * 获取json格式响应消息中的errcode
	 * @param json json格式响应消息
	 * @return 返回errcode
	 */
	private String parsRespMsgJson(String json){
		try
		{
			//把json格式字符串转为json对象
			JSONObject jsonObj = JsonUtil.parsJsonObj(json);
			if(jsonObj == null){
				EmpExecutionContext.error("获取json格式响应消息中的errcode失败，获取json对象为null。");
				return null;
			}
			
			if(jsonObj.get("errcode") == null){
				EmpExecutionContext.error("获取json格式响应消息中的errcode失败，errcode为null。");
				return null;
			}
			
			return jsonObj.get("errcode").toString();
		}
		catch (Exception e)
		{
			EmpExecutionContext.error(e, "获取json格式响应消息中的errcode异常。");
			return null;
		}
	}

	/**
	 * 获取消息id
	 * @return 返回消息id字符串
	 */
	private long getMsgId(int count){
		//批量获取msgid
		long msgId = empDao.getIdByPro(14, count);
		
		return msgId;
	}
	
}
