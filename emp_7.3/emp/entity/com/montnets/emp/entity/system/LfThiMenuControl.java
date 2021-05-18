/**
 * 
 */
package com.montnets.emp.entity.system;

/**
 * @project montnets_entity
 * @author wuxiaotao <819475589@qq.com>
 * @company ShenZhen Montnets Technology CO.,LTD.
 * @datetime 2011-9-7 下午06:57:51
 * @description 
 */

public class LfThiMenuControl implements java.io.Serializable
{

	/**
	 * 
	 */
	private static final long serialVersionUID = -3907195171780970420L;

	//标识列
	private Integer tid;
	
	//菜单顺序
	private Integer menuNum;
	
	// 菜单标题
	private String title;
	
	 //权限菜单
	private Integer priMenu;
	
	//权限菜单顺序
	private Integer priOrder;
	
	// 繁体菜单标题
	private String zhTwTitle;

	// 英文菜单标题
	private String zhHkTitle;
	
	public LfThiMenuControl(){
		
	}

	//标识列
	public Integer getTid()
	{
		return tid;
	}
	public void setTid(Integer tid)
	{
		this.tid = tid;
	}

	//菜单顺序
	public Integer getMenuNum()
	{
		return menuNum;
	}
	public void setMenuNum(Integer menuNum)
	{
		this.menuNum = menuNum;
	}

	// 菜单标题
	public String getTitle()
	{
		return title;
	}
	public void setTitle(String title)
	{
		this.title = title;
	}

 
	 //权限菜单
	public Integer getPriMenu()
	{
		return priMenu;
	}

	public void setPriMenu(Integer priMenu)
	{
		this.priMenu = priMenu;
	}

	//权限菜单顺序
	public Integer getPriOrder() {
		return priOrder;
	}

	public void setPriOrder(Integer priOrder) {
		this.priOrder = priOrder;
	}

	public String getZhTwTitle() {
		return zhTwTitle;
	}

	public void setZhTwTitle(String zhTwTitle) {
		this.zhTwTitle = zhTwTitle;
	}

	public String getZhHkTitle() {
		return zhHkTitle;
	}

	public void setZhHkTitle(String zhHkTitle) {
		this.zhHkTitle = zhHkTitle;
	}
 
}
