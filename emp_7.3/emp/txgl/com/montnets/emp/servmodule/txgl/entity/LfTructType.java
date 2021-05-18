package com.montnets.emp.servmodule.txgl.entity;

public class LfTructType implements java.io.Serializable{

	/**
	 * 指令类型
	 */
	private static final long serialVersionUID = 3450107475796005333L;

	private Long id; // 主键 标识  自动增长
	private String type; // 类型编码 
	private String name; // 类型名称 
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	
	
	
}
