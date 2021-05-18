package com.montnets.emp.bustype.biz.i;

import java.util.LinkedHashMap;
import java.util.List;

import org.apache.commons.beanutils.DynaBean;

import com.montnets.emp.entity.biztype.LfBusManager;
import com.montnets.emp.entity.monitor.LfMonBusbase;
import com.montnets.emp.entity.monitor.LfMonBusdata;
import com.montnets.emp.util.PageInfo;

public interface IBusTypeCfgBiz {
	/**
	 * 获得业务列表
	* @Description: TODO
	* @param @param conditionMap
	* @param @return
	* @param @throws Exception
	* @return List<LfBusManager>
	 */
	public List<LfBusManager> getLfBusManager(LinkedHashMap<String, String> conditionMap) throws Exception;
	
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
	 * 获得业务监控数据信息表
	* @Description: TODO
	* @param @param conditionMap
	* @param @return
	* @param @throws Exception
	* @return List<LfMonBusbase>
	 */
	public List<LfMonBusdata> getLfMonBusdata(LinkedHashMap<String, String> conditionMap) throws Exception;
	/**
	 * 保存业务监控基础信息与业务监控数据信息
	* @Description: TODO
	* @param @param base 业务监控基础信息
	* @param @param list 业务监控数据信息
	* @param @return 
	* @return boolean 返回是否成功
	 */
	public boolean save(LfMonBusbase base,List<LfMonBusdata> list);
	
	/***
	 * 单个删除
	* @Description: TODO
	* @param @param id 传入业务id
	* @param @param lgcorpcode 公司编码
	* @param @return
	* @return boolean
	 */
	public boolean delete(String id,String lgcorpcode);
	/***
	 * 修改监控基础信息状态
	* @Description: TODO
	* @param @param id 传入业务id
	* @param @param lgcorpcode 公司编码
	* @return boolean
	 */
	public boolean changeStatus(String id,String status,String lgcorpcode);
	/**
	 * 修改业务监控基础信息与业务监控数据信息
	* @Description: TODO
	* @param @param base 业务监控基础信息
	* @param @param list 业务监控数据信息
	* @param @return 
	* @return boolean 返回是否成功
	 */
	public boolean update(LfMonBusbase base,List<LfMonBusdata> list);
	/**
	 * 获得省份信息
	 * @param type
	 * @return
	 * @throws Exception
	 */
	public List<DynaBean> getProvinceList(String code);
}
