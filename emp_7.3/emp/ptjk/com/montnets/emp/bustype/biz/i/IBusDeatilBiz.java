package com.montnets.emp.bustype.biz.i;

import java.util.LinkedHashMap;
import java.util.List;

import com.montnets.emp.entity.monitor.LfMonBusinfo;
import com.montnets.emp.util.PageInfo;

public interface IBusDeatilBiz {
	/**
	 * 获得业务监控详情
	* @Description: TODO
	* @param @param conditionMap
	* @param @return
	* @param @throws Exception
	* @return 
	 */
	public List<LfMonBusinfo> getDeatilInfo(LinkedHashMap<String, String> conditionMap,PageInfo pageInfo)throws Exception;
}
