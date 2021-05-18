package  com.montnets.emp.wyquery.bean;

/**
 * 报表配置文件信息对象
 * @company ShenZhen Montnets Technology CO.,LTD.
 * @datetime 2015-10-13 下午02:32:05
 * @description
 */
public class RptWyConfInfo
{
	//菜单id
	private String menuid;
	//菜单描述
	private String description;
	//列id
	private String colId;
	//是否显示。0-不显示；1-显示
	private String display;
	//表格标题显示名称
	private String name;
	//类型  1  接收成功数=接收成功数 2接收成功数=接收成功数+未返数
	private String type;
	
	public String getType()
	{
		return type;
	}
	public void setType(String type)
	{
		this.type = type;
	}
	public String getMenuid()
	{
		return menuid;
	}
	public void setMenuid(String menuid)
	{
		this.menuid = menuid;
	}
	public String getDescription()
	{
		return description;
	}
	public void setDescription(String description)
	{
		this.description = description;
	}
	public String getColId()
	{
		return colId;
	}
	public void setColId(String colId)
	{
		this.colId = colId;
	}
	public String getDisplay()
	{
		return display;
	}
	public void setDisplay(String display)
	{
		this.display = display;
	}
	public String getName()
	{
		return name;
	}
	public void setName(String name)
	{
		this.name = name;
	}
}
