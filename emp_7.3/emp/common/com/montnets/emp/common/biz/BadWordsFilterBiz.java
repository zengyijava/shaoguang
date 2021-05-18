package com.montnets.emp.common.biz;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.montnets.emp.common.context.EmpExecutionContext;
import com.montnets.emp.entity.keywords.LfKeywords;

/**
 * 
 * @author Administrator
 * 
 */
public class BadWordsFilterBiz extends SuperBiz{

	public String textFormat(String str) {
		String stext = null;
		stext = str.replaceAll("[^\\u4e00-\\u9fa5|\\w]", "");
		return stext;
	}

	public List<String> cutText(String text) {
		List<String> list = new ArrayList<String>();
		String temp = this.textFormat(text);
		int i = 0, j = 0;
		for (i = 0; i < temp.length() - 7; i++) {
			j = i + 8;
			list.add(temp.substring(i, j));
		}
		for (i = 0; i < temp.length() - 6; i++) {
			j = i + 7;
			list.add(temp.substring(i, j));
		}
		for (i = 0; i < temp.length() - 5; i++) {
			j = i + 6;
			list.add(temp.substring(i, j));
		}
		for (i = 0; i < temp.length() - 4; i++) {
			j = i + 5;
			list.add(temp.substring(i, j));
		}
		for (i = 0; i < temp.length() - 3; i++) {
			j = i + 4;
			list.add(temp.substring(i, j));
		}
		for (i = 0; i < temp.length() - 2; i++) {
			j = i + 3;
			list.add(temp.substring(i, j));
		}
		for (i = 0; i < temp.length() - 1; i++) {
			j = i + 2;
			list.add(temp.substring(i, j));
		}
		for (i = 0; i < temp.length(); i++) {
			j = i + 1;
			list.add(temp.substring(i, j));
		}
		return list;
	}

	public boolean checkWords(String text, String bad) {
		if (text.length() >= bad.length() && bad.length() != 0) {
			for (int i = 0; i < text.length() - bad.length() + 1; i++) {
				int len = i + bad.length();
				if (text.substring(i, len).equals(bad)) {
					return true;
				}
			}
		}
		return false;
	}

	/**
	 * 
	 * @param text
	 * @param bad
	 * @return
	 */
	public boolean checkWordString(String text, String bad) {
		if (text.length() == 0) {
			return false;
		}

		if (text.length() != text.replace(bad, "").length()) {
			return true;
		}

		return false;
	}

	/**
	 * 
	 * @param text
	 * @param badWordList
	 * @return
	 */
	public boolean checkTextString(String text, List<String> badWordList) {
		int len = text.length();
		int len3;
		String word = "";
		int index = 0, index2 = 0;
		String text2 = "";
		String w, w1 = "";
		for (int i = 0; i < badWordList.size(); i++) {
			word = badWordList.get(i);
			word=word.toUpperCase();
			w = word.substring(0, 1);
			if (w.equals(w1)) {
				if (index2 == -1)
					continue;
			} else {
				w1 = w;
				index2 = text.indexOf(w);
				if (index2 < 0) {
					continue;
				}
			}
			len3 = word.length();
			for (int j = index2; j < len;) {
				text2 = text.substring(j);
				index = text2.indexOf(w);
				if (index == -1) {
					break;
				}
				if (index + len3 > text2.length()) {
					break;
				}
				if (text2.substring(index, index + len3).equals(word)) {
					return false;
				}
				j += index + 1;
			}

		}
		return true;
	}

	/**
	 * @description 过滤关键字
	 * @param text
	 * @param badWordList
	 * @return
	 */
	public boolean checkTestStrings(String text, List<String> badWordList) {
		// 遍历关键字列表集合

		String word = "";
		boolean flag = false;

		for (int i = 0; i < badWordList.size(); i++) {
			// 取出每一个关键字
			word = badWordList.get(i).trim();

			if (text.trim().indexOf(word) != -1) {

				flag = true;
				break;
			}

		}
		if (flag) {

			return false;
		}

		return true;

	}

	/**
	 * @param text
	 * @param badWordList
	 * @return
	 */
	public boolean checktest(String text, List<String> badWordList) {
		String word = "";
		boolean flag = false;

		for (int i = 0; i < badWordList.size(); i++) {
			// 取出每一个关键字
			word = badWordList.get(i).trim();
			// KMP m = new KMP(text, word);

			if (text.contains(word)) {
				flag = true;
				break;
			}


		}
		if (flag) {

			return false;
		}
		return true;

	}

	/**
	 * @param str1
	 * @param str2
	 * @return
	 */
	public boolean check(String str1, String str2) {
		Pattern p = Pattern.compile(str1);
		Matcher m = p.matcher(str2);
		boolean flag = false;
		while (m.find()) {
			flag = true;
		}
		return flag;
	}

	/**
	 * 
	 * @param text
	 * @return
	 * @throws Exception
	 */
	public String checkText(String text) throws Exception {
		List<String> kwsList = getKwInUsed();

		if (kwsList == null || kwsList.size() == 0) {
			return "";
		}

		String words = "";
		for (String keyword : kwsList) {
			if (checkWordString(text.toUpperCase(), keyword.toUpperCase())) {
				if (!words.equals("")) {
					words += ",";
				}
				words += keyword;
			}
		}
		return words;
	}
	
	public List<String> getKwInUsed() throws Exception {

		List<String> strKwsList;
		LinkedHashMap<String, String> conditionMap = new LinkedHashMap<String, String>();
		try{
			conditionMap.put("kwState", "1");
			List<LfKeywords> kwsList = empDao.findListByCondition(LfKeywords.class, conditionMap, null);
			if(kwsList == null || kwsList.size() == 0){
				return null;
			}
			strKwsList = new ArrayList<String>();
			for(int i=0;i<kwsList.size();i++){
				strKwsList.add(kwsList.get(i).getKeyWord().toUpperCase());
			}
		}catch(Exception e){
			EmpExecutionContext.error(e, "获取使用中的关键字异常。");
			//异常处理
			throw e;
		}
		Collections.sort(strKwsList);
		return strKwsList;
	}

}
