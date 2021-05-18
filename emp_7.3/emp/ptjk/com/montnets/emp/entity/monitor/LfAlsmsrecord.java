/**
 * @description
 * @author chentingsheng <cts314@163.com>
 * @datetime 2014-4-3 下午05:47:47
 */
package com.montnets.emp.entity.monitor;

/**
 * @description
 * @project p_ptjk
 * @company ShenZhen Montnets Technology CO.,LTD.
 * @author chentingsheng <cts314@163.com>
 * @datetime 2014-4-3 下午05:47:47
 */
import java.sql.Timestamp;


public class LfAlsmsrecord
{
	// 手机号码
	private String phone;
	// 短信内容
	private String message;
	// 错误代码（状态报告）
	private String errorcode;
	// 主端口
	private String spgate;
	// 接收时间
	private Timestamp recvtime;
	// 短信发送时间
	private Timestamp sendtime;
	// 自增ID
	private Long id;
	// 发送标志(0:未接收到状态报告；1：已接收到状态报告；2：网优发送)
	private Integer sendflag;
	// 发送状态（0：失败；1：成功）
	private Integer sendstatus;
	// 消息流水号
	private Long msgid;
	// 运营商:(0: 移动,1: 联通,21: 电信,5: 国际)
	private Integer unicom;
	// SP账号
	private String userid;

	public LfAlsmsrecord(){
	} 

	public String getPhone(){

		return phone;
	}

	public void setPhone(String phone){

		this.phone= phone;

	}

	public String getMessage(){

		return message;
	}

	public void setMessage(String message){

		this.message= message;

	}

	public String getErrorcode(){

		return errorcode;
	}

	public void setErrorcode(String errorcode){

		this.errorcode= errorcode;

	}

	public String getSpgate(){

		return spgate;
	}

	public void setSpgate(String spgate){

		this.spgate= spgate;

	}

	public Timestamp getRecvtime(){

		return recvtime;
	}

	public void setRecvtime(Timestamp recvtime){

		this.recvtime= recvtime;

	}

	public Timestamp getSendtime(){

		return sendtime;
	}

	public void setSendtime(Timestamp sendtime){

		this.sendtime= sendtime;

	}

	public Long getId(){

		return id;
	}

	public void setId(Long id){

		this.id= id;

	}

	public Integer getSendflag(){

		return sendflag;
	}

	public void setSendflag(Integer sendflag){

		this.sendflag= sendflag;

	}

	public Integer getSendstatus(){

		return sendstatus;
	}

	public void setSendstatus(Integer sendstatus){

		this.sendstatus= sendstatus;

	}

	public Long getMsgid(){

		return msgid;
	}

	public void setMsgid(Long msgid){

		this.msgid= msgid;

	}

	public Integer getUnicom(){

		return unicom;
	}

	public void setUnicom(Integer unicom){

		this.unicom= unicom;

	}

	public String getUserid(){

		return userid;
	}

	public void setUserid(String userid){

		this.userid= userid;

	}

}
