package com.montnets.emp.table.mmstask;

import java.util.HashMap;
import java.util.Map;

/**
 * @project emp
 * @author wuxiaotao <819475589@qq.com>
 * @company ShenZhen Montnets Technology CO.,LTD.
 * @datetime 2011-1-19 上午09:31:44
 * @description
 */
public class TableMmsBmtreq
{
	//表名
	public static final String TABLE_NAME = "MMS_BMTREQ";
	//自增ID
	public static final String ID="ID";
	//平台唯一流水号
	public static final String MSGID="MSGID";
	//批量文件处理流水号
	public static final String BMTMSGID="BMTMSGID";
	//用户账号
	public static final String USERID="USERID";
	//代理账号
	public static final String LOGINID="LOGINID";
	//用户UID
	public static final String USERUID="USERUID";
	//业务类型
	public static final String SERVICETYPE="SERVICETYPE";
	//信息类型 0：短信 	10：普通彩信（通过直接发送接口发送的彩信） 	11：静态模板彩信（彩信资源是固定的）12：动态模板彩信（模板中有参数）
	public static final String MSGTYPE="MSGTYPE";
	//任务ID
	public static final String TASKID="TASKID";
	//标题
	public static final String TITLE="TITLE";
	//短信内容
	public static final String MSG="MSG";
	//消息编码格式
	public static final String MSGFMT="MSGFMT";
	//文件远程地址
	public static final String REMOTEURL="REMOTEURL";
	//文件本地地址
	public static final String LOCALPATH="LOCALPATH";
	//发送级别
	public static final String SENDLEVEL="SENDLEVEL";
	//定时发送(由时间转换为大整型数如：20120806185600)
	public static final String ATTIME="ATTIME";
	//短信有效期(由时间转换为大整型数如：20120806185600)
	public static final String VALIDTIME="VALIDTIME";
	//发送状态
	public static final String SENDSTATUS="SENDSTATUS";
	//错误代码(状态报告)
	public static final String ERRORCODE="ERRORCODE";
	//接收时间
	public static final String RECVTIME="RECVTIME";
	//发送时间
	public static final String SENDTIME="SENDTIME";
	//发送类型
	public static final String SENDTYPE="SENDTYPE";
	//定时是否处理标志
	public static final String ATTIMEFLAG="ATTIMEFLAG";
	//下行源地址，也就通道号或扩展子号
	public static final String SA="SA";
	//服务类型
	public static final String SVRTYPE="SVRTYPE";
	//自定义参数1
	public static final String P1 = "P1";
	//自定义参数2
	public static final String P2 = "P2";
	//自定义参数3
	public static final String P3 = "P3";
	//自定义参数4
	public static final String P4 = "P4";
	//用户消息ID
	public static final String USERMSGID="USERMSGID";
	//模块ID
	public static final String MODULEID="MODULEID";
	//是否需要状态报告标识
	public static final String RETFLAG="RETFLAG";
	//模板id
	public static final String TEMPLID="TMPLID";
	//序列
	public static final String SEQUENCE = "MMS_BMTREQ_ID";
	
	protected static final Map<String , String> columns = new HashMap<String, String>();
    /**
     * 加载字段
     */ 
	static
	{
		columns.put("MmsBmtreq", TABLE_NAME);
		columns.put("sequence", SEQUENCE);
		columns.put("id", ID);
		columns.put("tableId", ID);
		columns.put("msgid", MSGID);
		columns.put("bmtmsgid", BMTMSGID);
		columns.put("userid", USERID);
		columns.put("loginid", LOGINID);
		columns.put("useruid", USERUID);
		columns.put("servicetype", SERVICETYPE);
		columns.put("msgtype", MSGTYPE);
		columns.put("taskid", TASKID);
		columns.put("title", TITLE);
		columns.put("msg", MSG);
		columns.put("msgfmt", MSGFMT);
		columns.put("remoteurl", REMOTEURL);
		columns.put("localpath", LOCALPATH);
		columns.put("sendlevel", SENDLEVEL);
		columns.put("attime", ATTIME);
		columns.put("validtime", VALIDTIME);
		columns.put("sendstatus", SENDSTATUS);
		columns.put("errorcode", ERRORCODE);
		columns.put("recvtime", RECVTIME);
		columns.put("sendtime", SENDTIME);
		columns.put("sendtype", SENDTYPE);
		columns.put("attimeflag", ATTIMEFLAG);
		columns.put("sa", SA);
		columns.put("svrtype", SVRTYPE);
		columns.put("p1", P1);
		columns.put("p2", P2);
		columns.put("p3", P3);
		columns.put("p4", P4);
		columns.put("usermsgid", USERMSGID);
		columns.put("moduleid", MODULEID);
		columns.put("retflag", RETFLAG);
		columns.put("templid",TEMPLID);
	};

	/**
	 * 返回实体类字段与数据库字段实体类映射的map集合
	 * 
	 * @return
	 */
	public static Map<String, String> getORM()
	{
		return columns;
	}
}
