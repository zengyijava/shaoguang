package com.montnets.emp.servmodule.txgl.entity;


/**
 * 
 * @project emp_std_new
 * @author huangzb
 * @company ShenZhen Montnets Technology CO.,LTD.
 * @datetime 2014-8-1 下午02:06:58
 * @description 上行指令通道路由
 */
public class AcmdPort implements java.io.Serializable{


	private static final long serialVersionUID = -4170642395356731527L;
	
	// 主键ID 自动增长
	private Long id;  
	//通道ID，对应表XT_GATE_QUEUE表中的ID
	private Long gateId;
	//指令ID，对应表A_CMD_ROUTE中的ID
	private Long cmdId;
	//绑定状态：0 启动 1 禁用
	private Integer status;
	//通道子号，可为空
	private String cpno;
	//通道指令匹配失败后，对上行的处理：0 进行子号路由;1 按默认指令处理;2 中断，写库
	private Integer failOpt;// 指令类型
	//默认处理指令，对应表A_CMD_ROUTE中的ID
	private Long defCmdId;
	
	public AcmdPort(){
	}

	public Long getId()
	{
		return id;
	}

	public void setId(Long id)
	{
		this.id = id;
	}

	public Long getGateId()
	{
		return gateId;
	}

	public void setGateId(Long gateId)
	{
		this.gateId = gateId;
	}

	public Long getCmdId()
	{
		return cmdId;
	}

	public void setCmdId(Long cmdId)
	{
		this.cmdId = cmdId;
	}

	public Integer getStatus()
	{
		return status;
	}

	public void setStatus(Integer status)
	{
		this.status = status;
	}

	public String getCpno()
	{
		return cpno;
	}

	public void setCpno(String cpno)
	{
		this.cpno = cpno;
	}

	public Integer getFailOpt()
	{
		return failOpt;
	}

	public void setFailOpt(Integer failOpt)
	{
		this.failOpt = failOpt;
	}

	public Long getDefCmdId()
	{
		return defCmdId;
	}

	public void setDefCmdId(Long defCmdId)
	{
		this.defCmdId = defCmdId;
	}
	
	
	
	
}
