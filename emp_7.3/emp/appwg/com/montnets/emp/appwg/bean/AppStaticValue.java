package com.montnets.emp.appwg.bean;

public class AppStaticValue
{
	/**
	 * 发送接口的TO属性允许的最大用户数
	 */
	public static final int TOUSERSIZE = 100;
	
	/**
	 * 公众信息推送接口(网关到消息中心)业务代码
	 */
	public static final int ICODE_SENDEMSG = 201;
	
	/**
	 * 个人消息推送接口(网关到消息中心)业务代码
	 */
	public static final int ICODE_SENDPMSG = 205;
	
	
	/**
	 * 网关接收个人消息
	 */
	public static final int ICODE_RECPMSG = 204;
	
	/**
	 * 接收mt消息缓存队列大小
	 */
	public static final int REC_MT_QUEUE_SIZE = 5000;
	
	/**
	 * 接收mo消息缓存队列大小
	 */
	public static final int REC_MO_QUEUE_SIZE = 5000;
	
	/**
	 * 接收rpt消息缓存队列大小
	 */
	public static final int REC_RPT_QUEUE_SIZE = 5000;
	
	/**
	 * mt数据库消息缓存
	 */
	public static final int DB_MSG_CACHE_SIZE = 5000;
	
	/**
	 * mo数据库消息缓存
	 */
	public static final int DB_MO_CACHE_SIZE = 5000;
	
	/**
	 * rpt数据库消息缓存
	 */
	public static final int DB_RPT_CACHE_SIZE = 5000;
	
	/**
	 * 重发次数
	 */
	public static final int RE_SEND_COUNT = 3;
	
	/**
	 * 发送成功
	 */
	public static final int SEND_STATE_SUCC = 2;
	
	/**
	 * 发送失败
	 */
	public static final int SEND_STATE_FAIL = 3;
	
	/**
	 * 发送中
	 */
	public static final int SEND_STATE_SENDING = 4;
	
	/**
	 * 已读待发送
	 */
	public static final int SEND_STATE_WAIT_SEND = 12;
	
	/**
	 * 未读
	 */
	public static final int SEND_STATE_NOREAD = 11;
	
	/**
	 * 缓存读取等待时间，秒
	 */
	public static final int CACHE_READ_WAIT_TIME = 5;
	
	
}
