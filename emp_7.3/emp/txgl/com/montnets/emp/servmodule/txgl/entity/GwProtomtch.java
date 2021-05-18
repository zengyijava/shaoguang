package com.montnets.emp.servmodule.txgl.entity;
import java.sql.Timestamp;

/**
 * 接口参数映射关系表
 * 用户接口参数与API V5.3标准接口参数映射关系表
 * @author Administrator
 *
 */
public class GwProtomtch {

	//客户接口字段类型
	private Integer cargtype;
	//固定字段值
	private String cargvalue;
	//上级节点。例如：第一级节点为空字符串，其他为上级节点
	private String belong;
	//方法名。例如：single_send
	private String funname;
	//类型:请求
	private Integer cmdtype;
	//梦网字段名。例如：userid
	private String margname;
	//记录创建时间
	private Timestamp createtime;
	//是否有下级节点，例如：0无子节点，1有下级子节点
	private Integer belongtype;
	//客户接口字段名。例如：cstuserid
	private String cargname;
	//记录修改时间
	private Timestamp modiftime;
	//方法类型。和FUNNAME、CMDTYPE、CARGNAME、BELONG一起作为主键
	private String funtype;
	//预留
	private String reserve;
	//自增ID
	private Integer id;
	//企业ID
	private Integer ecid;

	public GwProtomtch(){
	} 

	public Integer getCargtype(){

		return cargtype;
	}

	public void setCargtype(Integer cargtype){

		this.cargtype= cargtype;

	}

	public String getCargvalue(){

		return cargvalue;
	}

	public void setCargvalue(String cargvalue){

		this.cargvalue= cargvalue;

	}

	public String getBelong(){

		return belong;
	}

	public void setBelong(String belong){

		this.belong= belong;

	}

	public String getFunname(){

		return funname;
	}

	public void setFunname(String funname){

		this.funname= funname;

	}

	public Integer getCmdtype(){

		return cmdtype;
	}

	public void setCmdtype(Integer cmdtype){

		this.cmdtype= cmdtype;

	}

	public String getMargname(){

		return margname;
	}

	public void setMargname(String margname){

		this.margname= margname;

	}

	public Timestamp getCreatetime(){

		return createtime;
	}

	public void setCreatetime(Timestamp createtime){

		this.createtime= createtime;

	}

	public Integer getBelongtype(){

		return belongtype;
	}

	public void setBelongtype(Integer belongtype){

		this.belongtype= belongtype;

	}

	public String getCargname(){

		return cargname;
	}

	public void setCargname(String cargname){

		this.cargname= cargname;

	}

	public Timestamp getModiftime(){

		return modiftime;
	}

	public void setModiftime(Timestamp modiftime){

		this.modiftime= modiftime;

	}

	public String getFuntype(){

		return funtype;
	}

	public void setFuntype(String funtype){

		this.funtype= funtype;

	}

	public String getReserve(){

		return reserve;
	}

	public void setReserve(String reserve){

		this.reserve= reserve;

	}

	public Integer getId(){

		return id;
	}

	public void setId(Integer id){

		this.id= id;

	}

	public Integer getEcid(){

		return ecid;
	}

	public void setEcid(Integer ecid){

		this.ecid= ecid;

	}
}
