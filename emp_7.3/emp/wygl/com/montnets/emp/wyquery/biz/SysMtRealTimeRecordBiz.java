package com.montnets.emp.wyquery.biz;

import com.montnets.emp.common.context.EmpExecutionContext;
import com.montnets.emp.util.PageInfo;
import com.montnets.emp.wyquery.dao.GenericSystemMtTaskVoDAO;
import com.montnets.emp.wyquery.vo.SystemMtTaskVo;
import org.apache.commons.beanutils.DynaBean;

import java.util.List;

/**
 * 系统下行实时记录查询
 * @author Administrator
 *
 */
public class SysMtRealTimeRecordBiz {
	/**
	 * 系统下行实时记录查询
	 * @param curLongedUserId
	 * @param mtTaskVo
	 * @param pageInfo
	 * @return
	 * @throws Exception
	 */
	public List<DynaBean> getDownMtTaskVos(Long curLongedUserId,
			SystemMtTaskVo mtTaskVo, PageInfo pageInfo) throws Exception
	{
		List<DynaBean> mtTaskVosList;
		try
		{
				mtTaskVosList = new GenericSystemMtTaskVoDAO().findSystemMtTaskVo(mtTaskVo, pageInfo);

		} catch (Exception e)
		{
			//异常处理
			EmpExecutionContext.error(e, "系统下行实时记录查询异常!");
			throw e;
		}
		return mtTaskVosList;
	}


	/**
	 * 获取分信息
	 * @param preIndex
	 * @param pageSize
	 * @param curLongedUserId
	 * @param mtTaskVo
	 * @return
	 * @throws Exception
	 */
	public PageInfo getPageInfo(int preIndex, int pageSize, Long curLongedUserId,
			SystemMtTaskVo mtTaskVo) throws Exception
	{
		//定义分页对象
		PageInfo pageInfo;
		try
		{
		    //调用分页方法
			pageInfo = new GenericSystemMtTaskVoDAO().getPageInfo(preIndex, pageSize, mtTaskVo);
			
		} catch (Exception e)
		{
			EmpExecutionContext.error(e, "获取分信息异常!");
			throw e;
		}
		return pageInfo;
	}

}
