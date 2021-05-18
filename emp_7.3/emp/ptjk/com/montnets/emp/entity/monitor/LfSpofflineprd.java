
package com.montnets.emp.entity.monitor;

import java.sql.Timestamp;
/**
 * 
 * @功能概要：SP离线时间段告警阀值管理表实体类
 * @项目名称： emp
 * @初创作者： tanglili <jack860127@126.com>
 * @公司名称： ShenZhen Montnets Technology CO.,LTD.
 * @创建时间： 2016-1-23 下午02:05:57
 * <p>修改记录1：</p>
 * <pre>    
 *      修改日期：
 *      修改人：
 *      修改内容：
 * </pre>
 */
public class LfSpofflineprd
{
	//结束时间段(小时)
	private Integer endhour;
    //最后更新的操作员ID
	private Long userid;
    //创建时间
	private Timestamp createtime;
    //自增ID
	private Long id;
    //更新时间
	private Timestamp updatetime;
	//企业编码
	private String corpcode;
    //时长
	private Integer duration;
    //开始时间段(小时)
	private Integer beginhour;
    //SP账号
	private String spaccountid;

	public LfSpofflineprd(){
		//初始化时间
		createtime = new Timestamp(System.currentTimeMillis());
		updatetime=new Timestamp(System.currentTimeMillis());
	} 

	public Integer getEndhour(){

		return endhour;
	}

	public void setEndhour(Integer endhour){

		this.endhour= endhour;

	}

	public Long getUserid(){

		return userid;
	}

	public void setUserid(Long userid){

		this.userid= userid;

	}

	public Timestamp getCreatetime(){

		return createtime;
	}

	public void setCreatetime(Timestamp createtime){

		this.createtime= createtime;

	}

	public Long getId(){

		return id;
	}

	public void setId(Long id){

		this.id= id;

	}

	public Timestamp getUpdatetime(){

		return updatetime;
	}

	public void setUpdatetime(Timestamp updatetime){

		this.updatetime= updatetime;

	}

	public String getCorpcode(){

		return corpcode;
	}

	public void setCorpcode(String corpcode){

		this.corpcode= corpcode;

	}

	public Integer getDuration(){

		return duration;
	}

	public void setDuration(Integer duration){

		this.duration= duration;

	}

	public Integer getBeginhour(){

		return beginhour;
	}

	public void setBeginhour(Integer beginhour){

		this.beginhour= beginhour;

	}

	public String getSpaccountid(){

		return spaccountid;
	}

	public void setSpaccountid(String spaccountid){

		this.spaccountid= spaccountid;

	}

}
