package com.montnets.emp.bustype.dao.i;

import java.util.LinkedHashMap;
import java.util.List;

import org.apache.commons.beanutils.DynaBean;

import com.montnets.emp.entity.monitor.LfMonBusbase;
import com.montnets.emp.util.PageInfo;

public interface IBusTypeCfgDAO {

	/**
	 * 获得业务监控基础信息表
	* @Description: TODO
	* @param @param conditionMap
	* @param @return
	* @param @throws Exception
	* @return List<LfMonBusbase>
	 */
	public List<LfMonBusbase> getLfMonBusbase(LinkedHashMap<String, String> conditionMap,PageInfo pageInfo) throws Exception;

	/**
	 * 获得省份信息
	 * @param type
	 * @return
	 * @throws Exception
	 */
	public List<DynaBean> getProvinceList(String code);
}
