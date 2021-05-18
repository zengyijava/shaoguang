package com.montnets.emp.security.keyword;

import com.montnets.emp.common.biz.SuperBiz;
import com.montnets.emp.common.constant.EMPException;
import com.montnets.emp.common.constant.StaticValue;
import com.montnets.emp.common.context.EmpExecutionContext;
import com.montnets.emp.entity.keywords.LfKeywords;
import com.montnets.emp.security.context.ErrorLoger;
import com.montnets.emp.security.keyword.dao.KeyWordAtomDAO;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class KeyWordAtom extends SuperBiz
{
	// 关键字集合，Map：key-企业编码，value-对应企业的关键字集合
	private static Map<String, Map<String, List<String>>>	keyWordMap		= new HashMap<String, Map<String, List<String>>>();

	// 托管版时系统级关键字的corpCode
	private static final String								PUBLIC_KEY_CODE	= "100000";

	ErrorLoger errorLoger = new ErrorLoger();
	/**
	 * 检测内容
	 * 
	 * @param text
	 * @return
	 * @throws Exception
	 */
	public String checkText(String text) throws Exception
	{
		//短信内容为空处理
		if(text == null)
		{
			EmpExecutionContext.error("过滤关键字，短信内容为null");
			return "error";
		}
		List<String> kwsList = getKwInUsed();

		if(kwsList == null || kwsList.size() == 0)
		{
			return "";
		}

		String words = "";
		for (String keyword : kwsList)
		{
			if(checkWordString(text.toUpperCase(), keyword.toUpperCase()))
			{
				if(!words.equals(""))
				{
					words += ",";
				}
				words += keyword;
			}
		}
		return words;
	}

	/**
	 * 过滤关键字的方法，带企业编码
	 * 
	 * @param text
	 *        短信内容
	 * @param corpCode
	 *        企业编码
	 * @return
	 * @throws Exception
	 */
	public String checkText(String text, String corpCode) throws Exception
	{
		//短信内容为空处理
		if(text == null || text.trim().length() == 0)
		{
			EmpExecutionContext.error("过滤关键字异常，短信内容为空。text:" + text);
			return "error";
		}
		List<String> kwsList = getKwInUsed(corpCode);

		if(kwsList == null || kwsList.size() == 0)
		{
			return "";
		}
		// 遍历关键字
		String words = "";
		// 托管版会出现关键字重复的情况，所以添加于上一个关键的判断
		String prekey = "";
		for (String keyword : kwsList)
		{
			keyword = keyword.toUpperCase();
			// 如果与上一个关键字一样，则直接跳过过滤
			if(prekey.equals(keyword))
			{
				continue;
			}
			else
			{
				prekey = keyword;
			}
			if(checkWordString(text.toUpperCase(), keyword))
			{
				if(!words.equals(""))
				{
					words += ",";
				}
				words += keyword;
			}
		}
		return words;
	}

	/**
	 * 检测内容
	 * 
	 * @param text
	 * @param bad
	 * @return
	 */
	public boolean checkWordString(String text, String bad)
	{
		if(text.length() == 0)
		{
			return false;
		}

		if(text.length() != text.replace(bad, "").length())
		{
			return true;
		}

		return false;
	}

	/**
	 * 获取用户关键字
	 * 
	 * @return
	 * @throws Exception
	 */
	public List<String> getKwInUsed() throws Exception
	{

		List<String> strKwsList;
		LinkedHashMap<String, String> conditionMap = new LinkedHashMap<String, String>();
		try
		{
			conditionMap.put("kwState", "1");
			List<LfKeywords> kwsList = empDao.findListByCondition(LfKeywords.class, conditionMap, null);
			if(kwsList == null || kwsList.size() == 0)
			{
				return null;
			}
			strKwsList = new ArrayList<String>();
			for (int i = 0; i < kwsList.size(); i++)
			{
				strKwsList.add(kwsList.get(i).getKeyWord().toUpperCase());
			}
		}
		catch (Exception e)
		{
			EmpExecutionContext.error(errorLoger.getErrorLog(e, "获取用户关键字异常。"));
			// 异常处理
			throw e;
		}
		Collections.sort(strKwsList);
		return strKwsList;
	}

	/**
	 * 过滤关键字，带企业编码
	 * 
	 * @param corpcode
	 *        企业编码
	 * @return
	 * @throws Exception
	 */
	public List<String> getKwInUsed(String corpCode) throws Exception
	{
		List<String> strKwsList;
		LinkedHashMap<String, String> conditionMap = new LinkedHashMap<String, String>();
		LinkedHashMap<String, String> orderByMap = new LinkedHashMap<String, String>();
		try
		{
			// 查找当前企业及全局的管理
			conditionMap.put("corpCode&in", PUBLIC_KEY_CODE + "," + corpCode);
			// 启用状态
			conditionMap.put("kwState", "1");
			// 按关键字名称排序
			orderByMap.put("corpCode", StaticValue.ASC);
			orderByMap.put("keyWord", StaticValue.ASC);
			List<LfKeywords> kwsList = empDao.findListBySymbolsCondition(LfKeywords.class, conditionMap, null);
			if(kwsList == null || kwsList.size() == 0)
			{
				return null;
			}
			strKwsList = new ArrayList<String>();
			for (int i = 0; i < kwsList.size(); i++)
			{
				strKwsList.add(kwsList.get(i).getKeyWord().toUpperCase());
			}
		}
		catch (Exception e)
		{
			EmpExecutionContext.error(errorLoger.getErrorLog(e, "过滤关键字异常，企业编码：" + corpCode));
			throw e;
		}
		Collections.sort(strKwsList);
		return strKwsList;
	}

	/**
	 * 过滤短信内容关键字
	 * 
	 * @param text
	 *        短信内容
	 * @param kwMap
	 *        关键字集合
	 * @return false-包含关键字，true-不包含关键字
	 * @throws EMPException
	 */
	public boolean checkTextString(String text, Map<String, List<String>> kwMap) throws Exception
	{
		boolean result = true;
		try
		{
			Iterator<String> it = kwMap.keySet().iterator();
			String key;
			// 是否结束过滤开关
			while(result && it.hasNext())
			{
				key = it.next();
				// 如果首字母包含在
				if(text.contains(key))
				{
					List<String> kwStrList = kwMap.get(key);
					for (String wo : kwStrList)
					{
						if(text.contains(wo))
						{
							result = false;
							break;
						}
					}
				}
			}
		}
		catch (Exception e)
		{
			EmpExecutionContext.error(errorLoger.getErrorLog(e, "过滤短信内容关键字异常。"));
			throw new Exception("过滤关键字异常。", e);
		}
		return result;
	}

	/**
	 * 过滤短信内容关键字
	 * 
	 * @param text
	 *        短信内容
	 * @param kwMap
	 *        关键字集合
	 * @return
	 *         0:不包含关键字 。
	 *         1：包含关键字
	 *         -1：过滤异常
	 * @throws EMPException
	 */
	public int filterKeyWord(String text, String corpCode)
	{
		int result = 0;
		try
		{
			//关键字缓存中不存在数据
			if(keyWordMap == null || keyWordMap.size() == 0)
			{
				//返回不包含关键字
				return 0;
			}
			// 取出企业级关键字的集合
			Map<String, List<String>> filterKeyWordMap = keyWordMap.get(corpCode);
			//存在企业级关键字
			if(filterKeyWordMap != null)
			{
				// 先过滤自己所属企业的关键字
				result = checkKeyWord(filterKeyWordMap, text);
			}
			if(result == 1)
			{
				//返回包含关键字
				return 1;
			}
			// 取出系统级关键字的集合
			filterKeyWordMap = keyWordMap.get(PUBLIC_KEY_CODE);
			// 过滤公共企业关键字
			if(result == 0 && filterKeyWordMap != null)
			{
				return checkKeyWord(filterKeyWordMap, text);
			}
			return 0;
		}
		catch (Exception e)
		{
			EmpExecutionContext.error(errorLoger.getErrorLog(e, "过滤关键字失败。"));
			return -1;
		}
	}

	/**
	 * 过滤关键字子方法
	 * 
	 * @param filterKeyWordMap
	 *        关键字集合
	 * @param text
	 *        内容
	 * @return 0-不包含关键字，1-包含关键字
	 */
	private int checkKeyWord(Map<String, List<String>> filterKeyWordMap, String text) throws Exception
	{
		Iterator<String> it = null;
		String key;
		if(filterKeyWordMap != null)
		{
			it = filterKeyWordMap.keySet().iterator();

			// 过滤自己企业的关键字
			// 是否结束过滤开关
			while(it.hasNext())
			{
				key = it.next();
				// 如果首字母包含在
				if(text.contains(key))
				{
					List<String> kwStrList = filterKeyWordMap.get(key);
					int kwLen = kwStrList.size();
					String wo = "";
					for(int i=0; i<kwLen; i++)
					{
						wo = kwStrList.get(i);
						if(text.contains(wo))
						{
							return 1;
						}
					}
				}
			}
		}
		return 0;
	}

	/**
	 * 获取关键字集合,带企业编码
	 * 
	 * @param corpCode
	 *        企业编码,如果企业传入null则加载全部企业
	 * @return Map<关键字首字母，首字母相同的关键字集合>
	 * @throws Exception
	 */
	public void setKeyWordMap(String corpCode)
	{
		try
		{
			if(keyWordMap == null)
			{
				keyWordMap = new HashMap<String, Map<String, List<String>>>();
			}
			//加载关键字
			new KeyWordAtomDAO().setAllKeyWord(keyWordMap);
		}
		catch (Exception e)
		{
			EmpExecutionContext.error(errorLoger.getErrorLog(e, "加载关键字异常！"));
		}
	}

	public static Map<String, Map<String, List<String>>> getKeyWordMap(){
		return keyWordMap;
	}

	/**
	 * 清空关键字内存
	 */
	public void cleanKeyWordMap()
	{
		/*Iterator<String> iter = syncKwTimeMap.keySet().iterator();
		while(iter.hasNext())
		{
			String key = iter.next();
			if(System.currentTimeMillis() - syncKwTimeMap.get(key) > SYNC_TIME)
			{
				keyWordMap.remove(key);
				syncKwTimeMap.remove(key);
			}
		}*/

	}

}
