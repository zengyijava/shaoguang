package com.montnets.emp.report.biz;

import java.util.LinkedHashMap;
import java.util.List;

import org.apache.commons.beanutils.DynaBean;

import com.montnets.emp.report.dao.GenericRemainMtRecordDAO;

/**
 * 机构统计报表BIZ
 * @project p_cxtj
 * @author liaojirong <ljr0300@163.com>
 * @company ShenZhen Montnets Technology CO.,LTD.
 * @datetime 2013-11-22 下午02:45:13
 * @description
 */
public class RemainMtRecordBiz
{

	// 机构报表DAO
	protected GenericRemainMtRecordDAO	remainDao;

	// 构造函数
	public RemainMtRecordBiz()
	{
		remainDao = new GenericRemainMtRecordDAO();
	}

	/**
	 * 
	 */
	/*public <T> int update(Class<T> entityClass,LinkedHashMap<String, String> objectMap,LinkedHashMap<String, String> conditionMap) throws Exception
	{
		return remainDao.update(entityClass, objectMap, conditionMap);
	}*/

	/**
	 * 获取滞留记录发送账号对应的数量
	 * @return 返回账号数量集合
	 * @throws Exception
	 */
	public List<DynaBean> getMtTaskCount(LinkedHashMap<String, String> conditionMap) {
		return remainDao.getMtTaskCount(conditionMap);
	}
	
}
