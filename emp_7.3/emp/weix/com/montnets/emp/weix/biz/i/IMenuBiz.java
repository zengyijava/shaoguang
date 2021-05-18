package com.montnets.emp.weix.biz.i;

import java.util.LinkedHashMap;
import org.json.simple.JSONObject;
import com.montnets.emp.entity.weix.LfWcMenu;


public interface IMenuBiz
{
	/**
	 * 更新菜单信息
	 * @param objectMap
	 * @param conditionMap
	 * @return1
	 */
	public boolean updateMenu(LinkedHashMap<String, String> objectMap, LinkedHashMap<String, String> conditionMap);
	
	/**
	 * 调整菜单的顺序
	 * @param one
	 * @param two
	 * @return1
	 */
	public boolean updateOrder(String one, String two);

	/**
	 * 生成KeyId (公众帐号的Id+父id+随机编号)
	 * 
	 * @param pId
	 * @param mId
	 * @return
	 */
	public String getKeyId(String aId, String pId);

	/**
	 * 删除菜单
	 * 
	 * @param menu
	 *        菜单对象
	 * @return
	 */
	public boolean delMenu(LfWcMenu menu);

	/**
	 * 得到当前公众帐号对应的菜单json格式
	 * 
	 * @param acct
	 * @return
	 */
	public JSONObject getMenuJsonByAccount(String aid);

}
