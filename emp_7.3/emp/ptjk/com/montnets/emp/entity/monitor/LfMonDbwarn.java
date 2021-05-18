/**
 * @description
 * @author chentingsheng <cts314@163.com>
 * @datetime 2016-4-25 下午06:01:43
 */
package com.montnets.emp.entity.monitor;

import java.sql.Timestamp;

/**
 * @功能概要：
 * @项目名称： emp
 * @初创作者： chentingsheng <cts314@163.com>
 * @公司名称： ShenZhen Montnets Technology CO.,LTD.
 * @创建时间： 2016-4-25 下午06:01:43
 *        <p>
 *        修改记录1：
 *        </p>
 * 
 *        <pre>
 *      修改日期：
 *      修改人：
 *      修改内容：
 * </pre>
 */

public class LfMonDbwarn
{

	private String		monemail;

	private Long		id;

	private Timestamp	createtime;

	private Timestamp	updatetime;

	private String		monphone;

	public LfMonDbwarn()
	{
	}

	public String getMonemail()
	{

		return monemail;
	}

	public void setMonemail(String monemail)
	{

		this.monemail = monemail;

	}

	public Long getId()
	{

		return id;
	}

	public void setId(Long id)
	{

		this.id = id;

	}

	public Timestamp getCreatetime()
	{

		return createtime;
	}

	public void setCreatetime(Timestamp createtime)
	{

		this.createtime = createtime;

	}

	public Timestamp getUpdatetime()
	{

		return updatetime;
	}

	public void setUpdatetime(Timestamp updatetime)
	{

		this.updatetime = updatetime;

	}

	public String getMonphone()
	{

		return monphone;
	}

	public void setMonphone(String monphone)
	{

		this.monphone = monphone;

	}

}
