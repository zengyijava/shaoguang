package com.montnets.emp.common.timer;

/**
 * 定时器的错误代码
 * @project chinalifeemp
 * @author huangzhibin <307260621@qq.com>
 * @company ShenZhen Montnets Technology CO.,LTD.
 * @datetime 2011-1-22 下午04:32:00
 * @description
 */
public interface IErrorCode {
	

	//
	public static  String TT1231 = "TT:1231";
	
	//
	public static  String TT1211 = "TT:1211";
	
	/**
	 * 
	 */
	public static  String MT1413 = "MT:1413";

	//
	public static  String MT1414 = "MT:1414";
	
	//
	public static  String MT1423 = "MT:1413";

	//
	public static  String TR13 = "TR:13";
	
	
	//--------------------------------------------------------
	
	//定时器查询数据库异常
	public static  String E_DS_D100 = "E-DS-D100";
	
	//定时器业务层空指针异常
	public static  String E_DS_B100 = "E-DS-B100";
	
	//定时器通用业务层异常
	public static  String E_DS_B200 = "E-DS-B200";
	
	//任务已过期
	public static  String E_DS_B201 = "E-DS-B201";
	
	//不满足发送条件，如有历史记录
	public static  String E_DS_B202 = "E-DS-B202";
	
	//设置下次执行时间异常
	public static  String E_DS_B203 = "E-DS-B203";
	
	//检查定时任务是否可执行异常
	public static  String E_DS_B204 = "E-DS-B204";
	
	//任务已过期，但处理异常
	public static  String E_DS_B205 = "E-DS-B205";
	
	//定时器类加载异常
	public static  String E_DS_B300 = "E-DS-B300";
	
	//定时任务发送失败
	public static  String E_DS_B400 = "E-DS-B400";
	
	//发送前更新数据库异常
	public static  String E_DS_D200 = "E-DS-D200";
	
	//数据库连接失败
	public static  String E_DS_D300 = "E-DS-D300";
	
	//数据库新增异常
	public static  String E_DS_D400 = "E-DS-D400";
	
	//数据库查询异常
	public static  String E_DS_D500 = "E-DS-D500";
	
	//发送后更新数据库异常
	public static  String E_DS_D201 = "E-DS-D201";
	
	//数据库更新异常
	public static  String E_DS_D600 = "E-DS-D600";
	
	//发送前确认是否已更新到数据库
	public static  String E_DS_D202 = "E-DS-D201";
}
