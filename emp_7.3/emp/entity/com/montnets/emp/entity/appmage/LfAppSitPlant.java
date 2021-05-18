/**
 * Program  : LfAppSitPlant.java
 * Author   : zousy
 * Create   : 2014-6-16 下午03:58:02
 * company ShenZhen Montnets Technology CO.,LTD.
 *
 */

package com.montnets.emp.entity.appmage;

import java.sql.Timestamp;

/**
 * 
 * @author   zousy <zousy999@qq.com>
 * @version  1.0.0
 * @2014-6-16 下午03:58:02
 */
public class LfAppSitPlant
{
	//页面元素类型  list  head
	private String planttype;

	private Long pageid;

	private String name;
	
	//页面元素对应json串
	private String feildvalues;

	private Timestamp moditytime;

	private Timestamp createtime;

	private String corpcode;

	private Long sid;

	private String feildnames;

	private Long plantid;

	public LfAppSitPlant(){
	} 

	public String getPlanttype(){

		return planttype;
	}

	public void setPlanttype(String planttype){

		this.planttype= planttype;

	}

	public Long getPageid(){

		return pageid;
	}

	public void setPageid(Long pageid){

		this.pageid= pageid;

	}

	public String getName(){

		return name;
	}

	public void setName(String name){

		this.name= name;

	}

	public String getFeildvalues(){

		return feildvalues;
	}

	public void setFeildvalues(String feildvalues){

		this.feildvalues= feildvalues;

	}

	public Timestamp getModitytime(){

		return moditytime;
	}

	public void setModitytime(Timestamp moditytime){

		this.moditytime= moditytime;

	}

	public Timestamp getCreatetime(){

		return createtime;
	}

	public void setCreatetime(Timestamp createtime){

		this.createtime= createtime;

	}

	public String getCorpcode(){

		return corpcode;
	}

	public void setCorpcode(String corpcode){

		this.corpcode= corpcode;

	}

	public Long getSid(){

		return sid;
	}

	public void setSid(Long sid){

		this.sid= sid;

	}

	public String getFeildnames(){

		return feildnames;
	}

	public void setFeildnames(String feildnames){

		this.feildnames= feildnames;

	}

	public Long getPlantid(){

		return plantid;
	}

	public void setPlantid(Long plantid){

		this.plantid= plantid;

	}

	public LfAppSitPlant(String corpcode, Long sid)
	{
		super();
		this.corpcode = corpcode;
		this.sid = sid;
	}
	
	
}

