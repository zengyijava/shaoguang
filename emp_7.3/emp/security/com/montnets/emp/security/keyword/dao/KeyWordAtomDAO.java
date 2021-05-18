/**
 * @description  
 * @author chentingsheng <cts314@163.com>
 * @datetime 2014-7-31 下午05:13:00
 */
package com.montnets.emp.security.keyword.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import com.montnets.emp.common.constant.StaticValue;
import com.montnets.emp.common.context.EmpExecutionContext;
import com.montnets.emp.common.dao.SuperDAO;

/**
 * @description 
 * @project emp_std_185
 * @company ShenZhen Montnets Technology CO.,LTD.
 * @author chentingsheng <cts314@163.com>
 * @datetime 2014-7-31 下午05:13:00
 */

public class KeyWordAtomDAO extends SuperDAO
{
	/**
	 * 设置所有黑名单
	 * @description    
	 * @param keyWordMap       			 
	 * @author chentingsheng <cts314@163.com>
	 * @datetime 2014-7-31 下午06:15:29
	 */
	public void setAllKeyWord(Map<String, Map<String, List<String>>> keyWordMap)
	{
		String sql = "SELECT KEYWOED, CORP_CODE AS CORPCODE FROM LF_KEYWORDS WHERE KW_STATE = 1 ORDER BY CORP_CODE, KEYWOED ASC";
		Map<String, List<String>> keyWordMapResult = null;
		Connection conn = null;
		PreparedStatement ps = null;
		java.sql.ResultSet rs = null;
		try {
			conn = connectionManager.getDBConnection(StaticValue.EMP_POOLNAME);
			ps = conn.prepareStatement(sql);
			rs = ps.executeQuery();
			String nextCorpCode = "";
			String key = "";
			String k = "";
			String preCorpCode = "";
			List<String> strKwsList = null;
			int keyWordCount = 0;
			//清空关键字
			keyWordMap.clear();
			while(rs.next()){
				nextCorpCode = rs.getString("CORPCODE");
				//下个企业
				if(!preCorpCode.equals(nextCorpCode))
				{
					preCorpCode = nextCorpCode;
					keyWordMapResult = new LinkedHashMap<String, List<String>>();
					keyWordMap.put(nextCorpCode, keyWordMapResult);
				}
				// 转换为大写的关键字
				key = rs.getString("KEYWOED").toUpperCase();
				// 关键字第一位
				k = key.substring(0, 1); 
				// 关键字第一位KEY已存在
				if(keyWordMapResult.containsKey(k))
				{
					keyWordMapResult.get(k).add(key);
				}
				else
				{
					strKwsList = new ArrayList<String>();
					strKwsList.add(key);
					keyWordMapResult.put(k, strKwsList);
				}
				keyWordCount++;
			}
			EmpExecutionContext.info("定时加载关键字成功!共加载:" + keyWordCount);
		} catch (Exception e) {
			EmpExecutionContext.error(e,"获取所有关键字信息异常！");
		} finally {
			try
			{
				close(rs, ps, conn);
			}
			catch (Exception e)
			{
				EmpExecutionContext.error(e, "获取所有关键字信息关闭连接异常！");
			}
		}
	}
}
