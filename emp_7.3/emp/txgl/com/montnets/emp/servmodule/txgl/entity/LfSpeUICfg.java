/**
 * Program  : LfSpeUICfg.java
 * Author   : zousy
 * Create   : 2013-11-20 下午03:56:40
 * company ShenZhen Montnets Technology CO.,LTD.
 *
 */

package com.montnets.emp.servmodule.txgl.entity;

/**
 * 个性化界面配置信息实体
 * @author   Administrator <510061684@qq.com>
 * @version  1.0.0
 * @2013-11-20 下午03:56:40
 */
public class LfSpeUICfg
{	
	//内页公司名
	private String companyName;
	//内页公司logo
	private String companyLogo;
	//登录页背景图
	private String bgImg;
	//登录页logo
	private String loginLogo;
	//个性化显示内容
	private String dispContent;
	//显示类别  1 默认 0个性化
	private Integer displayType;
	//机构编码
	private String corpCode;
	public String getCompanyName()
	{
		return companyName;
	}
	public void setCompanyName(String companyName)
	{
		this.companyName = companyName;
	}
	public String getCompanyLogo()
	{
		return companyLogo;
	}
	public void setCompanyLogo(String companyLogo)
	{
		this.companyLogo = companyLogo;
	}
	public String getBgImg()
	{
		return bgImg;
	}
	public void setBgImg(String bgImg)
	{
		this.bgImg = bgImg;
	}
	public String getLoginLogo()
	{
		return loginLogo;
	}
	public void setLoginLogo(String loginLogo)
	{
		this.loginLogo = loginLogo;
	}
	public String getDispContent()
	{
		return dispContent;
	}
	public void setDispContent(String dispContent)
	{
		this.dispContent = dispContent;
	}
	public String getCorpCode()
	{
		return corpCode;
	}
	public void setCorpCode(String corpCode)
	{
		this.corpCode = corpCode;
	}
	public Integer getDisplayType()
	{
		return displayType;
	}
	public void setDisplayType(Integer displayType)
	{
		this.displayType = displayType;
	}
}

