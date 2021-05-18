package com.montnets.emp.common.constant;

/** 错误码表
 * servlet层统一以V1开头，业务层(BIZ)统一以B2开头，DAO层以D3开头，原子层以A4开头
 */
public interface IErrorCode {
	String B20000 = "B20000";
	String B20001 = "B20001";
	String B20002 = "B20002";
	String B20003 = "B20003";
	String B20004 = "B20004";
	String B20005 = "B20005";
	String B20006 = "B20006";
	String B20007 = "B20007";
	String B20008 = "B20008";
	String B20009 = "B20009";
	String B20010 = "B20010";
	String B20011 = "B20011";
	String B20012 = "B20012";
	String B20013 = "B20013";
	String B20014 = "B20014";
	String B20015 = "B20015";
	String B20016 = "B20016";
	String B20017 = "B20017";
	String B20018 = "B20018";
	String B20019 = "B20019";
	String B20020 = "B20020";
	String B20021 = "B20021";
	String B20022 = "B20022";
	String B20023 = "B20023";
	String B20024 = "B20024";
	/********运营 商余额查询错误码************/
	String B20025 = "B20025";
	String B20026 = "B20026";
	String B20027 = "B20027";
	String B20028 = "B20028";
	String B20029 = "B20029";
	String B20030 = "B20030";
	String B20031 = "B20031";
	String B20032 = "B20032";
	String B20033 = "B20033";
	String B20034 = "B20034";
	/**
	 * MBOSS接口返回信息
	 */
	String B20035 = "6001";
	String B20036 = "6002";
	String B20037 = "6003";
	/********************/
	String B20038 = "B20038";
	String B20039 = "B20039";
	
	String B20040 = "B20040";
	String B20041 = "B20041";
	String B20042 = "B20042";
	String B20043 = "B20043";
	String B20044 = "B20044";
	String B20045 = "B20045";

	String B20046 = "B20046";
	
	String A40000 = "A40000";
	
	String V10000 = "V10000";
	String V10001 = "V10001";
	String V10002 = "V10002";
	String V10003 = "V10003";
	String V10004 = "V10004";
	String V10005 = "V10005";
	String V10006 = "V10006";
	String V10007 = "V10007";
	String V10008 = "V10008";
	String V10009 = "V10009";
	String V10010 = "V10010";
	String V10011 = "V10011";
	String V10012 = "V10012";
	String V10013 = "V10013";
	String V10014 = "V10014";
	String V10015 = "V10015";
	String V10016 = "V10016";
	String V10017 = "V10017";
	String V10018 = "V10018";
	String V10019 = "V10019";
	String V10020 = "V10020";
	String V10021 = "V10021";
	String V10022 = "V10022";

	/**
	 * 富信发送异常
	 */
	String RM0001 = "RM0001";
	String RM0002 = "RM0002";
	String RM0003 = "RM0003";
	String RM0004 = "RM0004";
	String RM0005 = "RM0005";
	String RM0006 = "RM0006";
	String RM0007 = "RM0007";
	String RM0008 = "RM0008";
	String RM0009 = "RM0009";
	String RM00010 = "RM00010";
	String RM00011 = "RM00011";
	String RM00012 = "RM00012";
	String RM00013 = "RM00013";
	String RM00014 = "RM00014";
	String RM00015 = "RM00015";
	String RM00016 = "RM00016";
	String RM00017 = "RM00017";
	String RM00018 = "RM00018";

	String getErrorInfo(String code);
	
	String getErrorDes(String code);
}
