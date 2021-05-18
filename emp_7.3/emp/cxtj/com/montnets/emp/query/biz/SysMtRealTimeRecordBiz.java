package com.montnets.emp.query.biz;

import java.util.List;

import com.montnets.emp.common.biz.SuperBiz;
import com.montnets.emp.common.context.EmpExecutionContext;
import com.montnets.emp.query.dao.GenericSystemMtTaskVoDAO;
import com.montnets.emp.query.vo.SystemMtTaskVo;
import com.montnets.emp.util.PageInfo;

/**
 * 系统下行实时记录查询
 * @author Administrator
 *
 */
public class SysMtRealTimeRecordBiz extends SuperBiz 
{
	/**
	 * 系统下行实时记录查询
	 * @param curLongedUserId
	 * @param mtTaskVo
	 * @param pageInfo
	 * @return
	 * @throws Exception
	 */
	public List<SystemMtTaskVo> getDownMtTaskVos(SystemMtTaskVo mtTaskVo, PageInfo pageInfo) 
	{
		try
		{
			List<SystemMtTaskVo> mtTaskVosList = new GenericSystemMtTaskVoDAO().findSystemMtTaskVo(mtTaskVo, pageInfo);
			if(mtTaskVosList == null || mtTaskVosList.size() == 0)
			{
				pageInfo.setTotalRec(0);
				pageInfo.setTotalPage(1);
			}
			return mtTaskVosList;
		} 
		catch (Exception e)
		{
			EmpExecutionContext.error(e,"系统下行实时记录查询异常。");
			//异常处理
			return null;
		}
		
	}
	
	/**
	 * 获取网关数据转移配置(直接按实时转移来做，这方法暂时注释)
	 * @return 0-实时转移(实时转移)，1-整点转移(一天转移一次)
	 */
//	private int getGwTransConfig()
//	{
//		try
//		{
//			LinkedHashMap<String, String> conditionMap = new LinkedHashMap<String, String>();
//			conditionMap.put("paramItem", "INTB0TRANSMODE");
//			
//			List<AgwParamValue> gwParamValueList = empDao.findListByCondition(AgwParamValue.class, conditionMap, null);
//			//找不到则查两个表
//			if(gwParamValueList == null || gwParamValueList.size() == 0)
//			{
//				EmpExecutionContext.error("获取网关数据转移配置，获取配置对象为空。");
//				//0-实时转移(实时转移)，1-整点转移(一天转移一次)
//				return 1;
//			}
//			if(gwParamValueList.get(0).getParamValue() == null || gwParamValueList.get(0).getParamValue().trim().length() == 0)
//			{
//				return 1;
//			}
//			return Integer.parseInt(gwParamValueList.get(0).getParamValue());
//		}
//		catch (Exception e)
//		{
//			EmpExecutionContext.error(e, "获取网关数据转移配置，异常。");
//			return 1;
//		}
//	}
	
	/**
	 * 查询下行实时记录分页信息
	 * @param mtTaskVo
	 * @param pageInfo
	 * @return
	 */
	public boolean getDownMtTaskPageInfo(SystemMtTaskVo mtTaskVo, PageInfo pageInfo) 
	{
		return new GenericSystemMtTaskVoDAO().findSystemMtTaskPageInfo(mtTaskVo, pageInfo);
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
	public PageInfo getPageInfo(int preIndex, int pageSize,	SystemMtTaskVo mtTaskVo) throws Exception
	{
		//定义分页对象
		PageInfo pageInfo;
		try
		{
		    //调用分页方法
			pageInfo = new GenericSystemMtTaskVoDAO().getPageInfo(preIndex, pageSize, mtTaskVo);
			
		} catch (Exception e)
		{
			EmpExecutionContext.error(e,"获取分信息异常");
			throw e;
		}
		return pageInfo;
	}

}
