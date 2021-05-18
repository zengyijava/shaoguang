/**
 * @description  
 * @author chentingsheng <cts314@163.com>
 * @datetime 2016-4-25 上午08:49:37
 */
package com.montnets.emp.entity.monitor;

import java.sql.Timestamp;
/**
 * @功能概要：
 * @项目名称： emp
 * @初创作者： chentingsheng <cts314@163.com>
 * @公司名称： ShenZhen Montnets Technology CO.,LTD.
 * @创建时间： 2016-4-25 上午08:49:37
 * <p>修改记录1：</p>
 * <pre>    
 *      修改日期：
 *      修改人：
 *      修改内容：
 * </pre>
 */


public class LfMonDbopr
{

	private Long procenode;

	private Long id;

	private Timestamp createtime;

	public LfMonDbopr(){
	} 

	public Long getProcenode(){

		return procenode;
	}

	public void setProcenode(Long procenode){

		this.procenode= procenode;

	}

	public Long getId(){

		return id;
	}

	public void setId(Long id){

		this.id= id;

	}

	public Timestamp getCreatetime(){

		return createtime;
	}

	public void setCreatetime(Timestamp createtime){

		this.createtime= createtime;

	}

}


