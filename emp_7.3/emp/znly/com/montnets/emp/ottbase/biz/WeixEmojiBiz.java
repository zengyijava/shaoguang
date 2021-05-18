package com.montnets.emp.ottbase.biz;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import com.montnets.emp.common.biz.SuperBiz;
import com.montnets.emp.common.context.EmpExecutionContext;
import com.montnets.emp.entity.wxgl.LfWeixEmoji;

/**
 * 
 * @project emp_std_5.0new
 * @author huangzb
 * @company ShenZhen Montnets Technology CO.,LTD.
 * @datetime 2014-9-18 下午03:08:09
 * @description 微信emoji表情处理
 */
public class WeixEmojiBiz extends SuperBiz
{

	
	/**
	 * 初始化emoji表情
	 * @return 成功返回true
	 */
	private boolean initEmojiMap(){
		try
		{
			//已经初始化，则直接返回成功
			if(emojiSbunicodeMap != null && emojiSbunicodeMap.size() > 0){
				return true;
			}
			//未初始化，则初始化
			if(emojiSbunicodeMap == null){
				emojiSbunicodeMap = new HashMap<String,LfWeixEmoji>();
			}
			
			//从数据库取值
			List<LfWeixEmoji> emojiList = getEmojiList();
			
			if(emojiList == null || emojiList.size() == 0){
				EmpExecutionContext.error("初始化emoji表情，从数据库获取emoji表情为空。");
				return false;
			}
			//循环放到map中
			for(LfWeixEmoji emoji : emojiList){
				//没sbunicode的则跳过
				if(emoji == null || emoji.getSbunicode() == null || emoji.getSbunicode().length() == 0){
					continue;
				}
				emojiSbunicodeMap.put(emoji.getSbunicode().toUpperCase(), emoji);
			}
			
			if(emojiSbunicodeMap.size() > 0){
				return true;
			}else{
				return false;
			}
			
		}
		catch (Exception e)
		{
			EmpExecutionContext.error(e, "初始化emoji表情异常。");
			return false;
		}
	}
	
	private List<LfWeixEmoji> getEmojiList(){
		try
		{
			 LinkedHashMap<String, String> conditionMap = new  LinkedHashMap<String, String>();
			 List<LfWeixEmoji> emojiList = empDao.findListByCondition(LfWeixEmoji.class, conditionMap, null);
			 if(emojiList == null || emojiList.size() == 0){
				 return null;
			 }
			 return emojiList;
		}
		catch (Exception e)
		{
			EmpExecutionContext.error(e, "数据库获取emoji表情异常。");
			return null;
		}
	}

	/**
	 * 过滤emoji字符
	 * 
	 * @param source
	 * @return 返回过滤后的字符串
	 */
	public String filterEmoji(String source)
	{
		try
		{
			//没东西，原样返回
			if(source == null || source.length() == 0){
				return source;
			}
			
			//初始化emoji表情
			boolean initRes = initEmojiMap();
			//初始化不成功，直接返回，不处理
			if(!initRes){
				EmpExecutionContext.error("过滤emoji表情，初始化emoji表情失败，不做过滤处理。");
				return source;
			}
			
			StringBuilder buf = new StringBuilder();;

			int len = source.length();

			for (int i = 0; i < len; i++)
			{
				//取每个字符
				char codePoint = source.charAt(i);

				//转换字符为unicode
				String sbUniCodeTemp = Integer.toHexString((int)source.charAt(i));
				sbUniCodeTemp = sbUniCodeTemp.toUpperCase();
				//不存在emoji字符
				if(emojiSbunicodeMap.get(sbUniCodeTemp) == null){
					//拼进去
					buf.append(codePoint);
				}
				else{
					//存在emoji字符，则不拼进去，达到过滤目的
				}
			}

			//无东西，原样返回
			if(buf.length() == 0){
				EmpExecutionContext.error("过滤emoji表情，过滤后字符串为空。");
				return source;
			}
			
			return buf.toString();
		}
		catch (Exception e)
		{
			EmpExecutionContext.error(e, "过滤emoji字符异常。");
			//过滤异常，原样返回
			return source;
		}
	}
	
	/**
	 * emoji表情sbunicode集合，key为sbunicode，value为sbunicode
	 */
	private static Map<String,LfWeixEmoji> emojiSbunicodeMap = new HashMap<String,LfWeixEmoji>();

}
