					
package com.montnets.emp.entity.engine;


public class LfAutoreply
{
	//状态：1-启用；2-关闭
	private Integer state;

	private Long id;
	//回复内容
	private String replycontent;

	private String corpcode;
	//回复类型：1-无效指令回复
	private Integer type;

	public LfAutoreply(){
	} 

	public Integer getState(){

		return state;
	}

	public void setState(Integer state){

		this.state= state;

	}

	public Long getId(){

		return id;
	}

	public void setId(Long id){

		this.id= id;

	}

	public String getReplycontent(){

		return replycontent;
	}

	public void setReplycontent(String replycontent){

		this.replycontent= replycontent;

	}

	public String getCorpcode(){

		return corpcode;
	}

	public void setCorpcode(String corpcode){

		this.corpcode= corpcode;

	}

	public Integer getType(){

		return type;
	}

	public void setType(Integer type){

		this.type= type;

	}

}

					