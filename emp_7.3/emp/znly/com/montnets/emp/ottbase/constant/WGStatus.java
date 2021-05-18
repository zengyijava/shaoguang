package com.montnets.emp.ottbase.constant;

import java.util.HashMap;
import java.util.Map;

public class WGStatus
{
	protected static final Map<String,String> infoMap=new HashMap<String,String>();
	/**
	 * 发送短信时网关返回状态
	 */
	public WGStatus(){
		infoMap.put("DELIVRD","短信送到手机");
		infoMap.put("EXPIRED","短信过期");
		infoMap.put("DELETED","短信被删除");
		infoMap.put("UNDELIV","无法投递");
//		infoMap.put("ACCEPTD","最终用户接收");
		infoMap.put("UNKNOWN","状态不知");
//		infoMap.put("REJECTD","短信被拒绝");
		infoMap.put("MW:0101","缺少操作命令");
		infoMap.put("MW:0102","无效操作命令");
		infoMap.put("MW:0103","缺少SPID");
		infoMap.put("MW:0104","无效SPID");
		infoMap.put("MW:0105","缺少SP密码");
		infoMap.put("MW:0106","无效SP密码");
		infoMap.put("MW:0107","下行源地址被禁止");
		infoMap.put("MW:0108","无效下行源地址");
		infoMap.put("MW:0109","缺少下行目的地址");
		infoMap.put("MW:0110","无效下行目的地址");
		infoMap.put("MW:0111","超过下行目的地址限制");
		infoMap.put("MW:0112","ESM_CLASS被禁止");
		infoMap.put("MW:0113","无效ESM_CLASS");
		infoMap.put("MW:0114","PROTOCOL_ID被禁止");
		infoMap.put("MW:0115","无效PROTOCOL_ID");
		infoMap.put("MW:0116","缺少消息编码格式");
		infoMap.put("MW:0117","无效消息编码格式");
		infoMap.put("MW:0118","缺少消息内容");
		infoMap.put("MW:0119","无效消息内容");
		infoMap.put("MW:0120","无效消息内容长度");
		infoMap.put("MW:0121","优先级被禁止");
		infoMap.put("MW:0122","无效优先级");
		infoMap.put("MW:0123","定时发送时间被禁止");
		infoMap.put("MW:0124","无效定时发送时间");
		infoMap.put("MW:0125","有效时间被禁止");
		infoMap.put("MW:0126","无效有效时间");
		infoMap.put("MW:0127","通道ID被禁止");
		infoMap.put("MW:0128","无效通道ID");
		infoMap.put("MW:0131","缺少批量下行类型");
		infoMap.put("MW:0132","无效批量下行类型");
		infoMap.put("MW:0133","无效任务ID");
		infoMap.put("MW:0134","无效批量下行标题");
		infoMap.put("MW:0135","缺少批量下行内容");
		infoMap.put("MW:0136","无效批量下行内容");
		infoMap.put("MW:0137","缺少批量下行内容URL");
		infoMap.put("MW:0138","网关参数配置不正确或上传文件内容有误");
		infoMap.put("MW:0139","SP服务代码不存在");
		infoMap.put("MW:0140","无效的不同内容批量下行地址和内容");
		infoMap.put("MW:0201","MSISDN号码段不存在");
		infoMap.put("MW:0202","MSISDN号码段停用");
		infoMap.put("MW:0210","MSISDN号码被过滤");
		infoMap.put("MW:0220","内容被过滤");
		infoMap.put("MW:0221","内容被人工过滤");
		infoMap.put("MW:0230","下行路由失败");
		infoMap.put("MW:0240","上行路由失败");
		infoMap.put("MW:0250","配额不足");
		infoMap.put("MW:0251","没有配额");
		infoMap.put("MW:0254","EMP认证失败，已停止发送！");
		infoMap.put("MW:0301","SP被禁止");
		infoMap.put("MW:0302","SP被锁定");
		infoMap.put("MW:0303","无效IP地址");
		infoMap.put("MW:0304","超过传输速度限制");
		infoMap.put("MW:0305","超过传输连接限制");
		infoMap.put("MW:0306","SMS下行被禁止");
		infoMap.put("MW:0307","SMS批量下行被禁止");
		infoMap.put("MW:0308","SMS上行被禁止");
		infoMap.put("MW:0309","SMS状态报告被禁止");
		infoMap.put("MW:0401","源号码与通道号不匹配");
		infoMap.put("MW:0402","下发到运行商网关异常");
		infoMap.put("MW:0403","下发到运行商网关无反馈");
		infoMap.put("MW:0500","运行商网关反馈信息");
		infoMap.put("ACCEPTD","API接口消息被接收");
		infoMap.put("REJECTD","API接口消息被拒绝");
		infoMap.put("MW:0601","API接口HttpException");
		infoMap.put("MW:0602","API接口IOException");
		infoMap.put("MW:0603","API接口DBException");
		infoMap.put("noUserCode","缺少Param1参数（操作员编码）");
		infoMap.put("noDepCode","缺少Param2参数（机构编码）");
		infoMap.put("timeError","timeError");
	}
}
