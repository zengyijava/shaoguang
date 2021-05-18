package com.montnets.emp.bustype.biz;

import java.util.LinkedHashMap;
import java.util.List;

import com.montnets.emp.bustype.biz.i.IBusDeatilBiz;
import com.montnets.emp.bustype.dao.BusDeatilDAO;
import com.montnets.emp.common.biz.SuperBiz;
import com.montnets.emp.entity.monitor.LfMonBusinfo;

import com.montnets.emp.util.PageInfo;

public class BusDeatilBiz extends SuperBiz implements IBusDeatilBiz{
	BusDeatilDAO dao =new BusDeatilDAO();
	/**
	 * 获得业务监控详情
	* @Description: TODO
	* @param @param conditionMap
	* @param @return
	* @param @throws Exception
	* @return List<LfMonBusinfo>
	 */
	public List<LfMonBusinfo> getDeatilInfo(LinkedHashMap<String, String> conditionMap,PageInfo pageInfo) throws Exception{
		return dao.getDeatilInfo(conditionMap, pageInfo);
	}
}
