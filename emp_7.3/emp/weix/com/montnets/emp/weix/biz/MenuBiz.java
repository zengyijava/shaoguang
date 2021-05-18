package com.montnets.emp.weix.biz;

import java.sql.Connection;
import java.util.LinkedHashMap;
import java.util.List;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import com.montnets.emp.common.biz.SuperBiz;
import com.montnets.emp.common.constant.StaticValue;
import com.montnets.emp.common.context.EmpExecutionContext;
import com.montnets.emp.entity.weix.LfWcMenu;
import com.montnets.emp.weix.biz.i.IMenuBiz;
import com.montnets.emp.weix.common.util.GlobalMethods;
import com.montnets.emp.weix.common.util.RandomStrUtil;
import com.montnets.emp.weix.dao.MenuDao;

public class MenuBiz extends SuperBiz implements IMenuBiz
{
	/**
	 * 更新菜单信息
	 * @param objectMap
	 * @param conditionMap
	 * @return1
	 */
	public boolean updateMenu(LinkedHashMap<String, String> objectMap, LinkedHashMap<String, String> conditionMap)
	{
		Connection conn = empTransDao.getConnection();
		boolean result = true;
		try
		{
			empTransDao.update(conn, LfWcMenu.class, objectMap, conditionMap);
		}
		catch (Exception e)
		{
			result = false;
			EmpExecutionContext.error(e, "更新菜单信息出现错误！");
		}
		finally
		{
			empTransDao.closeConnection(conn);
		}
		return result;
	}
	
	/**
	 * 调整菜单的顺序
	 * @param one
	 * @param two
	 * @return1
	 */
	public boolean updateOrder(String one, String two)
	{
		Connection conn = empTransDao.getConnection();
		boolean result = true;
		try
		{
			LinkedHashMap<String, String> con1 = new LinkedHashMap<String, String>();
			LinkedHashMap<String, String> con2 = new LinkedHashMap<String, String>();
			con1.put("MId", one);
			con2.put("MId", two);
			empTransDao.beginTransaction(conn);
			List<LfWcMenu> list1 = empDao.findListByCondition(LfWcMenu.class, con1, null);
			List<LfWcMenu> list2 = empDao.findListByCondition(LfWcMenu.class, con2, null);
			LfWcMenu menu1 = null;
			LfWcMenu menu2 = null;
			if(!GlobalMethods.isNullList(list1) && !GlobalMethods.isNullList(list2))
			{
				menu1 = list1.get(0);
				menu2 = list2.get(0);
				int order1 = menu1.getMorder();
				int order2 = menu2.getMorder();
				LinkedHashMap<String, String> u1 = new LinkedHashMap<String, String>();
				LinkedHashMap<String, String> u2 = new LinkedHashMap<String, String>();
				u1.put("morder", String.valueOf(order2));
				u2.put("morder", String.valueOf(order1));
				empTransDao.update(conn, LfWcMenu.class, u1, con1);
				empTransDao.update(conn, LfWcMenu.class, u2, con2);
				empTransDao.commitTransaction(conn);
			}
			else
			{
				throw new RuntimeException("未找到菜单记录!");
			}
		}
		catch (Exception e)
		{
			empTransDao.rollBackTransaction(conn);
			result = false;
			EmpExecutionContext.error(e, "更新菜单顺序出现错误！");
		}
		finally
		{
			empTransDao.closeConnection(conn);
		}
		return result;
	}

	/**
	 * 生成KeyId (公众帐号的Id+父id+随机编号)
	 * 
	 * @param pId
	 * @param mId
	 * @return
	 */
	public String getKeyId(String aId, String pId)
	{
		StringBuffer keyStr = new StringBuffer();
		keyStr.append("M_");
		if(aId == null || "".equals(aId))
		{
			keyStr.append("0");
		}
		else
		{
			keyStr.append(aId);
		}
		keyStr.append("_");
		if(pId == null || "".equals(pId))
		{
			keyStr.append("0");
		}
		else
		{
			keyStr.append(pId);
		}
		keyStr.append("_");
		keyStr.append(RandomStrUtil.getLowerStr(8));
		return keyStr.toString();
	}

