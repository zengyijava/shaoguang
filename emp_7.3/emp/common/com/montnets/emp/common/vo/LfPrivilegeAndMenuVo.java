package com.montnets.emp.common.vo;

public class LfPrivilegeAndMenuVo {
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

	// 繁体菜单标题
	private String zhTwTitle;
	// 英文菜单标题
	private String zhHkTitle;
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
