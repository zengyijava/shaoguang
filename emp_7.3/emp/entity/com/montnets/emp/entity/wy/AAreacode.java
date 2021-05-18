/**
 * @description
 * @author chentingsheng <cts314@163.com>
 * @datetime 2014-3-25 上午11:11:54
 */
package com.montnets.emp.entity.wy;

import java.sql.Timestamp;

/**
 * @description
 * @project p_wygl
 * @company ShenZhen Montnets Technology CO.,LTD.
 * @author chentingsheng <cts314@163.com>
 * @datetime 2014-3-25 上午11:11:54
 */

public class AAreacode
{
	// 国家
	private String		areaname;

	// 自增ID
	private Integer		id;

	// 更新时间
	private Timestamp	createtime;

	// 电话代码
	private String		areacode;

	public AAreacode()
	{
	}

	public String getAreaname()
	{

		return areaname;
	}

	public void setAreaname(String areaname)
	{

		this.areaname = areaname;

	}

	public Integer getId()
	{

		return id;
	}

	public void setId(Integer id)
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

	public String getAreacode()
	{

		return areacode;
	}

	public void setAreacode(String areacode)
	{

		this.areacode = areacode;

	}

}
