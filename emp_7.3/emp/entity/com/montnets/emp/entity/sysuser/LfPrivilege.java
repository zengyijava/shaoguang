package com.montnets.emp.entity.sysuser;

/**
 * LF_PRIVILEGE  对应的实体类
 * 
 * @project emp
 * @author liujun <859127164@qq.com>   
 * @company ShenZhen Montnets Technology CO.,LTD.
 * @datetime 2011-1-21 下午12:45:44
 * @description 权限表
 */
public class LfPrivilege implements java.io.Serializable
{

	/**
	 * 
	 */
	private static final long serialVersionUID = -453720090586239004L;

	/**
	 * 
	 */
	// private static final long serialVersionUID = -7915563629486943322L;
	//主键
	private Long privilegeId;   

	//资源id对应lf_third_control
	private Long resourceId;
	//操作类型
	private Long operateId;

	//功能名称
	private String comments;   

	//每个模块功能对应的权限code
	private String privCode;

	//菜单名
	private String menuName;   

	//模块名
	private String modName;  

	//菜单编码
	private String menuCode;   

	//菜单跳转链接
	private String menuSite;
	
	//繁体功能名称
	private String zhTwComments;   
	//英文功能名称
	private String zhHkComments;
	//繁体菜单名
	private String zhTwMenuName;
	//英文菜单名
	private String zhHkMenuName;
	//繁体模块名
	private String zhTwModName;
	//英文模块名
	private String zhHkModName;   

 	public LfPrivilege()
	{
	}

	public Long getPrivilegeId()
	{
		return privilegeId;
	}

	public void setPrivilegeId(Long privilegeId)
	{
		this.privilegeId = privilegeId;
	}

	public Long getResourceId()
	{
		return resourceId;
	}

	public void setResourceId(Long resourceId)
	{
		this.resourceId = resourceId;
	}

	public Long getOperateId()
	{
		return operateId;
	}

	public void setOperateId(Long operateId)
	{
		this.operateId = operateId;
	}

	public String getComments()
	{
		return comments;
	}

	public void setComments(String comments)
	{
		this.comments = comments;
	}

	public String getPrivCode()
	{
		return privCode;
	}

	public void setPrivCode(String privCode)
	{
		this.privCode = privCode;
	}

	public String getMenuName()
	{
		return menuName;
	}

	public void setMenuName(String menuName)
	{
		this.menuName = menuName;
	}

	public String getModName()
	{
		return modName;
	}

	public void setModName(String modName)
	{
		this.modName = modName;
	}

	public String getMenuCode()
	{
		return menuCode;
	}

	public void setMenuCode(String menuCode)
	{
		this.menuCode = menuCode;
	}

	public String getMenuSite()
	{
		return menuSite;
	}

	public void setMenuSite(String menuSite)
	{
		this.menuSite = menuSite;
	}

	public String getZhTwComments() {
		return zhTwComments;
	}

	public void setZhTwComments(String zhTwComments) {
		this.zhTwComments = zhTwComments;
	}

	public String getZhHkComments() {
		return zhHkComments;
	}

	public void setZhHkComments(String zhHkComments) {
		this.zhHkComments = zhHkComments;
	}

	public String getZhTwMenuName() {
		return zhTwMenuName;
	}

	public void setZhTwMenuName(String zhTwMenuName) {
		this.zhTwMenuName = zhTwMenuName;
	}

	public String getZhHkMenuName() {
		return zhHkMenuName;
	}

	public void setZhHkMenuName(String zhHkMenuName) {
		this.zhHkMenuName = zhHkMenuName;
	}

	public String getZhTwModName() {
		return zhTwModName;
	}

	public void setZhTwModName(String zhTwModName) {
		this.zhTwModName = zhTwModName;
	}

	public String getZhHkModName() {
		return zhHkModName;
	}

	public void setZhHkModName(String zhHkModName) {
		this.zhHkModName = zhHkModName;
	}
 
}