	/**
	 * 删除菜单
	 * 
	 * @param menu
	 *        菜单对象
	 * @return
	 */
	public boolean delMenu(LfWcMenu menu)
	{
		// 获取连接
		Connection conn = empTransDao.getConnection();
		boolean result = true;
		try
		{
			empTransDao.beginTransaction(conn);
			// 删除本记录
			if(empTransDao.delete(conn, LfWcMenu.class, menu.getMId().toString()) > 0)
			{
				// 如果是一级菜单
				if(menu.getPId().intValue() == 0)
				{
					// 删除相关的子级
					LinkedHashMap<String, String> conditionMap = new LinkedHashMap<String, String>();
					conditionMap.put("PId", menu.getMId().toString());
					empTransDao.delete(conn, LfWcMenu.class, conditionMap);
				}
				// 更新排序
				int resultCount = new MenuDao().updateMenuOrder(conn, menu.getPId(), menu.getMorder(), menu.getAId());
				if(resultCount == -1)
				{
					empTransDao.rollBackTransaction(conn);
					result = false;
				}
				else
				{
					empTransDao.commitTransaction(conn);
				}
			}
			else
			{
				result = false;
				empTransDao.rollBackTransaction(conn);
			}
		}
		catch (Exception e)
		{
			empTransDao.rollBackTransaction(conn);
			EmpExecutionContext.error(e, "删除微信自定义菜单失败！");
			result = false;
		}
		finally
		{
			empTransDao.closeConnection(conn);
		}

		return result;
	}

	/**
	 * 得到当前公众帐号对应的菜单json格式
	 * 
	 * @param acct
	 * @return
	 */
	public JSONObject getMenuJsonByAccount(String aid)
	{
		LinkedHashMap<String, String> conditionMap = new LinkedHashMap<String, String>();
		LinkedHashMap<String, String> orderbyMap = new LinkedHashMap<String, String>();
		// 返回json
		JSONObject resultJsonObject = new JSONObject();
		try
		{
			// 查询公众帐号对应的菜单
			conditionMap.put("AId", aid);
			conditionMap.put("PId", "0");
			orderbyMap.put("PId", StaticValue.ASC);
			orderbyMap.put("morder", StaticValue.ASC);
			// 一级菜单
			List<LfWcMenu> pMenuList = empDao.findListByCondition(LfWcMenu.class, conditionMap, orderbyMap);

			// 父和子菜单序列
			JSONArray menuArray = new JSONArray();
			if(pMenuList != null && pMenuList.size() > 0)
			{
				for (LfWcMenu pitem : pMenuList)
				{
					conditionMap.put("AId", aid);
					conditionMap.put("PId", String.valueOf(pitem.getMId()));
					orderbyMap.put("PId", StaticValue.ASC);
					orderbyMap.put("morder", StaticValue.ASC);
					List<LfWcMenu> subMenuList = empDao.findListByCondition(LfWcMenu.class, conditionMap, orderbyMap);
					// subMenuHashMap.put(pitem.getMId(), subMenuList);

					JSONObject pAndSubJsonMenu = new JSONObject();
					pAndSubJsonMenu = getParentMentJson(pitem, subMenuList);
					menuArray.add(pAndSubJsonMenu);
				}
			}

			resultJsonObject.put("button", menuArray);
			EmpExecutionContext.debug("公众帐号的ID为：" + aid + "," + "返回的菜单JSON格式为：" + resultJsonObject.toString());
		}
		catch (Exception e)
		{
			EmpExecutionContext.error(e, "获取公众帐号对应菜单的json格式出错！");
		}
		return resultJsonObject;
	}

	/**
	 * 获取父菜单和子菜单的JSONObject对象
	 * 
	 * @param menu
	 * @param subMenuList
	 * @return
	 */
	private JSONObject getParentMentJson(LfWcMenu menu, List<LfWcMenu> subMenuList)
	{
		JSONObject jsonObj = new JSONObject();
		if(subMenuList != null && subMenuList.size() > 0)
		{
			jsonObj.put("name", menu.getMname());
			JSONArray resultArray = new JSONArray();
			for (LfWcMenu item : subMenuList)
			{
				JSONObject jsonObj2 = new JSONObject();
				jsonObj2 = getElementMenuJosn(item);
				resultArray.add(jsonObj2);
			}
			jsonObj.put("sub_button", resultArray);
		}
		else
		{
			jsonObj = getElementMenuJosn(menu);
		}

		return jsonObj;
	}

	/**
	 * 获取单个的菜单的JSONObject对象
	 * 
	 * @param menu
	 * @return
	 */
	private JSONObject getElementMenuJosn(LfWcMenu menu)
	{
		JSONObject jsonObj = new JSONObject();
		if(menu.getMtype() != null && "2".equals(String.valueOf(menu.getMtype())))
		{
			jsonObj.put("type", "view");
		}
		else
		{
			jsonObj.put("type", "click");
		}
		jsonObj.put("name", menu.getMname());
		if("2".equals(String.valueOf(menu.getMtype())))
		{
			if(menu.getMurl() != null)
			{
				jsonObj.put("url", menu.getMurl());
			}
			else
			{
				jsonObj.put("url", "");
			}
		}
		else
		{
			if(menu.getMkey() != null)
			{
				jsonObj.put("key", menu.getMkey());
			}
			else
			{
				jsonObj.put("key", "M_0_0");
			}
		}
		return jsonObj;
	}

}
