package com.montnets.emp.common.biz;

import java.util.LinkedHashMap;
import java.util.List;

import com.montnets.emp.common.constant.StaticValue;
import com.montnets.emp.common.context.EmpExecutionContext;
import com.montnets.emp.entity.notice.LfNotice;

public class SysNoticeBiz extends SuperBiz
{
	/**
	 * 获取最新公告记录
	 * @param corpcode 企业编码
	 * @return 返回最新的公告记录对象，其他返回null
	 */
	public LfNotice getNotice(String corpcode){
		try
		{
			LinkedHashMap<String, String> conditionMap = new LinkedHashMap<String, String>();
			conditionMap.put("corpcode", corpcode);
			LinkedHashMap<String, String> orderbyMap = new LinkedHashMap<String, String>();
			orderbyMap.put("publishTime", StaticValue.DESC);
			List<LfNotice> noticesList = empDao.findListByCondition(LfNotice.class, conditionMap, orderbyMap);
			if(noticesList == null || noticesList.size() == 0){
				return null;
			}
			return noticesList.get(0);
		}
		catch (Exception e)
		{
			EmpExecutionContext.error(e, "获取公告id异常。");
			return null;
		}
		
	}
}
