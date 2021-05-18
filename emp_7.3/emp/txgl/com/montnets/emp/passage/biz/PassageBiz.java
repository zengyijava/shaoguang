package com.montnets.emp.passage.biz;

import java.util.LinkedHashMap;
import java.util.List;

import com.montnets.emp.common.biz.SuperBiz;
import com.montnets.emp.common.context.EmpExecutionContext;
import com.montnets.emp.passage.dao.PassageDAO;
import com.montnets.emp.security.context.ErrorLoger;
import com.montnets.emp.servmodule.txgl.entity.XtGateQueue;
import com.montnets.emp.util.PageInfo;

/**
 * 通道管理biz
 * @description 
 * @project p_txgl
 * @company ShenZhen Montnets Technology CO.,LTD.
 * @author zhangmin
 * @datetime 2013-10-18 下午03:54:20
 */
public class PassageBiz extends SuperBiz{
	ErrorLoger errorLoger = new ErrorLoger();
	/**
	 * 通道管理查询通道信息
	 * @description    
	 * @param corpCode 企业编码
	 * @param conditionMap 查询条件
	 * @param pageInfo 分页信息
	 * @return       			 
	 * @author zhangmin
	 * @datetime 2013-10-18 下午03:54:00
	 */
	public List<XtGateQueue> getGateInfoByCorp(String corpCode , LinkedHashMap<String, String> conditionMap , PageInfo pageInfo)
	{
		//通道信息list
		List<XtGateQueue> xtqList= null;
		try
		{
			xtqList = new PassageDAO().findGateInfoByCorp(corpCode,conditionMap, pageInfo);
		}
		catch (Exception e) {
			EmpExecutionContext.error(errorLoger.getErrorLog(e,"通道管理查询通道信息biz层异常！"));
			xtqList = null;
		}
		return xtqList;
	}
	/**
	 * 通道管理查询通道信息
	 * @description    
	 * @param conditionMap
	 * @param pageInfo
	 * @return       			 
	 * @author zousy <zousy999@qq.com>
	 * @datetime 2015-4-22 下午03:40:47
	 */
	public List<XtGateQueue> getByCondition(LinkedHashMap<String, String> conditionMap , PageInfo pageInfo){
		List<XtGateQueue> xtqList= null;
		try
		{
			xtqList = new PassageDAO().getByCondition(conditionMap, pageInfo);
		}
		catch (Exception e) {
			EmpExecutionContext.error(errorLoger.getErrorLog(e,"通道管理查询通道信息biz层异常！"));
			xtqList = null;
		}
		return xtqList;
	}
}
