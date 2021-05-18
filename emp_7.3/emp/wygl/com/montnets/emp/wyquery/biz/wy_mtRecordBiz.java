package com.montnets.emp.wyquery.biz;

import com.montnets.emp.common.biz.SuperBiz;
import com.montnets.emp.util.PageInfo;
import com.montnets.emp.wyquery.dao.wy_mtRecordDAO;
import org.apache.commons.beanutils.DynaBean;

import java.util.LinkedHashMap;
import java.util.List;

public class wy_mtRecordBiz extends SuperBiz {
	/**
	 * 获取网优下行记录
	 * @description    
	 * @param pageInfo
	 * @param conditionMap
	 * @return       			 
	 * @author zhangmin 
	 * @datetime 2013-12-03 下午04:58:00
	 */
	public List<DynaBean> getMTRecord(PageInfo pageInfo, LinkedHashMap< String, String> conditionMap) {
		return new wy_mtRecordDAO().getMTRecord(pageInfo, conditionMap);
	}
}
